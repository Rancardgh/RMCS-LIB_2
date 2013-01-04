<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="com.cikanet.admin.util.uidGen" %>
<%@ page import="com.cikanet.server.topup.TopUpClientFactory" %>
<%@ page import="com.cikanet.server.topup.TopUpClient" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<sql:setDataSource dataSource="jdbc/cikanet"/>
<%
uidGen idGenerator = new uidGen();
request.setAttribute("transaction_id", idGenerator.getUId(12));
Calendar cal = new GregorianCalendar();
Date currentDate = cal.getTime();
java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
request.setAttribute("transaction_date", df.format(currentDate));
%>
<sql:query var="user_lookup">
    SELECT * FROM users WHERE msisdn = ?
    <sql:param value="${param['msisdn']}"/>                    
</sql:query>
<c:forEach var="row" items="${user_lookup.rows}">
    <c:set var="user_cikanet_acc_no" value="${row.cikanet_acc_no}"/>
    <c:set var="network_id" value="${row.network_id}"/>
</c:forEach>
<sql:query var="network_lookup">
    SELECT * FROM networks WHERE network_id = ?
    <sql:param value="${network_id}"/>                    
</sql:query>
<c:forEach var="row" items="${network_lookup.rows}">
    <c:set var="driver_name" value="${row.driver_name}" scope="request"/>
</c:forEach>
<sql:query var="balance_lookup">
    SELECT * FROM cikanet_accounts WHERE cikanet_acc_no = ?
    <sql:param value="${user_cikanet_acc_no}"/>                    
</sql:query>
<c:forEach var="row" items="${balance_lookup.rows}">
    <c:set var="current_balance" value="${row.current_balance}"/>
</c:forEach>
<c:if test="${(not empty param.amt) and (not empty param.msisdn)}">
    <%
        String network = (String)request.getAttribute("driver_name");
        TopUpClientFactory factory = new TopUpClientFactory();
        TopUpClient client = factory.createTopUpClient(network);
        com.cikanet.server.topup.Response t_response = client.requestTopUp(Double.parseDouble(request.getParameter("amt")), request.getParameter("msisdn"), "", "");    
        request.setAttribute("top_up_response", t_response.getStatusCode().getName());
    %>    
    <c:out value="${top_up_response}"/>
    <c:if test="${top_up_response == 'Success'}">
        <sql:transaction>        
            <sql:update var="add_transaction">
                INSERT INTO transactions(transaction_id, 
                transaction_date,
                transaction_type,
                to_from,
                cikanet_acc_no,
                transaction_status,
                amount,
                balance) 
                VALUES(?, ?, ?, ?, ?, ?, ?, ?)
                <sql:param value="${transaction_id}"/>
                <sql:param value="${transaction_date}"/>
                <sql:param value="${'TOPUP'}"/>
                <sql:param value="${'To'}"/>
                <sql:param value="${user_cikanet_acc_no}"/>
                <sql:param value="${'Complete'}"/>
                <sql:param value="${param['amt']}"/>
                <sql:param value="${current_balance - param['amt']}"/>
            </sql:update>     
            <sql:update var="load">
                UPDATE cikanet_accounts 
                SET current_balance = ?
                WHERE cikanet_acc_no = ?            
                <sql:param value="${current_balance - param['amt']}"/>
                <sql:param value="${user_cikanet_acc_no}"/>
            </sql:update>
        </sql:transaction> 
    </c:if>
</c:if>
<%--
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CikaNET RTEMS - Air Time Purchase </title>
    </head>
    <body>      
        <jsp:include page ="_user_list.jsp" flush="true" />
        <form action="topup.jsp" method="post" name="form1">
            <p><b><u>  Mobile Top Up</u></b></p>
            <table border="0">
                <tr>
                    <td width="25%">MSISDN</td>
                    <td><input type="text" name="msisdn" class="texta"></td>
                </tr>
                <tr>
                    <td>Amount </td>
                    <td><input type="text" name="amt" class="texta"></td>
                </tr>
            </table>            
            <p>
                <input type="submit" name="Submit" value="Top Up">
            </p>
        </form>        
    </body>
</html>
--%>
