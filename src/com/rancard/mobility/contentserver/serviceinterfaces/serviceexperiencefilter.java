package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.DConnect;
import com.rancard.common.Feedback;
import com.rancard.common.uidGen;
import com.rancard.mobility.common.ThreadedMessageSender;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.contentserver.CPSite;
import com.rancard.mobility.infoserver.InfoService;
import com.rancard.mobility.infoserver.common.services.ServiceManager;
import com.rancard.mobility.infoserver.common.services.UserService;
import com.rancard.mobility.infoserver.common.services.UserServiceExperience;
import com.rancard.mobility.infoserver.common.services.UserServiceTransaction;
import com.rancard.mobility.infoserver.viralmarketing.PromoImpression;
import com.rancard.mobility.infoserver.viralmarketing.VMServiceManager;
import com.rancard.mobility.infoserver.viralmarketing.VMTransaction;
import com.rancard.mobility.infoserver.viralmarketing.VMUser;
import com.rancard.util.payment.PaymentManager;
import com.rancard.util.payment.PricePoint;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class serviceexperiencefilter extends HttpServlet
        implements Filter {

    String push_sender = "";
    String inv_instruction = "";

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String accountId = (String) req.getAttribute("acctId");
        String siteId = request.getParameter("siteId");
        String keyword = req.getParameter("keyword");
        String msisdn = req.getParameter("msisdn");
        String dest = req.getParameter("dest");
        String shortCode = dest.contains("+") ? dest.substring(1) : dest;
        String smsc = req.getParameter("smsc") == null ? "" : req.getParameter("smsc");

        String pushMsg = "";
        String pushMsgSender = "";
        int pushMsgWaitTime;
        String promoId = "";
        boolean skipMessagePush = false;

        logState(accountId, siteId, keyword, msisdn, "Entering serviceexperiencefilter...");
        try {
            UserService srvc = ServiceManager.viewService(keyword, accountId);

            logState(accountId, siteId, keyword, msisdn, "Start service experience processing...");

            String service_keyword = srvc.getKeyword();
            UserServiceExperience srvcExpr = ServiceManager.viewServiceExperience(accountId, siteId, service_keyword);

            if (srvcExpr != null) {
                promoId = srvcExpr.getPromoId();
                String serviceRespCode = srvcExpr.getPromoRespCode();

                Map metaDataMap = srvcExpr.getMetaDataMap();
                String altKeyword = (String) metaDataMap.get("override_keyword".toUpperCase());
                if ((altKeyword != null) && (!altKeyword.equals(""))) {
                    request.setAttribute("override_keyword", altKeyword);
                }

                if (srvc.isSubscription()) {                    

                    boolean alreadySubscribed = false;
                    HashMap thisSubscription = ServiceManager.getSubscription(msisdn, accountId, keyword, altKeyword);
                    if ((thisSubscription != null) && (!thisSubscription.isEmpty())) {
                        alreadySubscribed = true;
                    }

                    if (alreadySubscribed) {
                        Timestamp curTimestamp = new Timestamp(new java.util.Date().getTime());

                        java.util.Date nextSubscriptionDate = thisSubscription.get("next_subscription_date") == null ? null : (Timestamp) thisSubscription.get("next_subscription_date");
                        ArrayList keywordList = new ArrayList();
                        keywordList.add(service_keyword);

                        if ((nextSubscriptionDate == null) || (nextSubscriptionDate.after(curTimestamp))) {
                            logState(accountId, siteId, keyword, msisdn, "Subscription already exists in DB");
                            if (pushMsg.isEmpty()) {
                                pushMsg = srvcExpr.getAlreadySubscribedMsg();
                            }
                            pushMsgSender = srvcExpr.getAlreadySubscribedMsgSender().equals("") ? shortCode : srvcExpr.getAlreadySubscribedMsgSender();

                            if ((srvcExpr.getMetaDataMap().get("FREEMIUM") != null) && (((String) srvcExpr.getMetaDataMap().get("FREEMIUM")).equals("YES")) && (Integer.parseInt(thisSubscription.get("billing_type").toString()) == 0)) {
                                pushMsgSender = srvcExpr.getWelcomeMsgSender();
                                logState(accountId, siteId, keyword, msisdn, "Setting Kannel FROM attribute to " + pushMsgSender);
                                ServiceManager.subscribeToService(msisdn, keywordList, accountId, srvcExpr.getSubscriptionInterval(), 1, 1);
                                request.setAttribute("x-kannel-header-from", pushMsgSender);

                                skipMessagePush = false;
                            } else if ((metaDataMap.get("OVERRIDE_CONTENT_ON_RETURNING_ACTIVE") != null) && (((String) metaDataMap.get("OVERRIDE_CONTENT_ON_RETURNING_ACTIVE")).trim().equalsIgnoreCase("TRUE"))) {
                                request.setAttribute("override_msg", pushMsg);
                                logState(accountId, siteId, keyword, msisdn, "Setting Kannel FROM attribute to " + pushMsgSender);
                                request.setAttribute("x-kannel-header-from", pushMsgSender);

                                skipMessagePush = true;
                            } else {
                                logState(accountId, siteId, keyword, msisdn, "Setting Kannel FROM attribute to " + srvc.getServiceResponseSender());
                                request.setAttribute("x-kannel-header-from", srvc.getServiceResponseSender());

                                promoId = "";
                                serviceRespCode = "";
                                try {
                                    skipMessagePush = Boolean.parseBoolean((String) metaDataMap.get("skip_msg_push".toUpperCase()));
                                } catch (Exception e) {
                                    skipMessagePush = false;
                                }

                                if (skipMessagePush) {
                                    int accessCount = 0;
                                    try {
                                        accessCount = ServiceManager.getLastAccessCount(msisdn, accountId, keyword);
                                    } catch (Exception e) {
                                    }
                                    if (accessCount > 2) {
                                        skipMessagePush = true;
                                    } else {
                                        skipMessagePush = false;
                                    }
                                }
                            }
                        } else {
                            String renewalMessage = "";
                            boolean isInActive = isInActiveUser(accountId, keyword, msisdn);

                            if (isInActive) {
                                updateSubscription(msisdn, accountId, keyword, srvcExpr.getSubscriptionInterval(), false);
                                renewalMessage = "Dear subscriber, your subscription to " + srvc.getKeyword() + " has been renewed. This service is free for " + srvcExpr.getSubscriptionInterval() + " days.";
                            } else {
                                srvcExpr.getAlreadySubscribedMsg();
                            }

                            if ((metaDataMap.get("OVERRIDE_CONTENT_ON_RETURNING_INACTIVE") != null) && (((String) metaDataMap.get("OVERRIDE_CONTENT_ON_RETURNING_INACTIVE")).trim().equalsIgnoreCase("TRUE"))) {
                                request.setAttribute("override_msg", renewalMessage);
                                logState(accountId, siteId, keyword, msisdn, "Setting Kannel FROM attribute to " + srvc.getServiceResponseSender());
                                request.setAttribute("x-kannel-header-from", srvcExpr.getWelcomeMsgSender());

                                skipMessagePush = true;
                            } else {
                                pushMsg = renewalMessage;
                                pushMsgSender = srvcExpr.getWelcomeMsgSender();

                                request.setAttribute("x-kannel-header-from", srvc.getServiceResponseSender());
                            }
                        }
                    } else {
                        if ((altKeyword != null) && (!altKeyword.equals(""))) {
                            service_keyword = altKeyword;
                            logState(accountId, siteId, keyword, msisdn, "Service keyword changed for subscription: " + service_keyword);
                        }

                        if (metaDataMap.get("SEND_CONTENT_IF_UNSUB") != null && metaDataMap.get("SEND_CONTENT_IF_UNSUB").toString().trim().equalsIgnoreCase("FALSE")) {
                            alreadySubscribed = ServiceManager.hasRecentUnsubscription(msisdn, service_keyword, accountId);
                        } else {
                            alreadySubscribed = false;
                        }

                        if (alreadySubscribed) {
                            ArrayList keywordList = new ArrayList();
                            keywordList.add(service_keyword);

                            if (srvcExpr.getSubscriptionInterval() == -1) {
                                pushMsg = srvcExpr.getPromoMsg();
                                pushMsgSender = srvcExpr.getPromoMsgSender();
                            } else if (srvcExpr.getSubscriptionInterval() == -2) {
                                request.setAttribute("override_msg", srvcExpr.getPromoMsg());
                                if ((request.getAttribute("x-kannel-header-from") != null) && (!((String) request.getAttribute("x-kannel-header-from")).equals(""))) {
                                    response.addHeader("X-Kannel-From", srvcExpr.getPromoMsgSender());
                                } else if ((srvc.getServiceResponseSender() != null) && (!srvc.getServiceResponseSender().equals(""))) {
                                    System.out.println(new java.util.Date() + ": Setting X-Kannel-From header (" + srvc.getServiceResponseSender() + ")");
                                    response.addHeader("X-Kannel-From", srvcExpr.getPromoMsgSender());
                                }
                            } else {
                                if ((srvcExpr.getMetaDataMap().get("FREEMIUM") == null) || (((String) srvcExpr.getMetaDataMap().get("FREEMIUM")).equals("NO"))) {
                                    ServiceManager.subscribeToService(msisdn, keywordList, accountId, srvcExpr.getSubscriptionInterval(), 1, 1);
                                } else {
                                    ServiceManager.subscribeToService(msisdn, keywordList, accountId, srvcExpr.getSubscriptionInterval(), 1, 0);
                                }

                                String subscriptionResumedMessage = srvcExpr.getWelcomeMsg();
                                request.setAttribute("override_msg", subscriptionResumedMessage);
                                logState(accountId, siteId, keyword, msisdn, "Setting Kannel FROM attribute to " + srvcExpr.getWelcomeMsgSender());
                                request.setAttribute("x-kannel-header-from", srvcExpr.getWelcomeMsgSender());

                                skipMessagePush = true;
                            }

                            vmAcceptance(accountId, service_keyword, msisdn, srvc.getServiceName(), 
                                    srvcExpr.getWelcomeMsgSender().equals("") ? shortCode : srvcExpr.getWelcomeMsgSender(), smsc);
                        } else if (srvcExpr.getSubscriptionInterval() == -1) {
                            pushMsg = srvcExpr.getPromoMsg();
                            pushMsgSender = srvcExpr.getPromoMsgSender();
                        } else if (srvcExpr.getSubscriptionInterval() == -2) {
                            request.setAttribute("override_msg", srvcExpr.getPromoMsg());
                            if ((request.getAttribute("x-kannel-header-from") != null) && (!((String) request.getAttribute("x-kannel-header-from")).equals(""))) {
                                response.addHeader("X-Kannel-From", srvcExpr.getPromoMsgSender());
                            } else if ((srvc.getServiceResponseSender() != null) && (!srvc.getServiceResponseSender().equals(""))) {
                                System.out.println(new java.util.Date() + ": Setting X-Kannel-From header (" + srvc.getServiceResponseSender() + ")");
                                response.addHeader("X-Kannel-From", srvcExpr.getPromoMsgSender());
                            }
                        } else {
                            logState(accountId, siteId, keyword, msisdn, "Subscription doesn't exist. Creating new subscription record...");

                            ArrayList keywordList = new ArrayList();
                            keywordList.add(service_keyword);

                            if (srvcExpr.getSubscriptionInterval() == 0) {
                                ServiceManager.subscribeToService(msisdn, keywordList, accountId);
                            } else if ((srvcExpr.getMetaDataMap().get("FREEMIUM") == null) || (((String) srvcExpr.getMetaDataMap().get("FREEMIUM")).equals("NO"))) {
                                ServiceManager.subscribeToService(msisdn, keywordList, accountId, srvcExpr.getSubscriptionInterval(), 1, 1);
                            } else {
                                ServiceManager.subscribeToService(msisdn, keywordList, accountId, srvcExpr.getSubscriptionInterval(), 1, 0);
                            }

                            vmAcceptance(accountId, service_keyword, msisdn, srvc.getServiceName(), 
                                    srvcExpr.getWelcomeMsgSender().equals("") ? shortCode : srvcExpr.getWelcomeMsgSender(), smsc);

                            pushMsg = srvcExpr.getWelcomeMsg();
                            pushMsgSender = srvcExpr.getWelcomeMsgSender().equals("") ? shortCode : srvcExpr.getWelcomeMsgSender();

                            promoId = "";
                            serviceRespCode = "";
                        }
                    }
                } else {
                    pushMsg = srvcExpr.getPromoMsg();
                    pushMsgSender = srvcExpr.getPromoMsgSender();
                }

                if (!skipMessagePush) {
                    pushMsgWaitTime = srvcExpr.getPushMsgWaitTime();

                    CPConnections cnxn;
                    String flashPush = (String) metaDataMap.get("push_msg_flash".toUpperCase());

                    if ((flashPush != null) && (flashPush.equals("1"))) {
                        cnxn = CPConnections.getConnection(accountId, msisdn, "kannel");
                        new Thread(new ThreadedMessageSender(cnxn, msisdn, pushMsgSender, pushMsg, "&mclass=0", pushMsgWaitTime)).start();
                    } else {
                        cnxn = CPConnections.getConnection(accountId, msisdn);
                        new Thread(new ThreadedMessageSender(cnxn, msisdn, pushMsgSender, pushMsg, pushMsgWaitTime)).start();
                    }
                }
                request.setAttribute("promoId", promoId);

               
                promoImpressionsCheck(serviceRespCode, keyword, accountId, msisdn, request);
            } else {
                logState(accountId, siteId, keyword, msisdn, "No experience configuration defined for service");
            }
        } catch (Exception e) {
            logState(accountId, siteId, keyword, msisdn, "Error @ serviceexperiencefilter: " + e.getMessage());
        }

        logState(accountId, siteId, keyword, msisdn, "Exiting serviceexperiencefilter.");

        filterChain.doFilter(req, res);
    }

    private void vmAcceptance(String accountId, String keyword, String msisdn, String serviceName, String shortCode, String smsc) {
        initializeServiceCustomization(accountId, keyword, shortCode);
        try {
            CPConnections cnxn = CPConnections.getConnection(accountId, msisdn);

            VMTransaction trans = VMServiceManager.viewTransaction(accountId, keyword, msisdn);
            if (trans.getStatus().equals("inv_sent")) {
                VMServiceManager.updateTransactionStatus(trans.getCampaignId(), msisdn, "inv_accepted", 10);

                String recruiter = trans.getRecruiterMsisdn();
                VMUser user = VMServiceManager.viewUser(keyword, accountId, recruiter);
                String displayable_number = "0" + trans.getRecipientMsisdn().substring(4);
                String confirmation_msg = "You recently invited " + displayable_number + " to use your favourite service. " + "We're excited to inform you that your invitation was just accepted! You now have " + user.getPoints() + " points.";

                new Thread(new ThreadedMessageSender(cnxn, recruiter, this.push_sender, confirmation_msg, 5000)).start();
            }

            if ((!smsc.equalsIgnoreCase("myBuzz")) && (!smsc.equalsIgnoreCase("myBuzz2"))) {
                new Thread(new ThreadedMessageSender(cnxn, msisdn, this.push_sender, this.inv_instruction, 30000)).start();
            }
        } catch (Exception exc) {
            System.out.println("Exception caught processing viral marketing step 2: acceptance");
        }
    }

    private void initializeServiceCustomization(String accountId, String keyword, String shortCode) {
        if (accountId.equals("000")) {
            this.push_sender = keyword;
            this.inv_instruction = ("Send INVITE followed by your friend's number to " + shortCode + " to share this service with them. Add FROM followed by your name to the message to personalize it.");
        } else {
            this.push_sender = shortCode;
            this.inv_instruction = ("Send INVITE followed by your friend's number to " + this.push_sender + " to share this service with them. Add FROM followed by your name to the message to personalize it.");
        }
    }

    private void promoImpressionsCheck(String srvcRespCode, String keyword, String accountId, String msisdn, HttpServletRequest request) throws Exception {
        try {
            System.out.println(new java.util.Date() + ": In promoImpression Checker");
            String hashString = msisdn + accountId + keyword + srvcRespCode.toUpperCase();
            
            PromoImpression pImpression = VMServiceManager.viewPromoImpression(hashString.hashCode());

            if (pImpression.exists()) {
                request.setAttribute("promoRespCode", keyword);
                VMServiceManager.updateAdRespSummary(pImpression);
                pImpression.setViewDate(new java.util.Date());
                VMServiceManager.updatePromoViewDate(pImpression);
                return;
            }

            if (!(srvcRespCode == null || srvcRespCode.equals(""))) {               

                pImpression.setHashCode(hashString.hashCode());
                pImpression.setAccountId(accountId);
                pImpression.setMsisdn(msisdn);
                pImpression.setKeyword(srvcRespCode);
                pImpression.setInventory_keyword(keyword);

                VMServiceManager.createPromoImpression(pImpression);
            }
        } catch (Exception exc) {
            System.out.println("Exception caught processing promotional campaign: " + exc.getMessage());
            throw new Exception(exc.getMessage());
        }
    }

    private void logState(String accountId, String siteId, String keyword, String msisdn, String state) {
        System.out.println(new java.util.Date() + ": " + accountId + ": " + siteId + ": " + keyword + ": " + msisdn + ": " + state);
    }

    private boolean bill(String msisdn, String accountID, String keyword, String site_id, HttpServletRequest request, String price, String shortcode) {
        boolean billed = false;
        String message = "";
        String siteType = "";
        String fullContextPath = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

        String lang = (String) request.getAttribute("default_lang");
        if ((lang == null) || (lang.equals(""))) {
            lang = "en";
        }

        Feedback feedback = (Feedback) getServletContext().getAttribute("feedback_" + lang);
        try {
            siteType = CPSite.viewSite(site_id).getSiteType();
            InfoService is = new InfoService();
            is.setAccountId(accountID);
            is.setKeyword(keyword);

            Calendar calendar = Calendar.getInstance();
            java.util.Date today = new java.util.Date(calendar.getTime().getTime());
            String dateString = com.rancard.util.Date.formatDate(today, "dd-MM-yyyy");
            is.setPublishDate(dateString);

            is.viewInfoService();

            System.out.println(new java.util.Date() + ": " + msisdn + ":About to bill...");

            if ((price == null) || (price.equals(""))) {
                System.out.println(new java.util.Date() + ": " + msisdn + ":ERROR:BILLING_MECH_FAILURE:PricePoint Missing!");
                throw new Exception("11000");
            }

            PricePoint pricePoint = PaymentManager.viewPricePointFor(new String[]{price}, msisdn);
            String pricePointId = pricePoint.getPricePointId();
            System.out.println(new java.util.Date() + ":pricePoint ID:" + pricePointId);

            if ((pricePointId == null) || ("".equals(pricePointId))) {
                System.out.println(new java.util.Date() + ": " + msisdn + ":ERROR: Invalid pricePointID(s): " + price);

                throw new Exception("11000");
            }

            System.out.println(new java.util.Date() + ": " + keyword + ": " + msisdn + ": Entering initiatePayment from sendinfo");

            String transactionId = "";
            if (pricePoint.getBillingMech().equals("4")) {
                transactionId = uidGen.genUID("", 5);
            } else if ((siteType.equals("1")) || (siteType.equals("0"))) {
                transactionId = uidGen.genUID("MP-", 5);
            } else {
                transactionId = uidGen.genUID("OD-", 5);
            }

            String completeTransnxnUrl = fullContextPath + "/sendinfo_push.jsp?msisdn=" + URLEncoder.encode(msisdn, "UTF-8") + "&keyword=" + URLEncoder.encode(keyword.toUpperCase(), "UTF-8")
                    + "&alert_count=" + is.getMsgId() + "&dest=" + URLEncoder.encode(shortcode, "UTF-8") + "&siteId=" + URLEncoder.encode(site_id, "UTF-8") + "&transId=" + URLEncoder.encode(transactionId, "UTF-8");

            if (pricePoint.getBillingMech().equals("3")) {
                completeTransnxnUrl = completeTransnxnUrl + "&sender=KEYWORD";
            }

            UserServiceTransaction trans = new UserServiceTransaction();
            trans.setAccountId(accountID);

            trans.setDate(new Timestamp(new java.util.Date().getTime()));
            trans.setKeyword(keyword.toUpperCase());
            trans.setMsg("on-demand");
            trans.setMsisdn(msisdn);
            trans.setPricePoint(pricePointId);
            trans.setTransactionId(transactionId);
            trans.setIsBilled(0);
            trans.setIsCompleted(0);
            trans.setCallBackUrl(completeTransnxnUrl);

            boolean transactionCreated;
            int isCompleted = 0;
            try {
                ServiceManager.createTransaction(trans);
                System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " created!");
                transactionCreated = true;
            } catch (Exception e) {
                transactionCreated = false;
                System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " NOT created! Error message: " + e.getMessage());
            }

            if (transactionCreated) {
                try {
                    CPConnections cnxn = CPConnections.getConnection(accountID, msisdn);
                    billed = PaymentManager.initiatePayment(pricePoint, cnxn, msisdn, transactionId, "", completeTransnxnUrl);
                } catch (Exception e) {
                    if (e.getMessage().equals("READ_TIMEOUT")) {
                        message = "We've received your request for a " + is.getServiceName() + " item. Please be patient while we process it.";
                        isCompleted = 0;
                    } else if (e.getMessage().equals("INSUFFICIENT_CREDIT")) {
                        message = "We couldn't complete purchase of the " + is.getServiceName() + " item you requested. Please top up and then send " + is.getKeyword().toUpperCase() + " to " + shortcode;

                        isCompleted = 1;
                    } else if (e.getMessage().equals("ERROR")) {
                        message = "We couldn't complete purchase of the " + is.getServiceName() + " item you requested. Please try again. Send " + is.getKeyword().toUpperCase() + " to " + shortcode + ". You've not been billed.";

                        isCompleted = 1;
                    }

                }

                request.setAttribute("x-kannel-header-binfo", transactionId);
                System.out.println(new java.util.Date() + ": " + keyword + ": " + msisdn + ": Completed initiatePayment from sendinfo with result: " + billed);

                if (billed) {
                    trans.setIsBilled(1);
                    trans.setIsCompleted(1);
                } else {
                    trans.setIsBilled(0);
                    trans.setIsCompleted(isCompleted);
                }

                try {
                    ServiceManager.updateTransaction(trans.getTransactionId(), trans.getIsCompleted(), trans.getIsBilled());
                    System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " updated!");
                } catch (Exception e) {
                    System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " NOT updated! Error message: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            try {
                if (siteType.equals("2")) {
                    message = feedback.getUserFriendlyDescription(e.getMessage());
                } else {
                    message = feedback.formDefaultMessage(e.getMessage());
                }
            } catch (Exception ex) {
                message = ex.getMessage();
            }
        }

        if (!message.equals("")) {
            new Thread(new ThreadedMessageSender(new CPConnections(), msisdn, shortcode, message, 0)).start();
        }

        return billed;
    }

    private int updateSubscription(String msisdn, String accountId, String keyword, int subscriptionInterval, boolean activeSub) throws Exception {
        int numOfDaysTillEndOfSubscription = 0;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        try {
            con = DConnect.getConnection();
            String SQL = "update service_subscription set subscription_date=subscription_date, status=1, next_subscription_date=DATE_ADD(" + (activeSub ? "next_subscription_date" : "CURRENT_TIMESTAMP") + ", INTERVAL " + subscriptionInterval + " DAY) where msisdn='" + msisdn + "' and keyword='" + keyword + "' and account_id='" + accountId + "'";

            prepstat = con.prepareStatement(SQL);
            prepstat.execute();

            SQL = "select next_subscription_date from service_subscription where msisdn='" + msisdn + "' and keyword='" + keyword + "' and account_id='" + accountId + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();

            while (rs.next()) {
                Timestamp nextSubxnDate = rs.getTimestamp("next_subscription_date");
                numOfDaysTillEndOfSubscription = rest_of_days(nextSubxnDate);
            }
        } catch (Exception ex) {
            System.out.println(new java.util.Date() + ": [bbcNewsMtnNg:ERROR]: Exception caught :" + ex.getMessage());
            throw new Exception(ex.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prepstat != null) {
                prepstat.close();
            }
            if (con != null) {
                con.close();
            }
        }

        return numOfDaysTillEndOfSubscription;
    }

    private static int rest_of_days(Timestamp nextSubxnDate) {
        GregorianCalendar tmpDate1 = new GregorianCalendar();
        GregorianCalendar tmpDate2 = new GregorianCalendar();

        tmpDate1.setTime(new java.util.Date());
        tmpDate2.setTimeInMillis(nextSubxnDate.getTime());

        int days_elapsed = calculate(tmpDate2, tmpDate1);

        if (days_elapsed < 1) {
            days_elapsed = 0;
        }

        return days_elapsed;
    }

    private static int calculate(GregorianCalendar t1, GregorianCalendar t2) {
        int ndays = 0;

        if (t1.get(1) < t2.get(1)) {
            ndays += 366 - t1.get(6);

            for (int n = t2.get(1) + 1; n <= t2.get(1) - 1; n++) {
                ndays += 365;
            }
        }

        ndays += t2.get(6);
        if (t2.get(1) == t1.get(1)) {
            ndays = t1.get(6) - t2.get(6);
        }

        return ndays;
    }

    private boolean isInActiveUser(String accountId, String keyword, String msisdn) throws Exception {
        boolean isActive = false;
        int status = 0;
        ResultSet rs = null;
        Connection dbConnection = null;
        PreparedStatement prepstat = null;
        try {
            dbConnection = DConnect.getConnection();
            prepstat = dbConnection.prepareStatement("SELECT status from service_subscription WHERE account_id=? AND keyword = ? AND msisdn LIKE ? limit 1;");

            prepstat.setString(1, accountId);
            prepstat.setString(2, keyword);
            prepstat.setString(3, msisdn);

            rs = prepstat.executeQuery();
            while (rs.next()) {
                status = rs.getInt(1);
            }
            isActive = status == -1;
        } catch (Exception ex) {
            System.out.println(new java.util.Date() + ": [isActiveUser Method:ERROR]: Exception caught :" + ex.getMessage());
            throw new Exception(ex.toString());
        } finally {
            if (dbConnection != null) {
                dbConnection.close();
            }

            if (prepstat != null) {
                prepstat.close();
            }

            if (rs != null) {
                rs.close();
            }
        }
        return isActive;
    }
}