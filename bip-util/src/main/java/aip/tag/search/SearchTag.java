package aip.tag.search;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Vector;

/**
 *  Generated tag class.
 */
public class SearchTag extends BodyTagSupport {

    /**
     * property declaration for tag attribute: table.
     */    
    private java.lang.String table;    

    /**
     * property declaration for tag attribute: PK.
     */    
    private java.lang.String pk = "ID";
    
    /**
     * Holds value of property action.
     */
    private String action;

    private Vector searchColumns=new Vector();
    
    String formActionStart,formActionEnd;
    String tableStart,tableEnd;
    String script,scriptGetValue,scriptGetWhereClause;
    

    private String getResourceContent(String resname){
        //pageContext.getServletContext().getRealPath(".")+"}";
        String res="";
        try{
            java.io.InputStream in = pageContext.getServletContext().getResourceAsStream("/WEB-INF/classes/searchtag/res/"+resname);
            byte buf[]=new byte[in.available()];
            in.read(buf);
            in.close();
            res=new String(buf);
        }catch(Exception ex){
            res=resname+":"+ex.toString();
        }
        return res;
    }
    private void initRes(){
        if(formActionStart==null)formActionStart=getResourceContent("formactionstart.html");
        if(formActionEnd==null)formActionEnd=getResourceContent("formactionend.html");
        if(tableStart==null)tableStart=getResourceContent("tablestart.html");
        if(tableEnd==null)tableEnd=getResourceContent("tableend.html");
        //if(script==null)
            script=getResourceContent("script.html");
     }

    private void initFormAction(){
//        formAction="";
//        char buf[]=null;
//        try{
//            
//            java.io.FileWriter fw=new java.io.FileWriter("111111.html");
//            fw.write("Test");
//            fw.close();
//            
//            java.io.FileReader fr=new java.io.FileReader("frmAction.html");
//            int len=fr.read();
//            buf=new char[len];
//            fr.read(buf);
//            fr.close();
//        }catch(java.io.IOException ex){
//            formAction=ex.getMessage();
//        }
//        if(buf!=null)
//            formAction=new String(buf);
        formActionStart="\n"+
            "<form id='frmAction' action='@ACTION@' method='post' accept-charset='ISO-8859-1'> \n"+
            "    <input type=hidden id='txtTableName' name='TableName' value='@TABLENAME@'/> \n"+
            "    <input type=hidden id='txtWhereClause' name='WhereClause'/> \n"+
            "    <script type='text/javascript' language='javascript'> \n"+
            "        <!-- \n"+
            "        function search(){ \n"+
	    "		var txtClause=document.getElementById('txtWhereClause'); \n"+
	    "	        txtClause.value=getWhereClause(); \n"+
            "	        //alert(txtClause.value); \n"+
            "           var frm=document.getElementById('frmAction'); \n"+
            "	        frm.submit(); \n"+
            "        } \n"+
            "        //--> \n"+
            "    </script> \n";
        formActionEnd="</form> \n";

    }
    private void initTable(){
            tableStart=" \n"+
            "<table border=1> \n"+
            "<tr> \n"+
            //"<td> Ã¤ÃÃ£ ÃÃ¤ÂÃ¡Ã­ÃÃ­</td> \n"+
            "<td>Ã¤ÃÃ£ ÃÃÃÃÃ­</td> \n"+
            "<td>Ã£ÃÃÃÃ 1</td> \n"+
            "<td>Ã£ÃÃÃÃ 2</td> \n"+
            "<td>ÃÃÃ­Ã¥/Ã£ÃÃÃ¦Ã­</td> \n"+
            "<td>Ã£ÃÃÃ­Ã¤</td> \n"+
            "</tr> \n"; 
            tableEnd="</table>";

            tableEnd+="This is a Test2";
/*            try{
                java.io.File f=new java.io.File (".");
                //java.net.URL u=new java.net.URL("");
                tableEnd+="("+f.toString()+")"+f.getAbsolutePath();
                java.io.FileInputStream in=new java.io.FileInputStream("test.html");
                byte buf[]=new byte[in.available()];
                in.read(buf);
                tableEnd+=new String(buf);
            }catch(Exception ex){
                tableEnd+=ex.toString();
            }
*/
/*            try{
                pageContext.include("default.jsp");
            }catch(Exception ex){
                tableEnd+=ex.toString();
            }
 */
            //

    }
    private void initScript(){
//		var items=new Array();
//		items=['Name','No'];
        scriptGetValue="\n"+
	"function getValue(name){ \n"+
        //" return name;"+
	"	var sValue1=document.getElementById('txt'+name+'1').value; \n"+
	"	var sValue2=document.getElementById('txt'+name+'2').value; \n"+
	"	var iEqualLike=document.getElementById('cmbEL'+name).selectedIndex; \n"+
	"	var iBetweenOR=document.getElementById('cmbBO'+name).selectedIndex; \n"+
	"	var sEqualLike = (iEqualLike==0?' LIKE ':' = ') \n"+
	"	var sLikeEx = (iEqualLike==0?'%':'') \n"+
	"	var res=''; \n"+
	"	//!!!!IMPERSONATION!!!!! \n"+
	"	if(sValue1!='' && sValue2!=''){ \n"+
	"		if(iBetweenOR==0){ //OR \n"+
	"			res=\" (\"+name+sEqualLike+\"'\"+sLikeEx+sValue1+sLikeEx+\"' OR \"+name+sEqualLike+\" '\"+sLikeEx+sValue2+sLikeEx+\"') \"; \n"+
	"		}else{//Between \n"+
	"			res=\" (\"+name+\" BETWEEN '\"+sValue1+\"' AND '\"+sValue2+\"') \"; \n"+
	"		} \n"+
	"	}else if(sValue1!='' || sValue2!=''){ \n"+
	"		res=\" (\"+name+sEqualLike+\"'\"+sLikeEx+(sValue1!=''?sValue1:sValue2)+sLikeEx+\"') \"; \n"+
	"	} \n"+
	"	return res; \n"+
        "} \n";
        scriptGetWhereClause="\n"+
	"function getWhereClause() { \n"+
	"	var ret,res,i; \n"+
	"	res=''; \n"+
	"	for(i=0;i<items.length;i++){ \n"+
	"		ret=getValue(items[i]); \n"+
	"		res+=((res!='' && ret!='')?' AND ':'')+ret; \n"+
	"	} \n"+
	"	// alert(res); \n"+
        "       return res; \n"+
	"} \n";
        script="\n<script language=javascript>\n"+
            "<!--\n"+
            "   var items=new Array(); \n"+
            "   items=[@ITEMS@]; \n"+
            scriptGetValue+"\n" +
            scriptGetWhereClause+"\n" +
            "//-->\n"+
            "</script>\n";
    }
    
