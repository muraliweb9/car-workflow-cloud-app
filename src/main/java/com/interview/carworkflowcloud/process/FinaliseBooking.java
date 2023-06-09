package com.interview.carworkflowcloud.process;

import com.interview.carworkflowcloud.consts.ErrorCode;
import com.interview.carworkflowcloud.data.BookingRecord;
import com.interview.carworkflowcloud.data.Car;
import com.interview.carworkflowcloud.data.CustomerDetails;
import com.interview.carworkflowcloud.wrapper.ZeebeClientWrapper;
import com.interview.proto.carrecords.service.RecordSaveRequest;
import com.interview.proto.carrecords.service.RecordSaveResponse;
import com.interview.proto.carrecords.service.RecordsServiceGrpc;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.CompleteJobResponse;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.discovery.DiscoveryClient;

@Slf4j
@AllArgsConstructor
public class FinaliseBooking {

    private final ZeebeClientWrapper zeebeClient;

    private final DiscoveryClient discoveryClient;

    // @GrpcClient("car-records-app")
    private RecordsServiceGrpc.RecordsServiceBlockingStub recordsServiceStub;

    public void log() {

        List<String> services = discoveryClient.getServices();

        services.stream().forEach(s -> discoveryClient.getInstances(s).stream().forEach(i -> {
            log.info("Service {}, Service Instance {}", s, i);
        }));

        RecordSaveRequest req = RecordSaveRequest.newBuilder()
                .setFirstName("mura")
                .setLastName("karu")
                .setLicenceNumber("1234")
                .build();
        RecordSaveResponse resp = recordsServiceStub.saveRecord(req);

        log.info("Response is {}", resp);
    }

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

        RecordSaveRequest req = RecordSaveRequest.newBuilder()
                .setFirstName("mura")
                .setLastName("karu")
                .setLicenceNumber("1234")
                .build();
        RecordSaveResponse resp = recordsServiceStub.saveRecord(req);

        log.info("Response is {}", resp);

        CompleteJobResponse response = zeebeClient.completeCommand(job, variables);

        log.info("FinaliseBooking CompleteJobResponse: [{}]", response);
    }
}
