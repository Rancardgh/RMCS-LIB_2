/*   1:    */ package com.rancard.util.payment;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import java.sql.Connection;
/*   5:    */ import java.sql.PreparedStatement;
/*   6:    */ import java.sql.ResultSet;
/*   7:    */ import java.sql.SQLException;
/*   8:    */ import java.util.ArrayList;
/*   9:    */ 
/*  10:    */ public class ePinDB
/*  11:    */ {
/*  12:    */   public ePin viewVoucher(String id)
/*  13:    */     throws Exception
/*  14:    */   {
/*  15: 30 */     ePin voucher = new ePin();
/*  16:    */     
/*  17: 32 */     ResultSet rs = null;
/*  18: 33 */     Connection con = null;
/*  19: 34 */     PreparedStatement prepstat = null;
/*  20:    */     try
/*  21:    */     {
/*  22: 37 */       new DConnect();con = DConnect.getConnection();
/*  23:    */       
/*  24: 39 */       String SQL = "select * from evoucher where PIN=" + id;
/*  25:    */       
/*  26: 41 */       prepstat = con.prepareStatement(SQL);
/*  27:    */       
/*  28: 43 */       rs = prepstat.executeQuery();
/*  29: 44 */       while (rs.next())
/*  30:    */       {
/*  31: 46 */         voucher.setProvider(rs.getString("list_id"));
/*  32: 47 */         voucher.setPin(rs.getString("PIN"));
/*  33: 48 */         voucher.setCurrentBalance(rs.getDouble("balance"));
/*  34: 49 */         voucher.setEValue(rs.getDouble("value"));
/*  35:    */       }
/*  36: 53 */       rs.close();
/*  37: 54 */       rs = null;
/*  38: 55 */       prepstat.close();
/*  39: 56 */       prepstat = null;
/*  40: 57 */       con.close();
/*  41: 58 */       con = null;
/*  42:    */     }
/*  43:    */     catch (Exception ex)
/*  44:    */     {
/*  45: 60 */       if (con != null)
/*  46:    */       {
/*  47: 61 */         con.close();
/*  48: 62 */         con = null;
/*  49:    */       }
/*  50: 64 */       throw new Exception(ex.getMessage());
/*  51:    */     }
/*  52:    */     finally
/*  53:    */     {
/*  54: 66 */       if (rs != null)
/*  55:    */       {
/*  56:    */         try
/*  57:    */         {
/*  58: 68 */           rs.close();
/*  59:    */         }
/*  60:    */         catch (SQLException e) {}
/*  61: 72 */         rs = null;
/*  62:    */       }
/*  63: 74 */       if (prepstat != null)
/*  64:    */       {
/*  65:    */         try
/*  66:    */         {
/*  67: 76 */           prepstat.close();
/*  68:    */         }
/*  69:    */         catch (SQLException e) {}
/*  70: 80 */         prepstat = null;
/*  71:    */       }
/*  72: 82 */       if (con != null)
/*  73:    */       {
/*  74:    */         try
/*  75:    */         {
/*  76: 84 */           con.close();
/*  77:    */         }
/*  78:    */         catch (SQLException e) {}
/*  79: 88 */         con = null;
/*  80:    */       }
/*  81:    */     }
/*  82: 92 */     return voucher;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public ArrayList viewVouchers(String listId)
/*  86:    */     throws Exception
/*  87:    */   {
/*  88: 96 */     ArrayList vouchers = new ArrayList();
/*  89:    */     
/*  90: 98 */     ResultSet rs = null;
/*  91: 99 */     Connection con = null;
/*  92:100 */     PreparedStatement prepstat = null;
/*  93:    */     try
/*  94:    */     {
/*  95:103 */       new DConnect();con = DConnect.getConnection();
/*  96:    */       
/*  97:105 */       String SQL = "select * from evoucher where list_id='" + listId + "'";
/*  98:    */       
/*  99:107 */       prepstat = con.prepareStatement(SQL);
/* 100:    */       
/* 101:109 */       rs = prepstat.executeQuery();
/* 102:110 */       while (rs.next())
/* 103:    */       {
/* 104:111 */         ePin voucher = new ePin();
/* 105:112 */         voucher.setProvider(rs.getString("list_id"));
/* 106:113 */         voucher.setPin(rs.getString("PIN"));
/* 107:114 */         voucher.setCurrentBalance(rs.getDouble("balance"));
/* 108:115 */         voucher.setEValue(rs.getDouble("value"));
/* 109:    */         
/* 110:117 */         vouchers.add(voucher);
/* 111:    */       }
/* 112:119 */       rs.close();
/* 113:120 */       rs = null;
/* 114:121 */       prepstat.close();
/* 115:122 */       prepstat = null;
/* 116:123 */       con.close();
/* 117:124 */       con = null;
/* 118:    */     }
/* 119:    */     catch (Exception ex)
/* 120:    */     {
/* 121:126 */       if (con != null)
/* 122:    */       {
/* 123:127 */         con.close();
/* 124:128 */         con = null;
/* 125:    */       }
/* 126:130 */       throw new Exception(ex.getMessage());
/* 127:    */     }
/* 128:    */     finally
/* 129:    */     {
/* 130:132 */       if (rs != null)
/* 131:    */       {
/* 132:    */         try
/* 133:    */         {
/* 134:134 */           rs.close();
/* 135:    */         }
/* 136:    */         catch (SQLException e) {}
/* 137:138 */         rs = null;
/* 138:    */       }
/* 139:140 */       if (prepstat != null)
/* 140:    */       {
/* 141:    */         try
/* 142:    */         {
/* 143:142 */           prepstat.close();
/* 144:    */         }
/* 145:    */         catch (SQLException e) {}
/* 146:146 */         prepstat = null;
/* 147:    */       }
/* 148:148 */       if (con != null)
/* 149:    */       {
/* 150:    */         try
/* 151:    */         {
/* 152:150 */           con.close();
/* 153:    */         }
/* 154:    */         catch (SQLException e) {}
/* 155:154 */         con = null;
/* 156:    */       }
/* 157:    */     }
/* 158:158 */     return vouchers;
/* 159:    */   }
/* 160:    */   
/* 161:    */   public void updateVoucher(String id, double balance)
/* 162:    */     throws Exception
/* 163:    */   {
/* 164:163 */     ePin voucher = new ePin();
/* 165:    */     
/* 166:    */ 
/* 167:166 */     ResultSet rs = null;
/* 168:167 */     Connection con = null;
/* 169:168 */     PreparedStatement prepstat = null;
/* 170:    */     try
/* 171:    */     {
/* 172:171 */       new DConnect();con = DConnect.getConnection();
/* 173:    */       
/* 174:173 */       String SQL = "update evoucher SET balance=" + balance + " where PIN=" + id;
/* 175:174 */       prepstat = con.prepareStatement(SQL);
/* 176:175 */       prepstat.execute(SQL);
/* 177:    */     }
/* 178:    */     catch (Exception ex)
/* 179:    */     {
/* 180:177 */       if (con != null)
/* 181:    */       {
/* 182:178 */         con.close();
/* 183:179 */         con = null;
/* 184:    */       }
/* 185:181 */       throw new Exception(ex.getMessage());
/* 186:    */     }
/* 187:    */     finally
/* 188:    */     {
/* 189:183 */       if (rs != null)
/* 190:    */       {
/* 191:    */         try
/* 192:    */         {
/* 193:185 */           rs.close();
/* 194:    */         }
/* 195:    */         catch (SQLException e) {}
/* 196:189 */         rs = null;
/* 197:    */       }
/* 198:191 */       if (prepstat != null)
/* 199:    */       {
/* 200:    */         try
/* 201:    */         {
/* 202:193 */           prepstat.close();
/* 203:    */         }
/* 204:    */         catch (SQLException e) {}
/* 205:197 */         prepstat = null;
/* 206:    */       }
/* 207:199 */       if (con != null)
/* 208:    */       {
/* 209:    */         try
/* 210:    */         {
/* 211:201 */           con.close();
/* 212:    */         }
/* 213:    */         catch (SQLException e) {}
/* 214:205 */         con = null;
/* 215:    */       }
/* 216:    */     }
/* 217:    */   }
/* 218:    */   
/* 219:    */   public void insertVoucher(String listId, String pin, double limit, double balance)
/* 220:    */     throws Exception
/* 221:    */   {
/* 222:213 */     ResultSet rs = null;
/* 223:214 */     Connection con = null;
/* 224:215 */     PreparedStatement prepstat = null;
/* 225:    */     try
/* 226:    */     {
/* 227:218 */       new DConnect();con = DConnect.getConnection();
/* 228:    */       
/* 229:220 */       String SQL = "INSERT into evoucher (list_id,pin,value,balance) values(?,?,?,?)";
/* 230:221 */       prepstat = con.prepareStatement(SQL);
/* 231:    */       
/* 232:223 */       prepstat.setString(1, listId);
/* 233:224 */       prepstat.setString(2, pin);
/* 234:225 */       prepstat.setDouble(3, limit);
/* 235:226 */       prepstat.setDouble(4, balance);
/* 236:    */       
/* 237:228 */       prepstat.execute(SQL);
/* 238:    */     }
/* 239:    */     catch (Exception ex)
/* 240:    */     {
/* 241:230 */       if (con != null)
/* 242:    */       {
/* 243:231 */         con.close();
/* 244:232 */         con = null;
/* 245:    */       }
/* 246:234 */       throw new Exception(ex.getMessage());
/* 247:    */     }
/* 248:    */     finally
/* 249:    */     {
/* 250:236 */       if (rs != null)
/* 251:    */       {
/* 252:    */         try
/* 253:    */         {
/* 254:238 */           rs.close();
/* 255:    */         }
/* 256:    */         catch (SQLException e) {}
/* 257:242 */         rs = null;
/* 258:    */       }
/* 259:244 */       if (prepstat != null)
/* 260:    */       {
/* 261:    */         try
/* 262:    */         {
/* 263:246 */           prepstat.close();
/* 264:    */         }
/* 265:    */         catch (SQLException e) {}
/* 266:250 */         prepstat = null;
/* 267:    */       }
/* 268:252 */       if (con != null)
/* 269:    */       {
/* 270:    */         try
/* 271:    */         {
/* 272:254 */           con.close();
/* 273:    */         }
/* 274:    */         catch (SQLException e) {}
/* 275:258 */         con = null;
/* 276:    */       }
/* 277:    */     }
/* 278:    */   }
/* 279:    */   
/* 280:    */   public static void deleteVoucher(String pin)
/* 281:    */     throws Exception
/* 282:    */   {
/* 283:265 */     ResultSet rs = null;
/* 284:266 */     Connection con = null;
/* 285:267 */     PreparedStatement prepstat = null;
/* 286:    */     try
/* 287:    */     {
/* 288:270 */       con = DConnect.getConnection();
/* 289:271 */       String query = "DELETE from evoucher WHERE pin=?";
/* 290:272 */       prepstat = con.prepareStatement(query);
/* 291:273 */       prepstat.setString(1, pin);
/* 292:274 */       prepstat.execute();
/* 293:    */     }
/* 294:    */     catch (Exception ex)
/* 295:    */     {
/* 296:276 */       if (con != null) {
/* 297:277 */         con.close();
/* 298:    */       }
/* 299:279 */       throw new Exception(ex.getMessage());
/* 300:    */     }
/* 301:281 */     if (con != null) {
/* 302:282 */       con.close();
/* 303:    */     }
/* 304:    */   }
/* 305:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.payment.ePinDB
 * JD-Core Version:    0.7.0.1
 */