package com.interview.carworkflowcloud.wrapper;

import com.interview.carworkflowcloud.consts.ProcessConstants;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.CompleteJobResponse;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ZeebeClientWrapper {
    private ZeebeClient zeebeClient;

    public ZeebeClientWrapper(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    public CompleteJobResponse completeCommand(final ActivatedJob job, Map<String, Object> variables) {
        CompleteJobResponse response = completeCommand(job.getKey(), variables);

        return response;
    }

    public CompleteJobResponse completeCommand(final Long jobKey, Map<String, Object> variables) {
        CompleteJobResponse response = zeebeClient
                .newCompleteCommand(jobKey)
                .variables(variables)
                .send()
                .join(); // Blocking behaviour - limited for scalability

        return response;
    }

    public CompleteJobResponse completeCommand(final Long jobKey) {
        CompleteJobResponse response =
                zeebeClient.newCompleteCommand(jobKey).send().join(); // Blocking behaviour - limited for scalability

        return response;
    }

    public ProcessInstanceEvent newInstance(Map<String, Object> variables) {
        ProcessInstanceEvent event = zeebeClient
                .newCreateInstanceCommand()
                .bpmnProcessId(ProcessConstants.PROCESS_NAME)
                .latestVersion()
                .variables(variables)
                .send()
                .join(); // Blocking behaviour - limited for scalability

        return event;
    }

    public List<ActivatedJob> activateJobs() {
        List<ActivatedJob> jobs = zeebeClient
                .newActivateJobsCommand()
                .jobType("io.camunda.zeebe:userTask")
                .maxJobsToActivate(1)
                .send()
                .join()
                .getJobs();

        return jobs;
    }
}
