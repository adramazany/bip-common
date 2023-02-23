package aip.orm;

import aip.util.AIPWebUserParam;

public class AIPBaseEntity {
	
	AIPWebUserParam webUserParam=null;

	public AIPWebUserParam getWebUserParam() {
		return webUserParam;
	}

	public void setWebUserParam(AIPWebUserParam webUserParam) {
		this.webUserParam = webUserParam;
	}
	
}
