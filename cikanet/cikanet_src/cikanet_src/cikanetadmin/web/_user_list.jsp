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

 
<% 
java.util.ArrayList lastTransactionDates = new java.util.ArrayList();
java.util.ArrayList accountNumbers = new java.util.ArrayList();
%>


<sql:transaction dataSource="jdbc/cikanet" >
<c:choose>
    <c:when test="${param['todo']=='search'}">
    <sql:query var="users_lookup">
 SELECT * FROM cikanet.users u  INNER JOIN cikanet.cikanet_accounts a on u.cikanet_acc_no=a.cikanet_acc_no  where  u.first_name like ? OR u.last_name like ? OR u.cikanet_acc_no like ?  OR u.msisdn like ?  
  <sql:param value="%${param['search']}%" />
 <sql:param value="%${param['search']}%" />
  <sql:param value="%${param['search']}%" />
<sql:param value="%${param['search']}%" />
</sql:query>
        </c:when>
    <c:otherwise>
         <sql:query var="users_lookup">
        SELECT * FROM cikanet.users u INNER JOIN cikanet.cikanet_accounts a on u.cikanet_acc_no=a.cikanet_acc_no                 
    </sql:query>
    </c:otherwise>
</c:choose>
    

</sql:transaction>

 <% 
    request.setAttribute("transact_dates",lastTransactionDates);
    request.setAttribute( "account_nums", accountNumbers );
    %>  
<div class="displayTable" >
    <display:table name="${users_lookup.rows}"  id="user_row" class="simple" >
        <display:column  title="CikaNET Account #" >
            <a href="user-details.jsp?action=view&id=${user_row.cikanet_acc_no}&useraction=1" >${user_row.cikanet_acc_no}</a> 
        </display:column>
        <display:column property="last_name" title="Last Name" />
        <display:column property="first_name" title="First Name"/>
        <display:column property="msisdn" title="MSISDN" />
        <display:column property="current_balance" title="Balance" />
        
        <display:column title="Account Status" property="status_enabled" >
        </display:column>
       
    </display:table>
</div>
