package com.oso.appt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import com.oso.appt.configurations.AppConfiguration;
import com.oso.appt.util.CommonUtils;

/**
 * 
 * @ClassName: ApptFilter
 * @Description: 过滤器
 * @author yetl
 * @date 2015年5月21日
 *
 */
public class ApptFilter implements Filter{
	
	@Value("${session.maxInactiveInterval.mobile}")
	private int session_maxInactiveInterval_mobile;

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String uri=req.getRequestURI();
		res.setCharacterEncoding("UTF-8");
		HttpSession sess = req.getSession(false);
		if(sess == null) sess = getSessionByHeader(req);
		
		if(uri.startsWith("/login") || uri.startsWith("/_fileserv/redis_session")){
			chain.doFilter(request, response);
			return;
		}
		
		if (sess==null) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("error", "未认证的访问");
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			res.setContentType("application/json; charset=UTF-8");
			new ObjectMapper().writeValue(res.getOutputStream(), m);
			return;
		}
		chain.doFilter(request, response);
	}

	public void destroy() {
	}
	
	private HttpSession getSessionByHeader(HttpServletRequest req) throws IOException {
		String sessionId=CommonUtils.getCookie(req);
		if(sessionId==null) return null;
		HttpSession sess = AppConfiguration.rsm.loadSessionFromRedis(sessionId);
		//注意：以下这个设置虽然也有效，但其实不应该在这里做，
		//而是应该在移动设备登录接口内做，就是移动设备登录成功后，返回令牌之前，做此设置
		//因为本微服务并不是认证服务接口，所以仅在此示意
		if(sess!=null) {
			sess.setMaxInactiveInterval(this.session_maxInactiveInterval_mobile);
		}
		
		return sess;
	}
}
