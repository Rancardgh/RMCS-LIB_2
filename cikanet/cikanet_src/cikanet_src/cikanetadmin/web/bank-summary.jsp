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
		<a href="default.jsp?logout=true">Logout [admin]</a>
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
                <ul><li class="category">Banks</li><li class="currentlink">
                    <a href="bank-summary.jsp" title="Click to see a list of banks in the system"
                      onmouseover="self.status='Click to see a list of banks in the system';return true;" onMouseOut="self.status='';return true;"
                      >Bank Summary</a>
                     
                </li><li class="">
                    <a href="/bank-create.jsp" title="Click to add a new bank to the system"
                      onmouseover="self.status='Click to add a new bank to the system';return true;" onMouseOut="self.status='';return true;"
                      >Create New Bank</a>
                     
                </li><li class="">
                    <a href="/user-search.jsp" title="Click to search for a particular user"
                      onmouseover="self.status='Click to search for a particular user';return true;" onMouseOut="self.status='';return true;"
                      >Bank Search</a>
                     
                </li><li class="">
                    <a href="/plugins/search/advance-user-search.jsp" title="Advanced user search"
                      onmouseover="self.status='Advanced user search';return true;" onMouseOut="self.status='';return true;"
                      >Advanced Bank Search</a>
                     
                </li><li class="category">Groups</li><li class="">
                    <a href="/group-summary.jsp" title="Click to see a list of groups in the system"
                      onmouseover="self.status='Click to see a list of groups in the system';return true;" onMouseOut="self.status='';return true;"
                      >Group Summary</a>
                     
                </li><li class="">
                    <a href="/group-create.jsp" title="Click to add a new group to the system"
                      onmouseover="self.status='Click to add a new group to the system';return true;" onMouseOut="self.status='';return true;"
                      >Create New Group</a>
                     
                </li></ul>
                <br>
                <img src="user-summary.jsp_files/blank.gif" width="150" height="1" border="0" alt="">
            </div>
        </td>
        <td width="99%" id="jive-content">

            

            <div id="jive-title">
                Bank Summary
            </div>

            <style type="text/css">
.jive-current {
    font-weight : bold;
    text-decoration : none;
}
</style>



<p>
Total Banks:
<b>7</b> --


Sorted by Username

-- Banks per page:
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

 <jsp:include page ="_bank_list.jsp" flush="true" />

<!--
<div class="jive-table">
<table cellpadding="0" cellspacing="0" border="0" width="100%">
<thead>
    <tr>
        <th>&nbsp;</th>
        <th nowrap>Online</th>
        <th nowrap>Username</th>
        <th nowrap>Name</th>
        <th nowrap>Created</th>
        <th nowrap>Last Logout</th>
         
        <th nowrap>Edit</th>
        <th nowrap>Delete</th>
        
    </tr>
