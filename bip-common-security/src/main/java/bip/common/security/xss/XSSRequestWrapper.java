package bip.common.security.xss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;

public class XSSRequestWrapper extends HttpServletRequestWrapper {
    Logger LOG = LoggerFactory.getLogger(XSSFilter.class);

//    private String cachedInputStream=null;
    private byte[] cachedInputStream=null;
    boolean isUploadData = false;//Determine if upload ignores
    XSSUtil xssUtil = new XSSUtil();


    public XSSRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
        String contentType = servletRequest.getContentType();
        if (null != contentType)
            isUploadData =contentType.startsWith ("multipart");
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);

        if (values == null) {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            //encodedValues[i] = stripXSS(values[i]);
            encodedValues[i] = xssUtil.stripXSS(values[i]);
        }

        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);

        return xssUtil.stripXSS(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return xssUtil.stripXSS(value);
    }

    @Override
    public Object getAttribute(String name) {
        Object value = super.getAttribute(name);
        if (null != value && value instanceof String) {
            value = xssUtil.stripXSS((String) value);
        }
        return value;
    }

    @Override
    public int getContentLength() {
        try {
            getInputStream();
        } catch (IOException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
        int length =0;
        if(cachedInputStream!=null){
            length=cachedInputStream.length;
        }
        LOG.info("getContentLength="+length);
        return length;
    }

    @Override
    public ServletInputStream getInputStream () throws IOException {
        LOG.info("getInputStream:isUploadData="+isUploadData);
        if (isUploadData){
            return super.getInputStream ();
        }else {
            if (cachedInputStream == null) {
//                cachedInputStream = inputHandlers(super.getInputStream());
                byte[] buf = readStream(super.getInputStream(),super.getContentLength());
                String originalInputStream  = new String(buf,"UTF-8");
                LOG.debug("inputStream-original="+originalInputStream);
                String cleanedInputStream = xssUtil.stripXSS(originalInputStream);
                //String cleanedInputStream = originalInputStream;
                LOG.debug("cachedInputStream-cleaned="+cleanedInputStream);
                cachedInputStream = cleanedInputStream.getBytes();
            }

            final ByteArrayInputStream bais = cachedInputStream==null?null: new ByteArrayInputStream(cachedInputStream);

                return new ServletInputStream() {
                    @Override
                    public int read() throws IOException {
                        if(bais==null)return -1;
                        return bais.read();
                    }
//
//                    public boolean isFinished() {
//                        return false;
//                    }
//
//                    public boolean isReady() {
//                        return false;
//                    }

                    //public void setReadListener(ReadListener readListener) { }
                };
        }

    }
    public   String inputHandlers1(InputStream servletInputStream){
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(servletInputStream, Charset.forName("UTF-8")));
            String line = "";
            //if(reader.ready()){
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
//            }else{
//                return null;
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (servletInputStream != null) {
                try {
                    servletInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String originalInputStream  = sb.toString ();
        LOG.debug("inputStream-original="+originalInputStream);
        String cleanedInputStream = xssUtil.stripXSS(originalInputStream);
        return cleanedInputStream;
    }

    public byte[] readStream(InputStream in,int contentLength) {
        if(contentLength>0){
            byte[] buf = new byte[contentLength];
            try {
                in.read(buf);
            }catch(Exception ex){
                ex.printStackTrace();
            }finally{
                try{in.close();}catch(Exception e) {}
            }
            return buf;
        }else{
            return null;
        }
    }

}