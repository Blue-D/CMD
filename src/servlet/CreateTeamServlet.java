package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.logininf;
import bean.student;
import bean.team;
import exception.TeamExistedException;
import net.sf.json.JSONObject;

public class CreateTeamServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//1.获取请求头
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=utf-8");
		//1.1队伍信息
		String TNA=req.getParameter("TNA");
		String CNO=req.getParameter("CNO");
		team t=new team();
		t.setTna(TNA);
		t.setCno(CNO);
		//1.2队员信息
		String SNA=req.getParameter("SNA");
		String SNO=req.getParameter("SNO");
		String Major=req.getParameter("Major");
		String tele=req.getParameter("tele");
		String email=req.getParameter("email");
		String ID=req.getParameter("ID");
		student stu=new student();
		stu.setEmail(email);
		stu.setTele(tele);
		stu.setId(ID);
		stu.setMajor(Major);
		stu.setSna(SNA);
		stu.setSno(SNO);
		t.setCaptainsno(SNO);
		//3.结果判断
		JSONObject jo=new JSONObject();
		try {
			logininf lgif=handler.TeamHandler.CreateTeam(t, stu);
			if(lgif==null){
				jo.put("Sucessed", 0);
				jo.put("reason", "用户创建失败，请重新报名");
				
			}else{
				jo.put("Sucessed", 1);
				jo.put("user", lgif.getUsernum());
				jo.put("pass", lgif.getPass());
			}
		} catch (TeamExistedException e) {
			jo.put("Sucessed", -1);
			jo.put("reason", "该队名已经存在");
		}
		resp.getOutputStream().write(jo.toString().getBytes());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
