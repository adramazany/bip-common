package aip.olap.export;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.olap4j.Cell;
import org.olap4j.CellSet;
import org.olap4j.Position;
import org.olap4j.metadata.Member;

import aip.olap.AIPOlapUtil;
import aip.util.AIPException;
import aip.util.AIPUtil;
import aip.util.NVL;
import aip.util.doc.AIPDocParam;

public class AIPOlapExportJSON extends AIPOlapExportAbstract{

	public void export(CellSet cellSet, OutputStream out,AIPOlapExportParam param) throws AIPOlapExportException {
		/*
[
	[	[],['Measures.مبلغ کل'],['Measures.مبلغ کل']	]
	,[	['فرآورده.رنگ.همه'],0,0		]
	,[	['فرآورده.رنگ.قرمز'],0,0	]
	,[	['فرآورده.رنگ.سبز'],0,0		]
]
		 */
		StringBuffer sb = new StringBuffer();
		sb.append("{cellset:");//json object-start 
		sb.append("[");//row array start
		/*
		 * column header
		 */
		sb.append("[");//column headers array//first row
		sb.append("[]");//first empty cell in table
		for (Position column : cellSet.getAxes().get(0)) {
			sb.append(",[");//each column header array
			for (Member member : column.getMembers()) {
				String value =member.getUniqueName().replace("[", "").replace("]", ""); 
				sb.append("'"+value+"',");
			}
			if(column.getMembers().size()>0)sb.deleteCharAt(sb.length()-1);
			sb.append("]");//each column header array -end
		}
		sb.append("]");//column headers array//first row-end

		/* 
		 * rows
		 */
		if(cellSet.getAxes().size()>1){
			
			for (Position row : cellSet.getAxes().get(1)) {
				
				sb.append(",[");//each row start
				/*
				 * row header
				 */
				sb.append("[");//each row header start
				for (Member member : row.getMembers()) {
					String value =member.getUniqueName().replace("[", "").replace("]", ""); 
					sb.append("'"+value+"',");
				}
				if(row.getMembers().size()>0)sb.deleteCharAt(sb.length()-1);
				sb.append("]");//each row header array -end
				
				/*
				 * data
				 */
				for (Position column : cellSet.getAxes().get(0)) {
					final Cell cell = cellSet.getCell(column, row);
					sb.append(",'"+cell.getFormattedValue()+"'");
					//System.out.println(cell.getFormattedValue());
					//System.out.println();
				}
				sb.append("]");//each row end
			}
		}else{// cellset has only columns
			sb.append(",[[]");// only 1 row start
			/*
			 * data
			 */
			for (Position column : cellSet.getAxes().get(0)) {
				final Cell cell = cellSet.getCell(column);
				sb.append(",'"+cell.getFormattedValue()+"'");
				//System.out.println(cell.getFormattedValue());
				//System.out.println();
			}
			sb.append("]");//each row end
		}
		sb.append("]");//row array end
		sb.append("}");//json object-end
		
		try {
			out.write(sb.toString().getBytes());
		} catch (IOException e) {
			throw new AIPOlapExportException("اشکال در نوشتن اطلاعات جیسون",e);
		}
	}
	
	public static void main(String[] args) {
		System.out.println("AIPOlapExportJSON.main():starting.........");
		try {
			
			String mdx = "select { [Measures].tedadtavalod	} on columns" +
					", { DimMahal.mahalha.[All].children } on rows" +
					" from TavalodFot";
			AIPOlapUtil olapUtil = new  AIPOlapUtil("http://192.168.0.71:80/olap/msmdpump.dll","AIPSabtBICube","bi_admin", "aippia");
			CellSet cellSet = olapUtil.executeMdx("aipsabtbicube",mdx);
	
			AIPOlapExportJSON export = new AIPOlapExportJSON();
			ByteArrayOutputStream baout = new ByteArrayOutputStream();
			export.export(cellSet, baout);
			baout.close();
			System.out.println("AIPOlapExportJSON.main():json is :"+baout.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("AIPOlapExportJSON.main():end");
	}

	public void export(List<List<String>> values, OutputStream out, AIPOlapExportParam param)
			throws AIPOlapExportException {
		// TODO Auto-generated method stub
		
	}
	

}
