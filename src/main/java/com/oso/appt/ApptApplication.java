package com.oso.appt;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

import com.oso.appt.configurations.AppConfiguration;

/**
 * 
 * @ClassName: ApptApplication
 * @Description: ������� ɨ��com.oso.appt**�µ�������
 * @author yetl
 * @date 2015��5��21��
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
