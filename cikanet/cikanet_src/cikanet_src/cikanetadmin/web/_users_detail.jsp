<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.cikanet.admin.util.uidGen" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<sql:setDataSource dataSource="jdbc/cikanet"/>
<c:if test="${not empty param.Update}">
    <sql:transaction>
        <sql:update var="update_contact_info">
            UPDATE contact_info SET street = ?, 
            city = ?,
            state = ?,
            zip = ?,
            country = ?,
            email = ? 
            WHERE contact_id = ?
            <sql:param value="${param['street']}"/>
            <sql:param value="${param['city']}"/>
            <sql:param value="${param['state']}"/>
            <sql:param value="${param['zip']}"/>
            <sql:param value="${param['country']}"/>
            <sql:param value="${param['email']}"/>
            <sql:param value="${hidden_contact_id}"/>
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
            UPDATE cikanet_accounts
            SET bank_id = ?,
            linked = ?,
            status_enabled = ?,
            pin = ?,
            daily_spending_limit = ?,
            reload_threshold = ?,
            auto_reload = ?,
            auto_reload_amt = ?,
            bank_acc_no = ? 
            WHERE cikanet_acc_no = ?
            <sql:param value="${bank_sel}"/>
            <sql:param value="${linked}"/>
            <sql:param value="${param['account_enabled']}"/>
            <sql:param value="${pin}"/>
            <sql:param value="${param['dailylimit']}"/>
            <sql:param value="${param['threshold']}"/>
            <sql:param value="${param['auto_reload']}"/>
            <sql:param value="${param['auto_reload_amt']}"/>
            <sql:param value="${bank_acc_no}"/>
            <sql:param value="${disp_cikanet_acc_no}"/>
        </sql:update>
        <sql:query var="network_lookup">
            SELECT * FROM networks WHERE network_name = ?
            <sql:param value="${param['network']}"/>                    
        </sql:query>
        <c:forEach var="row" items="${network_lookup.rows}">
            <c:set var="network_sel" value="${row.network_id}"/>
        </c:forEach>
        <sql:update var="update_users">
            UPDATE users
            SET last_name = ?, 
            first_name = ?,
            msisdn = ?,
            network_id = ?
            WHERE cikanet_acc_no = ?
            <sql:param value="${param['lastname']}"/>
            <sql:param value="${param['firstname']}"/>
            <sql:param value="${param['msisdn']}"/>
            <sql:param value="${network_sel}"/>
            <sql:param value="${disp_cikanet_acc_no}"/>
        </sql:update>                
    </sql:transaction>
    <jsp:forward page="_user_summary.jsp"/>
</c:if>
<c:if test="${param['action'] == 'view'}">
    <sql:query var="users_query">
        SELECT * FROM users WHERE cikanet_acc_no = ?
        <sql:param value="${param['id']}"/>
    </sql:query>
    <c:forEach var="row" items="${users_query.rows}">
        <c:set var="disp_lastname" value="${row.last_name}"/>
        <c:set var="disp_firstname" value="${row.first_name}"/>
        <c:set var="disp_msisdn" value="${row.msisdn}"/>
        <c:set var="disp_cikanet_acc_no" value="${row.cikanet_acc_no}" scope="session"/>
        <c:set var="disp_network_id" value="${row.network_id}"/>
        <c:set var="hidden_contact_id" value="${row.contact_id}" scope="session"/>
    </c:forEach>
    <sql:query var="contact_info_query">
        SELECT * FROM contact_info WHERE contact_id = ?
        <sql:param value="${hidden_contact_id}"/>
    </sql:query>
    <c:forEach var="row" items="${contact_info_query.rows}">
        <c:set var="disp_street" value="${row.street}"/>
        <c:set var="disp_city" value="${row.city}"/>
        <c:set var="disp_state" value="${row.state}"/>
        <c:set var="disp_zip" value="${row.zip}"/>
        <c:set var="disp_country" value="${row.country}"/>
        <c:set var="disp_email" value="${row.email}"/>
    </c:forEach>
    <sql:query var="cikanet_acc_query">
        SELECT * FROM cikanet_accounts WHERE cikanet_acc_no = ?
        <sql:param value="${param['id']}"/>
    </sql:query>
    <c:forEach var="row" items="${cikanet_acc_query.rows}">
        <c:set var="disp_bank_id" value="${row.bank_id}"/>
        <c:set var="disp_account_creation_date" value="${row.creation_date}"/>
        <c:set var="disp_current_balance" value="${row.current_balance}"/>
        <c:set var="disp_daily_spending_limit" value="${row.daily_spending_limit}"/>
        <c:set var="disp_reload_threshold" value="${row.reload_threshold}"/>
        <c:set var="disp_auto_reload" value="${row.auto_reload}"/>
        <c:set var="disp_auto_reload_amt" value="${row.auto_reload_amt}"/>
        <c:set var="disp_status_enabled" value="${row.status_enabled}"/>
        <c:set var="disp_pin" value="${row.pin}"/>
        <c:set var="disp_bank_acc_no" value="${row.bank_acc_no}"/>
    </c:forEach>
    <sql:query var="network_query">
        SELECT * FROM networks WHERE network_id = ?
        <sql:param value="${disp_network_id}"/>                
    </sql:query>            
    <c:forEach var="row" items="${network_query.rows}">
        <c:set var="disp_network" value="${row.network_name}"/>
    </c:forEach>
    <sql:query var="bank_query">
        SELECT * FROM banks WHERE bank_id = ?
        <sql:param value="${disp_bank_id}"/>
    </sql:query>
    <c:forEach var="row" items="${bank_query.rows}">
        <c:set var="disp_bank" value="${row.bank_name}"/>
    </c:forEach>
