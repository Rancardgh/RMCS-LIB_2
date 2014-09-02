


package com.rancard.util.payment;

import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.util.URLUTF8Encoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public abstract class PaymentManager {
    public static final String SMSC_BILL = "1";
    public static final String PIN_REDEMPTION = "2";
    public static final String OT_BILL = "3";
    public static final String AIRTEL_BILL = "4";
    public static final String GLO_BILL = "5";
    public static final String UNIFIED_BILL = "6";

    public static void createPricePoint(PricePoint pricePoint)
            throws Exception {
        PricePointDB.createPricePoint(pricePoint);
    }

    public static void updatePricePoint(PricePoint pricePoint)
            throws Exception {
        PricePointDB.updatePricePoint(pricePoint);
    }

    public static void updatePricePointValue(String pricePointId, String newValue)
            throws Exception {
        PricePointDB.updatePricePointValue(pricePointId, newValue);
    }

    public static void updatePricePointCurrency(String pricePointId, String newCurrency)
            throws Exception {
        PricePointDB.updatePricePointCurrency(pricePointId, newCurrency);
    }

    public static void updatePricePointURL(String pricePointId, String newURL)
            throws Exception {
        PricePointDB.updatePricePointURL(pricePointId, newURL);
    }

    public static void deletePricePoint(String pricePointId)
            throws Exception {
        PricePointDB.deletePricePoint(pricePointId);
    }

    public static PricePoint viewPricePoint(String pricePointId)
            throws Exception {
        return PricePointDB.viewPricePoint(pricePointId);
    }

    public static ArrayList viewPricePoints(String[] pricePointIds)
            throws Exception {
        return PricePointDB.viewPricePoints(pricePointIds);
    }

    public static PricePoint viewPricePointFor(String[] pricePointIds, String msisdn)
            throws Exception {
        return PricePointDB.viewPricePointFor(pricePointIds, msisdn);
    }

    public static ArrayList viewPricePoints(String[] pricePointIds, HashMap pricingMatrix)
            throws Exception {
        ArrayList pricePoints = new ArrayList();
        for (int i = 0; i < pricePointIds.length; i++) {
            PricePoint x = null;
            x = (PricePoint) pricingMatrix.get(pricePointIds[i]);
            if ((x != null) && (x.getPricePointId() != null) && (!x.getPricePointId().equals(""))) {
                pricePoints.add(x);
            }
        }
        return pricePoints;
    }

    public static ArrayList viewPricePointsSupportedByAccount(String accountId)
            throws Exception {
        return PricePointDB.viewPricePointsSupportedByAccount(accountId);
    }

    public static ArrayList viewPricePoints_al()
            throws Exception {
        return PricePointDB.viewPricePoints_al();
    }

    public static HashMap viewPricePoints_hm()
            throws Exception {
        return PricePointDB.viewPricePoints_hm();
    }

    public static boolean isPricingValid(String[] pricePointIds)
            throws Exception {
        return PricePointDB.isPricingValid(pricePointIds);
    }

    public static boolean initiatePayment(PricePoint pricePoint, CPConnections cnxn, String msisdn, String messageString, String pin)
            throws Exception {
        boolean paymentSuccessful = false;

        System.out.println(new Date() + ": " + msisdn + ":@PaymentManager.initiatePayement pricepointID:" + pricePoint.getPricePointId());
        try {
            if (pricePoint.getBillingMech().equals("1")) {
                paymentSuccessful = doShortCodeBilling(pricePoint, cnxn, msisdn, messageString);
                System.out.println(new Date() + ":shortcode billing billing:" + pricePoint.getBillingUrl());
            } else if (pricePoint.getBillingMech().equals("3")) {
                paymentSuccessful = doOTBilling(pricePoint, cnxn, msisdn, messageString);
                System.out.println(new Date() + ": " + msisdn + ":OT billing:" + pricePoint.getBillingUrl());
            } else if (pricePoint.getBillingMech().equals("2")) {
                System.out.println(new Date() + ":PIN redemption:" + pricePoint.getBillingUrl());
                paymentSuccessful = doPinRedemption(pricePoint, pin);
            } else {
                paymentSuccessful = false;
            }
        } catch (Exception e) {
            paymentSuccessful = false;
        }
        return paymentSuccessful;
    }

    public static boolean initiatePayment(PricePoint pricePoint, CPConnections cnxn, String msisdn, String messageString, String pin, HashMap cdrFields)
            throws Exception {
        boolean paymentSuccessful = false;

        System.out.println(new Date() + ": " + msisdn + ":@PaymentManager.initiatePayement pricepointID:" + pricePoint.getPricePointId());
        try {
            if (pricePoint.getBillingMech().equals("1")) {
                paymentSuccessful = doShortCodeBilling(pricePoint, cnxn, msisdn, messageString);
                System.out.println(new Date() + ":shortcode billing billing:" + pricePoint.getBillingUrl());
            } else if (pricePoint.getBillingMech().equals("3")) {
                paymentSuccessful = doOTBilling(pricePoint, cnxn, msisdn, messageString, cdrFields);
                System.out.println(new Date() + ": " + msisdn + ":OT billing:" + pricePoint.getBillingUrl());
            } else if (pricePoint.getBillingMech().equals("2")) {
                System.out.println(new Date() + ":PIN redemption:" + pricePoint.getBillingUrl());
                paymentSuccessful = doPinRedemption(pricePoint, pin);
            } else {
                paymentSuccessful = false;
            }
        } catch (Exception e) {
            e =


                    e;
            throw e;
        } finally {
        }
        return paymentSuccessful;
    }

    public static boolean initiatePayment(PricePoint pricePoint, CPConnections cnxn, String msisdn, String messageString, String pin, String callBackUrl)
            throws Exception {
        boolean paymentSuccessful = false;

        System.out.println(new Date() + ": " + msisdn + ":@PaymentManager.initiatePayement pricepointID:" + pricePoint.getPricePointId());
        if (pricePoint.getBillingMech().equals("1")) {
            paymentSuccessful = doShortCodeBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
            System.out.println(new Date() + ":shortcode billing billing:" + pricePoint.getBillingUrl());
        } else if (pricePoint.getBillingMech().equals("3")) {
            paymentSuccessful = doOTBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
            System.out.println(new Date() + ": " + msisdn + ":OT billing:" + pricePoint.getBillingUrl());
        } else if (pricePoint.getBillingMech().equals("2")) {
            System.out.println(new Date() + ":PIN redemption:" + pricePoint.getBillingUrl());
            paymentSuccessful = doPinRedemption(pricePoint, pin, callBackUrl);
        } else if (pricePoint.getBillingMech().equals("4")) {
            System.out.println(new Date() + ":Airtel Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doAirtelBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
        } else if (pricePoint.getBillingMech().equals("5")) {
            System.out.println(new Date() + ":Glo Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doGloBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
        } else if (pricePoint.getBillingMech().equals("6")) {
            System.out.println(new Date() + ":Unified Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doUnifiedBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
        } else {
            System.out.println(new Date() + ":Unified Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doUnifiedBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
        }
        return paymentSuccessful;
    }

    public static boolean initiatePayment(PricePoint pricePoint, CPConnections cnxn, String msisdn, String messageString, String pin, String callBackUrl, String serviceCode, String keyword)
            throws Exception {
        boolean paymentSuccessful = false;

        System.out.println(new Date() + ": " + msisdn + ":@PaymentManager.initiatePayement pricepointID:" + pricePoint.getPricePointId());
        if (pricePoint.getBillingMech().equals("1")) {
            paymentSuccessful = doShortCodeBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
            System.out.println(new Date() + ":shortcode billing billing:" + pricePoint.getBillingUrl());
        } else if (pricePoint.getBillingMech().equals("3")) {
            paymentSuccessful = doOTBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl, serviceCode, keyword);
            System.out.println(new Date() + ": " + msisdn + ":OT billing:" + pricePoint.getBillingUrl());
        } else if (pricePoint.getBillingMech().equals("2")) {
            System.out.println(new Date() + ":PIN redemption:" + pricePoint.getBillingUrl());
            paymentSuccessful = doPinRedemption(pricePoint, pin, callBackUrl);
        } else if (pricePoint.getBillingMech().equals("4")) {
            System.out.println(new Date() + ":Airtel Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doAirtelBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl, serviceCode, keyword);
        } else if (pricePoint.getBillingMech().equals("5")) {
            System.out.println(new Date() + ":Glo Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doGloBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl, serviceCode, keyword);
        } else if (pricePoint.getBillingMech().equals("6")) {
            System.out.println(new Date() + ":Unified Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doUnifiedBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl, serviceCode, keyword);
        } else {
            System.out.println(new Date() + ":Unified Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doUnifiedBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl, serviceCode, keyword);
        }
        return paymentSuccessful;
    }

    public static boolean initiatePayment(PricePoint pricePoint, CPConnections cnxn, String msisdn, String messageString, String pin, String callBackUrl, HashMap cdrFields)
            throws Exception {
        boolean paymentSuccessful = false;

        System.out.println(new Date() + ": " + msisdn + ":@PaymentManager.initiatePayement pricepointID:" + pricePoint.getPricePointId());
        if (pricePoint.getBillingMech().equals("1")) {
            paymentSuccessful = doShortCodeBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
            System.out.println(new Date() + ":shortcode billing billing:" + pricePoint.getBillingUrl());
        } else if (pricePoint.getBillingMech().equals("3")) {
            paymentSuccessful = doOTBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl, cdrFields);
            System.out.println(new Date() + ": " + msisdn + ":OT billing:" + pricePoint.getBillingUrl());
        } else if (pricePoint.getBillingMech().equals("2")) {
            System.out.println(new Date() + ":PIN redemption:" + pricePoint.getBillingUrl());
            paymentSuccessful = doPinRedemption(pricePoint, pin, callBackUrl);
        } else if (pricePoint.getBillingMech().equals("4")) {
            System.out.println(new Date() + ":Airtel Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doAirtelBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
        } else if (pricePoint.getBillingMech().equals("5")) {
            System.out.println(new Date() + ":Glo Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doGloBilling(pricePoint, cnxn, msisdn, messageString, callBackUrl);
        } else {
            System.out.println(new Date() + ":Unified Billing:" + pricePoint.getBillingUrl());
            paymentSuccessful = doUnifiedBilling(pricePoint, cnxn, msisdn, callBackUrl, callBackUrl);
        }
        return paymentSuccessful;
    }

    private static boolean doShortCodeBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn, String messageString, String callBackUrl)
            throws Exception {
        boolean isBilled = false;
        String resp = "";
        HttpClient client = null;
        GetMethod httpGETFORM = null;
        System.out.println(new Date() + ":performing shortcode based billing...");
        try {
            String rawurl = pricePoint.getBillingUrl();

            String username = URLUTF8Encoder.encode(cnxn.getUsername());
            String password = URLUTF8Encoder.encode(cnxn.getPassword());
            String transId = URLUTF8Encoder.encode(messageString);
            String number = URLUTF8Encoder.encode(msisdn);
            String conn = URLUTF8Encoder.encode(cnxn.getConnection());

            String insertions = "username=" + username + "&password=" + password + "&msg=" + transId + "&msisdn=" + number + "&conn=" + conn;
            String formattedurl = URLUTF8Encoder.doMessageEscaping(insertions, rawurl);
            System.out.println(new Date() + ":Billing UR_RAW: " + rawurl);
            System.out.println(insertions);

            System.out.println(new Date() + ":finalurl(post_formatting):" + formattedurl);

            client = new HttpClient();
            httpGETFORM = new GetMethod(formattedurl);

            client.executeMethod(httpGETFORM);
            resp = getResponse(httpGETFORM.getResponseBodyAsStream());
            System.out.println(new Date() + ":Billing response: " + resp);
            if (resp == null) {
                throw new Exception("11000");
            }
            return processResponse(resp);
        } catch (HttpException e) {
            resp = "5001: " + e.getMessage();

            System.out.println(new Date() + "PROTOCOL_ERROR:error response: " + resp);

            isBilled = false;


            return processResponse(resp);
        } catch (IOException e) {
            resp = "5002:TRANSPORT_ERROR:" + e.getMessage();

            System.out.println(new Date() + ":error response: " + resp);

            isBilled = false;


            return processResponse(resp);
        } catch (Exception e) {
            System.out.println(new Date() + ":error response: " + e.getMessage());

            isBilled = false;


            return processResponse(resp);
        } finally {
            httpGETFORM.releaseConnection();
            client = null;
            httpGETFORM = null;
            pricePoint = null;
        }

    }

    private static boolean doShortCodeBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn, String messageString)
            throws Exception {
        boolean isBilled = false;
        String resp = "";
        HttpClient client = null;
        GetMethod httpGETFORM = null;
        System.out.println(new Date() + ":performing shortcode based billing...");
        try {
            String rawurl = pricePoint.getBillingUrl();

            String username = URLUTF8Encoder.encode(cnxn.getUsername());
            String password = URLUTF8Encoder.encode(cnxn.getPassword());
            String transId = URLUTF8Encoder.encode(messageString);
            String number = URLUTF8Encoder.encode(msisdn);
            String conn = URLUTF8Encoder.encode(cnxn.getConnection());

            String insertions = "username=" + username + "&password=" + password + "&msg=" + transId + "&msisdn=" + number + "&conn=" + conn;
            String formattedurl = URLUTF8Encoder.doMessageEscaping(insertions, rawurl);
            System.out.println(new Date() + ":Billing UR_RAW: " + rawurl);
            System.out.println(insertions);

            System.out.println(new Date() + ":finalurl(post_formatting):" + formattedurl);

            client = new HttpClient();
            httpGETFORM = new GetMethod(formattedurl);

            client.executeMethod(httpGETFORM);
            resp = getResponse(httpGETFORM.getResponseBodyAsStream());
            System.out.println(new Date() + ":Billing response: " + resp);
            if (resp == null) {
                throw new Exception("11000");
            }
            return processResponse(resp);
        } catch (HttpException e) {
            resp = "5001: " + e.getMessage();

            System.out.println(new Date() + "PROTOCOL_ERROR:error response: " + resp);

            isBilled = false;


            return processResponse(resp);
        } catch (IOException e) {
            resp = "5002:TRANSPORT_ERROR:" + e.getMessage();

            System.out.println(new Date() + ":error response: " + resp);

            isBilled = false;


            return processResponse(resp);
        } catch (Exception e) {
            System.out.println(new Date() + ":error response: " + e.getMessage());

            isBilled = false;


            return processResponse(resp);
        } finally {
            httpGETFORM.releaseConnection();
            client = null;
            httpGETFORM = null;
            pricePoint = null;
        }
    }

    private static boolean doPinRedemption(PricePoint pricePoint, String pin, String callBackUrl)
            throws Exception {
        boolean isBilled = false;
        ePin voucher = new ePin();
        try {
            voucher.setPin(pin);
            if (!voucher.isValid()) {
                throw new Exception("4002");
            }
            if (voucher.getCurrentBalance() - Double.parseDouble(pricePoint.getValue()) < 0.0D) {
                throw new Exception("11002");
            }
            voucher.setCurrentBalance(voucher.getCurrentBalance() - Double.parseDouble(pricePoint.getValue()));
            voucher.updateMyLog();
            return true;
        } catch (Exception e) {
            return isBilled;
        } finally {
            pricePoint = null;
            voucher = null;
        }
    }

    private static boolean doPinRedemption(PricePoint pricePoint, String pin)
            throws Exception {
        boolean isBilled = false;
        ePin voucher = new ePin();
        try {
            voucher.setPin(pin);
            if (!voucher.isValid()) {
                throw new Exception("4002");
            }
            if (voucher.getCurrentBalance() - Double.parseDouble(pricePoint.getValue()) < 0.0D) {
                throw new Exception("11002");
            }
            voucher.setCurrentBalance(voucher.getCurrentBalance() - Double.parseDouble(pricePoint.getValue()));
            voucher.updateMyLog();
            return true;
        } catch (Exception e) {
            return isBilled;
        } finally {
            pricePoint = null;
            voucher = null;
        }
    }

    private static synchronized boolean doOTBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn, String transactionId, String callBackUrl)
            throws Exception {
        String STATUS_BILLED = "0";
        String INSUFFICIENT_CREDIT = "10";
        String POSTPAID_SUBSCRIBER = "919";
        int URL_CALL_TIMEOUT = 8000;

        HttpClient client = null;
        GetMethod httpGETFORM = null;
        String resp = "";

        String number = msisdn.substring(msisdn.indexOf("+233") + 4);

        String username = URLUTF8Encoder.encode(cnxn.getUsername());
        String password = URLUTF8Encoder.encode(cnxn.getPassword());
        number = URLUTF8Encoder.encode(number);

        String rawurl = pricePoint.getBillingUrl();
        String action = "&action=5";

        String transGuid = "";
        if (!transactionId.equals("")) {
            transGuid = "&trans_guid=" + transactionId;
        }
        String insertions = "username=" + username + "&password=" + password + "&msisdn=" + number + "&price=" + pricePoint.getValue() + transGuid;
        rawurl = URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        String callBackUrlParam = "&cUrl=" + URLUTF8Encoder.encode(callBackUrl);
        String formattedurl = rawurl + action + transGuid + callBackUrlParam;

        System.out.println(new Date() + ":billingURL:" + formattedurl);
        try {
            client = new HttpClient();
            httpGETFORM = new GetMethod(formattedurl);
            httpGETFORM.getParams().setParameter("http.socket.timeout", new Integer(8000));

            client.executeMethod(httpGETFORM);
            resp = getResponse(httpGETFORM.getResponseBodyAsStream());
            System.out.println(new Date() + ":OT_Billing URL call complete!");
        } catch (Exception e) {
            System.out.println(new Date() + ":" + msisdn + ":exception billing OT subscriber: " + e.getMessage());
            resp = null;
            throw new Exception("READ_TIMEOUT");
        } finally {
            httpGETFORM.releaseConnection();
            client = null;
            httpGETFORM = null;
        }
        System.out.println(new Date() + ":" + msisdn + ":billing status:" + resp);

        String keyword = "UNKNOWN";
        String serviceCode = "UNKNOWN";
        logBillTrans(transactionId, cnxn.getProviderId(), msisdn, pricePoint.getValue(), resp, transactionId, serviceCode, keyword, cnxn.getProviderId(), "VODAFONE");
        if (resp != null) {
            if ((resp.equals("0")) || (resp.equals("919"))) {
                return true;
            }
            if (resp.equals("10")) {
                throw new Exception("INSUFFICIENT_CREDIT");
            }
            throw new Exception("ERROR");
        }
        throw new Exception("READ_TIMEOUT");
    }

    private static synchronized boolean doOTBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn, String transactionId, String callBackUrl, String serviceCode, String keyword)
            throws Exception {
        String STATUS_BILLED = "0";
        String INSUFFICIENT_CREDIT = "10";
        String POSTPAID_SUBSCRIBER = "919";
        int URL_CALL_TIMEOUT = 8000;

        HttpClient client = null;
        GetMethod httpGETFORM = null;
        String resp = "";

        String number = msisdn.substring(msisdn.indexOf("+233") + 4);

        String username = URLUTF8Encoder.encode(cnxn.getUsername());
        String password = URLUTF8Encoder.encode(cnxn.getPassword());
        number = URLUTF8Encoder.encode(number);

        String rawurl = pricePoint.getBillingUrl();
        String action = "&action=5";

        String transGuid = "";
        if (!transactionId.equals("")) {
            transGuid = "&trans_guid=" + transactionId;
        }
        String insertions = "username=" + username + "&password=" + password + "&msisdn=" + number + "&price=" + pricePoint.getValue() + transGuid;
        rawurl = URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        String callBackUrlParam = "&cUrl=" + URLUTF8Encoder.encode(callBackUrl);
        String formattedurl = rawurl + action + transGuid + callBackUrlParam;

        System.out.println(new Date() + ":billingURL:" + formattedurl);
        try {
            client = new HttpClient();
            httpGETFORM = new GetMethod(formattedurl);
            httpGETFORM.getParams().setParameter("http.socket.timeout", new Integer(8000));

            client.executeMethod(httpGETFORM);
            resp = getResponse(httpGETFORM.getResponseBodyAsStream());
            System.out.println(new Date() + ":OT_Billing URL call complete!");
        } catch (Exception e) {
            System.out.println(new Date() + ":" + msisdn + ":exception billing OT subscriber: " + e.getMessage());
            resp = null;
            throw new Exception("READ_TIMEOUT");
        } finally {
            httpGETFORM.releaseConnection();
            client = null;
            httpGETFORM = null;
        }
        System.out.println(new Date() + ":" + msisdn + ":billing status:" + resp);
        logVodafoneBillTrans(transactionId, cnxn.getProviderId(), msisdn, pricePoint.getValue(), resp, transactionId, serviceCode, keyword, cnxn.getProviderId(), "VODAFONE");
        if (resp != null) {
            if ((resp.equals("0")) || (resp.equals("919"))) {
                return true;
            }
            if (resp.equals("10")) {
                throw new Exception("INSUFFICIENT_CREDIT");
            }
            throw new Exception("ERROR");
        }
        throw new Exception("READ_TIMEOUT");
    }

    private static synchronized boolean doOTBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn, String transactionId, String callBackUrl, HashMap cdrfields)
            throws Exception {
        String STATUS_BILLED = "0";
        String INSUFFICIENT_CREDIT = "10";
        int URL_CALL_TIMEOUT = 8000;

        HttpClient client = null;
        GetMethod httpGETFORM = null;
        String resp = "";

        String number = msisdn.substring(msisdn.indexOf("+233") + 4);

        String username = URLUTF8Encoder.encode(cnxn.getUsername());
        String password = URLUTF8Encoder.encode(cnxn.getPassword());
        number = URLUTF8Encoder.encode(number);

        String rawurl = pricePoint.getBillingUrl();
        String action = "&action=5";

        String transGuid = "";
        if (!transactionId.equals("")) {
            transGuid = "&trans_guid=" + transactionId;
        }
        String insertions = "username=" + username + "&password=" + password + "&msisdn=" + number + "&price=" + pricePoint.getValue() + transGuid;
        rawurl = URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        String callBackUrlParam = "&cUrl=" + URLUTF8Encoder.encode(callBackUrl);
        String formattedurl = rawurl + action + transGuid + callBackUrlParam;

        System.out.println(new Date() + ":billingURL:" + formattedurl);
        try {
            client = new HttpClient();
            httpGETFORM = new GetMethod(formattedurl);
            httpGETFORM.getParams().setParameter("http.socket.timeout", new Integer(8000));

            client.executeMethod(httpGETFORM);
            resp = getResponse(httpGETFORM.getResponseBodyAsStream());
            System.out.println(new Date() + ":OT_Billing URL call complete!");
        } catch (Exception e) {
            System.out.println(new Date() + ":" + msisdn + ":exception billing OT subscriber: " + e.getMessage());
            resp = null;
            throw new Exception("READ_TIMEOUT");
        } finally {
            httpGETFORM.releaseConnection();
            client = null;
            httpGETFORM = null;
        }
        System.out.println(new Date() + ":" + msisdn + ":billing status:" + resp);
        if (resp != null) {
            if (resp.equals("0")) {
                return true;
            }
            if (resp.equals("10")) {
                throw new Exception("INSUFFICIENT_CREDIT");
            }
            throw new Exception("ERROR");
        }
        throw new Exception("READ_TIMEOUT");
    }

    private static synchronized boolean doOTBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn, String transactionId)
            throws Exception {
        boolean isBilled = false;
        HttpClient client = null;
        GetMethod httpGETFORM = null;
        String ACCOUNT_NOT_FOUND = "201";
        String ACCOUNT_NOT_IN_DB = "5";
        String STATUS_BILLED = "0";
        String resp = "";
        boolean transSuccessful = false;
        long MILLISECONDS_BETWEEN_RETRY = 0L;
        int NUM_OF_RETRIES = 1;
        int URL_CALL_TIMEOUT = 8000;

        String number = msisdn.substring(msisdn.indexOf("+233") + 4);

        String username = URLUTF8Encoder.encode(cnxn.getUsername());
        String password = URLUTF8Encoder.encode(cnxn.getPassword());
        number = URLUTF8Encoder.encode(number);

        String rawurl = pricePoint.getBillingUrl();
        String action = "&action=5";

        String transGuid = "";
        if (!transactionId.equals("")) {
            transGuid = "&trans_guid=" + transactionId;
        }
        String insertions = "username=" + username + "&password=" + password + "&msisdn=" + number + "&price=" + pricePoint.getValue() + transGuid;
        rawurl = URLUTF8Encoder.doMessageEscaping(insertions, rawurl);


        String callBackUrlParam = "&cUrl=" + URLUTF8Encoder.encode(new StringBuilder().append("http://localhost:8084/rmcsserver/rmcspostbilling.jsp?transId=").append(transactionId).toString());
        String formattedurl = rawurl + action + transGuid + callBackUrlParam;

        System.out.println(new Date() + ":billingURL:" + formattedurl);
        try {
            client = new HttpClient();
            httpGETFORM = new GetMethod(formattedurl);
            httpGETFORM.getParams().setParameter("http.socket.timeout", new Integer(8000));

            client.executeMethod(httpGETFORM);
            resp = getResponse(httpGETFORM.getResponseBodyAsStream());
            System.out.println(new Date() + ":OT_Billing URL call complete!");
        } catch (Exception e) {
            System.out.println(new Date() + ":" + msisdn + ":exception billing OT subscriber: " + e.getMessage());
        } finally {
            httpGETFORM.releaseConnection();
            client = null;
            httpGETFORM = null;
        }
        System.out.println(new Date() + ":" + msisdn + ":billing status:" + resp);
        if ((resp != null) && ((resp.equals("0")) || (resp.equals("201")) || (resp.equals("5")))) {
            isBilled = true;
        } else {
            isBilled = false;
        }
        return isBilled;
    }

    private static synchronized boolean doOTBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn, String transactionId, HashMap fields)
            throws Exception {
        boolean isBilled = false;
        HttpClient client = null;
        GetMethod httpGETFORM = null;
        String ACCOUNT_NOT_FOUND = "201";
        String ACCOUNT_NOT_IN_DB = "5";
        String INSUFFICIENT_CREDIT = "10";
        String STATUS_BILLED = "0";
        String resp = "";
        boolean transSuccessful = false;
        long MILLISECONDS_BETWEEN_RETRY = 0L;
        int NUM_OF_RETRIES = 1;
        int URL_CALL_TIMEOUT = 8000;

        String number = msisdn.substring(msisdn.indexOf("+233") + 4);

        String username = URLUTF8Encoder.encode(cnxn.getUsername());
        String password = URLUTF8Encoder.encode(cnxn.getPassword());
        number = URLUTF8Encoder.encode(number);

        String rawurl = pricePoint.getBillingUrl();
        String action = "&action=5";

        String transGuid = "";
        if (!transactionId.equals("")) {
            transGuid = "&trans_guid=" + transactionId;
        }
        String insertions = "username=" + username + "&password=" + password + "&msisdn=" + number + "&price=" + pricePoint.getValue() + transGuid;
        rawurl = URLUTF8Encoder.doMessageEscaping(insertions, rawurl);


        String callBackUrlParam = "&cUrl=" + URLUTF8Encoder.encode(new StringBuilder().append("http://localhost:8084/rmcsserver/rmcspostbilling.jsp?transId=").append(transactionId).toString());
        String formattedurl = rawurl + action + transGuid + callBackUrlParam;

        System.out.println(new Date() + ":billingURL:" + formattedurl);
        try {
            client = new HttpClient();
            httpGETFORM = new GetMethod(formattedurl);
            httpGETFORM.getParams().setParameter("http.socket.timeout", new Integer(8000));

            client.executeMethod(httpGETFORM);
            resp = getResponse(httpGETFORM.getResponseBodyAsStream());
            System.out.println(new Date() + ":OT_Billing URL call complete!");
        } catch (Exception e) {
            System.out.println(new Date() + ":" + msisdn + ":exception billing OT subscriber: " + e.getMessage());
        } finally {
            httpGETFORM.releaseConnection();
            client = null;
            httpGETFORM = null;
        }
        System.out.println(new Date() + ":" + msisdn + ":billing status:" + resp);
        if ((resp != null) && ((resp.equals("0")) || (resp.equals("201")) || (resp.equals("5")))) {
            isBilled = true;
        } else {
            isBilled = false;
        }
        if (resp != null) {
            if (resp.equals("201")) {
                Date now = new Date();

                isBilled = logVodafoneTransaction("vodafone_gateway", fields.get("transaction_type").toString(), now, fields.get("cost").toString(), fields.get("service_code").toString(), fields.get("aparty").toString(), fields.get("bparty").toString(), fields.get("provider_id").toString(), "cdr");
            } else {
                if (resp.equals("0")) {
                    return true;
                }
                if (resp.equals("10")) {
                    throw new Exception("INSUFFICIENT_CREDIT");
                }
                throw new Exception("ERROR");
            }
        } else {
            throw new Exception("READ_TIMEOUT");
        }
        return isBilled;
    }

    private static synchronized boolean doAirtelBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn, String transactionId, String callBackUrl)
            throws Exception {
        String BILLING_SUCCESSFUL = "000";
        String INSUFFICIENT_FUNDS = "910";
        int WAIT_BTN_BILLING_RETRIES = 250;
        int NUM_OF_BILLING_RETRIES = 3;
        String INCOMPLETE_TRANSACTION = "911";
        String NO_RESPONSE_FROM_BILLING_SERVER = "915";
        String SYSTEM_BUSY = "914";
        String POSTPAID_SUBSCRIBER = "919";


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
        String billingUrl = URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        post = new GetMethod(billingUrl);
        String statusToken = "915";
        try {
            int count = 1;
            do {
                client.executeMethod(post);
                System.out.println(new Date() + ": AIRTEL_GH BILLING DRIVER: [x-DEBUG] Control passed on to external" + " node for billing! Expecting return in this thread.");


                responseBody = post.getResponseBodyAsString().trim();
                System.out.println(new Date() + ": AIRTEL_GH BILLING DRIVER: [x-INFO] Response from Billing server during subscription renewal': " + responseBody);


                System.out.println(new Date() + ": @airtel_gh-billing-impl.jsp: AIRTEL_GH BILLING DRIVER: [x-DEBUG]" + transactionId + ": bill attempt: " + count);
                if (responseBody != null) {
                    tokens = responseBody.split(":");
                }
                if (tokens.length <= 0) {
                    break;
                }
                statusToken = tokens[0];


                count++;
                if (((statusToken.equals("911")) || (statusToken.equals("914"))) && (count <= 3)) {
                    Thread.sleep(250L);
                }
                if ((!statusToken.equals("911")) && (!statusToken.equals("914"))) {
                    break;
                }
            } while (count <= 3);
        } catch (HttpException e) {
            System.out.println(new Date() + ": AIRTEL_GH BILLING DRIVER: [ERROR] Fatal protocol violation: " + e.getMessage());
        } catch (IOException e) {
            System.out.println(new Date() + ": AIRTEL_GH BILLING DRIVER: [ERROR] Fatal transport error: " + e.getMessage());
        } finally {
            post.releaseConnection();
            client = null;
        }
        if (statusToken != null) {
            if ((statusToken.equals("000")) || (statusToken.equals("919"))) {
                System.out.println(new Date() + ": AIRTEL_GH BILLING DRIVER: [x-INFO] Control for billing returned." + " Account of subscriber: " + msisdn + " has successfully been debitted for service on " + cnxn.getProviderId() + " account. Completing transaction " + transactionId);


                return true;
            }
            if (statusToken.equals("910")) {
                System.out.println(new Date() + ": AIRTEL_GH BILLING DRIVER: [x-INFO] Control for billing returned." + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId() + " account due to INSUFFICIENT FUNDS. Will NOT complete transaction " + transactionId);


                throw new Exception("INSUFFICIENT_CREDIT");
            }
            System.out.println(new Date() + ": AIRTEL_GH BILLING DRIVER: [x-INFO] Control for billing returned." + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId() + " account due to ERROR. Will NOT complete transaction " + transactionId);


            throw new Exception("ERROR");
        }
        throw new Exception("READ_TIMEOUT");
    }

    private static synchronized boolean doAirtelBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn, String transactionId, String callBackUrl, String serviceCode, String keyword)
            throws Exception {
        String BILLING_SUCCESSFUL = "000";
        String INSUFFICIENT_FUNDS = "910";
        int WAIT_BTN_BILLING_RETRIES = 250;
        int NUM_OF_BILLING_RETRIES = 3;
        String INCOMPLETE_TRANSACTION = "911";
        String NO_RESPONSE_FROM_BILLING_SERVER = "915";
        String SYSTEM_BUSY = "914";
        String POSTPAID_SUBSCRIBER = "919";


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
        String billingUrl = URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        post = new GetMethod(billingUrl);
        String statusToken = "915";
        try {
            int count = 1;
            do {
                client.executeMethod(post);
                System.out.println(new Date() + ": AIRTEL_GH BILLING DRIVER: [x-DEBUG] Control passed on to external" + " node for billing! Expecting return in this thread.");


                responseBody = post.getResponseBodyAsString().trim();
                System.out.println(new Date() + ": AIRTEL_GH BILLING DRIVER: [x-INFO] Response from Billing server during subscription renewal': " + responseBody);


                System.out.println(new Date() + ": @airtel_gh-billing-impl.jsp: AIRTEL_GH BILLING DRIVER: [x-DEBUG]" + transactionId + ": bill attempt: " + count);
                if (responseBody != null) {
                    tokens = responseBody.split(":");
                }
                if (tokens.length <= 0) {
                    break;
                }
                statusToken = tokens[0];


                count++;
                if (((statusToken.equals("911")) || (statusToken.equals("914"))) && (count <= 3)) {
                    Thread.sleep(250L);
                }
                if ((!statusToken.equals("911")) && (!statusToken.equals("914"))) {
                    break;
                }
            } while (count <= 3);
        } catch (HttpException e) {
            System.out.println(new Date() + ": AIRTEL_GH BILLING DRIVER: [ERROR] Fatal protocol violation: " + e.getMessage());
        } catch (IOException e) {
            System.out.println(new Date() + ": AIRTEL_GH BILLING DRIVER: [ERROR] Fatal transport error: " + e.getMessage());
        } finally {
            post.releaseConnection();
            client = null;
        }
        logBillTrans(transactionId, cnxn.getProviderId(), msisdn, pricePoint.getValue(), statusToken, transactionId, serviceCode, keyword, cnxn.getProviderId(), "ZAIN_RANCARD");
        if (statusToken != null) {
            if ((statusToken.equals("000")) || (statusToken.equals("919"))) {
                System.out.println(new Date() + ": AIRTEL_GH BILLING DRIVER: [x-INFO] Control for billing returned." + " Account of subscriber: " + msisdn + " has successfully been debitted for service on " + cnxn.getProviderId() + " account. Completing transaction " + transactionId);


                return true;
            }
            if (statusToken.equals("910")) {
                System.out.println(new Date() + ": AIRTEL_GH BILLING DRIVER: [x-INFO] Control for billing returned." + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId() + " account due to INSUFFICIENT FUNDS. Will NOT complete transaction " + transactionId);


                throw new Exception("INSUFFICIENT_CREDIT");
            }
            System.out.println(new Date() + ": AIRTEL_GH BILLING DRIVER: [x-INFO] Control for billing returned." + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId() + " account due to ERROR. Will NOT complete transaction " + transactionId);


            throw new Exception("ERROR");
        }
        throw new Exception("READ_TIMEOUT");
    }

    private static synchronized boolean doGloBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn, String transactionId, String callBackUrl)
            throws Exception {
        String BILLING_SUCCESSFUL = "000";
        String INSUFFICIENT_FUNDS = "910";
        int WAIT_BTN_BILLING_RETRIES = 250;
        int NUM_OF_BILLING_RETRIES = 3;
        String INCOMPLETE_TRANSACTION = "911";
        String NO_RESPONSE_FROM_BILLING_SERVER = "915";
        String SYSTEM_BUSY = "914";


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
        String billingUrl = URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        post = new GetMethod(billingUrl);
        String statusToken = "915";
        try {
            int count = 1;
            do {
                client.executeMethod(post);
                System.out.println(new Date() + ": GLO_NIG BILLING DRIVER: [x-DEBUG] Control passed on to external" + " node for billing! Expecting return in this thread.");


                responseBody = post.getResponseBodyAsString().trim();
                System.out.println(new Date() + ": GLO_NIG BILLING DRIVER: [x-INFO] Response from Billing server during subscription renewal': " + responseBody);


                System.out.println(new Date() + ": GLO_NIG BILLING DRIVER: [x-DEBUG]" + transactionId + ": bill attempt: " + count);
                if (responseBody != null) {
                    tokens = responseBody.split(":");
                }
                if (tokens.length <= 0) {
                    break;
                }
                statusToken = tokens[0];


                count++;
                if (((statusToken.equals("911")) || (statusToken.equals("914"))) && (count <= 3)) {
                    Thread.sleep(250L);
                }
                if ((!statusToken.equals("911")) && (!statusToken.equals("914"))) {
                    break;
                }
            } while (count <= 3);
        } catch (HttpException e) {
            System.out.println(new Date() + ": GLO_NIG BILLING DRIVER: [ERROR] Fatal protocol violation: " + e.getMessage());
        } catch (IOException e) {
            System.out.println(new Date() + ": GLO_NIG BILLING DRIVER: [ERROR] Fatal transport error: " + e.getMessage());
        } finally {
            post.releaseConnection();
            client = null;
        }
        if (statusToken != null) {
            if (statusToken.equals("000")) {
                System.out.println(new Date() + ": GLO_NIG BILLING DRIVER: [x-INFO] Control for billing returned." + " Account of subscriber: " + msisdn + " has successfully been debitted for service on " + cnxn.getProviderId() + " account. Completing transaction " + transactionId);


                return true;
            }
            if (statusToken.equals("910")) {
                System.out.println(new Date() + ": GLO_NIG BILLING DRIVER: [x-INFO] Control for billing returned." + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId() + " account due to INSUFFICIENT FUNDS. Will NOT complete transaction " + transactionId);


                throw new Exception("INSUFFICIENT_CREDIT");
            }
            System.out.println(new Date() + ": GLO_NIG BILLING DRIVER: [x-INFO] Control for billing returned." + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId() + " account due to ERROR. Will NOT complete transaction " + transactionId);


            throw new Exception("ERROR");
        }
        throw new Exception("READ_TIMEOUT");
    }

    private static synchronized boolean doGloBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn, String transactionId, String callBackUrl, String serviceCode, String keyword)
            throws Exception {
        String BILLING_SUCCESSFUL = "000";
        String INSUFFICIENT_FUNDS = "910";
        int WAIT_BTN_BILLING_RETRIES = 250;
        int NUM_OF_BILLING_RETRIES = 3;
        String INCOMPLETE_TRANSACTION = "911";
        String NO_RESPONSE_FROM_BILLING_SERVER = "915";
        String SYSTEM_BUSY = "914";


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
        String billingUrl = URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        post = new GetMethod(billingUrl);
        String statusToken = "915";
        try {
            int count = 1;
            do {
                client.executeMethod(post);
                System.out.println(new Date() + ": GLO_NIG BILLING DRIVER: [x-DEBUG] Control passed on to external" + " node for billing! Expecting return in this thread.");


                responseBody = post.getResponseBodyAsString().trim();
                System.out.println(new Date() + ": GLO_NIG BILLING DRIVER: [x-INFO] Response from Billing server during subscription renewal': " + responseBody);


                System.out.println(new Date() + ": GLO_NIG BILLING DRIVER: [x-DEBUG]" + transactionId + ": bill attempt: " + count);
                if (responseBody != null) {
                    tokens = responseBody.split(":");
                }
                if (tokens.length <= 0) {
                    break;
                }
                statusToken = tokens[0];


                count++;
                if (((statusToken.equals("911")) || (statusToken.equals("914"))) && (count <= 3)) {
                    Thread.sleep(250L);
                }
                if ((!statusToken.equals("911")) && (!statusToken.equals("914"))) {
                    break;
                }
            } while (count <= 3);
        } catch (HttpException e) {
            System.out.println(new Date() + ": GLO_NIG BILLING DRIVER: [ERROR] Fatal protocol violation: " + e.getMessage());
        } catch (IOException e) {
            System.out.println(new Date() + ": GLO_NIG BILLING DRIVER: [ERROR] Fatal transport error: " + e.getMessage());
        } finally {
            post.releaseConnection();
            client = null;
        }
        logBillTrans(transactionId, cnxn.getProviderId(), msisdn, pricePoint.getValue(), statusToken, transactionId, serviceCode, keyword, cnxn.getProviderId(), "GLO_NIGERIA");
        if (statusToken != null) {
            if (statusToken.equals("000")) {
                System.out.println(new Date() + ": GLO_NIG BILLING DRIVER: [x-INFO] Control for billing returned." + " Account of subscriber: " + msisdn + " has successfully been debitted for service on " + cnxn.getProviderId() + " account. Completing transaction " + transactionId);


                return true;
            }
            if (statusToken.equals("910")) {
                System.out.println(new Date() + ": GLO_NIG BILLING DRIVER: [x-INFO] Control for billing returned." + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId() + " account due to INSUFFICIENT FUNDS. Will NOT complete transaction " + transactionId);


                throw new Exception("INSUFFICIENT_CREDIT");
            }
            System.out.println(new Date() + ": GLO_NIG BILLING DRIVER: [x-INFO] Control for billing returned." + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId() + " account due to ERROR. Will NOT complete transaction " + transactionId);


            throw new Exception("ERROR");
        }
        throw new Exception("READ_TIMEOUT");
    }

    private static synchronized boolean doUnifiedBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn, String transactionId, String callBackUrl)
            throws Exception {
        String BILLING_SUCCESSFUL = "000";
        String INSUFFICIENT_FUNDS = "910";
        int WAIT_BTN_BILLING_RETRIES = 250;
        int NUM_OF_BILLING_RETRIES = 3;
        String INCOMPLETE_TRANSACTION = "911";
        String NO_RESPONSE_FROM_BILLING_SERVER = "915";
        String SYSTEM_BUSY = "914";


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
        String billingUrl = URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        post = new GetMethod(billingUrl);
        String statusToken = "915";
        try {
            int count = 1;
            do {
                client.executeMethod(post);
                System.out.println(new Date() + ": UNIFIED BILLING DRIVER: [x-DEBUG] Control passed on to external" + " node for billing! Expecting return in this thread.");


                responseBody = post.getResponseBodyAsString().trim();
                System.out.println(new Date() + ": UNIFIED BILLING DRIVER: [x-INFO] Response from Billing server during subscription renewal': " + responseBody);


                System.out.println(new Date() + ": UNIFIED BILLING DRIVER: [x-DEBUG]" + transactionId + ": bill attempt: " + count);
                if (responseBody != null) {
                    tokens = responseBody.split(":");
                }
                if (tokens.length <= 0) {
                    break;
                }
                statusToken = tokens[0];


                count++;
                if (((statusToken.equals("911")) || (statusToken.equals("914"))) && (count <= 3)) {
                    Thread.sleep(250L);
                }
                if ((!statusToken.equals("911")) && (!statusToken.equals("914"))) {
                    break;
                }
            } while (count <= 3);
        } catch (HttpException e) {
            System.out.println(new Date() + ": UNIFIED BILLING DRIVER: [ERROR] Fatal protocol violation: " + e.getMessage());
        } catch (IOException e) {
            System.out.println(new Date() + ": UNIFIED BILLING DRIVER: [ERROR] Fatal transport error: " + e.getMessage());
        } finally {
            post.releaseConnection();
            client = null;
        }
        if (statusToken != null) {
            if (statusToken.equals("000")) {
                System.out.println(new Date() + ": UNIFIED BILLING DRIVER: [x-INFO] Control for billing returned." + " Account of subscriber: " + msisdn + " has successfully been debitted for service on " + cnxn.getProviderId() + " account. Completing transaction " + transactionId);


                return true;
            }
            if (statusToken.equals("910")) {
                System.out.println(new Date() + ": UNIFIED BILLING DRIVER: [x-INFO] Control for billing returned." + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId() + " account due to INSUFFICIENT FUNDS. Will NOT complete transaction " + transactionId);


                throw new Exception("INSUFFICIENT_CREDIT");
            }
            System.out.println(new Date() + ": UNIFIED BILLING DRIVER: [x-INFO] Control for billing returned." + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId() + " account due to ERROR. Will NOT complete transaction " + transactionId);


            throw new Exception("ERROR");
        }
        throw new Exception("READ_TIMEOUT");
    }

    private static synchronized boolean doUnifiedBilling(PricePoint pricePoint, CPConnections cnxn, String msisdn, String transactionId, String callBackUrl, String serviceCode, String keyword)
            throws Exception {
        String BILLING_SUCCESSFUL = "000";
        String INSUFFICIENT_FUNDS = "910";
        int WAIT_BTN_BILLING_RETRIES = 250;
        int NUM_OF_BILLING_RETRIES = 3;
        String INCOMPLETE_TRANSACTION = "911";
        String NO_RESPONSE_FROM_BILLING_SERVER = "915";
        String SYSTEM_BUSY = "914";


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
        String billingUrl = URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        post = new GetMethod(billingUrl);
        String statusToken = "915";
        try {
            int count = 1;
            do {
                client.executeMethod(post);
                System.out.println(new Date() + ": UNIFIED BILLING DRIVER: [x-DEBUG] Control passed on to external" + " node for billing! Expecting return in this thread.");


                responseBody = post.getResponseBodyAsString().trim();
                System.out.println(new Date() + ": UNIFIED BILLING DRIVER: [x-INFO] Response from Billing server during subscription renewal': " + responseBody);


                System.out.println(new Date() + ": UNIFIED BILLING DRIVER: [x-DEBUG]" + transactionId + ": bill attempt: " + count);
                if (responseBody != null) {
                    tokens = responseBody.split(":");
                }
                if (tokens.length <= 0) {
                    break;
                }
                statusToken = tokens[0];


                count++;
                if (((statusToken.equals("911")) || (statusToken.equals("914"))) && (count <= 3)) {
                    Thread.sleep(250L);
                }
                if ((!statusToken.equals("911")) && (!statusToken.equals("914"))) {
                    break;
                }
            } while (count <= 3);
        } catch (HttpException e) {
            System.out.println(new Date() + ": UNIFIED BILLING DRIVER: [ERROR] Fatal protocol violation: " + e.getMessage());
        } catch (IOException e) {
            System.out.println(new Date() + ": UNIFIED BILLING DRIVER: [ERROR] Fatal transport error: " + e.getMessage());
        } finally {
            post.releaseConnection();
            client = null;
        }
        logBillTrans(transactionId, cnxn.getProviderId(), msisdn, pricePoint.getValue(), statusToken, transactionId, serviceCode, keyword, cnxn.getProviderId(), cnxn.getConnection().split(":")[0]);
        if (statusToken != null) {
            if (statusToken.equals("000")) {
                System.out.println(new Date() + ": UNIFIED BILLING DRIVER: [x-INFO] Control for billing returned." + " Account of subscriber: " + msisdn + " has successfully been debitted for service on " + cnxn.getProviderId() + " account. Completing transaction " + transactionId);


                return true;
            }
            if (statusToken.equals("910")) {
                System.out.println(new Date() + ": UNIFIED BILLING DRIVER: [x-INFO] Control for billing returned." + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId() + " account due to INSUFFICIENT FUNDS. Will NOT complete transaction " + transactionId);


                throw new Exception("INSUFFICIENT_CREDIT");
            }
            System.out.println(new Date() + ": UNIFIED BILLING DRIVER: [x-INFO] Control for billing returned." + " Account of subscriber: " + msisdn + " was NOT debitted for service on " + cnxn.getProviderId() + " account due to ERROR. Will NOT complete transaction " + transactionId);


            throw new Exception("ERROR");
        }
        throw new Exception("READ_TIMEOUT");
    }

    private static boolean logVodafoneTransaction(String serviceName, String transactionType, Date logDate, String cost, String serviceCode, String aParty, String bParty, String providerId, String logFolder) {
        boolean isSuccessful = true;
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String today = df.format(logDate);
        DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        String dateToday = df2.format(logDate);
        DateFormat df3 = new SimpleDateFormat("HH:mm:ss");
        String time = df3.format(logDate);

        BufferedReader br = null;
        PrintWriter output = null;
        if ((logFolder == null) || (logFolder.equals(""))) {
            logFolder = "unidentified";
        }
        try {
            File file = new File("/var/log/tomcat5/billing/VFONE-GH/" + logFolder + "/" + today + "-vodafone-tranx-log.csv");
            try {
                output = new PrintWriter(new FileWriter(file, true));
                br = new BufferedReader(new FileReader(file));

                String heading = br.readLine();
                br.close();
                if ((heading == null) || (heading.equals(""))) {
                    output.println("Service Name\tTransaction Type\tDate\tStart Time\tEnd Time\tDuration\tCost\tService Code\tA Party\tB Party\tReserved 1\tReserved 2\tReserved 3\tReserved 4\tProvider ID");
                }
                output.println(serviceName + "\t" + transactionType + "\t" + dateToday + "\t" + time + "\t" + time + "\t" + "00:00" + "\t" + cost + "\t" + serviceCode + "\t" + aParty + "\t" + bParty + "\t" + "" + "\t" + "" + "\t" + "" + "\t" + "" + "\t" + providerId);
            } catch (IOException ioe) {
                System.out.println(new Date() + ": [vodafoneGhBillingDriver:ERROR]: File IO Error: " + ioe.getMessage());
                System.out.println(new Date() + ": [vodafoneGhBillingDriver:WARNING]: Intended Insertion into folder: " + logFolder + "/ during IOError :: " + serviceName + "\t" + transactionType + "\t" + dateToday + "\t" + time + "\t" + time + "\t" + "00:00" + "\t" + cost + "\t" + serviceCode + "\t" + aParty + "\t" + bParty + "\t" + "" + "\t" + "" + "\t" + "" + "\t" + "" + "\t" + providerId);


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
            throw new Exception("5002: " + e.getMessage());
        } finally {
            br.close();
            in.close();

            br = null;
            in = null;
        }
        return responseString;
    }

    private static boolean processResponse(String reply)
            throws Exception {
        boolean isOk = false;
        if ((reply != null) && (!reply.equals(""))) {
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
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String today = df.format(new Date());
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df2.format(new Date());
        String logFile = "/var/log/tomcat5/networks/billing/" + loggingFolder + "_billing_" + today + ".log";
        String joiner = "";
        try {
            PrintWriter pr = new PrintWriter(new FileWriter(logFile, true));
            joiner = generateUID() + "\t" + trans_id + "\t" + cp_user_id + "\t" + msisdn + "\t" + amount + "\t" + status + "\t" + time + "\t" + trans_guid + "\t" + serviceCode + "\t" + keyword + "\t" + accountId;

            pr.println(joiner);
            pr.close();
        } catch (Exception e) {
            System.out.println("Could not write to file >> " + joiner + ":" + e.getMessage());
        }
    }

    private static void logVodafoneBillTrans(String trans_id, String cp_user_id, String msisdn, String amount, String status, String trans_guid, String serviceCode, String keyword, String accountId, String loggingFolder) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String today = df.format(new Date());
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df2.format(new Date());
        String logFile = "/var/log/tomcat5/ot/billing/billing_" + today + ".log";
        String joiner = "";
        try {
            PrintWriter pr = new PrintWriter(new FileWriter(logFile, true));
            joiner = generateUID() + "\t" + trans_id + "\t" + cp_user_id + "\t" + msisdn + "\t" + amount + "\t" + status + "\t" + time + "\t" + trans_guid + "\t" + serviceCode + "\t" + keyword + "\t" + accountId;

            pr.println(joiner);
            pr.close();
        } catch (Exception e) {
            System.out.println("Could not write to file >> " + joiner + ":" + e.getMessage());
        }
    }

    private static String generateUID() {
        SecureRandom wheel = new SecureRandom();
        int myInt = wheel.nextInt(10000000) * 10000000;
        String str = Integer.toString(myInt > 0 ? myInt : myInt * -1);
        while (str.length() < 4) {
            myInt = wheel.nextInt(10000000) * 10000000;
            str = Integer.toString(myInt > 0 ? myInt : myInt * -1);
        }
        Calendar cal = new GregorianCalendar();
        Date currentDate = cal.getTime();
        DateFormat df = new SimpleDateFormat("yyMMddHHmmss");
        return df.format(currentDate) + "" + str.substring(0, 4);
    }
}




















 