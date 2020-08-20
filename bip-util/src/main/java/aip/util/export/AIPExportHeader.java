package aip.util.export;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class AIPExportHeader implements Serializable{
	
	Hashtable<String, AIPExportHeaderItem> htHeaders = new Hashtable<String, AIPExportHeaderItem>();
	List<AIPExportHeaderItem> lstHeaders = new ArrayList<AIPExportHeaderItem>();
	
	public void setHeadValue(String value,int row,int col){
		String key="r"+row+"c"+col;
		AIPExportHeaderItem item = new AIPExportHeaderItem(value,row, col);
		htHeaders.put(key, item);
		lstHeaders.add(item);
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
		AIPExportHeaderItem item = new AIPExportHeaderItem(value,row,row+rowspan-1, col,col+colspan-1,width,height,nowrap);
		htHeaders.put(key, item);
		lstHeaders.add(item);
	}
	public int size(){
		return htHeaders.size();
		
	}
	public Iterator<AIPExportHeaderItem> iterator(){
		return htHeaders.values().iterator();
	}
	public List<AIPExportHeaderItem> list(){
		return lstHeaders;
	}
	
	
}
