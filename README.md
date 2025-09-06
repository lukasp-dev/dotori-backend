# 🌰 Dotori - Backend

> **Equal Opportunities for Students at a Fair Price.**
> Smart help. Honest prices. Real opportunity.

---

## Repositories

* Frontend: [dotori-web](https://github.com/lukasp-dev/dotori-web)
* AI Server: [dotori-fastapi](https://github.com/lukasp-dev/dotori-fastapi)
* Data: [dotori-data](https://github.com/lukasp-dev/dotori-data)

---

## Why Dotori?

Navigating college admissions—especially abroad—can be stressful, confusing, and
 far too expensive.
Agencies charge steep fees for services as simple as essay review, leaving many
students without affordable guidance.
                                                        

**Dotori changes this.**
We provide **24/7 AI-powered essay feedback**, **resume-driven prompts**, and a
**final human review**, all at a fair price.
                                                        
Built by students, for students—because your story deserves to be written, not p
riced.

---

## Features

* **AI Essay Feedback**: Get instant corrections and suggestions, anytime.
* **Resume-Driven Prompts**: Essay ideas tailored to your unique background.
* **Final Human Review**: Real experts ensure your voice stays authentic.
* **Dream School Guidance**: Strategic advice on where to apply.
* **School Analysis**: Data-driven insights into universities.
* **Alumni Connections**: Talk directly with peers and graduates.

---

## Technical Documentation

### 🛠️ Tech Stack

**Backend Framework**
- **Spring Boot 3.3.5** - Main application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database abstraction layer
- **Spring Web** - REST API development

**Database & Storage**
- **PostgreSQL** - Primary database (hosted on Neon)
- **JPA/Hibernate** - ORM for database operations
- **Google Cloud Storage** - File storage and management

**Authentication & Security**
- **JWT (JSON Web Tokens)** - Stateless authentication
- **BCrypt** - Password hashing
- **HttpOnly Cookies** - Secure refresh token storage

**Development Tools**
- **Lombok** - Boilerplate code reduction
- **Maven** - Dependency management
- **Docker** - Containerization
- **Swagger/OpenAPI 3** - API documentation

### 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Load Balancer │    │   Backend API   │
│   (React)       │◄──►│   (Optional)    │◄──►│   (Spring Boot) │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                        │
                       ┌─────────────────┐              │
                       │   PostgreSQL    │◄─────────────┘
                       │   Database      │
                       └─────────────────┘
                                │
                       ┌─────────────────┐
                       │   Google Cloud  │
                       │   Storage       │
                       └─────────────────┘
```

### 📚 API Endpoints

**Authentication**
- `POST /api/auth/signup` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/social-login` - Social login (Google, etc.)
- `POST /api/auth/refresh` - Refresh access token
- `POST /api/auth/logout` - User logout

**File Management**
- `POST /api/upload/resume` - Upload resume file
- `GET /api/upload/{userId}` - Get resume URL

**School & Essay Management**
- `GET /api/schools` - Get all schools
- `GET /api/essays/{schoolId}` - Get essays by school

**Personal Information**
- `POST /api/uploadPersonalInfo` - Upload personal/academic info

### 🚀 Quick Start

1. **Prerequisites**
   - Java 17+
   - Maven 3.6+
   - PostgreSQL database
   - Google Cloud Platform account

2. **Configuration**
   ```properties
   # Database
   spring.datasource.url=jdbc:postgresql://your-db-host:5432/your-db
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   
   # GCP
   spring.cloud.gcp.project-id=your-project-id
   resume.bucket.name=your-bucket-name
   ```

3. **Run Application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **API Documentation**
   - Swagger UI: `http://localhost:8080/swagger-ui.html`

### 🔒 Security Implementation

**JWT Authentication System**
- **Access Token**: 15-minute expiration, contains user claims (userId, email, role)
- **Refresh Token**: 7-day expiration, stored in HttpOnly cookies for security
- **Token Generation**: HMAC-SHA256 algorithm with 256-bit secret key
- **Token Validation**: Custom filter validates every protected request
- **Automatic Refresh**: Seamless token renewal without user intervention

**Password Security**
- **BCrypt Hashing**: Industry-standard password hashing with salt
- **Password Validation**: 6-150 character length requirement
- **Secure Storage**: Passwords never stored in plain text

**Request Security**
- **CORS Configuration**: Configured for frontend integration (localhost:3000)
- **SQL Injection Protection**: JPA/Hibernate parameterized queries
- **Input Validation**: Bean Validation annotations on DTOs
- **File Upload Security**: 10MB size limit, content type validation
- **XSS Protection**: Proper encoding and validation

**Role-Based Access Control**
- **User Roles**: USER (default), ADMIN (@dotori.com emails)
- **Endpoint Protection**: Spring Security configuration per endpoint
- **Token Claims**: Role information embedded in JWT for authorization

### 🗄️ Database Schema

**Users Table**
```sql
CREATE TABLE users (
    id VARCHAR PRIMARY KEY,
    firstname VARCHAR NOT NULL,
    lastname VARCHAR NOT NULL,
    email VARCHAR UNIQUE NOT NULL,
    password VARCHAR,
    role VARCHAR,
    image VARCHAR,
    name VARCHAR,
    "emailVerified" TIMESTAMP,
    "createdAt" TIMESTAMP NOT NULL,
    "updatedAt" TIMESTAMP NOT NULL
);
```

**Refresh Tokens Table**
```sql
CREATE TABLE refresh_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR UNIQUE NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    use_id VARCHAR REFERENCES users(id)
);
```

**Essays Table**
```sql
CREATE TABLE essays (
    id SERIAL PRIMARY KEY,
    school_id INTEGER,
    school_name VARCHAR,
    topic VARCHAR,
    year INTEGER,
    required BOOLEAN,
    group_id INTEGER,
    choice_count INTEGER,
    limit_type VARCHAR,
    limit_count_min INTEGER,
    limit_count_max INTEGER,
    details TEXT
);
```

**Schools Table**
```sql
CREATE TABLE "Schools" (
    id BIGSERIAL PRIMARY KEY,
    school_name VARCHAR NOT NULL,
    ranking INTEGER NOT NULL,
    url_parameter VARCHAR NOT NULL
);
```

### 🔧 Implementation Details

**JWT Service Implementation**
```java
// Token Creation with Claims
public String create(Map<String, Object> claims, LocalDateTime expireAt) {
    var key = Keys.hmacShaKeyFor(secretKey.getBytes());
    return Jwts.builder()
        .signWith(key, SignatureAlgorithm.HS256)
        .setClaims(claims)
        .setExpiration(Date.from(expireAt.atZone(ZoneId.systemDefault()).toInstant()))
        .compact();
}

// Token Validation with Error Handling
public void validation(String token) {
    try {
        var parser = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build();
        parser.parseClaimsJws(token);
    } catch (SignatureException e) {
        throw new RuntimeException("Invalid JWT signature");
    } catch (ExpiredJwtException e) {
        throw new RuntimeException("JWT token expired");
    }
}
```

**Authentication Flow**
1. **Login/Signup**: User credentials validated
2. **Token Generation**: Access (15min) + Refresh (7days) tokens created
3. **Cookie Setting**: Refresh token stored in HttpOnly cookie
4. **Request Filtering**: JwtAuthenticationFilter validates every request
5. **Context Setting**: Spring Security context populated with user details

**File Upload Implementation**
- **Google Cloud Storage**: Secure file storage with public URLs
- **File Management**: Automatic cleanup of existing files per user
- **Retry Logic**: 3-attempt retry mechanism for failed uploads
- **Error Handling**: Comprehensive exception handling with logging

**Essay Management System**
- **Individual Essays**: Standalone essay prompts
- **Grouped Essays**: Choice-based essay groups with limits
- **School-Specific**: Essays filtered by school ID
- **Flexible Structure**: Support for various essay types and requirements

### 🚀 Performance Optimizations

**Database Optimizations**
- **JPA Lazy Loading**: Optimized entity relationships
- **Query Optimization**: Custom repository methods
- **Connection Pooling**: PostgreSQL connection management
- **Indexing**: Proper database indexing on frequently queried fields

**Caching Strategy**
- **Token Caching**: JWT validation results cached
- **User Context**: User details cached in security context
- **File URLs**: Generated URLs cached for performance

**Error Handling**
- **Global Exception Handler**: Centralized error management
- **Custom Exceptions**: Specific exception types for different scenarios
- **HTTP Status Codes**: Proper REST API status code usage
- **Logging**: Comprehensive logging for debugging and monitoring

---

## Members

* **Jehoon Park** — Founder / SWE (Univ. of Minnesota Twin Cities)
* **Jewook Park** — Co-Founder / SWE (Georgia Tech)
* **Seoyoon Ham** — Co-Founder / Data Engineer (NYU)
* **Jiyoon Shin** — Co-Founder / Data Engineer (Yonsei Univ.)

---

## Contact

* Company: **dotori**
* Email: **[dotoripack@gmail.com](mailto:dotoripack@gmail.com)**
* Location: Minneapolis
* LinkedIn: [Dotori LinkedIn](https://www.linkedin.com/company/dotoripack/about/?viewAsMember=true)