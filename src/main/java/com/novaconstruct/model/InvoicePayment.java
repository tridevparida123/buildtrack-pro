package com.novaconstruct.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice_payments")
public class InvoicePayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private MaterialInvoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "payment_date")
    private LocalDate paymentDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 50)
    private PaymentMethodChoices paymentMethod = PaymentMethodChoices.BANK_TRANSFER;

    @Column(name = "reference_number", length = 100)
    private String referenceNumber;

    @Column(name = "receipt_document")
    private String receiptDocument;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorded_by_id")
    private User recordedBy;

    @Column(name = "is_reversed")
    private Boolean isReversed = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public InvoicePayment() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MaterialInvoice getInvoice() { return invoice; }
    public void setInvoice(MaterialInvoice invoice) { this.invoice = invoice; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public PaymentMethodChoices getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethodChoices paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public String getReceiptDocument() { return receiptDocument; }
    public void setReceiptDocument(String receiptDocument) { this.receiptDocument = receiptDocument; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public User getRecordedBy() { return recordedBy; }
    public void setRecordedBy(User recordedBy) { this.recordedBy = recordedBy; }

    public Boolean getIsReversed() { return isReversed; }
    public void setIsReversed(Boolean isReversed) { this.isReversed = isReversed; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
