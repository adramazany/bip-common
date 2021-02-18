package aip.olap.export;

import aip.olap.AIPOlapUtil;
import aip.olap.util.BIPOlapUtil;
import aip.olap.util.collections.Tree;
import aip.util.AIPUtil;
import aip.util.DateConvert;
import aip.util.NVL;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.olap4j.Cell;
import org.olap4j.CellSet;
import org.olap4j.Position;
import org.olap4j.metadata.Member;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class AIPOlapExportPdf extends AIPOlapExportAbstract{

		  
		  
	public void export(CellSet cellSet, OutputStream out,AIPOlapExportParam param) throws AIPOlapExportException {
		export(cellSet, out, (AIPOlapExportPdfParam)param);
	}

	public void export(List<List<String>> values, OutputStream out,AIPOlapExportParam param) throws AIPOlapExportException {
		export(values, out, (AIPOlapExportPdfParam)param);
	}

	
	private void export(List<List<String>> values, OutputStream out,AIPOlapExportPdfParam param) throws AIPOlapExportException {
		 try {
	
		BaseFont baseFont = BaseFont.createFont("font"+"/"+"arial.ttf",BaseFont.IDENTITY_H, true);
		Font titleFont = new Font(baseFont, 15,Font.NORMAL,BaseColor.BLACK);
		Font paramsFont = new Font(baseFont, 12,Font.NORMAL, BaseColor.BLACK);
		Font headerFont = new Font(baseFont, 12,Font.NORMAL,BaseColor.BLACK);
		Font dataFont = new Font(baseFont, 7,Font.NORMAL);
			 
		Document doc = new Document();

		if(param.getPageSize()!=null)doc.setPageSize(param.getPageSize());
		if(param.isLandscape())doc.setPageSize(doc.getPageSize().rotate());
		
		PdfWriter pdfw = PdfWriter.getInstance(doc,out);
		doc.open();

		doc.addAuthor("درگاه هوش تجاری");
		doc.addCreator("AIPOlapExportPdf");
		//doc.addHeader(arg0, arg1)
		doc.addKeywords("");
		doc.addProducer();
		doc.addSubject(NVL.getString(param.getSubject(),NVL.getString(param.getTitle())));
		doc.addTitle(param.getTitle());
		
		doc.setHtmlStyleClass(param.getHtmlStyleClass());
		doc.setJavaScript_onLoad(param.getJavascript());

		/*
		 * body
		 */
		Paragraph body = new Paragraph();
		
		/*
		 * Title
		 */
		PdfPTable title = new PdfPTable(1);
		title.setRunDirection(getRUN_DIRECTION(param));
		PdfPCell titleCell = new PdfPCell(new Phrase(param.getTitle(),titleFont));
		titleCell.setBorder(0);
		titleCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		title.addCell(titleCell);
		//title.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		//title.addCell(new Phrase(docParam.getTitle(),fontTitle));
		body.add(title);

		addEmptyLine(body, 1);
		
		/*
		 * Params
		 */
		int paramsBorder =0;
		PdfPTable params = new PdfPTable(2);
		body.add(params);

		params.setWidthPercentage(100f);
		params.setRunDirection(getRUN_DIRECTION(param));

		for (int i = 0; i < param.getParamString().length; i++) {
			PdfPCell paramsCell1 = new PdfPCell(new Phrase(param.getParamString()[i],paramsFont));
			paramsCell1.setBorder(paramsBorder);
			paramsCell1.setHorizontalAlignment( (i%2==0)?PdfPCell.ALIGN_LEFT:PdfPCell.ALIGN_RIGHT);
			params.addCell(paramsCell1);
		}
//		
//		PdfPCell paramsCell2 = new PdfPCell(new Phrase("‫از تاریخ : ٥٢/٢/٠٩٣١‬",paramsFont));
//		paramsCell2.setBorder(paramsBorder);
//		paramsCell2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//		params.addCell(paramsCell2);
//		
//		PdfPCell paramsCell3 = new PdfPCell(new Phrase("‫تاریخ گزارش:‬‫٨/٧/١٩٣١‬",paramsFont));
//		paramsCell3.setBorder(paramsBorder);
//		paramsCell3.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
//		params.addCell(paramsCell3);
//		
//		PdfPCell paramsCell4 = new PdfPCell(new Phrase("‫تا تاریخ : ٦٢/٢/٠٩٣١‬",paramsFont));
//		paramsCell4.setBorder(paramsBorder);
//		paramsCell4.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//		params.addCell(paramsCell4);
		
		
		addEmptyLine(body, 1);
		
//		
//		 CellStyle cellStyleTitle = getCellStyleTitle(wb, param);
//		 CellStyle cellStyleParam = getCellStyleParam(wb, param);
//		 CellStyle cellStyleRowHeader = getCellStyleHeader(wb, param);
//
//		 CellStyle cellStyleColHeader = getCellStyleHeader(wb, param);
//		 cellStyleColHeader.setAlignment(CellStyle.ALIGN_CENTER);
//		 
//		 CellStyle cellStyleData = getCellStyleData(wb, param);
		 
		 
		 /*
		  * column header
		  */
		 
		 //PdfPTable data = new PdfPTable(cellSet.getAxes().get(0).getPositionCount());
		int columnCount = values.get(0).size()-1;//cellSet.getAxes().get(0).getPositionCount();
		 PdfPTable data = new PdfPTable(columnCount+1);
		 
		 //data.setRunDirection(getRUN_DIRECTION(param));
		 //data.setRunDirection(PdfWriter.RUN_DIRECTION_NO_BIDI);
		 data.setWidthPercentage(100f);
		 
		 //float[] dataWidths = new float[data.getNumberOfColumns()];
		 //dataWidths = data.getAbsoluteWidths();
		 //data.setWidths(arg0)
		 //float absoluteWidths = data.getRow(0).
		//		 data.setBorderColor(BaseColor.BLACK);
////		 data.setPadding(4);
////		 data.setSpacing(4);
//		 data.setBorderWidth(1);

		 
//		 if(param.getColumnHeader()==null){
//			 setCellValueByRowName(sheet,"h0",0,"");//column headers array//first row//first empty cell in table
//			 for (int i=0;i<cellSet.getAxes().get(0).getPositionCount();i++) {
//				 Position column = cellSet.getAxes().get(0).getPositions().get(i);
//				 
//				 for (int headerRowIndex = 0; headerRowIndex < column.getMembers().size(); headerRowIndex++) {
//					 Member member = column.getMembers().get(headerRowIndex);
//					 String value = member.getCaption();
//					 String rowName = "h"+headerRowIndex;
//					 int columnIndex = rowHeaderNeededColumns+i;
//					 setCellValueByRowName(sheet,rowName,columnIndex,value,cellStyleColHeader);
//				}
//			}
//		 }else
		 {
//			}
//			for (Iterator<AIPOlapExportHeaderItem> iter = param.getColumnHeader().iterator(); iter.hasNext();) {
			 List<AIPOlapExportHeaderItem> headers = param.getColumnHeader().list(); 

			 List<AIPOlapExportHeaderItem> rtl_headers = getRTLHeaders(headers,columnCount);
			 
			 float totalWidths[] = getDataTotalWidths(800,columnCount+1,rtl_headers);
			 data.setTotalWidth(totalWidths);
			 
			 for (int i = 0; i < rtl_headers.size(); i++) {
				AIPOlapExportHeaderItem header = rtl_headers.get(i);

				PdfPCell cell=new PdfPCell(new Phrase(header.value,headerFont));

				
				cell.setRunDirection(getRUN_DIRECTION(param));
				//PdfPCell cell=new PdfPCell(new Phrase(""+i,headerFont));
				//cell.setPhrase();
				cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				if(header.range.getLastRow()>header.range.getFirstRow()){
					cell.setRowspan(header.getRowspan());
				}
				if(header.range.getLastColumn()>header.range.getFirstColumn()){
					cell.setColspan(header.getColspan());
				}
				if(header.width>0){
					//dataWidths[header.range.getFirstCol] = header.width;
				}
				if(header.height>0){
					//cell.setFixedHeight(header.height);
				}
				if(header.nowrap){
					//cell.setNoWrap(header.nowrap);
				}
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				//
				 data.addCell(cell);
			 }
		 }
//		sheet.autoSizeColumn(0, true);
		 body.add(data);
		 //data.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		

		/* 
		 * rows
		 */
		 Stack<PdfPCell> stackData = new Stack<PdfPCell>();
		 //if(values.size()>1){//if(cellSet.getAxes().size()>1){
			for (int rowIndex=0;rowIndex<values.size();rowIndex++) {//cellSet.getAxes().get(1).getPositionCount()
				 //Position row = cellSet.getAxes().get(1).getPositions().get(rowIndex);
				/*
				 * row header
				 */
//				 String rowName = "r"+rowIndex;
				 String value="";
				 //for (int headerColumnIndex = 0; headerColumnIndex < row.getMembers().size(); headerColumnIndex++) {
					 //Member member = row.getMembers().get(headerColumnIndex);
				 	value = values.get(rowIndex).get(0); //value += member.getCaption()+" ";
				//}
				 /*
				  * param.getRemoveStringFromMembers()
				  */
				if(param.getRemoveStringFromMembers()!=null){
					value=AIPUtil.replaceAllString(value, param.getRemoveStringFromMembers(), "");
				}
				 
				PdfPCell pdfcell=new PdfPCell();
				pdfcell.setPhrase(new Phrase(value,headerFont));
				pdfcell.setRunDirection(getRUN_DIRECTION(param));
				 
				//data.addCell(pdfcell);
				stackData.push(pdfcell);
				
				/*
				 * data
				 */
				for (int j = 1; j < values.get(rowIndex).size(); j++) {//cellSet.getAxes().get(0).getPositionCount()
					//Position column = cellSet.getAxes().get(0).getPositions().get(j);
					//Cell cell = cellSet.getCell(column, row);

					String cellValue = values.get(rowIndex).get(j);//cell.getFormattedValue();
					if(NVL.isEmpty(cellValue)){
						cellValue="0";
					}
//					if(!NVL.isEmpty(cell.getErrorText())){
//						cellValue += cell.getErrorText();
//					}
					
					pdfcell=new PdfPCell();
					pdfcell.setPhrase(new Phrase(cellValue,dataFont));
					pdfcell.setRunDirection(getRUN_DIRECTION(param));
					//data.addCell(pdfcell);
					stackData.push(pdfcell);
				}
				
				while(!stackData.isEmpty()){
					data.addCell(stackData.pop());
				}
				
			}
		/*}else{// cellset has only columns
			
			 * data
			 
			for (int j = 0; j < values.get(0).size(); j++) {//cellSet.getAxes().get(0).getPositionCount()
				//Position column = cellSet.getAxes().get(0).getPositions().get(j);
				//Cell cell = cellSet.getCell(column);
				String cellValue = values.get(0).get(j);//cell.getFormattedValue();
				if(!NVL.isEmpty(cell.getErrorText())){
					cellValue += cell.getErrorText();
				}
				
				PdfPCell pdfcell=new PdfPCell();
				pdfcell.setPhrase(new Phrase(cellValue));
				//data.addCell(pdfcell);
				stackData.push(pdfcell);
			}
			while(!stackData.isEmpty()){
				data.addCell(stackData.pop());
			}

		}*/
		//data.setWidths(dataWidths);
		 data.setComplete(true);
		 
		doc.add(body);
		doc.close();

	} catch (Exception e) {
			throw new AIPOlapExportException("اشکال در نوشتن اطلاعات در پی دی اف",e);
		}
		      		
	}
	
	private void export(CellSet cellSet, OutputStream out,AIPOlapExportPdfParam param) throws AIPOlapExportException {
		 try {
	
		//BaseFont baseFont = BaseFont.createFont("font"+"/"+"ARIALUNI.TTF",BaseFont.IDENTITY_H, true);
		BaseFont baseFont = BaseFont.createFont("font"+"/"+"arial.ttf",BaseFont.IDENTITY_H, true);
		Font titleFont = new Font(baseFont, 15,Font.NORMAL,BaseColor.BLACK);
		Font paramsFont = new Font(baseFont, 12,Font.NORMAL, BaseColor.BLACK);
		Font headerFont = new Font(baseFont, 12,Font.NORMAL,BaseColor.BLACK);
		Font dataFont = new Font(baseFont, 7,Font.NORMAL);
			 
			 
			 
		Document doc = new Document();

		if(param.getPageSize()!=null)doc.setPageSize(param.getPageSize());
		if(param.isLandscape())doc.setPageSize(doc.getPageSize().rotate());
		
		PdfWriter pdfw = PdfWriter.getInstance(doc,out);
		doc.open();

		doc.addAuthor("درگاه هوش تجاری");
		doc.addCreator("AIPOlapExportPdf");
		//doc.addHeader(arg0, arg1)
		doc.addKeywords("");
		doc.addProducer();
		doc.addSubject(NVL.getString(param.getSubject(),NVL.getString(param.getTitle())));
		doc.addTitle(param.getTitle());
		
		doc.setHtmlStyleClass(param.getHtmlStyleClass());
		doc.setJavaScript_onLoad(param.getJavascript());

		/*
		 * body
		 */
		Paragraph body = new Paragraph();
		
		/*
		 * Title
		 */
		PdfPTable title = new PdfPTable(1);
		title.setRunDirection(getRUN_DIRECTION(param));
		PdfPCell titleCell = new PdfPCell(new Phrase(param.getTitle(),titleFont));
		titleCell.setBorder(0);
		titleCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		title.addCell(titleCell);
		//title.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		//title.addCell(new Phrase(docParam.getTitle(),fontTitle));
		body.add(title);

		addEmptyLine(body, 1);
		
		/*
		 * Params
		 */
		int paramsBorder =0;
		PdfPTable params = new PdfPTable(2);
		body.add(params);

		params.setWidthPercentage(100f);
		params.setRunDirection(getRUN_DIRECTION(param));

		for (int i = 0; i < param.getParamString().length; i++) {
			PdfPCell paramsCell1 = new PdfPCell(new Phrase(param.getParamString()[i],paramsFont));
			paramsCell1.setBorder(paramsBorder);
			paramsCell1.setHorizontalAlignment( (i%2==0)?PdfPCell.ALIGN_LEFT:PdfPCell.ALIGN_RIGHT);
			params.addCell(paramsCell1);
		}
//		
//		PdfPCell paramsCell2 = new PdfPCell(new Phrase("‫از تاریخ : ٥٢/٢/٠٩٣١‬",paramsFont));
//		paramsCell2.setBorder(paramsBorder);
//		paramsCell2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//		params.addCell(paramsCell2);
//		
//		PdfPCell paramsCell3 = new PdfPCell(new Phrase("‫تاریخ گزارش:‬‫٨/٧/١٩٣١‬",paramsFont));
//		paramsCell3.setBorder(paramsBorder);
//		paramsCell3.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
//		params.addCell(paramsCell3);
//		
//		PdfPCell paramsCell4 = new PdfPCell(new Phrase("‫تا تاریخ : ٦٢/٢/٠٩٣١‬",paramsFont));
//		paramsCell4.setBorder(paramsBorder);
//		paramsCell4.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//		params.addCell(paramsCell4);
		
		
		addEmptyLine(body, 1);
		
//		
//		 CellStyle cellStyleTitle = getCellStyleTitle(wb, param);
//		 CellStyle cellStyleParam = getCellStyleParam(wb, param);
//		 CellStyle cellStyleRowHeader = getCellStyleHeader(wb, param);
//
//		 CellStyle cellStyleColHeader = getCellStyleHeader(wb, param);
//		 cellStyleColHeader.setAlignment(CellStyle.ALIGN_CENTER);
//		 
//		 CellStyle cellStyleData = getCellStyleData(wb, param);
		 
		 
		 /*
		  * column header
		  */
		 
		 //PdfPTable data = new PdfPTable(cellSet.getAxes().get(0).getPositionCount());
		int columnCount = cellSet.getAxes().get(0).getPositionCount();
		 PdfPTable data = new PdfPTable(columnCount+1);
		 
		 //data.setRunDirection(getRUN_DIRECTION(param));
		 //data.setRunDirection(PdfWriter.RUN_DIRECTION_NO_BIDI);
		 data.setWidthPercentage(100f);
		 
		 //float[] dataWidths = new float[data.getNumberOfColumns()];
		 //dataWidths = data.getAbsoluteWidths();
		 //data.setWidths(arg0)
		 //float absoluteWidths = data.getRow(0).
		//		 data.setBorderColor(BaseColor.BLACK);
////		 data.setPadding(4);
////		 data.setSpacing(4);
//		 data.setBorderWidth(1);

		 
//		 if(param.getColumnHeader()==null){
//			 setCellValueByRowName(sheet,"h0",0,"");//column headers array//first row//first empty cell in table
//			 for (int i=0;i<cellSet.getAxes().get(0).getPositionCount();i++) {
//				 Position column = cellSet.getAxes().get(0).getPositions().get(i);
//				 
//				 for (int headerRowIndex = 0; headerRowIndex < column.getMembers().size(); headerRowIndex++) {
//					 Member member = column.getMembers().get(headerRowIndex);
//					 String value = member.getCaption();
//					 String rowName = "h"+headerRowIndex;
//					 int columnIndex = rowHeaderNeededColumns+i;
//					 setCellValueByRowName(sheet,rowName,columnIndex,value,cellStyleColHeader);
//				}
//			}
//		 }else
		 {
//			}
//			for (Iterator<AIPOlapExportHeaderItem> iter = param.getColumnHeader().iterator(); iter.hasNext();) {
			 List<AIPOlapExportHeaderItem> headers = param.getColumnHeader().list(); 

			 List<AIPOlapExportHeaderItem> rtl_headers = getRTLHeaders(headers,columnCount);
			 
			 float totalWidths[] = getDataTotalWidths(800,columnCount+1,rtl_headers);
			 data.setTotalWidth(totalWidths);
			 
			 for (int i = 0; i < rtl_headers.size(); i++) {
				AIPOlapExportHeaderItem header = rtl_headers.get(i);

				PdfPCell cell=new PdfPCell(new Phrase(header.value,headerFont));

				
				cell.setRunDirection(getRUN_DIRECTION(param));
				//PdfPCell cell=new PdfPCell(new Phrase(""+i,headerFont));
				//cell.setPhrase();
				cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				if(header.range.getLastRow()>header.range.getFirstRow()){
					cell.setRowspan(header.getRowspan());
				}
				if(header.range.getLastColumn()>header.range.getFirstColumn()){
					cell.setColspan(header.getColspan());
				}
				if(header.width>0){
					//dataWidths[header.range.getFirstCol] = header.width;
				}
				if(header.height>0){
					//cell.setFixedHeight(header.height);
				}
				if(header.nowrap){
					//cell.setNoWrap(header.nowrap);
				}
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				//
				 data.addCell(cell);
			 }
		 }
//		sheet.autoSizeColumn(0, true);
		 body.add(data);
		 //data.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
		

		/* 
		 * rows
		 */
		 Stack<PdfPCell> stackData = new Stack<PdfPCell>();
		if(cellSet.getAxes().size()>1){
			for (int rowIndex=0;rowIndex<cellSet.getAxes().get(1).getPositionCount();rowIndex++) {
				 Position row = cellSet.getAxes().get(1).getPositions().get(rowIndex);
				/*
				 * row header
				 */
//				 String rowName = "r"+rowIndex;
				 String value="";
				 for (int headerColumnIndex = 0; headerColumnIndex < row.getMembers().size(); headerColumnIndex++) {
					 Member member = row.getMembers().get(headerColumnIndex);
					 value += member.getCaption()+" ";
				}
				 /*
				  * param.getRemoveStringFromMembers()
				  */
				if(param.getRemoveStringFromMembers()!=null){
					value=AIPUtil.replaceAllString(value, param.getRemoveStringFromMembers(), "");
				}
				 
				PdfPCell pdfcell=new PdfPCell();
				pdfcell.setPhrase(new Phrase(value,headerFont));
				pdfcell.setRunDirection(getRUN_DIRECTION(param));
				 
				//data.addCell(pdfcell);
				stackData.push(pdfcell);
				
				/*
				 * data
				 */
				for (int j = 0; j < cellSet.getAxes().get(0).getPositionCount(); j++) {
					Position column = cellSet.getAxes().get(0).getPositions().get(j);
					Cell cell = cellSet.getCell(column, row);

					String cellValue = cell.getFormattedValue();
					if(NVL.isEmpty(cellValue)){
						cellValue="0";
					}
//					if(!NVL.isEmpty(cell.getErrorText())){
//						cellValue += cell.getErrorText();
//					}
					
					pdfcell=new PdfPCell();
					pdfcell.setPhrase(new Phrase(cellValue,dataFont));
					pdfcell.setRunDirection(getRUN_DIRECTION(param));
					//data.addCell(pdfcell);
					stackData.push(pdfcell);
				}
				
				while(!stackData.isEmpty()){
					data.addCell(stackData.pop());
				}
				
			}
		}else{// cellset has only columns
			/*
			 * data
			 */
			for (int j = 0; j < cellSet.getAxes().get(0).getPositionCount(); j++) {
				Position column = cellSet.getAxes().get(0).getPositions().get(j);
				Cell cell = cellSet.getCell(column);
				String cellValue = cell.getFormattedValue();
				if(!NVL.isEmpty(cell.getErrorText())){
					cellValue += cell.getErrorText();
				}
				
				PdfPCell pdfcell=new PdfPCell();
				pdfcell.setPhrase(new Phrase(cellValue));
				//data.addCell(pdfcell);
				stackData.push(pdfcell);
			}
			while(!stackData.isEmpty()){
				data.addCell(stackData.pop());
			}

		}
		//data.setWidths(dataWidths);
		 data.setComplete(true);
		 
		doc.add(body);
		doc.close();

	} catch (Exception e) {
			throw new AIPOlapExportException("اشکال در نوشتن اطلاعات در پی دی اف",e);
		}
		      		
	}

	private float[] getDataTotalWidths(int totalwidth,int columncount, List<AIPOlapExportHeaderItem> rtl_headers) {
		float totalWidths[] =  new float[columncount];//{ 144, 72, 72 };
		float w = (float)totalwidth/(float)columncount;
		for (int i = 0; i < totalWidths.length; i++) {
			totalWidths[i]=w;
		}
		
		float diff_width=0;
		int colindex =0;
		for (int i = 0; i < rtl_headers.size(); i++) {
			AIPOlapExportHeaderItem header = rtl_headers.get(i);
			if(header.width>0){
				float item_width = header.width/header.getColspan();
				for (int j = colindex; j < colindex+header.getColspan(); j++) {
					float diff_width_item = totalWidths[j]-item_width; 
					totalWidths[colindex]=item_width;
					diff_width+=diff_width_item;
				}
			}
			colindex+=header.getColspan();
			if(colindex>=columncount){
				colindex=0;
			}
		}
		
		return totalWidths;
	}
	private List<AIPOlapExportHeaderItem> getRTLHeaders(List<AIPOlapExportHeaderItem> headers, int columnCount) {
		List<AIPOlapExportHeaderItem> rtl_headers = new ArrayList<AIPOlapExportHeaderItem>();
		Stack<AIPOlapExportHeaderItem> stack = new Stack<AIPOlapExportHeaderItem>();
		
		//int[] rowSpans = new int[columnCount];
		int[] rowSpans = new int[headers.size()+headers.get(headers.size()-1).getColspan()-1];

		int cellscount =0;
		for(int i=0;i<headers.size();i++){
			AIPOlapExportHeaderItem header = headers.get(i);
			cellscount += header.getColspan();
			stack.push(header);
			
			if(header.getRowspan()>1){
				for(int j=i;j<(i+header.getColspan());j++){
					rowSpans[j]+=(header.getRowspan()-1);
				}
			}
			
			if(cellscount>=(columnCount+1)){
				while(!stack.isEmpty()){
					rtl_headers.add(stack.pop());
				}
				
				cellscount=0;
				
				for (int j = 0; j < rowSpans.length; j++) {
					if(rowSpans[j]>0){
						cellscount++;
						rowSpans[j]--;
					}
				}
				
			}
		}//for
		while(!stack.isEmpty()){
			rtl_headers.add(stack.pop());
		}
		
		return rtl_headers;
	}
	
	private void addEmptyLine(Paragraph paragraph, int number) {
	    for (int i = 0; i < number; i++) {
	      paragraph.add(new Paragraph(" "));
	    }
	  }	
	

	private int getRUN_DIRECTION(AIPOlapExportPdfParam param) {
		return param.getRightToLeft()?PdfWriter.RUN_DIRECTION_RTL:PdfWriter.RUN_DIRECTION_DEFAULT;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		System.out.println("AIPOlapExportExcel.main():starting.........");
		try {
			
			String mdx = "select { [Measures].tedadtavalod	} on columns" +
					", { DimMahal.mahalha.[All].children } on rows" +
					" from TavalodFot";
//			String mdx1 = "select {[tavalod1headerset]} on columns "+
//					",{DESCENDANTS([DimEdare].[ostanshahrestanedare].&[1000])} on rows "+ 
//					"from TavalodFot "; 
			String mdx_mondrian = "with member  measures.tedadtavalod_100 as '(measures.tedadtavalod*100) '" +
					" select {tavalod1headerset} on columns "+
					",{DESCENDANTS(DimEdare.[1000])} on rows "+ 
					"from Tavalod " +
					" where (measures.tedadtavalod_100)"; 
			
			//AIPOlapUtil olapUtil = new  AIPOlapUtil("http://192.168.0.71:80/olap/msmdpump.dll","AIPSabtBICube","bi_admin", "aippia");
			//AIPOlapUtil olapUtil = new  AIPOlapUtil("jdbc:mondrian:Jdbc=jdbc:oracle:thin:@localhost:1521:orcl1256;JdbcUser=olapuser;JdbcPassword=olapusr;Catalog=E:/java/apache-tomcat-7.0.23-sabtbi/webapps/AIPSabtBI/WEB-INF/AIPSabtBICube.mondrian.xml;Role=","AIPSabtBICube","", "");
			CellSet cellSet = AIPOlapUtil.executeMdx("jdbc/aipsabtbicube",mdx_mondrian);

			/*
			 * export params
			 */
			String title="‫١- گزارش تعداد ولادت های ثبت شده برحسب جنس / شهری و روستایی / جاری و معوقه‬";
			String paramString[]={"محل : تهران"
					,"از تاریخ : 1391/01/01"
					,"تاریخ گزارش: "+DateConvert.getTodayJalali()+" "+DateConvert.getTime()
					,"تا تاریخ : 1391/01/01"
					};

			AIPOlapExportHeader columnHeader = new AIPOlapExportHeader();

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

			
			AIPOlapExportPdf export = new AIPOlapExportPdf();
			File f = new File("/home/bip/aipolapexportexceltext.pdf");
			FileOutputStream fout = new FileOutputStream(f);
			
			
			AIPOlapExportPdfParam param=new AIPOlapExportPdfParam(title, paramString, columnHeader);
			//export.export(cellSet, fout,param);
			Tree<List<String>> tree = BIPOlapUtil.convertCellSet2Tree(cellSet);
			tree.sortTree();
			export.export(tree.getTreeList(), fout,param);
			fout.close();
			System.out.println("AIPOlapExportExcel.main():pdf write in :"+f.getAbsolutePath());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("AIPOlapExportExcel.main():end");
	}

	
}
