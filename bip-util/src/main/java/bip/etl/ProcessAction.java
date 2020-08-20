package bip.etl;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import aip.util.AIPConfiguration;
import aip.util.AIPUtil;
import aip.util.AIPWebUserParam;
import aip.util.NVL;

public class ProcessAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		ActionForward af = null;
		ProcessParam param = getParam(request);
		
		if("".equalsIgnoreCase(param.getReqCode())){
			af = mapping.findForward("list");
		}else if("startProcess".equalsIgnoreCase(param.getReqCode())){
			try {
				(new ProcessService()).startProcess(param);
			} catch (Exception e) {
				e.printStackTrace();
				AIPUtil.forwardMessage(response, AIPUtil.getExceptionAllMessages(e));
			}
		}else if("info".equalsIgnoreCase(param.getReqCode())){
			ProcessETLInfo info = (new ProcessService()).getProcessETLInfo(param);
			if(info!=null){
				String result = info.getInfoJSON();
//				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>:info="+result);
				AIPUtil.forwardMessage(response, result);
			}
		}else if("infofull".equalsIgnoreCase(param.getReqCode())){
			ProcessETLInfo info = (new ProcessService()).getProcessETLInfo(param);
			if(info!=null){
				String result = info.getInfoJSONFull();
//				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>:infofull="+result);
				AIPUtil.forwardMessage(response, result);
			}
		}
		
		return af;
	}

	private ProcessParam getParam(HttpServletRequest request) {
		ProcessParam param = new ProcessParam();
		param.setReqCode(NVL.getString(request.getParameter("reqCode")));
		param.setTablename(NVL.getString(request.getParameter("tablename")));
		param.setIsfullprocess(NVL.getBool(request.getParameter("isfullprocess")));
		param.setTarikhaz(NVL.getString(request.getParameter("tarikhaz")));
		param.setTarikhta(NVL.getString(request.getParameter("tarikhta")));
		
		param.setIgnoreErrorAndLog(NVL.getBool(request.getParameter("ignoreErrorAndLog")));
		
		param.setWebUserParam(new AIPWebUserParam(request));
		
		param.setTableId(NVL.getString(request.getParameter("tableid")));
		
		/*
		 * otherParamNames
		 */
//		param.setCodemelli(NVL.getString(request.getParameter("codemelli")));
//		param.setEdare(NVL.getString(request.getParameter("edare")));
		String otherParamNames= AIPConfiguration.getProperty("bip.etl.otherParams", "");
		if(!NVL.isEmpty(otherParamNames)){
			String[] otherParams = otherParamNames.split(",");
			for(String otherParam :otherParams){
				if(!NVL.isEmpty(otherParam)){
					param.getOtherParams().put(otherParam, request.getParameter(otherParam));
				}
			}
		}
		
		/*
		 * otherParamsQueryName
		 */
		String otherParamsQueryName = request.getParameter("otherParamsQueryName");
		if(!NVL.isEmpty(otherParamsQueryName)){//codemelli,tarikh,tableid
			param.setOtherQueryCount("query-count-"+otherParamsQueryName);
			param.setOtherQueryEtl("query-etl-"+otherParamsQueryName);
		}
		

		return param;
	}

}