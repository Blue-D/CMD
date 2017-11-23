package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
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
 * 数据库操作类
 * @author 命空
 *
 */
public class MysqlDao {
	/**
	 * 删除指定数据
	 * 
	 * @param tablename
	 * @param dataname
	 * @param data
	 * @return
	 */
	public static boolean delete(String tablename, Map<String,String> condition) {
		try {
			dao.BaseDao.delete(BaseDao.getConn(),tablename,condition);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * 向数据库中插入数据，传入数据以累的方式储存
	 * 
	 * @param tablename
	 * @param t
	 * @return
	 */
	public static <T extends Bean> boolean insert(String tablename, T t) {
		Map map = BeanToMap(t);
		return BaseDao.insert(tablename, map);
	}

	/**
	 * 向数据库中插入数据，传入数据以映射的方式储存
	 * 
	 * @param tablename
	 * @param name_data
	 * @return
	 */
	public static boolean insert(String tablename, Map<String, Object> name_data) {
		return BaseDao.insert(tablename, name_data);
	}

	/**
	 * 这个方法是将用对象换成用Map储存，储存数据的对象必须继承Bean方法
	 * 
	 * @param t
	 * @return
	 */
	protected static <T extends Bean> Map<String, String> BeanToMap(T t) {
		// 1.获取t的class
		Class<T> c = (Class<T>) t.getClass();
		Map<String, String> map = new HashMap<String, String>();
		// 2.获取这个Bean的的字段组
		Field[] field = c.getDeclaredFields();
		// 3.生成Map
		for (Field f : field) {
			String name =(char)(f.getName().charAt(0)+'A'-'a')+f.getName().substring(1);
			try {
				Method m = c.getMethod("get" + name);
				map.put(name, (String) m.invoke(t));
			} catch (IllegalAccessException e) {
				System.out.println("非法访问方法" + "get" + name);
			} catch (NoSuchMethodException e) {
				System.out.println("get" + name + "不存在");
			} catch (SecurityException e) {
			} catch (IllegalArgumentException e) {
				System.out.println("参数不合法");
			} catch (InvocationTargetException e) {
				System.out.println("调用方法抛出异常");
			}
		}
		return map;
	}

	/**
	 * 将Map储存的数据换成以对象储存。传进来的数据必须是Bean的子类
	 * 
	 * @param t
	 * @param map
	 * @return
	 */
	protected static <T extends Bean> T MapToBean(T t, Map<String, String> map) {
		Class<T> c = (Class<T>) t.getClass();
		for (String name : map.keySet()) {
			try {
				Method m = c.getMethod("set" + name, String.class);
				m.invoke(t, map.get(name));
			} catch (NoSuchMethodException | SecurityException e) {
				System.out.println("方法不存在或非法访问方法:set" + name);
			} catch (IllegalAccessException e) {
				System.out.println("没有访问权限");
			} catch (IllegalArgumentException e) {
				System.out.println("set" + name + "方法参数参数错误");
			} catch (InvocationTargetException e) {
				System.out.println("调用方法抛出异常");
			}
		}
		return (T) t.clone();
	}

	/**
	 * 获取表中所有的列名与私有键(类型为SQL中的类型)
	 * 
	 * @param TableName
	 * @return
	 */
	public static Map<String, String> GetTableAllColAndKey(String TableName) {
		Map<String, String> col = BaseDao.GetTableAllCol(TableName);
		Map<String, String> PriKey = BaseDao.GetTablePrimaryKeys(TableName);
		Map<String, String> map = new HashMap<>();
		map.putAll(col);
		map.putAll(PriKey);
		return map;
	}

	/**
	 * 获取表中所有的列名与私有键(类型为JAVA中的类名)
	 * 
	 * @param TableName
	 * @return
	 */
	public static Map<String, String> GetTableFormatAllColAndKey(String TableName) {
		Map<String, String> col = BaseDao.GetTableAllCol(TableName);
		Map<String, String> PriKey = BaseDao.GetTablePrimaryKeys(TableName);
		Map<String, String> map = new HashMap<>();
		map.putAll(col);
		map.putAll(PriKey);
		for (String name : map.keySet()) {
			map.replace(name, DaoUtil.TypeNameFormat(map.get(name)));
		}
		return map;
	}

	/**
	 * 根据表名创建对应的bean类
	 * 
	 * @param TableName
	 */
	public static void CreateBeanByTable(String TableName) {
		// 1.获得模板类位置
		URL url = Template.class.getResource("Template.class");
		String path = url.getPath();
		try {
			path =URLDecoder.decode(path, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return ;
		}
		path = path.replace("build", "src");
		path = path.replace("/classes", "");
		path = path.replace("class", "java");
		// 2.打开模板类，读取模板类中模板信息
		File file = new File(path);
		String context = "";
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			String buf = "";
			while ((buf = br.readLine()) != null) {
				context += buf + "\r\n";
			}
			context = context.substring(0, context.length() - 1);
			// 3.新建目标表的bean类文件
			context = context.replace("Template", TableName);
			path = path.replace("Template", TableName);
			File newfile = new File(path);
			if (newfile.exists() == false) {
				newfile.createNewFile();
			} else {
				newfile.delete();
				newfile.createNewFile();
			}
			context = context.substring(0, context.lastIndexOf("}"));
			// 4.获取表的列信息
			Map<String, String> colmap = GetTableFormatAllColAndKey(TableName);
			// 5.为类生成字段
			for (String name : colmap.keySet()) {
				String ziduan = "private " +colmap.get(name) + " " +  DaoUtil.BigToSmall(name) + ";\r\n";
				context += ziduan;
			}
			context += "\r\n";
			// 6.为字段生成对应GET/SET方法
			for (String name : colmap.keySet()) {
				String GetMethod = "public " + colmap.get(name) + " get" + (char)(DaoUtil.BigToSmall(name).charAt(0)+('A'-'a'))+DaoUtil.BigToSmall(name).substring(1) + "(){ return " + DaoUtil.BigToSmall(name) + ";}\r\n";
				String SetMethod = "public void set" + (char)(DaoUtil.BigToSmall(name).charAt(0)+('A'-'a'))+DaoUtil.BigToSmall(name).substring(1)  + "(" + colmap.get(name) + " x){" + DaoUtil.BigToSmall(name) + "=x;}\r\n";
				context += GetMethod + SetMethod;
			}
			//7.为对应方法产生一个空的构造方法
			context+="\r\npublic "+TableName+"(){}\r\n\r\n";
			context += "}";
			// 7.将生成的信息写入类
			FileWriter fw = new FileWriter(newfile);
			fw.write(context);
			fw.flush();
			// 8.关闭流
			fr.close();
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 反射获取一个类中的所有字段名
	 * @param objclass
	 * @return
	 */
	protected static String[] getAllColName(Class objclass){
		Field[]  f=objclass.getDeclaredFields();
		String[] datanames=new String[f.length];
		for(int i=0;i<=f.length-1;i++) {
			datanames[i]=f[i].getName();
		}
		return datanames;
	}
	
	/**
	 * 将字段名组装成SQL中的字段部分
	 * @param datanames
	 * @return
	 */
	private static String DataNameInstall(String[] datanames){
		String dataname="";
		for(String str:datanames){
			dataname+=str+",";
		}
		return dataname.substring(0, dataname.length() - 1);
	}
	
}
