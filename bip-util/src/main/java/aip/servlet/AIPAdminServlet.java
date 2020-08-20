package aip.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.olap4j.CellSet;

import aip.db.AIPDBTable;
import aip.db.AIPDBUtil;
import aip.db.AIPTableModel;
import aip.olap.AIPOlapUtil;
import aip.orm.HibernateSessionFactory;
import aip.util.AIPUtil;
import aip.util.NVL;

/**
 * @author adramazany@yahoo.com
 * 
 * 
 * wex.xml
 * ex:
   <servlet>
    <servlet-name>aipadmin</servlet-name>
    <servlet-class>aip.servlet.AIPAdminServlet</servlet-class>
    <init-param>
      <param-name>dataSource</param-name>
      <param-value>java:comp/env/jdbc/aipsabtbi-dest</param-value>
    </init-param>
    <init-param>
      <param-name>user</param-name>
      <param-value>aip</param-value>
    </init-param>
    <init-param>
      <param-name>pass</param-name>
      <param-value>aippia</param-value>
    </init-param>
    <init-param>
      <param-name>mdxDataSource</param-name>
      <param-value>java:comp/env/jdbc/aipsabtbicube</param-value>
    </init-param>
  </servlet>
  <servlet-mapping>
  	<servlet-name>aipadmin</servlet-name>
  	<url-pattern>/aipadmin</url-pattern>
  </servlet-mapping>

 *
 */

public class AIPAdminServlet extends HttpServlet {
	
	private static final long serialVersionUID = 3663234880772177494L;
	static final String template = "<form method='post'><table width='100%' border='0'>"
			+ "<tr><td colspan='3' style='color:red'><!--CDATA[ErrorMessage]--></td></tr>" +
			"<tr><td colspan='3'>" +
			"<input type='radio' name='lang' value='dos'/>dos" +
			"<input type='radio' name='lang' value='sql'/>sql" +
			"<input type='radio' name='lang' value='hql'/>hql" +
			"<input type='radio' name='lang' value='mdx'/>mdx" +
			"U:<input type='password' name='u'/>" +
			"P:<input type='password' name='p'/>" +
			"</td></tr>" +
			"<tr><td>command:</td>" +
			"<td style='width:100%'><textarea name='command' style='width:100%'></textarea></td>" +
			"<td><input type='submit' value='execute'/></td>" +
			"</tr>" +
			"<tr><td colspan='3' style='border:1px solid gray'><!--CDATA[ExecutionResult]--></td></tr>" +
			"</table></form>";
	static final String CRLF = "<br/>";// "\n"
	static final String CommandSeperator = ";";// "\n"
	String dataSource;
	String user;
	String pass;
	String mdxDataSource;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		dataSource = config.getInitParameter("dataSource");
		user = NVL.getString(config.getInitParameter("user"));
		pass = NVL.getString(config.getInitParameter("pass"));
		mdxDataSource = NVL.getString(config.getInitParameter("mdxDataSource"));
	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String command = NVL.getString(request.getParameter("command"));
		String lang = NVL.getString(request.getParameter("lang"));
		String u = NVL.getString(request.getParameter("u"));
		String p = NVL.getString(request.getParameter("p"));
		
		StringBuffer html = new StringBuffer(template);
		AIPUtil.replaceString(html, "value='"+lang+"'", "value='"+lang+"' checked");
		AIPUtil.replaceString(html, "name='u'", "name='u' value='"+u+"'");
		AIPUtil.replaceString(html, "name='p'", "name='p' value='"+p+"'");
		AIPUtil.replaceAllString(html, "</textarea>", command+"</textarea>");

		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		
		if(user.equals(u) && pass.equals(p)){
			try{
			if("dos".equalsIgnoreCase(lang)){
				executeDosCommand(command,html);
			}else if("sql".equalsIgnoreCase(lang)){
				executeSqlCommand(command,html);
			}else if("hql".equalsIgnoreCase(lang)){
				executeHqlCommand(command,html);
			}else if("mdx".equalsIgnoreCase(lang)){
				executeMdxCommand(command,html);
			}
			} catch (Exception e) {
				e.printStackTrace();
				AIPUtil.replaceString(html, "<!--CDATA[ErrorMessage]-->",AIPUtil.getExceptionAllMessages(e));
			}
		}else{
			AIPUtil.replaceString(html, "<!--CDATA[ErrorMessage]-->", "exception occured!");
		}
			
		
		out.println(html);
		out.flush();

