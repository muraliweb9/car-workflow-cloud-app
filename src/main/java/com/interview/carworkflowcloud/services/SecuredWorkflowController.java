package com.interview.carworkflowcloud.services;

import com.interview.carworkflowcloud.consts.ProcessConstants;
import com.interview.carworkflowcloud.data.ProcessInstanceEventDto;
import com.interview.carworkflowcloud.wrapper.ZeebeClientWrapper;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secured/carworkflowcloud")
@Validated
@Slf4j
public class SecuredWorkflowController {

    private final ZeebeClientWrapper zeebeClient;

    private final TaskListService taskListService;

    public SecuredWorkflowController(ZeebeClientWrapper zeebeClient, TaskListService taskListService) {
        this.zeebeClient = zeebeClient;
        this.taskListService = taskListService;
    }

    @GetMapping("/name")
    public String name() {
        return ProcessConstants.PROCESS_NAME;
    }

    @PostMapping("/startProcess")
    public ProcessInstanceEventDto startProcess() {
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
