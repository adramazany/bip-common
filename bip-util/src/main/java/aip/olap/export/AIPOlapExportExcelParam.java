package aip.olap.export;


public class AIPOlapExportExcelParam extends AIPOlapExportParam{
	String sheetName="aip";
	int titleColumn=1;
	int paramStringColumn1=0;
	int paramStringColumn2=10;

	
	short cellStyleDataFormat=3;
	 /*
0, "General"
1, "0"
2, "0.00"
3, "#,##0"
4, "#,##0.00"
5, "$#,##0_);($#,##0)"
6, "$#,##0_);[Red]($#,##0)"
7, "$#,##0.00);($#,##0.00)"
8, "$#,##0.00_);[Red]($#,##0.00)"
9, "0%"
0xa, "0.00%"
0xb, "0.00E+00"
0xc, "# ?/?"
0xd, "# ??/??"
0xe, "m/d/yy"
0xf, "d-mmm-yy"
0x10, "d-mmm"
0x11, "mmm-yy"
0x12, "h:mm AM/PM"
0x13, "h:mm:ss AM/PM"
0x14, "h:mm"
0x15, "h:mm:ss"
0x16, "m/d/yy h:mm"

//0x17 - 0x24 reserved for international and undocumented 0x25, "#,##0_);(#,##0)"
0x26, "#,##0_);[Red](#,##0)"
0x27, "#,##0.00_);(#,##0.00)"
0x28, "#,##0.00_);[Red](#,##0.00)"
0x29, "_(*#,##0_);_(*(#,##0);_(* \"-\"_);_(@_)"
0x2a, "_($*#,##0_);_($*(#,##0);_($* \"-\"_);_(@_)"
0x2b, "_(*#,##0.00_);_(*(#,##0.00);_(*\"-\"??_);_(@_)"
0x2c, "_($*#,##0.00_);_($*(#,##0.00);_($*\"-\"??_);_(@_)"
0x2d, "mm:ss"
0x2e, "[h]:mm:ss"
0x2f, "mm:ss.0"
0x30, "##0.0E+0"
0x31, "@" - This is text format.
0x31 "text" - Alias for "@" 
	  */

	public AIPOlapExportExcelParam(String title,String[] paramString,AIPOlapExportHeader columnHeader){
		super(title,paramString,columnHeader,null);
	}
	public AIPOlapExportExcelParam(String title,String[] paramString,AIPOlapExportHeader columnHeader,String removeStringFromMembers){
		super(title,paramString,columnHeader,removeStringFromMembers);
	}
	

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public int getTitleColumn() {
		if(titleColumn==0){
			int titleColumnTmp = getColumnHeader().getMaxCol()/2-3;
			if(titleColumnTmp<2)titleColumnTmp=1;
			return titleColumnTmp;
		}
		return titleColumn;
	}

	public void setTitleColumn(int titleColumn) {
		this.titleColumn = titleColumn;
	}

	public int getParamStringColumn1() {
		return paramStringColumn1;
	}

	public void setParamStringColumn1(int paramStringColumn1) {
		this.paramStringColumn1 = paramStringColumn1;
	}

	public int getParamStringColumn2() {
		if(paramStringColumn2==0){
			int paramStringColumn2Tmp = getColumnHeader().getMaxCol();
			if(paramStringColumn2Tmp<2)paramStringColumn2Tmp=3;
			return paramStringColumn2Tmp;
		}
		return paramStringColumn2;
	}

	public void setParamStringColumn2(int paramStringColumn2) {
		this.paramStringColumn2 = paramStringColumn2;
	}
	public short getCellStyleDataFormat() {
		return cellStyleDataFormat;
	}
	public void setCellStyleDataFormat(short cellStyleDataFormat) {
		this.cellStyleDataFormat = cellStyleDataFormat;
	}
	
	
}
