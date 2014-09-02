package com.rancard.mobility.infoserver.livescore.serviceinterfaces;

import com.rancard.mobility.common.Driver;
import com.rancard.mobility.common.PushDriver;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.infoserver.livescore.LiveScoreFixture;
import com.rancard.mobility.infoserver.livescore.LiveScoreServiceManager;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class leaguelevelquery extends HttpServlet implements RequestDispatcher {
    public void init()
            throws ServletException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        String subscriber = request.getParameter("msisdn");
        String team = request.getParameter("msg");
        String accountId = (String) request.getAttribute("acctId");
        String keyword = request.getParameter("keyword");
        String ack = (String) request.getAttribute("ack");
        if (ack == null) {
            ack = "Your request is being processed";
        }
        Calendar calendar = Calendar.getInstance();
        java.sql.Date today = new java.sql.Date(calendar.getTime().getTime());
        String dateString = today.toString();
        ArrayList fixtures = null;

        CPConnections cnxn = new CPConnections();
        System.out.println("Event notification");
        try {
            if ((subscriber == null) || (subscriber.equals(""))) {
                throw new Exception("2000");
            }
            if ((accountId == null) || (accountId.equals(""))) {
                throw new Exception("10004");
            }
            if ((keyword == null) || (keyword.equals(""))) {
                throw new Exception("10001");
            }
            if ((team == null) || (team.equals(""))) {
                fixtures = LiveScoreServiceManager.viewAllActiveFixturesInLeague(dateString, keyword, accountId);
            } else {
                fixtures = LiveScoreServiceManager.viewAllActiveFixturesInLeague(dateString, keyword, accountId, team);
            }
            try {
                cnxn = CPConnections.getConnection(accountId, subscriber);
            } catch (Exception e) {
                throw new Exception("8002");
            }
            String message = "";
            if ((fixtures != null) && (fixtures.size() != 0)) {
                out.println(ack);
                for (int k = 0; k < fixtures.size(); k++) {
                    LiveScoreFixture game = (LiveScoreFixture) fixtures.get(k);

                    message = "Send OK " + game.getGameId() + " to 406 for live updates for " + game.getHomeTeam() + " vs " + game.getAwayTeam() + " on " + new java.util.Date(Timestamp.valueOf(game.getDate()).getTime()).toString();
                    try {
                        Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).sendSMSTextMessage(subscriber, "LiveScore", message, cnxn.getUsername(), cnxn.getPassword(), cnxn.getConnection(), "", "0");
                    } catch (Exception e) {
                        System.out.println("5002");
                        throw new Exception("5002");
                    }
                }
            } else {
                out.println("There are no active games today.");
            }
        } catch (Exception e) {
            out.println(e.getMessage().substring(e.getMessage().indexOf(":") + 1));
        } finally {
            today = null;
            fixtures = null;
            cnxn = null;
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
