/*   1:    */ package com.rancard.mobility.contentserver.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.mobility.common.ThreadedMessageSender;
/*   4:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   5:    */ import com.rancard.mobility.infoserver.common.services.ServiceManager;
/*   6:    */ import com.rancard.mobility.infoserver.common.services.UserService;
/*   7:    */ import java.io.IOException;
/*   8:    */ import java.io.PrintStream;
/*   9:    */ import java.util.ArrayList;
/*  10:    */ import java.util.Arrays;
/*  11:    */ import java.util.Date;
/*  12:    */ import java.util.List;
/*  13:    */ import javax.servlet.Filter;
/*  14:    */ import javax.servlet.FilterChain;
/*  15:    */ import javax.servlet.FilterConfig;
/*  16:    */ import javax.servlet.ServletException;
/*  17:    */ import javax.servlet.ServletRequest;
/*  18:    */ import javax.servlet.ServletResponse;
/*  19:    */ import javax.servlet.http.HttpServlet;
/*  20:    */ import javax.servlet.http.HttpServletRequest;
/*  21:    */ 
/*  22:    */ public class processnewsubscription
/*  23:    */   extends HttpServlet
/*  24:    */   implements Filter
/*  25:    */ {
/*  26: 22 */   private FilterConfig filterConfig = null;
/*  27:    */   
/*  28:    */   public void init(FilterConfig filterConfig)
/*  29:    */   {
/*  30: 25 */     this.filterConfig = filterConfig;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
/*  34:    */     throws IOException, ServletException
/*  35:    */   {
/*  36: 31 */     HttpServletRequest request = (HttpServletRequest)req;
/*  37: 32 */     String keyword = req.getParameter("keyword");
/*  38: 33 */     String dest = req.getParameter("dest");
/*  39: 34 */     String short_code = dest.contains("+") ? dest.substring(1) : dest;
/*  40: 35 */     String msisdn = req.getParameter("msisdn");
/*  41: 36 */     String account_id = (String)req.getAttribute("acctId");
/*  42:    */     
/*  43: 38 */     System.out.println(new Date() + ": " + keyword + ": " + msisdn + ": Entering processnewsubscription filter...");
/*  44:    */     
/*  45:    */ 
/*  46: 41 */     String alt_keyword = "";
/*  47: 42 */     if ((keyword.equalsIgnoreCase("IN")) && (account_id.equals("005"))) {
/*  48: 43 */       alt_keyword = "VOI";
/*  49: 44 */     } else if ((keyword.equalsIgnoreCase("AN")) && (account_id.equals("005"))) {
/*  50: 45 */       alt_keyword = "VOA";
/*  51:    */     }
/*  52: 47 */     request.setAttribute("override_keyword", alt_keyword);
/*  53:    */     
/*  54: 49 */     UserService srvc = null;
/*  55:    */     try
/*  56:    */     {
/*  57: 52 */       srvc = ServiceManager.viewService(keyword, account_id);
/*  58: 54 */       if (srvc.isSubscription())
/*  59:    */       {
/*  60: 56 */         System.out.println(new Date() + ": " + keyword + ": " + msisdn + ": Keyword is a valid subscription service");
/*  61:    */         
/*  62: 58 */         String service_name = srvc.getServiceName();
/*  63: 59 */         String service_keyword = srvc.getKeyword();
/*  64:    */         
/*  65: 61 */         ArrayList<String> sportsKeywords = new ArrayList();
/*  66: 62 */         sportsKeywords.add("FOOTBALL");
/*  67: 63 */         sportsKeywords.add("FB");
/*  68: 64 */         sportsKeywords.add("GF");
/*  69: 65 */         sportsKeywords.add("SR");
/*  70: 66 */         sportsKeywords.add("ESPN");
/*  71:    */         
/*  72: 68 */         boolean isSubscribedToOrig = ServiceManager.isSubscribed(msisdn, account_id, service_keyword);
/*  73: 69 */         boolean isSubscribedtoAlt = ServiceManager.isSubscribed(msisdn, account_id, alt_keyword);
/*  74: 71 */         if ((isSubscribedToOrig) || (isSubscribedtoAlt))
/*  75:    */         {
/*  76: 73 */           if (isSubscribedtoAlt)
/*  77:    */           {
/*  78: 74 */             service_keyword = alt_keyword;
/*  79: 75 */             System.out.println(new Date() + ": " + service_keyword + ": " + msisdn + ": Subscription keyword changed");
/*  80:    */           }
/*  81: 79 */           System.out.println(new Date() + ": " + service_keyword + ": " + msisdn + ": Subscription already exists in DB");
/*  82: 80 */           String unsubscribe_msg = "";
/*  83: 81 */           String response_sender = "";
/*  84: 83 */           if (account_id.equals("005"))
/*  85:    */           {
/*  86: 84 */             if ((short_code.equals("406")) || (sportsKeywords.contains(service_keyword.toUpperCase())))
/*  87:    */             {
/*  88: 85 */               if (service_keyword.equalsIgnoreCase("SR")) {
/*  89: 86 */                 unsubscribe_msg = "The latest " + service_name + " summaries for only GHC0.03 daily. To unsubscribe send STOP " + service_keyword.toUpperCase() + " to " + short_code;
/*  90:    */               } else {
/*  91: 88 */                 unsubscribe_msg = "The latest Tigo LiveScore " + service_keyword.toUpperCase() + " news for only GHC0.0795 daily. To unsubscribe send STOP " + service_keyword.toUpperCase() + " to 406.";
/*  92:    */               }
/*  93: 90 */               response_sender = "406";
/*  94:    */             }
/*  95: 91 */             else if (short_code.equals("801"))
/*  96:    */             {
/*  97: 92 */               if (service_keyword.equalsIgnoreCase("VOI")) {
/*  98: 93 */                 unsubscribe_msg = "The latest International News from Voice of America (VOA) for only GHC0.08 daily. To unsubscribe send STOP VOI to 801";
/*  99: 94 */               } else if (service_keyword.equalsIgnoreCase("VOA")) {
/* 100: 95 */                 unsubscribe_msg = "The latest African News from Voice of America (VOA) for only GHC0.08 daily. To unsubscribe send STOP VOA to 801";
/* 101: 96 */               } else if ((service_keyword.equalsIgnoreCase("BL")) || (service_keyword.equalsIgnoreCase("QU"))) {
/* 102: 97 */                 unsubscribe_msg = "The latest " + service_name + " summaries for only GHC0.03 daily. To unsubscribe send STOP " + service_keyword.toUpperCase() + " to " + short_code;
/* 103:    */               } else {
/* 104: 99 */                 unsubscribe_msg = "The latest " + service_name + " summaries for only GHC0.0636 daily. To unsubscribe send STOP " + service_keyword.toUpperCase() + " to " + short_code;
/* 105:    */               }
/* 106:101 */               response_sender = "801";
/* 107:    */             }
/* 108:    */           }
/* 109:103 */           else if (account_id.equals("049")) {
/* 110:104 */             unsubscribe_msg = "The latest " + service_name + " summaries for only Le 90 daily. To unsubscribe send STOP " + service_keyword.toUpperCase() + " to " + short_code;
/* 111:    */           }
/* 112:106 */           request.setAttribute("override_msg", unsubscribe_msg);
/* 113:107 */           request.setAttribute("x-kannel-header-from", response_sender);
/* 114:    */         }
/* 115:    */         else
/* 116:    */         {
/* 117:110 */           System.out.println(new Date() + ": " + service_keyword + ": " + msisdn + ": Subscription doesn't exist. Creating new subscription record...");
/* 118:112 */           if (!alt_keyword.equals(""))
/* 119:    */           {
/* 120:113 */             service_keyword = alt_keyword;
/* 121:114 */             System.out.println(new Date() + ": " + service_keyword + ": " + msisdn + ": Subscription keyword changed");
/* 122:    */           }
/* 123:117 */           ArrayList<String> keywordList = new ArrayList();
/* 124:118 */           keywordList.add(service_keyword);
/* 125:    */           
/* 126:120 */           String welcome_msg = "";
/* 127:122 */           if (account_id.equals("005"))
/* 128:    */           {
/* 129:123 */             if ((short_code.equals("406")) || (sportsKeywords.contains(service_keyword.toUpperCase()))) {
/* 130:124 */               welcome_msg = "You can subscribe to receive daily " + service_keyword.toUpperCase() + " updates from Ghanasoccernet.com at a cost of GHC0.0795 per day. To do so, reply to this message with the word MORE";
/* 131:125 */             } else if (short_code.equals("801")) {
/* 132:126 */               if (service_keyword.equalsIgnoreCase("VOI")) {
/* 133:127 */                 welcome_msg = "You can subscribe to receive daily International News updates from VOA at a cost of GHC0.08 per day. To do so, reply to this message with the word MORE";
/* 134:128 */               } else if (service_keyword.equalsIgnoreCase("VOA")) {
/* 135:129 */                 welcome_msg = "You can subscribe to receive daily African News updates from VOA at a cost of GHC0.08 per day. To do so, reply to this message with the word MORE";
/* 136:    */               } else {
/* 137:131 */                 welcome_msg = "You can subscribe to receive daily " + service_name + " updates at a cost of GHC0.0636 per day. To do so, reply to this message with the word MORE";
/* 138:    */               }
/* 139:    */             }
/* 140:136 */             List<String> tempNonSubscriptionKeywords = Arrays.asList(new String[] { "gtv", "tv3", "welcomemet", "ta", "usd", "tva", "dst", "te", "gia", "gbp", "eur", "aa", "cfa", "nn", "kl", "gse", "ad", "sa", "lh", "az", "kq", "tn", "ttv", "et", "sla", "as", "ms", "af", "naa", "mea", "ai", "be", "ae", "tm", "td", "ba", "em", "va", "we" });
/* 141:    */             
/* 142:    */ 
/* 143:    */ 
/* 144:140 */             ArrayList<String> nonSubscriptionKeywords = new ArrayList(tempNonSubscriptionKeywords);
/* 145:142 */             if (nonSubscriptionKeywords.contains(service_keyword.toLowerCase()))
/* 146:    */             {
/* 147:143 */               welcome_msg = "To get the latest international news from the Voice of America (VOA) simply text INEWS to 801 and get daily news updates for only GHc0.08!";
/* 148:144 */               short_code = "801";
/* 149:    */             }
/* 150:148 */             if ((service_keyword.equalsIgnoreCase("USD")) || (service_keyword.equalsIgnoreCase("LN")))
/* 151:    */             {
/* 152:149 */               welcome_msg = "Get BBC News any where you are on your phone! Simply text NEWS to 1988 and get daily updates for only GHc0.10!";
/* 153:150 */               short_code = "1988";
/* 154:    */             }
/* 155:154 */             if ((service_keyword.equalsIgnoreCase("BT")) || (service_keyword.equalsIgnoreCase("BL")))
/* 156:    */             {
/* 157:155 */               welcome_msg = "Are you downcast and need spiritual motivation? Simply text WORD to 801 and get daily Bible verses for only GHc0.0636!";
/* 158:156 */               short_code = "801";
/* 159:    */             }
/* 160:160 */             if (service_keyword.equalsIgnoreCase("CN"))
/* 161:    */             {
/* 162:161 */               welcome_msg = "Do you want to know what your favorite celebrities have been up to? Simply text CELEB  to 801 and get daily updates for only GHc0.0636!";
/* 163:162 */               short_code = "801";
/* 164:    */             }
/* 165:166 */             if ((service_keyword.equalsIgnoreCase("GTV")) || (service_keyword.equalsIgnoreCase("TV3")) || (service_keyword.equalsIgnoreCase("MET")) || (service_keyword.equalsIgnoreCase("DST")) || (service_keyword.equalsIgnoreCase("TE")) || (service_keyword.equalsIgnoreCase("FB")) || (service_keyword.equalsIgnoreCase("BA")) || (service_keyword.equalsIgnoreCase("IS")))
/* 166:    */             {
/* 167:167 */               welcome_msg = "Get BBC Sports any where you are on your phone! Simply text FBALL to 1988 and get daily updates for only GHc0.10!";
/* 168:168 */               short_code = "1988";
/* 169:    */             }
/* 170:172 */             if (service_keyword.equalsIgnoreCase("JO"))
/* 171:    */             {
/* 172:173 */               welcome_msg = "Do you want to hear something funny? Simply text JOKE to 801 and get daily jokes for only GHc0.0636!";
/* 173:174 */               short_code = "801";
/* 174:    */             }
/* 175:178 */             if (service_keyword.equalsIgnoreCase("MN"))
/* 176:    */             {
/* 177:179 */               welcome_msg = "Do you want fresh local and international music news delivered straight to your phone? Simply text MUSIC to 801 and get daily updates for only GHc0.0636!";
/* 178:180 */               short_code = "801";
/* 179:    */             }
/* 180:183 */             if (service_keyword.equalsIgnoreCase("ESPN"))
/* 181:    */             {
/* 182:184 */               welcome_msg = "You're now subscribed to the ESPN service on Tigo! You'll get sports news from ESPN for only GHp 8 daily! To unsubscribe, send STOP ESPN to 406!";
/* 183:185 */               short_code = "406";
/* 184:    */               
/* 185:187 */               ServiceManager.subscribeToService(msisdn, keywordList, account_id, 1, 1, 1);
/* 186:    */             }
/* 187:    */           }
/* 188:193 */           else if (account_id.equals("000"))
/* 189:    */           {
/* 190:195 */             welcome_msg = "Get BBC Sports any where you are on your phone! Simply text FBALL to 1988 and get daily updates for only GHc0.10!";
/* 191:196 */             short_code = "1988";
/* 192:    */           }
/* 193:200 */           CPConnections cnxn = new CPConnections();
/* 194:201 */           if (account_id.equals("005"))
/* 195:    */           {
/* 196:203 */             cnxn = CPConnections.getConnection(account_id, msisdn, "kannel");
/* 197:    */             
/* 198:    */ 
/* 199:206 */             new Thread(new ThreadedMessageSender(cnxn, msisdn, short_code, welcome_msg, "", 45000)).start();
/* 200:    */           }
/* 201:    */           else
/* 202:    */           {
/* 203:209 */             cnxn = CPConnections.getConnection(account_id, msisdn);
/* 204:    */             
/* 205:    */ 
/* 206:212 */             new Thread(new ThreadedMessageSender(cnxn, msisdn, short_code, welcome_msg, 45000)).start();
/* 207:    */           }
/* 208:    */         }
/* 209:    */       }
/* 210:    */       else
/* 211:    */       {
/* 212:219 */         System.out.println(new Date() + ": " + keyword + ": " + msisdn + ": Not a valid subscription service");
/* 213:    */       }
/* 214:    */     }
/* 215:    */     catch (Exception e)
/* 216:    */     {
/* 217:223 */       System.out.println(new Date() + ": " + keyword + ": " + msisdn + ": error @ processnewsubscription: " + e.getMessage());
/* 218:    */     }
/* 219:226 */     System.out.println(new Date() + ": " + keyword + ": " + msisdn + ": Exiting processnewsubscription filter...");
/* 220:227 */     filterChain.doFilter(req, res);
/* 221:    */   }
/* 222:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.processnewsubscription
 * JD-Core Version:    0.7.0.1
 */