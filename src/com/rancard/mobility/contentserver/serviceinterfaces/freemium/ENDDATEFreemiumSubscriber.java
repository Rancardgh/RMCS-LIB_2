/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.contentserver.serviceinterfaces.freemium;

import com.rancard.mobility.infoserver.common.services.ServiceSubscriberDB;
import com.rancard.util.DateUtil;
import java.util.Date;

/**
 *
 * @author Mustee
 */
class ENDDATEFreemiumSubscriber implements IFreemiumSubscriber {
    
    public boolean subscribe(Freemium freemium, String msisdn) throws Exception {
        
        if (freemium.getEndDate() == null || DateUtil.addDaysToDate(freemium.getEndDate(), 1).before(new Date()) || !freemium.isActive()) {
            return false;
        } else {
            
            try {
                ServiceSubscriberDB.addSubscription(new Date(), freemium.getLength(), msisdn, freemium.getAccountID(), freemium.getKeyword(), 1, 0);
                return true;
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }
    }
}
