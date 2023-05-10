package com.interview.carworkflowcloud.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDetail {
    @Id
    private Long id;

    private String processName;

    private String processDefinitionKey;

    private String processInstanceKey;

    private String taskDefinitionId;

    private String taskState;
}
