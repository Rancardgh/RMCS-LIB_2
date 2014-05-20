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
/*  72:    */       return;
/*  73:    */     }
/*  74:    */     catch (Exception e)
/*  75:    */     {
/*  76: 66 */       pw.println(e.getMessage());
/*  77:    */     }
/*  78:    */     finally
/*  79:    */     {
/*  80: 68 */       if (pw != null) {
/*  81: 69 */         pw.close();
/*  82:    */       }
/*  83:    */       try
/*  84:    */       {
/*  85: 72 */         br.close();
/*  86:    */       }
/*  87:    */       catch (Exception e) {}
/*  88:    */       try
/*  89:    */       {
/*  90: 76 */         isr.close();
/*  91:    */       }
/*  92:    */       catch (Exception e) {}
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void doPost(HttpServletRequest request, HttpServletResponse response)
/*  97:    */     throws ServletException, IOException
/*  98:    */   {
/*  99: 86 */     doGet(request, response);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void destroy() {}
/* 103:    */   
/* 104:    */   private String createReply(String text1, String price1, String text2, String price2)
/* 105:    */   {
/* 106: 95 */     String xml = new String();
/* 107: 96 */     xml = "<?xml version=\"1.0\" encoding=\"UTF−8\" ?>\r\n<NotificationReply>\r\n<messsage>\r\n<text>" + text1 + "</text>" + "\r\n" + "<cost>" + price1 + "</cost>" + "\r\n" + "</messsage>" + "\r\n" + "<messsage>" + "\r\n" + "<text>" + text2 + "</text>" + "\r\n" + "<cost>" + price2 + "</cost>" + "\r\n" + "</messsage>" + "\r\n" + "</NotificationReply>" + "\r\n";
/* 108:    */     
/* 109:    */ 
/* 110:    */ 
/* 111:    */ 
/* 112:    */ 
/* 113:    */ 
/* 114:    */ 
/* 115:    */ 
/* 116:    */ 
/* 117:    */ 
/* 118:107 */     return xml;
/* 119:    */   }
/* 120:    */   
/* 121:    */   private boolean processResponse(String xmlDoc)
/* 122:    */   {
/* 123:111 */     boolean isOk = false;
/* 124:    */     try
/* 125:    */     {
/* 126:113 */       Document xml = DocumentHelper.parseText(xmlDoc);
/* 127:114 */       Element rootNode = xml.getRootElement();
/* 128:115 */       Element element = null;
/* 129:116 */       Iterator i = rootNode.elementIterator();
/* 130:117 */       while (i.hasNext())
/* 131:    */       {
/* 132:118 */         element = (Element)i.next();
/* 133:119 */         if (element.getName().equals("sender")) {
/* 134:120 */           this.sender = element.getText();
/* 135:    */         }
/* 136:122 */         if (element.getName().equals("parameters")) {
/* 137:123 */           this.data = element.element("text").getText();
/* 138:    */         }
/* 139:    */       }
/* 140:126 */       if ((this.sender != null) && (this.data != null)) {
/* 141:127 */         isOk = true;
/* 142:    */       }
/* 143:    */     }
/* 144:    */     catch (Exception e)
/* 145:    */     {
/* 146:130 */       isOk = false;
/* 147:    */     }
/* 148:132 */     return isOk;
/* 149:    */   }
/* 150:    */   
/* 151:    */   private String sendRequest(String baseurl)
/* 152:    */     throws Exception
/* 153:    */   {
/* 154:    */     try
/* 155:    */     {
/* 156:137 */       StringTokenizer st = new StringTokenizer(this.data, "., ");
/* 157:138 */       String listId = st.nextToken();
/* 158:139 */       if ((listId.equalsIgnoreCase("FON")) || (listId.equalsIgnoreCase("NORTHEND"))) {
/* 159:140 */         listId = "cp001";
/* 160:    */       }
/* 161:142 */       String keyword = st.nextToken();
/* 162:143 */       String body = st.nextToken();
/* 163:144 */       while (st.hasMoreTokens()) {
/* 164:145 */         body = body + st.nextToken();
/* 165:    */       }
/* 166:147 */       body = URLUTF8Encoder.encode(body);
/* 167:148 */       String number = URLUTF8Encoder.encode(this.sender);
/* 168:149 */       String requestUrl = baseurl + "rmcsservices.jsp?keyword=" + keyword + "&msg=" + body + "&msisdn=" + this.sender + "&siteId=" + this.siteId;
/* 169:    */       
/* 170:151 */       URL url = new URL(requestUrl);
/* 171:152 */       BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
/* 172:153 */       String temp = br.readLine();
/* 173:154 */       String resp = new String();
/* 174:155 */       while (temp != null)
/* 175:    */       {
/* 176:156 */         resp = resp + temp;
/* 177:157 */         temp = br.readLine();
/* 178:    */       }
/* 179:159 */       br.close();
/* 180:160 */       return resp;
/* 181:    */     }
/* 182:    */     catch (IOException ex2)
/* 183:    */     {
/* 184:162 */       throw new Exception("8000");
/* 185:    */     }
/* 186:    */   }
/* 187:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.mobility.contentserver.serviceinterfaces.receiverequest
 * JD-Core Version:    0.7.0.1
 */