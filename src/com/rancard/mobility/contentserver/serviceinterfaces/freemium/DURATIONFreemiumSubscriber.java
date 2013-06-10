/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.contentserver.serviceinterfaces.freemium;

import com.rancard.mobility.infoserver.common.services.ServiceSubscriberDB;
import java.util.Date;

/**
 *
 * @author Mustee
 */
public class DURATIONFreemiumSubscriber implements IFreemiumSubscriber{

    public boolean subscribe(Freemium freemium, String msisdn) throws Exception {
        if(freemium.isActive()){
             ServiceSubscriberDB.addSubscription(new Date(), freemium.getLength(), msisdn, freemium.getAccountID(), freemium.getKeyword(), 1, 0);
             return true;
        }else{
            return false;
        }
    }
    
}