    private void init(){
        initFormAction();
        initTable();
        initScript();
    }
    
    public SearchTag() {
        super();
        //init();
    }
    
    
    ////////////////////////////////////////////////////////////////
    ///                                                          ///
    ///   User methods.                                          ///
    ///                                                          ///
    ///   Modify these methods to customize your tag handler.    ///
    ///                                                          ///
    ////////////////////////////////////////////////////////////////
    
    
    //
    // methods called from doStartTag()
    //
    /**
     *  
     * Fill in this method to perform other operations from doStartTag().
     * 
     */
    public void otherDoStartTagOperations()  {
    
        //
        // TODO: code that performs other operations in doStartTag
        //       should be placed here.
        //       It will be called after initializing variables, 
        //       finding the parent, setting IDREFs, etc, and 
        //       before calling theBodyShouldBeEvaluated(). 
        //
        //       For example, to print something out to the JSP, use the following:
        //
        //   try {
        //       JspWriter out = pageContext.getOut();
        //       out.println("something");
        //   } catch (java.io.IOException ex) {
        //       // do something
        //   }
        //
        //
        

    }
    
    /**
     *  
     * Fill in this method to determine if the tag body should be evaluated
     * Called from doStartTag().
     * 
     */
    public boolean theBodyShouldBeEvaluated()  {

        //
        // TODO: code that determines whether the body should be
        //       evaluated should be placed here.
        //       Called from the doStartTag() method.
        //
        return true;

    }
    
    
    //
    // methods called from doEndTag()
    //
    /**
     *  
     * Fill in this method to perform other operations from doEndTag().
     * 
     */
    public void otherDoEndTagOperations()  {
    
        //
        // TODO: code that performs other operations in doEndTag
        //       should be placed here.
        //       It will be called after initializing variables, 
        //       finding the parent, setting IDREFs, etc, and 
        //       before calling shouldEvaluateRestOfPageAfterEndTag(). 
        //
        

    }
    
    /**
     *  
     * Fill in this method to determine if the rest of the JSP page
     * should be generated after this tag is finished.
     * Called from doEndTag().
     * 
     */
    public boolean shouldEvaluateRestOfPageAfterEndTag()  {

        //
        // TODO: code that determines whether the rest of the page
        //       should be evaluated after the tag is processed
        //       should be placed here.
        //       Called from the doEndTag() method.
        ////////////
        
        return true;

    }
    
    
    ////////////////////////////////////////////////////////////////
    ///                                                          ///
    ///   Tag Handler interface methods.                         ///
    ///                                                          ///
    ///   Do not modify these methods; instead, modify the       ///
    ///   methods that they call.                                ///
    ///                                                          ///
    ////////////////////////////////////////////////////////////////
    
    
    /**
     * .
     *
     * This method is called when the JSP engine encounters the start tag,
     * after the attributes are processed.
     * Scripting variables (if any) have their values set here.
     * @return EVAL_BODY_INCLUDE if the JSP engine should evaluate the tag body, otherwise return SKIP_BODY.
     * This method is automatically generated. Do not modify this method.
     * Instead, modify the methods that this method calls.
     */
    public int doStartTag() throws JspException {
        otherDoStartTagOperations();
        initRes();
         try{             
            pageContext.setAttribute("searchColumns",searchColumns);
            
            //pageContext.getOut().write(tableStart.replaceFirst("@DIR@",(dir==null?"":"dir="+dir)));            
            String _action=formActionStart.replaceFirst("@TABLENAME@",getTable());
            _action=_action.replaceFirst("@ACTION@",getAction());
            _action=_action.replaceFirst("@OTHERINPUTS@","");
            pageContext.getOut().write(_action);

            pageContext.getOut().write(tableStart);
        }catch(Exception ex){
            ex.printStackTrace();
        }

        
        if (theBodyShouldBeEvaluated()) {
            return EVAL_BODY_BUFFERED;
        } else {
            return SKIP_BODY;
        }
    }
    
