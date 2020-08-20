package aip.util;

public class AIPClonable implements Cloneable {

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	} 

}
