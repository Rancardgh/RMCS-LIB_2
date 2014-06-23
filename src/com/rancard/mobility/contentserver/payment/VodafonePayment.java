package com.rancard.mobility.contentserver.payment;


import com.rancard.common.CPConnection;
import com.rancard.common.PricePoint;

/**
 * Created by Mustee on 2/17/14.
 */
public class VodafonePayment implements Payment {

    @Override
    public boolean initiatePayment(PricePoint pricePoint, CPConnection cnxn, String msisdn, String messageString, String pin, String callBackUrl, String serviceCode, String keyword) throws Exception {
        return false;
    }

}
