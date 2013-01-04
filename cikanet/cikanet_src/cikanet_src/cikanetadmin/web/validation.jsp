<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="com.cikanet.admin.util.uidGen" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Iterator" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
uidGen idGenerator = new uidGen();
request.setAttribute("user_id", idGenerator.getUId(12));
request.setAttribute("contact_id", idGenerator.getUId(12));
request.setAttribute("cikanet_acc_no", idGenerator.getUId(12));
%>
<sql:setDataSource dataSource="jdbc/cikanet"/>
<c:if test="${not empty param.Submit}">
    <%-- Ensure that all required fields have been filled 
    <c:if test="${(param.lastname == '') or(param.firstname == '') or
                    (param.street == '')}">
        <SCRIPT LANGUAGE="JAVASCRIPT" TYPE="TEXT/JAVASCRIPT">
            alert("Please fill all required fields");
        </script>
    </c:if> --%>
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
                <c:set var="bank_acc_no" value="-1"/>
            </c:when>
            <c:otherwise>
                <c:set var="linked" value="1"/>   
                <c:set var="bank_acc_no" value="${param['bank_acc_no']}"/>
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
            <sql:param value="0"/>
            <sql:param value="0"/>
            <sql:param value="${param['dailylimit']}"/>
            <sql:param value="${param['threshold']}"/>
            <sql:param value="${param['auto_reload']}"/>
            <sql:param value="${param['auto_reload_amt']}"/>
            <sql:param value="${bank_acc_no}"/>
        </sql:update>
        <sql:query var="network_lookup">
            SELECT * FROM networks WHERE network_name = ?
            <sql:param value="${param['network']}"/>                    
        </sql:query>
        <c:forEach var="row" items="${network_lookup.rows}">
            <c:set var="network_sel" value="${row.network_id}"/>
        </c:forEach>
        <sql:update var="update_users">
            INSERT INTO users(last_name, 
            first_name,
            msisdn,
            contact_id,
            network_id,
            cikanet_acc_no,
            user_id)
            VALUES(?, ?, ?, ?, ?, ?, ?)
            <sql:param value="${param['lastname']}"/>
            <sql:param value="${param['firstname']}"/>
            <sql:param value="${param['msisdn']}"/>
            <sql:param value="${contact_id}"/>
            <sql:param value="${network_sel}"/>
            <sql:param value="${cikanet_acc_no}"/>
            <sql:param value="${user_id}"/>
        </sql:update>                
    </sql:transaction>
</c:if>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Untitled Document</title>
        <script src="SpryAssets/SpryValidationTextField.js" type="text/javascript"></script>
        <script src="SpryAssets/SpryCollapsiblePanel.js" type="text/javascript"></script>
        <script src="SpryAssets/SpryValidationTextarea.js" type="text/javascript"></script>
        <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
        <link rel="stylesheet" type="text/css" href="include/css/rms_tables.css"> 
        <link rel="stylesheet" type="text/css" href="user-summary.jsp_files/global.css">
        <link href="SpryAssets/SpryValidationTextField.css" rel="stylesheet" type="text/css" />
        <link href="SpryAssets/SpryCollapsiblePanel.css" rel="stylesheet" type="text/css">
        <style type="text/css">
            <!--
