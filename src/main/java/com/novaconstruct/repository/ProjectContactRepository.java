package com.novaconstruct.repository;

import com.novaconstruct.model.ProjectContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectContactRepository extends JpaRepository<ProjectContact, Long> {
    List<ProjectContact> findByProjectIdOrderByNameAsc(Long projectId);
}
