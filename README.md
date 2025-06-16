# Shopify-App 🛒

**Shopify-App** is a Java-based backend application designed to serve as an e-commerce management system. It provides core features such as user login, product management, payment processing, and purchase transaction tracking.

---

## ✨ Main Features

- 🔐 Login using JWT-based authentication
- 📦 Product Management (Create, Read, Update, Delete)
- 💳 Order Payment Processing
- 🧾 Purchase Transaction Recording
- 📜 REST API Documentation (optional: Swagger/OpenAPI)
- 🐳 Docker configuration for containerized deployment

---

## 🛠 Tech Stack

| Layer      | Teknologi               |
|------------|-------------------------|
| Backend    | Java 17, Spring Boot    |
| Database   | MySQL                   |
| Security   | JWT Authentication      |
| ORM        | Spring Data JPA         |
| Build Tool | Maven                   |
| Dev Tool   | Docker (Dockerfile)     |

---

## 🚀 Getting Started

### With Maven
```bash
./mvnw spring-boot:run
```

### With Docker
```
docker build -t shopify-app
```
```
docker run -p 8080:8080 shopify-app
```

📂 Project Structure
Shopify-App/

├── src/                  # Main application source code

├── .mvn/                 # Maven wrapper

├── Dockerfile            # Docker configuration

├── pom.xml               # Maven dependencies and plugins

├── README.md             # Project documentation


## 📬 Sample Endpoints

| Method | Endpoint                              | Description                        |
|--------|----------------------------------------|------------------------------------|
| POST   | `/api/v1/auth/register`               | Register a new user                |
| POST   | `/api/v1/auth/login`                  | Authenticate user and return JWT  |
| GET    | `/api/v1/customers`                   | Get list of all customers          |
| GET    | `/api/v1/customers/search`            | Search customers by query params  |
| POST   | `/api/v1/customers`                   | Add a new customer                 |
| GET    | `/api/v1/product`                     | Get list of all products           |
| GET    | `/api/v1/product/{id}`                | Get product details by ID          |
| POST   | `/api/v1/product`                     | Create a new product               |
| PUT    | `/api/v1/product/{id}`                | Update existing product by ID      |
| POST   | `/api/v1/payments`                    | Process a payment                  |
| POST   | `/api/v1/transaction`                 | Create a new purchase transaction  |
| GET    | `/api/v1/transaction/{id}`            | Get transaction details by ID      |
| GET    | `/api/v1/transaction`                 | Get all purchase transactions      |

📄 Lisensi

This project is intended for learning and personal development purposes.
