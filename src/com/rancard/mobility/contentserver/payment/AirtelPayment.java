package com.rancard.mobility.contentserver.payment;

import com.rancard.common.PricePoint;
import com.rancard.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Ahmed on 8/18/2014.
 */
public class AirtelPayment implements Payment {
    private final static Logger logger = Logger.getLogger(AirtelPayment.class.getName());
    final String BILLING_SUCCESSFUL = "000";
    final String INSUFFICIENT_FUNDS = "910";
    final int WAIT_BTN_BILLING_RETRIES = 250;
    final int NUM_OF_BILLING_RETRIES = 3;
    final String INCOMPLETE_TRANSACTION = "911";
    final String NO_RESPONSE_FROM_BILLING_SERVER = "915";
    final String SYSTEM_BUSY = "914";
    final String POSTPAID_SUBSCRIBER = "919";

    private String buildBillingURL(PricePoint pricePoint, String msisdn, String transactionId, String accountId) throws UnsupportedEncodingException {
        Map<String, String> insertions = new HashMap<String, String>();
        insertions.put("msisdn", URLEncoder.encode(msisdn, "UTF-8"));
        insertions.put("price", URLEncoder.encode(pricePoint.getValue(), "UTF-8"));
        insertions.put("accountId", URLEncoder.encode(accountId, "UTF-8"));

        if (!StringUtils.isBlank(transactionId)) {
            insertions.put("trans_guid", URLEncoder.encode(transactionId, "UTF-8"));
        }

        return Utils.doURLEscaping(insertions, pricePoint.getBillingURL());
    }

    @Override
    public boolean initiatePayment(PricePoint pricePoint, String username, String password, String msisdn, String transactionId, String keyword, String accountId, String serviceCode) throws Exception {
        String timeoutValue = Utils.getPropertyFileValue("rmcs.properties", "airtel_billing_timeout");
        int timeout = URL_CALL_TIMEOUT;
        if (timeoutValue != null && StringUtils.isNumeric(timeoutValue)) {
            timeout = Integer.parseInt(timeoutValue);
        }

        final String billingURL = buildBillingURL(pricePoint, msisdn, transactionId, accountId);

        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        HttpGet get = null;
        try {
            int count = 1;
            String statusToken;
            String resp;
            do {
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

                resp = EntityUtils.toString(response.getEntity());
                logger.info("Response is: " + resp);

                statusToken = StringUtils.isBlank(resp) ? null : resp.split(":")[0];

                if ((statusToken.equals(INCOMPLETE_TRANSACTION) || statusToken.equals(SYSTEM_BUSY)) && count <= NUM_OF_BILLING_RETRIES) {
                    Thread.sleep(WAIT_BTN_BILLING_RETRIES);
                }

            } while ((statusToken.equals(INCOMPLETE_TRANSACTION) || statusToken.equals(SYSTEM_BUSY))
                    && count <= NUM_OF_BILLING_RETRIES);

            PaymentManager.logBillTrans(transactionId, accountId, msisdn, pricePoint.getValue(), resp.trim(), transactionId,
                    serviceCode, keyword, accountId, "VODAFONE");

            if (!StringUtils.isBlank(statusToken) && (statusToken.equals(BILLING_SUCCESSFUL) || statusToken.equals(POSTPAID_SUBSCRIBER))) {
                return true;
            }

            return false;
        } catch (Exception e) {
            logger.severe("Sorry billing could not be completed: " + e.getMessage());
            return false;
        } finally {
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
