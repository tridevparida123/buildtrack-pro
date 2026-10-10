package com.novaconstruct.repository;

import com.novaconstruct.model.InvoicePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoicePaymentRepository extends JpaRepository<InvoicePayment, Long> {
    List<InvoicePayment> findByProjectIdOrderByPaymentDateDesc(Long projectId);
    List<InvoicePayment> findByInvoiceId(Long invoiceId);
}
