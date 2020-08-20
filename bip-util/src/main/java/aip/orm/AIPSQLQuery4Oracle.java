package aip.orm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.Type;

public class AIPSQLQuery4Oracle {

	Session session;
	String sql;
//	String resultSetMapping;
	List<Class> entitys=new ArrayList<Class>();
	
	
	public static AIPSQLQuery4Oracle getNamedQuery(Session session, String namedQuery) {
		AIPSQLQuery4Oracle query = new AIPSQLQuery4Oracle();
		
		query.session = session;
		Query named_query  = session.getNamedQuery(namedQuery);
		query.sql = named_query.getQueryString();
		
		return query;
	}

	public AIPSQLQuery4Oracle setString(String paramName, String paramValue) {
		if(paramValue!=null){
			paramValue="'"+paramValue+"'";
		}else{
			paramValue="null";
		}
		sql = sql.replaceAll(":"+paramName, paramValue);
		return this;
	}

	public AIPSQLQuery4Oracle setDouble(String paramName, double paramValue) {
		sql = sql.replaceAll(":"+paramName, ""+paramValue);
		return this;
	}

	public AIPSQLQuery4Oracle setLong(String paramName, long paramValue) {
		sql = sql.replaceAll(":"+paramName, ""+paramValue);
		return this;
	}

	public AIPSQLQuery4Oracle setInteger(String paramName, int paramValue) {
		sql = sql.replaceAll(":"+paramName, ""+paramValue);
		return this;
	}

	public AIPSQLQuery4Oracle setBoolean(String paramName, boolean paramValue) {
		sql = sql.replaceAll(":"+paramName, ""+paramValue);
		return this;
	}

	private SQLQuery getSQLQuery(){
		SQLQuery sqlQuery = session.createSQLQuery(sql);
//		if(resultSetMapping!=null){
//			sqlQuery.setResultSetMapping(resultSetMapping);
//		}
		for (Iterator iterator = entitys.iterator(); iterator.hasNext();) {
			Class type = (Class) iterator.next();
			sqlQuery.addEntity(type);
		}
		return sqlQuery;
	}
	
	public List list() {
		return getSQLQuery().list();
	}

	public Object uniqueResult() {
		return getSQLQuery().uniqueResult();
	}

//	public void setResultSetMapping(String resultSetMapping) {
//		this.resultSetMapping = resultSetMapping;
//	}
	
	public AIPSQLQuery4Oracle addEntity(Class clazz){
		entitys.add(clazz);
		return this;
	}
	
}
