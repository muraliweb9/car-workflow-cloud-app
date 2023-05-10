package com.interview.carworkflowcloud.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetail {
    @Id
    private Long id;

    private String processId;

    private String processInstanceKey;

    private String taskDefinitionId;

    private String taskState;
}
