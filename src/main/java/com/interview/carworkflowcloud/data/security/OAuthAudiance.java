package com.interview.carworkflowcloud.data.security;

public enum OAuthAudiance {
    TASKLIST("tasklist.camunda.io"),
    OAUTH("zeebe.camunda.io"),
    CONSOLE("api.cloud.camunda.io"),
    OPERATE("operate.camunda.io"),
    OPTIMIZE("optimize.camunda.io");

    private String audiance;

    private OAuthAudiance(String audiance) {
        this.audiance = audiance;
    }

    public String getAudiance() {
        return this.audiance;
    }
}
