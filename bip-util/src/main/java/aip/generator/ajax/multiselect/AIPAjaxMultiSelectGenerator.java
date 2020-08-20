package aip.generator.ajax.multiselect;

import aip.generator.AIPGenUtil;
import aip.util.AIPException;
import aip.util.AIPUtil;
import aip.util.NVL;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

public class AIPAjaxMultiSelectGenerator
{
  public void generateCode(AIPAjaxMultiSelectGeneratorParam param)
    throws AIPException
  {
    System.out.println("AIPAjaxMultiSelectGenerator.generateCode().start at :" + NVL.getNowTime());
    try
    {
      if (param.hasGeneration(1)) {
        generateDAO_getBases(param);
      }
      if (param.hasGeneration(2))
      {
        generateDAO_getDetails(param);
        generateDAO_getDetailsStr(param);
      }
      if (param.hasGeneration(4)) {
        generateDAO_saveDetails(param);
      }
      if (param.hasGeneration(8)) {
        generateAction_getDetailsAjax(param);
      }
      if (param.hasGeneration(16)) {
        generateStrutsForward_details(param);
      }
      if (param.hasGeneration(32)) {
        generateJSP_details(param);
      }
      if (param.hasGeneration(64)) {
        generateAction_saveDetails(param);
      }
      if (param.hasGeneration(65536)) {
        generateGenerator(param);
      }
      System.out.println("AIPAjaxMultiSelectGenerator.generateCode().finished at :" + NVL.getNowTime());
    }
    catch (Exception e)
    {
      throw new AIPException(e);
    }
  }
  
  private void generateGenerator(AIPAjaxMultiSelectGeneratorParam param)
    throws IOException
  {
    StringBuffer template = AIPUtil.readResourceStream(this, "/aip/generator/ajax/multiselect/Generator.template");
    AIPUtil.replaceAllString(template, "@packagepath@", param.getPackagePath());
    AIPUtil.replaceAllString(template, "@detail@", param.getDetail());
    AIPUtil.replaceAllString(template, "@master@", param.getMaster());
    AIPUtil.replaceAllString(template, "@base@", param.getBase());
    AIPUtil.replaceAllString(template, "@baseEntityClass@", param.getBaseEntityClass());
    AIPUtil.replaceAllString(template, "@detailEntityClass@", param.getDetailEntityClass());
    AIPUtil.replaceAllString(template, "@masterIdArg@", param.getMasterIdArg());
    AIPUtil.replaceAllString(template, "@masterIdArgType@", param.getMasterIdArgType());
    AIPUtil.replaceAllString(template, "@baseIdArg@", param.getBaseIdArg());
    AIPUtil.replaceAllString(template, "@detailFk2baseArg@", param.getDetailFk2baseArg());
    AIPUtil.replaceAllString(template, "@detailFk2baseArgType@", param.getDetailFk2baseArgType());
    
    AIPUtil.replaceAllString(template, "@baseTitle@", param.getBaseTitle());
    AIPUtil.replaceAllString(template, "@title@", param.getTitle());
    AIPUtil.replaceAllString(template, "@destPath@", param.getDestPath());
    AIPUtil.replaceAllString(template, "@jspPath@", param.getJspPath());
    int gen = param.getGeneration();
    if (param.hasGeneration(65536)) {
      gen -= 65536;
    }
    if (gen == 0) {
      gen = 16383;
    }
    AIPUtil.replaceAllString(template, "@generation@", NVL.getString(Integer.valueOf(gen)));
    

    File f = new File(param.destPath, param.getDetail() + "Generator.java");
    PrintWriter out = new PrintWriter(f, "UTF-8");
    
    out.print(template.toString());
    
    out.flush();
    out.close();
  }
  
