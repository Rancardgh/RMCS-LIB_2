<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%--
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
        <title>CikaNET Admin Console</title>
    </head>
    <body  >
<jsp:include flush="true" page="manager/test_tab_1.jsp?" />
<link rel="stylesheet" type="text/css" href="include/css/rms_tables.css"> 
    <!--h1>CikaNET Admin Console</h1>
    <r>
    <a href="new_user.jsp">Add New User</a><br>
    <a href="new_bank.jsp">Add New Bank</a><br>
    <a href="new_network.jsp">Add New Network</a><br>
    <a href="new_merchant.jsp">Add New Merchant</a><br-->
    <div valign="top" ><br>
            <jsp:include page="_user_toolbar.jsp" flush="true" />
            <div align="center"><br>
            <jsp:include page ="_user_list.jsp" flush="true" />
        </div>
    </div>
     
    <%--
    This example uses JSTL, uncomment the taglib directive above.
    To test, display the page like this: index.jsp?sayHello=true&name=Murphy
    --%>
    <%--
    <c:if test="${param.sayHello}">
        <!-- Let's welcome the user ${param.name} -->
        Hello ${param.name}!
    </c:if>
    --%>
    
    </body>
</html>
