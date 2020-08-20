package aip.util.log;

import java.util.List;

import aip.report.AIPReportLST;
import aip.report.AIPReportParam;

public class AIPLogLST extends AIPReportLST {

	public AIPLogParam getParam() {
		return (AIPLogParam) super.getParam();
	}
	public List<AIPLogENT> getRows() {
		return super.getRows();
	}
	protected void initVisibleColumns() {
		

	}

}
