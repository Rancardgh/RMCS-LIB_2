/*   1:    */ package com.rancard.mobility.contentprovider;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ 
/*   5:    */ public class ThirdPartyUser
/*   6:    */ {
/*   7:    */   private String userId;
/*   8:    */   private String userName;
/*   9:    */   private String userKey;
/*  10:    */   private String accountId;
/*  11:    */   private ArrayList services;
/*  12:    */   private ArrayList<ThirdPartyUserPermission> permissions;
/*  13:    */   
/*  14:    */   public ThirdPartyUser()
/*  15:    */   {
/*  16: 31 */     this.userId = null;
/*  17: 32 */     this.userName = null;
/*  18: 33 */     this.userKey = null;
/*  19: 34 */     this.permissions = new ArrayList();
/*  20: 35 */     this.services = new ArrayList();
/*  21:    */   }
/*  22:    */   
/*  23:    */   public String getUserId()
/*  24:    */   {
/*  25: 40 */     return this.userId;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setUserId(String userId)
/*  29:    */   {
/*  30: 44 */     this.userId = userId;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public String getUserName()
/*  34:    */   {
/*  35: 48 */     return this.userName;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setUserName(String userName)
/*  39:    */   {
/*  40: 52 */     this.userName = userName;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public String getUserKey()
/*  44:    */   {
/*  45: 56 */     return this.userKey;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setUserKey(String userKey)
/*  49:    */   {
/*  50: 60 */     this.userKey = userKey;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public ArrayList getServices()
/*  54:    */   {
/*  55: 64 */     return this.services;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setServices(ArrayList services)
/*  59:    */   {
/*  60: 68 */     this.services = services;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public ArrayList getPermissions()
/*  64:    */   {
/*  65: 72 */     return this.permissions;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public ArrayList getPermissions(String serviceType)
/*  69:    */   {
/*  70: 79 */     ArrayList<ThirdPartyUserPermission> serviceTypepermissions = new ArrayList();
/*  71: 81 */     for (int i = 0; i < this.permissions.size(); i++)
/*  72:    */     {
/*  73: 82 */       ThirdPartyUserPermission userPermission = (ThirdPartyUserPermission)this.permissions.get(i);
/*  74: 83 */       if (serviceType.equals(userPermission.getServiceType())) {
/*  75: 84 */         serviceTypepermissions.add(userPermission);
/*  76:    */       }
/*  77:    */     }
/*  78: 88 */     return serviceTypepermissions;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public ThirdPartyUserPermission getPermission(String keyword)
/*  82:    */   {
/*  83: 92 */     ThirdPartyUserPermission servicePermissions = new ThirdPartyUserPermission();
/*  84: 94 */     for (int i = 0; i < this.permissions.size(); i++)
/*  85:    */     {
/*  86: 97 */       ThirdPartyUserPermission userPermission = (ThirdPartyUserPermission)this.permissions.get(i);
/*  87: 98 */       if (keyword.equals(userPermission.getKeyword()))
/*  88:    */       {
/*  89: 99 */         servicePermissions = userPermission;
/*  90:100 */         break;
/*  91:    */       }
/*  92:    */     }
/*  93:104 */     return servicePermissions;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void setPermissions(ArrayList permissions)
/*  97:    */   {
/*  98:107 */     this.permissions = permissions;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public String getAccountId()
/* 102:    */   {
/* 103:111 */     return this.accountId;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void setAccountId(String accountId)
/* 107:    */   {
/* 108:115 */     this.accountId = accountId;
/* 109:    */   }
/* 110:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentprovider.ThirdPartyUser
 * JD-Core Version:    0.7.0.1
 */