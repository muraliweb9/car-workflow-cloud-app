package com.interview.carworkflowcloud;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZeebeClient
@Deployment(resources = "classpath:process.bpmn")
public class CarWorkflowCloudAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarWorkflowCloudAppApplication.class, args);
    }
}
