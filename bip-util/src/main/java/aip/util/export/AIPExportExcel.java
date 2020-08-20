package aip.util.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import aip.util.DateConvert;
import aip.util.NVL;

public class AIPExportExcel {
	Hashtable<String, XSSFRow> sheetRowsByName = new Hashtable<String, XSSFRow>();
	int widthRatio=72;
	int paramStringColumn1=0;
	int paramStringColumn2=3;
	String sheetName="aip";
	boolean rightToLeft=true;
	short cellStyleDataFormat=3;//3, "#,##0"
	int titleColumn=1;

	public InputStream export(Object[][] cellSet
			, String title
			, String[] paramString
			, AIPExportHeader columnHeader
			) throws AIPExportException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		export(cellSet, baos, title, paramString, columnHeader);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		return bais;
	}
	private void export(Object[][] cellSet, OutputStream out
			, String title
			, String[] paramString
			, AIPExportHeader columnHeader
			) throws AIPExportException {
		export(cellSet, out, sheetName, rightToLeft, cellStyleDataFormat, title, titleColumn, paramString, paramStringColumn1, paramStringColumn2, columnHeader);
	}
	private void export(Object[][] cellSet, OutputStream out
			, String sheetName
			, boolean rightToLeft
			, short cellStyleDataFormat
			, String title
			, int titleColumn
			, String[] paramString
			, int paramStringColumn1
			, int paramStringColumn2
			, AIPExportHeader columnHeader
			) throws AIPExportException {
		XSSFWorkbook wb = new XSSFWorkbook();
		
		 XSSFSheet sheet = wb.createSheet(sheetName);
		 sheet.setRightToLeft(rightToLeft);

		 CellStyle cellStyleTitle = getCellStyleTitle(wb, rightToLeft);
		 CellStyle cellStyleParam = getCellStyleParam(wb, rightToLeft);
		 CellStyle cellStyleRowHeader = getCellStyleHeader(wb, rightToLeft);

		 CellStyle cellStyleColHeader = getCellStyleHeader(wb, rightToLeft);
		 cellStyleColHeader.setAlignment(HorizontalAlignment.CENTER);// CellStyle.ALIGN_CENTER);
		 
		 CellStyle cellStyleData = getCellStyleData(wb, rightToLeft, cellStyleDataFormat);
		 
		/*
		  * title
		  */
		 if(!NVL.isEmpty( title ) ){
			 if(rightToLeft){
				 title="‏"+title;//"‏" contains /u200F right-to-left mark
			 }
			 setCellValueByRowName(sheet,"title",titleColumn,title,cellStyleTitle);
			 //mergeCellsByRowName(sheet,"title",0,1);
		 }

		 /*
		  * params
		  */
		 if(paramString!=null && paramString.length>0){
			 int paramStringColumn; 
			 int paramStringRow; 
			 for (int i = 0; i < paramString.length; i++) {
				 paramStringColumn = (i%2==0?paramStringColumn1:paramStringColumn2);
				 paramStringRow = i/2;
				 setCellValueByRowName(sheet,"paramString"+paramStringRow,paramStringColumn,paramString[i],cellStyleParam);
			 }
			 //mergeCellsByRowName(sheet,"paramString",0,1);
		 }
		 
		 int rowHeaderNeededColumns=0;
		 
		 /*
		  * column header
		  */
		 boolean isFirstColumnHasWidth=false;
		 if(columnHeader==null){
//			 setCellValueByRowName(sheet,"h0",0,"",cellStyleData);//column headers array//first row//first empty cell in table
//			 for (int i=0;i<cellSet.getAxes().get(0).getPositionCount();i++) {
//				 Position column = cellSet.getAxes().get(0).getPositions().get(i);
//				 
//				 for (int headerRowIndex = 0; headerRowIndex < column.getMembers().size(); headerRowIndex++) {
//					 Member member = column.getMembers().get(headerRowIndex);
//					 String value = member.getCaption();
//					 
//					 String rowName = "h"+headerRowIndex;
//					 int columnIndex = rowHeaderNeededColumns+i;
//					 setCellValueByRowName(sheet,rowName,columnIndex,value,cellStyleColHeader);
//				}
//			}
		 }else{
			int tableStartRow = sheet.getLastRowNum()+1;
			List<AIPExportHeaderItem> headers = columnHeader.list();
			for (int i = 0; i < headers.size(); i++) {
				AIPExportHeaderItem header = headers.get(i);

				if(i==0 && header.width>0)isFirstColumnHasWidth=true;
				
				setCellValue(sheet,tableStartRow+header.range.getFirstRow(), header.range.getFirstColumn(), header.value,cellStyleColHeader);
				mergeCells(sheet, tableStartRow+header.range.getFirstRow(), tableStartRow+header.range.getLastRow(), header.range.getFirstColumn(), header.range.getLastColumn());
				if(header.width>0){
					sheet.setColumnWidth(header.range.getFirstColumn(), header.width*widthRatio);
				}
			}
		 }
		 if(!isFirstColumnHasWidth){
			sheet.autoSizeColumn(0, true);
		 }
		

		/* 
		 * rows
		 */
		for (int rowIndex=0;rowIndex<cellSet.length;rowIndex++) {
			/*
			 * row header
			 */
			 String rowName = "r"+rowIndex;
			 String value=NVL.getString( cellSet[rowIndex][0] );
			 /*
			  * param.getRemoveStringFromMembers()
			  */

			//if(row.getMembers().size()>0)sb.deleteCharAt(sb.length()-1);
			 setCellValueByRowName(sheet,rowName,0,value,cellStyleRowHeader);
			
			/*
			 * data
			 */
			for (int j = 1; j < cellSet[rowIndex].length; j++) {
				int column = j;
				String cell = NVL.getString( cellSet[rowIndex][column] );
				
				int columnIndex = rowHeaderNeededColumns+j;

				//String cellValue = cell.getFormattedValue();
				if(isNumber( NVL.getString(cell))){
					double cellValue = NVL.getDbl( cell );					
					setCellValueByRowName(sheet,rowName,columnIndex,cellValue,cellStyleData);
				}else{
					String cellValue = NVL.getString( cell );					
					setCellValueByRowName(sheet,rowName,columnIndex,cellValue,cellStyleData);
				}
				
				
//					if(!NVL.isEmpty(cell.getErrorText())){
//						cellValue += cell.getErrorText();
//					}
				
			}
		}
		 
		 /*
		  * write out
		  */
		 try {
			wb.write(out);
		} catch (IOException e) {
			throw new AIPExportException("اشکال در نوشتن اطلاعات در اکسل",e);
		}
		      		
	}
	
	private XSSFRow getRowByName(XSSFSheet sheet,String rowName){
		if(!sheetRowsByName.containsKey(rowName)){
			int rownum=sheet.getLastRowNum()+1;
			XSSFRow row = sheet.createRow(rownum);
			sheetRowsByName.put(rowName, row);
		}
		return sheetRowsByName.get(rowName);
	}
