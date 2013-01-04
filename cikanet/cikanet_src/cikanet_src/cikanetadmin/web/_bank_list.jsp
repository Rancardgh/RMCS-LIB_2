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
    <sql:query var="banks_lookup">
        SELECT * FROM banks                 
    </sql:query>

    
    
</sql:transaction>
<div class="displayTable" >
    <display:table name="${banks_lookup.rows}"  id="bank_row" class="simple" >
        <display:column property="bank_id" title="Bank ID #" decorator="com.cikanet.admin.server.decorators.AccountNumberDecorator"/>
        <display:column property="routing_no" title="Routing Number" />
        <display:column property="bank_name" title="Bank Name"/>
     </display:table>
</div>
