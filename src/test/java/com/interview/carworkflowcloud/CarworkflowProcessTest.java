package com.interview.carworkflowcloud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.interview.carworkflowcloud.consts.ProcessConstants;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.process.test.assertions.BpmnAssert;
import io.camunda.zeebe.process.test.filters.RecordStream;
import io.camunda.zeebe.process.test.filters.StreamFilter;
import io.camunda.zeebe.protocol.record.Record;
import io.camunda.zeebe.protocol.record.RejectionType;
import io.camunda.zeebe.protocol.record.intent.JobIntent;
import io.camunda.zeebe.protocol.record.value.JobRecordValue;
import io.camunda.zeebe.spring.test.ZeebeSpringTest;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ZeebeSpringTest
@ActiveProfiles("test")
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CarworkflowProcessTest {

    @Autowired
    private ZeebeClient zeebeClient;

    @Autowired
    private ZeebeTestEngine zeebeTestEngine;

    @Value("${spring.boot.test.process.timeout}")
    private Long processTestTimeout;

    @BeforeEach
    public void beforeEach() {
        ZeebeTestThreadSupport.setEngineForCurrentThread(zeebeTestEngine);
    }

    @AfterEach
    public void afterEach() {
        ZeebeTestThreadSupport.cleanupEngineForCurrentThread();
    }

    @Test
    @SneakyThrows
    @Order(1)
    public void testHappyPath() throws Exception {
        log.info("Running testHappyPath !!");

        // Deploy the process on test engine - NOT needed handled by @ZeebeSpringTest
        //        DeploymentEvent event = zeebeClient.newDeployResourceCommand()
        //                .addResourceFromClasspath("my-process.bpmn")
        //                .send()
        //                .join();
        //        DeploymentAssert assertions = BpmnAssert.assertThat(event);

        // start a process instance
        ProcessInstanceEvent processInstance = zeebeClient
                .newCreateInstanceCommand() //
                .bpmnProcessId(ProcessConstants.PROCESS_NAME)
                .latestVersion() //
                .variables(Collections.emptyMap()) //
                .send()
                .join();

        // ProcessInstanceAssert assertions = BpmnAssert.assertThat(processInstance);

        waitForUserTaskAndComplete(
                processInstance,
                ProcessConstants.CUSTOMER_DETAILS_TASK_NAME,
                Map.of("firstName", "Mura", "lastName", "Karu", "licenceNumber", "12345678"),
                "start");

        waitForUserTaskAndComplete(
                processInstance,
                ProcessConstants.HANDOVER_VEHICLE_TASK_NAME,
                Map.of("allChecksDone", Boolean.TRUE),
                "enter-customer-details");

        ZeebeTestThreadSupport.waitForProcessInstanceHasPassedElement(processInstance, "finalise-booking");

        BpmnAssert.assertThat(processInstance).isCompleted();
    }

    @Test
    @SneakyThrows
    @Order(2)
    // @Disabled
    public void testFailedHandover() throws Exception {
        log.info("Running testFailedHandover !!");

        // Deploy the process on test engine - NOT needed handled by @ZeebeSpringTest
        //        DeploymentEvent event = zeebeClient.newDeployResourceCommand()
        //                .addResourceFromClasspath("my-process.bpmn")
        //                .send()
        //                .join();
        //        DeploymentAssert assertions = BpmnAssert.assertThat(event);

        // start a process instance
        ProcessInstanceEvent processInstance = zeebeClient
                .newCreateInstanceCommand() //
                .bpmnProcessId(ProcessConstants.PROCESS_NAME)
                .latestVersion() //
                .variables(Collections.emptyMap()) //
                .send()
                .join();

        // ProcessInstanceAssert assertions = BpmnAssert.assertThat(processInstance);

        waitForUserTaskAndComplete(
                processInstance,
                ProcessConstants.CUSTOMER_DETAILS_TASK_NAME,
                Map.of("firstName", "Mura", "lastName", "Karu", "licenceNumber", "12345678"),
                "start");

        waitForUserTaskAndComplete(
                processInstance,
                ProcessConstants.HANDOVER_VEHICLE_TASK_NAME,
                Map.of("allChecksDone", Boolean.FALSE), // Failed Handover
                "enter-customer-details");

        // zeebeTestEngine.waitForIdleState(Duration.ofSeconds(processTestTimeout));

        ZeebeTestThreadSupport.waitForProcessInstanceHasPassedElement(processInstance, "handover-vehicle");

        BpmnAssert.assertThat(processInstance)
                .hasPassedElement("handover-vehicle")
                .isWaitingAtElements("return-vehicle-to-depot")
                .isNotCompleted();

        BpmnAssert.assertThat(processInstance).isNotCompleted();
    }

    @Test
    @SneakyThrows
    @Order(3)
    public void testFailedFinaliseBooking() throws Exception {
        log.info("Running testFailedFinaliseBooking !!");

        // Deploy the process on test engine - NOT needed handled by @ZeebeSpringTest
        //        DeploymentEvent event = zeebeClient.newDeployResourceCommand()
        //                .addResourceFromClasspath("my-process.bpmn")
        //                .send()
        //                .join();
        //        DeploymentAssert assertions = BpmnAssert.assertThat(event);

        // start a process instance
        ProcessInstanceEvent processInstance = zeebeClient
                .newCreateInstanceCommand() //
                .bpmnProcessId(ProcessConstants.PROCESS_NAME)
                .latestVersion() //
                .variables(Collections.emptyMap()) //
                .send()
                .join();

        // ProcessInstanceAssert assertions = BpmnAssert.assertThat(processInstance);

        waitForUserTaskAndComplete(
                processInstance,
                ProcessConstants.CUSTOMER_DETAILS_TASK_NAME,
                Map.of("firstName", "Mura", "lastName", "Karu", "licenceNumber", ""), // Missing lence number
                "start");

        waitForUserTaskAndComplete(
                processInstance,
                ProcessConstants.HANDOVER_VEHICLE_TASK_NAME,
                Map.of("allChecksDone", Boolean.TRUE),
                "enter-customer-details");

        ZeebeTestThreadSupport.waitForProcessInstanceHasPassedElement(processInstance, "check-handover");

        ZeebeTestThreadSupport.hasProcessInstanceThrownError(processInstance, "finalise-booking");

        RecordStream recordStream =
                RecordStream.of(Objects.requireNonNull(zeebeTestEngine).getRecordStreamSource());

        Long count = StreamFilter.processInstance(recordStream)
                .withProcessInstanceKey(processInstance.getProcessInstanceKey())
                .withRejectionType(RejectionType.NULL_VAL)
                // .withElementId(elementId)
                // .withIntents(
                //        ProcessInstanceIntent.ELEMENT_COMPLETED, ProcessInstanceIntent.SEQUENCE_FLOW_TAKEN)
                .stream()
                .count();

        /*
        IncidentAssert assertions = BpmnAssert.assertThat(processInstance)
        		.extractingLatestIncident();

        StringAssert s = assertions.extractingErrorMessage();

        log.info(s.toString());
        */

        Stream<Record<JobRecordValue>> records = StreamFilter.jobRecords(recordStream)
                .withIntent(JobIntent.ERROR_THROWN)
                .withElementId("finalise-booking")
                // .withKey()withProcessInstanceKey(processInstance.getProcessInstanceKey())
                .stream();

        List<Record<JobRecordValue>> list = records.collect(Collectors.toList());

        Iterator<Record<JobRecordValue>> iter = list.iterator();
        int i = 1;
        while (iter.hasNext()) {
            Record<JobRecordValue> n = iter.next();
            log.info("" + i + "=" + n.getIntent());
            i++;
        }

        BpmnAssert.assertThat(processInstance)
                .hasPassedElement("check-handover")
                .isWaitingAtElements("unable-to-finalise-booking")
                .isNotCompleted();

        BpmnAssert.assertThat(processInstance).isNotCompleted();
    }

    @SneakyThrows
    public void waitForUserTaskAndComplete(
            ProcessInstanceEvent processInstance,
            String userTaskId,
            Map<String, Object> variables,
            String passedStage) {

        ZeebeTestThreadSupport.waitForProcessInstanceHasPassedElement(processInstance, passedStage);

        //        BpmnAssert.assertThat(processInstance)
        //                .hasPassedElement(passedStage)
        //                .isWaitingAtElements(waitingStage)
        //                // .hasNotPassedElement("enterr-customer-details")
        //                .isNotCompleted();

        // Now get all user tasks
        List<ActivatedJob> jobs = zeebeClient
                .newActivateJobsCommand()
                .jobType("io.camunda.zeebe:userTask")
                .maxJobsToActivate(1)
                .send()
                .join()
                .getJobs();

        // Should be only one
        assertTrue(jobs.size() > 0, "Job for user task '" + userTaskId + "' found " + jobs.size());
        ActivatedJob userTaskJob = jobs.get(0);
        // Make sure it is the right one
        if (userTaskId != null) {
            assertEquals(userTaskId, userTaskJob.getElementId());
        }

        // And complete it passing the variables
        if (variables != null && variables.size() > 0) {
            zeebeClient
                    .newCompleteCommand(userTaskJob.getKey())
                    .variables(variables)
                    .send()
                    .join();
        } else {
            zeebeClient.newCompleteCommand(userTaskJob.getKey()).send().join();
        }
    }
}
