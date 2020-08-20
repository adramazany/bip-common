<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<div class="clsReportParam">
<table width="100%" >
	<tr><td class="viewTitles">از تاریخ:</td>
		<td class="viewData" nowrap="nowrap">
			<html:text name="param" property="fromDate" styleClass="clsDate" styleId="logFromDate"></html:text>
			<img src="images/buttons/calendar.gif" alt="calendar" onclick="displayDatePicker('logFromDate',this)" style="cursor: pointer;" />
			<html:text name="param" property="fromHour" styleClass="clsTime"></html:text>
		</td><td class="viewTitles">تا تاریخ:</td>	
		<td class="viewData" nowrap="nowrap">
			<html:text name="param" property="toDate" styleId="logToDate" styleClass="clsDate"></html:text>
			<img  src="images/buttons/calendar.gif"  alt="calendar" onclick="displayDatePicker('logToDate',this)" style="cursor: pointer;" />
			<html:text name="param" property="toHour" styleClass="clsTime"></html:text>
		</td>
	</tr>
	<tr><td colspan="8"><hr></td></tr>
	<tr><td class="viewTitles">کاربر</td>
		<td class="viewData">
			<html:text name="param" property="username"></html:text>
		</td>
		<td class="viewTitles">موضوع</td>
		<td class="viewData">
			<html:text name="param" property="uc_name"></html:text>
		</td>
	</tr>
	<tr><td class="viewTitles">سطح</td>
		<td class="viewData">
			<html:text name="param" property="uc_level"></html:text>
		</td>
		<td class="viewTitles">شماره</td>
		<td class="viewData">
			<html:text name="param" property="uc_no"></html:text>
		</td>
	</tr>
	<tr><td class="viewTitles">متد</td>
		<td class="viewData">
			<html:text name="param" property="uc_op"></html:text>
		</td>
		<td class="viewTitles">شرح</td>
		<td class="viewData">
			<html:text name="param" property="uc_desc"></html:text>
		</td>
	</tr>
</table>
</div>