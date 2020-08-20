package aip.tag.search;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.sql.*;
import java.util.Vector;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *  Generated tag class.
 */
public class ListTag extends BodyTagSupport {

    /**
     * property declaration for tag attribute: query.
     */
    private java.lang.String query;    
    private Vector columns=new Vector();

    /**
     * property declaration for tag attribute: cookies.
     */    
    //private javax.servlet.http.Cookie[] cookies;
    
    public ListTag() {
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
        
        pageContext.setAttribute("Columns",columns);

         java.nio.charset.Charset cs;
         java.nio.charset.CharsetEncoder ce;
         java.text.StringCharacterIterator s;
         
         try{
             //String _st=pageContext.getRequest().getParameter("WhereClause");
             //pageContext.getOut().write(_st);
    
             String _table=pageContext.getRequest().getParameter("TableName").compareTo("")==0?"": pageContext.getRequest().getParameter("TableName");
             //String _whereClause=pageContext.getRequest().getParameter("WhereClause").compareTo("")==0?"":" WHERE "+pageContext.getRequest().getParameter("WhereClause");
             String value= new String(pageContext.getRequest().getParameter("WhereClause").getBytes("ISO-8859-1"), "UTF-8");
             String _whereClause=pageContext.getRequest().getParameter("WhereClause").compareTo("")==0?"":" WHERE "+value;
             String _query=null;
             if(query==null || query.compareTo("")==0){
                 _query="select * from "+_table;
             }else{
                  _query=query;//+_whereClause;
             }
             //pageContext.getOut().write(_query);
             setQuery(_query+_whereClause);
             
//            pageContext.getOut().write(query);
//            pageContext.getOut().write("<table border=1>");
//            pageContext.getOut().write("\n<tr>\n");
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
        
         try{
             
//            pageContext.getOut().write("\n<tr>\n");
//             for(int i=0;i<columns.size();i++){
//                pageContext.getOut().write("<td>"+columns.get(i).toString()+"</td>");
//             }
//            pageContext.getOut().write("\n</tr>\n");
            try{
                filltable();
            }catch(Exception ee){
                ee.printStackTrace();
            }
//            pageContext.getOut().write("</tr>");
//            pageContext.getOut().write("</table>");
            
            setQuery(null);
            columns.removeAllElements();
            
        }catch(Exception ex){
            ex.printStackTrace();
        }

        
        if (shouldEvaluateRestOfPageAfterEndTag()) {
            return EVAL_PAGE;
        } else {
            return SKIP_PAGE;
        }
    }
    public void filltable(){
        Connection cn = null;
        Statement stmt = null;
        ResultSet rs = null;
        StringBuffer sb=new StringBuffer();
        try{
            //?????
            //pageContext.getOut().write(getQuery());
            
            String _dbDriver = pageContext.getAttribute("DBDriver",pageContext.SESSION_SCOPE).toString();
            String _dbConn = pageContext.getAttribute("DBConnectionString",pageContext.SESSION_SCOPE).toString();
            String _dbUser = pageContext.getAttribute("DBUser",pageContext.SESSION_SCOPE).toString();
            String _dbPass = pageContext.getAttribute("DBPassword",pageContext.SESSION_SCOPE).toString();
            Class.forName(_dbDriver);//"oracle.jdbc.driver.OracleDriver");
            cn = DriverManager.getConnection(_dbConn,_dbUser,_dbPass);//"jdbc:oracle:thin:@localhost:1521:db", "bts", "123");
                //it should removed
                //sb.append(getQuery()+"@\n");
            String query[]=getQuery().split(";");
            for(int j=0;j<query.length;j++){
                //it should removed
                //sb.append(query[j]);

                stmt = cn.createStatement();
                stmt.execute( query[j] );
                rs=stmt.getResultSet();
                if(rs!=null){
                    
                    int n = rs.getMetaData().getColumnCount();
                    String st;
                    int counter=1;
                    while(rs.next()){
                        sb.append("\n<tr>\n");
                        sb.append("\r\t<td>"+String.valueOf(counter++)+"</td>");
                        int pos;
                        for(int i=0;i<columns.size();i++){
                            sb.append("\r\t<td>");
                            try{
                                pos = rs.findColumn(columns.get(i).toString());
                                if(pos>0 && rs.getObject(pos)!=null)
                                    sb.append(rs.getObject(pos));
                            }catch(Exception ex2){
                                sb.append("<font color='Red'>"+ex2.getMessage()+"</font>");
                            }
                            sb.append("</td>");
                        }
                        sb.append("\n</tr>\n");

        //                if(cookies!=null){
        //                    for(int i=0;i<cookies.length;i++)
        //                        sb.append(cookies[i].getName()+"="+cookies[i].getValue()+"<BR>");
        //                }
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
            sb.append("<tr>"+ex.toString()+"</tr>");
        }finally{
            try{
                if(rs!=null)rs.close();
                stmt.close();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
         try{
            pageContext.getOut().write(sb.toString());
        }catch(Exception ex){
            ex.printStackTrace();
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
        throw new JspException("error in ListTag: " + ex);
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
     * Setter for property query.
     * @param query New value of property query.
     */
    public void setQuery(String query) {
        this.query=query;
    }
    
    public java.lang.String getQuery() {
        return query;
    }
    
//    public javax.servlet.http.Cookie[] getCookies() {
//        return cookies;
//    }
//    
//    public void setCookies(javax.servlet.http.Cookie[] value) {
//        cookies=value;
//    }
    
}
