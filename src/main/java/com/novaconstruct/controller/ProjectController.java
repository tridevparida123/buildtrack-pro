package com.novaconstruct.controller;

import com.novaconstruct.model.*;
import com.novaconstruct.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class ProjectController {

    @Autowired private ProjectRepository projectRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private LabourEntryRepository labourRepository;
    @Autowired private SupplierRepository supplierRepository;
    @Autowired private MaterialPurchaseOrderRepository poRepository;
    @Autowired private MaterialInvoiceRepository invoiceRepository;
    @Autowired private InvoicePaymentRepository paymentRepository;
    @Autowired private FinancialTransactionRepository transactionRepository;
    @Autowired private BudgetAllocationRepository budgetRepository;
    @Autowired private LegalDocumentRepository legalRepository;
    @Autowired private ProjectContactRepository contactRepository;
    @Autowired private DailyDiaryEntryRepository diaryRepository;
    @Autowired private WorkPlanTaskRepository planRepository;

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/projects";
    }

    @GetMapping("/projects")
    public String projectList(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/accounts/login";
        }
        User currentUser = userRepository.findById(userId).orElse(null);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("projects", projectRepository.findAllByOrderByUpdatedAtDesc());
        return "projects/project_list";
    }

    @GetMapping("/projects/create")
    public String projectCreateForm(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/accounts/login";
        User currentUser = userRepository.findById(userId).orElse(null);
        if (currentUser == null || !currentUser.canManageProject()) {
            return "redirect:/projects";
        }
        model.addAttribute("project", new Project());
        model.addAttribute("statusChoices", ProjectStatusChoices.values());
        return "projects/project_form";
    }

    @PostMapping("/projects/create")
    public String handleProjectCreate(@ModelAttribute Project project,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/accounts/login";
        User currentUser = userRepository.findById(userId).orElse(null);
        if (currentUser == null || !currentUser.canManageProject()) {
            return "redirect:/projects";
        }
        project.setCreatedBy(currentUser);
        projectRepository.save(project);
        redirectAttributes.addFlashAttribute("success", "Project " + project.getCode() + " created successfully!");
        return "redirect:/projects/" + project.getId();
    }

    @GetMapping("/projects/{id}")
    public String projectDetail(@PathVariable Long id,
                                @RequestParam(defaultValue = "dashboard") String tab,
                                HttpSession session,
                                Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/accounts/login";
        User currentUser = userRepository.findById(userId).orElse(null);
        Optional<Project> projectOpt = projectRepository.findById(id);
        if (projectOpt.isEmpty()) {
            return "redirect:/projects";
        }
        Project project = projectOpt.get();

        model.addAttribute("project", project);
        model.addAttribute("activeTab", tab);
        model.addAttribute("currentUser", currentUser);

        // Fetch Tab-Specific Collections
        model.addAttribute("labourEntries", labourRepository.findByProjectIdOrderByDateDescCreatedAtDesc(id));
        model.addAttribute("suppliers", supplierRepository.findAll());
        model.addAttribute("purchaseOrders", poRepository.findByProjectIdOrderByOrderDateDesc(id));
        model.addAttribute("invoices", invoiceRepository.findByProjectIdOrderByInvoiceDateDesc(id));
        model.addAttribute("payments", paymentRepository.findByProjectIdOrderByPaymentDateDesc(id));
        model.addAttribute("transactions", transactionRepository.findByProjectIdOrderByTransactionDateDesc(id));
        model.addAttribute("allocations", budgetRepository.findByProjectId(id));
        model.addAttribute("legalDocs", legalRepository.findByProjectIdOrderByIssueDateDesc(id));
        model.addAttribute("contacts", contactRepository.findByProjectIdOrderByNameAsc(id));
        model.addAttribute("diaryEntries", diaryRepository.findByProjectIdOrderByDateDescDayNumberDesc(id));
        model.addAttribute("planTasks", planRepository.findByProjectIdOrderByStartDateAscTargetDateAsc(id));

        return "projects/project_detail";
    }

    // 1. POST Handler for Adding Labour Entry
    @PostMapping("/projects/{id}/labour/create")
    public String createLabourEntry(@PathVariable Long id,
                                    @RequestParam String category,
                                    @RequestParam(required = false) String locationFloor,
                                    @RequestParam(defaultValue = "1") Integer workersCount,
                                    @RequestParam(defaultValue = "0.00") BigDecimal wageRatePerDay,
                                    @RequestParam(defaultValue = "0.00") BigDecimal overtimeHours,
                                    @RequestParam(defaultValue = "0.00") BigDecimal overtimeRatePerHour,
                                    @RequestParam(defaultValue = "0.00") BigDecimal paidAmount,
                                    @RequestParam(required = false) String notes,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/accounts/login";
        Project project = projectRepository.findById(id).orElseThrow();

        LabourEntry entry = new LabourEntry();
        entry.setProject(project);
        entry.setDate(LocalDate.now());
        entry.setCategory(category);
        entry.setLocationFloor(locationFloor);
        entry.setWorkersCount(workersCount);
        entry.setWageRatePerDay(wageRatePerDay);
        entry.setOvertimeHours(overtimeHours);
        entry.setOvertimeRatePerHour(overtimeRatePerHour);
        entry.setPaidAmount(paidAmount);
        entry.setNotes(notes);
        entry.setSupervisor(userRepository.findById(userId).orElse(null));
        labourRepository.save(entry);

        redirectAttributes.addFlashAttribute("success", "Labour log entry recorded successfully.");
        return "redirect:/projects/" + id + "?tab=labour-diary";
    }

    // 2. POST Handler for Adding Material Invoice
    @PostMapping("/projects/{id}/invoice/create")
    public String createInvoice(@PathVariable Long id,
                                @RequestParam String supplierName,
                                @RequestParam String invoiceNumber,
                                @RequestParam String materialDescription,
                                @RequestParam(defaultValue = "1") String quantity,
                                @RequestParam(defaultValue = "0.00") BigDecimal totalAmount,
                                @RequestParam(defaultValue = "0.00") BigDecimal taxAmount,
                                @RequestParam(required = false) String dueDateStr,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/accounts/login";
        Project project = projectRepository.findById(id).orElseThrow();

        Supplier supplier = supplierRepository.findByName(supplierName.trim())
                .orElseGet(() -> supplierRepository.save(new Supplier(supplierName.trim(), "", "", "", "")));

        MaterialInvoice invoice = new MaterialInvoice();
        invoice.setProject(project);
        invoice.setSupplier(supplier);
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setMaterialDescription(materialDescription);
        invoice.setQuantity(quantity);
        invoice.setTotalAmount(totalAmount);
        invoice.setTaxAmount(taxAmount);
        invoice.setPaidAmount(BigDecimal.ZERO);
        invoice.setOutstandingBalance(totalAmount);
        invoice.setInvoiceDate(LocalDate.now());
        if (dueDateStr != null && !dueDateStr.isBlank()) {
            invoice.setDueDate(LocalDate.parse(dueDateStr));
        } else {
            invoice.setDueDate(LocalDate.now().plusDays(30));
        }
        invoice.setStatus(InvoiceStatusChoices.UNPAID);
        invoice.setCreatedBy(userRepository.findById(userId).orElse(null));
        invoiceRepository.save(invoice);

        redirectAttributes.addFlashAttribute("success", "Material invoice #" + invoiceNumber + " added successfully.");
        return "redirect:/projects/" + id + "?tab=materials";
    }

    // 3. POST Handler for Recording Financial Transaction
    @PostMapping("/projects/{id}/transaction/create")
    public String createTransaction(@PathVariable Long id,
                                    @RequestParam String transactionType,
                                    @RequestParam String category,
                                    @RequestParam String description,
                                    @RequestParam(defaultValue = "0.00") BigDecimal amount,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/accounts/login";
        Project project = projectRepository.findById(id).orElseThrow();

        FinancialTransaction tx = new FinancialTransaction();
        tx.setProject(project);
        tx.setTransactionRef("TXN-" + (System.currentTimeMillis() % 1000000));
        tx.setTransactionDate(LocalDate.now());
        tx.setTransactionType(transactionType);
        tx.setCategory(category);
        tx.setDescription(description);
        tx.setAmount(amount);
        tx.setDebitAmount(amount);
        tx.setPaymentStatus("POSTED");
        tx.setCreatedBy(userRepository.findById(userId).orElse(null));
        transactionRepository.save(tx);

        redirectAttributes.addFlashAttribute("success", "Financial transaction recorded successfully.");
        return "redirect:/projects/" + id + "?tab=ledger";
    }

    // 4. POST Handler for Adding Budget Allocation
    @PostMapping("/projects/{id}/budget/create")
    public String createBudget(@PathVariable Long id,
                               @RequestParam String category,
                               @RequestParam(defaultValue = "0.00") BigDecimal allocatedAmount,
                               @RequestParam(required = false) String notes,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/accounts/login";
        Project project = projectRepository.findById(id).orElseThrow();

        BudgetAllocation allocation = new BudgetAllocation(project, category, allocatedAmount, notes);
        budgetRepository.save(allocation);

        redirectAttributes.addFlashAttribute("success", "Budget category '" + category + "' allocated successfully.");
        return "redirect:/projects/" + id + "?tab=budget";
    }

    // 5. POST Handler for Adding Legal Document
    @PostMapping("/projects/{id}/legal-doc/create")
    public String createLegalDoc(@PathVariable Long id,
                                 @RequestParam String title,
                                 @RequestParam String documentType,
                                 @RequestParam(required = false) String referenceNumber,
                                 @RequestParam(required = false) String issuingAuthority,
                                 @RequestParam(required = false) String expiryDateStr,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/accounts/login";
        Project project = projectRepository.findById(id).orElseThrow();

        LegalDocument doc = new LegalDocument();
        doc.setProject(project);
        doc.setTitle(title);
        doc.setDocumentType(documentType);
        doc.setReferenceNumber(referenceNumber);
        doc.setIssuingAuthority(issuingAuthority);
        doc.setIssueDate(LocalDate.now());
        if (expiryDateStr != null && !expiryDateStr.isBlank()) {
            doc.setExpiryDate(LocalDate.parse(expiryDateStr));
        }
        doc.setStatus("Active & Approved");
        doc.setUploadedBy(userRepository.findById(userId).orElse(null));
        legalRepository.save(doc);

        redirectAttributes.addFlashAttribute("success", "Legal document '" + title + "' recorded successfully.");
        return "redirect:/projects/" + id + "?tab=legal-docs";
    }

    // 6. POST Handler for Adding Project Contact / Stakeholder
    @PostMapping("/projects/{id}/contact/create")
    public String createContact(@PathVariable Long id,
                                @RequestParam String name,
                                @RequestParam String roleAssignment,
                                @RequestParam(required = false) String organization,
                                @RequestParam(required = false) String phone,
                                @RequestParam(required = false) String email,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/accounts/login";
        Project project = projectRepository.findById(id).orElseThrow();

        ProjectContact contact = new ProjectContact();
        contact.setProject(project);
        contact.setName(name);
        contact.setRoleAssignment(roleAssignment);
        contact.setOrganization(organization);
        contact.setPhone(phone);
        contact.setEmail(email);
        contactRepository.save(contact);

        redirectAttributes.addFlashAttribute("success", "Contact person '" + name + "' added successfully.");
        return "redirect:/projects/" + id + "?tab=contacts";
    }

    // 7. POST Handler for Adding Work Plan Task
    @PostMapping("/projects/{id}/work-plan/create")
    public String createWorkPlanTask(@PathVariable Long id,
                                     @RequestParam String title,
                                     @RequestParam String workArea,
                                     @RequestParam(defaultValue = "0") Integer progressPercentage,
                                     @RequestParam(defaultValue = "Planned") String status,
                                     @RequestParam(required = false) String targetDateStr,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/accounts/login";
        Project project = projectRepository.findById(id).orElseThrow();

        WorkPlanTask task = new WorkPlanTask();
        task.setProject(project);
        task.setTitle(title);
        task.setWorkArea(workArea);
        task.setStartDate(LocalDate.now());
        if (targetDateStr != null && !targetDateStr.isBlank()) {
            task.setTargetDate(LocalDate.parse(targetDateStr));
        } else {
            task.setTargetDate(LocalDate.now().plusDays(14));
        }
        task.setProgressPercentage(progressPercentage);
        task.setStatus(status);
        task.setSupervisor(userRepository.findById(userId).orElse(null));
        planRepository.save(task);

        redirectAttributes.addFlashAttribute("success", "Work plan task '" + title + "' added successfully.");
        return "redirect:/projects/" + id + "?tab=work-schedule";
    }

    // 8. POST Handler for Adding Daily Work Diary Entry
    @PostMapping("/projects/{id}/diary/create")
    public String createDiaryEntry(@PathVariable Long id,
                                   @RequestParam String title,
                                   @RequestParam(defaultValue = "1") Integer dayNumber,
                                   @RequestParam(defaultValue = "Clear") String weatherCondition,
                                   @RequestParam(defaultValue = "28") Integer temperatureCelsius,
                                   @RequestParam String workSummary,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/accounts/login";
        Project project = projectRepository.findById(id).orElseThrow();

        DailyDiaryEntry entry = new DailyDiaryEntry();
        entry.setProject(project);
        entry.setDate(LocalDate.now());
        entry.setDayNumber(dayNumber);
        entry.setTitle(title);
        entry.setWeatherCondition(weatherCondition);
        entry.setTemperatureCelsius(temperatureCelsius);
        entry.setWorkSummary(workSummary);
        entry.setSupervisor(userRepository.findById(userId).orElse(null));
        diaryRepository.save(entry);

        redirectAttributes.addFlashAttribute("success", "Daily work diary entry logged.");
        return "redirect:/projects/" + id + "?tab=daily-diary";
    }
}
