package aip.logsearch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import aip.util.AIPErrorHandler;
import aip.util.NVL;
import aip.util.doc.AIPDocManager;

public class AIPLogSearchStatisticAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) {

		ActionForward af = null;
		String reqCode = request.getParameter(mapping.getParameter());	
		LogSearchStatisticLSTParam param = getParam(request);
		
		if (reqCode == null || "".equalsIgnoreCase(reqCode) || "view".equalsIgnoreCase(reqCode)) {
			af = mapping.findForward("view");
		}else if("ShowParamDialog".equalsIgnoreCase(reqCode)){
			getParam(request);
			af = mapping.findForward("ShowParamDialog");
			return af;
		}
		LogSearchStatisticLST lst =new LogSearchStatisticLST();
		try{
			lst = (new AIPLogSearchDAO()).getLogSearchStatisticLST(param);
		}catch (Exception e) {
			e.printStackTrace();
			AIPErrorHandler.setAttributeErrorMessage(request, e, null);
		}
		request.setAttribute("list", lst);
		
		/*
		 * Print
		 */
		String title="گزارش آماری جستجو";
		AIPDocManager.printOrSaveDocument(request, response, reqCode,param.getPrintParam(), lst, title);
		
		return af;
		
		
	}
	
	private LogSearchStatisticLSTParam getParam(HttpServletRequest request) {
		LogSearchStatisticLSTParam param = new LogSearchStatisticLSTParam();
		
		param.fillParameters(request);
		
		param.setLogdatefrom(NVL.getString(request.getParameter("logdatefrom")));
		param.setLogdateto(NVL.getString(request.getParameter("logdateto")));
		param.setFilter(NVL.getString(request.getParameter("filter")));
		param.setUsername(NVL.getString(request.getParameter("username")));
	    

		request.setAttribute("param", param);
		return param;
	}

}
