package com.novaconstruct.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "project_type", length = 100)
    private String projectType = "Commercial & Residential";

    @Column(length = 10)
    private String currency = "INR";

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 150)
    private String location;

    @Column(name = "client_name", length = 150)
    private String clientName;

    @Column(name = "client_contact", length = 100)
    private String clientContact;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "approved_initial_budget", precision = 15, scale = 2)
    private BigDecimal approvedInitialBudget = BigDecimal.ZERO;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "expected_completion_date")
    private LocalDate expectedCompletionDate;

    @Column(name = "actual_completion_date")
    private LocalDate actualCompletionDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private ProjectStatusChoices status = ProjectStatusChoices.ACTIVE;

    @Column(name = "progress_percentage")
    private Integer progressPercentage = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LabourEntry> labourEntries = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaterialInvoice> materialInvoices = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoicePayment> invoicePayments = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FinancialTransaction> financialTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BudgetAllocation> budgetAllocations = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LegalDocument> legalDocuments = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectContact> contacts = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyDiaryEntry> diaryEntries = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkPlanTask> planTasks = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMembership> memberships = new ArrayList<>();

    public Project() {}

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Calculated Domain Financial Aggregates
    public BigDecimal getRecordedExpenditure() {
        BigDecimal total = BigDecimal.ZERO;
        // 1. Labour gross wages
        for (LabourEntry entry : labourEntries) {
            if (entry.getGrossWages() != null) {
                total = total.add(entry.getGrossWages());
            }
        }
        // 2. Material Invoices (non-cancelled)
        for (MaterialInvoice invoice : materialInvoices) {
            if (invoice.getStatus() != InvoiceStatusChoices.CANCELLED && invoice.getTotalAmount() != null) {
                total = total.add(invoice.getTotalAmount());
            }
        }
        // 3. Direct general expenses
        for (FinancialTransaction tx : financialTransactions) {
            if ("EXPENSE".equals(tx.getTransactionType()) && tx.getRelatedInvoice() == null && tx.getRelatedLabour() == null && !Boolean.TRUE.equals(tx.getIsReversed())) {
                if (tx.getAmount() != null) total = total.add(tx.getAmount());
            }
        }
        return total;
    }

    public BigDecimal getAmountPaid() {
        BigDecimal total = BigDecimal.ZERO;
        // 1. Labour wages paid
        for (LabourEntry entry : labourEntries) {
            if (entry.getPaidAmount() != null) {
                total = total.add(entry.getPaidAmount());
            }
        }
        // 2. Material invoice payments
        for (InvoicePayment pm : invoicePayments) {
            if (!Boolean.TRUE.equals(pm.getIsReversed()) && pm.getAmount() != null) {
                total = total.add(pm.getAmount());
            }
        }
        // 3. Direct financial disbursements
        for (FinancialTransaction tx : financialTransactions) {
            if (("PAYMENT_SUPPLIER".equals(tx.getTransactionType()) || "PAYMENT_LABOUR".equals(tx.getTransactionType())) && tx.getRelatedInvoice() == null && tx.getRelatedLabour() == null && !Boolean.TRUE.equals(tx.getIsReversed())) {
                if (tx.getAmount() != null) total = total.add(tx.getAmount());
            }
        }
        return total;
    }

    public BigDecimal getOutstandingLiabilities() {
        BigDecimal expenditure = getRecordedExpenditure();
        BigDecimal paid = getAmountPaid();
        BigDecimal diff = expenditure.subtract(paid);
        return diff.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : diff;
    }

    public BigDecimal getRemainingBudget() {
        return approvedInitialBudget.subtract(getRecordedExpenditure());
    }

    public double getBudgetBurnRate() {
        if (approvedInitialBudget == null || approvedInitialBudget.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        BigDecimal expenditure = getRecordedExpenditure();
        BigDecimal rate = expenditure.divide(approvedInitialBudget, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        return Math.min(100.0, rate.setScale(1, RoundingMode.HALF_UP).doubleValue());
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getProjectType() { return projectType; }
    public void setProjectType(String projectType) { this.projectType = projectType; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getClientContact() { return clientContact; }
    public void setClientContact(String clientContact) { this.clientContact = clientContact; }

    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }

    public BigDecimal getApprovedInitialBudget() { return approvedInitialBudget; }
    public void setApprovedInitialBudget(BigDecimal approvedInitialBudget) { this.approvedInitialBudget = approvedInitialBudget; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getExpectedCompletionDate() { return expectedCompletionDate; }
    public void setExpectedCompletionDate(LocalDate expectedCompletionDate) { this.expectedCompletionDate = expectedCompletionDate; }

    public LocalDate getActualCompletionDate() { return actualCompletionDate; }
    public void setActualCompletionDate(LocalDate actualCompletionDate) { this.actualCompletionDate = actualCompletionDate; }

    public ProjectStatusChoices getStatus() { return status; }
    public void setStatus(ProjectStatusChoices status) { this.status = status; }

    public Integer getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Integer progressPercentage) { this.progressPercentage = progressPercentage; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<LabourEntry> getLabourEntries() { return labourEntries; }
    public List<MaterialInvoice> getMaterialInvoices() { return materialInvoices; }
    public List<InvoicePayment> getInvoicePayments() { return invoicePayments; }
    public List<FinancialTransaction> getFinancialTransactions() { return financialTransactions; }
    public List<BudgetAllocation> getBudgetAllocations() { return budgetAllocations; }
    public List<LegalDocument> getLegalDocuments() { return legalDocuments; }
    public List<ProjectContact> getContacts() { return contacts; }
    public List<DailyDiaryEntry> getDiaryEntries() { return diaryEntries; }
    public List<WorkPlanTask> getPlanTasks() { return planTasks; }
    public List<ProjectMembership> getMemberships() { return memberships; }
}
