package com.oso.appt.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oso.appt.bean.User;
import com.oso.appt.util.CommonUtils;
import com.oso.appt.util.HttpClientUtil;

/**
 * 
 * @ClassName: LoginController
 * @Description: 登录 代理sync10做账号校验
 * @author yetl
 * @date 2015年5月21日
 *
 */
@RestController
public class LoginController {
	Logger log=LoggerFactory.getLogger(LoginController.class);
	Logger errLog=LoggerFactory.getLogger("E");
	
	@Value("${login.url}")
	private String loginUrl;
	@Value("${validateCode.url}")
	private String validateCodeUrl;
	@Value("${logout.url}")
	private String logoutUrl;
	@Value("${session.url}")
	private String sessionUrl;
	
	
	@RequestMapping(value="/login",produces="application/json")
	public String login(HttpServletRequest request,HttpServletResponse response,User user){
		log.info("代理登陆 请求参数-->"+user);
		Map<String,String> map=new HashMap<String,String>();
		map.put("account", user.getAccount());
		map.put("pwd", user.getPwd());
		map.put("licence", user.getLicence());
		HttpResponse httpresponse=HttpClientUtil.post(loginUrl, map);
		String resBody=HttpClientUtil.getBody(httpresponse);
		String resCookie=HttpClientUtil.getCookie(httpresponse);
		errLog.debug("代理登陆url-->"+loginUrl+",\r\n 响应:"+resBody+",\r\n cookie:"+resCookie);
		if(resCookie!=null){
			Cookie cookie=new Cookie("JSESSIONID", resCookie);
			cookie.setHttpOnly(true);
			cookie.setPath("/Appt");
			response.addCookie(cookie);
		}
		return resBody;
	}
	
	@RequestMapping(value="/login/validateCode")
	public void randomImage(HttpServletResponse response){
		try {
			log.info("代理获取验证码url"+validateCodeUrl);
			OutputStream out=response.getOutputStream();
			HttpClientUtil.getValidateCode(validateCodeUrl,out);
		} catch (IOException e) {
			log.error("获取验证码异常:"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/logout")
	public void logout(HttpServletRequest request,HttpServletResponse response){
		String jsid=CommonUtils.getCookie(request);
		HttpClientUtil.post(logoutUrl,jsid);
	}
	
	@RequestMapping(value="/_fileserv/redis_session")
	public String validateSession(HttpServletRequest request){
		String sessionId=CommonUtils.getCookie(request);
		String body=HttpClientUtil.get(sessionUrl, sessionId);
		return body;
	}
}
