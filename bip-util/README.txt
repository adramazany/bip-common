version 1.0.8
BUG: خروجی اکسل گزارشهای یک سطری مشکل دارد - یک ستون جابجا می افتد
	AIPOlapExportExcel.export
______________
version 1.0.7
BUG: کارهایی زمانبندی شده برای انتقال اطلاعات انجام نمی شود
	AIPCronScheluderJobServlet.cronExpression not set in trigger
______________
version 1.0.6
BUG: لیست استان ها و شهرستانها بجای اینکه زیر هم قرار گیرند به ترتیب اول استانها و سپس شهرستانها قرار می گیرند
	گزارش مورخ 20/12/94 خانم مهرآبادی
______________
version 1.0.5
BUG: NVL.getDbl was corrupted comma seperated string numbers

______________
version 1.0.4
BUG: java.lang.IndexOutOfBoundsException: Index: 1, Size: 0 / at BIPOlapUtil._convertCellSet2Tree(BIPOlapUtil.java:67)


version 1.0.3
______________
*.add PersianCollator for correctly sorting persian Gachpazh
*.add BIPUtil extends AIPUtil
*.add BIPUtil.sortPersianListOfList for using in Olap projects for sorting result in reports
*.add BIPUtil extends AIPUtil
*.add BIPDBUtil extends AIPDBUtil
*.add BIPOlapUtil extends AIPOlapUtil
*.add BIPLog extends AIPLog
*.add NVL extends aip.util.NVL
*.add bip.collections.Tree
*.add bip.collections.Node
*.add BIPOlapUtil.convertCellSet2Tree
*.add AIPOlapExportExcel.export(List<List<String>> values
*.add AIPOlapExportPdf.export(List<List<String>> values

* 

version 1.0.2
______________
*.caclulating last mdx processTime 
*.deprecated AIPOlapUtil.executeMdx(String mdx)
*.deprecated AIPOlapUtil.executeMdx2JSArray(String mdx)
*.depreceted AIPOlapUtil.executeMdx(String url,String catalog,String username,String password,String mdx)
*.deprecated AIPOlapUtil.executeMdx2JSArray(String url,String catalog,String username,String password,String mdx)
*.deprecated AIPOlapUtil(String url, String catalog, String username, String password)
*.adding junit
version 1.0.1
______________
1.MondrianCubeFlushSpringJob.java created for mondrian cache clearing with spring quartz jobs
2.AIPDBUtil._getDataSource clear error printing
3.bip.util.el package added includes : 
	BIPELEvaluator,BIPELEvaluatorException,BIPELResolver,BIPFunctionMapper,BIPVariableMapper
4.bip.util.jsf package added includes :
	BIPFacesUtil,UserLoginBean
4.bip.util.spring package added includes :
	BIPSpringUtil
	