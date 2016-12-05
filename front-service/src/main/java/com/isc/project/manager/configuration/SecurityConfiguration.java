package com.isc.project.manager.configuration;

import com.isc.project.manager.persistence.domain.UserEntity;
import com.isc.project.manager.persistence.domain.UserType;
import com.isc.project.manager.persistence.repository.UserEntityRepository;
import com.isc.project.manager.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                .exceptionHandling()
                .and()
                .csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers("/api/authenticate").permitAll()
                .and()
                .authorizeRequests().antMatchers("/api/**").hasRole("USER").anyRequest().permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public InitializingBean securityInitializingBean() {
        return new InitializingBean() {
            @Autowired
            private UserEntityRepository repository;

            @Autowired
            private PasswordEncoder passwordEncoder;

            @Override
            public void afterPropertiesSet() throws Exception {
                UserEntity entity = new UserEntity();
                entity.setTenantCode("");
                entity.setType(UserType.ADMIN);
                entity.setUsername("ivan");
                entity.setPassword(passwordEncoder.encode("1111"));

                repository.save(entity);
            }
        };
    }

    @Bean
    protected AuthenticationEntryPoint authenticationEntryPoint() {
        return (httpServletRequest, httpServletResponse, exception) -> {
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        };
    }
}
