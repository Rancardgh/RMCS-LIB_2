/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.contentserver.serviceinterfaces.freemium;

/**
 *
 * @author Mustee
 */
public class FreemiumSubscriber implements IFreemiumSubscriber {

    public boolean subscribe(Freemium freemium, String msisdn) throws Exception {
        if (freemium.getType() == FreemiumType.ENDDATE) {
            return new ENDDATEFreemiumSubscriber().subscribe(freemium, msisdn);
        } else if (freemium.getType() == FreemiumType.CAPPEDATENDDATE) {
            return new CAPPEDATENDDATEFreemiumSubscriber().subscribe(freemium, msisdn);
        } else if (freemium.getType() == FreemiumType.DURATION) {
            return new DURATIONFreemiumSubscriber().subscribe(freemium, msisdn);
        } else {
            return false;
        }
    }
}
