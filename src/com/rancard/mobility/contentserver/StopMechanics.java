package com.rancard.mobility.contentserver;

import com.rancard.common.ServiceDefinition;
import com.rancard.common.ServiceSubscription;
import com.rancard.mobility.common.ServiceMatcher;
import com.rancard.mobility.common.SimpleServiceMatcher;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Mustee on 3/31/2014.
 */
public abstract class StopMechanics {
    public final String STOP = "STOP";

    private ServiceDefinition service = null;

    protected ServiceDefinition getService(String msisdn, String message, String smsc, String shortCode) throws Exception {
        if(service != null){
            return service;
        }

        message = message.toUpperCase().replace(STOP+ " ", "").replace(STOP, "").trim();

        if (StringUtils.isBlank(message)) {
            ServiceSubscription subscription = ServiceSubscription.getLastSubscription(msisdn);
            service = ServiceDefinition.find(subscription.getAccountID(), subscription.getKeyword());
        } else {
            ServiceMatcher serviceMatcher = new SimpleServiceMatcher();
            service = serviceMatcher.matchService(message.toUpperCase().replace(STOP + " ", "").replace(STOP, ""),
                    ServiceDefinition.findBySMSCAndShortCode(smsc, shortCode), 0.7);
        }

        return service;
    }


    public abstract String processStop(HttpServletResponse servletResponse, String msisdn, String message, String smsc, String shortCode) throws Exception;
}
