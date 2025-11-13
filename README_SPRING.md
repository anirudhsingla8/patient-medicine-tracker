# Medicine Tracker Backend - Spring Boot Implementation

This is a Spring Boot implementation of the medicine tracker backend based on the requirements from the original Node.js/Express application.

## Features

- **Multi-User profiles**: Multiple family members can have their own medicine inventory
- **Complex scheduling**: Set up dosage schedules for medicines
- **Automated notifications/alerts**: Background jobs for dosage reminders and alerts
- **JWT authentication with token invalidation on password change**
- **Profile-centric medicine management**
- **Global medicine database**
- **Image upload with Cloudinary**
- **FCM token management for push notifications**

## Prerequisites

- Java 17 or higher
- Maven 3.6.0 or higher
- PostgreSQL database
- Cloudinary account for image uploads

## Environment Setup

Create a `.env` file in the root directory with the following variables:

```bash
DB_HOST='your-db-host'
DB_PORT=5432
DB_USER='your-db-user'
DB_PASSWORD='your-db-password'
DB_NAME='medicine_tracker'
DB_SSL_MODE='require'
CLOUDINARY_API_SECRET='your-cloudinary-secret'
CLOUDINARY_API_KEY='your-cloudinary-key'
CLOUDINARY_CLOUD_NAME='your-cloudinary-name'
PORT=8080
JWT_SECRET='your-jwt-secret'
```

## Database Setup

Before running the application, you need to set up the database schema. The `schema.sql` file contains all the necessary SQL commands to create the required tables.

1. Connect to your PostgreSQL database
2. Execute the contents of `src/main/resources/schema.sql`

## Running the Application

### Using Maven
```bash
mvn spring-boot:run
```

### Using Executable JAR
```bash
mvn clean package
java -jar target/tracker-1.0.0.jar
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login
- `POST /api/auth/forgot-password` - Forgot password

### Profile Management
- `POST /api/profiles` - Create a new profile
- `GET /api/profiles` - Get all profiles
- `PUT /api/profiles/{profileId}` - Update a profile
- `DELETE /api/profiles/{profileId}` - Delete a profile

### Medicine Management
- `POST /api/medicines/profiles/{profileId}/medicines` - Create a new medicine for a profile
- `GET /api/medicines/profiles/{profileId}/medicines` - Get all medicines for a profile
- `GET /api/medicines` - Get all medicines for a user
- `GET /api/medicines/{medicineId}` - Get a specific medicine
- `PUT /api/medicines/{medicineId}` - Update a medicine
- `DELETE /api/medicines/{medicineId}` - Soft delete a medicine
- `POST /api/medicines/{medicineId}/takedose` - Take a dose
- `POST /api/medicines/upload-image` - Upload medicine image

### Schedule Management
- `POST /api/schedules/medicines/{medicineId}/schedules` - Create a schedule for a medicine
- `GET /api/schedules/medicines/{medicineId}/schedules` - Get all schedules for a medicine
- `GET /api/schedules/profiles/{profileId}/schedules` - Get all schedules for a profile
- `GET /api/schedules` - Get all schedules for a user
- `PUT /api/schedules/{scheduleId}` - Update a schedule
- `DELETE /api/schedules/{scheduleId}` - Delete a schedule

### Global Medicine Management
- `POST /api/global-medicines` - Create a new global medicine
- `GET /api/global-medicines` - Get all global medicines
- `GET /api/global-medicines/{id}` - Get a specific global medicine
- `GET /api/global-medicines/search?name={name}` - Search global medicines by name
- `GET /api/global-medicines/category/{category}` - Get global medicines by category
- `PUT /api/global-medicines/{id}` - Update a global medicine
- `DELETE /api/global-medicines/{id}` - Delete a global medicine

### User Management
- `POST /api/users/fcm-token` - Update FCM token

### Health Check
- `GET /` - Server status

## Security Features

### Token Invalidation on Password Change
This application implements a security feature that invalidates all JWT tokens when a user changes their password. This prevents unauthorized access if tokens are compromised.

When a user changes their password through the forgot password API:
1. The user's `password_last_changed` timestamp is updated in the database
2. During authentication, the middleware checks if the token was issued before the last password change
3. If so, the token is rejected as invalid

This approach ensures that all existing sessions are terminated when a password is changed, without needing to maintain a token blacklist.

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/medicine/tracker/
│   │       ├── MedicineTrackerApplication.java
│   │       ├── config/
│   │       │   ├── CloudinaryConfig.java
│   │       │   ├── SchedulerConfig.java
│   │       │   ├── SecurityConfig.java
│   │       │   └── LoggingConfig.java
│   │       ├── controller/
│   │       │   ├── AuthController.java
│   │       │   ├── ProfileController.java
│   │       │   ├── MedicineController.java
│   │       │   ├── ScheduleController.java
│   │       │   ├── GlobalMedicineController.java
│   │       │   ├── UserController.java
│   │       │   └── HealthCheckController.java
│   │       ├── service/
│   │       │   ├── AuthService.java
│   │       │   ├── ProfileService.java
│   │       │   ├── MedicineService.java
│   │       │   ├── ScheduleService.java
│   │       │   ├── GlobalMedicineService.java
│   │       │   ├── UserService.java
│   │       │   ├── JwtService.java
│   │       │   ├── TokenBlacklistService.java
│   │       │   ├── ImageUploadService.java
│   │       │   ├── NotificationService.java
│   │       │   └── impl/
│   │       ├── repository/
│   │       ├── model/
│   │       │   ├── entity/
│   │       │   ├── dto/
│   │       │   │   ├── request/
│   │       │   │   └── response/
│   │       ├── exception/
│   │       ├── security/
│   │       ├── scheduler/
│   │       └── util/
│   └── resources/
│       ├── application.properties
│       └── schema.sql
```

## Dependencies

- Spring Boot Web Starter
- Spring Boot Data JPA
- Spring Boot Security
- PostgreSQL Driver
- JWT (Java JWT)
- Cloudinary Java SDK
- Lombok
- Validation Starter

## Testing

To run tests:
```bash
mvn test