		// super.service(request, response);
	}

	/**************************************************************************
	 * DOS
	 *************************************************************************/
	private String executeDosCommand(String command,StringBuffer html)throws Exception {
		if(!NVL.isEmpty(command)){
			StringBuffer executionResult = new StringBuffer();
			String cmd[] = command.split(CommandSeperator);
			Process proc = null;
			
			for(int i=0;i<cmd.length;i++){
				if(!NVL.isEmpty(cmd[i])){
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
			}
			
			AIPUtil.replaceAllString(html, "<!--CDATA[ExecutionResult]-->"
					, executionResult.toString());

		}
		
		return html.toString();
	}

	/**************************************************************************
	 * SQL
	 *************************************************************************/
	private void executeSqlCommand(String command,StringBuffer html) throws Exception{
		if (NVL.isEmpty(dataSource)) {
			AIPUtil.replaceAllString(html, "<!--CDATA[ErrorMessage]-->",
					"init-param dataSource not defined in servlet difinition!");
		} else if (!NVL.isEmpty(command)) {
				StringBuffer executionResult = new StringBuffer();
				String cmd[] = command.split(CommandSeperator);

				for (int i = 0; i < cmd.length; i++) {
					if (!NVL.isEmpty(cmd[i])) {
						executionResult.append("Result of:" + cmd[i]);
						executionResult.append(CRLF);

						if (cmd[i].toLowerCase().indexOf("select ") > -1) {
							AIPTableModel sqlresult = AIPDBUtil
									.executeQuery2Table(dataSource, cmd[i]);
							executionResult.append(sqlresult2html(sqlresult));
						} else {
							AIPDBUtil.executeUpdate(dataSource, cmd[i]);
							executionResult.append("succeed.");
						}
						executionResult.append(CRLF);
						executionResult.append("----------------------------");
						executionResult.append(CRLF);
					}
				}

				AIPUtil.replaceAllString(html, "<!--CDATA[ExecutionResult]-->",
						executionResult.toString());

		}

	}

	private String sqlresult2html(AIPTableModel sqlresult) {
		StringBuffer sb = new StringBuffer();
		sb.append("<table border='1'>");
		sb.append("<tr>");
		for (int j = 0; j < sqlresult.getColumnCount(); j++) {
			sb.append("<th>");
			sb.append(sqlresult.getColumnName(j));
			sb.append("</th>");
		}
		sb.append("</tr>");
		for (int row = 0; row < sqlresult.getRowCount(); row++) {
			sb.append("<tr>");
			for (int column = 0; column < sqlresult.getColumnCount(); column++) {
				sb.append("<td>");
				sb.append(sqlresult.getValueAt(row, column));
				sb.append("</td>");
			}
			sb.append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}

	
	/**************************************************************************
	 * HQL
	 *************************************************************************/
	private String executeHqlCommand(String command,StringBuffer html) throws Exception{
		if (!NVL.isEmpty(command)) {
			Session session=null;
			try {
				StringBuffer executionResult = new StringBuffer();
				String cmd[] = command.split(CommandSeperator);
				
				session = HibernateSessionFactory.getSession();

				for (int i = 0; i < cmd.length; i++) {
					if (!NVL.isEmpty(cmd[i])) {
						executionResult.append("Result of:" + cmd[i]);
						executionResult.append(CRLF);

						Query query = session.createQuery(cmd[i]);

						if (cmd[i].toLowerCase().indexOf("select ") > -1 
								|| (cmd[i].toLowerCase().indexOf("from ") > -1 && cmd[i].toLowerCase().indexOf("delete ")==-1 )) {
							List hqlresult = query.list();
							
							executionResult.append(hqlresult2html(hqlresult));
						} else {
							query.executeUpdate();
							executionResult.append("succeed.");
						}
						executionResult.append(CRLF);
						executionResult.append("----------------------------");
						executionResult.append(CRLF);
					}
				}

				AIPUtil.replaceAllString(html, "<!--CDATA[ExecutionResult]-->",
						executionResult.toString());

			}catch(Exception e){
				throw e;
			}finally{
				if(session!=null)session.close();
			}
		}

		return html.toString();
	}

	private String hqlresult2html(List hqlresult) {
		StringBuffer sb = new StringBuffer();
		sb.append("<table border='1'>");
		sb.append("<tr>");
//		for (int j = 0; j < hqlresult.size(); j++) {
//			sb.append("<th>");
//			sb.append(hqlresult.getColumnName(j));
//			sb.append("</th>");
//		}
		sb.append("</tr>");
		for (int row = 0; row < hqlresult.size(); row++) {
			sb.append("<tr><td>");
			AIPUtil.printObject(hqlresult.get(row),sb,0,true,true,false);
			sb.append("</td></tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}

	/**************************************************************************
	 * MDX
	 *************************************************************************/
	private void executeMdxCommand(String command,StringBuffer html) throws Exception{
		if (NVL.isEmpty(dataSource)) {
			AIPUtil.replaceAllString(html, "<!--CDATA[ErrorMessage]-->",
					"init-param dataSource not defined in servlet difinition!");
		} else if (!NVL.isEmpty(command)) {
				StringBuffer executionResult = new StringBuffer();
				String cmd[] = command.split(CommandSeperator);

				for (int i = 0; i < cmd.length; i++) {
					if (!NVL.isEmpty(cmd[i])) {
						executionResult.append("Result of:" + cmd[i]);
						executionResult.append(CRLF);

						CellSet mdxresult = AIPOlapUtil.executeMdx(mdxDataSource, cmd[i]);
						AIPOlapUtil.cnvCellSet2String(executionResult,mdxresult);
						//executionResult.append(mdxresult2html(mdxresult));

						executionResult.append(CRLF);
						executionResult.append("----------------------------");
						executionResult.append(CRLF);
					}
				}

				AIPUtil.replaceAllString(html, "<!--CDATA[ExecutionResult]-->",
						executionResult.toString());

		}

	}

	private Object mdxresult2html(CellSet mdxresult) {
		// TODO Auto-generated method stub
		return null;
	}
	
}