    /**
     * .
     *
     *
     * This method is called after the JSP engine finished processing the tag.
     * @return EVAL_PAGE if the JSP engine should continue evaluating the JSP page, otherwise return SKIP_PAGE.
     * This method is automatically generated. Do not modify this method.
     * Instead, modify the methods that this method calls.
     */
    public int doEndTag() throws JspException {
        otherDoEndTagOperations();


         try{
            pageContext.getOut().write(tableEnd);
            pageContext.getOut().write(formActionEnd);
            
            String _items="";
            String _name="";
            int n=searchColumns.size();
            for(int i=0;i<n;i++){
                _name=searchColumns.get(i).toString();
                _items+="'"+_name+"'"+(i==n-1?"":",");
                //_otherInputs+="'<input type=text id=txt"+_name+" name='"+_name+"'>\n";
            }

            pageContext.getOut().write(script.replaceFirst("@ITEMS@",_items));
            //pageContext.getOut().write("<INPUT id='Button1' type='button' value='Button' name='Button1' language=javascript onclick='return getWhereClause()'>");

            searchColumns.removeAllElements();
        }catch(Exception ex){
            ex.printStackTrace();
        }

        if (shouldEvaluateRestOfPageAfterEndTag()) {
            return EVAL_PAGE;
        } else {
            return SKIP_PAGE;
        }
    }
    
    
    public java.lang.String getTable() {
        return table;
    }
    
    public void setTable(java.lang.String value) {
        table = value;
    }
    
    public java.lang.String getPk() {
        return pk;
    }
    
    public void setPk(java.lang.String value) {
        pk = value;
    }
    
    /**
     * .
     * Fill in this method to process the body content of the tag.
     * You only need to do this if the tag's BodyContent property
     * is set to "JSP" or "tagdependent."
     * If the tag's bodyContent is set to "empty," then this method
     * will not be called.
     */    
    public void writeTagBodyContent(JspWriter out, BodyContent bodyContent) throws IOException {
        //
        // TODO: insert code to write html before writing the body content.
        //       e.g.  out.println("<B>" + getAttribute1() + "</B>");
        //             out.println("   <BLOCKQUOTE>");
        
        //
        // write the body content (after processing by the JSP engine) on the output Writer
        //
        bodyContent.writeOut(out);
        
        //
        // Or else get the body content as a string and process it, e.g.:
        //     String bodyStr = bodyContent.getString();
        //     String result = yourProcessingMethod(bodyStr);
        //     out.println(result);
        //
        
        // TODO: insert code to write html after writing the body content.
        //       e.g.  out.println("   <BLOCKQUOTE>");
        
        // clear the body content for the next time through.
        bodyContent.clearBody();
    }
    
    /**
     * .
     *
     * Handles exception from processing the body content.
     */    
    public void handleBodyContentException(Exception ex) throws JspException {
        // Since the doAfterBody method is guarded, place exception handing code here.
        throw new JspException("error in SearchTag: " + ex);
    }
    
    /**
     * .
     *
     *
     * This method is called after the JSP engine processes the body content of the tag.
     * @return EVAL_BODY_AGAIN if the JSP engine should evaluate the tag body again, otherwise return SKIP_BODY.
     * This method is automatically generated. Do not modify this method.
     * Instead, modify the methods that this method calls.
     */    
    public int doAfterBody() throws JspException {
        try {
            //
            // This code is generated for tags whose bodyContent is "JSP"
            //
            BodyContent bodyContent = getBodyContent();
            JspWriter out = bodyContent.getEnclosingWriter();
            
            writeTagBodyContent(out, bodyContent);
        } catch (Exception ex) {
            handleBodyContentException(ex);
        }
        
        if (theBodyShouldBeEvaluatedAgain()) {
            return EVAL_BODY_AGAIN;
        } else {
            return SKIP_BODY;
        }
    }
    
    /**
     *
     * Fill in this method to determine if the tag body should be evaluated
     * again after evaluating the body.
     * Use this method to create an iterating tag.
     * Called from doAfterBody().
     *
     */    
    public boolean theBodyShouldBeEvaluatedAgain() {
        //
        // TODO: code that determines whether the tag body should be
        //       evaluated again after processing the tag
        //       should be placed here.
        //       You can use this method to create iterating tags.
        //       Called from the doAfterBody() method.
        //
        return false;
    }
    
    /**
     * Getter for property action.
     * @return Value of property action.
     */
    public String getAction() {
        return this.action;
    }    
    
    /**
     * Setter for property action.
     * @param action New value of property action.
     */
    public void setAction(String action) {
        this.action = action;
    }    
    
}
