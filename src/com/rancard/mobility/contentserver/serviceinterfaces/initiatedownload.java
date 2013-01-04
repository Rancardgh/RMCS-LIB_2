package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.mobility.contentserver.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import com.rancard.common.Feedback;
import com.rancard.mobility.common.Driver;
import net.sourceforge.wurfl.wurflapi.*;
import com.rancard.mobility.common.FonCapabilityMtrx;
import java.util.StringTokenizer;

public class initiatedownload extends HttpServlet implements RequestDispatcher {

    private static final String FROM = "RMCS";

    //Initialize global variables
    public void init() throws ServletException {
    }

    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        //get reeponse writer
        PrintWriter out = response.getWriter();
        int isBilled = 1;
        int isDelivered = 0;

        /// create filter which stores the user agent header in the code and uses
        String siteType = (String) request.getAttribute("siteType");

        /// check device capabiliites and determine best type ofbearer to use.

        /// in the event that a bearer is unavailable  look for bearer


        String transactionId = (String) request.getAttribute("ticketId");
        if (transactionId == null) {
            transactionId = request.getParameter("ticketId");
        }
        Transaction transaction = new Transaction();

        //logging statements
        System.out.println("Received request to initiate download with transaction ID: " + transactionId);
        System.out.println("Date received: " + java.util.Calendar.getInstance().getTime().toString());
        //end of logging

        if (transactionId == null || transactionId.equals("")) {
            System.out.println(Feedback.MISSING_INVALID_TICKET_ID); //send appropriate message
            return;
        } else {
            try {
                System.out.println("Looking up transaction details for transaction ID: " + transactionId + "...");
                transaction = Transaction.viewTransaction(transactionId);


            } catch (Exception e) {
                //logging statements
                System.out.println("Could not find transaction details for transaction ID: " + transactionId);
                System.out.println("Exception thrown carries message: " + e.getMessage());
                //end of logging
                System.out.println(Feedback.MISSING_INVALID_TICKET_ID); //send appropriate message
                return;
            }
        }

        //get base URL for content server from settings file

        String s = request.getProtocol().toLowerCase();
        s = s.substring(0, s.indexOf("/")).toLowerCase();
        //String baseUrl = s + "://" + request.getServerName () + ":" +
        //request.getServerPort () + request.getContextPath () +  "/";
        String baseUrl = this.getServletContext().getInitParameter("contentServerPublicURL");//

        ContentItem item = transaction.getContentItem();
        Format format = item.getFormat();
        CPConnections cnxn = null;

        System.out.println(new java.util.Date() + ":ContentItem: title=" + item.gettitle() + ", format=" + item.getFormat().getFileExt() + ", downloadURL=" + item.getDownloadUrl());

        String msisdn = transaction.getSubscriberMSISDN();
        //logging statements
        System.out.println("User's mobile number is: " + msisdn);
        //end of logging

        //get connections for content subscriber
        CPSite site = transaction.getSite();

        try {
            //logging statements
            System.out.println("Looking up available connections for content provider with ID: " + site.getCpId() + " ......");
            //end of logging
            cnxn = CPConnections.getConnection(site.getCpId(), msisdn);
        } catch (Exception e) {
            System.out.println(Feedback.UNSUPPORTED_NETWORK);
            return;
        }

