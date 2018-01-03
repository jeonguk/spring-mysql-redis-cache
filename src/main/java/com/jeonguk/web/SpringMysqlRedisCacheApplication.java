package com.jeonguk.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@ComponentScan(basePackages = "com.jeonguk.web")
@EntityScan(
        basePackageClasses = {Jsr310JpaConverters.class},
        basePackages = {"com.jeonguk.web.entity"})
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