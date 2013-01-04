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
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>CikaNet Administrative Console</title>
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
        
        <title>CikaNET Admin Console - Add New Network</title>
</head>
    <c:if test="${not empty param.Submit}">
        <%
        uidGen idGenerator = new uidGen();
        request.setAttribute("network_id", idGenerator.getUId(12));
        request.setAttribute("contact_id", idGenerator.getUId(12));
        request.setAttribute("cikanet_acc_no", idGenerator.getUId(12));
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
                <sql:param value="${param['bank']}"/> 
                <sql:param value="1"/>
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
            <sql:update var="update_networks">
                INSERT INTO networks(network_id, 
                contact_id,
                cikanet_acc_no,
                network_name,
                driver_name)
                VALUES(?, ?, ?, ?, ?)
                
                <sql:param value="${network_id}"/>
                <sql:param value="${contact_id}"/>
                <sql:param value="${cikanet_acc_no}"/>
                <sql:param value="${param['network_name']}"/>
                <sql:param value="${param['short_network_name']}"/>
            </sql:update>    
            <c:set var="isNetworkAdded" value="true" />
        </sql:transaction>
    </c:if>
    
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
                                <jsp:include page="_network_details_toolbar.jsp" flush="true" />
                        </div>                        </td>
                        <td width="99%" id="jive-content">
                            
                            <div id="jive-title">
                            Add New Network           </div>
                            
                            
                            <style type="text/css">
                                .jive-current {
                                font-weight : bold;
                                text-decoration : none;
                                }
                            </style>
                            <!-- -->
                           
                            <c:choose>
                                <c:when test="${isNetworkAdded=='true'}">
                                    Network <a href ="network-details.jsp?id=${cikanet_acc_no}"> ${param['firstname']} ${param['lastname']} </a> sucessfully added<BR><br>
                                <img src="cikanetadmin/user-summary.jsp_files/success-16x16.gif" alt="success" width="16" height="16" /></c:when>
                                <c:otherwise>
                                    <c:if test="${userAddedMsg!=null}" >
                                        <B> ${userAddedMsg} <B><BR/>
                                        <br/>
                                    </c:if>
                                    <form action="new_network.jsp" method="post" name="form1">
                                        <div id="CollapsiblePanel1" class="CollapsiblePanel">
                                            <div class="CollapsiblePanelTab style1" tabindex="0">Contact Information</div>
                                            <div class="CollapsiblePanelContent">
                                                <table width="549" border="0">
                                                    <tr>
                                                        <td width="160">Network Name </td>
                                                        <td width="379"><span id="sprytextfield1">
                                                                <input type="text" name="network_name" id="network_name">
                                                        <span class="textfieldRequiredMsg">A value is required.</span></span></td>
                                                    </tr>
                                                    <tr>
                                                        <td width="160">Short Network Name (no spaces)</td>
                                                        <td width="379"><span id="sprytextfield2">
                                                                <input type="text" name="short_network_name" />
                                                            <span class="textfieldRequiredMsg">A value is required.</span></span>
                                                        </td>
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
                                                        <td><span id="sprytextfield6">
                                                        <input type="text" name="country" id="country">
                                                        <span class="textfieldRequiredMsg">A value is required.</span></span></td>
                                                  </tr>
                                                    <tr>
                                                        <td>Email</td>
                                                        <td><input type="text" name="email" /></td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                        <p>&nbsp;</p>
                                        <div id="CollapsiblePanel2" class="CollapsiblePanel">
                                            <div class="CollapsiblePanelTab style1" tabindex="0">CikaNet Account Information</div>
                                            <div class="CollapsiblePanelContent">
                                                <table width="549" border="0">
                                                    <tr>
                                                        <td width="154"> Auto-Unload Enabled </td>
                                                        <td width="385"><select name="auto_unload">
                                                                <option>No</option>
                                                                <option>Yes</option>
                                                        </select></td>
                                                    </tr>
                                                    <tr>
                                                        <td>Account PIN </td>
                                                        <td><span id="sprytextfield3">
                                                                <input type="text" name="pin" id="pin">
                                                        <span class="textfieldRequiredMsg">A value is required.</span></span></td>
                                                    </tr>
                                                    <tr>
                                                        <td>Retype Account PIN </td>
                                                        <td><span id="sprytextfield4">
                                                                <input type="text" name="pin_conf" id="pin_conf">
                                                        <span class="textfieldRequiredMsg">A value is required.</span></span></td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                        <p>&nbsp;</p>
                                        <div id="CollapsiblePanel3" class="CollapsiblePanel">
                                            <div class="CollapsiblePanelTab" tabindex="0">Bank Account Information </div>
                                            <div class="CollapsiblePanelContent">
                                                <table width="547" border="0">
                                                    <tr>
                                                        <td width="159">Bank </td>
                                                        <td width="378"><select name="bank">
                                                                <sql:query var="banks" sql="SELECT * FROM banks"> </sql:query>
                                                                <c:forEach var="row" items="${banks.rows}">
                                                                    <option value= "<c:out value="${row.bank_id}"/>                
                                                                    ">
                                                                        <c:out value="${row.bank_name}"/>
                                                                    </option>
                                                                </c:forEach>
                                                        </select></td>
                                                    </tr>
                                                    <tr>
                                                        <td>Account Number </td>
                                                        <td><span id="sprytextfield5">
                                                                <input type="text" name="bank_acc_no">
                                                        <span class="textfieldRequiredMsg">A value is required.</span></span></td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                        <p>&nbsp;</p>
                                        <p>
                                            <input type="submit" name="Submit" value="Add User">
                                        </p>
                                    </form>
                                </c:otherwise>
                        </c:choose>                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <script type="text/javascript">
<!--
var CollapsiblePanel1 = new Spry.Widget.CollapsiblePanel("CollapsiblePanel1");
var CollapsiblePanel2 = new Spry.Widget.CollapsiblePanel("CollapsiblePanel2");
var CollapsiblePanel3 = new Spry.Widget.CollapsiblePanel("CollapsiblePanel3");
var sprytextfield1 = new Spry.Widget.ValidationTextField("sprytextfield1");
var sprytextfield2 = new Spry.Widget.ValidationTextField("sprytextfield2");
var sprytextfield3 = new Spry.Widget.ValidationTextField("sprytextfield3");
var sprytextfield4 = new Spry.Widget.ValidationTextField("sprytextfield4");
var sprytextfield5 = new Spry.Widget.ValidationTextField("sprytextfield5");
var sprytextfield6 = new Spry.Widget.ValidationTextField("sprytextfield6");
//-->
</script>
    </body>
</html>
