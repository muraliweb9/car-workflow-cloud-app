package com.interview.carworkflowcloud.services.config;

import com.interview.carworkflowcloud.process.FinaliseBooking;
import com.interview.carworkflowcloud.wrapper.ZeebeClientWrapper;
import com.interview.proto.carrecords.service.RecordsServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @DependsOn the class that has the @GrpcClientBeans and @GrpcClientBean.
 * This is to ensure the bean creation order
 *
 * Within this class create the @Bean fro all the beans that need the
 * gRPC stub.
 *
 */
@Configuration
@DependsOn("grpcStubConfig")
class GrpcStubConfig2 {
    @Bean
    public FinaliseBooking finaliseBooking(
            @Autowired ZeebeClientWrapper zeebeClient,
            @Autowired DiscoveryClient discoveryClient,
            @Autowired RecordsServiceGrpc.RecordsServiceBlockingStub stub) {
        return new FinaliseBooking(zeebeClient, discoveryClient, stub);
    }
}
