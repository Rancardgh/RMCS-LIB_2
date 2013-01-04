package com.rancard.mobility.common;

import java.net.URL;
import java.io.*;
import org.dom4j.*;
import org.dom4j.io.*;
import java.util.List;
import java.util.Iterator;


import java.net.URLConnection;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class MksDriver implements PushDriver {
    
    public MksDriver(String gatewayUrl) {
        this.mksGatewayUrl = gatewayUrl;
    }
    
    //Settings
    protected String mksGatewayUrl = "";
    
    // content type
    public static final String SEVENBIT = "0";
    public static final String EIGHTBIT = "1";
    public static final String UCS2 = "2";
    public static final String SENDSMSTEXTRESPONSE = "";
    public static final String SENDSMSBINARYRESPONSE = "";
    public static final String SENDPUSHRESPONSE = "";
    public static final String SENDSMSTEXTMULTIRESPONSE = "";
    
    public String sendSMSTextMessage(String to, String from, String text, String username, String password, String conn, String meta_data) {
        // meta_data not available for this driver.
        // Call alternate version of sendSMSTextMessage passing empty
        // strings for `serviceID` and `price` parameters
        return sendSMSTextMessage(to, from, text, username, password, conn, "", "");
    }
    
    public String sendSMSTextMessage(String receiver, String sender, String text, String username, String password, String cnx, String service,
            String cost) {
        String messageBody = "";
        String response = "";
        messageBody = "<?xml version=\"1.0\" encoding=\"UTF\u22128\" ?>" +
                "\r\n"
                + "<SMSBoxXMLRequest>" + "\r\n"
                + "<username>" + username + "</username>" + "\r\n"
                + "<password>" + password + "</password>" + "\r\n"
                + "<command>WEBSEND</command>" + "\r\n"
                + "<parameters>" + "\r\n"
                + "<receiver>" + receiver + "</receiver>" + "\r\n"
                + "<service>FON " + service + "</service>" + "\r\n"
                + "<text>" + text + "</text>" + "\r\n"
                + "<cost>" + cost + "</cost>" + "\r\n"
                + "<operator>gt</operator>" + "\r\n"
                + "</parameters>" + "\r\n"
                + "</SMSBoxXMLRequest>" + "\r\n";
        
        try {
            response = submitData(messageBody, "text/xml; charset=UTF-8");
            response = this.processResponse(response);
        } catch (Exception ex) {
        }
        
        return response;
    }
    
    public String sendSMSTextMessage(String[] receiver, String sender,String text, String username, String password, String cnx, String service,
            String cost) {
        String response = "";
        String messageBody = "";
        String receiverString = "";
        for (int i = 0; i < receiver.length; i++) {
            receiverString += "<multiReceiver>" + receiver[i] +
                    "<multiReceiver>" + "\r\n";
        }
        messageBody = "<?xml version=\"1.0\" encoding=\"UTF\u22128\" ?>" +
                "\r\n"
                + "<SMSBoxXMLRequest>" + "\r\n"
                + "<username>" + username + "</username>" + "\r\n"
                + "<password>" + password + "</password>" + "\r\n"
                + "<command>WEBSEND</command>" + "\r\n"
                + "<parameters>" + "\r\n"
                + receiverString
                + "<service>FON " + service + "</service>" + "\r\n"
                + "<text>" + text + "</text>" + "\r\n"
                + "<cost>" + cost + "</cost>" + "\r\n"
                + "<operator>gt</operator>" + "\r\n"
                + "</parameters>" + "\r\n"
                + "</SMSBoxXMLRequest>" + "\r\n";
        
        try {
            response = submitData(messageBody, "text/xml; charset=UTF-8");
            response = this.processResponse(response);
        } catch (Exception ex) {
        }
        
        return response;
        
    }
    
    public String sendSMSBinaryMessage(String receiver, String sender, String UD, String UDH, String codingScheme, String format, String username,
            String password, String cnx, String service, String cost) {
        String messageBody = "";
        String response = "";
        messageBody = "<?xml version=\"1.0\" encoding=\"UTF\u22128\" ?>" +
                "\r\n"
                + "<SMSBoxXMLRequest>" + "\r\n"
                + "<username>" + username + "</username>" + "\r\n"
                + "<password>" + password + "</password>" + "\r\n"
                + "<command>WEBSENDBINARY</command>" + "\r\n"
                + "<parameters>" + "\r\n"
                + "<receiver>" + receiver + "</receiver>" + "\r\n"
                + "<service>FON " + service + "</service>" + "\r\n";
        
        //format-specific adaptation
        if (format.equalsIgnoreCase("noktxt")) {
            messageBody = messageBody + "<hex>" + "02" + UD + "</hex>" + "\r\n"
                    + "<class>ringtone</class>" + "\r\n";
        } else if (format.equalsIgnoreCase("ems")) {
            String udh = UD.substring(8);
            messageBody = messageBody + "<hex>" + udh + "</hex>" + "\r\n"
                    + "<class>usersound</class>" + "\r\n";
        } else if (format.equalsIgnoreCase("imy") || format.equalsIgnoreCase("10.imy")) {
            String udh = UD;
            messageBody = messageBody + "<hex>" + udh + "</hex>" + "\r\n"
                    + "<class>usersound</class>" + "\r\n";
        }  else if (format.equalsIgnoreCase("bmp") || format.equalsIgnoreCase("wbmp")) {
            String width = UD.substring(2, 4);
            int basetenofwidth = Integer.parseInt(width, 16);
            width = Integer.toHexString(basetenofwidth/8); //EMS standards require that the width(DEC) be divided by 8 and converted back to hex
            if(width.length() % 2 != 0) {width = "0" + width;} //zero extend
            String height = UD.substring(4, 6);
            UD = UD.substring(8);
            String udh = width + height + UD;
            messageBody = messageBody + "<hex>" + udh + "</hex>" + "\r\n"
                    + "<class>varpict</class>" + "\r\n";
        } else {
            messageBody = messageBody + "<hex>" + codingScheme + "~" + UDH + "~" + UD + "</hex>" + "\r\n"
                    + "<class>raw</class>" + "\r\n";
        }
        
        //contimuation of XML structure
        messageBody = messageBody + "<cost>" + "700" + "</cost>" + "\r\n"
                + "<operator>gt</operator>" + "\r\n"
                + "</parameters>" + "\r\n"
                + "</SMSBoxXMLRequest>" + "\r\n";
        
        try {
            response = submitData(messageBody, "text/xml; charset=UTF-8");
            response = this.processResponse(response);
        } catch (Exception ex) {
        }
        
        return response;
    }
    
    public String sendWAPPushMessage(String receiver, String sender, String text, String url, String username, String password, String cnx, String service,
            String cost) {
        String messageBody = "";
        String response = "";
        messageBody = "<?xml version=\"1.0\" encoding=\"UTF\u22128\" ?>" +
                "\r\n"
                + "<SMSBoxXMLRequest>" + "\r\n"
                + "<username>" + username + "</username>" + "\r\n"
                + "<password>" + password + "</password>" + "\r\n"
                + "<command>WAPPUSH</command>" + "\r\n"
                + "<parameters>" + "\r\n"
                + "<receiver>" + receiver + "</receiver>" + "\r\n"
                + "<service>FON " + service + "</service>" + "\r\n"
                + "<text>" + text + "</text>" + "\r\n"
                + "<url>" + url + "</url>" + "\r\n"
                + "<cost>" + cost + "</cost>" + "\r\n"
                + "</parameters>" + "\r\n"
                + "</SMSBoxXMLRequest>" + "\r\n";
        try {
            response = submitData(messageBody, "text/xml; charset=UTF-8");
            response = this.processResponse(response);
        } catch (Exception ex) {
        }
        
        return response;
        
    }
    
    private String submitData(String xml, String contentType) throws Exception {
        String responses = new String();
        PrintWriter out = null;
        
        InputStreamReader isr = null;
        BufferedReader br = null;
        URL u = new URL(mksGatewayUrl);
        try {
            // open the connection and prepare it to POST
            URLConnection uc = u.openConnection();
            uc.setDoOutput(true);
            
            uc.setRequestProperty("Content-Type", contentType);
            
            // The POST line, the Accept line, and
            // the content-type headers are sent by the URLConnection.
            // Just need to send the data
            out = new PrintWriter(uc.getOutputStream());
            out.println(xml);
            out.flush();
            // Read the response
            // System.out.println("Content Status:   "+ uc.getHeaderField(0));
            String status = uc.getHeaderField(0);
            if (status.indexOf(" 20") == -1) {
                String header;
                for (int j = 0; ; j++) {
                    header = uc.getHeaderField(j);
                    if (header == null) {
                        break;
                    }
                    responses +=
                            (uc.getHeaderFieldKey(j) + "\t " + header + "\n");
                } // end for
            } else {
                {
                    // System.out.println("status.indexOf(\"202\"): "+status.indexOf(" 20")+ " status: "+status );
                    isr = new InputStreamReader(uc.getInputStream());
                    br = new BufferedReader(isr);
                    String nextline;
                    while ((nextline = br.readLine()) != null) {
                        responses += (nextline + "\n");
                    }
                }
            }
        } catch (Exception e) {
//            logger.debug(e.toString(), e);
            throw new Exception(e.toString(), e);
        } finally {
            if (out != null) {
                out.close();
            } // end of if (out != null)
            try {
                br.close();
            } catch (Exception e) {
            } // end of try-catch
            try {
                isr.close();
            } catch (Exception e) {
            } // end of try-catch
        } // end of finally
        
        // logger.debug("FOO 4");
        
        return responses;
    }
    
    public String processResponse(String xmlDoc) throws Exception {
        boolean isOk = false;
        String resp = null;
        try {
            Document xml = org.dom4j.DocumentHelper.parseText(xmlDoc);
            Element rootNode = xml.getRootElement();
            Element element = null;
            Iterator i = rootNode.elementIterator();
            while (i.hasNext()) {
                element = (Element) i.next();
                if (element.getName().equals("ok")) {
                    element = (Element) i.next();
                    if (element.getName().equals("command")) {
                        Iterator elementItr = element.elementIterator();
                        element = (Element) elementItr.next();
                        if (element.getName().equals("receiver")) {
                            element.attribute("status").getData().equals(
                                    "ok");
                            isOk = true;
                            resp = "Request sent: " + isOk + ". Response from " +
                                    "gateway: " + element.getText();
                            
                        }
                    }
                    break;
                } else {
                    resp = "Request sent: " + isOk + ". Response from " +
                            "gateway: " + element.getText();
                    break;
                }
            }
        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        }
        return resp;
    }
    
    /**
     * This method  returns the name of any node in the xpath expression as a string or null object
     * @param xpath
     * @param document
     * @return Object
     */
    protected Object getNodeName(String xpath, Document document) throws Exception {
        
        Object results = document.selectObject(xpath);
        if (results instanceof Node) {
            Node node = (Node) results;
            if (node instanceof Document) {
                Document doc = (Document) node;
                return doc.getName();
            } else if (node instanceof Element) {
                Element element = (Element) node;
                return element.getName();
            } else {
                return node.getName();
                
            }
        } else if (results instanceof List) {
            List list = (List) results;
            return list;
        } else {
            return null;
        }
//return null;
        //    printResult(results);
        //xmlWriter.flush();
    }
    
     /**
     * processResponse
     *
     * @param responseType String
     */
    /*public String processResponse(String xmlDoc) throws
            Exception {
        String status = "error";
        String receiver = "";
        String error = "";
        boolean isOk = false;
        Document doc = DocumentHelper.parseText(xmlDoc);
        String responseString = "";
     
        this.getNodeName("/SMSBoxXMLReply/ok", doc);
        Node node = doc.selectSingleNode("/SMSBoxXMLReply/ok");
        //isOk =("ok".equalsIgnoreCase( getNodeName("/SMSBoxXMLReply/ok",doc)));
        if (node != null && node.getName().equalsIgnoreCase("ok")) {
     
            isOk = true;
        } else {
            status = "error";
            error = doc.valueOf("/SMSBoxXMLReply/error/");
     
        }
        if (SENDSMSTEXTRESPONSE.equalsIgnoreCase(doc.valueOf(
                "/SMSBoxXMLReply/command/@name"))) {
     
            status = doc.valueOf("/SMSBoxXMLReply/command/receiver/@status");
            receiver = doc.valueOf("/SMSBoxXMLReply/command/receiver/");
            responseString = receiver + ":" + status + ((isOk) ? "" : error);
     
        } else if (SENDSMSBINARYRESPONSE.equalsIgnoreCase(doc.valueOf(
                "/SMSBoxXMLReply/command/@name"))) {
     
            status = doc.valueOf("/SMSBoxXMLReply/command/receiver/@status");
            receiver = doc.valueOf("/SMSBoxXMLReply/command/receiver/");
            responseString = receiver + ":" + status + ((isOk) ? "" : error);
        } else if (SENDPUSHRESPONSE.equals(doc.valueOf(
                "/SMSBoxXMLReply/command/@name"))) {
     
            status = doc.valueOf("/SMSBoxXMLReply/command/receiver/@status");
            receiver = doc.valueOf("/SMSBoxXMLReply/command/receiver/");
            responseString = receiver + ":" + status + ((isOk) ? "" : error);
        } else if (SENDSMSTEXTMULTIRESPONSE.equals(doc.valueOf(
                "/SMSBoxXMLReply/command/@name"))) {
     
            status = doc.valueOf("/SMSBoxXMLReply/command/receiver/@status");
            receiver = doc.valueOf("/SMSBoxXMLReply/command/receiver/");
     
            XPath xpathSelector = DocumentHelper.createXPath(
                    "/SMSBoxXMLReply/command/receiver");
            List results = xpathSelector.selectNodes(doc);
            for (Iterator iter = results.iterator(); iter.hasNext(); ) {
                Element element = (Element) iter.next();
                responseString += element.getName() + ":" +
                        element.attributeValue("status") + "&";
            }
     
        } else {
     responseString = "error processing document!"; // error processing document
        }
     
        return responseString;
         }*/
}
