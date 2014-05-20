/*   1:    */ package com.rancard.mobility.contentserver.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import com.rancard.mobility.infoserver.common.services.UserService;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.sql.Connection;
/*   7:    */ import java.sql.PreparedStatement;
/*   8:    */ import java.sql.ResultSet;
/*   9:    */ import java.sql.Statement;
/*  10:    */ import java.util.ArrayList;
/*  11:    */ import java.util.Arrays;
/*  12:    */ import java.util.Date;
/*  13:    */ import java.util.HashMap;
/*  14:    */ import java.util.List;
/*  15:    */ import java.util.Map;
/*  16:    */ 
/*  17:    */ public class KeywordMatcher
/*  18:    */ {
/*  19:    */   String SQL;
/*  20:    */   String synonym;
/*  21: 27 */   Connection con = null;
/*  22: 28 */   PreparedStatement prepstat = null;
/*  23: 29 */   ArrayList<String> keywords = new ArrayList();
/*  24: 30 */   double bestGuess = 0.0D;
/*  25:    */   
/*  26:    */   private static double compareStrings(String str1, String str2)
/*  27:    */   {
/*  28: 33 */     ArrayList pairs1 = wordLetterPairs(str1.toUpperCase());
/*  29: 34 */     ArrayList pairs2 = wordLetterPairs(str2.toUpperCase());
/*  30: 35 */     int intersection = 0;
/*  31: 36 */     int union = pairs1.size() + pairs2.size();
/*  32: 37 */     for (int i = 0; i < pairs1.size(); i++)
/*  33:    */     {
/*  34: 38 */       Object pair1 = pairs1.get(i);
/*  35: 39 */       for (int j = 0; j < pairs2.size(); j++)
/*  36:    */       {
/*  37: 40 */         Object pair2 = pairs2.get(j);
/*  38: 41 */         if (pair1.equals(pair2))
/*  39:    */         {
/*  40: 42 */           intersection++;
/*  41: 43 */           pairs2.remove(j);
/*  42: 44 */           break;
/*  43:    */         }
/*  44:    */       }
/*  45:    */     }
/*  46: 48 */     return 2.0D * intersection / union;
/*  47:    */   }
/*  48:    */   
/*  49:    */   private static ArrayList wordLetterPairs(String str)
/*  50:    */   {
/*  51: 55 */     ArrayList allPairs = new ArrayList();
/*  52:    */     
/*  53: 57 */     String[] words = str.split("s");
/*  54: 59 */     for (int w = 0; w < words.length; w++)
/*  55:    */     {
/*  56: 61 */       String[] pairsInWord = letterPairs(words[w]);
/*  57: 62 */       allPairs.addAll(Arrays.asList(pairsInWord));
/*  58:    */     }
/*  59: 67 */     return allPairs;
/*  60:    */   }
/*  61:    */   
/*  62:    */   private static String[] letterPairs(String str)
/*  63:    */   {
/*  64: 74 */     int numPairs = str.length() - 1;
/*  65: 75 */     String[] pairs = new String[numPairs];
/*  66: 76 */     for (int i = 0; i < numPairs; i++) {
/*  67: 77 */       pairs[i] = str.substring(i, i + 2);
/*  68:    */     }
/*  69: 79 */     return pairs;
/*  70:    */   }
/*  71:    */   
/*  72:    */   private static Map search(String keyword, List<Map> list, String key)
/*  73:    */   {
/*  74: 84 */     double bestGuess = 0.0D;
/*  75: 85 */     Map synonym = null;
/*  76: 86 */     for (int i = 0; i < list.size(); i++) {
/*  77: 88 */       if (key.equalsIgnoreCase("tags"))
/*  78:    */       {
/*  79: 89 */         if ((((Map)list.get(i)).get(key) != null) && (!((Map)list.get(i)).get(key).toString().equals(""))) {
/*  80: 93 */           for (String tag : ((Map)list.get(i)).get(key).toString().split(","))
/*  81:    */           {
/*  82: 94 */             double result = compareStrings(keyword, tag);
/*  83: 95 */             if ((result > bestGuess) && (result > 0.4D))
/*  84:    */             {
/*  85: 96 */               bestGuess = result;
/*  86: 97 */               synonym = (Map)list.get(i);
/*  87:    */             }
/*  88:    */           }
/*  89:    */         }
/*  90:    */       }
/*  91:    */       else
/*  92:    */       {
/*  93:101 */         double result = compareStrings(keyword, ((Map)list.get(i)).get(key).toString());
/*  94:102 */         if ((result > bestGuess) && (result > 0.4D))
/*  95:    */         {
/*  96:103 */           bestGuess = result;
/*  97:104 */           synonym = (Map)list.get(i);
/*  98:    */         }
/*  99:    */       }
/* 100:    */     }
/* 101:108 */     return synonym;
/* 102:    */   }
/* 103:    */   
/* 104:    */   public static UserService wordMatch(String keyword, String smsc, String shortCode)
/* 105:    */     throws Exception
/* 106:    */   {
/* 107:113 */     Map synonym = null;
/* 108:114 */     List<Map> services = new ArrayList();
/* 109:115 */     Connection conn = null;
/* 110:116 */     Statement stmt = null;
/* 111:117 */     ResultSet rs = null;
/* 112:    */     try
/* 113:    */     {
/* 114:119 */       conn = DConnect.getConnection();
/* 115:122 */       if ((smsc.endsWith("2")) || (smsc.endsWith("1"))) {
/* 116:123 */         smsc = smsc.substring(0, smsc.length() - 1);
/* 117:    */       }
/* 118:126 */       String query = "select * from service_definition sd INNER JOIN cp_connections cp ON sd.account_id = cp.list_id where sd.service_type='14' AND cp.conn_id like '" + smsc + "%' " + "and (sd.allowed_shortcodes='' or  sd.allowed_shortcodes like '%" + shortCode + "%')";
/* 119:    */       
/* 120:    */ 
/* 121:    */ 
/* 122:130 */       System.out.println(new Date() + ": " + KeywordMatcher.class + ": Will run query: " + query);
/* 123:131 */       stmt = conn.createStatement();
/* 124:132 */       rs = stmt.executeQuery(query);
/* 125:    */       
/* 126:    */ 
/* 127:    */ 
/* 128:136 */       System.out.println(new Date() + ": " + KeywordMatcher.class + ": Sucessfully run query. Will add result to list");
/* 129:137 */       while (rs.next())
/* 130:    */       {
/* 131:138 */         Map map = new HashMap();
/* 132:139 */         map.put("keyword", rs.getString("keyword"));
/* 133:140 */         map.put("service_type", rs.getString("service_type"));
/* 134:141 */         map.put("account_id", rs.getString("account_id"));
/* 135:142 */         map.put("service_name", rs.getString("service_name"));
/* 136:143 */         map.put("tags", rs.getString("tags"));
/* 137:144 */         map.put("default_message", rs.getString("default_message"));
/* 138:145 */         map.put("last_updated", rs.getDate("last_updated"));
/* 139:146 */         map.put("command", rs.getString("command"));
/* 140:147 */         map.put("allowed_shortcodes", rs.getString("allowed_shortcodes"));
/* 141:148 */         map.put("allowed_site_types", rs.getString("allowed_site_types"));
/* 142:149 */         map.put("pricing", rs.getString("pricing"));
/* 143:150 */         map.put("service_response_sender", rs.getString("service_response_sender"));
/* 144:152 */         if (rs.getInt("is_basic") == 1) {
/* 145:153 */           map.put("is_basic", Boolean.valueOf(true));
/* 146:    */         } else {
/* 147:155 */           map.put("is_basic", Boolean.valueOf(false));
/* 148:    */         }
/* 149:158 */         if ((rs.getInt("is_subscription") == 1) || (rs.getString("is_subscription").equalsIgnoreCase("true"))) {
/* 150:159 */           map.put("is_subscription", Boolean.valueOf(true));
/* 151:    */         } else {
/* 152:161 */           map.put("is_subscription", Boolean.valueOf(false));
/* 153:    */         }
/* 154:164 */         services.add(map);
/* 155:    */       }
/* 156:167 */       System.out.println(new Date() + ": " + KeywordMatcher.class + ": Will search keywords for match");
/* 157:168 */       synonym = search(keyword, services, "keyword");
/* 158:170 */       if (synonym == null)
/* 159:    */       {
/* 160:171 */         System.out.println(new Date() + ": " + KeywordMatcher.class + ": Match not found will now search service name");
/* 161:172 */         search(keyword, services, "service_name");
/* 162:    */       }
/* 163:175 */       if (synonym == null)
/* 164:    */       {
/* 165:176 */         System.out.println(new Date() + ": " + KeywordMatcher.class + ": Match not found will now search tags");
/* 166:177 */         search(keyword, services, "tags");
/* 167:    */       }
/* 168:    */     }
/* 169:    */     catch (Exception e)
/* 170:    */     {
/* 171:181 */       System.out.println(new Date() + ": [keyMatcher[ERROR]]" + e.getMessage());
/* 172:182 */       throw new Exception(e.getMessage());
/* 173:    */     }
/* 174:    */     finally
/* 175:    */     {
/* 176:184 */       if (rs != null) {
/* 177:185 */         rs.close();
/* 178:    */       }
/* 179:187 */       if (stmt != null) {
/* 180:188 */         stmt.close();
/* 181:    */       }
/* 182:190 */       if (conn != null) {
/* 183:191 */         conn.close();
/* 184:    */       }
/* 185:    */     }
/* 186:196 */     if (synonym != null) {
/* 187:197 */       return new UserService(synonym.get("service_type").toString(), synonym.get("keyword").toString(), synonym.get("account_id").toString(), synonym.get("service_name").toString(), synonym.get("default_message").toString(), synonym.get("command").toString(), synonym.get("allowed_shortcodes").toString(), synonym.get("allowed_site_types").toString(), synonym.get("pricing").toString(), Boolean.getBoolean(synonym.get("is_basic").toString()), Boolean.getBoolean(synonym.get("is_subscription").toString()), synonym.get("service_response_sender").toString());
/* 188:    */     }
/* 189:203 */     return null;
/* 190:    */   }
/* 191:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.KeywordMatcher
 * JD-Core Version:    0.7.0.1
 */