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
        <title>CikaNET Admin Console - Add New Bank</title>
    </head>
    <body>
        <c:if test="${not empty param.Submit}">
            <%
            uidGen idGenerator = new uidGen();
            request.setAttribute("bank_id", idGenerator.getUId(12));
            request.setAttribute("contact_id", idGenerator.getUId(12));
            %>
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
                <sql:update var="update_banks">
                    INSERT INTO banks(bank_id, 
                    contact_id,
                    routing_no,
                    bank_name)
                    VALUES(?, ?, ?, ?)
                    <sql:param value="${bank_id}"/>
                    <sql:param value="${contact_id}"/>
                    <sql:param value="${param['routing_no']}"/>
                    <sql:param value="${param['bank_name']}"/>
                </sql:update>                
            </sql:transaction>
        </c:if>
        <form action="new_bank.jsp" method="post" name="form1">
            <p class="style2"><b><u>Add New Bank </u></b></p>
            <span class="style1"><b>Contact Information</b> </span>
            <table width="347" border="0">
                <tr>
                    <td width="193">Bank Name </td>
                    <td width="144"><input type="text" name="bank_name" /></td>
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
                    <td><input type="text" name="email" /></td>
                </tr>
            </table>
            <br />
            <span class="style1"><b>Bank Account Information </b></span>
            <table width="348" border="0">
                <tr>
                    <td width="194">Routing Number </td>
                    <td width="144"><input type="text" name="routing_no" /></td>
                </tr>
            </table>
            <p>
                <input type="submit" name="Submit" value="Add Bank" />
            </p>
        </form>
    </body>
</html>
