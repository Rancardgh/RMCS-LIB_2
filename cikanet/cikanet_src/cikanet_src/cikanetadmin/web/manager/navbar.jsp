<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- saved from url=(0014)about:internet -->
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
     <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="description" content="VASP Manager" />
		<link rel="shortcut icon" href="../include/images/favicon.ico" >
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>rancardmobility.com </title>
        <style type="text/css">
            td img {display: block;}body {
            margin-left: 0px;
            margin-top: 0px;
            margin-right: 0px;
            margin-bottom: 0px;
            }
        </style>
        <!--Fireworks 8 Dreamweaver 8 target.  Created Thu Jun 29 12:40:20 GMT+0000 (Greenwich Standard Time) 2006-->
        <script language="JavaScript1.2" type="text/javascript">
            <!--
            function MM_findObj(n, d) { //v4.01
            var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
            d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
            if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
            for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
            if(!x && d.getElementById) x=d.getElementById(n); return x;
            }
            /* Functions that swaps down images. */
            function MM_nbGroup(event, grpName) { //v6.0
            var i,img,nbArr,args=MM_nbGroup.arguments;
            if (event == "init" && args.length > 2) {
            if ((img = MM_findObj(args[2])) != null && !img.MM_init) {
            img.MM_init = true; img.MM_up = args[3]; img.MM_dn = img.src;
            if ((nbArr = document[grpName]) == null) nbArr = document[grpName] = new Array();
            nbArr[nbArr.length] = img;
            for (i=4; i < args.length-1; i+=2) if ((img = MM_findObj(args[i])) != null) {
            if (!img.MM_up) img.MM_up = img.src;
            img.src = img.MM_dn = args[i+1];
            nbArr[nbArr.length] = img;
            } }
            } else if (event == "over") {
            document.MM_nbOver = nbArr = new Array();
            for (i=1; i < args.length-1; i+=3) if ((img = MM_findObj(args[i])) != null) {
            if (!img.MM_up) img.MM_up = img.src;
            img.src = (img.MM_dn && args[i+2]) ? args[i+2] : ((args[i+1])?args[i+1] : img.MM_up);
            nbArr[nbArr.length] = img;
            }
            } else if (event == "out" ) {
            for (i=0; i < document.MM_nbOver.length; i++) { img = document.MM_nbOver[i]; img.src = (img.MM_dn) ? img.MM_dn : img.MM_up; }
            } else if (event == "down") {
            nbArr = document[grpName];
            if (nbArr) for (i=0; i < nbArr.length; i++) { img=nbArr[i]; img.src = img.MM_up; img.MM_dn = 0; }
            document[grpName] = nbArr = new Array();
            for (i=2; i < args.length-1; i+=2) if ((img = MM_findObj(args[i])) != null) {
            if (!img.MM_up) img.MM_up = img.src;
            img.src = img.MM_dn = (args[i+1])? args[i+1] : img.MM_up;
            nbArr[nbArr.length] = img;
            } }
            }

            /* Functions that handle preload. */
            function MM_preloadImages() { //v3.0
            var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
            var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
            if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
            }

            //-->
        </script>
        <link rel="shortcut icon" href="../include/images/favicon.ico" >
        <link href="../include/css/new_doc.css" rel="stylesheet" type="text/css" />
        <style type="text/css">
            <!--
            .account_title {font-size: 14px
            font-family: Arial, Helvetica, sans-serif;
            font-size: 24px;
            line-height: 24px;
            margin-left: 0px;
            margin-right: 0px;
            margin-top: 0px;
            color: #FFFFFF;
            }
            .style1 {
            color: #FFFFFF;
            font-size: 10px;
            }
            .style2 {
            font-size: 12px;
            font-weight: bold;
            font-family: Verdana, Arial, Helvetica, sans-serif;
            }
.style3 {
	font-size: 24px;
	color: #FFFFFF;
}
            -->
        </style>
    </head>
    <body bgcolor="#ffffff" onload="MM_preloadImages('../include/images/nav_bar/navbar_r2_c4_f2.gif','../include/images/nav_bar/navbar_r2_c4_f4.gif','../include/images/nav_bar/navbar_r2_c4_f3.gif','../include/images/nav_bar/navbar_r2_c6_f2.gif','../include/images/nav_bar/navbar_r2_c6_f4.gif','../include/images/nav_bar/navbar_r2_c6_f3.gif','../include/images/nav_bar/navbar_r2_c8_f2.gif','../include/images/nav_bar/navbar_r2_c8_f4.gif','../include/images/nav_bar/navbar_r2_c8_f3.gif','../include/images/nav_bar/navbar_r2_c10_f2.gif','../include/images/nav_bar/navbar_r2_c10_f4.gif','../include/images/nav_bar/navbar_r2_c10_f3.gif','../include/images/nav_bar/navbar_r3_c2_f2.gif','../include/images/nav_bar/navbar_r3_c2_f4.gif','../include/images/nav_bar/navbar_r3_c2_f3.gif','../include/images/nav_bar/navbar_r3_c12_f2.gif','../include/images/nav_bar/navbar_r3_c12_f4.gif','../include/images/nav_bar/navbar_r3_c12_f3.gif','../include/images/nav_bar/navbar_r3_c14_f2.gif','../include/images/nav_bar/navbar_r3_c14_f4.gif','../include/images/nav_bar/navbar_r3_c14_f3.gif');MM_nbGroup('down','navbar1','navbar_r2_c4','../include/images/nav_bar/navbar_r2_c4_f3.gif',1)">
        <!--The following section is an HTML table which reassembles the sliced image in a browser.-->
        <!--Copy the table section including the opening and closing table tags, and paste the data where-->
        <!--you want the reassembled image to appear in the destination document. -->
        <!--======================== BEGIN COPYING THE HTML HERE ==========================-->
        <table width="100%" border="0" cellpadding="4" cellspacing="0" background="../include/images/Black bar.gif" bgcolor="#003300">
            <!--DWLayoutTable-->
            <!-- fwtable fwsrc="RM UI.png" fwbase="navbar.gif" fwstyle="Dreamweaver" fwdocid = "628690250" fwnested="0" -->
