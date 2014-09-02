/*  1:   */ package com.rancard.mobility.contentserver;
/*  2:   */ 
/*  3:   */ public class NetworkConnectionsBean
/*  4:   */ {
/*  5:   */   private String username;
/*  6:   */   private String password;
/*  7:   */   private String conn_id;
/*  8:   */   private String gateway_url;
/*  9:   */   private String method;
/* 10:   */   private String type;
/* 11:   */   private String network_prefix;
/* 12:   */   
/* 13:   */   public void setusername(String username)
/* 14:   */   {
/* 15:20 */     this.username = username;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public String getusername()
/* 19:   */   {
/* 20:24 */     return this.username;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void setpassword(String password)
/* 24:   */   {
/* 25:27 */     this.password = password;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public String getpassword()
/* 29:   */   {
/* 30:31 */     return this.password;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void setconn_id(String conn_id)
/* 34:   */   {
/* 35:34 */     this.conn_id = conn_id;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public String getconn_id()
/* 39:   */   {
/* 40:38 */     return this.conn_id;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public void setgateway_url(String gateway_url)
/* 44:   */   {
/* 45:41 */     this.gateway_url = gateway_url;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public String getgateway_url()
/* 49:   */   {
/* 50:45 */     return this.gateway_url;
/* 51:   */   }
/* 52:   */   
/* 53:   */   public void setmethod(String method)
/* 54:   */   {
/* 55:48 */     this.method = method;
/* 56:   */   }
/* 57:   */   
/* 58:   */   public String getmethod()
/* 59:   */   {
/* 60:52 */     return this.method;
/* 61:   */   }
/* 62:   */   
/* 63:   */   public void settype(String type)
/* 64:   */   {
/* 65:55 */     this.type = type;
/* 66:   */   }
/* 67:   */   
/* 68:   */   public String gettype()
/* 69:   */   {
/* 70:59 */     return this.type;
/* 71:   */   }
/* 72:   */   
/* 73:   */   public void setnetwork_prefix(String network_prefix)
/* 74:   */   {
/* 75:62 */     this.network_prefix = network_prefix;
/* 76:   */   }
/* 77:   */   
/* 78:   */   public String getnetwork_prefix()
/* 79:   */   {
/* 80:66 */     return this.network_prefix;
/* 81:   */   }
/* 82:   */   
/* 83:   */   public void createcp_connections()
/* 84:   */     throws Exception
/* 85:   */   {
/* 86:69 */     NetworkconnectionsDB cp_connections = new NetworkconnectionsDB();
/* 87:   */     
/* 88:71 */     cp_connections.createcp_connections(this.username, this.password, this.conn_id, this.gateway_url, this.method, this.type, this.network_prefix);
/* 89:   */   }
/* 90:   */   
/* 91:   */   public void updatecp_connections()
/* 92:   */     throws Exception
/* 93:   */   {
/* 94:74 */     NetworkconnectionsDB cp_connections = new NetworkconnectionsDB();
/* 95:   */     
/* 96:76 */     cp_connections.updatecp_connections(this.username, this.password, this.conn_id, this.gateway_url, this.method, this.type, this.network_prefix);
/* 97:   */   }
/* 98:   */   
/* 99:   */   public void deletecp_connections()
/* :0:   */     throws Exception
/* :1:   */   {
/* :2:79 */     NetworkconnectionsDB cp_connections = new NetworkconnectionsDB();
/* :3:   */     
/* :4:81 */     cp_connections.deletecp_connections(this.conn_id);
/* :5:   */   }
/* :6:   */   
/* :7:   */   public void viewcp_connections()
/* :8:   */     throws Exception
/* :9:   */   {
/* ;0:84 */     NetworkConnectionsBean bean = new NetworkConnectionsBean();
/* ;1:85 */     NetworkconnectionsDB cp_connections = new NetworkconnectionsDB();
/* ;2:   */     
/* ;3:87 */     bean = cp_connections.viewcp_connections(this.conn_id);
/* ;4:   */     
/* ;5:89 */     this.username = bean.getusername();
/* ;6:90 */     this.password = bean.getpassword();
/* ;7:91 */     this.conn_id = bean.getconn_id();
/* ;8:92 */     this.gateway_url = bean.getgateway_url();
/* ;9:93 */     this.method = bean.getmethod();
/* <0:94 */     this.type = bean.gettype();
/* <1:95 */     this.network_prefix = bean.getnetwork_prefix();
/* <2:   */   }
/* <3:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.contentserver.NetworkConnectionsBean
 * JD-Core Version:    0.7.0.1
 */