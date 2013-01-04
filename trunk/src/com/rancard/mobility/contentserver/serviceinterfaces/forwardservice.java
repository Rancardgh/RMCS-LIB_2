/*
 * forwardservice.java
 *
 * Created on January 5, 2007, 1:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.Feedback;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.contentserver.CPSite;
import com.rancard.mobility.extension.ForwardedService;
import com.rancard.mobility.extension.ForwardedServiceManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 *
 * @author Messenger
 */
public class forwardservice extends HttpServlet implements RequestDispatcher {
    
    //Initialize global variables
    public void init() throws ServletException {
    }
    
    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        //logging statement
        System.out.println(new java.util.Date()+":@com.rancard.mobility.contentserver.serviceinterfaces.forwardservice...");
        
        
        String message = "";
        //get reeponse writer
        PrintWriter out = response.getWriter();
        CPConnections cnxn = new CPConnections();
        String accountId = (String) request.getAttribute("acctId");
        if(accountId == null || accountId.equals("")){
            accountId = request.getParameter("acctId");
        }
        String kw = request.getParameter("keyword");
        String msgBody = request.getParameter("msg");
        String dest = request.getParameter("dest");
        String msisdn = request.getParameter("msisdn");
        String date = request.getParameter("date");
        String phoneId = request.getParameter("phoneId");
        String ua = request.getParameter("ua");
        String siteId = request.getParameter("siteId");;
        String regId = request.getParameter("regId");
        String ack = (String) request.getAttribute("ack");
        String time = request.getParameter("time");
        String smsc = request.getParameter("smsc");
        String flag = request.getParameter("routeBy");
        String debitInbox = request.getParameter("debitInbox");
        
        //logging statement
        System.out.println(new java.util.Date()+":Request params: ack="+ack);
        
        
        
        if(flag == null){flag = "";}
        
        if(flag.equals(servicelocator.BY_SHORTCODE)){
            kw = dest.substring(dest.indexOf("+") + 1);
        }else if (flag.equals(servicelocator.BY_PROVIDER)) {
            kw = accountId;
        }else{
            kw = kw;
        }
        
        String siteType = (String) request.getAttribute("site_type");
        String lang = (String) request.getAttribute("default_lang");
        if(lang == null || lang.equals("")){
            lang = "en";
        }
        Feedback feedback = (Feedback) this.getServletContext().getAttribute("feedback_" + lang);
        if(feedback == null){
            try{
                feedback = new Feedback();
            }catch(Exception e){
            }
        }
        
        if(ack == null) {ack = "";}
        
        /*try{
            accountId = CPSite.getCpIdForSite (siteId);
        }catch(Exception e){
            out.println (Feedback.INVALID_REQUEST_CREDENTIALS);
            return;
        }*/
        
