package com.rancard.mobility.contentserver.payment;


import com.rancard.common.CPConnection;
import com.rancard.common.PricePoint;

/**
 * Created by Mustee on 2/17/14.
 */
public interface Payment {
    public final int URL_CALL_TIMEOUT = 8000;

    boolean initiatePayment(PricePoint pricePoint, String username, String password, String msisdn, String transactionId,
                            String keyword, String accountId, String serviceCode) throws Exception;
}
