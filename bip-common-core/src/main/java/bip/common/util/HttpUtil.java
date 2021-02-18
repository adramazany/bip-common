package bip.common.util;

import aip.util.AIPException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtil {
    public static String httpGet(String urlStr)
            throws AIPException
    {
        return new HttpUtil().httpGet(urlStr, null, null);
    }

    public String httpGet(String urlStr, String username, String password)
            throws AIPException
    {
        BufferedReader in = null;
        try
        {
            URL url = new URL(urlStr);
            URLConnection cn = url.openConnection();
            in = new BufferedReader(new InputStreamReader(cn.getInputStream()));


            StringBuffer sb = new StringBuffer();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            return sb.toString();
        }
        catch (MalformedURLException e)
        {
            throw new AIPException(e);
        }
        catch (IOException e)
        {
            throw new AIPException(e);
        }
        finally
        {
            if (in != null) {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
