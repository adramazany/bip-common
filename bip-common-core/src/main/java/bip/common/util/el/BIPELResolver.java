package bip.common.util.el;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.MapELResolver;
import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.Map;


public class BIPELResolver extends ELResolver {

  private ELResolver delegate = new MapELResolver();
  private Map<Object, Object> userMap;
  
  public BIPELResolver(Map<Object, Object> userMap) {
    this.userMap = userMap;
  }
  
  public Object getValue(ELContext context, Object base, Object property) {
    if(base==null) {
      base = userMap;
    }
    return delegate.getValue(context, base, property);
  }
  
  public Class<?> getCommonPropertyType(ELContext context, Object base) {
    if(base==null) {
      base = userMap;
    }
    return delegate.getCommonPropertyType(context, base);
  }

  public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
      Object base) {
    if(base==null) {
      base = userMap;
    }
    return delegate.getFeatureDescriptors(context, base);
  }

  public Class<?> getType(ELContext context, Object base, Object property) {
    if(base==null) {
      base = userMap;
    }
    return delegate.getType(context, base, property);
  }
  
  public boolean isReadOnly(ELContext context, Object base, Object property) {
    if(base==null) {
      base = userMap;
    }
    return delegate.isReadOnly(context, base, property);
  }

  public void setValue(ELContext context, Object base, Object property, Object value) {
    if(base==null) {
      base = userMap;
    }
    delegate.setValue(context, base, property, value);
  }

}