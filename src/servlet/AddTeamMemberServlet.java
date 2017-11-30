package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.student;
import bean.teammember;
import exception.TeamMenberMaxException;
import handler.TeamMemberHandler;
import net.sf.json.JSONObject;

public class AddTeamMemberServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//1.获取请求数据
		//1.1获取成员数据
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
		//1.2获取队伍编号
		JSONObject jo=new JSONObject();
		String TNO=req.getParameter("TNO");
		if(TNO==null || SNO==null){
			jo.put("Sucessed", "0");
			jo.put("reason", "主要辨别信息为空，插入或更新失败");
			resp.getOutputStream().write(jo.toString().getBytes());
			return ;
		}
		//2.插入队伍返回结果
		try {
			if(TeamMemberHandler.InsertMenber(TNO, stu)) {
				jo.put("Sucessed", 1);
			}else{
				jo.put("Sucessed", 0);
				jo.put("reason", "插入失败请重试");
			}
		} catch (TeamMenberMaxException e) {
			jo.put("Sucessed", 0);
			jo.put("reason", "人员数量已满");
		}
		resp.getOutputStream().write(jo.toString().getBytes());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
