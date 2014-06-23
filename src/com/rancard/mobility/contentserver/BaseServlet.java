package com.rancard.mobility.contentserver;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

/**
 * Created by Mustee on 4/5/2014.
 */
public class BaseServlet extends HttpServlet {
    protected final String STOP = "STOP";
    protected final String INVITE = "INVITE";
    protected final String HELP = "HELP";

    private EntityManagerFactory emf = null;

    protected EntityManagerFactory EMF(HttpServletRequest request){
        ServletContext context = request.getSession().getServletContext();
        if(context.getAttribute("emf") != null ){
            emf = (EntityManagerFactory)context.getAttribute("emf");
        }else {
            emf = Persistence.createEntityManagerFactory("PersistenceUnit");
            context.setAttribute("emf", emf);
        }

        return emf;
    }


    @Override
    public void destroy() {
        if(emf != null){
            emf.close();
        }
    }
}
