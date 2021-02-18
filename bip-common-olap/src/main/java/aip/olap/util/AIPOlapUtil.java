package aip.olap.util;

import aip.db.AIPDBUtil;
import aip.olap.ROlapUtil;
import aip.olap.process.AIPOlapProcessInterface;
import aip.olap.process.AIPOlapProcessManager;
import aip.util.AIPException;
import aip.util.AIPUtil;
import aip.util.NVL;
import mondrian.olap.CacheControl;
import mondrian.rolap.RolapConnection;
import mondrian.rolap.RolapCube;
import org.apache.commons.dbcp.PoolableConnection;
import org.olap4j.*;
import org.olap4j.metadata.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class AIPOlapUtil
{
  static Logger logger = LoggerFactory.getLogger(AIPOlapUtil.class);
  public static long lastExecuteMdxTime = 0L;
  String url;
  String catalog;
  String username;
  String password;
  
  /**
   * @deprecated
   */
  public static CellSet executeMdx(String url, String catalog, String username, String password, String mdx)
    throws AIPException
  {
    AIPOlapUtil olapUtil = new AIPOlapUtil(url, catalog, username, password);
    return olapUtil.executeMdx(mdx);
  }
  
  public static CellSet executeMdx(String datasource, String mdx)
    throws AIPException
  {
    OlapConnection cn = null;
    try
    {
      AIPOlapUtil olapUtil = new AIPOlapUtil();
      cn = getOlapConnection(datasource);
      return olapUtil.executeMdx(cn, mdx);
    }
    finally
    {
      try
      {
        if (cn != null) {
          cn.close();
        }
      }
      catch (Exception e)
      {
        logger.error(e.getMessage());
      }
    }
  }
  
  public static OlapConnection getOlapConnection(String datasource)
    throws AIPException
  {
    Connection cn = null;
    OlapConnection olapCN = null;
    try
    {
      cn = AIPDBUtil.getDataSourceConnection(datasource, false);
      if ((cn instanceof OlapConnection))
      {
        olapCN = (OlapConnection)cn.unwrap(OlapConnection.class);
      }
      else if ((cn instanceof org.apache.commons.dbcp.DelegatingConnection))
      {
        PoolableConnection poolableConnection = (PoolableConnection)((org.apache.commons.dbcp.DelegatingConnection)cn).getDelegate();
        olapCN = (OlapConnection)poolableConnection.getDelegate();
      }
      else if (cn.toString().indexOf("weblogic") > -1)
      {
        olapCN = (OlapConnection)cn.unwrap(OlapConnection.class);
      }
      else if ((cn instanceof org.apache.tomcat.dbcp.dbcp.DelegatingConnection))
      {
        org.apache.tomcat.dbcp.dbcp.DelegatingConnection del = new org.apache.tomcat.dbcp.dbcp.DelegatingConnection(cn);
        olapCN = (OlapConnection)del.getInnermostDelegate();
      }
      else if (cn.isWrapperFor(OlapConnection.class))
      {
        olapCN = (OlapConnection)cn.unwrap(OlapConnection.class);
      }
      else
      {
        cn.close();
      }
      if (olapCN == null) {
        throw new AIPException("AIPOlapUtil:make sure olap4j.jar copied in tomcat lib folder and your resource is like this &lt;Resource auth=\"Container\" type=\"javax.sql.DataSource\"  driverClassName=\"org.olap4j.driver.xmla.XmlaOlap4jDriver\"  maxActive=\"20\" maxIdle=\"10\" maxWait=\"-1\"  name=\"aipsabtbicube\" username=\"bi_admin\" password=\"aippia\"  url=\"jdbc:xmla:Server=http://192.168.0.71:80/olap/msmdpump.dll;Catalog=AIPSabtBICube\"  accessToUnderlyingConnectionAllowed=\"true\" /&gt;");
      }
      return olapCN;
    }
    catch (AIPException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      throw new AIPException(ex);
    }
  }
  
  /**
   * @deprecated
   */
  public static String executeMdx2JSArray(String url, String catalog, String username, String password, String mdx)
    throws AIPException
  {
    AIPOlapUtil olapUtil = new AIPOlapUtil(url, catalog, username, password);
    return olapUtil.executeMdx2JSArray(mdx);
  }
  
  public static String executeMdx2JSArray(String datasource, String mdx)
    throws AIPException
  {
    CellSet cellSet = executeMdx(datasource, mdx);
    AIPOlapUtil olapUtil = new AIPOlapUtil();
    return olapUtil._convertCellSet2JSArray(cellSet);
  }
  
  public static String[][] getCellSetValues(CellSet cellSet)
  {
    AIPOlapUtil olapUtil = new AIPOlapUtil();
    return olapUtil._getCellSetValues(cellSet);
  }
  
  public static List<List<String>> convertCellSet2List(CellSet cellSet)
  {
    AIPOlapUtil olapUtil = new AIPOlapUtil();
    return olapUtil._convertCellSet2List(cellSet);
  }
  
  protected AIPOlapUtil() {}
  
  /**
   * @deprecated
   */
  public AIPOlapUtil(String url, String catalog, String username, String password)
  {
    this.url = url;
    this.catalog = catalog;
    this.username = username;
    this.password = password;
    System.out.println("AIPOlapUtil.AIPOlapUtil():url=" + url + ", catalog=" + catalog + ", username=" + username + ", password=" + password);
  }
  
  /**
   * @deprecated
   */
  public CellSet executeMdx(String mdx)
    throws AIPException
  {
    System.out.println("AIPOlapUtil.executeMdx():mdx=" + mdx);
    


    OlapConnection olapConnection = null;
    OlapStatement statement = null;
    try
    {
      Class.forName("org.olap4j.driver.xmla.XmlaOlap4jDriver");
      

      Properties props = new Properties();
      props.put("user", this.username);
      props.put("password", this.password);
      props.put("charSet", "UTF-8");
      Connection connection = DriverManager.getConnection("jdbc:xmla:Server=" + this.url + ";" + "Catalog=" + this.catalog, props);
      

      olapConnection = (OlapConnection)connection.unwrap(OlapConnection.class);
      CellSet cellSet = executeMdx(olapConnection, mdx);
      return cellSet;
    }
    catch (AIPException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      throw new AIPException(ex);
    }
    finally
    {
      try
      {
        if (olapConnection != null) {
          olapConnection.close();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  public CellSet executeMdx(OlapConnection olapConnection, String mdx)
    throws AIPException
  {
    Date start = new Date();
    
    String olapConnectionInfo = getOlapConnectionInfo(olapConnection);
    OlapStatement statement = null;
    try
    {
      statement = olapConnection.createStatement();
      CellSet cellSet = statement.executeOlapQuery(mdx);
      return cellSet;
    }
    catch (OlapException ex)
    {
      logger.error("خطا در اجرای دستور:[" + mdx + "]", ex);
      throw new AIPException(getFaultMessage(ex), ex);
    }
    finally
    {
      try
      {
        if (statement != null) {
          statement.close();
        }
      }
      catch (Exception e)
      {
        logger.error(e.getMessage());
      }
      Date end = new Date();
      lastExecuteMdxTime = end.getTime() - start.getTime();
      logger.info("executeMdx for schema:" + olapConnectionInfo + " in [" + lastExecuteMdxTime + "] millisecond.");
      logger.debug("MDX=[" + mdx + "]");
    }
  }
  
  private String getOlapConnectionInfo(OlapConnection olapConnection)
  {
    String result = "";
    try
    {
      result = olapConnection.getOlapSchema().getName();
    }
    catch (Exception e)
    {
      logger.error(e.getMessage());
    }
    return result;
  }
  
  /**
   * @deprecated
   */
  public String executeMdx2JSArray(String mdx)
    throws AIPException
  {
    CellSet cellSet = executeMdx(mdx);
    String _jsarray = _convertCellSet2JSArray(cellSet);
    try
    {
      cellSet.close();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return _jsarray;
  }
  
  public static String convertCellSet2JSArray(CellSet cellSet)
  {
    AIPOlapUtil olapUtil = new AIPOlapUtil();
    return olapUtil._convertCellSet2JSArray(cellSet);
  }
  
  public String _convertCellSet2JSArray(CellSet cellSet)
  {
    StringBuffer sb = new StringBuffer();
    sb.append("{cellset:");
    sb.append("[");
    


    sb.append("[");
    sb.append("[]");
    for (Position column : (CellSetAxis)cellSet.getAxes().get(0))
    {
      sb.append(",[");
      for (Member member : column.getMembers())
      {
        String value = member.getUniqueName().replace("[", "").replace("]", "");
        sb.append("'" + value + "',");
      }
      if (column.getMembers().size() > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
    }
    sb.append("]");
    if (cellSet.getAxes().size() > 1)
    {
      for (Position row : (CellSetAxis)cellSet.getAxes().get(1))
      {
        sb.append(",[");
        


        sb.append("[");
        for (Member member : row.getMembers())
        {
          String value = member.getUniqueName().replace("[", "").replace("]", "");
          sb.append("'" + value + "',");
        }
        if (row.getMembers().size() > 0) {
          sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        for (Position column : (CellSetAxis)cellSet.getAxes().get(0))
        {
          Cell cell = cellSet.getCell(new Position[] { column, row });
          sb.append(",'" + cell.getFormattedValue() + "'");
        }
        sb.append("]");
      }
    }
    else
    {
      sb.append(",[[]");
      for (Position column : (CellSetAxis)cellSet.getAxes().get(0))
      {
        Cell cell = cellSet.getCell(new Position[] { column });
        sb.append(",'" + cell.getFormattedValue() + "'");
      }
      sb.append("]");
    }
    sb.append("]");
    sb.append("}");
    return sb.toString();
  }
  
  private String[][] _getCellSetValues(CellSet cellSet)
  {
    int cur_row = 0;int cur_col = 0;
    String[][] values;
    if (cellSet.getAxes().size() > 1)
    {
      values = new String[((CellSetAxis)cellSet.getAxes().get(1)).getPositionCount()][((CellSetAxis)cellSet.getAxes().get(0)).getPositionCount() + 1];
      for (Position row : (CellSetAxis)cellSet.getAxes().get(1))
      {
        cur_col = 0;
        for (Member member : row.getMembers())
        {
          String value = member.getUniqueName().replace("[", "").replace("]", "");
          values[cur_row][(cur_col++)] = value;
        }
        for (Position column : (CellSetAxis)cellSet.getAxes().get(0))
        {
          Cell cell = cellSet.getCell(new Position[] { column, row });
          values[cur_row][(cur_col++)] = cell.getFormattedValue();
        }
        cur_row++;
      }
    }
    else
    {
      values = new String[1][((CellSetAxis)cellSet.getAxes().get(0)).getPositionCount() + 1];
      for (Position column : (CellSetAxis)cellSet.getAxes().get(0))
      {
        Cell cell = cellSet.getCell(new Position[] { column });
        values[0][(cur_col++)] = cell.getFormattedValue();
      }
    }
    return values;
  }
  
  private List<List<String>> _convertCellSet2List(CellSet cellSet)
  {
    List<List<String>> values = new ArrayList();
    Iterator i$;
    Position row;
    List<String> row_list;
    if (cellSet.getAxes().size() > 1)
    {
      for (i$ = ((CellSetAxis)cellSet.getAxes().get(1)).iterator(); i$.hasNext();)
      {
        row = (Position)i$.next();
        row_list = new ArrayList();
        values.add(row_list);
        


        String rowheader = "";
        for (Member member : row.getMembers())
        {
          if ((NVL.isEmpty(rowheader)) && (member.getDepth() > 1)) {
            rowheader = NVL.getStringFixLen("", member.getDepth() - 1, '\t');
          }
          String value = rowheader + member.getCaption();
          row_list.add(value);
        }
        for (Position column : (CellSetAxis)cellSet.getAxes().get(0))
        {
          Cell cell = cellSet.getCell(new Position[] { column, row });
          row_list.add(cell.getFormattedValue());
        }
      }
    }
    else
    {
      row_list = new ArrayList();
      values.add(row_list);
      for (Position column : (CellSetAxis)cellSet.getAxes().get(0))
      {
        Cell cell = cellSet.getCell(new Position[] { column });
        row_list.add(cell.getFormattedValue());
      }
    }
    return values;
  }
  
  private String getFaultMessage(OlapException ex)
  {
    String message = NVL.getString(ex.getMessage());
    String faultcode = null;String faultstring = null;
    int pos_faultcode = message.indexOf("<faultcode>");
    if (pos_faultcode > 0)
    {
      int pos_faultcode_end = message.indexOf("</faultcode>");
      faultcode = message.substring(pos_faultcode + "<faultcode>".length(), pos_faultcode_end);
    }
    int pos_faultstring = message.indexOf("<faultstring>");
    if (pos_faultstring > 0)
    {
      int pos_faultstring_end = message.indexOf("</faultstring>");
      faultstring = message.substring(pos_faultstring + "<faultstring>".length(), pos_faultstring_end);
    }
    if ((!NVL.isEmpty(faultcode)) || (!NVL.isEmpty(faultstring))) {
      message = NVL.getString(faultcode) + ":" + NVL.getString(faultstring);
    }
    return message;
  }
  
  public static String executeXMLA(String url, String xmlSoapText)
    throws AIPException
  {
    AIPOlapUtil olapUtil = new AIPOlapUtil(url, null, null, null);
    return olapUtil.executeXMLA(xmlSoapText);
  }
  
  public String executeXMLA(String xmlSoapText)
    throws AIPException
  {
    return AIPSoapUtil.executeSOAP(this.url, xmlSoapText);
  }
  
  public static void processCube(String url, String catalog, String username, String password, String cube)
    throws AIPException
  {
    AIPOlapUtil olapUtil = new AIPOlapUtil(url, catalog, username, password);
    olapUtil.processCube(cube);
  }
  
  public void processCube(String cube)
    throws AIPException
  {
    AIPOlapProcessInterface cn = AIPOlapProcessManager.getConnection(this.url, this.username, this.password);
    cn.processCube(this.catalog, cube);
  }
  
  public static void processDimension(String url, String catalog, String username, String password, String dimension)
    throws AIPException
  {
    AIPOlapUtil olapUtil = new AIPOlapUtil(url, catalog, username, password);
    olapUtil.processDimension(dimension);
  }
  
  public void processDimension(String dimension)
    throws AIPException
  {
    AIPOlapProcessInterface cn = AIPOlapProcessManager.getConnection(this.url, this.username, this.password);
    cn.processDimension(this.catalog, dimension);
  }
  
  public static void mondrianCacheControlFlushSchemaCache(String datasource)
    throws AIPException
  {
    OlapConnection olapConnection = null;
    try
    {
      olapConnection = getOlapConnection(datasource);
      CacheControl cacheControl = ((RolapConnection)olapConnection.unwrap(RolapConnection.class)).getCacheControl(null);
      cacheControl.flushSchemaCache();
    }
    catch (Exception e)
    {
      throw new AIPException(e);
    }
    finally
    {
      if (olapConnection != null) {
        try
        {
          olapConnection.close();
        }
        catch (Exception e2) {}
      }
    }
  }
  
  private static void main1(String[] args)
  {
    try
    {
      String mdx = "select {  [Measures].[Ù…Ø¨Ù„Øº Ú©Ù„]\t} on columns , { [Ù�Ø±Ø¢ÙˆØ±Ø¯Ù‡].[Ø±Ù†Ú¯].members } on rows from [Ø­ÙˆØ§Ù„Ù‡ Ø¨Ø§Ù�ÛŒØ´]";
      


      String mdx1 = "select { [Measures]} on columns from [Ø­ÙˆØ§Ù„Ù‡ Ø¨Ø§Ù�ÛŒØ´]";
      
      Date d1 = new Date();
      

      AIPOlapUtil olapUtil = new AIPOlapUtil("http://192.168.0.23:8080/olap/msmdpump.dll", "NIOPDC_BI_Ver2", "aip-support", "aippia");
      
      Date d2 = new Date();
      
      CellSet cellSet = olapUtil.executeMdx(mdx);
      String _jsarray = olapUtil._convertCellSet2JSArray(cellSet);
      
      Date d3 = new Date();
      for (Iterator i$ = ((CellSetAxis)cellSet.getAxes().get(1)).iterator(); i$.hasNext();)
      {
    	  Position row = (Position)i$.next();
        for (Position column : (CellSetAxis)cellSet.getAxes().get(0))
        {
          for (Member member : row.getMembers()) {
            System.out.println(member.getUniqueName());
          }
          for (Member member : column.getMembers()) {
            System.out.println(member.getUniqueName());
          }
          Cell cell = cellSet.getCell(new Position[] { column, row });
          System.out.println(cell.getFormattedValue());
          System.out.println();
        }
      }
      Position row;
      cellSet.close();
      Date d4 = new Date();
      
      System.out.println("load:" + (d2.getTime() - d1.getTime()));
      System.out.println("execute:" + (d3.getTime() - d2.getTime()));
      System.out.println("print:" + (d4.getTime() - d3.getTime()));
      System.out.println("total:" + (d4.getTime() - d1.getTime()));
      
      System.out.println("_jsarray=" + _jsarray);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  private static void main2(String[] args)
  {
    try
    {
      String mdx = "select { [Measures].tedadtavalod\t} on columns, { DimMahal.mahalha.[All].children } on rows from aipsabtbi";
      


      Date d1 = new Date();
      

      AIPOlapUtil olapUtil = new AIPOlapUtil("http://192.168.0.71:80/olap/msmdpump.dll", "AIPSabtBICube", "bi_admin", "aippia");
      
      Date d2 = new Date();
      
      CellSet cellSet = executeMdx("aipsabtbicube", mdx);
      String _jsarray = convertCellSet2JSArray(cellSet);
      
      Date d3 = new Date();
      for (Iterator i$ = ((CellSetAxis)cellSet.getAxes().get(1)).iterator(); i$.hasNext();)
      {
    	  Position row = (Position)i$.next();
        for (Position column : (CellSetAxis)cellSet.getAxes().get(0))
        {
          for (Member member : row.getMembers()) {
            System.out.println(member.getUniqueName());
          }
          for (Member member : column.getMembers()) {
            System.out.println(member.getUniqueName());
          }
          Cell cell = cellSet.getCell(new Position[] { column, row });
          System.out.println(cell.getFormattedValue());
          System.out.println();
        }
      }
      Position row;
      cellSet.close();
      Date d4 = new Date();
      
      System.out.println("load:" + (d2.getTime() - d1.getTime()));
      System.out.println("execute:" + (d3.getTime() - d2.getTime()));
      System.out.println("print:" + (d4.getTime() - d3.getTime()));
      System.out.println("total:" + (d4.getTime() - d1.getTime()));
      
      System.out.println("_jsarray=" + _jsarray);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  private static void main4(String[] args)
  {
    System.out.println("AIPOlapUtil.main():starting........");
    try
    {
      String mdx1 = "select {  [Measures].[Ù…Ø¨Ù„Øº Ú©Ù„]\t} on columns , { [Ù�Ø±Ø¢ÙˆØ±Ø¯Ù‡].[Ø±Ù†Ú¯].members } on rows from [Ø­ÙˆØ§Ù„Ù‡ Ø¨Ø§Ù�ÛŒØ´]";
      


      String naftDataSource = "aipnaftbicube";
      String naftDataSource2 = "java:comp/env/aipnaftbicube";
      String sabtDataSource = "aipsabtbicube";
      String sabtDataSource2 = "java:comp/env/aipsabtbicube";
      
      Date d11 = new Date();
      CellSet cs1 = executeMdx(naftDataSource, mdx1);
      Date d12 = new Date();
      cs1 = executeMdx(naftDataSource, mdx1);
      Date d13 = new Date();
      String st1 = executeMdx2JSArray(naftDataSource, mdx1);
      Date d14 = new Date();
      System.out.println(mdx1 + "=" + st1);
      
      String mdx2 = "select { [Measures].tedadtavalod\t} on columns, { DimMahal.mahalha.[All].children } on rows from aipsabtbi";
      


      Date d21 = new Date();
      CellSet cs2 = executeMdx(sabtDataSource, mdx2);
      Date d22 = new Date();
      cs2 = executeMdx(sabtDataSource, mdx2);
      Date d23 = new Date();
      String st2 = executeMdx2JSArray(sabtDataSource, mdx2);
      Date d24 = new Date();
      System.out.println(mdx2 + "=" + st2);
      
      System.out.println("aipnaftbicube first exeution time:" + (d12.getTime() - d11.getTime()));
      System.out.println("aipnaftbicube second exeution time:" + (d13.getTime() - d12.getTime()));
      System.out.println("aipnaftbicube third exeution+cnv js time:" + (d14.getTime() - d13.getTime()));
      
      System.out.println("aipsabtbicube first exeution time:" + (d22.getTime() - d21.getTime()));
      System.out.println("aipsabtbicube second exeution time:" + (d23.getTime() - d22.getTime()));
      System.out.println("aipsabtbicube third exeution+cnv js time:" + (d24.getTime() - d23.getTime()));
      

      System.out.println("AIPOlapUtil.main():finished.");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public static void cnvCellSet2String(StringBuffer html, CellSet cs)
  {
    html.append("<table border='1'>");
    cnvCellSet2String(html, cs, "<tr class='[oddevenrowclass]'>", "</tr>", "<td nowrap>", "</td>", "<span style='padding:", "px'/>", "<td class='[columnnoclass]'>", "</td>", "oddRow", "evenRow", "c", true);
    






    html.append("</table>");
  }
  
  public static void cnvCellSet2String(StringBuffer html, CellSet cs, String rowprefix, String rowsuffix, String rowheaderprefix, String rowheadersuffix, String rowindentprefix, String rowindentsuffix, String cellprefix, String cellsuffix, String oddrowclass, String eventrowclass, String columnnoclass, boolean isDigitEn2Fa)
  {
    boolean rowindentEnabled = (!NVL.isEmpty(rowindentprefix)) && (!NVL.isEmpty(rowindentsuffix));
    
    int columnscount = ((CellSetAxis)cs.getAxes().get(0)).getPositionCount();
    int rowscount = 1;
    if (cs.getAxes().size() > 1) {
      rowscount = ((CellSetAxis)cs.getAxes().get(1)).getPositionCount();
    }
    int totalcount = columnscount * rowscount;
    
    String[] cellprefix_withno = new String[columnscount];
    for (int i = 0; i < columnscount; i++)
    {
      cellprefix_withno[i] = cellprefix;
      if ((!NVL.isEmpty(columnnoclass)) && (cellprefix.contains("[columnnoclass]"))) {
        cellprefix_withno[i] = cellprefix.replace("[columnnoclass]", columnnoclass + (i + 1));
      }
    }
    String rowprefix_odd = rowprefix;
    if ((!NVL.isEmpty(oddrowclass)) && (rowprefix.contains("[oddevenrowclass]"))) {
      rowprefix_odd = rowprefix.replace("[oddevenrowclass]", oddrowclass);
    }
    String rowprefix_even = rowprefix;
    if ((!NVL.isEmpty(eventrowclass)) && (rowprefix.contains("[oddevenrowclass]"))) {
      rowprefix_even = rowprefix.replace("[oddevenrowclass]", eventrowclass);
    }
    html.append("<tr>");
    html.append("<th></th>");
    for (Position column : (CellSetAxis)cs.getAxes().get(0))
    {
      html.append("<th>");
      for (Member member : column.getMembers())
      {
        String value = member.getUniqueName().replace("[", "").replace("]", "");
        html.append("'" + value + "',");
      }
      if (column.getMembers().size() > 0) {
        html.deleteCharAt(html.length() - 1);
      }
      html.append("</th>");
    }
    html.append("</tr>");
    




    int i = 0;
    int rowcounter = 0;
    while (i < totalcount)
    {
      if (rowcounter % 2 == 0) {
        html.append(rowprefix_odd);
      } else {
        html.append(rowprefix_even);
      }
      html.append(rowheaderprefix);
      Position row = (Position)((CellSetAxis)cs.getAxes().get(1)).getPositions().get(i / columnscount);
      String rowheader = "";
      for (Member member : row.getMembers())
      {
        if ((NVL.isEmpty(rowheader)) && (member.getDepth() > 1) && (rowindentEnabled))
        {
          int n = member.getDepth() - 1;
          
          String indent = rowindentprefix + n * 10 + rowindentsuffix;
          rowheader = indent;
        }
        rowheader = rowheader + member.getCaption() + " ";
      }
      html.append(rowheader);
      html.append(rowheadersuffix);
      for (int j = 0; j < columnscount; j++)
      {
        html.append(cellprefix_withno[j]);
        if (NVL.getLng(cs.getCell(i).getValue()) == 0L) {
          html.append("0");
        } else {
          html.append(NVL.getString(cs.getCell(i).getFormattedValue()));
        }
        i++;
        html.append(cellsuffix);
      }
      html.append(rowsuffix);
      rowcounter++;
    }
    if (isDigitEn2Fa) {
      AIPUtil.cnvDigitEn2Fa(html);
    }
  }
  
  public static void main(String[] args)
    throws Exception
  {
    ROlapUtil.registerNoneRolapAggregator();

    //String mdx1 = "with member [DimEdare].[جمع] as 'sum(EXCEPT([DimEdare].[All].Children,{[DimEdare].[ostan].[1031],[DimEdare].[shahrestan].[1487],[DimEdare].[edare].[23]}))' member Measures.TarikhFilteredTedadtavalod as 'sum({DimTarikhSabttavalod.[1394]},Measures.tedadtavalod)'  SET tavalod1set AS {(DimShahrrosta.[All],DimJarimoavaghe.[All],DimJensiat.[All]),(DimShahrrosta.[All],DimJarimoavaghe.[1],DimJensiat.[All]),(DimShahrrosta.[All],DimJarimoavaghe.[2],DimJensiat.[All]),(DimShahrrosta.[1],DimJarimoavaghe.[All],DimJensiat.[All]),(DimShahrrosta.[2],DimJarimoavaghe.[All],DimJensiat.[All]),(DimShahrrosta.[All],DimJarimoavaghe.[All],DimJensiat.[1]),(DimShahrrosta.[All],DimJarimoavaghe.[All],DimJensiat.[2]),(DimShahrrosta.[1],DimJarimoavaghe.[1],DimJensiat.[All]),(DimShahrrosta.[1],DimJarimoavaghe.[1],DimJensiat.[1]),(DimShahrrosta.[1],DimJarimoavaghe.[1],DimJensiat.[2]) ,(DimShahrrosta.[1],DimJarimoavaghe.[2],DimJensiat.[All]),(DimShahrrosta.[1],DimJarimoavaghe.[2],DimJensiat.[1]),(DimShahrrosta.[1],DimJarimoavaghe.[2],DimJensiat.[2]),(DimShahrrosta.[2],DimJarimoavaghe.[1],DimJensiat.[All]),(DimShahrrosta.[2],DimJarimoavaghe.[1],DimJensiat.[1]),(DimShahrrosta.[2],DimJarimoavaghe.[1],DimJensiat.[2]),(DimShahrrosta.[2],DimJarimoavaghe.[2],DimJensiat.[All]),(DimShahrrosta.[2],DimJarimoavaghe.[2],DimJensiat.[1]),(DimShahrrosta.[2],DimJarimoavaghe.[2],DimJensiat.[2])} select {[tavalod1set]} on columns,{Order( Filter( DESCENDANTS([DimEdare].[1000],[DimEdare].[edare],SELF_AND_BEFORE) , [Dimedare].CurrentMember not in {filteredEdare} ),Dimedare.CurrentMember.Caption,ASC)} on rows from [Tavalod] where ( Measures.TarikhFilteredTedadtavalod  )";
    String mdx = "with member [DimEdare].[جمع] as 'sum(EXCEPT([DimEdare].[All].Children,{[DimEdare].[ostan].[1031],[DimEdare].[shahrestan].[1487],[DimEdare].[edare].[23]}))' member Measures.TarikhFilteredTedadtavalod as 'sum({DimTarikhSabttavalod.[1394]},Measures.tedadtavalod)'  SET tavalod1set AS {DimShahrrosta.[All]} select {[tavalod1set]} on columns,{Order( Filter( DESCENDANTS([DimEdare].[1000],[DimEdare].[edare],SELF_AND_BEFORE) , [Dimedare].CurrentMember not in {filteredEdare} ),Dimedare.CurrentMember.Caption,ASC)} on rows from [Tavalod] where ( Measures.TarikhFilteredTedadtavalod  )";

    RolapCube cube=null;

/*
    RolapStar.Column[] columns;
    AggGen aggGen=new AggGen(cube.getName(),cube.getStar(),columns);
*/


    //String result = executeMdx2JSArray("jdbc/aipsabtbicube", mdx);
    String result = executeMdx2JSArray("jdbc/bipsabtsuppbicube", mdx);
    System.out.println("SabtBIUtil.main():result=" + result);
  }
}
