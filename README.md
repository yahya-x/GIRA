# ✈️ GIRA: Airport Complaint Management System

A robust, modern web application for managing passenger complaints and feedback at airports. Built with Java 21, Spring Boot, PostgreSQL, and Docker.

---

## 🚀 Features

- User registration, login, and profile management
- Secure authentication with JWT and role-based access (Admin, Agent, Passager, Superviseur)
- Admin can create users with any role and full profile
- Submit, track, and manage complaints (réclamations)
- File upload/download for complaint attachments
- Commenting and notification system
- Admin dashboard for complaint resolution and analytics
- RESTful API with DTO-based request/response models
- Dockerized PostgreSQL database for easy setup
- Monitoring with Prometheus and Grafana

---

## 🛠️ Tech Stack

- **Backend:** Java 21, Spring Boot, Spring Data JPA, Spring Security
- **Database:** PostgreSQL (Dockerized)
- **Build Tool:** Maven
- **Containerization:** Docker, Docker Compose
- **Monitoring:** Prometheus, Grafana

---

## ⚡ Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- Docker & Docker Compose

### Clone the Repository

```sh
git clone https://github.com/yahya-x/GIRA.git
cd GIRA
```

### Environment Variables

Copy `.env.example` to `.env` in the project root and fill in the required values:

```sh
cp .env.example .env
```

**Required variables:**
- `DB_URL`, `DB_USER`, `DB_PASSWORD`
- `JWT_SECRET`
- `MAIL_HOST`, `MAIL_PORT`, `MAIL_USER`, `MAIL_PASS`
- `FRONTEND_URL`

> **Note:**
> The `.env` file **must** be present in the project root for Docker Compose and `gira-dev.sh` to work. If services fail to start, check that `.env` exists and is correctly configured.

### Start the Full Stack (Dev)

Use the provided script for local development:



### Build and Run the Application

```sh
cd backend
./mvnw clean package
java -jar target/GIRA-0.0.1-SNAPSHOT.jar
```


---

## ⚙️ Configuration

Edit `backend/src/main/resources/application.properties` to adjust database, port, or security settings. Most settings are loaded from environment variables (see `.env`).

**Default Admin Credentials:**
- TODO: Set initial admin credentials or registration flow

---

## 📚 API Documentation

### Swagger UI
- [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
- OpenAPI docs: [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)

### Authentication
- All endpoints (except `/api/v1/auth/**`, `/api/public/**`, `/swagger-ui/**`, `/v3/api-docs/**`) require JWT authentication.
- Use `/api/v1/auth/login` to obtain a JWT token.
- Pass the token in the `Authorization: Bearer <token>` header.

### Main Endpoints

#### User Management
- `POST /api/v1/auth/register` — Register as PASSAGER
- `POST /api/v1/auth/login` — Login and receive JWT
- `POST /api/users` — (Admin only) Create user with any role (PASSAGER, AGENT, ADMIN, SUPERVISEUR)

#### Complaints (Réclamations)
- `POST /api/reclamations` — Submit a complaint (authenticated)
- `POST /api/reclamations/search` — Search/filter complaints
- `PUT /api/reclamations/{id}` — Update complaint status

#### Files
- `POST /api/files/upload` — Upload attachment (PDF, image, etc.)
- `GET /api/files/{id}/download` — Download attachment

#### Notifications
- `GET /api/notifications` — List notifications
- `PUT /api/notifications/{id}/read` — Mark as read

#### Comments
- `POST /api/comments` — Add comment to complaint
- `GET /api/comments/{reclamationId}` — List comments for a complaint

#### Evaluation
- `POST /api/evaluations` — Submit evaluation after resolution

#### Dashboard
- `GET /api/dashboard/admin` — Admin dashboard (global stats)
- `GET /api/dashboard/agent` — Agent dashboard (personal stats)

---

## 🧪 Testing

- Test configs are in `backend/src/test/resources/application.properties`.
- Run tests with:

```sh
cd backend
./mvnw test
```

---

## 🐳 Docker Compose

- Compose files are in the `docker/` directory.
- Use `gira-dev.sh` for dev/monitoring, or run compose files directly.

---

## 🤝 Contributing

Contributions are welcome! Please fork the repository and submit a pull request.

---

## 📝 License

This project is licensed under the .

---

## 👤 Author

Mohamed yahya jabrane  
Since: 03/07/2025 