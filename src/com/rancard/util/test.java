/*  1:   */ package com.rancard.util;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ import java.util.ArrayList;
/*  5:   */ import java.util.Date;
/*  6:   */ import javax.jms.JMSException;
/*  7:   */ import javax.jms.ObjectMessage;
/*  8:   */ 
/*  9:   */ public class test
/* 10:   */ {
/* 11:   */   public static void main(String[] args)
/* 12:   */   {
/* 13:30 */     ArrayList<String> msisdnList = new ArrayList();
/* 14:   */     try
/* 15:   */     {
/* 16:34 */       msisdnList.add("233277070711");
/* 17:35 */       msisdnList.add("2332777070711");
/* 18:36 */       msisdnList.add("0277070711");
/* 19:   */     }
/* 20:   */     catch (Exception e)
/* 21:   */     {
/* 22:39 */       System.out.println(e.getMessage());
/* 23:   */     }
/* 24:42 */     ArrayList<com.rancard.mobility.messaging.Message> msgObjs = new ArrayList();
/* 25:43 */     for (int i = 0; i < msisdnList.size(); i++)
/* 26:   */     {
/* 27:44 */       String msisdn = (String)msisdnList.get(i);
/* 28:   */       
/* 29:   */ 
/* 30:47 */       com.rancard.mobility.messaging.Message msgObject = new com.rancard.mobility.messaging.Message();
/* 31:48 */       msgObject.setBody("This messgae is going aout");
/* 32:49 */       msgObject.setCallBackUrl("");
/* 33:50 */       msgObject.setDispatchStatus("UNSENT");
/* 34:51 */       msgObject.setMessagingServiceRef(null);
/* 35:52 */       msgObject.setRecipient(msisdn);
/* 36:53 */       msgObject.setRecipientType("SMS");
/* 37:54 */       msgObject.setSender(msisdn);
/* 38:55 */       msgObject.setMessagingGatewayUsername("msfUserHJ");
/* 39:56 */       msgObject.setMessagingGatewayPassword("xxxxxXXXxxxxxx");
/* 40:57 */       msgObject.setMessagingGatewayUrl("http://192.168.1.249/rmcsserver");
/* 41:58 */       msgObject.setMessagingNode("myBuzz:801".split(":")[0]);
/* 42:   */       
/* 43:60 */       msgObjs.add(msgObject);
/* 44:   */     }
/* 45:63 */     QueueProducer me = new QueueProducer("myBuzz", (ArrayList)msgObjs)
/* 46:   */     {
/* 47:   */       public void onMessage(javax.jms.Message msg)
/* 48:   */       {
/* 49:   */         try
/* 50:   */         {
/* 51:69 */           if ((msg instanceof ObjectMessage))
/* 52:   */           {
/* 53:70 */             ObjectMessage objMsg = (ObjectMessage)msg;
/* 54:71 */             com.rancard.mobility.messaging.Message resp = (com.rancard.mobility.messaging.Message)objMsg.getObject();
/* 55:   */             
/* 56:73 */             System.out.println(new Date() + ":Received message: Account Id = " + resp.getBody() + " -- KWORD=" + resp.getMessagingGatewayUrl() + " -- MSISDN = " + resp.getRecipient());
/* 57:   */           }
/* 58:   */         }
/* 59:   */         catch (JMSException e)
/* 60:   */         {
/* 61:80 */           e.printStackTrace();
/* 62:   */         }
/* 63:   */       }
/* 64:84 */     };
/* 65:85 */     me.run();
/* 66:   */   }
/* 67:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.test
 * JD-Core Version:    0.7.0.1
 */