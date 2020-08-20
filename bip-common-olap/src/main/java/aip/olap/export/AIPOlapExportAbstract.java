package aip.olap.export;

import org.olap4j.CellSet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public abstract class AIPOlapExportAbstract implements AIPOlapExport {

	public void export(CellSet cellSet, OutputStream out) throws AIPOlapExportException {
		export(cellSet, out, new AIPOlapExportParam());
	}

	public InputStream export(CellSet cellSet)throws AIPOlapExportException{
		return export(cellSet, new AIPOlapExportParam());
	}

	public InputStream export(CellSet cellSet, AIPOlapExportParam param) throws AIPOlapExportException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		export(cellSet, baos,param);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		return bais;
	}

	public void export(List<List<String>> values, OutputStream out) throws AIPOlapExportException {
		export(values, out, new AIPOlapExportParam());
	}

	public InputStream export(List<List<String>> values)throws AIPOlapExportException{
		return export(values, new AIPOlapExportParam());
	}

	public InputStream export(List<List<String>> values, AIPOlapExportParam param) throws AIPOlapExportException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		export(values, baos,param);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		return bais;
	}

}
