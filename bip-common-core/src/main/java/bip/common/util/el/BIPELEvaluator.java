package bip.common.util.el;

import bip.common.util.NVL;

import javax.el.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class BIPELEvaluator {
	private static Logger logger = Logger.getLogger(BIPELEvaluator.class.getName());

	public static void main(String[] args) throws Exception {
	    Map<Object, Object> variables = new HashMap<Object, Object>();
	    variables.put("x", NVL.getInt(123));
	    variables.put("y", NVL.getInt(456));

	    //get the method for ${myprefix:hello(string)}
	    Method sayHello = BIPELEvaluator.class.getMethod("sayHello", new Class[]{String.class});
	    Map<String, Method> functions = new HashMap<String, Method>();
	    functions.put("hello", sayHello);
	    
	    String result = (String) evaluate("this is a sum=${x+y} sayHello=${bip:hello('Dave')}", variables, "bip", functions);
	    System.out.println("BIPELEvaluator.main()="+result);
	    
	}
    public static Object evaluate(String template,Map<Object, Object> variables) throws BIPELEvaluatorException {
    	return evaluate(template,variables,null,null);
    }
    public static Object evaluate(String template,Map<Object, Object> variables,String functionPrefix,Map<String, Method> functions) throws BIPELEvaluatorException {

    	try {
    	    ExpressionFactory expressionFactory = ExpressionFactory.newInstance();
    	    logger.info("javax.el.ExpressionFactory="+expressionFactory.getClass().getName());
			
    	    //create the context
    	    ELResolver elResolver = new BIPELResolver(variables);
    	    final VariableMapper variableMapper = new BIPVariableMapper();
    	    final BIPFunctionMapper functionMapper = new BIPFunctionMapper();
    	    if(functions!=null){
    	    	for (String functionName : functions.keySet()) {
    	    	    functionMapper.addFunction(functionPrefix, functionName, functions.get(functionName));
    			}
    	    }
    	    final CompositeELResolver compositeELResolver = new CompositeELResolver();
    	    compositeELResolver.add(elResolver);
    	    compositeELResolver.add(new ArrayELResolver());
    	    compositeELResolver.add(new ListELResolver());
    	    compositeELResolver.add(new BeanELResolver());
    	    compositeELResolver.add(new MapELResolver());
    	    ELContext context = new ELContext() {
    	      @Override
    	      public ELResolver getELResolver() {
    	        return compositeELResolver;
    	      }
    	      @Override
    	      public FunctionMapper getFunctionMapper() {
    	        return functionMapper;
    	      }
    	      @Override
    	      public VariableMapper getVariableMapper() {
    	        return variableMapper;
    	      }
    	    };
    	    
    	    //create and resolve a value expression
    	/*    String sumExpr = "${x+y}";
    	    ValueExpression ve = expressionFactory.createValueExpression(context, sumExpr, Object.class);
    	    Object result = ve.getValue(context);
    	*/  
    	    ValueExpression ve = expressionFactory.createValueExpression(context, template, Object.class);
    	    Object result = ve.getValue(context);
    	    //System.out.println("Result="+result);
    	    return result;
    	    
    	    //call a function
    	/*    String fnExpr = "#{myprefix:hello('Dave')}";
    	    ValueExpression fn = expressionFactory.createValueExpression(context, fnExpr, Object.class);
    	    fn.getValue(context);
    	*/
    	} catch (Exception e) {
			logger.severe("خطا درتبدیل متن الگو به متن نهایی !" + e.getMessage());
			throw new BIPELEvaluatorException("خطا درتبدیل متن الگو به متن نهایی! " , e);
		}
  }

  public static String sayHello(String argument) {
    System.out.println("Hello, "+argument);
    return "OK";
  }
  
}
