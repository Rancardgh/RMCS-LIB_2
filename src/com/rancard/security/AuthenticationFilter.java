package com.rancard.security;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class AuthenticationFilter extends HttpServlet implements Filter {

    private FilterConfig filterConfig;
    //Handle the passed-in FilterConfig

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    //Process the request/response pair
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain filterChain) {

        ServletContext context = filterConfig.getServletContext();
// setup base path.

        try {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            String s = request.getProtocol().toLowerCase();
            s = s.substring(0, s.indexOf("/")).toLowerCase();
            String base_url = s + "://" + request.getServerName() + request.getContextPath() + "/";
            InetAddress address = InetAddress.getByName(request.getServerName());
            String hostnameis = address.toString();
            String hostipis = hostnameis.substring(hostnameis.indexOf('/') + 1,
                    hostnameis.length()).trim();

            //ensure that the session is created if not present already
            HttpSession session = request.getSession(true);
            String login = (String) session.getAttribute("login");
            //if userId is not present in session, get it from request/header and store it

            String requestUri = request.getRequestURI(); //obtain the requested URI
            String queryString = request.getQueryString();
            boolean accessAllowed = login != null && "yes".equals(login);
            if (accessAllowed) {
                filterChain.doFilter(request, response);

            } else {
                if (queryString != null) {
                    response.sendRedirect(base_url
                            + "default.jsp?login_err=1&redirect="
                            + requestUri + "?" + queryString);
                } else {
                    response.sendRedirect(base_url
                            + "default.jsp?login_err=1&redirect="
                            + requestUri);
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
