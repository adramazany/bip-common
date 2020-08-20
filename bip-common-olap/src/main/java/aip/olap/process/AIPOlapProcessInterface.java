package aip.olap.process;


public interface AIPOlapProcessInterface {
	void processDimension(String database, String dimension)throws AIPOlapProcessException;
	void processCube(String database, String cube)throws AIPOlapProcessException;
}
