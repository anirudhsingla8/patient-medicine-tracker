# Medicine Tracker Backend - API Documentation

This document provides curl examples for all the APIs implemented in the Medicine Tracker Backend.

## Phase 2 Features

This backend now supports:

1. **Family/Multi-User profiles** - Multiple family members can have their own medicine inventory
2. **Complex scheduling** - Set up dosage schedules for medicines
3. **Automated notifications/alerts** - Background jobs for dosage reminders and alerts

## Security Features

### Token Invalidation on Password Change

This application implements a security feature that invalidates all JWT tokens when a user changes their password. This prevents unauthorized access if tokens are compromised.

When a user changes their password through the forgot password API:
1. The user's `password_last_changed` timestamp is updated in the database
2. During authentication, the middleware checks if the token was issued before the last password change
3. If so, the token is rejected as invalid

This approach ensures that all existing sessions are terminated when a password is changed, without needing to maintain a token blacklist.

## Environment Setup

Before running the application, you need to set up your environment variables. Copy the `.env.example` file to `.env` and fill in the values:

```bash
cp .env.example .env
```

### Required Environment Variables

1. **Database Configuration** (from Neon or Supabase dashboard):
   - `DB_HOST`: Database host
   - `DB_PORT`: Database port
   - `DB_USER`: Database user
   - `DB_PASSWORD`: Database password
   - `DB_NAME`: Database name
   - `DB_SSL_MODE`: Set to 'require' for Neon/Supabase

2. **JWT Secret**:
   - `JWT_SECRET`: A long, random string for signing JWT tokens (at least 32 characters)

3. **Cloudinary Configuration** (for image uploads):
   - `CLOUDINARY_CLOUD_NAME`: Cloudinary cloud name
   - `CLOUDINARY_API_KEY`: Cloudinary API key
   - `CLOUDINARY_API_SECRET`: Cloudinary API secret

4. **Optional**:
   - `PORT`: Server port (defaults to 3000 if not set)

### Generating a JWT Secret

You can generate a secure JWT secret using:
```bash
node -e "console.log(require('crypto').randomBytes(32).toString('hex'))"
```

## Database Setup

Before running the application, you need to set up the database schema. The schema.sql file contains all the necessary SQL commands to create the required tables.

**Important:** If you have already created the tables, you'll need to add the `password_last_changed` column to the users table:

```sql
ALTER TABLE users ADD COLUMN password_last_changed TIMESTAMPTZ DEFAULT NOW();
UPDATE users SET password_last_changed = created_at WHERE password_last_changed IS NULL;
```

### For Neon or Supabase:

1. Log in to your Neon or Supabase dashboard
2. Navigate to the SQL editor
3. Copy and paste the contents of `schema.sql` into the editor
4. Run the SQL commands to create the tables

**Note:** If you have already created the tables, you'll need to add the `composition` and `form` columns to the user_medicines table:
```sql
ALTER TABLE user_medicines ADD COLUMN composition JSONB DEFAULT '[]';
ALTER TABLE user_medicines ADD COLUMN form VARCHAR(20);
```

### For local PostgreSQL:

1. Make sure your PostgreSQL server is running
2. Connect to your database using psql:
   ```bash
   psql -U your_username -d your_database_name
   ```
3. Run the schema file:
   ```bash
   \i schema.sql
   ```

**Note:** If you have already created the tables, you'll need to add the `composition` and `form` columns to the user_medicines table:
```sql
ALTER TABLE user_medicines ADD COLUMN composition JSONB DEFAULT '[]';
ALTER TABLE user_medicines ADD COLUMN form VARCHAR(20);
```

## Database Schema

The application uses several tables to manage users, profiles, medicines, schedules, and global medicine information. Here's the complete database schema:

### users
Stores user account information.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unique identifier for the user |
| email | VARCHAR(25) | UNIQUE, NOT NULL | User's email address |
| password_hash | VARCHAR(255) | NOT NULL | Hashed user password |
| password_last_changed | TIMESTAMPTZ | DEFAULT NOW() | Timestamp of last password change |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Account creation timestamp |
| fcm_token | TEXT | OPTIONAL | Firebase Cloud Messaging token for push notifications |

### profiles
Stores family member profiles associated with user accounts.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unique identifier for the profile |
| user_id | UUID | NOT NULL, REFERENCES users(id) ON DELETE CASCADE | Link to the user who owns this profile |
| name | VARCHAR(255) | NOT NULL | Profile name (e.g., family member name) |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Profile creation timestamp |

