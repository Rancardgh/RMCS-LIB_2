/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.common;

import com.rancard.common.DConnect;
import com.rancard.common.uidGen;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.infoserver.InfoService;
import com.rancard.mobility.infoserver.common.services.UserService;
import com.rancard.mobility.infoserver.common.services.UserServiceTransaction;
import com.rancard.util.URLUTF8Encoder;
import com.rancard.util.payment.PaymentManager;
import com.rancard.util.payment.PricePoint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 *
 * @author ktandoh
 */
public class TempBatchTransmission implements Runnable {

    final long MILLISECONDS_BETWEEN_RETRY = 0;
    final long MILLISECONDS_BETWEEN_TRANSMIT = 0;
    final long MILLISECONDS_BETWEEN_BATCHES = 10000;
    final int BATCH_SIZE = 100;
    final int URL_CALL_TIMEOUT = 9000;
    final int NUM_OF_RETRIES = 3;
    private List subscribers;
    private InfoService service;

    public TempBatchTransmission () {
        subscribers = new ArrayList<String> ();
        service = new InfoService ();
    }

    public TempBatchTransmission (List subscribers, InfoService service) {
        this.subscribers = subscribers;
        this.service = service;
    }

    public void run () {

        try {
            ArrayList<UserServiceTransaction> transactionList = new ArrayList<UserServiceTransaction> ();

            long startTime = 0;
            long stopTime = 0;
            long totalTime = 0;
            int delivery_count = 0;

            CPConnections cnxn = null;
            Connection con = null;
            Statement stat = null;
            con = DConnect.getConnection ();
            con.setAutoCommit (false);
            con.createStatement ();
            stat = con.createStatement ();
            stat.clearBatch ();

            for (int i = 0; i < subscribers.size (); i++) {
                if (cnxn == null) {
                    cnxn = CPConnections.getConnection (service.getAccountId (), subscribers.get (0).toString (), "rms");
                }

                UserServiceTransaction tranxn = new UserServiceTransaction ();

                String msisdn = subscribers.get (i).toString ();
                String transactionId = uidGen.generateSecureUID ();

                String smsUrl = "";

                if (msisdn.matches ("(233|\\+233|00233|0)20\\d{7}")) {
                    smsUrl = cnxn.getGatewayURL () + "/sendsms?to=" + URLUTF8Encoder.encode (msisdn) + "&account="
                            + URLUTF8Encoder.encode (cnxn.getUsername ()) + "&smsc=OTBulk&username=OTBulk&password="
                            + URLUTF8Encoder.encode ("03F7*NV!") + "&from=" + URLUTF8Encoder.encode (service.getServiceName ())
                            + "&text=" + URLUTF8Encoder.encode (service.getMessage ())+"&binfo="+transactionId;
                } else {
                    smsUrl = cnxn.getGatewayURL () + "/sendsms?to=" + URLUTF8Encoder.encode (msisdn) + "&text="
                            + URLUTF8Encoder.encode (service.getMessage ()) + "&conn=" + URLUTF8Encoder.encode (cnxn.getConnection ())
                            + "&username=" + URLUTF8Encoder.encode (cnxn.getUsername ()) + "&password=" + URLUTF8Encoder.encode (cnxn.getPassword ())
                            + "&from=" + URLUTF8Encoder.encode (service.getServiceName ());
                }
                smsUrl = smsUrl.replaceAll ("'", "\\\\'");

                tranxn.setAccountId (service.getAccountId ());
                tranxn.setKeyword (service.getKeyword ());
                tranxn.setMsg ("");
                tranxn.setMsisdn (msisdn);
                tranxn.setTransactionId (transactionId);
                tranxn.setCallBackUrl (smsUrl);
                tranxn.setPricePoint (service.getPricing ());
                tranxn.setIsBilled (0);
                tranxn.setIsCompleted (0);

                //compose query
                String query = "INSERT into transactions (trans_id,keyword,account_id,msisdn,callback_url,date,msg,is_billed,"
                        + "is_completed,price_point_id) values('" + tranxn.getTransactionId () + "','" + tranxn.getKeyword ()
                        + "','" + tranxn.getAccountId () + "','" + tranxn.getMsisdn () + "','" + tranxn.getCallBackUrl () + "',now(),'"
                        + tranxn.getMsg () + "','" + tranxn.getIsBilled () + "','" + tranxn.getIsCompleted () + "','" + tranxn.getPricePoint () + "')";

                System.out.println (query);

                transactionList.add (tranxn);
                stat.addBatch (query);
            }

            if (transactionList != null && transactionList.size () > 0) {
                /*------------Execute process: bill & push alert------------------*/
                //--open transactions
                boolean transOpened = false;

                transOpened = openTransactions (con, stat);

                //--execute transactions
                if (transOpened) {
                    int totalBilled = 0;
                    if (subscribers.get (0).toString ().matches ("(233|\\+233|00233|0)20\\d{7}")) {
                        //condition to check for Vodafone GH subscriptions
                        totalBilled = executeTransactionOnVodafone (transactionList);
                    } else {
                        totalBilled = executeTransaction (transactionList);
                    }
                } else {
                    System.out.println (new java.util.Date () + "could not open transactions with size:" + transactionList.size ());
                }
            } else {//no subscribers found
            }
        } catch (Exception e) {
            System.out.println ("@ TempBatchTransmission: Error: " + e.getMessage ());
        }
    }

