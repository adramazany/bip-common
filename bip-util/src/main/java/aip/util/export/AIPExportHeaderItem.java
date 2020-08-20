package aip.util.export;

import java.io.Serializable;

import org.apache.poi.ss.util.CellRangeAddress;

public class AIPExportHeaderItem implements Serializable{
	CellRangeAddress range;
	String value;
	int width;
	int height;
	boolean nowrap;
	
	public AIPExportHeaderItem() {}
	public AIPExportHeaderItem(String value,CellRangeAddress range) {
		this.range=range;
		this.value=value;
	}
	public AIPExportHeaderItem(String value,int firstRow, int firstCol) {
		this(value,firstRow, firstRow, firstCol, firstCol);
	}
	public AIPExportHeaderItem(String value,int firstRow, int lastRow, int firstCol, int lastCol) {
		this(value, firstRow, lastRow, firstCol, lastCol, 0);
	}
	public AIPExportHeaderItem(String value,int firstRow, int lastRow, int firstCol, int lastCol,int width) {
		this(value, firstRow, lastRow, firstCol, lastCol, width, 0, false);
	}
	public AIPExportHeaderItem(String value,int firstRow, int lastRow, int firstCol, int lastCol,int width,int height,boolean nowrap) {
		this.value=value;
		this.range=new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		this.width=width;
		this.height=height;
		this.nowrap=nowrap;
	}
	
	public int getRowspan(){
		return range.getLastRow()-range.getFirstRow()+1;
	}
	public int getColspan(){
		return range.getLastColumn()-range.getFirstColumn()+1;
	}

}
