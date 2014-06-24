package com.rancard.mobility.contentserver;

import com.rancard.common.*;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Mustee on 3/31/2014.
 */
public class DefaultStopMechanics extends StopMechanics {

    @Override
    public String processStop(HttpServletResponse servletResponse, String msisdn, String message, String smsc, String shortCode) throws Exception {
        String resp;
        ServiceDefinition service = getService(msisdn, message, smsc, shortCode);

        if (service == null) {
            resp = "Sorry you are not subscribed to any of our services.";
        } else {
            CPSite cpSite = CPSite.getSMSSite(service.getAccountID());
            if (cpSite == null) {
                resp = "Sorry your request could not be completed.";
            } else {
                ServiceExperienceConfig config = ServiceExperienceConfig.find( service.getAccountID(), cpSite.getSiteId(), service.getKeyword());

                if (config == null) {
                    resp = "Sorry your request could not be completed.";
                } else {
                    ServiceSubscription.delete(service.getAccountID(), service.getKeyword(), msisdn);
                    resp = config.getUnsubscriptionMessage();
                }
            }
        }

        return resp;
    }



}
