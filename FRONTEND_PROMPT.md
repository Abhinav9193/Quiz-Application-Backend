# Frontend Development Prompt for Quiz & Notes Management Application

## Project Overview

Build a React.js frontend application for a Quiz & Notes Management System. The backend is a Spring Boot REST API running on `http://localhost:8080`. The backend uses simple authentication (no JWT, no OAuth, no Spring Security) - passwords are stored in plain text and authentication is done via database lookup.

## Technology Stack Required

- **React.js** (Latest version)
- **React Router** (for navigation)
- **Axios** (for API calls)
- **State Management** (Context API or Redux - your choice)
- **CSS Framework** (Tailwind CSS, Material-UI, or Bootstrap - your choice)
- **Form Handling** (React Hook Form or Formik)

## Deployment Platform

- **Vercel** (for frontend deployment)
- Backend runs separately on Spring Boot server

## Backend API Base URL

```
http://localhost:8080
```

For production, update to your deployed backend URL.

---

## Complete API List

### Authentication APIs

#### 1. Register User
- **Method:** POST
- **Endpoint:** `/api/user/auth/register`
- **Auth Required:** No
- **Request Body:**
```json
{
  "name": "John Doe",
  "email": "user@example.com",
  "password": "password123"
}
```
- **Response:**
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

