package com.interview.carworkflowcloud.services;

import com.interview.carworkflowcloud.consts.ProcessConstants;
import com.interview.carworkflowcloud.data.ProcessInstanceEventDto;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/carworkflowcloud")
@Validated
@Slf4j
public class WorkflowController {

    @Autowired
    private ZeebeClient zeebeClient;

    @PostMapping("/startProcess")
    public ProcessInstanceEventDto startProcess() {
        HashMap<String, Object> variables = new HashMap<String, Object>();
        //variables.put("automaticProcessing", true);
        //variables.put("someInput", "yeah");

        ProcessInstanceEvent event = zeebeClient.newCreateInstanceCommand() //
                .bpmnProcessId(ProcessConstants.PROCESS_NAME) //
                .latestVersion() //
                .variables(variables) //
                .send().join();
        return ProcessInstanceEventDto.builder()
                .processDefinitionKey(event.getProcessDefinitionKey())
                .bpmnProcessId(event.getBpmnProcessId())
                .version(event.getVersion())
                .processInstanceKey(event.getProcessInstanceKey())
                .build();
    }


}
