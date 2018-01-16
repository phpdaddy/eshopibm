package com.phpdaddy.eshopibm;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.phpdaddy.eshopibm")
@EnableJpaRepositories(basePackages = "com.phpdaddy.eshopibm.repository")
public class AppConfig {
}