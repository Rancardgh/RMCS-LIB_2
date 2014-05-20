/*   1:    */ package com.rancard.util;
/*   2:    */ 
/*   3:    */ import com.rancard.common.DConnect;
/*   4:    */ import com.rancard.common.Message;
/*   5:    */ import com.rancard.common.uidGen;
/*   6:    */ import java.io.PrintStream;
/*   7:    */ import java.sql.Connection;
/*   8:    */ import java.sql.PreparedStatement;
/*   9:    */ import java.util.HashMap;
/*  10:    */ import javax.servlet.ServletInputStream;
/*  11:    */ import javax.servlet.http.HttpServletRequest;
/*  12:    */ import jxl.Cell;
/*  13:    */ import jxl.CellType;
/*  14:    */ import jxl.Sheet;
/*  15:    */ import jxl.Workbook;
/*  16:    */ 
/*  17:    */ public class DataImport
/*  18:    */ {
/*  19:    */   public DataImport()
/*  20:    */   {
/*  21:    */     try
/*  22:    */     {
/*  23: 16 */       jbInit();
/*  24:    */     }
/*  25:    */     catch (Exception ex)
/*  26:    */     {
/*  27: 18 */       ex.printStackTrace();
/*  28:    */     }
/*  29:    */   }
/*  30:    */   
/*  31: 22 */   private HttpServletRequest request = null;
/*  32: 26 */   private String[] columnNames = null;
/*  33: 27 */   private HashMap fixedColumnValues = new HashMap();
/*  34: 28 */   private int[] requiredColumns = null;
/*  35: 32 */   private String tableName = null;
/*  36: 34 */   static Connection conn = null;
/*  37: 36 */   static PreparedStatement pst = null;
/*  38:    */   
/*  39:    */   public void setColumnNames(String[] columnNames)
/*  40:    */   {
/*  41: 39 */     this.columnNames = columnNames;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setRequest(HttpServletRequest request)
/*  45:    */   {
/*  46: 43 */     this.request = request;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setTableName(String tableName)
/*  50:    */   {
/*  51: 47 */     this.tableName = tableName;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void setRequiredColumns(int[] requiredColumns)
/*  55:    */   {
/*  56: 51 */     this.requiredColumns = requiredColumns;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void setFixedColumnValues(HashMap fixedColumnValues)
/*  60:    */   {
/*  61: 55 */     this.fixedColumnValues = fixedColumnValues;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public Message importdata()
/*  65:    */   {
/*  66: 60 */     Message reply = new Message();
/*  67:    */     try
/*  68:    */     {
/*  69: 62 */       ServletInputStream is = this.request.getInputStream();
/*  70: 63 */       byte[] junk = new byte[1024];
/*  71: 64 */       int bytesRead = 0;
/*  72:    */       
/*  73:    */ 
/*  74:    */ 
/*  75: 68 */       bytesRead = is.readLine(junk, 0, junk.length);
/*  76: 69 */       bytesRead = is.readLine(junk, 0, junk.length);
/*  77: 70 */       bytesRead = is.readLine(junk, 0, junk.length);
/*  78: 71 */       bytesRead = is.readLine(junk, 0, junk.length);
/*  79:    */       
/*  80:    */ 
/*  81: 74 */       Workbook workbook = Workbook.getWorkbook(is);
/*  82: 75 */       Sheet[] sheets = workbook.getSheets();
/*  83: 76 */       for (int index = 0; index < sheets.length; index++)
/*  84:    */       {
/*  85: 77 */         Sheet sheet = workbook.getSheet(index);
/*  86: 78 */         System.out.println(sheet.getName());
/*  87: 79 */         Cell cell = null;
/*  88:    */         
/*  89:    */ 
/*  90: 82 */         StringBuffer sql = new StringBuffer("INSERT INTO ").append(this.tableName).append("(");
/*  91:    */         
/*  92: 84 */         StringBuffer params = new StringBuffer("VALUES(");
/*  93: 85 */         int cols = this.columnNames.length;
/*  94: 86 */         for (int i = 0; i < cols; i++)
/*  95:    */         {
/*  96: 87 */           sql.append(this.columnNames[i]).append(",");
/*  97: 88 */           params.append("?,");
/*  98:    */         }
/*  99: 90 */         sql = sql.deleteCharAt(sql.length() - 1).append(")").append(params.deleteCharAt(params.length() - 1)).append(")");
/* 100:    */         
/* 101:    */ 
/* 102:    */ 
/* 103:    */ 
/* 104:    */ 
/* 105:    */ 
/* 106: 97 */         conn = DConnect.getConnection();
/* 107: 98 */         pst = conn.prepareStatement(sql.toString());
/* 108:    */         
/* 109:    */ 
/* 110:    */ 
/* 111:102 */         int rows = sheet.getRows();
/* 112:103 */         for (int i = 1; i < rows; i++)
/* 113:    */         {
/* 114:104 */           boolean isValid = true;
/* 115:106 */           for (int k = 0; k < this.requiredColumns.length; k++) {
/* 116:107 */             if ((sheet.getCell(this.requiredColumns[k], i) != null) && (sheet.getCell(this.requiredColumns[k], i).getType() == CellType.EMPTY))
/* 117:    */             {
/* 118:110 */               isValid = false;
/* 119:111 */               reply.setStatus(isValid);
/* 120:    */               
/* 121:113 */               String row = new Integer(i).toString();
/* 122:114 */               reply.setMessage(this.columnNames[this.requiredColumns[k]] + " is a required column. Row " + row + " of this column is empty");
/* 123:    */               
/* 124:    */ 
/* 125:    */ 
/* 126:118 */               return reply;
/* 127:    */             }
/* 128:    */           }
/* 129:122 */           if (isValid)
/* 130:    */           {
/* 131:124 */             pst.setString(1, uidGen.generateID(14));
/* 132:    */             
/* 133:    */ 
/* 134:127 */             int fixedColumnCount = 0;
/* 135:128 */             for (int j = 0; j < cols - 1; j++) {
/* 136:129 */               if (this.fixedColumnValues.containsKey(new Integer(j + 2)))
/* 137:    */               {
/* 138:130 */                 if (this.fixedColumnValues.get(new Integer(j + 2)).getClass().getName().equalsIgnoreCase("java.lang.String")) {
/* 139:133 */                   pst.setString(j + 2, (String)this.fixedColumnValues.get(new Integer(j + 2)));
/* 140:136 */                 } else if (this.fixedColumnValues.get(new Integer(j + 2)).getClass().getName().equalsIgnoreCase("java.util.Date")) {
/* 141:141 */                   pst.setDate(j + 2, new java.sql.Date(((java.util.Date)this.fixedColumnValues.get(new Integer(j + 2))).getTime()));
/* 142:    */                 }
/* 143:148 */                 fixedColumnCount += 1;
/* 144:    */               }
/* 145:    */               else
/* 146:    */               {
/* 147:151 */                 cell = sheet.getCell(j - fixedColumnCount, i);
/* 148:152 */                 pst.setString(j + 2, cell.getContents());
/* 149:    */               }
/* 150:    */             }
/* 151:    */           }
/* 152:157 */           pst.addBatch();
/* 153:    */         }
/* 154:159 */         pst.executeBatch();
/* 155:    */       }
/* 156:163 */       reply.setMessage("Upload Successful");
/* 157:164 */       reply.setStatus(true);
/* 158:165 */       workbook.close();
/* 159:    */     }
/* 160:    */     catch (Exception e)
/* 161:    */     {
/* 162:168 */       reply.setStatus(false);
/* 163:169 */       reply.setMessage(e.getMessage());
/* 164:    */     }
/* 165:    */     finally
/* 166:    */     {
/* 167:172 */       closeDB();
/* 168:    */     }
/* 169:175 */     return reply;
/* 170:    */   }
/* 171:    */   
/* 172:    */   private static void closeDB()
/* 173:    */   {
/* 174:    */     try
/* 175:    */     {
/* 176:180 */       if (pst != null)
/* 177:    */       {
/* 178:181 */         pst.close();
/* 179:182 */         pst = null;
/* 180:    */       }
/* 181:184 */       if (conn != null)
/* 182:    */       {
/* 183:185 */         conn.close();
/* 184:186 */         conn = null;
/* 185:    */       }
/* 186:    */     }
/* 187:    */     catch (Exception e) {}
/* 188:    */   }
/* 189:    */   
/* 190:    */   private void jbInit()
/* 191:    */     throws Exception
/* 192:    */   {}
/* 193:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.DataImport
 * JD-Core Version:    0.7.0.1
 */