#### 2. Login User
- **Method:** POST
- **Endpoint:** `/api/user/auth/login`
- **Auth Required:** No
- **Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```
- **Response:**
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
- **Note:** Backend uses session-based auth. Cookies will be set automatically.

#### 3. Logout User
- **Method:** POST
- **Endpoint:** `/api/user/auth/logout`
- **Auth Required:** Yes (Session)
- **Response:**
```json
{
  "success": true,
  "message": "Logout successful"
}
```

#### 4. Register Admin
- **Method:** POST
- **Endpoint:** `/api/admin/auth/register`
- **Auth Required:** No
- **Request Body:** Same as user registration
- **Response:** Same format as user registration (role will be "ADMIN")

#### 5. Login Admin
- **Method:** POST
- **Endpoint:** `/api/admin/auth/login`
- **Auth Required:** No
- **Request Body:** Same as user login
- **Response:** Same format as user login (role will be "ADMIN")

#### 6. Logout Admin
- **Method:** POST
- **Endpoint:** `/api/admin/auth/logout`
- **Auth Required:** Yes (Session)

---

### User APIs (Requires USER Role)

#### 7. Get All Categories
- **Method:** GET
- **Endpoint:** `/api/user/categories`
- **Auth Required:** Yes (Session)

#### 8. Get Quiz Questions
- **Method:** GET
- **Endpoint:** `/api/user/quiz/questions?categoryId=1&limit=10`
- **Auth Required:** Yes (Session)
- **Query Params:**
  - `categoryId` (required)
  - `limit` (optional, default: 10)

#### 9. Submit Quiz
- **Method:** POST
- **Endpoint:** `/api/user/quiz/submit`
- **Auth Required:** Yes (Session)
- **Request Body:**
```json
{
  "categoryId": 1,
  "answers": {
    "1": "A",
    "2": "B",
    "3": "C"
  },
  "timeTakenInSeconds": 300
}
```

#### 10. Get Quiz History
- **Method:** GET
- **Endpoint:** `/api/user/quiz/history?page=0&size=10`
- **Auth Required:** Yes (Session)
- **Query Params:**
  - `page` (optional, default: 0)
  - `size` (optional, default: 10)

#### 11. Get Notes by Category
- **Method:** GET
- **Endpoint:** `/api/user/notes?categoryId=1&page=0&size=10`
- **Auth Required:** Yes (Session)
- **Query Params:**
  - `categoryId` (required)
  - `page` (optional)
  - `size` (optional)

#### 12. Download Notes PDF
- **Method:** GET
- **Endpoint:** `/api/user/notes/{id}/download`
- **Auth Required:** Yes (Session)
- **Response:** PDF file download

---

### Admin APIs (Requires ADMIN Role)

#### 13. Get Dashboard Statistics
- **Method:** GET
- **Endpoint:** `/api/admin/dashboard/stats`
- **Auth Required:** Yes (Session - ADMIN role)

#### 14. Create Category
- **Method:** POST
- **Endpoint:** `/api/admin/categories`
- **Auth Required:** Yes (Session - ADMIN role)
- **Request Body:**
```json
{
  "name": "Mathematics"
}
```

#### 15. Get All Categories (Admin)
- **Method:** GET
- **Endpoint:** `/api/admin/categories`
- **Auth Required:** Yes (Session - ADMIN role)

#### 16. Toggle Category Status
- **Method:** PUT
- **Endpoint:** `/api/admin/categories/{id}/toggle`
- **Auth Required:** Yes (Session - ADMIN role)

#### 17. Create Question
- **Method:** POST
- **Endpoint:** `/api/admin/questions`
- **Auth Required:** Yes (Session - ADMIN role)
- **Request Body:**
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

#### 18. Get Questions by Category
- **Method:** GET
- **Endpoint:** `/api/admin/questions?categoryId=1&page=0&size=10`
- **Auth Required:** Yes (Session - ADMIN role)

#### 19. Disable Question
- **Method:** DELETE
- **Endpoint:** `/api/admin/questions/{id}`
- **Auth Required:** Yes (Session - ADMIN role)

#### 20. Upload Notes PDF
- **Method:** POST
- **Endpoint:** `/api/admin/notes`
- **Auth Required:** Yes (Session - ADMIN role)
- **Content-Type:** `multipart/form-data`
- **Form Data:**
  - `file` (PDF file, max 10MB)
  - `title` (string)
  - `categoryId` (number)

#### 21. Get All Notes (Admin)
- **Method:** GET
- **Endpoint:** `/api/admin/notes?page=0&size=10`
- **Auth Required:** Yes (Session - ADMIN role)

---

## Frontend Requirements

### Pages/Screens to Build

#### User Pages:
1. **Register Page** (`/register`)
   - Form: Name, Email, Password
   - Call API: POST `/api/user/auth/register`
   - Redirect to login on success

2. **Login Page** (`/login`)
   - Form: Email, Password
   - Call API: POST `/api/user/auth/login`
   - Store user info in context/localStorage
   - Redirect to dashboard on success

3. **User Dashboard** (`/user/dashboard`)
   - Display categories
   - Allow user to select category and start quiz
   - Show quiz history

4. **Quiz Page** (`/user/quiz/:categoryId`)
   - Fetch questions: GET `/api/user/quiz/questions?categoryId={id}`
   - Display questions with options (A, B, C, D)
   - Timer for quiz
   - Submit answers: POST `/api/user/quiz/submit`
   - Show results after submission

5. **Quiz History Page** (`/user/history`)
   - Display quiz attempts: GET `/api/user/quiz/history`
   - Show scores, percentages, dates

6. **Notes Page** (`/user/notes/:categoryId`)
   - List notes: GET `/api/user/notes?categoryId={id}`
   - Download PDF: GET `/api/user/notes/{id}/download`

#### Admin Pages:
1. **Admin Login** (`/admin/login`)
   - Form: Email, Password
   - Call API: POST `/api/admin/auth/login`
   - Redirect to admin dashboard

2. **Admin Dashboard** (`/admin/dashboard`)
   - Display statistics: GET `/api/admin/dashboard/stats`
   - Cards showing: Total Users, Total Questions, Total Attempts, Average Score

3. **Category Management** (`/admin/categories`)
   - List categories: GET `/api/admin/categories`
   - Create category: POST `/api/admin/categories`
   - Toggle category status: PUT `/api/admin/categories/{id}/toggle`

4. **Question Management** (`/admin/questions`)
   - List questions by category: GET `/api/admin/questions?categoryId={id}`
   - Create question form: POST `/api/admin/questions`
   - Disable question: DELETE `/api/admin/questions/{id}`

5. **Notes Management** (`/admin/notes`)
   - List all notes: GET `/api/admin/notes`
   - Upload PDF form: POST `/api/admin/notes` (multipart/form-data)

---

## Important Configuration

### CORS Configuration
The backend already allows CORS from:
- `http://localhost:3000`
- `http://localhost:5173`
- `https://*.vercel.app`
- All origins (for development)

