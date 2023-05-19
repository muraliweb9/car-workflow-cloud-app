package com.interview.carworkflowcloud.repository;

import com.interview.carworkflowcloud.data.TaskDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void test() {

        TaskDetail td1 = TaskDetail.builder()
                .id(1L)
                .processDefinitionKey("PD1")
                .processInstanceKey("PI1")
                .taskDefinitionId("TD1")
                .build();
        TaskDetail td2 = TaskDetail.builder()
                .id(2L)
                .processDefinitionKey("PD2")
                .processInstanceKey("PI1")
                .taskDefinitionId("TD1")
                .build();
        TaskDetail td3 = TaskDetail.builder()
                .id(3L)
                .processDefinitionKey("PD1")
                .processInstanceKey("PI2")
                .taskDefinitionId("TD1")
                .build();
        TaskDetail td4 = TaskDetail.builder()
                .id(4L)
                .processDefinitionKey("PD1")
                .processInstanceKey("PI1")
                .taskDefinitionId("TD2")
                .build();

        taskRepository.save(td1);
        taskRepository.save(td2);
        taskRepository.save(td3);
        taskRepository.save(td4);

        long count =
                taskRepository
                        .findByProcessDefinitionKeyAndProcessInstanceKeyAndTaskDefinitionId("PD1", "PI1", "TD1")
                        .stream()
                        .count();
        Assertions.assertEquals(1, count);
    }
}
