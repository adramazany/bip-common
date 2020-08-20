package aip.util;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;
import oracle.jdbc.OracleResultSet;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

public class AIPTransfer
{
  public void transferData(String srcDriver, String srcURL, String srcUser, String srcPass, String srcQuery, String[] srcColumns, String destDriver, String destURL, String destUser, String destPass, String destSQL, String[] destColumns, String ifExistsNoActionQuery, String ifExistsNoAlternateSQL)
  {
    PrintStream prntStream = null;
    transferData(srcDriver, srcURL, srcUser, srcPass, srcQuery, srcColumns, destDriver, destURL, destUser, destPass, destSQL, destColumns, ifExistsNoActionQuery, ifExistsNoAlternateSQL, new String[0], new String[0], Boolean.valueOf(false), prntStream);
  }
  
  public void transferData(String srcDriver, String srcURL, String srcUser, String srcPass, String srcQuery, String[] srcColumns, String destDriver, String destURL, String destUser, String destPass, String destSQL, String[] destColumns, String ifExistsNoActionQuery, String ifExistsNoAlternateSQL, PrintStream prntStream)
  {
    transferData(srcDriver, srcURL, srcUser, srcPass, srcQuery, srcColumns, destDriver, destURL, destUser, destPass, destSQL, destColumns, ifExistsNoActionQuery, ifExistsNoAlternateSQL, new String[0], new String[0], Boolean.valueOf(false), prntStream);
  }
  
  public void transferData(String srcDriver, String srcURL, String srcUser, String srcPass, String srcQuery, String[] srcColumns, String destDriver, String destURL, String destUser, String destPass, String destSQL, String[] destColumns, String ifExistsNoActionQuery, String ifExistsNoAlternateSQL, String[] srcClobColumns, String[] destClobColumns, Boolean isBlob, PrintStream prntStream)
  {
    transferData(srcDriver, srcURL, srcUser, srcPass, srcQuery, srcColumns, destDriver, destURL, destUser, destPass, destSQL, destColumns, ifExistsNoActionQuery, ifExistsNoAlternateSQL, srcClobColumns, destClobColumns, isBlob, prntStream, 100, null, null, null, 1, 100);
  }
  
