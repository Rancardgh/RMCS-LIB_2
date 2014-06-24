package com.rancard.mobility.contentserver.serviceinterfaces.config;

import com.rancard.common.Metadata;
import com.rancard.common.ServiceExperienceConfig;
import com.rancard.util.Utils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Mustee on 4/12/2014.
 */
public class DefaultResponseConfig implements ResponseConfig {
    final Logger logger = Logger.getLogger(DefaultResponseConfig.class.getName());

    @Override
    public void configureResponse(HttpServletResponse resp, ServiceExperienceConfig serviceExperience, String smsc, String message, boolean isFree) {
        if (!StringUtils.isAsciiPrintable(message)) {
            logger.finer("Setting request attribute for Kannel Header: X-Kannel-Coding to 2");
            resp.setHeader("X-Kannel-Coding", "2");
        }

        Map<Metadata, String> map = (serviceExperience == null) ? new EnumMap<Metadata, String>(Metadata.class) : Utils.createMetaDataMap(serviceExperience.getMetaData());
        if (isFree) {
            resp.setHeader("X-Kannel-SMSC", map.get(Metadata.FREE_SMSC) == null ? smsc : map.get(Metadata.FREE_SMSC));
        } else {
            resp.setHeader("X-Kannel-SMSC", map.get(Metadata.PAID_SMSC) == null ? smsc : map.get(Metadata.PAID_SMSC));
        }
    }
}
