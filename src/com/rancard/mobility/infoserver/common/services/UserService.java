package com.rancard.mobility.infoserver.common.services;

import java.util.Date;
import java.util.List;

public class UserService implements java.io.Serializable{
    
    private boolean isBasic;    
    private boolean isSubscription;
    private String serviceType;
    private String keyword;
    private String accountId;
    private String serviceName;
    private String defaultMessage;
    private Date lastUpdated;
    private String command;
    private List<String> allowedShortcodes;
    private List<String> allowedSiteTypes;
    private String pricing;
    private String serviceResponseSender;
    
    public UserService(){        
    }
    
    public UserService(String serviceType, String keyword, String accountId, String serviceid, String defaultMessage) {
        this.serviceType = serviceType;
        this.keyword = keyword;
        this.accountId = accountId;
        this.serviceName = serviceid;
        this.defaultMessage = defaultMessage;
    }
    
    public UserService(String serviceType, String keyword, String accountId, String serviceid, String defaultMessage, String command, List<String> allowedShortcodes, 
            List<String> allowedSiteTypes, String pricing, boolean isBasic) {
        this.isBasic = isBasic;
        
        this.serviceType = serviceType;
        this.keyword = keyword;
        this.accountId = accountId;
        this.serviceName = serviceid;
        this.defaultMessage = defaultMessage;
        this.command = command;
        this.allowedShortcodes = allowedShortcodes;
        this.allowedSiteTypes = allowedSiteTypes;        
        this.pricing = pricing;
    }
    
    public UserService(String serviceType, String keyword, String accountId, String serviceid, String defaultMessage, String command, List<String> allowedShortcodes, 
            List<String> allowedSiteTypes, String pricing, boolean isBasic, boolean isSubscription, String serviceReponseSender, Date lastUpdated) {
        this.isBasic = isBasic;
        this.isSubscription = isSubscription;
        this.serviceType = serviceType;
        this.keyword = keyword;
        this.accountId = accountId;
        this.serviceName = serviceid;
        this.defaultMessage = defaultMessage;
        this.command = command;
        this.allowedShortcodes = allowedShortcodes;
        this.allowedSiteTypes = allowedSiteTypes;        
        this.pricing = pricing;
        this.serviceResponseSender = serviceReponseSender;
        this.lastUpdated = lastUpdated;
    }
    
    public String getAccountId() {
        return accountId;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setServiceType(String serviceName) {
        this.serviceType = serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public boolean equals(Object elem) {
        if(elem instanceof UserService){
            return false;
        }
        
        UserService e = (UserService) elem;
        if (this.accountId.equals(e.getAccountId()) && this.keyword.equals(e.getKeyword())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.keyword != null ? this.keyword.hashCode() : 0);
        hash = 41 * hash + (this.accountId != null ? this.accountId.hashCode() : 0);
        return hash;
    }

    /**
     * doesUserExist
     */
    public boolean exists() {
        boolean userExists = false;
        if (this.getKeyword() != null && this.getAccountId() != null) {

            try {
                UserService serv = UserServiceDB.viewService(this.getKeyword(),
                        this.getAccountId());
                if (serv != null && serv.getAccountId() != null &&
                    serv.getAccountId().equals(this.accountId)) {
                    this.setAccountId(serv.getAccountId());
                    this.serviceName = serv.getServiceName();
                    this.serviceType = serv.getServiceType();
                    this.keyword =serv.getKeyword();
                     userExists = true;
                }

            } catch (Exception ex) {

            }

        }
        return userExists;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCommand () {
        return command;
    }

    public void setCommand (String command) {
        this.command = command;
    }

    public List<String> getAllowedShortcodes () {
        return allowedShortcodes;
    }

    public void setAllowedShortcodes (List<String> allowedShortcodes) {
        this.allowedShortcodes = allowedShortcodes;
    }
    
    public List<String> getAllowedSiteTypes () {
        return allowedSiteTypes;
    }

    public void setAllowedSiteTypes (List<String> allowedSiteTypes) {
        this.allowedSiteTypes = allowedSiteTypes;
    }

    public boolean isBasic () {
        return isBasic;
    }

    public void setIsBasic (boolean isBasic) {
        this.isBasic = isBasic;
    }
    
    public boolean isSubscription () {
        return isSubscription;
    }

    public void setIsSubscription (boolean isSubscription) {
        this.isSubscription = isSubscription;
    }
    
    public String getServiceResponseSender() {
        return serviceResponseSender;
    }
    
    public void setServiceResponseSender (String serviceResponseSender) {
        this.serviceResponseSender = serviceResponseSender;
    } 

    public String getPricing () {
        return pricing;
    }

    public void setPricing (String pricing) {
        this.pricing = pricing;
    }
}
