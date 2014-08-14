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
    public static final String[] KEYWORDS = {"STOP", "UNSUB", "UNSUBSCRIBE", "NO"} ;

    private ServiceDefinition service = null;

    protected ServiceDefinition getService(String msisdn, String message, String smsc, String shortCode) throws Exception {
        if(service != null){
            return service;
        }

        message = removeUnsubscriptionKeyword(message);

        if (StringUtils.isBlank(message)) {
            ServiceSubscription subscription = ServiceSubscription.getLastSubscription(msisdn);
            service = ServiceDefinition.find(subscription.getAccountID(), subscription.getKeyword());
        } else {
            ServiceMatcher serviceMatcher = new SimpleServiceMatcher();
            service = serviceMatcher.matchService(message, ServiceDefinition.findBySMSCAndShortCode(smsc, shortCode), 0.7);
        }

        return service;
    }

    public static boolean isUnsubscriptionMessage(String message){
        for(String keyword: KEYWORDS) {
            if (StringUtils.containsIgnoreCase(message, keyword + " ") || message.trim().equalsIgnoreCase(keyword)){
                return true;
            }
        }
        return false;
    }

    public String removeUnsubscriptionKeyword(String message){
        for(String keyword: KEYWORDS) {
            message = message.toUpperCase().replace(keyword+" ", "").replace(keyword, "").trim();
        }
        return message;
    }


    public abstract String processStop(HttpServletResponse servletResponse, String msisdn, String message, String smsc, String shortCode) throws Exception;
}
