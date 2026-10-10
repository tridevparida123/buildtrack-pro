package com.novaconstruct.repository;

import com.novaconstruct.model.MaterialPurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialPurchaseOrderRepository extends JpaRepository<MaterialPurchaseOrder, Long> {
    List<MaterialPurchaseOrder> findByProjectIdOrderByOrderDateDesc(Long projectId);
    Optional<MaterialPurchaseOrder> findByPoNumber(String poNumber);
}
