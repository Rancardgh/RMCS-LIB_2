package com.rancard.mobility;

import com.rancard.common.DConnect;
import java.util.HashMap;
import com.rancard.mobility.infoserver.common.services.ServiceManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.*;
import javax.servlet.http.*;

public class serviceusagefilter extends HttpServlet implements Filter {
    private FilterConfig filterConfig;
    private HashMap routingTable = new HashMap ();
    private static final String FROM = "RMCS";
    
    //Handle the passed-in FilterConfig
    String baseUrl;
    
    public void init (FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        /*try{
            routingTable = (HashMap) filterConfig.getServletContext().getAttribute("routingTable");
            System.out.println(routingTable.toString());
        }catch(Exception e){
            System.out.println("Could not initialize routing table");
        }*/
     }
    
    //Process the request/response pair
    public void doFilter (ServletRequest req, ServletResponse res,
            FilterChain filterChain) {
        
     //   if (debug) log ("ServiceUsageFilter:DoAfterProcessing");
        //
        // Write code here to process the request and/or response after
        // the rest of the filter chain is invoked.
        //
        // keyword=%k&from=%p&text=%r&time=%t&to=%P&smsc=%i&msg=%a
        
        String keyword  =req.getParameter ("keyword");
        String from = req.getParameter ("msisdn");
        String to = req.getParameter ("dest");
        String smscId = req.getParameter ("smsc");
        String time = req.getParameter ("time");
        try {
            
            // validate and check for sql injection attacks
            
            logServiceRequest (keyword,from,to,time,smscId);
        } catch (Exception ex) {
            log (ex.getMessage ());
        }
        try{
            filterChain.doFilter(req, res);
        }catch (Exception e) {
            
        }
        /*ServletContext context = filterConfig.getServletContext();
        // setup base path.
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String s = request.getProtocol().toLowerCase();
        s = s.substring(0, s.indexOf("/")).toLowerCase();
        String baseUrl = s + "://" + request.getServerName() + ":" +
                request.getServerPort() + request.getContextPath() +
                "/";
         
        //ensure that the session is created if not present already
        HttpSession session = request.getSession(true);
         
        //com.rancard.common.Message replyPg = new com.rancard.common.Message();
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException ex1) {
            //could not initialize reponse writer
        }
         
        String accountId = new String();
         
        // get required request parameters
        String kw = request.getParameter("keyword");
        String msgBody = request.getParameter("msg");
        String dest = request.getParameter("dest");
        String msisdn = request.getParameter("msisdn");
        String date = request.getParameter("date");
        String phoneId = request.getParameter("phoneId");
        String ua = request.getParameter("ua");
        String siteId = request.getParameter("siteId");;
        String regId = request.getParameter("regId");
         
        if(msgBody == null){msgBody = "";}
        if(dest == null){dest = "";}
        if(msisdn == null){msisdn = "";}
        if(date == null){date = "";}
        if(phoneId == null){phoneId = "";}
        if(ua == null){ua = "";}
        if(regId == null){regId = "";}
        //if(siteId == null){siteId = "";}
         
        //msg is empty
        if (kw == null || kw.equals("")) {
            try {
                request.setAttribute("dfltMsg", Feedback.NO_SUCH_SERVICE);
                filterChain.doFilter(request, response);
            } catch (ServletException sx) {
                filterConfig.getServletContext().log(sx.getMessage());
            } catch (IOException iox) {
                filterConfig.getServletContext().log(iox.getMessage());
            }
            return;
        }
        //no account ID specified
        if (siteId == null || siteId.equals("")) {
            try {
                request.setAttribute("dfltMsg", Feedback.INVALID_REQUEST_CREDENTIALS);
                filterChain.doFilter(request, response);
            } catch (ServletException sx) {
                filterConfig.getServletContext().log(sx.getMessage());
            } catch (IOException iox) {
                filterConfig.getServletContext().log(iox.getMessage());
            }
            return;
        }
         
        try {
            try{
                accountId = CPSite.getCpIdForSite(siteId);
            }catch(Exception e){
                throw new Exception(Feedback.INVALID_REQUEST_CREDENTIALS);
            }
                        public  void logServiceRequest(String keyword, S if (debug) log ("ServiceUsageFilter:DoAfterProcessing");tring from, String to ,String timeSent, String smscId) throws Exception {
         
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
         
        try {
            con = DConnect.getConnection();
         
  query ="insert into rmcs.service_usage_log (keyword, to_number, time_received,from_number, smscId, time_sent) values (?, ?, ?, ?, ?, ?)";
         
            prepstat = con.prepareStatement(query);
            prepstat.setString(1, keyword);
            prepstat.setString(2, to);
            prepstat.setString(3, timeSent);
            prepstat.setString(4, from);
            prepstat.setString(5, smscId);
            prepstat.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
            prepstat.execute();
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ex1) {
                    log(ex.getMessage());
                }
                con = null;
            }
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
                   log(e.getMessage()); ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                log(e.getMessage());    ;
                }
                con = null;
            }
        }
    }
            //search for service
            com.rancard.mobility.infoserver.common.services.UserService srvc = null;
            try {
                srvc = com.rancard.mobility.infoserver.common.services.ServiceManager.viewService(kw, accountId);
                if (srvc.getKeyword() == null ||
                        srvc.getKeyword().equals("")) {
                    throw new Exception(com.rancard.common.Feedback.NO_SUCH_SERVICE);
                }
            } catch (Exception e) {
                throw new Exception(com.rancard.common.Feedback.NO_SUCH_SERVICE);
            }
            String acknowledgement = srvc.getDefaultMessage();
         
            request.setAttribute ("acctId", accountId);
            request.setAttribute ("ack", acknowledgement);
         
            //get service url
            String srvcUrl = (String) routingTable.get(srvc.getServiceType());
            if (srvcUrl == null || srvcUrl.equals("")) {
                throw new Exception(Feedback.NO_URL_FOR_SERVICE);
            }
         
            //using socket calls
            /*
            kw = com.rancard.util.URLUTF8Encoder.encode(kw);
            msgBody = com.rancard.util.URLUTF8Encoder.encode(msgBody);
            dest = com.rancard.util.URLUTF8Encoder.encode(dest);
            msisdn = com.rancard.util.URLUTF8Encoder.encode(msisdn);
            acknowledgement  = com.rancard.util.URLUTF8Encoder.encode(acknowledgement);
            date  = com.rancard.util.URLUTF8Encoder.encode(date);
            ua = com.rancard.util.URLUTF8Encoder.encode(ua);
         
            srvcUrl = srvcUrl + "?keyword=" + kw + "&msg=" + msgBody + "&dest=" + dest + "&msisdn=" + msisdn + "&acctId=" + accountId + "&phoneId=" +
                    phoneId + "&ua=" + ua + "&siteId=" + siteId + "&ack=" + acknowledgement + "&date=" + date + "&regId=" + regId;
         
         
            java.io.BufferedReader br = null;
            try {
                //response.sendRedirect(srvcUrl);
                java.net.URL url = new java.net.URL(srvcUrl);
                br = new java.io.BufferedReader(new InputStreamReader(url.openStream()));
         
                String temp = br.readLine();
                String resp = new String();
         
                while(temp != null){
                    resp = resp + temp;
                    temp = br.readLine();
                }
         
                request.setAttribute("dfltMsg", resp);
                filterChain.doFilter(request, response);
            } catch (IOException ex2) {
                try{
                    br.close();
                }catch(Exception e){
                    br = null;
                    throw new Exception(Feedback.ROUTE_NOTIFICATION_FAILED);
                }
            } finally {
                try{
                    br.close();
                }catch(Exception e){
                    br = null;
                }
            }
         
            //using request dispatcher
            RequestDispatcher dispatch = null;
            try{
                dispatch = request.getRequestDispatcher (srvcUrl);
            }catch(Exception e){
                throw new Exception(Feedback.ROUTE_NOTIFICATION_FAILED);
            }
          if (debug) log ("ServiceUsageFilter:DoAfterProcessing");
            dispatch.include (request, response);
         
            request.setAttribute("dfltMsg", "");
            filterChain.doFilter(request, response);
         
        } catch (Exception e) {
            try {
                request.setAttribute("dfltMsg", e.getMessage());
                filterChain.doFilter(request, response);
            } catch (ServletException sx) {
                filterConfig.getServletContext().log(sx.getMessage());
            } catch (IOException iox) {
                filterConfig.getServletContext().log(iox.getMessage());
            }
            return;
        }*/
    } //Clean up resources
    
