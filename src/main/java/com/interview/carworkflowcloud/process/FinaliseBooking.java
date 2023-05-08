package com.interview.carworkflowcloud.process;

import com.interview.carworkflowcloud.consts.ErrorCode;
import com.interview.carworkflowcloud.data.BookingRecord;
import com.interview.carworkflowcloud.data.Car;
import com.interview.carworkflowcloud.data.CustomerDetails;
import com.interview.carworkflowcloud.data.Location;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.CompleteJobResponse;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Map;

@Component
@Slf4j
public class FinaliseBooking {

    @Autowired
    private ZeebeClient zeebeClient;

    @JobWorker(type = "finaliseBooking", fetchAllVariables = true, autoComplete = true)
    public void finaliseBooking(final JobClient client, final ActivatedJob job,
                                @Variable String firstName,
                                @Variable String lastName,
                                @Variable String licenceNumber,
                                @Variable Car car
    ) {

        log.info("Running FinaliseBooking !!");

        if (StringUtils.isBlank(licenceNumber) || "null".equals(licenceNumber)) {
            throw ErrorCode.FINALISE_FAILURE.bpmnError();
        }

        CustomerDetails cust = CustomerDetails.builder()
                .id("1")
                .firstName("firstName")
                .lastName("lastName")
                .licenceNumber("licenceNumber")
                .build();

        boolean allChecksDone = true;

        BookingRecord record = BookingRecord.builder()
                .id("1")
                .customerDetails(cust)
                .car(car)
                .allChecksDone(allChecksDone)
                .build();

        var variables = Map.of("bookingRecord", record);

        CompleteJobResponse response = zeebeClient
                .newCompleteCommand(job.getKey())
                .variables(variables)
                .send().join(); // Blocking behaviour - limited for scalability

        log.info("FinaliseBooking CompleteJobResponse: [{}]", response);

    }
}
