/*   1:    */ package com.rancard.mobility.contentserver.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.common.Message;
/*   4:    */ import com.rancard.mobility.contentserver.RepositoryManager;
/*   5:    */ import com.rancard.mobility.contentserver.uploadsBean;
/*   6:    */ import com.rancard.util.ExtManager;
/*   7:    */ import java.io.BufferedOutputStream;
/*   8:    */ import java.io.IOException;
/*   9:    */ import javax.servlet.ServletContext;
/*  10:    */ import javax.servlet.ServletException;
/*  11:    */ import javax.servlet.ServletOutputStream;
/*  12:    */ import javax.servlet.http.HttpServlet;
/*  13:    */ import javax.servlet.http.HttpServletRequest;
/*  14:    */ import javax.servlet.http.HttpServletResponse;
/*  15:    */ 
/*  16:    */ public class ContentPreview
/*  17:    */   extends HttpServlet
/*  18:    */ {
/*  19:    */   private static final String CONTENT_TYPE = "text/html";
/*  20:    */   private static final String DOC_TYPE = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
/*  21:    */   
/*  22:    */   public void init()
/*  23:    */     throws ServletException
/*  24:    */   {}
/*  25:    */   
/*  26:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  27:    */     throws ServletException, IOException
/*  28:    */   {
/*  29: 31 */     String s = request.getProtocol().toLowerCase();
/*  30: 32 */     s = s.substring(0, s.indexOf("/")).toLowerCase();
/*  31:    */     
/*  32:    */ 
/*  33:    */ 
/*  34: 36 */     String baseUrl = getServletContext().getInitParameter("contentServerPublicURL");
/*  35:    */     
/*  36: 38 */     Message replyPg = new Message();
/*  37:    */     
/*  38: 40 */     String listId = request.getParameter("listId");
/*  39: 41 */     String contentId = request.getParameter("id");
/*  40: 43 */     if (listId == null) {
/*  41: 44 */       listId = "";
/*  42:    */     }
/*  43: 46 */     if (contentId == null) {
/*  44: 47 */       contentId = "";
/*  45:    */     }
/*  46: 50 */     uploadsBean contentItem = null;
/*  47:    */     try
/*  48:    */     {
/*  49: 52 */       contentItem = RepositoryManager.fetchFile(listId, contentId);
/*  50: 53 */       int typeId = ExtManager.getTypeForFormat(contentItem.getFormat());
/*  51:    */       
/*  52: 55 */       String mimetype = new String();
/*  53: 56 */       if ((typeId == 1) || (typeId == 2) || (typeId == 3)) {
/*  54: 57 */         mimetype = "application/x-shockwave-flash";
/*  55: 58 */       } else if ((typeId == 4) || (typeId == 5) || (typeId == 6) || (typeId == 7)) {
/*  56: 59 */         mimetype = "image/jpeg";
/*  57: 60 */       } else if (typeId == 9) {
/*  58: 61 */         mimetype = "image/gif";
/*  59:    */       }
/*  60: 64 */       streamBinaryData(contentItem.getPreviewStream(), mimetype, response);
/*  61:    */     }
/*  62:    */     catch (Exception ex)
/*  63:    */     {
/*  64: 67 */       response.sendError(404);
/*  65:    */     }
/*  66:    */     finally
/*  67:    */     {
/*  68: 69 */       contentItem = null;
/*  69: 70 */       replyPg = null;
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/*  74:    */     throws ServletException, IOException
/*  75:    */   {
/*  76: 77 */     doGet(request, response);
/*  77:    */   }
/*  78:    */   
/*  79:    */   private boolean streamBinaryData(byte[] by, String formatMIMEType, HttpServletResponse response)
/*  80:    */     throws Exception
/*  81:    */   {
/*  82:135 */     boolean streamStatus = true;
/*  83:136 */     String ErrorStr = null;
/*  84:137 */     BufferedOutputStream bos = null;
/*  85:    */     
/*  86:139 */     ServletOutputStream sout = response.getOutputStream();
/*  87:    */     try
/*  88:    */     {
/*  89:141 */       response.setContentType(formatMIMEType);
/*  90:    */       
/*  91:143 */       sout.write(by);
/*  92:144 */       sout.flush();
/*  93:    */     }
/*  94:    */     catch (Exception e)
/*  95:    */     {
/*  96:146 */       streamStatus = false;
/*  97:147 */       e.printStackTrace();
/*  98:148 */       ErrorStr = "Error Streaming the Data";
/*  99:149 */       sout.println(ErrorStr);
/* 100:    */     }
/* 101:    */     finally
/* 102:    */     {
/* 103:    */       try
/* 104:    */       {
/* 105:152 */         if (bos != null) {
/* 106:153 */           bos.close();
/* 107:    */         }
/* 108:    */       }
/* 109:    */       catch (Exception e)
/* 110:    */       {
/* 111:157 */         e.printStackTrace();
/* 112:    */       }
/* 113:    */     }
/* 114:160 */     return streamStatus;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void destroy() {}
/* 118:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.ContentPreview
 * JD-Core Version:    0.7.0.1
 */