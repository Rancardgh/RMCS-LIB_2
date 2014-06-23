package com.rancard.mobility.contentserver;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Mustee on 4/4/2014.
 */
public class StopMechanicsFactory {

    public static String processStop(EntityManager em, HttpServletResponse servletResponse, String msisdn, String message, String smsc, String shortCode){
        StopMechanics stopMechanics;
        if(smsc.toUpperCase().contains("MTNGH")){
            stopMechanics = new MTNGHStopMechanics();
        }else {
            stopMechanics = new DefaultStopMechanics();
        }

        return stopMechanics.processStop(em, servletResponse, msisdn, message, smsc, shortCode);
    }
}
