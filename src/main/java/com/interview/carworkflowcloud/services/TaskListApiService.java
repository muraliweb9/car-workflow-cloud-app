package com.interview.carworkflowcloud.services;

import com.interview.carworkflowcloud.data.TaskDetail;
import com.interview.carworkflowcloud.data.TaskDetailKey;
import com.interview.carworkflowcloud.data.TaskDetails;
import com.interview.carworkflowcloud.data.oauth.AccessTokenRequest;
import com.interview.carworkflowcloud.data.oauth.AccessTokenResponse;
import com.interview.carworkflowcloud.data.oauth.OAuthAudiance;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TaskListApiService {

    @Autowired
    public RestTemplate restTemplate;

    @Autowired
    private ClusterDetails clusterDetails;

    public TaskDetail getTaskDetails(String processInstanceKey, String taskDefinitionId) {

        AccessTokenRequest tokenRequest = AccessTokenRequest.from(clusterDetails, OAuthAudiance.TASKLIST);
        String uri = clusterDetails.getOAuthUrl();

        AccessTokenResponse accessTokenResponse =
                restTemplate.postForObject(uri, tokenRequest, AccessTokenResponse.class);
        String accessToken = accessTokenResponse.getAccessToken();

        // String accessToken =
        // "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IlFVVXdPVFpDUTBVM01qZEVRME0wTkRFelJrUkJORFk0T0RZeE1FRTBSa1pFUlVWRVF6bERNZyJ9.eyJodHRwczovL2NhbXVuZGEuY29tL29yZ0lkIjoiMjg5MzAyZWQtZjE3NC00NDg3LTgxYmQtYzdjMDM2MDAxMmJkIiwiaXNzIjoiaHR0cHM6Ly93ZWJsb2dpbi5jbG91ZC5jYW11bmRhLmlvLyIsInN1YiI6Im13bzkwdDJyMzE2MDd6MzZCTkg2OXRXRktCWDU1ajFXQGNsaWVudHMiLCJhdWQiOiJ0YXNrbGlzdC5jYW11bmRhLmlvIiwiaWF0IjoxNjgzNjQyMjMyLCJleHAiOjE2ODM3Mjg2MzIsImF6cCI6Im13bzkwdDJyMzE2MDd6MzZCTkg2OXRXRktCWDU1ajFXIiwic2NvcGUiOiIxMjM5YzY3Ny03MmZlLTRjMDEtYWE2OC1mOWU0ZWZkZWQ4MDIiLCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMifQ.OwOk4rfIJy3Z7D5h_T1EjUzfOeSSJQOs0cmmKbx9gXuEO2nT-9vkvjD6pOe0B50wQT7jyXkbkGbpNc-sJiFmV-JCzaevIN59-BIqyvVE_BiWTTY_RgQ2NkuWY4RQSDpyW-iZXSd62S08Vh0sFPRphjpkhcKrE18gMntXo_y4WmoEbjyMd8lfMsUMLBgBKx5-RnhrQFkl5x1zql7wJkHBKYx9HqmsDPqBeerF-rrjoyg5W8eWpn9mkXbaat1x51ddMI3yAfLG6_-eDwidNKbETF5a8aHOcatOHdwcNp_5BoI3N-qo4Ik53XPcex_vY2eKHSVbC80VSQwlpvrmfZwyxg";

        TaskDetailKey taskDetailKey = TaskDetailKey.builder()
                .processInstanceKey(processInstanceKey)
                .taskDefinitionId(taskDefinitionId)
                .build();
        String taskSearchUri = clusterDetails.getTaskSearchUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken);

        HttpEntity<TaskDetailKey> httpEntity = new HttpEntity<>(taskDetailKey, headers);

        TaskDetails taskDetails = restTemplate
                .postForEntity(taskSearchUri, httpEntity, TaskDetails.class)
                .getBody();
        return taskDetails.getTaskDetails().get(0);
    }
}
