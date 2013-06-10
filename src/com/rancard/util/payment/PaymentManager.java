/*
 * PaymentManager.java
 *
 * Created on May 24, 2007, 11:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rancard.util.payment;

import com.rancard.common.Feedback;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.util.URLUTF8Encoder;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Messenger
 */
public abstract class PaymentManager {

    //billing mech
    public static final String SMSC_BILL = "1";
    public static final String PIN_REDEMPTION = "2";
    public static final String OT_BILL = "3";
    public static final String AIRTEL_BILL = "4";
    public static final String GLO_BILL = "5";
    public static final String UNIFIED_BILL = "6";

    public static void createPricePoint(PricePoint pricePoint) throws Exception {
        PricePointDB.createPricePoint(pricePoint);
    }

    public static void updatePricePoint(PricePoint pricePoint) throws Exception {
        PricePointDB.updatePricePoint(pricePoint);
    }

    public static void updatePricePointValue(String pricePointId, String newValue) throws Exception {
        PricePointDB.updatePricePointValue(pricePointId, newValue);
    }

    public static void updatePricePointCurrency(String pricePointId, String newCurrency) throws Exception {
        PricePointDB.updatePricePointCurrency(pricePointId, newCurrency);
    }

    public static void updatePricePointURL(String pricePointId, String newURL) throws Exception {
        PricePointDB.updatePricePointURL(pricePointId, newURL);
    }

    public static void deletePricePoint(String pricePointId) throws Exception {
        PricePointDB.deletePricePoint(pricePointId);
    }

    public static PricePoint viewPricePoint(String pricePointId) throws Exception {
        return PricePointDB.viewPricePoint(pricePointId);
    }

    public static ArrayList viewPricePoints(String[] pricePointIds) throws Exception {
        return PricePointDB.viewPricePoints(pricePointIds);
    }

    public static PricePoint viewPricePointFor(String[] pricePointIds, String msisdn) throws Exception {
        return PricePointDB.viewPricePointFor(pricePointIds, msisdn);
    }

    public static ArrayList viewPricePoints(String[] pricePointIds, HashMap pricingMatrix) throws Exception {
        ArrayList pricePoints = new ArrayList();

        for (int i = 0; i < pricePointIds.length; i++) {
            PricePoint x = null;
            x = (PricePoint) pricingMatrix.get(pricePointIds[i]);
            if (x != null && x.getPricePointId() != null && !x.getPricePointId().equals("")) {
                pricePoints.add(x);
            }
        }

        return pricePoints;
    }

    public static ArrayList viewPricePointsSupportedByAccount(String accountId) throws Exception {
        return PricePointDB.viewPricePointsSupportedByAccount(accountId);
    }

    public static ArrayList viewPricePoints_al() throws Exception {
        return PricePointDB.viewPricePoints_al();
    }

    public static HashMap viewPricePoints_hm() throws Exception {
        return PricePointDB.viewPricePoints_hm();
    }

    public static boolean isPricingValid(String[] pricePointIds) throws Exception {
        return PricePointDB.isPricingValid(pricePointIds);
    }

    public static boolean initiatePayment(PricePoint pricePoint, CPConnections cnxn, String msisdn, String messageString, String pin) throws Exception {
        boolean paymentSuccessful = false;

        System.out.println(new java.util.Date() + ": " + msisdn + ":@PaymentManager.initiatePayement pricepointID:" + pricePoint.getPricePointId());
        try {
            if (pricePoint.getBillingMech().equals(SMSC_BILL)) {
                paymentSuccessful = doShortCodeBilling(pricePoint, cnxn, msisdn, messageString);
                System.out.println(new java.util.Date() + ":shortcode billing billing:" + pricePoint.getBillingUrl());
            } else if (pricePoint.getBillingMech().equals(OT_BILL)) {
                paymentSuccessful = doOTBilling(pricePoint, cnxn, msisdn, messageString);
                System.out.println(new java.util.Date() + ": " + msisdn + ":OT billing:" + pricePoint.getBillingUrl());
            } else if (pricePoint.getBillingMech().equals(PIN_REDEMPTION)) {
                System.out.println(new java.util.Date() + ":PIN redemption:" + pricePoint.getBillingUrl());
                paymentSuccessful = doPinRedemption(pricePoint, pin);
            } else {
                paymentSuccessful = false;
            }
        } catch (Exception e) {
            paymentSuccessful = false;
        }

        return paymentSuccessful;
    }

