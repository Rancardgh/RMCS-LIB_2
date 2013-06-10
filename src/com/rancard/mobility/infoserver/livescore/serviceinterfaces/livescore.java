/*
 * livescore.java
 *
 * Created on January 28, 2007, 12:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.livescore.serviceinterfaces;

import com.rancard.common.Feedback;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;

/**
 *
 * @author Messenger
 */
public class livescore extends HttpServlet implements RequestDispatcher {
    
    //Initialize global variables
    public void init () throws ServletException {
    }
    
    //Process the HTTP Get request
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get reeponse writer
        PrintWriter out = response.getWriter ();
        String command = (String) request.getAttribute ("cmd");
        
        
        //using request dispatcher
        RequestDispatcher dispatch = null;
        
        if (command != null && command.equals ("1")) {
            try{
                dispatch = request.getRequestDispatcher (/*"/livescorelevelquery"*/"/livescore/query_leagues.jsp");
            }catch(Exception e){
                out.println (Feedback.ROUTE_NOTIFICATION_FAILED.substring (Feedback.ROUTE_NOTIFICATION_FAILED.indexOf (":") + 1));
                return;
            }
            
            dispatch.include (request, response);
        }else if (command != null && command.equals ("2")) {
            try{
                dispatch = request.getRequestDispatcher ("leaguelevelquery");
            }catch(Exception e){
                out.println (Feedback.ROUTE_NOTIFICATION_FAILED.substring (Feedback.ROUTE_NOTIFICATION_FAILED.indexOf (":") + 1));
                return;
            }
            
            dispatch.include (request, response);
        }else if (command != null && command.equals ("3")) {
            try{
                dispatch = request.getRequestDispatcher ("gamesubscription");
            }catch(Exception e){
                out.println (Feedback.ROUTE_NOTIFICATION_FAILED.substring (Feedback.ROUTE_NOTIFICATION_FAILED.indexOf (":") + 1));
                return;
            }
            
            dispatch.include (request, response);
        }else if (command != null && command.equals ("4")) {
            try{
                dispatch = request.getRequestDispatcher ("gamelevelquery");
            }catch(Exception e){
                out.println (Feedback.ROUTE_NOTIFICATION_FAILED.substring (Feedback.ROUTE_NOTIFICATION_FAILED.indexOf (":") + 1));
                return;
            }
            
            dispatch.include (request, response);
        } else {
            out.println ("Command not found");
        }
    }
    
    //Process the HTTP Post request
    public void doPost (HttpServletRequest request,
            HttpServletResponse response) throws
            ServletException, IOException {
        doGet (request, response);
    }
    
    //Clean up resources
    public void destroy () {
    }
    
    public void forward (ServletRequest request,
            ServletResponse response) throws ServletException, IOException{
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        doGet (req, resp);
    }
    
    public void include (ServletRequest request,
            ServletResponse response)  throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        doGet (req, resp);
    }
    
}
