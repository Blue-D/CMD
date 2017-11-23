package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import bean.Bean;
import bean.Template;

/**
 * 数据库工具类
 * @author 命空
 *
 */
class DaoUtil {
	/**
	 * 获取类型名
	 * @param t
	 * @return
	 */
	protected static <T> String GetTypeString(Class c){
		String[] type=c.getName().split("\\.");
		String result=BigToSmall(type[type.length-1]);
		result=(char)(result.charAt(0)-'a'+'A')+result.substring(1);
		return result;
		
	}
	
	
	/**
	 * 返回T的Class，如果T是基本数据类型的封装类，返回的是基础数据类型的class
	 * @param t
	 * @return
	 */
	protected static <T> Class getType(T t){
		try {
			Field f=t.getClass().getDeclaredField("TYPE");
			return (Class) f.get(null);
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
			return t.getClass();
		}
		
	}
	/**
	 * 将SQL中的格式转换为JAVA对象名
	 * @param type
	 * @return
	 */
	protected static String TypeNameFormat(String type){
		if(type.equals("VARCHAR") || type.equals("CHAR") || type.equals("LONGVARCHAR") ){
			return "String";
		}else if(type.equals("BIT") || type.equals("BINARY")){
			return "Byte";
		}else{
			type=type.substring(0,1)+type.toLowerCase().substring(1);
		}
		return type;
	}
	/**
	 * 类型值转类型名
	 * @param typenum
	 * @return
	 */
	protected  static String TypesToType(int typenum){
		Field[] fields=Types.class.getDeclaredFields();
		for(Field f:fields){
			try {
				if(f.get(null).equals(typenum)){
					return f.getName();
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 条件拼装
	 * @param map
	 * @return
	 */
	protected static String ConditionInstall(Map<String,String> map){
		if(map==null) return "1";
 		String condition="";
		for(String name:map.keySet()){
			condition+= name+" = '"+map.get(name)+"';";
		}
		condition=condition.substring(0,condition.length()-1);
		return condition;
	}
	
	/**
	 * 通过队列的方式拼装条件
	 * 队列顺序为 （名称 运算符 条件）
	 * @param que
	 * @return
	 */
	protected static String ConditionInstall(Queue<String> que){
		if(que==null) return "1";
		String condition="";
		int i=1;
		while(que.size()>0){
			String now=que.poll();
			if(i%4==3) condition+=" '"+now+"'";
			else{
				condition+=" "+now;
			}
			i++;
		}
		return condition;
	}
	
	protected static String BigToSmall(String A){
		String result="";
		for(int i=0;i<=A.length()-1;i++){
			if(A.charAt(i)>='a' && A.charAt(i)<='z'){
				result+=A.charAt(i);
			}else{
				result+=(char)(A.charAt(i)+'a'-'A');
			}
		}
		return result;
	}
	

	public static String CreateGetOSet(String opr,String str){
		if(str.charAt(0)>='A' && str.charAt(0)<='Z'){
			return opr+str;
		}else{
			return opr+(char)(str.charAt(0)-'a'+'A')+str.substring(1);
		}
		
	}


	public static Class GetType(Class c,String fliedName) throws NoSuchFieldException, SecurityException{
		return c.getDeclaredField(fliedName).getType();
	}
	
	public static String BigToSmalle(String str){
		String result="";
		for(char c:str.toCharArray()){
			if(c>='A' && c<='Z'){
				result+=(char)(c-'A'+'a');
			}else{
				result+=c;
			}
		}
		return result;
		
	}
	
}
