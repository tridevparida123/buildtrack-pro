package com.novaconstruct.repository;

import com.novaconstruct.model.BudgetAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetAllocationRepository extends JpaRepository<BudgetAllocation, Long> {
    List<BudgetAllocation> findByProjectId(Long projectId);
}
