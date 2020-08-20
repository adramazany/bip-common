package aip.logsearch;

import java.util.List;
import aip.report.AIPReportLST;

public class LogSearchLST  extends AIPReportLST {

	public LogSearchLSTParam getParam() {
		return (LogSearchLSTParam) param;
	}
	public List<LogSearchDTO> getRows() {
		return rows;
	}
	@Override
	protected void initVisibleColumns() {
		setVisibleColumn("logdate", "تاریخ");
		setVisibleColumn("time", "ساعت");
		setVisibleColumn("word", "کلمه");
		setVisibleColumn("sentence", "جمله");
		setVisibleColumn("username", "کاربر");
	}

}
