<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.cikanet.admin.util.uidGen" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<sql:setDataSource dataSource="jdbc/cikanet"/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CikaNET Admin Console - Add New User</title>
    </head>
    <body>
        <%
        uidGen idGenerator = new uidGen();
        request.setAttribute("merchant_id", idGenerator.getUId(12));
        request.setAttribute("contact_id", idGenerator.getUId(12));
        request.setAttribute("cikanet_acc_no", idGenerator.getUId(12));
        %>
        <c:if test="${not empty param.Submit}">
            <sql:transaction>
                <sql:update var="update_contact_info">
                    INSERT INTO contact_info(street, 
                    city,
                    state,
                    zip,
                    country,
                    email,
                    contact_id) 
                    VALUES(?, ?, ?, ?, ?, ?,?)
                    <sql:param value="${param['street']}"/>
                    <sql:param value="${param['city']}"/>
                    <sql:param value="${param['state']}"/>
                    <sql:param value="${param['zip']}"/>
                    <sql:param value="${param['country']}"/>
                    <sql:param value="${param['email']}"/>
                    <sql:param value="${contact_id}"/>
                </sql:update>
                <sql:query var="bank_lookup">
                    SELECT * FROM banks WHERE bank_name = ?
                    <sql:param value="${param['bank']}"/>                    
                </sql:query>
                <c:forEach var="row" items="${bank_lookup.rows}">
                    <c:set var="bank_sel" value="${row.bank_id}"/>
                </c:forEach>
                <c:choose>
                    <c:when test="${param['bank'] == 'None'}">
                        <c:set var="linked" value="0"/>
                        <c:set var="bank_sel" value="-1"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="linked" value="1"/>                        
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${param['pin'] == param['pin_conf']}">
                        <c:set var="pin" value="${param['pin']}"/>
                    </c:when>
                    <c:otherwise>
                        <c:out value="PINs do not match"/>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${param['auto_unload'] == 'No'}">
                        <c:set var="auto_unload" value="0"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="auto_unload" value="1"/>
                    </c:otherwise>
                </c:choose>
                <% 
                Calendar cal = new GregorianCalendar();
                Date currentDate = cal.getTime();
                java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
                request.setAttribute("current_date", df.format(currentDate));
                %>
                <sql:update var="update_cikanet_accounts">
                    INSERT INTO cikanet_accounts(cikanet_acc_no, 
                    bank_id,
                    linked,
                    creation_date,
                    status_enabled,
                    pin,
                    auto_unload,
                    current_balance,
                    daily_spending_limit,
                    reload_threshold,
                    auto_reload,
                    auto_reload_amt,
                    bank_acc_no) 
                    VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    <sql:param value="${cikanet_acc_no}"/>
                    <sql:param value="${bank_sel}"/>
                    <sql:param value="${linked}"/>
                    <sql:param value="${current_date}"/>
                    <sql:param value="0"/>
                    <sql:param value="${pin}"/>
                    <sql:param value="${auto_unload}"/>
                    <sql:param value="0"/>
                    <sql:param value="-1"/>
                    <sql:param value="-1"/>
                    <sql:param value="0"/>
                    <sql:param value="0"/>
                    <sql:param value="${param['bank_acc_no']}"/>
                </sql:update>
                <sql:update var="update_merchants">
                    INSERT INTO merchants(merchant_name,
                    contact_id,
                    cikanet_acc_no,
                    merchant_id)
                    VALUES(?, ?, ?, ?)
                    <sql:param value="${param['merchant_name']}"/>
                    <sql:param value="${contact_id}"/>
                    <sql:param value="${cikanet_acc_no}"/>
                    <sql:param value="${merchant_id}"/>
                </sql:update>                
            </sql:transaction>
        </c:if>
        <form action="new_merchant.jsp" method="post" name="form1">
            <p class="style2"><b><u>  Add New Merchant</u></b></p>
            <span class="style1"><b>Contact Information</b>  </span>
            <table width="347" border="0">
                <tr>
                    <td width="193">Merchant Name </td>
                    <td width="144"><input type="text" name="merchant_name"></td>
                </tr>
                <tr>
                    <td>Street</td>
                    <td><input type="text" name="street"></td>
                </tr>
                <tr>
                    <td>City</td>
                    <td><input type="text" name="city"></td>
                </tr>
                <tr>
                    <td>Zip Code</td>
                    <td><input type="text" name="zip"></td>
                </tr>
                <tr>
                    <td>State</td>
                    <td><input type="text" name="state"></td>
                </tr>
                <tr>
                    <td>Country</td>
                    <td><input type="text" name="country"></td>
                </tr>
                <tr>
                    <td>Email</td>
                    <td><input type="text" name="email"></td>
                </tr>
            </table>
            <br>
            <span class="style1"><b>CikaNET Account Information</b> </span>
            <table width="348" border="0">
                <tr>
                    <td>Auto-Unload Enabled </td>
                    <td><select name="auto_unload" onChange="MM_jumpMenu('parent',this,0)">
                            <option>No</option>
                            <option>Yes</option>
                    </select></td>
                </tr>
                <tr>
                    <td>Account PIN </td>
                    <td><input name="pin" type="password" /></td>
                </tr>
                <tr>
                    <td>Retype Account PIN </td>
                    <td><input name="pin_conf" type="password" /></td>
                </tr>
            </table>
            <br>
            <span class="style1"><b>Bank Account Information</b> </span>
            <table width="348" border="0">
                <tr>
                    <td width="194">Bank </td>
                    <td width="144"><select name="bank" onChange="MM_jumpMenu('parent',this,0)">
                            <sql:query var="banks" sql="SELECT * FROM banks">
                            </sql:query>
                            <c:forEach var="row" items="${banks.rows}">
                                <option><c:out value="${row.bank_name}"/></option>
                            </c:forEach>
                    </select></td>
                </tr>
                <tr>
                    <td>Account Number </td>
                    <td><input type="text" name="bank_acc_no"></td>
                </tr>
            </table>
            <p>
                <input type="submit" name="Submit" value="Add Merchant">
            </p>
        </form>
        
        
    </body>
</html>
