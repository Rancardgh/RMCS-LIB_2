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
import com.rancard.mobility.infoserver.common.services.ServiceManager;
import com.rancard.mobility.infoserver.common.services.UserService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
import com.rancard.util.URLUTF8Encoder;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import java.io.*;
import java.util.ArrayList;
/**
 *
 * @author Messenger
 */
public class processunsubscriberequest extends HttpServlet implements RequestDispatcher {
    //Initialize global variables
    public void init() throws ServletException {
    }
    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get reeponse writer
        PrintWriter out = response.getWriter();
        CPConnections cnxn = new CPConnections();
        final String ACTION_UNSUBSCRIBE_KW = "1";
        final String ACTION_UNSUBSCRIBE_ALL = "2";
        String action = ACTION_UNSUBSCRIBE_KW;

        String ACK = (String) request.getAttribute("ack");
        String kw = request.getParameter("keyword");
        String msg = request.getParameter("msg");
        String msisdn = request.getParameter("msisdn");
        String provId = (String) request.getAttribute("acctId");

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

        String provName = "";
        String message = "";

        if (provId == null || provId.equals("")) {
            try {
                if (siteType.equals(CPSite.SMS)) {
                    message = feedback.getUserFriendlyDescription(Feedback.MISSING_INVALID_PROV_ID);
                } else {
                    message = feedback.formDefaultMessage(Feedback.MISSING_INVALID_PROV_ID);
                }
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            out.println(message);
            return;
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
        //message body
        if (msg == null || msg.equals("")) {
            msg = "";
        }
        //default message for acknowledgment
        if (ACK == null || ACK.equals("")) {
            ACK = "";
        }

        //logging statements
        System.out.println("Cancelling subscription to a service");
        System.out.println("Date received: " + java.util.Calendar.getInstance().getTime().toString());
        System.out.println("Keyword: " + kw);
        System.out.println("Message: " + msg);
        System.out.println("Subscriber's number: " + msisdn);
        System.out.println("Service privoder's ID: " + provId);
        //end of logging

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
            out.println(message);
            return;
        }

        //continue
        try {
      
            provName = new com.rancard.mobility.contentprovider.User().viewDealer(provId).getName();

            String serviceNames = "";
            if (msg != null && !msg.equals("")) {
                //process multiple unsubscriptions if required
               String [] keywords = msg.split(",");
               ArrayList<String> keywordList = new ArrayList<String>();
               if(keywords.length > 1){

                   for(int i =0 ; i < keywords.length ; i++){
                       //trimming keywords
                       keywordList.add(keywords[i].trim());
                       System.out.print("Keyword #"+i+" = "+keywordList.get(i));
                   }
                  ServiceManager.forceUnsubscribe(msisdn, keywordList, provId);
                   ACK = "You have successfully cancelled your subscriptions. Thank you for using our services.";
                  action = "0";
               }else{
                  try {
                    UserService service = ServiceManager.viewService(msg, provId);
                    if (service == null || service.getAccountId() == null || service.getKeyword() == null || service.getAccountId().equals("") || service.getKeyword().equals("")) {
                        // keyword not found so perform lookup by alias instead
                        service = ServiceManager.viewServiceByAlias(msg, provId);
                        if (service == null || service.getAccountId() == null || service.getKeyword() == null || service.getAccountId().equals("") || service.getKeyword().equals("")) {
                            action = ACTION_UNSUBSCRIBE_ALL;
                        //throw new Exception(Feedback.NO_SUCH_SERVICE);
                        } else {//set msg to actual service KEYWORD
                            msg = service.getKeyword();
                        }
                    }

                    serviceNames = service.getServiceName();
                    service = null;
                } catch (Exception ex) {
                    throw new Exception(Feedback.NO_SUCH_SERVICE);
                }
               }
            } else {
                //serviceNames = "all services";
                action = ACTION_UNSUBSCRIBE_ALL;
            }

            //determine what action to take
            if (action.equals(ACTION_UNSUBSCRIBE_ALL)) {
                ServiceManager.unsubscribeToService(msisdn, "", provId);
                //set response message
                ACK = feedback.getValue("CANCEL_ALL_SUBSCRIPTIONS");
            } else if(action.equals(ACTION_UNSUBSCRIBE_KW)){
                /*
                 This bit of code should be TEMPORARY. It's only implemented to fix the
                 D2M problem where unsubscribing from the main service leaves a user
                 still subscribed to the child service. Rectify by implementing choice on how
                 many messages a subscriber can get a day per subscription
                */
                //--------------------------------------------------------------
                ArrayList<String> keywords = new ArrayList<String>();
                keywords.add (msg);
                keywords.add (msg + "2");
                ServiceManager.forceUnsubscribe (msisdn, keywords, provId);
                //--------------------------------------------------------------

                //This was the original code
                //--------------------------------------------------------------
                //ServiceManager.unsubscribeToService(msisdn, msg, provId);
                //--------------------------------------------------------------
            }

            String insertions = "ack=" + ACK + "&keyword=" + kw + "&msg=" + msg + "&msisdn=" + msisdn + "&acctId=" + provId + "&srvcName=" + serviceNames + "&provName=" + provName;
            ACK = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, ACK);

            out.println(ACK);
            /*String URL = "http://74.205.84.249/rms/sendsms?username=rsltest&password=rsltest&conn=myBuzz:801&to=" + URLUTF8Encoder.encode(msisdn) + "&text=" + URLUTF8Encoder.encode(ACK) + "&from=" + URLUTF8Encoder.encode("+801");
            HttpClient client = new HttpClient();
            GetMethod httpGETFORM = new GetMethod(URL);

            try {
                System.out.println(new java.util.Date() + ": " + kw + ": " + msisdn + ": Pushing unsubscribe confirmation message: " + URL);
                client.executeMethod(httpGETFORM);
                String resp = getResponse(httpGETFORM.getResponseBodyAsStream());
                System.out.println(new java.util.Date() + ": " + kw + ": " + msisdn + ": Message push complete! Gateway response: " + resp);    
            } catch (Exception e) {
                System.out.println(new java.util.Date() + ":" + kw + ": " + msisdn + ":exception pushing unsubscribe confirmation message: " + e.getMessage());
            } finally {
                httpGETFORM.releaseConnection();
                client = null;
                httpGETFORM = null;
            }*/

            cnxn = null;

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
            out.println(message);
            return;
        }
    }
    
    private String getResponse(java.io.InputStream in) throws Exception {
    String status = "error";
    String reply = "";
    String error = "";
    String responseString = "";
    BufferedReader br = null;
    try {
        InputStream responseBody = in;
        br = new BufferedReader(new InputStreamReader(
                responseBody));
        String line = br.readLine();
        while(line != null){
            responseString = responseString + line;
            line = br.readLine();
        }
    } catch (IOException e) {
        throw new Exception(Feedback.TRANSPORT_ERROR + ": " + e.getMessage());
    } finally {
        br.close();
        in.close();
        
        br = null;
        in = null;
    }
    
    return responseString;
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
}
