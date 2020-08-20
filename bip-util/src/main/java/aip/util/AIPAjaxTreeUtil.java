package aip.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class AIPAjaxTreeUtil {
	public static final String MODE_RADIO = "radio";
	public static final String MODE_CHECKBOX = "checkbox";
	
//	public static void main(String args[]){
//		RegionParam param = new RegionParam();
//		param.setUserId(1.2345);
//		RegionLST regionLST  = getDAO().getRegions(param);
//		createTree("WebRoot/cache/product.tree","مناطق",regionLST.getRegionDTOs(),"Caption", MODE_CHECKBOX,"Locations");
//	}
	
	public static void createTree(String fileName,String rootTitle, List ar, String valueField, String mode, String subchildsFiled){
		BufferedWriter out = null;
		try {
			FileOutputStream fw = new FileOutputStream(fileName);
			OutputStreamWriter osw = new OutputStreamWriter(fw,"utf8");
			out = new BufferedWriter(osw);		
	
			out.write("");
			out.write("<ul>");
			out.newLine();// root
			out.write("<li class='root'>");// root
			out.write(rootTitle);
			out.newLine();
			
			String nodeID = "";
			recursiveTree(out, ar, nodeID, valueField, mode, subchildsFiled);
			
			out.newLine();
			out.write("</li>");out.newLine();//root
			out.write("</ul>");out.newLine();//root
			
		}catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		} catch (SecurityException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
		}finally{
			if(out!=null)
				try {
					out.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
		}	
	}
	
	private static void recursiveTree(BufferedWriter out, List childs, String nodeID, String valueField, String mode, String subchildsFiled) throws IOException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		for (int i = 0; i < childs.size(); i++) {
			out.write("<ul>");//level
			
			Object odto = childs.get(i);
			String value = ""; 
			String locationId = ""; 
			String locationCode = ""; 
			String text = "";
			
			Object v = odto.getClass().getMethod("get"+valueField,null ).invoke(odto, null);
			if (v != null)
				value = v.toString();
			Object dbId = odto.getClass().getMethod("getId",null ).invoke(odto, null);
			if (dbId != null)
				locationId = dbId.toString();
			Object dbCode = odto.getClass().getMethod("getCode",null ).invoke(odto, null);
			if (dbCode != null)
				locationCode = dbCode.toString();

			String nodeIDChild = "";
			if (nodeID == null || "".equals(nodeID)) {
				nodeIDChild = String.valueOf(i);
				out.write("<li>");
				out.write("<span class='text'>");
				out.write( " "+ value +" ");
				/** content block end */
				out.write("</span>");
			} else {
				out.write("<li>");
				out.write("<span class='text' id='");
				out.write(locationId);
				out.write("'>");
				
				/** content block start */
				out.write(" <input type='" + mode + "' name='locationsCheck' value='");
				out.write(locationCode);
				out.write("' id='");
				out.write(locationId);
				out.write("' title=' ");
				out.write(locationCode);
				out.write(" ' /> ");
				out.write(value);
				/** content block end */
				out.write("</span>");
			}
			
			if(subchildsFiled != null ) {
				ArrayList subchilds = null;
				Object s = odto.getClass().getMethod("get"+subchildsFiled,null ).invoke(odto, null);
				if(s != null)
					subchilds = (ArrayList) s;
				if ( subchilds != null && subchilds.size() > 0)
				{
					String a = null;	
					recursiveTree(out, subchilds, nodeIDChild, valueField, mode, a );
				}
			}
			out.write("</li>");
			
			out.write("</ul>");//level
		}
	}

	public static void createTree(String fileName,String rootTitle, List ar, String valueField, String subchildsFiled, TreeTextInterface tti){
		BufferedWriter out = null;
		try{
			FileOutputStream fw = new FileOutputStream(fileName);
			OutputStreamWriter osw = new OutputStreamWriter(fw,"utf8");
			out = new BufferedWriter(osw);		
	
			out.write("");
			out.write("<ul>");out.newLine();//root
			out.write("<li class='root'>");//root
			out.write(rootTitle);out.newLine();
			
			String nodeID = "";
			recursiveTree(out, ar, nodeID,valueField,subchildsFiled, tti);
			//out.write("<li><span class='text'>Tree Node Ajax 1</span></li>");
			
			out.newLine();
			out.write("</li>");out.newLine();//root
			out.write("</ul>");out.newLine();//root
			
		}catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		} catch (SecurityException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
		}finally{
			if(out!=null)
				try {
					out.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
		}	
	}

	private static void recursiveTree(BufferedWriter out, List childs, String nodeID, String valueField, String subchildsFiled, TreeTextInterface tti) throws IOException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		for (int i = 0; i < childs.size(); i++) {
			out.write("<ul>");//level
			
			Object odto = childs.get(i);
			String value = "";  //odto.getOldId()
			String text = "";//odto.getCaption()
			
			Object v = odto.getClass().getMethod("get"+valueField,null ).invoke(odto, null);
			if(v !=null)
				value = v.toString();
			
			/*if(mode.equalsIgnoreCase("lawView")){
				Object t = odto.getClass().getMethod("getSectionTypeName",null ).invoke(odto, null);
				Object u = odto.getClass().getMethod("getSectionTypeNo", null).invoke(odto, null);
				if(t != null && u != null)
					text = t.toString() + " " + u.toString();
			}else if(mode.equalsIgnoreCase("subject") || mode.equalsIgnoreCase("organization")){
				Object t = odto.getClass().getMethod("getCaption",null ).invoke(odto, null);
				if(t != null)
					text = t.toString();
			}*/
			
			
			String nodeIDChild = "";
			if (nodeID == null || "".equals(nodeID)) {
				nodeIDChild = String.valueOf(i);
			} else {
				nodeIDChild = String.valueOf(nodeID + "-" + i);
			}
			
			out.write("<li>");
			out.write("<span class='text'>");

			/** content block start */
			
			out.write(tti.getText(odto, nodeIDChild));

			/** content block end */
			
			out.write("</span>");

			if(subchildsFiled != null ) {
				ArrayList subchilds = null;//odto.getOrganizationDTOs()
				Object s = odto.getClass().getMethod("get"+subchildsFiled,null ).invoke(odto, null);
				if(s != null)
					subchilds = (ArrayList) s;
				
				if ( subchilds != null && subchilds.size() > 0)
					recursiveTree(out, subchilds, nodeIDChild, valueField, subchildsFiled, tti);
			}
			out.write("</li>");
			
			out.write("</ul>");//level
		}
	}

//	private static AgentInterface getDAO() {
//		return SellDAOManager.getAgent();
//	}

	public static void create(String fileName,String rootTitle, List ar, String valueField, String mode) {
		BufferedWriter out = null;
		try {
			FileOutputStream fw = new FileOutputStream(fileName);
			OutputStreamWriter osw = new OutputStreamWriter(fw,"utf8");
			out = new BufferedWriter(osw);		

			out.write("");
			out.write("<ul>");
			out.newLine();// root
			out.write("<li class='root'>");// root
			out.write(rootTitle);
			out.newLine();

			String nodeID = "";
			
			out.newLine();
			out.write("</li>");out.newLine();//root
			out.write("</ul>");out.newLine();//root
			
		}catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}finally{
			if(out!=null)
				try {
					out.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
		}	
	}
	
}