    //initiatePayment with hashmap of extra parameters
    public static boolean initiatePayment(PricePoint pricePoint, CPConnections cnxn, String msisdn, String messageString, String pin, HashMap cdrFields) throws Exception {
        boolean paymentSuccessful = false;

        System.out.println(new java.util.Date() + ": " + msisdn + ":@PaymentManager.initiatePayement pricepointID:" + pricePoint.getPricePointId());
        try {
            if (pricePoint.getBillingMech().equals(SMSC_BILL)) {
                paymentSuccessful = doShortCodeBilling(pricePoint, cnxn, msisdn, messageString);
                System.out.println(new java.util.Date() + ":shortcode billing billing:" + pricePoint.getBillingUrl());
            } else if (pricePoint.getBillingMech().equals(OT_BILL)) {
                paymentSuccessful = doOTBilling(pricePoint, cnxn, msisdn, messageString, cdrFields);
                System.out.println(new java.util.Date() + ": " + msisdn + ":OT billing:" + pricePoint.getBillingUrl());
            } else if (pricePoint.getBillingMech().equals(PIN_REDEMPTION)) {
                System.out.println(new java.util.Date() + ":PIN redemption:" + pricePoint.getBillingUrl());
                paymentSuccessful = doPinRedemption(pricePoint, pin);
            } else {
                paymentSuccessful = false;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            //paymentSuccessful = false;
        }

        return paymentSuccessful;
    }

    public static boolean initiatePayment(PricePoint pricePoint, CPConnections cnxn, String msisdn, String messageString,
            String pin, String callBackUrl) throws Exception {
        boolean paymentSuccessful = false;

        System.out.println(new java.util.Date() + ": " + msisdn + ":@PaymentManager.initiatePayement pricepointID:" + pricePoint.getPricePointId());

        if (pricePoint.getBillingMech().equals(SMSC_BILL)) {
            paymentSuccessful = doShortCodeBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
            System.out.println(new java.util.Date() + ":shortcode billing billing:" + pricePoint.getBillingUrl());
        } else if (pricePoint.getBillingMech().equals(OT_BILL)) {
            paymentSuccessful = doOTBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
            System.out.println(new java.util.Date() + ": " + msisdn + ":OT billing:" + pricePoint.getBillingUrl());
        } else if (pricePoint.getBillingMech().equals(PIN_REDEMPTION)) {
            System.out.println(new java.util.Date() + ":PIN redemption:" + pricePoint.getBillingUrl());
            paymentSuccessful = doPinRedemption(pricePoint, pin, callBackUrl);
        } else if (pricePoint.getBillingMech().equals(AIRTEL_BILL)) {
            System.out.println(new java.util.Date() + ":Airtel Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doAirtelBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
        } else if (pricePoint.getBillingMech().equals(GLO_BILL)) {
            System.out.println(new java.util.Date() + ":Glo Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doGloBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
        } else if (pricePoint.getBillingMech().equals(UNIFIED_BILL)) {
            System.out.println(new java.util.Date() + ":Unified Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doUnifiedBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
        } else {
            System.out.println(new java.util.Date() + ":Unified Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doUnifiedBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
        }

        return paymentSuccessful;
    }
    
    public static boolean initiatePayment(PricePoint pricePoint, CPConnections cnxn, String msisdn, String messageString,
            String pin, String callBackUrl, String serviceCode, String keyword) throws Exception {
        boolean paymentSuccessful = false;

        System.out.println(new java.util.Date() + ": " + msisdn + ":@PaymentManager.initiatePayement pricepointID:" + pricePoint.getPricePointId());

        if (pricePoint.getBillingMech().equals(SMSC_BILL)) {
            paymentSuccessful = doShortCodeBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
            System.out.println(new java.util.Date() + ":shortcode billing billing:" + pricePoint.getBillingUrl());
        } else if (pricePoint.getBillingMech().equals(OT_BILL)) {
            paymentSuccessful = doOTBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl, serviceCode, keyword);
            System.out.println(new java.util.Date() + ": " + msisdn + ":OT billing:" + pricePoint.getBillingUrl());
        } else if (pricePoint.getBillingMech().equals(PIN_REDEMPTION)) {
            System.out.println(new java.util.Date() + ":PIN redemption:" + pricePoint.getBillingUrl());
            paymentSuccessful = doPinRedemption(pricePoint, pin, callBackUrl);
        } else if (pricePoint.getBillingMech().equals(AIRTEL_BILL)) {
            System.out.println(new java.util.Date() + ":Airtel Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doAirtelBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl, serviceCode, keyword);
        } else if (pricePoint.getBillingMech().equals(GLO_BILL)) {
            System.out.println(new java.util.Date() + ":Glo Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doGloBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl, serviceCode, keyword);
        } else if (pricePoint.getBillingMech().equals(UNIFIED_BILL)) {
            System.out.println(new java.util.Date() + ":Unified Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doUnifiedBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl, serviceCode, keyword);
        } else {
            System.out.println(new java.util.Date() + ":Unified Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doUnifiedBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl, serviceCode, keyword);
        }

        return paymentSuccessful;
    }

    public static boolean initiatePayment(PricePoint pricePoint, CPConnections cnxn, String msisdn, String messageString,
            String pin, String callBackUrl, HashMap cdrFields) throws Exception {
        boolean paymentSuccessful = false;

        System.out.println(new java.util.Date() + ": " + msisdn + ":@PaymentManager.initiatePayement pricepointID:" + pricePoint.getPricePointId());

        if (pricePoint.getBillingMech().equals(SMSC_BILL)) {
            paymentSuccessful = doShortCodeBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
            System.out.println(new java.util.Date() + ":shortcode billing billing:" + pricePoint.getBillingUrl());
        } else if (pricePoint.getBillingMech().equals(OT_BILL)) {
            paymentSuccessful = doOTBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl, cdrFields);
            System.out.println(new java.util.Date() + ": " + msisdn + ":OT billing:" + pricePoint.getBillingUrl());
        } else if (pricePoint.getBillingMech().equals(PIN_REDEMPTION)) {
            System.out.println(new java.util.Date() + ":PIN redemption:" + pricePoint.getBillingUrl());
            paymentSuccessful = doPinRedemption(pricePoint, pin, callBackUrl);
        } else if (pricePoint.getBillingMech().equals(AIRTEL_BILL)) {
            System.out.println(new java.util.Date() + ":Airtel Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doAirtelBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
        } else if (pricePoint.getBillingMech().equals(GLO_BILL)) {
            System.out.println(new java.util.Date() + ":Glo Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doGloBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
        } else {
            System.out.println(new java.util.Date() + ":Unified Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doUnifiedBilling(pricePoint, cnxn, msisdn, callBackUrl, callBackUrl);
        }

        return paymentSuccessful;
    }

    //does billing by sending SMS from the subscriber's number to a priced short code on the SMSC'
    private static boolean doShortCodeBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn,
            String messageString, String callBackUrl) throws Exception {
        boolean isBilled = false;
        String resp = "";
        HttpClient client = null;
        GetMethod httpGETFORM = null;
        System.out.println(new java.util.Date() + ":performing shortcode based billing...");
        // System.out.println(new java.util.Date()+ ":using the URL :"+pricePoint.getBillingUrl());
        try {

            String rawurl = pricePoint.getBillingUrl();

            String username = URLUTF8Encoder.encode(cnxn.getUsername());
            String password = URLUTF8Encoder.encode(cnxn.getPassword());
            String transId = URLUTF8Encoder.encode(messageString);
            String number = URLUTF8Encoder.encode(msisdn);
            String conn = URLUTF8Encoder.encode(cnxn.getConnection());

            String insertions = "username=" + username + "&password=" + password + "&msg=" + transId + "&msisdn=" + number + "&conn=" + conn;
            String formattedurl = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, rawurl);
            System.out.println(new java.util.Date() + ":Billing UR_RAW: " + rawurl);
            System.out.println(insertions);

            System.out.println(new java.util.Date() + ":finalurl(post_formatting):" + formattedurl);

            client = new HttpClient();
            httpGETFORM = new GetMethod(formattedurl);

            client.executeMethod(httpGETFORM);
            resp = getResponse(httpGETFORM.getResponseBodyAsStream());
            System.out.println(new java.util.Date() + ":Billing response: " + resp);
            if (resp == null) {
                throw new Exception(Feedback.BILLING_MECH_FAILURE);
            } else {
            }
        } catch (HttpException e) {
            resp = (Feedback.PROTOCOL_ERROR + ": " + e.getMessage());
            //logging statements
            System.out.println(new java.util.Date() + "PROTOCOL_ERROR:error response: " + resp);
            //end of logging
            isBilled = false;
        } catch (IOException e) {
            resp = (Feedback.TRANSPORT_ERROR + ":TRANSPORT_ERROR:" + e.getMessage());
            //logging statements
            System.out.println(new java.util.Date() + ":error response: " + resp);
            //end of logging
            isBilled = false;
        } catch (Exception e) {
            //logging statements
            System.out.println(new java.util.Date() + ":error response: " + e.getMessage());
            //end of logging
            isBilled = false;
        } finally {
            // Release the connection.
            httpGETFORM.releaseConnection();
            client = null;
            httpGETFORM = null;
            pricePoint = null;
            return processResponse(resp);
        }
    }

    //does billing by sending SMS from the subscriber's number to a priced short code on the SMSC'
    private static boolean doShortCodeBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn, String messageString) throws Exception {
        boolean isBilled = false;
        String resp = "";
        HttpClient client = null;
        GetMethod httpGETFORM = null;
        System.out.println(new java.util.Date() + ":performing shortcode based billing...");
        // System.out.println(new java.util.Date()+ ":using the URL :"+pricePoint.getBillingUrl());
        try {

            String rawurl = pricePoint.getBillingUrl();

            String username = URLUTF8Encoder.encode(cnxn.getUsername());
            String password = URLUTF8Encoder.encode(cnxn.getPassword());
            String transId = URLUTF8Encoder.encode(messageString);
            String number = URLUTF8Encoder.encode(msisdn);
            String conn = URLUTF8Encoder.encode(cnxn.getConnection());

            String insertions = "username=" + username + "&password=" + password + "&msg=" + transId + "&msisdn=" + number + "&conn=" + conn;
            String formattedurl = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, rawurl);
            System.out.println(new java.util.Date() + ":Billing UR_RAW: " + rawurl);
            System.out.println(insertions);

            System.out.println(new java.util.Date() + ":finalurl(post_formatting):" + formattedurl);

            client = new HttpClient();
            httpGETFORM = new GetMethod(formattedurl);

            client.executeMethod(httpGETFORM);
            resp = getResponse(httpGETFORM.getResponseBodyAsStream());
            System.out.println(new java.util.Date() + ":Billing response: " + resp);
            if (resp == null) {
                throw new Exception(Feedback.BILLING_MECH_FAILURE);
            } else {
            }
        } catch (HttpException e) {
            resp = (Feedback.PROTOCOL_ERROR + ": " + e.getMessage());
            //logging statements
            System.out.println(new java.util.Date() + "PROTOCOL_ERROR:error response: " + resp);
            //end of logging
            isBilled = false;
        } catch (IOException e) {
            resp = (Feedback.TRANSPORT_ERROR + ":TRANSPORT_ERROR:" + e.getMessage());
            //logging statements
            System.out.println(new java.util.Date() + ":error response: " + resp);
            //end of logging
            isBilled = false;
        } catch (Exception e) {
            //logging statements
            System.out.println(new java.util.Date() + ":error response: " + e.getMessage());
            //end of logging
            isBilled = false;
        } finally {
            // Release the connection.
            httpGETFORM.releaseConnection();
            client = null;
            httpGETFORM = null;
            pricePoint = null;
            return processResponse(resp);
        }
    }

    private static boolean doPinRedemption(PricePoint pricePoint, String pin, String callBackUrl) throws Exception {
        boolean isBilled = false;
        ePin voucher = new ePin();

        try {

            voucher.setPin(pin);
            if (voucher.isValid() == false) {
                throw new Exception(Feedback.MISSING_INVALID_PIN);
            } else {
                if ((voucher.getCurrentBalance() - Double.parseDouble(pricePoint.getValue())) < 0) {
                    throw new Exception(Feedback.INSUFFICIENT_CREDIT_ON_PIN);
                } else {
                    voucher.setCurrentBalance(voucher.getCurrentBalance() - Double.parseDouble(pricePoint.getValue()));
                    voucher.updateMyLog();
                    isBilled = true;
                }
            }
        } catch (Exception e) {
        } finally {
            pricePoint = null;
            voucher = null;
            return isBilled;
        }
    }

    private static boolean doPinRedemption(PricePoint pricePoint, String pin) throws Exception {
        boolean isBilled = false;
        ePin voucher = new ePin();

        try {

            voucher.setPin(pin);
            if (voucher.isValid() == false) {
                throw new Exception(Feedback.MISSING_INVALID_PIN);
            } else {
                if ((voucher.getCurrentBalance() - Double.parseDouble(pricePoint.getValue())) < 0) {
                    throw new Exception(Feedback.INSUFFICIENT_CREDIT_ON_PIN);
                } else {
                    voucher.setCurrentBalance(voucher.getCurrentBalance() - Double.parseDouble(pricePoint.getValue()));
                    voucher.updateMyLog();
                    isBilled = true;
                }
            }
        } catch (Exception e) {
        } finally {
            pricePoint = null;
            voucher = null;
            return isBilled;
        }
    }

    private synchronized static boolean doOTBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn,
            String transactionId, String callBackUrl) throws Exception {

        final String STATUS_BILLED = "0";
        final String INSUFFICIENT_CREDIT = "10";
        final String POSTPAID_SUBSCRIBER = "919";
        final int URL_CALL_TIMEOUT = 8000;

        HttpClient client = null;
        GetMethod httpGETFORM = null;
        String resp = "";

        String number = msisdn.substring(msisdn.indexOf("+233") + 4);

        String username = URLUTF8Encoder.encode(cnxn.getUsername());
        String password = URLUTF8Encoder.encode(cnxn.getPassword());
        number = URLUTF8Encoder.encode(number);

        String rawurl = pricePoint.getBillingUrl();
        String action = "&action=5"; //changed from 2 for 1-step debit

        String transGuid = "";
        if (!transactionId.equals("")) {
            transGuid = "&trans_guid=" + transactionId;
        }

        String insertions = "username=" + username + "&password=" + password + "&msisdn=" + number + "&price=" + pricePoint.getValue() + transGuid;
        rawurl = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        String callBackUrlParam = "&cUrl=" + com.rancard.util.URLUTF8Encoder.encode(callBackUrl);
        String formattedurl = rawurl + action + transGuid + callBackUrlParam;

        System.out.println(new java.util.Date() + ":billingURL:" + formattedurl);

        try {
            client = new HttpClient();
            httpGETFORM = new GetMethod(formattedurl);
            httpGETFORM.getParams().setParameter("http.socket.timeout", new Integer(URL_CALL_TIMEOUT));//timeout update

            client.executeMethod(httpGETFORM);
            resp = getResponse(httpGETFORM.getResponseBodyAsStream());
            System.out.println(new java.util.Date() + ":OT_Billing URL call complete!");
        } catch (Exception e) {
            System.out.println(new java.util.Date() + ":" + msisdn + ":exception billing OT subscriber: " + e.getMessage());
            resp = null;
            throw new Exception("READ_TIMEOUT");
        } finally {
            httpGETFORM.releaseConnection();
            client = null;
            httpGETFORM = null;
        }

        System.out.println(new java.util.Date() + ":" + msisdn + ":billing status:" + resp);
        
        String keyword = "UNKNOWN";
        String serviceCode = "UNKNOWN";
        logBillTrans(transactionId, cnxn.getProviderId (), msisdn, pricePoint.getValue (), resp, transactionId, serviceCode, keyword, cnxn.getProviderId (), "VODAFONE");

        if (resp != null) {
            if (resp.equals(STATUS_BILLED) || resp.equals(POSTPAID_SUBSCRIBER)) { //successfully billed
                return true;
            } else if (resp.equals(INSUFFICIENT_CREDIT)) { //subscriber does not have enough credit to support transaction
                throw new Exception("INSUFFICIENT_CREDIT");
            } else { //general error situation
                throw new Exception("ERROR");
            }
        } else { //probably a timeout -- ask subscriber to wait
            throw new Exception("READ_TIMEOUT");
        }
    }
    
    private synchronized static boolean doOTBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn,
            String transactionId, String callBackUrl, String serviceCode, String keyword) throws Exception {

        final String STATUS_BILLED = "0";
        final String INSUFFICIENT_CREDIT = "10";
        final String POSTPAID_SUBSCRIBER = "919";
        final int URL_CALL_TIMEOUT = 8000;

        HttpClient client = null;
        GetMethod httpGETFORM = null;
        String resp = "";

        String number = msisdn.substring(msisdn.indexOf("+233") + 4);

        String username = URLUTF8Encoder.encode(cnxn.getUsername());
        String password = URLUTF8Encoder.encode(cnxn.getPassword());
        number = URLUTF8Encoder.encode(number);

        String rawurl = pricePoint.getBillingUrl();
        String action = "&action=5"; //changed from 2 for 1-step debit

        String transGuid = "";
        if (!transactionId.equals("")) {
            transGuid = "&trans_guid=" + transactionId;
        }

        String insertions = "username=" + username + "&password=" + password + "&msisdn=" + number + "&price=" + pricePoint.getValue() + transGuid;
        rawurl = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        String callBackUrlParam = "&cUrl=" + com.rancard.util.URLUTF8Encoder.encode(callBackUrl);
        String formattedurl = rawurl + action + transGuid + callBackUrlParam;

        System.out.println(new java.util.Date() + ":billingURL:" + formattedurl);

        try {
            client = new HttpClient();
            httpGETFORM = new GetMethod(formattedurl);
            httpGETFORM.getParams().setParameter("http.socket.timeout", new Integer(URL_CALL_TIMEOUT));//timeout update

            client.executeMethod(httpGETFORM);
            resp = getResponse(httpGETFORM.getResponseBodyAsStream());
            System.out.println(new java.util.Date() + ":OT_Billing URL call complete!");
        } catch (Exception e) {
            System.out.println(new java.util.Date() + ":" + msisdn + ":exception billing OT subscriber: " + e.getMessage());
            resp = null;
            throw new Exception("READ_TIMEOUT");
        } finally {
            httpGETFORM.releaseConnection();
            client = null;
            httpGETFORM = null;
        }

        System.out.println(new java.util.Date() + ":" + msisdn + ":billing status:" + resp);
        logVodafoneBillTrans(transactionId, cnxn.getProviderId (), msisdn, pricePoint.getValue (), resp, transactionId, serviceCode, keyword, cnxn.getProviderId (), "VODAFONE");

        if (resp != null) {
            if (resp.equals(STATUS_BILLED) || resp.equals(POSTPAID_SUBSCRIBER)) { //successfully billed
                return true;
            } else if (resp.equals(INSUFFICIENT_CREDIT)) { //subscriber does not have enough credit to support transaction
                throw new Exception("INSUFFICIENT_CREDIT");
            } else { //general error situation
                throw new Exception("ERROR");
            }
        } else { //probably a timeout -- ask subscriber to wait
            throw new Exception("READ_TIMEOUT");
        }
    }

    private synchronized static boolean doOTBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn,
            String transactionId, String callBackUrl, HashMap cdrfields) throws Exception {

        final String STATUS_BILLED = "0";
        final String INSUFFICIENT_CREDIT = "10";
        final int URL_CALL_TIMEOUT = 8000;

        HttpClient client = null;
        GetMethod httpGETFORM = null;
        String resp = "";

        String number = msisdn.substring(msisdn.indexOf("+233") + 4);

        String username = URLUTF8Encoder.encode(cnxn.getUsername());
        String password = URLUTF8Encoder.encode(cnxn.getPassword());
        number = URLUTF8Encoder.encode(number);

        String rawurl = pricePoint.getBillingUrl();
        String action = "&action=5"; //changed from 2 for 1-step debit

        String transGuid = "";
        if (!transactionId.equals("")) {
            transGuid = "&trans_guid=" + transactionId;
        }

        String insertions = "username=" + username + "&password=" + password + "&msisdn=" + number + "&price=" + pricePoint.getValue() + transGuid;
        rawurl = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        String callBackUrlParam = "&cUrl=" + com.rancard.util.URLUTF8Encoder.encode(callBackUrl);
        String formattedurl = rawurl + action + transGuid + callBackUrlParam;

        System.out.println(new java.util.Date() + ":billingURL:" + formattedurl);

        try {
            client = new HttpClient();
            httpGETFORM = new GetMethod(formattedurl);
            httpGETFORM.getParams().setParameter("http.socket.timeout", new Integer(URL_CALL_TIMEOUT));//timeout update

            client.executeMethod(httpGETFORM);
            resp = getResponse(httpGETFORM.getResponseBodyAsStream());
            System.out.println(new java.util.Date() + ":OT_Billing URL call complete!");
        } catch (Exception e) {
            System.out.println(new java.util.Date() + ":" + msisdn + ":exception billing OT subscriber: " + e.getMessage());
            resp = null;
            throw new Exception("READ_TIMEOUT");
        } finally {
            httpGETFORM.releaseConnection();
            client = null;
            httpGETFORM = null;
        }

        System.out.println(new java.util.Date() + ":" + msisdn + ":billing status:" + resp);

        if (resp != null) {
            if (resp.equals(STATUS_BILLED)) { //successfully billed
                return true;
            } else if (resp.equals(INSUFFICIENT_CREDIT)) { //subscriber does not have enough credit to support transaction
                throw new Exception("INSUFFICIENT_CREDIT");
            } else { //general error situation
                throw new Exception("ERROR");
            }
        } else { //probably a timeout -- ask subscriber to wait
            throw new Exception("READ_TIMEOUT");
        }
    }

    private synchronized static boolean doOTBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn, String transactionId) throws Exception {
        boolean isBilled = false;
        HttpClient client = null;
        GetMethod httpGETFORM = null;
        final String ACCOUNT_NOT_FOUND = "201";
        final String ACCOUNT_NOT_IN_DB = "5";
        final String STATUS_BILLED = "0";
        String resp = "";
        boolean transSuccessful = false;
        final long MILLISECONDS_BETWEEN_RETRY = 0;
        final int NUM_OF_RETRIES = 1;
        final int URL_CALL_TIMEOUT = 8000;

        String number = msisdn.substring(msisdn.indexOf("+233") + 4);

        String username = URLUTF8Encoder.encode(cnxn.getUsername());
        String password = URLUTF8Encoder.encode(cnxn.getPassword());
        number = URLUTF8Encoder.encode(number);

        String rawurl = pricePoint.getBillingUrl();
        String action = "&action=5"; //changed from 2 for 1-step debit

        String transGuid = "";
        if (!transactionId.equals("")) {
            transGuid = "&trans_guid=" + transactionId;
        }

        String insertions = "username=" + username + "&password=" + password + "&msisdn=" + number + "&price=" + pricePoint.getValue() + transGuid;
        rawurl = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        //String callBackUrlParam = "&cUrl=" + com.rancard.util.URLUTF8Encoder.encode ("http://192.168.1.243/rmcs/rmcspostbilling.jsp?transId=" + transactionId);
        String callBackUrlParam = "&cUrl=" + com.rancard.util.URLUTF8Encoder.encode("http://localhost:8084/rmcsserver/rmcspostbilling.jsp?transId=" + transactionId);
        String formattedurl = rawurl + action + transGuid + callBackUrlParam;

        System.out.println(new java.util.Date() + ":billingURL:" + formattedurl);

        try {
            client = new HttpClient();
            httpGETFORM = new GetMethod(formattedurl);
            httpGETFORM.getParams().setParameter("http.socket.timeout", new Integer(URL_CALL_TIMEOUT));//timeout update

            client.executeMethod(httpGETFORM);
            resp = getResponse(httpGETFORM.getResponseBodyAsStream());
            System.out.println(new java.util.Date() + ":OT_Billing URL call complete!");
        } catch (Exception e) {

            System.out.println(new java.util.Date() + ":" + msisdn + ":exception billing OT subscriber: " + e.getMessage());
//               throw new Exception(": " + e.getMessage());
        } finally {
// Release the connection.
// System.out.println(new Date() + ": " + kw + ": " + msisdn + ": Releasing HttpClient Connection...");
            httpGETFORM.releaseConnection();
            client = null;
            httpGETFORM = null;
// System.out.println(new Date() + ": " + kw + ": " + msisdn + ": Connection released and cleaned up successfully!");
//print response message
//            out.println(resp);
        }

        System.out.println(new java.util.Date() + ":" + msisdn + ":billing status:" + resp);

//check billing status
        if (resp != null && (resp.equals(STATUS_BILLED) || resp.equals(ACCOUNT_NOT_FOUND) || resp.equals(ACCOUNT_NOT_IN_DB))) {//put account_not_found in its own condition
            isBilled = true;
        } else {
            isBilled = false;
        }

//        if (resp != null && resp.equals(ACCOUNT_NOT_FOUND)) {
//            //write to file
//            Date now = new Date();
//
//            logVodafoneTransaction(fields.get("service_name").toString(), fields.get("transaction_type").toString(), now, fields.get("cost").toString(), fields.get("service_code").toString(),
//                    fields.get("aparty").toString(), fields.get("bparty").toString(), fields.get("provider_id").toString(), "logs");
//        }

        // <editor-fold defaultstate="collapsed" desc="comment">
//2-step debit commented out on 5th Nov 2008 due to hold-up on staging server
        /*int count =0;
        while(!transSuccessful && count < NUM_OF_RETRIES){//retry billing if there's a network communication error
        try {
        client = new HttpClient();
        httpGETFORM = new GetMethod(formattedurl);
        //httpGETFORM.getParams().setParameter("http.socket.timeout", new Integer(URL_CALL_TIMEOUT));//timeout update
        
        client.executeMethod(httpGETFORM);
        resp = getResponse(httpGETFORM.getResponseBodyAsStream());
        System.out.println("OT Billing response: " + resp);
        if(resp == null) {
        throw new Exception(Feedback.BILLING_MECH_FAILURE);
        }else{
        httpGETFORM.releaseConnection();
        String code = resp.split(";")[4];
        if(code.equals(ACCOUNT_NOT_FOUND) || code.equals(ACCOUNT_NOT_IN_DB)){
        isBil led=true;
        transSuccessful = true;
        }else if(code.equals("0")){
        //isBilled = true;
        String opTransactionId = resp.split(";")[3];
        action = "&action=4";
        formattedurl = rawurl + action + "&op_transaction_id=" + opTransactionId;
        
        httpGETFORM = new GetMethod(formattedurl);
        resp = "";
        //httpGETFORM.getParams().setSoTimeout(URL_CALL_TIMEOUT);//timeout update
        // httpGETFORM.getParams().setParameter("http.socket.timeout", new Integer(URL_CALL_TIMEOUT));
        client.executeMethod(httpGETFORM);
        
        resp = getResponse(httpGETFORM.getResponseBodyAsStream());
        System.out.println("OT Billing response: " + resp);
        if(resp == null) {
        throw new Exception(Feedback.BILLING_MECH_FAILURE);
        }else{
        code = resp.split(";")[4];
        if(code.equals("0")){
        isBilled = true;
        transSuccessful = true;
        }
        }
        }else{
        transSuccessful = true;
        }
        }
        //end of logging
        } catch (HttpException e) {
        resp = (Feedback.PROTOCOL_ERROR + ": " + e.getMessage());
        //logging statements
        System.out.println("error response: " + resp);
        //end of logging
        isBilled = false;
        transSuccessful = false;
        } catch (IOException e) {
        resp = (Feedback.TRANSPORT_ERROR + ": " + e.getMessage());
        //logging statements
        System.out.println("error response: " + resp);
        //end of logging
        isBilled = false;
        transSuccessful = false;
        } catch (Exception e) {
        //logging statements
        System.out.println("error response: " + e.getMessage());
        //end of logging
        isBilled = false;
        transSuccessful = false;
        } finally {
        // Release the connection.
        httpGETFORM.releaseConnection();
        client = null;
        httpGETFORM = null;
        count++; //increment retry count
        }
        //wait
        Thread.sleep(MILLISECONDS_BETWEEN_RETRY);
        }*/// </editor-fold>


        return isBilled;
    }

    private synchronized static boolean doOTBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn, String transactionId, HashMap fields) throws Exception {
        boolean isBilled = false;
        HttpClient client = null;
        GetMethod httpGETFORM = null;
        final String ACCOUNT_NOT_FOUND = "201";
        final String ACCOUNT_NOT_IN_DB = "5";
        final String INSUFFICIENT_CREDIT = "10";
        final String STATUS_BILLED = "0";
        String resp = "";
        boolean transSuccessful = false;
        final long MILLISECONDS_BETWEEN_RETRY = 0;
        final int NUM_OF_RETRIES = 1;
        final int URL_CALL_TIMEOUT = 8000;

        String number = msisdn.substring(msisdn.indexOf("+233") + 4);

        String username = URLUTF8Encoder.encode(cnxn.getUsername());
        String password = URLUTF8Encoder.encode(cnxn.getPassword());
        number = URLUTF8Encoder.encode(number);

        String rawurl = pricePoint.getBillingUrl();
        String action = "&action=5"; //changed from 2 for 1-step debit

        String transGuid = "";
        if (!transactionId.equals("")) {
            transGuid = "&trans_guid=" + transactionId;
        }

        String insertions = "username=" + username + "&password=" + password + "&msisdn=" + number + "&price=" + pricePoint.getValue() + transGuid;
        rawurl = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        //String callBackUrlParam = "&cUrl=" + com.rancard.util.URLUTF8Encoder.encode ("http://192.168.1.243/rmcs/rmcspostbilling.jsp?transId=" + transactionId);
        String callBackUrlParam = "&cUrl=" + com.rancard.util.URLUTF8Encoder.encode("http://localhost:8084/rmcsserver/rmcspostbilling.jsp?transId=" + transactionId);
        String formattedurl = rawurl + action + transGuid + callBackUrlParam;

        System.out.println(new java.util.Date() + ":billingURL:" + formattedurl);

        try {
            client = new HttpClient();
            httpGETFORM = new GetMethod(formattedurl);
            httpGETFORM.getParams().setParameter("http.socket.timeout", new Integer(URL_CALL_TIMEOUT));//timeout update

            client.executeMethod(httpGETFORM);
            resp = getResponse(httpGETFORM.getResponseBodyAsStream());
            System.out.println(new java.util.Date() + ":OT_Billing URL call complete!");
        } catch (Exception e) {

            System.out.println(new java.util.Date() + ":" + msisdn + ":exception billing OT subscriber: " + e.getMessage());
//               throw new Exception(": " + e.getMessage());
        } finally {
// Release the connection.
// System.out.println(new Date() + ": " + kw + ": " + msisdn + ": Releasing HttpClient Connection...");
            httpGETFORM.releaseConnection();
            client = null;
            httpGETFORM = null;
// System.out.println(new Date() + ": " + kw + ": " + msisdn + ": Connection released and cleaned up successfully!");
//print response message
//            out.println(resp);
        }

        System.out.println(new java.util.Date() + ":" + msisdn + ":billing status:" + resp);

//check billing status
        if (resp != null && (resp.equals(STATUS_BILLED) || resp.equals(ACCOUNT_NOT_FOUND) || resp.equals(ACCOUNT_NOT_IN_DB))) {//put account_not_found in its own condition
            isBilled = true;
        } else {
            isBilled = false;
        }

        if (resp != null) {
            if (resp.equals(ACCOUNT_NOT_FOUND)) {
                //write to file
                Date now = new Date();

                isBilled = logVodafoneTransaction("vodafone_gateway", fields.get("transaction_type").toString(), now, fields.get("cost").toString(), fields.get("service_code").toString(),
                        fields.get("aparty").toString(), fields.get("bparty").toString(), fields.get("provider_id").toString(), "cdr");
            } else if (resp.equals(STATUS_BILLED)) { //successfully billed
                return true;
            } else if (resp.equals(INSUFFICIENT_CREDIT)) { //subscriber does not have enough credit to support transaction
                throw new Exception("INSUFFICIENT_CREDIT");
            } else { //general error situation
                throw new Exception("ERROR");
            }
        } else { //probably a timeout -- ask subscriber to wait
            throw new Exception("READ_TIMEOUT");
        }



        // <editor-fold defaultstate="collapsed" desc="comment">
//2-step debit commented out on 5th Nov 2008 due to hold-up on staging server
        /*int count =0;
        while(!transSuccessful && count < NUM_OF_RETRIES){//retry billing if there's a network communication error
        try {
        client = new HttpClient();
        httpGETFORM = new GetMethod(formattedurl);
        //httpGETFORM.getParams().setParameter("http.socket.timeout", new Integer(URL_CALL_TIMEOUT));//timeout update
        
        client.executeMethod(httpGETFORM);
        resp = getResponse(httpGETFORM.getResponseBodyAsStream());
        System.out.println("OT Billing response: " + resp);
        if(resp == null) {
        throw new Exception(Feedback.BILLING_MECH_FAILURE);
        }else{
        httpGETFORM.releaseConnection();
        String code = resp.split(";")[4];
        if(code.equals(ACCOUNT_NOT_FOUND) || code.equals(ACCOUNT_NOT_IN_DB)){
        isBilled=true;
        transSuccessful = true;
        }else if(code.equals("0")){
        //isBilled = true;
        String opTransactionId = resp.split(";")[3];
        action = "&action=4";
        formattedurl = rawurl + action + "&op_transaction_id=" + opTransactionId;
        
        httpGETFORM = new GetMethod(formattedurl);
        resp = "";
        //httpGETFORM.getParams().setSoTimeout(URL_CALL_TIMEOUT);//timeout update
        // httpGETFORM.getParams().setParameter("http.socket.timeout", new Integer(URL_CALL_TIMEOUT));
        client.executeMethod(httpGETFORM);
        
        resp = getResponse(httpGETFORM.getResponseBodyAsStream());
        System.out.println("OT Billing response: " + resp);
        if(resp == null) {
        throw new Exception(Feedback.BILLING_MECH_FAILURE);
        }else{
        code = resp.split(";")[4];
        if(code.equals("0")){
        isBilled = true;
        transSuccessful = true;
        }
        }
        }else{
        transSuccessful = true;
        }
        }
        //end of logging
        } catch (HttpException e) {
        resp = (Feedback.PROTOCOL_ERROR + ": " + e.getMessage());
        //logging statements
        System.out.println("error response: " + resp);
        //end of logging
        isBilled = false;
        transSuccessful = false;
        } catch (IOException e) {
        resp = (Feedback.TRANSPORT_ERROR + ": " + e.getMessage());
        //logging statements
        System.out.println("error response: " + resp);
        //end of logging
        isBilled = false;
        transSuccessful = false;
        } catch (Exception e) {
        //logging statements
        System.out.println("error response: " + e.getMessage());
        //end of logging
        isBilled = false;
        transSuccessful = false;
        } finally {
        // Release the connection.
        httpGETFORM.releaseConnection();
        client = null;
        httpGETFORM = null;
        count++; //increment retry count
        }
        //wait
        Thread.sleep(MILLISECONDS_BETWEEN_RETRY);
        }*/// </editor-fold>


        return isBilled;
    }

    private synchronized static boolean doAirtelBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn,
            String transactionId, String callBackUrl) throws Exception {

        final String BILLING_SUCCESSFUL = "000";
        final String INSUFFICIENT_FUNDS = "910";
        final int WAIT_BTN_BILLING_RETRIES = 250;
        final int NUM_OF_BILLING_RETRIES = 3;
        final String INCOMPLETE_TRANSACTION = "911";
        final String NO_RESPONSE_FROM_BILLING_SERVER = "915";
        final String SYSTEM_BUSY = "914";
        final String POSTPAID_SUBSCRIBER = "919";//Post Paid Subscriber

        //get price point into Pesewas by multiplying GHC value by 100
        String price_point = "0";
        try {
            price_point = "" + Double.parseDouble(pricePoint.getValue());
        } catch (Exception e) {
            System.out.println("ERROR Converting price point value");
            price_point = "0";
        }

        HttpClient client = new HttpClient();
        GetMethod post = null;
        String responseBody = "";
        String[] tokens = new String[0];

        String rawurl = pricePoint.getBillingUrl();

        String insertions = "trans_guid=" + transactionId + "&accountId=" + cnxn.getProviderId() + "&msisdn=" + msisdn + "&price=" + price_point;
        String billingUrl = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        post = new GetMethod(billingUrl);
        String statusToken = NO_RESPONSE_FROM_BILLING_SERVER;

        try {
            int count = 1;

            do {
                // Execute the method.
                client.executeMethod(post);
                System.out.println(new java.util.Date() + ": AIRTEL_GH BILLING DRIVER: [x-DEBUG] Control passed on to external"
                        + " node for billing! Expecting return in this thread.");

                // Read the response body.
                responseBody = post.getResponseBodyAsString().trim();
                System.out.println(new java.util.Date() + ": AIRTEL_GH BILLING DRIVER: [x-INFO] Response from Billing server during subscription renewal': "
                        + responseBody);

                System.out.println(new java.util.Date() + ": @airtel_gh-billing-impl.jsp: AIRTEL_GH BILLING DRIVER: [x-DEBUG]"
                        + transactionId + ": bill attempt: " + count);

                if (responseBody != null) {
                    tokens = responseBody.split(":");
                }

                if (tokens.length > 0) { //response has valuable tokens in it
                    statusToken = tokens[0];
                } else {
                    break;
                }
                count++;

                if ((statusToken.equals(INCOMPLETE_TRANSACTION) || statusToken.equals(SYSTEM_BUSY)) && count <= NUM_OF_BILLING_RETRIES) {
                    Thread.sleep(WAIT_BTN_BILLING_RETRIES);
                }
            } while ((statusToken.equals(INCOMPLETE_TRANSACTION) || statusToken.equals(SYSTEM_BUSY))
                    && count <= NUM_OF_BILLING_RETRIES);


        } catch (HttpException e) {
            System.out.println(new java.util.Date() + ": AIRTEL_GH BILLING DRIVER: [ERROR] Fatal protocol violation: " + e.getMessage());
            //e.printStackTrace ();
        } catch (IOException e) {
            System.out.println(new java.util.Date() + ": AIRTEL_GH BILLING DRIVER: [ERROR] Fatal transport error: " + e.getMessage());
            //e.printStackTrace ();
        } finally {
            // Release the connection.
            post.releaseConnection();
            client = null;
        }

        if (statusToken != null) {
            if (statusToken.equals(BILLING_SUCCESSFUL) || statusToken.equals(POSTPAID_SUBSCRIBER)) { //successfully billed
                System.out.println(new java.util.Date() + ": AIRTEL_GH BILLING DRIVER: [x-INFO] Control for billing returned."
                        + " Account of subscriber: " + msisdn + " has successfully been debitted for service on " + cnxn.getProviderId()
                        + " account. Completing transaction " + transactionId);
                return true;
            } else if (statusToken.equals(INSUFFICIENT_FUNDS)) { //subscriber does not have enough credit to support transaction
                System.out.println(new java.util.Date() + ": AIRTEL_GH BILLING DRIVER: [x-INFO] Control for billing returned."
                        + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId()
                        + " account due to INSUFFICIENT FUNDS. Will NOT complete transaction " + transactionId);
                throw new Exception("INSUFFICIENT_CREDIT");
            } else { //general error situation
                System.out.println(new java.util.Date() + ": AIRTEL_GH BILLING DRIVER: [x-INFO] Control for billing returned."
                        + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId()
                        + " account due to ERROR. Will NOT complete transaction " + transactionId);
                throw new Exception("ERROR");
            }
        } else { //probably a timeout -- ask subscriber to wait
            throw new Exception("READ_TIMEOUT");
        }
    }
    
