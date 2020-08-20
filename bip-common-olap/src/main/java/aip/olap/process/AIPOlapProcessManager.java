package aip.olap.process;


public class AIPOlapProcessManager {
	public static AIPOlapProcessInterface getConnection(String url,String username,String password) throws AIPOlapProcessException{
		try {
			String driverName = System.getProperty("aip.olap.process.driver", "aip.olap.process.AIPOlapProcessSSAS");
			AIPOlapProcessAbstract driver = (AIPOlapProcessAbstract) Class.forName(driverName).newInstance();
			driver.setUrl(url);
			driver.setUsername(username);
			driver.setPassword(password);
			return driver;
		} catch (Exception e) {
			throw new AIPOlapProcessException(e);
		}
	}

}
