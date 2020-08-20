<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="aip.util.NVL"%>
<%@page import="aip.logsearch.LogSearchLST" %>
<%@page import="aip.logsearch.LogSearchLSTParam"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/AIPTag.tld" prefix="aip"%>
<head>
	<title>Log Search</title>
</head>
<body id="baseBody" dir="rtl" >

	<div class="clsTitleBar">
		گزارش جستجوها
	</div>

	<jsp:include page="/aip/toolbar/aiptoolbar.jsp"></jsp:include>
	
<logic:notEmpty name="errorMessage">
	<div class="clsErrorMessage">
			<%= NVL.getString(request.getAttribute("errorMessage")) %>
		</div>
	</logic:notEmpty>

	<form action="t_logsearch.do" method="POST" class="clsReportForm">
		<input type="hidden" name="paramAction" value="logsearch.do">
		
		<html:hidden name="list"  property="requestPage" />
		<html:hidden name="list"  property="pageSize" />
		
	</form>

	<aip:skin type="ITEM"><div class="clsTitleBar2">${reporttoolbarparam.title}</div>
		<logic:notEmpty name="list">
			<div id="divParamString">
				<bean:write name="list" property="paramString"/>	
			</div>
		</logic:notEmpty>
		<div id="baseDiv" align="center">
			<table width="100%" Class="dataList clsVisibleColumns">
				<tr>
					<th id="radif"></th>
					<th id="sentence">عبارت</th>
					<th id="logdate">تاریخ</th>
					<th id="time">ساعت</th>
					<th id="remotehost">هاست</th>
					<th id="remoteip">IP</th>
					<th id="username">کاربر</th>
					<th id="remoteport">‍‍پورت</th>
					<th id="authtype">امنیت</th>
					<th id="sessionid">شناسه</th>
					<th id="userprincipal">نوع کاربر</th>
				</tr>
				
				<logic:notEmpty name="list">

				<bean:define id="requestPage" name="list" property="requestPage"></bean:define>
				<bean:define id="pageSize" name="list" property="pageSize"></bean:define>
				
			<logic:iterate id="dto" name="list" property="rows" type="aip.logsearch.LogSearchDTO" indexId="index">
				<% if(dto!=null){ %>
				<tr class="<%= (index%2==0?"oddRows":"evenRows")  %>">
					<td class="radif"><%=((NVL.getInt(requestPage)-1)*NVL.getInt(pageSize))+index+1 %></td>
					<td class="sentence" align="center"><%=NVL.getString(dto.getSentence()) %></td>
					<td class="logdate" align="center"><%=NVL.getString(dto.getLogdate()) %></td>
					<td class="time" align="center"><%=NVL.getString(dto.getTime()) %></td>
					<td class="remotehost" align="center"><%=NVL.getString(dto.getRemotehost()) %></td>
					<td class="remoteip" align="center"><%=NVL.getString(dto.getRemoteip()) %></td>
					<td class="username" align="center"><%=NVL.getString(dto.getUsername()) %></td>
					<td class="remoteport" align="center"><%=NVL.getString(dto.getRemoteport()) %></td>
					<td class="authtype" align="center"><%=NVL.getString(dto.getAuthtype()) %></td>
					<td class="sessionid" align="center"><%=NVL.getString(dto.getSessionid()) %></td>
					<td class="userprincipal" align="center"><%=NVL.getString(dto.getUserprincipal()) %></td>
				</tr>
				<% }//if(dto!=null) %>
			</logic:iterate>
			</logic:notEmpty>
			</table>
		</div>
	</aip:skin>
</body>
