<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.cikanet.admin.util.uidGen" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<sql:setDataSource dataSource="jdbc/cikanet"/>
<sql:query var="cikanet_acc_query">
    SELECT * FROM cikanet_accounts WHERE cikanet_acc_no = ?
    <sql:param value="${param['id']}"/>
</sql:query>
<c:forEach var="row" items="${cikanet_acc_query.rows}">
    <c:set var="bank_id" value="${row.bank_id}"/>
    <c:set var="current_balance" value="${row.current_balance}"/>
    <c:set var="bank_acc_no" value="${row.bank_acc_no}"/>
</c:forEach>
<c:if test="${ (not empty param.load) or (param['action']=='load')}">
    <sql:transaction>        
        <sql:update var="load">
            UPDATE cikanet_accounts 
            SET current_balance = ?
            WHERE cikanet_acc_no = ?            
            <sql:param value="${current_balance + param['amount']}"/>
            <sql:param value="${param['id']}"/>
        </sql:update>
        <c:set var="current_balance" value="${current_balance + param['amount']}"/>
    </sql:transaction>
    <jsp:forward page="_user_summary.jsp"/>
    <%-- INSERT CODE THAT INTERFACES WITH ACCOUNT MANAGER, 
    FUNDS POOL MANAGER AND LOAD ACQUIRER  --%>
</c:if>
<c:if test="${(not empty param.unload) or (param['action'] == 'unload')}">
    <sql:transaction>
        <sql:update var="load">
            UPDATE cikanet_accounts 
            SET current_balance = ?
            WHERE cikanet_acc_no = ?            
            <sql:param value="${current_balance - param['amount']}"/>
            <sql:param value="${param['id']}"/>
        </sql:update>
        <c:set var="current_balance" value="${current_balance - param['amount']}"/>
    </sql:transaction>
    <jsp:forward page="_user_summary.jsp"/>
    <%-- INSERT CODE THAT INTERFACES WITH ACCOUNT MANAGER, 
    FUNDS POOL MANAGER AND LOAD ACQUIRER  --%>
</c:if>
<html>
    <head1>
        <!--style type="text/css" media="all">
            @import url("css/maven-base.css");
            @import url("css/maven-theme.css");
            @import url("css/site.css");
            @import url("css/screen.css");
        </style-->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CikaNET Admin Console - Manage User Funds</title>
    </head>
    <body>                
        <form action="_user_account_manage.jsp" method="post" name="form1">
            <!--<p><b><u> User Account Management</u></b></p>-->
            <b>Please enter amount to load/unload:</b>
            <table border="0">
                <tr>
                    <td width="25%">Cikanet Account Number </td>
                    <td>${param['id']}</td>
                </tr>
                <tr>
                    <td>Current Balance</td>
                    <td>${current_balance}</td>
                </tr>
                <tr>
                    <td>Amount</td>
                    <td><input name="amount" type="text" /></td>
                </tr>
                <tr>
                    <td>Confirm Amount </td>
                    <td><input name="amount_conf" type="text" /></td>
                </tr>
            </table>
            <p>
                <input type="hidden" name="id" value=${param['id']}
                       <input type="submit" name="load" value="Load">
                <input type="submit" name="unload" value="Unload">
            </p>
        </form>        
    </body>
</html>
