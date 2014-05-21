/*  1:   */ package com.rancard.mobility.contentserver;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ 
/*  5:   */ public abstract class CsCpRelationship
/*  6:   */ {
/*  7:   */   public static void insertRelationship(String cs_id, String listId)
/*  8:   */     throws Exception
/*  9:   */   {
/* 10:10 */     RelationshipDB.insertRelationship(cs_id, listId);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public static void deleteRelationshipForProvider(String id)
/* 14:   */     throws Exception
/* 15:   */   {
/* 16:17 */     RelationshipDB.deleteRelationshipForProvider(id);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public static void deleteRelationshipForContentSubscriber(String id)
/* 20:   */     throws Exception
/* 21:   */   {
/* 22:21 */     RelationshipDB.deleteRelationshipForContentSubscriber(id);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public static void deleteRelationship(String csid, String cpid)
/* 26:   */     throws Exception
/* 27:   */   {
/* 28:25 */     RelationshipDB.deleteRelationship(csid, cpid);
/* 29:   */   }
/* 30:   */   
/* 31:   */   public static void deleteRelationship(String[] csid, String cpid)
/* 32:   */     throws Exception
/* 33:   */   {
/* 34:29 */     RelationshipDB.deleteRelationship(csid, cpid);
/* 35:   */   }
/* 36:   */   
/* 37:   */   public static void deleteProviderRelationship(String[] cpid, String csid)
/* 38:   */     throws Exception
/* 39:   */   {
/* 40:33 */     RelationshipDB.deleteProviderRelationship(cpid, csid);
/* 41:   */   }
/* 42:   */   
/* 43:   */   public static ArrayList viewProvidersForContentSubscriber(String id)
/* 44:   */     throws Exception
/* 45:   */   {
/* 46:41 */     return RelationshipDB.viewProvidersForContentSubscriber(id);
/* 47:   */   }
/* 48:   */   
/* 49:   */   public static ArrayList viewContentSubscribersForProvider(String id)
/* 50:   */     throws Exception
/* 51:   */   {
/* 52:46 */     return RelationshipDB.viewContentSubscribersForProvider(id);
/* 53:   */   }
/* 54:   */   
/* 55:   */   public static ArrayList viewProvidersDetailsForContentSubscriber(String id)
/* 56:   */     throws Exception
/* 57:   */   {
/* 58:50 */     return RelationshipDB.viewProvidersDetailsForContentSubscriber(id);
/* 59:   */   }
/* 60:   */   
/* 61:   */   public static ArrayList viewContentSubscribersDetailsForProvider(String id)
/* 62:   */     throws Exception
/* 63:   */   {
/* 64:55 */     return RelationshipDB.viewContentSubscriberDetailsForProvider(id);
/* 65:   */   }
/* 66:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.CsCpRelationship
 * JD-Core Version:    0.7.0.1
 */