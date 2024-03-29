package bip.common.util.el;

import java.util.HashMap;
import java.util.Map;

import javax.el.ValueExpression;
import javax.el.VariableMapper;

public class BIPVariableMapper extends VariableMapper {

  private Map<String, ValueExpression> expressions = new HashMap<String, ValueExpression>();
  
  public ValueExpression resolveVariable(String variable) {
    return expressions.get(variable);
  }

  public ValueExpression setVariable(String variable, ValueExpression expression) {
    return expressions.put(variable, expression);
  }

}