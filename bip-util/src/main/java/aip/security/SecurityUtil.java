package aip.security;

import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;

import aip.orm.HibernateSessionFactory;
import aip.util.AIPException;
import aip.util.AIPUtil;
import aip.util.NVL;


public class SecurityUtil {
	
	static Hashtable htUserRoles = new Hashtable();

	public static String userRoleClass = "UserRoleENT";
	public static String usernameField="username";
	public static String rolenameFiled="rolename";
	
	public static boolean isUserInRoles(HttpServletRequest request,String a_rolenames){
		String roles[] = a_rolenames.split(",");
		boolean isInRole = false;
		for(int i=0;i<roles.length;i++){
			if(request.isUserInRole(roles[i])){
				isInRole =true;
				break;
			}
		}
		return isInRole;
	}

	public static void clearUserRoleCache(String a_username) {
		if(htUserRoles.containsKey(a_username)){
			htUserRoles.remove(a_username);
		}
	}
	public static String getUserRoles(String a_username) {
		String strRoles="admin";
		/*Query q = HibernateSessionFactory.getSession().createQuery( "from UserRoleDBENT where username=?" );
		q.setString(0, a_username);
		List<UserRoleDBENT> l = (List<UserRoleDBENT>) q.list();
		String strRoles = "";
		if (l != null) {
			for (int i = 0; i < l.size(); i++) {
				if (i > 0) {
					strRoles += ",";
				}
				strRoles += l.get(i).getRolename();
			}
		}*/
		return strRoles;
	}
	public static boolean isAdmin(String username)throws AIPException {
		return isUserInRoles(username,"admin");
	}
	public static boolean isUserInRoles(String username,String rolenames) {
		try {
			return isUserInRoles(username, rolenames, userRoleClass, usernameField, rolenameFiled);
		} catch (AIPException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean isUserInRoles(String a_username,String a_rolenames,String userRoleClass,String usernameField,String rolenameFiled) throws AIPException {
		a_rolenames=NVL.getString(a_rolenames).toUpperCase();
		a_username=NVL.getString(a_username," ");
		//System.out.print("SecurityUtil.isUserInRoleByUN():user="+a_username+",role="+a_rolenames);
		
		if(!htUserRoles.containsKey(a_username)){
			Query q = HibernateSessionFactory.getSession().createQuery( "from "+userRoleClass+" where "+usernameField+"=?" );
			q.setString(0, a_username);
			List l = q.list();
			StringBuffer sb = new StringBuffer();
			if (l != null) {
				sb.append(",");
				for (int i = 0; i < l.size(); i++) {
					String rolename="";
					try {
						rolename = (String) AIPUtil.invokeGetter(l.get(i),rolenameFiled);
					} catch (Exception e) {
						e.printStackTrace();
						throw new AIPException(e);
						
					}
					sb.append(rolename.toUpperCase());
					sb.append(",");
				}
			}
			//System.out.print("userroles="+sb.toString());
			htUserRoles.put(a_username, sb.toString());
		}
		String roles = NVL.getString( htUserRoles.get(a_username) );
		//System.out.print("userroles2="+roles);
		String[] arrRoles = a_rolenames.split(",");
		for (int j = 0; j < arrRoles.length; j++) {
			if (roles.indexOf(","+arrRoles[j]+",")>=0){
				//System.out.println(":true");
				return true;
			}
		}
		//System.out.println(":false");
		return false;
	}
}
