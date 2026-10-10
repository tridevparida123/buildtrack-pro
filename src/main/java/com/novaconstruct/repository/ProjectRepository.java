package com.novaconstruct.repository;

import com.novaconstruct.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByCode(String code);
    List<Project> findAllByOrderByUpdatedAtDesc();
}
