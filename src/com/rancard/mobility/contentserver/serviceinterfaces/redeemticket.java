/*   1:    */ package com.rancard.mobility.contentserver.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.common.Message;
/*   4:    */ import com.rancard.mobility.contentserver.ContentItem;
/*   5:    */ import com.rancard.mobility.contentserver.Transaction;
/*   6:    */ import java.io.IOException;
/*   7:    */ import java.io.PrintWriter;
/*   8:    */ import javax.servlet.ServletException;
/*   9:    */ import javax.servlet.http.HttpServlet;
/*  10:    */ import javax.servlet.http.HttpServletRequest;
/*  11:    */ import javax.servlet.http.HttpServletResponse;
/*  12:    */ import org.apache.commons.httpclient.HttpClient;
/*  13:    */ import org.apache.commons.httpclient.HttpException;
/*  14:    */ import org.apache.commons.httpclient.methods.GetMethod;
/*  15:    */ 
/*  16:    */ public class redeemticket
/*  17:    */   extends HttpServlet
/*  18:    */ {
/*  19:    */   private static final String CONTENT_TYPE = "text/html";
/*  20:    */   
/*  21:    */   public void init()
/*  22:    */     throws ServletException
/*  23:    */   {}
/*  24:    */   
/*  25:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  26:    */     throws ServletException, IOException
/*  27:    */   {
/*  28: 24 */     String ticketId = request.getParameter("ticketId");
/*  29: 25 */     Message reply = new Message();
/*  30: 26 */     PrintWriter out = response.getWriter();
/*  31:    */     
/*  32: 28 */     String s = request.getProtocol().toLowerCase();
/*  33: 29 */     s = s.substring(0, s.indexOf("/")).toLowerCase();
/*  34: 30 */     String baseUrl = s + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
/*  35:    */     try
/*  36:    */     {
/*  37: 36 */       if ((ticketId == null) || (ticketId.equals(""))) {
/*  38: 37 */         throw new Exception();
/*  39:    */       }
/*  40:    */     }
/*  41:    */     catch (Exception e)
/*  42:    */     {
/*  43: 40 */       reply.setMessage("3000");
/*  44: 41 */       reply.setStatus(false);
/*  45: 42 */       out.println(reply.getMessage());
/*  46: 43 */       return;
/*  47:    */     }
/*  48: 47 */     Transaction failedDownload = new Transaction();
/*  49:    */     
/*  50:    */ 
/*  51: 50 */     String formatId = new String();
/*  52:    */     try
/*  53:    */     {
/*  54: 53 */       failedDownload = Transaction.viewTransaction(ticketId);
/*  55: 54 */       formatId = failedDownload.getTicketID().split("-")[1];
/*  56:    */     }
/*  57:    */     catch (Exception e)
/*  58:    */     {
/*  59: 56 */       reply.setMessage("3001");
/*  60: 57 */       reply.setStatus(false);
/*  61: 58 */       out.println(reply.getMessage());
/*  62: 59 */       return;
/*  63:    */     }
/*  64: 63 */     if ((!failedDownload.getTicketID().equals("")) && (!failedDownload.getDownloadCompleted()))
/*  65:    */     {
/*  66: 68 */       ContentItem item = failedDownload.getContentItem();
/*  67:    */       
/*  68:    */ 
/*  69: 71 */       String initDownloadRUL = baseUrl + "senddownloadrequest?id=" + item.getid() + "&listId=" + item.getListId() + "&formatId=" + formatId + "&to=" + failedDownload.getSubscriberMSISDN() + "&pin=" + failedDownload.getPin() + "&phoneId=" + failedDownload.getPhoneId();
/*  70:    */       
/*  71:    */ 
/*  72:    */ 
/*  73:    */ 
/*  74:    */ 
/*  75:    */ 
/*  76: 78 */       HttpClient httpclient = new HttpClient();
/*  77: 79 */       GetMethod httpget = new GetMethod(initDownloadRUL);
/*  78:    */       try
/*  79:    */       {
/*  80: 82 */         int statusCode = httpclient.executeMethod(httpget);
/*  81: 83 */         if ((statusCode == 400) || (statusCode == 409) || (statusCode == 500) || (statusCode == 408)) {
/*  82: 87 */           throw new HttpException(new Integer(statusCode).toString());
/*  83:    */         }
/*  84: 89 */         Transaction.removeTransaction(failedDownload.getTicketID());
/*  85:    */       }
/*  86:    */       catch (Exception e)
/*  87:    */       {
/*  88: 91 */         reply.setMessage("Error xxx: Server Error." + e.getMessage());
/*  89: 92 */         reply.setStatus(false);
/*  90: 93 */         out.println(reply.getMessage());
/*  91:    */       }
/*  92:    */       finally
/*  93:    */       {
/*  94: 96 */         httpget.releaseConnection();
/*  95:    */       }
/*  96:    */     }
/*  97:    */     else
/*  98:    */     {
/*  99:100 */       reply.setMessage("3000");
/* 100:101 */       reply.setStatus(false);
/* 101:102 */       out.println(reply.getMessage());
/* 102:    */     }
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/* 106:    */     throws ServletException, IOException
/* 107:    */   {
/* 108:110 */     doGet(request, response);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void destroy() {}
/* 112:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.redeemticket
 * JD-Core Version:    0.7.0.1
 */