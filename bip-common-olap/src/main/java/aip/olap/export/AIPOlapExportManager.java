package aip.olap.export;


public class AIPOlapExportManager {
	public static AIPOlapExport getExcel(){
		return new AIPOlapExportExcel();
	}
	public static AIPOlapExport getPdf(){
		return new AIPOlapExportPdf();
	}

}
