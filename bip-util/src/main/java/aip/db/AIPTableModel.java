package aip.db;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Hashtable;

import javax.swing.table.DefaultTableModel;

import aip.util.NVL;

public class AIPTableModel extends DefaultTableModel {
	int rowcounter=-1;
	
	Hashtable<String, Integer> columnNames;
	
	public Object getValueAt(int row, String columnName) {
		int column = getColumnByName(columnName.toLowerCase());
		
		return super.getValueAt(row, column);
	}

	private int getColumnByName(String columnName) {
		if(columnNames==null){
			columnNames = new Hashtable<String, Integer>();
			for (int i = 0; i < getColumnCount(); i++) {
				columnNames.put(getColumnName(i).toLowerCase(), i);
			}
		}
		return columnNames.get(columnName.toLowerCase());
	}
	public void clearColumnNameCache(){
		if(columnNames!=null){
			columnNames.clear();
			columnNames=null;
		}
	}

	public boolean next(){
		rowcounter++;
		return rowcounter<getRowCount();
	}
	public int getInt(int column){
		return NVL.getInt( getValueAt(rowcounter, column) );
	}
	public int getInt(String columnName){
		return NVL.getInt( getValueAt(rowcounter, columnName) );
	}
	public String getString(int column){
		Object obj = getValueAt(rowcounter, column);
		if(obj==null){
			return null;
		}else{
			return NVL.getString( obj );
		}
	}
	public String getString(String columnName){
		return getString(getColumnByName(columnName));
	}
	public Date getDate(int column){
		Object obj = getValueAt(rowcounter, column);
		if(obj==null){
			return null;
		}else{
			return (Date)obj;
		}
	}
	public Date getDate(String columnName){
		return getDate(getColumnByName(columnName));
	}
	
	public Timestamp getTimestamp(int column){
		Object obj = getValueAt(rowcounter, column);
		if(obj==null){
			return null;
		}else{
			return (Timestamp)obj;
		}
	}
	public Timestamp getTimestamp(String columnName){
		return getTimestamp(getColumnByName(columnName));
	}
	
	public void close(){
		clearColumnNameCache();
		super.dataVector.clear();
	}
}
