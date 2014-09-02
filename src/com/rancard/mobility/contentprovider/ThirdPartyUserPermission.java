/*   1:    */ package com.rancard.mobility.contentprovider;
/*   2:    */ 
/*   3:    */ import com.rancard.mobility.infoserver.common.services.UserService;
/*   4:    */ 
/*   5:    */ public class ThirdPartyUserPermission
/*   6:    */ {
/*   7: 17 */   private UserService service = new UserService();
/*   8:    */   private String accountId;
/*   9:    */   private String keyword;
/*  10:    */   private String ThirdPartyId;
/*  11:    */   private Boolean canView;
/*  12:    */   private Boolean canSubmit;
/*  13:    */   private Boolean canEdit;
/*  14:    */   
/*  15:    */   public String getAccountId()
/*  16:    */   {
/*  17: 33 */     return getService().getAccountId();
/*  18:    */   }
/*  19:    */   
/*  20:    */   public void setAccountId(String accountId)
/*  21:    */   {
/*  22: 38 */     getService().setAccountId(accountId);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public String getKeyword()
/*  26:    */   {
/*  27: 43 */     return getService().getKeyword();
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setKeyword(String keyword)
/*  31:    */   {
/*  32: 47 */     getService().setKeyword(keyword);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public String getServiceType()
/*  36:    */   {
/*  37: 52 */     return this.service != null ? this.service.getServiceType() : "";
/*  38:    */   }
/*  39:    */   
/*  40:    */   public String getThirdPartyId()
/*  41:    */   {
/*  42: 57 */     return this.ThirdPartyId;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setThirdPartyId(String ThirdPartyId)
/*  46:    */   {
/*  47: 61 */     this.ThirdPartyId = ThirdPartyId;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public Boolean getCanView()
/*  51:    */   {
/*  52: 65 */     return this.canView;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setCanView(Boolean canView)
/*  56:    */   {
/*  57: 69 */     this.canView = canView;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public int canView()
/*  61:    */   {
/*  62: 73 */     return this.canView.booleanValue() ? 1 : 0;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public Boolean getCanSubmit()
/*  66:    */   {
/*  67: 77 */     return this.canSubmit;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public int canSubmit()
/*  71:    */   {
/*  72: 81 */     return this.canSubmit.booleanValue() ? 1 : 0;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void setCanSubmit(Boolean canSubmit)
/*  76:    */   {
/*  77: 85 */     this.canSubmit = canSubmit;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public Boolean getCanEdit()
/*  81:    */   {
/*  82: 89 */     return this.canEdit;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public int canEdit()
/*  86:    */   {
/*  87: 93 */     return this.canEdit.booleanValue() ? 1 : 0;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setCanEdit(Boolean canEdit)
/*  91:    */   {
/*  92: 96 */     this.canEdit = canEdit;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public UserService getService()
/*  96:    */   {
/*  97:100 */     return this.service;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void setService(UserService service)
/* 101:    */   {
/* 102:104 */     this.service = service;
/* 103:    */   }
/* 104:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentprovider.ThirdPartyUserPermission
 * JD-Core Version:    0.7.0.1
 */