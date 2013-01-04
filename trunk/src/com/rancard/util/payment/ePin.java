/*
 * ePin.java
 *
 * Created on April 24, 2006, 7:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.util.payment;

/**
 *
 * @author USER
 */
public class ePin {

    /** Creates a new instance of ePin */
    public ePin() {
    }

    private String provider;
    private String pin;
    private double eValue;
    private double currentBalance;

    public boolean isValid() {
        boolean valid = false;
        ePinDB pin = new ePinDB();
        ePin voucher = new ePin();
        try {
            voucher = pin.viewVoucher(this.pin);
            if (voucher.getPin() != null && voucher.getEValue() != 0.00 &&
                voucher.getCurrentBalance() != 0.00) {
                this.setEValue(voucher.getEValue());
                this.setPin(voucher.getPin());
                this.setCurrentBalance(voucher.getCurrentBalance());
                this.setProvider(voucher.getProvider());
                valid = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return valid;
    }

    /**
     * Getter for property pin.
     * @return Value of property pin.
     */
    public String getPin() {
        return this.pin;
    }

    /**
     * Setter for property pin.
     * @param pin New value of property pin.
     */
    public void setPin(String pin) {
        this.pin = pin;
    }

    /**
     * Setter for property pin.
     * @param listId New value of property listId.
     */
    public void setProvider(String listId) {
        this.provider = listId;
    }

    /**
     * Getter for property provider.
     * @return Value of property provider.
     */
    public String getProvider() {
        return this.provider;
    }

    /**
     * Getter for property currentBalance.
     * @return Value of property currentBalance.
     */
    public double getCurrentBalance() {
        return this.currentBalance;
    }

    /**
     * Setter for property currentBalance.
     * @param currentBalance New value of property currentBalance.
     */
    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    /**
     * Getter for property eValue.
     * @return Value of property eValue.
     */
    public double getEValue() {
        return this.eValue;
    }

    /**
     * Setter for property eValue.
     * @param eValue New value of property eValue.
     */
    public void setEValue(double eValue) {
        this.eValue = eValue;
    }

    public void updateMyLog() throws Exception {
        new ePinDB().updateVoucher(this.getPin(), this.currentBalance);
    }

    public static void insertVoucher(String listId, String pin, double limit, double currentBalance) throws Exception {
        new ePinDB().insertVoucher(listId, pin, limit, currentBalance);
    }

    public static void deleteVoucher(String pin) throws Exception {
        new ePinDB().deleteVoucher(pin);
    }

    public static ePin viewVoucher(String pin) throws Exception {
        return new ePinDB().viewVoucher(pin);
    }

    public static java.util.ArrayList viewVouchers(String listId) throws Exception {
        return new ePinDB().viewVouchers(listId);
    }

}

