/*  1:   */ package com.rancard.mobility.infoserver.livescore;
/*  2:   */ 
/*  3:   */ import com.rancard.common.DConnect;
/*  4:   */ import java.sql.Connection;
/*  5:   */ import java.sql.PreparedStatement;
/*  6:   */ import java.sql.ResultSet;
/*  7:   */ import java.sql.Timestamp;
/*  8:   */ 
/*  9:   */ public abstract class LiveScoreTriggerScheduleDB
/* 10:   */ {
/* 11:   */   public static boolean createTriggerSchedule(Timestamp triggerTime)
/* 12:   */     throws Exception
/* 13:   */   {
/* 14:27 */     ResultSet rs = null;
/* 15:28 */     Connection con = null;
/* 16:29 */     PreparedStatement prepstat = null;
/* 17:30 */     boolean created = false;
/* 18:   */     try
/* 19:   */     {
/* 20:33 */       con = DConnect.getConnection();
/* 21:   */       
/* 22:35 */       String SQL = "select * from livescore_trigger_schedule where trigger_time=?";
/* 23:36 */       prepstat = con.prepareStatement(SQL);
/* 24:37 */       prepstat.setTimestamp(1, triggerTime);
/* 25:38 */       rs = prepstat.executeQuery();
/* 26:40 */       if (rs.next())
/* 27:   */       {
/* 28:41 */         if (rs.getInt("sent") == 0) {
/* 29:42 */           created = true;
/* 30:   */         } else {
/* 31:44 */           created = false;
/* 32:   */         }
/* 33:   */       }
/* 34:   */       else
/* 35:   */       {
/* 36:47 */         SQL = "insert into livescore_trigger_schedule (trigger_time,sent) values (?,?)";
/* 37:48 */         prepstat = con.prepareStatement(SQL);
/* 38:49 */         prepstat.setTimestamp(1, triggerTime);
/* 39:50 */         prepstat.setInt(2, 0);
/* 40:51 */         prepstat.execute();
/* 41:   */         
/* 42:53 */         created = true;
/* 43:   */       }
/* 44:   */     }
/* 45:   */     catch (Exception ex)
/* 46:   */     {
/* 47:58 */       throw new Exception(ex.getMessage());
/* 48:   */     }
/* 49:   */     finally
/* 50:   */     {
/* 51:60 */       if (con != null) {
/* 52:61 */         con.close();
/* 53:   */       }
/* 54:   */     }
/* 55:64 */     return created;
/* 56:   */   }
/* 57:   */   
/* 58:   */   public static void updateTriggerSchedule(Timestamp triggerTime)
/* 59:   */     throws Exception
/* 60:   */   {
/* 61:70 */     ResultSet rs = null;
/* 62:71 */     Connection con = null;
/* 63:72 */     PreparedStatement prepstat = null;
/* 64:73 */     boolean created = false;
/* 65:   */     try
/* 66:   */     {
/* 67:76 */       con = DConnect.getConnection();
/* 68:77 */       String timeString = triggerTime.toString();
/* 69:78 */       timeString = timeString.substring(0, timeString.lastIndexOf(":"));
/* 70:79 */       String SQL = "update livescore_trigger_schedule set sent=1 where trigger_time like '" + timeString + "%'";
/* 71:80 */       prepstat = con.prepareStatement(SQL);
/* 72:81 */       prepstat.execute();
/* 73:   */     }
/* 74:   */     catch (Exception ex)
/* 75:   */     {
/* 76:85 */       throw new Exception(ex.getMessage());
/* 77:   */     }
/* 78:   */     finally
/* 79:   */     {
/* 80:87 */       if (con != null) {
/* 81:88 */         con.close();
/* 82:   */       }
/* 83:   */     }
/* 84:   */   }
/* 85:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.LiveScoreTriggerScheduleDB
 * JD-Core Version:    0.7.0.1
 */