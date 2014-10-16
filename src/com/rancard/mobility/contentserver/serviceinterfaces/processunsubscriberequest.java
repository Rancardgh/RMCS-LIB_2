package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.Feedback;
import com.rancard.mobility.contentprovider.User;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.infoserver.common.services.ServiceManager;
import com.rancard.mobility.infoserver.common.services.UserService;
import com.rancard.util.URLUTF8Encoder;
import com.rancard.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class processunsubscriberequest
        extends HttpServlet
        implements RequestDispatcher {
    public void init()
            throws ServletException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        CPConnections cnxn = new CPConnections();
        String ACTION_UNSUBSCRIBE_KW = "1";
        String ACTION_UNSUBSCRIBE_ALL = "2";
        String action = "1";

        String ACK = (String) request.getAttribute("ack");
        String kw = request.getParameter("keyword");
        String msg = request.getParameter("msg");
        String msisdn = request.getParameter("msisdn");
        String provId = (String) request.getAttribute("acctId");

        String siteType = (String) request.getAttribute("site_type");
        String lang = (String) request.getAttribute("default_lang");
        if ((lang == null) || (lang.equals(""))) {
            lang = "en";
        }
        Feedback feedback = (Feedback) getServletContext().getAttribute("feedback_" + lang);
        if (feedback == null) {
            try {
                feedback = new Feedback();
            } catch (Exception e) {
            }
        }
        String provName = "";
        String message = "";
        if ((provId == null) || (provId.equals(""))) {
            try {
                if (siteType.equals("2")) {
                    message = feedback.getUserFriendlyDescription("10004");
                } else {
                    message = feedback.formDefaultMessage("10004");
                }
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            out.println(message);
            return;
        }
        if ((msisdn == null) || (msisdn.equals(""))) {
            try {
                if (siteType.equals("2")) {
                    message = feedback.getUserFriendlyDescription("2000");
                } else {
                    message = feedback.formDefaultMessage("2000");
                }
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            out.println(message);
            return;
        }
        if ((msg == null) || (msg.equals(""))) {
            msg = "";
        }
        if ((ACK == null) || (ACK.equals(""))) {
            ACK = "";
        }
        System.out.println("Cancelling subscription to a service");
        System.out.println("Date received: " + Calendar.getInstance().getTime().toString());
        System.out.println("Keyword: " + kw);
        System.out.println("Message: " + msg);
        System.out.println("Subscriber's number: " + msisdn);
        System.out.println("Service privoder's ID: " + provId);


        String number = msisdn;
        if (number.indexOf("+") != -1) {
            StringBuffer sb = new StringBuffer(number);
            sb.deleteCharAt(number.indexOf("+"));
            number = sb.toString();

            sb = null;
        }
        number = number.trim();
        try {
            Long.parseLong(number);
            msisdn = "+" + number;
        } catch (NumberFormatException e) {
            try {
                if (siteType.equals("2")) {
                    message = feedback.getUserFriendlyDescription("2000");
                } else {
                    message = feedback.formDefaultMessage("2000");
                }
            } catch (Exception ex) {
                message = ex.getMessage();
            }
            out.println(message);
            return;
        }
        try {
            provName = new User().viewDealer(provId).getName();

            String serviceNames = "";
            if ((msg != null) && (!msg.equals(""))) {
                String[] keywords = msg.split(",");
                ArrayList<String> keywordList = new ArrayList();
                if (keywords.length > 1) {
                    for (int i = 0; i < keywords.length; i++) {
                        keywordList.add(keywords[i].trim());
                        System.out.print("Keyword #" + i + " = " + (String) keywordList.get(i));
                    }
                    ServiceManager.forceUnsubscribe(msisdn, keywordList, provId);
                    ACK = "You have successfully cancelled your subscriptions. Thank you for using our services.";
                    action = "0";
                } else {
                    try {
                        UserService service = ServiceManager.viewService(msg, provId);
                        if ((service == null) || (service.getAccountId() == null) || (service.getKeyword() == null) || (service.getAccountId().equals("")) || (service.getKeyword().equals(""))) {
                            service = ServiceManager.viewServiceByAlias(msg, provId);
                            if ((service == null) || (service.getAccountId() == null) || (service.getKeyword() == null) || (service.getAccountId().equals("")) || (service.getKeyword().equals(""))) {
                                action = "2";
                            } else {
                                msg = service.getKeyword();
                            }
                        }
                        serviceNames = service.getServiceName();
                        service = null;
                    } catch (Exception ex) {
                        throw new Exception("10001");
                    }
                }
            } else {
                action = "2";
            }
            if (action.equals("2")) {
                ServiceManager.unsubscribeToService(msisdn, "", provId);

                ACK = feedback.getValue("CANCEL_ALL_SUBSCRIPTIONS");
            } else if (action.equals("1")) {
                ArrayList<String> keywords = new ArrayList();
                keywords.add(msg);
                keywords.add(msg + "2");
                ServiceManager.forceUnsubscribe(msisdn, keywords, provId);
            }
            String insertions = "ack=" + ACK + "&keyword=" + kw + "&msg=" + msg + "&msisdn=" + msisdn + "&acctId=" + provId + "&srvcName=" + serviceNames + "&provName=" + provName;
            ACK = URLUTF8Encoder.doMessageEscaping(insertions, ACK);

            if (provId.equals("171") || provId.equals("178") || provId.equals("181") || provId.equals("182") ) {
                Utils.informMASP(msisdn, provId, kw, "38080", "UNSUBSCRIBE", ACK);
            }
            out.println(ACK);



        } catch (Exception e) {
            try {
                if (siteType.equals("2")) {
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

    private String getResponse(InputStream in)
            throws Exception {
        String status = "error";
        String reply = "";
        String error = "";
        String responseString = "";
        BufferedReader br = null;
        try {
            InputStream responseBody = in;
            br = new BufferedReader(new InputStreamReader(responseBody));

            String line = br.readLine();
            while (line != null) {
                responseString = responseString + line;
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new Exception("5002: " + e.getMessage());
        } finally {
            br.close();
            in.close();

            br = null;
            in = null;
        }
        return responseString;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    public void destroy() {
    }

    public void forward(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        doGet(req, resp);
    }

    public void include(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        doGet(req, resp);
    }
}

