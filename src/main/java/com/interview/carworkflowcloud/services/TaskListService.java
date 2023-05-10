package com.interview.carworkflowcloud.services;

import com.interview.carworkflowcloud.data.TaskDetail;
import java.util.Optional;
import org.springframework.web.bind.annotation.PathVariable;

public interface TaskListService {

    public Optional<TaskDetail> getTaskDetails(
            @PathVariable String processDefinitionKey, String taskDefinitionId, String processInstanceKey);
}
