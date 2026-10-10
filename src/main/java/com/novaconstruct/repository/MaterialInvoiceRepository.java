package com.novaconstruct.repository;

import com.novaconstruct.model.MaterialInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialInvoiceRepository extends JpaRepository<MaterialInvoice, Long> {
    List<MaterialInvoice> findByProjectIdOrderByInvoiceDateDesc(Long projectId);
}
