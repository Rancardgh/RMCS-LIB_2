/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rancard.util;

import com.rancard.mobility.infoserver.feeds.*;
import com.rancard.mobility.messaging.Message;
//import com.rancard.util.QueueProducer;
import com.rancard.util.QueueProducer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;

/**
 *
 * @author john
 */
public class test {

    public static void main(String[] args) {
        //ContentDispatcher deq = new ContentDispatcher();
        //deq.run();
        //find subscribers who subscribed based on tags
        //-----------------------------------------------------------------------------
        ArrayList<String> msisdnList = new ArrayList();
        try {
            //msisdnList = retrieveSubscribers(service, tags);

            msisdnList.add("233277070711");
            msisdnList.add("2332777070711");
            msisdnList.add("0277070711");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        ArrayList<Message> msgObjs = new ArrayList();
        for (int i = 0; i < msisdnList.size(); i++) {
            String msisdn = msisdnList.get(i);

            //create message queue object
            Message msgObject = new Message();
            msgObject.setBody("This messgae is going aout");
            msgObject.setCallBackUrl("");
            msgObject.setDispatchStatus(Message.DISPATCH_UNSENT);
            msgObject.setMessagingServiceRef(null);
            msgObject.setRecipient(msisdn);
            msgObject.setRecipientType(Message.SMS_RECIPIENT);
            msgObject.setSender(msisdn);
            msgObject.setMessagingGatewayUsername("msfUserHJ");
            msgObject.setMessagingGatewayPassword("xxxxxXXXxxxxxx");
            msgObject.setMessagingGatewayUrl("http://192.168.1.249/rmcsserver");
            msgObject.setMessagingNode("myBuzz:801".split(":")[0]);

            msgObjs.add(msgObject);
        }

        QueueProducer me =  new QueueProducer("myBuzz",(ArrayList<Object>)(Object) msgObjs) {

            public void onMessage(javax.jms.Message msg) {
                //throw new UnsupportedOperationException("Not supported yet.");
                try {

            if (msg instanceof ObjectMessage) {
                ObjectMessage objMsg = (ObjectMessage) msg;
                Message resp = (Message) objMsg.getObject();

                System.out.println(new java.util.Date() + ":Received message: Account Id = " + resp.getBody() + " -- KWORD=" + resp.getMessagingGatewayUrl ()
                        + " -- MSISDN = " + resp.getRecipient());

            }

        } catch (JMSException e) {
            //System.out.println(new java.util.Date() + " :[RESPONSE QUEUE MONITOR]: Exception Caught: " + e + ((service == null) ? "(N/A)" : ( subject + "(acctId="+service.getAccountId()+"-keyword="+service.getKeyword()+")")));
            e.printStackTrace();
        }
            }
        };
        //me.setQueueMessages(msgObjs);
        me.run();
    }
}
