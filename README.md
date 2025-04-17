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
Authzen/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── yourorg/
│   │   │           └── authzen/
│   │   │               ├── AuthzenApplication.java
│   │   │               ├── config/
│   │   │               ├── constants/
│   │   │               ├── controller/
│   │   │               ├── dto/
│   │   │               ├── endpoint/
│   │   │               ├── exception/
│   │   │               ├── model/
│   │   │               ├── repository/
│   │   │               ├── response/
│   │   │               ├── security/
│   │   │               ├── service/
│   │   │               └── util/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── static/
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── reset-password/
│   │       └── templates/
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
git clone https://github.com/kgchinthana/authzen.git
cd authzen
```

### 2. Setup Environment Variables
Create `.env` file in root directory:
```yaml
  SPRING_APPLICATION_NAME=authzen
  SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/authzen?createDatabaseIfNotExist=true
  SPRING_DATASOURCE_USERNAME=root
  SPRING_DATASOURCE_PASSWORD=yourpassword
  SERVER_PORT=8080
  
  JWT_SECRET=your_secret_key
  JWT_ACCESS_TOKEN_EXPIRY_MS=900000
  JWT_REFRESH_TOKEN_EXPIRY_MS=604800000
  
  MAIL_HOST=smtp.gmail.com
  MAIL_PORT=587
  MAIL_USERNAME=youremail@gmail.com
  MAIL_PASSWORD=yourapppassword
  EMAIL_FROM=noreply@yourdomain.com
  
  GITHUB_CLIENT_ID=your_client_id
  GITHUB_CLIENT_SECRET=your_client_secret
  GITHUB_REDIRECT_URI=http://localhost:8080/oauth/callback
  
  ADMIN_EMAIL=admin@yourdomain.com
  ADMIN_USERNAME=admin
  ADMIN_PASSWORD=Admin@123
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

[MIT](LICENSE.md) © 2025 — AuthZen Team
