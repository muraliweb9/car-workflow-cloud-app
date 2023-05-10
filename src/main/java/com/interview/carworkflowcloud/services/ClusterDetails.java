package com.interview.carworkflowcloud.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ClusterDetails {

    @Value("${zeebe.client.cloud.region}")
    private String region;

    @Value("${zeebe.client.cloud.clusterId}")
    private String clusterId;

    @Value("${zeebe.client.cloud.clientId}")
    private String clientId;

    @Value("${zeebe.client.cloud.clientSecret}")
    private String clientSecret;

    @Value("${zeebe.client.cloud.oAuthUrl}")
    private String oAuthUrl;

    @Value("${zeebe.client.api.taskSearchUrl}")
    private String taskSearchUrl;

    //    public String getClientId() {return clientId;}
    //
    //    public String getClientSecret() {return clientSecret;}
    //
    //    public String getOAuthUrl() {return oAuthUrl;}
    //    public String getCompleteTaskSearchUrl() {
    //        Map<String, Object> values = Map.of("region", region, "clusterId", clusterId);
    //        return StrSubstitutor.replace(taskSearchUrl, values);
    //    }

}
