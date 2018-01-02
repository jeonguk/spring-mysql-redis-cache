package com.jeonguk.web.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.jeonguk.web.repository")
@EntityScan(basePackageClasses = {Jsr310JpaConverters.class}, basePackages = {"com.jeonguk.web.entity"})
public class JpaConfig {

}
