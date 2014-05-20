/*   1:    */ package com.rancard.util;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ 
/*   6:    */ public class GsmCharset
/*   7:    */ {
/*   8: 40 */   private static final Short ESC_BYTE = new Short((short)27);
/*   9: 47 */   private static final short[] isoGsmMap = { 64, 163, 36, 165, 232, 233, 249, 236, 242, 199, 10, 216, 248, 13, 197, 229, 0, 95, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 198, 230, 223, 201, 32, 33, 34, 35, 164, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 161, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 196, 214, 209, 220, 167, 191, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 228, 246, 241, 252, 224 };
/*  10: 67 */   private static final short[][] extIsoGsmMap = { { 10, 12 }, { 20, 94 }, { 40, 123 }, { 41, 125 }, { 47, 92 }, { 60, 91 }, { 61, 126 }, { 62, 93 }, { 64, 124 }, { 101, 164 } };
/*  11:    */   
/*  12:    */   public static byte[] translateToGsm0338(String dataIso)
/*  13:    */   {
/*  14: 79 */     byte[] dataIsoBytes = dataIso.getBytes();
/*  15: 80 */     ArrayList dataGsm = new ArrayList();
/*  16: 82 */     for (int dataIndex = 0; dataIndex < dataIsoBytes.length; dataIndex++)
/*  17:    */     {
/*  18: 83 */       byte currentDataIso = dataIsoBytes[dataIndex];
/*  19:    */       
/*  20:    */ 
/*  21: 86 */       short currentDataGsm = findGsmChar(currentDataIso);
/*  22: 90 */       if (currentDataGsm == -1)
/*  23:    */       {
/*  24: 91 */         currentDataGsm = findExtGsmChar(currentDataIso);
/*  25: 95 */         if (currentDataGsm != -1) {
/*  26: 96 */           dataGsm.add(ESC_BYTE);
/*  27:    */         }
/*  28:    */       }
/*  29:100 */       dataGsm.add(new Short(currentDataGsm));
/*  30:    */     }
/*  31:103 */     Short[] dataGsmShortArray = (Short[])dataGsm.toArray(new Short[0]);
/*  32:104 */     return translateShortToByteArray(dataGsmShortArray);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static boolean isGsm0338(String dataIso)
/*  36:    */   {
/*  37:113 */     byte[] dataIsoBytes = dataIso.getBytes();
/*  38:115 */     for (int dataIndex = 0; dataIndex < dataIsoBytes.length; dataIndex++)
/*  39:    */     {
/*  40:116 */       byte currentDataIso = dataIsoBytes[dataIndex];
/*  41:    */       
/*  42:    */ 
/*  43:119 */       short currentDataGsm = findGsmChar(currentDataIso);
/*  44:123 */       if (currentDataGsm == -1)
/*  45:    */       {
/*  46:124 */         currentDataGsm = findExtGsmChar(currentDataIso);
/*  47:128 */         if (currentDataGsm == -1) {
/*  48:131 */           return false;
/*  49:    */         }
/*  50:    */       }
/*  51:    */     }
/*  52:138 */     return true;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static String translateToIso(byte[] dataGsm)
/*  56:    */   {
/*  57:147 */     ArrayList dataIso = new ArrayList();
/*  58:    */     
/*  59:149 */     boolean isEscape = false;
/*  60:150 */     for (int dataIndex = 0; dataIndex < dataGsm.length; dataIndex++)
/*  61:    */     {
/*  62:152 */       short currentDataGsm = (short)dataGsm[dataIndex];
/*  63:153 */       short currentDataIso = -1;
/*  64:155 */       if (currentDataGsm == ESC_BYTE.shortValue())
/*  65:    */       {
/*  66:156 */         isEscape = true;
/*  67:    */       }
/*  68:158 */       else if (!isEscape)
/*  69:    */       {
/*  70:159 */         currentDataIso = findIsoChar(currentDataGsm);
/*  71:160 */         dataIso.add(new Short(currentDataIso));
/*  72:    */       }
/*  73:    */       else
/*  74:    */       {
/*  75:163 */         currentDataIso = findExtIsoChar(currentDataGsm);
/*  76:164 */         dataIso.add(new Short(currentDataIso));
/*  77:165 */         isEscape = false;
/*  78:    */       }
/*  79:    */     }
/*  80:169 */     Short[] dataIsoShortArray = (Short[])dataIso.toArray(new Short[0]);
/*  81:170 */     byte[] dataIsoByteArray = translateShortToByteArray(dataIsoShortArray);
/*  82:171 */     return new String(dataIsoByteArray);
/*  83:    */   }
/*  84:    */   
/*  85:    */   private static short findGsmChar(byte isoChar)
/*  86:    */   {
/*  87:180 */     short gsmChar = -1;
/*  88:182 */     for (short mapIndex = 0; mapIndex < isoGsmMap.length; mapIndex = (short)(mapIndex + 1)) {
/*  89:183 */       if (isoGsmMap[mapIndex] == (short)isoChar)
/*  90:    */       {
/*  91:184 */         gsmChar = mapIndex;
/*  92:185 */         break;
/*  93:    */       }
/*  94:    */     }
/*  95:189 */     return gsmChar;
/*  96:    */   }
/*  97:    */   
/*  98:    */   private static short findExtGsmChar(byte isoChar)
/*  99:    */   {
/* 100:198 */     short gsmChar = -1;
/* 101:200 */     for (short mapIndex = 0; mapIndex < extIsoGsmMap.length; mapIndex = (short)(mapIndex + 1)) {
/* 102:201 */       if (extIsoGsmMap[mapIndex][1] == isoChar)
/* 103:    */       {
/* 104:202 */         gsmChar = extIsoGsmMap[mapIndex][0];
/* 105:203 */         break;
/* 106:    */       }
/* 107:    */     }
/* 108:207 */     return gsmChar;
/* 109:    */   }
/* 110:    */   
/* 111:    */   private static short findIsoChar(short gsmChar)
/* 112:    */   {
/* 113:216 */     short isoChar = -1;
/* 114:218 */     if (gsmChar < isoGsmMap.length) {
/* 115:219 */       isoChar = isoGsmMap[gsmChar];
/* 116:    */     }
/* 117:222 */     return isoChar;
/* 118:    */   }
/* 119:    */   
/* 120:    */   private static short findExtIsoChar(short gsmChar)
/* 121:    */   {
/* 122:231 */     short isoChar = -1;
/* 123:233 */     for (short mapIndex = 0; mapIndex < extIsoGsmMap.length; mapIndex = (short)(mapIndex + 1)) {
/* 124:234 */       if (extIsoGsmMap[mapIndex][0] == gsmChar)
/* 125:    */       {
/* 126:235 */         isoChar = extIsoGsmMap[mapIndex][1];
/* 127:236 */         break;
/* 128:    */       }
/* 129:    */     }
/* 130:240 */     return isoChar;
/* 131:    */   }
/* 132:    */   
/* 133:    */   private static byte[] translateShortToByteArray(Short[] shortArray)
/* 134:    */   {
/* 135:250 */     byte[] byteArrayResult = new byte[shortArray.length];
/* 136:252 */     for (int i = 0; i < shortArray.length; i++) {
/* 137:253 */       byteArrayResult[i] = ((byte)shortArray[i].shortValue());
/* 138:    */     }
/* 139:256 */     return byteArrayResult;
/* 140:    */   }
/* 141:    */   
/* 142:    */   public static void main(String[] args)
/* 143:    */   {
/* 144:263 */     if (args.length < 1)
/* 145:    */     {
/* 146:264 */       System.out.println("Usage: java com.kunilkuda.GsmCharset <string>");
/* 147:265 */       System.exit(1);
/* 148:    */     }
/* 149:268 */     byte[] data = translateToGsm0338(args[0]);
/* 150:269 */     System.out.println("isGsm0338: " + isGsm0338(args[0]));
/* 151:270 */     for (int i = 0; i < data.length; i++) {
/* 152:271 */       System.out.print(Integer.toString(data[i], 16) + " ");
/* 153:    */     }
/* 154:273 */     System.out.println();
/* 155:    */   }
/* 156:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.GsmCharset
 * JD-Core Version:    0.7.0.1
 */