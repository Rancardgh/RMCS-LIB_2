package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.mobility.contentserver.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import com.rancard.common.Message;
import com.rancard.common.Feedback;
import java.net.URL;
import java.net.URLConnection;
import net.sourceforge.wurfl.wurflapi.*;
import com.rancard.mobility.common.Driver;
import org.apache.commons.httpclient.HttpStatus;

public class senddownloadrequest extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html";
    private static final String FROM = "RMCS";

    //need to create a new DB for RCS
    String to = null;
    //String shortcode = null;
    String text = null; ;
    //String username = null; // account Username?
    String responsePath = null;
    //String password = null; //account Password?
    //String smsc = null; //account smsc
    String queryString = null;
    String url = null;
    String listid = null;
    String provName = "";
    String pin = null;
    //String driverName = null;
    String ticket = new String();
    CPConnections cnxn = null;

    //Initialize global variables
    public void init() throws ServletException {

    }

    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        this.text = "Content Download:";
        //this.driverName = "rms";

        String id = request.getParameter("id");
        String listId = request.getParameter("listId");
        String subscriber = request.getParameter("to");
        String phone_id = request.getParameter("phoneId");
        String pinNo = request.getParameter("pin");
        String responsePath = request.getParameter("responsePath");

        String wurflpath = this.getServletContext().getInitParameter(
                "wurfllocation");

        com.rancard.common.Message replyPg = new com.rancard.common.Message();
        PrintWriter out = response.getWriter();

        String s = request.getProtocol().toLowerCase();
        s = s.substring(0, s.indexOf("/")).toLowerCase();
       // String baseUrl = s + "://" + request.getServerName() + ":" +
                       //  request.getServerPort() + request.getContextPath() +
                        // "/";
        String baseUrl = this.getServletContext().getInitParameter("contentServerPublicURL");//
                
        if (id == null) {
            id = "";
        }
        if (listId == null) {
            listId = "";
        }
        if (subscriber == null) {
            subscriber = "";
        }
        if (phone_id == null) {
            phone_id = "";
        }
        if (pinNo == null) {
            pinNo = "";
        }

        ContentItem item = null;
        ContentType type = null;
        Format format = null; ;

        //get provider credentials
        try {
            item = new ContentItem().viewContentItem(id, listId);
            /*this.username = item.getProviderDetails().getUsername();
             this.password = item.getProviderDetails().getPassword();
             this.smsc = item.getProviderDetails().getDefaultSmsc().split(":")[0];
             this.shortcode = item.getProviderDetails().getDefaultSmsc().split(
                    ":")[1];*/

            type = item.getContentTypeDetails();
            format = item.getFormat();

            if ((item.getid() == null) || (item.getid().equals("")) &&
                (item.getListId() != null) || (item.getListId().equals(""))) {
                throw new Exception();
            }
            this.text = this.text + " " + item.gettitle();
        } catch (Exception e) {
            replyPg.setMessage(Feedback.MISSING_INVALID_ITEM_REF);
            replyPg.setStatus(false);
            if (responsePath != null) {
                response.sendRedirect(responsePath + "?reply=" +
                                      replyPg.getMessage());
            } else {
                out.println(replyPg.getMessage());
            }
            return;
        }

        try {
            if (validateNumber(subscriber)) {
                //this.to = subscriber;
            } else {
                replyPg.setMessage(Feedback.MISSING_INVALID_MSISDN);
                replyPg.setStatus(false);
                if (responsePath != null) {
                    response.sendRedirect(responsePath + "?reply=" +
                                          replyPg.getMessage());
                } else {
                    out.println(replyPg.getMessage());
                }
                return;
            }

            try {
                cnxn = CPConnections.getConnection(item.getListId(), this.to);

            } catch (Exception e) {
                replyPg.setMessage(e.getMessage());
                replyPg.setStatus(false);
                if (responsePath != null) {
                    response.sendRedirect(responsePath + "?reply=" +
                                          replyPg.getMessage());
                } else {
                    out.println(replyPg.getMessage());
                }
                return;
            }

            //phone ID has been supplied. check compatibility with chosen format
            if (!phone_id.equals("")) {
                String capabilityValue = new String();
                try {
                    com.rancard.mobility.common.FonCapabilityMtrx fcm = null;

                    if (format.getPushBearer().equals("SMS")) {
                        capabilityValue = "true";
                    } else {
                        try {
                            //fcm = new com.rancard.mobility.common.
                                  //FonCapabilityMtrx(wurflpath + "wurfl.xml"); -- Deprecated
                            fcm = new com.rancard.mobility.common.FonCapabilityMtrx();
                        } catch (Exception e) {
                            throw new Exception(Feedback.
                                                PHONE_MATRIX_INIT_ERROR);
                        }

                        WurflDevice phone = fcm.getActualDevice(phone_id);
                        if (phone == null) {
                            throw new Exception(Feedback.
                                                MISSING_INVALID_PHONE_ID);
                        }

                        if (format.getPushBearer().equals("WAP")) {
                            if (!fcm.getCapabilitiesManager().
                                getCapabilityForDevice(phone_id,
                                    "wap_push_support").equals("true")) {

                                throw new Exception(Feedback.
                                        PUSH_PROTOCOL_NOT_SUPPORTED);
                            }
                        }
                        String capability = fcm.findSupportedCapability(
                                format.getFileExt());

                        capabilityValue = fcm.getCapabilitiesManager().
                                          getCapabilityForDevice(
                                                  phone_id, capability);

                    }

                    if (!capabilityValue.equals("true")) {
                        throw new Exception(Feedback.
                                            PHONE_FORMAT_INCOMPATIBILITY);
                    }

                } catch (Exception e) {
                    replyPg.setMessage(e.getMessage());
                    replyPg.setStatus(false);
                    if (responsePath != null) {
                        response.sendRedirect(responsePath + "?reply=" +
                                              replyPg.getMessage());
                    } else {
                        out.println(replyPg.getMessage());
                    }
                    return;
                }
            }

            //log details
            Transaction download = new Transaction();

            boolean logSuccessful = false;
            while (!logSuccessful) {
                logSuccessful =
                        logging(
                                10, download,
                                "" + format.getId());
            }

            this.url =
                    baseUrl +
                    "downloadcontent?ticketId=" +
                    download.getTicketID();
            this.ticket = download.getTicketID();
            this.text = this.text + " Download ID: " + this.ticket;
            this.text = this.text.replaceAll(" ", "%20");
            this.text = this.text.replaceAll("'", "%27");

            //route notification
            if (this.routeNotification(item)) {
                if (format.getPushBearer().equals("SMS") ||
                    format.getPushBearer().equals("SMS_BIN")) {
                    download.setDownloadCompleted(true);
                    //Transaction.updateTransaction(download.getTicketID(), true, true);
                }
                replyPg.setStatus(true);
            } else {
                Transaction.removeTransaction(download.getTicketID());
                replyPg.setMessage(Feedback.ROUTE_NOTIFICATION_FAILED);

                replyPg.setStatus(false);
                if (responsePath != null) {
                    response.sendRedirect(responsePath + "?reply=" +
                                          replyPg.getMessage());
                } else {
                    out.println(replyPg.getMessage());
                }
                return;
            }

            if (replyPg.isStatus() == true) {
                com.rancard.util.payment.ePin voucher = new com.rancard.
                        util.
                        payment.ePin();
                voucher.setPin(pinNo);
                voucher.isValid();
                voucher.setCurrentBalance(voucher.getCurrentBalance() -
                                          Double.parseDouble(item.getPrice()));
                voucher.updateMyLog();

                replyPg.setMessage("Ok: " + this.ticket);

                replyPg.setStatus(true);
                if (responsePath != null) {
                    response.sendRedirect(responsePath + "?reply=" +
                                          replyPg.getMessage());
                } else {
                    out.println(replyPg.getMessage());
                }
                return;
            }
        } catch (Exception e) {
            try {
                Transaction.removeTransaction(this.ticket);
            } catch (Exception ex) {
            }
            replyPg.setMessage(Feedback.GENERIC_ERROR + ": " +
                               e.getMessage());

            replyPg.setStatus(false);
            if (responsePath != null) {
                response.sendRedirect(responsePath + "?reply=" +
                                      replyPg.getMessage());
            } else {
                out.println(replyPg.getMessage());
            }
            return;
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

    // accomplishes logging of transaction
    public boolean logging(int idLength, Transaction dlb,
                           String formatID) {
        boolean logged = true;
        dlb.setTicketID(com.rancard.common.uidGen.generateID(idLength) + "-" +
                        formatID);
        try {
            //dlb.logTransaction(dlb.getTicketID(), dlb.getID(), dlb.getListID(),dlb.getSubscriberMSISDN(), dlb.getPhoneId(), false,dlb.getPin(),"", false,"");
        } catch (Exception e) {
            logged = false;
        }
        return logged;
    }

    //validate subscriber's number
    public boolean validateNumber(String number) throws Exception {
        boolean flag = false; //overall decision as to whether a number is valid

        if (number.indexOf("+") != -1) {
            StringBuffer sb = new StringBuffer(number);
            sb.deleteCharAt(number.indexOf("+"));
            number = sb.toString();
        }
        number = number.trim();

        try {
            Long.parseLong(number); //checks that number is a string of digits only
            /*if (number.length() < 10) {
                flag = false;
                         } else if (number.length() == 10) {
                if (number.substring(0, 3).equals("020")) {
                    this.driverName = "mks";
                }
                flag = true;
                this.to = number.replaceFirst("0", "+233");
                         } else if (number.length() > 10) {
                if (number.substring(0, 5).equals("23320")) {
                    this.driverName = "mks";
                }
                flag = true;
                this.to = "+" + number;
                         }*/
            flag = true;
            this.to = "+" + number;
        } catch (NumberFormatException e) {
            flag = false;
        }
        return flag;
    }

    //sends WAP push notification
    public boolean sendWapPushNotification(ContentItem item) throws Exception {
        String price_arg = (item.getPrice() == null ? "0" : item.getPrice());
        String response = new String();
        boolean flag = false;

        String number = this.to.substring(1, this.to.length());
        /*response = Driver.getPushDriver(this.driverName).
                   sendWAPPushMessage(number, FROM, this.text, this.url,
                                      this.username, this.password,
                                      new String(this.smsc + ":" +
                                                 this.shortcode),
         item.getContentTypeDetails().getServiceID(),
                                      price_arg);*/
        response = Driver.getDriver(this.cnxn.getDriverType(),
                                    this.cnxn.getGatewayURL()).
                   sendWAPPushMessage(number, FROM, this.text, this.url,
                                      this.cnxn.getUsername(),
                                      this.cnxn.getPassword(),
                                      this.cnxn.getConnection(),
                                      "", price_arg);

        if (response.length() > 18 && response.substring(14, 18).equals("true")) {
            flag = true;
        }
        return flag;
    }

    //sends SMS notification
    public boolean sendSMSNotification(ContentItem item) throws
            Exception {
        boolean flag = false;
        String price_arg = (item.getPrice() == null ? "0" : item.getPrice());
        String response = new String();
        String txt = new String();
        String number = this.to.substring(1, this.to.length());
        byte[] itemStream = null;

        if (!item.islocal()) {
            itemStream = RepositoryManager.getByteArray(item.getDownloadUrl());
        } else {
            com.rancard.mobility.contentserver.uploadsBean upload = new RepositoryManager().fetchFile(item.
                    getListId(), item.getid());
            itemStream = upload.getDataStream();
        }
        txt = new String(itemStream);

        /*if (this.driverName.equals("rms")) {
            response = Driver.getPushDriver(this.driverName).
                       sendSMSTextMessage(number, FROM, txt, this.username,
                                          this.password,
                                          new String(this.smsc + ":" +
                    this.shortcode), item.getContentTypeDetails().
                                          getServiceID(), price_arg);
                 } else if (this.driverName.equals("mks")) {
            response = Driver.getPushDriver(this.driverName).
                       sendSMSTextMessage(this.to, FROM, txt, "cp10050",
                                          "7l05p11tl06", "", "NORTHEND TEST",
                                          "700");
                 }*/
        response = Driver.getDriver(this.cnxn.getDriverType(),
                                    this.cnxn.getGatewayURL()).
                   sendSMSTextMessage(number, FROM, txt, this.cnxn.getUsername(),
                                      this.cnxn.getPassword(),
                                      this.cnxn.getConnection(),
                                      "", price_arg);

        if (response.length() > 18 && response.substring(14, 18).equals("true")) {
            flag = true;
        }

        return flag;
    }

    //sends Binary SMS notification
    public boolean sendSMSBINNotification(ContentItem item) throws
            Exception {
        boolean flag = false;
        String format = item.getFormat().getFileExt();
        String price_arg = (item.getPrice() == null ? "0" : item.getPrice());
        String response = new String();
        StringBuffer sb = null;
        String txt = new String();
        String number = this.to.substring(1, this.to.length());

        byte[] itemStream = null;

        if (!item.islocal()) {
            itemStream = RepositoryManager.getByteArray(item.getDownloadUrl());
        } else {
            com.rancard.mobility.contentserver.uploadsBean upload = new RepositoryManager().fetchFile(item.
                    getListId(), item.getid());
            itemStream = upload.getDataStream();
        }

        txt = RepositoryManager.getUD(format, item);
        /*FileWriter fw = new FileWriter(new File("c:\\ud.txt"));
                 fw.write(txt);
                 fw.close();

         txt = "090E424DE6000000000000003E00000028000000480000000E00000001000100" +
         "00000000A8000000130B0000130B0000020000000200000000000000FFFFFF00" +
         "E000001FF0000000070000008000000FE0000000010000008000000600000000" +
         "01000000039B67030039B67B6000000007DB6F81806DB6FB6000000004DB6980";

                 if (this.driverName.equals("rms")) {
            /*response = Driver.getPushDriver(this.driverName).
                        sendSMSBinaryMessage(number, FROM, txt, "udh", "cs",
                        format, this.username, this.password,
                        new String(this.smsc + ":" + this.shortcode),
              item.getContentTypeDetails().getServiceID(), price_arg);
                  } else if (this.driverName.equals("mks")) {
             response = Driver.getPushDriver(this.driverName).
          sendSMSBinaryMessage(this.to, FROM, txt, "", "", format,
                                             "cp10050", "7l05p11tl06", "",
                                             "NORTHEND TEST", "700");
                  }*/
        response = Driver.getDriver(this.cnxn.getDriverType(),
                                    this.cnxn.getGatewayURL()).
                    sendSMSBinaryMessage(number, FROM, txt, "udh", "cs",
                                         format, this.cnxn.getUsername(),
                                         this.cnxn.getPassword(),
                                         this.cnxn.getConnection(),
                                         "", price_arg);

        if (response.length() > 18 && response.substring(14, 18).equals("true")) {
            flag = true;
        }

        return flag;
    }

    //chooses most appropriate bearer for a particular notification for external files
    public boolean routeNotification(ContentItem item) throws Exception {
        boolean flag = true;
        Format format = item.getFormat();
        if (format.getPushBearer().equals("WAP")) {
            flag = sendWapPushNotification(item);
        } else if (format.getPushBearer().equals("SMS")) {
            flag = sendSMSNotification(item);
        } else if (format.getPushBearer().equals("SMS_BIN")) {
            flag = sendSMSBINNotification(item);
        }
        return flag;
    }
}
