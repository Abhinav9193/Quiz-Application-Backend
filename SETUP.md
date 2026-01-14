# Setup Guide

## Prerequisites

1. **Java 17** - Download from [Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or [OpenJDK](https://adoptium.net/)
2. **Maven 3.6+** - Download from [Apache Maven](https://maven.apache.org/download.cgi)
3. **MySQL 8.0+** - Download from [MySQL](https://dev.mysql.com/downloads/mysql/)

## Step-by-Step Setup

### 1. Clone/Download the Project

```bash
cd quizzz
```

### 2. Configure MySQL Database

#### Option A: Using MySQL Command Line
```sql
mysql -u root -p
CREATE DATABASE quiz_app_db;
EXIT;
```

#### Option B: Using MySQL Workbench
- Open MySQL Workbench
- Create a new schema named `quiz_app_db`
- Set default collation to `utf8mb4_unicode_ci`

### 3. Update Database Credentials

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

Replace `YOUR_MYSQL_PASSWORD` with your actual MySQL root password.

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

Or if you prefer to run the JAR:

```bash
mvn clean package
java -jar target/quiz-app-0.0.1-SNAPSHOT.jar
```

### 6. Verify Application is Running

Open your browser and check:
- Application logs should show: `Started QuizAppApplication`
- Check `http://localhost:8080` (you may see a Whitelabel error page, which is normal)

### 7. Test the API

#### Register an Admin User
```bash
curl -X POST http://localhost:8080/api/admin/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Admin User",
    "email": "admin@example.com",
    "password": "admin123"
  }'
```

#### Register a Regular User
```bash
curl -X POST http://localhost:8080/api/user/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "user@example.com",
    "password": "user123"
  }'
```

## File Upload Directory

The application will automatically create the `uploads/notes/` directory when you first upload a PDF file. Make sure the application has write permissions in the project root directory.

## Troubleshooting

### Port 8080 Already in Use

If port 8080 is already in use, change it in `application.properties`:

```properties
server.port=8081
```

### MySQL Connection Error

1. Verify MySQL is running:
   ```bash
   # Windows
   net start MySQL80
   
   # Linux/Mac
   sudo systemctl status mysql
   ```

2. Check MySQL credentials in `application.properties`

3. Verify database exists:
   ```sql
   SHOW DATABASES;
   ```

### Hibernate Schema Creation Issues

If tables are not being created automatically:

1. Check MySQL user has CREATE privileges
2. Verify `spring.jpa.hibernate.ddl-auto=update` in `application.properties`
3. Check application logs for Hibernate errors

### CORS Issues (Frontend Integration)

If you're connecting from a different frontend port:

1. Update `CorsConfig.java` to include your frontend URL
2. Or update `application.properties` if using a different CORS configuration

## Development Tips

1. **Hot Reload**: Spring Boot DevTools is included - restart the app automatically on code changes
2. **Database Console**: Consider adding H2 console for development (optional)
3. **Logging**: Check `application.properties` for logging levels
4. **Session Management**: Sessions expire after 30 minutes of inactivity

## Next Steps

1. Create your first admin account
2. Create categories
3. Add questions to categories
4. Upload PDF notes
5. Test user registration and quiz functionality

## Production Deployment

For production deployment:

1. Change `spring.jpa.hibernate.ddl-auto` to `validate` or `none`
2. Set `spring.datasource.password` via environment variables
3. Enable HTTPS and update CORS configuration
4. Set `useSecureCookie=true` in SessionConfig
5. Configure proper logging
6. Set up database backups


