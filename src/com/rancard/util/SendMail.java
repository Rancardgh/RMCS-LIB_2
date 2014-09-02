 package com.rancard.util;
 
 import java.util.Properties;
 import javax.mail.Message;
 import javax.mail.Message.RecipientType;
 import javax.mail.MessagingException;
 import javax.mail.Session;
 import javax.mail.Transport;
 import javax.mail.internet.AddressException;
 import javax.mail.internet.InternetAddress;
 import javax.mail.internet.MimeMessage;
 
 public class SendMail
 {
   Properties props;
   
   public SendMail(String host, String port, String localhost)
   {
     this.props = new Properties();
     this.props.put("mail.smtp.host", host);
     this.props.put("mail.smtp.port", port);
     this.props.put("mail.smtp.localhost", localhost);
   }
   
   public void send(String from, String to, String subject, String text)
   {
     Session mailSession = Session.getDefaultInstance(this.props);
     Message simpleMessage = new MimeMessage(mailSession);
     
     InternetAddress fromAddress = null;
     InternetAddress toAddress = null;
     try
     {
       fromAddress = new InternetAddress(from);
       toAddress = new InternetAddress(to);
     }
     catch (AddressException e)
     {
       e.printStackTrace();
     }
     try
     {
       simpleMessage.setFrom(fromAddress);
       simpleMessage.setRecipient(Message.RecipientType.TO, toAddress);
       simpleMessage.setSubject(subject);
       simpleMessage.setText(text);
       
       Transport.send(simpleMessage);
     }
     catch (MessagingException e)
     {
       e.printStackTrace();
     }
   }
   
   public static void main(String[] args)
     throws Exception
   {
     SendMail mailutils = new SendMail("mail.rancardsolutions.com", "25", "127.0.0.1");
     mailutils.send("Rancard Notification System<auto-notif@rancardsolutions.com>", "nii.ako@rancardsolutions.com", "Subject", "Body goes here...");
   }
 }



/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar

 * Qualified Name:     com.rancard.util.SendMail

 * JD-Core Version:    0.7.0.1

 */