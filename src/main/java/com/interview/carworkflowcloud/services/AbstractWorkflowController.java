package com.interview.carworkflowcloud.services;

import com.interview.carworkflowcloud.data.ProcessInstanceEventDto;
import com.interview.carworkflowcloud.wrapper.ZeebeClientWrapper;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import java.util.HashMap;

public class AbstractWorkflowController {

    protected final ZeebeClientWrapper zeebeClient;

    protected final TaskListService taskListService;

    public AbstractWorkflowController(ZeebeClientWrapper zeebeClient, TaskListService taskListService) {
        this.zeebeClient = zeebeClient;
        this.taskListService = taskListService;
    }

    protected ProcessInstanceEventDto startProcess() {
        HashMap<String, Object> variables = new HashMap<String, Object>();

        ProcessInstanceEvent event = zeebeClient.newInstance(variables);
        return ProcessInstanceEventDto.builder()
                .processDefinitionKey(event.getProcessDefinitionKey())
                .bpmnProcessId(event.getBpmnProcessId())
                .version(event.getVersion())
                .processInstanceKey(event.getProcessInstanceKey())
                .build();
    }
}
