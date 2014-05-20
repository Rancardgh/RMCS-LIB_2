/*  1:   */ package com.rancard.mobility.contentserver;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ 
/*  5:   */ public class CustomList
/*  6:   */ {
/*  7:   */   private String cpId;
/*  8:   */   private String customListId;
/*  9:   */   private String customListName;
/* 10:   */   private ArrayList items;
/* 11:   */   
/* 12:   */   public CustomList()
/* 13:   */   {
/* 14:26 */     this.cpId = "";
/* 15:27 */     this.customListId = "";
/* 16:28 */     this.customListName = "";
/* 17:29 */     this.items = new ArrayList();
/* 18:   */   }
/* 19:   */   
/* 20:   */   public CustomList(String cpId, String customListId, String customListName)
/* 21:   */   {
/* 22:34 */     this.cpId = cpId;
/* 23:35 */     this.customListId = customListId;
/* 24:36 */     this.customListName = customListName;
/* 25:37 */     this.items = new ArrayList();
/* 26:   */   }
/* 27:   */   
/* 28:   */   public CustomList(String cpId, String customListId, String customListName, ArrayList<ContentItem> items)
/* 29:   */   {
/* 30:42 */     this.cpId = cpId;
/* 31:43 */     this.customListId = customListId;
/* 32:44 */     this.customListName = customListName;
/* 33:45 */     this.items = items;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public String getCpId()
/* 37:   */   {
/* 38:49 */     return this.cpId;
/* 39:   */   }
/* 40:   */   
/* 41:   */   public void setCpId(String cpId)
/* 42:   */   {
/* 43:53 */     this.cpId = cpId;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public String getCustomListId()
/* 47:   */   {
/* 48:57 */     return this.customListId;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public void setCustomListId(String customListId)
/* 52:   */   {
/* 53:61 */     this.customListId = customListId;
/* 54:   */   }
/* 55:   */   
/* 56:   */   public String getCustomListName()
/* 57:   */   {
/* 58:65 */     return this.customListName;
/* 59:   */   }
/* 60:   */   
/* 61:   */   public void setCustomListName(String customListName)
/* 62:   */   {
/* 63:69 */     this.customListName = customListName;
/* 64:   */   }
/* 65:   */   
/* 66:   */   public ArrayList getItems()
/* 67:   */   {
/* 68:73 */     return this.items;
/* 69:   */   }
/* 70:   */   
/* 71:   */   public void setItems(ArrayList<ContentItem> items)
/* 72:   */   {
/* 73:77 */     this.items = items;
/* 74:   */   }
/* 75:   */   
/* 76:   */   public boolean isEmpty()
/* 77:   */   {
/* 78:82 */     return this.items.isEmpty();
/* 79:   */   }
/* 80:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.CustomList
 * JD-Core Version:    0.7.0.1
 */