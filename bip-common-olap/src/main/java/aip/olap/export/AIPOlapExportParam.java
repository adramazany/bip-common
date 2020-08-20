package aip.olap.export;

public class AIPOlapExportParam {
	String title;
	String[] paramString={};
	AIPOlapExportHeader columnHeader;
	
	boolean rightToLeft=true;

	String removeStringFromMembers;
	
	AIPOlapExportParam(){}
	
	AIPOlapExportParam(String title,String[] paramString,AIPOlapExportHeader columnHeader,String removeStringFromMembers){
		super();
		this.title = title;
		this.paramString = paramString;
		this.columnHeader = columnHeader;
		this.removeStringFromMembers = removeStringFromMembers;
	}


	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String[] getParamString() {
		return paramString;
	}
	public void setParamString(String[] paramString) {
		this.paramString = paramString;
	}
	public boolean getRightToLeft() {
		return rightToLeft;
	}
	public void setRightToLeft(boolean rightToLeft) {
		this.rightToLeft = rightToLeft;
	}
	public AIPOlapExportHeader getColumnHeader() {
		return columnHeader;
	}
	public void setColumnHeader(AIPOlapExportHeader columnHeader) {
		this.columnHeader = columnHeader;
	}
	
	
	public String getRemoveStringFromMembers() {
		return removeStringFromMembers;
	}

	public void setRemoveStringFromMembers(String removeStringFromMembers) {
		this.removeStringFromMembers = removeStringFromMembers;
	}

	
	
	
	
}
