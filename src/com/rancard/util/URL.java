/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.util;

import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 *
 * @author kweku
 */
public class URL {
    
    public static String shortenUrlWithGoogle (String longUrl) throws Exception {
        String shortUrl = "";
        com.rancard.util.Config propsConfig = new com.rancard.util.Config ();
        String apiUrl = propsConfig.getValue ("rmcsConfig", "GOOGLE_SHORT_URL");
        if (apiUrl != null && !apiUrl.equals ("")) {
            apiUrl = apiUrl + "?longUrl=" + java.net.URLEncoder.encode (longUrl, "UTF-8");
        }
        //String apiUrl = "http://localhost:8084/rmcsserver/api/_common/url-shortener.jsp?longUrl=" + java.net.URLEncoder.encode (longUrl, "UTF-8");
        
        HttpClient client = new HttpClient ();
        GetMethod httpGETFORM = new GetMethod (apiUrl);

        try {
            client.executeMethod (httpGETFORM);
            shortUrl = httpGETFORM.getResponseBodyAsString ();
        } catch (HttpException e) {
            System.out.println (new java.util.Date () + ":error response: " + e.getMessage ());
        } catch (IOException e) {
            System.out.println (new java.util.Date () + ":error response: " + e.getMessage ());
        } catch (Exception e) {
            System.out.println (new java.util.Date () + ":error response: " + e.getMessage ());
        } finally {
            httpGETFORM.releaseConnection ();
            client = null;
            httpGETFORM = null;
        }
        
        if (shortUrl == null) {
            shortUrl = "";
        }
        
        return shortUrl.trim ();
    }
    
    public static String doURLEscaping (String request_query_string, String stored_url) throws Exception{
        String url_tmp = stored_url;
        String result = "";
        String tmp_1_val = "";
        if( stored_url.indexOf ("?") != -1 ){
            String[] query = request_query_string.split ("&");
            
            for (int i = 0; i < query.length; i++) {
                String[] tmp = query[i].split ("=");
                //System.out.println (tmp[0]);
                //System.out.println(tmp[1]);
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile ("@@" + tmp[0] + "@@");
                java.util.regex.Matcher matcher = pattern.matcher (url_tmp);
                try{
                    tmp_1_val = tmp[1];
                }catch(ArrayIndexOutOfBoundsException a){
                    tmp_1_val = "";
                }
                url_tmp = matcher.replaceAll (tmp_1_val);
            }
            result = url_tmp;
        }else
            result = stored_url;
        return result;
    }
    
}
