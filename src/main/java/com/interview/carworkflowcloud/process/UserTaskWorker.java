package com.interview.carworkflowcloud.process;

import com.interview.carworkflowcloud.data.TaskDetail;
import com.interview.carworkflowcloud.repository.TaskRepository;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserTaskWorker {

    @Autowired
    private TaskRepository taskRepository;

    @JobWorker(type = "io.camunda.zeebe:userTask", autoComplete = false)
    public void handleUserTask(final ActivatedJob job) {
        Long taskKey = job.getKey();
        String processId = job.getBpmnProcessId();
        String processInstanceKey = String.valueOf(job.getProcessInstanceKey());
        String taskDefinitionId = job.getElementId();

        TaskDetail taskDetail = TaskDetail.builder()
                .id(taskKey)
                .processDefinitionKey(processId)
                .taskDefinitionId(taskDefinitionId)
                .processInstanceKey(processInstanceKey)
                .build();

        taskRepository.save(taskDetail);
    }
}
