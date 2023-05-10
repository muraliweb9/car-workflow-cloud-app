package com.interview.carworkflowcloud.services;

import com.interview.carworkflowcloud.data.TaskDetail;
import java.util.Optional;

public interface TaskListService {

    public Optional<TaskDetail> getTaskDetails(String taskDefinitionId, String processInstanceKey);
}
