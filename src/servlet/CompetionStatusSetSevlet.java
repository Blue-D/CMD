package servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exception.CompetitionNotExistedException;
import handler.CompetitionHander;
import net.sf.json.JSONObject;

public class CompetionStatusSetSevlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//1.获取请求头
		String opr=req.getParameter("Opr");
		String CNA=req.getParameter("CNA");
		System.out.println(opr);
		//2.拼装调用方法
		String MethodName=opr+"Competition";
		Method m=null;
		JSONObject jo=new JSONObject();
		try {
			m=CompetitionHander.class.getMethod(MethodName, String.class);
		} catch (NoSuchMethodException | SecurityException e) {
			jo.put("Sucessed", 0);
			jo.put("reason", "操作指令错误");
		}
		try {
			boolean b=(boolean) m.invoke(null, CNA);
			if(b){
				jo.put("Sucessed", 1);
			}else{
				jo.put("Sucessed", 0);
				jo.put("reason", "操作失败，请重试");
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Throwable cause=e.getCause();
			jo.put("Sucessed", 0);
			if( cause instanceof CompetitionNotExistedException){
				jo.put("reason", "该比赛不存在");
			}
		}
		resp.getOutputStream().write(jo.toString().getBytes());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
