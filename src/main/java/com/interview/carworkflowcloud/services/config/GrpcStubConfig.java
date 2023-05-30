package com.interview.carworkflowcloud.services.config;

import com.interview.proto.carrecords.service.RecordsServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.inject.GrpcClientBean;
import net.devh.boot.grpc.client.inject.GrpcClientBeans;
import org.springframework.context.annotation.Configuration;

/**
 * Create a @GrpcClientBean for each gRPC stub
 */
@Configuration(value = "grpcStubConfig")
@GrpcClientBeans({
    @GrpcClientBean(
            clazz = RecordsServiceGrpc.RecordsServiceBlockingStub.class,
            beanName = "recordsServiceBlockingStub",
            client = @GrpcClient("car-records-app"))
})
class GrpcStubConfig {}
