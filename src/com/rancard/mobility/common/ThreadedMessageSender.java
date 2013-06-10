/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.common;

import com.rancard.mobility.contentserver.CPConnections;

/**
 *
 * @author nii
 */
public class ThreadedMessageSender implements Runnable{

    CPConnections cnxn;
    String msisdn;
    String short_code;
    String message;
    String meta_data;
    int delay;

    public ThreadedMessageSender(CPConnections cnxn, String msisdn, String short_code, String message, int delay) {
        this.cnxn = cnxn;
        this.msisdn = msisdn;
        this.short_code = short_code;
        this.message = message;
        this.delay = delay;
        this.meta_data = "";
    }

    public ThreadedMessageSender(CPConnections cnxn, String msisdn, String short_code, String message, String meta_data, int delay) {
        this.cnxn = cnxn;
        this.msisdn = msisdn;
        this.short_code = short_code;
        this.message = message;
        this.delay = delay;
        this.meta_data = meta_data;
    }

    public void run() {
        try {
            Thread.sleep(delay);
                Driver.getDriver(cnxn.getDriverType(), cnxn.getGatewayURL()).sendSMSTextMessage(msisdn, short_code, message, cnxn.getUsername(),
                        cnxn.getPassword(), cnxn.getConnection(), meta_data);
        } catch (InterruptedException e) {
            System.out.println(new java.util.Date() + ": " + short_code + ": " + msisdn + ": thread error sending notification @ ThreadedMessageSender: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(new java.util.Date() + ": " + short_code + ": " + msisdn + ": error sending notification @ ThreadedMessageSender: " + e.getMessage());
        } finally {
            cnxn = null;
        }
    }
}
