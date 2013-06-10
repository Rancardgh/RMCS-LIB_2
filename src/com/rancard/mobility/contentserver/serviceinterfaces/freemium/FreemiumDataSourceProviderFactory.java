/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.contentserver.serviceinterfaces.freemium;

import org.apache.commons.lang.NotImplementedException;

/**
 *
 * @author Mustee
 */
public class FreemiumDataSourceProviderFactory {
    
    public static FreemiumDataSourceProvider getProvider(FreemiumDataSource dataSource) throws Exception{
        if(dataSource.getSourceType() == FreemiumDataSourceType.SQL){
            return new SQLFreemiumDataSourceProvider(dataSource.getSource(), dataSource.getSourceType(), dataSource.getColumn());
        }else{
            throw new NotImplementedException();
        }
    }
}
