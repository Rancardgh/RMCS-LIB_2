/*
 * processsubscriberequest.java
 *
 * Created on October 28, 2006, 1:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.Feedback;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.contentserver.CPSite;
import com.rancard.mobility.infoserver.InfoService;
import com.rancard.mobility.infoserver.InfoServiceDB;
import com.rancard.util.payment.PaymentManager;
import com.rancard.util.payment.PricePoint;
import com.rancard.mobility.infoserver.common.services.UserServiceTransaction;
import com.rancard.mobility.infoserver.common.services.ServiceManager;
import com.rancard.mobility.rendezvous.discovery.viral_marketing.VMServiceManager;
import com.rancard.util.GsmCharset;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;

/**
 *
 * @author Messenger
 */
public class sendinfo extends HttpServlet implements RequestDispatcher {

    //Initialize global variables
    public void init() throws ServletException {
    }

    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            request.setCharacterEncoding ("UTF-8");
        } catch (Exception e) {}

        String fullContextPath = "http://192.168.1.243:81" + request.getContextPath ();
        boolean skipMessagingFromCallBack = false; //used to communicate with handler who implements the callback functionality whether to send notification or not

        //get reeponse writer
        PrintWriter out = response.getWriter();
        CPConnections cnxn = new CPConnections();
        // get accountId of content owner i.e cpuser
        String provId = (String) request.getAttribute("acctId");
        provId = (provId == null) ? request.getParameter("acctId") : provId;

        // find out what type of site is requesting this content i.e look this up from cp_sites
        String siteType = (String) request.getAttribute("site_type");
        siteType = (siteType == null) ? request.getParameter("site_type") : siteType;               
        // get default language of the service
        String lang = (String) request.getAttribute("default_lang");
        String override_msg = (String) request.getAttribute("override_msg");
        String override_keyword = (String) request.getAttribute("override_keyword");
        lang = (lang == null) ? request.getParameter("default_lang") : lang;
        String shortcode = (request.getParameter("dest") == null) ? "" : request.getParameter("dest");
        String siteId = (request.getParameter("siteId") == null) ? "" : request.getParameter("siteId");
        // get details of user requesting for content
        String msisdn = request.getParameter("msisdn");

        // get keyword
        String kw = request.getParameter("keyword");
        String alreadyBilled = request.getParameter("pre_billed");

        //log statement
        System.out.println(new java.util.Date() + ":@com.rancard.mobility.contentserver.serviceinterfaces.sendinfo..");

        if (kw == null) {
            kw = (String) request.getAttribute("keyword");
        }
        if (alreadyBilled == null) {
            alreadyBilled = (String) request.getAttribute("pre_billed");
        }

        // now override keyword if necessary
        if (override_keyword == null || override_keyword.equals("")) {
            //Do nothing here, just keep going       
        } else {
            kw = override_keyword;
        }

        // if no default language is specified use english
        if (lang == null || lang.equals("")) {
            lang = "en";
        }
        Feedback feedback = (Feedback) this.getServletContext().getAttribute("feedback_" + lang);
        if (feedback == null) {
            try {
                feedback = new Feedback();
            } catch (Exception e) {
            }
        }

        /***********************************************************************************************************
         * Begin validations
         ***********************************************************************************************************/
        // check for site ID
        String message = "";
        if (provId == null || provId.equals("")) {
            try {
                if (siteType.equals(CPSite.SMS)) {
                    message = feedback.getUserFriendlyDescription(Feedback.MISSING_INVALID_PROV_ID);
                } else {
                    message = feedback.formDefaultMessage(Feedback.MISSING_INVALID_PROV_ID);
                }
                //log statment
                System.out.println(new java.util.Date() + ":Feedback.MISSING_INVALID_PROV_ID: " + message);

            } catch (Exception ex) {
                message = ex.getMessage();
            }
            boolean isAsciiPrintable = org.apache.commons.lang.StringUtils.isAsciiPrintable (message);
            if (!isAsciiPrintable) {
                System.out.println ("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                request.setAttribute ("X-Kannel-Coding", "2");
                if (request.getAttribute ("X-Kannel-Coding") != null)
                    System.out.println ("Request contains X-Kannel-Coding attribute");
            }
            out.println(message);
            return;
        }

        // check for keyword
        if (kw == null || kw.equals("")) {
            try {
                if (siteType.equals(CPSite.SMS)) {
                    message = feedback.getUserFriendlyDescription(Feedback.NO_SUCH_SERVICE);
                } else {
                    message = feedback.formDefaultMessage(Feedback.NO_SUCH_SERVICE);
                }
                //log statement
                System.out.println(new java.util.Date() + ":NO_SUCH_SERVICE (keyword empty): " + message);

            } catch (Exception ex) {
                message = ex.getMessage();
                //log statement
                System.out.println(new java.util.Date() + ":error: " + message);

            }
            boolean isAsciiPrintable = org.apache.commons.lang.StringUtils.isAsciiPrintable (message);
            if (!isAsciiPrintable) {
                System.out.println ("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                request.setAttribute ("X-Kannel-Coding", "2");
                if (request.getAttribute ("X-Kannel-Coding") != null)
                    System.out.println ("Request contains X-Kannel-Coding attribute");
            }
            out.println(message);
            return;
        }

        //logging statements
        System.out.println(new java.util.Date() + ":Info to be sent for keyword " + kw.toUpperCase() + " on account with ID " + provId);
        //end of logging

        //determine whether to do a message push or pull depending on the service type
        InfoService is = new InfoService();

        //do message pull - Info Service
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        java.util.Date today = new java.util.Date(calendar.getTime().getTime());
        String dateString = com.rancard.util.Date.formatDate(today, InfoService.DATEFORMAT);
        is.setAccountId(provId);
        is.setKeyword(kw);
        is.setPublishDate(dateString);

        calendar = null;
        today = null;

        //view info service
        try {
            is.viewInfoService();

        } catch (Exception e) {
            //loggin statement
            System.out.println(new java.util.Date() + ":error: " + e.getMessage());

            try {
                if (siteType.equals(CPSite.SMS)) {
                    message = feedback.getUserFriendlyDescription(Feedback.NO_SUCH_SERVICE);
                } else {
                    message = feedback.formDefaultMessage(Feedback.NO_SUCH_SERVICE);
                }
                //loggin statement
                System.out.println(new java.util.Date() + ":NO_SUCH_SERVICE: " + message);
            } catch (Exception ex) {
                message = ex.getMessage();
                //loggin statement
                System.out.println(new java.util.Date() + ":error: " + message);
            }

            boolean isAsciiPrintable = org.apache.commons.lang.StringUtils.isAsciiPrintable (message);
            if (!isAsciiPrintable) {
                System.out.println ("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                request.setAttribute ("X-Kannel-Coding", "2");
                if (request.getAttribute ("X-Kannel-Coding") != null)
                    System.out.println ("Request contains X-Kannel-Coding attribute");
            }
            out.println(message);
            return;
        }
        // get information to be sent back to the end-user
        //return info
        String info = "";
        String compactInfo = "";
        if (is.getMessage() == null || is.getMessage().equals("")) {
            info = is.getDefaultMessage();
        } else {
            info = is.getMessage();
        }

        if (msisdn != null && !msisdn.equals("")) {
            //validate number
            String number = msisdn;
            if (number.indexOf("+") != -1) {
                StringBuffer sb = new StringBuffer(number);
                sb.deleteCharAt(number.indexOf("+"));
                number = sb.toString();

                sb = null;
            }
            number = number.trim();

            try {
                //checks that number is a string of digits only
                Long.parseLong(number);
                msisdn = "+" + number;
            } catch (NumberFormatException e) {
                //log statement
                System.out.println(new java.util.Date() + ":error parsing msisdn: " + e.getMessage());
                try {
                    if (siteType.equals(CPSite.SMS)) {
                        message = feedback.getUserFriendlyDescription(Feedback.MISSING_INVALID_MSISDN);
                    } else {
                        message = feedback.formDefaultMessage(Feedback.MISSING_INVALID_MSISDN);
                    }
                    //log statement
                    System.out.println(new java.util.Date() + ":MISSING_INVALID_MSISDN: " + message);
                } catch (Exception ex) {
                    message = ex.getMessage();
                    //log statement
                    System.out.println(new java.util.Date() + ":error: " + message);
                }

                boolean isAsciiPrintable = org.apache.commons.lang.StringUtils.isAsciiPrintable (message);
                if (!isAsciiPrintable) {
                    System.out.println ("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                    request.setAttribute ("X-Kannel-Coding", "2");
                    if (request.getAttribute ("X-Kannel-Coding") != null)
                        System.out.println ("Request contains X-Kannel-Coding attribute");
                }
                out.println(message);
                return;
            }

            //get connection to requesting network for this provider and ensure that subscriber is allowed to receive content
            try {
                cnxn = CPConnections.getConnection(provId, msisdn);
            } catch (Exception e) {
                //log statement
                System.out.println(new java.util.Date() + ":error: " + e.getMessage());

                try {
                    if (siteType.equals(CPSite.SMS)) {
                        message = feedback.getUserFriendlyDescription(Feedback.UNSUPPORTED_NETWORK);
                    } else {
                        message = feedback.formDefaultMessage(Feedback.UNSUPPORTED_NETWORK);
                    }
                    //log statement
                    System.out.println(new java.util.Date() + ":UNSUPPORTED_NETWORK:" + message);
                } catch (Exception ex) {
                    message = ex.getMessage();
                    //log statement
                    System.out.println(new java.util.Date() + ":" + message);

                }
                
                boolean isAsciiPrintable = org.apache.commons.lang.StringUtils.isAsciiPrintable (message);
                if (!isAsciiPrintable) {
                    System.out.println ("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                    request.setAttribute ("X-Kannel-Coding", "2");
                    if (request.getAttribute ("X-Kannel-Coding") != null)
                        System.out.println ("Request contains X-Kannel-Coding attribute");
                }
                out.println(message);
                return;
            }

            System.out.println(new java.util.Date() + ":" + msisdn + ":Checking if billing required for content...");
            // so noww we know the network and the subscriber, lets check if billing is actually required for this content.
            boolean billed = false;
            String pricePointString = is.getPricing();//retreive price point ID
            if (pricePointString == null || "".equals(pricePointString)) {
                pricePointString = "";
                billed = true;
                //logging statement
                System.out.println(new java.util.Date() + ":" + msisdn + ":No pricePoint found on content. Billing not required!");
            }

            //----------check for blank info. bypass billing if info is blank!-----------------------
            if (info == null || "".equals(info)) {
                billed = true;
                //set default message for blank info
                info = "No info is currently available for " + is.getServiceName() + ". Please try again later.";
                //logging statements
                System.out.println(new java.util.Date() + ": " + msisdn + ":No info currently available for:" + is.getServiceName() + "(" + is.getKeyword() + ")");
                if (pricePointString != null && !"".equals(pricePointString)) {
                    pricePointString = "";
                    System.out.println(new java.util.Date() + ": " + msisdn + ":Do not bill subscriber:" + msisdn);
                }
            }

            //bypass billing for pre-billed requests
            if (alreadyBilled != null && "1".equals(alreadyBilled)) {
                billed = true;
                pricePointString = "";
                System.out.println(new java.util.Date() + ": " + msisdn + ":Pre-billed request: Content already paid for. deliver content!");

            }

            //--------------do billing if content is priced-----------------
            if (!pricePointString.equals("")) {
                try {
                    System.out.println(new java.util.Date() + ": " + msisdn + ":About to bill...");
                    //price point was not submitted - find it
                    String[] itemPricePoints = pricePointString.split(",");

                    if ((itemPricePoints == null || itemPricePoints[0] == null || itemPricePoints[0].equals(""))) {
                        System.out.println(new java.util.Date() + ": " + msisdn + ":ERROR:BILLING_MECH_FAILURE:PricePoint Missing!");
                        throw new Exception(Feedback.BILLING_MECH_FAILURE);
                    }

                    PricePoint pricePoint = PaymentManager.viewPricePointFor(itemPricePoints, msisdn);
                    String pricePointId = pricePoint.getPricePointId();
                    System.out.println(new java.util.Date() + ":pricePoint ID:" + pricePointId);

                    //throw exception if price point not found
                    if (pricePointId == null || "".equals(pricePointId)) {
                        System.out.println(new java.util.Date() + ": " + msisdn + ":ERROR: Invalid pricePointID(s):" + pricePointString);
                        billed = true;
                        //throw exception
                        throw new Exception(Feedback.BILLING_MECH_FAILURE);
                    }
                    /* retry implemented inside initiatePayment:doOTBilling()
                    int count = 0;
                    while(!billed && count < NUM_OF_RETRIES) {
                    billed = PaymentManager.initiatePayment(pricePoint, cnxn, msisdn, "", "");
                    //pause for 1 second
                    Thread.sleep(MILLISECONDS_BETWEEN_RETRY);
                    count++;
                    }*/
                    System.out.println(new java.util.Date() + ": " + kw + ": " + msisdn + ": Entering initiatePayment from sendinfo");

                    String transactionId = "";
                    if (pricePoint.getBillingMech().equals(PaymentManager.AIRTEL_BILL)) {
                        transactionId = com.rancard.common.uidGen.genUID("", 5);
                    } else {
                        if (siteType.equals(CPSite.WAP) || siteType.equals(CPSite.WEB)) {
                            transactionId = com.rancard.common.uidGen.genUID("MP-", 5);
                        } else {
                            transactionId = com.rancard.common.uidGen.genUID("OD-", 5);
                        }
                    }

                    //-----insert transacton------------
                    //URL used to complete the transaction after billing has been completed
                    String completeTransnxnUrl = fullContextPath + "/sendinfo_push.jsp?msisdn=" + com.rancard.util.URLUTF8Encoder.encode(msisdn)
                            + "&keyword=" + kw.toUpperCase() + "&alert_count=" + is.getMsgId() + "&dest=" + com.rancard.util.URLUTF8Encoder.encode(shortcode)
                            + "&siteId=" + siteId + "&transId=" + transactionId;

                    if (pricePoint.getBillingMech().equals(PaymentManager.OT_BILL)) {
                        completeTransnxnUrl = completeTransnxnUrl + "&sender=KEYWORD";
                    }

                    UserServiceTransaction trans = new UserServiceTransaction();
                    trans.setAccountId(provId);

                    trans.setDate(new java.sql.Timestamp(new java.util.Date().getTime()));
                    trans.setKeyword(kw.toUpperCase());
                    trans.setMsg("on-demand");
                    trans.setMsisdn(msisdn);
                    trans.setPricePoint(pricePointId);
                    trans.setTransactionId(transactionId);
                    trans.setIsBilled(0);
                    trans.setIsCompleted(0);
                    trans.setCallBackUrl(completeTransnxnUrl);

                    boolean transactionCreated = false;
                    int isCompleted = 0;

                    try {
                        ServiceManager.createTransaction(trans);
                        System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " created!");
                        transactionCreated = true;
                    } catch (Exception e) {
                        transactionCreated = false;
                        System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " NOT created! Error message: "
                                + e.getMessage());
                    }

                    if (transactionCreated) {
                        try {
                            billed = PaymentManager.initiatePayment(pricePoint, cnxn, msisdn, transactionId, "", completeTransnxnUrl, shortcode, trans.getKeyword ());
                        } catch (Exception e) {
                            if (e.getMessage().equals("READ_TIMEOUT")) {
                                message = "We've received your request for a " + is.getServiceName() + " item. Please be patient while we process it.";
                                skipMessagingFromCallBack = false;
                                isCompleted = 0;
                            } else if (e.getMessage().equals("INSUFFICIENT_CREDIT")) {
                                message = "We couldn't complete purchase of the " + is.getServiceName() + " item you requested. Please top up and then send "
                                        + is.getKeyword().toUpperCase() + " to " + shortcode;
                                skipMessagingFromCallBack = true;
                                isCompleted = 1;
                            } else if (e.getMessage().equals("ERROR")) {
                                message = "We couldn't complete purchase of the " + is.getServiceName() + " item you requested. Please try again. Send "
                                        + is.getKeyword().toUpperCase() + " to " + shortcode + ". You've not been billed.";
                                isCompleted = 1;
                                skipMessagingFromCallBack = true;
                            }
                        }

                        /*if (skipMessagingFromCallBack) {
                        completeTransnxnUrl = completeTransnxnUrl + "&push=FALSE";
                        } else {
                        completeTransnxnUrl = completeTransnxnUrl + "&push=TRUE";
                        }*/

                        request.setAttribute("x-kannel-header-binfo", transactionId);
                        System.out.println(new java.util.Date() + ": " + kw + ": " + msisdn + ": Completed initiatePayment from sendinfo with result: " + billed);

                        if (billed) {
                            trans.setIsBilled(1);
                            trans.setIsCompleted(1);
                            //updateOTTransaction(kw, provId, "on-demand", msisdn, pricePoint.getPricePointId(), 1, 1, transactionId);
                        } else {
                            trans.setIsBilled(0);
                            trans.setIsCompleted(isCompleted);
                            //updateOTTransaction(kw, provId, "on-demand", msisdn, pricePoint.getPricePointId(), 0, 0, transactionId);
                        }

                        try {
                            ServiceManager.updateTransaction(trans.getTransactionId(), trans.getIsCompleted(), trans.getIsBilled());
                            System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " updated!");
                        } catch (Exception e) {
                            System.out.println(new java.util.Date() + ":sendinfo: Transaction " + trans.getTransactionId() + " NOT updated! Error message: "
                                    + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    try {
                        if (siteType.equals(CPSite.SMS)) {
                            message = feedback.getUserFriendlyDescription(e.getMessage());
                        } else {
                            message = feedback.formDefaultMessage(e.getMessage());
                        }
                    } catch (Exception ex) {
                        message = ex.getMessage();
                    }
                    
                    boolean isAsciiPrintable = org.apache.commons.lang.StringUtils.isAsciiPrintable (message);
                    if (!isAsciiPrintable) {
                        System.out.println ("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                        request.setAttribute ("X-Kannel-Coding", "2");
                        if (request.getAttribute ("X-Kannel-Coding") != null)
                            System.out.println ("Request contains X-Kannel-Coding attribute");
                    }
                    out.println(message);
                    return;
                }
            } // end of price point check


            //logging statements
            compactInfo = info.replaceAll("\r\n", ".");
            System.out.println(new java.util.Date() + ":Destination number: " + msisdn);
            System.out.println(new java.util.Date() + ":Info: " + compactInfo);
            //end of logging


            System.out.println(new java.util.Date() + ": " + kw + ": " + msisdn + ": About to send content. billed: " + billed);
            if (billed) {
                if (override_msg == null || override_msg.equals("")) {
                    boolean isAsciiPrintable = org.apache.commons.lang.StringUtils.isAsciiPrintable (compactInfo);
                    if (!isAsciiPrintable) {
                        System.out.println ("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                        request.setAttribute ("X-Kannel-Coding", "2");
                        if (request.getAttribute ("X-Kannel-Coding") != null){
                            System.out.println ("Request contains X-Kannel-Coding attribute");
                        }
                    }
                    
                    try {
                        compactInfo = VMServiceManager.embedShareLink (provId, kw, compactInfo, msisdn);
                    } catch (Exception e) {
                    }
                    
                    out.println(compactInfo);
                    //request.setAttribute("dfltMsg", info);
                } else {
                    boolean isAsciiPrintable = org.apache.commons.lang.StringUtils.isAsciiPrintable (override_msg);
                    if (!isAsciiPrintable) {
                        System.out.println ("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                        request.setAttribute ("X-Kannel-Coding", "2");
                        if (request.getAttribute ("X-Kannel-Coding") != null){
                            System.out.println ("Request contains X-Kannel-Coding attribute");
                        }
                    }
                    out.println(override_msg);
                    //request.setAttribute("dfltMsg", override_msg);
                }
            } else {
                //out.println(feedback.getUserFriendlyDescription(Feedback.INSUFFICIENT_CREDIT_ON_PIN));
                boolean isAsciiPrintable = org.apache.commons.lang.StringUtils.isAsciiPrintable (message);
                if (!isAsciiPrintable) {
                    System.out.println ("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                    request.setAttribute ("X-Kannel-Coding", "2");
                    if (request.getAttribute ("X-Kannel-Coding") != null){
                        System.out.println ("Request contains X-Kannel-Coding attribute");
                    }
                }
                out.println(message);
            }

            //log request; temporarily disabled info request logging
            /*
            try{
            String shortcode = request.getParameter("dest");
            InfoService.logInfoRequest(is.getOwnerId(), info, is.getAccountId(),
            is.getKeyword(), msisdn, shortcode);

            }catch(Exception ex){
            System.out.println(new java.util.Date()+":@sendinfo:Error logging request:"+ ex.getMessage());

            }*/

            //set service_usage_log extra params
            request.setAttribute("log_thirdPartyCPId", is.getOwnerId());

            //free memory
            is = null;

            return;
        } else {
            try {
                if (siteType.equals(CPSite.SMS)) {
                    message = feedback.getUserFriendlyDescription(Feedback.MISSING_INVALID_MSISDN);
                } else {
                    message = feedback.formDefaultMessage(Feedback.MISSING_INVALID_MSISDN);
                }
                //log statement
                System.out.println(new java.util.Date() + ":MISSING_INVALID_MSISDN: " + message);

            } catch (Exception ex) {
                message = ex.getMessage();
                //log statement
                System.out.println(new java.util.Date() + ":error: " + message);
            }

            boolean isAsciiPrintable = org.apache.commons.lang.StringUtils.isAsciiPrintable (message);
            if (!isAsciiPrintable) {
                System.out.println ("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                request.setAttribute ("X-Kannel-Coding", "2");
                if (request.getAttribute ("X-Kannel-Coding") != null)
                    System.out.println ("Request contains X-Kannel-Coding attribute");
            }
            out.println(message);
            return;
            //do message push - Alert
            /*try {
            System.out.println ("Info: " + compactInfo);
            HashMap groupedSubscribers = ServiceManager.viewSubscribersGroupByNetworkPrefix (provId, kw, 1);
            Object[] keys = groupedSubscribers.keySet ().toArray ();
            for (int i = 0; i < keys.length; i++) {
            String[] subscribers = (String[]) groupedSubscribers.get (keys[i].toString ());
            if (subscribers != null && subscribers.length > 0) {
            //get connection to requesting network for this provider
            try {
            cnxn = CPConnections.getConnection (provId, subscribers[0]);
            } catch (Exception e) {
            throw new Exception (Feedback.UNSUPPORTED_NETWORK);
            }

            try{
            Driver.getDriver (cnxn.getDriverType (), cnxn.getGatewayURL ()).sendSMSTextMessage (subscribers, from, info, cnxn.getUsername (),
            cnxn.getPassword (), cnxn.getConnection (), "", "0");
            }catch(Exception e){
            System.out.println (Feedback.TRANSPORT_ERROR + " Response from gateway: " + e.getMessage ());
            throw new Exception (Feedback.TRANSPORT_ERROR);
            }
            } else {
            System.out.println ("No subscribers found for " + keys[i].toString ());
            }
            is = null;
            }
            } catch (Exception e) {
            out.println (e.getMessage ().substring (e.getMessage ().indexOf (":") + 1));
            return ;
            }*/
        }
    }

    //Process the HTTP Post request
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws
            ServletException, IOException {
        doGet(request, response);
    }

    //Clean up resources
    public void destroy() {
    }

    public void forward(ServletRequest request,
            ServletResponse response) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        doGet(req, resp);
    }

    public void include(ServletRequest request,
            ServletResponse response) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        doGet(req, resp);
    }

    public void updateOTTransaction(String kw, String provId, String alertCount, String msisdn, String pricePointId, int isBilled, int isCompleted, String transId) {
        String svrAddr = "";
        String transactionId = transId;
        UserServiceTransaction trans = null;


        //creat transaction
        trans = new UserServiceTransaction();
        svrAddr = "http://msg.rancardmobility.com:8080/ot.rms/sendsms?to=%2b2000&text=" + kw + "&conn=OT:5511&username=otsms&password=o1t1s1m1s1&serviceId=&price=0&from=" + com.rancard.util.URLUTF8Encoder.encode(msisdn);
        //transactionId = com.rancard.common.uidGen.getUId();
        trans.setAccountId(provId);
        trans.setKeyword(kw);
        trans.setMsg(alertCount);
        trans.setMsisdn(msisdn);
        trans.setTransactionId(transactionId);
        trans.setCallBackUrl(svrAddr);
        trans.setPricePoint(pricePointId);
        trans.setIsBilled(isBilled);
        trans.setIsCompleted(isCompleted);

        System.out.println(new java.util.Date() + ":updating transaction for OT:transId=" + transactionId + ":msisdn=" + msisdn + ":keyword=" + kw + ":billed=" + isBilled + ":completed=" + isCompleted);
        try {
            ServiceManager.createTransaction(trans);
        } catch (Exception e) {
            System.out.println(new java.util.Date() + ":sendinfo:error updating OTTransaction:" + e.getMessage());

        }

    }
}
