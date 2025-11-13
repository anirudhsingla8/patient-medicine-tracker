# Medicine Tracker Application - API Blueprint for Frontend Team

## 📋 Table of Contents
1. [Application Overview](#application-overview)
2. [Application Motto & Purpose](#application-motto--purpose)
3. [Key Features](#key-features)
4. [Authentication Flow](#authentication-flow)
5. [Complete API Reference](#complete-api-reference)
6. [Data Models](#data-models)
7. [Error Handling](#error-handling)
8. [UI/UX Recommendations](#uiux-recommendations)

---

## 🎯 Application Overview

**Medicine Tracker** is a comprehensive healthcare management application that helps users track their medications, manage dosage schedules, and receive timely reminders for taking medicines and expiry alerts.

### Application Motto & Purpose

**Motto**: *"Never Miss a Dose, Never Waste a Medicine"*

**Purpose**: 
- Help users manage multiple medications for themselves and family members
- Prevent missed doses through automated reminders
- Reduce medicine waste by tracking expiry dates
- Maintain a comprehensive medication history
- Provide detailed medicine information from a global database

**Target Users**:
- Elderly patients managing multiple medications
- Parents tracking children's medications
- Caregivers managing medicines for multiple family members
- Anyone with chronic conditions requiring regular medication

---

## 🌟 Key Features

### 1. **Multi-Profile Management**
- Create separate profiles for family members
- Track medications independently for each profile
- Manage schedules per profile

### 2. **Medicine Management**
- Add medicines with photos (via Cloudinary)
- Track dosage, quantity, and expiry dates
- Categorize medicines
- View medicine composition and details
- Mark medicines as active/inactive

### 3. **Smart Scheduling**
- Set up dosage schedules (daily, weekly, biweekly, monthly, custom)
- Multiple schedules per medicine
- Automated reminders at scheduled times

### 4. **Global Medicine Database**
- Search from a comprehensive medicine database
- View detailed medicine information (indications, contraindications, side effects)
- Get manufacturer and FDA approval information

### 5. **Notifications**
- Push notifications for dosage reminders (via FCM)
- Expiry alerts (7 days before expiry)
- Real-time notifications on mobile devices

### 6. **Security**
- JWT-based authentication
- Secure password management
- Token blacklisting for logout
- Password reset functionality

---

## 🔐 Authentication Flow

### Registration → Login → Access Protected Resources

```
1. User registers → Receives JWT token
2. User logs in → Receives JWT token
3. Include token in all subsequent requests: Authorization: Bearer <token>
4. Token expires after 10 days (configurable)
5. User can logout (token gets blacklisted)
```

---

## 📡 Complete API Reference

### Base URL
```
http://localhost:8080/api
```

---

## 1️⃣ Authentication APIs

### 1.1 Register User
**Purpose**: Create a new user account

**Endpoint**: `POST /auth/register`

**Request Body**:
```json
{
  "email": "user@example.com",
  "password": "SecurePassword123"
}
```

**Response** (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "user@example.com"
}
```

**Validation Rules**:
- Email must be valid format
- Email must be unique
- Password minimum 6 characters

**UI/UX Notes**:
- Show password strength indicator
- Validate email format in real-time
- Show error if email already exists

---

### 1.2 Login
**Purpose**: Authenticate existing user

**Endpoint**: `POST /auth/login`

**Request Body**:
```json
{
  "email": "user@example.com",
  "password": "SecurePassword123"
}
```

**Response** (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "user@example.com"
}
```

**UI/UX Notes**:
- Store token securely (localStorage/sessionStorage)
- Redirect to dashboard on success
- Show "Remember Me" option

---

### 1.3 Forgot Password
**Purpose**: Reset user password

**Endpoint**: `POST /auth/forgot-password`

**Request Body**:
```json
{
  "email": "user@example.com",
  "newPassword": "NewSecurePassword123"
}
```

**Response** (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "user@example.com"
}
```

**UI/UX Notes**:
- Confirm new password with re-entry field
- Show password strength indicator
- Auto-login after password reset

---

### 1.4 Logout
**Purpose**: Invalidate user's JWT token

**Endpoint**: `POST /auth/logout`

**Headers**:
```
Authorization: Bearer <token>
```

**Response** (200 OK):
```json
{
  "message": "Logged out successfully"
}
```

**UI/UX Notes**:
- Clear stored token
- Redirect to login page
- Clear all user data from state

---

## 2️⃣ Profile Management APIs

### 2.1 Create Profile
**Purpose**: Create a new profile (e.g., for family member)

**Endpoint**: `POST /profiles`

**Headers**:
```
Authorization: Bearer <token>
```

**Request Body**:
```json
{
  "name": "John Doe"
}
```

**Response** (201 Created):
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "userId": "660e8400-e29b-41d4-a716-446655440001",
  "name": "John Doe",
  "createdAt": "2024-01-15T10:30:00"
}
```

**UI/UX Notes**:
- Simple form with name field
- Show profile avatar/icon selection
- List all profiles on dashboard

---

### 2.2 Get All Profiles
**Purpose**: Retrieve all profiles for logged-in user

**Endpoint**: `GET /profiles`

**Headers**:
```
Authorization: Bearer <token>
```

**Response** (200 OK):
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "userId": "660e8400-e29b-41d4-a716-446655440001",
    "name": "John Doe",
    "createdAt": "2024-01-15T10:30:00"
  },
  {
    "id": "550e8400-e29b-41d4-a716-446655440002",
    "userId": "660e8400-e29b-41d4-a716-446655440001",
    "name": "Jane Doe",
    "createdAt": "2024-01-16T11:00:00"
  }
]
```

**UI/UX Notes**:
- Display as cards/tiles
- Show medicine count per profile
- Quick access to add medicine

---

### 2.3 Get Profile by ID
**Purpose**: Get specific profile details

**Endpoint**: `GET /profiles/{profileId}`

**Headers**:
```
Authorization: Bearer <token>
```

**Response** (200 OK):
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "userId": "660e8400-e29b-41d4-a716-446655440001",
  "name": "John Doe",
  "createdAt": "2024-01-15T10:30:00"
}
```

---

### 2.4 Update Profile
**Purpose**: Update profile information

**Endpoint**: `PUT /profiles/{profileId}`

**Headers**:
```
Authorization: Bearer <token>
```

**Request Body**:
```json
{
  "name": "John Smith"
}
```

**Response** (200 OK):
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "userId": "660e8400-e29b-41d4-a716-446655440001",
  "name": "John Smith",
  "createdAt": "2024-01-15T10:30:00"
}
```

---

### 2.5 Delete Profile
**Purpose**: Delete a profile

**Endpoint**: `DELETE /profiles/{profileId}`

**Headers**:
```
Authorization: Bearer <token>
```

**Response** (204 No Content)

**UI/UX Notes**:
- Show confirmation dialog
- Warn about deleting associated medicines
- Cannot be undone

---

## 3️⃣ Medicine Management APIs

### 3.1 Create Medicine
**Purpose**: Add a new medicine to a profile

**Endpoint**: `POST /medicines/profiles/{profileId}/medicines`

**Headers**:
```
Authorization: Bearer <token>
```

**Request Body**:
```json
{
  "name": "Aspirin",
  "imageUrl": "https://res.cloudinary.com/...",
  "dosage": "500mg",
  "quantity": 30,
  "expiryDate": "2025-12-31",
  "category": "Pain Relief",
  "notes": "Take with food",
  "composition": [
    {
      "name": "Acetylsalicylic Acid",
      "strengthValue": "500",
      "strengthUnit": "mg"
    }
  ],
  "form": "Tablet"
}
```

**Response** (201 Created):
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440000",
  "userId": "660e8400-e29b-41d4-a716-446655440001",
  "profileId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Aspirin",
  "imageUrl": "https://res.cloudinary.com/...",
  "dosage": "500mg",
  "quantity": 30,
  "expiryDate": "2025-12-31",
  "category": "Pain Relief",
  "notes": "Take with food",
  "composition": [
    {
      "name": "Acetylsalicylic Acid",
      "strengthValue": "500",
      "strengthUnit": "mg"
    }
  ],
  "form": "Tablet",
  "status": "ACTIVE",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**UI/UX Notes**:
- Multi-step form (basic info → composition → image)
- Image upload with preview
- Auto-suggest from global medicine database
- Date picker for expiry date
- Category dropdown

---

### 3.2 Upload Medicine Image
**Purpose**: Upload medicine image to Cloudinary

**Endpoint**: `POST /medicines/upload-image`

**Headers**:
```
Authorization: Bearer <token>
Content-Type: multipart/form-data
```

**Request Body** (Form Data):
```
image: <file>
```

**Response** (200 OK):
```json
{
  "imageUrl": "https://res.cloudinary.com/dkyqykh8i/image/upload/v1234567890/medicine_abc123.jpg"
}
```

**UI/UX Notes**:
- Drag-and-drop image upload
- Show upload progress
- Image preview before upload
- Max size: 10MB

---

### 3.3 Get All Medicines for Profile
**Purpose**: Get all medicines for a specific profile

**Endpoint**: `GET /medicines/profiles/{profileId}/medicines`

**Headers**:
```
Authorization: Bearer <token>
```

**Response** (200 OK):
```json
[
  {
    "id": "770e8400-e29b-41d4-a716-446655440000",
    "name": "Aspirin",
    "dosage": "500mg",
    "quantity": 30,
    "expiryDate": "2025-12-31",
    "status": "ACTIVE",
    ...
  }
]
```

**UI/UX Notes**:
- Grid/list view toggle
- Filter by status (active/inactive)
- Sort by expiry date, name
- Search functionality

---

### 3.4 Get All Medicines for User
**Purpose**: Get all medicines across all profiles

**Endpoint**: `GET /medicines`

**Headers**:
```
Authorization: Bearer <token>
```

**Response** (200 OK):
```json
[
  {
    "id": "770e8400-e29b-41d4-a716-446655440000",
    "profileId": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Aspirin",
    ...
  }
]
```

**UI/UX Notes**:
- Show profile name with each medicine
- Filter by profile
- Expiry alerts highlighted

---

### 3.5 Get Medicine by ID
**Purpose**: Get detailed medicine information

**Endpoint**: `GET /medicines/{medicineId}`

**Headers**:
```
Authorization: Bearer <token>
```

**Response** (200 OK):
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440000",
  "userId": "660e8400-e29b-41d4-a716-446655440001",
  "profileId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Aspirin",
  "imageUrl": "https://res.cloudinary.com/...",
  "dosage": "500mg",
  "quantity": 30,
  "expiryDate": "2025-12-31",
  "category": "Pain Relief",
  "notes": "Take with food",
  "composition": [...],
  "form": "Tablet",
  "status": "ACTIVE",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

### 3.6 Update Medicine
**Purpose**: Update medicine details

**Endpoint**: `PUT /medicines/{medicineId}`

**Headers**:
```
Authorization: Bearer <token>
```

**Request Body**:
```json
{
  "name": "Aspirin",
  "dosage": "500mg",
  "quantity": 25,
  "expiryDate": "2025-12-31",
  "category": "Pain Relief",
  "notes": "Take with food after meals",
  "composition": [...],
  "form": "Tablet"
}
```

**Response** (200 OK):
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440000",
  ...
  "updatedAt": "2024-01-16T14:20:00"
}
```

---

### 3.7 Delete Medicine
**Purpose**: Delete a medicine

**Endpoint**: `DELETE /medicines/{medicineId}`

**Headers**:
```
Authorization: Bearer <token>
```

**Response** (204 No Content)

**UI/UX Notes**:
- Confirmation dialog
- Warn about deleting associated schedules

---

### 3.8 Take Dose
**Purpose**: Record taking a dose (decrements quantity)

**Endpoint**: `POST /medicines/{medicineId}/take-dose`

**Headers**:
```
Authorization: Bearer <token>
```

**Response** (200 OK):
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440000",
  "quantity": 29,
  ...
}
```

**UI/UX Notes**:
- Quick action button
- Show confirmation toast
- Update quantity in real-time
- Alert if quantity is low

---

## 4️⃣ Schedule Management APIs

### 4.1 Create Schedule
**Purpose**: Create a dosage schedule for a medicine

**Endpoint**: `POST /schedules/medicines/{medicineId}/schedules`

**Headers**:
```
Authorization: Bearer <token>
```

**Request Body**:
```json
{
  "timeOfDay": "08:00:00",
  "frequency": "DAILY",
  "isActive": true
}
```

**Frequency Options**:
- `DAILY` - Every day
- `WEEKLY` - Once a week
- `BIWEEKLY` - Every two weeks
- `MONTHLY` - Once a month
- `CUSTOM` - Custom frequency

**Response** (201 Created):
```json
{
  "id": "880e8400-e29b-41d4-a716-446655440000",
  "medicineId": "770e8400-e29b-41d4-a716-446655440000",
  "profileId": "550e8400-e29b-41d4-a716-446655440000",
  "userId": "660e8400-e29b-41d4-a716-446655440001",
  "timeOfDay": "08:00:00",
  "frequency": "DAILY",
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00"
}
```

**UI/UX Notes**:
- Time picker for timeOfDay
- Frequency dropdown
- Multiple schedules per medicine
- Visual calendar view

---

### 4.2 Get Schedules for Medicine
**Purpose**: Get all schedules for a specific medicine

**Endpoint**: `GET /schedules/medicines/{medicineId}/schedules`

**Headers**:
```
Authorization: Bearer <token>
```

**Response** (200 OK):
```json
[
  {
    "id": "880e8400-e29b-41d4-a716-446655440000",
    "medicineId": "770e8400-e29b-41d4-a716-446655440000",
    "timeOfDay": "08:00:00",
    "frequency": "DAILY",
    "isActive": true,
    ...
  }
]
```

---

### 4.3 Get Schedules for Profile
**Purpose**: Get all schedules for a profile

**Endpoint**: `GET /schedules/profiles/{profileId}/schedules`

**Headers**:
```
Authorization: Bearer <token>
```

**Response** (200 OK):
```json
[
  {
    "id": "880e8400-e29b-41d4-a716-446655440000",
    "medicineId": "770e8400-e29b-41d4-a716-446655440000",
    "profileId": "550e8400-e29b-41d4-a716-446655440000",
    "timeOfDay": "08:00:00",
    "frequency": "DAILY",
    "isActive": true,
    ...
  }
]
```

**UI/UX Notes**:
- Timeline view of schedules
- Group by time of day
- Show medicine name with each schedule

---

### 4.4 Get All Schedules for User
**Purpose**: Get all schedules across all profiles

**Endpoint**: `GET /schedules`

**Headers**:
```
Authorization: Bearer <token>
```

**Response** (200 OK):
```json
[
  {
    "id": "880e8400-e29b-41d4-a716-446655440000",
    "medicineId": "770e8400-e29b-41d4-a716-446655440000",
    "profileId": "550e8400-e29b-41d4-a716-446655440000",
    "userId": "660e8400-e29b-41d4-a716-446655440001",
    "timeOfDay": "08:00:00",
    "frequency": "DAILY",
    "isActive": true,
    ...
  }
]
```

**UI/UX Notes**:
- Dashboard view with today's schedules
- Upcoming schedules
- Past due schedules highlighted

---

### 4.5 Get Schedule by ID
**Purpose**: Get specific schedule details

**Endpoint**: `GET /schedules/{scheduleId}`

**Headers**:
```
Authorization: Bearer <token>
```

**Response** (200 OK):
```json
{
  "id": "880e8400-e29b-41d4-a716-446655440000",
  "medicineId": "770e8400-e29b-41d4-a716-446655440000",
  "profileId": "550e8400-e29b-41d4-a716-446655440000",
  "userId": "660e8400-e29b-41d4-a716-446655440001",
  "timeOfDay": "08:00:00",
  "frequency": "DAILY",
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00"
}
```

---

### 4.6 Update Schedule
**Purpose**: Update schedule details

**Endpoint**: `PUT /schedules/{scheduleId}`

**Headers**:
```
Authorization: Bearer <token>
```

**Request Body**:
```json
{
  "timeOfDay": "09:00:00",
  "frequency": "DAILY",
  "isActive": true
}
```

**Response** (200 OK):
```json
{
  "id": "880e8400-e29b-41d4-a716-446655440000",
  "timeOfDay": "09:00:00",
  "frequency": "DAILY",
  "isActive": true,
  ...
}
```

---

### 4.7 Delete Schedule
**Purpose**: Delete a schedule

**Endpoint**: `DELETE /schedules/{scheduleId}`

**Headers**:
```
Authorization: Bearer <token>
```

**Response** (204 No Content)

**UI/UX Notes**:
- Confirmation dialog
- Option to deactivate instead of delete

---

## 5️⃣ Global Medicine Database APIs

### 5.1 Create Global Medicine (Admin)
**Purpose**: Add medicine to global database

**Endpoint**: `POST /global-medicines`

**Headers**:
```
Authorization: Bearer <token>
```

**Request Body**:
```json
{
  "name": "Aspirin",
  "brandName": "Bayer Aspirin",
  "genericName": "Acetylsalicylic Acid",
  "dosageForm": "Tablet",
  "strength": "500mg",
  "manufacturer": "Bayer",
  "description": "Pain reliever and fever reducer",
  "indications": ["Pain relief", "Fever reduction", "Anti-inflammatory"],
  "contraindications": ["Bleeding disorders", "Stomach ulcers"],
  "sideEffects": ["Stomach upset", "Nausea"],
  "warnings": ["Do not exceed recommended dose"],
  "interactions": ["Blood thinners", "NSAIDs"],
  "storageInstructions": "Store at room temperature",
  "category": "Pain Relief",
  "atcCode": "N02BA01",
  "fdaApprovalDate": "1950-01-01"
}
```

**Response** (201 Created):
```json
{
  "id": "990e8400-e29b-41d4-a716-446655440000",
  "name": "Aspirin",
  "brandName": "Bayer Aspirin",
  ...
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

### 5.2 Get All Global Medicines
**Purpose**: Get all medicines from global database

**Endpoint**: `GET /global-medicines`

**Headers**:
```
Authorization: Bearer <token>
```

**Response** (200 OK):
```json
[
  {
    "id": "990e8400-e29b-41d4-a716-446655440000",
    "name": "Aspirin",
    "brandName": "Bayer Aspirin",
    "genericName": "Acetylsalicylic Acid",
    ...
  }
]
```

**UI/UX Notes**:
- Searchable list
- Filter by category
- Pagination for large lists

---

### 5.3 Search Global Medicines by Name
**Purpose**: Search medicines by name

**Endpoint**: `GET /global-medicines/search?name={searchTerm}`

**Headers**:
```
Authorization: Bearer <token>
```

**Example**: `GET /global-medicines/search?name=aspirin`

**Response** (200 OK):
```json
[
  {
    "id": "990e8400-e29b-41d4-a716-446655440000",
    "name": "Aspirin",
    "brandName": "Bayer Aspirin",
    ...
  }
]
```

**UI/UX Notes**:
- Auto-complete search
- Show results as user types
- Click to view details or add to profile

---

### 5.4 Get Global Medicines by Category
**Purpose**: Filter medicines by category

**Endpoint**: `GET /global-medicines/category/{category}`

**Headers**:
```
Authorization: Bearer <token>
```

**Example**: `GET /global-medicines/category/Pain Relief`

**Response** (200 OK):
```json
[
  {
    "id": "990e8400-e29b-41d4-a716-446655440000",
    "name": "Aspirin",
    "category": "Pain Relief",
    ...
  }
]
```

---

### 5.5 Get Global Medicine by ID
**Purpose**: Get detailed medicine information

**Endpoint**: `GET /global-medicines/{id}`

**Headers**:
```
Authorization: Bearer <token>
```

**Response** (200 OK):
```json
{
  "id": "990e8400-e29b-41d4-a716-446655440000",
  "name": "Aspirin",
  "brandName": "Bayer Aspirin",
  "genericName": "Acetylsalicylic Acid",
  "dosageForm": "Tablet",
  "strength": "500mg",
  "manufacturer": "Bayer",
  "description": "Pain reliever and fever reducer",
  "indications": ["Pain relief", "Fever reduction"],
  "contraindications": ["Bleeding disorders"],
  "sideEffects": ["Stomach upset"],
  "warnings": ["Do not exceed recommended dose"],
  "interactions": ["Blood thinners"],
  "storageInstructions": "Store at room temperature",
  "category": "Pain Relief",
  "atcCode": "N02BA01",
  "fdaApprovalDate": "1950-01-01",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**UI/UX Notes**:
- Detailed view modal/page
- Sections for indications, side effects, warnings
- "Add to My Medicines" button

---

### 5.6 Update Global Medicine (Admin)
**Purpose**: Update global medicine information

**Endpoint**: `PUT /global-medicines/{id}`

**Headers**:
```
Authorization: Bearer <token>
```

**Request Body**: Same as Create

**Response** (200 OK): Updated medicine object

---

### 5.7 Delete Global Medicine (Admin)
**Purpose**: Delete from global database

**Endpoint**: `DELETE /global-medicines/{id}`

**Headers**:
```
Authorization: Bearer <token>
```

**Response** (204 No Content)

---

## 6️⃣ User Management APIs

### 6.1 Update FCM Token
**Purpose**: Register device for push notifications

**Endpoint**: `POST /users/fcm-token`

**Headers**:
```
Authorization: Bearer <token>
```

**Request Body**:
```json
{
  "fcmToken": "device-fcm-token-from-firebase-sdk"
}
```

**Response** (200 OK): Empty response

**UI/UX Notes**:
- Call this after user grants notification permission
- Update token on app launch
- Handle token refresh

---

## 7️⃣ Health Check API

### 7.1 Server Status
**Purpose**: Check if server is running

**Endpoint**: `GET /health`

**No authentication required**

**Response** (200 OK):
```json
{
  "status": "UP",
  "timestamp": "2024-01-15T10:30:00",
  "version": "1.0.0"
}
```

**UI/UX Notes**:
- Use for connectivity checks
- Show offline indicator if fails

---

## 📊 Data Models

### User
```typescript
interface User {
  id: string;
  email: string;
  password: string; // hashed
  passwordLastChanged: string;
  createdAt: string;
  fcmToken?: string;
}
```

### Profile
```typescript
interface Profile {
  id: string;
  userId: string;
  name: string;
  createdAt: string;
}
```

### Medicine
```typescript
interface Medicine {
  id: string;
  userId: string;
  profileId: string;
  name: string;
  imageUrl?: string;
  dosage: string;
  quantity: number;
  expiryDate: string; // YYYY-MM-DD
  category?: string;
  notes?: string;
  composition?: Composition[];
  form?: string;
  status: 'ACTIVE' | 'INACTIVE';
  createdAt: string;
  updatedAt: string;
}

interface Composition {
  name: string;
  strengthValue: string;
  strengthUnit: string;
}
```

### Schedule
```typescript
interface Schedule {
  id: string;
  medicineId: string;
  profileId: string;
  userId: string;
  timeOfDay: string; // HH:mm:ss
  frequency: 'DAILY' | 'WEEKLY' | 'BIWEEKLY' | 'MONTHLY' | 'CUSTOM';
  isActive: boolean;
  createdAt: string;
}
```

### Global Medicine
```typescript
interface GlobalMedicine {
  id: string;
  name: string;
  brandName?: string;
  genericName?: string;
  dosageForm?: string;
  strength?: string;
  manufacturer?: string;
  description?: string;
  indications?: string[];
  contraindications?: string[];
  sideEffects?: string[];
  warnings?: string[];
  interactions?: string[];
  storageInstructions?: string;
  category?: string;
  atcCode?: string;
  fdaApprovalDate?: string;
  createdAt: string;
  updatedAt: string;
}
```

---

## ⚠️ Error Handling

### Error Response Format
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "details": {
    "email": "Email is required",
    "password": "Password must be at least 6 characters"
  }
}
```

### Common HTTP Status Codes

| Code | Meaning | When It Occurs |
|------|---------|----------------|
| 200 | OK | Successful GET, PUT requests |
| 201 | Created | Successful POST (resource created) |
| 204 | No Content | Successful DELETE |
| 400 | Bad Request | Validation errors, invalid data |
| 401 | Unauthorized | Missing/invalid JWT token |
| 403 | Forbidden | Valid token but insufficient permissions |
| 404 | Not Found | Resource doesn't exist |
| 409 | Conflict | Email already exists (registration) |
| 500 | Internal Server Error | Server-side error |

### Error Handling in UI

```typescript
// Example error handling
try {
  const response = await fetch('/api/medicines', {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });
  
  if (!response.ok) {
    const error = await response.json();
    
    switch (response.status) {
      case 401:
        // Redirect to login
        break;
      case 400:
        // Show validation errors
        showErrors(error.details);
        break;
      case 404:
        // Show not found message
        break;
      default:
        // Show generic error
        showError(error.message);
    }
  }
  
  const data = await response.json();
  // Handle success
} catch (error) {
  // Network error
  showError('Network error. Please check your connection.');
}
```

---

## 🎨 UI/UX Recommendations

### 1. Dashboard
- **Today's Schedule**: Show all medicines due today with time
- **Upcoming Doses**: Next 3-5 scheduled doses
- **Expiry Alerts**: Medicines expiring within 7 days
- **Quick Actions**: Add medicine, take dose, view profiles
- **Statistics**: Total medicines, active schedules, profiles

### 2. Profile Management
- **Profile Cards**: Visual cards with avatar/icon
- **Medicine Count**: Show number of medicines per profile
- **Quick Add**: Add medicine directly from profile card
- **Color Coding**: Different colors for different profiles

### 3. Medicine List
- **Grid/List View**: Toggle between views
- **Filters**: Active/Inactive, Category, Expiry date
- **Search**: Real-time search by name
- **Sort**: By name, expiry date, quantity
- **Visual Indicators**: 
  - Red badge for expired
  - Yellow badge for expiring soon
  - Low quantity warning

### 4. Medicine Details
- **Image**: Large medicine image at top
- **Tabs**: Info, Composition, Schedules, History
- **Quick Actions**: Edit, Delete, Take Dose
- **Expiry Countdown**: Days until expiry
- **Quantity Tracker**: Visual progress bar

### 5. Schedule Management
- **Calendar View**: Monthly calendar with scheduled doses
- **Timeline View**: Daily timeline showing all doses
- **Color Coding**: Different colors for different medicines
- **Notifications Toggle**: Enable/disable per schedule

### 6. Notifications
- **In-App**: Show notification badge
- **Push Notifications**: For dosage reminders and expiry alerts
- **Notification Center**: List of all notifications
- **Mark as Taken**: Quick action from notification

### 7. Search & Add Medicine
- **Auto-Complete**: Search global database as user types
- **Quick Add**: Add from search results
- **Manual Entry**: Option to add custom medicine
- **Image Upload**: Drag-and-drop or camera capture

### 8. Responsive Design
- **Mobile First**: Optimize for mobile devices
- **Tablet Support**: Utilize larger screen space
- **Desktop**: Multi-column layout

### 9. Accessibility
- **High Contrast**: For elderly users
- **Large Text**: Adjustable font sizes
- **Voice Commands**: For hands-free operation
- **Screen Reader**: Support for visually impaired

### 10. Offline Support
- **Cache Data**: Store recent data locally
- **Sync on Reconnect**: Sync when back online
- **Offline Indicator**: Show connection status

---

## 🔔 Push Notification Flow

### Setup
1. User grants notification permission
2. App gets FCM token from Firebase SDK
3. App sends token to backend via `POST /users/fcm-token`
4. Backend stores token in user record

### Notification Types

#### Dosage Reminder
- **Trigger**: Scheduled time matches current time
- **Title**: "Medicine Reminder"
- **Body**: "Time to take your medicine: [Medicine Name]"
- **Action**: Open app to medicine details

#### Expiry Alert
- **Trigger**: Medicine expiring within 7 days
- **Title**: "Medicine Expiry Alert"
- **Body**: "Your medicine '[Medicine Name]' is expiring on [Date]"
- **Action**: Open app to medicine details

### Implementation Notes
- Notifications are sent by backend scheduler
- Currently logging only (Firebase not fully integrated)
- Frontend needs to implement Firebase SDK
- Handle notification clicks to navigate to relevant screen

---

## 🚀 Getting Started for Frontend Team

### 1. Authentication Flow
```
1. Build login/register screens
2. Store JWT token securely
3. Include token in all API requests
4. Handle token expiration (redirect to login)
5. Implement logout (clear token)
```

### 2. Core Features Priority
```
Priority 1: Authentication (Login, Register)
Priority 2: Profile Management (Create, List, Select)
Priority 3: Medicine Management (Add, List, View, Edit)
Priority 4: Schedule Management (Create, List, View)
Priority 5: Notifications (FCM integration)
Priority 6: Global Medicine Search
```

### 3. State Management
- Use Redux/Context for global state
- Store: user, profiles, medicines, schedules
- Persist token and user data

### 4. API Integration
- Create API service layer
- Centralize API calls
- Handle errors consistently
- Add loading states

### 5. Testing
- Test all API endpoints with Postman collection
- Test error scenarios
- Test offline behavior
- Test notification handling

---

## 📝 Notes

1. **Base URL**: Update to production URL when deploying
2. **JWT Token**: Expires after 10 days (configurable)
3. **Image Upload**: Max size 10MB, stored in Cloudinary
4. **Date Format**: ISO 8601 (YYYY-MM-DD for dates, HH:mm:ss for times)
5. **UUIDs**: All IDs are UUIDs (36 characters)
6. **Pagination**: Not implemented yet (future enhancement)
7. **Rate Limiting**: Not implemented yet (future enhancement)

---

## 🔗 Additional Resources

- **Postman Collection**: `Medicine_Tracker_API_Collection.postman_collection.json`
- **Environment Variables**: See `ENVIRONMENT_VARIABLES.md`
- **Troubleshooting**: See `TROUBLESHOOTING.md`
- **Technical Blueprint**: See `BLUEPRINT.md`

---

**Last Updated**: October 31, 2024  
**API Version**: 1.0.0  
**Backend Framework**: Spring Boot 3.2.0  
**Database**: PostgreSQL (Neon)
