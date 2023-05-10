package com.interview.carworkflowcloud.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDetailKey {

    String processDefinitionKey;

    private String taskDefinitionId;

    private String processInstanceKey;
}
