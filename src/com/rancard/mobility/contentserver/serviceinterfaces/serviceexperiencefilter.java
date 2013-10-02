package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.mobility.common.ThreadedMessageSender;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.infoserver.common.services.*;
import com.rancard.mobility.infoserver.viralmarketing.PromoImpression;
import com.rancard.mobility.infoserver.viralmarketing.VMServiceManager;
import com.rancard.util.DateUtil;

import java.io.IOException;
import java.util.Date;
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


    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        System.out.println(new Date() + "\t[" + serviceexperiencefilter.class + "]\tDEBUG\tEntering service experience: " + request.getQueryString());

        String accountID = (String) req.getAttribute("acctId");
        String siteId = request.getParameter("siteId");
        String keyword = req.getParameter("keyword");
        String msisdn = req.getParameter("msisdn");
        String shortCode = req.getParameter("dest").contains("+") ? req.getParameter("dest").substring(1) : req.getParameter("dest");

        String pushMsg = "";
        String pushMsgSender = "";
        int pushMsgWaitTime;
        boolean skipMessagePush = false;


        try {
            UserService srvc = (UserService) request.getAttribute("user_service");
            if (srvc == null) {
                srvc = UserServiceDB.viewService(keyword, accountID);
            }

            UserServiceExperience srvcExpr = ServiceManager.viewServiceExperience(accountID, siteId, srvc.getKeyword());

            if (srvcExpr == null) {
                System.out.println(new Date() + "\t[" + serviceexperiencefilter.class + "]\tERROR\tNo experience configuration defined for service");
            } else {
                ServiceExperienceMetaData keywordOverrideOption = srvcExpr.getMetaData().getOption(ServiceExperienceMetaDataOption.OVERRIDE_KEYWORD);
                String serviceKeyword;
                if (keywordOverrideOption != null) {
                    serviceKeyword = keywordOverrideOption.getValue();
                    request.setAttribute("override_keyword", keywordOverrideOption.getValue());
                } else {
                    serviceKeyword = keyword;
                }

                if (!srvc.isSubscription()) {
                    pushMsg = srvcExpr.getPromoMsg();
                    pushMsgSender = srvcExpr.getPromoMsgSender();
                } else {
                    Date now = new Date();
                    ServiceSubscription subscription = ServiceSubscriptionDB.viewSubscription(msisdn, srvc.getAccountId(), serviceKeyword);

                    if (subscription != null) {
                        if (subscription.getNextSubscriptionDate() == null || subscription.getNextSubscriptionDate().after(now)) {
                            System.out.println(new Date() + "\t[" + serviceexperiencefilter.class + "]\tINFO\tSubscription already exists");


                            pushMsg = srvcExpr.getAlreadySubscribedMsg();
                            pushMsgSender = srvcExpr.getAlreadySubscribedMsgSender() == null || srvcExpr.getAlreadySubscribedMsgSender().equals("")
                                    ? shortCode : srvcExpr.getAlreadySubscribedMsgSender();

                            System.out.println(new Date() + "\t[" + serviceexperiencefilter.class + "]\tINFO\tSetting Kannel FROM attribute to " + pushMsgSender);
                            request.setAttribute("x-kannel-header-from", pushMsgSender);

                            ServiceExperienceMetaData overrideContentOption = srvcExpr.getMetaData().getOption(ServiceExperienceMetaDataOption.OVERRIDE_CONTENT_ON_RETURNING_ACTIVE);
                            if (overrideContentOption != null && overrideContentOption.getValue().trim().equalsIgnoreCase("TRUE")) {
                                request.setAttribute("override_msg", pushMsg);
                            }

                            skipMessagePush = getSkipMessagePush(srvcExpr, msisdn);

                        } else {
                            System.out.println(new Date() + "\t[" + serviceexperiencefilter.class + "]\tINFO\tSubscription already exists. But is inactive");

                            ServiceExperienceMetaData allowExtensions = srvcExpr.getMetaData().getOption(ServiceExperienceMetaDataOption.ALLOW_EXTENSION);
                            if (allowExtensions != null && allowExtensions.getValue().equalsIgnoreCase("TRUE")) {
                                ServiceSubscriptionDB.updateNextSubscriptionDate(msisdn, subscription.getAccountID(),
                                        subscription.getKeyword(), DateUtil.addDaysToDate(now, srvcExpr.getSubscriptionInterval()));
                                pushMsg = "Dear subscriber, your subscription to " + srvc.getKeyword() + " has been renewed.";                                
                            }else{
                                pushMsg = srvcExpr.getAlreadySubscribedMsg();              
                            }
                            
                            pushMsgSender = srvcExpr.getWelcomeMsgSender() == null || srvcExpr.getWelcomeMsgSender().equals("")
                                        ? shortCode : srvcExpr.getWelcomeMsgSender();

                            System.out.println(new Date() + "\t[" + serviceexperiencefilter.class + "]\tINFO\tSetting Kannel FROM attribute to " + pushMsgSender);
                            request.setAttribute("x-kannel-header-from", pushMsgSender);
                        }

                    } else {
                        if (srvcExpr.getSubscriptionInterval() == -1) {
                            pushMsg = srvcExpr.getPromoMsg();
                            pushMsgSender = srvcExpr.getPromoMsgSender();
                        } else if (srvcExpr.getSubscriptionInterval() == -2) {
                            request.setAttribute("override_msg", srvcExpr.getPromoMsg());
                            if (request.getAttribute("x-kannel-header-from") == null) {
                                response.addHeader("X-Kannel-From", srvcExpr.getPromoMsgSender() == null || srvcExpr.getPromoMsgSender().equals("")
                                        ? shortCode : srvcExpr.getPromoMsgSender());
                            } else {
                                response.setHeader("x-kannel-header-from", srvcExpr.getPromoMsgSender() == null || srvcExpr.getPromoMsgSender().equals("")
                                        ? shortCode : srvcExpr.getPromoMsgSender());
                            }
                        } else {
                            ServiceExperienceMetaData freemiumOption = srvcExpr.getMetaData().getOption(ServiceExperienceMetaDataOption.FREEMIUM);
                            if (freemiumOption == null || freemiumOption.getValue().equalsIgnoreCase("NO")) {
                                ServiceSubscriptionDB.addSubscription(now, srvcExpr.getSubscriptionInterval(), msisdn, accountID,
                                        keyword, 1, 1);
                            } else {
                                ServiceSubscriptionDB.addSubscription(now, srvcExpr.getSubscriptionInterval(), msisdn, accountID,
                                        keyword, 1, 0);
                            }

                            pushMsg = srvcExpr.getWelcomeMsg();
                            pushMsgSender = srvcExpr.getWelcomeMsgSender() == null ? shortCode : srvcExpr.getWelcomeMsgSender();
                            request.setAttribute("x-kannel-header-from", srvcExpr.getWelcomeMsgSender());

                            ServiceExperienceMetaData sendContentifUnsubOption = srvcExpr.getMetaData().getOption(ServiceExperienceMetaDataOption.SEND_CONTENT_IF_UNSUB);
                            if (sendContentifUnsubOption != null && sendContentifUnsubOption.getValue().equalsIgnoreCase("FALSE")) {
                                if (ServiceManager.hasRecentUnsubscription(msisdn, serviceKeyword, accountID)) {
                                    request.setAttribute("override_msg", pushMsg);
                                    skipMessagePush = true;
                                }

                            }
                        }
                        VMServiceManager.handleViralMarketing(srvc, msisdn);
                    }
                }
            }

            if (!skipMessagePush) {
                pushMsgWaitTime = srvcExpr.getPushMsgWaitTime();

                CPConnections cnxn;
                ServiceExperienceMetaData flashPushOption = srvcExpr.getMetaData().getOption(ServiceExperienceMetaDataOption.PUSH_MSG_FLASH);

                if (flashPushOption != null && flashPushOption.getValue().trim().equals("1")) {
                    cnxn = CPConnections.getConnection(accountID, msisdn, "kannel");
                    new Thread(new ThreadedMessageSender(cnxn, msisdn, pushMsgSender, pushMsg, "&mclass=0", pushMsgWaitTime)).start();
                } else {
                    cnxn = CPConnections.getConnection(accountID, msisdn);
                    new Thread(new ThreadedMessageSender(cnxn, msisdn, pushMsgSender, pushMsg, pushMsgWaitTime)).start();
                }
            }

            if (!(srvcExpr.getPromoId() == null || srvcExpr.getPromoId().equals(""))) {
                request.setAttribute("promoId", srvcExpr.getPromoId());
                promoImpressionsCheck((srvcExpr.getPromoRespCode() == null || srvcExpr.getPromoRespCode().equals(""))
                        ? pushMsgSender : srvcExpr.getPromoRespCode(), keyword, accountID, msisdn, request);
            }

        } catch (Exception e) {
            System.out.println(new Date() + "\t[" + serviceexperiencefilter.class + "]\tERROR\tThere was a problem: " + e.getMessage());
        }

        System.out.println(new Date() + "\t[" + serviceexperiencefilter.class + "]\tINFO\tExiting serviceexperiencefilter.");

        filterChain.doFilter(req, res);
    }

    /*private void vmAcceptance(String accountId, String keyword, String msisdn, String serviceName, String shortCode, String smsc) {
     initializeServiceCustomization(accountId, keyword, shortCode);
     try {
     CPConnections cnxn = CPConnections.getConnection(accountId, msisdn);

     VMTransaction trans = VMServiceManager.viewTransaction(accountId, keyword, msisdn);
     if (trans.getStatus() == VMTransactionStatus.INV_SENT) {
     VMServiceManager.updateTransactionStatus(trans.getCampaignID(), msisdn, VMTransactionStatus.INV_ACCEPTED);

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

     //function to implement acceptance of viral marketting
     private void vmAcceptance(UserService srvc, String msisdn, String msisdnNetworkId) throws Exception {
     //Performing service customizations based on operator/provider
     com.rancard.mobility.rendezvous.discovery.viral_marketing.VMCampaign campaign =
     com.rancard.mobility.rendezvous.discovery.viral_marketing.VMServiceManager.viewCampaignByService(srvc.getAccountId(), srvc.getKeyword());

     try {
     //Get connections for sending messages
     CPConnections cnxn = new CPConnections();
     cnxn = CPConnections.getConnection(srvc.getAccountId(), msisdn);

     // Update VM profile and transaction log
     com.rancard.mobility.rendezvous.discovery.viral_marketing.VMTransaction trans =
     com.rancard.mobility.rendezvous.discovery.viral_marketing.VMServiceManager.viewMostRecentTransaction(srvc.getAccountId(), srvc.getKeyword(), msisdn);
     if (trans.getStatus().equals("inv_sent")) { // ensures user has never accepted in the past
     com.rancard.mobility.rendezvous.discovery.viral_marketing.VMServiceManager.updateTransactionStatus(trans.getCampaignId(), msisdn, trans.getRecruiterMsisdn(), "inv_accepted", 10);

     //Send confirmation message to recruiter
     String recruiter = trans.getRecruiterMsisdn();
     VMUser user = VMServiceManager.viewUser(srvc.getKeyword(), srvc.getAccountId(), recruiter);
     String displayable_number = "0" + trans.getRecipientMsisdn().substring(4);
     String default_confirmation_msg = "You recently invited " + displayable_number + " to use your favourite service. "
     + "We're excited to inform you that your invitation was just accepted! You now have " + user.getPoints() + " points.";
     String confirmation_msg = campaign.getInviteAcceptedMsg();
     if (confirmation_msg == null || "".equals(confirmation_msg)) {
     confirmation_msg = default_confirmation_msg;
     }
     String insertions = "msisdn=" + displayable_number + "&keyword=" + campaign.getKeyword() + "&serviceName=" + srvc.getServiceName() + "&points=" + user.getPoints();
     confirmation_msg = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, confirmation_msg);

     //Push acceptance notification to recruiter after 5 seconds including total points acquired.
     (new Thread(new ThreadedMessageSender(cnxn, recruiter, campaign.getMessageSender(), confirmation_msg, 5000))).start();

     }

     if (campaign.getVmHowTo() != null && !campaign.getVmHowTo().equals("")) {
     String howTo = com.rancard.mobility.rendezvous.discovery.viral_marketing.VMServiceManager.createHowToMessage(srvc, campaign, msisdn);
     // Push additional message showing how to invite more people with delay of 30 seconds
     (new Thread(new ThreadedMessageSender(cnxn, msisdn, campaign.getMessageSender(), howTo, 30000))).start();
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
     }*/
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

            pImpression.setHashCode(hashString.hashCode());
            pImpression.setAccountId(accountId);
            pImpression.setMsisdn(msisdn);
            pImpression.setKeyword(srvcRespCode);
            pImpression.setInventory_keyword(keyword);

            VMServiceManager.createPromoImpression(pImpression);

        } catch (Exception exc) {
            System.out.println("Exception caught processing promotional campaign: " + exc.getMessage());
            throw new Exception(exc.getMessage());
        }
    }

    private boolean getSkipMessagePush(UserServiceExperience srvcExp, String msisdn) {
        ServiceExperienceMetaData skipMsgOption = srvcExp.getMetaData().getOption(ServiceExperienceMetaDataOption.SKIP_MSG_PUSH);
        boolean skipMessagePush = (skipMsgOption == null)?false: Boolean.valueOf(skipMsgOption.getValue().toUpperCase());

        if (skipMessagePush) {
            int accessCount = 0;
            try {
                accessCount = ServiceManager.getLastAccessCount(msisdn, srvcExp.getAccountId(), srvcExp.getKeyword());
            } catch (Exception e) {
                System.out.println(new Date() + "\t" + serviceexperiencefilter.class.getName() + "ERROR\tProblem getting Last Access Count: " + e.getMessage());
            }
            return (accessCount > 2);
        }

        return skipMessagePush;
    }
}