### API Configuration in Frontend
Create an `api.js` or `config.js` file:

```javascript
export const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

// For Axios configuration
import axios from 'axios';

const api = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true, // Important for session cookies
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;
```

### Session/Cookie Handling
- Backend uses session-based authentication
- Cookies are set automatically on login
- Use `withCredentials: true` in Axios requests
- For file downloads, use blob response type

### Environment Variables for Vercel
Create `.env.local`:
```
REACT_APP_API_URL=http://localhost:8080
```

For production, set in Vercel:
```
REACT_APP_API_URL=https://your-backend-url.com
```

---

## Key Features to Implement

1. **Authentication Flow:**
   - Register → Login → Dashboard
   - Store user info in Context/Redux
   - Protected routes based on role (USER/ADMIN)
   - Logout functionality

2. **Quiz Functionality:**
   - Random question display
   - Timer component
   - Answer selection (radio buttons)
   - Score calculation and display
   - History tracking

3. **File Upload (Admin):**
   - PDF file upload
   - FormData for multipart requests
   - Progress indicator
   - File validation (PDF only, max 10MB)

4. **File Download (User):**
   - Download PDF notes
   - Use blob response type
   - Create download link

5. **Pagination:**
   - Implement pagination for lists
   - Use query parameters (page, size)

6. **Error Handling:**
   - Display error messages
   - Handle network errors
   - Show validation errors

---

## Deployment on Vercel

### Steps:
1. Push code to GitHub
2. Connect GitHub repo to Vercel
3. Set environment variables in Vercel dashboard
4. Build command: `npm run build`
5. Output directory: `build`
6. Deploy

### Vercel Configuration (vercel.json):
```json
{
  "version": 2,
  "builds": [
    {
      "src": "package.json",
      "use": "@vercel/static-build",
      "config": {
        "distDir": "build"
      }
    }
  ],
  "routes": [
    {
      "src": "/(.*)",
      "dest": "/index.html"
    }
  ]
}
```

---

## Testing Checklist

- [ ] User registration works
- [ ] User login works
- [ ] Admin login works
- [ ] Categories display correctly
- [ ] Quiz questions load and display
- [ ] Quiz submission works
- [ ] Quiz history displays
- [ ] Notes list displays
- [ ] PDF download works
- [ ] Admin dashboard shows stats
- [ ] Category management works
- [ ] Question management works
- [ ] Notes upload works
- [ ] Logout works
- [ ] Protected routes work
- [ ] Error handling works
- [ ] Responsive design works

---

## UI/UX Suggestions

1. **Design:**
   - Clean, modern interface
   - Use color coding for different roles
   - Loading states for API calls
   - Success/error notifications (toast messages)

2. **Navigation:**
   - Header with user info and logout
   - Sidebar for admin dashboard
   - Breadcrumbs for navigation

3. **Forms:**
   - Validation on client side
   - Clear error messages
   - Disable submit button while processing

4. **Quiz:**
   - Clear question display
   - Highlight selected answer
   - Timer countdown
   - Progress indicator

---

## API Error Response Format

All errors return this format:
```json
{
  "success": false,
  "message": "Error message",
  "errors": {
    "fieldName": "Validation error"
  }
}
```

Handle errors accordingly in the frontend.

---

## Additional Notes

- Backend runs on port 8080 by default
- Session timeout: 30 minutes
- File upload max size: 10MB
- Only PDF files allowed for notes
- Pagination defaults: page=0, size=10

---

**Good luck building the frontend! 🚀**

