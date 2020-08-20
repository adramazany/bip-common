1.struts-config.xml
 	<global-forwards>
		<forward name="download" path="/aipdownload.do"></forward>
 	</global-forwards>
2.struts-config.xml
	<action  path="/aipdownload" type="aip.util.export.AIPDownloadAction"/>
3.action class 
		if("exportexcel".equalsIgnoreCase(reqCode)){
			return exportexcel(mapping, form, request, response,lst,param);
		}
4.action class 
	private ActionForward exportexcel(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response, AmarLST lst, AmarLSTParam param) {
		try{
			//application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
			request.setAttribute("contentType", "application/vnd.ms-excel");
			
			AIPExportExcel exportExcel = new AIPExportExcel();
			/*
			 * paramString
			 */
			String organization = IribAuditUtil.getOrganizationCaptionByHierarchy(param.getOrganizationhierarchy());
			String paramString[]={"واحد سازمانی : "+organization
					,"از تاریخ : "+param.getDateFrom()
					,"تاریخ گزارش: "+DateConvert.getTodayJalali()+" "+DateConvert.getTime()
					,"تا تاریخ : "+param.getDateTo()
					};
			
			/*
			 * columnHeader
			 */
			AIPExportHeader columnHeader = new AIPExportHeader();
			columnHeader.setHeadValue("",0,1,0,1);
			for (int i=0;i<lst.getColumnMembers().size();i++) {
				AIPIdNameValue column = lst.getColumnMembers().get(i);
				columnHeader.setHeadValue(column.getValue(),0,1,i+1,1);
			}

			/*
			 * cellSet
			 */
			Object[][] cellSet=new Object[lst.getRowMembers().size()][lst.getColumnMembers().size()+1];
			for(int i=0;i<lst.getRowMembers().size();i++){
				cellSet[i][0]=lst.getRowMembers().get(i).getValue();
				for (int j = 0; j < lst.getColumnMembers().size(); j++) {
					String key=lst.getColumnMembers().get(j).getName()+"."+lst.getRowMembers().get(i).getName();
					cellSet[i][j+1]=lst.getDataMembers().get(key);
				}
			}
			/*
			 * export
			 */
			InputStream inputStream = exportExcel.export(cellSet, lst.getReporttitle(), paramString, columnHeader); 
			request.setAttribute("inputStream", inputStream);
		}catch (Exception e) {
			e.printStackTrace();
			AIPErrorHandler.setAttributeErrorMessage(request, e, null);
		}
		return mapping.findForward("download");
	}
5.param class
	public String getExportExcelHyperlink(){
		return "amar.do?reqCode=exportexcel"
				+ "&reportname="+reportname
				+ "&dateFrom="+dateFrom
				+ "&dateTo="+dateTo
				+ "&hasProcess="+hasProcess
				+ "&organizationhierarchy="+organizationhierarchy
				;
	}
6.jsp file
	<a target="_blank" href='<bean:write name="param" property="exportExcelHyperlink"/>' title="ذخیره اکسل"><img src="./aip/toolbar/export-excel.png" alt="ذخیره اکسل" width="24" height="24" /></a>
	
