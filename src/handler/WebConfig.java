package handler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import dao.BaseDao;

public class WebConfig {
	private static String FilePath;
	
	static {
		Properties pro = new Properties();
		try {
			InputStream in = BaseDao.class.getResourceAsStream("Dao.properties");
			pro.load(in);
			FilePath = pro.getProperty("FilePath");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static String GetFilePath(){
		return FilePath;
	}
	
}
