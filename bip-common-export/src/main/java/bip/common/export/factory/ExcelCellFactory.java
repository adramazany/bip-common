package bip.common.export.factory;

import org.apache.poi.ss.usermodel.*;

/**
 * Created by ramezani on 2/9/2019.
 */
public class ExcelCellFactory {

    Workbook workbook;
    CellStyle cellStyle;
    Font cellStyleFont;

    public static ExcelCellFactory createCellStyle(Workbook workbook){
        ExcelCellFactory excelCellFactory = new ExcelCellFactory();
        excelCellFactory.workbook = workbook;

        excelCellFactory.cellStyle = workbook.createCellStyle();
        excelCellFactory.cellStyle.setAlignment(HorizontalAlignment.RIGHT);

        excelCellFactory.cellStyleFont = workbook.createFont();
        excelCellFactory.cellStyle.setFont(excelCellFactory.cellStyleFont);

        return excelCellFactory;
    }
    public CellStyle buildCellStyle(){
        return cellStyle;
    }

    public ExcelCellFactory setFont(Font font) {
        cellStyle.setFont(font);
        return this;
    }

    public ExcelCellFactory setFillForegroundColor(short backgroundColor) {
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor((short)backgroundColor);
        return this;
    }

    public ExcelCellFactory setDataFormat(short dataFormat) {
        cellStyle.setDataFormat((short)dataFormat);
        return this;
    }

    public ExcelCellFactory setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        cellStyle.setAlignment(horizontalAlignment);
        return this;
    }

    public ExcelCellFactory setFontBold(boolean fontBold) {
        cellStyleFont.setBold( fontBold);
        return this;
    }

    public ExcelCellFactory setFontHeight(int fontHeight) {
        cellStyleFont.setFontHeightInPoints((short) fontHeight);
        return this;
    }

    public ExcelCellFactory setFontColor(int fontColor) {
        cellStyleFont.setColor((short) fontColor);
        return this;
    }
}
