# Quiz & Notes Management Application - Backend

A production-ready Spring Boot REST API backend for a Full Stack Quiz & Notes Management Application.

## 📋 Table of Contents

- [Technology Stack](#technology-stack)
- [Features](#features)
- [Project Structure](#project-structure)
- [Database Setup](#database-setup)
- [Running the Application](#running-the-application)
- [Complete API Documentation](#complete-api-documentation)
- [Authentication Flow](#authentication-flow)
- [File Upload](#file-upload)
- [Error Handling](#error-handling)
- [CORS Configuration](#cors-configuration)
- [Session Configuration](#session-configuration)
- [Validation Rules](#validation-rules)

## 🔧 Technology Stack

- **Java 17** - Stable LTS version
- **Spring Boot 3.2.0** - Rapid backend development
- **Spring Web** - REST API creation
- **Spring Data JPA** - Database abstraction and ORM
- **Spring Security** - Password encryption (BCrypt only)
- **MySQL** - Relational database
- **Hibernate Validator** - Input validation
- **Multipart File Upload** - PDF notes upload

## ✨ Features

- ✅ Separate USER and ADMIN authentication
- ✅ HttpSession-based authentication (no JWT)
- ✅ Role-based access control
- ✅ Quiz management with random question selection
- ✅ PDF notes upload and download
- ✅ Category-based organization
- ✅ Quiz attempt tracking with scores
- ✅ Admin dashboard statistics
- ✅ Pagination and sorting
- ✅ Global exception handling
- ✅ Input validation
- ✅ File upload validation (PDF only, max 10MB)

## 📁 Project Structure

```
com.quizapp
├── controller   → API endpoints
│   ├── UserAuthController.java
│   ├── AdminAuthController.java
│   ├── AdminController.java
│   └── UserController.java
├── service      → Business logic
│   ├── AuthService.java
│   ├── CategoryService.java
│   ├── QuestionService.java
│   ├── QuizService.java
│   ├── NotesService.java
│   └── DashboardService.java
├── repository   → Database access
│   ├── UserRepository.java
│   ├── CategoryRepository.java
│   ├── QuestionRepository.java
│   ├── QuizAttemptRepository.java
│   └── NotesRepository.java
├── entity       → JPA entities
│   ├── User.java
│   ├── Category.java
│   ├── Question.java
│   ├── QuizAttempt.java
│   └── Notes.java
├── dto          → Request/Response models
│   ├── UserRegisterRequest.java
│   ├── LoginRequest.java
│   ├── AuthResponse.java
│   ├── CategoryRequest.java
│   ├── QuestionRequest.java
│   ├── QuizSubmissionRequest.java
│   ├── QuizResultResponse.java
│   └── DashboardStatsResponse.java
├── exception    → Custom errors
│   ├── UnauthorizedAccessException.java
│   ├── ResourceNotFoundException.java
│   ├── InvalidInputException.java
│   ├── FileUploadException.java
│   └── GlobalExceptionHandler.java
├── config       → Configuration
│   ├── SecurityConfig.java
│   ├── CorsConfig.java
│   └── SessionConfig.java
└── util         → Helpers
    └── FileUtil.java
```

## 🗄️ Database Setup

1. Create MySQL database:
```sql
CREATE DATABASE quiz_app_db;
```

2. Update `application.properties` with your MySQL credentials:
```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

3. Tables will be created automatically by Hibernate (`spring.jpa.hibernate.ddl-auto=update`)

## 🚀 Running the Application

1. **Prerequisites:**
   - Java 17 or higher
   - Maven 3.6+
   - MySQL 8.0+

2. **Build the project:**
```bash
mvn clean install
```

3. **Run the application:**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

---

## 📚 Complete API Documentation

### Base URL
```
http://localhost:8080
```

---

## 🔐 USER Authentication APIs

### 1. Register User
**Endpoint:** `POST /api/user/auth/register`  
**Authentication:** Not Required  
**Description:** Register a new user account

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Registration successful",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "user@example.com",
    "role": "USER"
  }
}
```

---

### 2. Login User
**Endpoint:** `POST /api/user/auth/login`  
**Authentication:** Not Required  
**Description:** Login as a user (creates session)

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "user@example.com",
    "role": "USER"
  }
}
```

---

### 3. Logout User
**Endpoint:** `POST /api/user/auth/logout`  
**Authentication:** Required (USER role)  
**Description:** Logout and invalidate session

**Response:**
```json
{
  "success": true,
  "message": "Logout successful"
}
```

---

## 👑 ADMIN Authentication APIs

### 4. Register Admin
**Endpoint:** `POST /api/admin/auth/register`  
**Authentication:** Not Required  
**Description:** Register a new admin account

**Request Body:**
```json
{
  "name": "Admin User",
  "email": "admin@example.com",
  "password": "admin123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Registration successful",
  "user": {
    "id": 1,
    "name": "Admin User",
    "email": "admin@example.com",
    "role": "ADMIN"
  }
}
```

---

### 5. Login Admin
**Endpoint:** `POST /api/admin/auth/login`  
**Authentication:** Not Required  
**Description:** Login as an admin (creates session)

**Request Body:**
```json
{
  "email": "admin@example.com",
  "password": "admin123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "user": {
    "id": 1,
    "name": "Admin User",
    "email": "admin@example.com",
    "role": "ADMIN"
  }
}
```

---

### 6. Logout Admin
**Endpoint:** `POST /api/admin/auth/logout`  
**Authentication:** Required (ADMIN role)  
**Description:** Logout and invalidate session

**Response:**
```json
{
  "success": true,
  "message": "Logout successful"
}
```

---

## 👤 USER APIs (Requires USER Role)

### 7. Get All Categories
**Endpoint:** `GET /api/user/categories`  
**Authentication:** Required (USER role)  
**Description:** Get all active categories

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "Mathematics",
      "active": true
    },
    {
      "id": 2,
      "name": "Science",
      "active": true
    }
  ]
}
```

---

### 8. Get Quiz Questions
**Endpoint:** `GET /api/user/quiz/questions`  
**Authentication:** Required (USER role)  
**Description:** Get random quiz questions for a category

**Query Parameters:**
- `categoryId` (required) - Category ID
- `limit` (optional, default: 10) - Number of questions

**Example:**
```
GET /api/user/quiz/questions?categoryId=1&limit=10
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "questionText": "What is 2+2?",
      "optionA": "3",
      "optionB": "4",
      "optionC": "5",
      "optionD": "6",
      "correctOption": "B",
      "category": {
        "id": 1,
        "name": "Mathematics"
      }
    }
  ],
  "totalQuestions": 10
}
```

---

### 9. Submit Quiz
**Endpoint:** `POST /api/user/quiz/submit`  
**Authentication:** Required (USER role)  
**Description:** Submit quiz answers and get results

**Request Body:**
```json
{
  "categoryId": 1,
  "answers": {
    "1": "A",
    "2": "B",
    "3": "C",
    "4": "D"
  },
  "timeTakenInSeconds": 300
}
```

**Response:**
```json
{
  "success": true,
  "message": "Quiz submitted successfully",
  "score": 8,
  "totalQuestions": 10,
  "percentage": 80.0,
  "timeTakenInSeconds": 300
}
```

---

### 10. Get Quiz History
**Endpoint:** `GET /api/user/quiz/history`  
**Authentication:** Required (USER role)  
**Description:** Get user's quiz attempt history with pagination

**Query Parameters:**
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 10) - Page size

**Example:**
```
GET /api/user/quiz/history?page=0&size=10
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "score": 8,
      "totalQuestions": 10,
      "percentage": 80.0,
      "timeTakenInSeconds": 300,
      "attemptedAt": "2024-01-15T10:30:00",
      "category": {
        "id": 1,
        "name": "Mathematics"
      }
    }
  ],
  "totalElements": 25,
  "totalPages": 3,
  "currentPage": 0
}
```

---

### 11. Get Notes by Category
**Endpoint:** `GET /api/user/notes`  
**Authentication:** Required (USER role)  
**Description:** Get all notes for a category with pagination

**Query Parameters:**
- `categoryId` (required) - Category ID
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 10) - Page size

**Example:**
```
GET /api/user/notes?categoryId=1&page=0&size=10
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "Mathematics Notes",
      "fileName": "math_notes.pdf",
      "uploadedAt": "2024-01-15T10:00:00",
      "category": {
        "id": 1,
        "name": "Mathematics"
      }
    }
  ],
  "totalElements": 15,
  "totalPages": 2,
  "currentPage": 0
}
```

---

### 12. Download Notes PDF
**Endpoint:** `GET /api/user/notes/{id}/download`  
**Authentication:** Required (USER role)  
**Description:** Download a PDF notes file

**Path Parameters:**
- `id` (required) - Notes ID

**Example:**
```
GET /api/user/notes/1/download
```

**Response:** PDF file download

---

## 🛡️ ADMIN APIs (Requires ADMIN Role)

### 13. Get Dashboard Statistics
**Endpoint:** `GET /api/admin/dashboard/stats`  
**Authentication:** Required (ADMIN role)  
**Description:** Get dashboard statistics

**Response:**
```json
{
  "success": true,
  "data": {
    "totalUsers": 150,
    "totalQuestions": 500,
    "totalQuizAttempts": 1200,
    "averageScore": 75.5
  }
}
```

---

### 14. Create Category
**Endpoint:** `POST /api/admin/categories`  
**Authentication:** Required (ADMIN role)  
**Description:** Create a new category

**Request Body:**
```json
{
  "name": "Mathematics"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Category created successfully",
  "data": {
    "id": 1,
    "name": "Mathematics",
    "active": true
  }
}
```

---

### 15. Get All Categories (Admin)
**Endpoint:** `GET /api/admin/categories`  
**Authentication:** Required (ADMIN role)  
**Description:** Get all active categories

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "Mathematics",
      "active": true
    },
    {
      "id": 2,
      "name": "Science",
      "active": true
    }
  ]
}
```

---

### 16. Toggle Category Status
**Endpoint:** `PUT /api/admin/categories/{id}/toggle`  
**Authentication:** Required (ADMIN role)  
**Description:** Enable/disable a category

**Path Parameters:**
- `id` (required) - Category ID

**Example:**
```
PUT /api/admin/categories/1/toggle
```

**Response:**
```json
{
  "success": true,
  "message": "Category status updated",
  "data": {
    "id": 1,
    "name": "Mathematics",
    "active": false
  }
}
```

---

### 17. Create Question
**Endpoint:** `POST /api/admin/questions`  
**Authentication:** Required (ADMIN role)  
**Description:** Create a new quiz question

**Request Body:**
```json
{
  "questionText": "What is 2+2?",
  "optionA": "3",
  "optionB": "4",
  "optionC": "5",
  "optionD": "6",
  "correctOption": "B",
  "categoryId": 1
}
```

**Response:**
```json
{
  "success": true,
  "message": "Question created successfully",
  "data": {
    "id": 1,
    "questionText": "What is 2+2?",
    "optionA": "3",
    "optionB": "4",
    "optionC": "5",
    "optionD": "6",
    "correctOption": "B",
    "active": true,
    "createdAt": "2024-01-15T10:00:00",
    "category": {
      "id": 1,
      "name": "Mathematics"
    }
  }
}
```

---

### 18. Get Questions by Category
**Endpoint:** `GET /api/admin/questions`  
**Authentication:** Required (ADMIN role)  
**Description:** Get questions for a category with pagination

**Query Parameters:**
- `categoryId` (required) - Category ID
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 10) - Page size

**Example:**
```
GET /api/admin/questions?categoryId=1&page=0&size=10
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "questionText": "What is 2+2?",
      "optionA": "3",
      "optionB": "4",
      "optionC": "5",
      "optionD": "6",
      "correctOption": "B",
      "active": true,
      "createdAt": "2024-01-15T10:00:00"
    }
  ],
  "totalElements": 50,
  "totalPages": 5,
  "currentPage": 0
}
```

---

### 19. Disable Question
**Endpoint:** `DELETE /api/admin/questions/{id}`  
**Authentication:** Required (ADMIN role)  
**Description:** Disable a question (soft delete)

**Path Parameters:**
- `id` (required) - Question ID

**Example:**
```
DELETE /api/admin/questions/1
```

**Response:**
```json
{
  "success": true,
  "message": "Question disabled successfully"
}
```

---

### 20. Upload Notes PDF
**Endpoint:** `POST /api/admin/notes`  
**Authentication:** Required (ADMIN role)  
**Description:** Upload PDF notes file

**Content-Type:** `multipart/form-data`

**Form Data:**
- `file` (required) - PDF file (max 10MB)
- `title` (required) - Notes title
- `categoryId` (required) - Category ID

**Example using cURL:**
```bash
curl -X POST http://localhost:8080/api/admin/notes \
  -F "file=@notes.pdf" \
  -F "title=Mathematics Notes" \
  -F "categoryId=1"
```

**Response:**
```json
{
  "success": true,
  "message": "Notes uploaded successfully",
  "data": {
    "id": 1,
    "title": "Mathematics Notes",
    "fileName": "notes.pdf",
    "filePath": "uploads/notes/uuid-filename.pdf",
    "uploadedAt": "2024-01-15T10:00:00",
    "category": {
      "id": 1,
      "name": "Mathematics"
    }
  }
}
```

---

### 21. Get All Notes (Admin)
**Endpoint:** `GET /api/admin/notes`  
**Authentication:** Required (ADMIN role)  
**Description:** Get all notes with pagination

**Query Parameters:**
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 10) - Page size

**Example:**
```
GET /api/admin/notes?page=0&size=10
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "Mathematics Notes",
      "fileName": "math_notes.pdf",
      "filePath": "uploads/notes/uuid-filename.pdf",
      "uploadedAt": "2024-01-15T10:00:00",
      "category": {
        "id": 1,
        "name": "Mathematics"
      }
    }
  ],
  "totalElements": 30,
  "totalPages": 3,
  "currentPage": 0
}
```

---

## 📊 Complete API Summary

| # | Method | Endpoint | Role | Description |
|---|--------|----------|------|-------------|
| 1 | POST | `/api/user/auth/register` | Public | Register user |
| 2 | POST | `/api/user/auth/login` | Public | Login user |
| 3 | POST | `/api/user/auth/logout` | USER | Logout user |
| 4 | POST | `/api/admin/auth/register` | Public | Register admin |
| 5 | POST | `/api/admin/auth/login` | Public | Login admin |
| 6 | POST | `/api/admin/auth/logout` | ADMIN | Logout admin |
| 7 | GET | `/api/user/categories` | USER | Get categories |
| 8 | GET | `/api/user/quiz/questions` | USER | Get quiz questions |
| 9 | POST | `/api/user/quiz/submit` | USER | Submit quiz |
| 10 | GET | `/api/user/quiz/history` | USER | Get quiz history |
| 11 | GET | `/api/user/notes` | USER | Get notes by category |
| 12 | GET | `/api/user/notes/{id}/download` | USER | Download notes PDF |
| 13 | GET | `/api/admin/dashboard/stats` | ADMIN | Get dashboard stats |
| 14 | POST | `/api/admin/categories` | ADMIN | Create category |
| 15 | GET | `/api/admin/categories` | ADMIN | Get all categories |
| 16 | PUT | `/api/admin/categories/{id}/toggle` | ADMIN | Toggle category status |
| 17 | POST | `/api/admin/questions` | ADMIN | Create question |
| 18 | GET | `/api/admin/questions` | ADMIN | Get questions by category |
| 19 | DELETE | `/api/admin/questions/{id}` | ADMIN | Disable question |
| 20 | POST | `/api/admin/notes` | ADMIN | Upload notes PDF |
| 21 | GET | `/api/admin/notes` | ADMIN | Get all notes |

**Total APIs: 21**

---

## 🔄 Authentication Flow

1. User/Admin registers via respective endpoints
2. User/Admin logs in via respective endpoints
3. Session is created and user info stored in HttpSession
4. Subsequent requests check session for authentication
5. Role is verified in each controller method
6. Session is invalidated on logout

## 📁 File Upload

- Only PDF files are allowed
- Maximum file size: 10MB
- Files are stored in `uploads/notes/` directory
- File paths are stored in database
- Files are validated for:
  - File extension (.pdf)
  - MIME type (application/pdf)
  - File size (max 10MB)

## ⚠️ Error Handling

All errors are handled by `GlobalExceptionHandler` and return JSON responses:

```json
{
  "success": false,
  "message": "Error message",
  "errors": {
    "fieldName": "Validation error"
  }
}
```

## 🌐 CORS Configuration

CORS is configured for React frontend:
- Allowed origins: `http://localhost:3000`, `http://localhost:5173`
- Allowed methods: GET, POST, PUT, DELETE, OPTIONS
- Credentials: enabled

