/*   1:    */ package com.rancard.mobility.contentserver.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.mobility.common.ThreadedMessageSender;
/*   4:    */ import com.rancard.mobility.contentserver.CPConnections;
/*   5:    */ import com.rancard.mobility.infoserver.common.services.ServiceManager;
/*   6:    */ import com.rancard.mobility.infoserver.common.services.UserService;
/*   7:    */ import com.rancard.mobility.infoserver.common.services.UserServiceExperience;
/*   8:    */ import com.rancard.mobility.infoserver.viralmarketing.PromoImpression;
/*   9:    */ import com.rancard.mobility.infoserver.viralmarketing.VMServiceManager;
/*  10:    */ import com.rancard.mobility.infoserver.viralmarketing.VMTransaction;
/*  11:    */ import com.rancard.mobility.infoserver.viralmarketing.VMUser;
/*  12:    */ import java.io.IOException;
/*  13:    */ import java.io.PrintStream;
/*  14:    */ import java.util.ArrayList;
/*  15:    */ import java.util.Date;
/*  16:    */ import java.util.Map;
/*  17:    */ import javax.servlet.Filter;
/*  18:    */ import javax.servlet.FilterChain;
/*  19:    */ import javax.servlet.FilterConfig;
/*  20:    */ import javax.servlet.ServletException;
/*  21:    */ import javax.servlet.ServletRequest;
/*  22:    */ import javax.servlet.ServletResponse;
/*  23:    */ import javax.servlet.http.HttpServlet;
/*  24:    */ import javax.servlet.http.HttpServletRequest;
/*  25:    */ 
/*  26:    */ public class serviceexperiencefilter
/*  27:    */   extends HttpServlet
/*  28:    */   implements Filter
/*  29:    */ {
/*  30: 21 */   private FilterConfig filterConfig = null;
/*  31: 23 */   String push_sender = "";
/*  32: 24 */   String inv_instruction = "";
/*  33:    */   
/*  34:    */   public void init(FilterConfig filterConfig)
/*  35:    */   {
/*  36: 27 */     this.filterConfig = filterConfig;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
/*  40:    */     throws IOException, ServletException
/*  41:    */   {
/*  42: 33 */     HttpServletRequest request = (HttpServletRequest)req;
/*  43:    */     
/*  44:    */ 
/*  45: 36 */     String accountId = (String)req.getAttribute("acctId");
/*  46: 37 */     String siteId = request.getParameter("siteId");
/*  47: 38 */     String keyword = req.getParameter("keyword");
/*  48: 39 */     String msisdn = req.getParameter("msisdn");
/*  49: 40 */     String dest = req.getParameter("dest");
/*  50: 41 */     String shortCode = dest.contains("+") ? dest.substring(1) : dest;
/*  51: 42 */     String smsc = req.getParameter("smsc") == null ? "" : req.getParameter("smsc");
/*  52:    */     
/*  53:    */ 
/*  54: 45 */     String pushMsg = "";
/*  55: 46 */     String pushMsgSender = "";
/*  56: 47 */     int pushMsgWaitTime = 0;
/*  57: 48 */     String promoId = "";
/*  58: 49 */     boolean skipMessagePush = false;
/*  59:    */     
/*  60:    */ 
/*  61: 52 */     logState(accountId, siteId, keyword, msisdn, "Entering serviceexperiencefilter...");
/*  62:    */     try
/*  63:    */     {
/*  64: 56 */       UserService srvc = ServiceManager.viewService(keyword, accountId);
/*  65:    */       
/*  66:    */ 
/*  67: 59 */       logState(accountId, siteId, keyword, msisdn, "Start service experience processing...");
/*  68:    */       
/*  69:    */ 
/*  70: 62 */       String service_keyword = srvc.getKeyword();
/*  71: 63 */       UserServiceExperience srvcExpr = ServiceManager.viewServiceExperience(accountId, siteId, service_keyword);
/*  72: 71 */       if (srvcExpr.exists())
/*  73:    */       {
/*  74: 75 */         promoId = srvcExpr.getPromoId();
/*  75: 76 */         String serviceRespCode = srvcExpr.getPromoRespCode();
/*  76:    */         
/*  77:    */ 
/*  78: 79 */         Map<String, String> metaDataMap = srvcExpr.getMetaDataMap();
/*  79: 80 */         String altKeyword = (String)metaDataMap.get("override_keyword".toUpperCase());
/*  80: 81 */         if ((altKeyword != null) && (!altKeyword.equals(""))) {
/*  81: 82 */           request.setAttribute("override_keyword", altKeyword);
/*  82:    */         }
/*  83: 86 */         if (srvc.isSubscription())
/*  84:    */         {
/*  85: 90 */           boolean isSubscribedToOrig = ServiceManager.isSubscribed(msisdn, accountId, service_keyword);
/*  86: 91 */           boolean isSubscribedToAlt = false;
/*  87: 93 */           if ((altKeyword != null) && (!altKeyword.equals(""))) {
/*  88: 94 */             isSubscribedToAlt = ServiceManager.isSubscribed(msisdn, accountId, altKeyword);
/*  89:    */           }
/*  90: 97 */           if ((isSubscribedToOrig) || (isSubscribedToAlt))
/*  91:    */           {
/*  92:101 */             logState(accountId, siteId, keyword, msisdn, "Subscription already exists in DB. Treating request as On-demand");
/*  93:    */             
/*  94:103 */             pushMsg = srvcExpr.getAlreadySubscribedMsg();
/*  95:    */             
/*  96:105 */             pushMsgSender = srvcExpr.getAlreadySubscribedMsgSender().equals("") ? shortCode : srvcExpr.getAlreadySubscribedMsgSender();
/*  97:    */             
/*  98:    */ 
/*  99:108 */             logState(accountId, siteId, keyword, msisdn, "Setting Kannel FROM attribute to " + srvc.getServiceResponseSender());
/* 100:109 */             request.setAttribute("x-kannel-header-from", srvc.getServiceResponseSender());
/* 101:    */             
/* 102:111 */             promoId = "";
/* 103:112 */             serviceRespCode = "";
/* 104:    */             
/* 105:    */ 
/* 106:115 */             int accessCount = 0;
/* 107:    */             try
/* 108:    */             {
/* 109:117 */               accessCount = ServiceManager.getLastAccessCount(msisdn, accountId, keyword);
/* 110:    */             }
/* 111:    */             catch (Exception e) {}
/* 112:121 */             if (accessCount > 1) {
/* 113:122 */               skipMessagePush = true;
/* 114:    */             } else {
/* 115:124 */               skipMessagePush = false;
/* 116:    */             }
/* 117:    */           }
/* 118:131 */           else if (srvcExpr.getSubscriptionInterval() == -1)
/* 119:    */           {
/* 120:133 */             pushMsg = srvcExpr.getPromoMsg();
/* 121:134 */             pushMsgSender = srvcExpr.getPromoMsgSender();
/* 122:    */           }
/* 123:    */           else
/* 124:    */           {
/* 125:137 */             logState(accountId, siteId, keyword, msisdn, "Subscription doesn't exist. Creating new subscription record...");
/* 126:139 */             if ((altKeyword != null) && (!altKeyword.equals("")))
/* 127:    */             {
/* 128:140 */               service_keyword = altKeyword;
/* 129:141 */               logState(accountId, siteId, keyword, msisdn, "Service keyword changed for subscription: " + service_keyword);
/* 130:    */             }
/* 131:144 */             ArrayList<String> keywordList = new ArrayList();
/* 132:145 */             keywordList.add(service_keyword);
/* 133:147 */             if (srvcExpr.getSubscriptionInterval() == 0) {
/* 134:149 */               ServiceManager.subscribeToService(msisdn, keywordList, accountId);
/* 135:    */             } else {
/* 136:152 */               ServiceManager.subscribeToService(msisdn, keywordList, accountId, srvcExpr.getSubscriptionInterval(), 1, 1);
/* 137:    */             }
/* 138:155 */             vmAcceptance(accountId, service_keyword, msisdn, srvc.getServiceName(), shortCode, smsc);
/* 139:    */             
/* 140:157 */             pushMsg = srvcExpr.getWelcomeMsg();
/* 141:158 */             pushMsgSender = srvcExpr.getWelcomeMsgSender().equals("") ? shortCode : srvcExpr.getWelcomeMsgSender();
/* 142:    */             
/* 143:160 */             promoId = "";
/* 144:161 */             serviceRespCode = "";
/* 145:    */           }
/* 146:    */         }
/* 147:    */         else
/* 148:    */         {
/* 149:165 */           pushMsg = srvcExpr.getPromoMsg();
/* 150:166 */           pushMsgSender = srvcExpr.getPromoMsgSender();
/* 151:    */         }
/* 152:169 */         if (!skipMessagePush)
/* 153:    */         {
/* 154:171 */           pushMsgWaitTime = srvcExpr.getPushMsgWaitTime();
/* 155:    */           
/* 156:173 */           CPConnections cnxn = new CPConnections();
/* 157:174 */           String flashPush = (String)metaDataMap.get("push_msg_flash".toUpperCase());
/* 158:176 */           if ((flashPush != null) && (flashPush.equals("1")))
/* 159:    */           {
/* 160:177 */             cnxn = CPConnections.getConnection(accountId, msisdn, "kannel");
/* 161:178 */             new Thread(new ThreadedMessageSender(cnxn, msisdn, pushMsgSender, pushMsg, "&mclass=0", pushMsgWaitTime)).start();
/* 162:    */           }
/* 163:    */           else
/* 164:    */           {
/* 165:181 */             cnxn = CPConnections.getConnection(accountId, msisdn);
/* 166:182 */             new Thread(new ThreadedMessageSender(cnxn, msisdn, pushMsgSender, pushMsg, pushMsgWaitTime)).start();
/* 167:    */           }
/* 168:    */         }
/* 169:185 */         request.setAttribute("promoId", promoId);
/* 170:    */         
/* 171:    */ 
/* 172:188 */         String hashString = msisdn + accountId + keyword.toUpperCase();
/* 173:189 */         promoImpressionsCheck(hashString.hashCode(), serviceRespCode, keyword, accountId, msisdn, request);
/* 174:    */       }
/* 175:    */       else
/* 176:    */       {
/* 177:192 */         logState(accountId, siteId, keyword, msisdn, "No experience configuration defined for service");
/* 178:    */       }
/* 179:    */     }
/* 180:    */     catch (Exception e)
/* 181:    */     {
/* 182:195 */       logState(accountId, siteId, keyword, msisdn, "Error @ serviceexperiencefilter: " + e.getMessage());
/* 183:    */     }
/* 184:198 */     logState(accountId, siteId, keyword, msisdn, "Exiting serviceexperiencefilter.");
/* 185:    */     
/* 186:200 */     filterChain.doFilter(req, res);
/* 187:    */   }
/* 188:    */   
/* 189:    */   private void vmAcceptance(String accountId, String keyword, String msisdn, String serviceName, String shortCode, String smsc)
/* 190:    */   {
/* 191:206 */     initializeServiceCustomization(accountId, keyword, shortCode);
/* 192:    */     try
/* 193:    */     {
/* 194:210 */       CPConnections cnxn = new CPConnections();
/* 195:211 */       cnxn = CPConnections.getConnection(accountId, msisdn);
/* 196:    */       
/* 197:    */ 
/* 198:214 */       VMTransaction trans = VMServiceManager.viewTransaction(accountId, keyword, msisdn);
/* 199:215 */       if (trans.getStatus().equals("inv_sent"))
/* 200:    */       {
/* 201:216 */         VMServiceManager.updateTransactionStatus(trans.getCampaignId(), msisdn, "inv_accepted", 10);
/* 202:    */         
/* 203:    */ 
/* 204:219 */         String recruiter = trans.getRecruiterMsisdn();
/* 205:220 */         VMUser user = VMServiceManager.viewUser(keyword, accountId, recruiter);
/* 206:221 */         String displayable_number = "0" + trans.getRecipientMsisdn().substring(4);
/* 207:222 */         String confirmation_msg = "You recently invited " + displayable_number + " to use your favourite service. " + "We're excited to inform you that your invitation was just accepted! You now have " + user.getPoints() + " points.";
/* 208:    */         
/* 209:    */ 
/* 210:    */ 
/* 211:226 */         new Thread(new ThreadedMessageSender(cnxn, recruiter, this.push_sender, confirmation_msg, 5000)).start();
/* 212:    */       }
/* 213:229 */       if ((!smsc.equalsIgnoreCase("myBuzz")) && (!smsc.equalsIgnoreCase("myBuzz2"))) {
/* 214:231 */         new Thread(new ThreadedMessageSender(cnxn, msisdn, this.push_sender, this.inv_instruction, 30000)).start();
/* 215:    */       }
/* 216:    */     }
/* 217:    */     catch (Exception exc)
/* 218:    */     {
/* 219:234 */       System.out.println("Exception caught processing viral marketing step 2: acceptance");
/* 220:    */     }
/* 221:    */   }
/* 222:    */   
/* 223:    */   private void initializeServiceCustomization(String accountId, String keyword, String shortCode)
/* 224:    */   {
/* 225:240 */     if (accountId.equals("000"))
/* 226:    */     {
/* 227:241 */       this.push_sender = keyword;
/* 228:242 */       this.inv_instruction = ("Send INVITE followed by your friend's number to " + shortCode + " to share this service with them. Add FROM followed by your name to the message to personalize it.");
/* 229:    */     }
/* 230:    */     else
/* 231:    */     {
/* 232:245 */       this.push_sender = shortCode;
/* 233:246 */       this.inv_instruction = ("Send INVITE followed by your friend's number to " + this.push_sender + " to share this service with them. Add FROM followed by your name to the message to personalize it.");
/* 234:    */     }
/* 235:    */   }
/* 236:    */   
/* 237:    */   private void promoImpressionsCheck(long hashCode, String srvcRespCode, String keyword, String accountId, String msisdn, HttpServletRequest request)
/* 238:    */   {
/* 239:    */     try
/* 240:    */     {
/* 241:253 */       PromoImpression pImpression = VMServiceManager.viewPromoImpression(hashCode);
/* 242:255 */       if (pImpression.exists())
/* 243:    */       {
/* 244:258 */         request.setAttribute("promoRespCode", keyword);
/* 245:259 */         VMServiceManager.updateAdRespSummary(pImpression);
/* 246:    */       }
/* 247:263 */       if (!srvcRespCode.equals(""))
/* 248:    */       {
/* 249:264 */         String hashString = msisdn + accountId + srvcRespCode.toUpperCase();
/* 250:    */         
/* 251:266 */         pImpression.setHashCode(hashString.hashCode());
/* 252:267 */         pImpression.setAccountId(accountId);
/* 253:268 */         pImpression.setMsisdn(msisdn);
/* 254:269 */         pImpression.setKeyword(srvcRespCode);
/* 255:270 */         pImpression.setInventory_keyword(keyword);
/* 256:    */         
/* 257:272 */         VMServiceManager.createPromoImpression(pImpression);
/* 258:    */       }
/* 259:    */     }
/* 260:    */     catch (Exception exc)
/* 261:    */     {
/* 262:276 */       System.out.println("Exception caught processing promotional campaign");
/* 263:    */     }
/* 264:    */   }
/* 265:    */   
/* 266:    */   private void logState(String accountId, String siteId, String keyword, String msisdn, String state)
/* 267:    */   {
/* 268:282 */     System.out.println(new Date() + ": " + accountId + ": " + siteId + ": " + keyword + ": " + msisdn + ": " + state);
/* 269:    */   }
/* 270:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.serviceexperiencefilter
 * JD-Core Version:    0.7.0.1
 */