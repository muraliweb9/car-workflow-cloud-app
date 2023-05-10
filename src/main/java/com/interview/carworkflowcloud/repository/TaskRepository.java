package com.interview.carworkflowcloud.repository;

import com.interview.carworkflowcloud.data.TaskDetail;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<TaskDetail, String> {

    Optional<TaskDetail> findById(String id);

    Optional<TaskDetail> findByProcessIdAndTaskDefinitionIdAndProcessInstanceKey(
            String processId, String taskDefinitionId, String processInstanceKey);
}
