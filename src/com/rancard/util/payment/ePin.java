/*   1:    */ package com.rancard.util.payment;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ 
/*   5:    */ public class ePin
/*   6:    */ {
/*   7:    */   private String provider;
/*   8:    */   private String pin;
/*   9:    */   private double eValue;
/*  10:    */   private double currentBalance;
/*  11:    */   
/*  12:    */   public boolean isValid()
/*  13:    */   {
/*  14: 28 */     boolean valid = false;
/*  15: 29 */     ePinDB pin = new ePinDB();
/*  16: 30 */     ePin voucher = new ePin();
/*  17:    */     try
/*  18:    */     {
/*  19: 32 */       voucher = pin.viewVoucher(this.pin);
/*  20: 33 */       if ((voucher.getPin() != null) && (voucher.getEValue() != 0.0D) && (voucher.getCurrentBalance() != 0.0D))
/*  21:    */       {
/*  22: 35 */         setEValue(voucher.getEValue());
/*  23: 36 */         setPin(voucher.getPin());
/*  24: 37 */         setCurrentBalance(voucher.getCurrentBalance());
/*  25: 38 */         setProvider(voucher.getProvider());
/*  26: 39 */         valid = true;
/*  27:    */       }
/*  28:    */     }
/*  29:    */     catch (Exception ex)
/*  30:    */     {
/*  31: 42 */       ex.printStackTrace();
/*  32:    */     }
/*  33: 44 */     return valid;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public String getPin()
/*  37:    */   {
/*  38: 52 */     return this.pin;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setPin(String pin)
/*  42:    */   {
/*  43: 60 */     this.pin = pin;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setProvider(String listId)
/*  47:    */   {
/*  48: 68 */     this.provider = listId;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public String getProvider()
/*  52:    */   {
/*  53: 76 */     return this.provider;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public double getCurrentBalance()
/*  57:    */   {
/*  58: 84 */     return this.currentBalance;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void setCurrentBalance(double currentBalance)
/*  62:    */   {
/*  63: 92 */     this.currentBalance = currentBalance;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public double getEValue()
/*  67:    */   {
/*  68:100 */     return this.eValue;
/*  69:    */   }
/*  70:    */   
/*  71:    */   public void setEValue(double eValue)
/*  72:    */   {
/*  73:108 */     this.eValue = eValue;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void updateMyLog()
/*  77:    */     throws Exception
/*  78:    */   {
/*  79:112 */     new ePinDB().updateVoucher(getPin(), this.currentBalance);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public static void insertVoucher(String listId, String pin, double limit, double currentBalance)
/*  83:    */     throws Exception
/*  84:    */   {
/*  85:116 */     new ePinDB().insertVoucher(listId, pin, limit, currentBalance);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public static void deleteVoucher(String pin)
/*  89:    */     throws Exception
/*  90:    */   {
/*  91:120 */     new ePinDB();ePinDB.deleteVoucher(pin);
/*  92:    */   }
/*  93:    */   
/*  94:    */   public static ePin viewVoucher(String pin)
/*  95:    */     throws Exception
/*  96:    */   {
/*  97:124 */     return new ePinDB().viewVoucher(pin);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public static ArrayList viewVouchers(String listId)
/* 101:    */     throws Exception
/* 102:    */   {
/* 103:128 */     return new ePinDB().viewVouchers(listId);
/* 104:    */   }
/* 105:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.payment.ePin
 * JD-Core Version:    0.7.0.1
 */