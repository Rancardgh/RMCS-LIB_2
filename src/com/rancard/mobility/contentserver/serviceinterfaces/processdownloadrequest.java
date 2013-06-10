package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.mobility.contentserver.*;
import com.rancard.mobility.infoserver.common.services.UserService;
import com.rancard.util.payment.PaymentManager;
import com.rancard.util.payment.PricePoint;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import com.rancard.common.Feedback;
import com.rancard.mobility.common.FonCapabilityMtrx;
import com.rancard.mobility.infoserver.common.services.ServiceManager;
import java.util.HashMap;
import java.util.StringTokenizer;

public class processdownloadrequest extends HttpServlet implements RequestDispatcher {

    //static variables
    private static final String PIN_OPTION = "pinredemption";
    private static final String SHORTCODE_OPTION = "shortcode";

    //Initialize global variables
    public void init() throws ServletException {
    }

    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String itemName = "";
        String message = "";

        //get path to phone DB
        String svrAddr = this.getServletContext().getInitParameter("contentServerUrl");

        //get reeponse writer
        PrintWriter out = response.getWriter();
        PricePoint pricePoint = new PricePoint();

        String transaction = request.getParameter("msg");
        String keyword = request.getParameter("keyword");
        String msisdn = request.getParameter("msisdn");
        String phoneId = request.getParameter("phoneId");
        String ua = request.getParameter("ua");
        String siteId = request.getParameter("siteId");
        String ack = (String) request.getAttribute("ack");
        String regId = request.getParameter("regId");
        String pricePointId = request.getParameter("pricePoint");
        String siteType = (String) request.getAttribute("site_type");
        String lang = (String) request.getAttribute("default_lang");
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

        String id = new String();
        String listId = new String();
        String pin = new String();
        String responsePath = new String();
        if (ack == null) {
            ack = "";
        }
        if (regId == null) {
            regId = "";
        }
        if (phoneId == null) {
            phoneId = "";
        }
        if (pricePointId == null) {
            pricePointId = "";
        }

