/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.contentserver.serviceinterfaces.freemium;

/**
 *
 * @author Mustee
 */
interface IFreemiumSubscriber {
    public boolean subscribe(Freemium freemium, String msisdn) throws Exception ;       
    
}
