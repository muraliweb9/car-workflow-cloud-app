package com.interview.carworkflowcloud;

import static org.assertj.core.api.Assertions.assertThat;

import com.interview.carworkflowcloud.services.WorkflowController;
import io.camunda.zeebe.client.ZeebeClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CarWorkflowCloudAppApplicationTest {

    @Autowired
    private WorkflowController controller;

    @MockBean
    private ZeebeClient client;

    @Test
    public void contextLoads() {

        assertThat(controller).isNotNull();
    }
}
