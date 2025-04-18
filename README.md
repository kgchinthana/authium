# Authium🔐

A robust and extensible authentication and authorization backend built with Spring Boot. Perfect for real-world applications, AuthZen supports **JWT**, **OAuth2**, **role-based access**, **admin delegation**, and **full token lifecycle control**.

---

## 🌟 Key Features

- **User Registration & Login** (JWT-based)  
- **Refresh Token** handling (per user, revocable)  
- **GitHub OAuth2** login support  
- **Role-based Access Control** (User/Admin)  
- **Audit Logging** (for all sensitive actions)  
- **Admin Delegation** (promote/demote users)  
- **Token Revocation** on logout  
- **Account Lock/Unlock Management**  

---

## 🛠️ Tech Stack

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
| POST   | `/auth/refresh`       | Revoke refresh token                 |

### 🔐 Secured (Authenticated User)

| Method | Endpoint        | Description                |
|--------|-----------------|----------------------------|
| GET    | `/auth/me`      | Get logged-in user profile |
| PUT    | `/auth/update`  | Update profile             |
| POST   | `/auth/logout`  | Logout                     |

### 🛠️ Admin Only

| Method | Endpoint                 | Description                         |
|--------|--------------------------|-------------------------------------|
| GET    | `/admin/users`           | View all users                      |
| PUT    | `/admin/users/{id}/roles` | Update user roles                  |
| GET    | `/admin/audit-logs`      | View audit logs                     |
| GET    | `/admin/roles`           | List all available roles            |
| POST   | `/admin/delegate`        | Delegate admin role to another user |
| POST   | `/admin/users/roles`     | Update user roles                   |

---

## 🎓 Project Structure

```
Authium/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── yourorg/
│   │   │           └── authium/
│   │   │               ├── AuthiumApplication.java
│   │   │               ├── configs/
│   │   │               ├── constants/
│   │   │               ├── controllers/
│   │   │               ├── dtos/
│   │   │               ├── endpoints/
│   │   │               ├── exceptions/
│   │   │               ├── models/
│   │   │               ├── repositories/
│   │   │               ├── responses/
│   │   │               ├── security/
│   │   │               ├── services/
│   │   │               └── utils/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── static/
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── reset-password/
│   │       └── templates/
├── .env
├── .env.docker
├── .env.example
├── .gitignore
├── docker-compose.yml
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
git clone https://github.com/kgchinthana/authium.git
cd authium
```

### 2. Setup Environment Variables
Create `.env` file in root directory:
```yaml
  # Database Configuration
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/authium?createDatabaseIfNotExist=true
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=YOUR_DATABASE_PASSWORD_HERE

# Server Configuration
SERVER_PORT=8080
SPRING_APPLICATION_NAME=authium

# JWT Configuration
JWT_SECRET=YOUR_SECURE_JWT_SECRET_HERE
JWT_ACCESS_TOKEN_EXPIRY_MS=900000
JWT_REFRESH_TOKEN_EXPIRY_MS=604800000

# Mail Configuration
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=YOUR_EMAIL_HERE
MAIL_PASSWORD=YOUR_EMAIL_PASSWORD_HERE
EMAIL_FROM=noreply@yourdomain.com

# OAuth Provider Configuration (Multiple Providers)
GITHUB_CLIENT_ID=YOUR_GITHUB_CLIENT_ID_HERE
GITHUB_CLIENT_SECRET=YOUR_GITHUB_CLIENT_SECRET_HERE
GITHUB_REDIRECT_URI=http://localhost:8080/oauth/callback/github

GOOGLE_CLIENT_ID=YOUR_GOOGLE_CLIENT_ID_HERE
GOOGLE_CLIENT_SECRET=YOUR_GOOGLE_CLIENT_SECRET_HERE
GOOGLE_REDIRECT_URI=http://localhost:8080/oauth/callback/google

FACEBOOK_CLIENT_ID=YOUR_FACEBOOK_CLIENT_ID_HERE
FACEBOOK_CLIENT_SECRET=YOUR_FACEBOOK_CLIENT_SECRET_HERE
FACEBOOK_REDIRECT_URI=http://localhost:8080/oauth/callback/facebook

# Admin Configuration
ADMIN_EMAIL=admin@codex.com
ADMIN_USERNAME=admin
ADMIN_PASSWORD=YOUR_SECURE_ADMIN_PASSWORD_HERE
```

### 3. GitHub OAuth Setup
Register a GitHub OAuth App:
- Homepage: `http://localhost:8080`
- Callback: `http://localhost:8080/auth/oauth`

Set in `.env`:
```yaml
  GITHUB_CLIENT_ID=your_client_id
  GITHUB_CLIENT_SECRET=your_client_secret
  GITHUB_REDIRECT_URI=http://localhost:8080/oauth/callback
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

| Key                              | Description                          |
|----------------------------------|--------------------------------------|
| `SPRING_APPLICATION_NAME`        | Spring Boot application name         |
| `SPRING_DATASOURCE_URL`          | JDBC URL for MySQL database          |
| `SPRING_DATASOURCE_USERNAME`     | MySQL database username              |
| `SPRING_DATASOURCE_PASSWORD`     | MySQL database password              |
| `SERVER_PORT`                    | Application server port              |
| `JWT_SECRET`                     | JWT signing key                      |
| `JWT_ACCESS_TOKEN_EXPIRY_MS`     | Access token expiration in ms        |
| `JWT_REFRESH_TOKEN_EXPIRY_MS`    | Refresh token expiration in ms       |
| `MAIL_HOST`                      | SMTP server host                     |
| `MAIL_PORT`                      | SMTP server port                     |
| `MAIL_USERNAME`                  | Email service username               |
| `MAIL_PASSWORD`                  | Email service password               |
| `EMAIL_FROM`                     | Sender email address                 |
| `GITHUB_CLIENT_ID`               | GitHub OAuth client ID               |
| `GITHUB_CLIENT_SECRET`           | GitHub OAuth client secret           |
| `GITHUB_REDIRECT_URI`            | GitHub OAuth redirect URI            |
| `ADMIN_EMAIL`                    | Default admin email                  |
| `ADMIN_USERNAME`                 | Default admin username               |
| `ADMIN_PASSWORD`                 | Default admin password               |


---

## 📊 Database Schema

- `users`: user credentials and metadata  
- `roles`: available roles  
- `user_roles`: user-role associations  
- `refresh_tokens`: long-lived refresh tokens  
- `oauth_providers`: linked GitHub accounts  
- `audit_logs`: admin actions tracking  
- `email_tokens`: password reset tokens  
- `permissions`: defines fine-grained permissions  
- `role_permissions`: role-permissions associations  

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

For the open-source competition, check the [`issues`](https://github.com/kgchinthana/authzen/issues) tab for bugs to solve!

---

## 📃 License

Released under the [MIT License](LICENSE.md) © 2025 — Authium Team
