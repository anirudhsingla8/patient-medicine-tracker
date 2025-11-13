# Troubleshooting Guide for Medicine Tracker Spring Boot Application

This guide will help you resolve common issues when running the Medicine Tracker application.

## Build Error Troubleshooting

### 1. Maven Build Errors

**Common Error:** `mvn spring-boot:run` fails during build

**Solutions:**

1. **Clean and Rebuild:**
   ```bash
   mvn clean
   mvn compile
   mvn spring-boot:run
   ```

2. **Check Java Version:**
   - Ensure you're using Java 17 or higher
   - Check with: `java -version`
   - If using a different version, install Java 17 and set JAVA_HOME

3. **Update Maven Dependencies:**
   ```bash
   mvn clean install
   ```

4. **If you get Lombok-related errors:**
   - Make sure your IDE has Lombok plugin installed
   - IntelliJ IDEA: Go to Settings → Plugins → Search for "Lombok" → Install
   - In Eclipse: Download Lombok jar and run `java -jar lombok.jar`

### 2. Common Build Issues and Solutions

**Error:** `Could not resolve dependencies`
- Solution: Check your internet connection and run `mvn clean install`

**Error:** `java: package lombok does not exist`
- Solution: Install Lombok plugin in your IDE or run with proper dependencies

**Error:** `Could not find or load main class`
- Solution: Ensure the main class is properly defined and dependencies are resolved

**Error:** `No compiler is provided in this environment`
- Solution: Ensure JAVA_HOME is set correctly and Java JDK is installed (not just JRE)

### 3. Complete Build Process
```bash
# 1. Clean previous builds
mvn clean

# 2. Install dependencies
mvn install

# 3. Compile the project
mvn compile

# 4. Run the application
mvn spring-boot:run
```

## Common Issues and Solutions

### 1. Database Connection Issues

**Error:** `org.postgresql.util.PSQLException: FATAL: password authentication failed`

**Solution:**
- Verify your database credentials in environment variables or application.properties
- Ensure your Neon DB connection pooler is active
- Check that the database user has proper permissions

**To set environment variables:**
```bash
# Option 1: Create a .env file in the project root
PORT=8080
DB_HOST=ep-dawn-sun-ado7dfxi-pooler.c-2.us-east-1.aws.neon.tech
DB_PORT=5432
DB_USER=neondb_owner
DB_PASSWORD=npg_XqdsaxYoRl47
DB_NAME=medicine_tracker
DB_SSL_MODE=require
JWT_SECRET=@1608@
CLOUDINARY_CLOUD_NAME=dkyqykh8i
CLOUDINARY_API_KEY=147225233275112
CLOUDINARY_API_SECRET=7DdZEtIksAYNFF857n0bTZYfSRM
```

**Option 2: Set environment variables directly**
```bash
export DB_HOST=ep-dawn-sun-ado7dfxi-pooler.c-2.us-east-1.aws.neon.tech
export DB_USER=neondb_owner
export DB_PASSWORD=npg_XqdsaxYoRl47
export DB_NAME=medicine_tracker
export DB_SSL_MODE=require
export JWT_SECRET=@1608@
export CLOUDINARY_CLOUD_NAME=dkyqykh8i
export CLOUDINARY_API_KEY=147225233275112
export CLOUDINARY_API_SECRET=7DdZEtIksAYNFF857n0bTZYfSRM
```

### 2. SSL Connection Issues

**Error:** `FATAL: no pg_hba.conf entry for host...SSL connection is required`

**Solution:**
- Ensure `DB_SSL_MODE=require` is set in your environment
- The application.properties already has this configured

### 3. Missing Dependencies

**Error:** `ClassNotFoundException` or `NoSuchMethodError`

**Solution:**
- Ensure Maven dependencies are properly downloaded
- Run: `mvn clean install`
- Or refresh dependencies in your IDE

### 4. Database Schema Issues

**Error:** `relation "users" does not exist`

