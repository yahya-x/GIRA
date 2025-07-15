# ✈️ GIRA: Airport Complaint Management System

A robust, modern web application for managing passenger complaints and feedback at airports. Built with Java 21, Spring Boot, PostgreSQL, and Docker.

---

## 🚀 Features

- User registration, login, and profile management
- Secure authentication with JWT and role-based access (Admin, Agent, Passager)
- Admin can create users with any role and full profile
- Submit, track, and manage complaints (réclamations)
- File upload/download for complaint attachments
- Commenting and notification system
- Admin dashboard for complaint resolution and analytics
- RESTful API with DTO-based request/response models
- Dockerized PostgreSQL database for easy setup

---

## 🛠️ Tech Stack

- **Backend:** Java 21, Spring Boot, Spring Data JPA, Spring Security
- **Database:** PostgreSQL (Dockerized)
- **Build Tool:** Maven
- **Containerization:** Docker, Docker Compose

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

### Start the Database

```sh
docker compose up -d
```

### Build and Run the Application

```sh
./mvnw clean package
java -jar target/GIRA-0.0.1-SNAPSHOT.jar
```

The application will be available at http://localhost:8080

---

## ⚙️ Configuration

Edit `src/main/resources/application.properties` to adjust database, port, or security settings.

**Default Admin Credentials:**
- TODO: Set initial admin credentials or registration flow

---

## 📚 API Documentation

### Swagger UI
- [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- OpenAPI docs: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Authentication
- All endpoints (except `/api/v1/auth/**`, `/api/public/**`, `/swagger-ui/**`, `/v3/api-docs/**`) require JWT authentication.
- Use `/api/v1/auth/login` to obtain a JWT token.
- Pass the token in the `Authorization: Bearer <token>` header.

### Main Endpoints

#### User Management
- `POST /api/v1/auth/register` — Register as PASSAGER
- `POST /api/v1/auth/login` — Login and receive JWT
- `POST /api/users` — (Admin only) Create user with any role (PASSAGER, AGENT, ADMIN)

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

### Example Requests

#### Register User (PASSAGER)
```json
POST /api/v1/auth/register
{
  "email": "user@example.com",
  "password": "password123",
  "username": "JohnDoe"
}
```

#### Login
```json
POST /api/v1/auth/login
{
  "email": "user@example.com",
  "password": "password123"
}
```

#### Admin Create User
```json
POST /api/users
Authorization: Bearer <admin-token>
{
  "email": "agent@example.com",
  "password": "agentpass",
  "username": "Agent007",
  "role": "AGENT",
  "prenom": "James",
  "telephone": "+1234567890",
  "langue": "fr"
}
```

---

## 🤝 Contributing

Contributions are welcome! Please fork the repository and submit a pull request.

---

## 📝 License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

---

## 👤 Author

Mohamed yahya jabrane  
Since: 03/07/2025 