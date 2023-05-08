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
public class TaskDetails {
    @Id
    private Long id;

    private String processId;

    private String taskId;

    private long processInstanceId;


}
