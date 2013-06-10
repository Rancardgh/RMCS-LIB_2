package com.rancard.security;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class IPSecurityFilter extends HttpServlet implements Filter {
    private FilterConfig filterConfig;
   String baseUrl;
    //Handle the passed-in FilterConfig
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    //Process the request/response pair
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain filterChain) {

        ServletContext context = filterConfig.getServletContext();
      // setup base path.
      HttpServletRequest request = (HttpServletRequest) req;
      HttpServletResponse response = (HttpServletResponse) res;
      String s = request.getProtocol().toLowerCase();
	s= s.substring(0, s.indexOf("/")).toLowerCase();
      baseUrl = s+"://" +request.getServerName()+ ":" +
                request.getServerPort() + request.getContextPath() + "/";
      //ensure that the session is created if not present already
      HttpSession session = request.getSession(true);


      com.rancard.common.Message reply = new com.rancard.common.Message();
      java.util.ArrayList responses = new java.util.ArrayList();
      boolean canSend = false;

      boolean isValidIP = false;
      // posible error scenarios which need to be handled

        //get allowed ip list
      java.util.ArrayList allowedIps = new java.util.ArrayList();
      java.util.ArrayList deniedIps = new java.util.ArrayList();
      String remotehostIP = request.getRemoteHost();
      String responsePath = filterConfig.getInitParameter("error_page");
      try {
          allowedIps = AccessControlListFactory.viewWhiteList();
          deniedIps = AccessControlListFactory.viewBlackList();
      } catch (Exception ex) {
      }



      try {
          // get username and password
          // have to add support for *
          if (!deniedIps.contains(remotehostIP) &&
              allowedIps.contains(remotehostIP)) {
               isValidIP = true;
          }else{

             responses.add("Your IP address is not authorized");
          }

          // get allowed ip list

          // String login = (String) session.getAttribute("login");
          //if userId is not present in session, get it from request/header and store it
          // system.out. request.getRemoteAddr();
          String requestUri = request.getRequestURI(); //obtain the requested URI
          if (isValidIP ) {
              canSend = true;
          }
          if (canSend) {
              //request.setAttribute("user",cpuserClass);
              filterChain.doFilter(request, response);
          } else {
              String messageString = "";
              for (int i = 0; i < responses.size(); i++) {
                  messageString = messageString + responses.get(i).toString() +
                                  "<BR>";

              }
              reply.setStatus(false);
              reply.setMessage(messageString);

              if (responsePath != null) {
                  response.sendRedirect(responsePath + "?reply=" +
                                        reply.getMessage());
              } else {
                  response.sendRedirect(baseUrl +
                                        "error.jsp?reply=" +
                                        reply.getMessage());
              }
          }

        } catch (ServletException sx) {
            filterConfig.getServletContext().log(sx.getMessage());
        } catch (IOException iox) {
            filterConfig.getServletContext().log(iox.getMessage());
        }
    }

    //Clean up resources
    public void destroy() {
    }
}
