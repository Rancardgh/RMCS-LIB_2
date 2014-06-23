package com.rancard.mobility.contentserver.serviceinterfaces.config;

import com.rancard.common.ServiceExperienceConfig;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Mustee on 2/27/14.
 */
public interface ResponseConfig {

    void configureResponse(HttpServletResponse resp, ServiceExperienceConfig serviceExperience, String smsc, String message, boolean isFree);

}
