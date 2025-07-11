# ✈️ GIRA: Airport Complaint Management System

A robust, modern web application for managing passenger complaints and feedback at airports. Built with Java 21, Spring Boot, PostgreSQL, and Docker.

---

## 🚀 Features

- User registration, login, and profile management
- Secure authentication with role-based access (admin, user)
- Submit, track, and manage complaints
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

The application will be available at .

---

## ⚙️ Configuration

Edit `src/main/resources/application.properties` to adjust database, port, or security settings.

**Default Admin Credentials:**
- 

---

## 📚 API Documentation

-  Swagger UI: 

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