package aip.olap.process;

import aip.util.AIPUtil;


public class AIPOlapProcessSSAS extends AIPOlapProcessAbstract {

	public AIPOlapProcessSSAS() {
		try {System.setProperty("aip.olap.process.driver", "aip.olap.process.AIPOlapProcessSSAS");} catch (Exception e) {}
	}

	public void processDimension(String database, String dimension) throws AIPOlapProcessException {
		String url=getUrl()+"?database="+database+"&dimension="+dimension;
		String res;
		String errorMessage = "پردازش بانک اطلاعاتی "+database+" و دایمنشن "+dimension+" با خطا مواجه شده است!";
		try {
			res = AIPUtil.httpGet(url);
			if(!res.equals(".")){
				throw new AIPOlapProcessException(errorMessage);
			}
		} catch (Exception e) {
			throw new AIPOlapProcessException(errorMessage,e);
		}
	}

	public void processCube(String database, String cube) throws AIPOlapProcessException {
		String url=getUrl()+"?database="+database+"&cube="+cube;
		String res;
		String errorMessage = "پردازش بانک اطلاعاتی "+database+" و کیوب "+cube+" با خطا مواجه شده است!";
		try {
			res = AIPUtil.httpGet(url);
			if(!res.equals(".")){
				throw new AIPOlapProcessException(errorMessage);
			}
		} catch (Exception e) {
			throw new AIPOlapProcessException(errorMessage,e);
		}
	}

}
