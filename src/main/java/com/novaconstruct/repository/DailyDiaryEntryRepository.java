package com.novaconstruct.repository;

import com.novaconstruct.model.DailyDiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyDiaryEntryRepository extends JpaRepository<DailyDiaryEntry, Long> {
    List<DailyDiaryEntry> findByProjectIdOrderByDateDescDayNumberDesc(Long projectId);
}
