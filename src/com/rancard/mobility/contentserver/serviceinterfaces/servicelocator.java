package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.DConnect;
import com.rancard.common.Feedback;
import com.rancard.mobility.contentprovider.User;
import com.rancard.mobility.contentserver.CPSite;
import com.rancard.mobility.infoserver.common.services.ServiceManager;
import com.rancard.mobility.infoserver.common.services.UserService;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.*;

public class servicelocator extends HttpServlet implements Filter {

    private FilterConfig filterConfig;
    public static final String BY_SHORTCODE = "1";
    public static final String BY_PROVIDER = "2";
    public static final String EX_NO_SERVICE = "1";
    public static final String EX_HELP_SERVICE = "2";

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    //Process the request/response pair
    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain filterChain) {
        ServletContext context = filterConfig.getServletContext();
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        System.out.println(new Date() + "\t[" + servicelocator.class + "]\tDEBUG\tEntered Service Locator with Query String: " + request.getQueryString());

        // get required request parameters
        String kw = request.getParameter("keyword");
        String msgBody = request.getParameter("msg");
        String msisdn = request.getParameter("msisdn");
        String siteID = request.getParameter("siteId");
        String smscId = req.getParameter("smsc");
        String timeSent = req.getParameter("time");
        String flag = req.getParameter("routeBy");
        String dest = request.getParameter("dest");

        CPSite site = null;
        Feedback feedback = null;


        String searchParam = "";
        String accountID = "";
        try {
            request.setCharacterEncoding("UTF-8");

            if (!checkMsisdn(msisdn)) {
                throw new Exception(Feedback.MISSING_INVALID_MSISDN);
            }

            site = CPSite.viewSite(siteID);
            if (site == null) {
                throw new Exception(Feedback.INVALID_REQUEST_CREDENTIALS);
            }
            request.setAttribute("site", site);

            User sp = new User().viewDealer(site.getCpId());
            request.setAttribute("user", sp);
            if (sp.getDefaultLanguage() == null || sp.getDefaultLanguage().equals("")) {
                sp.setDefaultLanguage("en");
            }

            feedback = (Feedback) context.getAttribute("feedback_" + sp.getDefaultLanguage());
            if (feedback == null) {
                feedback = new Feedback();
            }
            request.setAttribute("default_lang", feedback.getLanguage());

            if (flag == null) {
                searchParam = kw;
            } else {
                if (flag.equals(BY_SHORTCODE)) {
                    searchParam = dest.substring(dest.indexOf("+") + 1);
                } else if (flag.equals(BY_PROVIDER)) {
                    searchParam = site.getCpId();
                } else {
                    searchParam = kw;
                }
            }

            accountID = site.getCpId();
            UserService srvc = ServiceManager.viewService(searchParam, accountID);
            request.setAttribute("user_service", srvc);

            if (srvc == null) {
                if (sp.getDefaultService().startsWith("HELP")) {
                    throw new Exception(com.rancard.util.DefaultService.getHelp(accountID, msisdn, dest, searchParam));
                } else {
                    throw new Exception(com.rancard.common.Feedback.NO_SUCH_SERVICE);
                }
            }

            //Will have to comment out this checck for now. Not currently observed.
            /*if (!srvc.getAllowedSiteTypes().contains(site.getSiteType())) {
                System.out.println(new Date() + "\t[" + servicelocator.class + "]\tERROR\tRequesting site (" + site.getCpSiteId() + ") not in allowed list.");
                throw new Exception(Feedback.INVALID_REQUEST_CREDENTIALS);

            }*/

            request.setAttribute("acctId", srvc.getAccountId());
            request.setAttribute("ack", srvc.getDefaultMessage());
            request.setAttribute("cmd", srvc.getCommand());
            request.setAttribute("attr_keyword", searchParam);
            request.setAttribute("siteId", siteID);

            Map routingTable = ServiceManager.populateRoutingTable();
            String srvcUrl = (String) routingTable.get(srvc.getServiceType());
            if (srvcUrl == null || srvcUrl.equals("")) {
                System.out.println(new Date() + "\t[" + servicelocator.class + "]\tERROR\tNo URL (routing) found for requested service (" + searchParam + ", " + srvc.getAccountId() + ")");
                throw new Exception(Feedback.NO_URL_FOR_SERVICE);
            }

            //using request dispatcher
            RequestDispatcher dispatch;
            try {
                dispatch = request.getRequestDispatcher(srvcUrl);
            } catch (Exception e) {
                System.out.println(new Date() + "\t[" + servicelocator.class + "]\tERROR\tRoute notification failed: " + e.getMessage());
                throw new Exception(Feedback.ROUTE_NOTIFICATION_FAILED);
            }

            dispatch.include(request, response);
            request.setAttribute("dfltMsg", "");

            //finally set the x-kannel-from http header if necessary
            if ((request.getAttribute("x-kannel-header-from") != null) && !request.getAttribute("x-kannel-header-from").equals("")) {
                response.addHeader("X-Kannel-From", (String) request.getAttribute("x-kannel-header-from"));
            } else if (srvc.getServiceResponseSender() != null && !srvc.getServiceResponseSender().equals("")) {
                System.out.println(new java.util.Date() + ": Setting X-Kannel-From header (" + srvc.getServiceResponseSender() + ")");
                response.addHeader("X-Kannel-From", srvc.getServiceResponseSender());
            }

            //set X-Kannel-BInfo http header if available
            if ((request.getAttribute("x-kannel-header-binfo") != null) && !request.getAttribute("x-kannel-header-binfo").equals("")) {
                response.addHeader("X-Kannel-BInfo", (String) request.getAttribute("x-kannel-header-binfo"));
            }

            if ((request.getAttribute("X-Kannel-Coding") != null) && !request.getAttribute("X-Kannel-Coding").equals("")) {
                response.addHeader("X-Kannel-Coding", (String) request.getAttribute("X-Kannel-Coding"));
                response.addHeader("Content-Type", "text/html;charset=UTF-8");
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            String message;
            try {
                if (site.getSiteType().equals(CPSite.SMS)) {
                    message = feedback.getUserFriendlyDescription(e.getMessage());

                    if (message == null || message.equals("")) {
                        message = feedback.getValue(e.getMessage());
                    }
                } else {
                    message = feedback.formDefaultMessage(e.getMessage());
                }
                if (message == null || message.equals("")) {
                    message = e.getMessage();
                }

                //put in insertions
                String insertions = "shortcode=" + dest;
                message = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, message);

                //log statement
                System.out.println(new Date() + "\t[" + servicelocator.class + "]\tERROR\tMessage: " + message + " error: " + e.getMessage());


                request.setAttribute("dfltMsg", message);
                filterChain.doFilter(request, response);
            } catch (ServletException sx) {
                filterConfig.getServletContext().log(sx.getMessage());
            } catch (IOException iox) {
                filterConfig.getServletContext().log(iox.getMessage());
            } catch (Exception ex) {
                filterConfig.getServletContext().log(ex.getMessage());
            }
        } finally {
            String fwdReqKw = (String) request.getAttribute("log_fwdReq_kw");

            try {
                String entireText;
                if (kw.equals("")) {
                    entireText = msgBody;
                } else {
                    entireText = kw + " " + msgBody;
                }


                this.logServiceRequest(searchParam, accountID, msisdn, dest, timeSent, entireText, site.getCpSiteId(), smscId, fwdReqKw, "", "");
            } catch (Exception ex) {
                log(ex.getMessage());
            }

        }
    } //Clean up resources

    public void logServiceRequest(String keyword, String accountId, String origin, String dest, String timeSent, String msg, String siteId, String smscId, String fwd_req_kw) throws Exception {
        Connection conn = null;
        PreparedStatement prepstat = null;

        //log statements
        System.out.println(new Date() + "\t[" + servicelocator.class + "]\tINFO\tUpdating service_usage_log with request details. keyword=" + keyword + ", "
                + "account_id=" + accountId + ", origin=" + origin + ", dest=" + dest + ", timeSent=" + ("".equals(timeSent) ? new java.util.Date() : timeSent) + ", "
                + "msg=" + msg + ", siteId=" + siteId + ", smsc=" + smscId + ", fwd_req_kw=" + fwd_req_kw);


        if (fwd_req_kw != null && !"".equals(fwd_req_kw)) {
            keyword = fwd_req_kw;
        }

        try {
            conn = DConnect.getConnection();
            String query = "insert into service_usage_log (keyword, account_id, to_number, time_received, from_number, msg, site_id, smscId, time_sent) values (?,?,?,?,?,?,?,?,?)";

            prepstat = conn.prepareStatement(query);

            prepstat.setString(1, keyword);
            prepstat.setString(2, accountId);
            prepstat.setString(3, dest);
            prepstat.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
            prepstat.setString(5, origin);
            prepstat.setString(6, msg);
            prepstat.setString(7, siteId);
            prepstat.setString(8, smscId);
            try {
                prepstat.setTimestamp(9, java.sql.Timestamp.valueOf(timeSent));
            } catch (Exception e) {
                System.out.println(new Date() + "\t[" + servicelocator.class + "]\tERROR\tProblem converting timesent: " + e.getMessage());
                prepstat.setTimestamp(9, new java.sql.Timestamp(new java.util.Date().getTime()));
            }
            prepstat.execute();

            //log update success statement
            System.out.println(new Date() + "\t[" + servicelocator.class + "]\tINFO\tservice_usage_log updated successfully!");


        } catch (Exception ex) {
            System.out.println(new java.util.Date() + "\t[" + servicelocator.class + "]\tINFO\tError updating service_usage_log: " + ex.getMessage());
        } finally {
            if (prepstat != null) {
                prepstat.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void logServiceRequest(String keyword, String accountId, String origin, String dest, String timeSent, String msg, String siteId,
            String smscId, String fwd_req_kw, String promoId, String promoRespCode) throws Exception {
        Connection conn = null;
        PreparedStatement prepstat = null;

        //log statements
        //log statements
        System.out.println(new Date() + "\t[" + servicelocator.class + "]\tINFO\tUpdating service_usage_log with request details. keyword=" + keyword + ", "
                + "account_id=" + accountId + ", origin=" + origin + ", dest=" + dest + ", timeSent=" + ("".equals(timeSent) ? new java.util.Date() : timeSent) + ", "
                + "msg=" + msg + ", siteId=" + siteId + ", smsc=" + smscId + ", fwd_req_kw=" + fwd_req_kw);


        if (fwd_req_kw != null && !"".equals(fwd_req_kw)) {
            keyword = fwd_req_kw;
        }

        try {
            conn = DConnect.getConnection();

            String query = "insert into service_usage_log (keyword, account_id, to_number, time_received, from_number, msg, site_id, smscId, time_sent,promo_id,promo_response_code) values (?,?,?,?,?,?,?,?,?,?,?)";

            prepstat = conn.prepareStatement(query);

            prepstat.setString(1, keyword);
            prepstat.setString(2, accountId);
            prepstat.setString(3, dest);
            prepstat.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
            prepstat.setString(5, origin);
            prepstat.setString(6, msg);
            prepstat.setString(7, siteId);
            prepstat.setString(8, smscId);
            try {
                prepstat.setTimestamp(9, java.sql.Timestamp.valueOf(timeSent));
            } catch (Exception e) {
                prepstat.setTimestamp(9, new java.sql.Timestamp(new java.util.Date().getTime()));
            }
            prepstat.setString(10, promoId);
            prepstat.setString(11, promoRespCode);

            prepstat.execute();

            //log update success statement
            System.out.println(new Date() + "\t[" + servicelocator.class + "]\tINFO\tservice_usage_log updated successfully!");


        } catch (Exception ex) {

            System.out.println(new java.util.Date() + ": error updating service_usage_log: " + ex.getMessage());

        } finally {
            if (prepstat != null) {
                prepstat.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

    public boolean checkMsisdn(String msisdn) {
        boolean msisdn_ok = true;

        //Remove '+' symbol if any
        if (msisdn.startsWith("+")) {
            msisdn = msisdn.replace("+", "");
        }
        try {
            Long.parseLong(msisdn);
        } catch (Exception e) {
            msisdn_ok = false;
            System.out.println(new java.util.Date() + ": error in msisdn : " + msisdn + " .Request would be terminated: " + e.getMessage());
        }
        return msisdn_ok;
    }
}
