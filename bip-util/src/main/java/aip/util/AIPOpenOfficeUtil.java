package aip.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;


public class AIPOpenOfficeUtil {
	
	File basePath = new File("../webapps/docconverter-cache/");
	
	public void convert(File src,File dest) {
		OpenOfficeConnection connection = null; 
		Properties p =loadOooProperties("OpenOffice");
		connection = new SocketOpenOfficeConnection(p.getProperty("host","127.0.0.1"),NVL.getInt(p.getProperty("port"),8100));
		DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
		converter.convert(src, dest);
		connection.disconnect();
	}
	public void convert(File src,File dest,DocumentFormat dfdest) {
		OpenOfficeConnection connection = null; 
		Properties p =loadOooProperties("OpenOffice");
		connection = new SocketOpenOfficeConnection(p.getProperty("host","127.0.0.1"),NVL.getInt(p.getProperty("port"),8100));
		DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
		converter.convert(src, dest,dfdest);
		connection.disconnect();
	}
	public void convert(File src,DocumentFormat dfsrc,File dest,DocumentFormat dfdest) {
		OpenOfficeConnection connection = null; 
		Properties p =loadOooProperties("OpenOffice");
		connection = new SocketOpenOfficeConnection(p.getProperty("host","127.0.0.1"),NVL.getInt(p.getProperty("port"),8100));
		DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
		converter.convert(src, dfsrc, dest, dfdest);
		connection.disconnect();
	}
	
