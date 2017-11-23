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

public class EncodingFilter implements Filter{
	public FilterConfig config;
	
	@Override
	public void destroy() {
		this.config=null;
	}
	
	public static boolean isContains(String container, String[] regx) {
        boolean result = false;

        for (int i = 0; i < regx.length; i++) {
            if (container.indexOf(regx[i]) != -1) {
                return true;
            }
        }
        return result;
    }
	
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		//1.获取config参数
		String encoding=config.getInitParameter("encoding");
		String forceEncoding=config.getInitParameter("forceEncoding");
		//2.限制req的请求编码格式
		if((forceEncoding.equals("true") || req.getCharacterEncoding()==null) && encoding!=null){
			req.setCharacterEncoding(encoding);
		}
		//3.显示resp的响应编码格式
		resp.setCharacterEncoding(encoding);
		resp.setContentType("text/html;charset=" + encoding);
		chain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig filterconfig) throws ServletException {
		config=filterconfig;
	}

}
