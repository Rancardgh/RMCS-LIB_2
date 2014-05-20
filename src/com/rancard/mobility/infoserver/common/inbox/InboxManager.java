/*  1:   */ package com.rancard.mobility.infoserver.common.inbox;
/*  2:   */ 
/*  3:   */ import com.rancard.util.Page;
/*  4:   */ import java.sql.Timestamp;
/*  5:   */ import java.util.ArrayList;
/*  6:   */ 
/*  7:   */ public abstract class InboxManager
/*  8:   */ {
/*  9:   */   public static String write(InboxEntry entry)
/* 10:   */     throws Exception
/* 11:   */   {
/* 12:11 */     return InboxDB.write(entry);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public static void updateVote(InboxEntry entry)
/* 16:   */     throws Exception
/* 17:   */   {
/* 18:16 */     InboxDB.updateVote(entry);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public static void deleteVote(String keyword, String msisdn, String accountId)
/* 22:   */     throws Exception
/* 23:   */   {
/* 24:21 */     InboxDB.deleteVote(keyword, msisdn, accountId);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public static void deleteVote(String entryId)
/* 28:   */     throws Exception
/* 29:   */   {
/* 30:25 */     InboxDB.deleteVote(entryId);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public static void deleteVotes(String keyword, String accountId)
/* 34:   */     throws Exception
/* 35:   */   {
/* 36:30 */     InboxDB.deleteVotes(keyword, accountId);
/* 37:   */   }
/* 38:   */   
/* 39:   */   public static InboxEntry viewEntry(String keyword, String msisdn, String accountId)
/* 40:   */     throws Exception
/* 41:   */   {
/* 42:35 */     return InboxDB.viewEntry(keyword, msisdn, accountId);
/* 43:   */   }
/* 44:   */   
/* 45:   */   public static InboxEntry viewEntry(String entryId)
/* 46:   */     throws Exception
/* 47:   */   {
/* 48:39 */     return InboxDB.viewEntry(entryId);
/* 49:   */   }
/* 50:   */   
/* 51:   */   public static ArrayList viewEntries(String keyword, String accountId)
/* 52:   */     throws Exception
/* 53:   */   {
/* 54:44 */     return InboxDB.viewEntries(keyword, accountId);
/* 55:   */   }
/* 56:   */   
/* 57:   */   public static ArrayList viewEntries(String competitionId)
/* 58:   */     throws Exception
/* 59:   */   {
/* 60:48 */     return InboxDB.viewEntries(competitionId);
/* 61:   */   }
/* 62:   */   
/* 63:   */   public static ArrayList viewEntriesOnDate(String competitionId, String date)
/* 64:   */     throws Exception
/* 65:   */   {
/* 66:52 */     return InboxDB.viewEntriesOnDate(competitionId, date);
/* 67:   */   }
/* 68:   */   
/* 69:   */   public static ArrayList viewCorrectEntries(String competitionId)
/* 70:   */     throws Exception
/* 71:   */   {
/* 72:56 */     return InboxDB.viewCorrectEntries(competitionId);
/* 73:   */   }
/* 74:   */   
/* 75:   */   public static ArrayList calculateResults(String competitionId)
/* 76:   */     throws Exception
/* 77:   */   {
/* 78:60 */     return InboxDB.calculateResults(competitionId);
/* 79:   */   }
/* 80:   */   
/* 81:   */   public static int getParticipationLevel(String keyword, String accountId)
/* 82:   */     throws Exception
/* 83:   */   {
/* 84:64 */     return InboxDB.getParticipationLevel(keyword, accountId);
/* 85:   */   }
/* 86:   */   
/* 87:   */   public static Page viewInbox(String keyword, String accountId, Timestamp startdate, Timestamp enddate, int start, int count)
/* 88:   */     throws Exception
/* 89:   */   {
/* 90:69 */     return InboxDB.viewInbox(keyword, accountId, startdate, enddate, start, count);
/* 91:   */   }
/* 92:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.common.inbox.InboxManager
 * JD-Core Version:    0.7.0.1
 */