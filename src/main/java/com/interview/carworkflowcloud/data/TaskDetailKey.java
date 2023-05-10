package com.interview.carworkflowcloud.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDetailKey {

    private String processInstanceKey;

    private String taskDefinitionId;
}
