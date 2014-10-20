package com.rancard.mobility.contentserver;

import com.rancard.common.ServiceSubscription;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Mustee on 4/3/2014.
 */
public class MTNGHStopMechanics extends StopMechanics {

    @Override
    public String processStop(HttpServletResponse servletResponse, String msisdn, String message, String smsc, String shortCode) throws Exception {
        ServiceSubscription.deleteBySMSCAndShortCode(smsc, shortCode, msisdn);
        return "So sad to see you leave. What service would you have preferred? Reply to " + shortCode + " and tell us what you want.";
    }

}
