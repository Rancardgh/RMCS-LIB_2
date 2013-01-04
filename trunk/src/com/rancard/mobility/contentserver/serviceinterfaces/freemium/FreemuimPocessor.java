/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.contentserver.serviceinterfaces.freemium;

import java.util.List;

/**
 *
 * @author Mustee
 */
public interface FreemuimPocessor {
    
    public boolean freemuimExists(String id) throws Exception;
    
    public boolean freemuimExists(String accountID, String keyword) throws Exception;
    
    public List<Freemium> getFreemiums(String accountID, String keyword) throws Exception;
    
    public List<Freemium> getActiveFreemiums(String accountID, String keyword) throws Exception;
    
    public Freemium getFreemium(String id) throws Exception;
    
    public boolean process(String msisdn, String accountID, String keyword) throws Exception;
    
}
