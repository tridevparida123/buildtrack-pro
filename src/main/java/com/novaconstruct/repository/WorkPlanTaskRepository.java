package com.novaconstruct.repository;

import com.novaconstruct.model.WorkPlanTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkPlanTaskRepository extends JpaRepository<WorkPlanTask, Long> {
    List<WorkPlanTask> findByProjectIdOrderByStartDateAscTargetDateAsc(Long projectId);
}
