/*
 * UserServiceTransaction.java
 *
 * Created on February 13, 2007, 3:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver.common.services;

/**
 *
 * @author Messenger
 */
public class UserServiceTransaction extends com.rancard.mobility.common.ServiceTransaction {
    
    /** Creates a new instance of UserServiceTransaction */
    public UserServiceTransaction () {
        super ();
    }
    
    public UserServiceTransaction (String transactionId, String keyword, String accountId, String msg, String msisdn, String callBackUrl, java.sql.Timestamp date, String pricePoint,
            int isBilled, int isCompleted) {
        super (transactionId, keyword, accountId, msg, msisdn, callBackUrl, date, pricePoint, isBilled, isCompleted);
    }    
}
