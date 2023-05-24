package com.interview.carworkflowcloud.process;

import com.interview.carworkflowcloud.consts.ErrorCode;
import com.interview.carworkflowcloud.data.BookingRecord;
import com.interview.carworkflowcloud.data.Car;
import com.interview.carworkflowcloud.data.CustomerDetails;
import com.interview.carworkflowcloud.wrapper.ZeebeClientWrapper;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.CompleteJobResponse;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FinaliseBooking {

    private ZeebeClientWrapper zeebeClient;

    public FinaliseBooking(ZeebeClientWrapper zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    // @GrpcC

    @JobWorker(type = "finaliseBooking", fetchAllVariables = true, autoComplete = true)
    @ZeebeWorker
    public void finaliseBooking(
            final JobClient client,
            final ActivatedJob job,
            @Variable String firstName,
            @Variable String lastName,
            @Variable String licenceNumber,
            @Variable Car car) {

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
                .customerDetails(cust)
                .car(car)
                .allChecksDone(allChecksDone)
                .build();

        Map<String, Object> variables = Map.of("bookingRecord", record);

        CompleteJobResponse response = zeebeClient.completeCommand(job, variables);

        log.info("FinaliseBooking CompleteJobResponse: [{}]", response);
    }
}
