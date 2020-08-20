package bip.common.util.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoader;

public class BIPSpringUtil {
	private static ApplicationContext localContext=null;

	public static Object getBean(String name){
		boolean isLoadDirect=false;
		System.out.println(">>>>>>>>>>>>>>>>>>\nBIPSpringUtil.getBean():starting........:"+name);
		ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		//WebApplicationContextUtils.
		if(ctx==null){
			System.out.println("ContextLoader.getCurrentWebApplicationContext() is null!!!!!!!!!!");
			isLoadDirect=true;
			if(localContext==null){
				localContext = new ClassPathXmlApplicationContext("/applicationContext.xml");
			}
			ctx = localContext;
		}
		try {
			return ctx.getBean(name);		
		} catch (Exception e) {
			if(isLoadDirect==false){
				System.out.println("load from /applicationContext.xml");
				if(localContext==null){
					localContext = new ClassPathXmlApplicationContext("/applicationContext.xml");
				}
				ctx = localContext;
				return ctx.getBean(name);
			}else{
				throw new RuntimeException(e);
			}
		}
	}
	
}
