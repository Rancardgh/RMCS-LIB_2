<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <script type="text/javascript" src="js/tabber.js"></script>
        <script type="text/javascript">
        document.write('<style type="text/css">.tabber{display:none;}<\/style>');
        </script>
        <style type="text/css" media="all">
           @import url("css/tabber.css");
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CikaNET Admin Console - View User Details</title>
    <link rel="stylesheet" type="text/css" href="include/css/rms_tables.css"> 
        </head>
    <body topmargin="0">
          <jsp:include flush="true" page="manager/test_tab_1.jsp?" />
         <br>
          <jsp:include flush="true" page ="_user_details_toolbar.jsp" />
        <div style="left-margin:20" > <h1>User Details</h1></div>
        
      
        <c:choose>
            <c:when test="${param.page=='contactinfo'}">
           <jsp:include page="_users_detail.jsp" flush="true"/>
                </c:when>
            <c:when test="${param.page=='transaction'}">
              <jsp:include page="_user_transaction_history.jsp" flush="true"/>
                </c:when>
            <c:when test="${param.page=='balance'}">
              <jsp:include page="_user_account_manage.jsp" flush="true"/>
                </c:when>
            <c:otherwise>
                <jsp:include page="_users_detail.jsp" flush="true"/>
            </c:otherwise>
        </c:choose>
        
        <!--div class="tabber">
          <div class="tabbertab">
                <h2>View/Edit Details</h2>
             
            </div >
            
            
            <div class="tabbertab">
                <h2>Transaction History</h2>
                <!- - jsp : include page="_user_transaction_history.jsp" flush="true"/ - - >
            </div>
            
            
            <div class="tabbertab">
                <h2>Manage Funds</h2>
                <!-- jsp : include page="_user_account_manage.jsp" flush="true" / >
            </div>
            
        </div-->
        
        
    </body>
</html>
