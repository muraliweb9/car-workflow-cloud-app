package com.interview.carworkflowcloud.data;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProcessInstanceEventDto {

    long processDefinitionKey;
    String bpmnProcessId;
    int version;

    long processInstanceKey;

}
