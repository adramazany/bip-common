package aip.generator.struts.list;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

//import aip.generator.AIPGenUtil;
import aip.util.AIPException;
import aip.util.AIPUtil;
//import aip.util.AIPWebUserParam;
import aip.util.NVL;


public class AIPStrutsReportListWithToolbarGEN {

	public void generateCode(AIPStrutsReportListWithToolbarParam param)throws AIPException{
		System.out.println("StrutsReportListWithToolbarGEN.generateCode().start at :"+NVL.getNowTime());
		try {
			Class.forName(param.connectionDriver);
			Connection cn = DriverManager.getConnection(param.connectionString,param.connectionUser,param.connectionPassword);
			String query = param.loaderSQL.replace("?", "1");
			Statement stmt = cn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			ResultSetMetaData meta = rs.getMetaData();
			
			Hashtable<String,String> columnsClassName = new Hashtable<String,String>();
			for(int column=1;column<=meta.getColumnCount();column++){
				columnsClassName.put(meta.getColumnName(column).toLowerCase(),getColumnClassName(meta,column));
			}
			
			//1
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GENERATION_HBM)){
				generateHbm(param,meta,rs.findColumn(param.idColumn),columnsClassName);
			}
			
			//2097152
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GENERATION_HBM_ENT)){
				generateHbmENT(param,meta,rs.findColumn(param.idColumn),columnsClassName);
			}
			
			//2
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GENERATION_ENTITY)){
				generateEntityClass(param,columnsClassName);
			}
			
			//3
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GENERATION_INTERFACE)){
				generateInterface(param);
			}
			
			//4
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GENERATION_PARAM)){
				generateParam(param,columnsClassName);
			}
			
			//5
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GENERATION_LST)){
				generateLST(param);
			}
			
			//6
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GENERATION_COMBOLST)){
				generateComboLST(param);
			}
			
			//7
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GENERATION_DAO)){
				generateDAO(param,columnsClassName);
			}
			
			
			//8
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GENERATION_ACTION)){
				generateAction(param,columnsClassName);
			}
			
			
			//9
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GENERATION_TILES)){
				generateTiles(param,columnsClassName);
			}
			
			
			//10
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GENERATION_JSP)){
				generateJSP(param);
			}
			
			//11
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GENERATION_JSPPARAM)){
				generateJSPParam(param);
			}
			
			//12
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GEN_UPD_HIBERNATE) ){
				updateHibernateCfg(param);
			}
			
			//13
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GEN_UPD_STRUTS) ){
				updateStrutsCfg(param);
			}
		
			//14
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GEN_UPD_TILES) ){
				updateTilesCfg(param);
			}
			
			//15
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GEN_ADD_ACTION_GETENTITY) ){
				updateActionAddGetEntity(param,columnsClassName);
			}
			
			//16
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GEN_DAO_GETENTITY) ){
				generateDAO_getEntity(param,columnsClassName);
			}
			
			//17
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GEN_DAO_SAVEENTITY) ){
				generateDAO_saveEntity(param,columnsClassName);
			}
			
			//18
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GEN_ACTION_EDITENTITY) ){
				generateAction_editEntity(param,columnsClassName);
			}
			
			//19
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GEN_ACTION_SAVEENTITY) ){
				generateAction_saveEntity(param,columnsClassName);
			}
			
			//20
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GEN_STRUTSFORWARD_EDITENTITY) ){
				generateStrutsForward_editEntity(param,columnsClassName);
			}
			
			//21
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GEN_JSP_EDITENTITY) ){
				generateJSP_editEntity(param,columnsClassName);
			}
			
			//?
			if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GENERATION_GENERATOR)){
				generateGenerator(param);
			}
			
			rs.close();
			stmt.close();
			cn.close();
			
			System.out.println("StrutsReportListWithToolbarGEN.generateCode().finished at :"+NVL.getNowTime());
		} catch (Exception e) {
			throw new AIPException(e);
		}
	}
	

	private void generateJSP_editEntity(AIPStrutsReportListWithToolbarParam param,Hashtable<String, String> columnsClassName)throws IOException, AIPException  {
		StringBuffer template = AIPUtil.readResourceStream(this,"/aip/generator/struts/list/edit.jsp.template");
		AIPUtil.replaceAllString(template, "@title@", param.getTitle());
		AIPUtil.replaceAllString(template, "@name_lower@", param.getName().toLowerCase());
		AIPUtil.replaceAllString(template, "@idColumn@", param.getIdColumn());
		
		int visiblecolumns_start = template.indexOf("@@visiblecolumns@@",0);
		int visiblecolumns_end = template.indexOf("@@/visiblecolumns@@",visiblecolumns_start);
		if(visiblecolumns_start>-1 && visiblecolumns_end>-1){
			
			String visiblecolumnsTemplate = template.substring(visiblecolumns_start+19, visiblecolumns_end);
			StringBuffer visiblecolumns = new StringBuffer();
			for(int i=0;i<param.visibleColumns.length;i++){
				String visiblecolumn_lower = param.visibleColumns[i][0].toLowerCase();
				if(!"radif".equalsIgnoreCase(visiblecolumn_lower)){
					String visiblecolumn_captital = AIPUtil.change2Capital(visiblecolumn_lower);
					String visiblecolumn_title = param.visibleColumns[i][1];
					
					String tmp = visiblecolumnsTemplate;
					tmp = tmp.replaceAll("@visiblecolumn_lower@", visiblecolumn_lower);
					tmp = tmp.replaceAll("@visiblecolumn_title@", visiblecolumn_title);
					visiblecolumns.append(tmp);
				}
			}
			
			template.replace(visiblecolumns_start, visiblecolumns_end+19, visiblecolumns.toString());
		}
		
		File f = new File(param.jspPath,param.getName().toLowerCase()+"edit.jsp");
		f.getParentFile().mkdirs();
		PrintWriter out = new PrintWriter(f,"UTF-8");

		out.print(template.toString());
		
		out.flush();
		out.close();
		
		
	}

	private void generateStrutsForward_editEntity(AIPStrutsReportListWithToolbarParam param,Hashtable<String, String> columnsClassName)  throws IOException, AIPException {
		// TODO Auto-generated method stub
		
	}

	private void generateAction_saveEntity(AIPStrutsReportListWithToolbarParam param,Hashtable<String, String> columnsClassName)  throws IOException, AIPException {
		// TODO Auto-generated method stub
		
	}

	private void generateAction_editEntity(AIPStrutsReportListWithToolbarParam param,Hashtable<String, String> columnsClassName)  throws IOException, AIPException {
		// TODO Auto-generated method stub
		
	}

	private void generateDAO_saveEntity(AIPStrutsReportListWithToolbarParam param,Hashtable<String, String> columnsClassName)  throws IOException, AIPException {
		// TODO Auto-generated method stub
		
	}

	private void generateDAO_getEntity(AIPStrutsReportListWithToolbarParam param,Hashtable<String, String> columnsClassName)  throws IOException, AIPException {
		/*
		 * interface
		 */
		/*
		 * param
		 */
		/*
		 * dao
		 */
//		File f = new File(param.destPath,param.getName()+"DAO.java");
//		String classFile=f.getPath();
//		String methodWithArgs="List<"+param.getDetailEntityClass()+">" +" get"+param.detail+"s("+param.masterIdArgType+" "+param.masterIdArg+")";
//		String value = "\"+"+param.masterIdArg+"+\"" ;
//		if("String".equalsIgnoreCase(param.masterIdArgType)){
//			value="'\"+"+param.masterIdArg+"+\"'" ;
//		}
//		String methodContent="return getSession().createQuery(\"from "+param.getDetailEntityClass()+" where "+param.masterIdArg+"="+value+"  \").list();";
//		AIPGenUtil.addMethod2Class(classFile, methodWithArgs, methodContent);
	}

	private void updateActionAddGetEntity(AIPStrutsReportListWithToolbarParam param,Hashtable<String,String> columnsClassName) throws IOException, AIPException {
		File f = new File(param.destPath,param.getName()+"Action.java");
		String content = AIPUtil.readFile(f.getPath());
		
		String method = param.getClassName() +" get"+param.getClassName()+"(HttpServletRequest request){";
		if(content.indexOf(method)<0){
			int pos_class_end = content.lastIndexOf("}");
			if(pos_class_end>0){

				String[] columnsName = columnsClassName.keySet().toArray(new String[0]);
				String[] columnsClass = columnsClassName.values().toArray(new String[0]);

				StringBuffer sb = new StringBuffer();
				for(int i=0;i<columnsName.length;i++){
					String columnsName_capital = AIPUtil.change2Capital(columnsName[i]);
					int posLastDot = columnsClass[i].lastIndexOf('.');
					String columnClass = columnsClass[i].substring(posLastDot+1);
					sb.append("\n\t	ent.set"+columnsName_capital+"( NVL.get"+columnClass+"( request.getParameter(\""+columnsName[i]+"\") ) );");
				}
				
				content = content.substring(0, pos_class_end)
				+"\n\tprivate "+param.getClassName()+" get"+param.getClassName()+"(HttpServletRequest request){ "
				+"\n\t	"+param.getClassName()+" ent = new "+param.getClassName()+"(); "
				+sb.toString()
				+"\n\t"
				+"\n\t	ent.setWebUserParam(new AIPWebUserParam(request));"
				+"\n\t"
				+"\n\t	return ent; "
				+"\n\t} \n"
				+content.substring(pos_class_end);
				
				AIPUtil.writeFile(f.getPath(), content);
			}else{
				throw new AIPException("فایل "+f.getPath()+" } را ندارد");
			}
		}else{
			throw new AIPException("فایل "+f.getPath()+" متد "+method+" را دارد!");
		}

	}

	private void generateGenerator(AIPStrutsReportListWithToolbarParam param) throws IOException {
		StringBuffer template = AIPUtil.readResourceStream(this,"/aip/generator/struts/list/Generator.template");
		AIPUtil.replaceAllString(template, "@packagepath@", param.getPackagePath());
		AIPUtil.replaceAllString(template, "@name@", param.getName());
		AIPUtil.replaceAllString(template, "@title@", param.getTitle());
		AIPUtil.replaceAllString(template, "@destPath@", param.getDestPath());
		AIPUtil.replaceAllString(template, "@jspPath@", param.getJspPath());
		AIPUtil.replaceAllString(template, "@tableName@", param.getTableName());
		AIPUtil.replaceAllString(template, "@loaderSQL@", param.getLoaderSQL().replace('\n', ' '));
		AIPUtil.replaceAllString(template, "@idColumn@", param.getIdColumn());
		AIPUtil.replaceAllString(template, "@idGeneratorClass@", param.getIdGeneratorClass());
		AIPUtil.replaceAllString(template, "@connectionDriver@", param.getConnectionDriver());
		AIPUtil.replaceAllString(template, "@connectionString@", param.getConnectionString());
		AIPUtil.replaceAllString(template, "@connectionUser@", param.getConnectionUser());
		AIPUtil.replaceAllString(template, "@connectionPassword@", param.getConnectionPassword());
		int gen = param.getGeneration(); 
		if(param.hasGeneration(AIPStrutsReportListWithToolbarParam.GENERATION_GENERATOR)){
			gen-=AIPStrutsReportListWithToolbarParam.GENERATION_GENERATOR;
		}
		if(gen==0){
			gen=AIPStrutsReportListWithToolbarParam.GENERATION_FULL;
		}
		AIPUtil.replaceAllString(template, "@generation@",NVL.getString(gen));
		
		/*
		 * @@visibleColumns@@
		 */
		int paramCombos_start = template.indexOf("@@paramCombos@@");
		int paramCombos_end = template.indexOf("@@/paramCombos@@",paramCombos_start);
		if(paramCombos_start>-1 && paramCombos_end>-1){
			String paramcolumnsTemplate = template.substring(paramCombos_start+16, paramCombos_end);
			StringBuffer paramcolumns = new StringBuffer();
			for(int i=0;i<param.paramCombos.length;i++){
				String tmp = paramcolumnsTemplate;
				tmp = tmp.replaceAll("@paramCombo@", param.paramCombos[i][0]);
				tmp = tmp.replaceAll("@paramCombo_class@", param.paramCombos[i][1]);
				tmp = tmp.replaceAll("@paramCombo_relation@", param.paramCombos[i][2]);
				tmp = tmp.replaceAll("@paramCombo_caption@", param.paramCombos[i][3]);
				tmp = tmp.replaceAll("@paramCombo_value@", param.paramCombos[i][4]);
				tmp = tmp.replaceAll("@commaifnotlast@", (i==param.paramCombos.length-1?"":","));
				paramcolumns.append(tmp);
			}
			template.replace(paramCombos_start, paramCombos_end+16, paramcolumns.toString());
		}
		
		/*
		 * @@visibleColumns@@
		 */
		int visibleColumns_start = template.indexOf("@@visibleColumns@@");
		int visibleColumns_end = template.indexOf("@@/visibleColumns@@",visibleColumns_start);
		if(visibleColumns_start>-1 && visibleColumns_end>-1){
			String paramcolumnsTemplate = template.substring(visibleColumns_start+19, visibleColumns_end);
			StringBuffer paramcolumns = new StringBuffer();
			for(int i=0;i<param.visibleColumns.length;i++){
				String tmp = paramcolumnsTemplate;
				tmp = tmp.replaceAll("@visibleColumn@", param.visibleColumns[i][0]);
				tmp = tmp.replaceAll("@visibleColumn_title@", param.visibleColumns[i][1]);
				tmp = tmp.replaceAll("@commaifnotlast@", (i==param.visibleColumns.length-1?"":","));
				paramcolumns.append(tmp);
			}
			template.replace(visibleColumns_start, visibleColumns_end+19, paramcolumns.toString());
		}
		
		/*
		 * @@paramColumnsTitle@@
		 */
		int paramcolumnstitle_start = template.indexOf("@@paramColumnsTitle@@");
		int paramcolumnstitle_end = template.indexOf("@@/paramColumnsTitle@@",paramcolumnstitle_start);
		if(paramcolumnstitle_start>-1 && paramcolumnstitle_end>-1){
			String paramcolumnsTemplate = template.substring(paramcolumnstitle_start+22, paramcolumnstitle_end);
			StringBuffer paramcolumns = new StringBuffer();
			for(int i=0;i<param.paramColumnsTitle.length;i++){
				String tmp = paramcolumnsTemplate;
				tmp = tmp.replaceAll("@paramColumnTitle@", param.paramColumnsTitle[i]);
				tmp = tmp.replaceAll("@commaifnotlast@", (i==param.paramColumnsTitle.length-1?"":","));
				paramcolumns.append(tmp);
			}
			template.replace(paramcolumnstitle_start, paramcolumnstitle_end+22, paramcolumns.toString());
		}
		
		/*
		 * @@paramColumns@@
		 */
		int paramcolumns_start = template.indexOf("@@paramColumns@@");
		int paramcolumns_end = template.indexOf("@@/paramColumns@@",paramcolumns_start);
		if(paramcolumns_start>-1 && paramcolumns_end>-1){
			String paramcolumnsTemplate = template.substring(paramcolumns_start+17, paramcolumns_end);
			StringBuffer paramcolumns = new StringBuffer();
			for(int i=0;i<param.paramColumns.length;i++){
				String tmp = paramcolumnsTemplate;
				tmp = tmp.replaceAll("@paramColumn@", param.paramColumns[i]);
				tmp = tmp.replaceAll("@commaifnotlast@", (i==param.paramColumns.length-1?"":","));
				paramcolumns.append(tmp);
			}
			template.replace(paramcolumns_start, paramcolumns_end+17, paramcolumns.toString());
		}

		
		File f = new File(param.destPath,param.getName()+"Generator.java");
		PrintWriter out = new PrintWriter(f,"UTF-8");

		out.print(template.toString());
		
		out.flush();
		out.close();
		
	}

	
	private void updateTilesCfg(AIPStrutsReportListWithToolbarParam param)throws IOException, AIPException  {
		String name_lower = param.getName().toLowerCase();
		/*
		 * tiles-defs.xml
		 */
		String defs = AIPUtil.readFile(param.getTilesDefsPath());
		
		String definition = "name=\"page."+name_lower+"\"";
		if(defs.indexOf(definition)<0){
			int pos_definitions_end = defs.indexOf("</tiles-definitions>");
			if(pos_definitions_end>0){

				defs = defs.substring(0, pos_definitions_end)
				+"\n\t<definition name=\"page."+name_lower+"\" extends=\"base.definition\"> "
				+"\n\t	<put name=\"title\" value=\""+param.getTitle()+"\" /> "
				+"\n\t	<put name=\"body\" value=\"/"+name_lower+".do\" /> "
				+"\n\t</definition> \n"

				+defs.substring(pos_definitions_end);
				
				AIPUtil.writeFile(param.getTilesDefsPath(), defs);
			}else{
				throw new AIPException("فایل "+param.getTilesDefsPath()+" </tiles-definitions> را ندارد");
			}
			
		}

		/*
		 * struts-config-tiles.xml
		 */
		String cfg = AIPUtil.readFile(param.getTilesCfgPath());
		
		String mapping = "path=\"/t_"+name_lower+"\"";
		if(cfg.indexOf(mapping)<0){
			int pos_action_end = cfg.indexOf("</action-mappings>");
			if(pos_action_end>0){

				cfg = cfg.substring(0, pos_action_end)
				+"\n\t<action  "
				+"\n\t	parameter=\"page."+name_lower+"\" " 
				+"\n\t	path=\"/t_"+name_lower+"\" " 
				+"\n\t	scope=\"request\" "
				+"\n\t	type=\"org.apache.struts.actions.ForwardAction\"/> "
				+cfg.substring(pos_action_end);
				
				AIPUtil.writeFile(param.getTilesCfgPath(), cfg);
			}else{
				throw new AIPException("فایل "+param.getTilesCfgPath()+" </action-mappings> را ندارد");
			}
			
		}
		
		
	}

	private void updateStrutsCfg(AIPStrutsReportListWithToolbarParam param)throws IOException, AIPException {
		String cfg = AIPUtil.readFile(param.getStrutsCfgPath());
		
		String actionpath_lower = param.getName().toLowerCase();
		String mapping = "path=\"/"+actionpath_lower+"\"";
		if(cfg.indexOf(mapping)<0){
			int pos_action_end = cfg.indexOf("</action-mappings>");
			if(pos_action_end>0){

				int pos_form = param.jspPath.indexOf("/form/");
				String jsppath = param.jspPath.substring(pos_form)+"/"+param.getName().toLowerCase()+"lst.jsp";
				String jspparampath = param.jspPath.substring(pos_form)+"/"+param.getName().toLowerCase()+"lstparam.jsp";
				
				cfg = cfg.substring(0, pos_action_end)
				+"\n\t<action  "
				+"\n\t	parameter=\"reqCode\" " 
				+"\n\t	path=\"/"+actionpath_lower+"\" " 
				+"\n\t	scope=\"request\" "
				+"\n\t	type=\""+param.getPackagePath()+"."+param.getName()+"Action\"> "
				+"\n\t	<forward name=\"view\" path=\""+jsppath+"\" /> "
				+"\n\t	<forward name=\"restrictedUser\" path=\"/layout/index.jsp\" /> "
				+"\n\t	<forward name=\"ShowParamDialog\" path=\""+jspparampath+"\" /> "
				+"\n\t</action> \n"
				+cfg.substring(pos_action_end);
				
				AIPUtil.writeFile(param.getStrutsCfgPath(), cfg);
			}else{
				throw new AIPException("فایل "+param.getStrutsCfgPath()+" </action-mappings> را ندارد");
			}
			
		}
		
	}

	private void generateHibernateCfg(AIPStrutsReportListWithToolbarParam param) throws IOException, AIPException {
		StringBuffer template = AIPUtil.readResourceStream(this,"/aip/generator/struts/list/hibernate.cfg.template");
		//AIPUtil.replaceAllString(template, "@packagepath@", param.getPackagePath());
		
		File f = new File(param.destPath,"hibernate.cfg.xml");
		f.getParentFile().mkdirs();
		PrintWriter out = new PrintWriter(f,"UTF-8");

		out.print(template.toString());
		
		out.flush();
		out.close();
	}

	private void updateHibernateCfg(AIPStrutsReportListWithToolbarParam param) throws IOException, AIPException {
		
		File f = new File(param.getHibernateCfgPath());
		if(!f.exists()){
			generateHibernateCfg(param);
		}
		
		String cfg = AIPUtil.readFile(param.getHibernateCfgPath());
		
		String mapping = "resource=\""+param.getClassFullName().replace('.', '/')+".hbm.xml\"";
		if(cfg.indexOf(mapping)<0){
			if(cfg.indexOf(param.getClassName()+".hbm.xml")>-1){
				throw new AIPException("یک "+param.getClassName()+".hbm.xml"+"دیگر با مسیری متفاوت در فایل "+param.getHibernateCfgPath()+"وجود دارد!");
			}
			int pos_factory_end = cfg.indexOf("</session-factory>");
			if(pos_factory_end>0){
				cfg = cfg.substring(0, pos_factory_end)+
				"\n\t<mapping resource=\""+param.getClassFullName().replace('.', '/')+".hbm.xml\" />\n"
				+cfg.substring(pos_factory_end);
				AIPUtil.writeFile(param.getHibernateCfgPath(), cfg);
			}else{
				throw new AIPException("فایل "+param.getHibernateCfgPath()+" </session-factory> را ندارد");
			}
			
		}
		
	}

	private void generateJSPParam(AIPStrutsReportListWithToolbarParam param) throws IOException  {
		StringBuffer template = AIPUtil.readResourceStream(this,"/aip/generator/struts/list/jspparam.template");
		//AIPUtil.replaceAllString(template, "@packagepath@", param.getPackagePath());
		
		Hashtable<String, String> htParamsTitle = new Hashtable<String, String>();
		for(int i=0;i<param.paramColumns.length;i++){
			htParamsTitle.put(param.paramColumns[i].toLowerCase(), param.paramColumnsTitle[i]);
		}
		ArrayList<String> userParams = new ArrayList<String>();
		
		int paramcombos_start = template.indexOf("@@paramcombos@@");
		int paramcombos_end = template.indexOf("@@/paramcombos@@",paramcombos_start);
		if(paramcombos_start>-1 && paramcombos_end>-1){
			
			String paramcolumnsTemplate = template.substring(paramcombos_start+16, paramcombos_end);
			StringBuffer paramcombos = new StringBuffer();
			for(int i=0;i<param.paramCombos.length;i++){
				
				String paramcombo_lower = param.paramCombos[i][0].toLowerCase();
				String paramname_lower = param.paramCombos[i][2].toLowerCase();
				String paramcombo_caption = param.paramCombos[i][3].toLowerCase();
				String paramcombo_value = param.paramCombos[i][4].toLowerCase();
				String paramname_title = htParamsTitle.get(paramname_lower);
				
				
				String tmp = paramcolumnsTemplate;
				tmp = tmp.replaceAll("@paramcombo_lower@", paramcombo_lower);
				tmp = tmp.replaceAll("@paramname_lower@", paramname_lower);
				tmp = tmp.replaceAll("@paramname_title@", paramname_title);
				tmp = tmp.replaceAll("@paramcombo_caption@", paramcombo_caption);
				tmp = tmp.replaceAll("@paramcombo_value@", paramcombo_value);
				paramcombos.append(tmp);
				
				userParams.add(paramname_lower);
			}
			
			template.replace(paramcombos_start, paramcombos_end+16, paramcombos.toString());
		}

		int paramcolumns_start = template.indexOf("@@otherparams@@");
		int paramcolumns_end = template.indexOf("@@/otherparams@@",paramcolumns_start);
		if(paramcolumns_start>-1 && paramcolumns_end>-1){
			
			String paramcolumnsTemplate = template.substring(paramcolumns_start+16, paramcolumns_end);
			StringBuffer paramcolumns = new StringBuffer();
			for(int i=0;i<param.paramColumns.length;i++){
				String paramname_lower = param.paramColumns[i].toLowerCase();
				if(!userParams.contains(paramname_lower)){
					String paramname_title = param.paramColumnsTitle[i];
					
					String tmp = paramcolumnsTemplate;
					tmp = tmp.replaceAll("@paramname_lower@", paramname_lower);
					tmp = tmp.replaceAll("@paramname_title@", paramname_title);
					paramcolumns.append(tmp);
				}
			}
			
			template.replace(paramcolumns_start, paramcolumns_end+16, paramcolumns.toString());
		}

		File f = new File(param.jspPath,param.getName().toLowerCase()+"lstparam.jsp");
		f.getParentFile().mkdirs();
		PrintWriter out = new PrintWriter(f,"UTF-8");

		out.print(template.toString());
		
		out.flush();
		out.close();
		
	}

	private void generateJSP(AIPStrutsReportListWithToolbarParam param) throws IOException  {
		StringBuffer template = AIPUtil.readResourceStream(this,"/aip/generator/struts/list/jsp.template");
		AIPUtil.replaceAllString(template, "@packagepath@", param.getPackagePath());
		AIPUtil.replaceAllString(template, "@name@", param.getName());
		AIPUtil.replaceAllString(template, "@title@", param.getTitle());
		AIPUtil.replaceAllString(template, "@name_lower@", param.getName().toLowerCase());
		AIPUtil.replaceAllString(template, "@classfullname@", param.getClassFullName());
		
		
		
		int paramcolumns_start = template.indexOf("@@params@@");
		int paramcolumns_end = template.indexOf("@@/params@@",paramcolumns_start);
		if(paramcolumns_start>-1 && paramcolumns_end>-1){
			
			String paramcolumnsTemplate = template.substring(paramcolumns_start+11, paramcolumns_end);
			StringBuffer paramcolumns = new StringBuffer();
			for(int i=0;i<param.paramColumns.length;i++){
				String columnNameLower = param.paramColumns[i].toLowerCase();
				
				String tmp = paramcolumnsTemplate;
				tmp = tmp.replaceAll("@paramname_lower@", columnNameLower);
				paramcolumns.append(tmp);
			}
			
			template.replace(paramcolumns_start, paramcolumns_end+11, paramcolumns.toString());
		}

		int visiblecolumns_startindex=0;
		for(int counter=0;counter<2;counter++){
			int visiblecolumns_start = template.indexOf("@@visiblecolumns@@",visiblecolumns_startindex);
			int visiblecolumns_end = template.indexOf("@@/visiblecolumns@@",visiblecolumns_start);
			visiblecolumns_startindex=paramcolumns_end;
			if(visiblecolumns_start>-1 && visiblecolumns_end>-1){
				
				String visiblecolumnsTemplate = template.substring(visiblecolumns_start+19, visiblecolumns_end);
				StringBuffer visiblecolumns = new StringBuffer();
				for(int i=0;i<param.visibleColumns.length;i++){
					String visiblecolumn_lower = param.visibleColumns[i][0].toLowerCase();
					if(!"radif".equalsIgnoreCase(visiblecolumn_lower)){
						String visiblecolumn_captital = AIPUtil.change2Capital(visiblecolumn_lower);
						String visiblecolumn_title = param.visibleColumns[i][1];
						
						String tmp = visiblecolumnsTemplate;
						tmp = tmp.replaceAll("@visiblecolumn_lower@", visiblecolumn_lower);
						tmp = tmp.replaceAll("@visiblecolumn_title@", visiblecolumn_title);
						tmp = tmp.replaceAll("@visiblecolumn_capital@", visiblecolumn_captital);
						visiblecolumns.append(tmp);
					}
				}
				
				template.replace(visiblecolumns_start, visiblecolumns_end+19, visiblecolumns.toString());
			}
			
		}
		
		File f = new File(param.jspPath,param.getName().toLowerCase()+"lst.jsp");
		f.getParentFile().mkdirs();
		PrintWriter out = new PrintWriter(f,"UTF-8");

		out.print(template.toString());
		
		out.flush();
		out.close();
		
	}

	private void generateTiles(AIPStrutsReportListWithToolbarParam param,Hashtable<String, String> columnsClassName) throws IOException {
		StringBuffer template = AIPUtil.readResourceStream(this,"/aip/generator/struts/list/TileAction.template");
		AIPUtil.replaceAllString(template, "<packagepath>", param.getPackagePath());
		AIPUtil.replaceAllString(template, "<name>", param.getName());
		
		File f = new File(param.destPath,"T_"+param.getName()+"Action.java");
		PrintWriter out = new PrintWriter(f,"UTF-8");

		out.print(template.toString());
		
		out.flush();
		out.close();
		
	}

	private void generateAction(AIPStrutsReportListWithToolbarParam param,Hashtable<String, String> columnsClassName) throws IOException {
		StringBuffer template = AIPUtil.readResourceStream(this,"/aip/generator/struts/list/Action.template");
		AIPUtil.replaceAllString(template, "<packagepath>", param.getPackagePath());
		AIPUtil.replaceAllString(template, "<name>", param.getName());
		AIPUtil.replaceAllString(template, "<title>", param.getTitle());
		
		
		int paramcolumns_start = template.indexOf("<paramcolumns>");
		int paramcolumns_end = template.indexOf("</paramcolumns>",paramcolumns_start);
		if(paramcolumns_start>-1 && paramcolumns_end>-1){
			
			String paramcolumnsTemplate = template.substring(paramcolumns_start+14, paramcolumns_end);
			StringBuffer paramcolumns = new StringBuffer();
			for(int i=0;i<param.paramColumns.length;i++){
				String columnNameLower = param.paramColumns[i].toLowerCase();
				String columnNameCapital = AIPUtil.change2Capital(columnNameLower);
				String columnClassName = columnsClassName.get(columnNameLower);
				int posLastDot = columnClassName.lastIndexOf('.');
				String columnClassSetter = "set" + columnClassName.substring(posLastDot+1);
				
				String tmp = paramcolumnsTemplate;
				tmp = tmp.replaceAll("<paramcolumn_captital>", columnNameCapital);
				if(columnClassName.indexOf("Integer")>-1){
					tmp = tmp.replaceAll("<paramcolumnclass_nvl>", "Int");
				}else if(columnClassName.indexOf("Long")>-1){
					tmp = tmp.replaceAll("<paramcolumnclass_nvl>", "Lng");
				}else if(columnClassName.indexOf("BigDecimal")>-1 || columnClassName.indexOf("Double")>-1){
					tmp = tmp.replaceAll("<paramcolumnclass_nvl>", "Dbl");
				}else{
					tmp = tmp.replaceAll("<paramcolumnclass_nvl>", "String");
				}
				tmp = tmp.replaceAll("<paramcolumn_lower>", columnNameLower);
				paramcolumns.append(tmp);
				//paramcolumns.append("\r\n");
			}
			
			template.replace(paramcolumns_start, paramcolumns_end+15, paramcolumns.toString());
		}
		
		File f = new File(param.destPath,param.getName()+"Action.java");
		PrintWriter out = new PrintWriter(f,"UTF-8");

		out.print(template.toString());
		
		out.flush();
		out.close();
		
	}

	private void generateDAO(AIPStrutsReportListWithToolbarParam param,Hashtable<String,String> columnsClassName) throws FileNotFoundException, UnsupportedEncodingException {
		File f = new File(param.destPath,param.getName()+"DAO.java");
		PrintWriter out = new PrintWriter(f,"UTF-8");
		
		out.println("package "+param.getPackagePath()+";");
		out.println();
		out.println("import java.io.Serializable;");
		out.println("import java.util.ArrayList;");
		out.println("import java.util.List;");
		out.println();
		out.println("import org.hibernate.Query;");
		out.println("import org.hibernate.Session;");
		out.println();
		out.println("import aip.orm.BaseHibernateDAO;");
		out.println("import aip.util.AIPException;");
		out.println("import aip.util.AIPPrintParam;");
		out.println("import aip.util.AIPUtil;");
		out.println("import aip.util.NVL;");
		out.println("public class "+param.getName()+"DAO  extends BaseHibernateDAO implements "+param.getName()+"Interface {");
		
		out.println();

		/*
		 * LST methods
		 */
		out.println("\tpublic "+param.getName()+"LST get"+param.getName()+"LST("+param.getName()+"LSTParam param) throws AIPException{");
		out.println("\t\t"+param.getName()+"LST lst = new "+param.getName()+"LST();");
		out.println("\t\ttry{");
		out.println("\t\t	Session session = getSession();");
			
		out.println();
		out.println("\t\t	List lstCountSum = session.getNamedQuery(\""+"sql"+param.getClassName()+"_count"+"\")");
		for(int i=0;i<param.paramColumns.length;i++){
			String columnNameLower = param.paramColumns[i].toLowerCase();
			String columnNameCapital = AIPUtil.change2Capital(columnNameLower);
			String columnClassName = columnsClassName.get(columnNameLower);
			int posLastDot = columnClassName.lastIndexOf('.');
			String columnClassSetter = "set" + columnClassName.substring(posLastDot+1);
			String nvlgetter = "";
			if(columnClassName.endsWith("String")){
				nvlgetter+="NVL.getStringNull";
			}else if(columnClassName.endsWith("Double")){
				nvlgetter+="NVL.getDbl";
			}else if(columnClassName.endsWith("Long")){
				nvlgetter+="NVL.getLng";
			}else if(columnClassName.endsWith("Integer")){
				nvlgetter+="NVL.getInt";
			}else if(columnClassName.endsWith("Boolean")){
				nvlgetter+="NVL.getBool";
			}
			out.println("\t\t		."+columnClassSetter+"(\""+columnNameLower+"\", "+ nvlgetter +"( param.get"+columnNameCapital+"()) )");
		}
		out.println("\t\t	.list();");
	
		out.println();
		out.println("\t\t	if(lstCountSum.size()>0){");
		out.println("\t\t		Object ar = lstCountSum.get(0);");
		out.println("\t\t		lst.setTotalItems( NVL.getLng( ar ) );");

		out.println();
		out.println("\t\t		//lst.setSumTotal(\"creditSum\", \"\"+NVL.getLng( ar[1] ) ,\"بدهکار\");");
		out.println("\t\t		//lst.setSumTotal(\"debitSum\", \"\"+NVL.getLng( ar[2] ) ,\"بستانکار\");");
		out.println("\t\t		//lst.setSumTotal(\"remainedSum\", \"\"+NVL.getLng( ar[3] ) ,\"مانده\");");
		out.println("\t\t	}");
			
		out.println();
		out.println("\t\t	lst.setParam(param);");
			
		out.println();
		out.println("\t\t	Query query = session.getNamedQuery(\""+"sql"+param.getClassName()+"_ids"+"\")");
		for(int i=0;i<param.paramColumns.length;i++){
			String columnNameLower = param.paramColumns[i].toLowerCase();
			String columnNameCapital = AIPUtil.change2Capital(columnNameLower);
			String columnClassName = columnsClassName.get(columnNameLower);
			int posLastDot = columnClassName.lastIndexOf('.');
			String columnClassSetter = "set" + columnClassName.substring(posLastDot+1);  
			String nvlgetter = "";
			if(columnClassName.endsWith("String")){
				nvlgetter+="NVL.getStringNull";
			}else if(columnClassName.endsWith("Double")){
				nvlgetter+="NVL.getDbl";
			}else if(columnClassName.endsWith("Long")){
				nvlgetter+="NVL.getLng";
			}else if(columnClassName.endsWith("Integer")){
				nvlgetter+="NVL.getInt";
			}else if(columnClassName.endsWith("Boolean")){
				nvlgetter+="NVL.getBool";
			}
			out.print("\t\t		."+columnClassSetter+"(\""+columnNameLower+"\", "+nvlgetter+"( param.get"+columnNameCapital+"()) )");
			if(i<param.paramColumns.length-1)out.println();
		}
		out.println(";");
			
		out.println();
		out.println("\t\t	List dto_ids=new ArrayList();");
		out.println("\t\t	if(param.getPrintParam().getPrintRange()==AIPPrintParam.RANGE_PAGE_CURRENT){");
		out.println("\t\t		dto_ids = query.setLong(\"firstrow\", lst.getFirstRow()).setLong(\"lastrow\", lst.getLastRow()).list();");
		out.println("\t\t	}else if(param.getPrintParam().getPrintRange()==AIPPrintParam.RANGE_PAGE_ALL){");
		out.println("\t\t		dto_ids = query.setLong(\"firstrow\", 1).setLong(\"lastrow\", lst.getTotalItems()).list();");
		out.println("\t\t	}else{");
		out.println("\t\t		long[][] startsAndEnds=param.getPrintParam().getRequestStartsAndEnds(lst.getPageSize(), lst.getTotalItems());");
		out.println("\t\t		for(int i=0;i<startsAndEnds.length;i++){");
		out.println("\t\t			List ids = query.setLong(\"firstrow\", startsAndEnds[i][0]).setLong(\"lastrow\", startsAndEnds[i][1]).list();");
		out.println("\t\t			dto_ids.addAll(ids);");
		out.println("\t\t		}");
		out.println("\t\t	}");
			
		out.println();
		out.println("\t\t	List<"+param.getClassName()+"> dtos = new ArrayList<"+param.getClassName()+">();");
			
		out.println();
		out.println("\t\t	long creditSumPage=0,debitSumPage=0,remainedSumPage=0;");
		out.println("\t\t	for(int i=0;i<dto_ids.size();i++){");
		String idColumnClass = columnsClassName.get(param.getIdColumn());
		String idGetter ;
		if(idColumnClass.indexOf("Integer")>-1){
			idGetter="NVL.getInt(dto_ids.get(i))";
		}else if(idColumnClass.indexOf("Long")>-1){
			idGetter="NVL.getLng(dto_ids.get(i))";
		}else{
			idGetter="NVL.getDbl(dto_ids.get(i))";
		}
		out.println("\t\t		"+param.getClassName()+" dto = ("+param.getClassName()+") this.get(session,"+param.getClassName()+".class, (Serializable)"+idGetter+");");
		out.println("\t\t		if(dto!=null){");
		out.println("\t\t			dtos.add( dto );");
					
		out.println();
		out.println("\t\t			//creditSumPage+=NVL.getLng( dto.getCredit() );");
		out.println("\t\t			//debitSumPage+=NVL.getLng( dto.getDebit() );");
		out.println("\t\t			//remainedSumPage+=NVL.getLng( dto.getRemained() );");
		out.println("\t\t		}");
		out.println("\t\t	}");
		out.println("\t\t	lst.setRows( dtos );");

		out.println();
		out.println("\t\t	//lst.setSumPage(\"creditSumPage\", \"\"+creditSumPage,\"بدهکار\");");
		out.println("\t\t	//lst.setSumPage(\"debitSumPage\", \"\"+debitSumPage,\"بستانکار\");");
		out.println("\t\t	//lst.setSumPage(\"remainedSumPage\", \"\"+remainedSumPage,\"مانده\");");

			
		out.println();
		out.println("\t\t	lst.setParamString( getParamString(param) );");
		out.println("\t\t}catch (Exception e) {");
		out.println("\t\t	e.printStackTrace();");
		out.println("\t\t	throw new AIPException(e);");
		out.println("\t\t}");
		out.println("\t\treturn lst;");

		out.println();
		out.println("\t}");

		
		/*
		 * getParamString methods
		 */
		out.println();
		out.println("\tprivate String getParamString("+param.getName()+"LSTParam param) {");
		out.println("\t	StringBuffer sb = new StringBuffer();");
		out.println("\t	//CommonDAO dao = new CommonDAO();");
		out.println("\t	//sb.append( dao.getParamStringBankType(param.getBankTypeId()) );");
			
		out.println();
		out.println("\t	return sb.toString();");
		out.println("\t}");
		
		
		
		
		/*
		 * ComboLST
		 */
		out.println();
		out.println("\tpublic "+param.getName()+"ComboLST get"+param.getName()+"ComboLST() throws AIPException {");
		out.println("\t	"+param.getName()+"ComboLST comboLST = new "+param.getName()+"ComboLST();");
		out.println("\t	try {");
				
		out.println();
		out.println("\t		Session session = getSession();");
		for(int i=0;i<param.paramCombos.length;i++){
			String comboNameLower = param.paramCombos[i][0].toLowerCase();
			String comboNameCapital = AIPUtil.change2Capital(comboNameLower);
			out.println("\t		comboLST.set"+comboNameCapital+"(session.createQuery(\"from "+param.paramCombos[i][1]+"\").list());");
		}
		out.println();
		out.println("\t	} catch (Exception e) {");
		out.println("\t		e.printStackTrace();");
		out.println("\t		throw new AIPException(e);");
		out.println("\t	}");
		out.println("\t	return comboLST;");
		out.println("\t}");
		
		out.println();
		out.println("}");

		
		out.flush();
		out.close();
	}

	private void generateComboLST(AIPStrutsReportListWithToolbarParam param)throws FileNotFoundException {
		File f = new File(param.destPath,param.getName()+"ComboLST.java");
		PrintWriter out = new PrintWriter(f);
		
		out.println("package "+param.getPackagePath()+";");
		out.println();
		out.println("import java.util.ArrayList;");
		out.println("import java.util.List;");
		out.println();
		out.println("public class "+param.getName()+"ComboLST {");
		
		out.println();
		
		for(int i=0;i<param.paramCombos.length;i++){
			out.println("\tprivate List<"+param.paramCombos[i][1]+"> "+param.paramCombos[i][0].toLowerCase()+" = new ArrayList<"+param.paramCombos[i][1]+">();");
		}
		
		out.println();
		
		for(int i=0;i<param.paramCombos.length;i++){
			String columnNameLower = param.paramCombos[i][0].toLowerCase();
			String columnClass = param.paramCombos[i][1];
			String columnNameCapital = AIPUtil.change2Capital(columnNameLower);
			out.println("\tpublic List<"+columnClass+"> get"+columnNameCapital+"() {");
			out.println("\t	return "+columnNameLower+";");
			out.println("\t}");
			out.println("\tpublic void set"+columnNameCapital+"(List<"+columnClass+"> "+columnNameLower+") {");
			out.println("\t	this."+columnNameLower+" = "+columnNameLower+";");
			out.println("\t}");
			out.println();
		}
		
		out.println("}");
		
		out.flush();
		out.close();
	}

	private void generateLST(AIPStrutsReportListWithToolbarParam param)throws FileNotFoundException, UnsupportedEncodingException  {
		File f = new File(param.destPath,param.getName()+"LST.java");
		PrintWriter out = new PrintWriter(f,"UTF-8");
		
		out.println("package "+param.getPackagePath()+";");
		out.println();
		out.println("import java.util.List;");
		out.println("import aip.report.AIPReportLST;");
		out.println();
		out.println("public class "+param.getName()+"LST  extends AIPReportLST {");
		
		out.println();
		
		out.println("\tpublic "+param.getName()+"LSTParam getParam() {");
		out.println("\t	return ("+param.getName()+"LSTParam) param;");
		out.println("\t}");
		out.println("\tpublic List<"+param.getClassName()+"> getRows() {");
		out.println("\t	return rows;");
		out.println("\t}");
		
		out.println("\t@Override");
		out.println("\tprotected void initVisibleColumns() {");
		for(int i=0;i<param.visibleColumns.length;i++){
			out.println("\t	setVisibleColumn(\""+param.visibleColumns[i][0].toLowerCase()+"\", \""+param.visibleColumns[i][1]+"\");");
		}
		out.println("\t}");
		
		out.println();
		
		out.println("}");
		
		out.flush();
		out.close();
	}

	private void generateParam(AIPStrutsReportListWithToolbarParam param,Hashtable<String,String> columnsClassName) throws FileNotFoundException {
		File f = new File(param.destPath,param.getName()+"LSTParam.java");
		PrintWriter out = new PrintWriter(f);
		
		out.println("package "+param.getPackagePath()+";");
		out.println();
		out.println("import aip.report.AIPReportParam;");
		out.println();
		out.println("public class "+param.getName()+"LSTParam  extends AIPReportParam {");
		
		out.println();
		
		for(int i=0;i<param.paramColumns.length;i++){
			out.println("\tprivate "+columnsClassName.get(param.paramColumns[i].toLowerCase())+" "+param.paramColumns[i].toLowerCase()+";");
		}
		
		out.println();
		
		for(int i=0;i<param.paramColumns.length;i++){
			String columnNameLower = param.paramColumns[i].toLowerCase();
			String columnClass = columnsClassName.get(columnNameLower);
			String columnNameCapital = AIPUtil.change2Capital(columnNameLower);
			out.println("\tpublic "+columnClass+" get"+columnNameCapital+"() {");
			out.println("\t	return "+columnNameLower+";");
			out.println("\t}");
			out.println("\tpublic void set"+columnNameCapital+"("+columnClass+" "+columnNameLower+") {");
			out.println("\t	this."+columnNameLower+" = "+columnNameLower+";");
			out.println("\t}");
			out.println();
		}
		
		out.println("}");
		
		out.flush();
		out.close();
	}

	private void generateInterface(AIPStrutsReportListWithToolbarParam param) throws FileNotFoundException {
		File f = new File(param.destPath,param.getName()+"Interface.java");
		PrintWriter out = new PrintWriter(f);
		
		out.println("package "+param.getPackagePath()+";");
		out.println();
		out.println("import aip.util.AIPException;");
		out.println();
		out.println("public interface "+param.getName()+"Interface {");
		
		out.println("	public "+param.getName()+"LST get"+param.getName()+"LST("+param.getName()+"LSTParam param) throws AIPException;");
		if(param.paramCombos.length>0 || param.hasGeneration(param.GENERATION_COMBOLST)){
			out.println("	public "+param.getName()+"ComboLST   get"+param.getName()+"ComboLST() throws AIPException;");
		}
		out.println("}");
	
		out.flush();
		out.close();
	}

	private void generateEntityClass(AIPStrutsReportListWithToolbarParam param,Hashtable<String,String> columnsClassName) throws FileNotFoundException {
		File f = new File(param.destPath,param.getClassName()+".java");
		PrintWriter out = new PrintWriter(f);
		
		out.println("package "+param.getPackagePath()+";");
		out.println();
		out.println("import aip.orm.AIPBaseEntity;");
		out.println();
		out.println("public class "+param.getClassName()+" extends AIPBaseEntity {");
		
		String[] columnsName = columnsClassName.keySet().toArray(new String[0]);
		String[] columnsClass = columnsClassName.values().toArray(new String[0]);

		out.println();
		for(int i=0;i<columnsName.length;i++){
			out.println("\tprivate "+columnsClass[i]+" "+columnsName[i]+";");
		}
		
		out.println();
		for(int i=0;i<columnsName.length;i++){
			String columnNameCapital = AIPUtil.change2Capital(columnsName[i]);
			out.println("\tpublic "+columnsClass[i]+" get"+columnNameCapital+"() {");
			out.println("\t	return "+columnsName[i]+";");
			out.println("\t}");
			out.println("\tpublic void set"+columnNameCapital+"("+columnsClass[i]+" "+columnsName[i]+") {");
			out.println("\t	this."+columnsName[i]+" = "+columnsName[i]+";");
			out.println("\t}");
			out.println();
		}
		
		out.println("}");

		out.flush();
		out.close();
		
	}

	private void generateHbm(AIPStrutsReportListWithToolbarParam param,ResultSetMetaData meta,int idColumnIndex,Hashtable<String,String> columnsClassName) throws FileNotFoundException, SQLException {
		File f = new File(param.destPath,param.getClassName()+".hbm.xml");
		f.getParentFile().mkdirs();
		PrintWriter out = new PrintWriter(f);
		
		out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		out.println("<!DOCTYPE hibernate-mapping PUBLIC \"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"");
		out.println("\"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\">");
		out.println("<!-- ");
		out.println("    Mapping file autogenerated by AIPGenerator Tools");
		out.println("-->");
		out.println("<hibernate-mapping>");
		out.println("\n");
		out.println("    <class name=\""+param.getClassFullName()+"\" dynamic-insert=\"false\" dynamic-update=\"false\" >");

		
		
		
		out.println("\t\t<id name=\""+param.idColumn+"\" type=\""+getColumnClassName(meta,idColumnIndex)+"\">");
		out.println("\t\t	<column name=\""+param.idColumn+"\" not-null=\"true\" />");
		out.println("\t\t	<generator class=\""+param.idGeneratorClass+"\" />");
		out.println("\t\t</id>");
		
		for(int column=1;column<=meta.getColumnCount();column++){
			if(column!=idColumnIndex){
				
				out.println("\t\t<property name=\""+meta.getColumnName(column).toLowerCase()+"\" type=\""+getColumnClassName(meta,column)+"\">");
				out.println("\t\t	<column name=\""+meta.getColumnName(column)+"\" not-null=\""+(meta.isNullable(column)==meta.columnNoNulls?"true":"false")+"\" />");
				out.println("\t\t</property>");
			}
		}

		out.println();
		String sqlLoaderName = "sql"+param.getClassName()+"_loader";
		out.println("\t\t<loader query-ref=\""+sqlLoaderName+"\"/>");
		
		out.println("\t</class>");
		out.println();
		out.println("\t<sql-query name=\""+sqlLoaderName+"\">");
		out.println("\t	<return class=\""+param.getClassFullName()+"\">");
		out.println("\t	</return>");
		out.print("\t\t");
		out.println(param.loaderSQL);
		out.println("\t</sql-query>");
		

		String sqlIdsName = "sql"+param.getClassName()+"_ids";
		out.println();
		out.println("\t<sql-query name=\""+sqlIdsName+"\">");
		out.println("\t	SELECT "+param.idColumn+" FROM ");
		out.println("\t	(SELECT "+param.idColumn+",ROWNUM RNUM FROM ");
		out.println("\t	(SELECT "+param.idColumn+" FROM "+param.tableName);
		out.println("\t\t WHERE ");
		if(param.paramColumns.length>0){
			for(int i=0;i<param.paramColumns.length;i++){
				String paramColumnName = param.paramColumns[i];
				String paramColumnName_lower = paramColumnName.toLowerCase();
				String columnClassName = columnsClassName.get(paramColumnName.toLowerCase());
				if(!NVL.isEmpty(columnClassName)){
					out.print("\t\t");
					if(columnClassName.indexOf("Integer")>0 || columnClassName.indexOf("Long")>0 || columnClassName.indexOf("Double")>0 || columnClassName.indexOf("BigDecimal")>0 ){
						out.println("(0=:"+paramColumnName_lower+" or "+paramColumnName+"=:"+paramColumnName_lower+")");
					}else if(columnClassName.indexOf("String")>0){
						out.println("(:"+paramColumnName_lower+" is null or "+paramColumnName+" like :"+paramColumnName_lower+")");
					}else if(columnClassName.indexOf("Boolean")>0){
						out.println("("+paramColumnName+"=:"+paramColumnName_lower+")");
					}else{
						out.println("("+paramColumnName+"=:"+paramColumnName_lower+")");
					}
				}
				if(i<param.paramColumns.length-1){
					out.print("\t\t AND  ");
				}
			}
		}
		out.println("\t\t ORDER BY "+param.idColumn+" DESC ");
		out.println("\t\t ) T1 where ROWNUM &lt;= :lastrow ");
		out.println("\t\t ) T  where RNUM>:firstrow ");
		out.println("\t</sql-query>");
		
		String sqlCountName = "sql"+param.getClassName()+"_count";
		out.println("\t<sql-query name=\""+sqlCountName+"\">");
		out.println("\t	 SELECT COUNT(*) RowCount FROM "+param.tableName);
		if(param.paramColumns.length>0){
			out.println("\t\t WHERE ");
			for(int i=0;i<param.paramColumns.length;i++){
				String paramColumnName = param.paramColumns[i];
				String paramColumnName_lower = paramColumnName.toLowerCase();
				String columnClassName = columnsClassName.get(paramColumnName.toLowerCase());
				if(!NVL.isEmpty(columnClassName)){
					out.print("\t\t");
					if(columnClassName.indexOf("Integer")>0 || columnClassName.indexOf("Long")>0 || columnClassName.indexOf("Double")>0 || columnClassName.indexOf("BigDecimal")>0 ){
						out.println("(0=:"+paramColumnName_lower+" or "+paramColumnName+"=:"+paramColumnName_lower+")");
					}else if(columnClassName.indexOf("String")>0){
						out.println("(:"+paramColumnName_lower+" is null or "+paramColumnName+" like :"+paramColumnName_lower+")");
					}else if(columnClassName.indexOf("Boolean")>0){
						out.println("("+paramColumnName+"=:"+paramColumnName_lower+")");
					}else{
						out.println("("+paramColumnName+"=:"+paramColumnName_lower+")");
					}
				}
				if(i!=param.paramColumns.length-1){
					out.print("\t\t AND  ");
				}
			}
		}
	    out.println("\t</sql-query>");


	    out.println();
	    out.println("</hibernate-mapping>");
	    
		out.flush();
		out.close();
	}

	private void generateHbmENT(AIPStrutsReportListWithToolbarParam param,ResultSetMetaData meta,int idColumnIndex,Hashtable<String,String> columnsClassName) throws FileNotFoundException, SQLException {
		File f = new File(param.destPath,param.getClassName()+".hbm.xml");
		f.getParentFile().mkdirs();
		PrintWriter out = new PrintWriter(f);
		
		out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		out.println("<!DOCTYPE hibernate-mapping PUBLIC \"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"");
		out.println("\"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\">");
		out.println("<!-- ");
		out.println("    Mapping file autogenerated by AIPGenerator Tools");
		out.println("-->");
		out.println("<hibernate-mapping>");
		out.println("\n");
		out.println("    <class name=\""+param.getClassFullName()+"\" table=\""+param.getTableName()+"\" >");
		
		out.println("\t\t<id name=\""+param.idColumn+"\" type=\""+getColumnClassName(meta,idColumnIndex)+"\">");
		out.println("\t\t	<column name=\""+param.idColumn+"\" not-null=\"true\" />");
		out.println("\t\t	<generator class=\""+param.idGeneratorClass+"\" />");
		out.println("\t\t</id>");
		
		for(int column=1;column<=meta.getColumnCount();column++){
			if(column!=idColumnIndex){
				
				out.println("\t\t<property name=\""+meta.getColumnName(column).toLowerCase()+"\" type=\""+getColumnClassName(meta,column)+"\">");
				out.println("\t\t	<column name=\""+meta.getColumnName(column)+"\" not-null=\""+(meta.isNullable(column)==meta.columnNoNulls?"true":"false")+"\" />");
				out.println("\t\t</property>");
			}
		}

		out.println("\t</class>");

	    out.println();
	    out.println("</hibernate-mapping>");
	    
		out.flush();
		out.close();
	}
	

	private String getColumnClassName(ResultSetMetaData meta, int column) throws SQLException {
		String className = meta.getColumnClassName(column);
		if(className.indexOf("BigDecimal")>-1){
			int scale = meta.getScale(column);
			int precision = meta.getPrecision(column);
			if(scale>0){
				className="java.lang.Double";
			}else if(precision>9){
				className="java.lang.Long";
			}else{
				className="java.lang.Integer";
			}
		}
		return className;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AIPStrutsReportListWithToolbarGEN gen = new AIPStrutsReportListWithToolbarGEN();
		AIPStrutsReportListWithToolbarParam param=new AIPStrutsReportListWithToolbarParam();
		
		param.name="User2";
		param.title="لیست کاربران";
		param.destPath="E:/java/workspace6.5/AIPNIOPDCSIB/src/aip/niopdc/sib/security/user2";
		param.jspPath="E:/java/workspace6.5/AIPNIOPDCSIB/WebRoot/form/security/user2";
		param.tableName="SIB.BankAccount";
		param.loaderSQL="select b.ID,b.AccountNumber,b.F_BankTypeID,bt.Caption As BANKTYPE,\n" +
				" b.BranchCode,b.BranchName,b.Phone,b.Fax,b.Address,\n" +
				" b.F_LocationID_Nahieh as ZoneID,l.Caption as ZONENAME,\n" +
				" l2.Caption as RegionName,l2.ID as RegionID,\n" +
				" b.F_AccountTypeID ,a.Caption as AccountType\n" +
				" ,b.IsActive\n" +
				" from SIB.BankAccount b\n" +
				" left outer join SIB.BankType bt	ON b.F_BankTypeID=bt.ID\n" +
				" left outer join SIB.Location l	ON b.F_LocationID_Nahieh=l.ID\n" +
				" left outer join SIB.Location l2	ON  l.F_ParentID=l2.ID\n" +
				" left outer join SIB.AccountType a ON b.F_AccountTypeID=a.ID\n" +
				" where b.ID=? \n";
		
		param.idColumn="id";
		param.idGeneratorClass=AIPStrutsReportListWithToolbarParam.IDGEN_ASSIGNED;
		
		param.connectionDriver="oracle.jdbc.driver.OracleDriver";
		param.connectionString="jdbc:oracle:thin:@192.168.0.195:1521:AIP11g";
		param.connectionUser="sib";
		param.connectionPassword="sib";
		
		/*
		 * columnName should exists in loaderSQL
		 * todo: count and ids querys generate without joins if exists manually handled
		 */
		param.paramColumns=new String[]{"F_BankTypeID","F_AccountTypeID","RegionID","ZoneID","AccountNumber"};
		param.paramColumnsTitle=new String[]{"بانک","نوع حساب","منطقه","ناحیه","شماره حساب"};
		param.visibleColumns=new String[][]{
				{"radif","ردیف"}
				,{"accountnumber", "شماره حساب"}
				,{"bankType", "بانک"}
				,{"branchCode", "کد شعبه"}
				,{"branchName", "نام شعبه"}
				,{"phone", "تلفن"}
				,{"fax", "فاکس"}
				,{"address", "آدرس"}
				,{"zoneName", "ناحیه"}
				,{"regionName", "منطقه"}
				,{"isActive", "فعال"}
				,{"accountType", "نوع حساب"}
		};
		/*
		 * columnName,className,sql
		 * note: if sql not empty generate HBM and class related that
		 * todo: class packages should imported manually
		 * todo: if any relation between manually exists should manually handled
		 * 0 name in ComboLST
		 * 1 class for List<class>
		 * 2 related field in params
		 * 3 display filed in class
		 * 4 value filed in class
		 */
		param.paramCombos=new String[][]{
				{"bankTypes","BankTypeDTO","F_BankTypeID","caption","id"}
				,{"accountTypes","AccountTypeDTO","F_AccountTypeID","caption","id"}
				,{"regions","LocationDTO","RegionID","caption","id"}
				,{"zones","LocationDTO","ZoneID","caption","id"}
		};
		
		param.generation = AIPStrutsReportListWithToolbarParam.GENERATION_FULL;
		//param.generation = AIPStrutsReportListWithToolbarParam.GENERATION_JSPPARAM;
		//param.generation = AIPStrutsReportListWithToolbarParam.GENERATION_TILES+AIPStrutsReportListWithToolbarParam.GEN_UPD_TILES;
		//param.generation = AIPStrutsReportListWithToolbarParam.GENERATION_FULL-param.GEN_UPD_HIBERNATE-param.GEN_UPD_STRUTS-param.GEN_UPD_TILES;
		//param.generation = AIPStrutsReportListWithToolbarParam.GENERATION_GENERATOR;
//		param.generation = AIPStrutsReportListWithToolbarParam.GEN_DAO_GETENTITY;
//		param.generation = AIPStrutsReportListWithToolbarParam.GEN_DAO_SAVEENTITY;
//		param.generation = AIPStrutsReportListWithToolbarParam.GEN_ACTION_EDITENTITY;
//		param.generation = AIPStrutsReportListWithToolbarParam.GEN_ACTION_SAVEENTITY;
//		param.generation = AIPStrutsReportListWithToolbarParam.GEN_STRUTSFORWARD_EDITENTITY;
		param.generation = AIPStrutsReportListWithToolbarParam.GEN_JSP_EDITENTITY;
				
		try {
			gen.generateCode(param);
		} catch (AIPException e) {
			e.printStackTrace();
		}

	}
	
}
