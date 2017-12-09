package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Session;

import bean.logininf;

public class LoginFilter implements Filter{
	
	public FilterConfig config;
	
	@Override
	public void destroy() {
		config=null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		//1.获取不需要登陆验证的页面
		String FirstPath=config.getInitParameter("FristPath");
		HttpServletRequest request=(HttpServletRequest)req;
		HttpServletResponse response=(HttpServletResponse)resp;
		String path=request.getServletPath();
		if(path.lastIndexOf("/login")!=-1 || path.lastIndexOf(FirstPath)!=-1){
			chain.doFilter(req, resp);
			System.out.println("不拦截");
			return ;
		}
		//2.获取登录用户的权限级别
		HttpSession session=request.getSession(true);
		logininf log=(logininf) session.getAttribute("logininf");
		int jur=0;
		if(log==null){
			response.sendRedirect("/CMD/index/NotLogin.html");
			return ;
		}else{
			jur=log.getJur();
		}
		//3.权限判别
		String Second=config.getInitParameter("SecondPath");
		String Third=config.getInitParameter("ThirdPath");
		String Forth=config.getInitParameter("ForthPath");
		System.out.println(jur);
		if(path.lastIndexOf(Second)!=-1 && jur==1){
			chain.doFilter(req, resp);
			return;
		}
		if(path.lastIndexOf(Third)!=-1 && (jur==2 || jur==3)){
			chain.doFilter(req, resp);
			return;
		}
		if(path.lastIndexOf(Forth)!=-1 && jur==3){
			chain.doFilter(req, resp);
			return;
		}
		//4.是否是公用的Servlet
		String InterceptButPublic=config.getInitParameter("InterceptButPublic");
		if(path.lastIndexOf(InterceptButPublic)!=-1){
			chain.doFilter(req, resp);
			return ;
		}
		response.getOutputStream().write("对不起您的访问权限不够".getBytes());
	}

	@Override
	public void init(FilterConfig filterconfig) throws ServletException {
		config=filterconfig;
	}

}
