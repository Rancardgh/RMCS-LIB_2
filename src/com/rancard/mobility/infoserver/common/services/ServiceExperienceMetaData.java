/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.infoserver.common.services;

/**
 *
 * @author Mustee
 */
public class ServiceExperienceMetaData {
    private ServiceExperienceMetaDataOption option;
    private String value;

    public ServiceExperienceMetaData(ServiceExperienceMetaDataOption option, String value) {
        this.option = option;
        this.value = value;
    }
    
    public ServiceExperienceMetaDataOption getOption(){
        return option;
    }

    public String getValue(){
        return value;
    }
}
