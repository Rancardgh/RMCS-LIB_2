package com.rancard.util;

import com.rancard.common.Metadata;
import com.rancard.common.MobileNetworkOperator;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by Mustee on 2/12/14.
 */
public class Utils {
    private static Logger logger = Logger.getLogger(Utils.class.getName());

    public static String formatToInternationalFormatGH(String msisdn) {
        if (msisdn.startsWith("+233")) {
            return msisdn;
        } else if (msisdn.startsWith("00233")) {
            return "+" + msisdn.substring(2);
        } else if (msisdn.startsWith("233")) {
            return "+" + msisdn;
        } else if (msisdn.startsWith("0")) {
            return "+233" + msisdn.substring(1);
        } else {
            throw new UnsupportedOperationException("MSISDN cannot be formatted.");
        }
    }

    public static String formatToInternationalFormatNG(String msisdn) {
        if (msisdn.startsWith("+234")) {
            return msisdn;
        } else if (msisdn.startsWith("00234")) {
            return "+" + msisdn.substring(2);
        } else if (msisdn.startsWith("234")) {
            return "+" + msisdn;
        } else if (msisdn.startsWith("0")) {
            return "+234" + msisdn.substring(1);
        } else {
            throw new UnsupportedOperationException("MSISDN cannot be formatted.");
        }
    }

    public static Map<Metadata, String> createMetaDataMap(String metaData) {
        Map<Metadata, String> metaMap = new EnumMap<Metadata, String>(Metadata.class);
        if (StringUtils.isBlank(metaData)) {
            return metaMap;
        }

        String[] meta = metaData.split("&");

        for (String s : meta) {
            String[] m = s.split("=");
            metaMap.put(Metadata.valueOf(m[0]), m[1]);
        }

        return metaMap;
    }

    public static String createMetaDataString(Map<Metadata, String> metaDataMap) {
        if (metaDataMap == null || metaDataMap.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (Metadata key : metaDataMap.keySet()) {
            sb.append(key).append("=").append(metaDataMap.get(key)).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }


    public static void sendContent(final String msisdn, final String sender, final String accountID, final String keyword,
                                   final String smsc, final String account, final String text) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final CloseableHttpClient client = HttpClients.createDefault();
                final HttpGet get;
                CloseableHttpResponse response = null;
                try {
                    int numOfRetries = 3;
                    String svrAddr = "http://192.168.1.249:13013/cgi-bin/sendsms"
                            + "?to=" + msisdn
                            + "&text=" + URLEncoder.encode(text, "UTF-8")
                            + "&username=tester"
                            + "&password=foobar"
                            + "&from=" + URLEncoder.encode(sender, "UTF-8")
                            + "&account=" + account
                            + "&meta-data=" + URLEncoder.encode("?smpp?keyword=" + keyword + "&cpid=" + accountID + "&product=RMCS", "UTF-8")
                            + "&smsc=" + smsc;

                    get = new HttpGet(svrAddr);


                    logger.info("About to send content: " + svrAddr);

                    for (int i = 0; i < numOfRetries; i++) {
                        response = client.execute(get);

                        String resp = EntityUtils.toString(response.getEntity());
                        logger.info("Response is: " + resp);
                        if (StringUtils.containsIgnoreCase(resp, "Accepted") || StringUtils.containsIgnoreCase(resp, "Queued")) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    logger.severe("Error Sending content: " + e.getMessage());

                } finally {
                    try {
                        if (response != null) {
                            response.close();
                        }
                        client.close();
                    } catch (IOException e) {
                        logger.severe("Problem closing connections");
                    }


                }
            }
        }).start();

    }

    public static String doURLEscaping(Map<String, String> values, String message) {
        for (String value : values.keySet()) {
            message = Pattern.compile("@@" + value + "@@").matcher(message).replaceAll(values.get(value));
        }

        return message;
    }

    public static boolean hasMSISDN(List<MobileNetworkOperator> mobileNetworkOperators, String message) {
        for (MobileNetworkOperator mobileNetworkOperator : mobileNetworkOperators) {
            Pattern pattern = Pattern.compile(mobileNetworkOperator.getMsisdnRegex());
            java.util.regex.Matcher matcher = pattern.matcher(message);

            if (matcher.matches()) {
                return true;
            }

        }

        return false;
    }

    public static Set<String> getMSISDNs(List<MobileNetworkOperator> mobileNetworkOperators, String message) {
        Set<String> msisdns = new HashSet<String>();
        for (MobileNetworkOperator mobileNetworkOperator : mobileNetworkOperators) {
            Pattern pattern = Pattern.compile(mobileNetworkOperator.getMsisdnRegex());
            java.util.regex.Matcher matcher = pattern.matcher(message);

            if (matcher.find()) {
                msisdns.add(matcher.group());
            }
        }

        return msisdns;
    }

    public static String getBaseSMSC(String smsc) {
        if (StringUtils.isNumeric(smsc.substring(smsc.length() - 1))) {
            smsc = smsc.substring(0, smsc.length() - 1);
        }
        return smsc;
    }

    public static void notify(final String url) {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                final CloseableHttpClient client = HttpClients.createDefault();
                final HttpGet get = new HttpGet(url);
                CloseableHttpResponse response;

                try {
                    logger.info("About to make request: " + url);

                    response = client.execute(get);
                    String resp = EntityUtils.toString(response.getEntity());
                    logger.info("Response is: " + resp);

                    response.close();
                    client.close();
                } catch (Exception e) {
                    logger.severe("Error making content: " + e.getMessage());
                }
            }
        })).start();

    }

    public static Properties loadPropertyFile(String fileName){
        Properties p = new Properties();
        try {
            p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
            return p;

        } catch (IOException e) {
            logger.severe("Error loading property file: "+e.getMessage());
            return null;
        }
    }

    public static void postSubscriptionNotification(String msisdn, String keyword) throws UnsupportedEncodingException {
        Properties property = Utils.loadPropertyFile("rmcs.properties");
        String notifURL = property.getProperty("rendezvous_subscription_notif_url");
        if(StringUtils.isBlank(notifURL)){
            return;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("msisdn", URLEncoder.encode(msisdn.replaceFirst("\\+", ""), "UTF-8"));
        map.put("keyword", URLEncoder.encode(keyword, "UTF-8"));
        notifURL = doURLEscaping(map, notifURL);

        notify(notifURL);
    }


}
