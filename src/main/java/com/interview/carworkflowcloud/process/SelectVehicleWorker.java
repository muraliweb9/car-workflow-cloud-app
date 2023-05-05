package com.interview.carworkflowcloud.process;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SelectVehicleWorker {

    @JobWorker(type="selectVehicle")
    public void selectVehicle() {
        log.info("Vehicle has been selected !!!!!");
    }
}
