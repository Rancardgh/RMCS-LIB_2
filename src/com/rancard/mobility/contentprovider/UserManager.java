/*  1:   */ package com.rancard.mobility.contentprovider;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.HashMap;
/*  5:   */ 
/*  6:   */ public abstract class UserManager
/*  7:   */ {
/*  8:   */   public static void create3rdPartyUser(String accountId, ThirdPartyUser user)
/*  9:   */     throws Exception
/* 10:   */   {
/* 11:21 */     ThirdPartyUserDB.create3rdPartyUser(accountId, user);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public static ThirdPartyUser viewThirdPartyUser(String userId)
/* 15:   */     throws Exception
/* 16:   */   {
/* 17:25 */     return ThirdPartyUserDB.viewThirdPartyUser(userId);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public static ArrayList viewAllThirdPartyUsers(String accountId)
/* 21:   */     throws Exception
/* 22:   */   {
/* 23:29 */     return ThirdPartyUserDB.viewAllThirdPartyUsers(accountId);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public static boolean deleteThirdPartyUsers(String accountId, ArrayList userIds)
/* 27:   */     throws Exception
/* 28:   */   {
/* 29:32 */     return ThirdPartyUserDB.deleteThirdPartyUsers(accountId, userIds);
/* 30:   */   }
/* 31:   */   
/* 32:   */   public static boolean setThirdPartyUserPermissions(String accountId, String userId, ArrayList<ThirdPartyUserPermission> userPermissionsList)
/* 33:   */     throws Exception
/* 34:   */   {
/* 35:36 */     if (ThirdPartyUserDB.set3rdPartyUserPermissions(userPermissionsList, accountId, userId) != null) {
/* 36:37 */       return true;
/* 37:   */     }
/* 38:40 */     return false;
/* 39:   */   }
/* 40:   */   
/* 41:   */   public static HashMap viewThirdPartyUserPermissionsMap(String accountId, String userId)
/* 42:   */     throws Exception
/* 43:   */   {
/* 44:45 */     HashMap permissions = new HashMap();
/* 45:   */     
/* 46:   */ 
/* 47:48 */     ThirdPartyUser user = ThirdPartyUserDB.viewThirdPartyUsersPermissions(userId);
/* 48:   */     
/* 49:50 */     ArrayList permissionsList = user.getPermissions();
/* 50:52 */     for (int i = 0; i < permissionsList.size(); i++)
/* 51:   */     {
/* 52:53 */       ThirdPartyUserPermission thirdPartyPerm = new ThirdPartyUserPermission();
/* 53:54 */       thirdPartyPerm = (ThirdPartyUserPermission)permissionsList.get(i);
/* 54:57 */       if (thirdPartyPerm.getCanSubmit().booleanValue())
/* 55:   */       {
/* 56:58 */         String permissionCS = accountId + "_" + userId + "_" + thirdPartyPerm.getKeyword() + "_cs";
/* 57:59 */         permissions.put(permissionCS, "1");
/* 58:   */       }
/* 59:61 */       if (thirdPartyPerm.getCanView().booleanValue())
/* 60:   */       {
/* 61:62 */         String permissionCV = accountId + "_" + userId + "_" + thirdPartyPerm.getKeyword() + "_cv";
/* 62:63 */         permissions.put(permissionCV, "1");
/* 63:   */       }
/* 64:65 */       if (thirdPartyPerm.getCanEdit().booleanValue())
/* 65:   */       {
/* 66:66 */         String permissionCE = accountId + "_" + userId + "_" + thirdPartyPerm.getKeyword() + "_ce";
/* 67:67 */         permissions.put(permissionCE, "1");
/* 68:   */       }
/* 69:   */     }
/* 70:71 */     return permissions;
/* 71:   */   }
/* 72:   */   
/* 73:   */   public static HashMap viewThirdPartyUserPermissions(String userId)
/* 74:   */     throws Exception
/* 75:   */   {
/* 76:77 */     HashMap permissions = new HashMap();
/* 77:   */     
/* 78:   */ 
/* 79:80 */     ThirdPartyUser user = ThirdPartyUserDB.viewThirdPartyUsersPermissions(userId);
/* 80:   */     
/* 81:82 */     ArrayList permissionsList = user.getPermissions();
/* 82:84 */     for (int i = 0; i < permissionsList.size(); i++)
/* 83:   */     {
/* 84:85 */       ThirdPartyUserPermission thirdPartyPerm = new ThirdPartyUserPermission();
/* 85:86 */       thirdPartyPerm = (ThirdPartyUserPermission)permissionsList.get(i);
/* 86:87 */       permissions.put(thirdPartyPerm.getKeyword(), thirdPartyPerm);
/* 87:   */     }
/* 88:92 */     return permissions;
/* 89:   */   }
/* 90:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentprovider.UserManager
 * JD-Core Version:    0.7.0.1
 */