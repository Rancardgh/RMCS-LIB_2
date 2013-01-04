/*
 * TestMain.java
 *
 * Created on December 26, 2006, 9:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.infoserver;

import com.rancard.mobility.infoserver.*;

/**
 *
 * @author USER
 */
public class TestMain {
    
    /** Creates a new instance of TestMain */
    public TestMain() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
 
    com.rancard.mobility.infoserver.InfoService is = new com.rancard.mobility.infoserver.InfoService();
    is.setPublishDate("12/12/2006");
    is.setMessage("Hello world");
    is.setAccountId("test");
    is.setKeyword("In");
    is.setServiceName("International news");
    is.setServiceType("11");
        try {
            
            com.rancard.mobility.infoserver.common.services.ServiceManager.createService(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            
            is.createInfoServiceEntry();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
