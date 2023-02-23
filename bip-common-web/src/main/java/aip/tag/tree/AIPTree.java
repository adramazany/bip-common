package aip.tag.tree;

import aip.tag.AIPBaseTag;
import aip.util.AIPUtil;
import aip.util.NVL;

import javax.servlet.jsp.JspException;
import java.io.*;

public class AIPTree extends AIPBaseTag {

	/*
	 * Attributes
	 */
	//required
	String loader="tag/tree/sampletreeloader";
	String id="tree1";
	//String title="tree root";
	//optoinal
	//String align="center";
	//String style="simple";
	String type;
	String isJQueryIncluded="true";//means don't include jquery
	String selectedIds="";
	String selectedNames="";
	
	
	/*
	 * internal variables
	 */
	String jqueryPath="	<script type=\"text/javascript\" src=\"aipconfig/jquery/jquery.js\"></script><script type=\"text/javascript\" src=\"aipconfig/jquery/jquery.tree.js\"></script>";
	//String contentStartTag="",contentEndTag="";
	long lastCheckTime,lastFileReaded;
	//static int delayTime = 5000; // 65000;
	//String baseFolder="aipconfig/tree/";
	String oldstyle="";
	//placeholders
	//static String placeHolder="[PLACEHOLDER]";
	String idPlaceHolder="[IDPLACEHOLDER]";
	//String titlePlaceHolder="[TITLEPLACEHOLDER]";
	//String alignPlaceHolder="[ALIGNPLACEHOLDER]";
	String jqueryPlaceHolder="[JQUERYPLACEHOLDER]";
	
	String selectedIdsPlaceHolder="[SELECTEDIDSPLACEHOLDER]";
	String selectedNamesPlaceHolder="[SELECTEDNAMESPLACEHOLDER]";

	public AIPTree(){
		super();
		style="simple";
	}

