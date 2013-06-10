package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.Feedback;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.dom4j.Document;
import org.dom4j.Element;

public class receiverequest extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html";
    
    private String TXT1 = "";
    private String TXT2 = "";
    private String PRC1 = "700";
    private String PRC2 = "700";
    private String sender = "";
    private String siteId = "";
    private String baseUrl = "";
    private String data = "";
    
    //Initialize global variables
    public void init () throws ServletException {
    }
    
    //Process the HTTP Get request
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        
        String s = request.getProtocol ().toLowerCase ();
        s = s.substring (0, s.indexOf ("/")).toLowerCase ();
        baseUrl = s + "://" + request.getServerName () + ":" + request.getServerPort () + request.getContextPath () + "/";
        
        PrintWriter pw = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        
        try {
            //receive notification
            isr = new InputStreamReader (request.getInputStream ());
            
            siteId=request.getParameter ("siteId");
            
            br = new BufferedReader (isr);
            String line = new String ();
            String resp = new String ();
            while ((line = br.readLine ()) != null) {
                resp = resp + line + "\n";
            }
            
            //process notification
            if (processResponse (resp) == true) {
                //send request
                String rsp = sendRequest (baseUrl);
                //send reply
                String xml = createReply (rsp, PRC1, TXT2, PRC2);
                
                pw = new PrintWriter (response.getOutputStream ());
                pw.println (xml);
                pw.flush ();
            }else{
                throw new Exception (Feedback.XML_PARSER_ERROR);
            }
        } catch (Exception e) {
            pw.println (e.getMessage ());
        } finally {
            if (pw != null) {
                pw.close ();
            } // end of if (out != null)
            try {
                br.close ();
            } catch (Exception e) {
            } // end of try-catch
            try {
                isr.close ();
            } catch (Exception e) {
            } // end of try-catch
        }
    }
    
    //Process the HTTP Post request
    public void doPost (HttpServletRequest request,
            HttpServletResponse response) throws
            ServletException, IOException {
        doGet (request, response);
    }
    
    //Clean up resources
    public void destroy () {
    }
    
    private String createReply (String text1, String price1, String text2,
            String price2) {
        String xml = new String ();
        xml = "<?xml version=\"1.0\" encoding=\"UTF\u22128\" ?>" + "\r\n"
                + "<NotificationReply>" + "\r\n"
                + "<messsage>" + "\r\n"
                + "<text>" + text1 + "</text>" + "\r\n"
                + "<cost>" + price1 + "</cost>" + "\r\n"
                + "</messsage>" + "\r\n"
                + "<messsage>" + "\r\n"
                + "<text>" + text2 + "</text>" + "\r\n"
                + "<cost>" + price2 + "</cost>" + "\r\n"
                + "</messsage>" + "\r\n"
                + "</NotificationReply>" + "\r\n";
        return xml;
    }
    
    private boolean processResponse (String xmlDoc) {
        boolean isOk = false;
        try {
            Document xml = org.dom4j.DocumentHelper.parseText (xmlDoc);
            Element rootNode = xml.getRootElement ();
            Element element = null;
            Iterator i = rootNode.elementIterator ();
            while (i.hasNext ()) {
                element = (Element) i.next ();
                if (element.getName ().equals ("sender")) {
                    this.sender = element.getText ();
                }
                if (element.getName ().equals ("parameters")) {
                    this.data = element.element ("text").getText ();
                }
            }
            if (this.sender != null && this.data != null) {
                isOk = true;
            }
        } catch (Exception e) {
            isOk = false;
        }
        return isOk;
    }
    
    private String sendRequest (String baseurl) throws Exception {
        try {
            java.util.StringTokenizer st = new java.util.StringTokenizer (this.data, "., ");
            String listId = st.nextToken ();
            if(listId.equalsIgnoreCase ("FON") || listId.equalsIgnoreCase ("NORTHEND")) {
                listId = "cp001";
            }
            String keyword = st.nextToken ();
            String body = st.nextToken ();
            while(st.hasMoreTokens ()){
                body = body + st.nextToken ();
            }
            body = com.rancard.util.URLUTF8Encoder.encode (body);
            String number = com.rancard.util.URLUTF8Encoder.encode (this.sender);
            String requestUrl = baseurl + "rmcsservices.jsp?keyword=" + keyword + "&msg=" + body + "&msisdn=" + this.sender + "&siteId=" + siteId;
            
            java.net.URL url = new java.net.URL (requestUrl);
            java.io.BufferedReader br = new java.io.BufferedReader (new InputStreamReader (url.openStream ()));
            String temp = br.readLine ();
            String resp = new String ();
            while(temp != null){
                resp = resp + temp;
                temp = br.readLine ();
            }
            br.close ();
            return resp;
        } catch (IOException ex2) {
            throw new Exception (com.rancard.common.Feedback.ROUTE_NOTIFICATION_FAILED);
        }
    }
}
