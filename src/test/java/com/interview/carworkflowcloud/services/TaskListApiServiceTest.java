package com.interview.carworkflowcloud.services;

import com.interview.carworkflowcloud.data.TaskDetail;
import com.interview.carworkflowcloud.data.TaskDetailKey;
import com.interview.carworkflowcloud.data.security.AccessToken;
import com.interview.carworkflowcloud.data.security.OAuthAudiance;
import com.interview.carworkflowcloud.services.config.ClusterDetails;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class TaskListApiServiceTest {

    @Test
    public void test() {
        String processDefinitionKey = "PD1";
        String taskDefinitionId = "TD1";
        String processInstanceKey = "PI1";
        String taskSearchUrl = "https://ont-1.tasklist.camunda.io/cluster-1234/v1/tasks/search";
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        ClusterDetails clusterDetails = Mockito.mock(ClusterDetails.class);
        AccessTokenCache accessTokenCache = Mockito.mock(AccessTokenCache.class);
        AccessToken accesToken = AccessToken.builder().accessToken("ABCD1234").build();
        Mockito.when(accessTokenCache.getAccessToken(OAuthAudiance.TASKLIST)).thenReturn(accesToken);
        Mockito.when(clusterDetails.getTaskSearchUrl()).thenReturn(taskSearchUrl);

        TaskDetailKey taskDetailKey = TaskDetailKey.builder()
                .processDefinitionKey(processDefinitionKey)
                .taskDefinitionId(taskDefinitionId)
                .processInstanceKey(processInstanceKey)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth("ABCD1234");

        HttpEntity<TaskDetailKey> httpEntity = new HttpEntity<>(taskDetailKey, headers);

        ResponseEntity<List<TaskDetail>> taskDetailsResp = new ResponseEntity<List<TaskDetail>>(
                Arrays.asList(TaskDetail.builder().id(12345L).build()), HttpStatusCode.valueOf(200));

        Mockito.when(restTemplate.exchange(
                        Mockito.eq(taskSearchUrl),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.eq(httpEntity),
                        Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(taskDetailsResp);

        TaskListApiService service = new TaskListApiService(restTemplate, clusterDetails, accessTokenCache);

        Optional<TaskDetail> taskDetails =
                service.getTaskDetails(processDefinitionKey, taskDefinitionId, processInstanceKey);

        Assertions.assertTrue(taskDetails.isPresent());
        Assertions.assertEquals(12345L, taskDetails.get().getId());
    }
}
