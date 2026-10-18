# Employee Management System

The React frontend is in `ems-frontend`; the Spring Boot backend is in `ems-backened` (the existing folder spelling is intentional).

## Run locally

Requirements: Java 25, Node.js with npm, and PostgreSQL. Create a PostgreSQL database named `employee_db` before starting the backend.

In PowerShell, start the backend:

```powershell
cd ems-backened
$env:DB_PASSWORD = 'your-postgres-password'
.\mvnw.cmd spring-boot:run
```

The API runs at `http://localhost:8080/api/employees`. `DB_URL` and `DB_USERNAME` can override the database URL and user (defaults: `jdbc:postgresql://localhost:5432/employee_db` and `postgres`). An optional, ignored `ems-backened/config/application-local.properties` file can hold local database settings. Do not commit credentials. Spring Boot does not automatically load `.env` files.

In another terminal:

```powershell
cd ems-frontend
npm.cmd ci
npm.cmd run dev
```

Open `http://localhost:3000`. To change the API location, copy `ems-frontend/.env.example` to `ems-frontend/.env.local`, edit `VITE_API_BASE_URL`, and restart Vite. Vite environment values are public browser configuration; never put secrets there.

## Checks

```powershell
cd ems-backened
.\mvnw.cmd test
cd ../ems-frontend
npm.cmd run lint
npm.cmd run build
```

Backend tests use an isolated H2 database and cover CRUD, validation, duplicate emails, missing employees, client-supplied IDs, and CORS. Local runtime uses PostgreSQL. Hibernate's `ddl-auto=update` is configured for development.

The workspace currently has no Git repository. Each app's `.gitignore` excludes generated files; the backend also excludes its local credentials file.
