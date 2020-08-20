package aip.olap;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import aip.util.AIPException;
import aip.util.AIPUtil;
import aip.util.AIPWebUserParam;
import aip.util.NVL;

public class AIPOlapServlet extends HttpServlet {

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(response.SC_OK);
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String out_str="";

		try {
			AIPOlapParam param = getParam(request);
			
			if(param.getReqCode().equals("")  || param.getReqCode().equalsIgnoreCase("test")){
				InputStream resource = this.getClass().getResourceAsStream("/aip/olap/aipolaptestpage.html");
				byte buf[]=new byte[resource.available()];
				resource.read(buf);
				out_str = new String(buf,"utf-8");
			}else if (param.getReqCode().equalsIgnoreCase("executeMdx")){
				AIPOlapUtil olapUtil = new AIPOlapUtil(param.getUrl(),
						param.getCatalog(), param.getUsername(),
						param.getPassword());
				out_str = olapUtil.executeMdx2JSArray(param.getMdx());
			}
		} catch (AIPException e) {
			e.printStackTrace();
			String msg = AIPUtil.replaceAllString(e.getMessage(),"\"","");
			msg = AIPUtil.replaceAllString(msg,"'","");
			msg = AIPUtil.replaceAllString(msg,"\r","");
			msg = AIPUtil.replaceAllString(msg,"\n","");
			out_str = "{exception:'" + msg + "'}";
			System.out.println(out_str);
			//response.setStatus(response.SC_EXPECTATION_FAILED);
		} catch (Exception e) {
			e.printStackTrace();
			String msg = AIPUtil.replaceAllString(e.getMessage(),"\"","");
			msg = AIPUtil.replaceAllString(msg,"'","");
			msg = AIPUtil.replaceAllString(msg,"\r","");
			msg = AIPUtil.replaceAllString(msg,"\n","");
			out_str = "{exception:'" + msg + "'}";
			System.out.println(out_str);
			response.setStatus(response.SC_EXPECTATION_FAILED);
		}
		out.print(out_str);
		out.flush();

		//super.service(request, response);
	}

	private AIPOlapParam getParam(HttpServletRequest request)
			throws AIPException {
		AIPOlapParam param = new AIPOlapParam();
		param.setWebUserParam(new AIPWebUserParam(request));

		param.setReqCode(NVL.getString(request.getParameter("reqCode")));
		param.setMdx(NVL.getString(request.getParameter("mdx")));

		param.setUrl(getInitParameter("url"));
		param.setCatalog(getInitParameter("catalog"));
		if (NVL.isEmpty(param.getUrl()) || NVL.isEmpty(param.getCatalog())) {
			throw new AIPException(
					"please add init-param for [url] and [catalog] to your web.xml to AIPOlapServlet");
		}

		HttpSession session = request.getSession();
		param.setUsername((String) session.getAttribute("AIPOlapServlet.username"));
		param.setPassword((String) session.getAttribute("AIPOlapServlet.password"));
		if (NVL.isEmpty(param.getUsername()) || NVL.isEmpty(param.getPassword())){
			String classname = getInitParameter("engine-security-provider-class");
			if (NVL.isEmpty(classname)) {
				throw new AIPException(
						"please add init-param for [engine-security-provider-class] to your web.xml to AIPOlapServlet");
			}
			System.out.println("AIPOlapServlet.getParam():engine-security-provider-class="+classname);
			try {
				AIPOlapEngineSecurityInterface engineSecurity = (AIPOlapEngineSecurityInterface) Class.forName(classname).newInstance();
				
				System.out.println("AIPOlapServlet.getParam():engine-security-provider-object="+engineSecurity.toString());

				param.setUsername(engineSecurity.getXMLAUser(param.getWebUsername()));
				param.setPassword(engineSecurity.getXMLAPassword(param.getWebUsername()));
				System.out.println("AIPOlapServlet.getParam():username="+param.getUsername()+",password="+param.getPassword());
			} catch (Exception e) {
				e.printStackTrace();
				throw new AIPException(e);
			}

			session.setAttribute("AIPOlapServlet.username", param.getUsername());
			session.setAttribute("AIPOlapServlet.password", param.getPassword());
		}

		return param;
	}
}
