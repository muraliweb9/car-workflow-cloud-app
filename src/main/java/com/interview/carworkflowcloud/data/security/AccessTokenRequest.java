package com.interview.carworkflowcloud.data.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.interview.carworkflowcloud.services.config.ClusterDetails;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
@Setter(AccessLevel.NONE)
public class AccessTokenRequest {

    @JsonProperty(value = "client_id")
    private String clientId;

    @JsonProperty(value = "client_secret")
    private String clientSecret;

    @JsonProperty(value = "audience")
    private String audience;

    @JsonProperty(value = "grant_type")
    private String grantType;

    public static AccessTokenRequest from(ClusterDetails clusterDetails, OAuthAudiance audience) {

        return AccessTokenRequest.builder()
                .clientId(clusterDetails.getClientId())
                .clientSecret(clusterDetails.getClientSecret())
                .audience(audience.getAudiance())
                .grantType("client_credentials")
                .build();
    }
}
