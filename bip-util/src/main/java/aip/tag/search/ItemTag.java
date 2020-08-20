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

/**
 *  Generated tag class.
 */
public class ItemTag extends BodyTagSupport {

    /**
     * property declaration for tag attribute: name.
     */    
    private java.lang.String name;    

    /**
     * property declaration for tag attribute: label.
     */    
    private java.lang.String label;
    
    /**
     * property declaration for tag attribute: type.
     */    
    private java.lang.String type;
    
    /**
     * property declaration for tag attribute: value1.
     */    
    private java.lang.String value1;
    
    /**
     * property declaration for tag attribute: value2.
     */    
    private java.lang.String value2;
    
    /**
     * property declaration for tag attribute: equalLike.
     */    
    private java.lang.String equalLike;
    
    /**
     * property declaration for tag attribute: betweenOR.
     */    
    private java.lang.String betweenOR;
    
    /**
     * property declaration for tag attribute: view.
     */    
    private java.lang.String view = "search+list";
    
    /**
     * property declaration for tag attribute: foreignkey.
     */    
    private java.lang.String foreignkey;
    
    /**
     * property declaration for tag attribute: relatedTable.
     */    
    private java.lang.String relatedTable;
    
    /**
     * property declaration for tag attribute: relatedPK.
     */    
    private java.lang.String relatedPK = "ID";
    
    public ItemTag() {
        super();
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
        //
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
    public int doStartTag() throws JspException, JspException {
        otherDoStartTagOperations();

        String value1="<INPUT type=text name=txt"+getName()+"1 value="+getValue1()+ " >";
        String value2="<INPUT type=text name=txt"+getName()+"2 value="+getValue2()+ " >";
        String equalLike="<SELECT name=cmbEL"+getName()+">"+
				"<OPTION value=Like selected>ÔÈ?å</OPTION>"+
				"<OPTION value=Equal>ãÓÇæ?</OPTION>"+
			"</SELECT>";
        String betweenOR="<SELECT name=cmbBO"+getName()+">"+
				"<OPTION value=Like selected>?Ç</OPTION>"+
				"<OPTION value=Equal>ãÇÈ?ä</OPTION>"+
			"</SELECT>";
        
        try{
            pageContext.getOut().write("<tr>");
            pageContext.getOut().write("<td>"+getName()+"</td>");
            pageContext.getOut().write("<td>"+getLabel()+"</td>");
            pageContext.getOut().write("<td>"+value1+"</td>");
            pageContext.getOut().write("<td>"+value2+"</td>");
            pageContext.getOut().write("<td>"+equalLike+"</td>");
            pageContext.getOut().write("<td>"+betweenOR+"</td>");
            pageContext.getOut().write("</tr>");
            	
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
    public int doEndTag() throws JspException, JspException {
        otherDoEndTagOperations();
        
        if (shouldEvaluateRestOfPageAfterEndTag()) {
            return EVAL_PAGE;
        } else {
            return SKIP_PAGE;
        }
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
        throw new JspException("error in ItemTag: " + ex);
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
    
    public java.lang.String getName() {
        return name==null?"":name;
    }
    
    public void setName(java.lang.String value) {
        name = value;
    }
    
    public java.lang.String getLabel() {
        return label==null?"":label;
    }
    
    public void setLabel(java.lang.String value) {
        label = value;
    }
    
    public java.lang.String getType() {
        return type==null?"":type;
    }
    
    public void setType(java.lang.String value) {
        type = value;
    }
    
    public java.lang.String getValue1() {
        return value1==null?"":value1;
    }
    
    public void setValue1(java.lang.String value) {
        value1 = value;
    }
    
    public java.lang.String getValue2() {
        return value2==null?"":value2;
    }
    
    public void setValue2(java.lang.String value) {
        value2 = value;
    }
    
    public java.lang.String getEqualLike() {
        return equalLike==null?"":equalLike;
    }
    
    public void setEqualLike(java.lang.String value) {
        equalLike = value;
    }
    
    public java.lang.String getBetweenOR() {
        return betweenOR==null?"":betweenOR;
    }
    
    public void setBetweenOR(java.lang.String value) {
        betweenOR = value;
    }
    
    public java.lang.String getView() {
        return view;
    }
    
    public void setView(java.lang.String value) {
        view = value;
    }
    
    public java.lang.String getForeignkey() {
        return foreignkey;
    }
    
    public void setForeignkey(java.lang.String value) {
        foreignkey = value;
    }
    
    public java.lang.String getRelatedTable() {
        return relatedTable;
    }
    
     /**
     * @deprecated
     * @see #getPreferredSize
     */
     public void setRelatedTable(java.lang.String value) {
        relatedTable = value;
    }
    
    public java.lang.String getRelatedPK() {
        return relatedPK;
    }
    
    public void setRelatedPK(java.lang.String value) {
        relatedPK = value;
    }
    
    public void doInitBody() throws javax.servlet.jsp.JspException {
        super.doInitBody();
    }
    
}
