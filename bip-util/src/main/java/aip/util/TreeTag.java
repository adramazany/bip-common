package aip.util;

import java.io.IOException;
import java.util.Random;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class TreeTag extends TagSupport {

    private int level = 0;              // The level of this node (REQUIRED) 
    private String style = "";          // Toogle icon style
    private String link = null;         // Link surrounding item text
    private String target = null;       // Frame in which to render link
    private String text = null;         // Item text              (REQUIRED)
    private String icon = null;         // Icon next to text
    private String browser = null;      // Browser agent string (need for outputing script)
    private boolean leaf = true;        // True if this is a leaf node
    private boolean nowrap = true;      // True if disable word wrap for the data cell
    private boolean script = false;     // Output the Javascript method first       
    private static String iconPath = "include/tree_menu/";  // default icons path
    
    
    // internal class stuff
    private static Random rand = null;  // used to give span's unique id's
      
    public void setLevel(String level) {
        this.level = (new Integer(level)).intValue();
    }
  
    public void setStyle(String style) {
        this.style = style;
    }
    
    public void setLink(String link) {
        this.link = link;
    }

	public void setTarget(String target) {
        this.target = target;
    }
    
    public void setText(String text) {
        this.text = text;
    }
  
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public void setLeaf(String leaf) {
        if (leaf.equalsIgnoreCase("true"))
            this.leaf = true;
        else
            this.leaf= false;
    }

    public void setNowrap(String nowrap) {
        if (nowrap.equalsIgnoreCase("true"))
            this.nowrap = true;
        else
            this.nowrap= false;
    }
    
    public void setScript(String script) {
        if (script.equalsIgnoreCase("true"))
            this.script = true;
        else
            this.script= false;
    }

    public void setIconPath(String path) {
        this.iconPath = path;
    }
    
    public int doStartTag() {
        JspWriter out = pageContext.getOut();
        return (doStartTag(out));
    }


    public int doStartTag(JspWriter out) {
        try {
            if (script) {          // need to output Javascript function once per page
                printScript(out);
                return (SKIP_BODY);
            }

            if (rand == null)     // lazy initialization
                rand = new Random();

            String name = Integer.toString(rand.nextInt());  // unique numeric id
        
            String img_closed, img_active;
            if (style.equalsIgnoreCase("WIN")) {
                img_closed = "plus.gif";
                img_active  = "minus.gif";
            } else if (style.equalsIgnoreCase("MAC")) {
                img_closed = "mac_closed_arrow.gif";
                img_active = "mac_open_arrow.gif"; 
            } else {
                img_closed = "plus_circle.gif";
                img_active = "minus_circle.gif"; 
            }
        
        
        	// DETERMINE DATA CELL(TD) HTML
            String tdStartHtml = "<td";
            String tdEndHtml = ">";
            String tdCloseHtml = "</td>";
            String nowrapHtml = "";
            if( nowrap == true ) nowrapHtml = " nowrap=\"1\"";			
            
            
            // BUILD TABLE
            out.println("<table border=\"0\" cellspacing=\"2\" cellpadding=\"0\"><tr>\n");
            String tdBasicHtml = tdStartHtml + nowrapHtml + " width=\"25\"" + tdEndHtml;
            for (int i=0; i<level; i++)
            	out.println( tdBasicHtml + "&nbsp;</td>\n" );
            if (leaf) 
            	out.println( tdBasicHtml + "&nbsp;</td>\n" );
            else {
            	out.println( tdBasicHtml + "<img id=\"img" + name + "\" src=\"" + iconPath + img_closed + "\" border=\"0\" " );
                out.println( "onClick=\"nodeClick(event, this, '" + name + "', '" + img_closed + "', '"+ img_active + "');\"></td>\n");  
            }

			
			// DETERMINE LINK(ANCHOR) HTML
			String anchorStartHtml = "";
            String anchorEndHtml = "";
            String anchorCloseHtml = "";
            String targetHtml = "";
            if( link != null ) 
            {
        		anchorStartHtml = "<a href=\"" + link + "\"";
        		anchorEndHtml = ">";
        		anchorCloseHtml = "</a>";        		
        		if( target != null ) targetHtml = " target=\"" + target + "\"";
            }
            String anchorHtml = anchorStartHtml + targetHtml + anchorEndHtml;            
			
            	
			// DISPLAY ICON
            if (icon != null) 
            {
            	// HJM:  CHANGED HERE, ORIGINAL BELOW
            	String tdIconHtml = tdStartHtml + nowrapHtml + "align=\"right\" width=\"25\"" + tdEndHtml;
            	String iconHtml = "<img src=\"" + iconPath  + icon + "\" border=\"0\" />";            	
            	
            	out.print( tdIconHtml + anchorHtml + iconHtml + anchorCloseHtml + tdCloseHtml );
            }            
       
            
            // DISPLAY TEXT
            String tdTextHtml = tdStartHtml + nowrapHtml + tdEndHtml;                     
            out.print( tdTextHtml + anchorHtml + text + anchorCloseHtml + tdCloseHtml );
            
            // END TABLE
            out.print( "\n</tr></table>\n" );
            
                    
            if (!leaf)   // this is an expandable node
                out.println( "<span id=\"span" + name + "\" class=\"clsHide\">\n");
        } catch(IOException ioe) {
        }
        return(EVAL_BODY_INCLUDE); // Include tag body
    }

    public int doEndTag() {
        JspWriter out = pageContext.getOut();
        return (doEndTag(out));
    }
    
    public int doEndTag(JspWriter out) {
        try {
            if (!leaf)
                out.print("</span>");
        } catch(IOException ioe) {
        }
        return(EVAL_PAGE); // Continue with rest of JSP page
    }

    /**
     *  Outputs the correct Javascript to generate dynamic tree views of listings
     */
    private void printScript(JspWriter out) throws IOException {
        out.println("<script language='JavaScript'>");
        out.println("function nodeClick( evt, eSrc, id, open, closed ) { ");
        // Netscape and Mozilla specific Javascript
        if ((browser != null) && (browser.indexOf("MSIE") < 0))  {
            out.println("evt.stopPropagation();");
            out.println("var eSpan = document.getElementById('span'+id);");
            out.println("eSpan.className = (eSpan.className=='clsShow') ? 'clsHide' : 'clsShow';");
            out.println("var eImg = document.getElementById('img'+id);");
        } else {  // Microsoft Internet Explorer
            out.println("window.event.cancelBubble = true;");
            out.println("var eSpan = document.all['span'+id];");
            out.println("eSpan.className = (eSpan.className=='clsShow') ? 'clsHide' : 'clsShow';");
            out.println("var eImg = document.all['img'+id];");
        }
        out.println("if( eSpan.className=='clsHide' ) {");
        out.println("eImg.src = ('"+iconPath+"' + open);");
        out.println("} else {");
        out.println("eImg.src = ('"+iconPath+"' + closed);");
        out.println("}");
        out.println("} </script>");
        // Now output the required CSS for hiding stuff
        out.println("<style type='text/css'>");
        out.println("   .clsShow { }");
        out.println("   .clsHide { display: none; }");
        out.println("</style>");
    }
}