        //get item reference
        if (transaction == null || transaction.equals("")) {
            try {
                if (siteType.equals(CPSite.SMS)) {
                    message = feedback.getUserFriendlyDescription(Feedback.MISSING_INVALID_ITEM_REF);
                } else {
                    message = feedback.formDefaultMessage(Feedback.MISSING_INVALID_ITEM_REF);
                }
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            out.println(message);
            return;
        } else {
            String[] rqst = transaction.split("-");
            if (rqst.length == 1) {
                listId = (String) request.getAttribute("acctId");
                try {
                    id = RepositoryManager.resolveShortItemReference(rqst[0], listId, keyword);
                } catch (Exception ex) {
                }
            } else if (rqst.length == 2) {
                id = rqst[0];
                listId = rqst[1];
            }


            /*
            StringTokenizer st = new StringTokenizer (transaction, "-");

            //get parameters
            if(st.countTokens () == 0 || st.countTokens () == 1){
            try {
            if(siteType.equals (CPSite.SMS)){
            message = feedback.getUserFriendlyDescription (Feedback.MISSING_INVALID_ITEM_REF);
            }else{
            message = feedback.formDefaultMessage (Feedback.MISSING_INVALID_ITEM_REF);
            }
            }catch (Exception ex) {
            message = ex.getMessage ();
            }
            out.println (message);
            return;
            }else if(st.countTokens () == 2) {
            id = st.nextToken ();
            listId = st.nextToken ();
            }else if(st.countTokens () >= 3){
            id = st.nextToken ();
            listId = st.nextToken ();
            pin = st.nextToken ();
            }

            st = null;
             **/
        }

        //no msisdn found
        if (msisdn == null || msisdn.equals("")) {
            try {
                if (siteType.equals(CPSite.SMS)) {
                    message = feedback.getUserFriendlyDescription(Feedback.MISSING_INVALID_MSISDN);
                } else {
                    message = feedback.formDefaultMessage(Feedback.MISSING_INVALID_MSISDN);
                }
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            out.println(message);
            return;
        }

        //logging statements
        System.out.println("Received request to download content");
        System.out.println("Date received: " + java.util.Calendar.getInstance().getTime().toString());
        System.out.println("Request: " + keyword + " " + transaction);
        System.out.println("User's number: " + msisdn);
        //end of logging

        //get base URL for content server
        String s = request.getProtocol().toLowerCase();
        s = s.substring(0, s.indexOf("/")).toLowerCase();
        String baseUrl = s + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

        ContentItem item = null;
        Format format = null;
        CPConnections cnxn = null;
        CPSite initSite = null;

        //authenticate site
        try {
            initSite = CPSite.viewSite(siteId);
            System.out.println("Requesting site: " + initSite.getCpSiteName());
            if (initSite.getCpId() == null || initSite.getCpId().equals("")) {
                throw new Exception();
            }
        } catch (Exception ex) {
            try {
                if (siteType.equals(CPSite.SMS)) {
                    message = feedback.getUserFriendlyDescription(Feedback.INVALID_REQUEST_CREDENTIALS);
                } else {
                    message = feedback.formDefaultMessage(Feedback.INVALID_REQUEST_CREDENTIALS);
                }
            } catch (Exception e) {
                message = ex.getMessage();
            }
            if (responsePath != null && !responsePath.equals("") && !responsePath.equalsIgnoreCase("null")) {
                response.sendRedirect(responsePath + "?reply=" + message);
            } else {
                out.println(message);
            }
            return;
        }

        //reference requested content item
        try {
            item = new ContentItem().viewContentItem(id, listId);
            System.out.println("Content item's title is: " + item.gettitle());
            System.out.println("Content MIME type is: " + item.getFormat().getMimeType());
            //end of logging
            format = item.getFormat();

            if ((item.getid() == null) || (item.getid().equals(""))
                    && (item.getListId() != null) || (item.getListId().equals(""))) {
                throw new Exception();
            }
            itemName = item.gettitle();
        } catch (Exception e) {
            try {
                if (siteType.equals(CPSite.SMS)) {
                    message = feedback.getUserFriendlyDescription(Feedback.MISSING_INVALID_ITEM_REF);
                } else {
                    message = feedback.formDefaultMessage(Feedback.MISSING_INVALID_ITEM_REF);
                }
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            if (responsePath != null && !responsePath.equals("") && !responsePath.equalsIgnoreCase("null")) {
                response.sendRedirect(responsePath + "?reply=" + message);
            } else {
                out.println(message);
            }
            return;
        }

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
            try {
                if (siteType.equals(CPSite.SMS)) {
                    message = feedback.getUserFriendlyDescription(Feedback.MISSING_INVALID_MSISDN);
                } else {
                    message = feedback.formDefaultMessage(Feedback.MISSING_INVALID_MSISDN);
                }
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            if (responsePath != null && !responsePath.equals("") && !responsePath.equalsIgnoreCase("null")) {
                response.sendRedirect(responsePath + "?reply=" + message);
            } else {
                out.println(message);
            }
            return;
        }

        if (initSite.getCheckUser() == true) {
            boolean check = false;
            if (regId != null && !regId.equals("")) {
                try {
                    check = ServiceManager.verifyUser(msisdn, regId, initSite.getCpId(), keyword);
                } catch (Exception e) {
                    check = false;
                }
                if (check == false) {
                    try {
                        if (siteType.equals(CPSite.SMS)) {
                            message = feedback.getUserFriendlyDescription(Feedback.NOT_REGISTERED);
                        } else {
                            message = feedback.formDefaultMessage(Feedback.NOT_REGISTERED);
                        }
                    } catch (Exception ex) {
                        message = ex.getMessage();
                    }
                    if (responsePath != null && !responsePath.equals("") && !responsePath.equalsIgnoreCase("null")) {
                        response.sendRedirect(responsePath + "?reply=" + message);
                    } else {
                        out.println(message);
                    }
                    return;
                }
            } else {
                try {
                    if (siteType.equals(CPSite.SMS)) {
                        message = feedback.getUserFriendlyDescription(Feedback.MISSING_INVALID_REGISTRATION_ID);
                    } else {
                        message = feedback.formDefaultMessage(Feedback.MISSING_INVALID_REGISTRATION_ID);
                    }
                } catch (Exception ex) {
                    message = ex.getMessage();
                }
                if (responsePath != null && !responsePath.equals("") && !responsePath.equalsIgnoreCase("null")) {
                    response.sendRedirect(responsePath + "?reply=" + message);
                } else {
                    out.println(message);
                }
                return;
            }
        }

        //check price point
        try {
            if (pricePointId.equals("")) {
                //price point was not submitted - find it
                String[] itemPricePoints = item.getPrice().split(",");
                if (itemPricePoints == null || itemPricePoints[0] == null || itemPricePoints[0].equals("")) {
                    UserService thisService = (UserService) request.getAttribute("thisService");
                    if (thisService != null) {
                        itemPricePoints = thisService.getPricing().split(",");
                    }
                    thisService = null;
                }

                if ((itemPricePoints == null || itemPricePoints[0] == null || itemPricePoints[0].equals("")) && item.isFree() != true) {
                    throw new Exception(Feedback.BILLING_MECH_FAILURE);
                }

                pricePoint = PaymentManager.viewPricePointFor(itemPricePoints, msisdn);
                pricePointId = pricePoint.getPricePointId();
            } else {
                String[] itemPricePoints = new String[1];
                itemPricePoints[0] = pricePointId;
                pricePoint = PaymentManager.viewPricePointFor(itemPricePoints, msisdn);
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
            out.println(message);
            return;
        }

        //get connections for content subscriber
        try {
            cnxn = CPConnections.getConnection(initSite.getCpId(), msisdn);
            System.out.println("Connection for Messaging: " + cnxn.getConnection());
        } catch (Exception e) {
            try {
                if (siteType.equals(CPSite.SMS)) {
                    message = feedback.getUserFriendlyDescription(Feedback.UNSUPPORTED_NETWORK);
                } else {
                    message = feedback.formDefaultMessage(Feedback.UNSUPPORTED_NETWORK);
                }
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            out.println(message);
            return;
        }

        //phone ID has been supplied. check compatibility with chosen format
        FonCapabilityMtrx fcm = null;
        if ((ua != null && !ua.equals("")) || (phoneId != null && !phoneId.equals(""))) {
            try {
                System.out.println("Setting up compatibility matrix");
                fcm = (FonCapabilityMtrx) this.getServletContext().getAttribute("capabilitiesMtrx");
                if (fcm == null) {
                    throw new Exception();
                }
            } catch (Exception e) {
                //logging statements
                System.out.println("Setup of compatibility matrix FAILED");
                //end of logging

                try {
                    if (siteType.equals(CPSite.SMS)) {
                        message = feedback.getUserFriendlyDescription(Feedback.PHONE_MATRIX_INIT_ERROR);
                    } else {
                        message = feedback.formDefaultMessage(Feedback.PHONE_MATRIX_INIT_ERROR);
                    }
                } catch (Exception ex) {
                    message = ex.getMessage();
                }
                out.println(message);
                return;
            }
        }

        if (ua != null && !ua.equals("")) {
            phoneId = fcm.getUAManager().getDeviceIDFromUA(ua);
        }
        if (!phoneId.equals("") && !phoneId.equalsIgnoreCase("null")) {
            //logging statements
            System.out.println("Phone ID was supplied. Checking compatibility");
            //end of logging
            String capabilityValue = "false"; //default condition
            try {

                if (format.getPushBearer().equals("SMS")) {
                    capabilityValue = "true";
                } else {
                    //get capability name for the file extension
                    String capability = fcm.findSupportedCapability(format.getFileExt());
                    capabilityValue = fcm.getCapabilitiesManager().getCapabilityForDevice(phoneId, capability);
                    System.out.println("Capability value for " + capability + " is: " + capabilityValue);

                    //check compatibility
                    if (!capabilityValue.equals("true")) {
                        throw new Exception(Feedback.PHONE_FORMAT_INCOMPATIBILITY);
                    } else {
                        System.out.println("Phone supports requested format");
                    }

                    //check support for WAP push
                    if (format.getPushBearer().equals("WAP")) {
                        System.out.println("Checking if phone can receive a WAP push...");
                        if (!fcm.getCapabilitiesManager().getCapabilityForDevice(phoneId, "wap_push_support").equals("true")) {
                            System.out.println("Phone cannot support WAP push");
                            throw new Exception(Feedback.PUSH_PROTOCOL_NOT_SUPPORTED);
                        } else {
                            System.out.println("Phone supports WAP push");
                        }
                    }
                }
            } catch (NullPointerException npe) {
                try {
                    if (siteType.equals(CPSite.SMS)) {
                        message = feedback.getUserFriendlyDescription(Feedback.MISSING_INVALID_PHONE_ID);
                    } else {
                        message = feedback.formDefaultMessage(Feedback.MISSING_INVALID_PHONE_ID);
                    }
                } catch (Exception ex) {
                    message = ex.getMessage();
                }
                if (responsePath != null && !responsePath.equals("") && !responsePath.equalsIgnoreCase("null")) {
                    response.sendRedirect(responsePath + "?reply=" + message);
                } else {
                    out.println(message);
                }
                return;
            } catch (Exception e) {
                try {
                    if (siteType.equals(CPSite.SMS)) {
                        message = feedback.getUserFriendlyDescription(e.getMessage());
                    } else {
                        message = feedback.formDefaultMessage(e.getMessage());
                    }
                    if (message == null) {
                        message = e.getMessage();
                    }
                } catch (Exception ex) {
                    message = ex.getMessage();
                }
                if (responsePath != null && !responsePath.equals("") && !responsePath.equalsIgnoreCase("null")) {
                    response.sendRedirect(responsePath + "?reply=" + message);
                } else {
                    out.println(message);
                }
                return;
            }
        } else {
            phoneId = "";
        }

        //check if cpuser's bandwidth is exceeded
        com.rancard.mobility.contentprovider.User user = new com.rancard.mobility.contentprovider.User();
        System.out.println("Checking bandwidth availability for " + initSite.getCpSiteName());
        try {
            user = user.viewDealer(initSite.getCpId());

            if (user != null) {
                double bandwidth = user.getBandwidthBalance();
                if (bandwidth - item.getSize() < 0) {
                    throw new Exception(Feedback.BANDWIDTH_EXCEEDED);
                } else {
                    System.out.println("Site has enough bandwidth");
                    //double result = bandwidth - item.getSize ();
                    //user.updateDealer (user.getId (), result, user.getInboxBalance (), user.getOutboxBalance ());
                }
            } else {
                throw new Exception(Feedback.MISSING_INVALID_PROV_ID);
            }
        } catch (Exception e) {
            try {
                if (siteType.equals(CPSite.SMS)) {
                    message = feedback.getUserFriendlyDescription(e.getMessage());
                } else {
                    message = feedback.formDefaultMessage(e.getMessage());
                }
                if (message == null) {
                    message = e.getMessage();
                }
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            if (responsePath != null && !responsePath.equals("") && !responsePath.equalsIgnoreCase("null")) {
                response.sendRedirect(responsePath + "?reply=" + message);
            } else {
                out.println(message);
            }
            return;
        }

        //log transaction
        if (pin.equalsIgnoreCase("null")) {
            pin = "";
        }
        Transaction download = new Transaction("", id, listId, msisdn, phoneId, false, pin, siteId, false, keyword, false, pricePointId);

        //logging statements
        System.out.println("Logging transaction...");
        //end of logging
        boolean isLogged = false;
        String ticket = null;
        while (!isLogged) {
            ticket = log(10, download, "" + format.getId(), keyword);
            if (ticket == null) {
                //logging statements
                System.out.println("Logging transaction FAILED...");
                //end of logging
                isLogged = false;
            } else {
                //logging statements
                System.out.println("Logging transaction COMPLETED...");
                //end of logging
                isLogged = true;
            }
        }

        try {
            //perform billing
            //logging statements
            System.out.println("Billing...");
            //end of logging
            if (isLogged) {
                svrAddr = svrAddr + "initiatedownload?ticketId=" + download.getTicketID();
                com.rancard.mobility.infoserver.common.services.UserServiceTransaction trans = new com.rancard.mobility.infoserver.common.services.UserServiceTransaction();
                trans.setAccountId(cnxn.getProviderId());
                trans.setKeyword(keyword);
                trans.setMsg(transaction);
                trans.setMsisdn(msisdn);
                trans.setTransactionId(download.getTicketID());
                trans.setCallBackUrl(svrAddr);
                trans.setPricePoint(pricePointId);


                boolean billed = false;
                //String network = cnxn.getAllowedNetworks ().get (0).toString ();
                //check if billing can be done
                if (!item.isFree()) {
                    System.out.println("Item is not free...");

                    //Create CDR Hash
                    HashMap cdrHash = new HashMap();
                    cdrHash.put("transaction_type", "download_" + item.gettitle());
                    cdrHash.put("cost", pricePoint.getCurrency() + " " + pricePoint.getValue());
                    cdrHash.put("service_code", "");
                    cdrHash.put("aparty", download.getSubscriberMSISDN());
                    cdrHash.put("bparty", "");
                    cdrHash.put("provider_id", item.getProviderDetails().getName());

                    try {
                        billed = PaymentManager.initiatePayment(pricePoint, cnxn, msisdn, ticket, pin, cdrHash);
                        //billed = true;//KB: restore line above after testing
                    } catch (Exception e) {
                        if (e.getMessage().equals("READ_TIMEOUT")) {
                            message = "We've received your request. Please be patient while we process it.";
                            trans.setIsBilled(0);
                        } else if (e.getMessage().equals("INSUFFICIENT_CREDIT")) {
                            message = "We couldn't complete your purchase. Please top up and then try again";
                            trans.setIsBilled(0);
                        } else if (e.getMessage().equals("ERROR")) {
                            message = "We couldn't complete your purchase. Please try again. You've not been billed.";
                            trans.setIsBilled(0);
                        }
                    }

                    if (billed == true && (pricePoint.getBillingMech().equals(PaymentManager.OT_BILL) || pricePoint.getBillingMech().equals(PaymentManager.PIN_REDEMPTION))) {
                        //mark transacction as billed
                        trans.setIsBilled(1);
                        com.rancard.mobility.infoserver.common.services.ServiceManager.createTransaction(trans);

                        System.out.println("billing successful. Initiating download....");

                        RequestDispatcher dispatch = null;
                        request.setAttribute("ticketId", ticket);
                        try {
                            dispatch = request.getRequestDispatcher("initiatedownload");
                        } catch (Exception e) {
                            throw new Exception(Feedback.ROUTE_NOTIFICATION_FAILED);
                        }

                        dispatch.include(request, response);
                    } else {
                        com.rancard.mobility.infoserver.common.services.ServiceManager.createTransaction(trans);
                    }
                    /*if(cnxn.getBillingMech ().equals (BillingManager.SHORTCODE_OPTION)){
                    //do billing using SMSC
                    System.out.println ("do billing using SMSC...");
                    billed = BillingManager.doShortCodeBilling ("" + item.getContentTypeDetails ().getServiceType (), cnxn, network, msisdn, ticket, download.getKeyword (), item.getListId ());
                    }else if(cnxn.getBillingMech ().equals (BillingManager.PIN_OPTION)) {
                    //deduct from voucher value
                    System.out.println ("deduct from voucher value...");
                    billed = BillingManager.doPinRedemption ("" + item.getContentTypeDetails ().getServiceType (), item.getListId (),  network, pin, download.getKeyword ());
                    if(billed){
                    //mark transacction as billed
                    com.rancard.mobility.infoserver.common.services.ServiceManager.updateTransaction (download.getTicketID (), 0, 1);

                    System.out.println ("billing successful. Initiating download....");

                    RequestDispatcher dispatch = null;
                    request.setAttribute ("ticketId", ticket);
                    try{
                    dispatch = request.getRequestDispatcher ("initiatedownload");
                    }catch(Exception e){
                    throw new Exception (Feedback.ROUTE_NOTIFICATION_FAILED);
                    }

                    dispatch.include (request, response);
                    }else{
                    System.out.println ("billing ws NOT successful");
                    }
                    } else if (cnxn.getBillingMech ().equals (BillingManager.OT_BILLING)) {
                    System.out.println ("deduct from user's balance...");
                    billed = BillingManager.doOTBilling ("" + item.getContentTypeDetails ().getServiceType (), cnxn, network, msisdn, download.getKeyword (), item.getListId ());

                    if(billed){
                    //mark transacction as billed
                    com.rancard.mobility.infoserver.common.services.ServiceManager.updateTransaction (download.getTicketID (), 0, 1);

                    System.out.println ("billing successful. Initiating download....");

                    RequestDispatcher dispatch = null;
                    request.setAttribute ("ticketId", ticket);
                    try{
                    dispatch = request.getRequestDispatcher ("initiatedownload");
                    }catch(Exception e){
                    throw new Exception (Feedback.ROUTE_NOTIFICATION_FAILED);
                    }

                    dispatch.include (request, response);
                    }
                    }*/

                } else {
                    System.out.println("Item is free...");
                    billed = true;

                    //mark transacction as billed
                    //com.rancard.mobility.infoserver.common.services.ServiceManager.updateTransaction(download.getTicketID(), 0, 1);
                    trans.setIsBilled(1);
                    com.rancard.mobility.infoserver.common.services.ServiceManager.createTransaction(trans);

                    RequestDispatcher dispatch = null;
                    // check the site type and if request is from a wap site
                    try {
                        if (siteType.equals(CPSite.WAP)) {
                            request.setAttribute("siteType", "WAP");
                        }
                    } catch (Exception ex) {
                        System.out.println(new java.util.Date() + ":WARNING unable to determine site type will default to using WAP push to deliver message ");
                    }
                    // pass information to initiate download for immediate downloaf
                    request.setAttribute("ticketId", ticket);
                    //
                    try {
                        dispatch = request.getRequestDispatcher("initiatedownload");
                    } catch (Exception e) {
                        throw new Exception(Feedback.ROUTE_NOTIFICATION_FAILED);
                    }

                    dispatch.include(request, response);
                }

                //check if billed
                if (!billed) {
                    System.out.println(new java.util.Date() + ": billing was NOT successful");
                    throw new Exception(message);
                }
            } else {
                throw new Exception(Feedback.GENERIC_ERROR);
            }

            String insertions = "ack=" + ack + "&keyword=" + keyword + "&msg=" + transaction + "&msisdn=" + msisdn + "&itemName=" + itemName + "&transId=" + download.getTicketID();
            ack = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, ack);

            try {
                if (siteType.equals(CPSite.SMS)) {
                    message = feedback.getUserFriendlyDescription(Feedback.DOWNLOAD_PROCESSED) + " " + ack;
                } else {
                    message = feedback.getType(Feedback.DOWNLOAD_PROCESSED) + ":" + download.getTicketID();
                }
            } catch (Exception ex) {
                message = ack;
            }
            if (responsePath != null && !responsePath.equals("") && !responsePath.equalsIgnoreCase("null")) {
                response.sendRedirect(responsePath + "?reply=" + message);
            } else {
                out.println(message);
            }
            return;

            //out.println (Feedback.DOWNLOAD_PROCESSED + ack + " Transaction ID: " + download.getTicketID ());
        } catch (Exception e) {
            /*try {
            com.rancard.mobility.infoserver.common.services.ServiceManager.deleteTransaction (ticket);
            download.removeTransaction (ticket);
            } catch (Exception ex) {
            }*/
            try {
                if (siteType.equals(CPSite.SMS)) {
                    message = feedback.getUserFriendlyDescription(e.getMessage());
                } else {
                    message = message;
                }
                if (message == null) {
                    message = e.getMessage();
                }
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            if (responsePath != null && !responsePath.equals("") && !responsePath.equalsIgnoreCase("null")) {
                response.sendRedirect(responsePath + "?reply=" + message);
            } else {
                out.println(message);
            }
            return;
        } finally {
            //help garbage collector
            cnxn = null;
            download = null;
            format = null;
            initSite = null;
            item = null;
            out = null;
            user = null;
            //end of garbage collection
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

    private void initiateDownload(String baseUrl, String ticket) throws Exception {
        java.io.BufferedReader br = null;
        java.net.URL url = null;
        try {
            //response.sendRedirect(srvcUrl);
            url = new java.net.URL(baseUrl + "initiatedownload?ticketId=" + ticket);
            //logging statements
            System.out.println(" Initiate download URL: " + url);
            //end of logging
            br = new java.io.BufferedReader(new InputStreamReader(url.openStream()));

            String temp = br.readLine();
            String resp = new String();

            while (temp != null) {
                resp = resp + temp;
                temp = br.readLine();
            }

            br.close();
        } catch (IOException ex2) {
            System.out.println("Error: " + ex2.getMessage());
            throw new Exception(Feedback.ROUTE_NOTIFICATION_FAILED);
        } finally {
            url = null;
            br = null;
        }
    }

    // accomplishes logging of transaction
    public String log(int idLength, Transaction dlb, String formatID, String keyword) {
        String ticket = null;
        dlb.setTicketID(com.rancard.common.uidGen.getUId() + "-" + formatID);
        try {
            //dlb.logTransaction (dlb.getTicketID (), dlb.getID (), dlb.getListID (),dlb.getSubscriberMSISDN (), dlb.getPhoneId (), false, dlb.getPin (), dlb.getSiteId (), false, keyword);
            dlb.logTransaction(dlb.getTicketID(), dlb.getID(), dlb.getListID(), dlb.getSubscriberMSISDN(), dlb.getPhoneId(), false, dlb.getPin(), dlb.getSiteId(), keyword);
            ticket = dlb.getTicketID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticket;
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
}
