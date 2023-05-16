package com.interview.carworkflowcloud.process;

import com.interview.carworkflowcloud.data.TaskDetail;
import com.interview.carworkflowcloud.repository.TaskRepository;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserTaskWorker {

    private final TaskRepository taskRepository;

    public UserTaskWorker(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Subscribing to type = "io.camunda.zeebe:userTask" causes issues with ProcessTests
    // @JobWorker(type = "io.camunda.zeebe:userTask", name= "ABC", autoComplete = false)
    public void handleUserTask(final ActivatedJob job) {
        Long taskKey = job.getKey();
        String processDefinitionKey = String.valueOf(job.getProcessDefinitionKey());
        String processInstanceKey = String.valueOf(job.getProcessInstanceKey());
        String taskDefinitionId = job.getElementId();

        TaskDetail taskDetail = TaskDetail.builder()
                .id(taskKey)
                .processDefinitionKey(processDefinitionKey)
                .taskDefinitionId(taskDefinitionId)
                .processInstanceKey(processInstanceKey)
                .build();

        taskRepository.save(taskDetail);
    }
}
