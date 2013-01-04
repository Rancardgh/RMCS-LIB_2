<%@ taglib prefix="a" uri="http://jmaki/v1.0/jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
    <head>
        <link rel="stylesheet" href="jmaki-standard-footer.css" type="text/css"></link>
        <title>Page Title</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    </head>
    <body>
        <div class="outerBorder">
            
            <div class="header">
                <div class="banner">CikaNet Manager</div>
                
                <div class="subheader">
                    
                    <div>
                        <a href="mailto:feedback@youraddress">Feedback</a> |
                        <a href="#">Site Map</a> |
                        <a href="#">Home</a>
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
                    
                </div> <!-- leftSidebar -->

                <div class="content" style="height:400px">
                    <a:widget name="Ext.menu"
                              value="{menu:  [
                              {label: 'Images',
                              menu : [
                              {label:'Birds', url:'jsonibrowse.jsp'},
                              {label:'Cat', url:'ibrowse.jsp'}
                              ]},
                              {label: 'Bookmarks',
                              menu : [
                              {label:'Digg', url:'digg.jsp'},
                              {label:'Delicious', url:'delicious.jsp'}
                              ]}                
                              ]}" />
                    <a:widget name="dojo.dropdowndatepicker" />
                    
                    Main Content Area
                    <a:widget name="dojo.editor" value="Edit Me" />
                       <a:widget name="yahoo.tabbedview"
                          value="{tabs:[
                          {label:'My Tab', content: 'Some Content'},
                          {label:'My Tab 2', content: 'Tab 2 Content'}
                          ]
                          }" /> 
                </div> <!-- content -->
            
                
            </div> <!-- main -->

            <div class="footer">Footer</div>  
            
        </div> <!-- outerborder -->

    </body>
</html>