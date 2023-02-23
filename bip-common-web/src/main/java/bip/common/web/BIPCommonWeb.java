package bip.common.web;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.jsp.PageContext;

/**
 * Created by ramezani on 1/6/2020.
 */
public class BIPCommonWeb {
    public static void forwardMessage(HttpServletResponse response, String text)
    {
        try
        {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(text);
            writer.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static Object getAttributeAnyScope(PageContext pageContext, String name)
    {
        Object value = null;
        int scope = pageContext.getAttributesScope(name);
        if (scope > 0) {
            value = pageContext.getAttribute(name, scope);
        }
        return value;
    }

}
