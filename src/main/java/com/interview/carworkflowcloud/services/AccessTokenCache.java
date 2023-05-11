package com.interview.carworkflowcloud.services;

import com.interview.carworkflowcloud.data.security.AccessToken;
import com.interview.carworkflowcloud.data.security.AccessTokenRequest;
import com.interview.carworkflowcloud.data.security.AccessTokenResponse;
import com.interview.carworkflowcloud.data.security.OAuthAudiance;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AccessTokenCache {

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    private ClusterDetails clusterDetails;

    private Map<OAuthAudiance, AccessToken> accessTokens = new ConcurrentHashMap<>();

    public AccessToken getAccessToken(OAuthAudiance audiance) {
        AccessToken accessToken = accessTokens.get(audiance);
        if (accessToken == null || !accessToken.isValidFor(audiance)) {
            accessToken = getNewAccessToken(audiance);
        }
        return accessToken;
    }

    private AccessToken getNewAccessToken(OAuthAudiance audiance) {

        LocalDateTime now = LocalDateTime.now();

        AccessTokenRequest tokenRequest = AccessTokenRequest.from(clusterDetails, audiance);

        String uri = clusterDetails.getOAuthUrl();

        AccessTokenResponse accessTokenResponse =
                restTemplate.postForObject(uri, tokenRequest, AccessTokenResponse.class);
        String accessTokenStr = accessTokenResponse.getAccessToken();

        Integer expiresInSeconds = accessTokenResponse.getExpiresIn();

        LocalDateTime accessTokenExpiry = now.plusSeconds(expiresInSeconds);

        AccessToken accessToken = AccessToken.builder()
                .accessToken(accessTokenStr)
                .expiry(accessTokenExpiry)
                .audiance(audiance)
                .build();

        accessTokens.put(audiance, accessToken);
        return accessToken;
    }
}