  private void generateAction_saveDetails(AIPAjaxMultiSelectGeneratorParam param)
    throws IOException, AIPException
  {
    File f = new File(param.destPath, param.master + "Action.java");
    String classFile = f.getPath();
    String itemsStr = AIPUtil.change2Capital(param.detail) + "sStr";
    String methodWithArgs = "ActionForward save" + param.detail + "s(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)";
    

    String masterIdNVL = "NVL.get" + AIPUtil.change2Capital(param.masterIdArgType);
    

    String methodContent = "String masterId = " + masterIdNVL + "(request.getParameter(\"masterId\")); \n" + "String selectedItems = NVL.getString(request.getParameter(\"selectedItems\")); \n" + "" + param.master + "DAO dao = new " + param.master + "DAO();  \n" + "dao.save" + param.detail + "s(masterId, selectedItems); \n" + "return null;  \n";
    






    AIPGenUtil.addMethod2Class(classFile, methodWithArgs, methodContent);
  }
  
  private void generateJSP_details(AIPAjaxMultiSelectGeneratorParam param)
    throws IOException, AIPException
  {
    StringBuffer template = AIPUtil.readResourceStream(this, "/aip/generator/ajax/multiselect/details.jsp.template");
    AIPUtil.replaceAllString(template, "@master_lower@", param.master.toLowerCase());
    AIPUtil.replaceAllString(template, "@detail@", param.detail);
    AIPUtil.replaceAllString(template, "@detailFk2baseArg@", param.detailFk2baseArg);
    AIPUtil.replaceAllString(template, "@title@", param.title);
    AIPUtil.replaceAllString(template, "@baseTitle@", param.baseTitle);
    AIPUtil.replaceAllString(template, "@baseIdArg@", param.baseIdArg);
    

    File f = new File(param.jspPath, param.detail.toLowerCase() + "s" + ".jsp");
    f.getParentFile().mkdirs();
    PrintWriter out = new PrintWriter(f, "UTF-8");
    
    out.print(template.toString());
    
    out.flush();
    out.close();
  }
  
  private void generateStrutsForward_details(AIPAjaxMultiSelectGeneratorParam param)
    throws IOException, AIPException
  {
    String cfg = AIPUtil.readFile(param.getStrutsCfgPath());
    
    String actionpath_lower = param.master.toLowerCase();
    String mapping = "path=\"/" + actionpath_lower + "\"";
    int pos_action_path = cfg.indexOf(mapping);
    String forward_name = param.detail.toLowerCase() + "s";
    if (pos_action_path > 0)
    {
      int pos_action_end = cfg.indexOf("</action>", pos_action_path);
      String forward = "<forward name=\"" + forward_name + "\"";
      int pos_forward = cfg.indexOf(forward, pos_action_path);
      if ((pos_action_end > 0) && (pos_forward < 0))
      {
        int pos_form = param.jspPath.indexOf("/form/");
        String jsppath = param.jspPath.substring(pos_form) + "/" + forward_name + ".jsp";
        
        cfg = cfg.substring(0, pos_action_end) + "\t<forward name=\"" + forward_name + "\" path=\"" + jsppath + "\" />\n\t\t" + cfg.substring(pos_action_end);
        


        AIPUtil.writeFile(param.getStrutsCfgPath(), cfg);
      }
      else
      {
        throw new AIPException("فایل " + param.getStrutsCfgPath() + " </action> را برای" + mapping + "ندارد" + " یا " + forward + "وجود دارد" + ":pos_action_end=" + pos_action_end + ",pos_forward=" + pos_forward);
      }
    }
  }
  
