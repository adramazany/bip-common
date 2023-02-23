package bip.common.util;

import aip.util.AIPUtil;
import aip.util.NVL;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class BIPReportLST {
	
	protected int pageSize=10;
	protected int requestPage;
	protected int totalPages;
	protected long totalItems;
	protected String title;

	//protected Properties lstSumPage=new Properties();
	protected ArrayList<KeyValue> lstSumPage = new ArrayList<KeyValue>();
	//protected Properties lstSumTotal=new Properties();
	protected ArrayList<KeyValue> lstSumTotal = new ArrayList<KeyValue>();

	protected String paramString="";
	
	protected BIPReportParam param = new BIPReportParam();
	
	protected List rows = new ArrayList();

	//protected Properties lstVisibleColumns=new Properties();
	protected ArrayList<KeyValue> lstVisibleColumns = new ArrayList<KeyValue>();
	protected abstract void initVisibleColumns();
	

	float processtime;


	
	
	{
		initVisibleColumns();
	}
	
	
	
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getRequestPage() {
		int rp = requestPage<1?1:requestPage;
		if(getTotalPages()>0 && rp>getTotalPages())rp=getTotalPages();
		return rp;
	}

	public void setRequestPage(int requestPage) {
		this.requestPage = requestPage;
	}

	public int getTotalPages() {
		int tp = totalPages;
		if(totalItems>0){
			tp=(int)( totalItems / param.getPageSize() );
			if((int)( totalItems % param.getPageSize() )>0)tp++;
		}
		return tp;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public long getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
	}


	public String getParamString() {
		return paramString;
	}

	public void setParamString(String paramString) {
		this.paramString = paramString;
	}

	public BIPReportParam getParam() {
		return param;
	}

	public void setParam(BIPReportParam param) {
		this.param = param;
		setRequestPage(param.getRequestPage());
		setPageSize(param.getPageSize());
		if(param.getRequestPage()>getTotalPages())param.setRequestPage(getTotalPages());
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}



	protected KeyValue getSumPageKeyValue(String key) {
		for(int i=0;i<lstSumPage.size();i++){
			KeyValue kv = lstSumPage.get(i);
			if(kv.key.equalsIgnoreCase(key)){
				return kv;
			}
		}
		return null;
	}
	public String[] getSumPageKeys(){
		String[] keys= new String[lstSumPage.size()];
		for(int i=0;i<lstSumPage.size();i++){
			keys[i] = lstSumPage.get(i).key;
		}
		return keys;
	}


	public int getSumPageCount(){
		return lstSumPage.size();
	}
	
	public Object getSumPage(String key) {
		KeyValue kv = getSumPageKeyValue(key);
		if(kv!=null)return kv.value;
		return null;
	}

	public String getSumPageLabel(String key) {
		KeyValue kv = getSumPageKeyValue(key);
		if(kv!=null)return kv.label;
		return null;
	}

	public long getSumPageLong(String key) {
		return NVL.getLng( getSumPage(key) );
	}

	public String getSumPage(String key,String format) {
		return NVL.getIntFormat( getSumPage(key) , format );
	}



	public void setSumPage(String key,String value,String label) {
		KeyValue kv = getSumPageKeyValue(key);
		if(kv!=null){
			kv.value=value;
			kv.label=label;
		}
		lstSumPage.add( new KeyValue( key, value , label));
	}


	public int getSumTotalCount(){
		return lstSumTotal.size();
	}
	
	protected KeyValue getSumTotalKeyValue(String key) {
		for(int i=0;i<lstSumTotal.size();i++){
			KeyValue kv = lstSumTotal.get(i);
			if(kv.key.equalsIgnoreCase(key)){
				return kv;
			}
		}
		return null;
	}

	public String[] getSumTotalKeys(){
		String[] keys= new String[lstSumTotal.size()];
		for(int i=0;i<lstSumTotal.size();i++){
			keys[i] = lstSumTotal.get(i).key;
		}
		return keys;
	}

	public Object getSumTotal(String key) {
		KeyValue kv = getSumTotalKeyValue(key);
		if(kv!=null)return kv.value;
		return null;
	}

	public String getSumTotalLabel(String key) {
		KeyValue kv = getSumTotalKeyValue(key);
		if(kv!=null)return kv.label;
		return null;
	}


	public long getSumTotalLong(String key) {
		return NVL.getLng(getSumTotal(key));
	}

	public String getSumTotal(String key,String format) {
		return NVL.getIntFormat( getSumTotal(key) , format );
	}

	public void setSumTotal(String key,String value,String label) {
		KeyValue kv = getSumTotalKeyValue(key);
		if(kv!=null){
			kv.value=value;
			kv.label=label;
		}
		lstSumTotal.add( new KeyValue( key, value , label));
	}

	
	public void clearVisibleColumns(){
		lstVisibleColumns.clear();
	}


//	public String getVisibleColumn(String key) {
//		ArrayList ar;
//		return lstVisibleColumns.getProperty(key);
//	}

	public void setVisibleColumn(String key,String value) {
		//lstVisibleColumns.setProperty(key, value);
		lstVisibleColumns.add(new KeyValue(key,value));
	}
	
	public int getVisibleColumnCount(){
		return lstVisibleColumns.size();
	}
	public String[] getVisibleColumnLabels(){
		//return lstVisibleColumns.values().toArray(new String[0]);
		String[] res = new String[lstVisibleColumns.size()];
		for(int i=0;i<lstVisibleColumns.size();i++){
			res[i]=lstVisibleColumns.get(i).value;
		}
		return res;
	}

	public String[] getVisibleColumnFieldsGetter(){
		//String[] columnFieldsGetter = lstVisibleColumns.keySet().toArray(new String[0]);
		String[] columnFieldsGetter = new String[lstVisibleColumns.size()];
		for(int i=0;i<lstVisibleColumns.size();i++){
			columnFieldsGetter[i]=lstVisibleColumns.get(i).key;
		}
		
		Object obj = null;
		ArrayList fieldNames = new ArrayList();
		if(getRows().size()>0){
			ArrayList<Field> fields = AIPUtil.getFields( getRows().get(0).getClass() ) ;
			for(int i=0;i<fields.size();i++)fieldNames.add(fields.get(i).getName().toLowerCase());
		}
		//if(AIPUtil.invokeGetter(obj))
		for(int j=0;j<columnFieldsGetter.length;j++){
			if(!NVL.isEmpty(columnFieldsGetter[j])){
				if(fieldNames.contains(columnFieldsGetter[j].toLowerCase()) || fieldNames.size()==0){
					columnFieldsGetter[j] = columnFieldsGetter[j].substring(0,1).toUpperCase()+columnFieldsGetter[j].substring(1);
				}else{
					columnFieldsGetter[j]="~"+columnFieldsGetter[j];
				}
			}
		}
		return columnFieldsGetter;
	}
	
	
	public long getFirstRow(){
		return (getRequestPage()-1)*getPageSize()+0;
	}
	public long getLastRow(){
		return getFirstRow() + getPageSize();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public float getProcesstime() {
		return processtime;
	}

	public void setProcesstime(float processtime) {
		this.processtime = processtime;
	}

	

}
class KeyValue{
	String key;
	String value; 
	String label;
	public KeyValue(String key,String value){
		this.key=key;
		this.value=value;
	}
	public KeyValue(String key,String value,String label){
		this.key=key;
		this.value=value;
		this.label=label;
	}
}
