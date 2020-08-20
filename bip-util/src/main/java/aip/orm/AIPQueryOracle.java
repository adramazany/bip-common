package aip.orm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.engine.spi.NamedSQLQueryDefinition;
import org.hibernate.engine.query.spi.sql.NativeSQLQueryRootReturn;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.Type;

import aip.util.NVL;

public class AIPQueryOracle implements Query {


	Session session;
	String sql;
	List<Class> entitys=new ArrayList<Class>();
	List<String> entitysStr=new ArrayList<String>();
	Query instance;
	boolean iscallable;
	boolean isReturnOk = true;
	
	
	public static AIPQueryOracle getNamedQuery(Session session, String namedQuery) {
		AIPQueryOracle query = new AIPQueryOracle();
		query.session = session;
		Query named_query  = session.getNamedQuery(namedQuery);
		query.instance = named_query;
		StringBuffer sql = new StringBuffer(named_query.getQueryString());
		
		int param_counter=0;
		int pos = sql.indexOf("?");
		while(pos>=0){
			sql.setCharAt(pos, ':');
			sql.insert(pos+1, param_counter);
			pos = sql.indexOf("?", pos+1);
			param_counter++;
		}
		query.sql = sql.toString();

		
		NamedSQLQueryDefinition nsq = (NamedSQLQueryDefinition) HibernateSessionFactory.getConfiguration().getNamedSQLQueries().get(namedQuery);
		query.iscallable = nsq.isCallable();
		
		for (int i=0;i<nsq.getQueryReturns().length; i++) {
			if( nsq.getQueryReturns()[0] instanceof  NativeSQLQueryRootReturn  ){
				NativeSQLQueryRootReturn r = (NativeSQLQueryRootReturn) nsq.getQueryReturns()[0]; 
				query.addEntity( r.getReturnEntityName() );
			}else{
				query.isReturnOk=false;
			}
		}
		
		
		return query;
	}
	

	public Object uniqueResult() throws HibernateException {
		if(iscallable || !isReturnOk){
			return instance.uniqueResult();
		}else{
			return getSQLQuery().uniqueResult();
		}
	}
	public List list() throws HibernateException {
		if(iscallable || !isReturnOk){
			return instance.list();
		}else{
			return getSQLQuery().list();
		}
	}
	private SQLQuery getSQLQuery(){
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		for (Iterator iterator = entitys.iterator(); iterator.hasNext();) {
			Class type = (Class) iterator.next();
			sqlQuery.addEntity(type);
		}
		for (Iterator iterator = entitysStr.iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			sqlQuery.addEntity(type);
		}
		
		return sqlQuery;
	}
	public AIPQueryOracle addEntity(Class clazz){
		entitys.add(clazz);
		return this;
	}
	private AIPQueryOracle addEntity(String returnEntityName) {
		entitysStr.add(returnEntityName);
		return this;
	}


	
	
	
	
