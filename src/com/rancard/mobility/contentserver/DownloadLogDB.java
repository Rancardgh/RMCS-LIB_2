/*   1:    */ package com.rancard.mobility.contentserver;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.PreparedStatement;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ 
/*   9:    */ public abstract class DownloadLogDB
/*  10:    */ {
/*  11:    */   public DownloadLogDB()
/*  12:    */   {
/*  13:    */     try
/*  14:    */     {
/*  15: 22 */       jbInit();
/*  16:    */     }
/*  17:    */     catch (Exception ex)
/*  18:    */     {
/*  19: 24 */       ex.printStackTrace();
/*  20:    */     }
/*  21:    */   }
/*  22:    */   
/*  23:    */   public static void createLogItem(String ticketID, String id, String listID, String subscriberMSISDN, int phoneId, java.util.Date date, boolean downloadCompleted, String pin)
/*  24:    */     throws Exception
/*  25:    */   {
/*  26: 36 */     ResultSet rs = null;
/*  27: 37 */     Connection con = null;
/*  28: 38 */     PreparedStatement prepstat = null;
/*  29:    */     try
/*  30:    */     {
/*  31: 41 */       con = DConnect.getConnection();
/*  32: 42 */       String query = "INSERT into download_log(ticketId,id,list_id,subscriberMSISDN,phone_id,date_of_download,status,pin) values(?,?,?,?,?,?,?,?)";
/*  33:    */       
/*  34:    */ 
/*  35: 45 */       prepstat = con.prepareStatement(query);
/*  36: 46 */       prepstat.setString(1, ticketID);
/*  37: 47 */       prepstat.setString(2, id);
/*  38: 48 */       prepstat.setString(3, listID);
/*  39: 49 */       prepstat.setString(4, subscriberMSISDN);
/*  40: 50 */       prepstat.setInt(5, phoneId);
/*  41: 51 */       prepstat.setDate(6, new java.sql.Date(date.getTime()));
/*  42: 52 */       if (downloadCompleted) {
/*  43: 53 */         prepstat.setInt(7, 1);
/*  44:    */       } else {
/*  45: 55 */         prepstat.setInt(7, 0);
/*  46:    */       }
/*  47: 56 */       prepstat.setString(8, pin);
/*  48:    */       
/*  49: 58 */       prepstat.execute();
/*  50:    */     }
/*  51:    */     catch (Exception ex)
/*  52:    */     {
/*  53: 60 */       if (con != null) {
/*  54: 61 */         con.close();
/*  55:    */       }
/*  56: 62 */       throw new Exception(ex.getMessage());
/*  57:    */     }
/*  58: 64 */     if (con != null) {
/*  59: 65 */       con.close();
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   public static void updateLogItem(String ticketID, java.util.Date date, boolean downloadCompleted)
/*  64:    */     throws Exception
/*  65:    */   {
/*  66: 74 */     ResultSet rs = null;
/*  67: 75 */     Connection con = null;
/*  68: 76 */     PreparedStatement prepstat = null;
/*  69:    */     try
/*  70:    */     {
/*  71: 79 */       int status = downloadCompleted ? 1 : 0;
/*  72: 80 */       con = DConnect.getConnection();
/*  73: 81 */       String query = "UPDATE download_log SET date_of_download=?,status=? WHERE ticketId='" + ticketID + "'";
/*  74:    */       
/*  75: 83 */       prepstat = con.prepareStatement(query);
/*  76: 84 */       prepstat.setDate(1, new java.sql.Date(date.getTime()));
/*  77: 85 */       prepstat.setInt(2, status);
/*  78:    */       
/*  79: 87 */       prepstat.execute();
/*  80:    */     }
/*  81:    */     catch (Exception ex)
/*  82:    */     {
/*  83: 90 */       if (con != null) {
/*  84: 91 */         con.close();
/*  85:    */       }
/*  86: 92 */       throw new Exception(ex.getMessage());
/*  87:    */     }
/*  88: 94 */     if (con != null) {
/*  89: 95 */       con.close();
/*  90:    */     }
/*  91:    */   }
/*  92:    */   
/*  93:    */   public static void deleteLogItem(String ticketID)
/*  94:    */     throws Exception
/*  95:    */   {
/*  96:103 */     ResultSet rs = null;
/*  97:104 */     Connection con = null;
/*  98:105 */     PreparedStatement prepstat = null;
/*  99:    */     try
/* 100:    */     {
/* 101:108 */       con = DConnect.getConnection();
/* 102:109 */       String query = "DELETE from download_log WHERE ticketId=?";
/* 103:110 */       prepstat = con.prepareStatement(query);
/* 104:111 */       prepstat.setString(1, ticketID);
/* 105:112 */       prepstat.execute();
/* 106:    */     }
/* 107:    */     catch (Exception ex)
/* 108:    */     {
/* 109:114 */       if (con != null) {
/* 110:115 */         con.close();
/* 111:    */       }
/* 112:116 */       throw new Exception(ex.getMessage());
/* 113:    */     }
/* 114:118 */     if (con != null) {
/* 115:119 */       con.close();
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   public static Transaction viewLogItem(String ticketID)
/* 120:    */     throws Exception
/* 121:    */   {
/* 122:126 */     Transaction newBean = new Transaction();
/* 123:    */     
/* 124:128 */     ResultSet rs = null;
/* 125:129 */     Connection con = null;
/* 126:130 */     PreparedStatement prepstat = null;
/* 127:    */     try
/* 128:    */     {
/* 129:133 */       con = DConnect.getConnection();
/* 130:134 */       String query = "SELECT * from download_log WHERE ticketId=?";
/* 131:135 */       prepstat = con.prepareStatement(query);
/* 132:136 */       prepstat.setString(1, ticketID);
/* 133:137 */       rs = prepstat.executeQuery();
/* 134:139 */       while (rs.next())
/* 135:    */       {
/* 136:140 */         newBean.setTicketID(rs.getString("ticketid"));
/* 137:141 */         newBean.setID(rs.getString("id"));
/* 138:142 */         newBean.setListID(rs.getString("list_id"));
/* 139:143 */         newBean.setSubscriberMSISDN(rs.getString("subscriberMSISDN"));
/* 140:144 */         newBean.setPhoneId(rs.getString("phone_id"));
/* 141:146 */         if (rs.getInt("status") == 1) {
/* 142:147 */           newBean.setDownloadCompleted(true);
/* 143:    */         } else {
/* 144:149 */           newBean.setDownloadCompleted(false);
/* 145:    */         }
/* 146:    */       }
/* 147:    */     }
/* 148:    */     catch (Exception ex)
/* 149:    */     {
/* 150:152 */       if (con != null) {
/* 151:153 */         con.close();
/* 152:    */       }
/* 153:154 */       throw new Exception(ex.getMessage());
/* 154:    */     }
/* 155:156 */     if (con != null) {
/* 156:157 */       con.close();
/* 157:    */     }
/* 158:158 */     return newBean;
/* 159:    */   }
/* 160:    */   
/* 161:    */   public static ArrayList getMostDownloaded(int typeId)
/* 162:    */     throws Exception
/* 163:    */   {
/* 164:165 */     ContentItem newBean = new ContentItem();
/* 165:    */     
/* 166:167 */     ResultSet rs = null;
/* 167:168 */     Connection con = null;
/* 168:169 */     PreparedStatement prepstat = null;
/* 169:    */     ArrayList itemList;
/* 170:    */     try
/* 171:    */     {
/* 172:172 */       con = DConnect.getConnection();
/* 173:173 */       String query = "SELECT id, list_id from download_log";
/* 174:174 */       prepstat = con.prepareStatement(query);
/* 175:175 */       rs = prepstat.executeQuery();
/* 176:    */       
/* 177:177 */       int fetchSize = 0;
/* 178:178 */       while (rs.next()) {
/* 179:179 */         fetchSize++;
/* 180:    */       }
/* 181:181 */       rs.beforeFirst();
/* 182:182 */       itemList = new ArrayList(0);
/* 183:183 */       itemList.add(0, "");
/* 184:184 */       int[] itemCount = new int[fetchSize];
/* 185:185 */       for (int i = 0; i < itemCount.length; i++) {
/* 186:186 */         itemCount[i] = 0;
/* 187:    */       }
/* 188:189 */       while (rs.next())
/* 189:    */       {
/* 190:190 */         newBean = newBean.viewContentItem(rs.getString("id"), rs.getString("list_id"));
/* 191:191 */         for (int i = 0; i < fetchSize; i++)
/* 192:    */         {
/* 193:192 */           if ((itemList.get(i).equals("")) || ((!((ContentItem)itemList.get(i)).isEqualTo(newBean)) && (i == fetchSize - 1)))
/* 194:    */           {
/* 195:193 */             itemList.add(i, newBean);
/* 196:194 */             itemCount[itemList.indexOf(newBean)] += 1;
/* 197:195 */             break;
/* 198:    */           }
/* 199:197 */           if (((ContentItem)itemList.get(i)).isEqualTo(newBean))
/* 200:    */           {
/* 201:198 */             itemCount[i] += 1;
/* 202:199 */             break;
/* 203:    */           }
/* 204:    */         }
/* 205:    */       }
/* 206:203 */       itemList.remove("");
/* 207:204 */       ArrayList items = new ArrayList();
/* 208:205 */       double mean = 0.0D;
/* 209:206 */       double sum = 0.0D;
/* 210:207 */       for (int i = 0; i < itemList.size(); i++) {
/* 211:208 */         if (((ContentItem)itemList.get(i)).gettype().intValue() == typeId)
/* 212:    */         {
/* 213:209 */           StatStruct struct = new StatStruct((ContentItem)itemList.get(i), itemCount[i]);
/* 214:210 */           items.add(struct);
/* 215:211 */           sum += struct.count;
/* 216:    */         }
/* 217:    */       }
/* 218:214 */       if (sum != 0.0D) {
/* 219:215 */         mean = sum / items.size();
/* 220:    */       } else {
/* 221:217 */         mean = 0.0D;
/* 222:    */       }
/* 223:218 */       itemList = new ArrayList();
/* 224:219 */       for (int i = 0; i < items.size(); i++)
/* 225:    */       {
/* 226:220 */         int downloadCounter = ((StatStruct)items.get(i)).count;
/* 227:221 */         if (downloadCounter >= mean) {
/* 228:222 */           itemList.add(((StatStruct)items.get(i)).clb);
/* 229:    */         }
/* 230:    */       }
/* 231:    */     }
/* 232:    */     catch (Exception e)
/* 233:    */     {
/* 234:226 */       throw new Exception();
/* 235:    */     }
/* 236:228 */     return itemList;
/* 237:    */   }
/* 238:    */   
/* 239:    */   static class StatStruct
/* 240:    */   {
/* 241:    */     ContentItem clb;
/* 242:    */     int count;
/* 243:    */     
/* 244:    */     public StatStruct(ContentItem clb_new, int count_new)
/* 245:    */     {
/* 246:240 */       this.clb = clb_new;
/* 247:241 */       this.count = count_new;
/* 248:    */     }
/* 249:    */   }
/* 250:    */   
/* 251:    */   public static void main(String[] args)
/* 252:    */     throws Exception
/* 253:    */   {
/* 254:272 */     updateLogItem("34h1ju88d0", new Transaction().now(), true);
/* 255:    */   }
/* 256:    */   
/* 257:    */   private void jbInit()
/* 258:    */     throws Exception
/* 259:    */   {}
/* 260:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.DownloadLogDB
 * JD-Core Version:    0.7.0.1
 */