<tr bgcolor="#00000"><!-- row 2 -->
                <td height="85" ><span class="style3">  CikaNet Admin Manager</span></td>
    <td>&nbsp;</tD>
<td width="50" nowrap="nowrap" ><span class="style1"><span class="style2"></span><br/>
                <strong><a  href="../default.jsp?logout=1" target="_top" > Sign out </a> </strong></span></td>
    <td width="10"><!--DWLayoutEmptyCell-->&nbsp;</td>
<td height="85">
<td>
    <td width="40" valign="top" nowrap="nowrap"><a target="main" href="../account/welcome.jsp?listId=" onmouseout="MM_nbGroup('out');" onmouseover="MM_nbGroup('over','navbar_r2_c4','../include/images/nav_bar/navbar_r2_c4_f2.gif','../include/images/nav_bar/navbar_r2_c4_f4.gif',1)" onclick="MM_nbGroup('down','navbar1','navbar_r2_c4','../include/images/nav_bar/navbar_r2_c4_f3.gif',1)"></a></td>
    <td width="10"><!--DWLayoutEmptyCell-->&nbsp;</td>
<!--td width="66" valign="top" ><a href='../design/info.jsp?listId=' target="main" onmouseout="MM_nbGroup('out');" onmouseover="MM_nbGroup('over','navbar_r3_c2','include/images/nav_bar/navbar_r3_c2_f2.gif','include/images/nav_bar/navbar_r3_c2_f4.gif',1);" onclick="MM_nbGroup('down','navbar1','navbar_r3_c2','include/images/nav_bar/navbar_r3_c2_f3.gif',1);"><img name="navbar_r3_c2" src="include/images/nav_bar/navbar_r3_c2.gif" width="66" height="76" border="0" id="navbar_r3_c2" alt="" /></a></td>
                <td width="10"></td-->
                <td  valign="top" nowrap="nowrap"><a target="main" href="../services/services.jsp?listId=&amp;type=8" onmouseout="MM_nbGroup('out');" onmouseover="MM_nbGroup('over','navbar_r2_c6','../include/images/nav_bar/navbar_r2_c6_f2.gif','../include/images/nav_bar/navbar_r2_c6_f4.gif',1)" onclick="MM_nbGroup('down','navbar1','navbar_r2_c6','../include/images/nav_bar/navbar_r2_c6_f3.gif',1)"></a></td>
    <td width="10"><!--DWLayoutEmptyCell-->&nbsp;</td>
    <td width="30" valign="top"><a href="../content/content.jsp?type=0&amp;listId=" target="main" onmouseout="MM_nbGroup('out');" onmouseover="MM_nbGroup('over','navbar_r2_c8','../include/images/nav_bar/navbar_r2_c8_f2.gif','../include/images/nav_bar/navbar_r2_c8_f4.gif',1)" onclick="MM_nbGroup('down','navbar1','navbar_r2_c8','../include/images/nav_bar/navbar_r2_c8_f3.gif',1)"></a></td>
    <td width="10"><!--DWLayoutEmptyCell-->&nbsp;</td>
    <td width="100" valign="top"><span class="style1"><%= new java.util.Date()%></span></td>
    <td width="10"><!--DWLayoutEmptyCell-->&nbsp;</td>
<!--td width="61" valign="top"><a href="javascript:;" onmouseout="MM_nbGroup('out');" onmouseover="MM_nbGroup('over','navbar_r3_c14','include/images/nav_bar/navbar_r3_c14_f2.gif','include/images/nav_bar/navbar_r3_c14_f4.gif',1);" onclick="MM_nbGroup('down','navbar1','navbar_r3_c14','include/images/nav_bar/navbar_r3_c14_f3.gif',1);"><img name="navbar_r3_c14" src="include/images/nav_bar/navbar_r3_c14.gif" width="61" height="75" border="0" id="navbar_r3_c14" alt="" /></a></td-->
                <td width="61" valign="top"></td>
    <td ></td>
    <td ></td>
          </tr>
            <tr>
              <td height="1" colspan="17"  bgcolor="#FFFFFF" ><img src="" alt="" name="spacer" width="1" height="1" id="spacer" /></td>
            </tr>
            <tr>
              <td height="1" colspan="17"  bgcolor="#D6D6D6" ></td>
            </tr>
			
            <!--   This table was automatically created with Macromedia Fireworks   -->
            <!--   http://www.macromedia.com   -->
        </table>
    <!--========================= STOP COPYING THE HTML HERE =========================-->
    </body>
</html>
