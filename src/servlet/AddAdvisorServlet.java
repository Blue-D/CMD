package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.advisor;
import handler.AdvisorHandler;
import net.sf.json.JSONObject;

public class AddAdvisorServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//1.获取队伍编号
		String TNO=req.getParameter("TNO");
		//2.获取指导老师信息
		String ANA=req.getParameter("ANA");
		JSONObject jo=new JSONObject();
		if(TNO==null || ANA==null){
			jo.put("Sucessed", "0");
			jo.put("reason", "主要辨别信息为空，插入或更新失败");
			resp.getOutputStream().write(jo.toString().getBytes());
			return ;
		}
		String Atele=req.getParameter("Atele");
		String Aemail=req.getParameter("Ae-mail");
		advisor ad=new advisor();
		ad.setAemail(Aemail);
		ad.setAna(ANA);
		ad.setAtele(Atele);
		ad.setTno(TNO);
		if(AdvisorHandler.InsertAdcisor(ad)) {
			jo.put("Sucessed", 1);
		}else{
			jo.put("Sucessed", 0);
			jo.put("reason", "插入或更新失败，请重试");
		}
		resp.getOutputStream().write(jo.toString().getBytes());
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
