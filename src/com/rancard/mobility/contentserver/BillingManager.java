package com.rancard.mobility.contentserver;

import com.rancard.util.URLUTF8Encoder;
import com.rancard.util.payment.ePin;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public abstract class BillingManager {

    public static final String PIN_OPTION = "pinredemption";
    public static final String SHORTCODE_OPTION = "shortcode";
    public static final String OT_BILLING = "ot_bill";

    public static boolean doShortCodeBilling(String serviceType, CPConnections cnxn, String networkPrefix, String msisdn, String messageString, String keyword, String provId)
            throws Exception {
        boolean isBilled = false;

        ServicePricingBean bean = ServicePricingBean.viewPriceWithUrl(provId, serviceType, networkPrefix);
        if (bean == null) {
            throw new Exception("10003");
        }
        String rawurl = bean.getUrl();

        String username = URLUTF8Encoder.encode(cnxn.getUsername());
        String password = URLUTF8Encoder.encode(cnxn.getPassword());
        String transId = URLUTF8Encoder.encode(messageString);
        String number = URLUTF8Encoder.encode(msisdn);
        String shortcode = URLUTF8Encoder.encode(bean.getShortCode());
        String conn = URLUTF8Encoder.encode(cnxn.getConnection());

        String insertions = "username=" + username + "&password=" + password + "&msg=" + transId + "&msisdn=" + number + "&shortcode=" + shortcode + "&conn=" + conn;

        String formattedurl = URLUTF8Encoder.doMessageEscaping(insertions, rawurl);

        HttpClient client = new HttpClient();
        GetMethod httpGETFORM = new GetMethod(formattedurl);
        String resp = "";
        try {
            client.executeMethod(httpGETFORM);
            resp = getResponse(httpGETFORM.getResponseBodyAsStream());
            System.out.println("Billing response: " + resp);
            if (resp == null) {
                throw new Exception("11000");
            }
            return processResponse(resp);
        } catch (HttpException e) {
            resp = "5001: " + e.getMessage();

            System.out.println("error response: " + resp);

            isBilled = false;

            return processResponse(resp);
        } catch (IOException e) {
            resp = "5002: " + e.getMessage();

            System.out.println("error response: " + resp);

            isBilled = false;

            return processResponse(resp);
        } catch (Exception e) {
            System.out.println("error response: " + e.getMessage());

            isBilled = false;

            return processResponse(resp);
        } finally {
            httpGETFORM.releaseConnection();
            client = null;
            httpGETFORM = null;
            bean = null;
        }
    }

    public static boolean doPinRedemption(String serviceType, String provId, String networkPrefix, String pin, String keyword)
            throws Exception {
        boolean isBilled = false;

        ServicePricingBean bean = ServicePricingBean.viewPrice(provId, serviceType, networkPrefix);
        if (!bean.getKeyword().equalsIgnoreCase(keyword)) {
            throw new Exception("10003");
        }
        ePin voucher = new ePin();
        voucher.setPin(pin);
        if (!voucher.isValid()) {
            throw new Exception("4002");
        }
        if (voucher.getCurrentBalance() - Double.parseDouble(bean.getPrice()) < 0.0D) {
            throw new Exception("11002");
        }
        voucher.setCurrentBalance(voucher.getCurrentBalance() - Double.parseDouble(bean.getPrice()));
        voucher.updateMyLog();
        isBilled = true;

        bean = null;
        voucher = null;
        return isBilled;
    }

    public static boolean doOTBilling(String serviceType, CPConnections cnxn, String networkPrefix, String msisdn, String keyword, String provId)
            throws Exception {
        boolean isBilled = false;

        ServicePricingBean bean = ServicePricingBean.viewPriceWithUrl(provId, serviceType, networkPrefix);
        if (bean == null) {
            throw new Exception("10003");
        }
        String number = msisdn.substring(msisdn.indexOf("+233") + 4);

        String username = URLUTF8Encoder.encode(cnxn.getUsername());
        String password = URLUTF8Encoder.encode(cnxn.getPassword());
        String price = URLUTF8Encoder.encode(bean.getPrice());
        number = URLUTF8Encoder.encode(number);

        String rawurl = bean.getUrl();
        String action = "&action=2";
        String insertions = "username=" + username + "&password=" + password + "&msisdn=" + number + "&price=" + price;
        rawurl = URLUTF8Encoder.doMessageEscaping(insertions, rawurl);
        String formattedurl = rawurl + action;

        HttpClient client = new HttpClient();
        GetMethod httpGETFORM = new GetMethod(formattedurl);
        String resp = "";
        try {
            client.executeMethod(httpGETFORM);
            resp = getResponse(httpGETFORM.getResponseBodyAsStream());
            System.out.println("OT Billing response: " + resp);
            if (resp == null) {
                throw new Exception("11000");
            }
            httpGETFORM.releaseConnection();

            String code = resp.split(";")[4];
            if (code.equals("0")) {
                String opTransactionId = resp.split(";")[3];
                action = "&action=4";
                formattedurl = rawurl + action + "&op_transaction_id=" + opTransactionId;

                httpGETFORM = new GetMethod(formattedurl);
                resp = "";
                client.executeMethod(httpGETFORM);
                resp = getResponse(httpGETFORM.getResponseBodyAsStream());
                System.out.println("OT Billing response: " + resp);
                if (resp == null) {
                    throw new Exception("11000");
                }
                code = resp.split(";")[4];
                if (!code.equals("0")) {
                }
            }
            return true;
        } catch (HttpException e) {
            resp = "5001: " + e.getMessage();

            System.out.println("error response: " + resp);

            return false;
        } catch (IOException e) {
            resp = "5002: " + e.getMessage();

            System.out.println("error response: " + resp);

            return false;
        } catch (Exception e) {
            System.out.println("error response: " + e.getMessage());

            return false;
        } finally {
            httpGETFORM.releaseConnection();
            client = null;
            httpGETFORM = null;
        }
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

    public static boolean processResponse(String reply)
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
}
