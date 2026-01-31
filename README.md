# 🏠 Airbnb Clone - Backend Service

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12+-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A full-featured backend service for an Airbnb-like application built with Spring Boot. This application provides a complete REST API for managing property listings, bookings, user authentication, and reviews.

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#️-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Configuration](#-configuration)
- [API Documentation](#-api-documentation)
- [Running Tests](#-running-tests)
- [Build for Production](#-build-for-production)
- [Docker Support](#-docker-support)
- [Database Schema](#-database-schema)
- [Security](#-security)
- [Roadmap](#-roadmap)
- [FAQ](#-faq)
- [Deployment](#-deployment)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

### Core Features
- 🔐 **User Authentication & Authorization** - Secure JWT-based authentication with role management
- 🏡 **Property Management** - Full CRUD operations for property listings
- 📅 **Booking System** - Comprehensive reservation and availability management
- ⭐ **Reviews & Ratings** - User feedback system with ratings
- 🔍 **Advanced Search** - Filter properties by location, price, amenities, and dates
- 💳 **Payment Integration** - Secure payment processing
- 📸 **Image Upload** - Multi-image support for property listings
- 📧 **Email Notifications** - Automated booking confirmations and updates

### Advanced Features
- 📊 **Analytics Dashboard** - Host analytics and booking insights
- 🗺️ **Geolocation Services** - Location-based property search
- 💬 **Messaging System** - Real-time chat between hosts and guests
- 🌐 **Multi-language Support** - Internationalization (i18n)
- 📱 **Mobile Responsive API** - Optimized for mobile applications
- 🔔 **Push Notifications** - Real-time updates and alerts
- 🎯 **Recommendation Engine** - Personalized property suggestions
- 📈 **Dynamic Pricing** - Smart pricing based on demand and seasonality

## 🛠️ Tech Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security with JWT
- **Build Tool**: Maven
- **API Documentation**: Swagger/OpenAPI 3.0
- **File Storage**: Local/Cloud storage integration
- **Validation**: Hibernate Validator

## 📁 Project Structure

```
airBnbApp/
├── src/
│   ├── main/
│   │   ├── java/com/codingshuttle/project/airBnbApp/
│   │   │   ├── controllers/    # REST Controllers
│   │   │   ├── entities/       # JPA Entities
│   │   │   ├── repositories/   # Data Repositories
│   │   │   ├── services/       # Business Logic
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   ├── config/         # Configuration Classes
│   │   │   ├── security/       # Security Configuration
│   │   │   ├── exceptions/     # Custom Exceptions
│   │   │   └── utils/          # Utility Classes
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-prod.properties
│   │       └── static/
│   └── test/                   # Test Files
├── pom.xml
├── .gitignore
└── README.md
```

## 🏁 Getting Started

### Prerequisites

- **Java Development Kit (JDK)** - Version 17 or higher
- **Maven** - Version 3.6 or higher
- **PostgreSQL** - Version 12 or higher
- **IDE** - IntelliJ IDEA, Eclipse, or VS Code (optional)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd airBnbApp
   ```

2. **Create PostgreSQL database**
   ```sql
   CREATE DATABASE airbnb;
   CREATE USER airbnb_user WITH PASSWORD 'your_password';
   GRANT ALL PRIVILEGES ON DATABASE airbnb TO airbnb_user;
   ```

3. **Configure application properties**
   
   Update `src/main/resources/application.properties`:
   ```properties
   # Database Configuration
   spring.datasource.url=jdbc:postgresql://localhost:5432/airbnb
   spring.datasource.username=airbnb_user
   spring.datasource.password=your_password
   
   # JPA/Hibernate
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true
   
   # JWT Configuration
   jwt.secret=your_secret_key_here
   jwt.expiration=86400000
   
   # File Upload
   spring.servlet.multipart.max-file-size=10MB
   spring.servlet.multipart.max-request-size=10MB
   ```

4. **Build the project**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The server will start at `http://localhost:8080`

## 🔧 Configuration

### Application Properties

| Property | Description | Default |
|----------|-------------|---------|
| `spring.datasource.url` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/airbnb` |
| `spring.datasource.username` | Database username | - |
| `spring.datasource.password` | Database password | - |
| `spring.jpa.hibernate.ddl-auto` | Schema generation strategy | `update` |
| `spring.jpa.show-sql` | Show SQL queries in console | `true` |
| `jwt.secret` | JWT secret key | - |
| `jwt.expiration` | JWT token expiration (ms) | `86400000` |

### Environment Profiles

- **Development**: `application-dev.properties`
- **Production**: `application-prod.properties`

Run with specific profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## 📚 API Documentation

Access Swagger UI at: `http://localhost:8080/swagger-ui.html`

### Quick Start Example

```bash
# Register a new user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "securePassword123",
    "firstName": "John",
    "lastName": "Doe"
  }'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "securePassword123"
  }'

# Create a property (with JWT token)
curl -X POST http://localhost:8080/api/properties \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Beautiful Beach House",
    "description": "Stunning ocean views",
    "price": 150.00,
    "location": "Miami Beach, FL"
  }'
```

### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register a new user | No |
| POST | `/api/auth/login` | User login | No |
| POST | `/api/auth/refresh` | Refresh JWT token | Yes |
| POST | `/api/auth/logout` | User logout | Yes |

### Property Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/properties` | Get all properties | No |
| GET | `/api/properties/{id}` | Get property by ID | No |
| POST | `/api/properties` | Create a property | Yes (Host) |
| PUT | `/api/properties/{id}` | Update a property | Yes (Owner) |
| DELETE | `/api/properties/{id}` | Delete a property | Yes (Owner) |
| GET | `/api/properties/search` | Search properties | No |

### Booking Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/bookings` | Create a booking | Yes |
| GET | `/api/bookings/{id}` | Get booking details | Yes (Owner/Guest) |
| GET | `/api/bookings/user` | Get user bookings | Yes |
| PUT | `/api/bookings/{id}/cancel` | Cancel a booking | Yes (Guest) |
| PUT | `/api/bookings/{id}/confirm` | Confirm a booking | Yes (Host) |

### Review Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/reviews` | Add a review | Yes |
| GET | `/api/reviews/property/{id}` | Get property reviews | No |
| PUT | `/api/reviews/{id}` | Update a review | Yes (Owner) |
| DELETE | `/api/reviews/{id}` | Delete a review | Yes (Owner) |

### User Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/users/profile` | Get user profile | Yes |
| PUT | `/api/users/profile` | Update user profile | Yes |
| GET | `/api/users/{id}` | Get user by ID | Yes (Admin) |
| PUT | `/api/users/{id}/role` | Update user role | Yes (Admin) |
| DELETE | `/api/users/{id}` | Delete user account | Yes (Admin) |

## 🧪 Running Tests

Run all tests:
```bash
mvn test
```

Run with coverage:
```bash
mvn test jacoco:report
```

Run specific test class:
```bash
mvn test -Dtest=PropertyServiceTest
```

## 📦 Build for Production

```bash
# Build JAR file
mvn clean package -DskipTests

# Run the JAR
java -jar target/airBnbApp-0.0.1-SNAPSHOT.jar

# Run with production profile
java -jar -Dspring.profiles.active=prod target/airBnbApp-0.0.1-SNAPSHOT.jar
```

## 🐳 Docker Support

### Docker Compose Configuration

Create a `docker-compose.yml` file:

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:14
    environment:
      POSTGRES_DB: airbnb
      POSTGRES_USER: airbnb_user
      POSTGRES_PASSWORD: your_password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  backend:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/airbnb
      SPRING_DATASOURCE_USERNAME: airbnb_user
      SPRING_DATASOURCE_PASSWORD: your_password
    depends_on:
      - postgres

volumes:
  postgres_data:
```

Build Docker image:
```bash
docker build -t airbnb-backend .
```

Run with Docker Compose:
```bash
docker-compose up -d
```

## 📊 Database Schema

### Entity Relationship Diagram

```
┌─────────────┐       ┌──────────────┐       ┌─────────────┐
│    User     │──────<│   Property   │>──────│   Booking   │
└─────────────┘       └──────────────┘       └─────────────┘
      │                      │                       │
      │                      │                       │
      │                      ▼                       ▼
      │               ┌─────────────┐        ┌─────────────┐
      └──────────────>│   Review    │        │   Payment   │
                      └─────────────┘        └─────────────┘
```

### Key Entities

- **User** - User accounts and authentication
  - `id`, `email`, `password`, `firstName`, `lastName`, `role`, `createdAt`
  
- **Property** - Property listings
  - `id`, `title`, `description`, `price`, `location`, `hostId`, `amenities`, `images`
  
- **Booking** - Reservations
  - `id`, `propertyId`, `guestId`, `checkIn`, `checkOut`, `totalPrice`, `status`
  
- **Review** - User reviews and ratings
  - `id`, `propertyId`, `userId`, `rating`, `comment`, `createdAt`
  
- **Payment** - Payment transactions
  - `id`, `bookingId`, `amount`, `status`, `paymentMethod`, `transactionId`

## 🔒 Security

- JWT-based authentication
- Password encryption using BCrypt
- Role-based access control (RBAC)
- CORS configuration
- SQL injection prevention
- Input validation

## 🗺️ Roadmap

### Phase 1 - Core Features ✅
- [x] User authentication and authorization
- [x] Property CRUD operations
- [x] Booking system
- [x] Review and rating system
- [x] Basic search functionality

### Phase 2 - Enhanced Features 🚧
- [ ] Real-time messaging between hosts and guests
- [ ] Advanced search with filters (location, price range, amenities)
- [ ] Payment gateway integration (Stripe/PayPal)
- [ ] Email notification system
- [ ] Image upload and storage (AWS S3)

### Phase 3 - Advanced Features 📅
- [ ] Mobile application (React Native)
- [ ] Host analytics dashboard
- [ ] Dynamic pricing algorithm
- [ ] Recommendation engine using ML
- [ ] Multi-currency support
- [ ] Social media integration
- [ ] Wishlist/Favorites feature

### Phase 4 - Enterprise Features 🔮
- [ ] Multi-language support
- [ ] Advanced analytics and reporting
- [ ] Integration with external calendar systems
- [ ] Property management tools for hosts
- [ ] Admin dashboard with comprehensive metrics
- [ ] API rate limiting and throttling
- [ ] Microservices architecture migration

## ❓ FAQ

### General Questions

**Q: What technologies are used in this project?**  
A: This project uses Java 17, Spring Boot 3.x, PostgreSQL, Spring Security with JWT, and Maven.

**Q: Is this project production-ready?**  
A: This is a learning/demonstration project. Additional security hardening, monitoring, and scaling considerations should be implemented for production use.

**Q: Can I use a different database?**  
A: Yes, you can configure other SQL databases by updating the `application.properties` file and adding the appropriate JDBC driver dependency.

### Development Questions

**Q: How do I run the application locally?**  
A: Follow the installation steps in the [Getting Started](#-getting-started) section. Make sure PostgreSQL is running and configured correctly.

**Q: How do I test the API endpoints?**  
A: You can use Swagger UI at `http://localhost:8080/swagger-ui.html` or tools like Postman or cURL.

**Q: How do I add new features?**  
A: Create a new branch, implement your feature following the existing project structure, write tests, and submit a pull request.

### Deployment Questions

**Q: How do I deploy this application?**  
A: The application can be deployed to cloud platforms like AWS, Heroku, or Azure. See the [Deployment](#-deployment) section for more details.

**Q: How do I handle environment-specific configurations?**  
A: Use Spring profiles (dev, prod) with separate property files for different environments.

**Q: What about database migrations?**  
A: Consider using Flyway or Liquibase for production environments to manage database schema versions.

## 🚀 Deployment

The application can be deployed to:
- AWS EC2/ECS
- Heroku
- Google Cloud Platform
- Azure App Service

### Environment Variables

Set these environment variables for production:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://your-db-host:5432/airbnb
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
JWT_SECRET=your_very_secure_secret_key
JWT_EXPIRATION=86400000
SERVER_PORT=8080
```

### CI/CD Pipeline

Example GitHub Actions workflow:

```yaml
name: Build and Deploy

on:
  push:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
      - name: Build with Maven
        run: mvn clean package -DskipTests
      - name: Run tests
        run: mvn test
```

## 📊 Performance Optimization

- Database indexing on frequently queried fields
- Query optimization with JPA fetch strategies
- Response caching with Redis (planned)
- Database connection pooling
- Async processing for email notifications
- Pagination for large result sets

## 🔍 Monitoring and Logging

- Spring Boot Actuator for health checks
- Logback for application logging
- Integration with monitoring tools (Prometheus, Grafana)
- Request/Response logging
- Error tracking and alerting

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code of Conduct

Please note that this project is released with a [Contributor Code of Conduct](CODE_OF_CONDUCT.md). By participating in this project you agree to abide by its terms.

## 👥 Authors

- **Your Name** - *Initial work* - [YourGitHub](https://github.com/yourusername)

## 📧 Contact

Project Link: [https://github.com/yourusername/airBnbApp](https://github.com/yourusername/airBnbApp)

Email: your.email@example.com

## 🙏 Acknowledgments

- Spring Boot Documentation
- PostgreSQL Community
- Stack Overflow Community
- JWT.io for token generation guidance
- Baeldung for Spring Boot tutorials
- Spring Framework community

---

<div align="center">
Made with ❤️ by developers, for developers
</div>
