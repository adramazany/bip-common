package aip.util;


import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Date;
import java.util.Calendar;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;

import javax.servlet.http.HttpServletRequest;

public class AIPLogRequest {

  public static boolean logEnabled=false;
  public static boolean logExceptionEnabled=false;
  
  public static boolean isLogParamAndAttributes = true;

  static Hashtable resource;
    static StringWriter sw=new StringWriter();

    static PrintWriter pwException;
    static {
      newExceptionLog();
    }

    static Stack stk=new Stack();
    static {
        sw.write("#LogTime \tType \tLevel \tClass.Method \tPassTime \n");
        
        //Transaction t = new Transaction();
        
        
    }
    //static  timeCalc
//    public AIPLog() {
//        super();
//    }
    public static void log(Object caller,String msg){
      if (logEnabled) {
        String client = "";
        if (caller != null && caller instanceof UnicastRemoteObject) {
          try {
            client = ( (UnicastRemoteObject) caller).getClientHost();
          }
          catch (java.rmi.server.ServerNotActiveException ex) {
            //log(null,msg);
            //ex.printStackTrace();
          }
        }
        sw.write(client + "-" + getTime() + "\t" + msg + "\n");
      }
    }
    public static void logException(Exception ex){
      if(logExceptionEnabled){
        pwException.println(
            "\n000000000000000000000000000000000000000000000000000000000000\n"
            + getTime() + ex.getMessage() + "\n");
        ex.printStackTrace(pwException);
      }
    }
    private static void newExceptionLog(){
//      try{
//        FileOutputStream out = new FileOutputStream("AIPLettersExceptions_" +
//            getDate() + getTime().replaceAll(":","-") + ".log");
//        BufferedOutputStream bufOut=new BufferedOutputStream(out);
//        pwException=new PrintWriter(bufOut);
//      }catch(Exception ex){
//        ex.printStackTrace();
//      }
      //psException=new pri;

    }
    public static void saveException(){
      pwException.flush();
      pwException.close();
      newExceptionLog();

    }
    public static void entering(Object caller,String sourceClass, String sourceMethod) {
//        if(sourceClass.startsWith("ImageMgr") && !sourceMethod.startsWith("getPrintBackgroundImage"))return;
        Date startDate=new Date();
        stk.push(startDate);
        log(caller,"ENTRY\t"+String.valueOf(stk.size()-1)+"\t"+sourceClass+"."+sourceMethod);
    }
    public static void exiting(Object caller,String sourceClass, String sourceMethod) {
//        if(sourceClass.startsWith("ImageMgr"))return;
        Date startDate=(Date) stk.pop();
        Date endDate=new Date();
        long passTimeValue=endDate.getTime()-startDate.getTime();
        log(caller,"EXIT \t"+String.valueOf(stk.size())+"\t"+sourceClass+"."+sourceMethod+"\t "+String.valueOf(passTimeValue)+"");
    }
    public static void exiting(Object caller,String sourceClass, String sourceMethod,Exception ex) {
        Date startDate=(Date) stk.pop();
        Date endDate=new Date();
        long passTimeValue=endDate.getTime()-startDate.getTime();
        log(caller,"EXIT \t"+String.valueOf(stk.size())+"\t!!!"+sourceClass+"."+sourceMethod+"!!!("+ex.getMessage()+")\t "+String.valueOf(passTimeValue)+"");
        logException(ex);
    }
    public static void internal(Object caller,String sourceClass, String sourceMethod,String msg) {
        Date date=new Date();
        log(caller,"\t\t\t\t"+sourceClass+"."+sourceMethod+"("+msg+")");
    }
    public static void exception(Object caller,String sourceClass, String sourceMethod,Exception ex) {
        Date date=new Date();
        internal(caller,"!!!"+sourceClass,sourceMethod+"!!!",ex.getMessage());
        logException(ex);
    }
    public static void save(String prefix){
    	if(logEnabled){
        Date d=new Date();
        try {
            FileOutputStream out = new FileOutputStream(prefix+"_Law_" + getDate()+getTime().replaceAll(":","-") +".log");
            //FileOutputStream out = new FileOutputStream("Raja.log");
            OutputStreamWriter osw=new OutputStreamWriter(out);
            osw.write(sw.toString());

            sw.flush();
            sw.close();
            
            osw.close();
            
            sw=new StringWriter();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    	}
    }
    public static String getTime(){
      Date d=new Date();
      Calendar c=Calendar.getInstance();
//      d.format("hh:mm:ss")
      String res="";
      res+=String.valueOf( c.get(c.HOUR_OF_DAY) );
      res+=":"+String.valueOf( c.get(c.MINUTE) );
      res+=":"+String.valueOf( c.get(c.SECOND) );
      res+="."+String.valueOf( c.get(c.MILLISECOND) );
      res+=" ";
      return res;
    }
    public static String getDate(){
      Date d=new Date();
      Calendar c=Calendar.getInstance();
//      d.format("hh:mm:ss")
      String res="";
      res+=String.valueOf( c.get(c.YEAR) );
      res+="-"+String.valueOf( c.get(c.MONTH) );
      res+="-"+String.valueOf( c.get(c.DAY_OF_YEAR) );
      res+=" ";
      return res;
    }
    
    
    static Hashtable htSession=new Hashtable();
    public static void log(HttpServletRequest request,Date date){
    	
    }
    static SimpleDateFormat _format = new SimpleDateFormat("hh:mm:ss");

    public static void start(HttpServletRequest request){
    	
    }
    public static void start2(HttpServletRequest request){
    	try {
    		Date date = new Date();
        	String st = request.getRemoteHost()
        		+"-"+request.getRemoteUser()
        		+"-"+request.getRequestURI()
        		+"?"+request.getQueryString();
        	htSession.put(st, date);
        	System.out.print(">"+_format.format(date)+"-"+st);
        	if(isLogParamAndAttributes){
        		System.out.print("-parameters:");
	        	for (Enumeration iter = request.getParameterNames();iter.hasMoreElements();) {
					String name=(String) iter.nextElement();
	        		String value=request.getParameter(name);
	        		System.out.print(name+"="+value+"&");
				}
        	}
        	System.out.println();
		} catch (Exception e) {
			exception(request, e);
		}
    }
    
    public static void end(HttpServletRequest request){
    	
    }
    public static void end2(HttpServletRequest request){
    	try {
    		Date date = new Date();
        	String st = request.getRemoteHost()
	    		+"-"+request.getRemoteUser()
	    		+"-"+request.getRequestURI()
	    		+"?"+request.getQueryString();
        	Date startDate =(Date) htSession.get(st);
        	htSession.remove(st);
        	System.out.print("<"+(date.getTime() - startDate.getTime())+"-"+_format.format(date)+"-"+st);
        	if(isLogParamAndAttributes){
        		System.out.print("-attributes:");
	        	for (Enumeration iter = request.getAttributeNames();iter.hasMoreElements();) {
					String name=(String) iter.nextElement();
	        		String value="";//request.getParameter(name);
	        		System.out.print(name+"="+value+"&");
				}
        	}
        	System.out.println();
		} catch (Exception e) {
			exception(request, e);
		}
    }
    public static void exception(HttpServletRequest request,Exception ex){
    	String st = "";
    	try {
			Date date = new Date();
	    	st = request.getRemoteHost()
				+"-"+request.getRemoteUser()
				+"-"+request.getRequestURI()
				+"?"+request.getQueryString();
	    	System.out.println("!!!"+_format.format(date)+"-"+st+"-"+ex.getMessage());
	    	//ex.printStackTrace();
		} catch (Exception e) {
	    	e.printStackTrace();
		}
    }
    
}
