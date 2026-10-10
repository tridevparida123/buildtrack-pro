package com.novaconstruct.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "labour_entries")
public class LabourEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private LocalDate date = LocalDate.now();

    @Column(nullable = false, length = 120)
    private String category;

    @Column(name = "location_floor", length = 150)
    private String locationFloor;

    @Column(name = "workers_count")
    private Integer workersCount = 1;

    @Column(name = "wage_rate_per_day", precision = 12, scale = 2)
    private BigDecimal wageRatePerDay = BigDecimal.ZERO;

    @Column(name = "overtime_hours", precision = 6, scale = 2)
    private BigDecimal overtimeHours = BigDecimal.ZERO;

    @Column(name = "overtime_rate_per_hour", precision = 10, scale = 2)
    private BigDecimal overtimeRatePerHour = BigDecimal.ZERO;

    @Column(name = "gross_wages", precision = 14, scale = 2)
    private BigDecimal grossWages = BigDecimal.ZERO;

    @Column(name = "paid_amount", precision = 14, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "outstanding_amount", precision = 14, scale = 2)
    private BigDecimal outstandingAmount = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    private User supervisor;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public LabourEntry() {}

    @PrePersist
    @PreUpdate
    public void calculateWages() {
        BigDecimal base = BigDecimal.valueOf(workersCount != null ? workersCount : 1)
                .multiply(wageRatePerDay != null ? wageRatePerDay : BigDecimal.ZERO);
        BigDecimal ot = (overtimeHours != null ? overtimeHours : BigDecimal.ZERO)
                .multiply(overtimeRatePerHour != null ? overtimeRatePerHour : BigDecimal.ZERO);
        this.grossWages = base.add(ot);
        BigDecimal paid = paidAmount != null ? paidAmount : BigDecimal.ZERO;
        BigDecimal diff = this.grossWages.subtract(paid);
        this.outstandingAmount = diff.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : diff;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLocationFloor() { return locationFloor; }
    public void setLocationFloor(String locationFloor) { this.locationFloor = locationFloor; }

    public Integer getWorkersCount() { return workersCount; }
    public void setWorkersCount(Integer workersCount) { this.workersCount = workersCount; }

    public BigDecimal getWageRatePerDay() { return wageRatePerDay; }
    public void setWageRatePerDay(BigDecimal wageRatePerDay) { this.wageRatePerDay = wageRatePerDay; }

    public BigDecimal getOvertimeHours() { return overtimeHours; }
    public void setOvertimeHours(BigDecimal overtimeHours) { this.overtimeHours = overtimeHours; }

    public BigDecimal getOvertimeRatePerHour() { return overtimeRatePerHour; }
    public void setOvertimeRatePerHour(BigDecimal overtimeRatePerHour) { this.overtimeRatePerHour = overtimeRatePerHour; }

    public BigDecimal getGrossWages() { return grossWages; }
    public void setGrossWages(BigDecimal grossWages) { this.grossWages = grossWages; }

    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }

    public BigDecimal getOutstandingAmount() { return outstandingAmount; }
    public void setOutstandingAmount(BigDecimal outstandingAmount) { this.outstandingAmount = outstandingAmount; }

    public User getSupervisor() { return supervisor; }
    public void setSupervisor(User supervisor) { this.supervisor = supervisor; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
