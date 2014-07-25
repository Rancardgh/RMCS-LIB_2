package com.rancard.util;

import com.rancard.mobility.infoserver.InfoService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Created by Ahmed on 7/23/2014.
 */
public class Utils {

    public static void informMASP(final String msisdn, final String accountId, final String keyword,
                                  final String dest, final String action, final String message) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String compactInfo;
                try {
                    if(message == null) {
                        InfoService is = new InfoService();

                        Calendar calendar = Calendar.getInstance();
                        java.util.Date today = new java.util.Date(calendar.getTime().getTime());
                        String dateString = com.rancard.util.Date.formatDate(today, "dd-MM-yyyy");
                        is.setAccountId(accountId);
                        is.setKeyword(keyword);
                        is.setPublishDate(dateString);

                        is.viewInfoService();

                        String info = (is.getMessage() == null || is.getMessage().trim().equals("")) ? is.getDefaultMessage() : is.getMessage();
                        compactInfo = info.replaceAll("\r\n", ".");
                    }else {
                        compactInfo = message;
                    }

                    String maspURL = "http://msg.rancardmobility.com/etisalatMASP/sync.jsp?msisdn=" + URLEncoder.encode(msisdn, "UTF-8")
                            + "&keyword=" + URLEncoder.encode(keyword, "UTF-8") + "&dest=" + URLEncoder.encode(dest, "UTF-8")
                            + "&message=" + URLEncoder.encode(compactInfo, "UTF-8") + "&action=" + action;
                    System.out.println(new Date() + "\tCalling Masp: " + maspURL);
                    HttpClient client = new HttpClient();
                    GetMethod method = null;
                    try {
                        method = new GetMethod(maspURL);
                        client.executeMethod(method);

                    } finally {
                        if (method != null) {
                            method.releaseConnection();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