	public int doStartTag() throws JspException {
		int res = super.doStartTag();
		try {
			//loadContentFile();
			String currentFolder = baseFolder+ "/aip/tag/tree/" +style+"/";
			
			//loadContentFile(currentFolder+"aiptree.html");
			contentStartTag = getStartTag(currentFolder+"aiptree.html", placeHolder);
			contentEndTag = getEndTag(currentFolder+"aiptree.html", placeHolder);
			
			//String content=AIPUtil.replaceAllString(contentStartTag,idPlaceHolder, id);
			String content=AIPUtil.replaceAllString(contentStartTag,idPlaceHolder, id);
			
			//System.out.println("AIPTree.doStartTag():content="+content);
			
			content=AIPUtil.replaceAllString(content,titlePlaceHolder,NVL.getString(title));
			content=AIPUtil.replaceAllString(content,alignPlaceHolder,NVL.getString( align));
			if(!NVL.getBool(isJQueryIncluded)){
				////System.out.println("AIPTree.doStartTag()___________________________");
				content=AIPUtil.replaceAllString(content, jqueryPlaceHolder,NVL.getString( jqueryPath));
			}else{
				////System.out.println("AIPTree.doStartTag()==============================");
				content=AIPUtil.replaceAllString(content, jqueryPlaceHolder, "");
			}
			
			//System.out.println("AIPTree.doStartTag():content="+content);

			pageContext.getOut().write(content);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try{
			//System.out.println("AIPTree.doStartTag():::::::::::loader="+loader);
			pageContext.include(loader);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return res;
	}

//	public int doAfterBody() throws JspException {
//		try {
//			BodyContent bodyContent = getBodyContent();
//			JspWriter out = bodyContent.getEnclosingWriter();
//			bodyContent.writeOut(out);
//			bodyContent.clearBody();
//			
//		} catch (Exception ex) {
//			throw new JspException("error in AIPTree: " + ex);
//		}
//		return super.doAfterBody();
//	}

//	public int doEndTag() throws JspException {
//		int i = super.doEndTag();
//		try {
//			
//			StringBuffer sb = new StringBuffer(contentEndTag);
//			//System.out.println("AIPTree.doEndTag():sb="+sb.toString());
//			
//			AIPUtil.replaceAllString(sb, idPlaceHolder, id );
//			AIPUtil.replaceAllString(sb, selectedIdsPlaceHolder, NVL.getString(selectedIds) ); // NVL.getString(pageContext.getAttribute("selectedIds")) );
//			AIPUtil.replaceAllString(sb, selectedNamesPlaceHolder, NVL.getString(selectedNames) ); // NVL.getString(pageContext.getAttribute("selectedNames")) );
//			
//			pageContext.getOut().write(sb.toString());
//			//System.out.println("AIPTree.doEndTag():sb="+sb.toString());
//			
//			//System.out.println("AIPTree.doEndTag():finished.");
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return i;
//	}
	public String getContentEndTag() {
		StringBuffer sb = new StringBuffer(contentEndTag);
		//System.out.println("AIPTree.doEndTag():sb="+sb.toString());
		
		AIPUtil.replaceAllString(sb, idPlaceHolder, id );
		AIPUtil.replaceAllString(sb, selectedIdsPlaceHolder, NVL.getString(selectedIds) ); // NVL.getString(pageContext.getAttribute("selectedIds")) );
		AIPUtil.replaceAllString(sb, selectedNamesPlaceHolder, NVL.getString(selectedNames) ); // NVL.getString(pageContext.getAttribute("selectedNames")) );
		
		//pageContext.getOut().write(sb.toString());
		//System.out.println("AIPTree.doEndTag():sb="+sb.toString());
		
		//System.out.println("AIPTree.doEndTag():finished.");
		return sb.toString();
	}

	
	public static void appendTreeNode(PrintWriter out ,String text,String urlChildLoader){
		appendTreeNode(out, text,"", urlChildLoader,"",TreeNodeTypeEnum.DEFAULT);
	}
	public static void appendTreeNode(PrintWriter out ,String text,String id,String urlChildLoader,String onclick,TreeNodeTypeEnum nodeType){
		String onclickStr=(onclick==null || "".equals(onclick.trim())) ? "" : " onclick=\""+onclick+"\" " ;
		switch (nodeType) {
		case CHECKBOX:
			text="<input type='checkbox' value='"+id+"' "+onclickStr+"/>"+text;
			appendTreeNode(out, text, urlChildLoader,"","");
			break;
		case RADIO:
			text="<input type='radio' name='' value='"+id+"' "+onclickStr+"/>"+text;
			appendTreeNode(out, text, urlChildLoader,"","");
			break;

		default:
			appendTreeNode(out, text, urlChildLoader,onclick,"");
			break;
		}
	}
	public static void appendTreeNode(PrintWriter out ,String text,String urlChildLoader,String onclick,String id){
		String onclickStr=(onclick==null || "".equals(onclick.trim())) ? "" : " onclick=\""+onclick+"\" " ;
		String idStr=(id==null || "".equals(id.trim())) ? "" : " id=\""+id+"\" " ;
		out.append("<ul>");
		out.append("<li><span class='text' "+onclickStr+" "+idStr+" >");
		out.append(text);
		out.append("</span>");
		out.append("<ul class='ajax'>");
		//out.append("		<li>{url:tag/tree/sampletreeloader?tree_id="+i+"}</li>");
		out.append("<li>{url:");
		out.append(urlChildLoader);
		out.append("}</li>");
		out.append("</ul>");
		out.append("</li>");
		out.append("</ul>");
	}
	public static void appendTreeNodeLeafBegin(PrintWriter out ,String onclick,String id){
		String onclickStr=(onclick==null || "".equals(onclick.trim())) ? "" : " onclick=\""+onclick+"\" " ;
		String idStr=(id==null || "".equals(id.trim())) ? "" : " id=\""+id+"\" " ;
		out.append("<ul>");
		out.append("<li ><span class='text' "+onclickStr+" "+idStr+" >");
	}
	public static void appendTreeNodeLeafEnd(PrintWriter out ){
		out.append("</span>");
		out.append("</li>");
		out.append("</ul>");
	}
	
	public static void appendTreeNodeLeaf(PrintWriter out ,String text,String onclick,String id){
//		String onclickStr=(onclick==null || "".equals(onclick.trim())) ? "" : " onclick=\""+onclick+"\" " ;
//		String idStr=(id==null || "".equals(id.trim())) ? "" : " id=\""+id+"\" " ;
//		out.append("<ul>");
//		out.append("<li ><span class='text' "+onclickStr+" "+idStr+" >");
		appendTreeNodeLeafBegin(out, onclick, id);
		out.append(text);
		appendTreeNodeLeafEnd(out);
//		out.append("</span>");
//		out.append("</li>");
//		out.append("</ul>");
	}
	
	public static void appendTreeNodeLeaf(PrintWriter out ,String text,String id,String onclick,TreeNodeTypeEnum nodeType){
		String onclickStr=(onclick==null || "".equals(onclick.trim())) ? "" : " onclick=\""+onclick+"\" " ;
		switch (nodeType) {
		case CHECKBOX:
			text="<input type='checkbox' value='"+id+"' "+" "+onclickStr+"/>"+text;
			appendTreeNodeLeaf(out, text, "",id);
			break;
		case RADIO:
			text="<input type='radio' name='' value='"+id+"' "+" "+onclickStr+"/>"+text;
			appendTreeNodeLeaf(out, text, "",id);
			break;

		default:
			//appendTreeNodeLeaf(out, text, "",id);
			appendTreeNodeLeaf(out, text, onclick,id);
			break;
		}
	}
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type=type;
	}
	
	
	void loadContentFile(){//AIPTreeTypeInfo info){
		if((lastCheckTime+delayTime)<System.currentTimeMillis() || !style.equalsIgnoreCase(oldstyle))
		{
			lastCheckTime=System.currentTimeMillis();
			try {
				String contentFile = baseFolder+ "/aip/tag/tree/" +style+"/aiptree.html";
				
				//System.out.println("AIPTree.loadContentFile()!!!!!!!!:contentFile="+contentFile);
				
				String realPath = pageContext.getServletContext().getRealPath(contentFile );
				File f = new File(realPath);
				////System.out.println("AIPTree.loadSkinFileContent():file="+f.getPath());
				//System.out.println("AIPTree.loadContentFile():file="+f.getPath());
				if(f.lastModified()>lastFileReaded || !style.equalsIgnoreCase(oldstyle))
				{
					FileInputStream fin = new FileInputStream(f);
					byte buf[]=new byte[fin.available()];
					fin.read(buf);
					fin.close();
					String sb = new String(buf);
					int pos;

					/*
					 * align
					 */
//					pos = sb.indexOf(alignPlaceHolder);
//					if(pos>=0){
//						sb = sb.substring(0,pos)+align+sb.substring(pos+alignPlaceHolder.length());
//					}
					
					/*
					 * split start and end
					 */
					pos = sb.indexOf(placeHolder);
					contentStartTag = sb.substring(0,pos);
					contentEndTag = sb.substring(pos+placeHolder.length());
					
					lastFileReaded=f.lastModified();
	
					//System.out.println("AIPTree.loadSkinFileContent():in................");
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			oldstyle=style;
		}
	}
//	public static void main(String args[]){
//		//System.out.println("="+AIPUtil.replaceAllString("this is a tes","is","as1"));
//	}

//	public String getStyle() {
//		return style;
//	}
//
//	public void setStyle(String style) {
//		this.style = style;
//	}
//
//	public String getAlign() {
//		return align;
//	}
//
//	public void setAlign(String align) {
//		this.align = align;
//	}

	public String getLoader() {
		return loader;
	}

	public void setLoader(String loader) {
		this.loader = loader;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

//	public String getTitle() {
//		return title;
//	}
//
//	public void setTitle(String title) {
//		this.title = title;
//	}

	public String getIsJQueryIncluded() {
		return isJQueryIncluded;
	}

	public void setIsJQueryIncluded(String isJQueryIncluded) {
		this.isJQueryIncluded = isJQueryIncluded;
	}

	public String getSelectedIds() {
		return selectedIds;
	}

	public void setSelectedIds(String selectedIds) {
		this.selectedIds = selectedIds;
	}

	public String getSelectedNames() {
		return selectedNames;
	}

	public void setSelectedNames(String selectedNames) {
		this.selectedNames = selectedNames;
	}

}
