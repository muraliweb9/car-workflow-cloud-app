package com.interview.carworkflowcloud.repository;


import com.interview.carworkflowcloud.data.TaskDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends CrudRepository<TaskDetails, String> {

    Optional<TaskDetails> findById(String id);

    Optional<TaskDetails> findByProcessIdAndTaskIdAndProcessInstanceId(
            String processId, String taskId, long processInstanceId);

}