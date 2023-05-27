package com.interview.carworkflowcloud.services.config;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * Temporary work around for:
 * <p>
 * https://github.com/yidongnan/grpc-spring-boot-starter/pull/775
 */
@Configuration
@ImportAutoConfiguration({
    net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration.class,
    net.devh.boot.grpc.client.autoconfigure.GrpcClientMetricAutoConfiguration.class,
    net.devh.boot.grpc.client.autoconfigure.GrpcClientHealthAutoConfiguration.class,
    net.devh.boot.grpc.client.autoconfigure.GrpcClientSecurityAutoConfiguration.class,
    net.devh.boot.grpc.client.autoconfigure.GrpcClientTraceAutoConfiguration.class,
    net.devh.boot.grpc.client.autoconfigure.GrpcDiscoveryClientAutoConfiguration.class,
    net.devh.boot.grpc.common.autoconfigure.GrpcCommonCodecAutoConfiguration.class,
    net.devh.boot.grpc.common.autoconfigure.GrpcCommonTraceAutoConfiguration.class,
    net.devh.boot.grpc.common.autoconfigure.GrpcCommonCodecAutoConfiguration.class,
    net.devh.boot.grpc.common.autoconfigure.GrpcCommonTraceAutoConfiguration.class
})
class GrpcStarterClientConfig {}
