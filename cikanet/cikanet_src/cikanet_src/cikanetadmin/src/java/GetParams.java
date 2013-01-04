/*
 * GetParams.java
 *
 * Created on July 20, 2007, 7:20 AM
 */

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author nii
 * @version
 */
public class GetParams extends HttpServlet {
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet GetParams</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet GetParams at " + request.getContextPath () + "</h1>");
        java.util.Enumeration e = request.getParameterNames();
        /*out.println("<table>");
        while (e.hasMoreElements()){
            String paramName = (String)e.nextElement();
            String paramValue = request.getParameter(paramName);
            out.println("<tr><td>");
            out.println(paramName);
            out.println("</td><td>");
            out.println(paramValue);
            out.println("</td></tr>");
        }
        out.println("</table>");*/
        out.println(request.getQueryString());
        out.println("</body>");
        out.println("</html>");
        
        out.close();
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
