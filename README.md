# ServiceFlow ⚡

**Enterprise Service Request & Complaint Management System**

ServiceFlow is a robust, full-featured backend application built with **Spring Boot 3**, **Java 17**, and **Spring Security** with **JWT Authentication**. It provides seamless complaint tracking, ticket assignment, SLA management, and role-based access for Customers, Support Agents, and Administrators.

---

## 🚀 Features

- **Role-Based Access Control (RBAC)**: Supports `ROLE_ADMIN`, `ROLE_SUPPORT`, and `ROLE_CUSTOMER`.
- **JWT Stateless Authentication**: Secure token-based access with 24-hour expiration.
- **Service Request & Ticket Lifecycle**: Status transitions (`OPEN` → `ASSIGNED` → `IN_PROGRESS` → `RESOLVED` → `CLOSED`).
- **SLA Deadline Calculations**: Priority-based resolution deadlines (Critical: 2h, High: 4h, Medium: 24h, Low: 48h).
- **Interactive Web Portal**: Built-in dashboard on root (`http://localhost:8080/`) for one-click testing and demonstration.
- **Interactive Swagger / OpenAPI Docs**: Complete API exploration console.
- **Dual Database Profiles**:
  - `dev`: Fast in-memory H2 database with automatic schema creation and data seeding.
  - `prod`: High-performance MySQL database configuration.

---

## 🛠️ Tech Stack

- **Java**: OpenJDK 17
- **Framework**: Spring Boot 3.2.5
- **Security**: Spring Security 6 & JJWT 0.12.5
- **Persistence**: Spring Data JPA & Hibernate
- **Database**: H2 (In-memory dev) / MySQL 8 (Production)
- **API Documentation**: Springdoc OpenAPI v2.5.0 (Swagger UI)
- **Build Tool**: Apache Maven 3.9+

---

## 🚦 Quick Start

### 1. Prerequisites
- **JDK 17** installed and configured on your system.
- **Maven 3.9+** (optional if using pre-packaged jar).

### 2. Run with Startup Scripts
- **Windows (Command Prompt / Double Click)**:
  ```cmd
  run.bat
  ```
- **PowerShell**:
  ```powershell
  .\run.ps1
  ```

### 3. Build & Run Manually with Maven
```bash
cd backend
mvn clean package -DskipTests
java -jar target/serviceflow-backend-1.0.0.jar
```

---

## 🌐 Endpoints & URLs

| Service | URL | Notes |
| :--- | :--- | :--- |
| **Interactive Web Portal** | `http://localhost:8080/` | Full web UI with 1-click test logins |
| **Swagger UI Console** | `http://localhost:8080/swagger-ui/index.html` | Interactive API documentation |
| **OpenAPI Specification** | `http://localhost:8080/api-docs` | Raw OpenAPI v3 JSON spec |
| **H2 Database Console** | `http://localhost:8080/h2-console` | JDBC: `jdbc:h2:mem:serviceflow_db`, User: `sa` |
| **Health Check Endpoint** | `http://localhost:8080/api/health` | Live system health status |

---

## 👥 Pre-Seeded Test Accounts

| Role | Email | Password | Permissions |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin@serviceflow.com` | `Password@123` | Full access, view all tickets, manage categories |
| **Support Agent 1** | `support1@serviceflow.com` | `Password@123` | View assigned tickets, update status, resolve |
| **Support Agent 2** | `support2@serviceflow.com` | `Password@123` | View assigned tickets, update status, resolve |
| **Customer 1** | `alice@example.com` | `Password@123` | Create new tickets, track customer tickets |
| **Customer 2** | `bob@example.com` | `Password@123` | Create new tickets, track customer tickets |

---

## 📄 License
MIT License