  public void transferData(String srcDriver, String srcURL, String srcUser, String srcPass, String srcQuery, String[] srcColumns, String destDriver, String destURL, String destUser, String destPass, String destSQL, String[] destColumns, String ifExistsNoActionQuery, String ifExistsNoAlternateSQL, String[] srcClobColumns, String[] destClobColumns, Boolean isBlob, PrintStream prntStream, int destdbtype, String beforeMigrateExecOnSrc, String beforeMigrateExecOnDest, String tablename, int batch_length, int transfer_print_length)
  {
    System.out.println("AIPTransfer.transferData():Starting.......");
    Date method_start = new Date();
    



    Connection srcCN = null;Connection destCN = null;
    Statement srcSTMT = null;Statement destSTMT = null;Statement destSTMTExist = null;
    ResultSet srcRS = null;ResultSet destRS = null;
    String tmpSQL = null;
    String tmpExists = null;
    String tmpAlternate = null;
    String clobQuery = null;
    if (destClobColumns != null)
    {
      clobQuery = "select ";
      for (int i = 0; i < destClobColumns.length; i++)
      {
        clobQuery = clobQuery + destClobColumns[i];
        if (i < destClobColumns.length - 1) {
          clobQuery = clobQuery + ",";
        }
      }
    }
    try
    {
      srcQuery = srcQuery.toLowerCase();
      destSQL = destSQL.toLowerCase();
      if (ifExistsNoAlternateSQL != null) {
        ifExistsNoAlternateSQL = ifExistsNoAlternateSQL.toLowerCase();
      }
      for (int i = 0; i < srcColumns.length; i++) {
        srcColumns[i] = srcColumns[i].toLowerCase();
      }
      for (int i = 0; i < destColumns.length; i++) {
        destColumns[i] = destColumns[i].toLowerCase();
      }
      Class.forName(srcDriver);
      srcCN = DriverManager.getConnection(srcURL, srcUser, srcPass);
      srcSTMT = srcCN.createStatement(1003, 1007);
      
      Class.forName(destDriver);
      destCN = DriverManager.getConnection(destURL, destUser, destPass);
      destSTMT = destCN.createStatement();
      if (ifExistsNoActionQuery != null)
      {
        ifExistsNoActionQuery = ifExistsNoActionQuery.toLowerCase();
        destSTMTExist = destCN.createStatement(1003, 1007);
      }
      if (!NVL.isEmpty(tablename)) {
        try
        {
          Statement stmtTablecount = srcCN.createStatement();
          ResultSet rsTablecount = stmtTablecount.executeQuery("select count(*) from " + tablename);
          int tablecount = 0;
          if (rsTablecount.next()) {
            tablecount = rsTablecount.getInt(1);
          }
          rsTablecount.close();
          stmtTablecount.close();
          System.out.print("table count=" + tablecount);
        }
        catch (Exception e)
        {
          System.out.println("table count:exception=" + e.getMessage());
        }
      }
      if (!NVL.isEmpty(beforeMigrateExecOnSrc)) {
        try
        {
          Statement stmtBeforeMigrateExecOnSrc = srcCN.createStatement();
          String[] sqls = AIPUtil.splitSelectedIds(beforeMigrateExecOnSrc, ";");
          for (int i = 0; i < sqls.length; i++) {
            stmtBeforeMigrateExecOnSrc.addBatch(sqls[i]);
          }
          stmtBeforeMigrateExecOnSrc.executeBatch();
          stmtBeforeMigrateExecOnSrc.close();
        }
        catch (Exception e)
        {
          System.out.println("BeforeMigrateExecOnSrc=" + beforeMigrateExecOnSrc);
          System.out.println("AIPTransfer.transferData():exception=" + e.getMessage());
          if (prntStream != null) {
            prntStream.println("BeforeMigrateExecOnSrc=" + beforeMigrateExecOnSrc + "\n\t exception=" + e.getMessage());
          }
        }
      }
      if (!NVL.isEmpty(beforeMigrateExecOnDest)) {
        try
        {
          Statement stmtBeforeMigrateExecOnDest = destCN.createStatement();
          String[] sqls = AIPUtil.splitSelectedIds(beforeMigrateExecOnDest, ";");
          for (int i = 0; i < sqls.length; i++) {
            stmtBeforeMigrateExecOnDest.addBatch(sqls[i]);
          }
          stmtBeforeMigrateExecOnDest.executeBatch();
          stmtBeforeMigrateExecOnDest.close();
        }
        catch (Exception e)
        {
          System.out.println("beforeMigrateExecOnDest=" + beforeMigrateExecOnDest);
          System.out.println("AIPTransfer.transferData():exception=" + e.getMessage());
          if (prntStream != null) {
            prntStream.println("beforeMigrateExecOnDest=" + beforeMigrateExecOnDest + "\n\t exception=" + e.getMessage());
          }
        }
      }
      if (srcClobColumns == null) {
        srcClobColumns = new String[0];
      }
      if (destClobColumns == null) {
        destClobColumns = new String[0];
      }
      boolean batch_enabled = false;
      if ((batch_length > 1) && ((srcClobColumns == null) || (srcClobColumns.length <= 0) || (destdbtype != 1))) {
        batch_enabled = true;
      }
      int batch_counter = 0;
      StringBuffer batch_tmpSQL = new StringBuffer();
      



      int transfer_print_all = 0;
      int transfer_print_counter = 0;
      if (transfer_print_length > 0) {
        System.out.print("\t transfer_print:");
      }
      srcRS = srcSTMT.executeQuery(srcQuery);
      while (srcRS.next())
      {
        tmpSQL = destSQL;
        tmpExists = ifExistsNoActionQuery;
        tmpAlternate = ifExistsNoAlternateSQL;
        
        String[] clobValue = new String[srcClobColumns.length];
        byte[][] blobValue = new byte[srcClobColumns.length][];
        for (int i = 0; i < destColumns.length; i++)
        {
          String value = NVL.getString(srcRS.getString(srcColumns[i]), "null");
          
          value = value.replaceAll("'", "''");
          
          tmpSQL = tmpSQL.replaceAll(destColumns[i], value);
          if (tmpExists != null) {
            tmpExists = tmpExists.replaceAll(destColumns[i], value);
          }
          if (tmpAlternate != null) {
            tmpAlternate = tmpAlternate.replaceAll(destColumns[i], value);
          }
        }
        for (int i = 0; i < srcClobColumns.length; i++) {
          if (isBlob.booleanValue()) {
            blobValue[i] = srcRS.getBytes(srcClobColumns[i]);
          } else {
            clobValue[i] = NVL.getString(srcRS.getString(srcClobColumns[i]));
          }
        }
        boolean doAction = true;
        if (tmpExists != null) {
          try
          {
            destRS = destSTMTExist.executeQuery(tmpExists);
            if (destRS.next()) {
              if (tmpAlternate != null)
              {
                tmpSQL = tmpAlternate;
              }
              else
              {
                doAction = false;
                System.out.println("tmpExists = " + tmpExists);
              }
            }
            destRS.close();
          }
          catch (Exception e)
          {
            System.out.println("tmpExists=" + tmpExists);
            System.out.println("AIPTransfer.transferData():exception=" + e.getMessage());
            if (prntStream != null) {
              prntStream.println("tmpExists=" + tmpExists + "\n\t exception=" + e.getMessage());
            }
          }
        }
        if (doAction)
        {
          tmpSQL = tmpSQL.replaceAll("'null'", "null");
          tmpSQL = tmpSQL.replaceAll("''", "' '");
          try
          {
            if (batch_enabled)
            {
              destSTMT.addBatch(tmpSQL);
              batch_counter++;
              transfer_print_counter++;
              batch_tmpSQL.append(tmpSQL);
              batch_tmpSQL.append(";\n");
              if (batch_counter >= batch_length)
              {
                destSTMT.executeBatch();
                batch_counter = 0;
                batch_tmpSQL = new StringBuffer();
              }
            }
            else
            {
              destSTMT.executeUpdate(tmpSQL);
              transfer_print_counter++;
            }
          }
          catch (Exception e)
          {
            if (batch_enabled)
            {
              System.out.println("batch_tmpSQL=" + batch_tmpSQL.toString());
              if (prntStream != null) {
                prntStream.println(batch_tmpSQL.toString() + "\n\t exception=" + e.getMessage());
              }
              batch_counter = 0;
              batch_tmpSQL = new StringBuffer();
            }
            else
            {
              System.out.println("tmpSQL=" + tmpSQL);
              if (prntStream != null) {
                prntStream.println(tmpSQL + "\n\t exception=" + e.getMessage());
              }
            }
            System.out.println("AIPTransfer.transferData():exception=" + e.getMessage());
          }
          if ((srcClobColumns != null) && (srcClobColumns.length > 0) && (destdbtype == 1))
          {
            String tmpClob = null;
            try
            {
              destCN.setAutoCommit(false);
              tmpClob = clobQuery + tmpExists.substring(tmpExists.indexOf(" from")) + " FOR UPDATE";
              OracleResultSet rs = (OracleResultSet)destSTMTExist.executeQuery(tmpClob);
              if (rs.next()) {
                for (int i = 0; i < destClobColumns.length; i++) {
                  if (isBlob.booleanValue())
                  {
                    BLOB blob = rs.getBLOB(destClobColumns[i]);
                    blob.putBytes(1L, blobValue[i], blobValue[i].length);
                  }
                  else
                  {
                    CLOB clob = rs.getCLOB(destClobColumns[i]);
                    char[] chars = clobValue[i].toCharArray();
                    clob.putChars(1L, chars, chars.length);
                  }
                }
              }
              rs.close();
              destCN.commit();
              destCN.setAutoCommit(true);
              
              System.gc();
              Runtime.getRuntime().gc();
            }
            catch (Exception e)
            {
              System.out.println("tmpClob=" + tmpClob);
              if (prntStream != null) {
                prntStream.println("tmpClob=" + tmpClob + "\n\t exception=" + e.getMessage());
              }
              System.out.println("AIPTransfer.transferData():exception=" + e.getMessage());
              destCN.rollback();
            }
          }
        }
        if ((transfer_print_length > 0) && (transfer_print_counter > 0) && (transfer_print_counter % transfer_print_length == 0))
        {
          System.out.print(",");
          transfer_print_all += transfer_print_counter;
          System.out.print(transfer_print_all);
          transfer_print_counter = 0;
        }
      }
      if ((batch_enabled) && (batch_counter > 0)) {
        try
        {
          destSTMT.executeBatch();
          
          System.out.print(",");
          transfer_print_all += transfer_print_counter;
          System.out.print(transfer_print_all);
          System.out.print("\n");
        }
        catch (Exception e)
        {
          System.out.println("batch_tmpSQL=" + batch_tmpSQL.toString());
          if (prntStream != null) {
            prntStream.println(batch_tmpSQL.toString() + "\n\t exception=" + e.getMessage());
          }
          batch_counter = 0;
          batch_tmpSQL = new StringBuffer();
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("tmpSQL=" + tmpSQL);
      if (prntStream != null) {
        prntStream.println(tmpSQL + "\n\t exception=" + e.getMessage());
      }
      System.out.println("AIPTransfer.transferData():exception=" + e.getMessage());
    }
    finally
    {
      try
      {
        if (srcRS != null) {
          srcRS.close();
        }
        if (destRS != null) {
          destRS.close();
        }
        if (srcSTMT != null) {
          srcSTMT.close();
        }
        if (destSTMT != null) {
          destSTMT.close();
        }
        if (destSTMTExist != null) {
          destSTMTExist.close();
        }
        if (srcCN != null) {
          srcCN.close();
        }
        if (destCN != null) {
          destCN.close();
        }
      }
      catch (Exception e2)
      {
        System.out.println("AIPTransfer.transferData():exception=" + e2.getMessage());
      }
    }
    Date method_end = new Date();
    System.out.println("AIPTransfer.transferData():end:time=" + (method_end.getTime() - method_start.getTime()) / 1000L + " seconds.");
  }
  
  private void _syncLocationWithDW()
  {
    System.out.println("UpdatePayerIdFromDW.syncLocationWithDW():Starting...............");
    
    String srcDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    String srcURL = "jdbc:sqlserver://192.168.0.240;databaseName=NIOPDC_DC1;instanceName=sql2008";
    String srcUser = "sa";
    String srcPass = "AIP88758380aip";
    String srcQuery = "select * from location";
    String[] srcColumns = { "ID", "Code", "Caption", "F_ParentID", "LocLevel", "ParentCaption", "IsMarkazi" };
    
    String destDriver = "oracle.jdbc.driver.OracleDriver";
    String destURL = "jdbc:oracle:thin:@192.168.0.195:1521:AIP11g";
    String destUser = "sib";
    String destPass = "sib";
    String destSQL = "insert into location (ID,CODE,CAPTION,F_PARENTID,LOCLEVEL,PARENTCAPTION,ISMARKAZI) values (:ID,':CODE',':CAPTION',:F_PARENTID,:LOCLEVEL,':PARENTCAPTION',:ISMARKAZI)";
    String[] destColumns = { ":ID", ":CODE", ":CAPTION", ":F_PARENTID", ":LOCLEVEL", ":PARENTCAPTION", ":ISMARKAZI" };
    
    String ifExistsNoActionQuery = "select id from location where id=:id";
    String ifExistsNoAlternateSQL = "update location set CODE=':CODE',CAPTION=':CAPTION',F_PARENTID=:F_PARENTID,LOCLEVEL=:LOCLEVEL,PARENTCAPTION=':PARENTCAPTION',ISMARKAZI=:ISMARKAZI where id=:id";
    
    transferData(srcDriver, srcURL, srcUser, srcPass, srcQuery, srcColumns, destDriver, destURL, destUser, destPass, destSQL, destColumns, ifExistsNoActionQuery, ifExistsNoAlternateSQL);
    
    System.out.println("UpdatePayerIdFromDW.syncLocationWithDW():end.");
  }
  
  private void _syncCustomerWithDW()
  {
    System.out.println("UpdatePayerIdFromDW.syncLocationWithDW2W():Starting...............");
    
    String srcDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    String srcURL = "jdbc:sqlserver://192.168.0.240;databaseName=NIOPDC_DC1;instanceName=sql2008";
    String srcUser = "sa";
    String srcPass = "AIP88758380aip";
    String srcQuery = "select * from customer";
    String[] srcColumns = { "ID", "CODE", "CUSTOMERNAME", "F_BUYTYPEID", "F_LOCATIONID_NAHIEH", "F_CountryDivitionID", "F_CUSTOMERSTATUSID", "F_CUSTOMERTYPEID", "F_CUSTOMERMASRAFTYPEID", "PHONE", "ADDRESS", "F_PERSONID", "BANKPAYERID", "MELLATPAYERID" };
    
    String destDriver = "oracle.jdbc.driver.OracleDriver";
    String destURL = "jdbc:oracle:thin:@192.168.0.195:1521:AIP11g";
    String destUser = "sib";
    String destPass = "sib";
    String destSQL = "insert into customer (ID,CODE,CUSTOMERNAME,F_BUYTYPEID,F_LOCATIONID_NAHIEH,F_COUNTRYDIVISIONID,F_CUSTOMERSTATUSID,F_CUSTOMERTYPEID,F_CUSTOMERMASRAFTYPEID,PHONE,ADDRESS,F_PERSONID,BANKPAYERID,MELLATPAYERID) values (:ID,':CODE',':CUSTOMERNAME',:F_BUYTYPEID,:F_LOCATIONID_NAHIEH,:F_COUNTRYDIVISIONID,:F_CUSTOMERSTATUSID,:F_CUSTOMERTYPEID ,:F_CUSTOMERMASRAFTYPEID,':PHONE',':ADDRESS',:F_PERSONID,':BANKPAYERID',':MELLATPAYERID')";
    


    String[] destColumns = { ":ID", ":CODE", ":CUSTOMERNAME", ":F_BUYTYPEID", ":F_LOCATIONID_NAHIEH", ":F_COUNTRYDIVISIONID", ":F_CUSTOMERSTATUSID", ":F_CUSTOMERTYPEID", ":F_CUSTOMERMASRAFTYPEID", ":PHONE", ":ADDRESS", ":F_PERSONID", ":BANKPAYERID", ":MELLATPAYERID" };
    
    String ifExistsNoActionQuery = "select code from customer where code=':code'";
    String ifExistsNoAlternateSQL = "update customer set ID=:ID,CODE=':CODE',CUSTOMERNAME=':CUSTOMERNAME',F_BUYTYPEID=:F_BUYTYPEID,F_LOCATIONID_NAHIEH=:F_LOCATIONID_NAHIEH,F_COUNTRYDIVISIONID=:F_COUNTRYDIVISIONID,F_CUSTOMERSTATUSID=:F_CUSTOMERSTATUSID,F_CUSTOMERTYPEID=:F_CUSTOMERTYPEID,F_CUSTOMERMASRAFTYPEID=:F_CUSTOMERMASRAFTYPEID,PHONE=':PHONE',ADDRESS=':ADDRESS',F_PERSONID=:F_PERSONID,BANKPAYERID=':BANKPAYERID',MELLATPAYERID=':MELLATPAYERID' where code=':code'";
    
    transferData(srcDriver, srcURL, srcUser, srcPass, srcQuery, srcColumns, destDriver, destURL, destUser, destPass, destSQL, destColumns, ifExistsNoActionQuery, ifExistsNoAlternateSQL);
    
    System.out.println("UpdatePayerIdFromDW.syncLocationWithDW2():end.");
  }
  
  public void execute(String destDriver, String destURL, String destUser, String destPass, String destSQL, PrintStream prntStream)
  {
    Connection destCN = null;
    Statement destSTMT = null;
    try
    {
      destSQL = destSQL.toLowerCase();
      
      Class.forName(destDriver);
      destCN = DriverManager.getConnection(destURL, destUser, destPass);
      destSTMT = destCN.createStatement();
      
      destSTMT.execute(destSQL);
    }
    catch (Exception e)
    {
      System.out.println("execute=" + destSQL);
      if (prntStream != null) {
        prntStream.println("execute=" + destSQL);
      }
      e.printStackTrace();
    }
    finally
    {
      try
      {
        if (destSTMT != null) {
          destSTMT.close();
        }
        if (destCN != null) {
          destCN.close();
        }
      }
      catch (Exception e2)
      {
        e2.printStackTrace();
      }
    }
  }
  
  private void _sync_Pilot_Location_With_Warehouse_AIPReplicationServer()
  {
    System.out.println("UpdatePayerIdFromDW.syncLocationWithDW2W():Starting...............");
    
    String srcDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    String srcURL = "jdbc:sqlserver://192.168.0.240;databaseName=NIOPDC_DC1;instanceName=sql2008";
    String srcUser = "sa";
    String srcPass = "AIP88758380aip";
    String srcQuery = "select * from customer";
    String[] srcColumns = { "ID", "CODE", "CUSTOMERNAME", "F_BUYTYPEID", "F_LOCATIONID_NAHIEH", "F_CountryDivitionID", "F_CUSTOMERSTATUSID", "F_CUSTOMERTYPEID", "F_CUSTOMERMASRAFTYPEID", "PHONE", "ADDRESS", "F_PERSONID", "BANKPAYERID", "MELLATPAYERID" };
    
    String destDriver = "oracle.jdbc.driver.OracleDriver";
    String destURL = "jdbc:oracle:thin:@192.168.0.195:1521:AIP11g";
    String destUser = "sib";
    String destPass = "sib";
    String destSQL = "insert into customer (ID,CODE,CUSTOMERNAME,F_BUYTYPEID,F_LOCATIONID_NAHIEH,F_COUNTRYDIVISIONID,F_CUSTOMERSTATUSID,F_CUSTOMERTYPEID,F_CUSTOMERMASRAFTYPEID,PHONE,ADDRESS,F_PERSONID,BANKPAYERID,MELLATPAYERID) values (:ID,':CODE',':CUSTOMERNAME',:F_BUYTYPEID,:F_LOCATIONID_NAHIEH,:F_COUNTRYDIVISIONID,:F_CUSTOMERSTATUSID,:F_CUSTOMERTYPEID ,:F_CUSTOMERMASRAFTYPEID,':PHONE',':ADDRESS',:F_PERSONID,':BANKPAYERID',':MELLATPAYERID')";
    


    String[] destColumns = { ":ID", ":CODE", ":CUSTOMERNAME", ":F_BUYTYPEID", ":F_LOCATIONID_NAHIEH", ":F_COUNTRYDIVISIONID", ":F_CUSTOMERSTATUSID", ":F_CUSTOMERTYPEID", ":F_CUSTOMERMASRAFTYPEID", ":PHONE", ":ADDRESS", ":F_PERSONID", ":BANKPAYERID", ":MELLATPAYERID" };
    
    String ifExistsNoActionQuery = "select code from customer where code=':code'";
    String ifExistsNoAlternateSQL = "update customer set ID=:ID,CODE=':CODE',CUSTOMERNAME=':CUSTOMERNAME',F_BUYTYPEID=:F_BUYTYPEID,F_LOCATIONID_NAHIEH=:F_LOCATIONID_NAHIEH,F_COUNTRYDIVISIONID=:F_COUNTRYDIVISIONID,F_CUSTOMERSTATUSID=:F_CUSTOMERSTATUSID,F_CUSTOMERTYPEID=:F_CUSTOMERTYPEID,F_CUSTOMERMASRAFTYPEID=:F_CUSTOMERMASRAFTYPEID,PHONE=':PHONE',ADDRESS=':ADDRESS',F_PERSONID=:F_PERSONID,BANKPAYERID=':BANKPAYERID',MELLATPAYERID=':MELLATPAYERID' where code=':code'";
    
    transferData(srcDriver, srcURL, srcUser, srcPass, srcQuery, srcColumns, destDriver, destURL, destUser, destPass, destSQL, destColumns, ifExistsNoActionQuery, ifExistsNoAlternateSQL);
    
    System.out.println("UpdatePayerIdFromDW.syncLocationWithDW2():end.");
  }
  
  public static void main(String[] args)
  {
    System.out.println("AIPTransfer.main():start....");
    Connection cn = null;
    try
    {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      
      Properties cn_info = new Properties();
      cn_info.setProperty("user", "aiplaw");
      cn_info.setProperty("password", "aiplaw");
      cn_info.setProperty("SetBigStringTryClob", "true");
      cn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.11:1521:orcl", cn_info);
      



      PreparedStatement ps = cn.prepareStatement("insert into cmlog (id,LOGOBJECTS,F_CMUSECASEID,refid,TRANSACTIONTYPE,USERID)values (?,?,1,1,1,1)");
      
      String clob_str = "01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789==01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789==01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789==01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789==01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789==01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789==01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789==01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789==01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789==01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789=01234567890123456789012345678901234567890123456789-01234567890123456789012345678901234567890123456789==";
      
      ps.setInt(1, 1);
      ps.setString(2, clob_str);
      














      ps.executeUpdate();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (cn != null) {
        try
        {
          cn.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
    }
    System.out.println("AIPTransfer.main():end.");
  }
}
