# 🏗️ BuildTrack Pro — Real Estate Construction & Digital Work Diary System

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/Database-MySQL%208.0-blue.svg)](https://www.mysql.com/)
[![Thymeleaf](https://img.shields.io/badge/Template-Thymeleaf%20HTML5-green.svg)](https://www.thymeleaf.org/)
[![License](https://img.shields.io/badge/License-Enterprise%20Proprietary-navy.svg)](#)

An enterprise-grade, production-ready multi-project real estate construction management portal and digital work diary system built with **Java 17+**, **Spring Boot 3**, **Spring Data JPA**, **MySQL**, and **Thymeleaf**.

Designed with the **Nova Astro** architectural editorial aesthetic featuring clean typography (*Outfit* & *Plus Jakarta Sans*), generous whitespace, warm off-white tones, deep navy accents, interactive Chart.js visualizations, and real-time operational site controls.

---

## 📸 Overview & Features

- **Multi-Site Construction Management:** Monitor active projects, initial approved budgets, expenditure, burn rates, and progress % across sites.
- **Role-Based Access Control (RBAC):** Server-side authorization enforcing 7 operational roles (Super Admin, Company Admin, Project Manager, Accountant, Legal Officer, Site Supervisor, Read-only Auditor).
- **Daily Labour & Wage Diary:** Track daily attendance per trade gang (Masonry, Steel Fixing, etc.), daily rates, overtime hours, automatic gross wages, and outstanding dues.
- **Material Procurement & Invoice Tracking:** Record vendor purchase orders (POs), delivery bills, tax calculations, partial payments, and live balance recalculation.
- **Central Financial Ledger:** Double-entry transaction logging (`EXPENSE`, `PAYMENT_SUPPLIER`, `PAYMENT_LABOUR`, `ADVANCE`, `RECEIPT_CLIENT`).
- **Legal Compliance & Document Management:** Track building plan sanctions, NOC approvals, environmental permits, issuing authorities, and expiry dates.
- **Digital Site Work Diary:** Log daily site work summaries, weather conditions, temperature, and photographic evidence.
- **Weekly Work Schedule & Milestones:** Milestone planning, target completion dates, progress percentages, and delay tracking.

---

## 🛠️ Technology Stack

| Layer | Technology |
| :--- | :--- |
| **Backend Framework** | Java 17+ / 21+, Spring Boot 3.3.4 (Spring MVC, Spring Data JPA) |
| **Database Engine** | MySQL 8.0+ / PostgreSQL (with Hibernate DDL Auto Schema Generation) |
| **Frontend Template Engine** | Thymeleaf 3.1+ HTML5 |
| **UI Design System** | Bootstrap 5.3, Bootstrap Icons, Nova Architectural Theme (`/css/nova-theme.css`) |
| **Data Visualizations** | Chart.js 4.4+ |
| **Build System** | Apache Maven |

---

## 👥 Role-Based Access Control (RBAC) Matrix

The system enforces strict server-side authorization checks across 7 organizational roles:

| Role Name | Code | Scope & Capabilities |
| :--- | :--- | :--- |
| **Super Administrator** | `SUPER_ADMIN` | Global executive control across all projects, user approval governance, system settings. |
| **Company Administrator** | `COMPANY_ADMIN` | Executive portfolio visibility, corporate budget approvals, and staff assignments. |
| **Project Manager** | `PROJECT_MANAGER` | Operational control of assigned site projects, purchase order approvals, task scheduling. |
| **Accountant** | `ACCOUNTANT` | Financial ledger entries, payment disbursements (RTGS/Cheque/Cash), supplier dues tracking. |
| **Legal Officer** | `LEGAL_OFFICER` | Building plan sanctions, statutory NOC clearances, environmental permits, expiry alerts. |
| **Site Supervisor** | `SITE_SUPERVISOR` | Daily labour attendance logging, wage calculations, site photo logs, digital work diary entries. |
| **Read-only Auditor** | `AUDITOR` | Read-only inspection of ledgers, audit logs, compliance records, and management reports. |

---

## 🚀 Getting Started & Production Setup

### 1. Prerequisites
- **Java Development Kit (JDK):** Version 17 or higher (`java -version`)
- **Apache Maven:** Version 3.8+ (`mvn -version`)
- **Database Server:** MySQL 8.0+ or PostgreSQL

### 2. Database Configuration
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

### 4. Run Development Server
```bash
mvn spring-boot:run
```

Visit the application in your browser:  
👉 https://buildtrack-pro-esn8.onrender.com

---

## 🔒 Security & Environment Safeguards

Database configuration properties are parameterized using Spring SpEL expressions:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3306/novaconstruct...}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:root}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:root}
```

No hardcoded production secrets, demo credentials, or sensitive database passwords are saved in public source control commits.

---

## 📝 License
BuildTrack Pro — Enterprise Proprietary & Restricted Distribution.
