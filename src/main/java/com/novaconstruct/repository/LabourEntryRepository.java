package com.novaconstruct.repository;

import com.novaconstruct.model.LabourEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabourEntryRepository extends JpaRepository<LabourEntry, Long> {
    List<LabourEntry> findByProjectIdOrderByDateDescCreatedAtDesc(Long projectId);
}
