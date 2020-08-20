package aip.db;

import java.util.ArrayList;
import java.util.List;

public class AIPDBTable {
	String tablename;
	List<AIPDBColumn> columns = new ArrayList<AIPDBColumn>();
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public List<AIPDBColumn> getColumns() {
		return columns;
	}
	public void setColumns(List<AIPDBColumn> columns) {
		this.columns = columns;
	}

}
