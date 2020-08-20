package bip.common.util;

import java.lang.reflect.InvocationTargetException;
import java.sql.Clob;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class NVL {
    public static int getInt(Object obj, int defaultValue)
    {
        if ((obj != null) && (!"".equalsIgnoreCase(obj.toString()))) {
            try
            {
                defaultValue = Integer.parseInt(obj.toString());
            }
            catch (Exception ex)
            {
                defaultValue = (int)getDbl(obj, defaultValue);
            }
        }
        return defaultValue;
    }

    public static long getLng(Object obj)
    {
        return getLng(obj, 0L);
    }

    public static long getLng(Object obj, long defaultValue)
    {
        if ((obj != null) && (!"".equalsIgnoreCase(obj.toString()))) {
            try
            {
                defaultValue = Long.parseLong(obj.toString());
            }
            catch (Exception ex)
            {
                defaultValue = (int)getDbl(obj, defaultValue);
            }
        }
        return defaultValue;
    }

    public static int getInt(Object obj)
    {
        return getInt(obj, 0);
    }

    public static double getDbl(Object obj, double defaultValue)
    {
        if ((obj != null) && (!"".equalsIgnoreCase(obj.toString()))) {
            try
            {
                defaultValue = Double.parseDouble(obj.toString().replaceAll("[^-?.?0-9]+", ""));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return defaultValue;
    }

    public static double getDbl(Object obj)
    {
        return getDbl(obj, 0.0D);
    }

    public static int getIntUtf8(Object obj, int defaultValue)
    {
        if ((obj != null) && (!"".equalsIgnoreCase(obj.toString()))) {
            defaultValue = Integer.parseInt(UTF8.cnvUTF8(obj.toString()));
        }
        return defaultValue;
    }

    public static int getIntUtf8(Object obj)
    {
        return getIntUtf8(obj, 0);
    }

    public static double getDblUtf8(Object obj, double defaultValue)
    {
        if ((obj != null) && (!"".equalsIgnoreCase(obj.toString()))) {
            defaultValue = Double.parseDouble(UTF8.cnvUTF8(obj.toString()));
        }
        return defaultValue;
    }

    public static double getDblUtf8(Object obj)
    {
        return getDblUtf8(obj, 0.0D);
    }

    public static String getString(Object obj, String defaultValue)
    {
        if (obj != null) {
            defaultValue = obj.toString();
        }
        return defaultValue;
    }

    public static String getString(Object obj)
    {
        return getString(obj, "");
    }

    public static String getStringNull(String obj, String nullValue)
    {
        if ((obj != null) &&
                (obj.toString().trim().equalsIgnoreCase(nullValue.trim()))) {
            obj = null;
        }
        return obj;
    }

    public static String getStringNull(Object obj)
    {
        if (obj == null) {
            return (String)obj;
        }
        return getStringNull(obj.toString(), "");
    }

    public static String getStringNullNoTrim(String obj, String nullValue)
    {
        if ((obj != null) &&
                (obj.toString().equalsIgnoreCase(nullValue))) {
            obj = null;
        }
        return obj;
    }

    public static String getStringNullNoTrim(Object obj)
    {
        if (obj == null) {
            return (String)obj;
        }
        return getStringNullNoTrim(obj.toString(), "");
    }

    public static String getEmptyString(Object obj, String defaultValue)
    {
        if ((obj != null) && (!"".equals(obj.toString()))) {
            defaultValue = obj.toString();
        }
        return defaultValue;
    }

    public static String getStringContext(Object obj, String fieldName, String defaultValue)
    {
        if (obj != null)
        {
            Object res = null;
            try
            {
                res = BIPUtil.invokeMulti(obj, fieldName);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            if (res != null) {
                defaultValue = res.toString();
            }
        }
        return defaultValue;
    }

    public static String getStringContext(Object obj, String fieldName)
    {
        return getStringContext(obj, fieldName, "");
    }

    public static int getIntContext(Object obj, String fieldName, int defaultValue)
    {
        String st = getStringContext(obj, fieldName, "" + defaultValue);
        return Integer.parseInt(st);
    }

    public static int getIntContext(Object obj, String fieldName)
    {
        return getIntContext(obj, fieldName, 0);
    }

    public static Integer getInteger(Object obj, int nullValue)
    {
        int cur = getInt(obj, nullValue);
        if (cur == nullValue) {
            return null;
        }
        return new Integer(cur);
    }

    public static Integer getInteger(Object obj)
    {
        return getInteger(obj, 0);
    }

    public static Long getLong(Object obj, long nullValue)
    {
        long cur = getLng(obj, nullValue);
        if (cur == nullValue) {
            return null;
        }
        return new Long(cur);
    }

    public static Long getLong(Object obj)
    {
        return getLong(obj, 0L);
    }

    public static Double getDouble(Object obj, double nullValue)
    {
        double cur = getDbl(obj, nullValue);
        if (cur == nullValue) {
            return null;
        }
        return new Double(cur);
    }

    public static Double getDouble(Object obj)
    {
        return getDouble(obj, 0.0D);
    }

    public static Boolean getBln(Object obj)
    {
        Boolean res = Boolean.valueOf(false);
        if ((obj != null) && (!"".equalsIgnoreCase(obj.toString()))) {
            try
            {
                if (("true".equalsIgnoreCase(obj.toString())) || ("1".equalsIgnoreCase(obj.toString())) || ("yes".equalsIgnoreCase(obj.toString()))) {
                    res = Boolean.valueOf(true);
                } else if (("false".equalsIgnoreCase(obj.toString())) || ("0".equalsIgnoreCase(obj.toString())) || ("no".equalsIgnoreCase(obj.toString()))) {
                    res = Boolean.valueOf(false);
                }
            }
            catch (Exception ex)
            {
                obj = null;
            }
        }
        return res;
    }

    public static boolean getBool(Object obj, boolean defaultValue)
    {
        if ((obj != null) && (!"".equalsIgnoreCase(obj.toString()))) {
            try
            {
                defaultValue = Boolean.parseBoolean(obj.toString());
                if (("on".equalsIgnoreCase(obj.toString())) || ("true".equalsIgnoreCase(obj.toString())) || ("1".equalsIgnoreCase(obj.toString())) || ("yes".equalsIgnoreCase(obj.toString()))) {
                    defaultValue = true;
                }
            }
            catch (Exception ex) {}
        }
        return defaultValue;
    }

    public static boolean getBool(Object obj)
    {
        return getBool(obj, false);
    }

    public static Boolean getBoolean(Object obj, boolean nullValue)
    {
        if ((obj != null) && (!"".equalsIgnoreCase(obj.toString())))
        {
            boolean cur = false;
            try
            {
                cur = Boolean.parseBoolean(obj.toString());
                obj = new Boolean(cur);
            }
            catch (Exception ex)
            {
                obj = null;
            }
            if (cur == nullValue) {
                obj = null;
            }
        }
        return (Boolean)obj;
    }

    public static Boolean getBoolean(Object obj)
    {
        return getBoolean(obj, false);
    }

    public static String getStringQute(Object obj)
    {
        String defaultValue = "";
        if (obj != null) {
            try
            {
                defaultValue = "'" + obj.toString() + "'";
            }
            catch (Exception ex)
            {
                defaultValue = "null";
            }
        } else {
            defaultValue = "null";
        }
        return defaultValue;
    }

    public static String getIntegerNull(Object obj)
    {
        if ((obj != null) && (!"".equalsIgnoreCase(obj.toString())))
        {
            int cur = 0;
            try
            {
                cur = Integer.parseInt(obj.toString());
                if (cur == 0) {
                    return "null";
                }
                return "" + cur;
            }
            catch (Exception ex)
            {
                return "null";
            }
        }
        return "null";
    }

    public static String getLongNull(Object obj)
    {
        if ((obj != null) && (!"".equalsIgnoreCase(obj.toString())))
        {
            long cur = 0L;
            try
            {
                cur = Long.parseLong(obj.toString());
                if (cur == 0L) {
                    return "null";
                }
                return "" + cur;
            }
            catch (Exception ex)
            {
                return "null";
            }
        }
        return "null";
    }

    public static String getStringIsEmpty(Object obj, String defaultValue)
    {
        if (obj != null) {
            try
            {
                String a = obj.toString();
                if (!"".equals(a.trim())) {
                    defaultValue = a;
                }
            }
            catch (Exception ex) {}
        }
        return defaultValue;
    }

    public static String getStringFixLen(Object obj, int len)
    {
        return getStringFixLen(obj, len, ' ');
    }

    public static String getStringFixLen(Object obj, int len, char fillchar)
    {
        return getStringFixLen(obj, len, fillchar, false);
    }

    public static String getStringFixLen(Object obj, int len, char fillchar, boolean appendLeft)
    {
        String defaultValue = getString(obj);
        if (defaultValue.length() > len)
        {
            defaultValue = defaultValue.substring(0, len);
        }
        else if (defaultValue.length() < len)
        {
            int extra_len = len - defaultValue.length();
            char[] chs = new char[extra_len];
            for (int i = 0; i < chs.length; i++) {
                chs[i] = fillchar;
            }
            if (appendLeft) {
                defaultValue = new String(chs) + defaultValue;
            } else {
                defaultValue = defaultValue + new String(chs);
            }
        }
        return defaultValue;
    }

    public static String getDate(Date d, String pattern)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(d);
    }

    public static String getGregorianToday(String pattern)
    {
        return getDate(new Date(), pattern);
    }

    public static String getNowTime(String pattern)
    {
        return getDate(new Date(), pattern);
    }

    public static String getNowTime()
    {
        return getDate(new Date(), "HH:mm:ss");
    }

    public static boolean isEmpty(String st)
    {
        if ((st == null) || ("".equals(st.trim()))) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String st)
    {
        return !isEmpty(st);
    }

    public static String getIntFormat(Object obj, String format)
    {
        if (!isEmpty(format))
        {
            NumberFormat formatter = new DecimalFormat(format);
            return formatter.format(getLng(obj));
        }
        return getString(Long.valueOf(getLng(obj)));
    }

    public static int getDateInt(String obj, String defaultDate)
    {
        String st = obj;
        if (isEmpty(st)) {
            st = defaultDate;
        }
        int y = 0;int m = 0;int d = 0;
        String[] ar = st.split("/");
        y = getInt(ar[0]) * 10000;
        if (ar.length > 1) {
            m = getInt(ar[1]) * 100;
        }
        if (ar.length > 2) {
            d = getInt(ar[2]);
        }
        return y + m + d;
    }

    public static int getTimeInt(String obj, String defaultTime)
    {
        String st = obj;
        if (isEmpty(st)) {
            st = defaultTime;
        }
        String[] ar = st.split(":");
        int h = 0;int m = 0;int s = 0;
        h = getInt(ar[0]) * 10000;
        if (ar.length > 1) {
            m = getInt(ar[1]) * 100;
        }
        if (ar.length > 2) {
            s = getInt(ar[2]);
        }
        return h + m + s;
    }

    public static boolean isEmptyList(List lst)
    {
        if ((lst == null) || (lst.size() == 0)) {
            return true;
        }
        return false;
    }

    public static boolean isEmptyList(Object[] ar)
    {
        if ((ar == null) || (ar.length == 0)) {
            return true;
        }
        return false;
    }

    public static long getLng(Object obj, long defaultValue, boolean removeNonDigits)
    {
        if ((obj != null) && (!"".equalsIgnoreCase(obj.toString()))) {
            try
            {
                defaultValue = Long.parseLong(obj.toString());
            }
            catch (Exception ex)
            {
                if (removeNonDigits)
                {
                    StringBuffer sb = new StringBuffer();
                    String n = obj.toString();
                    for (int i = 0; i < n.length(); i++) {
                        if ("0123456789".indexOf(n.charAt(i)) >= 0) {
                            sb.append(n.charAt(i));
                        }
                    }
                    defaultValue = Long.parseLong(sb.toString());
                }
            }
        }
        return defaultValue;
    }

    public static String getTrim(String st){
        if(st!=null){
            st= BIPUtil.replaceAllString(st, " ", "");
            st= BIPUtil.replaceAllString(st, ""+(char)160, "");
        }
        return st;
    }

    public static String getStringNullLike(Object obj, boolean hasPrefix, boolean hasSuffix)
    {
        obj = getStringNull(obj);
        if (obj == null) {
            return (String)obj;
        }
        return (hasPrefix ? "%" : "") + obj.toString() + (hasSuffix ? "%" : "");
    }

    public static String getStringNullLike(Object obj)
    {
        return getStringNullLike(obj, true, true);
    }

    public static String getStringEmpty(Object obj, String defaultValue)
    {
        if ((obj != null) && (!"".equals(obj.toString().trim()))) {
            defaultValue = obj.toString();
        }
        return defaultValue;
    }

    public static Boolean getBooleanNull(Object obj)
    {
        if ((obj != null) && (!"".equalsIgnoreCase(obj.toString())))
        {
            boolean cur = false;
            try
            {
                cur = Boolean.parseBoolean(obj.toString());
                obj = new Boolean(cur);
            }
            catch (Exception ex)
            {
                obj = null;
            }
            return (Boolean)obj;
        }
        return null;
    }

    public static String fixMellatTime(String transTime)
    {
        String result = transTime;
        if ((transTime != null) && (transTime.length() > 5)) {
            result = transTime.substring(0, 2) + ":" + transTime.substring(3, 5);
        }
        return result;
    }

    public static String fixMellatDate(String transDate)
    {
        String result = transDate;
        if ((transDate != null) && (transDate.length() == 8)) {
            result = transDate.substring(0, 4) + "/" + transDate.substring(4, 6) + "/" + transDate.substring(6, 8);
        }
        return result;
    }

    public static String fixMelliTime(String tranTime)
    {
        String result = tranTime;
        if ((tranTime != null) && (tranTime.length() >= 6)) {
            result = tranTime.substring(0, 2) + ":" + tranTime.substring(2, 4) + ":" + tranTime.substring(4, 6);
        }
        return result;
    }

    public static String fixMelliDate(String transDate)
    {
        String result = transDate;
        if ((transDate != null) && (transDate.length() == 6)) {
            result = "13" + transDate.substring(0, 2) + "/" + transDate.substring(2, 4) + "/" + transDate.substring(4, 6);
        }
        return result;
    }

    public static String getStringClob(Clob clob)
    {
        String res = null;
        if (clob != null) {
            try
            {
                if (clob.length() > 0L)
                {
                    char[] buf = new char[(int)clob.length()];
                    clob.getCharacterStream().read(buf);
                    res = new String(buf);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return res;
    }

/*
  public static void setStringClob(Clob clob, String value)
  {
    try
    {
      if (!isEmpty(value))
      {
        Writer writer = clob.setCharacterStream(1L);
        writer.write(value);
        writer.flush();
        writer.close();
        clob.truncate(value.length());
      }
      else
      {
        clob.free();
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
*/

    public static Object invokeMulti(Object obj, String field)
            throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        Object v = null;
        if (field.indexOf(".") > -1)
        {
            String[] ar = splitString(field, ".");
            Object oo = obj;
            for (int i = 0; i < ar.length; i++) {
                oo = BIPUtil.invoke(oo, ar[i]);
            }
            v = oo;
        }
        else
        {
            v = BIPUtil.invoke(obj, field);
        }
        return v;
    }
    public static String[] splitString(String src, String delimeter)
    {
        StringTokenizer tokenizer = new StringTokenizer(src, delimeter);
        int n = tokenizer.countTokens();
        String[] ar = new String[n];
        for (int i = 0; i < n; i++) {
            ar[i] = tokenizer.nextToken();
        }
        return ar;
    }

    public static void main(String[] args) {
        String st="abc";
        System.out.println(String.format("name=%s, canonicalName=%s, simpleName=%s"
                , st.getClass().getName()
                , st.getClass().getCanonicalName()
                , st.getClass().getSimpleName()
        ));

    }

}
