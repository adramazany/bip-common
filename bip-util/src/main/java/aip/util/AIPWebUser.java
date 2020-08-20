package aip.util;

import javax.servlet.http.HttpServletRequest;
import aip.util.NVL;

public class AIPWebUser extends AIPWebUserParam{

	public AIPWebUser(HttpServletRequest request) {
		super(request);
	}

	 public AIPWebUser(String userName) {
		 super(userName);
	 }
}
