<%@ page import="java.util.Iterator" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>


<% Integer Type =new Integer(0);
if(request.getParameter("useraction")!=null){
    Type = new Integer(request.getParameter("useraction"));
} %>




<ul><li class="category">Users</li>
      <li <%if(Type!=null && 0==Type.intValue()){%>class="currentlink" <%}else{%>class=""<%}%>>
        <a href="user-summary.jsp?type=0" title="Click to see a list of users in the system"
           onmouseover="self.status='Click to see a list of users in the system';return true;" onMouseOut="self.status='';return true;">User Summary</a>
</li>
<!-- sub items menu  -->
<%if(Type!=null && (1==Type.intValue() || 2== Type.intValue() || 3 == Type.intValue())){ %>
    <ul class="subitems">
        <li class="category">User Details</li><li <%if(Type!=null && 1==Type.intValue()){%>class="currentlink" <%}else{%>class=""<%}%>>
            
            <a href="user-details.jsp?action=view&useraction=1&id=<%=request.getParameter("id")%>" title="Click to edit the user\'s properties"
               onmouseover="self.status='Click to edit the user\'s properties';return true;" onmouseout="self.status='';return true;"
            >Contact Information</a>
        </li>
        <li <%if(Type!=null && 2==Type.intValue()){%>class="currentlink" <%}else{%>class=""<%}%>>
            <a href="user-details.jsp?action=view&useraction=2&id=<%=request.getParameter("id")%>" title="Click to change the user\'s password"
               onmouseover="self.status='Click to change the user\'s password';return true;" onmouseout="self.status='';return true;"
            >Transaction History</a>
        </li>
        <li <%if(Type!=null && 3==Type.intValue()){%>class="currentlink" <%}else{%>class=""<%}%>>
            <a href="user-details.jsp?action=view&useraction=3&id=<%=request.getParameter("id")%>" title="Click to delete the user"
               onmouseover="self.status='Click to delete the user';return true;" onmouseout="self.status='';return true;"
            >Manage Balance</a>
        </li>
        
    <br></ul>
    
    <%}%>
    <li <%if(Type!=null && 4==Type.intValue()){%>class="currentlink" <%}else{%>class=""<%}%>>
        <a href="new_user.jsp?action=view&useraction=4&id=<%=request.getParameter("id")%>" title="Click to add a new user to the system"
           onmouseover="self.status='Click to add a new user to the system';return true;" onMouseOut="self.status='';return true;"        >Create New User</a>
            </li>
    <li class="">
        <a href="user-summary.jsp?type=0" title="Click to search for a particular user"
           onmouseover="self.status='Click to search for a particular user';return true;" onMouseOut="self.status='';return true;"
        >User Search</a>
        
    </li><li class="">
        <a href="user-summary.jsp?type=0" title="Advanced user search"
           onmouseover="self.status='Advanced user search';return true;" onMouseOut="self.status='';return true;"
        >Advanced User Search</a>
        
    </li>
    <!--
    <li class="category">Groups</li><li class="">
        <a href="/group-summary.jsp" title="Click to see a list of groups in the system"
           onmouseover="self.status='Click to see a list of groups in the system';return true;" onMouseOut="self.status='';return true;"
        >Group Summary</a>
        
    </li><li class="">
        <a href="/group-create.jsp" title="Click to add a new group to the system"
           onmouseover="self.status='Click to add a new group to the system';return true;" onMouseOut="self.status='';return true;"
        >Create New Group</a>
        
</li>
-->
</ul>
<br>
<img src="user-summary.jsp_files/blank.gif" width="150" height="1" border="0" alt="">