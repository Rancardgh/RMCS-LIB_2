/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.mobility.infoserver.feeds;

import com.rancard.common.DConnect;
import com.rancard.mobility.common.TempBatchTransmission;
import com.rancard.mobility.infoserver.InfoService;
import com.rancard.mobility.infoserver.InfoServiceDB;
import com.rancard.mobility.contentserver.CPConnections;
import com.rancard.mobility.infoserver.common.services.UserService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rancard.mobility.messaging.Message;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

/**
 *
 * @author ktandoh
 */
public class ContentDispatcher implements Runnable, MessageListener {

    private Date date;
    private String messageRef;
    private UserService service;
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
    private Session session;
    private Connection connection;

    public ContentDispatcher() {
        date = new Date();
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
        session = null;
    }

    public ContentDispatcher(Date date, UserService service, String messageRef) {
        this.date = date;
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
        this.subject = service.getAccountId() + ":" + (service.getKeyword().replaceAll(" ", "_")).toUpperCase();
        this.transacted = false;
        this.persistent = true;
        this.objectMessage = null;
        this.replyConsumer = null;
        this.session = null;
    }

    public void run() {

        //get content
        //-----------------------------------------------------------------------------
        InfoService outboundMessage = new InfoService();

        /* try {
        outboundMessage = InfoServiceDB.retrieveOutboundMessage (service, date, messageRef);
        } catch (Exception e) {
        //loggin statement
        System.out.println (new java.util.Date () + ":error: " + e.getMessage ());
        return;
        }*/

        String info = "";
        if (outboundMessage.getMessage() == null || outboundMessage.getMessage().equals("")) {
            info = outboundMessage.getDefaultMessage();
        } else {
            info = outboundMessage.getMessage();
        }
        //========================================================


        //extract tags
        //-----------------------------------------------------------------------------
        String tagString = outboundMessage.getTags();
        String tags = "'" + service.getKeyword() + "'";

        if (tagString != null && !tagString.equals("")) {
            //build a string in the format 'string1','string2','string3'
            String[] tagArray = tagString.split(",");
            tagString = "'" + tagArray[0].trim() + "'";

            for (int i = 1; i < tagArray.length; i++) {
                tagString = tagString + ",'" + tagArray[i] + "'";
            }
            tags = tags + "," + tagString;
        }
        //========================================================


        //find subscribers who subscribed based on tags
        //-----------------------------------------------------------------------------
        ArrayList<String> msisdnList = new ArrayList();
        try {
            msisdnList = retrieveSubscribers(service, tags);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        boolean canSend = false;

        //use this one -- >> before sending messages into queue, ...
        // ... create connection to queue -- already done in contractur

        try {
            // ... create a session
            session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(subject + ":REQUEST");
            canSend = true;
        } catch (JMSException ex) {
            canSend = false;
        }

        if (session != null && destination != null && canSend == true) {
            try {
                // ... create the producer
                MessageProducer producer = session.createProducer(destination);
                if (persistent) {
                    producer.setDeliveryMode(DeliveryMode.PERSISTENT);
                } else {
                    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                }
                if (timeToLive != 0) {
                    producer.setTimeToLive(timeToLive);
                }

                CPConnections cnxn = null;
                //now, start pumping those messages into queue
                for (int i = 0; i < msisdnList.size(); i++) {
                    String msisdn = msisdnList.get(i);

                    //create message queue object
                    Message msgObject = new Message();
                    msgObject.setBody(outboundMessage.getMessage());
                    msgObject.setCallBackUrl("");
                    msgObject.setDispatchStatus(Message.DISPATCH_UNSENT);
                    msgObject.setMessagingServiceRef(service.getAccountId () + ":" + service.getKeyword ().toUpperCase ());
                    msgObject.setRecipient(msisdn);
                    msgObject.setRecipientType(Message.SMS_RECIPIENT);
                    msgObject.setSender(msisdn);

                    //set cp connection  params where possible or drop processing of request
                    if (cnxn == null) {
                        try {
                            cnxn = new CPConnections();
                            /*cnxn = CPConnections.getConnection(service.getAccountId(), msisdn);

                            msgObject.setConUsername(cnxn.getUsername());
                            msgObject.setConPassword(cnxn.getPassword());
                            msgObject.setConGatewayUrl(cnxn.getGatewayURL());
                            msgObject.setConSmsc(cnxn.getConnection().split(":")[0]);*/

                            msgObject.setMessagingGatewayUsername ("msfUserHJ");
                            msgObject.setMessagingGatewayPassword ("xxxxxXXXxxxxxx");
                            msgObject.setMessagingGatewayUrl ("http://192.168.1.249/rmcsserver");
                            msgObject.setMessagingNode ("myBuzz:801".split(":")[0]);

                        } catch (Exception ex) {
                            System.out.println(new java.util.Date() + ": [bbcFballMtnNg:ERROR]: Error. Can not submit alert to this user .(msisdn=" + msisdn + " on acctId =" + service.getAccountId() + ")");
                            cnxn = null;
                            continue;
                        }

                    }

                    objectMessage = session.createObjectMessage();
                    objectMessage.setObject(msgObject);

                    producer.send(objectMessage);

                    if (transacted) {
                        session.commit();
                    }
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException ex) {
                    }
                }

            } catch (JMSException ex) {
            }
        }

    }

    private ArrayList<String> retrieveSubscribers(UserService service, String tags) throws Exception {

        ArrayList<String> msisdnList = new ArrayList<String>();

        String SQL;
        ResultSet rs = null;
        java.sql.Connection con = null;
        PreparedStatement prepstat = null;

        //------log Statement
        System.out.println(new java.util.Date() + ":@com.rancard.mobility.infoserver.feeds.ContentDispatcher..");
        System.out.println(new java.util.Date() + ": retrieving subscribers for keyword in " + tags);

        try {

            SQL = "select distinct msisdn from service_subscription where keyword in (" + tags
                    + ") and account_id ='" + service.getAccountId() + "'";

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

            System.out.println(new java.util.Date() + ": error retrieving msisdns for " + service.getKeyword() + "/" + service.getAccountId()
                    + " combination: " + ex.getMessage());

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

            if (message instanceof ObjectMessage) {
                ObjectMessage objMsg = (ObjectMessage) message;
                Message resp = (Message) objMsg.getObject();

                if (true) {
                } else {
                }
            }

        } catch (JMSException e) {
            System.out.println(new java.util.Date() + " :[RESPONSE QUEUE MONITOR]: Exception Caught: " + e);
            e.printStackTrace();
        } finally {
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public synchronized void onException(JMSException ex) {
        System.out.println(new java.util.Date() + " :[RESPONSE QUEUE MONITOR]: JMS Exception occured.  Shutting down client");
        //running = false;
    }

    public void objectMessageListener() {

        try {
            replyConsumer = session.createConsumer(session.createQueue(subject + "_RESPONSE"));
            System.out.println(new java.util.Date() + ": About to Monitor Bill Response Messages... ::");

            replyConsumer.setMessageListener(this);
        } catch (Exception e) {
            System.out.println(new java.util.Date() + ": [RESPONSE QUEUE MONITOR] Exception Caught: " + e);
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        Connection con = null;
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(queue_user, queue_password, queue_url);
            con = connectionFactory.createConnection();
            con.start();
        } catch (Exception e) {
            System.out.println(new java.util.Date() + ": [QUEUE EXCEPTION] Caught " + e);
            e.printStackTrace();
        }
        return con;
    }
}
