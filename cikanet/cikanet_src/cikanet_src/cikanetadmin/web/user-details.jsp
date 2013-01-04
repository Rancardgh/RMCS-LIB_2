<%@ page import="java.util.Iterator" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html><!-- Source is http://127.0.0.1:9090/user-summary.jsp -->
<head>
    <title>CikaNet Admin Console: User Summary</title>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
    <link rel="stylesheet" type="text/css" href="include/css/rms_tables.css"> 
    <link rel="stylesheet" type="text/css" href="user-summary.jsp_files/global.css">
  
    <script language="JavaScript" type="text/javascript" src="user-summary.jsp_files/prototype.js"></script>
    <script language="JavaScript" type="text/javascript" src="user-summary.jsp_files/scriptaculous.js"></script>
    <script language="JavaScript" type="text/javascript" src="user-summary.jsp_files/cookies.js"></script>
    <script language="JavaScript" type="text/javascript">
    <!-- // code for window popups
    function helpwin() {
        var newwin = window.open('/help/index.html#about_users_and_groups.html',
            'helpWindow','width=750,height=550,menubar=yes,location=no,personalbar=no,scrollbars=yes,resize=yes');
        newwin.focus();
    }
    //-->
    </script>
    <script type="text/javascript" src="user-summary.jsp_files/behaviour.js"></script>
    <script type="text/javascript">
    // Add a nice little rollover effect to any row in a jive-table object. This will help
    // visually link left and right columns.
    /*
    var myrules = {
        '.jive-table TBODY TR' : function(el) {
            el.onmouseover = function() {
                this.style.backgroundColor = '#ffffee';
            }
            el.onmouseout = function() {
                this.style.backgroundColor = '#ffffff';
            }
        }
    };
    Behaviour.register(myrules);
    */
    </script>
    <meta name="pageID" content="user-summary"/>
        <meta name="helpPage" content="about_users_and_groups.html"/>
</head>

<body id="jive-body">

<div id="jive-header">
	<div class="info">
		<a href="#" onClick="helpwin();return false;"><img src="user-summary.jsp_files/header-help_new.gif" width="22" height="22" border="0" alt="Click for help"></a><br>
		CikaNet RTEMS 1.0</div>
	<div id="jive-logo-image_new">
		<strong>CikaNet Administration Console</strong>	</div>
<div id="jive-logout" style="float: right;">
		<a href="/index.jsp?logout=true">Logout [admin]</a>
	</div>
	<jsp:include page="navigation.jsp" flush="true" />
	<div id="sidebar-top"></div>
</div>



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
                User Details
            </div>

            <style type="text/css">
.jive-current {
    font-weight : bold;
    text-decoration : none;
}
</style>



<p>
Total Users:
<b>7</b> --


Sorted by Username

-- Users per page:
<select size="1" onChange="location.href='user-summary.jsp?start=0&range=' + this.options[this.selectedIndex].value;">

    

        <option value="15"
         selected>15</option>

    

        <option value="25"
         >25</option>

    

        <option value="50"
         >50</option>

    

        <option value="75"
         >75</option>

    

        <option value="100"
         >100</option>

    

</select>
</p>
     <c:choose>
            <c:when test="${param.useraction=='1'}">
           <jsp:include page="_users_detail.jsp" flush="true"/>
                </c:when>
            <c:when test="${param.useraction=='2'}">
              <jsp:include page="_user_transaction_history.jsp" flush="true"/>
                </c:when>
            <c:when test="${param.useraction=='3'}">
              <jsp:include page="_user_account_manage.jsp" flush="true"/>
                </c:when>
             <c:otherwise>
                <jsp:include page="_users_detail.jsp" flush="true"/>
            </c:otherwise>
        </c:choose>

        </td>
    </tr>
</tbody>
</table>
</div>

</body>
</html>