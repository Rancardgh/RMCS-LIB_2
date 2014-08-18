/*
 * PaymentManager.java
 *
 * Created on May 24, 2007, 11:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rancard.mobility.contentserver.payment;


import com.rancard.common.CPConnection;
import com.rancard.common.PricePoint;
import org.apache.commons.lang3.StringUtils;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Messenger
 */
public class PaymentManager {
    public static final int SMSC_BILL = 1;
    public static final int PIN_REDEMPTION = 2;
    public static final int OT_BILL = 3;
    public static final int AIRTEL_BILL = 4;
    public static final int GLO_BILL = 5;
    public static final int UNIFIED_BILL = 6;

    public static boolean doPayment(PricePoint pricePoint, String username, String password, String msisdn, String transactionId,
                                    String keyword, String accountId, String serviceCode) throws Exception{
        if(!StringUtils.isNumeric(pricePoint.getBillingMech())){
            return false;
        }

        Payment payment;
        if(Integer.parseInt(pricePoint.getBillingMech()) == OT_BILL){
            payment = new VodafonePayment();
        }else if(Integer.parseInt(pricePoint.getBillingMech()) == AIRTEL_BILL){
            payment = new AirtelPayment();
        } else{
            return false;
        }

        return payment.initiatePayment(pricePoint, username, password, msisdn, transactionId, keyword, accountId, serviceCode);
    }

    public static void logBillTrans(String trans_id, String cp_user_id, String msisdn, String amount, String status,
                                     String trans_guid, String serviceCode, String keyword, String accountId, String loggingFolder) {

        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
        String today = df.format(new Date());
        java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df2.format(new Date());
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

    private static String generateUID() {
        java.security.SecureRandom wheel = new java.security.SecureRandom();
        int myInt = wheel.nextInt(10000000) * 10000000;
        String str = Integer.toString(myInt > 0 ? myInt : myInt * -1);
        while (str.length() < 4) {
            myInt = wheel.nextInt(10000000) * 10000000;
            str = Integer.toString(myInt > 0 ? myInt : myInt * -1);
        }

        Calendar cal = new GregorianCalendar();
        Date currentDate = cal.getTime();
        java.text.DateFormat df = new java.text.SimpleDateFormat("yyMMddHHmmss");
        return df.format(currentDate) + "" + str.substring(0, 4);
    }

}
