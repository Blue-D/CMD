package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.logininf;
import dao.BaseDao;
import net.sf.json.JSONObject;

public class LoginServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String usr=req.getParameter("usr");
		String pass=req.getParameter("pass");
		try {
			logininf log=(logininf) BaseDao.Select("logininf", null, new String[]{"usernum='"+usr+"'","pass='"+pass+"'"}).get(0);
			HttpSession session=req.getSession(true);
			session.setAttribute("logininf", log);
		} catch (ClassNotFoundException | NullPointerException e) {
			resp.getOutputStream().write(0);
			return ;
		}
		resp.getOutputStream().write(1);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
}
