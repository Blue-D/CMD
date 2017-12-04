package servlet;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.student;
import bean.teammember;
import dao.BaseDao;
import dao.DaoUtil;

public class StudentUpdateServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//1.获取原学号与新学号
		String oldSNO=req.getParameter("oldsno");
		String newSNO=req.getParameter("newsno");
		//2.动态获取请求修改的信息，并且动态赋值
		student stu=new student();
		teammember tm=new teammember();
		Class stuclass=student.class;
		Field[] fields=stuclass.getDeclaredFields();
		String tno="";
		for(Field f:fields){
			String name=f.getName();
			String value=req.getParameter(name);
			String Set=DaoUtil.CreateGetOSet("set", name);
			if(name.equals("tno")){
				tm.setTno(value);
				tno=value;
				continue;
			}
			if(value==null || value.equals("")){
				continue;
			}
			try {
				Method m=stuclass.getMethod(Set, String.class);
				m.invoke(stu, value);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		boolean flag=false;
		if(newSNO==null || newSNO.equals("")){
			stu.setSno(oldSNO);
			List<Object> objs=new ArrayList<>();
			objs.add(stu);
			flag=BaseDao.Insertcommit(objs);
		}else{
			stu.setSno(newSNO);
			List<Object> objs=new ArrayList<>();
			objs.add(stu);
			int i=BaseDao.Update(stu, new String[]{"SNO='"+oldSNO+"'"});
			if(i==1){
				flag=true;
			}
		}
		if(flag){
			resp.getOutputStream().write("yes".getBytes());	
		}else{
			resp.getOutputStream().write("no".getBytes());
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
