package com.interview.carworkflowcloud.services;

import com.interview.carworkflowcloud.consts.ProcessConstants;
import com.interview.carworkflowcloud.data.TaskDetail;
import com.interview.carworkflowcloud.repository.TaskRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "spring.application.custom.config", name = "taskService", havingValue = "repo")
public class TaskListRepositoryService implements TaskListService {

    @Autowired
    private TaskRepository taskRepository;

    public Optional<TaskDetail> getTaskDetails(String taskDefinitionId, String processInstanceKey) {

        Optional<TaskDetail> taskDetailsOpt =
                taskRepository.findByProcessDefinitionKeyAndTaskDefinitionIdAndProcessInstanceKey(
                        ProcessConstants.PROCESS_NAME, taskDefinitionId, processInstanceKey);

        return taskDetailsOpt;
    }
}