  private void generateAction_getDetailsAjax(AIPAjaxMultiSelectGeneratorParam param)
    throws IOException, AIPException
  {
    File f = new File(param.destPath, param.master + "Action.java");
    String classFile = f.getPath();
    String itemsStr = AIPUtil.change2Capital(param.detail) + "sStr";
    String methodWithArgs = "ActionForward get" + param.detail + "sAjax(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)";
    
    String masterIdNVL = "NVL.get" + AIPUtil.change2Capital(param.masterIdArgType);
    

    String methodContent = "List<" + param.baseEntityClass + "> baseItems = new ArrayList<" + param.baseEntityClass + ">(); \n" + "String selectedItems = \"\"; \n" + "boolean[] selectedItemsValue={}; \n" + "" + param.masterIdArgType + " masterId = " + masterIdNVL + "(request.getParameter(\"" + param.masterIdArg + "\")); \n" + "try { \n" + "\t" + param.master + "DAO dao = new " + param.master + "DAO(); \n" + "\t \n" + "\tbaseItems = dao.get" + param.base + "s(); \n" + "\t \n" + "\tselectedItems = dao.get" + param.detail + "sStr(masterId); \n" + "\t \n" + "\tList lst = AIPUtil.splitSelectedIdsArray(selectedItems, \",\"); \n" + "\tselectedItemsValue = new boolean[baseItems.size()]; \n" + "\tfor (int i = 0; i < baseItems.size(); i++) { \n" + "\t\tif(lst.contains(NVL.getString( baseItems.get(i).get" + AIPUtil.change2Capital(param.baseIdArg) + "() ) )){ \n" + "\t\t\tselectedItemsValue[i]=true; \n" + "\t\t} \n" + "\t} \n" + "\t \n" + "} catch (AIPException e) { \n" + "\te.printStackTrace(); \n" + "} \n" + "request.setAttribute(\"masterId\", masterId); \n" + "request.setAttribute(\"baseItems\", baseItems); \n" + "request.setAttribute(\"selectedItems\", selectedItems); \n" + "request.setAttribute(\"selectedItemsValue\", selectedItemsValue); \n" + " \n" + "return mapping.findForward(\"" + param.detail.toLowerCase() + "s\"); \n";
    





























    AIPGenUtil.addMethod2Class(classFile, methodWithArgs, methodContent);
  }
  
  private void generateDAO_saveDetails(AIPAjaxMultiSelectGeneratorParam param)
    throws IOException, AIPException
  {
    File f = new File(param.destPath, param.master + "DAO.java");
    String classFile = f.getPath();
    String itemsStr = AIPUtil.change2Capital(param.detail) + "sStr";
    String methodWithArgs = "void save" + param.detail + "s(" + param.masterIdArgType + " " + param.masterIdArg + ",String new" + itemsStr + ")";
    

    String masterIdNVL = "NVL.get" + AIPUtil.change2Capital(param.masterIdArgType);
    String detailFk2baseNVL = "NVL.get" + AIPUtil.change2Capital(param.detailFk2baseArgType);
    
    String methodContent = "String old" + itemsStr + " = get" + param.detail + "sStr(" + param.masterIdArg + ");\n" + "SyncIdsResult sync = AIPUtil.syncNames(new" + itemsStr + ", old" + itemsStr + ");\n" + "\n" + "Session session = getSession();\n" + "Transaction tx = null;\n" + "try {\n" + "\ttx = session.beginTransaction();\n" + "\tfor (int i = 0; i < sync.getDelIds().size(); i++) {\n" + "\t\t Object obj = session.createQuery(\"from " + param.detailEntityClass + " where " + param.masterIdArg + "='\"+" + param.masterIdArg + "+\"' and " + param.detailFk2baseArg + "='\"+sync.getDelIds().get(i)+\"'\") \n" + "\t\t .uniqueResult(); \n" + "\t\t session.delete(obj); \n" + "\t} \n" + "\tfor (int i = 0; i < sync.getNewIds().size(); i++) { \n" + "\t\t" + param.detail + "ENT ent = new " + param.detail + "ENT(); \n" + "\t\tent.set" + AIPUtil.change2Capital(param.masterIdArg) + "(" + masterIdNVL + "(" + param.masterIdArg + ")); \n" + "\t\tent.set" + AIPUtil.change2Capital(param.detailFk2baseArg) + "(" + detailFk2baseNVL + "(sync.getNewIds().get(i))); \n" + "\t\tsession.save(ent); \n" + "\t} \n" + "\ttx.commit(); \n" + "} catch (Exception e) { \n" + "\tif(tx!=null && tx.isActive()){ \n" + "\t\ttx.rollback(); \n" + "\t} \n" + "\t//session.close(); \n" + "\tthrow new AIPException(e); \n" + "} \n";
    



























    AIPGenUtil.addMethod2Class(classFile, methodWithArgs, methodContent);
  }
  