**Solution:**
- Execute the schema.sql file on your Neon database:
```sql
-- Connect to your Neon database and run:
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Then execute all the CREATE TABLE statements from src/main/resources/schema.sql
```

### 5. Cloudinary Configuration Issues

**Error:** `com.cloudinary.ApiException` or `Connection refused`

**Solution:**
- Verify Cloudinary credentials are correct
- Ensure you have internet connection
- Check that the Cloudinary account is active

### 6. Port Already in Use

**Error:** `Web server failed to start. Port 8080 was already in use`

**Solution:**
- Change the port: `export PORT=8081` or set `SERVER_PORT=8081`
- Or kill the process using port 8080: `lsof -ti:8080 | xargs kill -9`

## How to Run the Application

### Method 1: Using Maven (Recommended)
```bash
# Clone the repository
git clone <your-repo-url>
cd <project-directory>

# Set environment variables (as shown above)

# Clean and install dependencies
mvn clean install

# Run the application
mvn spring-boot:run
```

### Method 2: Using Executable JAR
```bash
# Build the JAR
mvn clean package

# Run the JAR (with environment variables set)
java -jar target/tracker-1.0.0.jar
```

### Method 3: In IDE (IntelliJ IDEA / Eclipse)
1. Import the project as a Maven project
2. Make sure Lombok plugin is installed in your IDE
3. Set environment variables in run configuration:
   - Go to Run → Edit Configurations
   - Add environment variables as specified above
4. Run the `MedicineTrackerApplication.java` class

## Required Setup Steps

### 1. Database Setup
Before running the application, you need to set up the database schema:

```sql
-- Connect to your Neon database using any PostgreSQL client
-- For example, using psql:
psql -h your-neon-host -p 5432 -U neondb_owner -d medicine_tracker

-- Then execute the following (from schema.sql):
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    password_last_changed TIMESTAMPTZ DEFAULT NOW(),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    fcm_token TEXT
);

-- Create profiles table
CREATE TABLE profiles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create global_medicines table
CREATE TABLE global_medicines (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    brand_name VARCHAR(255),
    generic_name VARCHAR(255),
    dosage_form VARCHAR(100),
    strength VARCHAR(10),
    manufacturer VARCHAR(255),
    description TEXT,
    indications TEXT[],
    contraindications TEXT[],
    side_effects TEXT[],
    warnings TEXT[],
    interactions TEXT[],
    storage_instructions TEXT,
    category VARCHAR(100),
    atc_code VARCHAR(10),
    fda_approval_date DATE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- Create user_medicines table
CREATE TABLE user_medicines (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    profile_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    image_url TEXT,
    dosage TEXT,
    quantity INTEGER NOT NULL DEFAULT 0,
    expiry_date DATE NOT NULL,
    category VARCHAR(10),
    notes TEXT,
    composition JSONB DEFAULT '[]',
    form VARCHAR(20),
    status VARCHAR(20) DEFAULT 'active' CHECK (status IN ('active', 'inactive')),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE
);

-- Create schedules table
CREATE TABLE schedules (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    medicine_id UUID NOT NULL,
    profile_id UUID NOT NULL,
    user_id UUID NOT NULL,
    time_of_day TIME NOT NULL,
    frequency VARCHAR(50) NOT NULL DEFAULT 'daily',
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    FOREIGN KEY (medicine_id) REFERENCES user_medicines(id) ON DELETE CASCADE,
    FOREIGN KEY (profile_id) REFERENCES profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create token_blacklist table
CREATE TABLE token_blacklist (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    token TEXT NOT NULL,
    user_id UUID NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    blacklisted_at TIMESTAMPTZ DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_profiles_user_id ON profiles(user_id);
CREATE INDEX idx_user_medicines_user_id ON user_medicines(user_id);
CREATE INDEX idx_user_medicines_profile_id ON user_medicines(profile_id);
CREATE INDEX idx_user_medicines_status ON user_medicines(status);
CREATE INDEX idx_schedules_medicine_id ON schedules(medicine_id);
CREATE INDEX idx_schedules_profile_id ON schedules(profile_id);
CREATE INDEX idx_schedules_user_id ON schedules(user_id);
```

