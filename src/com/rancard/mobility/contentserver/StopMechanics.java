package com.rancard.mobility.contentserver;

import com.rancard.common.Metadata;
import com.rancard.common.ServiceDefinition;
import com.rancard.common.ServiceExperienceConfig;
import com.rancard.common.ServiceSubscription;
import com.rancard.mobility.common.ServiceMatcher;
import com.rancard.mobility.common.SimpleServiceMatcher;
import com.rancard.util.Utils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Mustee on 3/31/2014.
 */
public abstract class StopMechanics {
    public final String STOP = "STOP";

    private ServiceDefinition service = null;

    protected ServiceDefinition getService(EntityManager em, String msisdn, String message, String smsc, String shortCode) {
        if(service != null){
            return service;
        }

        message = message.toUpperCase().replace(STOP+ " ", "").replace(STOP, "").trim();

        if (StringUtils.isBlank(message)) {
            service = ServiceSubscription.getLastSubscription(em, msisdn).getServiceDefinition();
        } else {
            ServiceMatcher serviceMatcher = new SimpleServiceMatcher();
            service = serviceMatcher.matchService(message.toUpperCase().replace(STOP + " ", "").replace(STOP, ""),
                    ServiceDefinition.findBySMSCAndShortCode(em, smsc, shortCode), 0.7);
        }

        return service;
    }


    public abstract String processStop(EntityManager em, HttpServletResponse servletResponse, String msisdn, String message, String smsc, String shortCode);
}