    private boolean openTransactions (Connection con, Statement stat) throws SQLException {

        String queryString = "";
        PreparedStatement prepstat = null;
        ResultSet rs = null;

        int[] aiupdateCounts = null;
        boolean bError = false;
        boolean status = false;

        try {
            // execute the statements
            aiupdateCounts = stat.executeBatch ();
        } catch (BatchUpdateException bue) {
            bError = true;
            aiupdateCounts = bue.getUpdateCounts ();

            SQLException SQLe = bue;
            while (SQLe != null) {
                // do exception stuff
                SQLe = SQLe.getNextException ();
            }
            System.out.println (new java.util.Date () + ":BATCH SQLerror:" + bue.getMessage ());
        } catch (SQLException SQLe) {
            System.out.println (new java.util.Date () + ":there was an error during the update:");
            System.out.println (new java.util.Date () + ":BATCH SQLerror:" + SQLe.getMessage ());
        } finally {
            int iProcessed;
            // determine operation result
            for (int i = 0; i < aiupdateCounts.length; i++) {
                iProcessed = aiupdateCounts[i];
                if (iProcessed > 0 || iProcessed == -2) {
                    // statement was successful
                    System.out.println (new java.util.Date () + ":" + i + ":BATCH SQL Update sucessful");
                } else {
                    // error on statement
                    bError = true;
                    break;
                }
            } // end for

            if (bError) {
                System.out.println (new java.util.Date () + ":CODE_GEN: BATCH SQL Update fail. RollBack...");
                con.rollback ();
                status = false;
            } else {
                con.commit ();
                status = true;
            }
            if (con != null) {
                con.close ();
            }

        }
        return status;
    }