### user_medicines
Stores medicine information for each profile.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unique identifier for the medicine |
| user_id | UUID | NOT NULL, REFERENCES users(id) ON DELETE CASCADE | Link to the user who owns this medicine |
| profile_id | UUID | NOT NULL, REFERENCES profiles(id) ON DELETE CASCADE | Link to the profile this medicine belongs to |
| name | VARCHAR(255) | NOT NULL | Medicine name |
| image_url | TEXT | OPTIONAL | URL to medicine image |
| dosage | TEXT | OPTIONAL | Dosage information |
| quantity | INTEGER | NOT NULL, DEFAULT 0 | Current quantity of medicine |
| expiry_date | DATE | NOT NULL | Medicine expiration date |
| category | VARCHAR(100) | OPTIONAL | Medicine category |
| notes | TEXT | OPTIONAL | Additional notes about the medicine |
| composition | JSONB | DEFAULT '[]' | Array of composition details |
| form | VARCHAR(20) | OPTIONAL | Medicine form (Tablet, Syrup, etc.) |
| status | VARCHAR(20) | DEFAULT 'active', CHECK (status IN ('active', 'inactive')) | Medicine status for soft deletion |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Medicine creation timestamp |
| updated_at | TIMESTAMPTZ | DEFAULT NOW() | Last update timestamp |

### schedules
Stores dosage schedules for medicines.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unique identifier for the schedule |
| medicine_id | UUID | NOT NULL, REFERENCES user_medicines(id) ON DELETE CASCADE | Link to the medicine this schedule is for |
| profile_id | UUID | NOT NULL, REFERENCES profiles(id) ON DELETE CASCADE | Link to the profile this schedule belongs to |
| user_id | UUID | NOT NULL, REFERENCES users(id) ON DELETE CASCADE | Link to the user for security scoping |
| time_of_day | TIME | NOT NULL | Time of day for the dosage |
| frequency | VARCHAR(50) | NOT NULL, DEFAULT 'daily' | Schedule frequency (daily, weekly, etc.) |
| is_active | BOOLEAN | DEFAULT true | Whether the schedule is active |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Schedule creation timestamp |

### global_medicines
Stores global medicine information that can be shared across all users.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unique identifier for the global medicine |
| name | VARCHAR(255) | NOT NULL | Medicine name |
| brand_name | VARCHAR(255) | OPTIONAL | Brand name of the medicine |
| generic_name | VARCHAR(255) | OPTIONAL | Generic name of the medicine |
| dosage_form | VARCHAR(100) | OPTIONAL | Dosage form (tablet, capsule, liquid, etc.) |
| strength | VARCHAR(100) | OPTIONAL | Medicine strength (500mg, 10ml, etc.) |
| manufacturer | VARCHAR(255) | OPTIONAL | Medicine manufacturer |
| description | TEXT | OPTIONAL | Detailed description of the medicine |
| indications | TEXT[] | OPTIONAL | Array of conditions the medicine treats |
| contraindications | TEXT[] | OPTIONAL | Array of conditions where medicine should not be used |
| side_effects | TEXT[] | OPTIONAL | Array of common side effects |
| warnings | TEXT[] | OPTIONAL | Array of warnings |
| interactions | TEXT[] | OPTIONAL | Array of drug interactions |
| storage_instructions | TEXT | OPTIONAL | Storage instructions |
| category | VARCHAR(100) | OPTIONAL | Medicine category (antibiotics, pain relievers, etc.) |
| atc_code | VARCHAR(10) | OPTIONAL | Anatomical Therapeutic Chemical Classification System code |
| fda_approval_date | DATE | OPTIONAL | FDA approval date |
| created_at | TIMESTAMPTZ | DEFAULT NOW() | Record creation timestamp |
| updated_at | TIMESTAMPTZ | DEFAULT NOW() | Last update timestamp |

### token_blacklist
Stores invalidated JWT tokens for security purposes.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PRIMARY KEY, DEFAULT uuid_generate_v4() | Unique identifier for the blacklist entry |
| token | TEXT | NOT NULL | The invalidated JWT token |
| user_id | UUID | NOT NULL, REFERENCES users(id) ON DELETE CASCADE | Link to the user who owned this token |
| expires_at | TIMESTAMPTZ | NOT NULL | Token expiration timestamp |
| blacklisted_at | TIMESTAMPTZ | DEFAULT NOW() | When the token was blacklisted |

## Database Configuration

When using cloud PostgreSQL providers like Neon or Supabase, you may need to configure SSL for secure connections. Set the following environment variable in your `.env` file:

```
DB_SSL_MODE=require
```

For local development, you can leave this variable unset or set it to an empty value.

## Authentication

### Register a new user
```bash
curl -X POST http://localhost:3000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "securepassword"
  }'
```

