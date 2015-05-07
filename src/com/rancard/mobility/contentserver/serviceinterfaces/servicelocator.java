package com.rancard.mobility.contentserver.serviceinterfaces;

import com.rancard.common.DConnect;
import com.rancard.common.Feedback;
import com.rancard.mobility.contentprovider.User;
import com.rancard.mobility.contentserver.CPSite;
import com.rancard.mobility.infoserver.common.services.ServiceManager;
import com.rancard.mobility.infoserver.common.services.UserService;
import com.rancard.rndvu.events.UserEvents;
import com.rancard.util.DefaultService;
import com.rancard.util.URLUTF8Encoder;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class servicelocator
        extends HttpServlet
        implements Filter {
    private FilterConfig filterConfig;
    private Map routingTable = null;
    private static final String FROM = "RMCS";
    public static final String BY_SHORTCODE = "1";
    public static final String BY_PROVIDER = "2";
    public static final String EX_NO_SERVICE = "1";
    public static final String EX_HELP_SERVICE = "2";
    String baseUrl;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        try {
            this.routingTable = ServiceManager.populateRoutingTable();
        } catch (Exception e) {
        }
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) {
        ServletContext context = this.filterConfig.getServletContext();

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String s = request.getProtocol().toLowerCase();
        s = s.substring(0, s.indexOf("/")).toLowerCase();
        String baseUrl = s + ":" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        try {
            req.setCharacterEncoding("UTF-8");
            request.setCharacterEncoding("UTF-8");
        } catch (Exception e) {
        }
        HttpSession session = request.getSession(true);
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex1) {
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
        User sp = null;

        String entireText = "";
        String searchParam = "";
        String defaultLang = "";


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


        msisdn = msisdn.replaceAll(" ", "");
        boolean msisdn_ok = checkMsisdn(msisdn);

        System.out.println(new Date() + ":@com.rancard.mobility.contentserver.serviceinterfaces.servicelocator..");
        System.out.println(new Date() + ": Service reqest params: " + "keyword=" + kw + ", " + "msg=" + msgBody + ", " + "dest=" + dest + ", " + "msisdn=" + msisdn + ", " + "date=" + date + ", " + "phoneId=" + phoneId + ", " + "ua=" + ua + ", " + "siteId=" + siteId + ", " + "regId=" + regId + ", " + "smsc=" + smscId + ", " + "time=" + timeSent + ", " + "routeBy=" + flag + ", ");
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
        try {
            try {
                site = CPSite.viewSite(siteId);
                if ((site.getCpSiteId() == null) || (site.getCpSiteId().equals(""))) {
                    throw new Exception("4001");
                }
                if (!msisdn_ok) {
                    throw new Exception("2000");
                }
                accountId = site.getCpId();
                request.setAttribute("site_type", site.getSiteType());
            } catch (Exception e) {
                if (!msisdn_ok) {
                    throw new Exception("2000");
                }
                throw new Exception("4001");
            }
            try {
                sp = new User().viewDealer(accountId);
                defaultSrvc = sp.getDefaultService();

                String sp_lang = "";
                if ((sp.getDefaultLanguage() == null) || (sp.getDefaultLanguage().equals(""))) {
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
                throw new Exception("4001");
            }
            if (flag.equals("1")) {
                searchParam = dest.substring(dest.indexOf("+") + 1);
            } else if (flag.equals("2")) {
                searchParam = accountId;
            } else {
                searchParam = kw;
            }
            if (searchParam == null) {
                throw new Exception("10001");
            }
            if ((siteId == null) || (siteId.equals(""))) {
                throw new Exception("4001");
            }
            UserService srvc = null;
            String serviceExeptionFlag = "1";
            try {
                srvc = ServiceManager.viewService(searchParam, accountId);
                if ((srvc.getKeyword() == null) || (srvc.getKeyword().equals(""))) {
                    // Log Rendezvous HELP/SEARCH here
                    
//                    boolean doLogging = new Config().doGraphLogging();
//                    if (doLogging){
                        final String rndvuMsisdn = msisdn;
                        final String clientId = "74nc4r6rn6vu";
                        final String searchString = searchParam;
                        // Log User HELP/SEARCH action
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try{
                                    UserEvents.help(rndvuMsisdn, clientId, searchString);
                                } catch (Exception ex){
                                    System.out.println(new java.util.Date()+"\tERROR\t[servicelocator]\t"+rndvuMsisdn+"\tError while writing User action [SEARCH] to RNDVU Graph: "+ex.getMessage());
                                }
                            }
                        }).start();
                    //}
                    if (sp.getDefaultService().startsWith("HELP")) {
                        serviceExeptionFlag = "2";
                        throw new Exception(DefaultService.getHelp(accountId, msisdn, dest, searchParam));
                    }
                    serviceExeptionFlag = "1";
                    throw new Exception("10001");
                }
                request.setAttribute("thisService", srvc);
            } catch (Exception e) {
                if (serviceExeptionFlag.equals("2")) {
                    throw new Exception(e.getMessage());
                }
                throw new Exception("10001");
            }
            if ((srvc.getAllowedSiteTypes() != null) && (!srvc.getAllowedSiteTypes().equals(""))) {
                List allowedSites = Arrays.asList(srvc.getAllowedSiteTypes().split(","));
                if (!allowedSites.contains("" + site.getSiteType())) {
                    System.out.println(new Date() + ": requesting site (" + site.getCpSiteId() + ") not in allowed list.");
                    throw new Exception("4001");
                }
                if (site.getSiteType().equals("2")) {
                    String allowedShortcodes = srvc.getAllowedShortcodes();
                    String tempDest = dest.substring(dest.indexOf("+") + 1);
                    if ((allowedShortcodes != null) && (!allowedShortcodes.equals(""))) {
                        List as = Arrays.asList(allowedShortcodes.split(","));
                        if (!as.contains(tempDest)) {
                            System.out.println(new Date() + ": requested shortcode (" + tempDest + ") not in allowed list.");

                            throw new Exception("10003");
                        }
                    }
                }
            }
            String acknowledgement = srvc.getDefaultMessage();

            request.setAttribute("acctId", accountId);
            request.setAttribute("ack", acknowledgement);
            request.setAttribute("cmd", srvc.getCommand());
            request.setAttribute("attr_keyword", searchParam);


            String srvcUrl = (String) this.routingTable.get(srvc.getServiceType());
            if ((srvcUrl == null) || (srvcUrl.equals(""))) {
                System.out.println(new Date() + ": no URL (routing) found for requested service (" + searchParam + ", " + accountId + ")");

                throw new Exception("10002");
            }
            RequestDispatcher dispatch = null;
            try {
                dispatch = request.getRequestDispatcher(srvcUrl);
            } catch (Exception e) {
                throw new Exception("8000");
            }
            dispatch.include(request, response);

            request.setAttribute("dfltMsg", "");


            promoId = (String) request.getAttribute("promoId");
            promoRespCode = (String) request.getAttribute("promoRespCode");
            if ((request.getAttribute("x-kannel-header-from") != null) && (!((String) request.getAttribute("x-kannel-header-from")).equals(""))) {
                response.addHeader("X-Kannel-From", (String) request.getAttribute("x-kannel-header-from"));
            } else if ((srvc.getServiceResponseSender() != null) && (!srvc.getServiceResponseSender().equals(""))) {
                System.out.println(new Date() + ": Setting X-Kannel-From header (" + srvc.getServiceResponseSender() + ")");
                response.addHeader("X-Kannel-From", srvc.getServiceResponseSender());
            }
            if ((request.getAttribute("x-kannel-header-binfo") != null) && (!((String) request.getAttribute("x-kannel-header-binfo")).equals(""))) {
                response.addHeader("X-Kannel-BInfo", (String) request.getAttribute("x-kannel-header-binfo"));
            }
            if ((request.getAttribute("X-Kannel-Coding") != null) && (!((String) request.getAttribute("X-Kannel-Coding")).equals(""))) {
                response.addHeader("X-Kannel-Coding", (String) request.getAttribute("X-Kannel-Coding"));
                response.addHeader("Content-Type", "text/html;charset=UTF-8");
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            String fwdReqKw;
            String message = "";
            try {
                if (site.getSiteType().equals("2")) {
                    message = feedback.getUserFriendlyDescription(e.getMessage());
                    if ((message == null) || (message.equals(""))) {
                        message = feedback.getValue(e.getMessage());
                    }
                } else {
                    message = feedback.formDefaultMessage(e.getMessage());
                }
                if ((message == null) || (message.equals(""))) {
                    message = e.getMessage();
                }
                String insertions = "shortcode=" + dest;
                message = URLUTF8Encoder.doMessageEscaping(insertions, message);


                System.out.println(new Date() + ": error: " + message + ":" + e.getMessage());


                request.setAttribute("dfltMsg", message);
                filterChain.doFilter(request, response);
            } catch (ServletException sx) {
                this.filterConfig.getServletContext().log(sx.getMessage());
            } catch (IOException iox) {
                this.filterConfig.getServletContext().log(iox.getMessage());
            } catch (Exception ex) {
                this.filterConfig.getServletContext().log(ex.getMessage());
            }
            e.printStackTrace();
        } finally {
            String fwdReqKw = (String) request.getAttribute("log_fwdReq_kw");
            try {
                if (kw.equals("")) {
                    entireText = msgBody;
                } else {
                    entireText = kw + " " + msgBody;
                }
                logServiceRequest(searchParam, accountId, msisdn, dest, timeSent, entireText, siteId, smscId, fwdReqKw, promoId, promoRespCode);
            } catch (Exception ex) {
                log(ex.getMessage());
            }
        }
    }

    public void logServiceRequest(String keyword, String accountId, String origin, String dest, String timeSent, String msg, String siteId, String smscId, String fwd_req_kw)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;


        System.out.println(new Date() + ": updating service_usage_log with request details..");
        System.out.println(new Date() + ": service_usage_log details: " + "keyword=" + keyword + ", " + "account_id=" + accountId + ", " + "origin=" + origin + ", " + "dest=" + dest + ", " + "timeSent=" + ("".equals(timeSent) ? new Date() : timeSent) + ", " + "msg=" + msg + ", " + "siteId=" + siteId + ", " + "smsc=" + smscId + ", " + "fwd_req_kw=" + fwd_req_kw);
        if ((fwd_req_kw != null) && (!"".equals(fwd_req_kw))) {
            keyword = fwd_req_kw;
        }
        try {
            con = DConnect.getConnection();

            String query = "insert into service_usage_log (keyword, account_id, to_number, time_received, from_number, msg, site_id, smscId, time_sent) values (?,?,?,?,?,?,?,?,?)";

            prepstat = con.prepareStatement(query);

            prepstat.setString(1, keyword);
            prepstat.setString(2, accountId);
            prepstat.setString(3, dest);
            prepstat.setTimestamp(4, new Timestamp(new Date().getTime()));
            prepstat.setString(5, origin);
            prepstat.setString(6, msg);
            prepstat.setString(7, siteId);
            prepstat.setString(8, smscId);
            try {
                prepstat.setTimestamp(9, Timestamp.valueOf(timeSent));
            } catch (Exception e) {
                prepstat.setTimestamp(9, new Timestamp(new Date().getTime()));
            }
            prepstat.execute();


            System.out.println(new Date() + ": service_usage_log updated successfully!");
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ex1) {
                    log(ex.getMessage());
                }
                con = null;
            }
            System.out.println(new Date() + ": error updating service_usage_log: " + ex.getMessage());
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
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    log(e.getMessage());
                }
                con = null;
            }
        }
    }

    public void logServiceRequest(String keyword, String accountId, String origin, String dest, String timeSent, String msg, String siteId, String smscId, String fwd_req_kw, String promoId, String promoRespCode)
            throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;


        System.out.println(new Date() + ": updating service_usage_log with request details..");
        System.out.println(new Date() + ": service_usage_log details: " + "keyword=" + keyword + ", " + "account_id=" + accountId + ", " + "origin=" + origin + ", " + "dest=" + dest + ", " + "timeSent=" + ("".equals(timeSent) ? new Date() : timeSent) + ", " + "msg=" + msg + ", " + "promoId=" + promoId + ", " + "promoRespCode=" + promoRespCode + ", " + "siteId=" + siteId + ", " + "smsc=" + smscId + ", " + "fwd_req_kw=" + fwd_req_kw);
        if ((fwd_req_kw != null) && (!"".equals(fwd_req_kw))) {
            keyword = fwd_req_kw;
        }
        try {
            con = DConnect.getConnection();

            String query = "insert into service_usage_log (keyword, account_id, to_number, time_received, from_number, msg, site_id, smscId, time_sent,promo_id,promo_response_code) values (?,?,?,?,?,?,?,?,?,?,?)";

            prepstat = con.prepareStatement(query);

            prepstat.setString(1, keyword);
            prepstat.setString(2, accountId);
            prepstat.setString(3, dest);
            prepstat.setTimestamp(4, new Timestamp(new Date().getTime()));
            prepstat.setString(5, origin);
            prepstat.setString(6, msg);
            prepstat.setString(7, siteId);
            prepstat.setString(8, smscId);
            try {
                prepstat.setTimestamp(9, Timestamp.valueOf(timeSent));
            } catch (Exception e) {
                prepstat.setTimestamp(9, new Timestamp(new Date().getTime()));
            }
            prepstat.setString(10, promoId);
            prepstat.setString(11, promoRespCode);

            prepstat.execute();


            System.out.println(new Date() + ": service_usage_log updated successfully!");
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ex1) {
                    log(ex.getMessage());
                }
                con = null;
            }
            System.out.println(new Date() + ": error updating service_usage_log: " + ex.getMessage());
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
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    log(e.getMessage());
                }
                con = null;
            }
        }
    }

    public void log(String msg) {
        this.filterConfig.getServletContext().log(msg);
    }

    public boolean checkMsisdn(String msisdn) {
        boolean msisdn_ok = true;
        if (msisdn.startsWith("+")) {
            msisdn = msisdn.replace("+", "");
        }
        try {
            Long.parseLong(msisdn);
        } catch (Exception e) {
            msisdn_ok = false;
            System.out.println(new Date() + ": error in msisdn : " + msisdn + " .Request would be terminated.");
        }
        return msisdn_ok;
    }
}



/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar

 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.servicelocator

 * JD-Core Version:    0.7.0.1

 */