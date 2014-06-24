package com.rancard.mobility.common;


import com.rancard.common.CPConnection;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Mustee on 2/10/14.
 */
public class RMSDriver extends PushDriver {
    private final Logger logger = Logger.getLogger(KannelDriver.class.getName());

    public RMSDriver(CPConnection cnxn, Map<String, String> params) {
        super(cnxn, params);

    }

    @Override
    public String sendSMSTextMessage() throws IOException {
        logger.finest("About to send Message");
        CPConnection cnxn = getCPConnection();
        Map<String, String> params = getParams();

        final String url = cnxn.getGatewayURL() + "/sendsms?to=" + URLEncoder.encode(getParameterValue(params, "to"), "UTF-8")
                + "&text=" + URLEncoder.encode(getParameterValue(params, "text"), "UTF-8")
                + "&conn=" + URLEncoder.encode(cnxn.getConnID(), "UTF-8")
                + "&username=" + URLEncoder.encode(cnxn.getUsername(), "UTF-8")
                + "&password=" + URLEncoder.encode(cnxn.getPassword(), "UTF-8")
                + "&from=" + URLEncoder.encode(getParameterValue(params, "from"), "UTF-8");
        logger.log(Level.INFO, "Calling URL to send message: " + url);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(get);

        try {
            HttpEntity entity = response.getEntity();
            String res = EntityUtils.toString(entity);
            logger.log(Level.INFO, "Response is: " + res);
            return res;
        } finally {
            response.close();
            httpClient.close();
        }
    }
}
