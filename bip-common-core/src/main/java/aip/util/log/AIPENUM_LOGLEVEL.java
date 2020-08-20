package aip.util.log;

public enum AIPENUM_LOGLEVEL {

	INFO(1),
	WARNING(2),
	ERROR(3);

	private final int val;

	AIPENUM_LOGLEVEL(int val){
		this.val = val;
	}
	public String val() {
		String strVal = "";
		switch (val) {
		case 1:
			strVal = "INFO"; 
			break;
		case 2:
			strVal = "WARNING"; 
			break;
		case 3:
			strVal = "ERROR"; 
			break;
		default:
			break;
		}
		return strVal;
	}
}
