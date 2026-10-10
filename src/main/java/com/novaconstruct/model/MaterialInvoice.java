package com.novaconstruct.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "material_invoices")
public class MaterialInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    private MaterialPurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "invoice_number", nullable = false, length = 100)
    private String invoiceNumber;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate = LocalDate.now();

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "material_description", nullable = false, length = 250)
    private String materialDescription;

    @Column(length = 100)
    private String quantity;

    @Column(name = "total_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "tax_amount", precision = 14, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "paid_amount", precision = 14, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "outstanding_balance", precision = 14, scale = 2)
    private BigDecimal outstandingBalance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private InvoiceStatusChoices status = InvoiceStatusChoices.UNPAID;

    @Column(name = "delivery_status", length = 30)
    private String deliveryStatus = "Delivered";

    @Column(name = "invoice_document")
    private String invoiceDocument;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoicePayment> payments = new ArrayList<>();

    public MaterialInvoice() {}

    @PrePersist
    public void onPrePersist() {
        if (outstandingBalance == null || outstandingBalance.compareTo(BigDecimal.ZERO) == 0) {
            this.outstandingBalance = this.totalAmount;
        }
    }

    public void recalculateStatusAndBalance() {
        BigDecimal sumPayments = BigDecimal.ZERO;
        for (InvoicePayment pm : payments) {
            if (!Boolean.TRUE.equals(pm.getIsReversed()) && pm.getAmount() != null) {
                sumPayments = sumPayments.add(pm.getAmount());
            }
        }
        this.paidAmount = sumPayments;
        BigDecimal diff = totalAmount.subtract(paidAmount);
        this.outstandingBalance = diff.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : diff;
        if (paidAmount.compareTo(BigDecimal.ZERO) == 0) {
            this.status = InvoiceStatusChoices.UNPAID;
        } else if (paidAmount.compareTo(totalAmount) >= 0) {
            this.status = InvoiceStatusChoices.PAID;
        } else {
            this.status = InvoiceStatusChoices.PARTIALLY_PAID;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public MaterialPurchaseOrder getPurchaseOrder() { return purchaseOrder; }
    public void setPurchaseOrder(MaterialPurchaseOrder purchaseOrder) { this.purchaseOrder = purchaseOrder; }

    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public LocalDate getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getMaterialDescription() { return materialDescription; }
    public void setMaterialDescription(String materialDescription) { this.materialDescription = materialDescription; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }

    public BigDecimal getOutstandingBalance() { return outstandingBalance; }
    public void setOutstandingBalance(BigDecimal outstandingBalance) { this.outstandingBalance = outstandingBalance; }

    public InvoiceStatusChoices getStatus() { return status; }
    public void setStatus(InvoiceStatusChoices status) { this.status = status; }

    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }

    public String getInvoiceDocument() { return invoiceDocument; }
    public void setInvoiceDocument(String invoiceDocument) { this.invoiceDocument = invoiceDocument; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public List<InvoicePayment> getPayments() { return payments; }
}
