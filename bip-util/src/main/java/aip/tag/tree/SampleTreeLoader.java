package aip.tag.tree;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import aip.util.NVL;

public class SampleTreeLoader extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		//System.out.println("SampleTreeLoader.doGet()");
		
		int parentId = NVL.getInt( request.getParameter("tree_id") );
		if(parentId<=0){//root
			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			for(int i=0;i<3;i++){
//				out.println("<ul>");
//				out.println("<li ><span class='text'>%%%%%%%%%Tree Node "+i+"</span>");
//				out.println("	<ul class='ajax'>");
//				out.println("		<li>{url:tag/tree/sampletreeloader?tree_id="+i+"}</li>");
//				out.println("	</ul>");
//				out.println("</li>");
//				out.println("</ul>");
				AIPTree.appendTreeNode(out, "%%%%%%%%%Tree Node "+i+"", "tag/tree/sampletreeloader?tree_id="+i);
			}
		}else{
			//System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			for(int i=0;i<3;i++){
//				out.println("<ul>");
//				out.println("<li><span class='text'>Tree Node "+parentId+""+i+"</span>");
//				out.println("	<ul class='ajax'>");
//				out.println("		<li>{url:tag/tree/sampletreeloader?tree_id="+parentId+""+i+"}</li>");
//				out.println("	</ul>");
//				out.println("</li>");
//				out.println("</ul>");
				AIPTree.appendTreeNode(out, "%%%%%%%%%Tree Node "+parentId+""+i+"", "tag/tree/sampletreeloader?tree_id="+parentId+""+i);
			}
			
		}
		
		out.flush();
		//it should not close else we have error in aiptree close tag
		//out.close();
	}

}
