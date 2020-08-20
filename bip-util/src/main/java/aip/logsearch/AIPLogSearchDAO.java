package aip.logsearch;

import aip.db.AIPDBUtil;
import aip.util.AIPException;
import aip.util.AIPUtil;
import aip.util.AIPWebUserParam;
import aip.util.DateConvert;
import aip.util.NVL;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AIPLogSearchDAO
  implements LogSearchInterface
{
  private static final String dataSourceName = "java:comp/env/jdbc/aiplogsearchds";
  private static String dataSourceDriverName;
  public static String seq_logsearchlog = "seq_logsearchlog";
  public static String seq_logsearchword = "seq_logsearchword";
  public static String seq_logsearchsentence = "seq_logsearchsentence";
  public static String seq_logsearchsentenceword = "seq_logsearchsentenceword";
  private static boolean is_check_tables_exist = false;
  
  public LogSearchLST getLogSearchLST(LogSearchLSTParam param)
    throws AIPException
  {
    LogSearchLST lst = new LogSearchLST();
    try
    {
      Connection cn = AIPDBUtil.getDataSourceConnection("java:comp/env/jdbc/aiplogsearchds");
      
      long lstCountSum = NVL.getLng(AIPDBUtil.executeScalar(cn, getLogSearchLogCountSQL(param)));
      lst.setTotalItems(lstCountSum);
      lst.setParam(param);
      
      String queryIds = getLogSearchLogIdsSQL(param);
      List dto_ids = AIPDBUtil.getIds(cn, queryIds, param);
      
      String queryId = "select ls.*,s.sentence from LOGSEARCHLOG ls inner join LOGSEARCHSENTENCE s on ls.f_logsearchsentenceid=s.id where " + AIPDBUtil.getWhereClauseIdIN(dto_ids, "ls.id") + " order by id desc ";
      String[] columns = { "id", "f_logsearchsentenceid", "logdate", "time", "username", "remoteip", "remotehost", "remoteport", "authtype", "sessionid", "userprincipal", "sentence" };
      List<LogSearchDTO> dtos = AIPDBUtil.getObjects(LogSearchDTO.class, cn, queryId, columns, columns);
      
      lst.setRows(dtos);
      
      lst.setParamString(getParamString(param));
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new AIPException(e);
    }
    return lst;
  }
  
  public LogSearchStatisticLST getLogSearchStatisticLST(LogSearchStatisticLSTParam param)
    throws AIPException
  {
    LogSearchStatisticLST lst = new LogSearchStatisticLST();
    try
    {
      Connection cn = AIPDBUtil.getDataSourceConnection("java:comp/env/jdbc/aiplogsearchds");
      
      long lstCountSum = NVL.getLng(AIPDBUtil.executeScalar(cn, getLogSearchStatisticCountSQL(param)));
      lst.setTotalItems(lstCountSum);
      lst.setParam(param);
      
      String queryIds = getLogSearchStatisticIdsSQL(param);
      List dto_ids = AIPDBUtil.getIds(cn, queryIds, param);
      
      String queryId = getLogSearchStatisticIdSQL(param, dto_ids);
      String[] columns = { "id", "word", "wcount" };
      List<LogSearchStatisticDTO> dtos = AIPDBUtil.getObjects(LogSearchStatisticDTO.class, cn, queryId, columns, columns);
      
      lst.setRows(dtos);
      
      lst.setParamString(getParamStringStatistic(param));
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new AIPException(e);
    }
    return lst;
  }
  
  private String getLogSearchStatisticIdSQL(LogSearchStatisticLSTParam param, List dto_ids)
  {
    StringBuffer sb = new StringBuffer();
    sb.append("select w.id,w.word,count(*) as wcount from LOGSEARCHWORD w ,LOGSEARCHSENTENCEWORD lssw,LOGSEARCHLOG ls where lssw.f_logsearchwordid=w.id and lssw.f_logsearchsentenceid = ls.f_logsearchsentenceid and " + AIPDBUtil.getWhereClauseIdIN(dto_ids, "w.id"));
    if (!NVL.isEmpty(param.getLogdatefrom()))
    {
      if (sb.length() > 0) {
        sb.append(" AND ");
      }
      sb.append("(ls.logdate >= '");
      sb.append(param.getLogdatefrom());
      sb.append("')");
    }
    if (!NVL.isEmpty(param.getLogdateto()))
    {
      if (sb.length() > 0) {
        sb.append(" AND ");
      }
      sb.append("(ls.logdate <= '");
      sb.append(param.getLogdateto());
      sb.append("')");
    }
    if (!NVL.isEmpty(param.getUsername()))
    {
      if (sb.length() > 0) {
        sb.append(" AND ");
      }
      sb.append("(ls.username like '%");
      sb.append(param.getUsername());
      sb.append("%')");
    }
    sb.append(" group by w.id,w.word order by count(*) desc ");
    return sb.toString();
  }
  
  public static void logSearch(String searchText, AIPWebUserParam webUserParam)
  {
    Connection cn = null;
    try
    {
      if (NVL.isEmpty(searchText.trim())) {
        return;
      }
      List<String> words = AIPUtil.splitSearchText(searchText);
      cn = AIPDBUtil.getDataSourceConnection("java:comp/env/jdbc/aiplogsearchds");
      if (!is_check_tables_exist)
      {
        checkTablesExistAndCreate(cn);
        is_check_tables_exist = true;
      }
      long sentenceid = NVL.getInt(AIPDBUtil.executeScalar(cn, "select id from LOGSEARCHSENTENCE where sentence='" + searchText + "'"));
      long[] wordid = new long[words.size()];
      for (int i = 0; i < wordid.length; i++) {
        wordid[i] = NVL.getLng(AIPDBUtil.executeScalar(cn, "select id from LOGSEARCHWORD where word='" + (String)words.get(i) + "'"));
      }
      cn.setAutoCommit(false);
      if (sentenceid == 0L)
      {
        String ins_logsearchsentence = getLogsearchsentenceInsSQL(cn, searchText);
        sentenceid = NVL.getLng(AIPDBUtil.executeInsert(cn, ins_logsearchsentence, seq_logsearchsentence));
        for (int i = 0; i < wordid.length; i++)
        {
          if (wordid[i] == 0L)
          {
            String ins_logsearchword = getLogsearchwordInsSQL(cn, (String)words.get(i));
            wordid[i] = NVL.getLng(AIPDBUtil.executeInsert(cn, ins_logsearchword, seq_logsearchword));
          }
          String ins_logsearchsentenceword = getLogsearchsentencewordInsSQL(cn, sentenceid, wordid[i]);
          NVL.getLng(AIPDBUtil.executeInsert(cn, ins_logsearchsentenceword, seq_logsearchword));
        }
      }
      String ins_logsearchlog = getLogsearchlogInsSQL(cn, sentenceid, webUserParam);
      AIPDBUtil.executeInsert(cn, ins_logsearchlog, seq_logsearchlog);
      
      cn.commit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      if (cn != null) {
        try
        {
          cn.rollback();
        }
        catch (SQLException e1) {}
      }
    }
    finally
    {
      if (cn != null) {
        try
        {
          cn.close();
        }
        catch (SQLException e) {}
      }
    }
  }
  
  private String getParamString(LogSearchLSTParam param)
  {
    StringBuffer sb = new StringBuffer();
    if (!NVL.isEmpty(param.getLogdatefrom())) {
      sb.append("تاریخ از:" + param.getLogdatefrom());
    }
    if (!NVL.isEmpty(param.getLogdateto())) {
      sb.append("تاریخ تا:" + param.getLogdateto());
    }
    if (!NVL.isEmpty(param.getFilter())) {
      sb.append("شامل متن:" + param.getFilter());
    }
    if (!NVL.isEmpty(param.getUsername())) {
      sb.append("کاربر:" + param.getUsername());
    }
    return sb.toString();
  }
  
  private String getParamStringStatistic(LogSearchStatisticLSTParam param)
  {
    StringBuffer sb = new StringBuffer();
    if (!NVL.isEmpty(param.getLogdatefrom())) {
      sb.append("تاریخ از:" + param.getLogdatefrom());
    }
    if (!NVL.isEmpty(param.getLogdateto())) {
      sb.append("تاریخ تا:" + param.getLogdateto());
    }
    if (!NVL.isEmpty(param.getFilter())) {
      sb.append("شامل متن:" + param.getFilter());
    }
    if (!NVL.isEmpty(param.getUsername())) {
      sb.append("کاربر:" + param.getUsername());
    }
    return sb.toString();
  }
  
  private String getLogSearchLogCountSQL(LogSearchLSTParam param)
  {
    String sbWhere = getLogSearchLogWhereClause(param);
    
    String result = "select count(*) from LOGSEARCHLOG sl";
    if (!NVL.isEmpty(param.getFilter())) {
      result = result + " inner join LOGSEARCHSENTENCE s on sl.f_logsearchsentenceid = s.id ";
    }
    if (sbWhere.length() > 0) {
      result = result + " WHERE " + sbWhere.toString();
    }
    return result;
  }
  
  private String getLogSearchLogIdsSQL(LogSearchLSTParam param)
  {
    String sbWhere = getLogSearchLogWhereClause(param);
    
    StringBuffer result = new StringBuffer();
    
    result.append("select sl.id from LOGSEARCHLOG sl");
    if (!NVL.isEmpty(param.getFilter())) {
      result.append(" inner join LOGSEARCHSENTENCE s on sl.f_logsearchsentenceid = s.id ");
    }
    if (sbWhere.length() > 0) {
      result.append(" WHERE " + sbWhere.toString());
    }
    result.append(" order by id desc ");
    
    return result.toString();
  }
  
  private String getLogSearchLogWhereClause(LogSearchLSTParam param)
  {
    StringBuffer sbWhere = new StringBuffer();
    if (!NVL.isEmpty(param.getLogdatefrom()))
    {
      sbWhere.append("(logdate >= '");
      sbWhere.append(param.getLogdatefrom());
      sbWhere.append("')");
    }
    if (!NVL.isEmpty(param.getLogdateto()))
    {
      if (sbWhere.length() > 0) {
        sbWhere.append(" AND ");
      }
      sbWhere.append("(logdate <= '");
      sbWhere.append(param.getLogdateto());
      sbWhere.append("')");
    }
    if (!NVL.isEmpty(param.getFilter()))
    {
      if (sbWhere.length() > 0) {
        sbWhere.append(" AND ");
      }
      ArrayList<String> filter_words = AIPUtil.splitSearchText(param.getFilter());
      for (int i = 0; i < filter_words.size(); i++)
      {
        if (i > 0) {
          sbWhere.append(" AND ");
        }
        sbWhere.append("(");
        



        sbWhere.append(" sentence like '%");
        sbWhere.append(param.getFilter());
        sbWhere.append("%')");
      }
    }
    if (!NVL.isEmpty(param.getUsername()))
    {
      if (sbWhere.length() > 0) {
        sbWhere.append(" AND ");
      }
      sbWhere.append("(username like '%");
      sbWhere.append(param.getUsername());
      sbWhere.append("%')");
    }
    return sbWhere.toString();
  }
  
  private String getLogSearchStatisticCountSQL(LogSearchStatisticLSTParam param)
  {
    String sbWhere = getLogSearchStatisticWhereClause(param);
    









    String result = "select count(*) from (select w.id,count(*) from LOGSEARCHWORD w ,LOGSEARCHSENTENCEWORD lssw,LOGSEARCHLOG ls where " + sbWhere.toString() + " group by w.id) t";
    



    return result;
  }
  
  private String getLogSearchStatisticIdsSQL(LogSearchStatisticLSTParam param)
  {
    String sbWhere = getLogSearchStatisticWhereClause(param);
    








    StringBuffer result = new StringBuffer();
    
    result.append("select w.id,count(*) from LOGSEARCHWORD w ,LOGSEARCHSENTENCEWORD lssw,LOGSEARCHLOG ls where ");
    result.append(sbWhere.toString());
    result.append(" group by w.id");
    result.append(" order by count(*) desc");
    
    return result.toString();
  }
  
  private String getLogSearchStatisticWhereClause(LogSearchStatisticLSTParam param)
  {
    StringBuffer sbWhere = new StringBuffer();
    if (!NVL.isEmpty(param.getFilter()))
    {
      sbWhere.append("(w.word like '%");
      sbWhere.append(param.getFilter());
      sbWhere.append("%')");
    }
    if (!NVL.isEmpty(param.getLogdatefrom()))
    {
      if (sbWhere.length() > 0) {
        sbWhere.append(" AND ");
      }
      sbWhere.append("(ls.logdate >= '");
      sbWhere.append(param.getLogdatefrom());
      sbWhere.append("')");
    }
    if (!NVL.isEmpty(param.getLogdateto()))
    {
      if (sbWhere.length() > 0) {
        sbWhere.append(" AND ");
      }
      sbWhere.append("(ls.logdate <= '");
      sbWhere.append(param.getLogdateto());
      sbWhere.append("')");
    }
    if (!NVL.isEmpty(param.getUsername()))
    {
      if (sbWhere.length() > 0) {
        sbWhere.append(" AND ");
      }
      sbWhere.append("(ls.username like '%");
      sbWhere.append(param.getUsername());
      sbWhere.append("%')");
    }
    if (sbWhere.length() > 0) {
      sbWhere.append(" AND ");
    }
    sbWhere.append(" lssw.f_logsearchwordid=w.id and lssw.f_logsearchsentenceid = ls.f_logsearchsentenceid ");
    

    return sbWhere.toString();
  }
  
  private static String getLogsearchlogInsSQL(Connection cn, long sentenceid, AIPWebUserParam webUserParam)
  {
    boolean isoracle = getDataSourceDriverName().toLowerCase().indexOf("oracle") > -1;
    
    StringBuffer sb = new StringBuffer();
    sb.append("insert into LOGSEARCHLOG (");
    if (isoracle) {
      sb.append("id,");
    }
    sb.append("f_logsearchsentenceid ,logdate ,time ,username ,remoteip ,remotehost ,remoteport ,authtype ,sessionid ,userprincipal)values(");
    if (isoracle)
    {
      sb.append(seq_logsearchlog);
      sb.append(".NEXTVAL,");
    }
    sb.append(sentenceid);
    sb.append(",'");
    sb.append(DateConvert.getTodayJalali());
    sb.append("','");
    sb.append(NVL.getNowTime());
    sb.append("','");
    sb.append(webUserParam.getRemoteUser());
    sb.append("','");
    sb.append(webUserParam.getRemoteAddr());
    sb.append("','");
    sb.append(webUserParam.getRemoteHost());
    sb.append("',");
    sb.append(webUserParam.getRemotePort());
    sb.append(",'");
    sb.append(webUserParam.getAuthType());
    sb.append("','");
    sb.append(webUserParam.getRequestedSessionId());
    sb.append("','");
    sb.append(webUserParam.getUserPrincipal());
    sb.append("')");
    
    return sb.toString();
  }
  
  private static String getLogsearchwordInsSQL(Connection cn, String word)
  {
    boolean isoracle = getDataSourceDriverName().toLowerCase().indexOf("oracle") > -1;
    
    StringBuffer sb = new StringBuffer();
    sb.append("insert into LOGSEARCHWORD (");
    if (isoracle) {
      sb.append("id,");
    }
    sb.append("word)values(");
    if (isoracle)
    {
      sb.append(seq_logsearchword);
      sb.append(".NEXTVAL,");
    }
    sb.append("'");
    sb.append(word);
    sb.append("')");
    
    return sb.toString();
  }
  
  private static String getLogsearchsentenceInsSQL(Connection cn, String searchText)
  {
    boolean isoracle = getDataSourceDriverName().toLowerCase().indexOf("oracle") > -1;
    
    StringBuffer sb = new StringBuffer();
    sb.append("insert into LOGSEARCHSENTENCE (");
    if (isoracle) {
      sb.append("id,");
    }
    sb.append("sentence)values(");
    if (isoracle)
    {
      sb.append(seq_logsearchsentence);
      sb.append(".NEXTVAL,");
    }
    sb.append("'");
    sb.append(searchText);
    sb.append("')");
    
    return sb.toString();
  }
  
  private static String getLogsearchsentencewordInsSQL(Connection cn, long sentenceid, long wordid)
  {
    boolean isoracle = getDataSourceDriverName().toLowerCase().indexOf("oracle") > -1;
    
    StringBuffer sb = new StringBuffer();
    sb.append("insert into LOGSEARCHSENTENCEWORD (");
    if (isoracle) {
      sb.append("id,");
    }
    sb.append("f_logsearchsentenceid,f_logsearchwordid)values(");
    if (isoracle)
    {
      sb.append(seq_logsearchsentenceword);
      sb.append(".NEXTVAL,");
    }
    sb.append(sentenceid);
    sb.append(",");
    sb.append(wordid);
    sb.append(")");
    
    return sb.toString();
  }
  
  private static String getDataSourceDriverName()
  {
    if (NVL.isEmpty(dataSourceDriverName))
    {
      dataSourceDriverName = AIPDBUtil.getDriverName("java:comp/env/jdbc/aiplogsearchds");
      System.out.print("AIPLogSearchDAO: Driver " + dataSourceDriverName + " initialized by " + "java:comp/env/jdbc/aiplogsearchds");
      try
      {
        Connection cn = AIPDBUtil.getDataSourceConnection("java:comp/env/jdbc/aiplogsearchds");
        
        System.out.println(" to connect to {catalog=" + cn.getCatalog() + ",url=" + cn.getMetaData().getURL());
        
        cn.close();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return dataSourceDriverName;
  }
  
  private static void checkTablesExistAndCreate(Connection cn)
    throws Exception
  {
    ResultSet rs = cn.getMetaData().getTables(null, null, "LOGSEARCHLOG", new String[] { "TABLE" });
    boolean is_exist_LOGSEARCHLOG = rs.next();
    rs.close();
    if (!is_exist_LOGSEARCHLOG) {
      createTable_LOGSEARCHLOG(cn);
    }
    rs = cn.getMetaData().getTables(null, null, "LOGSEARCHSENTENCE", new String[] { "TABLE" });
    boolean is_exist_LOGSEARCHSENTENCE = rs.next();
    rs.close();
    if (!is_exist_LOGSEARCHSENTENCE) {
      createTable_LOGSEARCHSENTENCE(cn);
    }
    rs = cn.getMetaData().getTables(null, null, "LOGSEARCHWORD", new String[] { "TABLE" });
    boolean is_exist_LOGSEARCHWORD = rs.next();
    rs.close();
    if (!is_exist_LOGSEARCHWORD) {
      createTable_LOGSEARCHWORD(cn);
    }
    rs = cn.getMetaData().getTables(null, null, "LOGSEARCHSENTENCEWORD", new String[] { "TABLE" });
    boolean is_exist_LOGSEARCHSENTENCEWORD = rs.next();
    rs.close();
    if (!is_exist_LOGSEARCHSENTENCEWORD) {
      createTable_LOGSEARCHSENTENCEWORD(cn);
    }
  }
  
  private static void createTable_LOGSEARCHWORD(Connection cn)
    throws Exception
  {
    boolean isoracle = getDataSourceDriverName().toLowerCase().indexOf("oracle") > -1;
    if (isoracle)
    {
      AIPDBUtil.executeUpdate(cn, "create table LOGSEARCHWORD (id number primary key,word varchar2(50))");
      AIPDBUtil.executeUpdate(cn, "create sequence " + seq_logsearchword);
    }
    else
    {
      AIPDBUtil.executeUpdate(cn, "create table LOGSEARCHWORD (id int primary key AUTO_INCREMENT,word varchar(50)) character set utf8 collate utf8_general_ci");
    }
  }
  
  private static void createTable_LOGSEARCHSENTENCE(Connection cn)
    throws Exception
  {
    boolean isoracle = getDataSourceDriverName().toLowerCase().indexOf("oracle") > -1;
    if (isoracle)
    {
      AIPDBUtil.executeUpdate(cn, "create table LOGSEARCHSENTENCE (id number primary key,sentence varchar2(150))");
      AIPDBUtil.executeUpdate(cn, "create sequence " + seq_logsearchsentence);
    }
    else
    {
      AIPDBUtil.executeUpdate(cn, "create table LOGSEARCHSENTENCE (id int primary key AUTO_INCREMENT,sentence varchar(150)) character set utf8 collate utf8_general_ci");
    }
  }
  
  private static void createTable_LOGSEARCHLOG(Connection cn)
    throws Exception
  {
    boolean isoracle = getDataSourceDriverName().toLowerCase().indexOf("oracle") > -1;
    if (isoracle)
    {
      AIPDBUtil.executeUpdate(cn, "create table LOGSEARCHLOG (id number primary key,f_logsearchsentenceid number,logdate char(10),time char(8),username varchar2(50),remoteip char(25),remotehost varchar2(50),remoteport int,authtype varchar2(20),sessionid varchar2(50),userprincipal varchar2(100))");
      AIPDBUtil.executeUpdate(cn, "create sequence " + seq_logsearchlog);
    }
    else
    {
      AIPDBUtil.executeUpdate(cn, "create table LOGSEARCHLOG (id int primary key AUTO_INCREMENT,f_logsearchsentenceid long,logdate char(10),time char(8),username varchar(50),remoteip char(25),remotehost varchar(50),remoteport int,authtype varchar(20),sessionid varchar(50),userprincipal varchar(100)) character set utf8 collate utf8_general_ci");
    }
  }
  
  private static void createTable_LOGSEARCHSENTENCEWORD(Connection cn)
    throws Exception
  {
    boolean isoracle = getDataSourceDriverName().toLowerCase().indexOf("oracle") > -1;
    if (isoracle)
    {
      AIPDBUtil.executeUpdate(cn, "create table LOGSEARCHSENTENCEWORD (id number primary key,f_logsearchsentenceid number,f_logsearchwordid number)");
      AIPDBUtil.executeUpdate(cn, "create sequence " + seq_logsearchsentenceword);
    }
    else
    {
      AIPDBUtil.executeUpdate(cn, "create table LOGSEARCHSENTENCEWORD (id int primary key AUTO_INCREMENT,f_logsearchsentenceid int,f_logsearchwordid int) character set utf8 collate utf8_general_ci");
    }
  }
  
  public static void main(String[] args)
  {
    try
    {
      System.out.println("LogSearchDAO.main():start......");
      




      AIPLogSearchDAO dao = new AIPLogSearchDAO();
      


      LogSearchStatisticLSTParam param = new LogSearchStatisticLSTParam();
      LogSearchStatisticLST lst = dao.getLogSearchStatisticLST(param);
      
      AIPUtil.printObject(lst);
      
      System.out.println("LogSearchDAO.main():end");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
