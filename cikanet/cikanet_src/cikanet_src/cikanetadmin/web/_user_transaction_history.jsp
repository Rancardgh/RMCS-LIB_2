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

<sql:transaction dataSource="jdbc/cikanet">
    <sql:query var="transactions">
        SELECT * FROM transactions WHERE cikanet_acc_no = ? ORDER BY transaction_date DESC    
        <sql:param value="${param['id']}"/>
    </sql:query>
</sql:transaction>

<!--<p><b><u> Transaction History</u></b></p>  -->
<div class="content"  style="height:600px; background-color: #ffffff; padding-left: 25px; padding-top: 25px; padding-right: 25px;">
    <div class="displayTable" >
        <display:table name="${transactions.rows}" class="simple" >
            <display:column property="transaction_id" title="Transaction ID" />
            <display:column property="transaction_date" title="Date" />
            <display:column property="transaction_type" title="Transaction Type"/>
            <display:column property="to_from" title="To/From" />
            <display:column property="cikanet_acc_no" title="Cikanet Acc No" />
            <display:column property="transaction_status" title="Transaction Status" />
            <display:column property="amount" title="Amount" />
            <display:column property="balance" title="Account Balance" />
        </display:table>
    </div>
</div>
