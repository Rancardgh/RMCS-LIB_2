<%@ taglib prefix="a" uri="http://jmaki/v1.0/jsp" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.cikanet.admin.util.uidGen" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="css/print.css" type="text/css" media="print" />
<sql:setDataSource dataSource="jdbc/cikanet"/><%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        
        <sql:transaction dataSource="jdbc/cikanet">
            <sql:query var="users_lookup">
                SELECT * FROM users                  
            </sql:query>
            
        </sql:transaction>
       
        
        <table><tr><td><a href="new_user.jsp">Add New User</a><br></td><tr></table>
        <table width = 600 border="1">
            <thead>
                <tr>
                    <td>Msisdn</td>
                    <td>First Name</td>
                    <td>Last Name</td>
                    <td>Last Transaction</td>
                    <td>Balance</td>
                    <td>Edit</td>
                    <td>Delete</td>
                </tr>
            </thead>
            
            <c:forEach var="row" items="${users_lookup.rows}">
                <tr>
                    <td><c:out  value="${row.msisdn}"/></td>
                    <td><c:out  value="${row.first_name}"/></td>
                    <td><c:out  value="${row.last_name}"/></td>
                    <td><%= new java.util.Date()%></td>
                    <td>200</td>
                    <td>edit</td>
                    <td>delete</td>
                </tr>
                
            </c:forEach>
            
        </table>
    </body>
</html>
