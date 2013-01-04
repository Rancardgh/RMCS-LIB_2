package com.rancard.mobility.contentserver.serviceinterfaces;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class contentlist extends HttpServlet {
    private static final String CONTENT_TYPE = "application/xml";

    //Initialize global variables
    public void init() throws ServletException {
    }

    //Process the HTTP Get request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        String vaspId = request.getParameter("vasp_id");
        if (vaspId == null) {
            vaspId = "";
        }
        String category = request.getParameter("category");
        if (category == null) {
            category = "";
        }
        String type = request.getParameter("type");
        if (type == null) {
            type = "";
        }
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }
        String count = request.getParameter("count");
        if (count == null) {
            count = "";
        }
        String start = request.getParameter("start");
        if (start == null) {
            start = "";
        }
        String pageno = request.getParameter("pageno");
        if (pageno == null) {
            pageno = "";
        }
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>contentlist</title></head>");
        out.println("<body bgcolor=\"#ffffff\">");
        out.println("<p>The servlet has received a " + request.getMethod() +
                    ". This is the reply.</p>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    //Process the HTTP Post request
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        doGet(request, response);
    }

    //Clean up resources
    public void destroy() {
    }
}
