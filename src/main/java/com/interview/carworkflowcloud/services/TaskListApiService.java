package com.interview.carworkflowcloud.services;

import com.interview.carworkflowcloud.data.TaskDetail;
import com.interview.carworkflowcloud.data.TaskDetailKey;
import com.interview.carworkflowcloud.data.security.AccessToken;
import com.interview.carworkflowcloud.data.security.OAuthAudiance;
import com.interview.carworkflowcloud.services.config.ClusterDetails;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    private RestTemplate restTemplate;

    private ClusterDetails clusterDetails;

    private AccessTokenCache accessTokenCache;

    public TaskListApiService(
            RestTemplate restTemplate, ClusterDetails clusterDetails, AccessTokenCache accessTokenCache) {
        this.restTemplate = restTemplate;
        this.clusterDetails = clusterDetails;
        this.accessTokenCache = accessTokenCache;
    }

    public Optional<TaskDetail> getTaskDetails(
            String processDefinitionKey, String taskDefinitionId, String processInstanceKey) {

        AccessToken accessToken = accessTokenCache.getAccessToken(OAuthAudiance.TASKLIST);

        TaskDetailKey taskDetailKey = TaskDetailKey.builder()
                .processDefinitionKey(processDefinitionKey)
                .taskDefinitionId(taskDefinitionId)
                .processInstanceKey(processInstanceKey)
                .build();
        String taskSearchUri = clusterDetails.getTaskSearchUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken.getAccessToken());

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
