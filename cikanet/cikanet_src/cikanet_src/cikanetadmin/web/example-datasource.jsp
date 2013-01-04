<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.cikanet.admin.util.uidGen" %>
<%@ page import="com.cikanet.admin.server.decorators.*"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<sql:setDataSource dataSource="jdbc/cikanet" var="cikanet"/>


<head>
    <style type="text/css" media="all">
        @import url("css/maven-base.css");
        @import url("css/maven-theme.css");
        @import url("css/site.css");
        @import url("css/screen.css");
    </style>
    <link rel="stylesheet" href="./css/print.css" type="text/css" media="print" />
</head>
<% 
java.util.ArrayList lastTransactionDates = new java.util.ArrayList();
java.util.ArrayList accountNumbers = new java.util.ArrayList();
%>
<sql:transaction dataSource="jdbc/cikanet">
    <sql:query var="users_lookup">
        SELECT * FROM cikanet.users u INNER JOIN cikanet.cikanet_accounts a on u.cikanet_acc_no=a.cikanet_acc_no                 
    </sql:query>
    <c:forEach var="row" items="${users_lookup.rows}">
        <%-- Get most recent transaction date for user --%>
        <c:set var="account_no" value="${row.cikanet_acc_no}" scope="request"/>
        <% accountNumbers.add(request.getAttribute("account_no"));%> 
        <% %>
        <sql:query var="users_trans_lookup">
            SELECT * FROM transactions WHERE cikanet_acc_no = ? ORDER BY transaction_date DESC LIMIT 1
            <sql:param value="${account_no}"/>
        </sql:query>
        <c:choose>
            <c:when test="${users_trans_lookup.rowCount != 0}">
                <c:forEach var="usr_row" items="${users_trans_lookup.rows}">
                    <c:set var="trans_date" value="${usr_row.transaction_date}" scope="request"/>
                    <% lastTransactionDates.add(request.getAttribute("trans_date")); %>        
                </c:forEach>
            </c:when>
            <c:otherwise>
                <% lastTransactionDates.add("None");%>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <% 
    request.setAttribute("transact_dates",lastTransactionDates);
    request.setAttribute( "account_nums", accountNumbers );
    %>
    
</sql:transaction>


<display:table name="${users_lookup.rows}"  id="user_row" class="simple" >
    <display:column property="cikanet_acc_no" title="CikaNET Account #" />
    <display:column property="last_name" title="Last Name" />
    <display:column property="first_name" title="First Name"/>
    <display:column property="msisdn" title="MSISDN" />
    <display:column title="Last Transaction Date" >
        <c:out value="${transact_dates[user_row_rowNum-1]}"/>
    </display:column>
    <display:column title="Account Enabled" property="status_enabled" >
    </display:column>
    <display:column title="Actions" >
        <c:out value="${account_nums[user_row_rowNum-1]}"/>
    </display:column>
</display:table>

