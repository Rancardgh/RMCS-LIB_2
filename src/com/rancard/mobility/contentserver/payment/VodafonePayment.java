package com.rancard.mobility.contentserver.payment;


import com.rancard.common.CPConnection;
import com.rancard.common.PricePoint;
import com.rancard.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Mustee on 2/17/14.
 */
public class VodafonePayment implements Payment {
    private final static Logger logger = Logger.getLogger(VodafonePayment.class.getName());
    private final String STATUS_BILLED = "0";
    private final String INSUFFICIENT_CREDIT = "10";
    private final String POSTPAID_SUBSCRIBER = "919";


    private String formatMSISDN(String msisdn) {
        if (msisdn.startsWith("+233")) {
            msisdn = msisdn.substring(4);
        } else if (msisdn.startsWith("00233")) {
            msisdn = msisdn.substring(5);
        } else if (msisdn.startsWith("0233")) {
            msisdn = msisdn.substring(4);
        } else if (msisdn.startsWith("233")) {
            msisdn = msisdn.substring(3);
        } else if (msisdn.startsWith("0")) {
            msisdn = msisdn.substring(1);
        }
        return msisdn;
    }

    private String buildBillingURL(PricePoint pricePoint, String username, String password, String msisdn, String transactionId) throws UnsupportedEncodingException {
        Map<String, String> insertions = new HashMap<String, String>();
        insertions.put("msisdn", URLEncoder.encode(formatMSISDN(msisdn), "UTF-8"));
        insertions.put("price", URLEncoder.encode(pricePoint.getValue(), "UTF-8"));

        if (StringUtils.isNoneBlank(username, password)) {
            insertions.put("username", URLEncoder.encode(username, "UTF-8"));
            insertions.put("password", URLEncoder.encode(password, "UTF-8"));
        }

        if (!StringUtils.isBlank(transactionId)) {
            insertions.put("trans_guid", URLEncoder.encode(transactionId, "UTF-8"));
        }

        return Utils.doURLEscaping(insertions, pricePoint.getBillingURL());
    }


    @Override
    public boolean initiatePayment(PricePoint pricePoint, String username, String password, String msisdn, String transactionId,
                                   String keyword, String accountId, String serviceCode) throws Exception {
        String timeoutValue = Utils.getPropertyFileValue("rmcs.properties", "vodafone_billing_timeout");
        int timeout = URL_CALL_TIMEOUT;
        if (timeoutValue != null && StringUtils.isNumeric(timeoutValue)) {
            timeout = Integer.parseInt(timeoutValue);
        }

        final String billingURL = buildBillingURL(pricePoint, username, password, msisdn, transactionId);

        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        HttpGet get = null;
        try {
            client = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom()
                    .setSocketTimeout(timeout)
                    .setConnectTimeout(timeout)
                    .setConnectionRequestTimeout(timeout)
                    .setStaleConnectionCheckEnabled(true)
                    .build())
                    .build();

            get = new HttpGet(billingURL);
            logger.info("About to bill: " + billingURL);

            response = client.execute(get);

            String resp = EntityUtils.toString(response.getEntity());
            logger.info("Response is: " + resp);

            PaymentManager.logBillTrans(transactionId, accountId, msisdn, pricePoint.getValue(), resp.trim(), transactionId,
                    serviceCode, keyword, accountId, "VODAFONE");

            if (StringUtils.trim(resp).equals(STATUS_BILLED) || StringUtils.trim(resp).equals(POSTPAID_SUBSCRIBER)) {
                return true;
            }

            return false;
        }catch (Exception e){
            logger.severe("Sorry billing could not be completed: "+e.getMessage());
            return false;
        }finally {
            if (get != null) {
                get.releaseConnection();
            }
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }

        }
    }


}
