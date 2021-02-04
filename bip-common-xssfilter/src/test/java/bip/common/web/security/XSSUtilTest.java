package bip.common.web.security;

import static org.junit.Assert.*;

public class XSSUtilTest {

    @org.junit.Test
    public void stripXSS() {
        XSSUtil x=new XSSUtil();
        String in = "{\"io\":12345}";
        String res = x.stripXSS(in);
        System.out.println("res = " + res+" , \nin ="+in);
    }
}