package com.interview.carworkflowcloud.repository;

import com.interview.carworkflowcloud.data.TaskDetail;
import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends ListCrudRepository<TaskDetail, String> {

    Optional<TaskDetail> findById(String id);

    Optional<TaskDetail> findByProcessDefinitionKeyAndTaskDefinitionIdAndProcessInstanceKey(
            String processDefinitionKey, String taskDefinitionId, String processInstanceKey);
}
