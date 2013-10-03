package com.rancard.mobility.infoserver.common.services;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Mustee
 * Date: 9/26/13
 * Time: 7:06 PM
 * To change this template use File | Settings | File Templates.
 */


public class ServiceExperienceMetaDataOptions {
    private List<ServiceExperienceMetaData> options = new ArrayList<ServiceExperienceMetaData>();

    public ServiceExperienceMetaDataOptions(){}


    public void addOption(ServiceExperienceMetaData option){
        options.add(option);
    }

    public void removeOption(ServiceExperienceMetaDataOption option){
        options.remove(option);
    }

    public ServiceExperienceMetaData getOption(ServiceExperienceMetaDataOption option){
       for(ServiceExperienceMetaData d: options){
           if(d.getOption()==option){
               return d;
           }
       }

        return null;
    }


    public static ServiceExperienceMetaDataOptions createFromString(String metaData){
        ServiceExperienceMetaDataOptions options = new ServiceExperienceMetaDataOptions();

        if (metaData == null || metaData.equals("")) {
            return options;
        }

        List<String> mDataPairs = new ArrayList<String>();
        mDataPairs.addAll(Arrays.asList(metaData.split("&")));
        for (String pair : mDataPairs) {
            String[] parts = pair.split("=");
            if(parts == null || parts.length != 2){
                continue;
            }

            ServiceExperienceMetaDataOption option =  ServiceExperienceMetaDataOption.valueOf(parts[0]);
            if(option != null && !parts[1].trim().equals("")){
                options.addOption(new ServiceExperienceMetaData(option, parts[1]));
            }
        }

        return options;
    }


}
