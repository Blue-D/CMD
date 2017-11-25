package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class LoginFilter implements Filter{
	
	public FilterConfig config;
	
	@Override
	public void destroy() {
		config=null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain config)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(FilterConfig filterconfig) throws ServletException {
		config=filterconfig;
	}

}
