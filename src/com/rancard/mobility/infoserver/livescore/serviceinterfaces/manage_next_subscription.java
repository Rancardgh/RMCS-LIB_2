package com.rancard.mobility.infoserver.livescore.serviceinterfaces;

import com.rancard.mobility.common.Driver;
import com.rancard.mobility.common.PushDriver;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.infoserver.common.services.ServiceManager;
import com.rancard.mobility.infoserver.common.services.UserService;
import com.rancard.mobility.infoserver.livescore.LiveScoreServiceManager;
import com.rancard.util.PropertyHolder;
import com.rancard.util.URLUTF8Encoder;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class manage_next_subscription
        extends HttpServlet
        implements RequestDispatcher {
    public void init()
            throws ServletException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println(new Date() + ":@com.rancard.mobility.infoserver.livescore.serviceinterfaces.manage_next_subscription");


        int MILLISECS_BETWEEN_TRANSMITS = 0;
        try {
            String millis = PropertyHolder.getPropsValue("MILLIS_BETWEEN_LS_RE-REG_ATMPT");
            if ((millis != null) && (!millis.equals(""))) {
                MILLISECS_BETWEEN_TRANSMITS = Integer.parseInt(millis);
            } else {
                MILLISECS_BETWEEN_TRANSMITS = 500;
            }
        } catch (Exception e) {
            MILLISECS_BETWEEN_TRANSMITS = 500;
        }
        Date today = new Date();
        String processId = "";

        System.out.println(new Date() + ":renewing subscription for subscribers on MONTHLY_BILLING...");
        try {
            CPConnections cnxn = new CPConnections();


            Map<String, String> accountsReSubKeywordMatrix = ServiceManager.getCPIDsForServiceType("15", "9");

            Iterator iter = accountsReSubKeywordMatrix.keySet().iterator();
            String accountId = "";
            String kw = "";
            while (iter.hasNext()) {
                accountId = (String) iter.next();
                kw = (String) accountsReSubKeywordMatrix.get(accountId);


                int deactivateStatus = LiveScoreServiceManager.manageNextLivescoreSubscription(today, accountId);
                if (deactivateStatus > 0) {
                    String networkPrefix = "";


                    HashMap groupedSubscribers = ServiceManager.viewTempSubscribersGroupByNetworkPrefix(accountId, 0, today);
                    Iterator grpSubsItr = groupedSubscribers.keySet().iterator();
                    while (grpSubsItr.hasNext()) {
                        networkPrefix = (String) grpSubsItr.next();
                        String[] subscribers = (String[]) groupedSubscribers.get(networkPrefix);

                        System.out.println(new Date() + ":getting connection for network:" + networkPrefix + "...");
                        if ((subscribers != null) && (subscribers.length > 0)) {
                            try {
                                cnxn = CPConnections.getConnection(accountId, subscribers[0].toString());
                            } catch (Exception e) {
                                throw new Exception("8002");
                            }
                            String message = "";


                            UserService us = new UserService();
                            String messageTemplate = "";
                            try {
                                us = (UserService) ServiceManager.viewAllServices(accountId, "15", "11").get(0);
                            } catch (Exception e) {
                            }
                            messageTemplate = us.getDefaultMessage();


                            String from = "";
                            UserService lsHeadSrvc = new UserService();
                            try {
                                lsHeadSrvc = LiveScoreServiceManager.viewHeadLiveScoreService(accountId);
                                from = lsHeadSrvc.getKeyword();
                            } catch (Exception e) {
                                from = "406";
                            }
                            System.out.println(new Date() + ":composing resubscription notification message...");

                            String insertions = "lsHeadSrvcName=" + lsHeadSrvc.getServiceName() + "&subscriptionKw=" + kw + "&shortcode=" + lsHeadSrvc.getKeyword() + "&date=" + today;


                            System.out.println(new Date() + ":Message Template:" + messageTemplate);
                            message = message + URLUTF8Encoder.doMessageEscaping(insertions, messageTemplate) + ". ";

                            System.out.println(new Date() + ":final message:" + message);
                            try {
                                Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).sendSMSTextMessage(subscribers, from, message, cnxn.getUsername(), cnxn.getPassword(), cnxn.getConnection(), "", "0");
                            } catch (Exception e) {
                                System.out.println(new Date() + ":Feedback.TRANSPORT_ERROR:" + e.getMessage());
                            }
                            us = null;
                            lsHeadSrvc = null;
                        } else {
                            System.out.println(new Date() + ":No subscribers found for " + networkPrefix);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(new Date() + ":error in Livescore auto monthly re-subscription process:" + e.getMessage());
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
