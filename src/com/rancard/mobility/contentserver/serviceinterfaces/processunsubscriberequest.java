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
import com.rancard.mobility.common.ThreadedMessageSender;
import com.rancard.mobility.contentprovider.User;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.contentserver.CPSite;
import com.rancard.mobility.contentserver.CPSiteDB;
import com.rancard.mobility.infoserver.common.services.ServiceSubscriptionDB;
import com.rancard.mobility.infoserver.common.services.UserService;
import com.rancard.mobility.infoserver.common.services.UserServiceDB;
import com.rancard.mobility.infoserver.common.services.UserServiceExperience;
import com.rancard.mobility.infoserver.common.services.UserServiceExperienceDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Messenger
 */
public class processunsubscriberequest extends HttpServlet implements RequestDispatcher {

    //Process the HTTP Get request
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get reeponse writer
        PrintWriter out = response.getWriter();
        String ACK = (String) request.getAttribute("ack");
        String kw = request.getParameter("keyword");
        String msg = request.getParameter("msg");
        String msisdn = request.getParameter("msisdn");
        String provId = (String) request.getAttribute("acctId");
        String dest = request.getParameter("dest");

        String siteID = (String) request.getAttribute("siteId");
        String lang = (String) request.getAttribute("default_lang");
        if (lang == null || lang.equals("")) {
            lang = "en";
        }
        CPSite site = null;
        Feedback feedback = null;
        try {
            System.out.println(new Date() + "\t[" + processunsubscriberequest.class + "]\tDEBUG\tCancelling subscription to a service: " + request.getQueryString());
            feedback = (Feedback) this.getServletContext().getAttribute("feedback_" + lang);
            if (feedback == null) {
                feedback = new Feedback();
            }

            site = (CPSite) request.getAttribute("site");
            if (site == null) {
                site = CPSiteDB.viewSite(siteID);
            }

            if (site == null) {
                out.println(feedback.getUserFriendlyDescription(Feedback.INVALID_REQUEST_CREDENTIALS));
                return;
            }

            if (provId == null || provId.equals("")) {
                if (site.getSiteType().equals(CPSite.SMS)) {
                    out.println(feedback.getUserFriendlyDescription(Feedback.MISSING_INVALID_PROV_ID));
                } else {
                    out.println(feedback.formDefaultMessage(Feedback.MISSING_INVALID_PROV_ID));
                }
                return;
            }

            if (msisdn == null || msisdn.equals("")) {
                if (site.getSiteType().equals(CPSite.SMS)) {
                    out.println(feedback.getUserFriendlyDescription(Feedback.MISSING_INVALID_MSISDN));
                } else {
                    out.println(feedback.formDefaultMessage(Feedback.MISSING_INVALID_MSISDN));
                }
                return;
            }

            msisdn = formatMSISDN(msisdn);
            if (!checkMSISDN(msisdn.substring(1))) {
                if (site.getSiteType().equals(CPSite.SMS)) {
                    out.println(feedback.getUserFriendlyDescription(Feedback.MISSING_INVALID_MSISDN));
                } else {
                    out.println(feedback.formDefaultMessage(Feedback.MISSING_INVALID_MSISDN));
                }
                return;
            }

            User user = (User) request.getAttribute("user");
            if (user == null) {
                user = new User().viewDealer(provId);
            }


            String insertions = "ack=" + ACK + "&keyword=" + kw + "&msg=" + msg + "&msisdn=" + msisdn + "&acctId=" + provId + "&provName=" + user.getName();

            if (msg == null || msg.equals("")) {
                out.println(unsubscribeAll(msisdn, provId, feedback, insertions));
            } else {
                List<String> keywords = Arrays.asList(msg.split(","));

                if (keywords.size() == 1) {
                    UserService service = UserServiceDB.viewService(keywords.get(0), provId);
                    if (service == null) {
                        out.println(unsubscribeAll(msisdn, provId, feedback, insertions));
                        return;
                    }

                    ServiceSubscriptionDB.deleteSubscription(msisdn, service.getAccountId(), service.getKeyword());
                    insertions = insertions + "&srvcName=" + service.getServiceName();

                    UserServiceExperience serviceExp = UserServiceExperienceDB.viewServiceExperience(service.getAccountId(),
                            site.getCpSiteId(), service.getKeyword());

                    if (serviceExp == null || serviceExp.getUnsubscriptionConfirmationMsg() == null || serviceExp.getUnsubscriptionConfirmationMsg().equals("")) {
                        UserService stopService = UserServiceDB.viewService("STOP", service.getAccountId());
                        if (stopService == null || stopService.getDefaultMessage() == null || stopService.getDefaultMessage().equals("")) {
                            out.println("You have successfully cancelled your subscription. Thank you for using our service.");
                            return;
                        }

                        String sender = stopService.getServiceResponseSender() == null
                                || stopService.getServiceResponseSender().equals("") ? dest : stopService.getServiceResponseSender();
                        request.setAttribute("x-kannel-header-from", sender);
                        out.println(com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, stopService.getDefaultMessage()));
                        return;
                    }

                    String sender = serviceExp.getUnsubscriptionConfirmationMsgSender() == null || serviceExp.getUnsubscriptionConfirmationMsgSender().equals("")
                            ? dest : serviceExp.getUnsubscriptionConfirmationMsgSender();
                    request.setAttribute("x-kannel-header-from", sender);
                    out.println(com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, serviceExp.getUnsubscriptionConfirmationMsg()));
                    return;

                }

