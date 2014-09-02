package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.Feedback;
import com.rancard.mobility.common.Driver;
import com.rancard.mobility.common.PushDriver;
import com.rancard.mobility.contentprovider.User;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.infoserver.common.services.ServiceManager;
import com.rancard.mobility.infoserver.common.services.UserService;
import com.rancard.util.URLUTF8Encoder;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class processsubscriberequest
        extends HttpServlet
        implements RequestDispatcher {
    public void init()
            throws ServletException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        CPConnections cnxn = new CPConnections();
        String[] regId = new String[2];

        String ACK = (String) request.getAttribute("ack");
        String kw = request.getParameter("keyword");
        String msg = request.getParameter("msg");
        String msisdn = request.getParameter("msisdn");
        String provId = (String) request.getAttribute("acctId");
        String subsPeriodStr = request.getParameter("subs_period");
        if ((provId == null) || (provId.equals(""))) {
            provId = request.getParameter("acctId");
        }
        String cmd = (String) request.getAttribute("cmd");
        if ((cmd == null) || (cmd.equals(""))) {
            cmd = "0";
        }
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
        String message = "";
        String provName = "";
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
        System.out.println(new Date() + ":Received request to subscribe to a service");
        System.out.println(new Date() + ":Keyword: " + kw);
        System.out.println(new Date() + ":Message: " + msg);
        System.out.println(new Date() + ":Subscriber's number: " + msisdn);
        System.out.println(new Date() + ":Service privoder's ID: " + provId);


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
                    System.out.println(new Date() + ":MISSING or INVALID_MSISDN: " + number);
                    message = feedback.getUserFriendlyDescription("2000");
                } else {
                    System.out.println(new Date() + ":MISSING or INVALID_MSISDN: " + number);
                    message = feedback.formDefaultMessage("2000");
                }
            } catch (Exception ex) {
                System.out.println(new Date() + ":ERROR: " + ex.getMessage());
                message = ex.getMessage();
            }
            out.println(message);
            return;
        }
        try {
            try {
                cnxn = CPConnections.getConnection(provId, msisdn);
            } catch (Exception e) {
                throw new Exception("8002");
            }
            ArrayList keywords = new ArrayList();
            String resp = new String();

            String serviceNames = "";
            if ((msg != null) && (!msg.equals(""))) {
                if (!keywords.contains(msg)) {
                    keywords.add(msg);
                }
            } else {
                keywords = ServiceManager.getKeywordsOfBasicServices(provId);
            }
            try {
                serviceNames = ServiceManager.viewService(keywords.get(0).toString(), provId).getServiceName();
                for (int i = 1; i < keywords.size(); i++) {
                    serviceNames = serviceNames + ", " + ServiceManager.viewService(keywords.get(i).toString(), provId).getServiceName();
                }
                StringBuffer sb = new StringBuffer(serviceNames);
                if (serviceNames.lastIndexOf(",") != -1) {
                    sb.replace(serviceNames.lastIndexOf(","), serviceNames.lastIndexOf(",") + 1, " and");
                }
                serviceNames = sb.toString();
                sb = null;
            } catch (Exception ex) {
                throw new Exception("10001");
            }
            int numOfDays = 0;
            if ((subsPeriodStr != null) && (!"".equals(subsPeriodStr))) {
                numOfDays = Integer.parseInt(subsPeriodStr);

                ServiceManager.subscribeToService(msisdn, keywords, provId, numOfDays);
            } else {
                ServiceManager.subscribeToService(msisdn, keywords, provId);
            }
            System.out.println("Subscription completed.");


            provName = new User().viewDealer(provId).getName();
            String insertions = "ack=" + ACK + "&keyword=" + kw + "&msg=" + msg + "&msisdn=" + msisdn + "&provName=" + provName + "&pin=" + regId[0] + "&srvcName=" + serviceNames;
            ACK = URLUTF8Encoder.doMessageEscaping(insertions, ACK);
            if (regId[1] == null) {
                message = ACK;
            } else {
                message = regId[1] + " " + ACK + " ";
            }
            if (cmd.equals("0")) {
                out.println(message);
            } else if (cmd.equals("1")) {
                try {
                    Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).sendSMSTextMessage(msisdn, provName, message, cnxn.getUsername(), cnxn.getPassword(), cnxn.getConnection(), "", "0");
                } catch (Exception e) {
                    System.out.println("5002");
                    throw new Exception("5002");
                }
            }
            keywords = null;
            cnxn = null;
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
            if (cmd.equals("0")) {
                out.println(message);
            } else if (cmd.equals("1")) {
                try {
                    Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).sendSMSTextMessage(msisdn, provName, message, cnxn.getUsername(), cnxn.getPassword(), cnxn.getConnection(), "", "0");
                } catch (Exception ex) {
                    System.out.println("5002");
                }
            }
            return;
        }
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



/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar

 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.processsubscriberequest

 * JD-Core Version:    0.7.0.1

 */