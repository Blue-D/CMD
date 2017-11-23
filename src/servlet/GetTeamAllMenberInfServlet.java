package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import handler.TeamMemberHandler;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetTeamAllMenberInfServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String TNO=req.getParameter("TNO");
		JSONArray ja=TeamMemberHandler.GetAllTeamMemberInf(TNO);
		JSONObject jo=new JSONObject();
		jo.put("stuinf", ja);
		resp.getOutputStream().write(jo.toString().getBytes());
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