	public String generateHTML(AIPOpenOfficeUtilParam param){
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
		sb.append("<style type='text/css'>");
		sb.append(".dataList table {border-top: 1px solid #e5eff8;border-right: 1px solid #e5eff8;margin: 5px auto;border-collapse: collapse;}");
		sb.append(".dataList th {background: #f4f9fe;text-align: center;border-left: 1px solid #e5eff8;font: bold 16px 'Century Gothic', 'Trebuchet MS', Arial, Helvetica, sans-serif;");
		sb.append("	padding-right: 5px;color: #66a3d3;padding-top: 5px;padding-bottom: 5px;}");
		sb.append(".dataList table td {border-bottom: 1px solid #e5eff8;border-left: 1px solid #e5eff8;padding: .3em 1em;text-align: right;}");
		sb.append(".dataList .collectionItemTR table {margin: 0px;}");
		sb.append(".oddRows {background: #f4f9fe ;}");
		sb.append(".evenRows {background-color: white;}");

		sb.append("</style>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<h1>");
		sb.append(param.title);
		sb.append("</h1>");
		if(!NVL.isEmpty(param.params)){
			sb.append("<h3>");
			sb.append("پارامترها:");
			sb.append("</h3>");
		}

		sb.append("<table class='dataList' style='width: 20cm; table-layout: fixed;'>");
		
		String[] cols =param.getSplitedColumns();
		
		if(param.lst!=null && cols!=null){
			sb.append("<tr>");
			for(int i=0;i<cols.length;i++){
				sb.append("<th>");
				sb.append(cols[i]);
				sb.append("</th>");
			}
			sb.append("</tr>");
			for(int i=0;i<param.lst.size();i++){
				Object obj = param.lst.get(i);
				
				sb.append("<tr class='");
				sb.append(i%2==0 ? "evenRows" : "oddRows");
				sb.append("'>");
				
				for(int j=0;j<cols.length;j++){
					sb.append("<td>");
					try {
						
						String fieldName = cols[j].substring(0,1).toUpperCase()+cols[j].substring(1);
						sb.append(NVL.getString(AIPUtil.invokeGetter(obj, fieldName) ,"&nbsp;"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					sb.append("</td>");
				}
				sb.append("</tr>");
			}
		}
		sb.append("</table>");
		
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}
	public File writeHTML(AIPOpenOfficeUtilParam param) throws IOException{
		File f = createTempFile("aip-", ".html");
		FileOutputStream fout = new FileOutputStream(f);
		String html = generateHTML(param);
		fout.write(html.getBytes());
		return f;
	}
	
	public File writeExcel(AIPOpenOfficeUtilParam param) throws IOException{
		File src = writeHTML(param);
		File dest = createTempFile("aip-", ".xsl");
		//File dest = createTempFile("aip-", ".ods");
		convert(src, dest);
		return dest;
	}
	
	public File writePdf(AIPOpenOfficeUtilParam param) throws IOException{
		File dest = createTempFile("aip-", ".pdf");
		Document doc = new Document();
		//doc.setPageSize( PageSize.A4.rotate() );
		try {

			if(param.getPageSize()!=null)doc.setPageSize(param.getPageSize());
			
			if(param.isLandscape())doc.setPageSize(doc.getPageSize().rotate());
			
			
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			PdfWriter pdfw = PdfWriter.getInstance(doc,new FileOutputStream(dest));
			doc.open();

			doc.addAuthor("رمضانی");
			doc.addCreator("درگاه هوش مصنوعی");
			//doc.addHeader(arg0, arg1)
			doc.addKeywords("");
			doc.addProducer();
			doc.addSubject("سامانه یکپارچه بانکی");
			doc.addTitle(param.getTitle());
			
			/*
			 * Style
			 */
			BaseFont baseFont = BaseFont.createFont("font/ARIALUNI.TTF",BaseFont.IDENTITY_H, true);
			Font fontTitle = new Font(baseFont, 24, Font.BOLD,BaseColor.BLUE);
			Font fontParams = new Font(baseFont);
			Font fontTable = new Font(baseFont);
			BaseColor colorTableHeader = new BaseColor( 0xf4f9fe);
			
			/*
			 * Title
			 */
			PdfPTable title = new PdfPTable(1);
			title.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			title.addCell(new Phrase(param.getTitle(),fontTitle));
			
			/*
			 * Params
			 */
			PdfPTable params = new PdfPTable(1);
			params.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			params.addCell(new Phrase(param.getParams(),fontParams));
			
			/*
			 * Data
			 */
			String[] cols =param.getSplitedColumns();
			PdfPTable data = new PdfPTable(cols.length);
			data.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			title.setHeaderRows(1);
			for(int i=0;i<cols.length;i++){
				PdfPCell cell = new PdfPCell(new Phrase(cols[i],fontTable));
				//cell.setBackgroundColor( colorTableHeader );
				data.addCell(cell);
			}
			for(int i=0;i<param.lst.size();i++){
				Object obj = param.lst.get(i);
				for(int j=0;j<cols.length;j++){
					try {
						String fieldName = cols[j].substring(0,1).toUpperCase()+cols[j].substring(1);
						Phrase cell =new Phrase( NVL.getString(AIPUtil.invokeGetter(obj, fieldName) ) , fontTable );
						data.addCell(cell);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			/*
			 * add to document
			 */
			doc.add(title);
			doc.add(params);
			doc.add(data);
			
			doc.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		return dest;
	}
	
	
	
	public static final String[] getPageSizeNames(){
		return new String[]{
				"A4"
				,"LETTER"
				,"NOTE"
				,"LEGAL"
				,"TABLOID"
				,"EXECUTIVE"
				,"POSTCARD"
				,"A0"
				,"A1"
				,"A2"
				,"A3"
				,"A4"
				,"A5"
				,"A6"
				,"A7"
				,"A8"
				,"A9"
				,"A10"
				,"B0"
				,"B1"
				,"B2"
				,"B3"
				,"B4"
				,"B5"
				,"B6"
				,"B7"
				,"B8"
				,"B9"
				,"B10"
				,"ARCH_E"
				,"ARCH_D"
				,"ARCH_C"
				,"ARCH_B"
				,"ARCH_A"
				,"FLSA"
				,"FLSE"
				,"HALFLETTER"
				,"_11X17"
				,"ID_1"
				,"ID_2"
				,"ID_3"
				,"LEDGER"
				,"CROWN_QUARTO"
				,"LARGE_CROWN_QUARTO"
				,"DEMY_QUARTO"
				,"ROYAL_QUARTO"
				,"CROWN_OCTAVO"
				,"LARGE_CROWN_OCTAVO"
				,"DEMY_OCTAVO"
				,"ROYAL_OCTAVO"
				,"SMALL_PAPERBACK"
				,"PENGUIN_SMALL_PAPERBACK"
				,"PENGUIN_LARGE_PAPERBACK"
		};
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	File createTempFile(String prefix,String suffix) throws IOException{
		if(!basePath.exists()){
			boolean b = basePath.mkdirs();
			if(!b)throw new IOException("اشکال در مسیر ایجاد فایلهای تبدیل شده!!!"); 
		}
		String fn = prefix + Math.random() + suffix;
		return  new File(basePath , fn);
	}

	
	public static String getSystemCharset(){
		return System.getProperty("sun.jnu.encoding", System.getProperty("file.encoding", "UTF8"));//UTF8 | Cp1256
	}
	
	private static Properties loadOooProperties(String resourceName){
		final  ResourceBundle rb = ResourceBundle.getBundle(resourceName);
		 Properties result = new Properties ();
         for (Enumeration keys = rb.getKeys (); keys.hasMoreElements ();)
         {
             final String key = (String) keys.nextElement ();
             final String value = rb.getString (key);
             
             result.put (key, value);
         } 
         return result;
	}

	
	
	
	public static void main(String[] args) {
		System.out.println("AIPOpenOfficeUtil.main().start.......");
		Document doc = new Document();
		doc.setPageSize( PageSize.A4.rotate() );
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			PdfWriter pdfw = PdfWriter.getInstance(doc,new FileOutputStream("/home/adl/test.pdf"));
			pdfw.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			//PdfWriter pdfw = PdfWriter.getInstance(doc,buffer);

			doc.open();

			BaseFont bf = BaseFont.createFont("font/ARIALUNI.TTF",BaseFont.IDENTITY_H, true);
			//BaseFont bf = BaseFont.createFont("/home/adl/font/ARIALUNI.TTF",BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			//BaseFont.CHAR_RANGE_ARABIC
			//bf.correctArabicAdvance();
			Font f2 = new Font(bf, 24, Font.NORMAL,BaseColor.BLUE);

			
			PdfString st = new PdfString("تست");

			PdfPCell st2 = new PdfPCell(new Phrase("تست۲",f2));
			doc.add(st2);
			
			Paragraph p = new Paragraph(new Phrase("تست۲---2",f2));
			doc.add(p);
			
			PdfContentByte cb = new PdfContentByte(pdfw);
			ColumnText ct = new ColumnText(cb);
			ct.setSimpleColumn(100, 300, 200, 500);
			//ct.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			//ct.setArabicOptions(ColumnText.DIGITS_EN2AN);
			

			ct.addText(new Phrase("تست۲---3333",f2));
			ct.go();
			
			
			
			PdfPTable table = new PdfPTable(2);
			table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			
			
			Phrase pa = new Phrase(  "تست فارسی 123",f2 );

//			ct.showTextAligned(cb,PdfContentByte.ALIGN_RIGHT, pa,
//                    doc.right(), doc.top() +f2.getCalculatedSize(), 0,PdfWriter.RUN_DIRECTION_RTL, ColumnText.AR_NOVOWEL); 
			
			PdfPCell pc = new PdfPCell(pa);
			//pc.setArabicOptions(ColumnText.AR_NOVOWEL);//AR_NOVOWEL, AR_COMPOSEDTASHKEEL and AR_LIG.
			pc.setArabicOptions(ColumnText.DIGITS_EN2AN);
			pc.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			pc.setBackgroundColor(BaseColor.YELLOW);
			
			
			table.addCell( pc );
			table.addCell("2");
			table.addCell("3");
			table.addCell("4");
			table.addCell("5");
			table.addCell("6");		
			
			doc.add(table);

            Font font = new Font(bf, 12);
            String text1 = "This is the quite popular True Type font 'Comic'.";
            String text2 = "Some greek characters: \u0393\u0394\u03b6";
            String text3 = "Some cyrillic characters: \u0418\u044f";
            String text4 = "تست فارسی";
            doc.add(new Paragraph(text1, font));
            doc.add(new Paragraph(text2, font));
            doc.add(new Paragraph(text3, font));
			
			doc.close();
//			FileOutputStream fout = new FileOutputStream("/home/adl/test1.pdf");
//			fout.write(buffer.toByteArray() );
//			fout.close();
			
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		System.out.println("AIPOpenOfficeUtil.main().finished.");
	}
	


	
	
	
}



/*
	public File writePdf(AIPOpenOfficeUtilParam param) throws IOException{
		File src = writeHTML(param);
		File dest = createTempFile("aip-", ".pdf");
		DocumentFormat customPdfFormat = new DocumentFormat("Portable Document Format", "application/pdf", "pdf");
		//customPdfFormat.setExportFilter(DocumentFamily.TEXT, "writer_pdf_Export");
		Map pdfOptions = new HashMap();
		
		//Example
		//pdfOptions.put("EncryptFile", Boolean.TRUE);
		//pdfOptions.put("DocumentOpenPassword", "mysecretpassword");
		//pdfOptions.put("Paper Size", "A5 148\"x210\"");
		//pdfOptions.put("Paper Size", "A5, Portrait (4 x 4 inch)");

		//PDF properties
		pdfOptions.put("TITLE", param.getTitle());
		//pdfOptions.put("LOCATION", "");
		pdfOptions.put("SUBJECT", "سامانه گزارشات یکپارچه بانکی");
		pdfOptions.put("AUTHOR", "رمضانی");
		pdfOptions.put("KEYWORDS", "");
		//pdfOptions.put("PRODUCER", "");
		pdfOptions.put("CREATOR", "درگاه هوش مصنوعی");
		//pdfOptions.put("CREATED", "");
		//pdfOptions.put("MODIFIED", "");
		//pdfOptions.put("FORMAT", "PDF-1.4");
		//pdfOptions.put("NUMBEROFPAGES", "");
		//pdfOptions.put("OPTIMIZED", "");
		//pdfOptions.put("SECURITY", "");
		//pdfOptions.put("PAPERSIZE", "");
		

		//
		pdfOptions.put("ORIENTATION","LANDSCAPE");//ORIENTATION=<PORTRAIT/LANDSCAPE> 
		pdfOptions.put("PAPERSIZE","LEGAL");//<LETTER/LEGAL/ISOA4> or ("8in","8in") 
//		pdfOptions.put("LEFTMARGIN","");// Sizes such as "1 in" or "2 cm" 
//		pdfOptions.put("RIGHTMARGIN","");
//		pdfOptions.put("TOPMARGIN","");
//		pdfOptions.put("BOTTOMMARGIN","");
//		pdfOptions.put("STYLE","");//<STYLESHEET NAME>
		//ODS LAYOUT <START/END> HEIGHT=<value> WIDTH=<value> 
		//ODS REGION X=<value> Y=<value> HEIGHT=<value> WIDTH=<value>
		//STARTPAGE=<YES/NOW/NO/NEVER> //
		//COMRESS=<value> //from 0 to 9 where 0 means no compression and 9 means maximum compression. The default value is 6
		
		customPdfFormat.setExportFilter(DocumentFamily.TEXT, "writer_pdf_Export");
		customPdfFormat.setExportOption(DocumentFamily.TEXT, "FilterData", pdfOptions);
		customPdfFormat.setExportOption(DocumentFamily.TEXT, "Options", pdfOptions);

		convert(src, dest,customPdfFormat);
		src.delete();
		return dest;
	}


*/