### Login
```bash
curl -X POST http://localhost:3000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "securepassword"
  }'
```

### Forgot Password
```bash
curl -X POST http://localhost:3000/api/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "newPassword": "newsecurepassword"
  }'
```

**Note:** Both `email` and `newPassword` fields are required. The API will return a 400 error if either field is missing.

## Medicine Management (Legacy)

**Note:** These endpoints are deprecated in favor of the profile-centric approach. All medicine endpoints require authentication. Replace `YOUR_JWT_TOKEN` with the token received from the login endpoint.

**Soft Deletion:** All delete operations now perform soft deletion by setting the medicine status to 'inactive' instead of permanently removing the record. The GET endpoints automatically filter to only show medicines with 'active' status.

**Composition Field:** The `composition` field is an optional array of objects that can be used to specify the composition of a medicine. Each object in the array should have the following properties:
- `name`: The name of the component (string)
- `strength_value`: The strength value (number)
- `strength_unit`: The strength unit (string)

Example:
```json
"composition": [
  {
    "name": "Acetylsalicylic Acid",
    "strength_value": 100,
    "strength_unit": "mg"
  }
]
```

**Form Field:** The `form` field is an optional string that indicates the type of medicine. It can be one of the following values:
- Tablet
- Syrup
- Capsule
- Injection
- Ointment
- Powder
- Drops
- Inhaler

Example:
```json
"form": "Tablet"
```

### Create a new medicine (requires profile_id)
```bash
curl -X POST http://localhost:3000/api/medicines/ \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Aspirin",
    "dosage": "100mg",
    "quantity": 30,
    "expiry_date": "2025-12-31",
    "category": "Pain Relief",
    "notes": "Take with food",
    "composition": [
      {
        "name": "Acetylsalicylic Acid",
        "strength_value": 100,
        "strength_unit": "mg"
      }
    ],
    "form": "Tablet",
    "profile_id": "PROFILE_ID"
  }'
```

### Get all medicines
```bash
curl -X GET http://localhost:3000/api/medicines/ \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Get a specific medicine
```bash
curl -X GET http://localhost:3000/api/medicines/MEDICINE_ID \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Update a medicine
```bash
curl -X PUT http://localhost:3000/api/medicines/MEDICINE_ID \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Aspirin",
    "dosage": "200mg",
    "quantity": 25,
    "expiry_date": "2025-12-31",
    "category": "Pain Relief",
    "notes": "Take with food, doubled dose",
    "composition": [
      {
        "name": "Acetylsalicylic Acid",
        "strength_value": 200,
        "strength_unit": "mg"
      }
    ],
    "form": "Tablet",
    "profile_id": "PROFILE_ID"
  }'
```

### Delete a medicine (soft delete)
```bash
curl -X DELETE http://localhost:3000/api/medicines/MEDICINE_ID \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Take a dose (decrement quantity by 1)
```bash
curl -X POST http://localhost:3000/api/medicines/MEDICINE_ID/takedose \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Upload medicine image
```bash
curl -X POST http://localhost:3000/api/medicines/upload-image \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "medicineImage=@/path/to/your/image.jpg"
```

## Health Check

### Server status
```bash
curl -X GET http://localhost:3000/
```

## Profile Management

**Note:** All profile endpoints require authentication. Replace `YOUR_JWT_TOKEN` with the token received from the login endpoint.

### Create a new profile
```bash
curl -X POST http://localhost:3000/api/profiles \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "John Doe"
  }'
```

### Get all profiles
```bash
curl -X GET http://localhost:3000/api/profiles \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Update a profile
```bash
curl -X PUT http://localhost:3000/api/profiles/PROFILE_ID \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Jane Doe"
  }'
```

### Delete a profile
```bash
curl -X DELETE http://localhost:3000/api/profiles/PROFILE_ID \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## User Management

### Update FCM token
```bash
curl -X POST http://localhost:3000/api/users/fcm-token \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "fcmToken": "YOUR_FCM_TOKEN"
  }'
```

## Medicine Management (Profile-Centric)

**Note:** All medicine endpoints require authentication. Replace `YOUR_JWT_TOKEN` with the token received from the login endpoint.

**Soft Deletion:** All delete operations now perform soft deletion by setting the medicine status to 'inactive' instead of permanently removing the record. The GET endpoints automatically filter to only show medicines with 'active' status.

**Composition Field:** The `composition` field is an optional array of objects that can be used to specify the composition of a medicine. Each object in the array should have the following properties:
- `name`: The name of the component (string)
- `strength_value`: The strength value (number)
- `strength_unit`: The strength unit (string)

