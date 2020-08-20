package aip.util;

import java.io.*; 
import java.util.*; 
import javax.servlet.*; 
import javax.servlet.http.*;

public class CharsetFilter implements Filter {

    private String encoding;

      public void init(FilterConfig config) throws ServletException {
          encoding = config.getInitParameter("requestEncoding"); 
          if( encoding==null ) 
        	 encoding=AIPUtil.getSystemCharset(); 
      }

      public void doFilter(ServletRequest request, ServletResponse response, FilterChain next) throws IOException, ServletException {
           // Respect the client-specified character encoding // (see HTTP specification section 3.4.1) 
    	  if(null == request.getCharacterEncoding())
                request.setCharacterEncoding(encoding); 
            next.doFilter(request, response); 
      } 
      public void destroy(){} 

	
}


/*
 * 
 * 
The Essentials of Filters
	  	
  	

The Java Servlet specification version 2.3 introduces a new component type, called a filter. A filter dynamically intercepts requests and responses to transform or use the information contained in the requests or responses. Filters typically do not themselves create responses, but instead provide universal functions that can be "attached" to any type of servlet or JSP page.

Filters are important for a number of reasons. First, they provide the ability to encapsulate recurring tasks in reusable units. Organized developers are constantly on the lookout for ways to modularize their code. Modular code is more manageable and documentable, is easier to debug, and if done well, can be reused in another setting.

Second, filters can be used to transform the response from a servlet or a JSP page. A common task for the web application is to format data sent back to the client. Increasingly the clients require formats (for example, WML) other than just HTML. To accommodate these clients, there is usually a strong component of transformation or filtering in a fully featured web application. Many servlet and JSP containers have introduced proprietary filter mechanisms, resulting in a gain for the developer that deploys on that container, but reducing the reusability of such code. With the introduction of filters as part of the Java Servlet specification, developers now have the opportunity to write reusable transformation components that are portable across containers.

Filters can perform many different types of functions. We'll discuss examples of the italicized items in this paper:

    *
    * Authentication-Blocking requests based on user identity.
    * Logging and auditing-Tracking users of a web application.
    * Image conversion-Scaling maps, and so on.
    * Data compression-Making downloads smaller.
    * Localization-Targeting the request and response to a particular locale.
    * XSL/T transformations of XML content-Targeting web application responses to more that one type of client.

These are just a few of the applications of filters. There are many more, such as encryption, tokenizing, triggering resource access events, mime-type chaining, and caching.

In this paper we'll first discuss how to program filters to perform the following types of tasks:

    *
    * Querying the request and acting accordingly
    * Blocking the request and response pair from passing any further.
    * Modifying the request headers and data. You do this by providing a customized version of the request.
    * Modifying the response headers and data. You do this by providing a customized version of the response.

We'll outline the filter API, and describe how to develop customized requests and responses.

Programming the filter is only half the job of using filters-you also need to configure how they are mapped to servlets when the application is deployed in a web container. This decoupling of programming and configuration is a prime benefit of the filter mechanism:

    *
    * You don't have to recompile anything to change the input or output of your web application. You just edit a text file or use a tool to change the configuration. For example, adding compression to a PDF download is just a matter of mapping a compression filter to the download servlet.
    * You can experiment with filters easily because they are so easy to configure.

The last section of this paper shows how to use the very flexible filter configuration mechanism. Once you have read this paper, you will be armed with the knowledge to implement your own filters and have a handy bag of tricks based on some common filter types.
Programming Filters

The filter API is defined by the Filter, FilterChain, and FilterConfig interfaces in the javax.servlet package. You define a filter by implementing the Filter interface. A filter chain, passed to a filter by the container, provides a mechanism for invoking a series of filters. A filter config contains initialization data.

The most important method in the Filter interface is the doFilter method, which is the heart of the filter. This method usually performs some of the following actions:

    *
    * Examines the request headers
    * Customizes the request object if it wishes to modify request headers or data or block the request entirely
    * Customizes the response object if it wishes to modify response headers or data
    * Invokes the next entity in the filter chain. If the current filter is the last filter in the chain that ends with the target servlet, the next entity is the resource at the end of the chain; otherwise, it is the next filter that was configured in the WAR. It invokes the next entity by calling the doFilter method on the chain object (passing in the request and response it was called with, or the wrapped versions it may have created). Alternatively, it can choose to block the request by not making the call to invoke the next entity. In the latter case, the filter is responsible for filling out the response.
    * Examines response headers after it has invoked the next filter in the chain
    * Throws an exception to indicate an error in processing

In addition to doFilter, you must implement the init and destroy methods. The in it method is called by the container when the filter is instantiated. If you wish to pass initialization parameters to the filter you retrieve them from the FilterConfig object passed to init.
Example: Logging Servlet Access

Now that you know what the main elements of the filter API are, let's take a look at a very simple filter that does not block requests, transform responses, or anything fancy-a good place to start learning the basic concepts of the API.

Consider web sites that track the number of users. To add this capability to an existing web application without changing any servlets you could use a logging filter.

HitCounterFilter increments and logs the value of a counter when a servlet is accessed. In the doFilter method, HitCounterFilter first retrieves the servlet context from the filter configuration object so that it can access the counter, which is stored as a context attribute. After the filter retrieves, increments, and writes the counter to a log, it invokes doFilter on the filter chain object passed into the original doFilter method. The elided code is discussed in Programming Customized Requests and Responses .

                         
    public final class HitCounterFilter implements Filter {
       private FilterConfig filterConfig = null;
       public void init(FilterConfig filterConfig) 
          throws ServletException {
          this.filterConfig = filterConfig;
       }
       public void destroy() {
          this.filterConfig = null;
       }
       public void doFilter(ServletRequest request,
          ServletResponse response, FilterChain chain) 
          throws IOException, ServletException {
          if (filterConfig == null)
             return;
          StringWriter sw = new StringWriter();
          PrintWriter writer = new PrintWriter(sw);
          Counter counter = (Counter)filterConfig.
             getServletContext().
             getAttribute("hitCounter");
          writer.println();
          writer.println("===============");
          writer.println("The number of hits is: " +
             counter.incCounter());
          writer.println("===============");

          // Log the resulting string
          writer.flush();
          filterConfig.getServletContext().
             log(sw.getBuffer().toString());
          ...
          chain.doFilter(request, wrapper);
          ...
       }
    }
      
                        

                      


Example: Modifying the Request Character Encoding

Currently, many browsers do not send character encoding information in the Content-Type header of an HTTP request. If an encoding has not been specified by the client request, the container uses a default encoding to parse request parameters. If the client hasn't set character encoding and the request parameters are encoded with a different encoding than the default, the parameters will be parsed incorrectly. You can use the method setCharacterEncoding in the ServletRequest interface to set the encoding. Since this method must be called prior to parsing any post data or reading any input from the request, this function is a prime application for filters.

Such a filter is contained in the examples distributed with the Tomcat 4.0 web container. The filter sets the character encoding from a filter initialization parameter. This filter could easily be extended to set the encoding based on characteristics of the incoming request, such as the values of the Accept-Language and User-Agent headers, or a value saved in the current user's session.

                         
    public void doFilter(ServletRequest request, 
       ServletResponse response, FilterChain chain) throws
       IOException, ServletException {
       String encoding = selectEncoding(request);
       if (encoding != null)
          request.setCharacterEncoding(encoding);
       chain.doFilter(request, response);
    }
    public void init(FilterConfig filterConfig) throws
       ServletException {
       this.filterConfig = filterConfig;
       this.encoding = filterConfig.getInitParameter("encoding");
    }
    protected String selectEncoding(ServletRequest request) {
       return (this.encoding);
    }
      
                        

                      


Programming Customized Requests and Responses

So far we have looked at some simple examples. Now let's get a bit more sophisticated and look at a filter that modifies the request from or response back to the client. There are many ways for a filter to modify a request or response. For example, a filter could add an attribute to the request or it could insert data in or otherwise transform the response.

A filter that modifies a response must usually capture the response before it is returned to the client. The way to do this is to pass the servlet that generates the response a stand-in stream. The stand-in stream prevents the servlet from closing the original response stream when it completes and allows the filter to modify the servlet's response.

In order to pass this stand-in stream to the servlet, the filter creates a response "wrapper" that overrides the getWriter or getOutputStream method to return this stand-in stream. The wrapper is passed to the doFilter method of the filter chain. Wrapper methods default to calling through to the wrapped request or response object. This approach follows the well-known Wrapper or Decorator pattern described in Design Patterns, Elements of Reusable Object-Oriented Software. The following sections describe how the hit counter filter described earlier and other types of filters use wrappers.

To override request methods, you wrap the request in an object that extends ServletRequestWrapper or HttpServletRequestWrapper . To override response methods, you wrap the response in an object that extends ServletResponseWrapper or HttpServletResponseWrapper .

The hit counter filter described in Programming Filters inserts the value of the counter into the response. The elided code from HitCounterFilter is:

                         
    PrintWriter out = response.getWriter();
    CharResponseWrapper wrapper = new CharResponseWrapper(
       (HttpServletResponse)response);
    chain.doFilter(request, wrapper);
    if(wrapper.getContentType().equals("text/html")) {
       CharArrayWriter caw = new CharArrayWriter();
       caw.write(wrapper.toString().substring(0,
          wrapper.toString().indexOf("</body>")-1));
       caw.write("<p>\nYou are visitor number 
       <font color='red'>" + counter.getCounter() + "</font>");
       caw.write("\n</body></html>");
       response.setContentLength(caw.toString().length());
       out.write(caw.toString());
    } else 
       out.write(wrapper.toString());
    out.close();
      
                        

                      


HitCounterFilter wraps the response in a CharResponseWrapper. CharResponseWrapper overrides the getWriter method to return a stand-in stream into which the servlet at the end of the filter chain writes its response. When chain.doFilter returns, HitCounterFilter retrieves the servlet's response from PrintWriter and writes it to a buffer if it is an HTML response. The filter inserts the value of the counter into the buffer, resets the content length header of the response, and finally writes the contents of the buffer to the response stream.

                         
    public class CharResponseWrapper extends
       HttpServletResponseWrapper {
       private CharArrayWriter output;
       public String toString() {
          return output.toString();
       }
       public CharResponseWrapper(HttpServletResponse response){
          super(response);
          output = new CharArrayWriter();
       }
       public PrintWriter getWriter(){
          return new PrintWriter(output);
       }
    }
      
                        

                      


Example: Compressing the Response

Another example of a filter that modifies the response is the compression filter contained in the examples distributed with the Tomcat servlet engine. Although high-speed Internet connections are becoming more commonplace, there is still a need to use bandwidth effectively. A compression filter is handy because you can attach it to any servlet to reduce the size of a response.

Like the hit counter filter, the compression filter creates a stand-in stream, in this case CompressionResponseStream, for the servlet to write to and wraps the response passed to the servlet.

The filter creates the wrapper and stand-in stream only if the client can accept a compressed response. The servlet writes its response to the compression stream it retrieves from the wrapper. CompressionResponseStream overrides the write method to write response data to a GZIPOutputStream once the data is larger than a threshold passed as an initialization parameter to the filter:

                         
    public void write(int b) throws IOException {
       ...
       if ((bufferCount >= buffer.length) || 
          (count>=compressionThreshold)) {
          compressionThresholdReached = true;
       }
       if (compressionThresholdReached) {
          writeToGZip(b);
       } else {
          buffer[bufferCount++] = (byte) b;
          count++;
       }
    }
      
                        

                      


Example: Transforming the Response

The last filter we'll discuss is an XSLT filter. XSLT is a language for transforming XML data. You can use XSLT to transform an XML document to end user-oriented formats such as HTML or PDF, or to another XML format. Some example applications include:

    *
    * Converting an XML document in a format required by one company to the format required by another company.
    * Customizing the look and feel of a web page based on user preferences.
    * Enabling the same web application to respond to different types of clients, for example, WML phones and cHTML phones, by looking at the User-Agent header and choosing a stylesheet.

Consider a web service that responds to requests for product inventory. The following XML document is an example of such a response:

                         
    <book>
       <isbn>123</isbn>
       <title>Web Servers for Fun and Profit</title>
       <quantity>10</quantity>
       <price>$17.95</price>
    </book>
      
                        

                      


The following XSL stylesheets render this XML document as a user-oriented description of the inventory in HTML format and as a machine-oriented version in XML format.

                         
    <?xml version="1.0" ?> 
    <xsl:stylesheet version="1.0" 
       xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" omit-xml-declaration="yes"/>
    <xsl:template match="/">
       <xsl:apply-templates/>
    </xsl:template>
                           
    <xsl:template match="book">
    <html>
    <body>There are <xsl:value-of select="quantity"/> copies of 
    <i><xsl:value-of select="title"/></i> available.
    </body>
    </html>
    </xsl:template>
    </xsl:stylesheet>
                          
                        

                         
    <?xml version="1.0" ?> 
    <xsl:stylesheet version="1.0" 
       xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" omit-xml-declaration="no"/>
    <xsl:template match="/">
       <xsl:apply-templates/>
    </xsl:template>
    <xsl:template match="book">
    <xsl:element name="book">
    <xsl:attribute name="isbn"><xsl:value-of select="isbn"/></
    xsl:attribute>
    <xsl:element name="quantity"><xsl:value-of select="quantity"/
    ></xsl:element>
    </xsl:element>
    </xsl:template>
    </xsl:stylesheet>
      
                        

                      


The following XSLT filter uses the stylesheets to transform the response depending on the value of a request parameter. The filter sets content type of the response according to the request parameter. The response is then wrapped in a CharResponseWrapper and passed to the doFilter method of the filter chain. The last element in the filter chain is a servlet that returns the inventory response described earlier. When doFilter returns, the filter retrieves the response data from the wrapper and transforms it using the stylesheet.

                         
    public void doFilter(ServletRequest request, 
       ServletResponse response, FilterChain chain)
       throws IOException, ServletException {
       String contentType;
       String styleSheet;
       String type = request.getParameter("type");
       if (type == null || type.equals("")) {
          contentType = "text/html";
          styleSheet = "/xml/html.xsl";
       } else {
          if (type.equals("xml")) {
             contentType = "text/plain";
             styleSheet = "/xml/xml.xsl";
          } else {
             contentType = "text/html";
             styleSheet = "/xml/html.xsl";
          }
       }
       response.setContentType(contentType);
       String stylepath=filterConfig.getServletContext().
          getRealPath(styleSheet);
       Source styleSource = new StreamSource(stylePath);

       PrintWriter out = response.getWriter();
       CharResponseWrapper responseWrapper = 
          new CharResponseWrapper(
             (HttpServletResponse)response);
       chain.doFilter(request, wrapper);
       // Get response from servlet
       StringReader sr = new StringReader(
          new String(wrapper.getData()));
       Source xmlSource = new StreamSource((Reader)sr);

       try {
          TransformerFactory transformerFactory =
             TransformerFactory.newInstance();
          Transformer transformer = transformerFactory.
             newTransformer(styleSource);
          CharArrayWriter caw = new CharArrayWriter();
          StreamResult result  = new StreamResult(caw);
          transformer.transform(xmlSource, result);
          response.setContentLength(caw.toString().length());
          out.write(caw.toString());
       } catch(Exception ex) {
          out.println(ex.toString());
          out.write(wrapper.toString());
       }
    }
      
                        

                      


Specifying Filter Configuration

Now that we have seen how to program a filter, the last step is to specify how to apply it to a web component or a set of web components. To map a filter to a servlet you:

    *
    * Declare the filter using the <filter> element in the web application deployment descriptor. This element creates a name for the filter and declares the filter's implementation class and initialization parameters.
    * Map the filter to a servlet by defining a <filter-mapping> element in the deployment descriptor. This element maps a filter name to a servlet by name or by URL pattern.

The following elements show how to specify the elements needed for the compression filter. To define the compression filter you provide a name for the filter, the class that implements the filter, and name and value of the threshold initialization parameter.

                         
    <filter>
       <filter-name>Compression Filter</filter-name>
       <filter-class>CompressionFilter</filter-class>
       <init-param>
          <param-name>compressionThreshold</param-name>
          <param-value>10</param-value>
       </init-param>
    </filter>
      
                        

                      


The filter-mapping element maps the compression filter to the servlet CompressionTest. The mapping could also have specified the URL pattern /CompressionTest. Note that the filter, filter-mapping, servlet, and servlet-mapping elements must appear in the web application deployment descriptor in that order.

                         
    <filter-mapping>
       <filter-name>Compression Filter</filter-name>
       <servlet-name>CompressionTest</servlet-name>
    </filter-mapping>
    <servlet>
       <servlet-name>CompressionTest</servlet-name>
       <servlet-class>CompressionTest</servlet-class>
    </servlet>
    <servlet-mapping>
       <servlet-name>CompressionTest</servlet-name>
       <url-pattern>/CompressionTest</url-pattern>
    </servlet-mapping>
      
                        

                      


Note that this mapping causes the filter to be called for all requests to the CompressionTest servlet and to any servlet JSP or static content mapped to the URL pattern /CompressionTest.

If you want to log every request to a web application, you would map the hit counter filter to the URL pattern /*. Here's the deployment descriptor distributed with the examples:

                         
    ?xml version="1.0" encoding="ISO-8859-1"?>

    <!DOCTYPE web-app PUBLIC "-//Sun Microsystems, Inc.//DTD Web 
    Application 2.3//EN" "http://java.sun.com/dtd/web-
    app_2_3.dtd">
    <web-app>
       <filter>
          <filter-name>XSLTFilter</filter-name>
          <filter-class>XSLTFilter</filter-class>
       </filter>
       <filter>
          <filter-name>HitCounterFilter</filter-name>
          <filter-class>HitCounterFilter</filter-class>
       </filter>
       <filter-mapping>
          <filter-name>HitCounterFilter</filter-name>
          <url-pattern>/*</url-pattern>
       </filter-mapping>  
       <filter-mapping>
          <filter-name>XSLTFilter</filter-name>
          <servlet-name>FilteredFileServlet</servlet-name>
       </filter-mapping>  
       <servlet>
          <servlet-name>FilteredFileServlet</servlet-name>
          <servlet-class>FileServlet</servlet-class>
       </servlet>
       <servlet-mapping>
          <servlet-name>FilteredFileServlet</servlet-name>
          <url-pattern>/ffs</url-pattern>
       </servlet-mapping>
    </web-app>
      
                        

                      


As you can see, you can map a filter to one or more servlets and you can map more than one filter to a servlet. This is illustrated in Figure 1, where filter F1is mapped to servlets S1, S2, and S3, filter F2 is mapped to servlet S2, and filter F3 is mapped to servlets S1 and S2.



Figure 1 Filter to Servlet Mapping

Recall that a filter chain is one of the objects passed to the doFilter method of a filter. This chain is formed indirectly via filter mappings. The order of the filters in the chain is the same as the order that filter mappings appear in the web application deployment descriptor.

When a URL is mapped to servlet S1, the web container invokes the doFilter method of F1. The doFilter method of each filter in S1's filter chain is invoked by the preceding filter in the chain via the chain.doFilter method. Since servlet S1's filter chain contains filters F1 and F3, F1's call to chain.doFilter invokes the doFilter method of filter F3. When F3's doFilter method completes, control returns to F1's doFilter method.

The deployment descriptor just discussed puts the hit counter and XSLT filter in the filter chain of FilteredFileServlet. The hit counter filter logs access whenever FilteredFileServlet is invoked, but inserts the value of the counter into the response after the XSLT transformation only if the response type is HTML:





Conclusion

The filter mechanism provides a way to encapsulate common functionality in a component that can reused in many different contexts. Filters are easy to write and configure as well as being portable and reusable. In summary, filters are an essential element in a web developer's toolkit.
Acknowledgments

The character encoding filter was developed by Craig McClanahan of Sun Microsystems, the compression filter was developed by Amy Roh of Sun Microsystems, and the XSLT filter was provided by Alan Canon of National Processing Company.
Resources

You can get the character encoding and compression filters by downloading Tomcat 4.0 . The character encoding filter is located in the TOMCAT_HOME/webapps/examples/WEB-INF/classes/filters directory. The compression filter is located in the TOMCAT_HOME/webapps/examples/WEB-INF/classes/compressionFilters directory. You can download a zip file containing the other filters described in this paper from here . To compile and run the XSLT filter, you'll need to obtain an XML parser and transformation engine from

                   
                     
                       
    http://java.sun.com/xml/downloads/
                                    
                  

                

For further information about the technologies described in this paper, see the following resources:

    *
    * http://java.sun.com/products/servlet
    * http://www.w3.org/XML
    * http://www.w3.org/Style/XSL
    * http://java.sun.com/xml

*/
