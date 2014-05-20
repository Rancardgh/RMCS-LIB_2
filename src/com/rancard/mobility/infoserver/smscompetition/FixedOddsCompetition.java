/*  1:   */ package com.rancard.mobility.infoserver.smscompetition;
/*  2:   */ 
/*  3:   */ import com.rancard.mobility.infoserver.common.inbox.InboxEntry;
/*  4:   */ import com.rancard.mobility.infoserver.common.inbox.InboxManager;
/*  5:   */ import java.sql.Timestamp;
/*  6:   */ import java.util.ArrayList;
/*  7:   */ 
/*  8:   */ public class FixedOddsCompetition
/*  9:   */   extends Competition
/* 10:   */ {
/* 11:   */   public FixedOddsCompetition()
/* 12:   */   {
/* 13:10 */     setParticipationLimit("0");
/* 14:11 */     setType(1);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public FixedOddsCompetition(String serviceType, String serviceName, String defaultMessage, String keyword, String desc, String thumburl, String limit, String accountId, int type, int partLevel, Timestamp start, Timestamp end, ArrayList prizes, String qstn, ArrayList opt, String ans, ArrayList altAns)
/* 18:   */   {
/* 19:17 */     super(serviceType, serviceName, defaultMessage, keyword, desc, thumburl, limit, accountId, type, partLevel, start, end, prizes, qstn, opt, ans, altAns);
/* 20:   */     
/* 21:19 */     setParticipationLimit("0");
/* 22:20 */     setType(1);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public boolean canAddParticipant()
/* 26:   */     throws Exception
/* 27:   */   {
/* 28:26 */     boolean canRegister = false;
/* 29:27 */     if (getParticipationLimit().equals("*")) {
/* 30:29 */       canRegister = true;
/* 31:31 */     } else if (getCurrentParticipation() + 1L > Integer.parseInt(getParticipationLimit())) {
/* 32:33 */       canRegister = false;
/* 33:   */     } else {
/* 34:35 */       canRegister = true;
/* 35:   */     }
/* 36:38 */     return canRegister;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public boolean alreadyRegistered(String mobileno)
/* 40:   */     throws Exception
/* 41:   */   {
/* 42:43 */     return CompetitionDB.alreadyRegistered(mobileno, getKeyword(), getAccountId());
/* 43:   */   }
/* 44:   */   
/* 45:   */   public boolean alreadyVoted(String mobileno)
/* 46:   */     throws Exception
/* 47:   */   {
/* 48:48 */     return CompetitionDB.alreadyVoted(mobileno, getKeyword(), getAccountId());
/* 49:   */   }
/* 50:   */   
/* 51:   */   public String register(InboxEntry entry)
/* 52:   */     throws Exception
/* 53:   */   {
/* 54:54 */     String response = InboxManager.write(entry);
/* 55:55 */     return response;
/* 56:   */   }
/* 57:   */   
/* 58:   */   public void vote(InboxEntry entry)
/* 59:   */     throws Exception
/* 60:   */   {
/* 61:59 */     InboxManager.updateVote(entry);
/* 62:   */   }
/* 63:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.smscompetition.FixedOddsCompetition
 * JD-Core Version:    0.7.0.1
 */