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

    // POST Handler for Adding Labour Entry
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

    // POST Handler for Adding Daily Work Diary Entry
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
