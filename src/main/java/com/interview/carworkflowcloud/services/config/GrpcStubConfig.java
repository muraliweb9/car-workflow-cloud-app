package com.interview.carworkflowcloud.services.config;

import com.interview.carworkflowcloud.process.FinaliseBooking;
import com.interview.carworkflowcloud.wrapper.ZeebeClientWrapper;
import com.interview.proto.carrecords.service.RecordsServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.inject.GrpcClientBean;
import net.devh.boot.grpc.client.inject.GrpcClientBeans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@GrpcClientBeans({
    @GrpcClientBean(
            clazz = RecordsServiceGrpc.RecordsServiceBlockingStub.class,
            beanName = "blockingStub",
            client = @GrpcClient("car-records-app"))
})
class GrpcStubConfig {
    @Bean
    public FinaliseBooking csBlockingStub(
            @Autowired ZeebeClientWrapper zeebeClient,
            @Autowired DiscoveryClient discoveryClient,
            @Autowired RecordsServiceGrpc.RecordsServiceBlockingStub stub) {
        return new FinaliseBooking(zeebeClient, discoveryClient, stub);
    }
}
