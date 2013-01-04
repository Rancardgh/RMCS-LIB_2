package com.rancard.mobility.infoserver;
import java.util.Vector;
public class system_sms_queue_groupBean {

// Bean Properties
private java.lang.Integer category_id;
private java.lang.String category_name;
//constructor
public system_sms_queue_groupBean(){  
}


public void setcategory_id(java.lang.Integer category_id) {
	 this.category_id=category_id;
}

public java.lang.Integer getcategory_id(){
return this.category_id;
}
public void setcategory_name(java.lang.String category_name) {
	 this.category_name=category_name;
}

public java.lang.String getcategory_name(){
return this.category_name;
}
public void createsystem_sms_queue_group() throws Exception {
system_sms_queue_groupDB system_sms_queue_group=new system_sms_queue_groupDB();

system_sms_queue_group.createsystem_sms_queue_group(category_id,category_name);
}
public void updatesystem_sms_queue_group() throws Exception {
system_sms_queue_groupDB system_sms_queue_group=new system_sms_queue_groupDB();

system_sms_queue_group.updatesystem_sms_queue_group(category_id,category_name);
}
public void deletesystem_sms_queue_group() throws Exception {
system_sms_queue_groupDB system_sms_queue_group=new system_sms_queue_groupDB();

system_sms_queue_group.deletesystem_sms_queue_group(category_id);
}
public void viewsystem_sms_queue_group()throws Exception {
system_sms_queue_groupBean bean=new system_sms_queue_groupBean ();
system_sms_queue_groupDB system_sms_queue_group=new system_sms_queue_groupDB();

bean=system_sms_queue_group.viewsystem_sms_queue_group(category_id);

category_id=bean.getcategory_id();
category_name=bean.getcategory_name();

}
public java.util.Vector Query1system_sms_queue_group() throws Exception{
/**
* @todo 
 add the neccessary parameters (if any)  
**/
system_sms_queue_groupDB system_sms_queue_group=new system_sms_queue_groupDB();

java.util.Vector beans=new java.util.Vector();
beans=system_sms_queue_group.Query1system_sms_queue_group();
 return beans;
}
}