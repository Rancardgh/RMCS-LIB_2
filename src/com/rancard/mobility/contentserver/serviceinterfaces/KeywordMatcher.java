/*   1:    */ package com.rancard.mobility.contentserver.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import com.rancard.mobility.infoserver.common.services.UserService;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.sql.Connection;
/*   7:    */ import java.sql.ResultSet;
/*   8:    */ import java.sql.Statement;
/*   9:    */ import java.util.ArrayList;
/*  10:    */ import java.util.Arrays;
/*  11:    */ import java.util.Date;
/*  12:    */ import java.util.HashMap;
/*  13:    */ import java.util.List;
/*  14:    */ import java.util.Map;
/*  15:    */ 
/*  16:    */ public class KeywordMatcher
/*  17:    */ {
/*  18:    */   private static double compareStrings(String str1, String str2)
/*  19:    */   {
/*  20: 26 */     ArrayList pairs1 = wordLetterPairs(str1.toUpperCase());
/*  21: 27 */     ArrayList pairs2 = wordLetterPairs(str2.toUpperCase());
/*  22: 28 */     int intersection = 0;
/*  23: 29 */     int union = pairs1.size() + pairs2.size();
/*  24: 30 */     for (int i = 0; i < pairs1.size(); i++)
/*  25:    */     {
/*  26: 31 */       Object pair1 = pairs1.get(i);
/*  27: 32 */       for (int j = 0; j < pairs2.size(); j++)
/*  28:    */       {
/*  29: 33 */         Object pair2 = pairs2.get(j);
/*  30: 34 */         if (pair1.equals(pair2))
/*  31:    */         {
/*  32: 35 */           intersection++;
/*  33: 36 */           pairs2.remove(j);
/*  34: 37 */           break;
/*  35:    */         }
/*  36:    */       }
/*  37:    */     }
/*  38: 41 */     return 2.0D * intersection / union;
/*  39:    */   }
/*  40:    */   
/*  41:    */   private static ArrayList wordLetterPairs(String str)
/*  42:    */   {
/*  43: 48 */     ArrayList allPairs = new ArrayList();
/*  44:    */     
/*  45: 50 */     String[] words = str.split("s");
/*  46: 52 */     for (int w = 0; w < words.length; w++)
/*  47:    */     {
/*  48: 54 */       String[] pairsInWord = letterPairs(words[w]);
/*  49: 55 */       allPairs.addAll(Arrays.asList(pairsInWord));
/*  50:    */     }
/*  51: 60 */     return allPairs;
/*  52:    */   }
/*  53:    */   
/*  54:    */   private static String[] letterPairs(String str)
/*  55:    */   {
/*  56: 67 */     int numPairs = str.length() - 1;
/*  57: 68 */     String[] pairs = new String[numPairs];
/*  58: 69 */     for (int i = 0; i < numPairs; i++) {
/*  59: 70 */       pairs[i] = str.substring(i, i + 2);
/*  60:    */     }
/*  61: 72 */     return pairs;
/*  62:    */   }
/*  63:    */   
/*  64:    */   private static Map search(String keyword, List<Map> list, String key)
/*  65:    */   {
/*  66: 77 */     double bestGuess = 0.0D;
/*  67: 78 */     Map synonym = null;
/*  68: 79 */     for (int i = 0; i < list.size(); i++) {
/*  69: 81 */       if (key.equalsIgnoreCase("tags"))
/*  70:    */       {
/*  71: 82 */         if ((((Map)list.get(i)).get(key) != null) && (!((Map)list.get(i)).get(key).toString().equals(""))) {
/*  72: 86 */           for (String tag : ((Map)list.get(i)).get(key).toString().split(","))
/*  73:    */           {
/*  74: 87 */             double result = compareStrings(keyword, tag);
/*  75: 88 */             if ((result > bestGuess) && (result > 0.6D))
/*  76:    */             {
/*  77: 89 */               bestGuess = result;
/*  78: 90 */               synonym = (Map)list.get(i);
/*  79:    */             }
/*  80:    */           }
/*  81:    */         }
/*  82:    */       }
/*  83:    */       else
/*  84:    */       {
/*  85: 94 */         double result = compareStrings(keyword, ((Map)list.get(i)).get(key).toString());
/*  86: 95 */         if ((result > bestGuess) && (result > 0.6D))
/*  87:    */         {
/*  88: 96 */           bestGuess = result;
/*  89: 97 */           synonym = (Map)list.get(i);
/*  90:    */         }
/*  91:    */       }
/*  92:    */     }
/*  93:101 */     return synonym;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public static UserService wordMatch(String keyword, String smsc, String shortCode)
/*  97:    */     throws Exception
/*  98:    */   {
/*  99:106 */     Map synonym = null;
/* 100:107 */     List<Map> services = new ArrayList();
/* 101:108 */     Connection conn = null;
/* 102:109 */     Statement stmt = null;
/* 103:110 */     ResultSet rs = null;
/* 104:    */     try
/* 105:    */     {
/* 106:112 */       conn = DConnect.getConnection();
/* 107:    */       
/* 108:114 */       String query = "select * from service_definition sd INNER JOIN cp_connections cp ON sd.account_id = cp.list_id where sd.service_type='14' AND cp.conn_id like '" + smsc + "%' " + "and (sd.allowed_shortcodes='' or  sd.allowed_shortcodes like '%" + shortCode + "%')";
/* 109:    */       
/* 110:    */ 
/* 111:    */ 
/* 112:118 */       System.out.println(new Date() + ": " + KeywordMatcher.class + ": Will run query: " + query);
/* 113:119 */       stmt = conn.createStatement();
/* 114:120 */       rs = stmt.executeQuery(query);
/* 115:    */       
/* 116:    */ 
/* 117:    */ 
/* 118:124 */       System.out.println(new Date() + ": " + KeywordMatcher.class + ": Sucessfully run query. Will add result to list");
/* 119:125 */       while (rs.next())
/* 120:    */       {
/* 121:126 */         Map map = new HashMap();
/* 122:127 */         map.put("keyword", rs.getString("keyword"));
/* 123:128 */         map.put("service_type", rs.getString("service_type"));
/* 124:129 */         map.put("account_id", rs.getString("account_id"));
/* 125:130 */         map.put("service_name", rs.getString("service_name"));
/* 126:131 */         map.put("tags", rs.getString("tags"));
/* 127:132 */         map.put("default_message", rs.getString("default_message"));
/* 128:133 */         map.put("last_updated", rs.getDate("last_updated"));
/* 129:134 */         map.put("command", rs.getString("command"));
/* 130:135 */         map.put("allowed_shortcodes", rs.getString("allowed_shortcodes"));
/* 131:136 */         map.put("allowed_site_types", rs.getString("allowed_site_types"));
/* 132:137 */         map.put("pricing", rs.getString("pricing"));
/* 133:138 */         map.put("service_response_sender", rs.getString("service_response_sender"));
/* 134:140 */         if (rs.getInt("is_basic") == 1) {
/* 135:141 */           map.put("is_basic", Boolean.valueOf(true));
/* 136:    */         } else {
/* 137:143 */           map.put("is_basic", Boolean.valueOf(false));
/* 138:    */         }
/* 139:146 */         if ((rs.getInt("is_subscription") == 1) || (rs.getString("is_subscription").equalsIgnoreCase("true"))) {
/* 140:147 */           map.put("is_subscription", Boolean.valueOf(true));
/* 141:    */         } else {
/* 142:149 */           map.put("is_subscription", Boolean.valueOf(false));
/* 143:    */         }
/* 144:152 */         services.add(map);
/* 145:    */       }
/* 146:155 */       System.out.println(new Date() + ": " + KeywordMatcher.class + ": Will search keywords for match");
/* 147:156 */       synonym = search(keyword, services, "keyword");
/* 148:158 */       if (synonym == null)
/* 149:    */       {
/* 150:159 */         System.out.println(new Date() + ": " + KeywordMatcher.class + ": Match not found will now search service name");
/* 151:160 */         search(keyword, services, "service_name");
/* 152:    */       }
/* 153:163 */       if (synonym == null)
/* 154:    */       {
/* 155:164 */         System.out.println(new Date() + ": " + KeywordMatcher.class + ": Match not found will now search tags");
/* 156:165 */         search(keyword, services, "tags");
/* 157:    */       }
/* 158:    */     }
/* 159:    */     catch (Exception e)
/* 160:    */     {
/* 161:169 */       System.out.println(new Date() + ": [keyMatcher[ERROR]]" + e.getMessage());
/* 162:170 */       throw new Exception(e.getMessage());
/* 163:    */     }
/* 164:    */     finally
/* 165:    */     {
/* 166:172 */       if (rs != null) {
/* 167:173 */         rs.close();
/* 168:    */       }
/* 169:175 */       if (stmt != null) {
/* 170:176 */         stmt.close();
/* 171:    */       }
/* 172:178 */       if (conn != null) {
/* 173:179 */         conn.close();
/* 174:    */       }
/* 175:    */     }
/* 176:184 */     if (synonym != null) {
/* 177:185 */       return new UserService(synonym.get("service_type").toString(), synonym.get("keyword").toString(), synonym.get("account_id").toString(), synonym.get("service_name").toString(), synonym.get("default_message").toString(), synonym.get("command").toString(), synonym.get("allowed_shortcodes").toString(), synonym.get("allowed_site_types").toString(), synonym.get("pricing").toString(), Boolean.getBoolean(synonym.get("is_basic").toString()), Boolean.getBoolean(synonym.get("is_subscription").toString()), synonym.get("service_response_sender").toString());
/* 178:    */     }
/* 179:191 */     return null;
/* 180:    */   }
/* 181:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.KeywordMatcher
 * JD-Core Version:    0.7.0.1
 */