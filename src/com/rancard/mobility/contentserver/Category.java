/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.List;
/*   6:    */ 
/*   7:    */ public class Category
/*   8:    */ {
/*   9:    */   private int id;
/*  10:    */   private String categoryDesc;
/*  11:    */   private int contentType;
/*  12:    */   
/*  13:    */   public Category()
/*  14:    */   {
/*  15: 26 */     this.id = 0;
/*  16: 27 */     this.categoryDesc = "";
/*  17: 28 */     this.contentType = 0;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public Category(int id, String desc, int contentType)
/*  21:    */   {
/*  22: 33 */     this.id = id;
/*  23: 34 */     this.categoryDesc = desc;
/*  24: 35 */     this.contentType = contentType;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setId(int id)
/*  28:    */   {
/*  29: 40 */     this.id = id;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setCategoryDesc(String desc)
/*  33:    */   {
/*  34: 44 */     this.categoryDesc = desc;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setContentType(int contentType)
/*  38:    */   {
/*  39: 48 */     this.contentType = contentType;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public int getId()
/*  43:    */   {
/*  44: 53 */     return this.id;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public String getCategoryDesc()
/*  48:    */   {
/*  49: 57 */     return this.categoryDesc;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public int getContentType()
/*  53:    */   {
/*  54: 61 */     return this.contentType;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public static Category viewCategory(int referenceCode)
/*  58:    */     throws Exception
/*  59:    */   {
/*  60: 66 */     return CategoryDB.viewCategory(referenceCode);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void updateCategory()
/*  64:    */     throws Exception
/*  65:    */   {
/*  66: 70 */     CategoryDB.updateCategory(this.id, this.categoryDesc, this.contentType);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void removeCategory()
/*  70:    */     throws Exception
/*  71:    */   {
/*  72: 75 */     CategoryDB.deleteCategory(this.id);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void insertCategory()
/*  76:    */     throws Exception
/*  77:    */   {
/*  78: 79 */     CategoryDB.insertCategory(this.categoryDesc, this.contentType);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static List getCategories(String contentType)
/*  82:    */     throws Exception
/*  83:    */   {
/*  84: 84 */     List categoryIDs = new ArrayList();
/*  85: 85 */     categoryIDs = CategoryDB.getDistinctCategories(contentType);
/*  86: 86 */     return categoryIDs;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public static List getPopulatedCategories(String contentType, String siteId)
/*  90:    */     throws Exception
/*  91:    */   {
/*  92: 96 */     List categoryIDs = new ArrayList();
/*  93: 97 */     categoryIDs = CategoryDB.getPopulatedCategories(contentType, siteId);
/*  94: 98 */     return categoryIDs;
/*  95:    */   }
/*  96:    */   
/*  97:    */   public static HashMap getPopulatedCategories(String siteId)
/*  98:    */     throws Exception
/*  99:    */   {
/* 100:102 */     return CategoryDB.getPopulatedCategories(siteId);
/* 101:    */   }
/* 102:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.Category
 * JD-Core Version:    0.7.0.1
 */