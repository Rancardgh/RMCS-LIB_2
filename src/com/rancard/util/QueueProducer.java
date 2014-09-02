 package com.rancard.util;
 
 import com.rancard.mobility.infoserver.common.services.UserService;
 import java.io.PrintStream;
 import java.io.Serializable;
 import java.util.ArrayList;
 import java.util.Date;
 import java.util.Iterator;
 import javax.jms.Connection;
 import javax.jms.Destination;
 import javax.jms.JMSException;
 import javax.jms.MessageConsumer;
 import javax.jms.MessageListener;
 import javax.jms.MessageProducer;
 import javax.jms.ObjectMessage;
 import javax.jms.Session;
 import org.apache.activemq.ActiveMQConnection;
 import org.apache.activemq.ActiveMQConnectionFactory;
 
 public abstract class QueueProducer
   implements Runnable, MessageListener
 {
   private Destination destination;
   private long sleepTime;
   private long timeToLive;
   private String queue_user = ActiveMQConnection.DEFAULT_USER;
   private String queue_password = ActiveMQConnection.DEFAULT_PASSWORD;
   private String queue_url = "failover://tcp://localhost:61616";
   private String subject;
   private boolean transacted;
   private boolean persistent = true;
   private ObjectMessage objectMessage = null;
   private MessageConsumer replyConsumer;
   private MessageProducer messageProducer;
   private Session session;
   private Connection connection;
   private final String messageRef;
   private final UserService service;
   private ArrayList<Object> queueMessages;
   
   public QueueProducer()
   {
     this.messageRef = "";
     this.service = new UserService();
     this.connection = getConnection();
     this.session = null;
     this.destination = null;
     this.sleepTime = 0L;
     this.timeToLive = 0L;
     this.queue_user = ActiveMQConnection.DEFAULT_USER;
     this.queue_password = ActiveMQConnection.DEFAULT_PASSWORD;
     this.queue_url = "failover://tcp://localhost:61616";
     this.subject = "";
     this.transacted = false;
     this.persistent = true;
     this.objectMessage = null;
     this.replyConsumer = null;
     this.messageProducer = null;
     this.session = null;
     this.queueMessages = null;
   }
   
   public QueueProducer(String queueName, UserService service, String messageRef)
   {
     this.messageRef = messageRef;
     this.service = service;
     this.connection = getConnection();
     this.session = null;
     this.destination = null;
     this.sleepTime = 0L;
     this.timeToLive = 0L;
     this.queue_user = ActiveMQConnection.DEFAULT_USER;
     this.queue_password = ActiveMQConnection.DEFAULT_PASSWORD;
     this.queue_url = "failover://tcp://localhost:61616";
     this.subject = queueName;
     this.transacted = false;
     this.persistent = true;
     this.objectMessage = null;
     this.replyConsumer = null;
     this.messageProducer = null;
     this.session = null;
     this.queueMessages = null;
   }
   
   public QueueProducer(String queueName, ArrayList<Object> messages)
   {
     this.messageRef = "";
     this.service = null;
     this.connection = getConnection();
     this.session = null;
     this.destination = null;
     this.sleepTime = 0L;
     this.timeToLive = 0L;
     this.queue_user = ActiveMQConnection.DEFAULT_USER;
     this.queue_password = ActiveMQConnection.DEFAULT_PASSWORD;
     this.queue_url = "failover://tcp://localhost:61616";
     this.subject = queueName;
     this.transacted = false;
     this.persistent = true;
     this.objectMessage = null;
     this.replyConsumer = null;
     this.messageProducer = null;
     this.session = null;
     this.queueMessages = messages;
   }
   
   private Connection getConnection()
   {
     Connection con = null;
     try
     {
       ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.queue_user, this.queue_password, this.queue_url);
       con = connectionFactory.createConnection();
       con.start();
     }
     catch (Exception e)
     {
       System.out.println(new Date() + ": [QUEUE EXCEPTION] Fatal Error " + e + (this.service == null ? " (N/A)" : new StringBuilder().append(this.subject).append("(acctId=").append(this.service.getAccountId()).append("-keyword=").append(this.service.getKeyword()).append(")").toString()));
       e.printStackTrace();
     }
     return con;
   }
   
   public synchronized void onException(JMSException ex)
   {
     throw new UnsupportedOperationException(ex);
   }
   
   public void run()
   {
     boolean canSend = initializeQueue();
     if ((canSend) && (this.queueMessages != null)) {
       try
       {
         Iterator<Object> messageObjectList = this.queueMessages.iterator();
         while (messageObjectList.hasNext())
         {
           Object msgObject = messageObjectList.next();
           
 
           this.objectMessage = this.session.createObjectMessage();
           this.objectMessage.setObject((Serializable)msgObject);
           
           this.messageProducer.send(this.objectMessage);
           if (this.transacted) {
             this.session.commit();
           }
           Thread.sleep(this.sleepTime);
         }
       }
       catch (Exception ex)
       {
         System.out.println(new Date() + " :[RESPONSE QUEUE MONITOR]: JMS Exception occured while submiting message to queue :: " + this.subject + ":REQUEST");
       }
     }
   }
   
   private boolean initializeQueue()
   {
     boolean canSend = false;
     try
     {
       if ((this.subject != null) && (!this.subject.equals("")))
       {
         this.session = this.connection.createSession(this.transacted, 1);
         this.destination = this.session.createQueue(this.subject + ":REQUEST");
         canSend = true;
       }
       else
       {
         System.out.println(new Date() + ": [queueProcess:ERROR] Queue name cannot be null  (acctId=" + this.service.getAccountId() + "-keyword=" + this.service.getKeyword() + ")");
       }
     }
     catch (JMSException ex)
     {
       canSend = false;
     }
     if ((this.session != null) && (this.destination != null) && (canSend == true)) {
       try
       {
         this.messageProducer = this.session.createProducer(this.destination);
         System.out.println(new Date() + ": [queueProcess:INFO] Successfully created the message request queue :: " + this.subject + ":REQUEST " + (this.service == null ? "(N/A)" : new StringBuilder().append(this.subject).append("(acctId=").append(this.service.getAccountId()).append("-keyword=").append(this.service.getKeyword()).append(")").toString()));
         if (this.persistent) {
           this.messageProducer.setDeliveryMode(2);
         } else {
           this.messageProducer.setDeliveryMode(1);
         }
         if (this.timeToLive != 0L) {
           this.messageProducer.setTimeToLive(this.timeToLive);
         }
         this.replyConsumer = this.session.createConsumer(this.session.createQueue(this.subject + ":RESPONSE"));
         this.replyConsumer.setMessageListener(this);
         System.out.println(new Date() + ": [queueProcess:INFO] Successfully Connected to message response queue :: " + this.subject + ":RESPONSE " + (this.service == null ? "(N/A)" : new StringBuilder().append(this.subject).append("(acctId=").append(this.service.getAccountId()).append("-keyword=").append(this.service.getKeyword()).append(")").toString()));
       }
       catch (JMSException ex)
       {
         canSend = false;
       }
     }
     return canSend;
   }
   
   public Destination getDestination()
   {
     return this.destination;
   }
   
   public void setDestination(Destination destination)
   {
     this.destination = destination;
   }
   
   public ObjectMessage getObjectMessage()
   {
     return this.objectMessage;
   }
   
   public void setObjectMessage(ObjectMessage objectMessage)
   {
     this.objectMessage = objectMessage;
   }
   
   public boolean isPersistent()
   {
     return this.persistent;
   }
   
   public void setPersistent(boolean persistent)
   {
     this.persistent = persistent;
   }
   
   public ArrayList<Object> getQueueMessages()
   {
     return this.queueMessages;
   }
   
   public void setQueueMessages(ArrayList<Object> queueMessages)
   {
     this.queueMessages = queueMessages;
   }
   
   public String getQueue_password()
   {
     return this.queue_password;
   }
   
   public void setQueue_password(String queue_password)
   {
     this.queue_password = queue_password;
   }
   
   public String getQueue_url()
   {
     return this.queue_url;
   }
   
   public void setQueue_url(String queue_url)
   {
     this.queue_url = queue_url;
   }
   
   public String getQueue_user()
   {
     return this.queue_user;
   }
   
   public void setQueue_user(String queue_user)
   {
     this.queue_user = queue_user;
   }
   
   public MessageConsumer getReplyConsumer()
   {
     return this.replyConsumer;
   }
   
   public void setReplyConsumer(MessageConsumer replyConsumer)
   {
     this.replyConsumer = replyConsumer;
   }
   
   public Session getSession()
   {
     return this.session;
   }
   
   public void setSession(Session session)
   {
     this.session = session;
   }
   
   public long getSleepTime()
   {
     return this.sleepTime;
   }
   
   public void setSleepTime(long sleepTime)
   {
     this.sleepTime = sleepTime;
   }
   
   public String getSubject()
   {
     return this.subject;
   }
   
   public void setSubject(String subject)
   {
     this.subject = subject;
   }
   
   public long getTimeToLive()
   {
     return this.timeToLive;
   }
   
   public void setTimeToLive(long timeToLive)
   {
     this.timeToLive = timeToLive;
   }
   
   public boolean isTransacted()
   {
     return this.transacted;
   }
   
   public void setTransacted(boolean transacted)
   {
     this.transacted = transacted;
   }
   
   public MessageProducer getMessageProducer()
   {
     return this.messageProducer;
   }
   
   public void setMessageProducer(MessageProducer messageProducer)
   {
     this.messageProducer = messageProducer;
   }
 }



/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar

 * Qualified Name:     com.rancard.util.QueueProducer

 * JD-Core Version:    0.7.0.1

 */