## 🔐 Session Configuration

- Session timeout: 30 minutes
- Session stored in server memory
- Session ID sent via cookie (JSESSIONID)

## ✅ Validation Rules

- **Email**: Must be valid email format
- **Password**: Minimum 6 characters
- **Question Options**: All 4 options (A, B, C, D) required
- **Correct Option**: Must be A, B, C, or D
- **Category**: Must exist and be active
- **File Upload**: PDF only, max 10MB

## 💻 Development Notes

- No Lombok - explicit getters/setters for clarity
- Clean architecture with separation of concerns
- DTO-based APIs for request/response
- Proper exception handling
- Comprehensive validation
- Production-ready code structure

## 📝 License

This project is for academic purposes.

---

## 🧪 Testing APIs

### Using cURL Examples

**Register User:**
```bash
curl -X POST http://localhost:8080/api/user/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"user@example.com","password":"password123"}'
```

**Login User:**
```bash
curl -X POST http://localhost:8080/api/user/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}' \
  -c cookies.txt
```

**Get Categories (with session):**
```bash
curl -X GET http://localhost:8080/api/user/categories \
  -b cookies.txt
```

**Upload Notes:**
```bash
curl -X POST http://localhost:8080/api/admin/notes \
  -F "file=@notes.pdf" \
  -F "title=Mathematics Notes" \
  -F "categoryId=1" \
  -b cookies.txt
```

---

**For more details, see `SETUP.md` for installation instructions.**
