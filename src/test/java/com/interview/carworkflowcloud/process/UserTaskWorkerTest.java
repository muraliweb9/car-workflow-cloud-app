package com.interview.carworkflowcloud.process;

import com.interview.carworkflowcloud.consts.ProcessConstants;
import com.interview.carworkflowcloud.data.TaskDetail;
import com.interview.carworkflowcloud.repository.TaskRepository;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class UserTaskWorkerTest {

    @BeforeAll
    static void beforeAll() {}

    @Test
    void test1() {

        TaskRepository tasKRepo = Mockito.mock(TaskRepository.class);
        UserTaskWorker userTaskWorker = new UserTaskWorker(tasKRepo);

        ActivatedJob job = Mockito.mock(ActivatedJob.class);

        Mockito.when(job.getKey()).thenReturn(1L);
        Mockito.when(job.getProcessDefinitionKey()).thenReturn(2L);
        Mockito.when(job.getProcessInstanceKey()).thenReturn(3L);
        Mockito.when(job.getElementId()).thenReturn(ProcessConstants.CUSTOMER_DETAILS_TASK_NAME);

        userTaskWorker.handleUserTask(job);

        ArgumentCaptor<TaskDetail> parameterCaptor = ArgumentCaptor.forClass(TaskDetail.class);

        Mockito.verify(tasKRepo, Mockito.times(1)).save(Mockito.any(TaskDetail.class));
    }

    @Test
    void test2() {

        TaskRepository tasKRepo = Mockito.mock(TaskRepository.class);
        UserTaskWorker userTaskWorker = new UserTaskWorker(tasKRepo);

        ActivatedJob job = Mockito.mock(ActivatedJob.class);

        Mockito.when(job.getKey()).thenReturn(1L);
        Mockito.when(job.getProcessDefinitionKey()).thenReturn(2L);
        Mockito.when(job.getProcessInstanceKey()).thenReturn(3L);
        Mockito.when(job.getElementId()).thenReturn(ProcessConstants.CUSTOMER_DETAILS_TASK_NAME);

        userTaskWorker.handleUserTask(job);

        ArgumentCaptor<TaskDetail> parameterCaptor = ArgumentCaptor.forClass(TaskDetail.class);

        Mockito.verify(tasKRepo).save(parameterCaptor.capture());

        TaskDetail taskDetail = parameterCaptor.getValue();

        Assertions.assertEquals(1L, taskDetail.getId());
        Assertions.assertEquals("2", taskDetail.getProcessDefinitionKey());
        Assertions.assertEquals("3", taskDetail.getProcessInstanceKey());
        Assertions.assertEquals(ProcessConstants.CUSTOMER_DETAILS_TASK_NAME, taskDetail.getTaskDefinitionId());
    }
}
