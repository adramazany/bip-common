package aip.tag.pagging;

import aip.tag.AIPBodyTagSupport;
import aip.util.AIPUtil;
import aip.util.NVL;
import bip.common.web.BIPCommonWeb;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AIPPagging extends AIPBodyTagSupport {

	String title;
	String titlePlaceHolder="[TITLEPLACEHOLDER]";
	
	String totalCount;
	String pageSize;
	String currentPage;
	String type="server";//client or server;
	String style="bullet-gray";
	String paggingCount="10";
	
	String hasFirstAndLast;
	String cssFile="aippagging.css";
	
	String ajaxEnabledAndLocationIs="";
	
	String baseFolder="aipconfig/pagging/";
	
	AIPPaggingLSTInterface paggingLSTObj;
	String paggingLST;

	/*
	 * action properties
	 */
	String href;
	String counterReplace="@";
	//or
	String formId;
	String inputName="requestPage";
	
	/*
	 * extra script onclick {for LawView.jsp onclick="this.form.elements['whichButtonClick'].value='go/state/phase';}
	 */
	String onclickPlaceHolder="//[ONCLICKPLACEHOLDER]";
	String onclick="";
	String paggingClickEnabled="true";
	
	String extraAttributes="";
	
	String alignPlaceHolder="[ALIGNPLACEHOLDER]";
	String align="right";

	static String placeHolder="[PLACEHOLDER]";
	String contentStartTag="",contentEndTag="";
	long lastCheckTime,lastFileReaded;
	static int delayTime = 5000; // 65000;
	String oldstyle="";

	public AIPPagging(){
		super();
	}

	boolean doStartTagReturenEmpty=false;
	public int doStartTag() throws JspException {
		int res = super.doStartTag();
		try {
//			if(paggingLST!=null && paggingLST.getTotalCount()>paggingLST.getPageSize()){
//				pageContext.getOut().write(contentStartTag);
//			}
			//
			

//			int pos=contentStartTag.indexOf(onclickPlaceHolder);
//			String sb;
//			if(pos>=0){
//				sb=contentStartTag.substring(0,pos)+onclick+contentStartTag.substring(pos+onclickPlaceHolder.length());
//			}else{
//				sb=contentStartTag;
//			}
			 
			/*
			 * contentStartTag
			 */
			String currentFolder = baseFolder+style;//+"/"+"/aippagging.html";
			String resourceURL = currentFolder+"/aippagging.html";
			
//			loadContentFile();
			contentStartTag = getStartTag(resourceURL, placeHolder);
			contentEndTag = getEndTag(resourceURL, placeHolder);
			
			/*
			 * align
			 */
			contentStartTag=AIPUtil.replaceAllString(contentStartTag, alignPlaceHolder, align);
			contentEndTag=AIPUtil.replaceAllString(contentEndTag, alignPlaceHolder, align);
			
			

			StringBuffer pagging=new StringBuffer();
			/*
			 * title
			 */
//			String content =contentStartTag;
//			int pos = content.indexOf(titlePlaceHolder);
//			if(pos>=0){
//				content = content.substring(0,pos)+title+content.substring(pos+titlePlaceHolder.length());
//			}
			pagging.append( replaceString( contentStartTag , titlePlaceHolder , title )  );

			
			int totalCount =  NVL.getInt(this.getTotalCount());
			int pageSize = NVL.getInt(this.getPageSize());
			int currentPage = NVL.getInt(this.getCurrentPage());
			int paggingCount = NVL.getInt(this.getPaggingCount());
			boolean hasFirstAndLast = NVL.getBool(this.getHasFirstAndLast());
			boolean paggingClickEnabled = NVL.getBool(this.paggingClickEnabled);
			
//			System.out.println("AIPPagging.doStartTag()!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//			System.out.println("title="+title);
//			System.out.println("totalCount = "+ NVL.getInt(this.getTotalCount()));
//			System.out.println("pageSize ="+ NVL.getInt(this.getPageSize()));
//			System.out.println("currentPage ="+ NVL.getInt(this.getCurrentPage()));
//			System.out.println("paggingCount ="+ NVL.getInt(this.getPaggingCount()));
//			System.out.println("hasFirstAndLast ="+ NVL.getBool(this.getHasFirstAndLast()));
			
			/*
			 * incomplete params
			 */
			if(totalCount<=0 || pageSize<=0){
//				System.out.println("AIPPagging.doStartTag()..............INCOMPLETE PARAMS");
				doStartTagReturenEmpty=true;
				return res;
			}

			/*
			 * total page
			 */
			int totalPage = totalCount / pageSize; 
			if(totalCount % pageSize != 0) totalPage++;

			/*
			 * no need to show pagging
			 */
			if(totalPage<=1){
				//System.out.println("AIPPagging.doStartTag()..............NO NEED TO SHOW PAGGING");
				doStartTagReturenEmpty=true;
				return res;
			}

			
			/*
			 * current page
			 */
			if(currentPage<=0 || currentPage>totalPage){
//				System.out.println("AIPPagging.doStartTag()..............Current Page set to 1");
				currentPage = 1;
			}
			
			/*
			 * start page
			 */
			int remainPage = ( currentPage % paggingCount );
			remainPage = remainPage==0 ? paggingCount : remainPage;
			int startPage = currentPage - remainPage + 1;  
			
			/*
			 * end page
			 */
			int endPage = startPage + paggingCount -1;
			if(totalPage<endPage)endPage=totalPage;
			
			/*
			 * has couterReplacemeny Symbol or no
			 */
			String curAction="";
			boolean hasCounterReplace = (href!=null && href.contains(counterReplace));
			boolean hasForm = (formId!=null && formId.length()>0 ); 
			


			//moved to top plus placeholder
			//pagging.append("<td class='"+style+"-title'>"+title);
			
			/*
			 * add hidden input for return in form submit
			 */
			String inputId=this.inputName;
			if(hasForm){
				inputId = formId+"_"+this.inputName;
				pagging.append("<input type='hidden' name='"+inputName+"' id='"+inputId+"' value='"+currentPage+"'/>");
			}
			
			pagging.append("</td>");
			
			
			//AIPUtil.printObject(this);
			
			String img;
			/*
			 * first
			 */
			if(hasFirstAndLast && startPage>paggingCount){
				appendImage(pagging,hasCounterReplace,1,hasForm,style+"-first");//currentFolder+"bulletFirst.png",
			}
			/*
			 * prev
			 */
			if(startPage>paggingCount){
				appendImage(pagging,hasCounterReplace,startPage-1,hasForm,style+"-prev");//currentFolder+"bulletPrev.png",
			}
			/*
			 * bullet
			 */
			
			for(int i=startPage;i<=endPage;i++){
				
				String extraAttributes = this.extraAttributes.replaceAll(counterReplace, ""+i);
				
				if(hasForm){
					curAction="<span "+getOnclickString(i,inputId)+" "+extraAttributes+" >"+i+"</span>";
				}else{
					curAction="<a "+getHrefStr(hasCounterReplace,i)+" "+extraAttributes+">"+i+"</a>";
				}
				
				if(i==currentPage){
					pagging.append("<td class='"+style+"-selected'>"+curAction+"</td>");
				}else{
					pagging.append("<td class='"+style+"'>"+curAction+"</td>");
				}
			}
			/*
			 * next
			 */
			if(endPage<totalPage){
				appendImage(pagging,hasCounterReplace,endPage+1,hasForm,style+"-next");//currentFolder+"bulletNext.png",
			}
			/*
			 * last page
			 */
			if(endPage<totalPage && hasFirstAndLast){
				appendImage(pagging,hasCounterReplace,totalPage,hasForm,style+"-last", totalPage);//currentFolder+"bulletLast.png",
			}

			
			pageContext.getOut().write(pagging.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}
	private String getOnclickString(int page,String inputId) {
		boolean paggingClickEnabled= NVL.getBool(this.paggingClickEnabled);
		String onclickString="";
		if(paggingClickEnabled || !"".equals(onclick)){
			onclickString="onclick=\"javascript:";
			if(!"".equals(onclick)){
				String onclick = this.onclick.replaceAll(counterReplace, ""+page);
				onclickString+=onclick;
			}
			if(paggingClickEnabled){
				onclickString+="paggingClick(this,"+page+",'"+inputId+"','"+formId+"','"+ajaxEnabledAndLocationIs+"')\"";
			}
		}
		return onclickString;
	}
	private String replaceString(String src,String find,String replacement){
		if(src!=null && !"".equals(src) && find!=null){
			int pos = src.indexOf(find);
			if(pos>=0){
				src = src.substring(0,pos)+replacement+src.substring(pos+find.length());
			}
		}
		return src;
	}

	private void appendImage(StringBuffer pagging, boolean hasCounterReplace,int page,boolean hasForm,String td_class){//String imgSrc ,
		String curAction;
		String img= "<img src='"+baseFolder+style+"/clear.gif' alt='&nbsp;'/>";//"&nbsp;";//"<img src='"+imgSrc+"'/>";
		String extraAttributes = this.extraAttributes.replaceAll(counterReplace, ""+ page);
		if(hasForm){
			String inputId = formId+"_"+inputName;
			curAction="<span "+getOnclickString(page,inputId)+" "+extraAttributes+"  >"+img+"</span>";
		}else{
			curAction="<a "+getHrefStr(hasCounterReplace,page)+" "+extraAttributes+">"+img+"</a>";
		}
		pagging.append("<td class='"+td_class+"'>"+curAction+"</td>");

	}
	
	private void appendImage(StringBuffer pagging, boolean hasCounterReplace,int page,boolean hasForm,String td_class, int totalPage){//String imgSrc ,
		String curAction;
		String img= "<img src='"+baseFolder+style+"/clear.gif' title='برو به صفحه: "+totalPage+"' alt='&nbsp;'/>";//"&nbsp;";//"<img src='"+imgSrc+"'/>";
		String extraAttributes = this.extraAttributes.replaceAll(counterReplace, ""+ page);
		if(hasForm){
			String inputId = formId+"_"+inputName;
			curAction="<span "+getOnclickString(page,inputId)+" "+extraAttributes+"  >"+img+"</span>";
		}else{
			curAction="<a "+getHrefStr(hasCounterReplace,page)+" "+extraAttributes+">"+img+"</a>";
		}
		pagging.append("<td class='"+td_class+"'>"+curAction+"</td>");
	}

	private String getHrefStr(boolean hasCounterReplace,int page) {
		/*
		 * replace couterReplacemeny Symbol with counter else append to end
		 */
		if(hasCounterReplace){
			return (href!=null && !"".equals(href.trim())) ? "href='"+href.replace(counterReplace,""+ page)+"'" : "";
		}else{
			return (href!=null && !"".equals(href.trim())) ? "href='"+href +page+"'" : ""; 
		}
	}

	public int doAfterBody() throws JspException {
		try {
			BodyContent bodyContent = getBodyContent();
			JspWriter out = bodyContent.getEnclosingWriter();
			bodyContent.writeOut(out);
			bodyContent.clearBody();
		} catch (Exception ex) {
			throw new JspException("error in AIPSkin: " + ex);
		}
		return super.doAfterBody();
	}

	public int doEndTag() throws JspException {
		int i = super.doEndTag();
		try {
			if(!doStartTagReturenEmpty){
				pageContext.getOut().write(contentEndTag);
			}
			doStartTagReturenEmpty=false;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return i;
	}

	
	void loadContentFile(){//AIPSkinTypeInfo info){
		if((lastCheckTime+delayTime)<System.currentTimeMillis() || !style.equalsIgnoreCase(oldstyle))
		{
			lastCheckTime=System.currentTimeMillis();
			try {
				String contentFile = baseFolder+style+"/aippagging.html";
				String realPath = pageContext.getServletContext().getRealPath(contentFile );
				File f = new File(realPath);
				//System.out.println("AIPSkin.loadSkinFileContent():file="+f.getPath());
//				System.out.println("AIPPagging.loadContentFile():file="+f.getPath());
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
					pos = sb.indexOf(alignPlaceHolder);
					if(pos>=0){
						sb = sb.substring(0,pos)+align+sb.substring(pos+alignPlaceHolder.length());
					}
					
					/*
					 * split start and end
					 */
					pos = sb.indexOf(placeHolder);
					contentStartTag = sb.substring(0,pos);
					contentEndTag = sb.substring(pos+placeHolder.length());
					
					lastFileReaded=f.lastModified();
	
//					System.out.println("AIPPagging.loadSkinFileContent():in................");
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			oldstyle=style;
		}
	}

//	public String getPaggingLSTProperty() {
//		return paggingLSTProperty;
//	}
//
//	public void setPaggingLSTProperty(String paggingLSTProperty) {
//		Object o = pageContext.getAttribute(paggingLSTProperty);
//		if(o!=null && o instanceof AIPPaggingLSTInterface){
//			this.paggingLST=(AIPPaggingLSTInterface) o;
//		}
//		this.paggingLSTProperty = paggingLSTProperty;
//	}

	public String getTotalCount() {
		if(paggingLSTObj!=null && NVL.getInt(totalCount)<1){
			try{
				return NVL.getString(paggingLSTObj.getTotalCount() );
			}catch(Exception ex){
				System.out.println("AIPPagging.getTotalCount():"+ex.getMessage());
			}
		}
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getPageSize() {
		if(paggingLSTObj!=null && NVL.getInt(pageSize)<1){
			try{
				return NVL.getString(paggingLSTObj.getPageSize());
			}catch(Exception ex){System.out.println("AIPPagging.getPageSize():"+ex.getMessage());}
		}
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getCurrentPage() {
		if(paggingLSTObj!=null && NVL.getInt(currentPage)<1){
			try{
				return NVL.getString(paggingLSTObj.getCurrentPage());
			}catch(Exception ex){System.out.println("AIPPagging.getCurrentPage():"+ex.getMessage());}
		}
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPaggingCount() {
		return paggingCount;
	}

	public void setPaggingCount(String paggingCount) {
		this.paggingCount = paggingCount;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getCounterReplace() {
		return counterReplace;
	}

	public void setCounterReplace(String counterReplace) {
		this.counterReplace = counterReplace;
	}

	public String getHasFirstAndLast() {
		return hasFirstAndLast;
	}

	public void setHasFirstAndLast(String hasFirstAndLast) {
		this.hasFirstAndLast = hasFirstAndLast;
	}

	public String getCssFile() {
		return cssFile;
	}

	public void setCssFile(String cssFile) {
		this.cssFile = cssFile;
	}

	public String getPaggingLST() {
		return paggingLST;
	}

//	Object getAttributeAllScope(String name){
//		Object attr = pageContext.getAttribute(name, pageContext.getAttributesScope(name) );
//		Object attr=pageContext.getAttribute(name);
//		if(attr==null){
//			attr=pageContext.getAttribute(name,PageContext.REQUEST_SCOPE);
//			if(attr==null){
//				attr=pageContext.getAttribute(name,PageContext.SESSION_SCOPE);
//				if(attr==null){
//					attr=pageContext.getAttribute(name,PageContext.APPLICATION_SCOPE);
//				}
//			}
//		}
//		return attr;
//	}
	public void setPaggingLST(String paggingLST) {
		this.paggingLST = paggingLST;
		
		int pos = paggingLST.indexOf("."); 
		if(pos>-1){
			String name = paggingLST.substring(0,pos);
			String field = paggingLST.substring(pos+1);
//			System.out.println("AIPPagging.setPaggingLST():name="+name+",field="+field);
//			Object obj = getAttributeAllScope(name);
			Object obj = BIPCommonWeb.getAttributeAnyScope(pageContext,name);
			try {
				obj = AIPUtil.invokeMulti(obj, field);
//				System.out.println("AIPPagging.setPaggingLST():obj="+obj);
				paggingLSTObj = (AIPPaggingLSTInterface) obj;
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}else{
//			paggingLSTObj = (AIPPaggingLSTInterface) getAttributeAllScope(paggingLST);
			paggingLSTObj = (AIPPaggingLSTInterface) BIPCommonWeb.getAttributeAnyScope(pageContext,paggingLST);
		}
	}

	public String getInputName() {
		return inputName;
	}

	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public String getAjaxEnabledAndLocationIs() {
		return ajaxEnabledAndLocationIs;
	}

	public void setAjaxEnabledAndLocationIs(String ajaxEnabledAndLocationIs) {
		this.ajaxEnabledAndLocationIs = ajaxEnabledAndLocationIs;
	}

	public String getExtraAttributes() {
		return extraAttributes;
	}

	public void setExtraAttributes(String extraAttributes) {
		this.extraAttributes = extraAttributes;
	}

	public String getPaggingClickEnabled() {
		return paggingClickEnabled;
	}

	public void setPaggingClickEnabled(String paggingClickEnabled) {
		this.paggingClickEnabled = paggingClickEnabled;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}


	

}

