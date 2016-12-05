package com.isc.project.manager.configuration;

import com.isc.project.manager.persistence.configuration.PersistenceConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {PersistenceConfiguration.class})
@ComponentScan("com.isc.project.manager")
public class ApplicationConfiguration {

}