</thead>
<tbody>


    <tr class="jive-odd">
        <td width="1%">
            1
        </td>
        <td width="1%" align="center" valign="middle">
            

                <img src="user-summary.jsp_files/user-clear-16x16.gif" width="16" height="16" border="0" alt="Offline">

            
        </td>
        <td width="23%">
            <a href="user-properties.jsp?username=233276221212">233276221212</a>
        </td>
        <td width="33%">&nbsp;
             
        </td>
        <td width="15%">
            Feb 7, 2007
        </td>
        <td width="25%">
            181 days, 10 hours, 42 minutes

        </td>
         
        <td width="1%" align="center">
            <a href="user-edit-form.jsp?username=233276221212"
             title="Click to edit..."
             ><img src="user-summary.jsp_files/edit-16x16.gif" width="16" height="16" border="0" alt="Click to edit..."></a>
        </td>
        <td width="1%" align="center" style="border-right:1px #ccc solid;">
            <a href="user-delete.jsp?username=233276221212"
             title="Click to delete..."
             ><img src="user-summary.jsp_files/delete-16x16.gif" width="16" height="16" border="0" alt="Click to delete..."></a>
        </td>
        
    </tr>


    <tr class="jive-even">
        <td width="1%">
            2
        </td>
        <td width="1%" align="center" valign="middle">
            

                <img src="user-summary.jsp_files/user-clear-16x16.gif" width="16" height="16" border="0" alt="Offline">

            
        </td>
        <td width="23%">
            <a href="user-properties.jsp?username=admin">admin</a>
        </td>
        <td width="33%">
            Administrator &nbsp;
        </td>
        <td width="15%">
            Nov 22, 2006
        </td>
        <td width="25%">&nbsp;
            

        </td>
         
        <td width="1%" align="center">
            <a href="user-edit-form.jsp?username=admin"
             title="Click to edit..."
             ><img src="user-summary.jsp_files/edit-16x16.gif" width="16" height="16" border="0" alt="Click to edit..."></a>
        </td>
        <td width="1%" align="center" style="border-right:1px #ccc solid;">
            <a href="user-delete.jsp?username=admin"
             title="Click to delete..."
             ><img src="user-summary.jsp_files/delete-16x16.gif" width="16" height="16" border="0" alt="Click to delete..."></a>
        </td>
        
    </tr>


    <tr class="jive-odd">
        <td width="1%">
            3
        </td>
        <td width="1%" align="center" valign="middle">
            

                <img src="user-summary.jsp_files/user-clear-16x16.gif" width="16" height="16" border="0" alt="Offline">

            
        </td>
        <td width="23%">
            <a href="user-properties.jsp?username=ehi">ehi</a>
        </td>
        <td width="33%">
            Ehizogie &nbsp;
        </td>
        <td width="15%">
            Jan 4, 2007
        </td>
        <td width="25%">
            209 days, 14 hours, 11 minutes

        </td>
         
        <td width="1%" align="center">
            <a href="user-edit-form.jsp?username=ehi"
             title="Click to edit..."
             ><img src="user-summary.jsp_files/edit-16x16.gif" width="16" height="16" border="0" alt="Click to edit..."></a>
        </td>
        <td width="1%" align="center" style="border-right:1px #ccc solid;">
            <a href="user-delete.jsp?username=ehi"
             title="Click to delete..."
             ><img src="user-summary.jsp_files/delete-16x16.gif" width="16" height="16" border="0" alt="Click to delete..."></a>
        </td>
        
    </tr>


    <tr class="jive-even">
        <td width="1%">
            4
        </td>
        <td width="1%" align="center" valign="middle">
            

                <img src="user-summary.jsp_files/user-clear-16x16.gif" width="16" height="16" border="0" alt="Offline">

            
        </td>
        <td width="23%">
            <a href="user-properties.jsp?username=kafka">kafka</a>
        </td>
        <td width="33%">
            franz &nbsp;
        </td>
        <td width="15%">
            Jan 4, 2007
        </td>
        <td width="25%">&nbsp;
            

        </td>
         
        <td width="1%" align="center">
            <a href="user-edit-form.jsp?username=kafka"
             title="Click to edit..."
             ><img src="user-summary.jsp_files/edit-16x16.gif" width="16" height="16" border="0" alt="Click to edit..."></a>
        </td>
        <td width="1%" align="center" style="border-right:1px #ccc solid;">
            <a href="user-delete.jsp?username=kafka"
             title="Click to delete..."
             ><img src="user-summary.jsp_files/delete-16x16.gif" width="16" height="16" border="0" alt="Click to delete..."></a>
        </td>
        
    </tr>


    <tr class="jive-odd">
        <td width="1%">
            5
        </td>
        <td width="1%" align="center" valign="middle">
            

                <img src="user-summary.jsp_files/user-clear-16x16.gif" width="16" height="16" border="0" alt="Offline">

            
        </td>
        <td width="23%">
            <a href="user-properties.jsp?username=kofi">kofi</a>
        </td>
        <td width="33%">
            Kofi &nbsp;
        </td>
        <td width="15%">
            Jan 7, 2007
        </td>
        <td width="25%">
            209 days, 14 hours, 11 minutes

        </td>
         
        <td width="1%" align="center">
            <a href="user-edit-form.jsp?username=kofi"
             title="Click to edit..."
             ><img src="user-summary.jsp_files/edit-16x16.gif" width="16" height="16" border="0" alt="Click to edit..."></a>
        </td>
        <td width="1%" align="center" style="border-right:1px #ccc solid;">
            <a href="user-delete.jsp?username=kofi"
             title="Click to delete..."
             ><img src="user-summary.jsp_files/delete-16x16.gif" width="16" height="16" border="0" alt="Click to delete..."></a>
        </td>
        
    </tr>


    <tr class="jive-even">
        <td width="1%">
            6
        </td>
        <td width="1%" align="center" valign="middle">
            

                <img src="user-summary.jsp_files/user-clear-16x16.gif" width="16" height="16" border="0" alt="Offline">

            
        </td>
        <td width="23%">
            <a href="user-properties.jsp?username=kotoka">kotoka</a>
        </td>
        <td width="33%">&nbsp;
             
        </td>
        <td width="15%">
            Feb 7, 2007
        </td>
        <td width="25%">
            180 days, 13 hours, 44 minutes

        </td>
         
        <td width="1%" align="center">
            <a href="user-edit-form.jsp?username=kotoka"
             title="Click to edit..."
             ><img src="user-summary.jsp_files/edit-16x16.gif" width="16" height="16" border="0" alt="Click to edit..."></a>
        </td>
        <td width="1%" align="center" style="border-right:1px #ccc solid;">
            <a href="user-delete.jsp?username=kotoka"
             title="Click to delete..."
             ><img src="user-summary.jsp_files/delete-16x16.gif" width="16" height="16" border="0" alt="Click to delete..."></a>
        </td>
        
    </tr>


    <tr class="jive-odd">
        <td width="1%">
            7
        </td>
        <td width="1%" align="center" valign="middle">
            

                <img src="user-summary.jsp_files/user-clear-16x16.gif" width="16" height="16" border="0" alt="Offline">

            
        </td>
        <td width="23%">
            <a href="user-properties.jsp?username=null">null</a>
        </td>
        <td width="33%">&nbsp;
             
        </td>
        <td width="15%">
            Feb 7, 2007
        </td>
        <td width="25%">&nbsp;
            

        </td>
         
        <td width="1%" align="center">
            <a href="user-edit-form.jsp?username=null"
             title="Click to edit..."
             ><img src="user-summary.jsp_files/edit-16x16.gif" width="16" height="16" border="0" alt="Click to edit..."></a>
        </td>
        <td width="1%" align="center" style="border-right:1px #ccc solid;">
            <a href="user-delete.jsp?username=null"
             title="Click to delete..."
             ><img src="user-summary.jsp_files/delete-16x16.gif" width="16" height="16" border="0" alt="Click to delete..."></a>
        </td>
        
    </tr>


</tbody>
</table>
</div>
-->
        </td>
    </tr>
</tbody>
</table>
</div>

</body>
</html>