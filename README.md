# Open Books - Small Social Network for Book Lovers

Open Books is a full-stack social network platform designed for book enthusiasts to discover, share, and manage their personal libraries. Built with a modern tech stack, it provides a seamless experience for users to track their reading progress, review books, and connect with other readers.

---

## 🚀 Features

- **User Authentication**: Secure JWT-based authentication and account activation via email.
- **Book Management**: Add, update, and manage your personal book collection.
- **Social Sharing**: Mark books as shareable and borrow books from other users.
- **Feedback & Ratings**: Rate and review books you've read.
- **Search & Discovery**: Discover new books through the global library.
- **Responsive UI**: A modern, mobile-friendly interface built with Angular and Bootstrap.
- **API Documentation**: Interactive API documentation powered by OpenAPI/Swagger.

---

## 🛠️ Tech Stack

### Backend
- **Framework**: Spring Boot 3.3.0
- **Language**: Java 22
- **Security**: Spring Security with JWT
- **Database**: MySQL
- **ORM**: Spring Data JPA
- **API Documentation**: SpringDoc OpenAPI (SwaggerUI)
- **Email**: Spring Boot Starter Mail

### Frontend
- **Framework**: Angular 18
- **Styling**: Bootstrap 5, FontAwesome 6
- **API Client**: Generated using `ng-openapi-gen`
- **Notifications**: ngx-toastr

---

## 📋 Prerequisites

Before you begin, ensure you have the following installed:
- [JDK 22](https://www.oracle.com/java/technologies/javase/jdk22-archive-downloads.html)
- [Node.js & npm](https://nodejs.org/) (Compatible with Angular 18)
- [MySQL Server](https://www.mysql.com/)
- [Maven](https://maven.apache.org/) (or use the provided wrapper)

---

## ⚙️ Getting Started

### 1. Database Setup
1. Create a MySQL database named `open_books`.
2. Update the `application.yml` or `application.properties` in `open-books-backend/src/main/resources` with your MySQL credentials.

### 2. Backend Setup
```bash
cd open-books-backend
./mvnw clean install
./mvnw spring-boot:run
```
The backend will be available at `http://localhost:8080`.
Access Swagger UI at: `http://localhost:8080/swagger-ui/index.html`

### 3. Frontend Setup
```bash
cd open-books-ui
npm install
npm run start
```
The frontend will be available at `http://localhost:4200`.

---

## 📖 API Documentation
Once the backend is running, you can explore the API endpoints using **Swagger UI**:
`http://localhost:8080/swagger-ui/index.html`

---

## 🤝 Contributing
Contributions are welcome! Please feel free to submit a Pull Request.

---

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