### 2. Environment Variables
Make sure all required environment variables are set:

| Variable | Description | Default Value |
|----------|-------------|---------------|
| PORT | Server port | 8080 |
| DB_HOST | Database host | ep-dawn-sun-ado7dfxi-pooler.c-2.us-east-1.aws.neon.tech |
| DB_PORT | Database port | 5432 |
| DB_USER | Database user | neondb_owner |
| DB_PASSWORD | Database password | npg_XqdsaxYoRl47 |
| DB_NAME | Database name | medicine_tracker |
| DB_SSL_MODE | SSL mode | require |
| JWT_SECRET | JWT signing secret | @1608@ |
| JWT_EXPIRATION | Token expiration (ms) | 8640000 |
| CLOUDINARY_CLOUD_NAME | Cloudinary cloud name | dkyqykh8i |
| CLOUDINARY_API_KEY | Cloudinary API key | 14725233275112 |
| CLOUDINARY_API_SECRET | Cloudinary API secret | 7DdZEtIksAYNFF857n0bTZYfSRM |

## Debugging Tips

### 1. Enable Debug Logging
Add this to application.properties:
```properties
logging.level.com.medicine.tracker=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

### 2. Check Application Startup
Look for these success messages in the logs:
```
Tomcat started on port(s): 8080 (http)
Started MedicineTrackerApplication in X.XXX seconds
```

### 3. Verify Database Connection
When the app starts, you should see:
```
HikariPool-1 - Starting...
HikariPool-1 - Added connection...
```

### 4. Test Endpoints
Once running, test the health endpoint:
```bash
curl http://localhost:8080/
```

Should return:
```json
{
  "status": "UP",
  "timestamp": "...",
  "service": "Medicine Tracker Backend",
  "version": "1.0.0"
}
```

## Common Error Messages and Solutions

| Error Message | Possible Cause | Solution |
|---------------|----------------|----------|
| `Failed to configure a DataSource` | No database configuration | Set DB environment variables |
| `Table 'users' doesn't exist` | Schema not created | Run schema.sql on your database |
| `Connection refused` | Database not accessible | Check DB_HOST, DB_PORT, network connectivity |
| `SSL error` | SSL configuration issue | Ensure DB_SSL_MODE=require |
| `JWT secret too short` | Weak JWT secret | Use at least 32 characters for JWT_SECRET |
| `Cloudinary upload failed` | Invalid credentials | Verify Cloudinary configuration |
| `Could not resolve dependencies` | Network/internet issue | Check connection and run `mvn clean install` |
| `package lombok does not exist` | Missing Lombok | Install Lombok plugin in IDE |
| `No compiler provided` | Missing JDK | Install Java JDK (not just JRE) |

## Need Further Help?

If you're still experiencing issues:

1. Check the complete application startup logs
2. Verify your Neon DB is active and accessible
3. Ensure all environment variables are properly set
4. Confirm the database schema is created
5. Check that your Cloudinary account is active
6. Make sure you have Java JDK 17+ installed (not just JRE)
7. Install Lombok plugin in your IDE if needed

You can also run the application with more verbose logging:
```bash
java -jar target/tracker-1.0.jar --logging.level.root=DEBUG
```

## Additional Configuration Notes

- The JWT expiration is set to 100 days (864000 milliseconds) - adjust as needed
- The application uses PostgreSQL with UUID support (requires uuid-ossp extension)
- Make sure the database has the uuid-ossp extension enabled
- If using an older version of PostgreSQL, you may need to enable the extension: `CREATE EXTENSION IF NOT EXISTS "uuid-ossp";`
- For Lombok to work properly, ensure your IDE has the Lombok plugin installed and annotation processing is enabled