    public  void logServiceRequest (String keyword, String from, String to ,String timeSent, String smscId) throws Exception {
        
        String query;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection ();
            
            query ="insert into rmcs.service_usage_log (keyword, to_number, time_received,from_number, smscId, time_sent) values (?, ?, ?, ?, ?, ?)";
            
            prepstat = con.prepareStatement (query);
            prepstat.setString (1, keyword);
            prepstat.setString (2, to);
            prepstat.setString (3, timeSent);
            prepstat.setString (4, from);
            prepstat.setString (5, smscId);
            prepstat.setTimestamp (6, new java.sql.Timestamp (new java.util.Date ().getTime ()));
            prepstat.execute ();
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.close ();
                } catch (Exception ex1) {
                    log (ex.getMessage ());
                }
                con = null;
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close ();
                } catch (Exception e) {
                    log (e.getMessage ());
                }
                rs = null;
            }
            if (prepstat != null) {
                try {
                    prepstat.close ();
                } catch (Exception e) {
                    log (e.getMessage ()); ;
                }
                prepstat = null;
            }
            if (con != null) {
                try {
                    con.close ();
                } catch (Exception e) {
                    log (e.getMessage ());    ;
                }
                con = null;
            }
        }
    }
    
    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }
    
    private static final boolean debug = false;
}
