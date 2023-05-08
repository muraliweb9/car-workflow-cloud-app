package com.interview.carworkflowcloud.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskVariable {

    private String key;
    private Object value;
}
