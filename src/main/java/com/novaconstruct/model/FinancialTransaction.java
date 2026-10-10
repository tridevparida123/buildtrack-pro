package com.novaconstruct.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_transactions")
public class FinancialTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "transaction_ref", nullable = false, unique = true, length = 50)
    private String transactionRef;

    @Column(name = "transaction_date")
    private LocalDate transactionDate = LocalDate.now();

    @Column(name = "transaction_type", nullable = false, length = 30)
    private String transactionType; // EXPENSE, PAYMENT_SUPPLIER, PAYMENT_LABOUR, RECEIPT_CLIENT, ADVANCE

    @Column(nullable = false, length = 100)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(precision = 14, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "debit_amount", precision = 14, scale = 2)
    private BigDecimal debitAmount = BigDecimal.ZERO;

    @Column(name = "credit_amount", precision = 14, scale = 2)
    private BigDecimal creditAmount = BigDecimal.ZERO;

    @Column(name = "payment_status", length = 30)
    private String paymentStatus = "POSTED";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_invoice_id")
    private MaterialInvoice relatedInvoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_labour_id")
    private LabourEntry relatedLabour;

    @Column(name = "is_reversed")
    private Boolean isReversed = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public FinancialTransaction() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public String getTransactionRef() { return transactionRef; }
    public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }

    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getDebitAmount() { return debitAmount; }
    public void setDebitAmount(BigDecimal debitAmount) { this.debitAmount = debitAmount; }

    public BigDecimal getCreditAmount() { return creditAmount; }
    public void setCreditAmount(BigDecimal creditAmount) { this.creditAmount = creditAmount; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public MaterialInvoice getRelatedInvoice() { return relatedInvoice; }
    public void setRelatedInvoice(MaterialInvoice relatedInvoice) { this.relatedInvoice = relatedInvoice; }

    public LabourEntry getRelatedLabour() { return relatedLabour; }
    public void setRelatedLabour(LabourEntry relatedLabour) { this.relatedLabour = relatedLabour; }

    public Boolean getIsReversed() { return isReversed; }
    public void setIsReversed(Boolean isReversed) { this.isReversed = isReversed; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
