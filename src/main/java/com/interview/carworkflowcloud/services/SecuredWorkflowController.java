package com.interview.carworkflowcloud.services;

import com.interview.carworkflowcloud.consts.ProcessConstants;
import com.interview.carworkflowcloud.data.ProcessInstanceEventDto;
import com.interview.carworkflowcloud.wrapper.ZeebeClientWrapper;
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
public class SecuredWorkflowController extends AbstractWorkflowController {

    public SecuredWorkflowController(ZeebeClientWrapper zeebeClient, TaskListService taskListService) {
        super(zeebeClient, taskListService);
    }

    @GetMapping("/name")
    public String name() {
        return ProcessConstants.PROCESS_NAME;
    }

    @PostMapping("/startProcess")
    public ProcessInstanceEventDto startProcess() {
        return super.startProcess();
    }
}
