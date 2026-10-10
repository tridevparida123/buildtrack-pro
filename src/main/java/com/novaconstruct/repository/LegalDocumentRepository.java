package com.novaconstruct.repository;

import com.novaconstruct.model.LegalDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LegalDocumentRepository extends JpaRepository<LegalDocument, Long> {
    List<LegalDocument> findByProjectIdOrderByIssueDateDesc(Long projectId);
}
