# 🏗️ ApexBuild — Construction & Enterprise Portal

An enterprise-grade, multi-project real estate construction management and digital work diary portal built with **Java 17+**, **Spring Boot 3**, **Spring Data JPA**, **MySQL**, and **Thymeleaf**.

Designed with the **Nova Astro** architectural editorial design system featuring clean typography, warm off-white tones, deep navy accents, and real-time operational dashboard capabilities.

---

## 🛠️ Technology Stack

- **Backend Framework:** Java 17+ / Java 21+, Spring Boot 3.3.4 (Spring MVC, Spring Data JPA)
- **Database Engine:** MySQL 8.0+ (via JDBC driver with Hibernate DDL auto-schema updating)
- **Frontend Template Engine:** Thymeleaf HTML5
- **UI Framework & Design Tokens:** Bootstrap 5.3, Bootstrap Icons, Nova Architectural Theme (`/css/nova-theme.css`)
- **Data Visualizations:** Chart.js 4.4+
- **Build System:** Apache Maven

---

## 👥 Role-Based Portals & Permissions

The application implements a 7-tier Role-Based Access Control (RBAC) authorization matrix:

| Portal & Role | Role Code | Scope & Operational Capabilities |
| :--- | :--- | :--- |
| **Owner / Super Admin** | `SUPER_ADMIN` | Global executive control across all projects, user approval governance, system settings. |
| **Owner / Company Admin** | `COMPANY_ADMIN` | Executive portfolio visibility, corporate budget approvals, and staff assignments. |
| **Manager / Project Lead** | `PROJECT_MANAGER` | Operational control of assigned site projects, purchase order approvals, task scheduling. |
| **Accountant Portal** | `ACCOUNTANT` | Financial ledger entries, payment disbursements (RTGS/Cheque/Cash), supplier dues. |
| **Legal Officer Portal** | `LEGAL_OFFICER` | Building plan sanctions, statutory NOC clearances, environmental permits, expiry alerts. |
| **Supervisor Portal** | `SITE_SUPERVISOR` | Daily labour attendance logging, wage calculations, site photo logs, digital work diary entries. |
| **Auditor Portal** | `AUDITOR` | Read-only inspection of ledgers, audit logs, compliance records, and management reports. |

---

## 🚀 Quick Start & Local Execution

### 1. Prerequisites
- **Java Development Kit (JDK):** Version 17 or higher (`java -version`)
- **Apache Maven:** Version 3.8+ (`mvn -version`)
- **MySQL Database Server:** Running on `localhost:3306`

### 2. Configure Database Connection
Set environment variables or update fallback values in `src/main/resources/application.properties`:

```bash
export SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/novaconstruct?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
export SPRING_DATASOURCE_USERNAME="root"
export SPRING_DATASOURCE_PASSWORD="your_mysql_password"
```

### 3. Build & Install Dependencies
```bash
mvn clean install
```

### 4. Launch Application
```bash
mvn spring-boot:run
```

Visit the application in your web browser at:  
👉 **`http://localhost:8080/accounts/login`**

---

## 🔑 Seeded Demo Credentials

All test accounts share the default password: **`Admin@12345`**

| Role | Username | Permissions Scope |
| :--- | :--- | :--- |
| **Super Administrator** | `superadmin` | Full Global Executive Rights |
| **Company Administrator** | `companyadmin` | Multi-Project Corporate View |
| **Project Manager** | `pm_lead` | Active Site Operational Control |
| **Accountant** | `accountant` | Ledgers, Invoices & Disbursements |
| **Legal Officer** | `legal_officer` | Permits, NOCs & Compliance |
| **Site Supervisor** | `supervisor` | Labour Logs & Daily Work Diary |
| **Auditor** | `auditor` | Read-only Audit Inspection |

---

## 🔒 Security & Environment Configuration

Database credentials and environment configuration are parameterized using Spring SpEL expressions:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3306/novaconstruct?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:root}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:root}
```

No plain-text production keys or sensitive database passwords are stored in public source control commits.

---

## 📝 License
Corporate Construction System — Enterprise Proprietary & Restricted Distribution.
