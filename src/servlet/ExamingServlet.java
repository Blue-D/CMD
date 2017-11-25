package servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BaseDao;
import dao.DaoUtil;
import handler.ExamingHandler;
import net.sf.json.JSONObject;

public class ExamingServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String table=req.getParameter("table");
		String pk_value=req.getParameter("PK_value");
		String ispassed=req.getParameter("ispassed");
		String reason=req.getParameter("reason");
		JSONObject jo=new JSONObject();
		if(ExamingHandler.Exam(table, pk_value, ispassed, reason)==0){
			jo.put("Sucessed", 0);
			jo.put("reason", "操作失败请重试");
		}else{
			jo.put("Sucessed", 1);
		}
		resp.getOutputStream().write(jo.toString().getBytes());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
