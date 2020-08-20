package bip.common.export.service;

import bip.common.export.exception.ExportServiceException;

import java.io.OutputStream;

/**
 * Created by ramezani on 2/5/2019.
 */
public interface BIPExportService {
    void exportExcel(OutputStream out,String query,String title,String filter,String[] columnHeaders)throws ExportServiceException;
    void exportExcel(OutputStream out,String query,String title,String filter,String[] columnHeaders,int pageNo,int pageSize, String rowNumberField)throws ExportServiceException;
}
