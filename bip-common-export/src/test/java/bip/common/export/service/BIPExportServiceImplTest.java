package bip.common.export.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileOutputStream;

import static org.junit.Assert.*;

/**
 * Created by ramezani on 2/7/2019.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/applicationContext.xml"})
public class BIPExportServiceImplTest {

    @Autowired
    BIPExportServiceImpl bipExportService;

    @Test
    public void exportExcel() throws Exception {
        String query="select ROWNUM,bt.TYPE_TITLE,bc.TITLE,a.NOTIFICATION_NUMBER,a.APPROVAL_DATE , a.TITLE, bh.OFFICE_NAME from approvals a \n" +
                "join bt_type bt on bt.id = a.F_TYPE_CIRCULARS\n" +
                "join BT_CONDITION bc on bc.id = a.F_CONDITION\n" +
                "join BT_HEADQUARTERS_OFFICE bh on bh.id = a.F_HEADQUARTERS\n" +
                "where bt.id = 3 and bc.id = 1 and a.NOTIFICATION_NUMBER like '%00020'\n" +
                "and a.APPROVAL_DATE like '%%' and a.TITLE like '%%'\n" +
                "and bh.INDICATION_CODE = 113 and a.F_STATUS = 1\n" +
                "and a.LAST_VERSION = 1 order by a.APPROVAL_DATE desc";
        String title="لیست اجمالی";
        String filter="فیلتر : نوع دستورالعمل:اطلاعیه,وضعیت:معتبر,شماره ثبت:20,ابلاغ کننده:اداره کل تشکیلات و روشها (113)";
        String columnHeaders="ردیف,نوع,وضعیت,شماره ثبت,تاریخ تصویب,عنوان,ابلاغ کننده";

        File outFile = new File("test.xlsx");
        bipExportService.exportExcel(new FileOutputStream(outFile),query,title,filter,columnHeaders.split(","));
        System.out.println("outFile = " + outFile.getAbsolutePath());

        Runtime.getRuntime().exec("cmd /c "+outFile.getAbsolutePath());
    }

    @Test
    public void exportExcelPagging() throws Exception {

        String query1="select row_number() over (order by a.APPROVAL_DATE desc) as radif,bt.TYPE_TITLE,bc.TITLE condition,a.NOTIFICATION_NUMBER,a.APPROVAL_DATE , a.TITLE, bh.OFFICE_NAME from approvals a \n" +
                "join bt_type bt on bt.id = a.F_TYPE_CIRCULARS\n" +
                "join BT_CONDITION bc on bc.id = a.F_CONDITION\n" +
                "join BT_HEADQUARTERS_OFFICE bh on bh.id = a.F_HEADQUARTERS\n" ;
        String query2="select bt.TYPE_TITLE,bc.TITLE condition,a.NOTIFICATION_NUMBER,a.APPROVAL_DATE , a.TITLE, bh.OFFICE_NAME from approvals a \n" +
                "join bt_type bt on bt.id = a.F_TYPE_CIRCULARS\n" +
                "join BT_CONDITION bc on bc.id = a.F_CONDITION\n" +
                "join BT_HEADQUARTERS_OFFICE bh on bh.id = a.F_HEADQUARTERS\n" +
                "order by a.APPROVAL_DATE desc";
        String query3="select bt.TYPE_TITLE,bc.TITLE condition,a.NOTIFICATION_NUMBER,a.APPROVAL_DATE , a.TITLE, bh.OFFICE_NAME from approvals a \n" +
                "join bt_type bt on bt.id = a.F_TYPE_CIRCULARS\n" +
                "join BT_CONDITION bc on bc.id = a.F_CONDITION\n" +
                "join BT_HEADQUARTERS_OFFICE bh on bh.id = a.F_HEADQUARTERS\n";
        String title="لیست اجمالی";
        String filter="فیلتر : نوع دستورالعمل:اطلاعیه,وضعیت:معتبر,شماره ثبت:20,ابلاغ کننده:اداره کل تشکیلات و روشها (113)";
        String columnHeaders="ردیف,نوع,وضعیت,شماره ثبت,تاریخ تصویب,عنوان,ابلاغ کننده";
        String columnHeadersWithoutRadif="نوع,وضعیت,شماره ثبت,تاریخ تصویب,عنوان,ابلاغ کننده";

        File outFile = new File("test1.xlsx");
        bipExportService.exportExcel(new FileOutputStream(outFile),query1,title,filter,columnHeaders.split(",")
                ,3,10,"radif");
        System.out.println("outFile = " + outFile.getAbsolutePath());
        Runtime.getRuntime().exec("cmd /c "+outFile.getAbsolutePath());

        outFile = new File("test2.xlsx");
        bipExportService.exportExcel(new FileOutputStream(outFile),query2,title,filter,columnHeadersWithoutRadif.split(",")
                ,3,10,null);
        System.out.println("outFile = " + outFile.getAbsolutePath());
        Runtime.getRuntime().exec("cmd /c "+outFile.getAbsolutePath());

        outFile = new File("test3.xlsx");
        bipExportService.exportExcel(new FileOutputStream(outFile),query3,title,filter,columnHeadersWithoutRadif.split(",")
                ,3,10,null);
        System.out.println("outFile = " + outFile.getAbsolutePath());
        Runtime.getRuntime().exec("cmd /c "+outFile.getAbsolutePath());

    }

}