package com.jeonguk.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.jeonguk.web")
public class SpringMysqlRedisCacheApplication extends SpringBootServletInitializer {

    private static final Class<SpringMysqlRedisCacheApplication> applicationClass = SpringMysqlRedisCacheApplication.class;
    
	public static void main(String[] args) {
		SpringApplication.run(SpringMysqlRedisCacheApplication.class, args);
	}
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }
    
}