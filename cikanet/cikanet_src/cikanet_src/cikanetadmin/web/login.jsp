<%@ page import="java.sql.*" %>
<%
String path = request.getContextPath();
  String s = request.getProtocol().toLowerCase();
	s= s.substring(0, s.indexOf("/")).toLowerCase();

String baseUrl = s+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%!String cms_user;%>
<%
// get username and password.
String username = request.getParameter("username");
String password = request.getParameter("password");

// check if username a user with that username exists
com.rancard.security.ProfilesBean profiles=new com.rancard.security.ProfilesBean();
profiles.setusername(username);
profiles.setpassword(password);
try
{
  profiles.verifyUser();
}
catch(Exception e)
{
}


String db_uname = "";
String db_pwd = "";
String role_id= "";


db_uname=profiles.getusername();
db_pwd=profiles.getpassword();

// if userexists  check if password is valid
if ( username != null && username.equals(db_uname) )
{
  //System.out.println("username match");

  if ( password != null && password.equals(db_pwd) )
  {
// if is valid redirect to welcome page
    session.setAttribute("login","yes");

    profiles.setusername(db_uname);
    session.setAttribute("user",profiles);

    if(request.getParameter("redirect")!=null && !"null".equals(request.getParameter("redirect")) ){
     response.sendRedirect(request.getParameter("redirect"));
    }else{
    response.sendRedirect(baseUrl + "/index.jsp");
   }
  }
  else{
    response.sendRedirect("default.jsp?login_err=2");
  }
}
else{
  response.sendRedirect("default.jsp?login_err=1");
}
%>