  private void generateDAO_getDetailsStr(AIPAjaxMultiSelectGeneratorParam param)
    throws IOException, AIPException
  {
    File f = new File(param.destPath, param.master + "DAO.java");
    String classFile = f.getPath();
    String methodWithArgs = "String get" + param.detail + "sStr(" + param.masterIdArgType + " " + param.masterIdArg + ")";
    
    String detailFk2base_capital = AIPUtil.change2Capital(param.detailFk2baseArg);
    String methodContent = "List<" + param.detailEntityClass + "> lst = get" + param.detail + "s(" + param.masterIdArg + "); \n" + "StringBuffer sb = new StringBuffer();\n" + "for (int i = 0; i < lst.size(); i++) {\n" + "\tsb.append(lst.get(i).get" + detailFk2base_capital + "());\n" + "\tif(i<lst.size()-1)sb.append(\",\");\n" + "}\n" + "return sb.toString();\n";
    







    AIPGenUtil.addMethod2Class(classFile, methodWithArgs, methodContent);
  }
  
  private void generateDAO_getDetails(AIPAjaxMultiSelectGeneratorParam param)
    throws IOException, AIPException
  {
    File f = new File(param.destPath, param.master + "DAO.java");
    String classFile = f.getPath();
    String methodWithArgs = "List<" + param.getDetailEntityClass() + ">" + " get" + param.detail + "s(" + param.masterIdArgType + " " + param.masterIdArg + ")";
    String value = "\"+" + param.masterIdArg + "+\"";
    if ("String".equalsIgnoreCase(param.masterIdArgType)) {
      value = "'\"+" + param.masterIdArg + "+\"'";
    }
    String methodContent = "return getSession().createQuery(\"from " + param.getDetailEntityClass() + " where " + param.masterIdArg + "=" + value + "  \").list();";
    AIPGenUtil.addMethod2Class(classFile, methodWithArgs, methodContent);
  }
  
  private void generateDAO_getBases(AIPAjaxMultiSelectGeneratorParam param)
    throws IOException, AIPException
  {
    File f = new File(param.destPath, param.master + "DAO.java");
    String classFile = f.getPath();
    String methodWithArgs = "List<" + param.getBaseEntityClass() + ">" + " get" + param.base + "s()";
    String methodContent = "return getSession().createQuery(\"from " + param.getBaseEntityClass() + "\").list();";
    AIPGenUtil.addMethod2Class(classFile, methodWithArgs, methodContent);
  }
  
  public static void main(String[] args)
  {
    AIPAjaxMultiSelectGenerator gen = new AIPAjaxMultiSelectGenerator();
    AIPAjaxMultiSelectGeneratorParam param = new AIPAjaxMultiSelectGeneratorParam();
    
    param.master = "User";
    param.base = "Role";
    param.detail = "UserRole";
    
    param.baseEntityClass = "RoleENT";
    param.detailEntityClass = "UserRoleENT";
    
    param.masterIdArg = "username";
    param.baseIdArg = "id";
    param.masterIdArgType = "String";
    param.detailFk2baseArg = "rolename";
    param.detailFk2baseArgType = "String";
    param.baseTitle = "rolenamefarsi";
    

    param.title = "نقشهای کاربر";
    param.destPath = "E:/java/workspace6.5/AIPNIOPDCSIB/src/aip/niopdc/sib/security/user";
    param.jspPath = "E:/java/workspace6.5/AIPNIOPDCSIB/WebRoot/form/security/user";
    








    param.generation = 65536;
    try
    {
      gen.generateCode(param);
    }
    catch (AIPException e)
    {
      e.printStackTrace();
    }
  }
}
