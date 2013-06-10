package com.rancard.mobility.infoserver;
import com.rancard.common.DConnect;
import java.sql.*;
import java.util.Vector;
public class system_sms_queue_groupDB {

//constructor
public system_sms_queue_groupDB(){  }

//insert record
public void createsystem_sms_queue_group(java.lang.Integer category_id,java.lang.String category_name) throws Exception {

String SQL;
ResultSet rs=null;
Connection con=null;
PreparedStatement prepstat=null;
try{
	 con=com.rancard.common.DConnect.getConnection();

SQL="insert into system_sms_queue_group(category_id,category_name) values(?,?)";
prepstat=con.prepareStatement(SQL);

prepstat.setInt(1,category_id.intValue());

prepstat.setString(2,category_name);
prepstat.execute();
}catch (Exception ex){
 if(con !=null)
 con.close();
 throw new Exception(ex.getMessage());
}
 if(con !=null)
 con.close();
}
public void updatesystem_sms_queue_group(java.lang.Integer category_id,java.lang.String category_name) throws Exception {

String SQL;
ResultSet rs=null;
Connection con=null;
PreparedStatement prepstat=null;
try{
	 con=DConnect.getConnection();


SQL="update system_sms_queue_group set category_name=? where category_id=?";
prepstat=con.prepareStatement(SQL);

prepstat.setString(1,category_name);


prepstat.setInt(2,category_id.intValue());
prepstat.execute();
}catch (Exception ex){
 if(con !=null)
 con.close();
 throw new Exception(ex.getMessage());
}
 if(con !=null)
 con.close();
}
public void deletesystem_sms_queue_group(java.lang.Integer category_id) throws Exception {

String SQL;
ResultSet rs=null;
Connection con=null;
PreparedStatement prepstat=null;
try{
	 con=DConnect.getConnection();
SQL="delete from system_sms_queue_group where category_id=?";
prepstat=con.prepareStatement(SQL);

prepstat.setInt(1,category_id.intValue());
prepstat.execute();
}catch (Exception ex){
 if(con !=null)
 con.close();
 throw new Exception(ex.getMessage());
}
 if(con !=null)
 con.close();
}
public system_sms_queue_groupBean viewsystem_sms_queue_group(java.lang.Integer category_id) throws Exception {
system_sms_queue_groupBean system_sms_queue_group =new system_sms_queue_groupBean();

String SQL;
ResultSet rs=null;
Connection con=null;
PreparedStatement prepstat=null;
try{
	 con=DConnect.getConnection();

SQL="select * from  system_sms_queue_group where category_id=?";
prepstat=con.prepareStatement(SQL);

prepstat.setInt(1,category_id.intValue());
rs=prepstat.executeQuery();
while (rs.next()){

system_sms_queue_group.setcategory_id(new Integer(rs.getInt("category_id")));
system_sms_queue_group.setcategory_name(rs.getString("category_name"));
}
}catch (Exception ex){
 if(con !=null)
 con.close();
 throw new Exception(ex.getMessage());
}
 if(con !=null)
 con.close();
 return system_sms_queue_group;
}
public java.util.Vector Query1system_sms_queue_group() throws Exception{/**
* @todo
 add the neccessary parameters (if any)
**/
Vector results=new Vector();
String SQL;
ResultSet rs=null;
Connection con=null;
PreparedStatement prepstat=null;
try{
	 con=DConnect.getConnection();
SQL="select * from system_sms_queue_group order by category_id";
prepstat=con.prepareStatement(SQL);/**
* @todo If the query had parameters set the PreparedStatement's parameters

**/
rs=prepstat.executeQuery();
while (rs.next()){
system_sms_queue_groupBean system_sms_queue_group =new system_sms_queue_groupBean();

system_sms_queue_group.setcategory_id(new Integer(rs.getInt("category_id")));
system_sms_queue_group.setcategory_name(rs.getString("category_name"));
results.addElement(system_sms_queue_group);
}
}catch (Exception ex){
 if(con !=null)
 con.close();
 throw new Exception(ex.getMessage());
}
 if(con !=null)
 con.close();
return results;
}
}