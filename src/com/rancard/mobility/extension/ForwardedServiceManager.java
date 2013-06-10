/*
 * ForwardedServiceManager.java
 *
 * Created on January 5, 2007, 1:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.extension;

import java.util.ArrayList;

/**
 *
 * @author Messenger
 */
public abstract class ForwardedServiceManager {
    
    public static void createForwardedService (ForwardedService service) throws Exception {
        ForwardedServiceDB.createForwardedService (service);
    }
    
    public static void updateForwardedServiceName (String keyword, String account_id, String serviceName) throws Exception {
        ForwardedServiceDB.updateForwardedServiceName (keyword, account_id, serviceName);
    }
    
    public static void updateForwardedServiceParameters (String keyword, String account_id, String url, int timeout, int listenStatus) throws Exception {
        ForwardedServiceDB.updateForwardedServiceParameters (keyword, account_id, url, timeout, listenStatus);
    }
    
    public static void updateForwardedService (String keyword, String account_id, String name, String url, int timeout, int listenStatus) throws Exception {
        ForwardedServiceDB.updateForwardedService (keyword, account_id, name, url, timeout, listenStatus);
    }
    
    public static ForwardedService viewForwardedService (String keyword, String accountId) throws Exception {
        return ForwardedServiceDB.viewForwardedService (keyword, accountId);
    }
    
    public static ArrayList viewAllForwardedServices (String accountId) throws Exception {
        return ForwardedServiceDB.viewAllForwardedServices (accountId);
    }
    
    public static void deleteForwardedService (String keyword, String account_id) throws Exception {
        ForwardedServiceDB.deleteForwardedService (keyword, account_id);
    }
    
}
