/*
 * UserProfilingFilter.java
 *
 * Created on March 13, 2007, 5:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.security;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author Mawudem
 */


public class UserProfilingFilter extends HttpServlet implements Filter {
    
    private FilterConfig filterConfig;
    /** Creates a new instance of UserProfilingFilter */
    public UserProfilingFilter () {
        
        //Handle the passed-in FilterConfig
        
    }
    
    public void init (FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }
    
    public void doFilter (ServletRequest req, ServletResponse res,
            FilterChain filterChain) {
        
        try {
            HttpServletRequest request = (HttpServletRequest) req;
            
            if( request.getParameter ("type")!=null) //user requesting for a service
            {
                filterChain.doFilter (new RMCSProfileRequestWrapper ((HttpServletRequest)req), res );
            } else//not a service request
            {
                filterChain.doFilter (req, res);
            }
            
        } catch (ServletException sx) {
            filterConfig.getServletContext ().log (sx.getMessage ());
        } catch (IOException iox) {
            filterConfig.getServletContext ().log (iox.getMessage ());
        }
        
    }
    
    public void destroy () {
        this.filterConfig = null;
    }
    
    
}
