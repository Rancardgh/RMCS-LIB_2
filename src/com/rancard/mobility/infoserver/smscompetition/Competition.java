/*   1:    */ package com.rancard.mobility.infoserver.smscompetition;
/*   2:    */ 
/*   3:    */ import com.rancard.mobility.infoserver.common.services.UserService;
/*   4:    */ import java.sql.Timestamp;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Calendar;
/*   7:    */ import java.util.Date;
/*   8:    */ import java.util.StringTokenizer;
/*   9:    */ 
/*  10:    */ public class Competition
/*  11:    */   extends UserService
/*  12:    */ {
/*  13:    */   private String description;
/*  14:    */   private String thumbnailURL;
/*  15:    */   private String participationLimit;
/*  16:    */   private String competitionId;
/*  17:    */   private String question;
/*  18:    */   private String answer;
/*  19:    */   private int type;
/*  20:    */   private long currentParticipation;
/*  21:    */   private Timestamp startDate;
/*  22:    */   private Timestamp endDate;
/*  23:    */   private ArrayList prizes;
/*  24:    */   private ArrayList options;
/*  25:    */   private ArrayList alternativeAnswers;
/*  26:    */   
/*  27:    */   public Competition()
/*  28:    */   {
/*  29: 30 */     this.description = "";
/*  30: 31 */     this.thumbnailURL = "";
/*  31: 32 */     this.participationLimit = "";
/*  32: 33 */     this.competitionId = "";
/*  33: 34 */     this.question = "";
/*  34: 35 */     this.answer = "";
/*  35:    */     
/*  36: 37 */     this.type = -1;
/*  37: 38 */     this.currentParticipation = 0L;
/*  38:    */     
/*  39: 40 */     this.startDate = now();
/*  40: 41 */     this.endDate = now();
/*  41:    */     
/*  42: 43 */     this.prizes = new ArrayList();
/*  43: 44 */     this.options = new ArrayList();
/*  44: 45 */     this.alternativeAnswers = new ArrayList();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public Competition(String serviceType, String serviceName, String defaultMessage, String keyword, String desc, String thumburl, String limit, String accountId, int type, int partLevel, Timestamp start, Timestamp end, ArrayList prizes, String qstn, ArrayList opt, String ans, ArrayList altAns)
/*  48:    */   {
/*  49: 52 */     super(serviceType, keyword, accountId, serviceName, defaultMessage);
/*  50:    */     
/*  51: 54 */     this.description = desc;
/*  52: 55 */     this.thumbnailURL = thumburl;
/*  53: 56 */     this.participationLimit = limit;
/*  54: 57 */     this.type = type;
/*  55: 58 */     this.currentParticipation = partLevel;
/*  56: 59 */     this.startDate = start;
/*  57: 60 */     this.endDate = end;
/*  58: 61 */     this.prizes = prizes;
/*  59: 62 */     this.question = qstn;
/*  60: 63 */     this.options = opt;
/*  61: 64 */     this.answer = ans;
/*  62: 65 */     this.alternativeAnswers = altAns;
/*  63:    */   }
/*  64:    */   
/*  65:    */   private Timestamp now()
/*  66:    */   {
/*  67: 70 */     return new Timestamp(Calendar.getInstance().getTime().getTime());
/*  68:    */   }
/*  69:    */   
/*  70:    */   public String getDescription()
/*  71:    */   {
/*  72: 79 */     return this.description;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public String getThumbnailURL()
/*  76:    */   {
/*  77: 83 */     return this.thumbnailURL;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public String getParticipationLimit()
/*  81:    */   {
/*  82: 87 */     return this.participationLimit;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public int getType()
/*  86:    */   {
/*  87: 95 */     return this.type;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public long getCurrentParticipation()
/*  91:    */   {
/*  92: 99 */     return this.currentParticipation;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public Timestamp getStartDate()
/*  96:    */   {
/*  97:103 */     return this.startDate;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public Timestamp getEndDate()
/* 101:    */   {
/* 102:107 */     return this.endDate;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public ArrayList getPrizes()
/* 106:    */   {
/* 107:111 */     return this.prizes;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public String getQuestion()
/* 111:    */   {
/* 112:115 */     return this.question;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public ArrayList getOptions()
/* 116:    */   {
/* 117:119 */     return this.options;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public String getAnswer()
/* 121:    */   {
/* 122:123 */     return this.answer;
/* 123:    */   }
/* 124:    */   
/* 125:    */   public ArrayList getAlternativeAnswers()
/* 126:    */   {
/* 127:127 */     return this.alternativeAnswers;
/* 128:    */   }
/* 129:    */   
/* 130:    */   public String getAlternativeAnswersToString()
/* 131:    */   {
/* 132:131 */     String altAns = "";
/* 133:133 */     for (int i = 0; i < this.alternativeAnswers.size(); i++) {
/* 134:134 */       altAns = altAns + this.alternativeAnswers.get(i).toString() + ",";
/* 135:    */     }
/* 136:136 */     altAns = "".equals(altAns) ? "" : altAns.substring(0, altAns.length() - 1);
/* 137:137 */     return altAns;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public boolean isEnded()
/* 141:    */   {
/* 142:141 */     if (now().getTime() >= this.endDate.getTime()) {
/* 143:142 */       return true;
/* 144:    */     }
/* 145:144 */     return false;
/* 146:    */   }
/* 147:    */   
/* 148:    */   public void setDescription(String desc)
/* 149:    */   {
/* 150:154 */     this.description = desc;
/* 151:    */   }
/* 152:    */   
/* 153:    */   public void setThumbnailURL(String url)
/* 154:    */   {
/* 155:158 */     this.thumbnailURL = url;
/* 156:    */   }
/* 157:    */   
/* 158:    */   public void setParticipationLimit(String limit)
/* 159:    */   {
/* 160:162 */     this.participationLimit = limit;
/* 161:    */   }
/* 162:    */   
/* 163:    */   public void setType(int type)
/* 164:    */   {
/* 165:170 */     this.type = type;
/* 166:    */   }
/* 167:    */   
/* 168:    */   public void setCurrentParticipation(long number)
/* 169:    */   {
/* 170:174 */     this.currentParticipation = number;
/* 171:    */   }
/* 172:    */   
/* 173:    */   public void setStartDate(Timestamp startDate)
/* 174:    */   {
/* 175:178 */     this.startDate = startDate;
/* 176:    */   }
/* 177:    */   
/* 178:    */   public void setEndDate(Timestamp endDate)
/* 179:    */   {
/* 180:182 */     this.endDate = endDate;
/* 181:    */   }
/* 182:    */   
/* 183:    */   public void setPrizes(ArrayList prizes)
/* 184:    */   {
/* 185:186 */     this.prizes = prizes;
/* 186:    */   }
/* 187:    */   
/* 188:    */   public void setQuestion(String question)
/* 189:    */   {
/* 190:190 */     this.question = question;
/* 191:    */   }
/* 192:    */   
/* 193:    */   public void setOptions(ArrayList optns)
/* 194:    */   {
/* 195:194 */     this.options = optns;
/* 196:    */   }
/* 197:    */   
/* 198:    */   public void setAnswer(String ans)
/* 199:    */   {
/* 200:198 */     this.answer = ans;
/* 201:    */   }
/* 202:    */   
/* 203:    */   public void setAlternativeAnswers(String ans)
/* 204:    */   {
/* 205:203 */     if (ans != null)
/* 206:    */     {
/* 207:204 */       StringTokenizer st = new StringTokenizer(ans, ",");
/* 208:205 */       while (st.hasMoreTokens())
/* 209:    */       {
/* 210:206 */         String answer = st.nextToken().trim();
/* 211:207 */         this.alternativeAnswers.add(answer);
/* 212:    */       }
/* 213:    */     }
/* 214:    */   }
/* 215:    */   
/* 216:    */   public void setAlternativeAnswers(ArrayList ans)
/* 217:    */   {
/* 218:214 */     this.alternativeAnswers = ans;
/* 219:    */   }
/* 220:    */   
/* 221:    */   public String getCompetitionId()
/* 222:    */   {
/* 223:222 */     return this.competitionId;
/* 224:    */   }
/* 225:    */   
/* 226:    */   public void setCompetitionId(String competitionId)
/* 227:    */   {
/* 228:230 */     this.competitionId = competitionId;
/* 229:    */   }
/* 230:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.smscompetition.Competition
 * JD-Core Version:    0.7.0.1
 */