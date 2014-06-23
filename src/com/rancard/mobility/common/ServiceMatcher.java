package com.rancard.mobility.common;

import com.rancard.common.ServiceDefinition;

import java.util.List;

/**
 * Created by Mustee on 3/30/2014.
 */
public interface ServiceMatcher {

    public ServiceDefinition matchService(String keyword, List<ServiceDefinition> services, double matchLevel);
}