    private int executeTransactionOnVodafone (ArrayList<UserServiceTransaction> transList) {

        boolean billed = false;
        String msisdn = "";
        String kw = "";
        UserServiceTransaction trans = null;

        int billCount = 0;

        int IN_period = 10;
        //--bill and push
        int IN_check_counter = 0;
        Connection conn = null;
        PreparedStatement prepstat = null;
        ResultSet rs = null;

        String in_directive = "", in_wait_period = "", in_check_period = "", in_status = "", transmission_halt_time = "";
        for (int i = 0; i < transList.size (); i++) {
            in_directive = "";
            in_wait_period = "";
            in_check_period = "";
            in_status = "";
            transmission_halt_time = "";

            Calendar cal = Calendar.getInstance ();
            int minute = cal.get (Calendar.MINUTE);

            if (minute % IN_period != 0) {
                IN_check_counter = 0;
            }

            if (i == 0 || minute % IN_period == 0) {
                IN_check_counter++;
                if (IN_check_counter == 1) {
                    try {
                        System.out.println ("=======================checking directive from IN=====================");
                        conn = DConnect.getConnection ();
                        String query = "select * from ot_transmission_settings where settings_key='Status'";
                        prepstat = conn.prepareStatement (query);
                        rs = prepstat.executeQuery ();
                        while (rs.next ()) {
                            in_directive = rs.getString ("settings_value");
                        }
                        query = "select * from ot_transmission_settings where settings_key='wait_period'";
                        prepstat = conn.prepareStatement (query);
                        rs = prepstat.executeQuery ();
                        while (rs.next ()) {
                            in_wait_period = rs.getString ("settings_value");
                        }
                        query = "select * from ot_transmission_settings where settings_key='trans_halt_time'";
                        prepstat = conn.prepareStatement (query);
                        rs = prepstat.executeQuery ();
                        while (rs.next ()) {
                            transmission_halt_time = rs.getString ("settings_value");
                        }
                        query = "select * from ot_transmission_settings where settings_key='in_check_period'";
                        prepstat = conn.prepareStatement (query);
                        rs = prepstat.executeQuery ();
                        while (rs.next ()) {
                            in_check_period = rs.getString ("settings_value");
                        }
                        conn.close ();
                    } catch (Exception ex1) {
                        ex1.printStackTrace ();
                        //     conn.close();
                    }

                    if (in_check_period != null && !in_check_period.equals ("")) {
                        try {
                            IN_period = new Integer (in_check_period);
                        } catch (Exception ex) {
                            IN_period = 10;
                        }
                    } else {
                        IN_period = 10;
                    }

                    if (in_directive != null && in_directive.equals ("pause")) {
                        int in_wait = 0;

                        if (in_wait_period == null || in_wait_period.equals ("")) {
                            in_wait_period = "5";
                        } else {
                            try {
                                in_wait = new Integer (in_wait_period).intValue ();
                            } catch (Exception ndf) {
                                in_wait_period = "5";
                            }
                        }
                        in_wait = in_wait * 60000;
                        try {
                            System.out.println ("waiting....");
                            Thread.sleep (in_wait);
                            System.out.println ("resuming....");
                            if (in_directive == null) {
                                in_directive = "";
                            }

                            while (in_directive.equals ("pause")) {
                                conn = DConnect.getConnection ();
                                String query = "select * from ot_transmission_settings where settings_key='Status'";
                                prepstat = conn.prepareStatement (query);
                                rs = prepstat.executeQuery ();
                                while (rs.next ()) {
                                    in_directive = rs.getString ("settings_value");
                                }
                                if (in_directive != null && in_directive.equals ("pause")) {
                                    Thread.sleep (in_wait);
                                }
                            }
                        } catch (Exception ex1) {
                            ex1.printStackTrace ();
                        }
                    }
                }
            }
            trans = transList.get (i);
            kw = trans.getKeyword ();
            msisdn = trans.getMsisdn ();
            System.out.println (new Date () + ":" + (i + 1) + ":next_transaction:" + trans.getTransactionId () + ":" + kw + ": " + msisdn + "...");
            try {
                billed = bill (trans);

                if (billed) {
                    pushAlert (trans);
                    billCount++;
                }
            } catch (Exception e) {
                // throw new Exception(Feedback.UNSUPPORTED_NETWORK);
                System.out.println (new java.util.Date () + ":Info alert: error executing transaction for: " + trans.getTransactionId () + ":" + kw + ":" + msisdn + ": " + e.getMessage ());
            }
        }
        trans = null;
        return billCount;
    }

    private int executeTransaction (ArrayList<UserServiceTransaction> transList) {

        boolean billed = false;
        String msisdn = "";
        String kw = "";
        UserServiceTransaction trans = null;

        int billCount = 0;

        for (int i = 0; i < transList.size (); i++) {
            
            trans = transList.get (i);
            kw = trans.getKeyword ();
            msisdn = trans.getMsisdn ();
            System.out.println (new Date () + ":" + (i + 1) + ":next_transaction:" + trans.getTransactionId () + ":" + kw + ": " + msisdn + "...");
            try {
                billed = bill (trans);

                if (billed) {
                    pushAlert (trans);
                    billCount++;
                }
            } catch (Exception e) {
                // throw new Exception(Feedback.UNSUPPORTED_NETWORK);
                System.out.println (new java.util.Date () + ":Info alert: error executing transaction for: " + trans.getTransactionId () + ":" + kw + ":" + msisdn + ": " + e.getMessage ());
            }
        }
        trans = null;
        return billCount;
    }

