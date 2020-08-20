package aip.util;

public class AIPException extends Exception {
	
	public static final int AIPEX_UNKNOWN=0;
	public static final int AIPEX_FETCH=1;
	public static final int AIPEX_SAVE=2;
	public static final int AIPEX_DELETE=3;

	public static final int AIPEX_SAVE_DUPLICATE=4;
	
	private int type=0;
	
	
	public AIPException() {
	}

	public AIPException(String message) {
		super(message);
	}

	public AIPException(Throwable cause) {
		super(cause);
	}

	public AIPException(String message, Throwable cause) {
		super(message, cause);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
