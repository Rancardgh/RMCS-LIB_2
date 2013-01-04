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
public interface FreemiumDataSourceProvider {
    
    public List<String> getList() throws Exception;
    
    public boolean inProvider(String value) throws Exception;
    
    public FreemiumDataSourceType getProviderType();
    
    public String getProviderSource();
}
