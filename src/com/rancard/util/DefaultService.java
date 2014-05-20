/*  1:   */ package com.rancard.util;
/*  2:   */ 
/*  3:   */ import com.rancard.mobility.infoserver.common.services.ServiceManager;
/*  4:   */ import java.util.ArrayList;
/*  5:   */ import java.util.Arrays;
/*  6:   */ import java.util.Iterator;
/*  7:   */ import java.util.List;
/*  8:   */ 
/*  9:   */ public class DefaultService
/* 10:   */ {
/* 11:16 */   private static List<String> tempSubscriptionKeywords = Arrays.asList(new String[] { "in", "ln", "jo", "mn", "an", "gf", "fb", "qo", "bt", "CN", "is", "bn", "qt", "gh", "ifn", "hb", "fn", "epl", "spd", "isa", "voa", "voi", "bl", "qu", "sr" });
/* 12:19 */   private static ArrayList<String> subscriptionKeywords = new ArrayList(tempSubscriptionKeywords);
/* 13:   */   
/* 14:   */   public static String getHelp(String accountId, String msisdn, String dest, String keyword)
/* 15:   */   {
/* 16:22 */     String helpMessage = "";
/* 17:23 */     if (accountId.equals("005")) {
/* 18:24 */       helpMessage = handleTigoHelp(msisdn);
/* 19:25 */     } else if (accountId.equals("085")) {
/* 20:26 */       helpMessage = handleGloNigEspnHelp();
/* 21:   */     }
/* 22:28 */     return helpMessage;
/* 23:   */   }
/* 24:   */   
/* 25:   */   private static String handleTigoHelp(String msisdn)
/* 26:   */   {
/* 27:32 */     String finalMessage = "Sorry! We couldn't find the service you wanted. Please reply to this message with HELP.";
/* 28:   */     
/* 29:34 */     ArrayList<String> currentSubscriptions = new ArrayList();
/* 30:   */     try
/* 31:   */     {
/* 32:36 */       currentSubscriptions = ServiceManager.getKeywordsUserSubscribedTo(msisdn, "005", subscriptionKeywords);
/* 33:38 */       if (currentSubscriptions.size() == 0)
/* 34:   */       {
/* 35:39 */         finalMessage = "SMS Plus: To receive content, select an option (eg: African News) from the Tigo SMS plus menu on your phone. To unsubscribe, send STOP + keyword to 801";
/* 36:   */       }
/* 37:42 */       else if (currentSubscriptions.size() == 1)
/* 38:   */       {
/* 39:43 */         String keywordList = expandKeywordList(currentSubscriptions);
/* 40:44 */         finalMessage = "You are currently subscribed to the following service: " + keywordList + ". " + "If you wish to cancel your subscription, send STOP to 801";
/* 41:   */       }
/* 42:   */       else
/* 43:   */       {
/* 44:48 */         String keywordList = expandKeywordList(currentSubscriptions);
/* 45:49 */         String singleKeyword = keywordList.split(", ")[0];
/* 46:50 */         finalMessage = "You are currently subscribed to the following services: " + keywordList + ". " + "If you wish to cancel one of the services, send (example) STOP " + singleKeyword + " to 801";
/* 47:   */       }
/* 48:   */     }
/* 49:   */     catch (Exception e) {}
/* 50:57 */     return finalMessage;
/* 51:   */   }
/* 52:   */   
/* 53:   */   private static String handleGloNigEspnHelp()
/* 54:   */   {
/* 55:61 */     return "Sorry we cannot identify your keyword. To get the hottest sports news from around the world simply send ESPN to 32929";
/* 56:   */   }
/* 57:   */   
/* 58:   */   private static String expandKeywordList(ArrayList<String> keywordList)
/* 59:   */   {
/* 60:66 */     StringBuffer keyListBuffer = new StringBuffer();
/* 61:67 */     Iterator<String> keywordIterator = keywordList.iterator();
/* 62:68 */     while (keywordIterator.hasNext())
/* 63:   */     {
/* 64:69 */       keyListBuffer.append(((String)keywordIterator.next()).toUpperCase());
/* 65:70 */       if (keywordIterator.hasNext()) {
/* 66:71 */         keyListBuffer.append(", ");
/* 67:   */       }
/* 68:   */     }
/* 69:73 */     return keyListBuffer.toString();
/* 70:   */   }
/* 71:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.DefaultService
 * JD-Core Version:    0.7.0.1
 */