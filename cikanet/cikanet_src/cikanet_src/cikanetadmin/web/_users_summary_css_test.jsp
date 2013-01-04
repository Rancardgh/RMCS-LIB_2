<%@ taglib prefix="a" uri="http://jmaki/v1.0/jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
    <head>
        <link rel="stylesheet" href="jmaki-standard-footer.css" type="text/css"></link>
        <title>Page Title</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>
    <script src="SpryAssets/SpryCollapsiblePanel.js" type="text/javascript"></script>
    <link href="SpryAssets/SpryCollapsiblePanel.css" rel="stylesheet" type="text/css" />

                </head>
    <body>
        <div class="outerBorder">
            
            <div class="header">
                <div class="banner">CikaNet Admin Console</div>
                
                <div class="subheader">
                    
                    <div>
                      <table border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="20">&nbsp;</td>
                            <td width="100" align="center" bgcolor="#999999">Lorem</td>
                            <td width="20">&nbsp;</td>
                            <td align="center" width="100"><a href="#">Ipsum</a></td>
                            <td width="20">&nbsp;</td>
                            <td align="center" width="100"><a href="#">Dolar</a></td>
                            <td width="20">&nbsp;</td>
                            <td align="center" width="100"><a href="#">Amit</a></td>
                            <td width="20">&nbsp;</td>
                          </tr>
                          <tr>
                            <td height="1" colspan="10" bgcolor="#CCCCCC"><table height="1" width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                  <td></td>
                                </tr>
                            </table></td>
                          </tr>
                        </table>
</div>
                    
              </div> <!-- sub-header -->
            </div> <!-- header -->

            <div class="main">
                <div class="leftSidebar">
                    
                    Sidebar Content Here
                    <a:widget name="Ext.tree"
                              args="{topic : '/jmaki/menu'}"
                              value="{
                              root : {
                              title : 'Ext Tree Root Node',
                              expanded : true,
                              children : [
                              { title : 'Node 1.1'},
                              { title : 'Node 1.2',
                              children : [
                              { title : 'Node 3.1',
                              onclick : {url:'foo'}}
                              ]
                              }
                              ]
                              }
                              }"/>
                    <div id="CollapsiblePanel1" class="CollapsiblePanel">
                      <div class="CollapsiblePanelTab" tabindex="0">My Contents</div>
                      <div class="CollapsiblePanelContent">
                        <p>item 1</p>
                        <p>item 2</p>
                        <p>item 3</p>
                        <p>&nbsp;</p>
                      </div>
                    </div>
                </div> 
                <!-- leftSidebar -->

              <div class="content" style="height:400px; background-color: #ffffff; padding-left: 25px; padding-top: 25px; padding-right: 25px;">
       
                    
                    Main Content Area
      
                       <div id="CollapsiblePanel2" class="CollapsiblePanel">
                         <div class="CollapsiblePanelTab" tabindex="0">Bank Details</div>
                         <div class="CollapsiblePanelContent">
                           <p>Content</p>
                           <p>heter</p>
                           <p>some form details</p>
                           <p>wetere</p>
                         </div>
                       </div>
                       <p>&nbsp;</p>
                       <div id="CollapsiblePanel3" class="CollapsiblePanel">
                         <div class="CollapsiblePanelTab" tabindex="0">Account information</div>
                         <div class="CollapsiblePanelContent">
                           <p>Content</p>
                           <p>some other stuff</p>
                         </div>
                       </div>
                       <p>&nbsp;</p>
                       <div id="CollapsiblePanel4" class="CollapsiblePanel">
                         <div class="CollapsiblePanelTab" tabindex="0">Financial details</div>
                         <div class="CollapsiblePanelContent">
                           <p>Hey there</p>
                           <p>What's going on</p>
                         </div>
                       </div>
                       <p>&nbsp;</p>
              </div> 
                <!-- content -->
            
                
</div> <!-- main -->

            <div class="footer">Footer</div>  
            
        </div> <!-- outerborder -->

        <script type="text/javascript">
<!--
var CollapsiblePanel1 = new Spry.Widget.CollapsiblePanel("CollapsiblePanel1");
var CollapsiblePanel2 = new Spry.Widget.CollapsiblePanel("CollapsiblePanel2");
var CollapsiblePanel3 = new Spry.Widget.CollapsiblePanel("CollapsiblePanel3", {contentIsOpen:false});
var CollapsiblePanel4 = new Spry.Widget.CollapsiblePanel("CollapsiblePanel4", {contentIsOpen:false});
//-->
</script>
</body>
</html>