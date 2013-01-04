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
    <sql:query var="merchants_lookup">
        SELECT m.* , cikanet_accounts.current_balance ,c.*  FROM cikanet_accounts, merchants m inner join contact_info c on m.contact_id = c.contact_id where 
        cikanet_accounts.cikanet_acc_no = m.cikanet_acc_no           
    </sql:query>
    
    
    
</sql:transaction>
<div class="displayTable" >
    <display:table name="${merchants_lookup.rows}"  id="merchant_row" class="simple" >
        <display:column property="merchant_id" title="MErchantID #" decorator="com.cikanet.admin.server.decorators.AccountNumberDecorator"/>
        <display:column property="merchant_name" title="Merchant Name"/>
        <display:column property="city" title="City"/>
        <display:column property="country" title="Country"/> 
        <display:column property="current_balance" title="Current Balance"/> 
        
    </display:table>
</div>
