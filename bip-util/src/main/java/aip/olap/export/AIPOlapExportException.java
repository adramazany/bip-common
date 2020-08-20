package aip.olap.export;

import aip.util.AIPException;

public class AIPOlapExportException extends AIPException {
	
	private static final long serialVersionUID = 6464182267256316115L;

	public AIPOlapExportException() {
	}

	public AIPOlapExportException(String message) {
		super(message);
	}

	public AIPOlapExportException(Throwable cause) {
		super(cause);
	}

	public AIPOlapExportException(String message, Throwable cause) {
		super(message, cause);
	}

}
