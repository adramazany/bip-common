package bip.common.web;

import bip.common.util.BIPRequestWrapper;

import javax.servlet.http.HttpServletRequest;

public class BIPRequestWrapperImpl implements BIPRequestWrapper {

    HttpServletRequest request;
    public BIPRequestWrapperImpl(HttpServletRequest request){
        this.request = request;
    }

    @Override
    public String getParameter(String name) {
        return request.getParameter(name);
    }

    @Override
    public Object getSessionAttribute(String name) {
        return request.getSession().getAttribute(name);
    }

    @Override
    public void setSessionAttribute(String name, Object value) {
        request.getSession().setAttribute(name,value);

    }

    @Override
    public void removeSessionAttribute(String name) {
        request.getSession().removeAttribute(name);

    }

    @Override
    public String getRemoteUser() {
        return request.getRemoteUser();
    }
}
