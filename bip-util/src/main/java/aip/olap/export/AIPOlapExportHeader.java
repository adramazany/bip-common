package aip.olap.export;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class AIPOlapExportHeader implements Serializable{
	int maxRow=0;
	int maxCol=0;
	
	Hashtable<String, AIPOlapExportHeaderItem> htHeaders = new Hashtable<String, AIPOlapExportHeaderItem>();
	List<AIPOlapExportHeaderItem> lstHeaders = new ArrayList<AIPOlapExportHeaderItem>();
	
	public void setHeadValue(String value,int row,int col){
		String key="r"+row+"c"+col;
		AIPOlapExportHeaderItem item = new AIPOlapExportHeaderItem(value,row, col);
		htHeaders.put(key, item);
		lstHeaders.add(item);
		
		maxRow = Math.max(maxRow, row);
		maxCol = Math.max(maxCol, col);
	}
	public void setHeadValue(String value,int row,int rowspan,int col,int colspan){
		setHeadValue(value, row, rowspan, col, colspan, 0);
	}
	public void setHeadValue(String value,int row,int rowspan,int col,int colspan,boolean nowrap){
		setHeadValue(value, row, rowspan, col, colspan, 0);
	}
	public void setHeadValue(String value,int row,int rowspan,int col,int colspan,int width){
		setHeadValue(value, row, rowspan, col, colspan, width,0,false);
	}
	public void setHeadValue(String value,int row,int rowspan,int col,int colspan,int width,int height,boolean nowrap){
		String key="r"+row+"c"+col;
		AIPOlapExportHeaderItem item = new AIPOlapExportHeaderItem(value,row,row+rowspan-1, col,col+colspan-1,width,height,nowrap);
		htHeaders.put(key, item);
		lstHeaders.add(item);
		
		maxRow = Math.max(maxRow, row+rowspan-1);
		maxCol = Math.max(maxCol, col+colspan-1);
	}
	public int getMaxCol(){
		return maxCol;
		
	}
	public int getMaxRow(){
		return maxRow;
		
	}
	public Iterator<AIPOlapExportHeaderItem> iterator(){
		return htHeaders.values().iterator();
	}
	public List<AIPOlapExportHeaderItem> list(){
		return lstHeaders;
	}
	
	
}
