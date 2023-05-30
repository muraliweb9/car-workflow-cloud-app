package com.interview.carworkflowcloud.process;

import com.interview.carworkflowcloud.data.Car;
import com.interview.carworkflowcloud.data.Location;
import com.interview.carworkflowcloud.wrapper.ZeebeClientWrapper;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.CompleteJobResponse;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class SelectVehicleWorker {

    private ZeebeClientWrapper zeebeClient;

    @JobWorker(type = "selectVehicle", autoComplete = true)
    public void selectVehicle(final JobClient client, final ActivatedJob job) {

        log.info("Running SelectVehicleWorker !!");
        Location loc = Location.builder().id("1").locationName("Toronto").build();
        Car car = Car.builder()
                .id("1")
                .make("Audi")
                .model("Q7")
                .type("SUV")
                .capacity("7")
                .registrationPlate("LL75MVB")
                .location(loc)
                .build();
        Map<String, Object> variables = Map.of("car", car);

        CompleteJobResponse response = zeebeClient.completeCommand(job, variables);

        log.info("SelectVehicleWorker CompleteJobResponse: [{}]", response);
    }
}
