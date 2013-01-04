<%@ page import="java.util.Iterator" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>


<% Integer Type =new Integer(0);
if(request.getParameter("type")!=null){
    Type = new Integer(request.getParameter("type"));
    } %>
    
<div id="jive-tabs">
		<ul><li class="">
		<a href="" title="Click to manage server settings" onMouseOver="self.status='Click to manage server settings';return true;" onMouseOut="self.status='';return true;">Server</a>
		</li><li  <%if(Type!=null && 0==Type.intValue()){%>class="currentlink" <%}else{%>class=""<%}%> >
		<a href="user-summary.jsp?type=0" title="Click to manage users " onMouseOver="self.status='Click to manage users and groups';return true;" onMouseOut="self.status='';return true;">Users</a>
		</li>
		<li <%if(Type!=null && 1==Type.intValue()){%>class="currentlink" <%}else{%>class=""<%}%>>
		<a href="merchant-summary.jsp?type=1" title="Click to manage connected merchants" onMouseOver="self.status='Click to manage connected networks';return true;" onMouseOut="self.status='';return true;">Merchants</a>
		</li><li <%if(Type!=null && 2==Type.intValue()){%>class="currentlink" <%}else{%>class=""<%}%>>
		<a href="network-summary.jsp?type=2" title="Click to manage network settings" onMouseOver="self.status='Click to manage network settings';return true;" onMouseOut="self.status='';return true;">Networks</a>
		</li><li <%if(Type!=null && 3==Type.intValue()){%>class="currentlink" <%}else{%>class=""<%}%>>
		<a href="bank-summary.jsp?type=3" title="Click to manage all plugins" onMouseOver="self.status='Click to manage all bankslugins';return true;" onMouseOut="self.status='';return true;">Banks</a>
		</li><li <%if(Type!=null && 4==Type.intValue()){%>class="currentlink" <%}else{%>class=""<%}%>>
		<a href="" title="CikaNET RTEMS Reports" onMouseOver="self.status='CikaNet RTEMS';return true;" onMouseOut="self.status='';return true;">Reports</a>
		</li></ul>
	</div>