        //competition cannot be found because provider ID is missing
        if (accountId == null || accountId.equals("")) {
            try {
                if(siteType.equals(CPSite.SMS)){
                    message = feedback.getUserFriendlyDescription(Feedback.MISSING_INVALID_PROV_ID);
                }else{
                    message = feedback.formDefaultMessage(Feedback.MISSING_INVALID_PROV_ID);
                }
            }catch (Exception ex) {
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
        if (kw == null || kw.equals("")) {
            try {
                if(siteType.equals(CPSite.SMS)){
                    message = feedback.getUserFriendlyDescription(Feedback.NO_SUCH_SERVICE);
                }else{
                    message = feedback.formDefaultMessage(Feedback.NO_SUCH_SERVICE);
                }
            }catch (Exception ex) {
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
        
        //check TP inbox credits balance for this CP
        if("1".equals(debitInbox) ){
            com.rancard.mobility.contentprovider.User user = new com.rancard.mobility.contentprovider.User();
            try{
                user = user.viewDealer(accountId);
                
                if(user != null){
                    double inboxLimit = user.getInboxBalance();
                    if(inboxLimit - 1 < 0){
                        System.out.println(new java.util.Date()+":User does NOT have enough space in inbox");
                        throw new Exception(Feedback.INBOX_EXCEEDED);
                    }else{
                        System.out.println(new java.util.Date()+":User has enough space in inbox");
                        double result = inboxLimit - 1;
                        user.updateDealer(user.getId(), user.getBandwidthBalance(), result, user.getOutboxBalance());
                    }
                }else{
                    throw new Exception(Feedback.MISSING_INVALID_PROV_ID);
                }
            }catch(Exception e){
                try {
                    if(siteType.equals(CPSite.SMS)){
                        message = feedback.getUserFriendlyDescription(e.getMessage());
                    }else{
                        message = feedback.formDefaultMessage(e.getMessage());
                    }
                    if(message == null){
                        message = e.getMessage();
                    }
                }catch (Exception ex) {
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
        }
        
        //logging statements
        System.out.println(new java.util.Date()+":Looking for forwarded service for keyword: " + kw + " on account with ID: " + accountId);
        //end of logging
        
        //determine whether to do a message push or pull depending on the service type
        ForwardedService fs = new ForwardedService();
        
        try{
            fs = ForwardedServiceManager.viewForwardedService(kw, accountId);
            if (fs == null || fs.getAccountId().equals("") || fs.getKeyword().equals("")) {
                throw new Exception();
            }
        }catch (Exception e) {
            try {
                if(siteType.equals(CPSite.SMS)){
                    message = feedback.getUserFriendlyDescription(Feedback.NO_SUCH_SERVICE);
                }else{
                    message = feedback.formDefaultMessage(Feedback.NO_SUCH_SERVICE);
                }
            }catch (Exception ex) {
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
        
        //forward request
        String query_string = request.getQueryString();
        String dest_url = fs.getUrl();
        String resultingUrl = "";
        
        //logging statement
        System.out.println(new java.util.Date()+":Forwarding_URL_raw:"+dest_url);
        
        try{
            resultingUrl = com.rancard.util.URLUTF8Encoder.doURLEscaping(query_string, dest_url);
            //logging
            System.out.println(new java.util.Date()+":Forwarding_URL_postURLEscaping: "+resultingUrl);
        }catch (Exception e) {
            System.out.println(new java.util.Date()+":Error in the forward URL:"+dest_url);
            try {
                if(siteType.equals(CPSite.SMS)){
                    message = feedback.getUserFriendlyDescription(Feedback.NO_URL_FOR_SERVICE);
                }else{
                    message = feedback.formDefaultMessage(Feedback.NO_URL_FOR_SERVICE);
                }
            }catch (Exception ex) {
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
        
        HttpClient client = new HttpClient();
        GetMethod httpGETFORM = new GetMethod(resultingUrl);
        httpGETFORM.getParams().setParameter("http.socket.timeout", new Integer(fs.getTimeout()));
        String resp = "";
        
        try {
            client.executeMethod(httpGETFORM);
            resp = getResponse(httpGETFORM.getResponseBodyAsStream());
            //logging statements
            
            //end of logging
        } catch (HttpException e) {
            resp = (Feedback.PROTOCOL_ERROR + ": " + e.getMessage());
            //logging statements
            System.out.println(new java.util.Date()+":error response: " + resp);
            //end of logging
        } catch (IOException e) {
            resp = (Feedback.TRANSPORT_ERROR + ": " + e.getMessage());
            //logging statements
            System.out.println(new java.util.Date()+":error response: " + resp);
            //end of logging
        } catch (Exception e) {
            //logging statements
            System.out.println(new java.util.Date()+":error response: " + e.getMessage());
            //end of logging
        } finally {
            // Release the connection.
            httpGETFORM.releaseConnection();
            client = null;
            httpGETFORM = null;
        }
        
        if (fs.getListenStatus() == fs.LISTEN_FOR_REPLY) {
            //logging statement
            System.out.println(new java.util.Date()+":ListenStatus= LISTEN_FOR_REPLY");
            
            System.out.println(new java.util.Date()+":response: " + resp);
            
            boolean isAsciiPrintable = org.apache.commons.lang.StringUtils.isAsciiPrintable (resp);
            if (!isAsciiPrintable) {
                System.out.println ("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                request.setAttribute ("X-Kannel-Coding", "2");
                if (request.getAttribute ("X-Kannel-Coding") != null)
                    System.out.println ("Request contains X-Kannel-Coding attribute");
            }
            out.println(resp);
        } else if (fs.getListenStatus() == fs.REPLY_WITH_DEFAULT){
            //logging statement
            System.out.println(new java.util.Date()+":ListenStatus= REPLY_WITH_DEFAULT");
            
            String insertions = "ack=" + ack + "&keyword=" + kw + "&msg=" + msgBody + "&msisdn=" + msisdn + "&dest=" + dest + "&date=" + date + "&phoneId=" +
                    phoneId + "&ua=" + ua + "&regId=" + regId + "&resp=" + resp;
            
            System.out.println(new java.util.Date()+":insertions= "+insertions);
            
            try{
                
                ack = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, ack);
            }catch(Exception e) {
                //logging statement
                System.out.println(new java.util.Date()+":Error doing MessageEscaping:");
                e.printStackTrace();
                
                ack="";
            }
            System.out.println(new java.util.Date()+":response: " + ack);
            
            boolean isAsciiPrintable = org.apache.commons.lang.StringUtils.isAsciiPrintable (ack);
            if (!isAsciiPrintable) {
                System.out.println ("Setting request attribute for Kannel Header: X-Kannel-Coding ...");
                request.setAttribute ("X-Kannel-Coding", "2");
                if (request.getAttribute ("X-Kannel-Coding") != null)
                    System.out.println ("Request contains X-Kannel-Coding attribute");
            }
            out.println(ack);
            
        } else {
            //logging statement
            System.out.println(new java.util.Date()+":ListenStatus= NEVER_LISTEN_FOR_REPLY");
            
            System.out.println(new java.util.Date()+":response: " + resp);
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
            ServletResponse response) throws ServletException, IOException{
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        doGet(req, resp);
    }
    
    public void include(ServletRequest request,
            ServletResponse response)  throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        doGet(req, resp);
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
}
