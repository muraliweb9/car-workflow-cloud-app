package com.interview.carworkflowcloud.services;

import com.interview.carworkflowcloud.consts.ProcessConstants;
import com.interview.carworkflowcloud.data.CustomerDetails;
import com.interview.carworkflowcloud.data.ProcessInstanceEventDto;
import com.interview.carworkflowcloud.data.RestApiResult;
import com.interview.carworkflowcloud.data.TaskDetail;
import com.interview.carworkflowcloud.data.VehicleHandoverDetails;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.CompleteJobResponse;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carworkflowcloud")
@Validated
@Slf4j
public class WorkflowController {

    @Autowired
    private ZeebeClient zeebeClient;

    // @Autowired
    // private TaskRepository taskRepository;

    // @Autowired
    // private TaskListApiService taskListApiService;

    @Autowired
    private TaskListService taskListService;

    @PostMapping("/startProcess")
    public ProcessInstanceEventDto startProcess() {
        HashMap<String, Object> variables = new HashMap<String, Object>();

        ProcessInstanceEvent event = zeebeClient
                .newCreateInstanceCommand()
                .bpmnProcessId(ProcessConstants.PROCESS_NAME)
                .latestVersion()
                .variables(variables)
                .send()
                .join();
        return ProcessInstanceEventDto.builder()
                .processDefinitionKey(event.getProcessDefinitionKey())
                .bpmnProcessId(event.getBpmnProcessId())
                .version(event.getVersion())
                .processInstanceKey(event.getProcessInstanceKey())
                .build();
    }

    @PostMapping("enterCustomerDetails/{processInstanceId}")
    public RestApiResult enterCustomerDetails(
            @PathVariable String processInstanceId, @RequestBody CustomerDetails customerDetails) {
        String firstName = customerDetails.getFirstName();
        String lastName = customerDetails.getLastName();
        String licenceNumber = customerDetails.getLicenceNumber();

        Map<String, Object> variables = Map.of(
                "firstName", firstName,
                "lastName", lastName,
                "licenceNumber", licenceNumber);

        Optional<TaskDetail> taskDetailsOpt =
                taskListService.getTaskDetails(ProcessConstants.CUSTOMER_DETAILS_TASK_NAME, processInstanceId);

        if (taskDetailsOpt.isPresent()) {
            Long jobKey = taskDetailsOpt.get().getId();
            CompleteJobResponse response = zeebeClient
                    .newCompleteCommand(jobKey)
                    .variables(variables)
                    .send()
                    .join();
            return RestApiResult.COMPLETED_OK;
        } else {
            log.error(
                    "Unable to enter customer details task not found - processId [{}], " + "taskId [{}], "
                            + "processInstanceId [{}]",
                    ProcessConstants.PROCESS_NAME,
                    ProcessConstants.CUSTOMER_DETAILS_TASK_NAME,
                    processInstanceId);
            return RestApiResult.COMPLETED_FAILED;
        }
    }

    @PostMapping("vehicleHandover/{processInstanceId}")
    public RestApiResult vehicleHandover(
            @PathVariable String processInstanceId, @RequestBody VehicleHandoverDetails vehicleHandoverDetails) {

        boolean allChecksDone = vehicleHandoverDetails.allChecksDone();

        Map<String, Object> variables = Map.of("allChecksDone", Boolean.valueOf(allChecksDone));

        Optional<TaskDetail> taskDetailsOpt =
                taskListService.getTaskDetails(ProcessConstants.HANDOVER_VEHICLE_TASK_NAME, processInstanceId);

        if (taskDetailsOpt.isPresent()) {
            Long jobKey = taskDetailsOpt.get().getId();
            CompleteJobResponse response = zeebeClient
                    .newCompleteCommand(jobKey)
                    .variables(variables)
                    .send()
                    .join();
            return RestApiResult.COMPLETED_OK;
        } else {
            log.error(
                    "Unable to handover vehicle task not found - processId [{}], " + "taskId [{}], "
                            + "processInstanceId [{}]",
                    ProcessConstants.PROCESS_NAME,
                    ProcessConstants.CUSTOMER_DETAILS_TASK_NAME,
                    processInstanceId);
            return RestApiResult.COMPLETED_FAILED;
        }
    }
}
