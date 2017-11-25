package handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dao.BaseDao;
import dao.DaoUtil;
import net.sf.json.JSONObject;

public class ExamingHandler {
	
	public static int Exam(String table,String pk_value,String ispassed,String reason){
		int x=0;
		try {
			if(pk_value==null) return 0;
			Class c=Class.forName("bean."+table);
			Object o=c.newInstance();
			Method m=c.getMethod("GetPrivateKey");
			String[] pk_name=m.invoke(o).toString().split(";");
			String[] pk_values=pk_value.split(";");
			String ct="";
			for(int i=0;i<=pk_name.length-1;i++){
				ct+=pk_name[i]+"='"+pk_values[i]+"' and ";
			}
			ct=ct.substring(0,ct.length()-5);
			String SQL="";
			if(ispassed.equals("1")){
				SQL="UPDATE "+table+" SET IsPassed=1 Where "+ct;
				System.out.println(SQL);
			}else{
				SQL="UPDATE "+table+" SET reason='"+reason+"' Where "+ct;
				System.out.println(SQL);
			}
			x=BaseDao.Excute(SQL);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return x;
	}
	public static void main(String[] args) {
		Exam("teammember", "20171001 ;1562810210", "0", "此人太强了");
	}
	
}
