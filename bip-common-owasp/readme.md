ESAPIWebApplicationFirewallFilter (WAF)
---


This is the main class for the ESAPI Web Application Firewall (WAF). It is a standard J2EE servlet filter that, in different methods, invokes the reading of the configuration file and handles the runtime processing and enforcing of the developer-specified rules. Ideally the filter should be configured to catch all requests (/*) in web.xml. If there are URL segments that need to be extremely fast and don't require any protection, the pattern may be modified with extreme caution.


Configuring BadInputFilter
---
```xml 
<filter>
     <filter-name>BadInputFilter</filter-name>
     <filter-class>bip.common.owasp.filter.BadInputFilter</filter-class>
     <init-param>
       <param-name>deny</param-name>
       <param-value>\x00,\x04,\x08,\x0a,\x0d</param-value>
     </init-param>
     <init-param>
       <param-name>escapeQuotes</param-name>
       <param-value>true</param-value>
     </init-param>
     <init-param>
       <param-name>escapeAngleBrackets</param-name>
       <param-value>true</param-value>
     </init-param>
     <init-param>
       <param-name>escapeJavaScript</param-name>
       <param-value>true</param-value>
     </init-param>
   </filter>
   <filter-mapping>
     <filter-name>BadInputFilter</filter-name>
     <url-pattern>/*</url-pattern>
   </filter-mapping>
```
Read more: http://mrbool.com/restoring-security-in-tomcat-with-badinputfilter/34072#ixzz67DQoOEKx


Configuring BadInputValve
---





Configuring ESAPI for use with a Java Web Application (Java 1.4)
---

First thing's first: I would strongly advice against applying ESAPI to your web application if you are in production or even at the final stages of testing. Doing so will render all tests mute and you will have to re test the application from the ground up. Securing a web application should be one of the first things to consider, not the last.
ESAPI (The OWASP Enterprise Security API) is a free, open source, web application security control library that makes it easier for programmers to write lower-risk applications. The ESAPI libraries are designed to make it easier for programmers to retrofit security into existing applications or build a solid foundation for new development. If you can figure out these libraries that is :-). If you Google ESAPI Sample Code you wont find much and the documentation provided in ESAPI for Java is incomplete. They do have some general guidelines here: ESAPI Secure Coding Guidelines
So the best way to go is to download the source code from http://owasp-esapi-java.googlecode.com/svn/ and try to figure out from the very thin documentation and some reference implementation classes what to do. Here are the steps that I took to apply the ESAPI libraries to an **existing** web application.

    For Java 1.4, download code (checkout from svn) from http://owasp-esapi-java.googlecode.com/svn/tags/releases/1.4.0
    Add owasp-esapi-full-java-1.4.jar and antisamy-bin.1.2.jar to your project classpath.
    Note: From http://code.google.com/p/owaspantisamy/: AntiSamy is a collection of APIs for safely allowing users to supply their own HTML and CSS without exposing the site to XSS vulnerabilities.
    Create a ESAPI.properties file in the root source directory of your web application. Do not place it in a package inside the root source directory because the DefaultSecurityConfiguration will not find it.
    Download/Copy the antisamy-esapi.xml file in the root source directory of your web application.
    Next step is to implement interfaces org.owasp.esapi.User and org.owasp.esapi.Authenticator This is because the default reference implementations use a File as the user database. So you will need to create your own, unless of course you store users in a text file. For our purposes, we store user information in a database table.

    Note that this is not necessary, unless you want to use the ESAPI API for user authentication and authorization. In addition, the org.owasp.esapi.User object carries with it some interesting methods to set and verify CSRF tokens.
    In the context initializer of your web app, call

    ESAPI.setAuthenticator( new MyUserAuthenticator() );

    This will load the properties and print them in the console output, and then set the system Authenticator.
    Next step is to write and configure an http filter class for your application. Good reference implementation are the java classes in the org.owasp.esapi.filters package. Here's an example that implements the doFilter method of the javax.servlet.Filter:

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
                      throws IOException, ServletException {

            if (!(request instanceof HttpServletRequest)) {
                chain.doFilter(request, response);
                return;
            }

            HttpServletRequest hrequest = (HttpServletRequest)request;
            HttpServletResponse hresponse = (HttpServletResponse)response;

            // this is necessary on every call
            ESAPI.httpUtilities().setCurrentHTTP(hrequest, hresponse);
            
            // doFilter by wrapping the request and the response to the 
            // ESAPI safe HttpServletRequest and  HttpServletResponse  
            chain.doFilter(new SafeRequest(hrequest), new SafeResponse(hresponse));

    }

    Another good example of an ESAPI filter is  org.owasp.esapi.filters.ESAPIFilter.java
    You need to be careful with the url-pattern of the ESAPIFilter filter. If you use /* then all http requests, including requests for images, css files and JavaScript files included on a page will pass through the filter and probably fail if you have a call to
    ESAPI.authenticator().login(request, response);. The recommendation here is to create a separate directory for your protected JSPs and put them in that directory. Anything else, like JavaScript files, css files and images should go into another folder and not have their requests pass through the filter. Don't forget to also specify url patters of protected servlets that include the secured directory.
    Deploy and run or debug your web application. You should see the ESAPI properties printed on the console. If you see an error that ESAPI cannot load properties, make sure that the ESAPI properties file resides in the source of your web application code (i.e., in WEB-INF/classes)
    You may need to modify the ESAPI validator properties. For example, if your application user interface is non-English, then the Validator.HTTPParameterValue pattern will not do:

    Validator.HTTPParameterValue=^[a-zA-Z0-9.\\-\\/+=_ ]*$

    The above will cause ESAPI to throw an IntrusionDetector exception if any of your html fields contain non-English characters. We had to change the above to:

    Validator.HTTPParameterValue=^[\p{L}\p{Nd}0-9.\\-\\/+=_ ]$

    to allow Unicode characters. See below for a list of all ESAPI.properties we had to change and why.
    Depending on your application needs, you may need to modify other validation patterns in the ESAPI.properties file. But keep these changes to a minimum. The guys that wrote this code knew what they were doing.
    And lastly, the hard part: Test, test and then test again. As mentioned above, the Validator.* regular expression patterns defined in ESAPI.properties may cause validation exceptions to be thrown. For example, the default cookie name validation pattern does not allow for dots in the cookie name, but in our case the application server was actually setting a cookie with a dot in the name.

List of ESAPI.properties that you may need to change if running on Oracle Application Server 10g

    Validator.HTTPCookieName: OAS 10g sets a cookie with name "oracle.uix" even if you do not use Oracle UIX. The HTTPCookieName pattern was changed to

    Validator.HTTPCookieName=^[a-zA-Z0-9.\\-_]{0,32}$ 

    Validator.HTTPCookieValue: The "oracle.uix" cookie value in our case was 0^^GMT+2:00 The pattern was changed to

    Validator.HTTPCookieValue=^[a-zA-Z0-9:.\\^\\-\\/+=_ ]*$ 

    Validator.HTTPParameterName: The default ESAPI.properties file allows for a maximum of 32 characters. We changed this to allow for 50:

    Validator.HTTPParameterName=^[a-zA-Z0-9_]{0,50}$ 

