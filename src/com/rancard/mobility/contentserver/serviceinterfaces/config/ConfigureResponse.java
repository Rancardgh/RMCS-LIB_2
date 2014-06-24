package com.rancard.mobility.contentserver.serviceinterfaces.config;

import com.rancard.common.ServiceExperienceConfig;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Mustee on 2/28/14.
 */
public class ConfigureResponse {

    public static void responseConfigurer(HttpServletResponse resp, ServiceExperienceConfig serviceExperience, String smsc,  String message, boolean isFree){
        ResponseConfig config = null;

        if(StringUtils.containsIgnoreCase(smsc, "MTNGH")){
            config = new MTNResponseConfig();
        }else{
            config = new DefaultResponseConfig();
        }

        if(config != null){
            config.configureResponse(resp, serviceExperience, smsc, message, isFree);
        }
    }
}
