package com.rancard.mobility.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class BillingDriver {
    public static final String CREDIT = "1";
    public static final String DEBIT = "0";
    protected String billingUrl = "";

    public BillingDriver(String networkPrefix, String command)
            throws Exception {
        this.billingUrl = BillingDriverDB.viewBillingUrl(networkPrefix, command);
    }

    public String fireBillingRequest(String transactionId, String msisdn, String price) {
        System.out.println("Inside Billing Driver - doDebit....");

        String response = null;
        String reply = null;

        String billurl = this.billingUrl + "&user_id=" + msisdn + "&transaction_price=" + price + "&cp_transaction_id=" + transactionId;


        System.out.println("BILL URL: " + billurl);

        HttpClient client = new HttpClient();
        GetMethod httpGETFORM = new GetMethod(billurl);
        try {
            client.executeMethod(httpGETFORM);
            String resp = getResponse(httpGETFORM.getResponseBodyAsStream());
            response = processResponse(resp);

            System.out.println("response: " + response);


            return response;
        } catch (HttpException e) {
            reply = "ERROR:305";
            response = "5001: " + e.getMessage();

            System.out.println("error response: " + response);


            return response;
        } catch (IOException e) {
            reply = "ERROR:304";
            response = "5002: " + e.getMessage();

            System.out.println("error response: " + response);


            return response;
        } catch (Exception e) {
            reply = "ERROR:304";
            response = "5002: " + e.getMessage();

            System.out.println("error response: " + response);
        }finally
         {
            httpGETFORM.releaseConnection();
        }
        return response;
    }

    public String fireBillingRequest(String transactionId, String msisdn, String price, String callBackUrl) {
        System.out.println("Inside Billing Driver - doDebit....");

        String response = null;
        String reply = null;

        String billurl = this.billingUrl + "&user_id=" + msisdn + "&transaction_price=" + price + "&cp_transaction_id=" + transactionId + "responsePath=" + callBackUrl;


        System.out.println("BILL URL: " + billurl);

        HttpClient client = new HttpClient();
        GetMethod httpGETFORM = new GetMethod(billurl);
        try {
            client.executeMethod(httpGETFORM);
            String resp = getResponse(httpGETFORM.getResponseBodyAsStream());
            response = processResponse(resp);

            System.out.println("response: " + response);


            return response;
        } catch (HttpException e) {
            reply = "ERROR:305";
            response = "5001: " + e.getMessage();

            System.out.println("error response: " + response);


            return response;
        } catch (IOException e) {
            reply = "ERROR:304";
            response = "5002: " + e.getMessage();

            System.out.println("error response: " + response);


            return response;
        } catch (Exception e) {
            reply = "ERROR:304";
            response = "5002: " + e.getMessage();

            System.out.println("error response: " + response);
        }finally {
            httpGETFORM.releaseConnection();
            client = null;
            httpGETFORM = null;
        }
        return response;
    }

    private static String getResponse(InputStream in)
            throws Exception {
        String status = "error";
        String reply = "";
        String error = "";
        String responseString = "";
        BufferedReader br = null;
        try {
            InputStream responseBody = in;
            br = new BufferedReader(new InputStreamReader(responseBody));

            String line = br.readLine();
            while (line != null) {
                responseString = responseString + line;
                line = br.readLine();
            }
        } catch (IOException e) {
            reply = "ERROR:304";
            System.err.println("5002: " + e.getMessage());
        } finally {
            br.close();
            in.close();

            br = null;
            in = null;
        }
        return responseString;
    }

    private static String processResponse(String reply)
            throws Exception {
        boolean isOk = false;
        if ((reply != null) || (!reply.equals(""))) {
            if (reply.substring(0, 2).equalsIgnoreCase("OK")) {
                isOk = true;
            } else if ((reply.length() > 290) && (reply.indexOf("1001") != -1)) {
                isOk = true;
            } else if (reply.substring(0, 1).equalsIgnoreCase("3")) {
                isOk = true;
            }
        }
        return "Request sent: " + isOk + ". Response from gateway: " + reply;
    }

    public static void test(String url) {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod();
        String[] tokens = url.split("[?]");
        try {
            method = new GetMethod(url);
            client.executeMethod(method);
        } catch (UnsupportedEncodingException ee) {
            System.out.println(ee.getMessage());
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
