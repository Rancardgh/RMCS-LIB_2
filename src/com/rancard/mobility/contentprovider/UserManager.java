/*
 * UserManager.java
 *
 * Created on May 11, 2007, 5:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.contentprovider;

import java.util.ArrayList;

/**
 *
 * @author Messenger
 */
public abstract class UserManager {
    
    public static void create3rdPartyUser(String accountId, ThirdPartyUser user) throws Exception {
        ThirdPartyUserDB.create3rdPartyUser(accountId, user);
    }
    
    public static ThirdPartyUser viewThirdPartyUser(String userId) throws Exception {
        return ThirdPartyUserDB.viewThirdPartyUser(userId);
    }
    
    public static ArrayList viewAllThirdPartyUsers(String accountId) throws Exception {
        return ThirdPartyUserDB.viewAllThirdPartyUsers(accountId);
    }
    public static boolean deleteThirdPartyUsers(String accountId,ArrayList userIds) throws Exception {
        return ThirdPartyUserDB.deleteThirdPartyUsers(accountId,userIds);
    }
    public static boolean setThirdPartyUserPermissions(String accountId,String userId, java.util.ArrayList<ThirdPartyUserPermission> userPermissionsList) throws Exception {
        
        if(ThirdPartyUserDB.set3rdPartyUserPermissions(userPermissionsList,accountId,userId)!=null){
            return true;
        }
//return ThirdPartyUserDB.deleteThirdPartyUsers(accountId,userIds);
        return false;
    }
    
    public static java.util.HashMap viewThirdPartyUserPermissionsMap(String accountId,String userId) throws Exception {
        
        java.util.HashMap permissions = new java.util.HashMap();
        
        
        ThirdPartyUser user = ThirdPartyUserDB.viewThirdPartyUsersPermissions(userId);
        
        java.util.ArrayList permissionsList =  user.getPermissions();
        
        for(int i=0;i< permissionsList.size();i++){
            com.rancard.mobility.contentprovider.ThirdPartyUserPermission thirdPartyPerm = new com.rancard.mobility.contentprovider.ThirdPartyUserPermission();
            thirdPartyPerm =  (com.rancard.mobility.contentprovider.ThirdPartyUserPermission)permissionsList.get(i);
            
// creaye permissions prodile
            if(thirdPartyPerm.getCanSubmit().booleanValue()){
                String permissionCS = accountId+"_"+userId+"_"+thirdPartyPerm.getKeyword()+"_cs";
            permissions.put(permissionCS,"1");
            }
            if(thirdPartyPerm.getCanView().booleanValue()){
                String permissionCV = accountId+"_"+userId+"_"+thirdPartyPerm.getKeyword()+"_cv";
            permissions.put(permissionCV,"1");
            }
             if(thirdPartyPerm.getCanEdit().booleanValue()){
                String permissionCE = accountId+"_"+userId+"_"+thirdPartyPerm.getKeyword()+"_ce";
            permissions.put(permissionCE,"1");
            }
        }
  
        return permissions;
    }
    

    public static java.util.HashMap viewThirdPartyUserPermissions(String userId) throws Exception {
        
        java.util.HashMap permissions = new java.util.HashMap();
        
        
        ThirdPartyUser user = ThirdPartyUserDB.viewThirdPartyUsersPermissions(userId);
        
        java.util.ArrayList permissionsList =  user.getPermissions();
        
        for(int i=0;i< permissionsList.size();i++){
            com.rancard.mobility.contentprovider.ThirdPartyUserPermission thirdPartyPerm = new com.rancard.mobility.contentprovider.ThirdPartyUserPermission();
            thirdPartyPerm =  (com.rancard.mobility.contentprovider.ThirdPartyUserPermission)permissionsList.get(i);
            permissions.put(thirdPartyPerm.getKeyword(),thirdPartyPerm );
// creaye permissions prodile
         
        }
  
        return permissions;
    }
 

}
