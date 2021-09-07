package com.microservices.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] permittedUrls ={
            "/authenticate",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api/v1/user/**"

    };

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(permittedUrls);


        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Note:
        // Use this to enable the tomcat basic authentication (tomcat popup rather than spring login page)
        // Note that the CSRf token is disabled for all requests (change it as you wish...)
        http.csrf().disable().authorizeRequests().anyRequest().authenticated().and().httpBasic();
    }
}
