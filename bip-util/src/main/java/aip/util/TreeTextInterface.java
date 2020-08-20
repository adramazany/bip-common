package aip.util;

import java.lang.reflect.InvocationTargetException;

public interface TreeTextInterface {
	
	public String getText(Object obj, String nodeIDChild) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException ;
	
}
