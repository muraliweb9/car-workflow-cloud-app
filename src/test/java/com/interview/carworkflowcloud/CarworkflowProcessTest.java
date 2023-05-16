package com.interview.carworkflowcloud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.interview.carworkflowcloud.consts.ProcessConstants;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.process.test.assertions.BpmnAssert;
import io.camunda.zeebe.spring.test.ZeebeSpringTest;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ZeebeSpringTest
@ActiveProfiles("test")
@Slf4j
public class CarworkflowProcessTest {

    @Autowired
    private ZeebeClient zeebeClient;

    @Autowired
    private ZeebeTestEngine zeebeTestEngine;

    @Test
    @SneakyThrows
    public void testStartProcess() throws Exception {
        log.info("Running testStartProcess");

        log.info("ZeebeClient [{}]", zeebeClient);

        log.info("ZeebeTestEngine [{}]", zeebeTestEngine);

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
                Collections.emptyMap(),
                "start",
                "enter-customer-details");
    }

    @SneakyThrows
    public void waitForUserTaskAndComplete(
            ProcessInstanceEvent processInstance,
            String userTaskId,
            Map<String, Object> variables,
            String passedStage,
            String waitingStage) {
        // Let the workflow engine do whatever it needs to do
        zeebeTestEngine.waitForIdleState(Duration.ofSeconds(10));

        BpmnAssert.assertThat(processInstance)
                .hasPassedElement("start")
                .isWaitingAtElements("enter-customer-details")
                // .hasNotPassedElement("enterr-customer-details")
                .isNotCompleted();

        // Now get all user tasks
        List<ActivatedJob> jobs = zeebeClient
                .newActivateJobsCommand()
                .jobType("io.camunda.zeebe:userTask")
                .maxJobsToActivate(1)
                .send()
                .join()
                .getJobs();

        // Should be only one
        assertTrue(jobs.size() > 0, "Job for user task '" + userTaskId + "' does not exist");
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