        //route notification
        try {

            // if the request is comming via WAP .. then
            if ("WAP".equals(siteType)) {
                // call the initiate download interface directly here
                RequestDispatcher dispatch = null;
                request.setAttribute("ticketId", transactionId);
                try {
                    dispatch = request.getRequestDispatcher("downloadcontent");
                } catch (Exception e) {
                    throw new Exception(Feedback.ROUTE_NOTIFICATION_FAILED);
                }

                dispatch.include(request, response);

            } else if (this.routeNotification(item, msisdn, cnxn, transactionId, baseUrl, transaction.getKeyword())) {
                //logging statements
                System.out.println("Notification sent!!");
                //end of logging
                if (format.getPushBearer().equals("SMS") || format.getPushBearer().equals("SMS_BIN")) {
                    transaction.setDownloadCompleted(true);
                    //logging statements
                    System.out.println("Download Complete.");
                    //end of logging
                    Transaction.updateDownloadStatus(transactionId, true);
                    isDelivered = 1;
                } else if (format.getPushBearer().equals("WAP")) {
                    //Transaction.updateTransactionWithBilling (transactionId, true);
                    isDelivered = 1;
                }
            } else {
                //logging statements
                System.out.println("Routing notification FAILED.");
                //end of logging
                //Transaction.removeTransaction (transactionId);
                System.out.println(Feedback.ROUTE_NOTIFICATION_FAILED);
            }
        } catch (Exception e) {
            /*try {
            //Transaction.removeTransaction (transactionId);
            } catch (Exception ex) {
            ex.printStackTrace ();
            }*/
            System.out.println(e.getMessage());
            return;
        } finally {
            cnxn = null;
            format = null;
            item = null;
            out = null;
            site = null;
            transaction = null;
            try {
                com.rancard.mobility.infoserver.common.services.ServiceManager.updateTransaction(transactionId, isDelivered, isBilled);
            } catch (Exception e) {
            }
        }
    }

    //Process the HTTP Post request
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    //Clean up resources
    public void destroy() {
    }

    //sends WAP push notification
    public boolean sendWapPushNotification(ContentItem item, String number, CPConnections cnxn, String transactionId,
            String baseURL, String service) throws Exception {
        String response = new String();
        boolean flag = false;

        String url = baseURL + "downloadcontent?ticketId=" + transactionId;
        String text = "Downloading '" + item.gettitle() + "'...";//. Your download ID is " + transactionId;

        try {
            response = Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).
                    sendWAPPushMessage(number, FROM, text, url, cnxn.getUsername(), cnxn.getPassword(), cnxn.getConnection(),
                    service, "0");
        } catch (Exception e) {
            throw new Exception(Feedback.TRANSPORT_ERROR);
        }

        if (response.length() > 18 && response.substring(14, 18).equals("true")) {
            flag = true;
        }
        return flag;
    }

    //sends SMS notification
    public boolean sendSMSNotification(ContentItem item, String number, CPConnections cnxn, String transactionId,
            String baseURL, String service) throws Exception {
        boolean flag = false;
        String response = new String();
        String txt = new String();
        byte[] itemStream = null;

        if (!item.islocal()) {
            itemStream = RepositoryManager.getByteArray(item.getDownloadUrl());
        } else {
            com.rancard.mobility.contentserver.uploadsBean upload = new RepositoryManager().fetchFile(item.getListId(), item.getid());
            itemStream = upload.getDataStream();
        }
        txt = new String(itemStream);

        try {
            response = Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).
                    sendSMSTextMessage(number, FROM, txt, cnxn.getUsername(), cnxn.getPassword(), cnxn.getConnection(),
                    service, "0");
        } catch (Exception e) {
            throw new Exception(Feedback.TRANSPORT_ERROR);
        } finally {
            itemStream = null;
        }

        if (response.length() > 18 && response.substring(14, 18).equals("true")) {
            flag = true;
        }

        return flag;
    }

    //sends Binary SMS notification
    public boolean sendSMSBINNotification(ContentItem item, String number, CPConnections cnxn, String transactionId,
            String baseURL, String service) throws Exception {
        boolean flag = false;
        String format = item.getFormat().getFileExt();
        String response = new String();
        String txt = new String();
        String udh = new String();
        String cs = new String();

        txt = RepositoryManager.getUD(format, item);

        try {
            response = Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).
                    sendSMSBinaryMessage(number, FROM, txt, udh, cs, format, cnxn.getUsername(), cnxn.getPassword(),
                    cnxn.getConnection(), service, "0");
        } catch (Exception e) {
            throw new Exception(Feedback.TRANSPORT_ERROR);
        }

        if (response.length() > 18 && response.substring(14, 18).equals("true")) {
            flag = true;
        }

        return flag;
    }

//chooses most appropriate bearer for a particular notification for external files
    public boolean routeNotification(ContentItem item, String number, CPConnections cnxn, String transactionId,
            String baseURL, String service) throws Exception {
        boolean flag = true;
        Format format = item.getFormat();
        if (format.getPushBearer().equals("WAP")) {
            //logging statements
            System.out.println("Sending WAP push.....");
            //end of logging
            flag = sendWapPushNotification(item, number, cnxn, transactionId, baseURL, service);
        } else if (format.getPushBearer().equals("SMS")) {
            //logging statements
            System.out.println("Sending SMS.....");
            //end of logging
            flag = sendSMSNotification(item, number, cnxn, transactionId, baseURL, service);
        } else if (format.getPushBearer().equals("SMS_BIN")) {
            //logging statements
            System.out.println("Sending SMS with binary content.....");
            //end of logging
            flag = sendSMSBINNotification(item, number, cnxn, transactionId, baseURL, service);
        }
        return flag;
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
