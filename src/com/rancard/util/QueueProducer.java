/*   1:    */ package com.rancard.util;
/*   2:    */ 
/*   3:    */ import com.rancard.mobility.infoserver.common.services.UserService;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.io.Serializable;
/*   6:    */ import java.util.ArrayList;
/*   7:    */ import java.util.Date;
/*   8:    */ import java.util.Iterator;
/*   9:    */ import javax.jms.Connection;
/*  10:    */ import javax.jms.Destination;
/*  11:    */ import javax.jms.JMSException;
/*  12:    */ import javax.jms.MessageConsumer;
/*  13:    */ import javax.jms.MessageListener;
/*  14:    */ import javax.jms.MessageProducer;
/*  15:    */ import javax.jms.ObjectMessage;
/*  16:    */ import javax.jms.Session;
/*  17:    */ import org.apache.activemq.ActiveMQConnection;
/*  18:    */ import org.apache.activemq.ActiveMQConnectionFactory;
/*  19:    */ 
/*  20:    */ public abstract class QueueProducer
/*  21:    */   implements Runnable, MessageListener
/*  22:    */ {
/*  23:    */   private Destination destination;
/*  24:    */   private long sleepTime;
/*  25:    */   private long timeToLive;
/*  26: 35 */   private String queue_user = ActiveMQConnection.DEFAULT_USER;
/*  27: 36 */   private String queue_password = ActiveMQConnection.DEFAULT_PASSWORD;
/*  28: 37 */   private String queue_url = "failover://tcp://localhost:61616";
/*  29:    */   private String subject;
/*  30:    */   private boolean transacted;
/*  31: 40 */   private boolean persistent = true;
/*  32: 41 */   private ObjectMessage objectMessage = null;
/*  33:    */   private MessageConsumer replyConsumer;
/*  34:    */   private MessageProducer messageProducer;
/*  35:    */   private Session session;
/*  36:    */   private Connection connection;
/*  37:    */   private final String messageRef;
/*  38:    */   private final UserService service;
/*  39:    */   private ArrayList<Object> queueMessages;
/*  40:    */   
/*  41:    */   public QueueProducer()
/*  42:    */   {
/*  43: 51 */     this.messageRef = "";
/*  44: 52 */     this.service = new UserService();
/*  45: 53 */     this.connection = getConnection();
/*  46: 54 */     this.session = null;
/*  47: 55 */     this.destination = null;
/*  48: 56 */     this.sleepTime = 0L;
/*  49: 57 */     this.timeToLive = 0L;
/*  50: 58 */     this.queue_user = ActiveMQConnection.DEFAULT_USER;
/*  51: 59 */     this.queue_password = ActiveMQConnection.DEFAULT_PASSWORD;
/*  52: 60 */     this.queue_url = "failover://tcp://localhost:61616";
/*  53: 61 */     this.subject = "";
/*  54: 62 */     this.transacted = false;
/*  55: 63 */     this.persistent = true;
/*  56: 64 */     this.objectMessage = null;
/*  57: 65 */     this.replyConsumer = null;
/*  58: 66 */     this.messageProducer = null;
/*  59: 67 */     this.session = null;
/*  60: 68 */     this.queueMessages = null;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public QueueProducer(String queueName, UserService service, String messageRef)
/*  64:    */   {
/*  65: 72 */     this.messageRef = messageRef;
/*  66: 73 */     this.service = service;
/*  67: 74 */     this.connection = getConnection();
/*  68: 75 */     this.session = null;
/*  69: 76 */     this.destination = null;
/*  70: 77 */     this.sleepTime = 0L;
/*  71: 78 */     this.timeToLive = 0L;
/*  72: 79 */     this.queue_user = ActiveMQConnection.DEFAULT_USER;
/*  73: 80 */     this.queue_password = ActiveMQConnection.DEFAULT_PASSWORD;
/*  74: 81 */     this.queue_url = "failover://tcp://localhost:61616";
/*  75: 82 */     this.subject = queueName;
/*  76: 83 */     this.transacted = false;
/*  77: 84 */     this.persistent = true;
/*  78: 85 */     this.objectMessage = null;
/*  79: 86 */     this.replyConsumer = null;
/*  80: 87 */     this.messageProducer = null;
/*  81: 88 */     this.session = null;
/*  82: 89 */     this.queueMessages = null;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public QueueProducer(String queueName, ArrayList<Object> messages)
/*  86:    */   {
/*  87: 93 */     this.messageRef = "";
/*  88: 94 */     this.service = null;
/*  89: 95 */     this.connection = getConnection();
/*  90: 96 */     this.session = null;
/*  91: 97 */     this.destination = null;
/*  92: 98 */     this.sleepTime = 0L;
/*  93: 99 */     this.timeToLive = 0L;
/*  94:100 */     this.queue_user = ActiveMQConnection.DEFAULT_USER;
/*  95:101 */     this.queue_password = ActiveMQConnection.DEFAULT_PASSWORD;
/*  96:102 */     this.queue_url = "failover://tcp://localhost:61616";
/*  97:103 */     this.subject = queueName;
/*  98:104 */     this.transacted = false;
/*  99:105 */     this.persistent = true;
/* 100:106 */     this.objectMessage = null;
/* 101:107 */     this.replyConsumer = null;
/* 102:108 */     this.messageProducer = null;
/* 103:109 */     this.session = null;
/* 104:110 */     this.queueMessages = messages;
/* 105:    */   }
/* 106:    */   
/* 107:    */   private Connection getConnection()
/* 108:    */   {
/* 109:114 */     Connection con = null;
/* 110:    */     try
/* 111:    */     {
/* 112:116 */       ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.queue_user, this.queue_password, this.queue_url);
/* 113:117 */       con = connectionFactory.createConnection();
/* 114:118 */       con.start();
/* 115:    */     }
/* 116:    */     catch (Exception e)
/* 117:    */     {
/* 118:120 */       System.out.println(new Date() + ": [QUEUE EXCEPTION] Fatal Error " + e + (this.service == null ? " (N/A)" : new StringBuilder().append(this.subject).append("(acctId=").append(this.service.getAccountId()).append("-keyword=").append(this.service.getKeyword()).append(")").toString()));
/* 119:121 */       e.printStackTrace();
/* 120:    */     }
/* 121:123 */     return con;
/* 122:    */   }
/* 123:    */   
/* 124:    */   public synchronized void onException(JMSException ex)
/* 125:    */   {
/* 126:156 */     throw new UnsupportedOperationException(ex);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void run()
/* 130:    */   {
/* 131:162 */     boolean canSend = initializeQueue();
/* 132:164 */     if ((canSend) && (this.queueMessages != null)) {
/* 133:    */       try
/* 134:    */       {
/* 135:167 */         Iterator<Object> messageObjectList = this.queueMessages.iterator();
/* 136:168 */         while (messageObjectList.hasNext())
/* 137:    */         {
/* 138:170 */           Object msgObject = messageObjectList.next();
/* 139:    */           
/* 140:    */ 
/* 141:173 */           this.objectMessage = this.session.createObjectMessage();
/* 142:174 */           this.objectMessage.setObject((Serializable)msgObject);
/* 143:    */           
/* 144:176 */           this.messageProducer.send(this.objectMessage);
/* 145:178 */           if (this.transacted) {
/* 146:179 */             this.session.commit();
/* 147:    */           }
/* 148:182 */           Thread.sleep(this.sleepTime);
/* 149:    */         }
/* 150:    */       }
/* 151:    */       catch (Exception ex)
/* 152:    */       {
/* 153:185 */         System.out.println(new Date() + " :[RESPONSE QUEUE MONITOR]: JMS Exception occured while submiting message to queue :: " + this.subject + ":REQUEST");
/* 154:    */       }
/* 155:    */     }
/* 156:    */   }
/* 157:    */   
/* 158:    */   private boolean initializeQueue()
/* 159:    */   {
/* 160:192 */     boolean canSend = false;
/* 161:    */     try
/* 162:    */     {
/* 163:199 */       if ((this.subject != null) && (!this.subject.equals("")))
/* 164:    */       {
/* 165:200 */         this.session = this.connection.createSession(this.transacted, 1);
/* 166:201 */         this.destination = this.session.createQueue(this.subject + ":REQUEST");
/* 167:202 */         canSend = true;
/* 168:    */       }
/* 169:    */       else
/* 170:    */       {
/* 171:204 */         System.out.println(new Date() + ": [queueProcess:ERROR] Queue name cannot be null  (acctId=" + this.service.getAccountId() + "-keyword=" + this.service.getKeyword() + ")");
/* 172:    */       }
/* 173:    */     }
/* 174:    */     catch (JMSException ex)
/* 175:    */     {
/* 176:207 */       canSend = false;
/* 177:    */     }
/* 178:210 */     if ((this.session != null) && (this.destination != null) && (canSend == true)) {
/* 179:    */       try
/* 180:    */       {
/* 181:213 */         this.messageProducer = this.session.createProducer(this.destination);
/* 182:214 */         System.out.println(new Date() + ": [queueProcess:INFO] Successfully created the message request queue :: " + this.subject + ":REQUEST " + (this.service == null ? "(N/A)" : new StringBuilder().append(this.subject).append("(acctId=").append(this.service.getAccountId()).append("-keyword=").append(this.service.getKeyword()).append(")").toString()));
/* 183:215 */         if (this.persistent) {
/* 184:216 */           this.messageProducer.setDeliveryMode(2);
/* 185:    */         } else {
/* 186:218 */           this.messageProducer.setDeliveryMode(1);
/* 187:    */         }
/* 188:220 */         if (this.timeToLive != 0L) {
/* 189:221 */           this.messageProducer.setTimeToLive(this.timeToLive);
/* 190:    */         }
/* 191:225 */         this.replyConsumer = this.session.createConsumer(this.session.createQueue(this.subject + ":RESPONSE"));
/* 192:226 */         this.replyConsumer.setMessageListener(this);
/* 193:227 */         System.out.println(new Date() + ": [queueProcess:INFO] Successfully Connected to message response queue :: " + this.subject + ":RESPONSE " + (this.service == null ? "(N/A)" : new StringBuilder().append(this.subject).append("(acctId=").append(this.service.getAccountId()).append("-keyword=").append(this.service.getKeyword()).append(")").toString()));
/* 194:    */       }
/* 195:    */       catch (JMSException ex)
/* 196:    */       {
/* 197:232 */         canSend = false;
/* 198:    */       }
/* 199:    */     }
/* 200:236 */     return canSend;
/* 201:    */   }
/* 202:    */   
/* 203:    */   public Destination getDestination()
/* 204:    */   {
/* 205:241 */     return this.destination;
/* 206:    */   }
/* 207:    */   
/* 208:    */   public void setDestination(Destination destination)
/* 209:    */   {
/* 210:245 */     this.destination = destination;
/* 211:    */   }
/* 212:    */   
/* 213:    */   public ObjectMessage getObjectMessage()
/* 214:    */   {
/* 215:249 */     return this.objectMessage;
/* 216:    */   }
/* 217:    */   
/* 218:    */   public void setObjectMessage(ObjectMessage objectMessage)
/* 219:    */   {
/* 220:253 */     this.objectMessage = objectMessage;
/* 221:    */   }
/* 222:    */   
/* 223:    */   public boolean isPersistent()
/* 224:    */   {
/* 225:257 */     return this.persistent;
/* 226:    */   }
/* 227:    */   
/* 228:    */   public void setPersistent(boolean persistent)
/* 229:    */   {
/* 230:261 */     this.persistent = persistent;
/* 231:    */   }
/* 232:    */   
/* 233:    */   public ArrayList<Object> getQueueMessages()
/* 234:    */   {
/* 235:265 */     return this.queueMessages;
/* 236:    */   }
/* 237:    */   
/* 238:    */   public void setQueueMessages(ArrayList<Object> queueMessages)
/* 239:    */   {
/* 240:269 */     this.queueMessages = queueMessages;
/* 241:    */   }
/* 242:    */   
/* 243:    */   public String getQueue_password()
/* 244:    */   {
/* 245:273 */     return this.queue_password;
/* 246:    */   }
/* 247:    */   
/* 248:    */   public void setQueue_password(String queue_password)
/* 249:    */   {
/* 250:277 */     this.queue_password = queue_password;
/* 251:    */   }
/* 252:    */   
/* 253:    */   public String getQueue_url()
/* 254:    */   {
/* 255:281 */     return this.queue_url;
/* 256:    */   }
/* 257:    */   
/* 258:    */   public void setQueue_url(String queue_url)
/* 259:    */   {
/* 260:285 */     this.queue_url = queue_url;
/* 261:    */   }
/* 262:    */   
/* 263:    */   public String getQueue_user()
/* 264:    */   {
/* 265:289 */     return this.queue_user;
/* 266:    */   }
/* 267:    */   
/* 268:    */   public void setQueue_user(String queue_user)
/* 269:    */   {
/* 270:293 */     this.queue_user = queue_user;
/* 271:    */   }
/* 272:    */   
/* 273:    */   public MessageConsumer getReplyConsumer()
/* 274:    */   {
/* 275:297 */     return this.replyConsumer;
/* 276:    */   }
/* 277:    */   
/* 278:    */   public void setReplyConsumer(MessageConsumer replyConsumer)
/* 279:    */   {
/* 280:301 */     this.replyConsumer = replyConsumer;
/* 281:    */   }
/* 282:    */   
/* 283:    */   public Session getSession()
/* 284:    */   {
/* 285:305 */     return this.session;
/* 286:    */   }
/* 287:    */   
/* 288:    */   public void setSession(Session session)
/* 289:    */   {
/* 290:309 */     this.session = session;
/* 291:    */   }
/* 292:    */   
/* 293:    */   public long getSleepTime()
/* 294:    */   {
/* 295:313 */     return this.sleepTime;
/* 296:    */   }
/* 297:    */   
/* 298:    */   public void setSleepTime(long sleepTime)
/* 299:    */   {
/* 300:317 */     this.sleepTime = sleepTime;
/* 301:    */   }
/* 302:    */   
/* 303:    */   public String getSubject()
/* 304:    */   {
/* 305:321 */     return this.subject;
/* 306:    */   }
/* 307:    */   
/* 308:    */   public void setSubject(String subject)
/* 309:    */   {
/* 310:325 */     this.subject = subject;
/* 311:    */   }
/* 312:    */   
/* 313:    */   public long getTimeToLive()
/* 314:    */   {
/* 315:329 */     return this.timeToLive;
/* 316:    */   }
/* 317:    */   
/* 318:    */   public void setTimeToLive(long timeToLive)
/* 319:    */   {
/* 320:333 */     this.timeToLive = timeToLive;
/* 321:    */   }
/* 322:    */   
/* 323:    */   public boolean isTransacted()
/* 324:    */   {
/* 325:337 */     return this.transacted;
/* 326:    */   }
/* 327:    */   
/* 328:    */   public void setTransacted(boolean transacted)
/* 329:    */   {
/* 330:341 */     this.transacted = transacted;
/* 331:    */   }
/* 332:    */   
/* 333:    */   public MessageProducer getMessageProducer()
/* 334:    */   {
/* 335:345 */     return this.messageProducer;
/* 336:    */   }
/* 337:    */   
/* 338:    */   public void setMessageProducer(MessageProducer messageProducer)
/* 339:    */   {
/* 340:349 */     this.messageProducer = messageProducer;
/* 341:    */   }
/* 342:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.util.QueueProducer
 * JD-Core Version:    0.7.0.1
 */