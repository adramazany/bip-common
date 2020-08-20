package aip.util.doc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts.util.RequestUtils;


import aip.report.AIPReportLST;
import aip.util.AIPException;
import aip.util.AIPPrintParam;
import aip.util.AIPUtil;
import aip.util.AIPWebUserParam;
import aip.util.NVL;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class AIPDocManager {

	static File basePath = null;
	//static File basePath = new File("/home/adl/aipdoc-cache/");
	private static String rootPath = null;

	public static void main(String[] args) {
		AIPDocManager doc=new AIPDocManager();
		try {
			doc.writeExcel(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AIPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isRequestForPrintOrSave(String reqCode){
		if("SaveHTML".equalsIgnoreCase(reqCode) 
				|| "SaveText".equalsIgnoreCase(reqCode)
				|| "SaveExcel".equalsIgnoreCase(reqCode)
				|| "SavePdf".equalsIgnoreCase(reqCode)
				|| "Preview".equalsIgnoreCase(reqCode)
				|| "Print".equalsIgnoreCase(reqCode)
		){
			return true;
		}else{
			return false;
		}
		
	}
	
	public static void printOrSaveDocument(HttpServletRequest request, HttpServletResponse response,String reqCode,AIPPrintParam printParam,AIPReportLST list){
		printOrSaveDocument(request, response, reqCode, printParam, list, list.getTitle());
	}
	public static void printOrSaveDocument(HttpServletRequest request, HttpServletResponse response,String reqCode,AIPPrintParam printParam,AIPReportLST list,String title){
		if("SaveHTML".equalsIgnoreCase(reqCode) 
				|| "SaveText".equalsIgnoreCase(reqCode)
				|| "SaveExcel".equalsIgnoreCase(reqCode)
				|| "SavePdf".equalsIgnoreCase(reqCode)
				|| "Preview".equalsIgnoreCase(reqCode)
				|| "Print".equalsIgnoreCase(reqCode)
				
		){
			if(NVL.isEmpty(rootPath)){
				rootPath = request.getSession().getServletContext().getRealPath("/../");
			}
			if(basePath==null){
				basePath = new File(rootPath,"aipdoc-cache");
				if(!basePath.exists()){
					basePath.mkdirs();
				}
			}
			
			
			AIPDocParam docParam = new AIPDocParam();//getParamOfPrint(request);
			docParam.setPrintParam(printParam);
			docParam.setTitle(title);
			docParam.setList(list);
			docParam.setWebUserParam(new AIPWebUserParam(request));
			
			docParam.setPdfFontPath( request.getRealPath("font") );
			
			//printParam.setParams(lst.getParamString());
			//printParam.setLst(lst.getDtos());
			//printParam.setColumnNames(columnNames)

			if("SaveHTML".equalsIgnoreCase(reqCode)){
				try {
					response.setContentType("text/html;charset:UTF-8");
					PrintWriter out = response.getWriter();
					try {
						AIPDocManager docManager = new AIPDocManager();
						
						File htmlFile = docManager.writeHTML(docParam);
						out.write("/aipdoc-cache/"+ htmlFile.getName());
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
						out.write( NVL.getString(e.getMessage()+"!!!","خطای null"));
						out.flush();
					} 
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}else if("SaveText".equalsIgnoreCase(reqCode)){
				try {
					response.setContentType("text/html;charset:UTF-8;");
					PrintWriter out = response.getWriter();
					try {
						AIPDocManager docManager = new AIPDocManager();
						File txtFile = docManager.writeText(docParam);
						out.write("/aipdoc-cache/"+ txtFile.getName());
						//String text = docManager.generateText(docParam);
						//out.write( new String( text.getBytes() , "iso-8859-1" ) );
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
						out.write( NVL.getString(e.getMessage()+"!!!","خطای null"));
						out.flush();
					} 
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}else if("SaveExcel".equalsIgnoreCase(reqCode)){
				try {
					//response.setContentType("text/html;charset:UTF-8");
					PrintWriter out = response.getWriter();
					try {
						AIPDocManager docManager = new AIPDocManager();
						
						File xlsFile = docManager.writeExcel(docParam);
						out.write("/aipdoc-cache/"+ xlsFile.getName());
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
						out.write( NVL.getString(e.getMessage()+"!!!","خطای null"));
						out.flush();
					} 
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}else if("SavePdf".equalsIgnoreCase(reqCode)){
				try {
					response.setContentType("text/html;charset:UTF-8");
					PrintWriter out = response.getWriter();
					try {
						AIPDocManager docManager = new AIPDocManager();
						
						String pdfFontPath = docParam.getPdfFontPath()+"/"+docParam.getPdfFontName();
						if(!(new File(pdfFontPath)).exists()){
							String pdfFontPathROOT ="../ROOT";
							pdfFontPath = pdfFontPathROOT+"/"+docParam.getPdfFontName();
							docParam.setPdfFontPath( request.getSession().getServletContext().getRealPath(pdfFontPathROOT) );
						}
						
						File pdfFile = docManager.writePdf(docParam);
						out.write("/aipdoc-cache/"+pdfFile.getName());
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
						out.write( NVL.getString(e.getMessage()+"!!!","خطای null"));
						out.flush();
					} 
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}else if("Preview".equalsIgnoreCase(reqCode)){
				try {
					response.setContentType("text/html;charset:UTF-8");
					PrintWriter out = response.getWriter();
					try {
						AIPDocManager docManager = new AIPDocManager();
						
						File htmlFile = docManager.writePreview(docParam);
						out.write("/aipdoc-cache/"+htmlFile.getName());
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
						out.write( NVL.getString(e.getMessage()+"!!!","خطای null"));
						out.flush();
					} 
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}else if("Print".equalsIgnoreCase(reqCode)){
				try {
					PrintWriter out = response.getWriter();
					try {
						response.setContentType("text/html;charset:UTF-8");
						AIPDocManager docManager = new AIPDocManager();
						
						File pdfFile = docManager.writePrint(docParam);
						out.write("/aipdoc-cache/"+pdfFile.getName());
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
						out.write( NVL.getString(e.getMessage()+"!!!","خطای null"));
						out.flush();
					} 
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	
	
	private String generateHTML(AIPDocParam docParam)throws AIPException{
		return generateHTML(docParam,"");
	}
	private String generateHTML(AIPDocParam docParam,String extraHead)throws AIPException{
		docParam.verify();
		
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<meta http-equiv='Content-Type' content='text/html;charset=UTF-8'/>");
		sb.append("<style type='text/css'>");
		sb.append(".dataList table {border-top: 1px solid #e5eff8;border-right: 1px solid #e5eff8;margin: 5px auto;border-collapse: collapse;}");
		sb.append(".dataList th {background: #f4f9fe;text-align: center;border-left: 1px solid #e5eff8;font: bold 16px 'Century Gothic', 'Trebuchet MS', Arial, Helvetica, sans-serif;");
		sb.append("	padding-right: 5px;color: #66a3d3;padding-top: 5px;padding-bottom: 5px;}");
		sb.append(".dataList table td {border-bottom: 1px solid #e5eff8;border-left: 1px solid #e5eff8;padding: .3em 1em;text-align: right;}");
		sb.append(".dataList .collectionItemTR table {margin: 0px;}");
		sb.append(".oddRows {background: #f4f9fe ;}");
		sb.append(".evenRows {background-color: white;}");
		sb.append(".clsResultSummerize{background-color: #f4f9fe;width: 100%;}");
		sb.append(".clsResultSummerize td{border-style: dotted;border-width: 1px;}");
		sb.append("</style>");
		sb.append(extraHead);
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<h1>");
		sb.append(docParam.title);
		sb.append("</h1>");
		if(!NVL.isEmpty(docParam.getList().getParamString())){
			sb.append("<h3>");
			sb.append("پارامترها:");
			sb.append("</h3>");
		}

		sb.append("<table class='dataList' >");//style='width: 20cm; table-layout: fixed;'
		
		
		String[] colLabels =docParam.getList().getVisibleColumnLabels();//.getSplitedColumns()
		
		if(docParam.getList().getRows()!=null && colLabels.length>0){
			sb.append("<tr>");
			for(int i=0;i<colLabels.length;i++){
				sb.append("<th>");
				sb.append(colLabels[i]);
				sb.append("</th>");
			}
			sb.append("</tr>");
			
			String[] colFieldsGetter = docParam.getList().getVisibleColumnFieldsGetter();
			for(int i=0;i<docParam.getList().getRows().size();i++){
				Object obj = docParam.getList().getRows().get(i);
				
				sb.append("<tr class='");
				sb.append(i%2==0 ? "evenRows" : "oddRows");
				sb.append("'>");
				
				for(int j=0;j<colFieldsGetter.length;j++){
					sb.append("<td>");
					try {
						sb.append( getObjectFieldValue( obj , colFieldsGetter[j] , i , "&nbsp;" ) );
					} catch (Exception e) {
						e.printStackTrace();
					}
					sb.append("</td>");
				}
				sb.append("</tr>");
			}
		}
		sb.append("</table>");
		
		sb.append("<table Class='clsResultSummerize'>");
		sb.append("<tr>");
		sb.append("<td>صفحه</td>");
		sb.append("<td>تعداد</td>");
		sb.append("<td>"+ NVL.getIntFormat( docParam.getList().getRows().size() ,"#,###")+"</td>");

		String[] pageKeys = docParam.getList().getSumPageKeys();
		for(int i=0;i<pageKeys.length;i++){
			sb.append("<td>"+docParam.getList().getSumPageLabel(pageKeys[i]) +"</td>");
			sb.append("<td>"+docParam.getList().getSumPage(pageKeys[i],"#,###")+"</td>");
		}
		
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td>کل</td>");
		sb.append("<td>تعداد</td>");
		sb.append("<td>"+ NVL.getIntFormat( docParam.getList().getTotalItems() ,"#,###")+"</td>");

		String[] totalKeys = docParam.getList().getSumTotalKeys();
		for(int i=0;i<totalKeys.length;i++){
			sb.append("<td>"+docParam.getList().getSumTotalLabel(totalKeys[i]) +"</td>");
			sb.append("<td>"+docParam.getList().getSumTotal(totalKeys[i],"#,###")+"</td>");
		}
		sb.append("</tr>");
		sb.append("</table>");
		
		
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}
	
	private String getObjectFieldValue(Object obj, String fieldName,int row, String emptyValue) throws Exception {
		String value = emptyValue;
		if("radif".equalsIgnoreCase(fieldName) || "~radif".equalsIgnoreCase(fieldName) ){
			value=""+(row+1);
		}else if( NVL.isEmpty( fieldName ) || fieldName.charAt(0)=='~' ){
		}else{
			value=NVL.getString(AIPUtil.invokeGetter(obj, fieldName) ,emptyValue);
		}
		return value;
	}


	public File writeHTML(AIPDocParam docParam) throws IOException,AIPException{
		File f = AIPUtil.createTempFile("aip-", ".html",getBasePath(),docParam.getWebUserParam());
		FileOutputStream fout = new FileOutputStream(f);
		String html = generateHTML(docParam);
		fout.write(html.getBytes("UTF-8"));
		return f;
	}
	public File writePreview(AIPDocParam docParam)throws IOException ,AIPException{
		File f = AIPUtil.createTempFile("aip-", ".html",getBasePath(),docParam.getWebUserParam());
		FileOutputStream fout = new FileOutputStream(f);
		String html = generateHTML(docParam);
		fout.write(html.getBytes("UTF-8"));
		return f;
	}


	public File writePrint(AIPDocParam docParam) throws IOException,AIPException{
		File f = AIPUtil.createTempFile("aip-", ".html",getBasePath(),docParam.getWebUserParam());
		FileOutputStream fout = new FileOutputStream(f);
		String extraHead="<script type='text/javascript'>window.print();</script>";
		String html = generateHTML(docParam,extraHead);
		fout.write(html.getBytes("UTF-8"));
		return f;
	}
	
	
	public File writePdf(AIPDocParam docParam) throws IOException,AIPException{
		docParam.verify();

		File dest = AIPUtil.createTempFile("aip-", ".pdf",getBasePath(),docParam.getWebUserParam());
		Document doc = new Document();
		//doc.setPageSize( PageSize.A4.rotate() );
		try {

			if(docParam.getPrintParam().getPageSize()!=null)doc.setPageSize(docParam.getPrintParam().getPageSize());
			
			if(docParam.getPrintParam().isLandscape())doc.setPageSize(doc.getPageSize().rotate());
			
			
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			PdfWriter pdfw = PdfWriter.getInstance(doc,new FileOutputStream(dest));
			doc.open();

			doc.addAuthor("رمضانی");
			doc.addCreator("درگاه هوش مصنوعی");
			//doc.addHeader(arg0, arg1)
			doc.addKeywords("");
			doc.addProducer();
			doc.addSubject("سامانه یکپارچه بانکی");
			doc.addTitle(docParam.getTitle());
			
			/*
			 * Style
			 */
			
			String pdfFontPath = docParam.getPdfFontPath()+"/"+docParam.getPdfFontName();
//			if(!(new File(pdfFontPath)).exists()){
//				String pdfFontPathROOT ="../ROOT";
//				pdfFontPath = pdfFontPathROOT+"/"+docParam.getPdfFontName();
//			}
			System.out.println("::::::pdfFontPath="+new File(pdfFontPath).getAbsolutePath());
			BaseFont baseFont = BaseFont.createFont(pdfFontPath,BaseFont.IDENTITY_H, true);
			//BaseFont baseFont = BaseFont.createFont(BaseFont.TIMES_ROMAN,BaseFont.IDENTITY_H, true);
			//BaseFont baseFont = BaseFont.createFont(BaseFont.TIMES_ROMAN,"UTF-8", true);
			Font fontTitle = new Font(baseFont, 24, Font.BOLD,BaseColor.BLUE);
			Font fontParams = new Font(baseFont);
			Font fontTable = new Font(baseFont);
			BaseColor colorTableHeader = new BaseColor( 0xf4f9fe);
			
			/*
			 * Title
			 */
			PdfPTable title = new PdfPTable(1);
			title.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			title.addCell(new Phrase(docParam.getTitle(),fontTitle));
			
			/*
			 * Params
			 */
			PdfPTable params = new PdfPTable(1);
			params.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			params.addCell(new Phrase(docParam.getList().getParamString(),fontParams));
			
			/*
			 * Data
			 */
			String[] colLabels = docParam.getList().getVisibleColumnLabels(); //docParam.getSplitedColumns();
			PdfPTable data = new PdfPTable(colLabels.length);
			data.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			//data.setHeaderRows(1);
			for(int i=0;i<colLabels.length;i++){
				PdfPCell cell = new PdfPCell(new Phrase(colLabels[i],fontTable));
				//cell.setBackgroundColor( colorTableHeader );
				data.addCell(cell);
			}
			String[] colFieldsGetter =  docParam.getList().getVisibleColumnFieldsGetter();
			for(int i=0;i<docParam.getList().getRows().size();i++){
				Object obj = docParam.getList().getRows().get(i);
				for(int j=0;j<colFieldsGetter.length;j++){
					try {
						String value = getObjectFieldValue( obj , colFieldsGetter[j] , i , "" ) ;
						Phrase cell =new Phrase( value , fontTable );
						data.addCell(cell);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			/*
			 * Summary
			 */

			PdfPTable summary = new PdfPTable(Math.max(docParam.getList().getSumPageCount(), docParam.getList().getSumTotalCount())+1);
			summary.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

			summary.addCell(new Phrase("صفحه",fontParams));
			summary.addCell(new Phrase("تعداد",fontParams));
			summary.addCell(new Phrase(NVL.getIntFormat( docParam.getList().getRows().size() ,"#,###"),fontParams));
			summary.addCell(new Phrase("",fontParams));

			String[] pageKeys = docParam.getList().getSumPageKeys();
			for(int i=0;i<pageKeys.length;i++){
				summary.addCell(new Phrase(docParam.getList().getSumPageLabel(pageKeys[i]),fontParams));
				summary.addCell(new Phrase(docParam.getList().getSumPage(pageKeys[i],"#,###"),fontParams));
			}
			
			summary.addCell(new Phrase("کل",fontParams));
			summary.addCell(new Phrase("تعداد",fontParams));
			summary.addCell(new Phrase(NVL.getIntFormat( docParam.getList().getTotalItems() ,"#,###"),fontParams));

			String[] totalKeys = docParam.getList().getSumTotalKeys();
			for(int i=0;i<totalKeys.length;i++){
				summary.addCell(new Phrase(docParam.getList().getSumTotalLabel(totalKeys[i]),fontParams));
				summary.addCell(new Phrase(docParam.getList().getSumTotal(totalKeys[i],"#,###"),fontParams));
			}
			

			/*
			 * add to document
			 */
			doc.add(title);
			doc.add(params);
			doc.add(data);
			doc.add(summary);
			
			doc.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		return dest;
	}
	
	public File writeExcel(AIPDocParam docParam) throws IOException,AIPException{
		docParam.verify();


		HSSFWorkbook wb = new HSSFWorkbook();
		
		 HSSFSheet sheet = wb.createSheet("aip");
		 HSSFRow row = null;

		 // Aqua background
		 HSSFCellStyle style = wb.createCellStyle();
		 //style.setFillBackgroundColor(HSSFColor.AQUA.index);
		 //style.setFillPattern(HSSFCellStyle.BIG_SPOTS);
		 //style.setFont(new Font("Arial",16,java.awt.Font.BOLD));
		 //style.setFillForegroundColor(style)
		 HSSFCell cell =null;
		 
		 /*
		  * title
		  */
		 row = sheet.createRow(0);
		 HSSFCell title = row.createCell(1);
		 title.setCellValue(docParam.getTitle());
		 //cell.setCellStyle(style);

		 /*
		  * params
		  */
		 if(!NVL.isEmpty( docParam.getList().getParamString() ) ){
			 row = sheet.createRow(1);
			 HSSFCell paramsTitle = row.createCell(0);
			 paramsTitle.setCellValue("پارامترها:");
			 HSSFCell params = row.createCell(1);
			 params.setCellValue(docParam.getList().getParamString());
		 }
		 
		 // Orange "foreground",
		 //     foreground being the fill foreground not the font color.
		 style = wb.createCellStyle();
		 style.setFillForegroundColor(HSSFColor.ORANGE.index);
		 //style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			/*
			 * Data
			 */
			String[] colLabels = docParam.getList().getVisibleColumnLabels(); //docParam.getSplitedColumns();
			
			row = sheet.createRow(2);
			for(int i=0;i<colLabels.length;i++){
				cell = row.createCell(i);
		 		cell.setCellValue(colLabels[i]);
				cell.setCellStyle(style);
			}
			String[] colFieldsGetter = docParam.getList().getVisibleColumnFieldsGetter();
			for(int i=0;i<docParam.getList().getRows().size();i++){
				row = sheet.createRow(3+i);
				Object obj = docParam.getList().getRows().get(i);
				for(int j=0;j<colFieldsGetter.length;j++){
					try {
						String value = getObjectFieldValue( obj , colFieldsGetter[j] , i , "" ) ;
						cell = row.createCell(j);
				 		cell.setCellValue(value);
						cell.setCellStyle(style);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			/*
			 * Summary
			 */

			row = sheet.createRow(docParam.getList().getRows().size()+4);
			cell = row.createCell(0);cell.setCellValue("صفحه");cell.setCellStyle(style);
			cell = row.createCell(1);cell.setCellValue("تعداد");cell.setCellStyle(style);
			cell = row.createCell(2);cell.setCellValue(NVL.getIntFormat( docParam.getList().getRows().size() ,"#,###"));cell.setCellStyle(style);
			String[] pageKeys = docParam.getList().getSumPageKeys();
			for(int i=0;i<pageKeys.length;i++){
				cell = row.createCell(i*2+3);cell.setCellValue(docParam.getList().getSumPageLabel(pageKeys[i]));cell.setCellStyle(style);
				cell = row.createCell(i*2+4);cell.setCellValue(docParam.getList().getSumPage(pageKeys[i],"#,###"));cell.setCellStyle(style);
			}
			
			row = sheet.createRow(docParam.getList().getRows().size()+5);
			cell = row.createCell(0);cell.setCellValue("کل");cell.setCellStyle(style);
			cell = row.createCell(1);cell.setCellValue("تعداد");cell.setCellStyle(style);
			cell = row.createCell(2);cell.setCellValue(NVL.getIntFormat( docParam.getList().getTotalItems() ,"#,###"));cell.setCellStyle(style);
			String[] totalKeys = docParam.getList().getSumTotalKeys();
			for(int i=0;i<totalKeys.length;i++){
				cell = row.createCell(i*2+3);cell.setCellValue(docParam.getList().getSumTotalLabel(totalKeys[i]));cell.setCellStyle(style);
				cell = row.createCell(i*2+4);cell.setCellValue(docParam.getList().getSumTotal(totalKeys[i],"#,###"));cell.setCellStyle(style);
			}

		 
		 // Write the output to a file
		 File dest = AIPUtil.createTempFile("aip-", ".xls",getBasePath(),docParam.getWebUserParam());
		 FileOutputStream fileOut = new FileOutputStream(dest);
		 wb.write(fileOut);
		 fileOut.close();
		      		
		return dest;
	}
	
	
	public String generateText(AIPDocParam docParam) throws AIPException{
		docParam.verify();
		
		StringBuffer sb = new StringBuffer();
		sb.append(docParam.getTitle());
		sb.append("\r\n");
		if(!NVL.isEmpty(docParam.getList().getParamString())){
			sb.append("پارامترها:");
			sb.append(docParam.getList().getParamString());
		}
		sb.append("\r\n");

		String[] colLabels = docParam.getList().getVisibleColumnLabels(); //docParam.getSplitedColumns();
		
		if(docParam.getList().getRows()!=null && colLabels!=null){
			for(int i=0;i<colLabels.length;i++){
				sb.append(colLabels[i]);
				sb.append("\t\t");
			}
			sb.append("\r\n");
			String[] colFieldsGetter = docParam.getList().getVisibleColumnFieldsGetter();
			for(int i=0;i<docParam.getList().getRows().size();i++){
				Object obj = docParam.getList().getRows().get(i);
				
				for(int j=0;j<colFieldsGetter.length;j++){
					try {
						sb.append( getObjectFieldValue( obj , colFieldsGetter[j] , i , "" ) );
					} catch (Exception e) {
						e.printStackTrace();
					}
					sb.append("\t\t");
				}
				sb.append("\r\n");
			}
		}
		/*
		 * Summary
		 */

		sb.append("صفحه");
		sb.append("\t\t");
		sb.append("تعداد");
		sb.append(":");
		sb.append(NVL.getIntFormat( docParam.getList().getRows().size() ,"#,###"));
		sb.append("\t\t");
		String[] pageKeys = docParam.getList().getSumPageKeys();
		for(int i=0;i<pageKeys.length;i++){
			sb.append(docParam.getList().getSumPageLabel(pageKeys[i]));
			sb.append(":");
			sb.append(docParam.getList().getSumPage(pageKeys[i],"#,###"));
			sb.append("\t\t");
		}
		
		sb.append("\r\n");
		sb.append("کل");
		sb.append("\t\t");
		sb.append("تعداد");
		sb.append(":");
		sb.append(NVL.getIntFormat( docParam.getList().getTotalItems() ,"#,###"));
		sb.append("\t\t");
		String[] totalKeys = docParam.getList().getSumTotalKeys();
		for(int i=0;i<totalKeys.length;i++){
			sb.append(docParam.getList().getSumTotalLabel(totalKeys[i]));
			sb.append(":");
			sb.append(docParam.getList().getSumTotal(totalKeys[i],"#,###"));
			sb.append("\t\t");
		}

		
		
		return sb.toString();
	}
	public File writeText(AIPDocParam docParam) throws IOException,AIPException{
		File f = AIPUtil.createTempFile("aip-", ".txt",getBasePath(),docParam.getWebUserParam());
		FileOutputStream fout = new FileOutputStream(f);
		String text = generateText(docParam);
		fout.write( text.getBytes("UTF-8") );
		fout.close();
//		fout.write( new String( text.getBytes() , "iso-8859-1" ) );
//		fout.write( new String( text.getBytes(),"iso-8859-1").getBytes() );
//		PrintWriter out = new PrintWriter(fout);
//		out.write( new String( text.getBytes() , "iso-8859-1" ) );
//		out.close();
		return f;
	}
	
	
	
	
	public static File getBasePath() {
		if(!basePath.exists()){
			basePath.mkdirs();
		}
		return basePath;
	}
	public static void setBasePath(File basePath) {
		AIPDocManager.basePath = basePath;
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
