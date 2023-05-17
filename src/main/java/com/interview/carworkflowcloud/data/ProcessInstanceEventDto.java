package com.interview.carworkflowcloud.data;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Builder
@Data
@Setter(AccessLevel.NONE)
public class ProcessInstanceEventDto {

    long processDefinitionKey;
    String bpmnProcessId;
    int version;
    long processInstanceKey;
}