                ServiceSubscriptionDB.deleteSubscription(msisdn, provId, keywords);
                List<UserService> services = UserServiceDB.viewService(keywords, provId);
                CPConnections cnxn = CPConnections.getConnection(provId, msisdn);
                String stopMessage;
                for (int i = 0; i < services.size(); i++) {

                    UserServiceExperience serviceExp = UserServiceExperienceDB.viewServiceExperience(services.get(i).getAccountId(),
                            site.getCpSiteId(), services.get(i).getKeyword());
                    String sender = serviceExp.getUnsubscriptionConfirmationMsgSender() == null
                            || serviceExp.getUnsubscriptionConfirmationMsgSender().equals("") ? dest : serviceExp.getUnsubscriptionConfirmationMsgSender();

                    if (serviceExp == null || serviceExp.getUnsubscriptionConfirmationMsg() == null || serviceExp.getUnsubscriptionConfirmationMsg().equals("")) {
                        UserService stopService = UserServiceDB.viewService("STOP", services.get(i).getAccountId());
                        if (stopService == null || stopService.getDefaultMessage() == null || stopService.getDefaultMessage().equals("")) {
                            stopMessage = "You have successfully cancelled your subscription. Thank you for using our service.";
                        } else {
                            stopMessage = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, stopService.getDefaultMessage());
                        }
                    } else {
                        stopMessage = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, serviceExp.getUnsubscriptionConfirmationMsg());
                    }

                    if (i == services.size() - 1) {
                        request.setAttribute("x-kannel-header-from", sender);
                        out.println(stopMessage);
                        return;
                    } else {

                        new Thread(new ThreadedMessageSender(cnxn, msisdn, sender, stopMessage, 0)).start();
                    }
                }
            }


        } catch (Exception e) {
            System.out.println(new Date() + "\t[" + processunsubscriberequest.class + "]\tERROR\tCancelling subscription to a service: " + e.getMessage());
            String message;
            try {

                if (site.getSiteType().equals(CPSite.SMS)) {
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
        }
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws
            ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public void forward(ServletRequest request,
            ServletResponse response) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        doGet(req, resp);
    }

    @Override
    public void include(ServletRequest request,
            ServletResponse response) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        doGet(req, resp);
    }

    private String formatMSISDN(String msisdn) {
        if (msisdn.indexOf("+") == -1) {
            return "+" + msisdn;
        }

        return msisdn;
    }

    private boolean checkMSISDN(String msisdn) {
        try {
            Long.parseLong(msisdn);
            return true;


        } catch (NumberFormatException nfe) {
            System.out.println(new Date() + "\t[" + processunsubscriberequest.class
                    + "]\tERROR\tMSISDN is not correct: " + nfe.getMessage());

            return false;
        }
    }

    private String unsubscribeAll(String msisdn, String accountID, Feedback feedback, String insertions) throws Exception {
        System.out.println(new Date() + "\t[" + processunsubscriberequest.class + "]\tINFO\tMessage is null. Will unsubscribe all");
        ServiceSubscriptionDB.deleteSubscription(msisdn, accountID);

        String message = feedback.getValue("CANCEL_ALL_SUBSCRIPTIONS");
        return com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, message);
    }
}
