package com.interview.carworkflowcloud.services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityFilterConfig {

    @Autowired
    private SecurityConfig securityConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        String[] whiteList = securityConfig.getWhiteList();
        return httpSecurity
                // Enabled by default in Spring
                // All POST requests are authenticated
                // Irrespective of White List - so off but should not be done in Prod
                .csrf()
                .disable()
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(whiteList).permitAll().anyRequest().authenticated())
                .oauth2Login(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .build();
    }
}
