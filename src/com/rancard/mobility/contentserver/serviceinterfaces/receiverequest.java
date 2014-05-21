/*   1:    */ package com.rancard.mobility.contentserver.serviceinterfaces;
/*   2:    */ 
/*   3:    */ import com.rancard.util.URLUTF8Encoder;
/*   4:    */ import java.io.BufferedReader;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStreamReader;
/*   7:    */ import java.io.PrintWriter;
/*   8:    */ import java.net.URL;
/*   9:    */ import java.util.Iterator;
/*  10:    */ import java.util.StringTokenizer;
/*  11:    */ import javax.servlet.ServletException;
/*  12:    */ import javax.servlet.http.HttpServlet;
/*  13:    */ import javax.servlet.http.HttpServletRequest;
/*  14:    */ import javax.servlet.http.HttpServletResponse;
/*  15:    */ import org.dom4j.Document;
/*  16:    */ import org.dom4j.DocumentHelper;
/*  17:    */ import org.dom4j.Element;
/*  18:    */ 
/*  19:    */ public class receiverequest
/*  20:    */   extends HttpServlet
/*  21:    */ {
/*  22:    */   private static final String CONTENT_TYPE = "text/html";
/*  23: 14 */   private String TXT1 = "";
/*  24: 15 */   private String TXT2 = "";
/*  25: 16 */   private String PRC1 = "700";
/*  26: 17 */   private String PRC2 = "700";
/*  27: 18 */   private String sender = "";
/*  28: 19 */   private String siteId = "";
/*  29: 20 */   private String baseUrl = "";
/*  30: 21 */   private String data = "";
/*  31:    */   
/*  32:    */   public void init()
/*  33:    */     throws ServletException
/*  34:    */   {}
/*  35:    */   
/*  36:    */   public void doGet(HttpServletRequest request, HttpServletResponse response)
/*  37:    */     throws ServletException, IOException
/*  38:    */   {
/*  39: 31 */     String s = request.getProtocol().toLowerCase();
/*  40: 32 */     s = s.substring(0, s.indexOf("/")).toLowerCase();
/*  41: 33 */     this.baseUrl = (s + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
/*  42:    */     
/*  43: 35 */     PrintWriter pw = null;
/*  44: 36 */     InputStreamReader isr = null;
/*  45: 37 */     BufferedReader br = null;
/*  46:    */     try
/*  47:    */     {
/*  48: 41 */       isr = new InputStreamReader(request.getInputStream());
/*  49:    */       
/*  50: 43 */       this.siteId = request.getParameter("siteId");
/*  51:    */       
/*  52: 45 */       br = new BufferedReader(isr);
/*  53: 46 */       String line = new String();
/*  54: 47 */       String resp = new String();
/*  55: 48 */       while ((line = br.readLine()) != null) {
/*  56: 49 */         resp = resp + line + "\n";
/*  57:    */       }
/*  58: 53 */       if (processResponse(resp) == true)
/*  59:    */       {
/*  60: 55 */         String rsp = sendRequest(this.baseUrl);
/*  61:    */         
/*  62: 57 */         String xml = createReply(rsp, this.PRC1, this.TXT2, this.PRC2);
/*  63:    */         
/*  64: 59 */         pw = new PrintWriter(response.getOutputStream());
/*  65: 60 */         pw.println(xml);
/*  66: 61 */         pw.flush();
/*  67:    */       }
/*  68:    */       else
/*  69:    */       {
/*  70: 63 */         throw new Exception("7000");
/*  71:    */       }
/*  72:    */     }
/*  73:    */     catch (Exception e)
/*  74:    */     {
/*  75: 66 */       pw.println(e.getMessage());
/*  76:    */     }
/*  77:    */     finally
/*  78:    */     {
/*  79: 68 */       if (pw != null) {
/*  80: 69 */         pw.close();
/*  81:    */       }
/*  82:    */       try
/*  83:    */       {
/*  84: 72 */         br.close();
/*  85:    */       }
/*  86:    */       catch (Exception e) {}
/*  87:    */       try
/*  88:    */       {
/*  89: 76 */         isr.close();
/*  90:    */       }
/*  91:    */       catch (Exception e) {}
/*  92:    */     }
/*  93:    */   }
/*  94:    */   
/*  95:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/*  96:    */     throws ServletException, IOException
/*  97:    */   {
/*  98: 86 */     doGet(request, response);
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void destroy() {}
/* 102:    */   
/* 103:    */   private String createReply(String text1, String price1, String text2, String price2)
/* 104:    */   {
/* 105: 95 */     String xml = new String();
/* 106: 96 */     xml = "<?xml version=\"1.0\" encoding=\"UTF−8\" ?>\r\n<NotificationReply>\r\n<messsage>\r\n<text>" + text1 + "</text>" + "\r\n" + "<cost>" + price1 + "</cost>" + "\r\n" + "</messsage>" + "\r\n" + "<messsage>" + "\r\n" + "<text>" + text2 + "</text>" + "\r\n" + "<cost>" + price2 + "</cost>" + "\r\n" + "</messsage>" + "\r\n" + "</NotificationReply>" + "\r\n";
/* 107:    */     
/* 108:    */ 
/* 109:    */ 
/* 110:    */ 
/* 111:    */ 
/* 112:    */ 
/* 113:    */ 
/* 114:    */ 
/* 115:    */ 
/* 116:    */ 
/* 117:107 */     return xml;
/* 118:    */   }
/* 119:    */   
/* 120:    */   private boolean processResponse(String xmlDoc)
/* 121:    */   {
/* 122:111 */     boolean isOk = false;
/* 123:    */     try
/* 124:    */     {
/* 125:113 */       Document xml = DocumentHelper.parseText(xmlDoc);
/* 126:114 */       Element rootNode = xml.getRootElement();
/* 127:115 */       Element element = null;
/* 128:116 */       Iterator i = rootNode.elementIterator();
/* 129:117 */       while (i.hasNext())
/* 130:    */       {
/* 131:118 */         element = (Element)i.next();
/* 132:119 */         if (element.getName().equals("sender")) {
/* 133:120 */           this.sender = element.getText();
/* 134:    */         }
/* 135:122 */         if (element.getName().equals("parameters")) {
/* 136:123 */           this.data = element.element("text").getText();
/* 137:    */         }
/* 138:    */       }
/* 139:126 */       if ((this.sender != null) && (this.data != null)) {
/* 140:127 */         isOk = true;
/* 141:    */       }
/* 142:    */     }
/* 143:    */     catch (Exception e)
/* 144:    */     {
/* 145:130 */       isOk = false;
/* 146:    */     }
/* 147:132 */     return isOk;
/* 148:    */   }
/* 149:    */   
/* 150:    */   private String sendRequest(String baseurl)
/* 151:    */     throws Exception
/* 152:    */   {
/* 153:    */     try
/* 154:    */     {
/* 155:137 */       StringTokenizer st = new StringTokenizer(this.data, "., ");
/* 156:138 */       String listId = st.nextToken();
/* 157:139 */       if ((listId.equalsIgnoreCase("FON")) || (listId.equalsIgnoreCase("NORTHEND"))) {
/* 158:140 */         listId = "cp001";
/* 159:    */       }
/* 160:142 */       String keyword = st.nextToken();
/* 161:143 */       String body = st.nextToken();
/* 162:144 */       while (st.hasMoreTokens()) {
/* 163:145 */         body = body + st.nextToken();
/* 164:    */       }
/* 165:147 */       body = URLUTF8Encoder.encode(body);
/* 166:148 */       String number = URLUTF8Encoder.encode(this.sender);
/* 167:149 */       String requestUrl = baseurl + "rmcsservices.jsp?keyword=" + keyword + "&msg=" + body + "&msisdn=" + this.sender + "&siteId=" + this.siteId;
/* 168:    */       
/* 169:151 */       URL url = new URL(requestUrl);
/* 170:152 */       BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
/* 171:153 */       String temp = br.readLine();
/* 172:154 */       String resp = new String();
/* 173:155 */       while (temp != null)
/* 174:    */       {
/* 175:156 */         resp = resp + temp;
/* 176:157 */         temp = br.readLine();
/* 177:    */       }
/* 178:159 */       br.close();
/* 179:160 */       return resp;
/* 180:    */     }
/* 181:    */     catch (IOException ex2)
/* 182:    */     {
/* 183:162 */       throw new Exception("8000");
/* 184:    */     }
/* 185:    */   }
/* 186:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.receiverequest
 * JD-Core Version:    0.7.0.1
 */