<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="aip.util.NVL"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/AIPTag.tld" prefix="aip"%>
<head>
	<title>گزارش آماری جستجو</title>
</head>
<body id="baseBody" dir="rtl" >

	<div class="clsTitleBar">
		گزارش آماری جستجو
	</div>

	<jsp:include page="/aip/toolbar/aiptoolbar.jsp"></jsp:include>

	<logic:notEmpty name="errorMessage">
	<div class="clsErrorMessage">
			<%= NVL.getString(request.getAttribute("errorMessage")) %>
	</div>
	</logic:notEmpty>

	<form action="t_searchstat.do" method="POST" class="clsReportForm">
		<input type="hidden" name="paramAction" value="searchstat.do">
		
		<html:hidden name="list"  property="requestPage" />
		<html:hidden name="list"  property="pageSize" />
		<html:hidden name="param" property="logdatefrom"/>
		<html:hidden name="param" property="logdateto"/>
		<html:hidden name="param" property="filter"/>
		<html:hidden name="param" property="username"/>
		
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
										<th id="word">کلمه</th>
										<th id="count">مجموع</th>
					
				</tr>
				
				<logic:notEmpty name="list">

				<bean:define id="requestPage" name="list" property="requestPage"></bean:define>
				<bean:define id="pageSize" name="list" property="pageSize"></bean:define>
				
				<logic:iterate id="dto" name="list" property="rows" type="aip.logsearch.LogSearchStatisticDTO" indexId="index">
					<% if(dto!=null){ %>
					<tr class="<%= (index%2==0?"oddRows":"evenRows")  %>">
						<td class="radif"><%=((NVL.getInt(requestPage)-1)*NVL.getInt(pageSize))+index+1 %></td>
												<td class="word" align="center"><%=NVL.getString(dto.getWord()) %></td>
												<td class="wcount" align="center"><%=NVL.getString(dto.getWcount()) %></td>
						
					</tr>
					<% }//if(dto!=null) %>
				</logic:iterate>
				</logic:notEmpty>
			</table>
		</div>
	</aip:skin>
</body>