	private void changesql(String paramName, Object paramValue) {
		if(paramValue==null){
			paramValue="null";
		}
		sql = sql.replaceAll(":"+paramName, NVL.getString(paramValue));
	}
	private void changesqlquoted(String paramName, Object paramValue) {
		if(paramValue!=null){
			paramValue="'"+paramValue+"'";
		}else{
			paramValue="null";
		}
		sql = sql.replaceAll(":"+paramName, NVL.getString(paramValue));
	}
	public void changesql(int paramName, Object paramValue) {
		if(paramValue==null){
			paramValue="null";
		}
		sql = sql.replaceAll(":"+paramName, NVL.getString(paramValue));
	}
	public void changesqlquted(int paramName, Object paramValue) {
		if(paramValue!=null){
			paramValue="'"+paramValue+"'";
		}else{
			paramValue="null";
		}
		sql = sql.replaceAll(":"+paramName, NVL.getString(paramValue));
	}

	
	
	
	
	
	
	
	
	
	public Query setBigDecimal(int arg0, BigDecimal arg1) {
		changesql(arg0,arg1);
		instance.setBigDecimal(arg0,arg1);
		return this;
	}
	public Query setBigDecimal(String arg0, BigDecimal arg1) {
		changesql(arg0,arg1);
		instance.setBigDecimal(arg0,arg1);
		return this;
	}
	public Query setBigInteger(int arg0, BigInteger arg1) {
		changesql(arg0,arg1);
		instance.setBigInteger(arg0,arg1);
		return this;
	}
	public Query setBigInteger(String arg0, BigInteger arg1) {
		changesql(arg0,arg1);
		instance.setBigInteger(arg0,arg1);
		return this;
	}
	public Query setBinary(int arg0, byte[] arg1) {
		changesql(arg0,arg1);
		instance.setBinary(arg0,arg1);
		return this;
	}
	public Query setBinary(String arg0, byte[] arg1) {
		changesql(arg0,arg1);
		instance.setBinary(arg0,arg1);
		return this;
	}
	public Query setBoolean(int arg0, boolean arg1) {
		changesql(arg0,arg1);
		instance.setBoolean(arg0,arg1);
		return this;
	}
	public Query setBoolean(String arg0, boolean arg1) {
		changesql(arg0,arg1);
		instance.setBoolean(arg0,arg1);
		return this;
	}
	public Query setByte(int arg0, byte arg1) {
		changesql(arg0,arg1);
		instance.setByte(arg0,arg1);
		return this;
	}
	public Query setByte(String arg0, byte arg1) {
		changesql(arg0,arg1);
		instance.setByte(arg0,arg1);
		return this;
	}
	public Query setCharacter(int arg0, char arg1) {
		changesqlquted(arg0,arg1);
		instance.setCharacter(arg0,arg1);
		return this;
	}
	public Query setCharacter(String arg0, char arg1) {
		changesqlquoted(arg0,arg1);
		instance.setCharacter(arg0,arg1);
		return this;
	}
	public Query setDate(int arg0, Date arg1) {
		changesqlquted(arg0,arg1);
		instance.setDate(arg0,arg1);
		return this;
	}
	public Query setDate(String arg0, Date arg1) {
		changesql(arg0,arg1);
		instance.setDate(arg0,arg1);
		return this;
	}
	public Query setDouble(int arg0, double arg1) {
		changesql(arg0,arg1);
		instance.setDouble(arg0,arg1);
		return this;
	}
	public Query setDouble(String arg0, double arg1) {
		changesql(arg0,arg1);
		instance.setDouble(arg0,arg1);
		return this;
	}
	public Query setEntity(int arg0, Object arg1) {
		changesql(arg0,arg1);
		instance.setEntity(arg0,arg1);
		return this;
	}
	public Query setEntity(String arg0, Object arg1) {
		changesql(arg0,arg1);
		instance.setEntity(arg0,arg1);
		return this;
	}
	public Query setFetchSize(int arg0) {
		instance.setFetchSize(arg0);
		return this;
	}
	public Query setFirstResult(int arg0) {
		instance.setFirstResult(arg0);
		return this;
	}
	public Query setFloat(int arg0, float arg1) {
		changesql(arg0,arg1);
		instance.setFloat(arg0,arg1);
		return this;
	}
	public Query setFloat(String arg0, float arg1) {
		changesql(arg0,arg1);
		instance.setFloat(arg0,arg1);
		return this;
	}
	public Query setFlushMode(FlushMode arg0) {
		instance.setFlushMode(arg0);
		return this;
	}
	public Query setInteger(int arg0, int arg1) {
		changesql(arg0,arg1);
		instance.setInteger(arg0,arg1);
		return this;
	}
	public Query setInteger(String arg0, int arg1) {
		changesql(arg0,arg1);
		instance.setInteger(arg0,arg1);
		return this;
	}
	public Query setLocale(int arg0, Locale arg1) {
		changesql(arg0,arg1);
		instance.setLocale(arg0,arg1);
		return this;
	}
	public Query setLocale(String arg0, Locale arg1) {
		changesql(arg0,arg1);
		instance.setLocale(arg0,arg1);
		return this;
	}
	public Query setLockMode(String arg0, LockMode arg1) {
		changesql(arg0,arg1);
		instance.setLockMode(arg0,arg1);
		return this;
	}
	public Query setLong(int arg0, long arg1) {
		changesql(arg0,arg1);
		instance.setLong(arg0,arg1);
		return this;
	}
	public Query setLong(String arg0, long arg1) {
		changesql(arg0,arg1);
		instance.setLong(arg0,arg1);
		return this;
	}
	public Query setMaxResults(int arg0) {
		instance.setMaxResults(arg0);
		return this;
	}
	public Query setParameter(int arg0, Object arg1) throws HibernateException {
		changesqlquted(arg0,arg1);
		instance.setParameter(arg0,arg1);
		return this;
	}
	public Query setParameter(String arg0, Object arg1)
			throws HibernateException {
		changesql(arg0,arg1);
		instance.setParameter(arg0,arg1);
		return this;
	}
	public Query setParameter(int arg0, Object arg1, Type arg2) {
		changesql(arg0,arg1);
		instance.setParameter(arg0,arg1,arg2);
		return this;
	}
	public Query setParameter(String arg0, Object arg1, Type arg2) {
		changesql(arg0,arg1);
		instance.setParameter(arg0,arg1,arg2);
		return this;
	}
	public Query setParameterList(String arg0, Collection arg1)
			throws HibernateException {
		changesql(arg0,arg1);
		instance.setParameterList(arg0,arg1);
		return this;
	}
	public Query setParameterList(String arg0, Object[] arg1)
			throws HibernateException {
		changesql(arg0,arg1);
		instance.setParameterList(arg0,arg1);
		return this;
	}
	public Query setParameterList(String arg0, Collection arg1, Type arg2)
			throws HibernateException {
		changesql(arg0,arg1);
		instance.setParameterList(arg0,arg1,arg2);
		return this;
	}
	public Query setParameterList(String arg0, Object[] arg1, Type arg2)
			throws HibernateException {
		changesql(arg0,arg1);
		instance.setParameterList(arg0,arg1,arg2);
		return this;
	}
	public Query setParameters(Object[] arg0, Type[] arg1)
			throws HibernateException {
		instance.setParameters(arg0,arg1);
		return this;
	}
	public Query setProperties(Object arg0) throws HibernateException {
		instance.setProperties(arg0);
		return this;
	}
	public Query setProperties(Map arg0) throws HibernateException {
		instance.setProperties(arg0);
		return this;
	}
	public Query setReadOnly(boolean arg0) {
		instance.setReadOnly(arg0);
		return this;
	}
	public Query setResultTransformer(ResultTransformer arg0) {
		instance.setResultTransformer(arg0);
		return this;
	}
	public Query setSerializable(int arg0, Serializable arg1) {
		changesql(arg0,arg1);		
		instance.setSerializable(arg0,arg1);
		return this;
	}
	public Query setSerializable(String arg0, Serializable arg1) {
		changesql(arg0,arg1);
		instance.setSerializable(arg0,arg1);
		return this;
	}
	public Query setShort(int arg0, short arg1) {
		changesql(arg0,arg1);
		instance.setShort(arg0,arg1);
		return this;
	}
	public Query setShort(String arg0, short arg1) {
		changesql(arg0,arg1);
		instance.setShort(arg0,arg1);
		return this;
	}
	public Query setString(int arg0, String arg1) {
		changesqlquted(arg0,arg1);
		instance.setString(arg0,arg1);
		return this;
	}
	public Query setString(String arg0, String arg1) {
		changesqlquoted(arg0,arg1);
		instance.setString(arg0,arg1);
		return this;
	}
	public Query setStringNoQuote(int arg0, String arg1) {
		changesql(arg0,arg1);
		instance.setString(arg0,arg1);
		return this;
	}
	public Query setStringNoQuote(String arg0, String arg1) {
		changesql(arg0,arg1);
		instance.setString(arg0,arg1);
		return this;
	}

