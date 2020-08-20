1.add these actions to struts-config-tiles.xml
	<action parameter="page.logsearch" path="/t_logsearch" scope="request" 
		type="org.apache.struts.actions.ForwardAction"/> 
	<action parameter="page.searchstat" path="/t_searchstat" scope="request" 
		type="org.apache.struts.actions.ForwardAction"/> 

2.add these definition to tiles-def.xml
	<definition name="page.logsearch" extends="base.definition"> 
		<put name="title" value="Log Search" /> 
		<put name="body" value="/logsearch.do" /> 
	</definition> 
	<definition name="page.searchstat" extends="base.definition"> 
		<put name="title" value="گزارش آماری جستجو" /> 
		<put name="body" value="/searchstat.do" /> 
	</definition> 


3.add these actions to struts-config.xml
	<action  
		parameter="reqCode" 
		path="/logsearch" 
		scope="request" 
		type="aip.logsearch.AIPLogSearchAction"> 
		<forward name="view" path="/form/LogSearch/logsearchlst.jsp" /> 
		<forward name="restrictedUser" path="/layout/index.jsp" /> 
		<forward name="ShowParamDialog" path="/form/LogSearch/logsearchlstparam.jsp" /> 
	</action> 
	<action  
		parameter="reqCode" 
		path="/searchstat" 
		scope="request" 
		type="aip.logsearch.AIPLogSearchStatisticAction"> 
		<forward name="view" path="/form/LogSearch/logsearchstatisticlst.jsp" /> 
		<forward name="restrictedUser" path="/layout/index.jsp" /> 
		<forward name="ShowParamDialog" path="/form/LogSearch/logsearchstatisticlstparam.jsp" /> 
	</action> 
	
4.add these files to /WebRoot/form/LogSearch
	logsearchlst.jsp
	logsearchlstparam.jsp
	logsearchstatisticlst.jsp
	logsearchstatisticlstparam.jsp

5.use LogSearchDAO.logSearch(param.getSearchtext(),param.getWebUserParam()) for logging

6.use http://.../searchstat.do for statistics on search

7.use http://.../logsearch.do for logsearch detail
 