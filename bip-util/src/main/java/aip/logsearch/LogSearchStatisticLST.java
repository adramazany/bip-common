package aip.logsearch;

import java.util.List;
import aip.report.AIPReportLST;

public class LogSearchStatisticLST  extends AIPReportLST {

	public LogSearchStatisticLSTParam getParam() {
		return (LogSearchStatisticLSTParam) param;
	}
	public List<LogSearchStatisticDTO> getRows() {
		return rows;
	}
	@Override
	protected void initVisibleColumns() {
		setVisibleColumn("word", "کلمه");
		setVisibleColumn("wcount", "مجموع");
	}

}
