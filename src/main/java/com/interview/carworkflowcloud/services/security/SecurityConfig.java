package com.interview.carworkflowcloud.services.security;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.security.custom.config")
@Data
public class SecurityConfig {

    private SecurityType type;

    private List<String> secureWhitelist;

    private List<String> noneWhitelist;

    public String[] getWhiteList() {
        switch (type) {
            case NONE:
                return noneWhitelist.toArray(new String[0]);
            case SECURE:
                return secureWhitelist.toArray(new String[0]);
            default:
                return secureWhitelist.toArray(new String[0]);
        }
    }
}
