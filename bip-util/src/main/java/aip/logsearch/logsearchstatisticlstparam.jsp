<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<head>
<script type="text/javascript" src="./js/common/JsFarsiCalendar.js"></script>
</head>
<div class="clsReportParam">
<table width="100%">
	
	<tr>
		<td class="viewTitles">از تاریخ:</td>
		<td class="viewData">
			<html:text name="param" property="logdatefrom" styleId="param_fromdate" styleClass="clsDate"></html:text>						
			<img  src="./images/calendar/calendar.gif"  alt="calendar" onclick="displayDatePicker('param_fromdate',this)" style="cursor: pointer;" />
		</td>
		<td class="viewTitles">تا تاریخ:</td>
		<td class="viewData">
			<html:text name="param" property="logdateto" styleId="param_todate" styleClass="clsDate"></html:text>						
			<img  src="./images/calendar/calendar.gif"  alt="calendar" onclick="displayDatePicker('param_todate',this)" style="cursor: pointer;" />
		</td>
	</tr>
	<tr>
		<td class="viewTitles">کلمه:</td>
		<td class="viewData">
			<html:text name="param" property="filter" ></html:text>						
		</td>
		<td class="viewTitles">کاربر:</td>
		<td class="viewData">
			<html:text name="param" property="username"></html:text>						
		</td>
	</tr>
	
</table>
</div>
