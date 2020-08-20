package aip.logsearch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import aip.util.AIPErrorHandler;
import aip.util.AIPException;
import aip.util.NVL;
import aip.util.doc.AIPDocManager;

public class AIPLogSearchAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) {

		ActionForward af = null;
		String reqCode = request.getParameter(mapping.getParameter());	
		LogSearchLSTParam param = getParam(request);
		
		if (reqCode == null || "".equalsIgnoreCase(reqCode) || "view".equalsIgnoreCase(reqCode)) {
			af = mapping.findForward("view");
		}else if("ShowParamDialog".equalsIgnoreCase(reqCode)){
			getParam(request);
//			LogSearchComboLST comboLST = new LogSearchComboLST(); 
//			try {
//				comboLST = (new LogSearchDAO()).getLogSearchComboLST();
//			} catch (AIPException e) {
//				e.printStackTrace();
//				AIPErrorHandler.setAttributeErrorMessage(request, e, null);
//			}
//			request.setAttribute("comboLST", comboLST);
			af = mapping.findForward("ShowParamDialog");
			return af;
		}else if("your_code".equalsIgnoreCase(reqCode)){
		}
		
		LogSearchLST lst =new LogSearchLST();
		try{
			lst = (new AIPLogSearchDAO()).getLogSearchLST(param);
		}catch (Exception e) {
			e.printStackTrace();
			AIPErrorHandler.setAttributeErrorMessage(request, e, null);
		}
		request.setAttribute("list", lst);
		
		/*
		 * Print
		 */
		String title="Log Search";
		AIPDocManager.printOrSaveDocument(request, response, reqCode,param.getPrintParam(), lst, title);
		
		return af;
		
		
	}
	
	private LogSearchLSTParam getParam(HttpServletRequest request) {
		LogSearchLSTParam param = new LogSearchLSTParam();
		
		param.fillParameters(request);
		
		param.setLogdatefrom(NVL.getString(request.getParameter("logdatefrom")));
		param.setLogdateto(NVL.getString(request.getParameter("logdateto")));
		param.setFilter(NVL.getString(request.getParameter("filter")));
		param.setUsername(NVL.getString(request.getParameter("username")));
		

		request.setAttribute("param", param);
		return param;
	}

}