    private synchronized static boolean doAirtelBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn,
            String transactionId, String callBackUrl, String serviceCode, String keyword) throws Exception {

        final String BILLING_SUCCESSFUL = "000";
        final String INSUFFICIENT_FUNDS = "910";
        final int WAIT_BTN_BILLING_RETRIES = 250;
        final int NUM_OF_BILLING_RETRIES = 3;
        final String INCOMPLETE_TRANSACTION = "911";
        final String NO_RESPONSE_FROM_BILLING_SERVER = "915";
        final String SYSTEM_BUSY = "914";
        final String POSTPAID_SUBSCRIBER = "919";//Post Paid Subscriber

        //get price point into Pesewas by multiplying GHC value by 100
        String price_point = "0";
        try {
            price_point = "" + Double.parseDouble(pricePoint.getValue());
        } catch (Exception e) {
            System.out.println("ERROR Converting price point value");
            price_point = "0";
        }

        HttpClient client = new HttpClient();
        GetMethod post = null;
        String responseBody = "";
        String[] tokens = new String[0];

        String rawurl = pricePoint.getBillingUrl();

        String insertions = "trans_guid=" + transactionId + "&accountId=" + cnxn.getProviderId() + "&msisdn=" + msisdn + "&price=" + price_point;
        String billingUrl = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        post = new GetMethod(billingUrl);
        String statusToken = NO_RESPONSE_FROM_BILLING_SERVER;

        try {
            int count = 1;

            do {
                // Execute the method.
                client.executeMethod(post);
                System.out.println(new java.util.Date() + ": AIRTEL_GH BILLING DRIVER: [x-DEBUG] Control passed on to external"
                        + " node for billing! Expecting return in this thread.");

                // Read the response body.
                responseBody = post.getResponseBodyAsString().trim();
                System.out.println(new java.util.Date() + ": AIRTEL_GH BILLING DRIVER: [x-INFO] Response from Billing server during subscription renewal': "
                        + responseBody);

                System.out.println(new java.util.Date() + ": @airtel_gh-billing-impl.jsp: AIRTEL_GH BILLING DRIVER: [x-DEBUG]"
                        + transactionId + ": bill attempt: " + count);

                if (responseBody != null) {
                    tokens = responseBody.split(":");
                }

                if (tokens.length > 0) { //response has valuable tokens in it
                    statusToken = tokens[0];
                } else {
                    break;
                }
                count++;

                if ((statusToken.equals(INCOMPLETE_TRANSACTION) || statusToken.equals(SYSTEM_BUSY)) && count <= NUM_OF_BILLING_RETRIES) {
                    Thread.sleep(WAIT_BTN_BILLING_RETRIES);
                }
            } while ((statusToken.equals(INCOMPLETE_TRANSACTION) || statusToken.equals(SYSTEM_BUSY))
                    && count <= NUM_OF_BILLING_RETRIES);


        } catch (HttpException e) {
            System.out.println(new java.util.Date() + ": AIRTEL_GH BILLING DRIVER: [ERROR] Fatal protocol violation: " + e.getMessage());
            //e.printStackTrace ();
        } catch (IOException e) {
            System.out.println(new java.util.Date() + ": AIRTEL_GH BILLING DRIVER: [ERROR] Fatal transport error: " + e.getMessage());
            //e.printStackTrace ();
        } finally {
            // Release the connection.
            post.releaseConnection();
            client = null;
        }
        
        logBillTrans(transactionId, cnxn.getProviderId (), msisdn, pricePoint.getValue (), statusToken, transactionId, serviceCode, keyword, cnxn.getProviderId (), "ZAIN_RANCARD");

        if (statusToken != null) {
            if (statusToken.equals(BILLING_SUCCESSFUL) || statusToken.equals(POSTPAID_SUBSCRIBER)) { //successfully billed
                System.out.println(new java.util.Date() + ": AIRTEL_GH BILLING DRIVER: [x-INFO] Control for billing returned."
                        + " Account of subscriber: " + msisdn + " has successfully been debitted for service on " + cnxn.getProviderId()
                        + " account. Completing transaction " + transactionId);
                return true;
            } else if (statusToken.equals(INSUFFICIENT_FUNDS)) { //subscriber does not have enough credit to support transaction
                System.out.println(new java.util.Date() + ": AIRTEL_GH BILLING DRIVER: [x-INFO] Control for billing returned."
                        + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId()
                        + " account due to INSUFFICIENT FUNDS. Will NOT complete transaction " + transactionId);
                throw new Exception("INSUFFICIENT_CREDIT");
            } else { //general error situation
                System.out.println(new java.util.Date() + ": AIRTEL_GH BILLING DRIVER: [x-INFO] Control for billing returned."
                        + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId()
                        + " account due to ERROR. Will NOT complete transaction " + transactionId);
                throw new Exception("ERROR");
            }
        } else { //probably a timeout -- ask subscriber to wait
            throw new Exception("READ_TIMEOUT");
        }
    }

    private synchronized static boolean doGloBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn,
            String transactionId, String callBackUrl) throws Exception {

        final String BILLING_SUCCESSFUL = "000";
        final String INSUFFICIENT_FUNDS = "910";
        final int WAIT_BTN_BILLING_RETRIES = 250;
        final int NUM_OF_BILLING_RETRIES = 3;
        final String INCOMPLETE_TRANSACTION = "911";
        final String NO_RESPONSE_FROM_BILLING_SERVER = "915";
        final String SYSTEM_BUSY = "914";
        // final String POST_PAID = "919";

        //get price point into Pesewas by multiplying GHC value by 100
        String price_point = "0";
        try {
            price_point = "" + Double.parseDouble(pricePoint.getValue());
        } catch (Exception e) {
            System.out.println("ERROR Converting price point value");
            price_point = "0";
        }

        HttpClient client = new HttpClient();
        GetMethod post = null;
        String responseBody = "";
        String[] tokens = new String[0];

        String rawurl = pricePoint.getBillingUrl();

        String insertions = "trans_guid=" + transactionId + "&accountId=" + cnxn.getProviderId() + "&msisdn=" + msisdn + "&price=" + price_point;
        String billingUrl = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        post = new GetMethod(billingUrl);
        String statusToken = NO_RESPONSE_FROM_BILLING_SERVER;

        try {
            int count = 1;

            do {
                // Execute the method.
                client.executeMethod(post);
                System.out.println(new java.util.Date() + ": GLO_NIG BILLING DRIVER: [x-DEBUG] Control passed on to external"
                        + " node for billing! Expecting return in this thread.");

                // Read the response body.
                responseBody = post.getResponseBodyAsString().trim();
                System.out.println(new java.util.Date() + ": GLO_NIG BILLING DRIVER: [x-INFO] Response from Billing server during subscription renewal': "
                        + responseBody);

                System.out.println(new java.util.Date() + ": GLO_NIG BILLING DRIVER: [x-DEBUG]"
                        + transactionId + ": bill attempt: " + count);

                if (responseBody != null) {
                    tokens = responseBody.split(":");
                }

                if (tokens.length > 0) { //response has valuable tokens in it
                    statusToken = tokens[0];
                } else {
                    break;
                }
                count++;

                if ((statusToken.equals(INCOMPLETE_TRANSACTION) || statusToken.equals(SYSTEM_BUSY)) && count <= NUM_OF_BILLING_RETRIES) {
                    Thread.sleep(WAIT_BTN_BILLING_RETRIES);
                }
            } while ((statusToken.equals(INCOMPLETE_TRANSACTION) || statusToken.equals(SYSTEM_BUSY))
                    && count <= NUM_OF_BILLING_RETRIES);


        } catch (HttpException e) {
            System.out.println(new java.util.Date() + ": GLO_NIG BILLING DRIVER: [ERROR] Fatal protocol violation: " + e.getMessage());
            //e.printStackTrace ();
        } catch (IOException e) {
            System.out.println(new java.util.Date() + ": GLO_NIG BILLING DRIVER: [ERROR] Fatal transport error: " + e.getMessage());
            //e.printStackTrace ();
        } finally {
            // Release the connection.
            post.releaseConnection();
            client = null;
        }

        if (statusToken != null) {
            if (statusToken.equals(BILLING_SUCCESSFUL)) { //successfully billed
                System.out.println(new java.util.Date() + ": GLO_NIG BILLING DRIVER: [x-INFO] Control for billing returned."
                        + " Account of subscriber: " + msisdn + " has successfully been debitted for service on " + cnxn.getProviderId()
                        + " account. Completing transaction " + transactionId);
                return true;

            } else if (statusToken.equals(INSUFFICIENT_FUNDS)) { //subscriber does not have enough credit to support transaction
                System.out.println(new java.util.Date() + ": GLO_NIG BILLING DRIVER: [x-INFO] Control for billing returned."
                        + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId()
                        + " account due to INSUFFICIENT FUNDS. Will NOT complete transaction " + transactionId);
                throw new Exception("INSUFFICIENT_CREDIT");

            } else { //general error situation
                System.out.println(new java.util.Date() + ": GLO_NIG BILLING DRIVER: [x-INFO] Control for billing returned."
                        + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId()
                        + " account due to ERROR. Will NOT complete transaction " + transactionId);
                throw new Exception("ERROR");
            }
        } else { //probably a timeout -- ask subscriber to wait
            throw new Exception("READ_TIMEOUT");
        }
    }
    
    private synchronized static boolean doGloBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn,
            String transactionId, String callBackUrl, String serviceCode, String keyword) throws Exception {

        final String BILLING_SUCCESSFUL = "000";
        final String INSUFFICIENT_FUNDS = "910";
        final int WAIT_BTN_BILLING_RETRIES = 250;
        final int NUM_OF_BILLING_RETRIES = 3;
        final String INCOMPLETE_TRANSACTION = "911";
        final String NO_RESPONSE_FROM_BILLING_SERVER = "915";
        final String SYSTEM_BUSY = "914";
        // final String POST_PAID = "919";

        //get price point into Pesewas by multiplying GHC value by 100
        String price_point = "0";
        try {
            price_point = "" + Double.parseDouble(pricePoint.getValue());
        } catch (Exception e) {
            System.out.println("ERROR Converting price point value");
            price_point = "0";
        }

        HttpClient client = new HttpClient();
        GetMethod post = null;
        String responseBody = "";
        String[] tokens = new String[0];

        String rawurl = pricePoint.getBillingUrl();

        String insertions = "trans_guid=" + transactionId + "&accountId=" + cnxn.getProviderId() + "&msisdn=" + msisdn + "&price=" + price_point;
        String billingUrl = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        post = new GetMethod(billingUrl);
        String statusToken = NO_RESPONSE_FROM_BILLING_SERVER;

        try {
            int count = 1;

            do {
                // Execute the method.
                client.executeMethod(post);
                System.out.println(new java.util.Date() + ": GLO_NIG BILLING DRIVER: [x-DEBUG] Control passed on to external"
                        + " node for billing! Expecting return in this thread.");

                // Read the response body.
                responseBody = post.getResponseBodyAsString().trim();
                System.out.println(new java.util.Date() + ": GLO_NIG BILLING DRIVER: [x-INFO] Response from Billing server during subscription renewal': "
                        + responseBody);

                System.out.println(new java.util.Date() + ": GLO_NIG BILLING DRIVER: [x-DEBUG]"
                        + transactionId + ": bill attempt: " + count);

                if (responseBody != null) {
                    tokens = responseBody.split(":");
                }

                if (tokens.length > 0) { //response has valuable tokens in it
                    statusToken = tokens[0];
                } else {
                    break;
                }
                count++;

                if ((statusToken.equals(INCOMPLETE_TRANSACTION) || statusToken.equals(SYSTEM_BUSY)) && count <= NUM_OF_BILLING_RETRIES) {
                    Thread.sleep(WAIT_BTN_BILLING_RETRIES);
                }
            } while ((statusToken.equals(INCOMPLETE_TRANSACTION) || statusToken.equals(SYSTEM_BUSY))
                    && count <= NUM_OF_BILLING_RETRIES);


        } catch (HttpException e) {
            System.out.println(new java.util.Date() + ": GLO_NIG BILLING DRIVER: [ERROR] Fatal protocol violation: " + e.getMessage());
            //e.printStackTrace ();
        } catch (IOException e) {
            System.out.println(new java.util.Date() + ": GLO_NIG BILLING DRIVER: [ERROR] Fatal transport error: " + e.getMessage());
            //e.printStackTrace ();
        } finally {
            // Release the connection.
            post.releaseConnection();
            client = null;
        }
        
        logBillTrans(transactionId, cnxn.getProviderId (), msisdn, pricePoint.getValue (), statusToken, transactionId, serviceCode, keyword, cnxn.getProviderId (), "GLO_NIGERIA");

        if (statusToken != null) {
            if (statusToken.equals(BILLING_SUCCESSFUL)) { //successfully billed
                System.out.println(new java.util.Date() + ": GLO_NIG BILLING DRIVER: [x-INFO] Control for billing returned."
                        + " Account of subscriber: " + msisdn + " has successfully been debitted for service on " + cnxn.getProviderId()
                        + " account. Completing transaction " + transactionId);
                return true;

            } else if (statusToken.equals(INSUFFICIENT_FUNDS)) { //subscriber does not have enough credit to support transaction
                System.out.println(new java.util.Date() + ": GLO_NIG BILLING DRIVER: [x-INFO] Control for billing returned."
                        + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId()
                        + " account due to INSUFFICIENT FUNDS. Will NOT complete transaction " + transactionId);
                throw new Exception("INSUFFICIENT_CREDIT");

            } else { //general error situation
                System.out.println(new java.util.Date() + ": GLO_NIG BILLING DRIVER: [x-INFO] Control for billing returned."
                        + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId()
                        + " account due to ERROR. Will NOT complete transaction " + transactionId);
                throw new Exception("ERROR");
            }
        } else { //probably a timeout -- ask subscriber to wait
            throw new Exception("READ_TIMEOUT");
        }
    }

    private synchronized static boolean doUnifiedBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn,
            String transactionId, String callBackUrl) throws Exception {

        final String BILLING_SUCCESSFUL = "000";
        final String INSUFFICIENT_FUNDS = "910";
        final int WAIT_BTN_BILLING_RETRIES = 250;
        final int NUM_OF_BILLING_RETRIES = 3;
        final String INCOMPLETE_TRANSACTION = "911";
        final String NO_RESPONSE_FROM_BILLING_SERVER = "915";
        final String SYSTEM_BUSY = "914";

        //get price point into Pesewas by multiplying GHC value by 100
        String price_point = "0";
        try {
            price_point = "" + Double.parseDouble(pricePoint.getValue());
        } catch (Exception e) {
            System.out.println("ERROR Converting price point value");
            price_point = "0";
        }

        HttpClient client = new HttpClient();
        GetMethod post = null;
        String responseBody = "";
        String[] tokens = new String[0];

        String rawurl = pricePoint.getBillingUrl();

        String insertions = "trans_guid=" + transactionId + "&accountId=" + cnxn.getProviderId() + "&msisdn=" + msisdn + "&price=" + price_point + "&keyword=" + callBackUrl;
        String billingUrl = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        post = new GetMethod(billingUrl);
        String statusToken = NO_RESPONSE_FROM_BILLING_SERVER;

        try {
            int count = 1;

            do {
                // Execute the method.
                client.executeMethod(post);
                System.out.println(new java.util.Date() + ": UNIFIED BILLING DRIVER: [x-DEBUG] Control passed on to external"
                        + " node for billing! Expecting return in this thread.");

                // Read the response body.
                responseBody = post.getResponseBodyAsString().trim();
                System.out.println(new java.util.Date() + ": UNIFIED BILLING DRIVER: [x-INFO] Response from Billing server during subscription renewal': "
                        + responseBody);

                System.out.println(new java.util.Date() + ": UNIFIED BILLING DRIVER: [x-DEBUG]"
                        + transactionId + ": bill attempt: " + count);

                if (responseBody != null) {
                    tokens = responseBody.split(":");
                }

                if (tokens.length > 0) { //response has valuable tokens in it
                    statusToken = tokens[0];
                } else {
                    break;
                }
                count++;

                if ((statusToken.equals(INCOMPLETE_TRANSACTION) || statusToken.equals(SYSTEM_BUSY)) && count <= NUM_OF_BILLING_RETRIES) {
                    Thread.sleep(WAIT_BTN_BILLING_RETRIES);
                }
            } while ((statusToken.equals(INCOMPLETE_TRANSACTION) || statusToken.equals(SYSTEM_BUSY))
                    && count <= NUM_OF_BILLING_RETRIES);


        } catch (HttpException e) {
            System.out.println(new java.util.Date() + ": UNIFIED BILLING DRIVER: [ERROR] Fatal protocol violation: " + e.getMessage());
            //e.printStackTrace ();
        } catch (IOException e) {
            System.out.println(new java.util.Date() + ": UNIFIED BILLING DRIVER: [ERROR] Fatal transport error: " + e.getMessage());
            //e.printStackTrace ();
        } finally {
            // Release the connection.
            post.releaseConnection();
            client = null;
        }

        if (statusToken != null) {
            if (statusToken.equals(BILLING_SUCCESSFUL)) { //successfully billed
                System.out.println(new java.util.Date() + ": UNIFIED BILLING DRIVER: [x-INFO] Control for billing returned."
                        + " Account of subscriber: " + msisdn + " has successfully been debitted for service on " + cnxn.getProviderId()
                        + " account. Completing transaction " + transactionId);
                return true;
            } else if (statusToken.equals(INSUFFICIENT_FUNDS)) { //subscriber does not have enough credit to support transaction
                System.out.println(new java.util.Date() + ": UNIFIED BILLING DRIVER: [x-INFO] Control for billing returned."
                        + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId()
                        + " account due to INSUFFICIENT FUNDS. Will NOT complete transaction " + transactionId);
                throw new Exception("INSUFFICIENT_CREDIT");
            } else { //general error situation
                System.out.println(new java.util.Date() + ": UNIFIED BILLING DRIVER: [x-INFO] Control for billing returned."
                        + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId()
                        + " account due to ERROR. Will NOT complete transaction " + transactionId);
                throw new Exception("ERROR");
            }
        } else { //probably a timeout -- ask subscriber to wait
            throw new Exception("READ_TIMEOUT");
        }
    }
    
    private synchronized static boolean doUnifiedBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn,
            String transactionId, String callBackUrl, String serviceCode, String keyword) throws Exception {

        final String BILLING_SUCCESSFUL = "000";
        final String INSUFFICIENT_FUNDS = "910";
        final int WAIT_BTN_BILLING_RETRIES = 250;
        final int NUM_OF_BILLING_RETRIES = 3;
        final String INCOMPLETE_TRANSACTION = "911";
        final String NO_RESPONSE_FROM_BILLING_SERVER = "915";
        final String SYSTEM_BUSY = "914";

        //get price point into Pesewas by multiplying GHC value by 100
        String price_point = "0";
        try {
            price_point = "" + Double.parseDouble(pricePoint.getValue());
        } catch (Exception e) {
            System.out.println("ERROR Converting price point value");
            price_point = "0";
        }

        HttpClient client = new HttpClient();
        GetMethod post = null;
        String responseBody = "";
        String[] tokens = new String[0];

        String rawurl = pricePoint.getBillingUrl();

        String insertions = "trans_guid=" + transactionId + "&accountId=" + cnxn.getProviderId() + "&msisdn=" + msisdn + "&price=" + price_point + "&keyword=" + callBackUrl;
        String billingUrl = com.rancard.util.URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        post = new GetMethod(billingUrl);
        String statusToken = NO_RESPONSE_FROM_BILLING_SERVER;

        try {
            int count = 1;

            do {
                // Execute the method.
                client.executeMethod(post);
                System.out.println(new java.util.Date() + ": UNIFIED BILLING DRIVER: [x-DEBUG] Control passed on to external"
                        + " node for billing! Expecting return in this thread.");

                // Read the response body.
                responseBody = post.getResponseBodyAsString().trim();
                System.out.println(new java.util.Date() + ": UNIFIED BILLING DRIVER: [x-INFO] Response from Billing server during subscription renewal': "
                        + responseBody);

                System.out.println(new java.util.Date() + ": UNIFIED BILLING DRIVER: [x-DEBUG]"
                        + transactionId + ": bill attempt: " + count);

                if (responseBody != null) {
                    tokens = responseBody.split(":");
                }

                if (tokens.length > 0) { //response has valuable tokens in it
                    statusToken = tokens[0];
                } else {
                    break;
                }
                count++;

                if ((statusToken.equals(INCOMPLETE_TRANSACTION) || statusToken.equals(SYSTEM_BUSY)) && count <= NUM_OF_BILLING_RETRIES) {
                    Thread.sleep(WAIT_BTN_BILLING_RETRIES);
                }
            } while ((statusToken.equals(INCOMPLETE_TRANSACTION) || statusToken.equals(SYSTEM_BUSY))
                    && count <= NUM_OF_BILLING_RETRIES);


        } catch (HttpException e) {
            System.out.println(new java.util.Date() + ": UNIFIED BILLING DRIVER: [ERROR] Fatal protocol violation: " + e.getMessage());
            //e.printStackTrace ();
        } catch (IOException e) {
            System.out.println(new java.util.Date() + ": UNIFIED BILLING DRIVER: [ERROR] Fatal transport error: " + e.getMessage());
            //e.printStackTrace ();
        } finally {
            // Release the connection.
            post.releaseConnection();
            client = null;
        }
        
        logBillTrans(transactionId, cnxn.getProviderId (), msisdn, pricePoint.getValue (), statusToken, transactionId, serviceCode, keyword, cnxn.getProviderId (), cnxn.getConnection ().split (":")[0]);

        if (statusToken != null) {
            if (statusToken.equals(BILLING_SUCCESSFUL)) { //successfully billed
                System.out.println(new java.util.Date() + ": UNIFIED BILLING DRIVER: [x-INFO] Control for billing returned."
                        + " Account of subscriber: " + msisdn + " has successfully been debitted for service on " + cnxn.getProviderId()
                        + " account. Completing transaction " + transactionId);
                return true;
            } else if (statusToken.equals(INSUFFICIENT_FUNDS)) { //subscriber does not have enough credit to support transaction
                System.out.println(new java.util.Date() + ": UNIFIED BILLING DRIVER: [x-INFO] Control for billing returned."
                        + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId()
                        + " account due to INSUFFICIENT FUNDS. Will NOT complete transaction " + transactionId);
                throw new Exception("INSUFFICIENT_CREDIT");
            } else { //general error situation
                System.out.println(new java.util.Date() + ": UNIFIED BILLING DRIVER: [x-INFO] Control for billing returned."
                        + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId()
                        + " account due to ERROR. Will NOT complete transaction " + transactionId);
                throw new Exception("ERROR");
            }
        } else { //probably a timeout -- ask subscriber to wait
            throw new Exception("READ_TIMEOUT");
        }
    }

    static private boolean logVodafoneTransaction(String serviceName, String transactionType, Date logDate, String cost, String serviceCode, String aParty, String bParty,
            String providerId, String logFolder) {
        boolean isSuccessful = true;
        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
        String today = df.format(logDate);
        java.text.DateFormat df2 = new java.text.SimpleDateFormat("dd-MM-yyyy");
        String dateToday = df2.format(logDate);
        java.text.DateFormat df3 = new java.text.SimpleDateFormat("HH:mm:ss");
        String time = df3.format(logDate);

        BufferedReader br = null;
        PrintWriter output = null;

        if (logFolder == null || logFolder.equals("")) {
            logFolder = "unidentified";
        }

        try {
            File file = new File("/var/log/tomcat5/billing/VFONE-GH/" + logFolder + "/" + today + "-vodafone-tranx-log.csv");
            //File file = new File ("/home/ktandoh/billing-proxy/local-client-logs/" + logFolder + "/" + today + "-" + originOperatorID + "-tranx-log.csv");

            try {
                output = new PrintWriter(new FileWriter(file, true));
                br = new BufferedReader(new FileReader(file));

                String heading = br.readLine();
                br.close();

                if (heading == null || heading.equals("")) {
                    output.println("Service Name" + "\t" + "Transaction Type" + "\t" + "Date" + "\t" + "Start Time" + "\t" + "End Time" + "\t" + "Duration" + "\t" + "Cost" + "\t"
                            + "Service Code" + "\t" + "A Party" + "\t" + "B Party" + "\t" + "Reserved 1" + "\t" + "Reserved 2" + "\t" + "Reserved 3" + "\t" + "Reserved 4" + "\t"
                            + "Provider ID");
                }

                output.println(serviceName + "\t" + transactionType + "\t" + dateToday + "\t" + time + "\t" + time + "\t" + "00:00" + "\t" + cost + "\t" + serviceCode + "\t"
                        + aParty + "\t" + bParty + "\t" + "" + "\t" + "" + "\t" + "" + "\t" + "" + "\t" + providerId);
            } catch (IOException ioe) {
                //file doesn't exist -- write new one.
                System.out.println(new java.util.Date() + ": [vodafoneGhBillingDriver:ERROR]: File IO Error: " + ioe.getMessage());
                System.out.println(new java.util.Date() + ": [vodafoneGhBillingDriver:WARNING]: Intended Insertion into folder: " + logFolder + "/ during IOError :: "
                        + serviceName + "\t" + transactionType + "\t" + dateToday + "\t" + time + "\t" + time + "\t" + "00:00" + "\t" + cost + "\t" + serviceCode + "\t"
                        + aParty + "\t" + bParty + "\t" + "" + "\t" + "" + "\t" + "" + "\t" + "" + "\t" + providerId);
                isSuccessful = false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            isSuccessful = false;
        } finally {
            if (output != null) {
                output.close();
            }
            br = null;
            output = null;
        }
        return isSuccessful;
    }

    private static String getResponse(java.io.InputStream in) throws Exception {
        String status = "error";
        String reply = "";
        String error = "";
        String responseString = "";
        BufferedReader br = null;
        try {
            InputStream responseBody = in;
            br = new BufferedReader(new InputStreamReader(
                    responseBody));
            String line = br.readLine();
            while (line != null) {
                responseString = responseString + line;
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new Exception(Feedback.TRANSPORT_ERROR + ": " + e.getMessage());
        } finally {
            br.close();
            in.close();

            br = null;
            in = null;
        }

        return responseString;
    }

    private static boolean processResponse(String reply) throws Exception {
        boolean isOk = false;
        if (reply != null && !reply.equals("")) {
            if (reply.substring(0, 2).equalsIgnoreCase("OK")) {
                isOk = true;
            } else if ((reply.length() > 290) && (reply.indexOf("1001") != -1)) {
                isOk = true;
            } else if (reply.substring(0, 1).equalsIgnoreCase("3")) {
                isOk = true;
            }
        }
        return isOk;
    }
    
    private static void logBillTrans(String trans_id, String cp_user_id, String msisdn, String amount, String status, String trans_guid, String serviceCode, String keyword, String accountId, String loggingFolder) {

        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
        String today = df.format(new java.util.Date());
        java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String time = df2.format(new java.util.Date());
	String logFile = "/var/log/tomcat5/networks/billing/" + loggingFolder + "_billing_" + today + ".log";
        String joiner = "";

        try {
            PrintWriter pr = new PrintWriter(new FileWriter(logFile, true));
            joiner = generateUID() + "\t" + trans_id + "\t" + cp_user_id + "\t" + msisdn + "\t" + amount + "\t" + status + "\t" 
                    + time + "\t" + trans_guid + "\t" + serviceCode + "\t" + keyword + "\t" + accountId;
            pr.println(joiner);
            pr.close();
        } catch (Exception e) {
            System.out.println("Could not write to file >> " + joiner + ":" + e.getMessage());
        }
    }
    
    private static void logVodafoneBillTrans(String trans_id, String cp_user_id, String msisdn, String amount, String status, String trans_guid, String serviceCode, String keyword, String accountId, String loggingFolder) {

        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
        String today = df.format(new java.util.Date());
        java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String time = df2.format(new java.util.Date());
	String logFile = "/var/log/tomcat5/ot/billing/billing_" + today + ".log";
        String joiner = "";

        try {
            PrintWriter pr = new PrintWriter(new FileWriter(logFile, true));
            joiner = generateUID() + "\t" + trans_id + "\t" + cp_user_id + "\t" + msisdn + "\t" + amount + "\t" + status + "\t" 
                    + time + "\t" + trans_guid + "\t" + serviceCode + "\t" + keyword + "\t" + accountId;
            pr.println(joiner);
            pr.close();
        } catch (Exception e) {
            System.out.println("Could not write to file >> " + joiner + ":" + e.getMessage());
        }
    }
    
    private static String generateUID() {
        /*java.security.SecureRandom n = new java.security.SecureRandom();
        byte b[] = n.generateSeed(10);
        String str = "";
        int i[] = new int[b.length];
        for (int k = 0; k < b.length; k++) {
            i[k] = (int) b[k];
            str = str + new Integer(i[k]).toString();
        }
        str = str.replaceAll("-", "");*/

	java.security.SecureRandom wheel = new java.security.SecureRandom();
        int myInt = wheel.nextInt(10000000) * 10000000;
        String str = Integer.toString(myInt > 0 ? myInt : myInt * -1);
        while (str.length() < 4) {
            myInt = wheel.nextInt(10000000) * 10000000;
            str = Integer.toString(myInt > 0 ? myInt : myInt * -1);
        }

        Calendar cal = new GregorianCalendar();
        java.util.Date currentDate = cal.getTime();
        java.text.DateFormat df = new java.text.SimpleDateFormat("yyMMddHHmmss");
        return df.format(currentDate) + "" + str.substring(0, 4);
    }
}
