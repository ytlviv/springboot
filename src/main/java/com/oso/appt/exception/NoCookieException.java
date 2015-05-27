package com.oso.appt.exception;

/**
 * 
 * @ClassName: NoCookieException
 * @Description: cookie没有
 * @author yetl
 * @date 2015年5月25日
 *
 */
public class NoCookieException extends RuntimeException{
	
	private static final long serialVersionUID = -665008687613442668L;

	public NoCookieException(){
		super();
	}
	
	public NoCookieException(String msg){
		super(msg);
	}
	
	public NoCookieException(String msg,Throwable cause){
		super(msg,cause);
	}
	
	public NoCookieException(Throwable cause){
		super(cause);
	}
	
}
