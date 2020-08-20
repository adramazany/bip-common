package aip.migrate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class AIPMigrateXMLInfo {

	String tablename;
	boolean checkExist;
	boolean hasLOB;
	int destdbtype;
	String srcQuery;
	String destSQL;
	String checkExistQuery;
	String existSQL;
	boolean existSQLEnable;
	
	String beforeMigrateExecOnSrc;

	String beforeMigrateExecOnDest;
	
	int batchLength;
	int transferPrintLength;
	
	String pk;
	String destClobClause;
	
	List<AIPMigrateXMLColumn> columns = new ArrayList<AIPMigrateXMLColumn>();

	private List<AIPMigrateXMLColumn> getColumnsSorted(){
		Collections.sort(columns, new Comparator<AIPMigrateXMLColumn>() {
				public int compare(AIPMigrateXMLColumn o1, AIPMigrateXMLColumn o2) {
					return compareColumnByLength(o1.getColumn_src(), o2.getColumn_src());
				}
			}
		);
		return columns;
	}
	
	public String[] getSrcColumns() {
		List<AIPMigrateXMLColumn> columns_sorted = getColumnsSorted();
		ArrayList<String> srcColumns = new ArrayList<String>();
		for (int i = 0; i < columns_sorted.size(); i++) {
			if(!columns_sorted.get(i).isIslob()){
				srcColumns.add(columns_sorted.get(i).getColumn_src());
			}
		}
		return srcColumns.toArray(new String[0]);
	} 

	public String[] getDestColumns() {
		List<AIPMigrateXMLColumn> columns_sorted = getColumnsSorted();
		ArrayList<String> destColumns = new ArrayList<String>();
		for (int i = 0; i < columns_sorted.size(); i++) {
			if(!columns_sorted.get(i).isIslob()){
				destColumns.add(columns_sorted.get(i).getColumn_dest());
			}
		}
		return destColumns.toArray(new String[0]);
	} 

	public String[] getSrcClobColumns() {
		List<AIPMigrateXMLColumn> columns_sorted = getColumnsSorted();
		ArrayList<String> srcColumns = new ArrayList<String>();
		for (int i = 0; i < columns_sorted.size(); i++) {
			if(columns_sorted.get(i).isIslob()){
				srcColumns.add(columns_sorted.get(i).getColumn_src());
			}
		}
		return srcColumns.toArray(new String[0]);
	} 

	public String[] getDestClobColumns() {
		List<AIPMigrateXMLColumn> columns_sorted = getColumnsSorted();
		ArrayList<String> destColumns = new ArrayList<String>();
		for (int i = 0; i < columns_sorted.size(); i++) {
			if(columns_sorted.get(i).isIslob()){
				destColumns.add(columns_sorted.get(i).getColumn_dest());
			}
		}
		return destColumns.toArray(new String[0]);
	} 

	
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public boolean isCheckExist() {
		return checkExist;
	}
	public void setCheckExist(boolean checkExist) {
		this.checkExist = checkExist;
	}
	public boolean isHasLOB() {
		return hasLOB;
	}
	public void setHasLOB(boolean hasLOB) {
		this.hasLOB = hasLOB;
	}
	public int getDestdbtype() {
		return destdbtype;
	}
	public void setDestdbtype(int destdbtype) {
		this.destdbtype = destdbtype;
	}
	public String getSrcQuery() {
		return srcQuery;
	}
	public void setSrcQuery(String srcQuery) {
		this.srcQuery = srcQuery;
	}
	public String getDestSQL() {
		return destSQL;
	}
	public void setDestSQL(String destSQL) {
		this.destSQL = destSQL;
	}
	public String getCheckExistQuery() {
		return checkExistQuery;
	}
	public void setCheckExistQuery(String checkExistQuery) {
		this.checkExistQuery = checkExistQuery;
	}
	public String getExistSQL() {
		return existSQL;
	}
	public void setExistSQL(String existSQL) {
		this.existSQL = existSQL;
	}
	public List<AIPMigrateXMLColumn> getColumns() {
		return columns;
	}
	public void setColumns(List<AIPMigrateXMLColumn> columns) {
		this.columns = columns;
	}
	public boolean isExistSQLEnable() {
		return existSQLEnable;
	}

	public void setExistSQLEnable(boolean existSQLEnable) {
		this.existSQLEnable = existSQLEnable;
	}
	public String getBeforeMigrateExecOnSrc() {
		return beforeMigrateExecOnSrc;
	}

	public void setBeforeMigrateExecOnSrc(String beforeMigrateExecOnSrc) {
		this.beforeMigrateExecOnSrc = beforeMigrateExecOnSrc;
	}


	
	
	public String getBeforeMigrateExecOnDest() {
		return beforeMigrateExecOnDest;
	}

	public void setBeforeMigrateExecOnDest(String beforeMigrateExecOnDest) {
		this.beforeMigrateExecOnDest = beforeMigrateExecOnDest;
	}

	public int getBatchLength() {
		return batchLength;
	}

	public void setBatchLength(int batchLength) {
		this.batchLength = batchLength;
	}

	
	
	public int getTransferPrintLength() {
		return transferPrintLength;
	}

	public void setTransferPrintLength(int transferPrintLength) {
		this.transferPrintLength = transferPrintLength;
	}

	private int compareColumnByLength(String o1, String o2) {
		if(o1.length()>o2.length()){
			return -1;
		}else if(o1.length()<o2.length()){
			return 1;
		}
		return 0;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getDestClobClause() {
		return destClobClause;
	}

	public void setDestClobClause(String destClobClause) {
		this.destClobClause = destClobClause;
	}

	
}
