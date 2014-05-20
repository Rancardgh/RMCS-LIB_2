/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import java.util.StringTokenizer;
/*   6:    */ 
/*   7:    */ public class Phone
/*   8:    */ {
/*   9:    */   private int id;
/*  10:    */   private String phoneModel;
/*  11:    */   private int phoneMake;
/*  12:    */   private String supportedFormats;
/*  13:    */   
/*  14:    */   public Phone()
/*  15:    */   {
/*  16: 24 */     this.id = 0;
/*  17: 25 */     this.phoneModel = "";
/*  18: 26 */     this.phoneMake = 0;
/*  19: 27 */     this.supportedFormats = "";
/*  20:    */   }
/*  21:    */   
/*  22:    */   public Phone(int id, String phoneModel, int phoneMake, String supportedFormats)
/*  23:    */   {
/*  24: 31 */     this.id = id;
/*  25: 32 */     this.phoneModel = phoneModel;
/*  26: 33 */     this.phoneMake = phoneMake;
/*  27: 34 */     this.supportedFormats = supportedFormats;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setId(int id)
/*  31:    */   {
/*  32: 39 */     this.id = id;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setModel(String model)
/*  36:    */   {
/*  37: 43 */     this.phoneModel = model;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void setMake(int make)
/*  41:    */   {
/*  42: 47 */     this.phoneMake = make;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setSupportedFormats(String formats)
/*  46:    */   {
/*  47: 51 */     if (this.supportedFormats == null) {
/*  48: 52 */       this.supportedFormats = "";
/*  49:    */     }
/*  50: 53 */     this.supportedFormats = formats;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public int getId()
/*  54:    */   {
/*  55: 58 */     return this.id;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public String getModel()
/*  59:    */   {
/*  60: 62 */     return this.phoneModel;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public int getMake()
/*  64:    */   {
/*  65: 66 */     return this.phoneMake;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public String getSupportedFormats()
/*  69:    */   {
/*  70: 70 */     if (this.supportedFormats == null) {
/*  71: 71 */       this.supportedFormats = "";
/*  72:    */     }
/*  73: 72 */     return this.supportedFormats;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public ArrayList getSupportedFormatsList()
/*  77:    */   {
/*  78: 76 */     ArrayList formats = new ArrayList();
/*  79: 77 */     if (this.supportedFormats != null)
/*  80:    */     {
/*  81: 78 */       StringTokenizer st = new StringTokenizer(this.supportedFormats, ",");
/*  82: 80 */       while (st.hasMoreTokens()) {
/*  83: 81 */         formats.add(st.nextToken());
/*  84:    */       }
/*  85:    */     }
/*  86:    */     else
/*  87:    */     {
/*  88: 84 */       formats = null;
/*  89:    */     }
/*  90: 85 */     return formats;
/*  91:    */   }
/*  92:    */   
/*  93:    */   public static Phone viewPhone(int referenceCode)
/*  94:    */     throws Exception
/*  95:    */   {
/*  96: 90 */     return PhoneDB.viewPhone(referenceCode);
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void updatePhone()
/* 100:    */     throws Exception
/* 101:    */   {
/* 102: 94 */     PhoneDB.updatePhone(this.id, this.phoneModel, this.phoneMake, this.supportedFormats);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void removePhone()
/* 106:    */     throws Exception
/* 107:    */   {
/* 108: 98 */     PhoneDB.deletePhone(this.id);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void insertPhone()
/* 112:    */     throws Exception
/* 113:    */   {
/* 114:102 */     PhoneDB.insertPhone(this.phoneModel, this.phoneMake, this.supportedFormats);
/* 115:    */   }
/* 116:    */   
/* 117:    */   public static List getPhoneModels(String queryEnding)
/* 118:    */     throws Exception
/* 119:    */   {
/* 120:106 */     return PhoneDB.getDistinctMakes(queryEnding);
/* 121:    */   }
/* 122:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.Phone
 * JD-Core Version:    0.7.0.1
 */