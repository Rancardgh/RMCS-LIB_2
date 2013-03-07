/*
 * Probably useless now. But just to make things easy and keep supporting old stuff
 */
package com.rancard.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Ahmed Mustapha
 *
 */
public class AddressBook {

    private String accountID;
    private String msisdn;
    private String registrationID;

    public AddressBook(String accountID, String msisdn, String registrationID) {
        this.accountID = accountID;
        this.msisdn = msisdn;
        this.registrationID = registrationID;
    }

    public String getAccountID() {
        return accountID;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public String getRegistrationID() {
        return registrationID;
    }

  
}