	public Query setText(int arg0, String arg1) {
		changesqlquted(arg0,arg1);
		instance.setText(arg0,arg1);
		return this;
	}
	public Query setText(String arg0, String arg1) {
		changesqlquoted(arg0,arg1);
		instance.setText(arg0,arg1);
		return this;
	}
	public Query setTime(int arg0, Date arg1) {
		changesqlquted(arg0,arg1);
		instance.setTime(arg0,arg1);
		return this;
	}
	public Query setTime(String arg0, Date arg1) {
		changesqlquoted(arg0,arg1);
		instance.setTime(arg0,arg1);
		return this;
	}
	public Query setTimeout(int arg0) {
		instance.setTimeout(arg0);
		return this;
	}
	public Query setTimestamp(int arg0, Date arg1) {
		changesqlquted(arg0,arg1);
		instance.setTimestamp(arg0,arg1);
		return this;
	}
	public Query setTimestamp(String arg0, Date arg1) {
		changesqlquoted(arg0,arg1);
		instance.setTimestamp(arg0,arg1);
		return this;
	}


	
	
	
	
	
	public Iterator iterate() throws HibernateException {
		return instance.iterate();
	}
	public ScrollableResults scroll() throws HibernateException {
		
		return instance.scroll();
	}
	public int executeUpdate() throws HibernateException {
		if(iscallable || !isReturnOk){
			return instance.executeUpdate();
		}else{
			return getSQLQuery().executeUpdate();
		}
		//return instance.executeUpdate();
	}
	public String[] getNamedParameters() throws HibernateException {
		return instance.getNamedParameters();
	}
	public String getQueryString() {
		return instance.getQueryString();
	}
	public String[] getReturnAliases() throws HibernateException {
		
		return instance.getReturnAliases();
	}
	public Type[] getReturnTypes() throws HibernateException {
		return instance.getReturnTypes();
	}
	public ScrollableResults scroll(ScrollMode arg0) throws HibernateException {
		return instance.scroll(arg0);
	}
	public Query setCacheMode(CacheMode arg0) {
		instance.setCacheMode(arg0);
		return this;
	}
	public Query setCacheRegion(String arg0) {
		instance.setCacheRegion(arg0);
		return this;
	}
	public Query setCacheable(boolean arg0) {
		
		instance.setCacheable(arg0);
		return this;
	}
	public Query setCalendar(int arg0, Calendar arg1) {
		
		instance.setCalendar(arg0,arg1);
		return this;
	}
	public Query setCalendar(String arg0, Calendar arg1) {
		
		instance.setCalendar(arg0,arg1);
		return this;
	}
	public Query setCalendarDate(int arg0, Calendar arg1) {
		
		instance.setCalendarDate(arg0,arg1);
		return this;
	}
	public Query setCalendarDate(String arg0, Calendar arg1) {
		
		instance.setCalendarDate(arg0,arg1);
		return this;
	}
	public Query setComment(String arg0) {
		
		instance.setComment(arg0);
		return this;
	}

	public boolean isReadOnly() {
		return instance.isReadOnly();
	}

	public Query setLockOptions(LockOptions arg0) {
		return instance.setLockOptions(arg0);
	}

	public LockOptions getLockOptions() {
		return instance.getLockOptions();
	}
	
	
	
	
}
