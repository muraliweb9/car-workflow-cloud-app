package com.interview.carworkflowcloud.data.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenResponse {

    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "scope")
    private String scope;

    @JsonProperty(value = "expires_in")
    private Integer expiresIn;

    @JsonProperty(value = "token_type")
    private String tokenType;
}
