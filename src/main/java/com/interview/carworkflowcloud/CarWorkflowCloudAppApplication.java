package com.interview.carworkflowcloud;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.Deployment;
import io.camunda.zeebe.spring.client.annotation.ZeebeDeployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableZeebeClient
@Deployment(resources = {"process.bpmn"})
public class CarWorkflowCloudAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarWorkflowCloudAppApplication.class, args);
	}

}
