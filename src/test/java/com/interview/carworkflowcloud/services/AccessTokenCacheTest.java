package com.interview.carworkflowcloud.services;

import com.interview.carworkflowcloud.data.security.AccessToken;
import com.interview.carworkflowcloud.data.security.AccessTokenRequest;
import com.interview.carworkflowcloud.data.security.AccessTokenResponse;
import com.interview.carworkflowcloud.data.security.OAuthAudiance;
import com.interview.carworkflowcloud.services.config.ClusterDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

public class AccessTokenCacheTest {

    @Test
    public void test() {

        String oAuthUri = "https://login.cloud.camunda.io/oauth/token";
        ClusterDetails clusterDetails = ClusterDetails.builder()
                .clientId("abcd1234")
                .clientSecret("verysecret")
                .oAuthUrl(oAuthUri)
                .build();
        OAuthAudiance audiance = OAuthAudiance.TASKLIST;
        AccessTokenRequest tokenRequest = AccessTokenRequest.from(clusterDetails, audiance);

        AccessTokenResponse accessTokenResponse = AccessTokenResponse.builder()
                .accessToken("1234ABCD£$!!")
                .expiresIn(86_400)
                .build();

        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

        Mockito.when(restTemplate.postForObject(
                        Mockito.eq(oAuthUri), Mockito.eq(tokenRequest), Mockito.eq(AccessTokenResponse.class)))
                .thenReturn(accessTokenResponse);

        AccessTokenCache tokenCache = new AccessTokenCache(restTemplate, clusterDetails);

        AccessToken accessToken = tokenCache.getAccessToken(audiance);

        Assertions.assertEquals("1234ABCD£$!!", accessToken.getAccessToken());

        Assertions.assertEquals(OAuthAudiance.TASKLIST, accessToken.getAudiance());

        Assertions.assertTrue(accessToken.isValidFor(audiance));

        Assertions.assertEquals(false, accessToken.isValidFor(OAuthAudiance.OPERATE));

        Assertions.assertTrue(accessToken.hasNotExpired());

        // Get it from the cache this time

        accessToken = tokenCache.getAccessToken(audiance);

        Assertions.assertEquals("1234ABCD£$!!", accessToken.getAccessToken());

        Mockito.verify(restTemplate, Mockito.times(1))
                .postForObject(Mockito.eq(oAuthUri), Mockito.eq(tokenRequest), Mockito.eq(AccessTokenResponse.class));
    }
}
