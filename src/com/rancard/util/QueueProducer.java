/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.util;

//import com.rancard.common.MessageQueueObject;
import com.rancard.mobility.infoserver.common.services.UserService;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

/**
 *
 * @author john
 */
public abstract class QueueProducer implements Runnable, MessageListener {

    //Queue Params
    private Destination destination;
    private long sleepTime;
    private long timeToLive;
    private String queue_user = ActiveMQConnection.DEFAULT_USER;
    private String queue_password = ActiveMQConnection.DEFAULT_PASSWORD;
    private String queue_url = ActiveMQConnection.DEFAULT_BROKER_URL;
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

    public QueueProducer() {
        messageRef = "";
        service = new UserService();
        connection = getConnection();
        session = null;
        destination = null;
        sleepTime = 0;
        timeToLive = 0;
        queue_user = ActiveMQConnection.DEFAULT_USER;
        queue_password = ActiveMQConnection.DEFAULT_PASSWORD;
        queue_url = ActiveMQConnection.DEFAULT_BROKER_URL;
        subject = "";
        transacted = false;
        persistent = true;
        objectMessage = null;
        replyConsumer = null;
        messageProducer = null;
        session = null;
        queueMessages = null;
    }

    public QueueProducer(String queueName, UserService service, String messageRef) {
        this.messageRef = messageRef;
        this.service = service;
        this.connection = getConnection();
        this.session = null;
        this.destination = null;
        this.sleepTime = 0;
        this.timeToLive = 0;
        this.queue_user = ActiveMQConnection.DEFAULT_USER;
        this.queue_password = ActiveMQConnection.DEFAULT_PASSWORD;
        this.queue_url = ActiveMQConnection.DEFAULT_BROKER_URL;
        this.subject = queueName;
        this.transacted = false;
        this.persistent = true;
        this.objectMessage = null;
        this.replyConsumer = null;
        this.messageProducer = null;
        this.session = null;
        this.queueMessages = null;
    }

    public QueueProducer(String queueName, ArrayList<Object> messages) {
        this.messageRef = "";
        this.service = null;
        this.connection = getConnection();
        this.session = null;
        this.destination = null;
        this.sleepTime = 0;
        this.timeToLive = 0;
        this.queue_user = ActiveMQConnection.DEFAULT_USER;
        this.queue_password = ActiveMQConnection.DEFAULT_PASSWORD;
        this.queue_url = ActiveMQConnection.DEFAULT_BROKER_URL;
        this.subject = queueName;
        this.transacted = false;
        this.persistent = true;
        this.objectMessage = null;
        this.replyConsumer = null;
        this.messageProducer = null;
        this.session = null;
        this.queueMessages = messages;
    }

