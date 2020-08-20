package aip.util.log;


public interface NIOLogInterface {
	void log(NIOLogENT logENT);
	String extractChangedCriticalData(Object newObj, Object oldObj, String[] criticalFields);
	NIOLogLST getLogLST(NIOLogParam param); 
}
