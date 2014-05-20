/*   1:    */ package com.rancard.util;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.OutputStreamWriter;
/*   5:    */ import java.io.PrintStream;
/*   6:    */ import java.util.regex.Matcher;
/*   7:    */ import java.util.regex.Pattern;
/*   8:    */ 
/*   9:    */ public class URLUTF8Encoder
/*  10:    */ {
/*  11: 15 */   static final String[] hex = { "%00", "%01", "%02", "%03", "%04", "%05", "%06", "%07", "%08", "%09", "%0a", "%0b", "%0c", "%0d", "%0e", "%0f", "%10", "%11", "%12", "%13", "%14", "%15", "%16", "%17", "%18", "%19", "%1a", "%1b", "%1c", "%1d", "%1e", "%1f", "%20", "%21", "%22", "%23", "%24", "%25", "%26", "%27", "%28", "%29", "%2a", "%2b", "%2c", "%2d", "%2e", "%2f", "%30", "%31", "%32", "%33", "%34", "%35", "%36", "%37", "%38", "%39", "%3a", "%3b", "%3c", "%3d", "%3e", "%3f", "%40", "%41", "%42", "%43", "%44", "%45", "%46", "%47", "%48", "%49", "%4a", "%4b", "%4c", "%4d", "%4e", "%4f", "%50", "%51", "%52", "%53", "%54", "%55", "%56", "%57", "%58", "%59", "%5a", "%5b", "%5c", "%5d", "%5e", "%5f", "%60", "%61", "%62", "%63", "%64", "%65", "%66", "%67", "%68", "%69", "%6a", "%6b", "%6c", "%6d", "%6e", "%6f", "%70", "%71", "%72", "%73", "%74", "%75", "%76", "%77", "%78", "%79", "%7a", "%7b", "%7c", "%7d", "%7e", "%7f", "%80", "%81", "%82", "%83", "%84", "%85", "%86", "%87", "%88", "%89", "%8a", "%8b", "%8c", "%8d", "%8e", "%8f", "%90", "%91", "%92", "%93", "%94", "%95", "%96", "%97", "%98", "%99", "%9a", "%9b", "%9c", "%9d", "%9e", "%9f", "%a0", "%a1", "%a2", "%a3", "%a4", "%a5", "%a6", "%a7", "%a8", "%a9", "%aa", "%ab", "%ac", "%ad", "%ae", "%af", "%b0", "%b1", "%b2", "%b3", "%b4", "%b5", "%b6", "%b7", "%b8", "%b9", "%ba", "%bb", "%bc", "%bd", "%be", "%bf", "%c0", "%c1", "%c2", "%c3", "%c4", "%c5", "%c6", "%c7", "%c8", "%c9", "%ca", "%cb", "%cc", "%cd", "%ce", "%cf", "%d0", "%d1", "%d2", "%d3", "%d4", "%d5", "%d6", "%d7", "%d8", "%d9", "%da", "%db", "%dc", "%dd", "%de", "%df", "%e0", "%e1", "%e2", "%e3", "%e4", "%e5", "%e6", "%e7", "%e8", "%e9", "%ea", "%eb", "%ec", "%ed", "%ee", "%ef", "%f0", "%f1", "%f2", "%f3", "%f4", "%f5", "%f6", "%f7", "%f8", "%f9", "%fa", "%fb", "%fc", "%fd", "%fe", "%ff" };
/*  12:    */   
/*  13:    */   public static String encode(String s)
/*  14:    */   {
/*  15: 76 */     StringBuffer sbuf = new StringBuffer();
/*  16: 77 */     int len = s.length();
/*  17: 78 */     for (int i = 0; i < len; i++)
/*  18:    */     {
/*  19: 79 */       int ch = s.charAt(i);
/*  20: 80 */       if ((65 <= ch) && (ch <= 90))
/*  21:    */       {
/*  22: 81 */         sbuf.append((char)ch);
/*  23:    */       }
/*  24: 82 */       else if ((97 <= ch) && (ch <= 122))
/*  25:    */       {
/*  26: 83 */         sbuf.append((char)ch);
/*  27:    */       }
/*  28: 84 */       else if ((48 <= ch) && (ch <= 57))
/*  29:    */       {
/*  30: 85 */         sbuf.append((char)ch);
/*  31:    */       }
/*  32: 86 */       else if (ch == 32)
/*  33:    */       {
/*  34: 87 */         sbuf.append('+');
/*  35:    */       }
/*  36: 88 */       else if ((ch == 45) || (ch == 95) || (ch == 46) || (ch == 33) || (ch == 126) || (ch == 42) || (ch == 39) || (ch == 40) || (ch == 41))
/*  37:    */       {
/*  38: 93 */         sbuf.append((char)ch);
/*  39:    */       }
/*  40: 94 */       else if (ch <= 127)
/*  41:    */       {
/*  42: 95 */         sbuf.append(hex[ch]);
/*  43:    */       }
/*  44: 96 */       else if (ch <= 2047)
/*  45:    */       {
/*  46: 97 */         sbuf.append(hex[(0xC0 | ch >> 6)]);
/*  47: 98 */         sbuf.append(hex[(0x80 | ch & 0x3F)]);
/*  48:    */       }
/*  49:    */       else
/*  50:    */       {
/*  51:100 */         sbuf.append(hex[(0xE0 | ch >> 12)]);
/*  52:101 */         sbuf.append(hex[(0x80 | ch >> 6 & 0x3F)]);
/*  53:102 */         sbuf.append(hex[(0x80 | ch & 0x3F)]);
/*  54:    */       }
/*  55:    */     }
/*  56:105 */     return sbuf.toString();
/*  57:    */   }
/*  58:    */   
/*  59:    */   public static String doMessageEscaping(String insertions, String stored_ack)
/*  60:    */     throws Exception
/*  61:    */   {
/*  62:109 */     String url_tmp = stored_ack;
/*  63:110 */     String result = "";
/*  64:111 */     String tmp_1_val = "";
/*  65:    */     
/*  66:113 */     String[] query = insertions.split("&");
/*  67:115 */     for (int i = 0; i < query.length; i++)
/*  68:    */     {
/*  69:116 */       String[] tmp = query[i].split("=");
/*  70:117 */       Pattern pattern = Pattern.compile("@@" + tmp[0] + "@@");
/*  71:118 */       Matcher matcher = pattern.matcher(url_tmp);
/*  72:    */       try
/*  73:    */       {
/*  74:120 */         tmp_1_val = tmp[1];
/*  75:    */       }
/*  76:    */       catch (ArrayIndexOutOfBoundsException a)
/*  77:    */       {
/*  78:122 */         tmp_1_val = "";
/*  79:    */       }
/*  80:124 */       url_tmp = matcher.replaceAll(tmp_1_val);
/*  81:    */     }
/*  82:126 */     result = url_tmp;
/*  83:    */     
/*  84:    */ 
/*  85:129 */     return result;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public static String doURLEscaping(String request_query_string, String stored_url)
/*  89:    */     throws Exception
/*  90:    */   {
/*  91:133 */     String url_tmp = stored_url;
/*  92:134 */     String result = "";
/*  93:135 */     String tmp_1_val = "";
/*  94:136 */     if (stored_url.indexOf("?") != -1)
/*  95:    */     {
/*  96:137 */       String[] query = request_query_string.split("&");
/*  97:139 */       for (int i = 0; i < query.length; i++)
/*  98:    */       {
/*  99:140 */         String[] tmp = query[i].split("=");
/* 100:    */         
/* 101:    */ 
/* 102:143 */         Pattern pattern = Pattern.compile("@@" + tmp[0] + "@@");
/* 103:144 */         Matcher matcher = pattern.matcher(url_tmp);
/* 104:    */         try
/* 105:    */         {
/* 106:146 */           tmp_1_val = tmp[1];
/* 107:    */         }
/* 108:    */         catch (ArrayIndexOutOfBoundsException a)
/* 109:    */         {
/* 110:148 */           tmp_1_val = "";
/* 111:    */         }
/* 112:150 */         url_tmp = matcher.replaceAll(tmp_1_val);
/* 113:    */       }
/* 114:152 */       result = url_tmp;
/* 115:    */     }
/* 116:    */     else
/* 117:    */     {
/* 118:154 */       result = stored_url;
/* 119:    */     }
/* 120:155 */     return result;
/* 121:    */   }
/* 122:    */   
/* 123:    */   public static String removeMalformedChars(String malformedString)
/* 124:    */   {
/* 125:159 */     System.out.println("Replacing malformed characters from " + malformedString + " ...");
/* 126:160 */     String wellFormedString = "";
/* 127:    */     
/* 128:162 */     malformedString = malformedString.replaceAll("â€œ", "\"");
/* 129:163 */     malformedString = malformedString.replaceAll("â€¢", "-");
/* 130:164 */     malformedString = malformedString.replaceAll("â€“", "–");
/* 131:    */     
/* 132:166 */     malformedString = malformedString.replaceAll("Ã‰", "É");
/* 133:167 */     malformedString = malformedString.replaceAll("â€", "\"");
/* 134:168 */     malformedString = malformedString.replaceAll("Ã‡", "Ç");
/* 135:169 */     malformedString = malformedString.replaceAll("Ãƒ", "Ã");
/* 136:170 */     malformedString = malformedString.replaceAll("Ãº", "ú");
/* 137:171 */     malformedString = malformedString.replaceAll("Ã˜", "Ø");
/* 138:172 */     malformedString = malformedString.replaceAll("Ã¨", "è");
/* 139:173 */     malformedString = malformedString.replaceAll("Ãµ", "õ");
/* 140:174 */     malformedString = malformedString.replaceAll("Ã­", "í");
/* 141:175 */     malformedString = malformedString.replaceAll("Ã¢", "â");
/* 142:176 */     malformedString = malformedString.replaceAll("Ã£", "ã");
/* 143:177 */     malformedString = malformedString.replaceAll("Ãª", "ê");
/* 144:178 */     malformedString = malformedString.replaceAll("Ã¡", "á");
/* 145:179 */     malformedString = malformedString.replaceAll("Ã©", "é");
/* 146:180 */     malformedString = malformedString.replaceAll("Ã³", "ó");
/* 147:181 */     malformedString = malformedString.replaceAll("Ã§", "ç");
/* 148:182 */     malformedString = malformedString.replaceAll("Âª", "ª");
/* 149:183 */     malformedString = malformedString.replaceAll("Âº", "º");
/* 150:    */     
/* 151:185 */     malformedString = malformedString.replaceAll("Ã¯", "ï");
/* 152:186 */     malformedString = malformedString.replaceAll("Ã\\?", "�?");
/* 153:    */     
/* 154:188 */     malformedString = malformedString.replaceAll("Ã", "à");
/* 155:189 */     malformedString = malformedString.replaceAll(". Ã", "À");
/* 156:191 */     if ((malformedString.length() > 0) && (malformedString.charAt(0) == 'à')) {
/* 157:192 */       malformedString = malformedString.replaceAll("à", "À");
/* 158:    */     }
/* 159:194 */     wellFormedString = malformedString;
/* 160:    */     
/* 161:196 */     System.out.println("Well-formed String: " + wellFormedString);
/* 162:    */     
/* 163:198 */     return wellFormedString;
/* 164:    */   }
/* 165:    */   
/* 166:    */   public static void main(String[] args)
/* 167:    */   {
/* 168:202 */     OutputStreamWriter out = new OutputStreamWriter(new ByteArrayOutputStream());
/* 169:203 */     System.out.println("Current Encoding:  " + out.getEncoding());
/* 170:204 */     System.out.println("Literal output:  ��ã�");
/* 171:    */   }
/* 172:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.util.URLUTF8Encoder
 * JD-Core Version:    0.7.0.1
 */