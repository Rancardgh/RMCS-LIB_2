package com.rancard.mobility.contentserver;

import java.util.Vector;
public class NetworkConnectionsBean {

// Bean Properties
private java.lang.String username;
private java.lang.String password;
private java.lang.String conn_id;
private java.lang.String gateway_url;
private java.lang.String method;
private java.lang.String type;
private java.lang.String network_prefix;
//constructor
public NetworkConnectionsBean(){
}


public void setusername(java.lang.String username) {
	 this.username=username;
}

public java.lang.String getusername(){
return this.username;
}
public void setpassword(java.lang.String password) {
	 this.password=password;
}

public java.lang.String getpassword(){
return this.password;
}
public void setconn_id(java.lang.String conn_id) {
	 this.conn_id=conn_id;
}

public java.lang.String getconn_id(){
return this.conn_id;
}
public void setgateway_url(java.lang.String gateway_url) {
	 this.gateway_url=gateway_url;
}

public java.lang.String getgateway_url(){
return this.gateway_url;
}
public void setmethod(java.lang.String method) {
	 this.method=method;
}

public java.lang.String getmethod(){
return this.method;
}
public void settype(java.lang.String type) {
	 this.type=type;
}

public java.lang.String gettype(){
return this.type;
}
public void setnetwork_prefix(java.lang.String network_prefix) {
	 this.network_prefix=network_prefix;
}

public java.lang.String getnetwork_prefix(){
return this.network_prefix;
}
public void createcp_connections() throws Exception {
NetworkconnectionsDB cp_connections=new NetworkconnectionsDB();

cp_connections.createcp_connections(username,password,conn_id,gateway_url,method,type,network_prefix);
}
public void updatecp_connections() throws Exception {
NetworkconnectionsDB cp_connections=new NetworkconnectionsDB();

cp_connections.updatecp_connections(username,password,conn_id,gateway_url,method,type,network_prefix);
}
public void deletecp_connections() throws Exception {
NetworkconnectionsDB cp_connections=new NetworkconnectionsDB();

cp_connections.deletecp_connections(conn_id);
}
public void viewcp_connections()throws Exception {
NetworkConnectionsBean bean=new NetworkConnectionsBean ();
NetworkconnectionsDB cp_connections=new NetworkconnectionsDB();

bean=cp_connections.viewcp_connections(conn_id);

username=bean.getusername();
password=bean.getpassword();
conn_id=bean.getconn_id();
gateway_url=bean.getgateway_url();
method=bean.getmethod();
type=bean.gettype();
network_prefix=bean.getnetwork_prefix();

}

}
