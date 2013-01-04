/*
 * ThirdPartyUserDB.java
 *
 * Created on May 11, 2007, 4:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.mobility.contentprovider;

import com.rancard.common.DConnect;
import com.rancard.mobility.infoserver.common.services.UserService;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Messenger
 */
public abstract class ThirdPartyUserDB {
    public static void create3rdPartyUser(String accountId, ThirdPartyUser user) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        
        try {
            con = DConnect.getConnection();
            SQL =   "Select * from 3rd_party_info where 3rd_party_id='" + user.getUserId() + "' and 3rd_party_key='" + user.getUserKey() + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            
            if(!rs.next()){
                String services = "";
             /*  if(user.getServices().size() > 0){
                    services = user.getServices().get(0).toString();
                    for(int i = 1; i < user.getServices().size(); i++) {
                        services = services + ",";
                    }
                    services = services.substring(0, services.lastIndexOf(","));
                }
              */  
                SQL =   "insert into 3rd_party_info (3rd_party_id,3rd_party_name,3rd_party_key,account_id,allowed_services,time_created) values (?,?,?,?,?,?)";
                prepstat = con.prepareStatement(SQL);
                prepstat.setString(1, user.getUserId());
                prepstat.setString(2, user.getUserName());
                prepstat.setString(3, user.getUserKey());
                prepstat.setString(4, accountId);
                prepstat.setString(5, services);
                prepstat.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
                prepstat.execute();
                
                //create alias
                SQL =   "select ";
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }
    
    public static int[] set3rdPartyUserPermissions(java.util.ArrayList<ThirdPartyUserPermission> userPermissionList,String accountId,String thirdPartyId) throws Exception {
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        Statement stmt = null;
        int[] upCounts = null;
        java.util.Iterator<ThirdPartyUserPermission> it = userPermissionList.iterator();
        String keywordlist = "('";
        
        while(it.hasNext()){
            keywordlist = keywordlist + it.next().getKeyword();
            if(it.hasNext()){
                keywordlist =  keywordlist +"','";
            }
        }
        
        keywordlist =  keywordlist +"')";
        
        try {
            con = DConnect.getConnection();
            con.setAutoCommit(false);
            stmt = con.createStatement();
            SQL =   "delete from 3rd_party_permissions where account_id ='"+accountId+"' and 3rd_party_id ='"+thirdPartyId+"'" ;
            stmt.addBatch(SQL);
            
            for(int i=0;i<userPermissionList.size();i++){
                ThirdPartyUserPermission thrdPartyPem = userPermissionList.get(i);
                SQL = "insert into 3rd_party_permissions (keyword, account_id, can_view, can_add, can_view_reports,3rd_party_id) values ('"+thrdPartyPem.getKeyword()+"','"+accountId+"',"+(thrdPartyPem.getCanView().booleanValue()?1:0)+","+ (thrdPartyPem.getCanSubmit().booleanValue()?1:0)+","+(thrdPartyPem.getCanEdit().booleanValue()?1:0)+",'"+thirdPartyId+"')";
                
                stmt.addBatch(SQL);
            }
            
            
            upCounts = stmt.executeBatch();
            con.commit();
            
            
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return upCounts;
    }
    
    
    public static ThirdPartyUser viewThirdPartyUser(String userId) throws Exception {
        ThirdPartyUser user = new ThirdPartyUser();
        ArrayList myServices = new ArrayList();
        ArrayList<com.rancard.mobility.contentprovider.ThirdPartyUserPermission> permissions = new ArrayList();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        String accountId = "";
        String services = "";
        
        try {
            con = DConnect.getConnection();
            
            SQL = "SELECT * FROM 3rd_party_info where 3rd_party_id='" + userId + "'";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            
            if (rs.next()) {
                user.setUserId(rs.getString("3rd_party_id"));
                user.setUserName(rs.getString("3rd_party_name"));
                user.setUserKey(rs.getString("3rd_party_key"));
                accountId = rs.getString("account_id");
                services = rs.getString("allowed_services");
                user.setAccountId(accountId);
                
                
                
                SQL = "SELECT * FROM service_definition sd INNER JOIN  3rd_party_permissions   tp on sd.account_id =tp.account_id and sd.keyword = tp.keyword  where sd.account_id='"+accountId+"' and sd.keyword in (select tp.keyword from 3rd_party_permissions   tp  where tp.3rd_party_id  ='"+userId+"') and tp.3rd_party_id  ='"+userId+"'";
                prepstat = con.prepareStatement(SQL);
                rs = prepstat.executeQuery();
                
                while (rs.next()) {
                    com.rancard.mobility.contentprovider.ThirdPartyUserPermission userPermission = new ThirdPartyUserPermission();
                   // set user permission for this service
                    userPermission.setAccountId(rs.getString("account_id"));
                    userPermission.setKeyword(rs.getString("keyword"));
                    userPermission.setThirdPartyId(userId);
                    userPermission.setCanEdit(1==rs.getInt("can_view_reports"));
                    userPermission.setCanView(1==rs.getInt("can_view"));
                    userPermission.setCanSubmit(1==rs.getInt("can_add"));
                    
                    // create service associated with these permission
                    UserService service = new UserService();
                    
                    service.setKeyword(rs.getString("keyword"));
                    service.setServiceType(rs.getString("service_type"));
                    service.setAccountId(rs.getString("account_id"));
                    service.setServiceName(rs.getString("service_name"));
                    service.setDefaultMessage(rs.getString("default_message"));
                    
                   
                    java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
                    String publishTime = df.format(new java.util.Date(rs.getTimestamp("last_updated").getTime()));
                    service.setLastUpdated(publishTime);
                    service.setCommand(rs.getString("command"));
                    service.setAllowedShortcodes(rs.getString("allowed_shortcodes"));
                    service.setAllowedSiteTypes(rs.getString("allowed_site_types"));
                   
                    // set the service associated with this permission
                    userPermission.setService(service);
                    permissions.add(userPermission);
                    // backward compatibility method  to ensure code does not break
                    myServices.add(service);
                }
                user.setPermissions(permissions);
                user.setServices(myServices);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
            
            if(rs!=null){
                rs.close();
                
            }
            return user;
        }
    }
    
    
    
    public static ThirdPartyUser viewThirdPartyUsersPermissions(String userId) throws Exception {
        ThirdPartyUser user = new ThirdPartyUser();
        ArrayList myServices = new ArrayList();
        ArrayList<com.rancard.mobility.contentprovider.ThirdPartyUserPermission> permissions = new ArrayList();
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        String accountId = "";
        String services = "";
        
        try {
            con = DConnect.getConnection();
            
            SQL = "select * from 3rd_party_info ti inner join 3rd_party_permissions  tp on ti.3rd_party_id = tp.3rd_party_id where ti.3rd_party_id  = ?";
            prepstat = con.prepareStatement(SQL);
            prepstat.setString(1,userId);
            rs = prepstat.executeQuery();
            
            // get 3rd party info from the first row. add permissions into an array List
            
            while (rs.next()) {
                if(user.getUserId()==null || "".equals(user.getUserId())){
                    user.setUserId(rs.getString("3rd_party_id"));
                    user.setUserName(rs.getString("3rd_party_name"));
                    user.setUserKey(rs.getString("3rd_party_key"));
                    
                    accountId = rs.getString("account_id");
                    services = rs.getString("allowed_services");
                user.setAccountId(accountId);
                }
                
                com.rancard.mobility.contentprovider.ThirdPartyUserPermission userPermission = new ThirdPartyUserPermission();
                userPermission.setAccountId(rs.getString("account_id"));
                userPermission.setKeyword(rs.getString("keyword"));
                userPermission.setThirdPartyId(rs.getString("3rd_party_id"));
                userPermission.setCanEdit(1==rs.getInt("can_view_reports"));
                userPermission.setCanView(1==rs.getInt("can_view"));
                userPermission.setCanSubmit(1==rs.getInt("can_add"));
                permissions.add(userPermission);
            }
            user.setPermissions(permissions);
        } catch (Exception ex) {
            if (con != null) {
                con.close();
                con = null;
            }
            if (rs != null) {
                rs.close();
                rs= null;
            }
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
            return user;
        }
    }
    
    public static boolean deleteThirdPartyUsers(String accountId,java.util.ArrayList<String>  userIds) throws Exception {
        ThirdPartyUser user = new ThirdPartyUser();
        ArrayList myServices = new ArrayList();
        boolean deleteOK = false;
        String ids = "('";
        
        java.util.Iterator<String> IdIter = userIds.iterator();
        while(IdIter.hasNext()){
            String userId =IdIter.next();
            ids = ids + userId;
            if(IdIter.hasNext()){
                ids= ids+"','";
            }
            
        }
        ids =ids +"')";
        
        String SQL;
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        accountId = (accountId==null)?"":accountId;
        String services = "";
        
        try {
            con = DConnect.getConnection();
            
            SQL = "delete  FROM 3rd_party_info where account_id ='"+accountId+"' and 3rd_party_id in " + ids ;
            prepstat = con.prepareStatement(SQL);
            deleteOK = prepstat.execute();
            
            
        } catch (Exception ex) {
            if (con != null) {
                con.close();
                con = null;
            }
            
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
            return deleteOK;
        }
    }
    
    
    public static ArrayList viewAllThirdPartyUsers(String accountId) throws Exception {
        ArrayList myThirdPartyUsers = new ArrayList();
        
        String SQL;
        ResultSet rs = null;
        ResultSet rs2 = null;
        Connection con = null;
        PreparedStatement prepstat = null;
        String services = "";
        
        try {
            con = DConnect.getConnection();
            
            SQL = "SELECT * FROM 3rd_party_info where account_id='" + accountId + "' order by time_created desc";
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            
            while (rs.next()) {
                ThirdPartyUser user = new ThirdPartyUser();
                ArrayList myServices = new ArrayList();
                
                user.setUserId(rs.getString("3rd_party_id"));
                user.setUserName(rs.getString("3rd_party_name"));
                user.setUserKey(rs.getString("3rd_party_key"));
                services = rs.getString("allowed_services");
                
                String[] services_arr = null;
                if(services != null && !services.equals("")){
                    services_arr = services.split(",");
                    
                    services = "";
                    for (int i = 0; i < services_arr.length; i++) {
                        services = services + "'" + services_arr[i] + "',";
                    }
                    services = services.substring(0, services.lastIndexOf(","));
                    
                    SQL = "SELECT * FROM service_definition where account_id='" + accountId + "' and keyword in (" + services + ")";
                    prepstat = con.prepareStatement(SQL);
                    rs2 = prepstat.executeQuery();
                    
                    
                    while (rs2.next()) {
                        UserService service = new UserService();
                        
                        service.setKeyword(rs2.getString("keyword"));
                        service.setServiceType(rs2.getString("service_type"));
                        service.setAccountId(rs2.getString("account_id"));
                        service.setServiceName(rs2.getString("service_name"));
                        service.setDefaultMessage(rs2.getString("default_message"));
                        java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
                        String publishTime = df.format(new java.util.Date(rs2.getTimestamp("last_updated").getTime()));
                        service.setLastUpdated(publishTime);
                        service.setCommand(rs2.getString("command"));
                        service.setAllowedShortcodes(rs2.getString("allowed_shortcodes"));
                        service.setAllowedSiteTypes(rs2.getString("allowed_site_types"));
                        
                        myServices.add(service);
                    }
                }
                user.setServices(myServices);
                myThirdPartyUsers.add(user);
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            throw new Exception(ex.getMessage());
        } finally {
            if (con != null) {
                con.close();
            }
            return myThirdPartyUsers;
        }
    }
}
