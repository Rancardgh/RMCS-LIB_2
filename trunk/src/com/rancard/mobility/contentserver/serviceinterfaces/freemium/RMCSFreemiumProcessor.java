/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.contentserver.serviceinterfaces.freemium;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Mustee
 */
public class RMCSFreemiumProcessor implements FreemuimPocessor {

    public boolean freemuimExists(String id) throws Exception {
        try{
            return FreemiumDB.getFreemuim(id) == null;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }        
    }

    public boolean freemuimExists(String accountID, String keyword) throws Exception {
        try{
            return FreemiumDB.getFreemuim(accountID, keyword).size() > 0;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }       
    }

    public List<Freemium> getFreemiums(String accountID, String keyword) throws Exception {
        return FreemiumDB.getFreemuim(accountID, keyword);
    }

    public Freemium getFreemium(String id) throws Exception {
        return FreemiumDB.getFreemuim(id);
    }

    public boolean process(String msisdn, String accountID, String keyword) throws Exception {
        for (Iterator<Freemium> it = getActiveFreemiums(accountID, keyword).iterator(); it.hasNext();) {
            Freemium free = it.next();
            for(FreemiumDataSource source: FreemiumDB.getFreemuimDataSource(free)){
                if(source.isNotIn() && !FreemiumDataSourceProviderFactory.getProvider(source).inProvider(msisdn)){
                    return new FreemiumSubscriber().subscribe(free, msisdn);
                }
            }
        }
        return false;
    }

    public List<Freemium> getActiveFreemiums(String accountID, String keyword) throws Exception {
        return FreemiumDB.getFreemuim(accountID, keyword, true);
    }
    
    

  
}
