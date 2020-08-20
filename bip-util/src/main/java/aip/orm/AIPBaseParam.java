package aip.orm;

import aip.util.AIPWebUserParam;

public class AIPBaseParam {
	
	AIPWebUserParam webUserParam=null;

	public AIPBaseParam(){}

	public AIPBaseParam(AIPWebUserParam webUserParam){
		this.webUserParam = webUserParam;
	}
	
	public AIPWebUserParam getWebUserParam() {
		return webUserParam;
	}

	public void setWebUserParam(AIPWebUserParam webUserParam) {
		this.webUserParam = webUserParam;
	}

	public String getWebUsername(){
		if(webUserParam==null){
			return null;
		}else{
			return webUserParam.getRemoteUser();
		}
	}
	
}
