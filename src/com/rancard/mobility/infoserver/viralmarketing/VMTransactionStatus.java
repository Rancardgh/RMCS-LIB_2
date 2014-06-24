/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.infoserver.viralmarketing;

/**
 *
 * @author Mustee
 */
public enum VMTransactionStatus {
    INV_SENT("inv_sent"),
    INV_ACCEPTED("inv_accepted");
    
    private String value;
    
    private VMTransactionStatus(String value){
        this.value = value;
    }
    
    public String getValue(){
        return value;
    }
}
