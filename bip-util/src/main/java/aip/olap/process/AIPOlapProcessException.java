package aip.olap.process;

import aip.util.AIPException;

public class AIPOlapProcessException extends AIPException {
	private static final long serialVersionUID = 7358068813642336544L;

	public AIPOlapProcessException() {
	}

	public AIPOlapProcessException(String message) {
		super(message);
	}

	public AIPOlapProcessException(Throwable cause) {
		super(cause);
	}

	public AIPOlapProcessException(String message, Throwable cause) {
		super(message, cause);
	}

}
