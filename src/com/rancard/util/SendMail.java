/*  1:   */ package com.rancard.util;
/*  2:   */ 
/*  3:   */ import java.util.Properties;
/*  4:   */ import javax.mail.Message;
/*  5:   */ import javax.mail.Message.RecipientType;
/*  6:   */ import javax.mail.MessagingException;
/*  7:   */ import javax.mail.Session;
/*  8:   */ import javax.mail.Transport;
/*  9:   */ import javax.mail.internet.AddressException;
/* 10:   */ import javax.mail.internet.InternetAddress;
/* 11:   */ import javax.mail.internet.MimeMessage;
/* 12:   */ 
/* 13:   */ public class SendMail
/* 14:   */ {
/* 15:   */   Properties props;
/* 16:   */   
/* 17:   */   public SendMail(String host, String port, String localhost)
/* 18:   */   {
/* 19:26 */     this.props = new Properties();
/* 20:27 */     this.props.put("mail.smtp.host", host);
/* 21:28 */     this.props.put("mail.smtp.port", port);
/* 22:29 */     this.props.put("mail.smtp.localhost", localhost);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void send(String from, String to, String subject, String text)
/* 26:   */   {
/* 27:34 */     Session mailSession = Session.getDefaultInstance(this.props);
/* 28:35 */     Message simpleMessage = new MimeMessage(mailSession);
/* 29:   */     
/* 30:37 */     InternetAddress fromAddress = null;
/* 31:38 */     InternetAddress toAddress = null;
/* 32:   */     try
/* 33:   */     {
/* 34:40 */       fromAddress = new InternetAddress(from);
/* 35:41 */       toAddress = new InternetAddress(to);
/* 36:   */     }
/* 37:   */     catch (AddressException e)
/* 38:   */     {
/* 39:44 */       e.printStackTrace();
/* 40:   */     }
/* 41:   */     try
/* 42:   */     {
/* 43:48 */       simpleMessage.setFrom(fromAddress);
/* 44:49 */       simpleMessage.setRecipient(Message.RecipientType.TO, toAddress);
/* 45:50 */       simpleMessage.setSubject(subject);
/* 46:51 */       simpleMessage.setText(text);
/* 47:   */       
/* 48:53 */       Transport.send(simpleMessage);
/* 49:   */     }
/* 50:   */     catch (MessagingException e)
/* 51:   */     {
/* 52:56 */       e.printStackTrace();
/* 53:   */     }
/* 54:   */   }
/* 55:   */   
/* 56:   */   public static void main(String[] args)
/* 57:   */     throws Exception
/* 58:   */   {
/* 59:62 */     SendMail mailutils = new SendMail("mail.rancardsolutions.com", "25", "127.0.0.1");
/* 60:63 */     mailutils.send("Rancard Notification System<auto-notif@rancardsolutions.com>", "nii.ako@rancardsolutions.com", "Subject", "Body goes here...");
/* 61:   */   }
/* 62:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.util.SendMail
 * JD-Core Version:    0.7.0.1
 */