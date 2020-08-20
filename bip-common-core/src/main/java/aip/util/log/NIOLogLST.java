package aip.util.log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NIOLogLST implements Serializable {
	
	private static final long serialVersionUID = 1L;
	List<NIOLogENT> logENTs = new ArrayList<NIOLogENT>();
	int totalRows;
	int currentPage;
	int pageSize;

	public List<NIOLogENT> getLogENTs() {
		return logENTs;
	}

	public void setLogENTs(List<NIOLogENT> logENTs) {
		this.logENTs = logENTs;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
