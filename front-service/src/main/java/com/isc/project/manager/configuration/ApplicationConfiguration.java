package com.isc.project.manager.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.isc.project.manager")
public class ApplicationConfiguration {

    @Bean(name = "projectApiServiceAddress")
    public String projectApiServiceAddress() {
        return "http://localhost:8081";
    }
}
