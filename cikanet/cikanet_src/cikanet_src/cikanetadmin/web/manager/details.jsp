
<html>
    <head>
        <title>www.rancardmobility.com</title>
        <link rel="shortcut icon" href="include/images/favicon.ico" >
    </head>
    <%if("true".equals(request.getParameter("refreshlist"))){%>
    <script>
        parent.ViewsFrame.location.reload();
    </script>
    <%}%>
    <frameset rows="77,*" cols="*" framespacing="0" frameborder="no" border="0" bordercolor="#FFFFFF" >
        <frame name="header" scrolling="no" noresize target="main" src="navbar.jsp?name=Charles">
        <frame name="main" src="../_user_index.jsp?name=" marginwidth="0" marginheight="0" scrolling="auto" noresize>
        <frame src="blank.htm" />

    </frameset><noframes></noframes>

</html>
