package com.novaconstruct.repository;

import com.novaconstruct.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMembershipRepository extends JpaRepository<ProjectMembership, Long> {
    List<ProjectMembership> findByProjectId(Long projectId);
    List<ProjectMembership> findByUserId(Long userId);
    Optional<ProjectMembership> findByUserIdAndProjectIdAndIsActiveTrue(Long userId, Long projectId);
}