.style1 {font-size: 1em}
-->
        </style>
        <link href="SpryAssets/SpryValidationTextField.css" rel="stylesheet" type="text/css">
        <link href="SpryAssets/SpryValidationTextarea.css" rel="stylesheet" type="text/css">
    </head>
    
    <body id="jive-body">
        <!-- header file -->
        <div id="jive-header">
            <jsp:include page="header.jsp" flush="true" />
        </div>
        
        <!-- header file -->
        
        <div id="jive-main">
            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                <tbody>
                    <tr valign="top">
                        <td width="1%" id="jive-sidebar-box">
                            <div id="jive-sidebar">
                                <jsp:include page="_user_details_toolbar.jsp" flush="true" />
                            </div> 
                        </td>
                        <td width="99%" id="jive-content">
                            
                            <div id="jive-title">
                            Add New User            </div>
                            
                            <p>
                                <style type="text/css">
                                    .jive-current {
                                    font-weight : bold;
                                    text-decoration : none;
                                    }
                                </style>
                                
                                <!-- -->
                            </p>
                            <form id="form1" name="form1" method="post" action="">
                                
                                <div id="CollapsiblePanel1" class="CollapsiblePanel">
                                    <div class="CollapsiblePanelTab style1" tabindex="0"><b>Contact Information</b></div>
                                    <div class="CollapsiblePanelContent">
                                        <table border="0">
                                            <tr>
                                                <td width="25%">Last Name* </td>
                                                <td><span id="sprytextfield1">                                    <input type="text" name="lastname" id="lastname" />
                                <span class="textfieldRequiredMsg">A value is required.</span></span> </td>
                                            </tr>
                                            <tr>
                                                <td>First Name* </td>
                                                <td><span id="sprytextfield2">
                                                        <input type="text" name="firstname" id="firstname">
                                                <span class="textfieldRequiredMsg">A value is required.</span></span></td>
                                            </tr>
                                            <tr>
                                                <td>Address*</td>
                                                <td><span id="sprytextarea1">
                                                        <textarea name="street" id="textarea1" cols="45" rows="5"></textarea>
                                                <span class="textareaRequiredMsg">Enter a valid address.</span></span></td>
                                            </tr>
                                            <tr>
                                                <td>City*</td>
                                                <td><span id="sprytextfield3">
                                                        <input type="text" name="city" id="city">
                                                <span class="textfieldRequiredMsg">A value is required.</span></span></td>
                                            </tr>
                                            <tr>
                                                <td>Zip Code</td>
                                                <td><span id="sprytextfield4">
                                                        <input type="text" name="zip" id="zip">
                                                <span class="textfieldRequiredMsg">A value is required.</span></span></td>
                                            </tr>
                                            <tr>
                                                <td>State</td>
                                                <td><jsp:include page="inc/state_codes.html" flush="true"/></td>
                                            </tr>
                                            <tr>
                                                <td>Country*</td>
                                                <td><jsp:include page="inc/country_codes.html" flush="true"/></td>
                                            </tr>
                                            <tr>
                                                <td>Email</td>
                                                <td><input type="text" name="email" class="texta"></td>
                                            </tr>
                                            <tr>
                                                <td>MSISDN*</td>
                                                <td><span id="sprytextfield5">
                                                        <input type="text" name="msisdn" id="msisdn">
                                                <span class="textfieldRequiredMsg">MSISDN value is required.</span></span></td>
                                            </tr>
                                            <tr>
                                                <td>Network Provider* </td>
                                                <td><select name="network" onChange="MM_jumpMenu('parent',this,0)">
                                                        <option value="">-- Select Network --</option>
                                                        <sql:query var="networks" sql="SELECT * FROM networks"> </sql:query>
                                                        <c:forEach var="row" items="${networks.rows}">
                                                            <option>
                                                                <c:out value="${row.network_name}"/>
                                                            </option>
                                                        </c:forEach>
                                                </select></td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                <p>&nbsp;</p>
                                <div id="CollapsiblePanel2" class="CollapsiblePanel">
                                    <div class="CollapsiblePanelTab style1" tabindex="0"><b>CikaNET Account Information</b></div>
                                    <div class="CollapsiblePanelContent">
                                        <table border="0">
                                            <tr>
                                                <td width="25%">Daily Spending Limit </td>
                                                <td><input type="text" name="dailylimit" value="100" class="texta"></td>
                                            </tr>
                                            <tr>
                                                <td>Reload Threshold </td>
                                                <td><input type="text" name="threshold" value="0" class="texta"></td>
                                            </tr>
                                            <tr>
                                                <td>Auto-Reload Enabled </td>
                                                <td><select name="auto_reload" onChange="MM_jumpMenu('parent',this,0)">
                                                        <option value="0">No</option>
                                                        <option value="1">Yes</option>
                                                </select></td>
                                            </tr>
                                            <tr>
                                                <td>Auto-Reload Amount </td>
                                                <td><input type="text" name="auto_reload_amt" value="0" class="texta"></td>
                                            </tr>
                                            <tr>
                                                <td>Account PIN* </td>
                                                <td><span id="sprytextfield6">
                                                        <input type="text" name="pin" id="pin">
                                                <span class="textfieldRequiredMsg">A value is required.</span></span></td>
                                            </tr>
                                            <tr>
                                                <td>Retype Account PIN* </td>
                                                <td><input name="pin_conf" type="password"  class="texta"/></td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                <p>&nbsp;</p>
                                <div id="CollapsiblePanel3" class="CollapsiblePanel">
                                    <div class="CollapsiblePanelTab style1" tabindex="0"><b>Bank Account Information</b></div>
                                    <div class="CollapsiblePanelContent">
                                        <table border="0">
                                            <tr>
                                                <td width="25%">Bank* </td>
                                                <td><select name="bank" onChange="MM_jumpMenu('parent',this,0)">
                                                        <option value="">-- Select Bank --</option>
                                                        <sql:query var="banks" sql="SELECT * FROM banks"> </sql:query>
                                                        <c:forEach var="row" items="${banks.rows}">
                                                            <option>
                                                                <c:out value="${row.bank_name}"/>
                                                            </option>
                                                        </c:forEach>
                                                </select></td>
                                            </tr>
                                            <tr>
                                                <td>Account Number </td>
                                                <td><input type="text" name="bank_acc_no" class="texta"></td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                <p>&nbsp;</p>
                                <p>
                                <input type="submit" name="button" id="button" value="Submit" />
                            </form>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        
        <script type="text/javascript">
<!--
var sprytextfield1 = new Spry.Widget.ValidationTextField("sprytextfield1");

var CollapsiblePanel1 = new Spry.Widget.CollapsiblePanel("CollapsiblePanel1");
var CollapsiblePanel2 = new Spry.Widget.CollapsiblePanel("CollapsiblePanel2");
var CollapsiblePanel3 = new Spry.Widget.CollapsiblePanel("CollapsiblePanel3");
//var sprytextfield1 = new Spry.Widget.ValidationTextField("sprytextfield1", "none", {hint:"Enter your surname"});
var sprytextfield2 = new Spry.Widget.ValidationTextField("sprytextfield2", "none", {validateOn:["change"]});
var sprytextarea1 = new Spry.Widget.ValidationTextarea("sprytextarea1", {validateOn:["change"]});
var sprytextfield3 = new Spry.Widget.ValidationTextField("sprytextfield3", "none", {validateOn:["change"]});
var sprytextfield4 = new Spry.Widget.ValidationTextField("sprytextfield4");
var sprytextfield5 = new Spry.Widget.ValidationTextField("sprytextfield5");
var sprytextfield6 = new Spry.Widget.ValidationTextField("sprytextfield6");

//-->
        </script>
    </body>
</html>
