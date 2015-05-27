package com.oso.appt.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.oso.appt.exception.NoCookieException;

/**
 * 
 * @ClassName: HttpClientUtil
 * @Description: HttpClientUtil
 * @author yetl
 * @date 2015年5月21日
 *
 */
@SuppressWarnings("deprecation")
public class HttpClientUtil {
	private static HttpClient httpClient = new DefaultHttpClient();

	/**
	 * 发送Get请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String get(String url, List<NameValuePair> params) {
		String body = null;
		try {
			// Get请求
			HttpGet httpget = new HttpGet(url);
			// 设置参数
			String str = EntityUtils.toString(new UrlEncodedFormEntity(params));
			httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));
			// 发送请求
			HttpResponse httpresponse = httpClient.execute(httpget);
			// 获取返回数据
			HttpEntity entity = httpresponse.getEntity();
			body = EntityUtils.toString(entity);
			if (entity != null) {
				entity.consumeContent();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return body;
	}
	
	/**
	 * 
	 * @param url
	 * @param jsessionId
	 * @return
	 */
	public static String get(String url, String jsessionId) {
		String body=null;
		try {
			// Get请求
			HttpGet httpget = new HttpGet(url);
			if(jsessionId!=null){
				httpget.setHeader("Cookie", "JSESSIONID="+jsessionId);
			}
			// 发送请求
			HttpResponse httpresponse = httpClient.execute(httpget);
			// 获取返回数据
			HttpEntity entity = httpresponse.getEntity();
			body = EntityUtils.toString(entity);
			if (entity != null) {
				entity.consumeContent();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}

	/**
	 * 发送 Post请求
	 * 
	 * @param url
	 * @param args
	 * @return
	 */
	public static HttpResponse post(String url, Map<String,?> args) {
		try {
			httpClient.getParams().setParameter(
					"http.protocol.content-charset", HTTP.UTF_8);
			httpClient.getParams().setParameter(HTTP.CONTENT_ENCODING,
					HTTP.UTF_8);
			httpClient.getParams().setParameter(HTTP.CHARSET_PARAM, HTTP.UTF_8);
			httpClient.getParams().setParameter(HTTP.DEFAULT_PROTOCOL_CHARSET,
					HTTP.UTF_8);
			// Post请求
			HttpPost httppost = new HttpPost(url);
			
			// 设置post编码
			httppost.getParams().setParameter("http.protocol.content-charset",HTTP.UTF_8);
			httppost.getParams().setParameter(HTTP.CONTENT_ENCODING, HTTP.UTF_8);
			httppost.getParams().setParameter(HTTP.CHARSET_PARAM, HTTP.UTF_8);
			httppost.getParams().setParameter(HTTP.DEFAULT_PROTOCOL_CHARSET,HTTP.UTF_8);
			
			// 设置参数
			if(args!=null && args.size()>0){
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				Set<String> keySet = args.keySet();
		        for(String key : keySet) {
		        	params.add(new BasicNameValuePair(key, String.valueOf(args.get(key))));  
		        }
				httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}
			// 发送请求
			HttpResponse httpresponse = httpClient.execute(httppost);
			return httpresponse;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取resp content
	 * @param httpresponse
	 * @return
	 */
	public static String getBody(HttpResponse httpresponse){
		HttpEntity entity = httpresponse.getEntity();
		String body;
		try {
			body = EntityUtils.toString(entity);
			if (entity != null) {
				entity.consumeContent();
			}
			return body;
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 校验图片
	 * @param url
	 * @param out
	 * @throws IOException
	 */
	public static void getValidateCode(String url,OutputStream out) throws IOException{
		InputStream inputStream=null;
		try{
			HttpGet httpget = new HttpGet(url);
		    HttpResponse response = httpClient.execute(httpget);  
		    HttpEntity entity = response.getEntity();  
		    inputStream=entity.getContent();
		                           
		    int read = 0;  
		    byte[] bytes = new byte[1024];  
		                           
		    while ((read = inputStream.read(bytes)) != -1) {  
		        out.write(bytes, 0, read);  
		    }  

		}finally{
			if(inputStream!=null)
				inputStream.close(); 
			if(out!=null){
			    out.flush();
			    out.close();
			}
		}
	}
	
	/**
	 * 获取cookie
	 * @param httpResponse
	 */
	public static String getCookie(HttpResponse httpResponse) {
	    Header cookieHeader=httpResponse.getFirstHeader("Set-Cookie");
	    if(cookieHeader==null)
	    	throw new NoCookieException("http header没有Set-Cookie");
	    String setCookie = cookieHeader.getValue();
	    if(setCookie.indexOf("JSESSIONID=")>=0){
		    String JSESSIONID = setCookie.substring("JSESSIONID=".length(),setCookie.indexOf(";"));
		    return JSESSIONID;
	    }
	    return null;
	}
	
	/**
	 * 
	 * @param url
	 * @param jsessionId
	 * @return
	 */
	public static String post(String url, String jsessionId) {
		try {
			HttpPost httppost = new HttpPost(url);
			httppost.setHeader("Cookie", "JSESSIONID="+jsessionId);
			HttpResponse httpresponse = httpClient.execute(httppost);
			HttpEntity entity = httpresponse.getEntity();
			String body = EntityUtils.toString(entity);
			if (entity != null) {
				entity.consumeContent();
			}
			return body;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
