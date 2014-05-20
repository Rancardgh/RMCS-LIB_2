/*  1:   */ package com.rancard.mobility.infoserver.smscompetition;
/*  2:   */ 
/*  3:   */ import com.rancard.mobility.infoserver.common.inbox.InboxEntry;
/*  4:   */ import com.rancard.mobility.infoserver.common.inbox.InboxManager;
/*  5:   */ import java.sql.Timestamp;
/*  6:   */ import java.util.ArrayList;
/*  7:   */ 
/*  8:   */ public class EndlessOddsCompetition
/*  9:   */   extends Competition
/* 10:   */ {
/* 11:   */   public EndlessOddsCompetition()
/* 12:   */   {
/* 13:11 */     setParticipationLimit("*");
/* 14:12 */     setType(0);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public EndlessOddsCompetition(String serviceType, String serviceName, String defaultMessage, String keyword, String desc, String thumburl, String limit, String accountId, int type, int partLevel, Timestamp start, Timestamp end, ArrayList prizes, String qstn, ArrayList opt, String ans, ArrayList altAns)
/* 18:   */   {
/* 19:18 */     super(serviceType, serviceName, defaultMessage, keyword, desc, thumburl, limit, accountId, type, partLevel, start, end, prizes, qstn, opt, ans, altAns);
/* 20:   */     
/* 21:20 */     setParticipationLimit("*");
/* 22:21 */     setType(0);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public String vote(InboxEntry entry)
/* 26:   */     throws Exception
/* 27:   */   {
/* 28:26 */     String response = InboxManager.write(entry);
/* 29:   */     
/* 30:28 */     return response;
/* 31:   */   }
/* 32:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.infoserver.smscompetition.EndlessOddsCompetition
 * JD-Core Version:    0.7.0.1
 */