package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.competition;
import exception.CompetitionExistedException;
import handler.CompetitionHander;
import net.sf.json.JSONObject;

public class CreateCompetitionServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//1.获取比赛信息
		String CNA=req.getParameter("CNA");
		String SOG=req.getParameter("SOG");
		String Level=req.getParameter("Level");
		String MaxTeamMember=req.getParameter("MaxTeamMember");
		JSONObject result=new JSONObject();
		//2.创建队伍
		try {
			if(CompetitionHander.CreateCompetion(CNA, SOG, Level,Integer.valueOf(MaxTeamMember))){
				result.put("Sucessed", 1);
			}else{
				result.put("Sucessed", 0);
				result.put("reason", "操作失败，请重试");
			}
		} catch (CompetitionExistedException e) {
			result.put("Sucessed", 0);
			result.put("reason", "该比赛已经存在");
		}
		resp.getOutputStream().write(result.toString().getBytes());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
