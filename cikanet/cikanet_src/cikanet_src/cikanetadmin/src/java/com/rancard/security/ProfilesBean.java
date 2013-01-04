package com.rancard.security;
import com.rancard.common.*;
import java.util.Vector;
import java.lang.Runtime;
public class ProfilesBean extends roles {

// Bean Properties
private java.lang.String username;
private java.lang.String password;
private java.lang.String role_id;
private java.lang.String emp_id;
private java.lang.String firstname;
private java.lang.String lastname;
private java.lang.String last_login;
private String email;
private String mobile_phone;
private String address;
public static final int COUNT = 5;
  private int start;
  private String middlename;
  private String keyword;
  private int count=5;
  private String sort_type;
//constructor
public ProfilesBean(){
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
public void setrole_id(java.lang.String role_ID) {
         this.role_id=role_ID;
}

public java.lang.String getrole_id(){
return this.role_id;
}
public void setemp_id(java.lang.String emp_ID) {
         this.emp_id=emp_ID;
}
 // overwrite this method to support javbaean convention
 public void setEmp_id(java.lang.String emp_ID) {
                this.emp_id=emp_ID;
}

public java.lang.String getemp_id(){
return this.emp_id;
}

public void setlastname(java.lang.String lastname) {
         this.lastname=lastname;
}
public void setLastname(java.lang.String lastname) {
                this.lastname=lastname;
}

public java.lang.String getlastname(){
return this.lastname;
}

public void setfirstname(java.lang.String firstname) {
         this.firstname=firstname;
}
       public void setFirstname(java.lang.String firstname) {
                this.firstname=firstname;
}
public java.lang.String getfirstname(){
return this.firstname;
}

public void setlast_login(java.lang.String last_login) {
         this.last_login=last_login;
}
  public void setStart(int start) {
    this.start = start;

  }
public java.lang.String getlast_login(){
return this.last_login;
}

public void createProfiles() throws Exception {
ProfilesDB Profiles=new ProfilesDB();

java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
java.util.Date utilDate_last_logindate = null;

Profiles.createProfiles(username,password,role_id,firstname,lastname,email,middlename);
}

// does not enter last login date
public void createProfiles2() throws Exception {
ProfilesDB Profiles=new ProfilesDB();

Profiles.createProfiles(username,password,role_id,emp_id);
}

public void updateProfiles() throws Exception {
ProfilesDB Profiles=new ProfilesDB();

java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
java.util.Date utilDate_last_logindate = null;
try
{
  utilDate_last_logindate = sdf.parse(last_login);
}catch(Exception e)
{
  throw new Exception(e.getMessage());
}

Profiles.updateProfiles(username,password,role_id,emp_id,utilDate_last_logindate);
}

// do not update lastlogin date
public void updateProfiles2() throws Exception {
ProfilesDB Profiles=new ProfilesDB();

Profiles.updateProfiles(username,password,role_id,emp_id);
}
public void updateAllProfiles() throws Exception {
ProfilesDB Profiles=new ProfilesDB();

Profiles.updateAllProfiles(username,password,role_id,emp_id,firstname,lastname,email,middlename,mobile_phone);
}
public void updateUserProfile() throws Exception {
ProfilesDB Profiles=new ProfilesDB();
Profiles.updateUserProfiles(username,password,role_id);
}


public void updateLastLogin() throws Exception
{
  ProfilesDB Profiles=new ProfilesDB();

  java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
  java.util.Date utilDate_last_logindate = null;
  try
  {
    utilDate_last_logindate = sdf.parse(last_login);
  }catch(Exception e)
  {
    throw new Exception(e.getMessage());
  }

  Profiles.updateLastLogin(emp_id,utilDate_last_logindate);
}


public void deleteProfiles() throws Exception {
ProfilesDB Profiles=new ProfilesDB();

Profiles.deleteProfiles();
}

public void deleteUser()throws Exception
{
  ProfilesDB Profiles=new ProfilesDB();

   Profiles.deleteUser(username);
}

public void viewProfiles()throws Exception
{
  ProfilesBean bean=new ProfilesBean ();
  ProfilesDB Profiles=new ProfilesDB();
  java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd");
  java.text.SimpleDateFormat newDF=new java.text.SimpleDateFormat("dd/MM/yyyy");
  java.util.Date utilDate=null;

  bean=Profiles.viewProfiles(this.username);

  username=bean.getusername();
  password=bean.getpassword();
  role_id=bean.getrole_id();
  emp_id=bean.getemp_id();
  firstname=bean.getfirstname();
  lastname=bean.getlastname();
  this.setEmail(bean.getEmail());
  this.setMiddlename(bean.getMiddlename());
  this.setMobile_phone(bean.getMobile_phone());
  if(bean.getlast_login()!=null)
  {
    utilDate=df.parse(bean.getlast_login());
    last_login=newDF.format(utilDate);
  }
}

//public java.util.ArrayList viewAllProfiles()throws Exception
//{

//return new ProfilesDB().viewAllUserProfiles();
//}

public com.rancard.util.Page viewAllUserProfiles(int st)throws Exception
{
return new ProfilesDB().viewAllUserProfiles(st,this.count,this.sort_type);
}

public com.rancard.util.Page find()throws Exception
{

return new ProfilesDB().viewAllUserProfiles(this.getKeyword(), this.getStart(),COUNT);
}
  /*public void viewEmployeeName()throws Exception
  {
    ProfilesBean bean=new ProfilesBean ();
    ProfilesDB Profiles=new ProfilesDB();

    bean=Profiles.viewEmployeeName();

    username=bean.getusername();
    password=bean.getpassword();
    role_id=bean.getrole_id();
    emp_id=bean.getemp_id();
    firstname=bean.getfirstname();
    lastname=bean.getlastname();
  }*/
public ProfilesBean searchProfiles(String keyword) throws Exception {
ProfilesDB Profiles=new ProfilesDB();
return Profiles.searchProfiles(keyword);
}
  public String WinLogonUser_env()
  {
    String envUser = System.getProperty("user.name");

    return envUser;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getEmail() {
    return email;
  }
  public void setMobile_phone(String mobile_phone) {
    this.mobile_phone = mobile_phone;
  }
  public String getMobile_phone() {
    return mobile_phone;
  }
  public void setAddress(String address) {
    this.address = address;
  }
  public String getAddress() {
    return address;
  }
  public String getName() {
    return this.getfirstname()+" "+this.getMiddlename()+" "+this.getlastname();
  }

  public int getStart() {
    return this.start;
  }
  public void setMiddlename(String middlename) {
    this.middlename = middlename;
  }
  public String getMiddlename() {
    return middlename;
  }
  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }
  public String getKeyword() {
    return keyword;
  }

  public void setCount(int count) {
    this.count = count;
  }
  public int getCount() {
    return count;
  }
  public void setSort_type(String sort_type) {
    this.sort_type = sort_type;
  }
  public String getSort_type() {
    return sort_type;
  }
  public void verifyUser()throws Exception
  {
  java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd");
  java.text.SimpleDateFormat newDF=new java.text.SimpleDateFormat("dd/MM/yyyy");
   java.util.Date utilDate=null; 
  ProfilesDB Profile=new ProfilesDB();
    ProfilesBean  bean=Profile.verifyUser(username,password);
    
  username=bean.getusername();
  password=bean.getpassword();
  role_id=bean.getrole_id();
  emp_id=bean.getemp_id();
  this.firstname=bean.getfirstname()!=null?bean.getfirstname():"";
  this.lastname=bean.getlastname()!=null?bean.getlastname():"";
  this.setEmail(bean.getEmail());
  this.setMiddlename(bean.getMiddlename()!=null?bean.getMiddlename():"");
  this.setMobile_phone(bean.getMobile_phone()!=null ? bean.getMobile_phone():"");
  if(bean.getlast_login()!=null)
  {
    utilDate=df.parse(bean.getlast_login());
    last_login=newDF.format(utilDate);
  }

  }


}
