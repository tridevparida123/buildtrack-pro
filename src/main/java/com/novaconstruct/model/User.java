package com.novaconstruct.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 150)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(length = 254)
    private String email;

    @Column(name = "first_name", length = 150)
    private String firstName;

    @Column(name = "last_name", length = 150)
    private String lastName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "primary_role", length = 30, nullable = false)
    private RoleChoices primaryRole = RoleChoices.SITE_SUPERVISOR;

    @Column(name = "is_approved")
    private Boolean isApproved = true;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_superuser")
    private Boolean isSuperuser = false;

    @Column(length = 150)
    private String organization;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMembership> memberships = new ArrayList<>();

    public User() {}

    public User(String username, String password, String email, String firstName, String lastName, RoleChoices primaryRole, String organization) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.primaryRole = primaryRole;
        this.organization = organization;
        this.isApproved = true;
        this.isActive = true;
    }

    // RBAC & Domain Helper Methods matching Django model logic
    public boolean isSuperAdmin() {
        return Boolean.TRUE.equals(isSuperuser) || primaryRole == RoleChoices.SUPER_ADMIN;
    }

    public boolean isCompanyAdmin() {
        return isSuperAdmin() || primaryRole == RoleChoices.COMPANY_ADMIN;
    }

    public boolean canManageProject() {
        if (!Boolean.TRUE.equals(isActive) || !Boolean.TRUE.equals(isApproved)) return false;
        return isCompanyAdmin() || primaryRole == RoleChoices.PROJECT_MANAGER;
    }

    public boolean canManageFinance() {
        if (!Boolean.TRUE.equals(isActive) || !Boolean.TRUE.equals(isApproved)) return false;
        return isCompanyAdmin() || primaryRole == RoleChoices.PROJECT_MANAGER || primaryRole == RoleChoices.ACCOUNTANT;
    }

    public boolean canAccessLegal() {
        if (!Boolean.TRUE.equals(isActive) || !Boolean.TRUE.equals(isApproved)) return false;
        return isCompanyAdmin() || primaryRole == RoleChoices.PROJECT_MANAGER || primaryRole == RoleChoices.LEGAL_OFFICER || primaryRole == RoleChoices.AUDITOR;
    }

    public String getFullName() {
        if (firstName != null && !firstName.isBlank()) {
            return (lastName != null && !lastName.isBlank()) ? firstName + " " + lastName : firstName;
        }
        return username;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public RoleChoices getPrimaryRole() { return primaryRole; }
    public void setPrimaryRole(RoleChoices primaryRole) { this.primaryRole = primaryRole; }

    public Boolean getIsApproved() { return isApproved; }
    public void setIsApproved(Boolean isApproved) { this.isApproved = isApproved; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Boolean getIsSuperuser() { return isSuperuser; }
    public void setIsSuperuser(Boolean isSuperuser) { this.isSuperuser = isSuperuser; }

    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }

    public List<ProjectMembership> getMemberships() { return memberships; }
    public void setMemberships(List<ProjectMembership> memberships) { this.memberships = memberships; }
}
