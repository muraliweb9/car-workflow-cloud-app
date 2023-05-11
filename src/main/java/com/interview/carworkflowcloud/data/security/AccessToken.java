package com.interview.carworkflowcloud.data.security;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessToken {

    private String accessToken;

    private LocalDateTime expiry;

    private OAuthAudiance audiance;

    public boolean hasNotExpired() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expiry)) {
            return false;
        }
        return true;
    }

    public boolean isValidFor(OAuthAudiance audiance) {

        return this.audiance.equals(audiance) && hasNotExpired();
    }
}
