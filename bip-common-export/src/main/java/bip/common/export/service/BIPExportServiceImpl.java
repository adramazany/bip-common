package bip.common.export.service;

import bip.common.export.exception.ExportServiceException;
import bip.common.export.factory.ExcelCellFactory;
import bip.common.util.BIPJalaliDate;
import bip.common.util.NVL;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by ramezani on 2/5/2019.
 */
@Service
public class BIPExportServiceImpl implements BIPExportService{
    Logger logger = LoggerFactory.getLogger(BIPExportServiceImpl.class);

    @Autowired
    NamedParameterJdbcOperations jdbcTemplate;

    public void exportExcel(OutputStream out, String query, String title, String filter, String[] columnHeaders)throws ExportServiceException{
        logger.info(String.format("exportExcel(query:%s, title:%s, filter:%s, columnHeaders:%s)",query, title, filter, Arrays.toString( columnHeaders)));

        try {
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(query,new HashMap<String, Object>());
            logger.debug("query execute done.");

            if(columnHeaders==null || columnHeaders.length==0){
                columnHeaders = sqlRowSet.getMetaData().getColumnNames();
            }

            // Create a Workbook
            Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

            /* CreationHelper helps us create instances of various things like DataFormat,
               Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
            CreationHelper createHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.createSheet("bipsolution");
            sheet.setRightToLeft(true);


            int rowNum = 0;

            //title
            Row titleRow = sheet.createRow(rowNum);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(title);
            titleCell.setCellStyle(
                    ExcelCellFactory.createCellStyle(workbook)
                    .setFontBold(true)
                    .setFontHeight(20)
                    .setFontColor(IndexedColors.DARK_BLUE.getIndex())
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .buildCellStyle()
                    );
            sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum,0,columnHeaders.length-1));

            rowNum++;


            // filter
            rowNum++;
            Row filterRow = sheet.createRow(rowNum);
            CellStyle filterCellStyle = ExcelCellFactory.createCellStyle(workbook)
                    .setFontBold(false)
                    .setFontHeight(11)
                    .setFontColor(IndexedColors.DARK_TEAL.getIndex())
                    .buildCellStyle();
            Cell cellFilter = filterRow.createCell(0);
            cellFilter.setCellValue(filter);
            cellFilter.setCellStyle(filterCellStyle);
            if(columnHeaders.length>2){
                sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum,0,columnHeaders.length-2));
            }
            rowNum++;


            Cell cellReportTimestamp = filterRow.createCell(columnHeaders.length-1);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            cellReportTimestamp.setCellValue(String.format("تاریخ : %s %s",new BIPJalaliDate().getFullDate(),sdf.format(new Date())));
            cellReportTimestamp.setCellStyle(filterCellStyle);


            // columns header
            rowNum++;
            Row columnsHeaderRow = sheet.createRow(rowNum++);
            CellStyle columnsHeaderCellStyle = ExcelCellFactory.createCellStyle(workbook)
                    .setFontBold(true)
                    .setFontHeight(14)
                    .setFontColor(IndexedColors.WHITE.getIndex())/*RGB: 0 0 128 HSL: 240° 100% 25% Hex: #000080*/
                    .setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex())
                    .buildCellStyle();

            for(int i = 0; i < columnHeaders.length; i++) {
                Cell cell = columnsHeaderRow.createCell(i);
                cell.setCellValue(columnHeaders[i]);
                cell.setCellStyle(columnsHeaderCellStyle);
            }

            // data
            int columnCount = sqlRowSet.getMetaData().getColumnCount();

//            XSSFColor c = new XSSFColor();
//            c.setARGBHex("DBE5F1");

            CellStyle dataCellStyleOdd = ExcelCellFactory.createCellStyle(workbook)
                    .setFontBold(false)
                    .setFontHeight(12)
                    .setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex())
                    .buildCellStyle();
            CellStyle dataCellStyleEven = ExcelCellFactory.createCellStyle(workbook)
                    .setFontBold(false)
                    .setFontHeight(12)
                    .buildCellStyle();

            while(sqlRowSet.next()){
                Row row = sheet.createRow(rowNum++);

                for (int i = 0; i < columnCount; i++) {
                    Cell dataCell = row.createCell(i);
                    dataCell.setCellValue(sqlRowSet.getString(i+1));
                    if(rowNum%2==0){
                        dataCell.setCellStyle(dataCellStyleEven);
                    }else{
                        dataCell.setCellStyle(dataCellStyleOdd);
                    }
                }

            }

            // Resize all columns to fit the content size
            for(int i = 0; i < columnCount; i++) {
                sheet.autoSizeColumn(i);
            }
            logger.debug("create workbook done.");

            // Write the output to a file
            //FileOutputStream fileOut = new FileOutputStream("poi-generated-file.xlsx");
            workbook.write(out);
            logger.debug("output write done.");
            workbook.close();
        } catch (IOException e) {
            String error = String.format("error in exportExcel : %s",e);
            logger.error(error);
            throw new ExportServiceException(error,e);
        }
        logger.debug("exportExcel succeed.");
    }

    public void exportExcel(OutputStream out, String query, String title, String filter, String[] columnHeaders
            , int pageNo, int pageSize, String rowNumberField)throws ExportServiceException{
        String queryLower = query.toLowerCase();
        if(NVL.isEmpty(rowNumberField)){
            int orderByPos = queryLower.indexOf("order by ");
            String orderBy;
            if(orderByPos>-1){
                orderBy = query.substring(orderByPos);
                query = query.substring(0,orderByPos);
            }else{
                int selectEndPos = queryLower.indexOf("select ");
                int firstFieldEndPos = queryLower.indexOf(",");
                if(firstFieldEndPos==-1)firstFieldEndPos = queryLower.indexOf(" from ");
                orderBy = "order by "+query.substring(selectEndPos+7,firstFieldEndPos);
            }
            query = query.replace("select ","select row_number() over ("+orderBy+") as radif,");
            rowNumberField = "radif";
            String[] newColumnHeaders = new String[columnHeaders.length+1];
            newColumnHeaders[0]="ردیف";
            System.arraycopy(columnHeaders,0,newColumnHeaders,1,columnHeaders.length);
            columnHeaders = newColumnHeaders;

        }
        int firstRow = (pageNo-1)*pageSize+1;
        int lastRow = firstRow+pageSize-1;
        query = String.format("select t.* from (%s)t where %s between %d and %d",query,rowNumberField,firstRow,lastRow);
        logger.debug("exportExcel pagging query is "+query);

        exportExcel(out,query,title,filter,columnHeaders);
    }

}