Example:
```json
"composition": [
  {
    "name": "Acetylsalicylic Acid",
    "strength_value": 100,
    "strength_unit": "mg"
  }
]
```

**Form Field:** The `form` field is an optional string that indicates the type of medicine. It can be one of the following values:
- Tablet
- Syrup
- Capsule
- Injection
- Ointment
- Powder
- Drops
- Inhaler

Example:
```json
"form": "Tablet"
```

### Create a new medicine for a profile
```bash
curl -X POST http://localhost:3000/api/medicines/profiles/PROFILE_ID/medicines \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Aspirin",
    "dosage": "100mg",
    "quantity": 30,
    "expiry_date": "2025-12-31",
    "category": "Pain Relief",
    "notes": "Take with food",
    "composition": [
      {
        "name": "Acetylsalicylic Acid",
        "strength_value": 100,
        "strength_unit": "mg"
      }
    ],
    "form": "Tablet"
  }'
```

### Get all medicines for a profile
```bash
curl -X GET http://localhost:3000/api/medicines/profiles/PROFILE_ID/medicines \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Update a medicine
```bash
curl -X PUT http://localhost:3000/api/medicines/MEDICINE_ID \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Aspirin",
    "dosage": "200mg",
    "quantity": 25,
    "expiry_date": "2025-12-31",
    "category": "Pain Relief",
    "notes": "Take with food, doubled dose",
    "composition": [
      {
        "name": "Acetylsalicylic Acid",
        "strength_value": 200,
        "strength_unit": "mg"
      }
    ],
    "form": "Tablet",
    "profile_id": "PROFILE_ID"
  }'
```

### Delete a medicine (soft delete)
```bash
curl -X DELETE http://localhost:3000/api/medicines/MEDICINE_ID \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Schedule Management

### Create a schedule for a medicine
```bash
curl -X POST http://localhost:3000/api/medicines/MEDICINE_ID/schedules \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "time_of_day": "09:00",
    "frequency": "daily",
    "is_active": true
  }'
```

### Get all schedules for a medicine
```bash
curl -X GET http://localhost:3000/api/medicines/MEDICINE_ID/schedules \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Update a schedule
```bash
curl -X PUT http://localhost:3000/api/schedules/SCHEDULE_ID \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "time_of_day": "10:00",
    "frequency": "daily",
    "is_active": true
  }'
```

### Delete a schedule
```bash
curl -X DELETE http://localhost:3000/api/schedules/SCHEDULE_ID \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Global Medicine Management

**Note:** All global medicine endpoints require authentication. Replace `YOUR_JWT_TOKEN` with the token received from the login endpoint.

### Create a new global medicine
```bash
curl -X POST http://localhost:3000/api/global-medicines \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Aspirin",
    "brand_name": "Bayer",
    "generic_name": "Acetylsalicylic Acid",
    "dosage_form": "Tablet",
    "strength": "100mg",
    "manufacturer": "Bayer AG",
    "description": "Pain reliever and fever reducer",
    "indications": "Headache, muscle pain, fever",
    "contraindications": "Bleeding disorders, stomach ulcers",
    "side_effects": "Stomach upset, heartburn",
    "warnings": "Do not exceed recommended dose",
    "interactions": "Blood thinners",
    "storage_instructions": "Store at room temperature",
    "category": "Analgesic",
    "atc_code": "N02BA01",
    "fda_approval_date": "1970-01-01"
  }'
```

### Get all global medicines
```bash
curl -X GET http://localhost:3000/api/global-medicines \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Get a specific global medicine
```bash
curl -X GET http://localhost:3000/api/global-medicines/GLOBAL_MEDICINE_ID \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Update a global medicine
```bash
curl -X PUT http://localhost:3000/api/global-medicines/GLOBAL_MEDICINE_ID \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Aspirin",
    "brand_name": "Bayer",
    "generic_name": "Acetylsalicylic Acid",
    "dosage_form": "Tablet",
    "strength": "200mg",
    "manufacturer": "Bayer AG",
    "description": "Pain reliever and fever reducer",
    "indications": "Headache, muscle pain, fever",
    "contraindications": "Bleeding disorders, stomach ulcers",
    "side_effects": "Stomach upset, heartburn",
    "warnings": "Do not exceed recommended dose",
    "interactions": "Blood thinners",
    "storage_instructions": "Store at room temperature",
    "category": "Analgesic",
    "atc_code": "N02BA01",
    "fda_approval_date": "1970-01-01"
  }'
```

### Delete a global medicine
```bash
curl -X DELETE http://localhost:3000/api/global-medicines/GLOBAL_MEDICINE_ID \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"