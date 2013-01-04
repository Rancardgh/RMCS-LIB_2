<%
if(session.getAttribute("user")==null){
response.sendRedirect("default.jsp");

}%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Frameset//EN">
<html>

<head>
<title>Rancard Mobility Server</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<noscript>
<meta http-equiv="REFRESH" content="0;URL=index.jsp?noscript=1">
</noscript>
<script language="JavaScript">
function liveActionInternal(topHelpWindow, pluginId, className, argument)
{


}
function showTopicInContentsInternal(topHelpWindow, topic) {
	try{
		topHelpWindow.DetailsFrame.NavFrame.displayTocFor(topic);
	}catch(e){
	}
}

</script>
<style type="text/css">FRAMESET {
	BORDER-RIGHT: 0px; BORDER-TOP: 0px; BORDER-LEFT: 1px; BORDER-BOTTOM: 1px
}
</style>
<script language="JavaScript">

function onloadHandler(e)
{
	var h=window.SearchFrame.document.getElementById("searchLabel").offsetHeight;
	if(h<=19){
		return;
	}
	document.getElementById("indexFrameset").setAttribute("rows", "44,"+(11+h)+",*");


	window.frames["SearchFrame"].document.getElementById("searchWord").focus();
}

</script>
<meta content="MSHTML 6.00.2900.2722" name="GENERATOR">
</head>

<frameset id="indexFrameset" border="0" framespacing="0" rows="71,30,*,30" frameborder="0" spacing="0">
	<frame title="Banner" tabindex="3" name="BannerFrame" marginwidth="0" marginheight="0" src="banner_new.jsp" frameborder="0" noresize scrolling="no" target="_self">
	<frame title="Server Toolbar" name="SearchFrame" marginwidth="0" marginheight="0" src="blank.htm" frameborder="0" noresize scrolling="no" target="proArea">
	<frameset id="helpFrameset" style="BORDER-RIGHT: #f1f1f1 4px solid; BORDER-TOP:  #f1f1f1 0px solid; BORDER-LEFT:  #f1f1f1 4px solid; BORDER-BOTTOM:  #f1f1f1 4px solid" border="0" framespacing="0" frameborder="1" cols="200" scrolling="no">
		<frame class="content" title="Layout frame: proArea" name="proArea" marginwidth="0" marginheight="0" src="monitor/details.jsp" frameborder="0" scrolling="yes" resize="yes" target="proArea">
	</frameset>
	<frame src="footer.jsp" >

</frameset>

</html>
