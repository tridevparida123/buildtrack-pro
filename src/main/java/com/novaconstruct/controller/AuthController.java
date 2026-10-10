package com.novaconstruct.controller;

import com.novaconstruct.model.User;
import com.novaconstruct.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/accounts")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model) {
        if (session.getAttribute("userId") != null) {
            return "redirect:/projects";
        }
        return "accounts/login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            User user = userOpt.get();
            if (!Boolean.TRUE.equals(user.getIsApproved()) || !Boolean.TRUE.equals(user.getIsActive())) {
                redirectAttributes.addFlashAttribute("error", "Your account is pending administrator approval or inactive.");
                return "redirect:/accounts/login";
            }
            session.setAttribute("userId", user.getId());
            redirectAttributes.addFlashAttribute("success", "Welcome back, " + user.getFullName() + "!");
            return "redirect:/projects";
        }

        redirectAttributes.addFlashAttribute("error", "Invalid username or password credentials.");
        return "redirect:/accounts/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("info", "You have been logged out successfully.");
        return "redirect:/accounts/login";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/accounts/login";
        User user = userRepository.findById(userId).orElse(null);
        model.addAttribute("profileUser", user);
        return "accounts/profile";
    }

    @GetMapping("/users")
    public String userList(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/accounts/login";
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || (!user.isCompanyAdmin() && user.getPrimaryRole() != com.novaconstruct.model.RoleChoices.PROJECT_MANAGER)) {
            return "redirect:/projects";
        }
        model.addAttribute("usersList", userRepository.findAll());
        return "accounts/user_list";
    }
}
