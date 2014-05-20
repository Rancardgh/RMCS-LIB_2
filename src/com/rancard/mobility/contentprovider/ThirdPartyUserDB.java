 package com.rancard.mobility.contentprovider;
 
 import com.rancard.common.DConnect;
 import com.rancard.mobility.infoserver.common.services.UserService;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.Statement;
 import java.sql.Timestamp;
 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Date;
 import java.util.Iterator;
 
 public abstract class ThirdPartyUserDB
 {
   public static void create3rdPartyUser(String accountId, ThirdPartyUser user)
     throws Exception
   {
     ResultSet rs = null;
     Connection con = null;
     PreparedStatement prepstat = null;
     try
     {
       con = DConnect.getConnection();
       String SQL = "Select * from 3rd_party_info where 3rd_party_id='" + user.getUserId() + "' and 3rd_party_key='" + user.getUserKey() + "'";
       prepstat = con.prepareStatement(SQL);
       rs = prepstat.executeQuery();
       if (!rs.next())
       {
         String services = "";
         
 
 
 
 
 
 
 
         SQL = "insert into 3rd_party_info (3rd_party_id,3rd_party_name,3rd_party_key,account_id,allowed_services,time_created) values (?,?,?,?,?,?)";
         prepstat = con.prepareStatement(SQL);
         prepstat.setString(1, user.getUserId());
         prepstat.setString(2, user.getUserName());
         prepstat.setString(3, user.getUserKey());
         prepstat.setString(4, accountId);
         prepstat.setString(5, services);
         prepstat.setTimestamp(6, new Timestamp(new Date().getTime()));
         prepstat.execute();
         
 
         SQL = "select ";
       }
     }
     catch (Exception ex)
     {
       if (con != null) {
         con.close();
       }
     }
     finally
     {
       if (con != null) {
         con.close();
       }
     }
   }
   
   public static int[] set3rdPartyUserPermissions(ArrayList<ThirdPartyUserPermission> userPermissionList, String accountId, String thirdPartyId)
     throws Exception
   {
     ResultSet rs = null;
     Connection con = null;
     Statement stmt = null;
     int[] upCounts = null;
     Iterator<ThirdPartyUserPermission> it = userPermissionList.iterator();
     String keywordlist = "('";
     while (it.hasNext())
     {
       keywordlist = keywordlist + ((ThirdPartyUserPermission)it.next()).getKeyword();
       if (it.hasNext()) {
         keywordlist = keywordlist + "','";
       }
     }
     keywordlist = keywordlist + "')";
     try
     {
       con = DConnect.getConnection();
       con.setAutoCommit(false);
       stmt = con.createStatement();
       String SQL = "delete from 3rd_party_permissions where account_id ='" + accountId + "' and 3rd_party_id ='" + thirdPartyId + "'";
       stmt.addBatch(SQL);
       for (int i = 0; i < userPermissionList.size(); i++)
       {
         ThirdPartyUserPermission thrdPartyPem = (ThirdPartyUserPermission)userPermissionList.get(i);
         SQL = "insert into 3rd_party_permissions (keyword, account_id, can_view, can_add, can_view_reports,3rd_party_id) values ('" + thrdPartyPem.getKeyword() + "','" + accountId + "'," + (thrdPartyPem.getCanView().booleanValue() ? 1 : 0) + "," + (thrdPartyPem.getCanSubmit().booleanValue() ? 1 : 0) + "," + (thrdPartyPem.getCanEdit().booleanValue() ? 1 : 0) + ",'" + thirdPartyId + "')";
         
         stmt.addBatch(SQL);
       }
       upCounts = stmt.executeBatch();
       con.commit();
     }
     catch (Exception ex)
     {
       if (con != null) {
         con.close();
       }
     }
     finally
     {
       if (con != null) {
         con.close();
       }
     }
     return upCounts;
   }
   
   public static ThirdPartyUser viewThirdPartyUser(String userId)
     throws Exception
   {
     ThirdPartyUser user = new ThirdPartyUser();
     ArrayList myServices = new ArrayList();
     ArrayList<ThirdPartyUserPermission> permissions = new ArrayList();
     
     ResultSet rs = null;
     Connection con = null;
     PreparedStatement prepstat = null;
     String accountId = "";
     String services = "";
     try
     {
       con = DConnect.getConnection();
       
       String SQL = "SELECT * FROM 3rd_party_info where 3rd_party_id='" + userId + "'";
       prepstat = con.prepareStatement(SQL);
       rs = prepstat.executeQuery();
       if (rs.next())
       {
         user.setUserId(rs.getString("3rd_party_id"));
         user.setUserName(rs.getString("3rd_party_name"));
         user.setUserKey(rs.getString("3rd_party_key"));
         accountId = rs.getString("account_id");
         services = rs.getString("allowed_services");
         user.setAccountId(accountId);
         
 
 
         SQL = "SELECT * FROM service_definition sd INNER JOIN  3rd_party_permissions   tp on sd.account_id =tp.account_id and sd.keyword = tp.keyword  where sd.account_id='" + accountId + "' and sd.keyword in (select tp.keyword from 3rd_party_permissions   tp  where tp.3rd_party_id  ='" + userId + "') and tp.3rd_party_id  ='" + userId + "'";
         prepstat = con.prepareStatement(SQL);
         rs = prepstat.executeQuery();
         while (rs.next())
         {
           ThirdPartyUserPermission userPermission = new ThirdPartyUserPermission();
           
           userPermission.setAccountId(rs.getString("account_id"));
           userPermission.setKeyword(rs.getString("keyword"));
           userPermission.setThirdPartyId(userId);
           userPermission.setCanEdit(Boolean.valueOf(1 == rs.getInt("can_view_reports")));
           userPermission.setCanView(Boolean.valueOf(1 == rs.getInt("can_view")));
           userPermission.setCanSubmit(Boolean.valueOf(1 == rs.getInt("can_add")));
           
 
           UserService service = new UserService();
           
           service.setKeyword(rs.getString("keyword"));
           service.setServiceType(rs.getString("service_type"));
           service.setAccountId(rs.getString("account_id"));
           service.setServiceName(rs.getString("service_name"));
           service.setDefaultMessage(rs.getString("default_message"));
           
 
           SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
           String publishTime = df.format(new Date(rs.getTimestamp("last_updated").getTime()));
           service.setLastUpdated(publishTime);
           service.setCommand(rs.getString("command"));
           service.setAllowedShortcodes(rs.getString("allowed_shortcodes"));
           service.setAllowedSiteTypes(rs.getString("allowed_site_types"));
           
 
           userPermission.setService(service);
           permissions.add(userPermission);
           
           myServices.add(service);
         }
         user.setPermissions(permissions);
         user.setServices(myServices);
       }
       return user;
     }
     catch (Exception ex)
     {
       if (con != null) {
         con.close();
       }
       throw new Exception(ex.getMessage());
     }
     finally
     {
       if (con != null) {
         con.close();
       }
       if (rs != null) {
         rs.close();
       }
     }
   }
   
   public static ThirdPartyUser viewThirdPartyUsersPermissions(String userId)
     throws Exception
   {
     ThirdPartyUser user = new ThirdPartyUser();
     ArrayList myServices = new ArrayList();
     ArrayList<ThirdPartyUserPermission> permissions = new ArrayList();
     
     ResultSet rs = null;
     Connection con = null;
     PreparedStatement prepstat = null;
     String accountId = "";
     String services = "";
     try
     {
       con = DConnect.getConnection();
       
       String SQL = "select * from 3rd_party_info ti inner join 3rd_party_permissions  tp on ti.3rd_party_id = tp.3rd_party_id where ti.3rd_party_id  = ?";
       prepstat = con.prepareStatement(SQL);
       prepstat.setString(1, userId);
       rs = prepstat.executeQuery();
       while (rs.next())
       {
         if ((user.getUserId() == null) || ("".equals(user.getUserId())))
         {
           user.setUserId(rs.getString("3rd_party_id"));
           user.setUserName(rs.getString("3rd_party_name"));
           user.setUserKey(rs.getString("3rd_party_key"));
           
           accountId = rs.getString("account_id");
           services = rs.getString("allowed_services");
           user.setAccountId(accountId);
         }
         ThirdPartyUserPermission userPermission = new ThirdPartyUserPermission();
         userPermission.setAccountId(rs.getString("account_id"));
         userPermission.setKeyword(rs.getString("keyword"));
         userPermission.setThirdPartyId(rs.getString("3rd_party_id"));
         userPermission.setCanEdit(Boolean.valueOf(1 == rs.getInt("can_view_reports")));
         userPermission.setCanView(Boolean.valueOf(1 == rs.getInt("can_view")));
         userPermission.setCanSubmit(Boolean.valueOf(1 == rs.getInt("can_add")));
         permissions.add(userPermission);
       }
       user.setPermissions(permissions);
       
 
 
 
 
 
 
 
 
 
 
 
 
 
       return user;
     }
     catch (Exception ex)
     {
       if (con != null)
       {
         con.close();
         con = null;
       }
       if (rs != null)
       {
         rs.close();
         rs = null;
       }
       throw new Exception(ex.getMessage());
     }
     finally
     {
       if (con != null) {
         con.close();
       }
     }
   }
   
   public static boolean deleteThirdPartyUsers(String accountId, ArrayList<String> userIds)
     throws Exception
   {
     ThirdPartyUser user = new ThirdPartyUser();
     ArrayList myServices = new ArrayList();
     boolean deleteOK = false;
     String ids = "('";
     
     Iterator<String> IdIter = userIds.iterator();
     while (IdIter.hasNext())
     {
       String userId = (String)IdIter.next();
       ids = ids + userId;
       if (IdIter.hasNext()) {
         ids = ids + "','";
       }
     }
     ids = ids + "')";
     
 
     ResultSet rs = null;
     Connection con = null;
     PreparedStatement prepstat = null;
     accountId = accountId == null ? "" : accountId;
     String services = "";
     try
     {
       con = DConnect.getConnection();
       
       String SQL = "delete  FROM 3rd_party_info where account_id ='" + accountId + "' and 3rd_party_id in " + ids;
       prepstat = con.prepareStatement(SQL);
       return prepstat.execute();
     }
     catch (Exception ex)
     {
       if (con != null)
       {
         con.close();
         con = null;
       }
       throw new Exception(ex.getMessage());
     }
     finally
     {
       if (con != null) {
         con.close();
       }
     }
   }
   
   public static ArrayList viewAllThirdPartyUsers(String accountId)
     throws Exception
   {
     ArrayList myThirdPartyUsers = new ArrayList();
     
 
     ResultSet rs = null;
     ResultSet rs2 = null;
     Connection con = null;
     PreparedStatement prepstat = null;
     String services = "";
     try
     {
       con = DConnect.getConnection();
       
       String SQL = "SELECT * FROM 3rd_party_info where account_id='" + accountId + "' order by time_created desc";
       prepstat = con.prepareStatement(SQL);
       rs = prepstat.executeQuery();
       while (rs.next())
       {
         ThirdPartyUser user = new ThirdPartyUser();
         ArrayList myServices = new ArrayList();
         
         user.setUserId(rs.getString("3rd_party_id"));
         user.setUserName(rs.getString("3rd_party_name"));
         user.setUserKey(rs.getString("3rd_party_key"));
         services = rs.getString("allowed_services");
         
         String[] services_arr = null;
         if ((services != null) && (!services.equals("")))
         {
           services_arr = services.split(",");
           
           services = "";
           for (int i = 0; i < services_arr.length; i++) {
             services = services + "'" + services_arr[i] + "',";
           }
           services = services.substring(0, services.lastIndexOf(","));
           
           SQL = "SELECT * FROM service_definition where account_id='" + accountId + "' and keyword in (" + services + ")";
           prepstat = con.prepareStatement(SQL);
           rs2 = prepstat.executeQuery();
           while (rs2.next())
           {
             UserService service = new UserService();
             
             service.setKeyword(rs2.getString("keyword"));
             service.setServiceType(rs2.getString("service_type"));
             service.setAccountId(rs2.getString("account_id"));
             service.setServiceName(rs2.getString("service_name"));
             service.setDefaultMessage(rs2.getString("default_message"));
             SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH.mm.ss");
             String publishTime = df.format(new Date(rs2.getTimestamp("last_updated").getTime()));
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
       return myThirdPartyUsers;
     }
     catch (Exception ex)
     {
       throw new Exception(ex.getMessage());
     }
     finally
     {
       if (con != null) {
         con.close();
       }
     }
   }
 }



/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar

 * Qualified Name:     com.rancard.mobility.contentprovider.ThirdPartyUserDB

 * JD-Core Version:    0.7.0.1

 */