/*   1:    */ package com.rancard.mobility;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.PreparedStatement;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ import java.sql.Timestamp;
/*   8:    */ import java.util.Date;
/*   9:    */ import java.util.HashMap;
/*  10:    */ import javax.servlet.Filter;
/*  11:    */ import javax.servlet.FilterChain;
/*  12:    */ import javax.servlet.FilterConfig;
/*  13:    */ import javax.servlet.ServletContext;
/*  14:    */ import javax.servlet.ServletRequest;
/*  15:    */ import javax.servlet.ServletResponse;
/*  16:    */ import javax.servlet.http.HttpServlet;
/*  17:    */ 
/*  18:    */ public class serviceusagefilter
/*  19:    */   extends HttpServlet
/*  20:    */   implements Filter
/*  21:    */ {
/*  22:    */   private FilterConfig filterConfig;
/*  23: 14 */   private HashMap routingTable = new HashMap();
/*  24:    */   private static final String FROM = "RMCS";
/*  25:    */   String baseUrl;
/*  26:    */   private static final boolean debug = false;
/*  27:    */   
/*  28:    */   public void init(FilterConfig filterConfig)
/*  29:    */   {
/*  30: 21 */     this.filterConfig = filterConfig;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
/*  34:    */   {
/*  35: 41 */     String keyword = req.getParameter("keyword");
/*  36: 42 */     String from = req.getParameter("msisdn");
/*  37: 43 */     String to = req.getParameter("dest");
/*  38: 44 */     String smscId = req.getParameter("smsc");
/*  39: 45 */     String time = req.getParameter("time");
/*  40:    */     try
/*  41:    */     {
/*  42: 50 */       logServiceRequest(keyword, from, to, time, smscId);
/*  43:    */     }
/*  44:    */     catch (Exception ex)
/*  45:    */     {
/*  46: 52 */       log(ex.getMessage());
/*  47:    */     }
/*  48:    */     try
/*  49:    */     {
/*  50: 55 */       filterChain.doFilter(req, res);
/*  51:    */     }
/*  52:    */     catch (Exception e) {}
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void logServiceRequest(String keyword, String from, String to, String timeSent, String smscId)
/*  56:    */     throws Exception
/*  57:    */   {
/*  58:285 */     ResultSet rs = null;
/*  59:286 */     Connection con = null;
/*  60:287 */     PreparedStatement prepstat = null;
/*  61:    */     try
/*  62:    */     {
/*  63:290 */       con = DConnect.getConnection();
/*  64:    */       
/*  65:292 */       String query = "insert into rmcs.service_usage_log (keyword, to_number, time_received,from_number, smscId, time_sent) values (?, ?, ?, ?, ?, ?)";
/*  66:    */       
/*  67:294 */       prepstat = con.prepareStatement(query);
/*  68:295 */       prepstat.setString(1, keyword);
/*  69:296 */       prepstat.setString(2, to);
/*  70:297 */       prepstat.setString(3, timeSent);
/*  71:298 */       prepstat.setString(4, from);
/*  72:299 */       prepstat.setString(5, smscId);
/*  73:300 */       prepstat.setTimestamp(6, new Timestamp(new Date().getTime()));
/*  74:301 */       prepstat.execute();
/*  75:    */     }
/*  76:    */     catch (Exception ex)
/*  77:    */     {
/*  78:303 */       if (con != null)
/*  79:    */       {
/*  80:    */         try
/*  81:    */         {
/*  82:305 */           con.close();
/*  83:    */         }
/*  84:    */         catch (Exception ex1)
/*  85:    */         {
/*  86:307 */           log(ex.getMessage());
/*  87:    */         }
/*  88:309 */         con = null;
/*  89:    */       }
/*  90:    */     }
/*  91:    */     finally
/*  92:    */     {
/*  93:312 */       if (rs != null)
/*  94:    */       {
/*  95:    */         try
/*  96:    */         {
/*  97:314 */           rs.close();
/*  98:    */         }
/*  99:    */         catch (Exception e)
/* 100:    */         {
/* 101:316 */           log(e.getMessage());
/* 102:    */         }
/* 103:318 */         rs = null;
/* 104:    */       }
/* 105:320 */       if (prepstat != null)
/* 106:    */       {
/* 107:    */         try
/* 108:    */         {
/* 109:322 */           prepstat.close();
/* 110:    */         }
/* 111:    */         catch (Exception e)
/* 112:    */         {
/* 113:324 */           log(e.getMessage());
/* 114:    */         }
/* 115:326 */         prepstat = null;
/* 116:    */       }
/* 117:328 */       if (con != null)
/* 118:    */       {
/* 119:    */         try
/* 120:    */         {
/* 121:330 */           con.close();
/* 122:    */         }
/* 123:    */         catch (Exception e)
/* 124:    */         {
/* 125:332 */           log(e.getMessage());
/* 126:    */         }
/* 127:334 */         con = null;
/* 128:    */       }
/* 129:    */     }
/* 130:    */   }
/* 131:    */   
/* 132:    */   public void log(String msg)
/* 133:    */   {
/* 134:340 */     this.filterConfig.getServletContext().log(msg);
/* 135:    */   }
/* 136:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.serviceusagefilter
 * JD-Core Version:    0.7.0.1
 */