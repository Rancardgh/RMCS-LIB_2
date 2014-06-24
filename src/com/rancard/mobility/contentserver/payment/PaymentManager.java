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

/**
 *
 * @author Messenger
 */
public class PaymentManager {
    public static final String SMSC_BILL = "1";
    public static final String PIN_REDEMPTION = "2";
    public static final String OT_BILL = "3";
    public static final String AIRTEL_BILL = "4";
    public static final String GLO_BILL = "5";
    public static final String UNIFIED_BILL = "6";

    public static boolean doPayment(PricePoint pricePoint, CPConnection cnxn, String msisdn, String messageString,
                                    String pin, String callBackUrl, String serviceCode, String keyword) throws Exception{
        return false;
    }

}
