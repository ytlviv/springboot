package com.oso.appt.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.sun.istack.internal.Nullable;

/**
 * 
 * @ClassName: CommonUtils
 * @Description: 
 * @author yetl
 * @date 2015年5月26日
 *
 */
public final class CommonUtils {
	
	public static String getCookie(@Nullable HttpServletRequest request){
		if(request==null)
			return null;
		Cookie[] cookies = request.getCookies();
		String jsessionId=null;
		if(cookies!=null){
			for(Cookie cookie:cookies){
				String name=cookie.getName();
				if(name.equalsIgnoreCase("JSESSIONID")){
					jsessionId=cookie.getValue();
				}
			}
		}
		return jsessionId;
	}
}
