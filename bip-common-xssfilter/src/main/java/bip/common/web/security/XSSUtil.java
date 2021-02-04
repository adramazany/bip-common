package bip.common.web.security;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Whitelist;
import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class XSSUtil {
    Logger LOG = LoggerFactory.getLogger(XSSUtil.class);

    private static Pattern[] patterns = new Pattern[]{
            // Script fragments
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            // src='...'
            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // lonely script tags
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // eval(...)
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // expression(...)
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // javascript:...
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            // vbscript:...
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            // onload(...)=...
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };


    /**
     * Apply the XSS filter to the parameters
     * @param parameters
     * //@param type
     */
//    private void cleanParams( MultivaluedMap<String, String> parameters )
//    {
//        LOG.debug( "Checking for XSS Vulnerabilities: {}", parameters );
//
//        for( Map.Entry<String, List<String>> params : parameters.entrySet() )
//        {
//            String key = params.getKey();
//            List<String> values = params.getValue();
//
//            List<String> cleanValues = new ArrayList<String>();
//            for( String value : values )
//            {
//                cleanValues.add( stripXSS( value ) );
//            }
//
//            parameters.put( key, cleanValues );
//        }
//
//        LOG.debug( "XSS Vulnerabilities removed: {}", parameters );
//    }

    /**
     * Strips any potential XSS threats out of the value
     * @param value
     * @return
     */
    public String stripXSS( String value )
    {
        if( value == null )
            return null;

        // Use the ESAPI library to avoid encoded attacks.
        value = ESAPI.encoder().canonicalize( value );

        // Avoid null characters
        value = value.replaceAll("\0", "");

        // Clean out HTML
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode( Entities.EscapeMode.xhtml );
        outputSettings.prettyPrint( false );
        value = Jsoup.clean( value, "", Whitelist.none(), outputSettings );

//        // Remove all sections that match a pattern
//        for (Pattern scriptPattern : patterns){
//            value = scriptPattern.matcher(value).replaceAll("");
//        }


        return value;
    }

}
