package com.interview.carworkflowcloud.services.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {
        // -- Swagger UI v2
        "/v2/api-docs",
        "v2/api-docs",
        "/swagger-resources",
        "swagger-resources",
        "/swagger-resources/**",
        "swagger-resources/**",
        "/configuration/ui",
        "configuration/ui",
        "/configuration/security",
        "configuration/security",
        "/swagger-ui.html",
        "swagger-ui.html",
        "webjars/**",
        // -- Swagger UI v3
        "/v3/api-docs/**",
        "v3/api-docs/**",
        "/swagger-ui/**",
        "swagger-ui/**",
        // CSA Controllers
        "/csa/api/token",
        // Actuators
        "/actuator/**",
        "/health/**",
        // Camunda
        "/carworkflowcloud/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                // Enabled by default
                // All POST requests are authenticated
                // Irrespective of White List - so off but should not be done in Prod
                .csrf()
                .disable()
                .authorizeHttpRequests(auth -> auth.requestMatchers(AUTH_WHITELIST)
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .oauth2Login(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .build();
    }
}
