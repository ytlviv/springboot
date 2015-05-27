package com.oso.appt;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

import com.oso.appt.configurations.AppConfiguration;

/**
 * 
 * @ClassName: ApptApplication
 * @Description: 程序入口 扫描com.oso.appt**下的所有类
 * @author yetl
 * @date 2015年5月21日
 *
 */
@Import(AppConfiguration.class)
public class ApptApplication {
	
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ApptApplication.class);
        app.setShowBanner(false);
        app.run(args);
    }
    
}
