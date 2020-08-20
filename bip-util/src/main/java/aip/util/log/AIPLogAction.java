package aip.util.log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import aip.util.NVL;
import aip.util.doc.AIPDocManager;


public class AIPLogAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) {

		ActionForward af = null;
		String reqCode = request.getParameter(mapping.getParameter());	
		AIPLogParam param = getParam(request);
		
		if (reqCode == null || "".equalsIgnoreCase(reqCode) || "view".equalsIgnoreCase(reqCode)) {
			af = mapping.findForward("view");
		}else if("ShowParamDialog".equalsIgnoreCase(reqCode)){
			getParam(request);
//			try {
				af = mapping.findForward("ShowParamDialog");
				return af;
//			} catch (AIPException e) {
//				e.printStackTrace();
//				AIPErrorHandler.setAttributeErrorMessage(request, e, null);
//			}
		}
		

		AIPLogLST lst =null;
		try{
			lst = AIPLog.logList(param);//generateBankTranByAccountLST();//
			request.setAttribute("list", lst);
		}catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", e);
		}

		/*
		 * Print
		 */
		String title="رخدادهای سیستم";
		AIPDocManager.printOrSaveDocument(request, response, reqCode,param.getPrintParam(), lst, title);
		
		return af;
	}
	
	private AIPLogParam getParam(HttpServletRequest request) {
		AIPLogParam param = new AIPLogParam();
		
		param.fillParameters(request);
		
	    param.setFromDate(NVL.getString(request.getParameter("fromDate")));
	    param.setToDate(NVL.getString(request.getParameter("toDate")));
	    param.setFromHour(NVL.getString(request.getParameter("fromHour")));
	    param.setToHour(NVL.getString(request.getParameter("toHour")));
	    param.setUsername(NVL.getString(request.getParameter("username")));
	    param.setUc_name(NVL.getString(request.getParameter("uc_name")));
	    param.setUc_level(NVL.getString(request.getParameter("uc_level")));
	    param.setUc_no(NVL.getString(request.getParameter("uc_no")));
	    param.setUc_op(NVL.getString(request.getParameter("uc_op")));
	    param.setUc_desc(NVL.getString(request.getParameter("uc_desc")));

		request.setAttribute("param", param);
		return param;
	}
	

	 
}