package aip.generator;

import java.io.File;
import java.io.IOException;

import aip.util.AIPException;
import aip.util.AIPUtil;

public class AIPGenUtil {
	public static void addMethod2Class(String classFile,String methodWithArgs,String methodContent) throws IOException, AIPException{
		addMethod2Class(classFile, methodWithArgs, methodContent, "public",new String[]{"AIPException"},new String[]{"Exception"});
	}
	public static void addMethod2Class(String classFile,String methodWithArgs,String methodContent,String modifier,String[] throwsException,String[] handleExceptions) throws IOException, AIPException{
		File f = new File(classFile);
		String content = AIPUtil.readFile(f.getPath());
		
		if(content.indexOf(methodWithArgs)<0){
			int pos_class_end = content.lastIndexOf("}");
			if(pos_class_end>0){
				StringBuffer sb = new StringBuffer(content.substring(0, pos_class_end));
				sb.append("\n\t");
				sb.append(modifier);
				sb.append(" ");
				sb.append(methodWithArgs);
				if(throwsException.length>0){
					sb.append("throws ");
					for (int i = 0; i < throwsException.length; i++) {
						sb.append(throwsException[i]);
						if(i<throwsException.length-1)sb.append(",");
					}
				}
				sb.append("{");
				String contentNewLineAndTabs = "\n\t\t"; 
				sb.append(contentNewLineAndTabs);
				if(handleExceptions.length>0){
					sb.append("try{");
					contentNewLineAndTabs = "\n\t\t\t";
					sb.append(contentNewLineAndTabs);
				}
				
				sb.append(methodContent.replaceAll("\n", contentNewLineAndTabs));
				
				contentNewLineAndTabs="\n\t\t";
				
				if(handleExceptions.length>0){
					sb.append(contentNewLineAndTabs);
					sb.append("}");
					for (int i = 0; i < handleExceptions.length; i++) {
						sb.append("catch("+handleExceptions[i]+" ex){");
						sb.append(contentNewLineAndTabs+"\t");
						sb.append("ex.printStackTrace();");
						sb.append(contentNewLineAndTabs+"\t");
						sb.append("throw new "+throwsException[0]+"(ex);");
						sb.append(contentNewLineAndTabs);
						sb.append("}");
					}
				}

				sb.append("\n\t}\n\n");
				sb.append(content.substring(pos_class_end));
				
				AIPUtil.writeFile(f.getPath(), sb.toString());
			}else{
				throw new AIPException("فایل "+f.getPath()+" } را ندارد");
			}
		}else{
			throw new AIPException("فایل "+f.getPath()+" متد "+methodWithArgs+" را دارد!");
		}

	}

}
