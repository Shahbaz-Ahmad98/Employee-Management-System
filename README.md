# Employee Management System (EMS)

A full-stack **Employee Management System** built using **Spring Boot, React.js, and PostgreSQL**. The application provides a RESTful API for managing employee records and a responsive frontend for interacting with employee data.

The project demonstrates full-stack development using Java, Spring Boot, Spring Data JPA, React, and relational database integration.

## Tech Stack

| Category | Technologies |
|---|---|
| Backend | Java 25, Spring Boot, Spring Web |
| Frontend | React.js, JavaScript, Vite |
| Database | PostgreSQL |
| ORM | Spring Data JPA, Hibernate |
| API | RESTful APIs, JSON |
| Build Tools | Maven, npm |
| Testing | Spring Boot Tests, H2 |
| Version Control | Git, GitHub |

## Features

- Create and store employee records.
- Retrieve and display employee information.
- Update existing employee details.
- Delete employee records.
- Validate employee information before saving.
- Prevent duplicate employee email addresses.
- Handle requests for nonexistent employees.
- Connect React frontend with Spring Boot REST APIs.
- Persist employee records using PostgreSQL.
- Configure cross-origin communication using CORS.

## System Architecture

The application follows a **layered architecture**, separating frontend presentation, backend business logic, and database operations.

```text
          ┌──────────────────────────────┐
          │       React Frontend         │
          │    Components / UI / Forms   │
          │     http://localhost:3000    │
          └──────────────┬───────────────┘
                         │
                    HTTP / JSON
                         │
                         ▼
          ┌──────────────────────────────┐
          │    Spring Boot Backend       │
          │                              │
          │      REST Controller         │
          │              │               │
          │              ▼               │
          │       Service Layer          │
          │              │               │
          │              ▼               │
          │      Repository Layer        │
          │       (Spring Data JPA)      │
          └──────────────┬───────────────┘
                         │
                      Hibernate
                         │
                         ▼
          ┌──────────────────────────────┐
          │         PostgreSQL           │
          │        employee_db           │
          └──────────────────────────────┘
```

### Architecture Components

**1. Frontend Layer — React.js**

Provides the user interface for viewing and managing employee records. Communicates with the backend using HTTP requests.

**2. Controller Layer — Spring Boot**

Receives API requests, handles request validation, and returns appropriate HTTP responses.

**3. Service Layer**

Contains business logic, including employee creation, updates, deletion, and validation rules.

**4. Repository Layer — Spring Data JPA**

Handles database operations through JPA repositories and Hibernate.

**5. Database Layer — PostgreSQL**

Stores and manages employee records persistently.

## Project Structure

```text
Employee-Management-System/
│
├── ems-backened/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   └── test/
│   ├── config/
│   │   └── application-local.properties
│   │       (optional, ignored)
│   ├── mvnw
│   ├── mvnw.cmd
│   └── pom.xml
│
├── ems-frontend/
│   ├── src/
│   ├── public/
│   ├── .env.example
│   ├── package.json
│   └── package-lock.json
│
└── README.md
```

The structure above shows the main application directories; the optional local configuration file is not committed.

## Getting Started

### Prerequisites

Make sure the following software is installed:

- Java Development Kit (JDK 25)
- Node.js and npm
- PostgreSQL
- Git
- An IDE such as IntelliJ IDEA or VS Code

### 1. Clone the Repository

```bash
git clone https://github.com/Shahbaz-Ahmad98/Employee-Management-System.git

cd Employee-Management-System
```

### 2. Configure PostgreSQL

Start PostgreSQL and create a database:

```sql
CREATE DATABASE employee_db;
```

Default database configuration:

```properties
DB_URL=jdbc:postgresql://localhost:5432/employee_db
DB_USERNAME=postgres
```

Set your PostgreSQL password using an environment variable rather than committing it to the repository.

### 3. Run the Backend

Open PowerShell in the project directory:

```powershell
cd ems-backened

$env:DB_PASSWORD = 'your-postgres-password'

.\mvnw.cmd spring-boot:run
```

The backend runs on:

```text
http://localhost:8080
```

Employee API base URL:

```text
http://localhost:8080/api/employees
```

You can override `DB_URL` and `DB_USERNAME` through environment variables.

An optional ignored file, `ems-backened/config/application-local.properties`, can store local database settings if your Spring Boot configuration is set up to load it.

**Important:** Never commit database passwords, credentials, or secret environment variables. Spring Boot does not automatically load `.env` files.

### 4. Run the Frontend

Open another PowerShell terminal:

```powershell
cd ems-frontend

npm.cmd ci

npm.cmd run dev
```

Open the frontend in your browser:

```text
http://localhost:3000
```

### 5. Configure Frontend API URL

To change the backend API location, copy:

```text
ems-frontend/.env.example
```

to:

```text
ems-frontend/.env.local
```

Configure the frontend API URL using:

```properties
VITE_API_BASE_URL=http://localhost:8080
```

Use the URL format expected by your frontend API client. Restart the Vite development server after changing environment variables.

Vite environment variables are exposed to the browser. Never store passwords or secret tokens in variables prefixed with `VITE_`.

## API Overview

The backend exposes RESTful endpoints under:

```text
/api/employees
```

Typical employee CRUD operations:

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/employees` | Retrieve employees |
| GET | `/api/employees/{id}` | Retrieve employee by ID |
| POST | `/api/employees` | Create employee |
| PUT | `/api/employees/{id}` | Update employee |
| DELETE | `/api/employees/{id}` | Delete employee |

The route patterns above represent the standard CRUD API design; confirm individual mappings against your controller implementation.

## Testing and Quality Checks

### Backend Tests

```powershell
cd ems-backened

.\mvnw.cmd test
```

Backend tests use an isolated H2 database to avoid modifying the local PostgreSQL database.

Test coverage includes:

- Employee CRUD operations
- Input validation
- Duplicate email handling
- Missing employee handling
- Client-supplied employee IDs
- CORS configuration

### Frontend Checks

```powershell
cd ems-frontend

npm.cmd run lint

npm.cmd run build
```

These commands validate code quality and verify that the frontend builds successfully.

## Database Configuration

The application uses:

- **PostgreSQL** for local runtime data persistence.
- **H2** for isolated backend testing.
- **Hibernate / Spring Data JPA** for ORM and repository operations.

Hibernate is configured with:

```properties
spring.jpa.hibernate.ddl-auto=update
```

This is suitable for local development. For production environments, database migrations using Flyway or Liquibase are recommended.

## Author

**Md Shahbaz Ahmad**

Java Developer | Full-Stack Development

GitHub: https://github.com/Shahbaz-Ahmad98

Project Repository: https://github.com/Shahbaz-Ahmad98/Employee-Management-System

---

Built using Java, Spring Boot, React.js, and PostgreSQL.