</c:if>
<c:if test="${(param['action'] == 'delete') or (not empty param.Delete)}">
    
    <sql:transaction>
        <sql:query var="users_query">
            SELECT * FROM users WHERE cikanet_acc_no = ?
            <sql:param value="${param['id']}"/>
        </sql:query>
        <c:forEach var="row" items="${users_query.rows}">
            <c:set var="del_contact_id" value="${row.contact_id}"/>
        </c:forEach>
        <sql:update var="delete_user_account">
            DELETE FROM users WHERE cikanet_acc_no = ?
            <sql:param value="${param['id']}"/>
        </sql:update>
        <sql:update var="delete_cikanet_account">
            DELETE FROM cikanet_accounts WHERE cikanet_acc_no = ?
            <sql:param value="${param['id']}"/>
        </sql:update>
        <sql:update var="delete_contact_info">
            DELETE FROM contact_info WHERE contact_id = ?
            <sql:param value="${del_contact_id}"/>
        </sql:update>
    </sql:transaction>
    <jsp:forward page="_user_summary.jsp"/>
</c:if>
<c:if test="${param['action'] == 'manage'}">
    <jsp:forward page="_user_account_manage.jsp"/>
</c:if>
<html>
    <head>
        <!--style type="text/css" media="all">
            @import url("css/maven-base.css");
            @import url("css/maven-theme.css");
            @import url("css/site.css");
            @import url("css/screen.css");
        </style-->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CikaNET Admin Console - View User Details</title>
        <script src="SpryAssets/SpryCollapsiblePanel.js" type="text/javascript"></script>
    <script language="Javascript">
function clearBB() {
                if (confirm("Are you sure you delete this record?")) {
                        
                } else { 
                    
                }
            }
function MM_jumpMenu(targ,selObj,restore){ //v3.0
  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
  if (restore) selObj.selectedIndex=0;
}
</script>
    <link href="SpryAssets/SpryCollapsiblePanel.css" rel="stylesheet" type="text/css">
        <style type="text/css">
<!--
#apDiv1 {
	position:absolute;
	left:400px;
	top:74px;
	width:290px;
	height:128px;
	z-index:1;
}
.style1 {font-size: 1em}
-->
        </style>
</head>
    <body>
    
