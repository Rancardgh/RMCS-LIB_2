<%@ page import="java.util.Iterator" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>


<% Integer Type =new Integer(0);
if(request.getParameter("type")!=null){
    Type = new Integer(request.getParameter("type"));
    } %>
<link href="include/css/new_doc.css" rel="stylesheet" type="text/css" />
<table width="100%" cellpadding="0" cellspacing="0"  bgcolor="#D6D6D6" >
    <tr>
        <td height="32"  valign="bottom" ><table width="100%"  cellpadding="0" cellspacing="0" >
                <tr >
                    <td width="300" height="22"><div class="subtitle" style="margin-left: 20px;height=25px" >  </div></td>
                    
                    <td width="100" valign="bottom" height="22" <%if(Type!=null && 0==Type.intValue()){%>id="navigation" <%} %>>
                    <a href="_user_index.jsp?id=<%=request.getParameter("id")%>&amp;type=0" class="tabs"  ><span>Users</span></a></td>
                    <td width="10" height="22"><img src="include/images/spacer.gif" alt="spacer" width="10" height="22" /></td>
                    <td width="100" valign="bottom" height="22" <%if(Type!=null && 2==Type.intValue()){%>id="navigation" <%} %>>
                    <a href="_network_index.jsp?id=<%=request.getParameter("id")%>&amp;type=2" class="tabs"  ><span>Networks</span></a></td>
                    <td width="10" height="22"><img src="include/images/spacer.gif" alt="spacer" width="10" height="22" /></td>
                     <td WIDTH=100 valign="bottom"  nowrap <%if(Type!=null && 1==Type.intValue()){%>id="navigation" <%} %> ><a class="tabs" href="_merchant_index.jsp?id=<%=request.getParameter("id")%>&amp;type=1&amp;viewsummary=true" ><span>Merchants</span></a></td>
                    <td width="10" height="22"><img src="include/images/spacer.gif" alt="spacer" width="10" height="22" /></td>
                    <td WIDTH=100 valign="bottom"  nowrap <%if(Type!=null && 3==Type.intValue()){%>id="navigation" <%} %> ><a class="tabs" href="_bank_index.jsp?id=<%=request.getParameter("id")%>&amp;type=3&amp;viewsummary=true" ><span>Banks</span></a></td>
                    <td width="10" height="22"><img src="include/images/spacer.gif" alt="spacer" width="10" height="22" /></td>    
                    <td width="150"   height="22"><img src="include/images/spacer.gif" alt="spacer" width="10" height="22" /></td>
                </tr>
            </table>
        </td>
    </tr>
</table>