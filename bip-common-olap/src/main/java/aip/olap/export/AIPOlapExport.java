package aip.olap.export;

import org.olap4j.CellSet;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface AIPOlapExport {
	void export(CellSet cellSet, OutputStream out)throws AIPOlapExportException;
	void export(CellSet cellSet, OutputStream out, AIPOlapExportParam param)throws AIPOlapExportException;
	InputStream export(CellSet cellSet)throws AIPOlapExportException;
	InputStream export(CellSet cellSet, AIPOlapExportParam param)throws AIPOlapExportException;

	void export(List<List<String>> values, OutputStream out)throws AIPOlapExportException;
	void export(List<List<String>> values, OutputStream out, AIPOlapExportParam param)throws AIPOlapExportException;
	InputStream export(List<List<String>> values)throws AIPOlapExportException;
	InputStream export(List<List<String>> values, AIPOlapExportParam param)throws AIPOlapExportException;
}
