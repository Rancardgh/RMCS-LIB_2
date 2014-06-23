package com.rancard.mobility.contentserver;

import com.rancard.common.ServiceSubscription;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Mustee on 4/3/2014.
 */
public class MTNGHStopMechanics extends StopMechanics {

    @Override
    public String processStop(EntityManager em, HttpServletResponse servletResponse, String msisdn, String message, String smsc, String shortCode) {
        ServiceSubscription.deleteSubscriptions(em, smsc, shortCode, msisdn);
        return "You have been unsubscribed from all service on " + shortCode + ".";
    }

}
