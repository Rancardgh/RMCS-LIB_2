/*   1:    */ package com.rancard.util;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ 
/*   6:    */ public class Validator
/*   7:    */ {
/*   8:  6 */   private static ArrayList validCharSet = new ArrayList();
/*   9:    */   
/*  10:    */   public static String stripLeadingPlus(String in)
/*  11:    */     throws Exception
/*  12:    */   {
/*  13: 14 */     int len = in.length();
/*  14: 15 */     char[] in_tokens = in.toCharArray();
/*  15: 17 */     if ((in_tokens[0] == '+') || (in_tokens[0] == ' '))
/*  16:    */     {
/*  17: 19 */       int lastPlusIndex = in.lastIndexOf("+");
/*  18: 20 */       in = in.substring(lastPlusIndex + 1, in.length());
/*  19: 21 */       in = new StringBuffer(in).deleteCharAt(in.indexOf(' ')).toString();
/*  20: 22 */       System.out.println("The parsed number is: " + in);
/*  21: 23 */       return in;
/*  22:    */     }
/*  23: 26 */     return in;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public static boolean isNumber(String n)
/*  27:    */     throws Exception
/*  28:    */   {
/*  29:    */     try
/*  30:    */     {
/*  31: 31 */       char[] tokens = n.toCharArray();
/*  32: 33 */       for (int i = 0; i < tokens.length; i++) {
/*  33: 34 */         if (!isCharValid(new Character(tokens[i]).toString(), validCharSet)) {
/*  34: 36 */           return false;
/*  35:    */         }
/*  36:    */       }
/*  37: 41 */       int lastPlusPosition = n.lastIndexOf("+");
/*  38: 43 */       if ((lastPlusPosition != 0) || (lastPlusPosition != 1)) {
/*  39: 44 */         for (int i = 0; i < lastPlusPosition; i++) {
/*  40: 45 */           if (tokens[i] != '+') {
/*  41: 46 */             return false;
/*  42:    */           }
/*  43:    */         }
/*  44:    */       }
/*  45: 52 */       return true;
/*  46:    */     }
/*  47:    */     catch (NumberFormatException e) {}
/*  48: 55 */     return false;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public static boolean isCharValid(String in, ArrayList validTokens)
/*  52:    */     throws Exception
/*  53:    */   {
/*  54: 62 */     if (validTokens.contains(in))
/*  55:    */     {
/*  56: 63 */       System.out.println("Tokens size " + validTokens.size());
/*  57: 64 */       System.out.println(in + " is a valid number");
/*  58: 65 */       return true;
/*  59:    */     }
/*  60: 67 */     System.out.println("Tokens size " + validTokens.size());
/*  61: 68 */     System.out.println(in + " is NOT a valid number");
/*  62: 69 */     return false;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public static boolean isLengthValid(String number, int length)
/*  66:    */     throws Exception
/*  67:    */   {
/*  68: 75 */     boolean state = false;
/*  69: 76 */     if (number.length() <= length) {
/*  70: 78 */       state = true;
/*  71:    */     } else {
/*  72: 81 */       state = false;
/*  73:    */     }
/*  74: 83 */     return state;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static ArrayList validateNumbers(String[] numbers, int numLen, String validTokenStr)
/*  78:    */     throws Exception
/*  79:    */   {
/*  80: 87 */     ArrayList all = new ArrayList();
/*  81: 88 */     String[] valids = new String[numbers.length];
/*  82: 89 */     String[] invalids = new String[numbers.length];
/*  83: 90 */     ArrayList valids_arrlist = new ArrayList();
/*  84: 91 */     String[] validTokens = buildValidCharsArray(validTokenStr);
/*  85: 94 */     for (int i = 0; i < validTokens.length; i++) {
/*  86: 95 */       valids_arrlist.add(validTokens[i]);
/*  87:    */     }
/*  88: 96 */     setValidCharSet(valids_arrlist);
/*  89: 99 */     for (int j = 0; j < numbers.length; j++)
/*  90:    */     {
/*  91:100 */       String number = numbers[j];
/*  92:102 */       if (isLengthValid(number, numLen))
/*  93:    */       {
/*  94:104 */         if (isNumber(number))
/*  95:    */         {
/*  96:105 */           number = stripLeadingPlus(number);
/*  97:106 */           valids[j] = number;
/*  98:    */         }
/*  99:    */         else
/* 100:    */         {
/* 101:109 */           invalids[j] = number;
/* 102:    */         }
/* 103:    */       }
/* 104:    */       else {
/* 105:113 */         invalids[j] = number;
/* 106:    */       }
/* 107:    */     }
/* 108:117 */     String reply = buildMultipleSendResultMessage(valids, invalids);
/* 109:    */     
/* 110:119 */     all.add(valids);
/* 111:120 */     all.add(invalids);
/* 112:121 */     all.add(reply);
/* 113:    */     
/* 114:123 */     return all;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public static String buildMultipleSendResultMessage(String[] valids, String[] invalids)
/* 118:    */     throws Exception
/* 119:    */   {
/* 120:129 */     String reply = "";
/* 121:130 */     if (valids.length != 0) {
/* 122:131 */       for (int i = 0; i < valids.length; i++) {
/* 123:132 */         if (valids[i] != null) {
/* 124:133 */           reply = reply + "OK: " + valids[i] + "\n";
/* 125:    */         }
/* 126:    */       }
/* 127:    */     }
/* 128:137 */     if (invalids.length != 0) {
/* 129:138 */       for (int i = 0; i < invalids.length; i++) {
/* 130:139 */         if (invalids[i] != null) {
/* 131:140 */           reply = reply + "ERROR: 101t to " + invalids[i] + "\n";
/* 132:    */         }
/* 133:    */       }
/* 134:    */     }
/* 135:145 */     return reply;
/* 136:    */   }
/* 137:    */   
/* 138:    */   public static String[] buildValidCharsArray(String in)
/* 139:    */     throws Exception
/* 140:    */   {
/* 141:148 */     String[] out = new String[in.length()];
/* 142:149 */     char[] tmp = in.toCharArray();
/* 143:150 */     for (int i = 0; i < tmp.length; i++) {
/* 144:151 */       out[i] = new Character(tmp[i]).toString();
/* 145:    */     }
/* 146:154 */     return out;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public static boolean isSpacefonNumber(String n)
/* 150:    */   {
/* 151:158 */     boolean state = false;
/* 152:    */     try
/* 153:    */     {
/* 154:160 */       if (n.length() > 10)
/* 155:    */       {
/* 156:161 */         state = false;
/* 157:162 */         throw new Exception("The number is invalid: It is longer than 10 characters! Enter an Areeba number of 10 characters only!");
/* 158:    */       }
/* 159:163 */       if (n.length() == 10)
/* 160:    */       {
/* 161:164 */         String firstThree = n.substring(0, 3);
/* 162:165 */         System.out.println("Number: " + firstThree);
/* 163:166 */         if (firstThree.equals("024"))
/* 164:    */         {
/* 165:167 */           state = true;
/* 166:    */         }
/* 167:    */         else
/* 168:    */         {
/* 169:169 */           state = false;
/* 170:170 */           throw new Exception("The number is invalid. Enter an Areeba number of the form 0244954413 or 0243954413");
/* 171:    */         }
/* 172:    */       }
/* 173:    */     }
/* 174:    */     catch (Exception e)
/* 175:    */     {
/* 176:175 */       System.out.println(e.getMessage());
/* 177:    */     }
/* 178:177 */     return state;
/* 179:    */   }
/* 180:    */   
/* 181:    */   public static String formatMoney(double value)
/* 182:    */   {
/* 183:184 */     Money amt1 = new Money(value);
/* 184:185 */     return amt1.toString();
/* 185:    */   }
/* 186:    */   
/* 187:    */   public static void setValidCharSet(ArrayList validCharset)
/* 188:    */   {
/* 189:190 */     validCharSet = validCharset;
/* 190:    */   }
/* 191:    */   
/* 192:    */   public ArrayList getValidCharSet()
/* 193:    */   {
/* 194:194 */     return validCharSet;
/* 195:    */   }
/* 196:    */   
/* 197:    */   public static boolean isNumberNoValidChars(String in)
/* 198:    */     throws NumberFormatException
/* 199:    */   {
/* 200:    */     try
/* 201:    */     {
/* 202:199 */       long tmp = Long.parseLong(in);
/* 203:200 */       return true;
/* 204:    */     }
/* 205:    */     catch (NumberFormatException e) {}
/* 206:202 */     return false;
/* 207:    */   }
/* 208:    */   
/* 209:    */   public static void main(String[] args)
/* 210:    */     throws Exception
/* 211:    */   {
/* 212:208 */     System.out.println(isNumber("233276176997"));
/* 213:    */   }
/* 214:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.util.Validator
 * JD-Core Version:    0.7.0.1
 */