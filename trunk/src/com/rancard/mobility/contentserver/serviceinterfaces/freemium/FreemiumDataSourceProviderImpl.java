/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.contentserver.serviceinterfaces.freemium;

/**
 *
 * @author Mustee
 */
public abstract class FreemiumDataSourceProviderImpl implements FreemiumDataSourceProvider{
    private String source;
    private FreemiumDataSourceType type;
    
    public FreemiumDataSourceProviderImpl(String source, FreemiumDataSourceType type){
        this.source = source;
        this.type = type;
    }
    
    public FreemiumDataSourceType getProviderType(){
        return type;
    }
    
    public String getProviderSource(){
        return source;
    }
}
