package com.interview.carworkflowcloud.services;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.interview.carworkflowcloud.consts.ProcessConstants;
import com.interview.carworkflowcloud.data.CustomerDetails;
import com.interview.carworkflowcloud.data.TaskDetail;
import com.interview.carworkflowcloud.data.VehicleHandoverDetails;
import com.interview.carworkflowcloud.utils.TestUtils;
import com.interview.carworkflowcloud.wrapper.ZeebeClientWrapper;
import io.camunda.zeebe.client.api.response.CompleteJobResponse;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class WorkflowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ZeebeClientWrapper zeebeClientWrapper;

    @MockBean
    private TaskListService taskListService;

    @Test
    @SneakyThrows
    public void testStartProcess() {

        ProcessInstanceEvent event = Mockito.mock(ProcessInstanceEvent.class);
        when(event.getProcessDefinitionKey()).thenReturn(1234L);
        when(event.getProcessInstanceKey()).thenReturn(5678L);
        when(event.getVersion()).thenReturn(123);
        when(event.getBpmnProcessId()).thenReturn("sample-process");

        when(zeebeClientWrapper.newInstance(new HashMap<String, Object>())).thenReturn(event);
        this.mockMvc
                .perform(post("/carworkflowcloud/startProcess"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        content()
                                .string(
                                        containsString(
                                                "{\"processDefinitionKey\":1234,\"bpmnProcessId\":\"sample-process\",\"version\":123,\"processInstanceKey\":5678}")));
    }

    @Test
    @SneakyThrows
    public void testEnterCustomerDetails() {

        String processDefinitionKey = "1234";
        String processInstanceKey = "5678";
        String firstName = "m";
        String lastName = "k";
        String licenceNumber = "1234";
        Long taskId = 9999L;

        CompleteJobResponse response = Mockito.mock(CompleteJobResponse.class);
        CustomerDetails details = CustomerDetails.builder()
                .firstName(firstName)
                .lastName(lastName)
                .licenceNumber(licenceNumber)
                .build();

        when(taskListService.getTaskDetails(
                        Mockito.eq(processDefinitionKey),
                        Mockito.eq(ProcessConstants.CUSTOMER_DETAILS_TASK_NAME),
                        Mockito.eq(processInstanceKey)))
                .thenReturn(Optional.of(TaskDetail.builder().id(taskId).build()));

        when(zeebeClientWrapper.completeCommand(
                        taskId,
                        Map.of(
                                "firstName", firstName,
                                "lastName", lastName,
                                "licenceNumber", licenceNumber)))
                .thenReturn(response);

        this.mockMvc
                .perform(post("/carworkflowcloud/enterCustomerDetails/" + processDefinitionKey + "/"
                                + processInstanceKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(details)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("COMPLETED_OK")));
    }

    @Test
    @SneakyThrows
    public void testEnterCustomerDetailsFailed() {

        String processDefinitionKey = "1234";
        String processInstanceKey = "5678";
        String firstName = "m";
        String lastName = "k";
        String licenceNumber = "1234";
        Long taskId = 9999L;

        CompleteJobResponse response = Mockito.mock(CompleteJobResponse.class);
        CustomerDetails details = CustomerDetails.builder()
                .firstName(firstName)
                .lastName(lastName)
                .licenceNumber(licenceNumber)
                .build();

        when(taskListService.getTaskDetails(
                        Mockito.eq(processDefinitionKey),
                        Mockito.eq(ProcessConstants.CUSTOMER_DETAILS_TASK_NAME),
                        Mockito.eq(processInstanceKey)))
                .thenReturn(Optional.empty());

        when(zeebeClientWrapper.completeCommand(
                        taskId,
                        Map.of(
                                "firstName", firstName,
                                "lastName", lastName,
                                "licenceNumber", licenceNumber)))
                .thenReturn(response);

        this.mockMvc
                .perform(post("/carworkflowcloud/enterCustomerDetails/" + processDefinitionKey + "/"
                                + processInstanceKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(details)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("COMPLETED_FAILED")));
    }

    @Test
    @SneakyThrows
    public void testVehicleHandover() {

        String processDefinitionKey = "1234";
        String processInstanceKey = "5678";
        Long taskId = 9999L;

        CompleteJobResponse response = Mockito.mock(CompleteJobResponse.class);
        VehicleHandoverDetails details = VehicleHandoverDetails.builder()
                .cleaned(true)
                .fuelFull(true)
                .exteriorGood(true)
                .build();

        when(taskListService.getTaskDetails(
                        Mockito.eq(processDefinitionKey),
                        Mockito.eq(ProcessConstants.HANDOVER_VEHICLE_TASK_NAME),
                        Mockito.eq(processInstanceKey)))
                .thenReturn(Optional.of(TaskDetail.builder().id(taskId).build()));

        when(zeebeClientWrapper.completeCommand(
                        taskId, Map.of("allChecksDone", Boolean.valueOf(details.allChecksDone()))))
                .thenReturn(response);

        this.mockMvc
                .perform(post("/carworkflowcloud/vehicleHandover/" + processDefinitionKey + "/" + processInstanceKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(details)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("COMPLETED_OK")));
    }

    @Test
    @SneakyThrows
    public void testVehicleHandoverFailed() {

        String processDefinitionKey = "1234";
        String processInstanceKey = "5678";
        Long taskId = 9999L;

        CompleteJobResponse response = Mockito.mock(CompleteJobResponse.class);
        VehicleHandoverDetails details = VehicleHandoverDetails.builder()
                .cleaned(true)
                .fuelFull(true)
                .exteriorGood(true)
                .build();

        when(taskListService.getTaskDetails(
                        Mockito.eq(processDefinitionKey),
                        Mockito.eq(ProcessConstants.HANDOVER_VEHICLE_TASK_NAME),
                        Mockito.eq(processInstanceKey)))
                .thenReturn(Optional.empty());

        when(zeebeClientWrapper.completeCommand(
                        taskId, Map.of("allChecksDone", Boolean.valueOf(details.allChecksDone()))))
                .thenReturn(response);

        this.mockMvc
                .perform(post("/carworkflowcloud/vehicleHandover/" + processDefinitionKey + "/" + processInstanceKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(details)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("COMPLETED_FAILED")));
    }
}