//	private void setCellValueByRowName(XSSFSheet sheet, String rowName, int columnNum,String value) {
//		setCellValueByRowName(sheet, rowName, columnNum, value, null);
//	}
	private void setCellValueByRowName(XSSFSheet sheet, String rowName, int columnNum,String value,CellStyle cellStyle) {
		XSSFRow row = getRowByName(sheet, rowName);
		setCellValue(sheet, row, columnNum, value, cellStyle);
	}
	private void setCellValueByRowName(XSSFSheet sheet, String rowName, int columnNum,double value,CellStyle cellStyle) {
		XSSFRow row = getRowByName(sheet, rowName);
		setCellValue(sheet, row, columnNum, value, cellStyle);
	}
	private XSSFRow getRowForce(XSSFSheet sheet, int rowNum){
		XSSFRow row = sheet.getRow(rowNum);
		if(row==null){
			row = sheet.createRow(rowNum);
		}
		return row;
	}
	private XSSFCell getCellForce(XSSFSheet sheet, XSSFRow row, int columnNum){
		XSSFCell cell = row.getCell(columnNum);
		if(cell==null){
			cell = row.createCell(columnNum);
		}
		return cell;
	}
	private XSSFCell getCellForce(XSSFSheet sheet, int rowNum, int columnNum){
		return getCellForce(sheet, getRowForce(sheet, rowNum), columnNum);
	}
	private void setCellValue(XSSFSheet sheet, int rowNum, int columnNum,String value,CellStyle cellStyle) {
		setCellValue(sheet, getRowForce(sheet, rowNum), columnNum, value, cellStyle);
	}
	private void setCellValue(XSSFSheet sheet, XSSFRow row, int columnNum,String value,CellStyle cellStyle) {
		XSSFCell cell = getCellForce(sheet, row, columnNum);
		
		cell.setCellValue(value);
		
		if(cellStyle!=null){
			cell.setCellStyle(cellStyle);
		}
	}
	private void setCellValue(XSSFSheet sheet, XSSFRow row, int columnNum,double value,CellStyle cellStyle) {
		XSSFCell cell = getCellForce(sheet, row, columnNum);
		
		cell.setCellValue(value);
		
		if(cellStyle!=null){
			cell.setCellStyle(cellStyle);
		}
	}
	
	private void mergeCellsByRowName(XSSFSheet sheet,String rowName,int firstCol,int lastCol){
		 XSSFRow row = getRowByName(sheet, rowName);
		 mergeCells(sheet, row.getRowNum(), row.getRowNum(),firstCol, lastCol);
	}
	private void mergeCells(XSSFSheet sheet,int firstRow,int lastRow,int firstCol,int lastCol){
		 CellRangeAddress cellRangeAddress=new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		 if(lastRow>firstRow+1 || lastCol>firstCol+1){
			 sheet.addMergedRegion(cellRangeAddress);
		 }

		 XSSFCell maincell = getCellForce(sheet, firstRow, firstCol); 
		 
		 for (int i = firstRow; i <= lastRow; i++) {
			 for (int j = firstCol; j <= lastCol; j++) {
				XSSFCell cell = getCellForce(sheet, i, j);
				cell.setCellStyle(maincell.getCellStyle());
			}
		}
	}


	 /*
	  * title style
	  */
	private CellStyle getCellStyleTitle(Workbook wb, boolean rightToLeft){
		 Font font = wb.createFont();
		 font.setBold(true);// setBoldweight(Font.BOLDWEIGHT_BOLD);
		 //font.setFontHeight((short)18);
		 font.setColor(IndexedColors.DARK_BLUE.getIndex());
		 
/*		 XSSFWorkbook hwb;
		 XSSFCellStyle hcs;
		 hcs.setd
*/		
		 CellStyle cellStyleTitle = wb.createCellStyle();
		 cellStyleTitle.setFont(font);
//		 cellStyleTitle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
//		 cellStyleTitle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		 if(rightToLeft){
			 cellStyleTitle.setAlignment(HorizontalAlignment.RIGHT);// CellStyle.ALIGN_RIGHT);
			 cellStyleTitle.setDataFormat((short)2);//righttoleft
			 //cellStyleTitle.setDataFormat(CellStyle.ALIGN_RIGHT);
		 }
		 return cellStyleTitle;
	}
	private CellStyle getCellStyleParam(Workbook wb, boolean rightToLeft){
		 Font font = wb.createFont();
		 //font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		 font.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
		
		CellStyle cellStyleParam = wb.createCellStyle();
		cellStyleParam.setFont(font);
//		 cellStyleParam.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
//		 cellStyleParam.setFillPattern(CellStyle.SOLID_FOREGROUND);
		 if(rightToLeft){
			 cellStyleParam.setAlignment(HorizontalAlignment.RIGHT);//CellStyle.ALIGN_RIGHT);
		 }
		 return cellStyleParam;
	}
	 /*
	  * rowheader style
	  */
	private CellStyle getCellStyleHeader(Workbook wb, boolean rightToLeft){
		 Font font = wb.createFont();
		 font.setBold(true);//setBoldweight(Font.BOLDWEIGHT_BOLD);
		 font.setColor(IndexedColors.DARK_BLUE.getIndex());
		
		 CellStyle cellStyleHeader = wb.createCellStyle();
		 cellStyleHeader.setFont(font);
		 cellStyleHeader.setBorderTop(BorderStyle.THIN);// CellStyle.BORDER_THIN);
		 cellStyleHeader.setBorderBottom(BorderStyle.THIN);//CellStyle.BORDER_THIN);
		 cellStyleHeader.setBorderLeft(BorderStyle.THIN);//CellStyle.BORDER_THIN);
		 cellStyleHeader.setBorderRight(BorderStyle.THIN);//CellStyle.BORDER_THIN);
		 cellStyleHeader.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		 cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);//CellStyle.SOLID_FOREGROUND);
		 //cellStyleHeader.setFillBackgroundColor(XSSFColor.LIGHT_CORNFLOWER_BLUE.index);
		 //cellStyleHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
		 if(rightToLeft){
			 cellStyleHeader.setAlignment(HorizontalAlignment.RIGHT);// XSSFCellStyle.ALIGN_RIGHT);
		 }
		 
		 return cellStyleHeader;
	}
	 /*
	  * data style
	  */
	private CellStyle getCellStyleData(Workbook wb, boolean rightToLeft, short cellStyleDataFormat){
		 Font font = wb.createFont();
		 //font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		 //font.setColor(IndexedColors.BLUE_GREY.getIndex());
		
		 CellStyle cellStyleData = wb.createCellStyle();
		 cellStyleData.setFont(font);
		 cellStyleData.setBorderTop(BorderStyle.THIN);//CellStyle.BORDER_THIN);
		 cellStyleData.setBorderBottom(BorderStyle.THIN);//CellStyle.BORDER_THIN);
		 cellStyleData.setBorderLeft(BorderStyle.THIN);//CellStyle.BORDER_THIN);
		 cellStyleData.setBorderRight(BorderStyle.THIN);//CellStyle.BORDER_THIN);
		 //cellStyleData.setDataFormat(CellStyle.)
		 
		 /*
		  *  0, "General"
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

// 0x17 - 0x24 reserved for international and undocumented 0x25, "#,##0_);(#,##0)"
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
		 //cellStyleData.setDataFormat((short)4);
		 cellStyleData.setDataFormat(cellStyleDataFormat);
		 
		 if(rightToLeft){
			 cellStyleData.setAlignment(HorizontalAlignment.RIGHT);//CellStyle.ALIGN_RIGHT);
		 }
		 return cellStyleData;
//		 XSSFRow row = null;

		 // Aqua background
//		 XSSFCellStyle style = wb.createCellStyle();
		 //style.setFillBackgroundColor(XSSFColor.AQUA.index);
		 //style.setFillPattern(XSSFCellStyle.BIG_SPOTS);
		 //style.setFont(new Font("Arial",16,java.awt.Font.BOLD));
		 //style.setFillForegroundColor(style)
//		 XSSFCell cell =null;
//		 style.setFillForegroundColor(foregroundColor);
	}
	public boolean isNumber(String str) {
	    return str.matches("^-?[0-9]+(\\.[0-9]+)?$");
	}
	public boolean isInteger(String str) {
	    return str.matches("^-?[0-9]+?$");
	}
	public boolean isDouble(String str) {
	    return str.matches("^-?[0-9]+(\\.[0-9]+)?$");
	}
	
	
	
	public static void main(String[] args) {
		System.out.println("AIPExportExcel.main():starting.........");
		try {
			
			String mdx = "select { [Measures].tedadtavalod	} on columns" +
					", { DimMahal.mahalha.[All].children } on rows" +
					" from TavalodFot";
			String mdx1 = "select {[tavalod1headerset]} on columns "+
					",{DESCENDANTS([DimEdare].[ostanshahrestanedare].&[1000])} on rows "+ 
					"from TavalodFot "; 
			String mdx_mondrian = "with member  measures.tedadtavalod_100 as '(measures.tedadtavalod*100) '" +
					" select {tavalod1headerset} on columns "+
					",{DimEdare.[All]} on rows "+ 
					"from Tavalod " +
					" where (measures.tedadtavalod_100)"; 
			
			
//			AIPOlapUtil olapUtil = new  AIPOlapUtil("http://192.168.0.71:80/olap/msmdpump.dll","AIPSabtBICube","bi_admin", "aippia");
//			CellSet cellSet = olapUtil.executeMdx("aipsabtbicube",mdx1);
			//CellSet cellSet = AIPOlapUtil.executeMdx("aipsabtbicube-mondrian",mdx_mondrian);
			Object[][] cellSet={
					{"a",1234567,2.123456,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19}
					,{"b",1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19}
			};

			/*
			 * export params
			 */
			String title="‫١- گزارش تعداد ولادت های ثبت شده برحسب جنس / شهری و روستایی / جاری و معوقه‬";
			String paramString[]={"محل : تهران"
					,"از تاریخ : 1391/01/01"
					,"تاریخ گزارش: "+DateConvert.getTodayJalali()+" "+DateConvert.getTime()
					,"تا تاریخ : 1391/01/01"
					};
			AIPExportHeader columnHeader = new AIPExportHeader();

			columnHeader.setHeadValue("محدوده جغرافیایی",0,3,0,1);
			columnHeader.setHeadValue("مجموع",0,2,1,7);
			columnHeader.setHeadValue("شهری",0,1,8,6);
			columnHeader.setHeadValue("روستایی",0,1,14,6);
			
			columnHeader.setHeadValue("جاری",1,1,8,3);
			columnHeader.setHeadValue("معوقه",1,1,11,3);
			columnHeader.setHeadValue("جاری",1,1,14,3);
			columnHeader.setHeadValue("معوقه",1,1,17,3);

			int c=1;
			columnHeader.setHeadValue("جمع",2,1,c++,1);
			columnHeader.setHeadValue("جاری",2,1,c++,1);
			columnHeader.setHeadValue("معوقه",2,1,c++,1);
			columnHeader.setHeadValue("شهری",2,1,c++,1);
			columnHeader.setHeadValue("روستایی",2,1,c++,1);
			columnHeader.setHeadValue("مرد",2,1,c++,1);
			columnHeader.setHeadValue("زن",2,1,c++,1);
			columnHeader.setHeadValue("جمع",2,1,c++,1);
			columnHeader.setHeadValue("مرد",2,1,c++,1);
			columnHeader.setHeadValue("زن",2,1,c++,1);
			columnHeader.setHeadValue("جمع",2,1,c++,1);
			columnHeader.setHeadValue("مرد",2,1,c++,1);
			columnHeader.setHeadValue("زن",2,1,c++,1);
			columnHeader.setHeadValue("جمع",2,1,c++,1);
			columnHeader.setHeadValue("مرد",2,1,c++,1);
			columnHeader.setHeadValue("زن",2,1,c++,1);
			columnHeader.setHeadValue("جمع",2,1,c++,1);
			columnHeader.setHeadValue("مرد",2,1,c++,1);
			columnHeader.setHeadValue("زن",2,1,c++,1);


			//AIPExportExcelParam param=new AIPExportExcelParam(title, paramString, columnHeader);
			//param.setTitleColumn(5);
			//param.setParamStringColumn2(17);

			AIPExportExcel export = new AIPExportExcel();
			File f = new File("e:/aipexport_excel.xls");
			FileOutputStream fout = new FileOutputStream(f);
			
			export.export(cellSet, fout, title, paramString, columnHeader);
			fout.close();
			System.out.println("AIPExportExcel.main():excel write in :"+f.getAbsolutePath());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("AIPExportExcel.main():end");
	}

}

