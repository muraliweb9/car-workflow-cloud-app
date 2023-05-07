package com.interview.carworkflowcloud.process;

import com.interview.carworkflowcloud.data.TaskDetails;
import com.interview.carworkflowcloud.repository.TaskRepository;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class EnterCustomerDetailsWorker {

    @Autowired
    private TaskRepository taskRepository;

    @JobWorker(type ="io.camunda.zeebe:userTask", autoComplete = false)
    public void enterCustomerDetails(final ActivatedJob job) {
        Long taskKey = job.getKey();
        String processId = job.getBpmnProcessId();
        Long processInstanceId = job.getProcessInstanceKey();
        String taskId = job.getElementId();

        TaskDetails taskDetails = TaskDetails.builder()
                .id(taskKey)
                .processId(processId)
                .taskId(taskId)
                .processInstanceId(processInstanceId)
                .build();

        taskRepository.save(taskDetails);
    }
}