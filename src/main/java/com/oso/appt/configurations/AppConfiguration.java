package com.oso.appt.configurations;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.oso.appt.ApptFilter;
import com.radiadesign.catalina.session.RedisSessionHandlerValve;
import com.radiadesign.catalina.session.RedisSessionManager;

/**
 * 
 * @ClassName: AppConfiguration
 * @Description: 装载类
 * @author yetl
 * @date 2015年5月20日
 *
 */
@Configuration
@ComponentScan({"com.oso.appt"})
@EnableAutoConfiguration
@EnableWebMvc
@ImportResource({"classpath:applicationContext.xml"})
public class AppConfiguration {
	public static final RedisSessionManager rsm = new RedisSessionManager();
	@Value("${redis.host}")
	private String redis_host;
	@Value("${session.maxInactiveInterval.web}")
	private int session_maxInactiveInterval_web;
	@Value("${session.timeout}")
	private int session_timeout;
	
	@Bean
	public FilterRegistrationBean authFilter() {
		FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
		filterRegBean.setFilter(new ApptFilter());
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/*");
		filterRegBean.setUrlPatterns(urlPatterns);
		return filterRegBean;
	}
	
	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
		RedisSessionHandlerValve rshv = new RedisSessionHandlerValve();

		factory.addContextValves(rshv);
		factory.addContextCustomizers(new TomcatContextCustomizer() {

			@Override
			public void customize(Context context) {
				rsm.setHost(redis_host);
				rsm.setPort(6379);
				rsm.setDatabase(0);
				rsm.setMaxInactiveInterval(session_maxInactiveInterval_web);
				rsm.setSerializationStrategyClass("com.radiadesign.catalina.session.JsonSerializer");
				context.setManager(rsm);
				context.setSessionCookiePath("/Appt");
				context.setSessionCookiePathUsesTrailingSlash(true);
				context.setSessionTimeout(session_timeout);
			}

		});

		return factory;
	}
}
