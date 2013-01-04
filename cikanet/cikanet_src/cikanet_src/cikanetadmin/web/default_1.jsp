<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<% boolean reload=false;%>
<%!String page_msg;%>

<%
page_msg = "";
// this is a test for cms
if( request.getParameter("login_err")!=null )
{
  if ( request.getParameter("login_err").equals("1") )
    page_msg = "<font color='black' size='2'> <b>Invalid login. Please try again</b></font>";

  else if (request.getParameter("login_err").equals("2") )
    page_msg = "<font color='white'>Error connecting to database. Please contact sys admin.</font>";
}

if( request.getParameter("logout") != null &&  request.getParameter("logout").equals("1") )
{
  session.invalidate();
  //session.setAttribute("cms_user","0");
  response.sendRedirect("default.jsp");
}

//String cms_user = (String)session.getAttribute("cms_user");
//System.out.println("cms_user in default.jsp at logout = " + cms_user);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"><head><title>RMS Admin Server Login</title>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" type="text/css" media="screen, projection" href="include/css/style.css"></head>


<body>
<div class="lball">
	<div class="lblayout lbtopmargin" style="width: 500px; height: 25px"><p>
		<span class="logotext">Rancard Mobility Server Administrator Login</span></p></div>
	<div class="lblayout lbcontainer">
			<div class="lbinfo">

				<p>
					Enter a valid user credential to get access to the administration console. <br><br>
									</p>
			</div>
			<div class="lbfields">
				<div class="lbfieldstext">
					<p class="lbuser">Username:</p>
					<p class="lbpass">Password:</p>
				</div>
				<div class="lbinput">
					<form method="post" action="login.jsp">
						<p>
							<input name="username" size="15" value="" type="text"><br>
							<input class="lbpassword" name="password" size="15" value="" type="password"><br>
							<input class="lbsubmit" name="loginsubmit" value="Submit" type="submit">
							<input class="lbsubmit" name="logincancel" value="Cancel" type="submit">
						</p>
					</form>
				</div>
			</div>
	</div>
	<div class="lblayout footer">Copyright 2005 Rancard Solutions Ltd.</div>
</div>
</body></html>