    private Connection getConnection() {
        Connection con = null;
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(queue_user, queue_password, queue_url);
            con = connectionFactory.createConnection();
            con.start();
        } catch (Exception e) {
            System.out.println(new java.util.Date() + ": [QUEUE EXCEPTION] Fatal Error " + e+ ((service == null) ? " (N/A)" : ( subject + "(acctId="+service.getAccountId()+"-keyword="+service.getKeyword()+")")));
            e.printStackTrace();
        }
        return con;
    }

    /*public void onMessage(Message message) {
        int completionCode = 0;
        int deliveryCount = 0;
        int billingCount = 0;
        try {

            if (message instanceof ObjectMessage) {
                ObjectMessage objMsg = (ObjectMessage) message;
                MessageQueueObject resp = (MessageQueueObject) objMsg.getObject();

                if (true) {
                } else {
                }
            }

        } catch (JMSException e) {
            System.out.println(new java.util.Date() + " :[RESPONSE QUEUE MONITOR]: Exception Caught: " + e + ((service == null) ? "(N/A)" : ( subject + "(acctId="+service.getAccountId()+"-keyword="+service.getKeyword()+")")));
            e.printStackTrace();
        } finally {
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                }
            }
        }
    }*/

     public synchronized void onException(JMSException ex) {
        //System.out.println(new java.util.Date() + " :[RESPONSE QUEUE MONITOR]: JMS Exception occured.  Shutting down client "+ ((service == null) ? "(N/A)" : ( subject + "(acctId="+service.getAccountId()+"-keyword="+service.getKeyword()+")")));
        throw new UnsupportedOperationException(ex);
     }


    public void run() {

        boolean canSend = initializeQueue();

        if (canSend && queueMessages != null) {
            try {
                //Start pushing messages into queue
                Iterator<Object> messageObjectList = queueMessages.iterator();
                while (messageObjectList.hasNext()) {
                    //create message queue object
                    Object msgObject = messageObjectList.next();


                    objectMessage = session.createObjectMessage();
                    objectMessage.setObject((java.io.Serializable)msgObject);

                    messageProducer.send(objectMessage);

                    if (transacted) {
                        session.commit();
                    }

                    Thread.sleep(sleepTime);
                }
            } catch (Exception ex) {
                System.out.println(new java.util.Date() + " :[RESPONSE QUEUE MONITOR]: JMS Exception occured while submiting message to queue :: "+subject+":REQUEST");
            }
        }
    }

    private boolean initializeQueue() {
        //Set up queue for use
        boolean canSend = false;

        //use this one -- >> before sending messages into queue, ...
        // ... create connection to queue -- already done in contractur

        try {
            // ... create a session
            if (subject != null && !subject.equals("")) {
                session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);
                destination = session.createQueue(subject + ":REQUEST");
                canSend = true;
            }else{
                System.out.println(new java.util.Date() + ": [queueProcess:ERROR] Queue name cannot be null  (acctId="+service.getAccountId()+"-keyword="+service.getKeyword()+")");
            }
        } catch (JMSException ex) {
            canSend = false;
        }

        if (session != null && destination != null && canSend == true) {
            try {
                // ... create the producer
                messageProducer = session.createProducer(destination);
                System.out.println(new java.util.Date() + ": [queueProcess:INFO] Successfully created the message request queue :: " + subject + ":REQUEST "+ ((service == null) ? "(N/A)" : ( subject + "(acctId="+service.getAccountId()+"-keyword="+service.getKeyword()+")")));
                if (persistent) {
                    messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
                } else {
                    messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                }
                if (timeToLive != 0) {
                    messageProducer.setTimeToLive(timeToLive);
                }

                // ... create the reply consumer
                replyConsumer = session.createConsumer(session.createQueue(subject + ":RESPONSE"));
                replyConsumer.setMessageListener(this);
                System.out.println(new java.util.Date() + ": [queueProcess:INFO] Successfully Connected to message response queue :: " + subject + ":RESPONSE "+ ((service == null) ? "(N/A)" : ( subject + "(acctId="+service.getAccountId()+"-keyword="+service.getKeyword()+")")));


            } catch (JMSException ex) {

                canSend = false;
            }
        }

        return canSend;
    }

    //Getters and Setters
    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public ObjectMessage getObjectMessage() {
        return objectMessage;
    }

    public void setObjectMessage(ObjectMessage objectMessage) {
        this.objectMessage = objectMessage;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public ArrayList<Object> getQueueMessages() {
        return queueMessages;
    }

    public void setQueueMessages(ArrayList<Object> queueMessages) {
        this.queueMessages = queueMessages;
    }

    public String getQueue_password() {
        return queue_password;
    }

    public void setQueue_password(String queue_password) {
        this.queue_password = queue_password;
    }

    public String getQueue_url() {
        return queue_url;
    }

    public void setQueue_url(String queue_url) {
        this.queue_url = queue_url;
    }

    public String getQueue_user() {
        return queue_user;
    }

    public void setQueue_user(String queue_user) {
        this.queue_user = queue_user;
    }

    public MessageConsumer getReplyConsumer() {
        return replyConsumer;
    }

    public void setReplyConsumer(MessageConsumer replyConsumer) {
        this.replyConsumer = replyConsumer;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public boolean isTransacted() {
        return transacted;
    }

    public void setTransacted(boolean transacted) {
        this.transacted = transacted;
    }

    public MessageProducer getMessageProducer() {
        return messageProducer;
    }

    public void setMessageProducer(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }
}
