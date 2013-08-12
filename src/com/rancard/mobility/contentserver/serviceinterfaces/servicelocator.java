package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.DConnect;
import com.rancard.common.Feedback;
import com.rancard.mobility.contentserver.CPSite;
import com.rancard.mobility.infoserver.common.services.ServiceManager;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;


public class servicelocator extends HttpServlet implements Filter {
    private FilterConfig filterConfig;
    private Map routingTable = null;
    private static final String FROM = "RMCS";
    public static final String BY_SHORTCODE = "1";
    public static final String BY_PROVIDER = "2";
    public static final String EX_NO_SERVICE = "1";
    public static final String EX_HELP_SERVICE = "2";
    //Handle the passed-in FilterConfig
    String baseUrl;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        /*try{
        routingTable = (HashMap) filterConfig.getServletContext().getAttribute("routingTable");
        System.out.println(routingTable.toString());
        }catch(Exception e){
        System.out.println("Could not initialize routing table");
        }*/
        try {
            this.routingTable = ServiceManager.populateRoutingTable();
        } catch (Exception e) {
            //cannot initializ e routing table
        }
    }

    //Process the request/response pair
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain filterChain) {

        ServletContext context = filterConfig.getServletContext();
        // setup base path.
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String s = request.getProtocol().toLowerCase();
        s = s.substring(0, s.indexOf("/")).toLowerCase();
        baseUrl = s + "://" + request.getServerName() + request.getContextPath() + "/";
        
        try {
            req.setCharacterEncoding("UTF-8");
            request.setCharacterEncoding ("UTF-8");
        } catch (Exception e) {}

        //ensure that the session is created if not present already
        HttpSession session = request.getSession(true);
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex1) {
            //could not initialize reponse writer
        }

        Feedback feedback = (Feedback) context.getAttribute("feedback_en");
        if (feedback == null) {
            try {
                feedback = new Feedback();
            } catch (Exception e) {
            }
        }

        String accountId = new String();
        CPSite site = null;
        com.rancard.mobility.contentprovider.User sp = null;

        String entireText = "";
        String searchParam = "";
        String defaultLang = "";

        // get required request parameters
        String kw = request.getParameter("keyword");
        String msgBody = request.getParameter("msg");
        String dest = request.getParameter("dest");
        String msisdn = request.getParameter("msisdn");
        String date = request.getParameter("date");
        String phoneId = request.getParameter("phoneId");
        String ua = request.getParameter("ua");
        String siteId = request.getParameter("siteId");
        String regId = request.getParameter("regId");
        String smscId = req.getParameter("smsc");
        String timeSent = req.getParameter("time");
        String flag = req.getParameter("routeBy");
        String promoId = "";
        String promoRespCode = "";
        String defaultSrvc = "";

        //Removing all white spaces in msisdn
        msisdn = msisdn.replaceAll(" ", "");
        boolean msisdn_ok = checkMsisdn(msisdn);
        //log request parameters
        System.out.println(new java.util.Date() + ":@com.rancard.mobility.contentserver.serviceinterfaces.servicelocator..");
        System.out.println(new java.util.Date() + ": Service reqest params: "
                + "keyword=" + kw + ", "
                + "msg=" + msgBody + ", "
                + "dest=" + dest + ", "
                + "msisdn=" + msisdn + ", "
                + "date=" + date + ", "
                + "phoneId=" + phoneId + ", "
                + "ua=" + ua + ", "
                + "siteId=" + siteId + ", "
                + "regId=" + regId + ", "
                + "smsc=" + smscId + ", "
                + "time=" + timeSent + ", "
                + "routeBy=" + flag + ", ");


        if (msgBody == null) {
            msgBody = "";
        }
        if (dest == null) {
            dest = "";
        }
        if (msisdn == null) {
            msisdn = "";
        }
        if (date == null) {
            date = "";
        }
        if (phoneId == null) {
            phoneId = "";
        }
        if (ua == null) {
            ua = "";
        }
        if (regId == null) {
            regId = "";
        }
        if (smscId == null) {
            smscId = "";
        }
        if (timeSent == null) {
            timeSent = "";
        }
        if (siteId == null) {
            siteId = "";
        }
        if (flag == null) {
            flag = "";
        }
        if (kw == null) {
            kw = "";
        }
        //if(promoId == null){promoId = "";}

        try {
            
            try {
                site = CPSite.viewSite(siteId);
                if (site.getCpSiteId() == null || site.getCpSiteId().equals("")) {
                    throw new Exception(Feedback.INVALID_REQUEST_CREDENTIALS);
                }
                if (!msisdn_ok) {
                    throw new Exception(Feedback.MISSING_INVALID_MSISDN);
                }
                accountId = site.getCpId();
                request.setAttribute("site_type", site.getSiteType());
            } catch (Exception e) {
                //throw new Exception(Feedback.INVALID_REQUEST_CREDENTIALS);
                if (!msisdn_ok) {
                    throw new Exception(Feedback.MISSING_INVALID_MSISDN);
                }else
                throw new Exception(Feedback.INVALID_REQUEST_CREDENTIALS);
            }

            try {
                sp = new com.rancard.mobility.contentprovider.User().viewDealer(accountId);
                defaultSrvc = sp.getDefaultService();

                String sp_lang = "";
                if (sp.getDefaultLanguage() == null || sp.getDefaultLanguage().equals("")) {
                    sp_lang = "en";
                } else {
                    sp_lang = sp.getDefaultLanguage();
                }
                feedback = (Feedback) context.getAttribute("feedback_" + sp_lang);
                if (feedback == null) {
                    feedback = new Feedback();
                }
                request.setAttribute("default_lang", feedback.getLanguage());
            } catch (Exception e) {
                throw new Exception(Feedback.INVALID_REQUEST_CREDENTIALS);
            }

            if (flag.equals(BY_SHORTCODE)) {
                //kw = dest.substring (dest.indexOf ("+") + 1);
                searchParam = dest.substring(dest.indexOf("+") + 1);
            } else if (flag.equals(BY_PROVIDER)) {
                //kw = accountId;
                searchParam = accountId;
            } else {
                searchParam = kw;
            }

            if (searchParam == null) {
                throw new Exception(Feedback.NO_SUCH_SERVICE);
            }

            //no site ID specified
            if (siteId == null || siteId.equals("")) {
                throw new Exception(Feedback.INVALID_REQUEST_CREDENTIALS);
            }

            //search for service
            com.rancard.mobility.infoserver.common.services.UserService srvc = null;
            String serviceExeptionFlag = EX_NO_SERVICE;
            try {
                srvc = com.rancard.mobility.infoserver.common.services.ServiceManager.viewService(searchParam, accountId);
                if (srvc.getKeyword() == null || srvc.getKeyword().equals("")) {
                    // carry out internet search//

                    if (sp.getDefaultService().startsWith("HELP")) {
                        serviceExeptionFlag = EX_HELP_SERVICE;
                        throw new Exception(com.rancard.util.DefaultService.getHelp(accountId, msisdn, dest, searchParam));
                    } else {
                        // if nothing then return  no such service error
                        serviceExeptionFlag = EX_NO_SERVICE;
                        throw new Exception(com.rancard.common.Feedback.NO_SUCH_SERVICE);
                    }
                }
                request.setAttribute("thisService", srvc);
            } catch (Exception e) {
                if (serviceExeptionFlag.equals(EX_HELP_SERVICE)) {
                    throw new Exception(e.getMessage());
                } else {
                    throw new Exception(com.rancard.common.Feedback.NO_SUCH_SERVICE);
                }
            }

            //access valdation
            if (srvc.getAllowedSiteTypes() != null && !srvc.getAllowedSiteTypes().equals("")) {
                java.util.List allowedSites = java.util.Arrays.asList(srvc.getAllowedSiteTypes().split(","));

                if (!allowedSites.contains("" + site.getSiteType())) {

                    System.out.println(new java.util.Date() + ": requesting site (" + site.getCpSiteId() + ") not in allowed list.");
                    throw new Exception(Feedback.INVALID_REQUEST_CREDENTIALS);

                }

                if (site.getSiteType().equals(CPSite.SMS)) {
                    String allowedShortcodes = srvc.getAllowedShortcodes();
                    String tempDest = dest.substring(dest.indexOf("+") + 1);

                    if (allowedShortcodes != null && !allowedShortcodes.equals("")) {
                        java.util.List as = java.util.Arrays.asList(allowedShortcodes.split(","));

                        if (!as.contains(tempDest)) {
                            //log statment
                            System.out.println(new java.util.Date() + ": requested shortcode (" + tempDest + ") not in allowed list.");

                            throw new Exception(Feedback.INVALID_SERVICE_REQUEST);
                        }
                    }
                }
            }
            
            String acknowledgement = srvc.getDefaultMessage();

            request.setAttribute("acctId", accountId);
            request.setAttribute("ack", acknowledgement);
            request.setAttribute("cmd", srvc.getCommand());
            request.setAttribute("attr_keyword", searchParam);

            //get service url
            String srvcUrl = (String) routingTable.get(srvc.getServiceType());
            if (srvcUrl == null || srvcUrl.equals("")) {

                System.out.println(new java.util.Date() + ": no URL (routing) found for requested service (" + searchParam + ", " + accountId + ")");

                throw new Exception(Feedback.NO_URL_FOR_SERVICE);
            }

            //using request dispatcher
            RequestDispatcher dispatch = null;
            try {
                dispatch = request.getRequestDispatcher(srvcUrl);
            } catch (Exception e) {
                throw new Exception(Feedback.ROUTE_NOTIFICATION_FAILED);
            }

            dispatch.include(request, response);

            request.setAttribute("dfltMsg", "");

            //Retrieving promoId and promo_response_code for the purpose of logging
            promoId = (String) request.getAttribute("promoId");
            promoRespCode = (String) request.getAttribute("promoRespCode");

            //finally set the x-kannel-from http header if necessary
            if ((request.getAttribute("x-kannel-header-from") != null) && !((String) request.getAttribute("x-kannel-header-from")).equals("")) {
                response.addHeader("X-Kannel-From", (String) request.getAttribute("x-kannel-header-from"));
            } else if (srvc.getServiceResponseSender() != null && !srvc.getServiceResponseSender().equals("")) {
                System.out.println(new java.util.Date() + ": Setting X-Kannel-From header (" + srvc.getServiceResponseSender() + ")");
                response.addHeader("X-Kannel-From", srvc.getServiceResponseSender());
            }

            //set X-Kannel-BInfo http header if available
            if ((request.getAttribute("x-kannel-header-binfo") != null) && !((String) request.getAttribute("x-kannel-header-binfo")).equals("")) {
                response.addHeader("X-Kannel-BInfo", (String) request.getAttribute("x-kannel-header-binfo"));
            }
            
            if ((request.getAttribute("X-Kannel-Coding") != null) && !((String) request.getAttribute("X-Kannel-Coding")).equals("")) {
                response.addHeader ("X-Kannel-Coding", (String) request.getAttribute("X-Kannel-Coding"));
                response.addHeader ("Content-Type", "text/html;charset=UTF-8");
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            String message = "";
            try {
                if (site.getSiteType().equals(site.SMS)) {
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
                System.out.println(new java.util.Date() + ": error: " + message + ":" + e.getMessage());


                request.setAttribute("dfltMsg", message);
                filterChain.doFilter(request, response);
            } catch (ServletException sx) {
                filterConfig.getServletContext().log(sx.getMessage());
            } catch (IOException iox) {
                filterConfig.getServletContext().log(iox.getMessage());
            } catch (Exception ex) {
                filterConfig.getServletContext().log(ex.getMessage());
            }
            //return;
        } finally {

            //get extra service loging params
            String fwdReqKw = (String) request.getAttribute("log_fwdReq_kw");
            //String thirdPartyCPId = (String)request.getAttribute("log_thirdPartyCPId");


            try {
                if (kw.equals("")) {
                    entireText = msgBody;
                } else {
                    entireText = kw + " " + msgBody;
                }


                this.logServiceRequest(searchParam, accountId, msisdn, dest, timeSent, entireText, siteId, smscId, fwdReqKw, promoId, promoRespCode);
            } catch (Exception ex) {
                log(ex.getMessage());
            }

        }
    } //Clean up resources

    public void logServiceRequest(String keyword, String accountId, String origin, String dest, String timeSent, String msg, String siteId, String smscId, String fwd_req_kw) throws Exception {

        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        //log statements
        System.out.println(new java.util.Date() + ": updating service_usage_log with request details..");
        System.out.println(new java.util.Date() + ": service_usage_log details: "
                + "keyword=" + keyword + ", "
                + "account_id=" + accountId + ", "
                + "origin=" + origin + ", "
                + "dest=" + dest + ", "
                + "timeSent=" + ("".equals(timeSent) ? new java.util.Date() : timeSent) + ", "
                + "msg=" + msg + ", "
                + "siteId=" + siteId + ", "
                + "smsc=" + smscId + ", "
                + "fwd_req_kw=" + fwd_req_kw);

        if (fwd_req_kw != null && !"".equals(fwd_req_kw)) {
            keyword = fwd_req_kw;
        }

        try {
            con = DConnect.getConnection();

            query = "insert into service_usage_log (keyword, account_id, to_number, time_received, from_number, msg, site_id, smscId, time_sent) values (?,?,?,?,?,?,?,?,?)";

            prepstat = con.prepareStatement(query);

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


            prepstat.execute();

            //log update success statement
            System.out.println(new java.util.Date() + ": service_usage_log updated successfully!");


        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ex1) {
                    log(ex.getMessage());
                }
                con = null;
            }

            System.out.println(new java.util.Date() + ": error updating service_usage_log: " + ex.getMessage());

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    log(e.getMessage());
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (Exception e) {
                    log(e.getMessage());
                    ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    log(e.getMessage());
                    ;
                }
                con = null;
            }
        }
    }

    public void logServiceRequest(String keyword, String accountId, String origin, String dest, String timeSent, String msg, String siteId, String smscId, String fwd_req_kw, String promoId, String promoRespCode) throws Exception {

        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;

        //log statements
        System.out.println(new java.util.Date() + ": updating service_usage_log with request details..");
        System.out.println(new java.util.Date() + ": service_usage_log details: "
                + "keyword=" + keyword + ", "
                + "account_id=" + accountId + ", "
                + "origin=" + origin + ", "
                + "dest=" + dest + ", "
                + "timeSent=" + ("".equals(timeSent) ? new java.util.Date() : timeSent) + ", "
                + "msg=" + msg + ", "
                + "promoId=" + promoId + ", "
                + "promoRespCode=" + promoRespCode + ", "
                + "siteId=" + siteId + ", "
                + "smsc=" + smscId + ", "
                + "fwd_req_kw=" + fwd_req_kw);

        if (fwd_req_kw != null && !"".equals(fwd_req_kw)) {
            keyword = fwd_req_kw;
        }

        try {
            con = DConnect.getConnection();

            query = "insert into service_usage_log (keyword, account_id, to_number, time_received, from_number, msg, site_id, smscId, time_sent,promo_id,promo_response_code) values (?,?,?,?,?,?,?,?,?,?,?)";

            prepstat = con.prepareStatement(query);

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
            System.out.println(new java.util.Date() + ": service_usage_log updated successfully!");


        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ex1) {
                    log(ex.getMessage());
                }
                con = null;
            }

            System.out.println(new java.util.Date() + ": error updating service_usage_log: " + ex.getMessage());

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    log(e.getMessage());
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close();
                } catch (Exception e) {
                    log(e.getMessage());
                    ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    log(e.getMessage());
                    ;
                }
                con = null;
            }
        }
    }

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
            System.out.println(new java.util.Date() + ": error in msisdn : " + msisdn + " .Request would be terminated.");
        }
        return msisdn_ok;
    }
}
