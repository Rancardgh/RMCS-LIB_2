


package com.rancard.mobility.infoserver.feeds;

import com.rancard.common.DConnect;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.infoserver.InfoService;
import com.rancard.mobility.infoserver.common.services.UserService;

import java.io.PrintStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ContentDispatcher
        implements Runnable, MessageListener {
    private Date date;
    private String messageRef;
    private UserService service;
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
    private Session session;
    private javax.jms.Connection connection;

    public ContentDispatcher() {
        this.date = new Date();
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
        this.session = null;
    }

    public ContentDispatcher(Date date, UserService service, String messageRef) {
        this.date = date;
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
        this.subject = (service.getAccountId() + ":" + service.getKeyword().replaceAll(" ", "_").toUpperCase());
        this.transacted = false;
        this.persistent = true;
        this.objectMessage = null;
        this.replyConsumer = null;
        this.session = null;
    }

    public void run() {
        InfoService outboundMessage = new InfoService();


        String info = "";
        if ((outboundMessage.getMessage() == null) || (outboundMessage.getMessage().equals(""))) {
            info = outboundMessage.getDefaultMessage();
        } else {
            info = outboundMessage.getMessage();
        }
        String tagString = outboundMessage.getTags();
        String tags = "'" + this.service.getKeyword() + "'";
        if ((tagString != null) && (!tagString.equals(""))) {
            String[] tagArray = tagString.split(",");
            tagString = "'" + tagArray[0].trim() + "'";
            for (int i = 1; i < tagArray.length; i++) {
                tagString = tagString + ",'" + tagArray[i] + "'";
            }
            tags = tags + "," + tagString;
        }
        ArrayList<String> msisdnList = new ArrayList();
        try {
            msisdnList = retrieveSubscribers(this.service, tags);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        boolean canSend = false;
        try {
            this.session = this.connection.createSession(this.transacted, 1);
            this.destination = this.session.createQueue(this.subject + ":REQUEST");
            canSend = true;
        } catch (JMSException ex) {
            canSend = false;
        }
        if ((this.session != null) && (this.destination != null) && (canSend == true)) {
            try {
                MessageProducer producer = this.session.createProducer(this.destination);
                if (this.persistent) {
                    producer.setDeliveryMode(2);
                } else {
                    producer.setDeliveryMode(1);
                }
                if (this.timeToLive != 0L) {
                    producer.setTimeToLive(this.timeToLive);
                }
                CPConnections cnxn = null;
                for (int i = 0; i < msisdnList.size(); i++) {
                    String msisdn = (String) msisdnList.get(i);


                    com.rancard.mobility.messaging.Message msgObject = new com.rancard.mobility.messaging.Message();
                    msgObject.setBody(outboundMessage.getMessage());
                    msgObject.setCallBackUrl("");
                    msgObject.setDispatchStatus("UNSENT");
                    msgObject.setMessagingServiceRef(this.service.getAccountId() + ":" + this.service.getKeyword().toUpperCase());
                    msgObject.setRecipient(msisdn);
                    msgObject.setRecipientType("SMS");
                    msgObject.setSender(msisdn);
                    if (cnxn == null) {
                        try {
                            cnxn = new CPConnections();


                            msgObject.setMessagingGatewayUsername("msfUserHJ");
                            msgObject.setMessagingGatewayPassword("xxxxxXXXxxxxxx");
                            msgObject.setMessagingGatewayUrl("http://192.168.1.249/rmcsserver");
                            msgObject.setMessagingNode("myBuzz:801".split(":")[0]);
                        } catch (Exception ex) {
                            System.out.println(new Date() + ": [bbcFballMtnNg:ERROR]: Error. Can not submit alert to this user .(msisdn=" + msisdn + " on acctId =" + this.service.getAccountId() + ")");
                            cnxn = null;
                            continue;
                        }
                    }
                    this.objectMessage = this.session.createObjectMessage();
                    this.objectMessage.setObject(msgObject);

                    producer.send(this.objectMessage);
                    if (this.transacted) {
                        this.session.commit();
                    }
                    try {
                        Thread.sleep(this.sleepTime);
                    } catch (InterruptedException ex) {
                    }
                }
            } catch (JMSException ex) {
            }
        }
    }

    private ArrayList<String> retrieveSubscribers(UserService service, String tags)
            throws Exception {
        ArrayList<String> msisdnList = new ArrayList();


        ResultSet rs = null;
        java.sql.Connection con = null;
        PreparedStatement prepstat = null;


        System.out.println(new Date() + ":@com.rancard.mobility.infoserver.feeds.ContentDispatcher..");
        System.out.println(new Date() + ": retrieving subscribers for keyword in " + tags);
        try {
            String SQL = "select distinct msisdn from service_subscription where keyword in (" + tags + ") and account_id ='" + service.getAccountId() + "'";


            con = DConnect.getConnection();
            prepstat = con.prepareStatement(SQL);
            rs = prepstat.executeQuery();
            while (rs.next()) {
                msisdnList.add(rs.getString("msisdn"));
            }
        } catch (Exception ex) {
            if (con != null) {
                con.close();
            }
            System.out.println(new Date() + ": error retrieving msisdns for " + service.getKeyword() + "/" + service.getAccountId() + " combination: " + ex.getMessage());


            throw new Exception(ex.getMessage());
        } finally {
            if (prepstat != null) {
                prepstat.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return msisdnList;
    }

    public void onMessage(javax.jms.Message message) {
        int completionCode = 0;
        int deliveryCount = 0;
        int billingCount = 0;
        try {
            com.rancard.mobility.messaging.Message resp;
            if ((message instanceof ObjectMessage)) {
                ObjectMessage objMsg = (ObjectMessage) message;
                resp = (com.rancard.mobility.messaging.Message) objMsg.getObject();
            }
            if (this.sleepTime > 0L) {
                try {
                    Thread.sleep(this.sleepTime);
                } catch (InterruptedException e) {
                }
            }
            return;
        } catch (JMSException e) {
            System.out.println(new Date() + " :[RESPONSE QUEUE MONITOR]: Exception Caught: " + e);
            e.printStackTrace();
            if (this.sleepTime > 0L) {
                try {
                    Thread.sleep(this.sleepTime);
                } catch (InterruptedException ee) {
                }
            }
        } finally {
            if (this.sleepTime > 0L) {
                try {
                    Thread.sleep(this.sleepTime);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public synchronized void onException(JMSException ex) {
        System.out.println(new Date() + " :[RESPONSE QUEUE MONITOR]: JMS Exception occured.  Shutting down client");
    }

    public void objectMessageListener() {
        try {
            this.replyConsumer = this.session.createConsumer(this.session.createQueue(this.subject + "_RESPONSE"));
            System.out.println(new Date() + ": About to Monitor Bill Response Messages... ::");

            this.replyConsumer.setMessageListener(this);
        } catch (Exception e) {
            System.out.println(new Date() + ": [RESPONSE QUEUE MONITOR] Exception Caught: " + e);
            e.printStackTrace();
        }
    }

    private javax.jms.Connection getConnection() {
        javax.jms.Connection con = null;
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.queue_user, this.queue_password, this.queue_url);
            con = connectionFactory.createConnection();
            con.start();
        } catch (Exception e) {
            System.out.println(new Date() + ": [QUEUE EXCEPTION] Caught " + e);
            e.printStackTrace();
        }
        return con;
    }
}

















 