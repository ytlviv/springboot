package com.oso.appt.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

public class RESTClient {
	
    private RestTemplate template;
	
    private final static String url = "http://localhost:8080/SpringRestWS/restful/";
    
    public String show() {
        return template.getForObject(url + "show.do", String.class, new String[]{});
    }
    
    public String getUserById(String id) {
        return template.getForObject(url + "get/{id}.do", String.class, id); 
    }
    
}
