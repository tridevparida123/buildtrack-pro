package com.novaconstruct.service;

import com.novaconstruct.model.*;
import com.novaconstruct.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private SupplierRepository supplierRepository;
    @Autowired private MaterialPurchaseOrderRepository poRepository;
    @Autowired private MaterialInvoiceRepository invoiceRepository;
    @Autowired private InvoicePaymentRepository paymentRepository;
    @Autowired private LabourEntryRepository labourRepository;
    @Autowired private FinancialTransactionRepository transactionRepository;
    @Autowired private BudgetAllocationRepository budgetRepository;
    @Autowired private LegalDocumentRepository legalRepository;
    @Autowired private ProjectContactRepository contactRepository;
    @Autowired private DailyDiaryEntryRepository diaryRepository;
    @Autowired private WorkPlanTaskRepository planRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return; // Data already initialized
        }

        System.out.println("Initializing Nova Construct Java Demo Data...");

        // 1. Seed 7 RBAC Users
        User superAdmin = userRepository.save(new User("superadmin", "Admin@12345", "admin@novaconstruct.com", "Chief", "Administrator", RoleChoices.SUPER_ADMIN, "Nova Global Infrastructure"));
        User companyAdmin = userRepository.save(new User("companyadmin", "Admin@12345", "executive@novaconstruct.com", "Corporate", "Director", RoleChoices.COMPANY_ADMIN, "Nova Real Estate Holdings"));
        User pmLead = userRepository.save(new User("pm_lead", "Admin@12345", "pm@novaconstruct.com", "Vikram", "Sharma", RoleChoices.PROJECT_MANAGER, "Nova Construct Operations"));
        User accountant = userRepository.save(new User("accountant", "Admin@12345", "finance@novaconstruct.com", "Ananya", "Roy", RoleChoices.ACCOUNTANT, "Nova Finance & Auditing"));
        User legalOfficer = userRepository.save(new User("legal_officer", "Admin@12345", "legal@novaconstruct.com", "Rajesh", "Verma", RoleChoices.LEGAL_OFFICER, "Nova Compliance Counsel"));
        User supervisor = userRepository.save(new User("supervisor", "Admin@12345", "site@novaconstruct.com", "Suresh", "Kumar", RoleChoices.SITE_SUPERVISOR, "Nova Field Logistics"));
        User auditor = userRepository.save(new User("auditor", "Admin@12345", "audit@novaconstruct.com", "Meera", "Deshmukh", RoleChoices.AUDITOR, "Independent Site Audit Corp"));

        // 2. Seed Suppliers
        Supplier s1 = supplierRepository.save(new Supplier("Ultratech Cement & Concrete Ltd", "Amit Patel", "+91-9820012345", "sales@ultratech.com", "27AAACU1234F1Z1"));
        Supplier s2 = supplierRepository.save(new Supplier("Tata Tiscon Steel Works", "Rohan Mehta", "+91-9820054321", "orders@tatatiscon.com", "27AAACT5678G2Z2"));
        Supplier s3 = supplierRepository.save(new Supplier("Kajaria Ceramics & Sanitaryware", "Priya Nair", "+91-9820098765", "info@kajariaceramics.com", "27AAACK9988H3Z3"));

        // 3. Seed Construction Projects
        Project p1 = new Project();
        p1.setCode("PRJ-2026-001");
        p1.setName("Nova Sky Towers - Phase 1");
        p1.setProjectType("Commercial & High-Rise Residential");
        p1.setLocation("Central City Waterfront, Sector 45");
        p1.setAddress("Plot 12-14, Waterfront Boulevard, Sector 45, Metropolis");
        p1.setClientName("Apex Realty Group");
        p1.setClientContact("contact@apexrealty.com / +91-9800011122");
        p1.setApprovedInitialBudget(new BigDecimal("25000000.00")); // ₹2.5 Crore
        p1.setStartDate(LocalDate.of(2026, 1, 15));
        p1.setExpectedCompletionDate(LocalDate.of(2027, 12, 31));
        p1.setStatus(ProjectStatusChoices.ACTIVE);
        p1.setProgressPercentage(38);
        p1.setCreatedBy(pmLead);
        p1 = projectRepository.save(p1);

        Project p2 = new Project();
        p2.setCode("PRJ-2026-002");
        p2.setName("Horizon Green Commercial Park");
        p2.setProjectType("IT Park & Commercial Offices");
        p2.setLocation("Tech Corridor, Phase 3");
        p2.setAddress("Plot 88, Electronic City Expressway, Tech Corridor");
        p2.setClientName("Horizon Infrastructure Ltd");
        p2.setClientContact("build@horizoninfra.com");
        p2.setApprovedInitialBudget(new BigDecimal("42000000.00")); // ₹4.2 Crore
        p2.setStartDate(LocalDate.of(2026, 3, 1));
        p2.setExpectedCompletionDate(LocalDate.of(2028, 6, 30));
        p2.setStatus(ProjectStatusChoices.ACTIVE);
        p2.setProgressPercentage(22);
        p2.setCreatedBy(pmLead);
        p2 = projectRepository.save(p2);

        // 4. Seed Budget Allocations for P1
        budgetRepository.save(new BudgetAllocation(p1, "Foundation & Civil Structure", new BigDecimal("10000000.00"), "Concrete, TMT rebar, excavation"));
        budgetRepository.save(new BudgetAllocation(p1, "Labour & Site Personnel", new BigDecimal("6000000.00"), "Direct daily wage gangs & overtime"));
        budgetRepository.save(new BudgetAllocation(p1, "Finishing, Flooring & Joinery", new BigDecimal("4500000.00"), "Tiles, plaster, glazing, doors"));
        budgetRepository.save(new BudgetAllocation(p1, "Electrical, MEP & Plumbing", new BigDecimal("3000000.00"), "Conduits, wiring, sanitary fittings"));
        budgetRepository.save(new BudgetAllocation(p1, "Legal, Statutory & Permits", new BigDecimal("1500000.00"), "Municipal approvals & NOC clearances"));

        // 5. Seed Labour Entries
        LabourEntry l1 = new LabourEntry();
        l1.setProject(p1);
        l1.setDate(LocalDate.now().minusDays(2));
        l1.setCategory("Structural Brickwork & Concrete Gang");
        l1.setLocationFloor("Floor 14, Tower A");
        l1.setWorkersCount(25);
        l1.setWageRatePerDay(new BigDecimal("850.00"));
        l1.setOvertimeHours(new BigDecimal("12.50"));
        l1.setOvertimeRatePerHour(new BigDecimal("150.00"));
        l1.setPaidAmount(new BigDecimal("18000.00"));
        l1.setSupervisor(supervisor);
        l1.setNotes("Completed outer periphery masonry walling on Floor 14.");
        labourRepository.save(l1);

        LabourEntry l2 = new LabourEntry();
        l2.setProject(p1);
        l2.setDate(LocalDate.now().minusDays(1));
        l2.setCategory("Steel Fixing & Shuttering Crew");
        l2.setLocationFloor("Floor 15 Slab Layout");
        l2.setWorkersCount(18);
        l2.setWageRatePerDay(new BigDecimal("950.00"));
        l2.setOvertimeHours(new BigDecimal("8.00"));
        l2.setOvertimeRatePerHour(new BigDecimal("175.00"));
        l2.setPaidAmount(new BigDecimal("18500.00"));
        l2.setSupervisor(supervisor);
        l2.setNotes("Fixed TMT rebar grid layout for Floor 15 casting.");
        labourRepository.save(l2);

        // 6. Seed Purchase Orders & Invoices
        MaterialPurchaseOrder po1 = new MaterialPurchaseOrder();
        po1.setProject(p1);
        po1.setPoNumber("PO-2026-0089");
        po1.setSupplier(s1);
        po1.setCategory("Ready Mix Concrete & OPC 53 Grade");
        po1.setDescription("500 Bags OPC 53 Grade Cement + 40 cu.m RMC M30 grade concrete batching delivery");
        po1.setQuantity("500 Bags / 40 cu.m");
        po1.setEstimatedAmount(new BigDecimal("485000.00"));
        po1.setStatus(POStatusChoices.DELIVERED);
        po1.setOrderDate(LocalDate.now().minusDays(10));
        po1.setCreatedBy(pmLead);
        po1 = poRepository.save(po1);

        MaterialInvoice inv1 = new MaterialInvoice();
        inv1.setProject(p1);
        inv1.setPurchaseOrder(po1);
        inv1.setSupplier(s1);
        inv1.setInvoiceNumber("INV-UTC-99812");
        inv1.setInvoiceDate(LocalDate.now().minusDays(8));
        inv1.setDueDate(LocalDate.now().plusDays(22));
        inv1.setMaterialDescription("OPC 53 Grade Cement (500 Bags) & RMC Batching Delivery");
        inv1.setQuantity("500 Bags");
        inv1.setTotalAmount(new BigDecimal("485000.00"));
        inv1.setTaxAmount(new BigDecimal("87300.00"));
        inv1.setPaidAmount(new BigDecimal("300000.00"));
        inv1.setOutstandingBalance(new BigDecimal("185000.00"));
        inv1.setStatus(InvoiceStatusChoices.PARTIALLY_PAID);
        inv1.setCreatedBy(accountant);
        inv1 = invoiceRepository.save(inv1);

        InvoicePayment pay1 = new InvoicePayment();
        pay1.setProject(p1);
        pay1.setInvoice(inv1);
        pay1.setAmount(new BigDecimal("300000.00"));
        pay1.setPaymentDate(LocalDate.now().minusDays(3));
        pay1.setPaymentMethod(PaymentMethodChoices.BANK_TRANSFER);
        pay1.setReferenceNumber("UTR-HDFC-2026040987");
        pay1.setNotes("First tranche partial settlement via RTGS.");
        pay1.setRecordedBy(accountant);
        paymentRepository.save(pay1);

        // 7. Seed Financial Transactions
        FinancialTransaction t1 = new FinancialTransaction();
        t1.setProject(p1);
        t1.setTransactionRef("TXN-2026-00411");
        t1.setTransactionDate(LocalDate.now().minusDays(3));
        t1.setTransactionType("PAYMENT_SUPPLIER");
        t1.setCategory("Material Purchase Settlement");
        t1.setDescription("Partial RTGS payment for Cement Invoice #INV-UTC-99812");
        t1.setAmount(new BigDecimal("300000.00"));
        t1.setDebitAmount(new BigDecimal("300000.00"));
        t1.setRelatedInvoice(inv1);
        t1.setCreatedBy(accountant);
        transactionRepository.save(t1);

        // 8. Seed Legal Documents
        LegalDocument doc1 = new LegalDocument();
        doc1.setProject(p1);
        doc1.setTitle("Municipal Building Sanction Plan Approval");
        doc1.setDocumentType("Building Plan Sanction");
        doc1.setReferenceNumber("NOC-MC-2025/8892");
        doc1.setIssuingAuthority("Municipal Corporation Urban Planning Dept");
        doc1.setIssueDate(LocalDate.of(2025, 11, 10));
        doc1.setExpiryDate(LocalDate.of(2028, 11, 09));
        doc1.setStatus("Active & Approved");
        doc1.setUploadedBy(legalOfficer);
        legalRepository.save(doc1);

        // 9. Seed Project Contacts
        ProjectContact c1 = new ProjectContact();
        c1.setProject(p1);
        c1.setName("Ar. Sandeep Singhania");
        c1.setRoleAssignment("Principal Structural Architect");
        c1.setOrganization("Singhania & Partners Design Studio");
        c1.setPhone("+91-9811122334");
        c1.setEmail("sandeep@singhaniadesign.com");
        contactRepository.save(c1);

        // 10. Seed Work Plan Tasks
        WorkPlanTask wp1 = new WorkPlanTask();
        wp1.setProject(p1);
        wp1.setTitle("Floor 15 Slab Casting & Curing");
        wp1.setWorkArea("Tower A - Floor 15");
        wp1.setSupervisor(supervisor);
        wp1.setStartDate(LocalDate.now().minusDays(1));
        wp1.setTargetDate(LocalDate.now().plusDays(5));
        wp1.setProgressPercentage(45);
        wp1.setStatus("In Progress");
        planRepository.save(wp1);

        // 11. Seed Daily Work Diary Entry
        DailyDiaryEntry d1 = new DailyDiaryEntry();
        d1.setProject(p1);
        d1.setDate(LocalDate.now().minusDays(1));
        d1.setDayNumber(84);
        d1.setTitle("Floor 14 Masonry Walling & Floor 15 Shuttering Work");
        d1.setWeatherCondition("Clear & Sunny");
        d1.setTemperatureCelsius(31);
        d1.setWorkSummary("Site operations commenced at 08:00 AM. Masonry gang completed 120 sq.m perimeter walling on Floor 14. Steel fixing team completed rebar binding for Floor 15 slab grid. Quality inspection conducted by PM Lead at 03:00 PM.");
        d1.setSupervisor(supervisor);
        d1.setPhoto1Caption("Periphery wall brickwork on Floor 14");
        diaryRepository.save(d1);

        System.out.println("Nova Construct Java Demo Data Initialization Complete!");
    }
}
