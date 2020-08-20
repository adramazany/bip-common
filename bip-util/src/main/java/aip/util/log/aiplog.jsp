<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic-el" prefix="logic"%>
<%@ taglib uri="/WEB-INF/AIPTag.tld" prefix="aip"%>
<%@page import="aip.util.NVL"%>
<head>
	<html:base />
	<title>رخدادهای سیستم</title>
</head>
<body id="baseBody" dir="rtl">
	<jsp:include page="/aipconfig/toolbar/aiptoolbar.jsp"></jsp:include>
	
	<aip:headerTitle title="رخدادهای سیتم"  />

	<form action="aiplog.do" method="POST" class="clsReportForm">
		<input type="hidden" name="paramAction" value="aiplog.do">
		
		<html:hidden name="list"  property="requestPage" />
		<html:hidden name="list"  property="pageSize" />
		<html:hidden name="param" property="fromDate" />
		<html:hidden name="param" property="fromHour" />
		<html:hidden name="param" property="toDate" />
		<html:hidden name="param" property="toHour" />
		<html:hidden name="param" property="username"/>
		<html:hidden name="param" property="uc_name"/>
		<html:hidden name="param" property="uc_level"/>
		<html:hidden name="param" property="uc_no"/>
		<html:hidden name="param" property="uc_op"/>
		<html:hidden name="param" property="uc_desc"/>
	</form>

	<aip:skin type="ITEM">
		<logic:notEmpty name="list">
			<div id="divParamString">
				<bean:write name="list" property="paramString"/>	
			</div>
		</logic:notEmpty>
		<div id="baseDiv" align="center">

			<table width="100%" Class="dataList clsVisibleColumns" >
				<tr>
					<th id="radif">ردیف</th>
					<th id="date">تاریخ</th>
					<th id="time">ساعت</th>
					<th id="username">کاربر</th>
					<th id="remoteip">آدرس اینترنتی</th>
					<th id="uc_name">موضوع</th>
					<th id="uc_level">سطح</th>
					<th id="uc_id">شناسه</th>
					<th id="uc_no">شماره</th>
					<th id="uc_op">متد</th>
					<th id="uc_desc">شرح</th>
				</tr>
				
				<logic:notEmpty name="list">

				<bean:define id="requestPage" name="list" property="requestPage"></bean:define>
				<bean:define id="pageSize" name="list" property="pageSize"></bean:define>
				
				<logic:iterate id="dto" name="list" property="dtos" type="aip.util.log.AIPLogENT" indexId="index">
					<% if(dto!=null){ %>
					<tr>
						<td class="radif"><%=((NVL.getInt(requestPage)-1)*NVL.getInt(pageSize))+index+1 %></td>
						<td class="date"><%=NVL.getString(dto.getDate()) %></td>
						<td class="time"><%=NVL.getString(dto.getTime()) %></td>
						<td class="username"><%=NVL.getString(dto.getUsername()) %></td>
						<td class="remoteip"><%=NVL.getString(dto.getRemoteip()) %></td>
						<td class="uc_name"><%=NVL.getString(dto.getUc_name()) %></td>
						<td class="uc_level"><%=NVL.getString(dto.getUc_level()) %></td>
						<td class="uc_id"><%=NVL.getString(dto.getUc_id()) %></td>
						<td class="uc_no"><%=NVL.getString(dto.getUc_no()) %></td>
						<td class="uc_op"><%=NVL.getString(dto.getUc_op()) %></td>
						<td class="uc_desc"><%=NVL.getString(dto.getUc_desc()) %></td>
					</tr>
					<% }//if(dto!=null) %>
				</logic:iterate>
				</logic:notEmpty>
			</table>

<!--			<jsp:include page="/form/common/reportSummary.jsp"></jsp:include>-->

		</div>
	</aip:skin>
</body>
