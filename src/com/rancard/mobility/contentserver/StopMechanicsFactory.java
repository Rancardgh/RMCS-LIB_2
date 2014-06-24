package com.rancard.mobility.contentserver;


import javax.servlet.http.HttpServletResponse;

/**
 * Created by Mustee on 4/4/2014.
 */
public class StopMechanicsFactory {

    public static String processStop(HttpServletResponse servletResponse, String msisdn, String message, String smsc, String shortCode) throws Exception {
        StopMechanics stopMechanics;
        if(smsc.toUpperCase().contains("MTNGH")){
            stopMechanics = new MTNGHStopMechanics();
        }else {
            stopMechanics = new DefaultStopMechanics();
        }

        return stopMechanics.processStop(servletResponse, msisdn, message, smsc, shortCode);
    }
}
