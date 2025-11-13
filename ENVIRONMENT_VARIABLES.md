# Environment Variables in application.properties

This document explains how environment variables are used in the `application.properties` file for the Medicine Tracker Spring Boot application.

## Syntax

In Spring Boot, environment variables are referenced using the following syntax:
```
property.name=${ENVIRONMENT_VARIABLE_NAME:default_value}
```

- `ENVIRONMENT_VARIABLE_NAME` is the name of the environment variable
- `default_value` is the value that will be used if the environment variable is not set
- The colon `:` separates the environment variable name from the default value

## Environment Variables Used in This Application

### Server Configuration
```
server.port=${PORT:8080}
```
- `PORT`: The port number for the server
- Default: 8080

### Database Configuration (Neon PostgreSQL)
```
spring.datasource.url=jdbc:postgresql://${DB_HOST:ep-dawn-sun-ado7dfxi-pooler.c-2.us-east-1.aws.neon.tech}:${DB_PORT:5432}/${DB_NAME:medicine_tracker}
```
- `DB_HOST`: The hostname of the Neon PostgreSQL database
- `DB_PORT`: The port number of the database (default: 5432)
- `DB_NAME`: The name of the database (default: medicine_tracker)

```
spring.datasource.username=${DB_USER:neondb_owner}
```
- `DB_USER`: The username for database authentication
- Default: neondb_owner

```
spring.datasource.password=${DB_PASSWORD:npg_XqdsaxYoRl47}
```
- `DB_PASSWORD`: The password for database authentication
- Default: npg_XqdsaxYoRl47

```
spring.datasource.hikari.data-source-properties.sslmode=${DB_SSL_MODE:require}
```
- `DB_SSL_MODE`: SSL mode for database connection
- Default: require (required for Neon DB)

### JWT Configuration
```
jwt.secret=${JWT_SECRET:medicineTrackerSecretKey2024SecureJwtTokenSigningKey32Chars}
```
- `JWT_SECRET`: Secret key for JWT token signing (must be at least 32 characters for HMAC-SHA256, no special characters like hyphens)
- Default: medicineTrackerSecretKey2024SecureJwtTokenSigningKey32Chars
- **IMPORTANT**: Change this to a secure random string in production! Use only alphanumeric characters to avoid Base64 encoding issues.

```
jwt.expiration=${JWT_EXPIRATION:864000}
```
- `JWT_EXPIRATION`: Token expiration time in milliseconds (default: 864000ms = 10 days)
- Default: 864000

### Cloudinary Configuration
```
cloudinary.cloud_name=${CLOUDINARY_CLOUD_NAME:dkyqykh8i}
```
- `CLOUDINARY_CLOUD_NAME`: Cloudinary cloud name
- Default: dkyqykh8i

```
cloudinary.api_key=${CLOUDINARY_API_KEY:14722523275112}
```
- `CLOUDINARY_API_KEY`: Cloudinary API key
- Default: 147225233275112

```
cloudinary.api_secret=${CLOUDINARY_API_SECRET:7DdZEtIksAYNFF857n0bTZYfSRM}
```
- `CLOUDINARY_API_SECRET`: Cloudinary API secret
- Default: 7DdZEtIksAYNFF857n0bTZYfSRM

## How Environment Variables Are Loaded

Spring Boot automatically loads environment variables from several sources:

1. **Operating System Environment Variables**: Variables set at the system level
2. **.env File**: If present in the project root
3. **JVM System Properties**: Passed via -D flag when starting the application
4. **Command Line Arguments**: Passed when starting the application

## Setting Environment Variables

### Method 1: Using a .env file (recommended)
Create a `.env` file in the project root:
```
PORT=8080
DB_HOST=your-neon-db-host
DB_PORT=5432
DB_USER=your_username
DB_PASSWORD=your_password
DB_NAME=your_database_name
JWT_SECRET=your_jwt_secret
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
```

### Method 2: Setting system environment variables
On macOS/Linux:
```bash
export DB_HOST=your-neon-db-host
export DB_USER=your_username
export DB_PASSWORD=your_password
# ... etc
```

On Windows:
```cmd
set DB_HOST=your-neon-db-host
set DB_USER=your_username
set DB_PASSWORD=your_password
```

### Method 3: Passing as JVM system properties
```bash
java -jar your-app.jar -DDB_HOST=your-neon-db-host -DDB_USER=your_username
```

### Method 4: Using Spring Boot command line arguments
```bash
java -jar your-app.jar --DB_HOST=your-neon-db-host --DB_USER=your_username
```

## Security Note

For production environments, it's important to:
1. Never commit actual credentials to version control
2. Use environment-specific configuration
3. Consider using a secrets management system
4. Ensure that default values in the properties file are safe for development only
