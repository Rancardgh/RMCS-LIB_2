/*
 * ServicePricingBean.java
 *
 * Created on August 30, 2006, 4:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.contentserver;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Kweku Tandoh
 */
public class ServicePricingBean {
    
    private String accountId;
    private String keyword;
    private String networkPrefix;
    private String price;
    private String serviceType;
    private String shortCode;
    
    //billing url
    private String url;
    
    /** Creates a new instance of ServicePricingBean */
    public ServicePricingBean () {
    }

    public String getAccountId () {
        return accountId;
    }

    public void setAccountId (String accountId) {
        this.accountId = accountId;
    }

    public String getKeyword () {
        return keyword;
    }

    public void setKeyword (String keyword) {
        this.keyword = keyword;
    }

    public String getNetworkPrefix () {
        return networkPrefix;
    }

    public void setNetworkPrefix (String networkPrefix) {
        this.networkPrefix = networkPrefix;
    }

    public String getPrice () {
        return price;
    }

    public void setPrice (String price) {
        this.price = price;
    }
    
    public String getServiceType () {
        return serviceType;
    }

    public void setServiceType (String serviceType) {
        this.serviceType = serviceType;
    }

    public String getShortCode () {
        return shortCode;
    }

    public void setShortCode (String shortCode) {
        this.shortCode = shortCode;
    }
    
     //management functions
    public static void create(String cpId, String keyword, String networkPrefix, String price, String serviceType, String shortCode) throws Exception {
        ServicePricingDB.create (cpId, keyword, networkPrefix, price, serviceType, shortCode);
    }
    
    public static void updatePrice(String price, String cpId, String serviceType, String networkCode) throws Exception {
        ServicePricingDB.updatePrice(price, cpId, serviceType, networkCode);
    }
        
    /*public static void updateNetworkPrefix(String network, String cpId, String keyword) throws Exception {
        ServicePricingDB.updateNetworkPrefix(network, cpId, keyword);
    }

    public static void update(String price, String network, String cpId, String keyword) throws Exception {
        ServicePricingDB.update(price, network, cpId, keyword);
    }*/
    
    public static void delete(String cpId, String serviceType, String networkCode) throws Exception {
        ServicePricingDB.delete(cpId, serviceType, networkCode);
    }
    
    public static void deleteAllForCP(String cpId) throws Exception {
        ServicePricingDB.deleteAllForCP(cpId);
    }
    
    /*public static ServicePricingBean view(String accountId, String keyword, String networkPrefix) throws Exception {
        return ServicePricingDB.view(accountId, keyword, networkPrefix);
    }*/
    
    public static ServicePricingBean viewPrice(String accountId, String serviceType, String networkPrefix) throws Exception {
        return ServicePricingDB.viewPrice (accountId, serviceType, networkPrefix);
    }
    
    public static ServicePricingBean viewPriceWithUrl(String accountId, String serviceType, String networkPrefix) throws Exception {
        return ServicePricingDB.viewPriceWithUrl (accountId, serviceType, networkPrefix);
    }
    
    public static ArrayList viewServicePricesForNetwork(String accountId, String serviceType) throws Exception {
        return ServicePricingDB.viewServicePricesForNetwork (accountId, serviceType);
    }
    
    public static ArrayList viewAllForCP(String accountId) throws Exception {
        return ServicePricingDB.viewAllForCP(accountId);
    }
    
    public static HashMap viewPricesForCP(String accountId) throws Exception {
        return ServicePricingDB.viewPricesForCP(accountId);
    }
    
    public static HashMap viewAllPrices() throws Exception {
        return ServicePricingDB.viewAllPrices();
    }

    public String getUrl () {
        return url;
    }

    public void setUrl (String url) {
        this.url = url;
    }

    
}
