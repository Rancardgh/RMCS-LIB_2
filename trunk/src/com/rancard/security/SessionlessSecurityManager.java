package com.rancard.security;

import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *  IP blacklists
 *  IP whitelists
 *     access  here
 *
 * ***/

public class SessionlessSecurityManager extends HttpServlet implements Filter {
    private FilterConfig filterConfig;


    //Handle the passed-in FilterConfig
    String baseUrl;
    public void init(FilterConfig filterConfig) {
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
        String baseUrl = s+"://" + request.getServerName() + ":" +
                              request.getServerPort() + request.getContextPath()+"/";
        //ensure that the session is created if not present already
        HttpSession session = request.getSession(true);

        String username = request.getParameter("username");
        String responsePath = request.getParameter("responsePath");
        String password = request.getParameter("password");
        com.rancard.common.Message reply = new com.rancard.common.Message();
        java.util.ArrayList responses = new java.util.ArrayList();
        boolean canSend = false;
        boolean isValidUser = false;

        // posible error scenarios which need to be handled

        // invalid username or password
        if (username != null && password != null) {
            //canSend = true;
            // check if user is valid user

            isValidUser = true;

        } else {
            responses.add("Invalid user name or password");

        }
        // no destination address

        //get allowed ip list

        try {
            // get username and password
            // have to add support for *
            String requestUri = request.getRequestURI(); //obtain the requested URI
            if ( isValidUser) {
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
                                          "default.jsp?reply=" +
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