    private void pushAlert (UserServiceTransaction trans) {

        String resp = "";
        int count = 1;
        String kw = trans.getKeyword ();
        String msisdn = trans.getMsisdn ();
        String callbackUrl = trans.getCallBackUrl ();

        //call sendsms
        HttpClient client = new HttpClient ();
        GetMethod httpGETFORM = new GetMethod (callbackUrl);

        System.out.println (new Date () + ": " + kw + ": " + msisdn + ": HttpClient created. Now calling sendsmsURL...");
        System.out.println (new Date () + ":AlertURL:" + callbackUrl);
        try {
            while (!resp.contains ("Accepted") && !resp.contains ("Queued") && count <= NUM_OF_RETRIES) {
                //System.out.println (new Date () + ": " + kw + ": " + msisdn + ": push attempt:" + count);
                client.executeMethod (httpGETFORM);
                resp = getResponse (httpGETFORM.getResponseBodyAsStream ());
                System.out.println (new Date () + " :: " + kw + " :: " + msisdn + " :: Messaging Gateway response=" + resp + " :: tranxn ID=" + trans.getTransactionId ()
                        + " :: Attempt=" + count + " :: callback URL=" + trans.getCallBackUrl ());
                count++;
            }
            System.out.println (new Date () + ":===============================================================");
        } catch (Exception e) {
            System.out.println (new java.util.Date () + ":" + kw + ": " + msisdn + ":exception forwarding alert: " + e.getMessage ());
        } finally {
            httpGETFORM.releaseConnection ();
            client = null;
            httpGETFORM = null;
        }

    }

    private String getResponse (java.io.InputStream in) throws Exception {
        String status = "error";
        String reply = "";
        String error = "";
        String responseString = "";
        BufferedReader br = null;

        try {
            InputStream responseBody = in;
            br = new BufferedReader (new InputStreamReader (
                    responseBody));
            String line = br.readLine ();
            while (line != null) {
                responseString = responseString + line;
                line = br.readLine ();
            }
        } catch (Exception e) {
        } finally {
            br.close ();
            in.close ();

            br = null;
            in = null;
        }
        return responseString;
    }

    private boolean bill (UserServiceTransaction trans) throws Exception {
        boolean isBilled = false;
        final String ACCOUNT_NOT_FOUND = "201";
        final String ACCOUNT_NOT_IN_DB = "5";
        final String STATUS_BILLED = "0";
        String resp = "";
        String queryString = "";

        PricePoint pp = PaymentManager.viewPricePoint (service.getPricing ());
        String billingURL = pp.getBillingUrl ();
        String kw = trans.getKeyword ();
        String msisdn = trans.getMsisdn ();

        if (billingURL == null || billingURL.equals ("")) {
            System.out.println (new Date () + kw + ": " + msisdn + ":billingURL:" + billingURL);
            return true;
        } else {

            if (msisdn.matches ("(233|\\+233|00233|0)20\\d{7}")) {
                queryString = "msisdn=" + msisdn.substring (4);
            } else {
                queryString = "msisdn=" + msisdn;
            }
            queryString = queryString + "&transactionId=" + trans.getTransactionId ()
                    + "&keyword=" + trans.getKeyword ();

            billingURL = URLUTF8Encoder.doURLEscaping (queryString, billingURL);

            HttpClient client = new HttpClient ();
            GetMethod httpGETFORM = new GetMethod (billingURL);
            httpGETFORM.getParams ().setParameter ("http.socket.timeout", new Integer (URL_CALL_TIMEOUT));//timeout update

            //System.out.println (new Date () + " " + kw + ": " + msisdn + ": billingURL:" + billingURL);
            try {

                client.executeMethod (httpGETFORM);
                resp = getResponse (httpGETFORM.getResponseBodyAsStream ());
                System.out.println (new Date () + " :: " + kw + " :: " + msisdn + " :: Response from billing gateway=" + resp
                        + " :: tranxn ID=" + trans.getTransactionId () + " :: billingURL=" + billingURL + " :: callback URL=" + trans.getCallBackUrl ());

            } catch (Exception e) {
                System.out.println (new java.util.Date () + ":" + kw + ": " + msisdn + ":exception billing subscriber: " + e.getMessage ());
                if (e.getMessage ().equals ("Read timed out")) {
                }
            } finally {
                httpGETFORM.releaseConnection ();
                client = null;
                httpGETFORM = null;
            }
        }

        //check billing status
        if (resp != null && resp.equals (STATUS_BILLED)) {
            isBilled = true;
        } else {
            isBilled = false;
        }
        return isBilled;
    }
}
