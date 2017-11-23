package test;

import java.io.File;
import java.util.List;

import dao.BaseDao;
import dao.MysqlDao;

public class CreateBean {
	public static void main(String[] args) {
		List<String> tables=BaseDao.GetAllTables();
		if(tables!=null){
			for(String tablename:tables){
				MysqlDao.CreateBeanByTable(tablename);
				util.Util.Comliper(new File("src/bean/"+tablename+".java"));
			}
		}
	}
}
