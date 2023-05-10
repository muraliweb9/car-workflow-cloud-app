package com.interview.carworkflowcloud.services;

import com.interview.carworkflowcloud.data.TaskDetail;
import com.interview.carworkflowcloud.data.TaskDetailKey;
import com.interview.carworkflowcloud.data.oauth.AccessTokenRequest;
import com.interview.carworkflowcloud.data.oauth.AccessTokenResponse;
import com.interview.carworkflowcloud.data.oauth.OAuthAudiance;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@ConditionalOnProperty(prefix = "spring.application.custom.config", name = "taskService", havingValue = "api")
public class TaskListApiService implements TaskListService {

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    private ClusterDetails clusterDetails;

    public Optional<TaskDetail> getTaskDetails(String taskDefinitionId, String processInstanceKey) {

        AccessTokenRequest tokenRequest = AccessTokenRequest.from(clusterDetails, OAuthAudiance.TASKLIST);
        String uri = clusterDetails.getOAuthUrl();

        AccessTokenResponse accessTokenResponse =
                restTemplate.postForObject(uri, tokenRequest, AccessTokenResponse.class);
        String accessToken = accessTokenResponse.getAccessToken();

        TaskDetailKey taskDetailKey = TaskDetailKey.builder()
                .processInstanceKey(processInstanceKey)
                .taskDefinitionId(taskDefinitionId)
                .build();
        String taskSearchUri = clusterDetails.getTaskSearchUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken);

        HttpEntity<TaskDetailKey> httpEntity = new HttpEntity<>(taskDetailKey, headers);

        //        TaskDetail[] taskDetails = restTemplate
        //                .postForEntity(taskSearchUri, httpEntity, TaskDetail[].class)
        //                .getBody();
        //        return taskDetails[0];

        ResponseEntity<List<TaskDetail>> taskDetailsResp = restTemplate.exchange(
                taskSearchUri, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<TaskDetail>>() {});

        List<TaskDetail> taskListDetails = taskDetailsResp.getBody();

        return (taskListDetails != null && taskListDetails.size() > 0
                ? Optional.of(taskListDetails.get(0))
                : Optional.empty());
    }
}
