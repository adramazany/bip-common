package bip.etl;

import aip.util.NVL;
import java.io.PrintStream;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProcessETLInfo
  implements ProcessETLListener
{
  public static final int STAT_NONE = 0;
  public static final int STAT_RUNNING = 1;
  public static final int STAT_FINISHED = 2;
  public static final int STAT_ERROR = 4;
  Lock etlLock = new ReentrantLock();
  ProcessParam processParam;
  int status;
  int totalcount = -1;
  int counter;
  Exception exception;
  String exceptionMessage;
  String query;
  StringBuffer exceptionFull = new StringBuffer();
  StringBuffer queryFull = new StringBuffer();
  boolean isProcessed = false;
  String exceptionSQL = "";
  boolean fullProcess;
  Date startTime;
  int errorCounter;
  
  public String getInfoJSONFull()
  {
    if (!NVL.isEmpty(this.query)) {
      this.queryFull.append(this.query);
    }
    if (!NVL.isEmpty(this.exceptionMessage)) {
      this.exceptionFull.append(this.exceptionMessage);
    }
    String json = "{query:'" + this.queryFull.toString() + "'" + ",progress:" + getProgress() + ",exception:'" + this.exceptionFull.toString() + "'" + ",counter:" + getCounter() + ",totalcount:" + getTotalcount() + ",exceptionSQL:'" + this.exceptionSQL + "'" + ",status:" + getStatus() + ",remaintime:'" + estimateRemainTime(new Date()) + "'" + ",errorcounter:" + getErrorCounter() + "}";
    








    this.query = "";
    this.exceptionMessage = "";
    return json;
  }
  
  public String getInfoJSON()
  {
    if (!NVL.isEmpty(this.query)) {
      this.queryFull.append(this.query);
    }
    if (!NVL.isEmpty(this.exceptionMessage)) {
      this.exceptionFull.append(this.exceptionMessage);
    }
    String json = "{query:'" + this.query + "'" + ",progress:" + getProgress() + ",exception:'" + this.exceptionMessage + "'" + ",counter:" + getCounter() + ",totalcount:" + getTotalcount() + ",exceptionSQL:'" + this.exceptionSQL + "'" + ",status:" + getStatus() + ",remaintime:'" + estimateRemainTime(new Date()) + "'" + ",errorcounter:" + getErrorCounter() + "}";
    









    this.query = "";
    this.exceptionMessage = "";
    
    return json;
  }
  
  public int getProgress()
  {
    int progress = 0;
    if (this.totalcount == 0) {
      progress = 90 + (this.isProcessed ? 10 : 0);
    } else {
      progress = (int)(this.counter / this.totalcount * 90.0D) + (this.isProcessed ? 10 : 0);
    }
    return progress;
  }
  
  Lock getEtlLock()
  {
    return this.etlLock;
  }
  
  public Exception getException()
  {
    return this.exception;
  }
  
  public void setException(Exception exception)
  {
    this.exception = exception;
    this.exceptionMessage = (exception != null ? NVL.getString(exception.getMessage()) : "").replaceAll("'", "&#039;").replaceAll("\n", "<br/>");
  }
  
  public int getTotalcount()
  {
    return this.totalcount;
  }
  
  public void setTotalcount(int totalcount)
  {
    this.totalcount = totalcount;
  }
  
  public int getCounter()
  {
    return this.counter;
  }
  
  public void setCounter(int counter)
  {
    this.counter = counter;
  }
  
  public String getQuery()
  {
    return this.query;
  }
  
  public void setQuery(String query)
  {
    this.query = NVL.getString(query).replaceAll("'", "&#039;").replaceAll("\n", "<br/>");
  }
  
  public ProcessParam getProcessParam()
  {
    return this.processParam;
  }
  
  public void setProcessParam(ProcessParam processParam)
  {
    this.processParam = processParam;
  }
  
  public int getStatus()
  {
    return this.status;
  }
  
  public void setStatus(int status)
  {
    this.status = status;
  }
  
  public boolean getIsProcessed()
  {
    return this.isProcessed;
  }
  
  public void setIsProcessed(boolean isProcessed)
  {
    this.isProcessed = isProcessed;
  }
  
  public String getExceptionSQL()
  {
    return this.exceptionSQL;
  }
  
  public void setExceptionSQL(String exceptionSQL)
  {
    this.exceptionSQL = NVL.getString(exceptionSQL).replaceAll("'", "&#039;").replaceAll("\n", "<br/>");
  }
  
  public void reset()
  {
    System.out.println("ProcessETLInfo.reset():start..................................");
    this.exceptionMessage = "";
    this.query = "";
    this.status = 0;
    this.isProcessed = false;
    this.exceptionSQL = "";
    
    this.totalcount = -1;
    this.counter = 0;
    
    this.exception = null;
    
    this.exceptionFull = new StringBuffer();
    this.queryFull = new StringBuffer();
    
    this.processParam = null;
    
    this.fullProcess = false;
    
    this.startTime = null;
    
    this.errorCounter = 0;
    
    System.out.println("ProcessETLInfo.reset():end..................................");
  }
  
  public boolean isFullProcess()
  {
    return this.fullProcess;
  }
  
  public void setFullProcess(boolean fullProcess)
  {
    this.fullProcess = fullProcess;
  }
  
  public void setProcessStartTime(Date startTime)
  {
    this.startTime = startTime;
  }
  
  public int estimateRemainSecond(Date curTime)
  {
    if (this.startTime == null) {
      return -1;
    }
    long passedTime = curTime.getTime() - this.startTime.getTime();
    
    int _counter = this.counter == 0 ? 1 : this.counter;
    long remainMilliSecond = (this.totalcount - this.counter) * passedTime / _counter;
    
    return (int)(remainMilliSecond / 1000L);
  }
  
  public String estimateRemainTime(Date curTime)
  {
    int remainSecond = estimateRemainSecond(curTime);
    int hour = remainSecond / 3600;
    remainSecond %= 3600;
    int minute = remainSecond / 60;
    int second = remainSecond % 60;
    
    return hour + ":" + minute + ":" + second;
  }
  
  public int getErrorCounter()
  {
    return this.errorCounter;
  }
  
  public void setErrorCounter(int errorCounter)
  {
    this.errorCounter = errorCounter;
  }
}
