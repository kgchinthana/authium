# AuthZen 🔐

A secure, extensible authentication and authorization backend for modern applications built with **Spring Boot**. Designed for scalability and real-world integration, this project includes **JWT**, **OAuth2**, **role-based access**, **admin controls**, and **token lifecycle management**.

---

## ✨ Features

- **User Registration & Login** (JWT-based)
- **Refresh Token** handling (per user, revocable)
- **GitHub OAuth2** login support
- **Role-based Access Control** (User/Admin)
- **Audit Logging** (for all sensitive actions)
- **Admin Delegation** (promote/demote users)
- **Token Revocation** on logout
- **Account Lock/Unlock Management**

---

## 📆 Technologies

- Java 21
- Spring Boot 3
- Spring Security
- JWT (Access & Refresh)
- OAuth2 (GitHub)
- MySQL
- Hibernate (JPA)
- Lombok
- MapStruct (optional)
- Docker (optional for deployment)

---

## 🌐 API Endpoints

### 🔓 Public

| Method | Endpoint              | Description                          |
|--------|-----------------------|--------------------------------------|
| POST   | `/auth/register`      | Register a new user                  |
| POST   | `/auth/login`         | Login with email & password          |
| POST   | `/auth/oauth`         | GitHub OAuth login                   |
| POST   | `/auth/reset-request` | Request password reset token         |
| POST   | `/auth/reset-password`| Reset password using token           |

### 🔐 Secured (Authenticated User)

| Method | Endpoint        | Description                |
|--------|-----------------|----------------------------|
| GET    | `/auth/me`      | Get logged-in user profile |
| PUT    | `/auth/update`  | Update profile             |
| POST   | `/auth/logout`  | Logout                     |
| POST   | `/auth/refresh` | Revoke refresh token       |

### 🛠️ Admin Only

| Method | Endpoint               | Description                         |
|--------|------------------------|-------------------------------------|
| GET    | `/admin/users`         | View all users                      |
| PUT    | `/admin/users/{id}/roles` | Update user roles to relevent user  |
| GET    | `/admin/audit-logs`    | View audit logs                     |
| GET    | `/admin/roles`         | List all available roles            |
| POST   | `/admin/delegate`      | Delegate admin role to another user |
| POST   | `/admin/users/roles`  | Update user roles                   |

---

## 🎓 Project Structure

```
Authzen/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── codejam/
│   │   │           └── codex/
│   │   │               └── authzen/
│   │   │                   ├── AuthzenApplication.java
│   │   │                   ├── config/               # Spring configuration beans
│   │   │                   ├── constants/            # Static final values (messages, keys, etc.)
│   │   │                   ├── controller/           # REST controllers (expose API endpoints)
│   │   │                   ├── dto/                  # Request/Response DTOs
│   │   │                   ├── endpoint/             # Enum or constants for endpoint URLs
│   │   │                   ├── exception/            # Custom exceptions and handlers
│   │   │                   ├── model/                # JPA entities
│   │   │                   ├── repository/           # Spring Data JPA interfaces
│   │   │                   ├── response/             # Custom response wrappers (if any)
│   │   │                   ├── security/             # JWT, filter chains, config, etc.
│   │   │                   ├── service/              # Business logic layer
│   │   │                   └── util/                 # Utility/helper classes
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── static/
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── reset-password/
│   │       └── templates/                           # Thymeleaf or HTML files
├── .env
├── .gitignore
├── Dockerfile
├── HELP.md
├── LICENSE.md
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md

```

---

## ⚙️ Getting Started

### 1. Clone the Repo
```bash
git clone https://github.com/your-org/authzen.git
cd authzen
```

### 2. Configure Database
Create a MySQL DB and update `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/authzen
    username: root
    password: password
```

### 3. GitHub OAuth Setup
Register a GitHub OAuth App:
- Homepage: `http://localhost:8080`
- Callback: `http://localhost:8080/auth/oauth`

Set in `application.yml`:
```yaml
oauth:
  github:
    client-id: your-client-id
    client-secret: your-client-secret
```

### 4. Run the Application
```bash
./mvnw spring-boot:run
```
Roles (`ROLE_USER`, `ROLE_ADMIN`) will be auto-initialized.

---

## 🌎 OAuth Flow
- Frontend gets GitHub access token from GitHub
- Sends it to `/auth/oauth`
- Server verifies token with GitHub & links to user

---

## 🚀 Example Request: Register
```http
POST /auth/register
Content-Type: application/json

{
  "username": "alice",
  "email": "alice@example.com",
  "password": "securepass"
}
```

---

## 📄 Environment Variables

| Key                        | Description                  |
|---------------------------|------------------------------|
| `GITHUB_CLIENT_ID`        | GitHub OAuth client ID       |
| `GITHUB_CLIENT_SECRET`    | GitHub OAuth client secret   |
| `JWT_SECRET`              | JWT signing key              |
| `JWT_EXPIRATION`          | Access token duration        |
| `REFRESH_TOKEN_EXPIRATION`| Refresh token duration       |

---

## 📊 Database Schema

- `users`: user credentials and metadata
- `roles`: available roles
- `user_roles`: user-role associations
- `refresh_tokens`: long-lived refresh tokens
- `oauth_providers`: linked GitHub accounts
- `audit_logs`: admin actions tracking
- `email_tokens`: password reset tokens

---

## 🛡️ Security Highlights

- BCrypt hashed passwords
- JWT with separate refresh token DB
- Role-checking via `@PreAuthorize` & `SecurityContext`
- Admin-only endpoints protected with `ROLE_ADMIN`
- GitHub token verification before linking

---

## 🔧 Contributing

1. Fork the repo
2. Fix a bug or implement a feature
3. Submit a pull request

For the open-source competition, check the [`issues`](https://github.com/your-org/authzen/issues) tab for bugs to solve!

---

## 📃 License

[MIT](LICENSE) © 2025 — CodeJam Codex Team