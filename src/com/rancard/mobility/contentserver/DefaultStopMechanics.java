package com.rancard.mobility.contentserver;

import com.rancard.common.*;
import com.rancard.common.key.ServiceExperienceConfigKey;
import com.rancard.common.key.ServiceSubscriptionKey;
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
public class DefaultStopMechanics extends StopMechanics {

    @Override
    public String processStop(EntityManager em, HttpServletResponse servletResponse, String msisdn, String message, String smsc, String shortCode) {
        String resp;
        ServiceDefinition serviceDefinition = getService(em, msisdn, message, smsc, shortCode);

        if (serviceDefinition == null) {
            resp = "Sorry you are not subscribed to any of our services.";
        } else {
            CPSite cpSite = CPSite.getSMSSite(em, serviceDefinition.getServiceDefinitionKey().getAccountID());
            if (cpSite == null) {
                resp = "Sorry your request could not be completed.";
            } else {
                ServiceExperienceConfig config = em.find(ServiceExperienceConfig.class, new ServiceExperienceConfigKey(
                        serviceDefinition.getServiceDefinitionKey().getAccountID(), cpSite.getSiteID(), serviceDefinition.getServiceDefinitionKey().getKeyword()));

                if (config == null) {
                    resp = "Sorry your request could not be completed.";
                } else {
                    ServiceSubscription.deleteSubscription(em, new ServiceSubscriptionKey(serviceDefinition.getServiceDefinitionKey().getAccountID(),
                            serviceDefinition.getServiceDefinitionKey().getKeyword(), msisdn));
                    resp = config.getUnsubscriptionMessage();
                }
            }
        }

        return resp;
    }



}
