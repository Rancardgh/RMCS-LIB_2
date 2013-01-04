/*
 * ThirdPartyUser.java
 *
 * Created on May 11, 2007, 4:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.contentprovider;

import java.util.ArrayList;
import com.rancard.mobility.contentprovider.ThirdPartyUserPermission ;
/**
 *
 * @author Messenger
 */
public class ThirdPartyUser {
    
    private String userId;
    private String userName;
    private String userKey;
    private String accountId;
    
    
    private ArrayList services;
    private ArrayList<com.rancard.mobility.contentprovider.ThirdPartyUserPermission> permissions;
    
    /** Creates a new instance of ThirdPartyUser */
    public ThirdPartyUser() {
       userId = null;
        userName = null;
        userKey = null;
        permissions = new ArrayList();
        services = new ArrayList();
   
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserKey() {
        return userKey;
    }
    
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
    
    public ArrayList getServices() {
        return services;
    }
    
    public void setServices(ArrayList services) {
        this.services = services;
    }
    
    public ArrayList getPermissions() {
        return permissions;
    }
    /**
      this method sorts the permissions based on type
     
     **/
    public ArrayList getPermissions(String serviceType) {
        ArrayList<com.rancard.mobility.contentprovider.ThirdPartyUserPermission> serviceTypepermissions = new java.util.ArrayList();
        
        for (int i=0 ;i< permissions.size();i++){
            com.rancard.mobility.contentprovider.ThirdPartyUserPermission userPermission = (ThirdPartyUserPermission)permissions.get(i);
            if(serviceType.equals(userPermission.getServiceType())){
               serviceTypepermissions.add(userPermission); 
            }
        }
        
        return serviceTypepermissions;
    }

        public com.rancard.mobility.contentprovider.ThirdPartyUserPermission getPermission(String keyword) {
        com.rancard.mobility.contentprovider.ThirdPartyUserPermission  servicePermissions = new com.rancard.mobility.contentprovider.ThirdPartyUserPermission();
        
        for (int i=0 ;i< permissions.size();i++){
            
            
            com.rancard.mobility.contentprovider.ThirdPartyUserPermission userPermission = (ThirdPartyUserPermission)permissions.get(i);
            if(keyword.equals(userPermission.getKeyword())){
               servicePermissions =userPermission;
               break;
            }
        }
        
        return servicePermissions;
    }
    public void setPermissions(ArrayList permissions) {
        this.permissions = permissions;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