<!--link rel="stylesheet" type="text/css" href="include/css/rms_tables.css"-->
<form action="_users_detail.jsp" method="post" name="form1">
            <!--<p><b><u> View/Edit User Information</u></b></p>-->
            <div class="content"  style="height:600px; background-color: #ffffff; padding-left: 25px; padding-top: 25px; padding-right: 25px;">
            <div id="CollapsiblePanel1" class="CollapsiblePanel">
              <div class="CollapsiblePanelTab style1" tabindex="0">Contact Information</div>
              <div class="CollapsiblePanelContent">
               <table><tr><td width="361">  <table width="305" border="0">
                 <tr>
                    <td width="25%">Last Name </td>
                    <td><input type="text" name="lastname" class="texta" value="${disp_lastname}"/></td>
                 </tr>
                  <tr>
                    <td>First Name </td>
                    <td><input type="text" name="firstname" value="${disp_firstname}" class="texta"/></td>
                  </tr>
                  <tr>
                    <td>Street</td>
                    <td><input type="text" name="street" value="${disp_street}" class="texta"/></td>
                  </tr>
                  <tr>
                    <td>City</td>
                    <td><input type="text" name="city" value="${disp_city}" class="texta"/></td>
                  </tr>
                  <tr>
                    <td>Zip Code</td>
                    <td><input type="text" name="zip" value="${disp_zip}" class="texta" /></td>
                  </tr>
                </table></td><td width="306"><table width="261" border="0">
            <tr>
                      <td>State</td>
                      <td><input type="text" name="state" value="${disp_state}" class="texta" /></td>
                  </tr>
                    <tr>
                      <td>Country</td>
                      <td><input type="text" name="country" value="${disp_country}" class="texta" /></td>
                    </tr>
                    <tr>
                      <td>Email</td>
                      <td><input type="text" name="email" value="${disp_email}" class="texta" /></td>
                    </tr>
                    <tr>
                      <td>MSISDN</td>
                      <td><input type="text" name="msisdn" value="${disp_msisdn}" class="texta" /></td>
                    </tr>
                    <tr>
                      <td>Network Provider </td>
                      <td><select name="network" onChange="MM_jumpMenu('parent',this,0)">
                          <sql:query var="networks" sql="SELECT * FROM networks"> </sql:query>
                          <c:forEach var="row" items="${networks.rows}"> <option 
                                    
                            
                              <c:if test="${row.network_name == disp_network}">
                                <c:out value="selected"/>
                              </c:if>
                              >
                              <c:out value="${row.network_name}"/>
                              </option>
                          </c:forEach>
                        </select>
                      </td>
                    </tr>
                  </table></td></tr></table>
              
               
               
                <div>
                  
                </div>
              </div>
            </div>
            <span> </span><br>
            <div id="CollapsiblePanel2" class="CollapsiblePanel">
              <div class="CollapsiblePanelTab style1" tabindex="0">CikaNET Account Information (Click to view details)</div>
              <div class="CollapsiblePanelContent">
              <table><tr><td>  <table width="348" border="0">
                  <tr>
                    <td width="40%">Cikanet Account Number </td>
                    <td width="60%">${disp_cikanet_acc_no}</td>
                  </tr>
                  <tr>
                    <td>Account Creation Date</td>
                    <td>${disp_account_creation_date}</td>
                  </tr>

                  <tr>
                    <td width="40%">Daily Spending Limit </td>
                    <td><input type="text" name="dailylimit" value="${disp_daily_spending_limit}" class="texta"></td>
                  </tr>
                  <tr>
                    <td>Reload Threshold </td>
                    <td><input type="text" name="threshold" value="${disp_reload_threshold}" class="texta"></td>
                  </tr>
                  </table></td><td><table>
                  <tr>
                    <td>Current Balance</td>
                    <td>${disp_current_balance}</td>
                  </tr>
                  <tr>
                    <td>Balance Auto-Reload Enabled </td>
                    <td><select name="auto_reload" onChange="MM_jumpMenu('parent',this,0)">
                        <option value="0">No</option>
                        <option value="1">Yes</option>
                    </select></td>
                </tr>
                  <tr>
                    <td>Balance Auto-Reload Amount </td>
                    <td><input type="text" name="auto_reload_amt" value="${disp_auto_reload_amt}" class="texta"></td>
                  </tr>
                  <tr>
                    <td>Account PIN </td>
                    <td><input name="pin" type="password" value="${disp_pin}" class="texta"/></td>
                  </tr>
                  <tr>
                    <td>Retype Account PIN </td>
                    <td><input name="pin_conf" type="password" value="${disp_pin}" class="texta"/></td>
                  </tr>
                  <tr>
                    <td>Account Enabled</td>
                    <td><select name="account_enabled" onChange="MM_jumpMenu('parent',this,0)">
                        <option value="0">No</option>
                        <option value="1" >Yes</option>
                    </select></td>
                  </tr>
                </table></td></tr></table>
                <p>
                </p>
              </div>
            </div>
            <br>
            <div id="CollapsiblePanel3" class="CollapsiblePanel">
              <div class="CollapsiblePanelTab style1" tabindex="0">Bank Account Information(Click to view details)</div>
              <div class="CollapsiblePanelContent">
                <table width="348" border="0">
                  <tr>
                    <td width="25%">Bank </td>
                    <td><select name="bank" onChange="MM_jumpMenu('parent',this,0)">
                        <sql:query var="banks" sql="SELECT * FROM banks"> </sql:query>
                        <c:forEach var="row" items="${banks.rows}"> <option
                                    
                            <c:if test="${row.bank_name == disp_bank}">
                              <c:out value="selected"/>
                            </c:if>
                            >
                          <c:out value="${row.bank_name}"/>
                          </option>
                        </c:forEach>
                    </select></td>
                  </tr>
                  <tr>
                    <td>Account Number </td>
                    <td><input type="text" name="bank_acc_no" value="${disp_bank_acc_no}" class="texta"></td>
                  </tr>
                </table>
              </div>
            </div>
            <p>
              <input type="submit" name="Update" value="Update Fields">
                <input type="submit" name="Delete" value="Delete Account">
            </p>
          </div>
    </form>        
        <script type="text/javascript">
<!--
var CollapsiblePanel1 = new Spry.Widget.CollapsiblePanel("CollapsiblePanel1");
var CollapsiblePanel2 = new Spry.Widget.CollapsiblePanel("CollapsiblePanel2", {contentIsOpen:false});
var CollapsiblePanel3 = new Spry.Widget.CollapsiblePanel("CollapsiblePanel3", {contentIsOpen:false});
//-->
</script>
</body>
</html>
