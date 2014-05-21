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
/* 47:56 */       if (con != null) {
/* 48:57 */         con.close();
/* 49:   */       }
/* 50:58 */       throw new Exception(ex.getMessage());
/* 51:   */     }
/* 52:   */     finally
/* 53:   */     {
/* 54:60 */       if (con != null) {
/* 55:61 */         con.close();
/* 56:   */       }
/* 57:   */     }
/* 58:64 */     return created;
/* 59:   */   }
/* 60:   */   
/* 61:   */   public static void updateTriggerSchedule(Timestamp triggerTime)
/* 62:   */     throws Exception
/* 63:   */   {
/* 64:70 */     ResultSet rs = null;
/* 65:71 */     Connection con = null;
/* 66:72 */     PreparedStatement prepstat = null;
/* 67:73 */     boolean created = false;
/* 68:   */     try
/* 69:   */     {
/* 70:76 */       con = DConnect.getConnection();
/* 71:77 */       String timeString = triggerTime.toString();
/* 72:78 */       timeString = timeString.substring(0, timeString.lastIndexOf(":"));
/* 73:79 */       String SQL = "update livescore_trigger_schedule set sent=1 where trigger_time like '" + timeString + "%'";
/* 74:80 */       prepstat = con.prepareStatement(SQL);
/* 75:81 */       prepstat.execute();
/* 76:   */     }
/* 77:   */     catch (Exception ex)
/* 78:   */     {
/* 79:83 */       if (con != null) {
/* 80:84 */         con.close();
/* 81:   */       }
/* 82:85 */       throw new Exception(ex.getMessage());
/* 83:   */     }
/* 84:   */     finally
/* 85:   */     {
/* 86:87 */       if (con != null) {
/* 87:88 */         con.close();
/* 88:   */       }
/* 89:   */     }
/* 90:   */   }
/* 91:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.livescore.LiveScoreTriggerScheduleDB
 * JD-Core Version:    0.7.0.1
 */