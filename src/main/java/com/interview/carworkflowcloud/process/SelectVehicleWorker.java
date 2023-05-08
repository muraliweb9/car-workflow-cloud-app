package com.interview.carworkflowcloud.process;

import com.interview.carworkflowcloud.data.Car;
import com.interview.carworkflowcloud.data.Location;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.CompleteJobResponse;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class SelectVehicleWorker {

    @Autowired
    private ZeebeClient zeebeClient;

    @JobWorker(type = "selectVehicle", autoComplete = true)
    public void selectVehicle(final JobClient client, final ActivatedJob job) {

        log.info("Running SelectVehicleWorker !!");
        Location loc = Location.builder()
                .id("1")
                .locationName("Toronto").build();
        Car car = Car.builder()
                .id("1")
                .make("Audi")
                .model("Q7")
                .type("SUV")
                .capacity("7")
                .registrationPlate("LL75MVB")
                .location(loc).build();
        Map<String, Object> variables = Map.of("car", car);


        CompleteJobResponse response = zeebeClient
                .newCompleteCommand(job.getKey())
                .variables(variables)
                .send().join(); // Blocking behaviour - limited for scalability

        log.info("SelectVehicleWorker CompleteJobResponse: [{}]", response);

    }
}
