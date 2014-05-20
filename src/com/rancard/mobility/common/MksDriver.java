package com.rancard.mobility.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class MksDriver
        implements PushDriver
{
    public MksDriver(String gatewayUrl)
    {
        this.mksGatewayUrl = gatewayUrl;
    }

    protected String mksGatewayUrl = "";
    public static final String SEVENBIT = "0";
    public static final String EIGHTBIT = "1";
    public static final String UCS2 = "2";
    public static final String SENDSMSTEXTRESPONSE = "";
    public static final String SENDSMSBINARYRESPONSE = "";
    public static final String SENDPUSHRESPONSE = "";
    public static final String SENDSMSTEXTMULTIRESPONSE = "";

    public String sendSMSTextMessage(String to, String from, String text, String username, String password, String conn, String meta_data)
    {
        return sendSMSTextMessage(to, from, text, username, password, conn, "", "");
    }

    public String sendSMSTextMessage(String receiver, String sender, String text, String username, String password, String cnx, String service, String cost)
    {
        String messageBody = "";
        String response = "";
        messageBody = "<?xml version=\"1.0\" encoding=\"UTF−8\" ?>\r\n<SMSBoxXMLRequest>\r\n<username>" + username + "</username>" + "\r\n" + "<password>" + password + "</password>" + "\r\n" + "<command>WEBSEND</command>" + "\r\n" + "<parameters>" + "\r\n" + "<receiver>" + receiver + "</receiver>" + "\r\n" + "<service>FON " + service + "</service>" + "\r\n" + "<text>" + text + "</text>" + "\r\n" + "<cost>" + cost + "</cost>" + "\r\n" + "<operator>gt</operator>" + "\r\n" + "</parameters>" + "\r\n" + "</SMSBoxXMLRequest>" + "\r\n";
        try
        {
            response = submitData(messageBody, "text/xml; charset=UTF-8");
            response = processResponse(response);
        }
        catch (Exception ex) {}
        return response;
    }

    public String sendSMSTextMessage(String[] receiver, String sender, String text, String username, String password, String cnx, String service, String cost)
    {
        String response = "";
        String messageBody = "";
        String receiverString = "";
        for (int i = 0; i < receiver.length; i++) {
            receiverString = receiverString + "<multiReceiver>" + receiver[i] + "<multiReceiver>" + "\r\n";
        }
        messageBody = "<?xml version=\"1.0\" encoding=\"UTF−8\" ?>\r\n<SMSBoxXMLRequest>\r\n<username>" + username + "</username>" + "\r\n" + "<password>" + password + "</password>" + "\r\n" + "<command>WEBSEND</command>" + "\r\n" + "<parameters>" + "\r\n" + receiverString + "<service>FON " + service + "</service>" + "\r\n" + "<text>" + text + "</text>" + "\r\n" + "<cost>" + cost + "</cost>" + "\r\n" + "<operator>gt</operator>" + "\r\n" + "</parameters>" + "\r\n" + "</SMSBoxXMLRequest>" + "\r\n";
        try
        {
            response = submitData(messageBody, "text/xml; charset=UTF-8");
            response = processResponse(response);
        }
        catch (Exception ex) {}
        return response;
    }

    public String sendSMSBinaryMessage(String receiver, String sender, String UD, String UDH, String codingScheme, String format, String username, String password, String cnx, String service, String cost)
    {
        String messageBody = "";
        String response = "";
        messageBody = "<?xml version=\"1.0\" encoding=\"UTF−8\" ?>\r\n<SMSBoxXMLRequest>\r\n<username>" + username + "</username>" + "\r\n" + "<password>" + password + "</password>" + "\r\n" + "<command>WEBSENDBINARY</command>" + "\r\n" + "<parameters>" + "\r\n" + "<receiver>" + receiver + "</receiver>" + "\r\n" + "<service>FON " + service + "</service>" + "\r\n";
        if (format.equalsIgnoreCase("noktxt"))
        {
            messageBody = messageBody + "<hex>" + "02" + UD + "</hex>" + "\r\n" + "<class>ringtone</class>" + "\r\n";
        }
        else if (format.equalsIgnoreCase("ems"))
        {
            String udh = UD.substring(8);
            messageBody = messageBody + "<hex>" + udh + "</hex>" + "\r\n" + "<class>usersound</class>" + "\r\n";
        }
        else if ((format.equalsIgnoreCase("imy")) || (format.equalsIgnoreCase("10.imy")))
        {
            String udh = UD;
            messageBody = messageBody + "<hex>" + udh + "</hex>" + "\r\n" + "<class>usersound</class>" + "\r\n";
        }
        else if ((format.equalsIgnoreCase("bmp")) || (format.equalsIgnoreCase("wbmp")))
        {
            String width = UD.substring(2, 4);
            int basetenofwidth = Integer.parseInt(width, 16);
            width = Integer.toHexString(basetenofwidth / 8);
            if (width.length() % 2 != 0) {
                width = "0" + width;
            }
            String height = UD.substring(4, 6);
            UD = UD.substring(8);
            String udh = width + height + UD;
            messageBody = messageBody + "<hex>" + udh + "</hex>" + "\r\n" + "<class>varpict</class>" + "\r\n";
        }
        else
        {
            messageBody = messageBody + "<hex>" + codingScheme + "~" + UDH + "~" + UD + "</hex>" + "\r\n" + "<class>raw</class>" + "\r\n";
        }
        messageBody = messageBody + "<cost>" + "700" + "</cost>" + "\r\n" + "<operator>gt</operator>" + "\r\n" + "</parameters>" + "\r\n" + "</SMSBoxXMLRequest>" + "\r\n";
        try
        {
            response = submitData(messageBody, "text/xml; charset=UTF-8");
            response = processResponse(response);
        }
        catch (Exception ex) {}
        return response;
    }

    public String sendWAPPushMessage(String receiver, String sender, String text, String url, String username, String password, String cnx, String service, String cost)
    {
        String messageBody = "";
        String response = "";
        messageBody = "<?xml version=\"1.0\" encoding=\"UTF−8\" ?>\r\n<SMSBoxXMLRequest>\r\n<username>" + username + "</username>" + "\r\n" + "<password>" + password + "</password>" + "\r\n" + "<command>WAPPUSH</command>" + "\r\n" + "<parameters>" + "\r\n" + "<receiver>" + receiver + "</receiver>" + "\r\n" + "<service>FON " + service + "</service>" + "\r\n" + "<text>" + text + "</text>" + "\r\n" + "<url>" + url + "</url>" + "\r\n" + "<cost>" + cost + "</cost>" + "\r\n" + "</parameters>" + "\r\n" + "</SMSBoxXMLRequest>" + "\r\n";
        try
        {
            response = submitData(messageBody, "text/xml; charset=UTF-8");
            response = processResponse(response);
        }
        catch (Exception ex) {}
        return response;
    }

    private String submitData(String xml, String contentType) throws Exception
    {
        String responses = new String();
        PrintWriter out = null;

        InputStreamReader isr = null;
        BufferedReader br = null;
        URL u = new URL(this.mksGatewayUrl);
        try
        {
            URLConnection uc = u.openConnection();
            uc.setDoOutput(true);

            uc.setRequestProperty("Content-Type", contentType);




            out = new PrintWriter(uc.getOutputStream());
            out.println(xml);
            out.flush();


            String status = uc.getHeaderField(0);
            if (status.indexOf(" 20") == -1)
            {
                for (int j = 0;; j++)
                {
                    String header = uc.getHeaderField(j);
                    if (header == null) {
                        break;
                    }
                    responses = responses + uc.getHeaderFieldKey(j) + "\t " + header + "\n";
                }
            }
            else
            {
                isr = new InputStreamReader(uc.getInputStream());
                br = new BufferedReader(isr);
                String nextline;
                while ((nextline = br.readLine()) != null) {
                    responses = responses + nextline + "\n";
                }
            }
            return responses;
        }
        catch (Exception e)
        {
            throw new Exception(e.toString(), e);
        }
        finally
        {
            if (out != null) {
                out.close();
            }
            try
            {
                br.close();
            }
            catch (Exception e) {}
            try
            {
                isr.close();
            }
            catch (Exception e) {}
        }
    }

    public String processResponse(String xmlDoc)
            throws Exception
    {
        boolean isOk = false;
        String resp = null;
        try
        {
            Document xml = DocumentHelper.parseText(xmlDoc);
            Element rootNode = xml.getRootElement();
            Element element = null;
            Iterator i = rootNode.elementIterator();
            if (i.hasNext())
            {
                element = (Element)i.next();
                if (element.getName().equals("ok"))
                {
                    element = (Element)i.next();
                    if (element.getName().equals("command"))
                    {
                        Iterator elementItr = element.elementIterator();
                        element = (Element)elementItr.next();
                        if (element.getName().equals("receiver"))
                        {
                            element.attribute("status").getData().equals("ok");

                            isOk = true;
                            resp = "Request sent: " + isOk + ". Response from " + "gateway: " + element.getText();
                        }
                    }
                }
                else
                {
                    resp = "Request sent: " + isOk + ". Response from " + "gateway: " + element.getText();
                }
            }
        }
        catch (Exception e)
        {
            throw new Exception("Error: " + e.getMessage());
        }
        return resp;
    }

    protected Object getNodeName(String xpath, Document document)
            throws Exception
    {
        Object results = document.selectObject(xpath);
        if ((results instanceof Node))
        {
            Node node = (Node)results;
            if ((node instanceof Document))
            {
                Document doc = (Document)node;
                return doc.getName();
            }
            if ((node instanceof Element))
            {
                Element element = (Element)node;
                return element.getName();
            }
            return node.getName();
        }
        if ((results instanceof List))
        {
            List list = (List)results;
            return list;
        }
        return null;
    }
}
