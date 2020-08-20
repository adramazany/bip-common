package bip.common.util;

/**
 * Created by ramezani on 3/14/2019.
 */
public interface BIPRequestWrapper {
    String getParameter(String name);
    Object getSessionAttribute(String name);
    void setSessionAttribute(String name,Object value);
    void removeSessionAttribute(String name);
    String getRemoteUser();

}
