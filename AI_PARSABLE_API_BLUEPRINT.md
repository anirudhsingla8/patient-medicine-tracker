# Medicine Tracker API Blueprint - AI Parsable Format

## METADATA
```yaml
api_version: "1.0.0"
base_url: "http://localhost:8080/api"
authentication_type: "Bearer JWT"
token_header: "Authorization: Bearer {token}"
token_expiry: "10 days"
content_type: "application/json"
```

## APPLICATION_INFO
```yaml
name: "Medicine Tracker"
purpose: "Healthcare management application for medication tracking and reminders"
motto: "Never Miss a Dose, Never Waste a Medicine"
key_features:
  - multi_profile_management
  - medicine_tracking
  - dosage_scheduling
  - expiry_alerts
  - push_notifications
  - global_medicine_database
```

## API_ENDPOINTS

### AUTH_001: Register User
```yaml
endpoint: POST /auth/register
authentication: false
use_case: "Create new user account for first-time app users"
when_to_use:
  - User downloads app for first time
  - User wants to create account
  - User needs cloud sync capability
request_body:
  email: string|required|email_format
  password: string|required|min_length:6
response_200:
  token: string
  email: string
errors:
  409: "Email already exists"
  400: "Validation failed"
```

### AUTH_002: Login User
```yaml
endpoint: POST /auth/login
authentication: false
use_case: "Authenticate existing user and get access token"
when_to_use:
  - User returns to app after logout
  - User switches devices
  - Token expired and needs renewal
request_body:
  email: string|required|email_format
  password: string|required
response_200:
  token: string
  email: string
errors:
  401: "Invalid credentials"
  400: "Validation failed"
```

### AUTH_003: Forgot Password
```yaml
endpoint: POST /auth/forgot-password
authentication: false
use_case: "Reset password when user forgets credentials"
when_to_use:
  - User forgets password
  - User wants to change compromised password
  - Account recovery needed
request_body:
  email: string|required|email_format
  newPassword: string|required|min_length:6
response_200:
  token: string
  email: string
errors:
  404: "User not found"
  400: "Validation failed"
```

### AUTH_004: Logout
```yaml
endpoint: POST /auth/logout
authentication: true
use_case: "Invalidate current session and blacklist token"
when_to_use:
  - User wants to sign out
  - Switching accounts
  - Security logout from all devices
request_body: null
response_200:
  message: string
errors:
  401: "Unauthorized"
```

### PROFILE_001: Create Profile
```yaml
endpoint: POST /profiles
authentication: true
use_case: "Create family member profile for medicine tracking"
when_to_use:
  - Adding family member (child, parent, spouse)
  - Creating pet profile
  - Setting up caregiver profiles
request_body:
  name: string|required|max_length:100
response_201:
  id: uuid
  userId: uuid
  name: string
  createdAt: datetime
errors:
  401: "Unauthorized"
  400: "Validation failed"
```

### PROFILE_002: Get All Profiles
```yaml
endpoint: GET /profiles
authentication: true
use_case: "List all family member profiles for selection"
when_to_use:
  - Profile switcher UI
  - Dashboard overview
  - Medicine assignment screen
request_body: null
response_200:
  type: array
  items:
    id: uuid
    userId: uuid
    name: string
    createdAt: datetime
errors:
  401: "Unauthorized"
```

### PROFILE_003: Get Profile By ID
```yaml
endpoint: GET /profiles/{profileId}
authentication: true
use_case: "Get specific profile details for editing or viewing"
when_to_use:
  - Profile edit screen
  - Profile details view
  - Validating profile existence
path_params:
  profileId: uuid|required
request_body: null
response_200:
  id: uuid
  userId: uuid
  name: string
  createdAt: datetime
errors:
  401: "Unauthorized"
  404: "Profile not found"
```

### PROFILE_004: Update Profile
```yaml
endpoint: PUT /profiles/{profileId}
authentication: true
use_case: "Edit profile name or details"
when_to_use:
  - Correcting profile name
  - Updating profile information
  - Profile management screen
path_params:
  profileId: uuid|required
request_body:
  name: string|required|max_length:100
response_200:
  id: uuid
  userId: uuid
  name: string
  createdAt: datetime
errors:
  401: "Unauthorized"
  404: "Profile not found"
  400: "Validation failed"
```

### PROFILE_005: Delete Profile
```yaml
endpoint: DELETE /profiles/{profileId}
authentication: true
use_case: "Remove profile and all associated medicines/schedules"
when_to_use:
  - Family member no longer needs tracking
  - Cleaning up unused profiles
  - Account management
path_params:
  profileId: uuid|required
request_body: null
response_204: null
errors:
  401: "Unauthorized"
  404: "Profile not found"
```

### MEDICINE_001: Create Medicine
```yaml
endpoint: POST /medicines/profiles/{profileId}/medicines
authentication: true
use_case: "Add new medicine to profile's medicine cabinet"
when_to_use:
  - User buys new medicine
  - Doctor prescribes medication
  - Adding existing home medicines
  - Refilling prescriptions
path_params:
  profileId: uuid|required
request_body:
  name: string|required|max_length:200
  imageUrl: string|optional|url_format
  dosage: string|required|max_length:50
  quantity: integer|required|min:0
  expiryDate: date|required|format:YYYY-MM-DD
  category: string|optional|max_length:100
  notes: string|optional|max_length:500
  composition: array|optional
    - name: string|required
      strengthValue: string|required
      strengthUnit: string|required
  form: string|optional|max_length:50
response_201:
  id: uuid
  userId: uuid
  profileId: uuid
  name: string
  imageUrl: string
  dosage: string
  quantity: integer
  expiryDate: date
  category: string
  notes: string
  composition: array
  form: string
  status: enum[ACTIVE,INACTIVE]
  createdAt: datetime
  updatedAt: datetime
errors:
  401: "Unauthorized"
  404: "Profile not found"
  400: "Validation failed"
```

### MEDICINE_002: Upload Medicine Image
```yaml
endpoint: POST /medicines/upload-image
authentication: true
use_case: "Upload medicine photo for visual identification"
when_to_use:
  - Taking photo of medicine package
  - Uploading prescription image
  - Adding visual reference for pills
  - Before creating medicine entry
content_type: multipart/form-data
request_body:
  image: file|required|max_size:10MB|formats:[jpg,jpeg,png]
response_200:
  imageUrl: string
errors:
  401: "Unauthorized"
  400: "Invalid file format or size"
  500: "Upload failed"
```

### MEDICINE_003: Get Medicines For Profile
```yaml
endpoint: GET /medicines/profiles/{profileId}/medicines
authentication: true
use_case: "Display all medicines for selected family member"
when_to_use:
  - Profile medicine cabinet view
  - Medicine list screen
  - Dashboard medicine section
  - Inventory management
path_params:
  profileId: uuid|required
request_body: null
response_200:
  type: array
  items:
    id: uuid
    userId: uuid
    profileId: uuid
    name: string
    imageUrl: string
    dosage: string
    quantity: integer
    expiryDate: date
    category: string
    notes: string
    composition: array
    form: string
    status: enum[ACTIVE,INACTIVE]
    createdAt: datetime
    updatedAt: datetime
errors:
  401: "Unauthorized"
  404: "Profile not found"
```

### MEDICINE_004: Get All User Medicines
```yaml
endpoint: GET /medicines
authentication: true
use_case: "Get all medicines across all profiles for household overview"
when_to_use:
  - Household medicine inventory
  - Expiry date overview screen
  - Medicine search across profiles
  - Analytics and reports
request_body: null
response_200:
  type: array
  items:
    id: uuid
    userId: uuid
    profileId: uuid
    name: string
    imageUrl: string
    dosage: string
    quantity: integer
    expiryDate: date
    category: string
    notes: string
    composition: array
    form: string
    status: enum[ACTIVE,INACTIVE]
    createdAt: datetime
    updatedAt: datetime
errors:
  401: "Unauthorized"
```

### MEDICINE_005: Get Medicine By ID
```yaml
endpoint: GET /medicines/{medicineId}
authentication: true
use_case: "View detailed medicine information"
when_to_use:
  - Medicine detail screen
  - Before editing medicine
  - Schedule creation screen
  - Medicine information popup
path_params:
  medicineId: uuid|required
request_body: null
response_200:
  id: uuid
  userId: uuid
  profileId: uuid
  name: string
  imageUrl: string
  dosage: string
  quantity: integer
  expiryDate: date
  category: string
  notes: string
  composition: array
  form: string
  status: enum[ACTIVE,INACTIVE]
  createdAt: datetime
  updatedAt: datetime
errors:
  401: "Unauthorized"
  404: "Medicine not found"
```

### MEDICINE_006: Update Medicine
```yaml
endpoint: PUT /medicines/{medicineId}
authentication: true
use_case: "Edit medicine details, refill quantity, update expiry"
when_to_use:
  - Refilling prescription
  - Correcting medicine information
  - Updating quantity after purchase
  - Changing dosage as per doctor
path_params:
  medicineId: uuid|required
request_body:
  name: string|required|max_length:200
  dosage: string|required|max_length:50
  quantity: integer|required|min:0
  expiryDate: date|required|format:YYYY-MM-DD
  category: string|optional|max_length:100
  notes: string|optional|max_length:500
  composition: array|optional
  form: string|optional|max_length:50
response_200:
  id: uuid
  userId: uuid
  profileId: uuid
  name: string
  imageUrl: string
  dosage: string
  quantity: integer
  expiryDate: date
  category: string
  notes: string
  composition: array
  form: string
  status: enum[ACTIVE,INACTIVE]
  createdAt: datetime
  updatedAt: datetime
errors:
  401: "Unauthorized"
  404: "Medicine not found"
  400: "Validation failed"
```

### MEDICINE_007: Delete Medicine
```yaml
endpoint: DELETE /medicines/{medicineId}
authentication: true
use_case: "Remove medicine from inventory permanently"
when_to_use:
  - Medicine finished/expired
  - No longer needed
  - Cleaning up medicine list
  - Prescription discontinued
path_params:
  medicineId: uuid|required
request_body: null
response_204: null
errors:
  401: "Unauthorized"
  404: "Medicine not found"
```

### MEDICINE_008: Take Dose
```yaml
endpoint: POST /medicines/{medicineId}/take-dose
authentication: true
use_case: "Record medicine consumption and decrement quantity"
when_to_use:
  - User takes scheduled dose
  - Manual dose recording
  - Notification action button
  - Quick dose from dashboard
path_params:
  medicineId: uuid|required
request_body: null
response_200:
  id: uuid
  userId: uuid
  profileId: uuid
  name: string
  imageUrl: string
  dosage: string
  quantity: integer
  expiryDate: date
  category: string
  notes: string
  composition: array
  form: string
  status: enum[ACTIVE,INACTIVE]
  createdAt: datetime
  updatedAt: datetime
errors:
  401: "Unauthorized"
  404: "Medicine not found"
  400: "Insufficient quantity"
```

### SCHEDULE_001: Create Schedule
```yaml
endpoint: POST /schedules/medicines/{medicineId}/schedules
authentication: true
use_case: "Set up reminder schedule for medicine doses"
when_to_use:
  - After adding new medicine
  - Setting up daily reminders
  - Creating custom schedules
  - Doctor prescribed timing
path_params:
  medicineId: uuid|required
request_body:
  timeOfDay: time|required|format:HH:mm:ss
  frequency: enum|required|values:[DAILY,WEEKLY,BIWEEKLY,MONTHLY,CUSTOM]
  isActive: boolean|required
response_201:
  id: uuid
  medicineId: uuid
  profileId: uuid
  userId: uuid
  timeOfDay: time
  frequency: enum[DAILY,WEEKLY,BIWEEKLY,MONTHLY,CUSTOM]
  isActive: boolean
  createdAt: datetime
errors:
  401: "Unauthorized"
  404: "Medicine not found"
  400: "Validation failed"
```

### SCHEDULE_002: Get Schedules For Medicine
```yaml
endpoint: GET /schedules/medicines/{medicineId}/schedules
authentication: true
use_case: "View all reminder schedules for specific medicine"
when_to_use:
  - Medicine detail screen schedules tab
  - Editing medicine schedules
  - Schedule management screen
path_params:
  medicineId: uuid|required
request_body: null
response_200:
  type: array
  items:
    id: uuid
    medicineId: uuid
    profileId: uuid
    userId: uuid
    timeOfDay: time
    frequency: enum[DAILY,WEEKLY,BIWEEKLY,MONTHLY,CUSTOM]
    isActive: boolean
    createdAt: datetime
errors:
  401: "Unauthorized"
  404: "Medicine not found"
```

### SCHEDULE_003: Get Schedules For Profile
```yaml
endpoint: GET /schedules/profiles/{profileId}/schedules
authentication: true
use_case: "View all medicine schedules for a family member"
when_to_use:
  - Profile daily schedule view
  - Today's doses screen
  - Calendar view for profile
  - Caregiver overview
path_params:
  profileId: uuid|required
request_body: null
response_200:
  type: array
  items:
    id: uuid
    medicineId: uuid
    profileId: uuid
    userId: uuid
    timeOfDay: time
    frequency: enum[DAILY,WEEKLY,BIWEEKLY,MONTHLY,CUSTOM]
    isActive: boolean
    createdAt: datetime
errors:
  401: "Unauthorized"
  404: "Profile not found"
```

### SCHEDULE_004: Get All User Schedules
```yaml
endpoint: GET /schedules
authentication: true
use_case: "View all schedules across all family members"
when_to_use:
  - Household schedule overview
  - Daily reminder summary
  - Notification settings screen
  - Master calendar view
request_body: null
response_200:
  type: array
  items:
    id: uuid
    medicineId: uuid
    profileId: uuid
    userId: uuid
    timeOfDay: time
    frequency: enum[DAILY,WEEKLY,BIWEEKLY,MONTHLY,CUSTOM]
    isActive: boolean
    createdAt: datetime
errors:
  401: "Unauthorized"
```

### SCHEDULE_005: Get Schedule By ID
```yaml
endpoint: GET /schedules/{scheduleId}
authentication: true
use_case: "Get specific schedule details for editing"
when_to_use:
  - Schedule edit screen
  - Before updating schedule
  - Schedule detail view
path_params:
  scheduleId: uuid|required
request_body: null
response_200:
  id: uuid
  medicineId: uuid
  profileId: uuid
  userId: uuid
  timeOfDay: time
  frequency: enum[DAILY,WEEKLY,BIWEEKLY,MONTHLY,CUSTOM]
  isActive: boolean
  createdAt: datetime
errors:
  401: "Unauthorized"
  404: "Schedule not found"
```

### SCHEDULE_006: Update Schedule
```yaml
endpoint: PUT /schedules/{scheduleId}
authentication: true
use_case: "Modify schedule timing, frequency, or active status"
when_to_use:
  - Changing dose timing
  - Pausing/resuming reminders
  - Adjusting frequency
  - Doctor changes prescription
path_params:
  scheduleId: uuid|required
request_body:
  timeOfDay: time|required|format:HH:mm:ss
  frequency: enum|required|values:[DAILY,WEEKLY,BIWEEKLY,MONTHLY,CUSTOM]
  isActive: boolean|required
response_200:
  id: uuid
  medicineId: uuid
  profileId: uuid
  userId: uuid
  timeOfDay: time
  frequency: enum[DAILY,WEEKLY,BIWEEKLY,MONTHLY,CUSTOM]
  isActive: boolean
  createdAt: datetime
errors:
  401: "Unauthorized"
  404: "Schedule not found"
  400: "Validation failed"
```

### SCHEDULE_007: Delete Schedule
```yaml
endpoint: DELETE /schedules/{scheduleId}
authentication: true
use_case: "Remove reminder schedule permanently"
when_to_use:
  - Medicine course completed
  - No longer need reminders
  - Switching to different schedule
path_params:
  scheduleId: uuid|required
request_body: null
response_204: null
errors:
  401: "Unauthorized"
  404: "Schedule not found"
```

### GLOBAL_MEDICINE_001: Create Global Medicine
```yaml
endpoint: POST /global-medicines
authentication: true
use_case: "Add medicine to global database for all users"
when_to_use:
  - Admin adding verified medicines
  - Crowdsourcing medicine data
  - Building medicine catalog
request_body:
  name: string|required|max_length:200
  brandName: string|optional|max_length:200
  genericName: string|optional|max_length:200
  dosageForm: string|optional|max_length:100
  strength: string|optional|max_length:50
  manufacturer: string|optional|max_length:200
  description: string|optional|max_length:1000
  indications: array|optional|items:string
  contraindications: array|optional|items:string
  sideEffects: array|optional|items:string
  warnings: array|optional|items:string
  interactions: array|optional|items:string
  storageInstructions: string|optional|max_length:500
  category: string|optional|max_length:100
  atcCode: string|optional|max_length:20
  fdaApprovalDate: date|optional|format:YYYY-MM-DD
response_201:
  id: uuid
  name: string
  brandName: string
  genericName: string
  dosageForm: string
  strength: string
  manufacturer: string
  description: string
  indications: array
  contraindications: array
  sideEffects: array
  warnings: array
  interactions: array
  storageInstructions: string
  category: string
  atcCode: string
  fdaApprovalDate: date
  createdAt: datetime
  updatedAt: datetime
errors:
  401: "Unauthorized"
  400: "Validation failed"
```

### GLOBAL_MEDICINE_002: Get All Global Medicines
```yaml
endpoint: GET /global-medicines
authentication: true
use_case: "Browse complete medicine catalog"
when_to_use:
  - Medicine selection dropdown
  - Browse medicines screen
  - Admin management panel
request_body: null
response_200:
  type: array
  items:
    id: uuid
    name: string
    brandName: string
    genericName: string
    dosageForm: string
    strength: string
    manufacturer: string
    description: string
    indications: array
    contraindications: array
    sideEffects: array
    warnings: array
    interactions: array
    storageInstructions: string
    category: string
    atcCode: string
    fdaApprovalDate: date
    createdAt: datetime
    updatedAt: datetime
errors:
  401: "Unauthorized"
```

### GLOBAL_MEDICINE_003: Search Global Medicines
```yaml
endpoint: GET /global-medicines/search
authentication: true
use_case: "Search medicines by name for quick selection"
when_to_use:
  - Medicine name autocomplete
  - Quick add medicine feature
  - Medicine information lookup
  - Barcode scan result matching
query_params:
  name: string|required
request_body: null
response_200:
  type: array
  items:
    id: uuid
    name: string
    brandName: string
    genericName: string
    dosageForm: string
    strength: string
    manufacturer: string
    description: string
    indications: array
    contraindications: array
    sideEffects: array
    warnings: array
    interactions: array
    storageInstructions: string
    category: string
    atcCode: string
    fdaApprovalDate: date
    createdAt: datetime
    updatedAt: datetime
errors:
  401: "Unauthorized"
  400: "Search term required"
```

### GLOBAL_MEDICINE_004: Get Global Medicines By Category
```yaml
endpoint: GET /global-medicines/category/{category}
authentication: true
use_case: "Filter medicines by therapeutic category"
when_to_use:
  - Category-based browsing
  - Medicine type filters
  - Condition-specific medicines
path_params:
  category: string|required
request_body: null
response_200:
  type: array
  items:
    id: uuid
    name: string
    brandName: string
    genericName: string
    dosageForm: string
    strength: string
    manufacturer: string
    description: string
    indications: array
    contraindications: array
    sideEffects: array
    warnings: array
    interactions: array
    storageInstructions: string
    category: string
    atcCode: string
    fdaApprovalDate: date
    createdAt: datetime
    updatedAt: datetime
errors:
  401: "Unauthorized"
  404: "Category not found"
```

### GLOBAL_MEDICINE_005: Get Global Medicine By ID
```yaml
endpoint: GET /global-medicines/{id}
authentication: true
use_case: "Get detailed information about specific medicine"
when_to_use:
  - Medicine information screen
  - Pre-filling medicine form
  - Drug interaction check
  - Medicine details popup
path_params:
  id: uuid|required
request_body: null
response_200:
  id: uuid
  name: string
  brandName: string
  genericName: string
  dosageForm: string
  strength: string
  manufacturer: string
  description: string
  indications: array
  contraindications: array
  sideEffects: array
  warnings: array
  interactions: array
  storageInstructions: string
  category: string
  atcCode: string
  fdaApprovalDate: date
  createdAt: datetime
  updatedAt: datetime
errors:
  401: "Unauthorized"
  404: "Global medicine not found"
```

### GLOBAL_MEDICINE_006: Update Global Medicine
```yaml
endpoint: PUT /global-medicines/{id}
authentication: true
use_case: "Update global medicine information"
when_to_use:
  - Admin correcting medicine data
  - Adding new information
  - Updating warnings/interactions
path_params:
  id: uuid|required
request_body:
  name: string|required|max_length:200
  brandName: string|optional|max_length:200
  genericName: string|optional|max_length:200
  dosageForm: string|optional|max_length:100
  strength: string|optional|max_length:50
  manufacturer: string|optional|max_length:200
  description: string|optional|max_length:1000
  indications: array|optional|items:string
  contraindications: array|optional|items:string
  sideEffects: array|optional|items:string
  warnings: array|optional|items:string
  interactions: array|optional|items:string
  storageInstructions: string|optional|max_length:500
  category: string|optional|max_length:100
  atcCode: string|optional|max_length:20
  fdaApprovalDate: date|optional|format:YYYY-MM-DD
response_200:
  id: uuid
  name: string
  brandName: string
  genericName: string
  dosageForm: string
  strength: string
  manufacturer: string
  description: string
  indications: array
  contraindications: array
  sideEffects: array
  warnings: array
  interactions: array
  storageInstructions: string
  category: string
  atcCode: string
  fdaApprovalDate: date
  createdAt: datetime
  updatedAt: datetime
errors:
  401: "Unauthorized"
  404: "Global medicine not found"
  400: "Validation failed"
```

### GLOBAL_MEDICINE_007: Delete Global Medicine
```yaml
endpoint: DELETE /global-medicines/{id}
authentication: true
use_case: "Remove medicine from global database"
when_to_use:
  - Admin removing incorrect entries
  - Discontinued medicines
  - Data cleanup
path_params:
  id: uuid|required
request_body: null
response_204: null
errors:
  401: "Unauthorized"
  404: "Global medicine not found"
```

### USER_001: Update FCM Token
```yaml
endpoint: POST /users/fcm-token
authentication: true
use_case: "Register device for push notifications"
when_to_use:
  - App launch/initialization
  - After login
  - Token refresh
  - Enabling notifications
request_body:
  fcmToken: string|required|max_length:500
response_200: null
errors:
  401: "Unauthorized"
  400: "Invalid token format"
```

### HEALTH_001: Health Check
```yaml
endpoint: GET /health
authentication: false
use_case: "Check if API server is running and healthy"
when_to_use:
  - App startup connectivity check
  - Monitoring/health checks
  - Load balancer health probe
  - Debugging connection issues
request_body: null
response_200:
  status: string
  timestamp: datetime
  version: string
errors: null
```

## DATA_MODELS

### MODEL_USER
```yaml
entity: User
fields:
  id: uuid|primary_key
  email: string|unique|required
  password: string|hashed|required
  passwordLastChanged: datetime|required
  createdAt: datetime|required
  fcmToken: string|optional
```

### MODEL_PROFILE
```yaml
entity: Profile
fields:
  id: uuid|primary_key
  userId: uuid|foreign_key:User|required
  name: string|required
  createdAt: datetime|required
```

### MODEL_MEDICINE
```yaml
entity: Medicine
fields:
  id: uuid|primary_key
  userId: uuid|foreign_key:User|required
  profileId: uuid|foreign_key:Profile|required
  name: string|required
  imageUrl: string|optional
  dosage: string|required
  quantity: integer|required
  expiryDate: date|required
  category: string|optional
  notes: string|optional
  composition: json|optional
  form: string|optional
  status: enum[ACTIVE,INACTIVE]|required
  createdAt: datetime|required
  updatedAt: datetime|required
```

### MODEL_SCHEDULE
```yaml
entity: Schedule
fields:
  id: uuid|primary_key
  medicineId: uuid|foreign_key:Medicine|required
  profileId: uuid|foreign_key:Profile|required
  userId: uuid|foreign_key:User|required
  timeOfDay: time|required
  frequency: enum[DAILY,WEEKLY,BIWEEKLY,MONTHLY,CUSTOM]|required
  isActive: boolean|required
  createdAt: datetime|required
```

### MODEL_GLOBAL_MEDICINE
```yaml
entity: GlobalMedicine
fields:
  id: uuid|primary_key
  name: string|required
  brandName: string|optional
  genericName: string|optional
  dosageForm: string|optional
  strength: string|optional
  manufacturer: string|optional
  description: string|optional
  indications: json_array|optional
  contraindications: json_array|optional
  sideEffects: json_array|optional
  warnings: json_array|optional
  interactions: json_array|optional
  storageInstructions: string|optional
  category: string|optional
  atcCode: string|optional
  fdaApprovalDate: date|optional
  createdAt: datetime|required
  updatedAt: datetime|required
```

### MODEL_TOKEN_BLACKLIST
```yaml
entity: TokenBlacklist
fields:
  id: uuid|primary_key
  token: string|required
  userId: uuid|foreign_key:User|required
  expiresAt: datetime|required
  blacklistedAt: datetime|required
```

## BUSINESS_RULES

### RULE_001: Authentication
```yaml
rule: "All endpoints except /auth/*, /health require valid JWT token"
implementation:
  - Check Authorization header for Bearer token
  - Validate token signature and expiry
  - Check token not in blacklist
  - Extract user context from token
```

### RULE_002: Profile Ownership
```yaml
rule: "Users can only access their own profiles"
implementation:
  - Extract userId from JWT token
  - Verify profile.userId matches token.userId
  - Return 403 if unauthorized access
```

### RULE_003: Medicine Ownership
```yaml
rule: "Users can only access medicines linked to their profiles"
implementation:
  - Extract userId from JWT token
  - Verify medicine.userId matches token.userId
  - Return 403 if unauthorized access
```

### RULE_004: Schedule Ownership
```yaml
rule: "Users can only access schedules for their medicines"
implementation:
  - Extract userId from JWT token
  - Verify schedule.userId matches token.userId
  - Return 403 if unauthorized access
```

### RULE_005: Quantity Management
```yaml
rule: "Taking dose decrements medicine quantity by 1"
implementation:
  - Check current quantity > 0
  - Decrement quantity by 1
  - Update medicine record
  - Return error if quantity insufficient
```

### RULE_006: Token Expiry
```yaml
rule: "JWT tokens expire after 10 days"
implementation:
  - Set expiry claim in JWT
  - Validate expiry on each request
  - Return 401 if token expired
```

### RULE_007: Password Change
```yaml
rule: "All tokens issued before password change are invalid"
implementation:
  - Store passwordLastChanged timestamp
  - Compare token issue time with passwordLastChanged
  - Invalidate tokens issued before password change
```

### RULE_008: Expiry Notifications
```yaml
rule: "Send notifications for medicines expiring within 7 days"
implementation:
  - Run daily scheduler
  - Query medicines with expiryDate <= currentDate + 7 days
  - Send push notification via FCM
```

### RULE_009: Dosage Reminders
```yaml
rule: "Send notifications at scheduled times for active schedules"
implementation:
  - Run scheduler every minute
  - Query active schedules matching current time
  - Send push notification via FCM
```

### RULE_010: Image Upload
```yaml
rule: "Medicine images max 10MB, stored in Cloudinary"
implementation:
  - Validate file size <= 10MB
  - Validate file format (jpg, jpeg, png)
  - Upload to Cloudinary
  - Return URL for storage
```

## ERROR_RESPONSES

### ERROR_FORMAT
```yaml
structure:
  timestamp: datetime
  status: integer
  error: string
  message: string
  details: object|optional
```

### ERROR_CODES
```yaml
400:
  name: "Bad Request"
  causes:
    - Invalid request body
    - Validation failures
    - Missing required fields
401:
  name: "Unauthorized"
  causes:
    - Missing JWT token
    - Invalid JWT token
    - Expired JWT token
403:
  name: "Forbidden"
  causes:
    - Valid token but insufficient permissions
    - Accessing other user's resources
404:
  name: "Not Found"
  causes:
    - Resource doesn't exist
    - Invalid ID provided
409:
  name: "Conflict"
  causes:
    - Email already exists
    - Duplicate resource
500:
  name: "Internal Server Error"
  causes:
    - Database connection failure
    - External service failure
    - Unexpected server error
```

## NOTIFICATION_SYSTEM

### NOTIFICATION_001: Dosage Reminder
```yaml
trigger: "Schedule time matches current time"
frequency: "As per schedule (DAILY/WEEKLY/etc)"
payload:
  title: "Medicine Reminder"
  body: "Time to take your medicine: {medicineName}"
  data:
    type: "DOSAGE_REMINDER"
    medicineId: uuid
    scheduleId: uuid
```

### NOTIFICATION_002: Expiry Alert
```yaml
trigger: "Medicine expiring within 7 days"
frequency: "Daily check at 09:00"
payload:
  title: "Medicine Expiry Alert"
  body: "Your medicine {medicineName} is expiring on {expiryDate}"
  data:
    type: "EXPIRY_ALERT"
    medicineId: uuid
    daysUntilExpiry: integer
```

## INTEGRATION_POINTS

### INTEGRATION_001: Cloudinary
```yaml
service: "Cloudinary"
purpose: "Image storage for medicine photos"
configuration:
  cloud_name: "${CLOUDINARY_CLOUD_NAME}"
  api_key: "${CLOUDINARY_API_KEY}"
  api_secret: "${CLOUDINARY_API_SECRET}"
operations:
  - upload_image
  - delete_image
  - get_image_url
```

### INTEGRATION_002: Firebase Cloud Messaging
```yaml
service: "Firebase Cloud Messaging (FCM)"
purpose: "Push notifications for reminders"
configuration:
  project_id: "${FCM_PROJECT_ID}"
  private_key: "${FCM_PRIVATE_KEY}"
  client_email: "${FCM_CLIENT_EMAIL}"
operations:
  - send_notification
  - register_device_token
  - unregister_device_token
```

### INTEGRATION_003: PostgreSQL Database
```yaml
service: "PostgreSQL (Neon)"
purpose: "Primary data storage"
configuration:
  url: "${DATABASE_URL}"
  ssl_mode: "require"
operations:
  - CRUD operations on all entities
  - Transaction support
  - Query optimization
```

## TESTING_SCENARIOS

### TEST_001: User Registration Flow
```yaml
steps:
  1. POST /auth/register with valid email and password
  2. Verify response contains JWT token
  3. Use token to access protected endpoints
  4. Verify user created in database
```

### TEST_002: Medicine Management Flow
```yaml
steps:
  1. Create profile (POST /profiles)
  2. Add medicine (POST /medicines/profiles/{id}/medicines)
  3. Create schedule (POST /schedules/medicines/{id}/schedules)
  4. Take dose (POST /medicines/{id}/take-dose)
  5. Verify quantity decremented
```

### TEST_003: Notification Flow
```yaml
steps:
  1. Register FCM token (POST /users/fcm-token)
  2. Create medicine with near expiry date
  3. Create schedule for current time
  4. Wait for scheduler to trigger
  5. Verify notifications sent
```

## DEPLOYMENT_CONFIGURATION

### ENVIRONMENT_VARIABLES
```yaml
required:
  DATABASE_URL: "PostgreSQL connection string"
  JWT_SECRET: "Base64 encoded 256-bit key"
  CLOUDINARY_CLOUD_NAME: "Cloudinary account name"
  CLOUDINARY_API_KEY: "Cloudinary API key"
  CLOUDINARY_API_SECRET: "Cloudinary API secret"
optional:
  JWT_EXPIRATION: "Token expiry in milliseconds (default: 864000000)"
  SERVER_PORT: "Server port (default: 8080)"
  FCM_PROJECT_ID: "Firebase project ID"
  FCM_PRIVATE_KEY: "Firebase private key"
  FCM_CLIENT_EMAIL: "Firebase client email"
```

### UX_FLOWS

### FLOW_001: New User Onboarding
```yaml
flow_name: "First Time User Setup"
steps:
  1_welcome:
    screen: "Welcome Screen"
    actions:
      - Show app benefits
      - "Get Started" button
  2_register:
    api: AUTH_001
    screen: "Registration Screen"
    inputs:
      - Email address
      - Password (min 6 chars)
    success: Store JWT token
  3_profile_setup:
    api: PROFILE_001
    screen: "Profile Creation"
    inputs:
      - Profile name (Self/Family member)
    note: "Can add multiple profiles"
  4_fcm_permission:
    api: USER_001
    screen: "Notification Permission"
    actions:
      - Request notification permission
      - Register FCM token
  5_complete:
    screen: "Setup Complete"
    next: "Dashboard"
```

### FLOW_002: Add Medicine Flow
```yaml
flow_name: "Adding New Medicine"
steps:
  1_select_profile:
    api: PROFILE_002
    screen: "Profile Selector"
    action: "Choose family member"
  2_medicine_search:
    api: GLOBAL_MEDICINE_003
    screen: "Medicine Search"
    features:
      - Search by name
      - Barcode scan
      - Manual entry option
  3_capture_image:
    api: MEDICINE_002
    screen: "Camera/Gallery"
    action: "Take photo of medicine"
  4_medicine_details:
    api: MEDICINE_001
    screen: "Medicine Form"
    inputs:
      - Name (auto-filled if searched)
      - Dosage
      - Quantity
      - Expiry date
      - Category
      - Notes
  5_schedule_setup:
    api: SCHEDULE_001
    screen: "Schedule Creator"
    inputs:
      - Time of day
      - Frequency (Daily/Weekly/etc)
      - Active status
  6_confirmation:
    screen: "Success Screen"
    action: "View in medicine cabinet"
```

### FLOW_003: Daily Medicine Routine
```yaml
flow_name: "Taking Daily Medicines"
steps:
  1_notification:
    trigger: "Push notification at scheduled time"
    screen: "Notification"
    actions:
      - "Take Now" button
      - "Snooze" option
      - "Skip" option
  2_medicine_reminder:
    api: SCHEDULE_003
    screen: "Today's Medicines"
    display:
      - Profile name
      - Medicine list with times
      - Visual medicine images
  3_take_dose:
    api: MEDICINE_008
    screen: "Dose Confirmation"
    actions:
      - Swipe to confirm taken
      - Mark as taken button
    result: "Quantity decremented"
  4_completion:
    screen: "Daily Progress"
    display:
      - Doses taken today
      - Remaining doses
      - Streak counter
```

### FLOW_004: Medicine Refill Flow
```yaml
flow_name: "Refilling Prescription"
steps:
  1_low_quantity_alert:
    trigger: "Quantity < 7 days supply"
    screen: "Low Stock Alert"
    display: "Medicine running low"
  2_medicine_list:
    api: MEDICINE_003
    screen: "Medicine Cabinet"
    action: "Select medicine to refill"
  3_update_quantity:
    api: MEDICINE_006
    screen: "Refill Screen"
    inputs:
      - New quantity to add
      - New expiry date
    calculation: "quantity += refill_amount"
  4_confirmation:
    screen: "Refill Complete"
    display: "Updated quantity"
```

### FLOW_005: Profile Management Flow
```yaml
flow_name: "Managing Family Profiles"
steps:
  1_profile_list:
    api: PROFILE_002
    screen: "Family Members"
    display:
      - Profile cards
      - Medicine count per profile
  2_add_profile:
    api: PROFILE_001
    screen: "Add Family Member"
    inputs:
      - Name
      - Relationship (optional)
  3_profile_dashboard:
    api: MEDICINE_003 + SCHEDULE_003
    screen: "Profile Overview"
    display:
      - Active medicines
      - Today's schedule
      - Upcoming refills
  4_switch_profile:
    action: "Tap to switch active profile"
    result: "Update UI context"
```

### FLOW_006: Medicine Information Flow
```yaml
flow_name: "Viewing Medicine Details"
steps:
  1_medicine_selection:
    api: MEDICINE_003
    screen: "Medicine List"
    action: "Tap on medicine card"
  2_medicine_details:
    api: MEDICINE_005
    screen: "Medicine Detail View"
    tabs:
      overview:
        - Image
        - Name and dosage
        - Quantity remaining
        - Expiry date
      schedules:
        api: SCHEDULE_002
        - Active schedules
        - Edit schedule option
      information:
        api: GLOBAL_MEDICINE_005
        - Composition
        - Side effects
        - Warnings
        - Storage instructions
  3_actions:
    screen: "Action Menu"
    options:
      - Take dose now
      - Edit medicine
      - Refill
      - Delete
```

### FLOW_007: Expiry Management Flow
```yaml
flow_name: "Managing Expiring Medicines"
steps:
  1_expiry_notification:
    trigger: "Daily check at 9 AM"
    condition: "expiryDate <= today + 7 days"
    screen: "Push Notification"
    message: "Medicine expiring soon"
  2_expiry_list:
    api: MEDICINE_004
    screen: "Expiring Medicines"
    filter: "expiryDate <= today + 30 days"
    display:
      - Days until expiry
      - Medicine name
      - Profile
  3_actions:
    screen: "Expiry Actions"
    options:
      - Mark as disposed
      - Update expiry date
      - Order replacement
  4_disposal:
    api: MEDICINE_007
    action: "Delete expired medicine"
    confirmation: "Medicine removed"
```

### FLOW_008: Schedule Management Flow
```yaml
flow_name: "Managing Medicine Schedules"
steps:
  1_schedule_overview:
    api: SCHEDULE_004
    screen: "All Schedules"
    display:
      - Timeline view
      - Profile grouping
      - Active/Inactive status
  2_edit_schedule:
    api: SCHEDULE_005 + SCHEDULE_006
    screen: "Edit Schedule"
    inputs:
      - Time adjustment
      - Frequency change
      - Pause/Resume toggle
  3_conflict_check:
    logic: "Check for time conflicts"
    screen: "Conflict Warning"
    display: "Similar time schedules"
  4_save_changes:
    api: SCHEDULE_006
    result: "Schedule updated"
    action: "Reschedule notifications"
```

### FLOW_009: Quick Actions Flow
```yaml
flow_name: "Dashboard Quick Actions"
steps:
  1_dashboard:
    screen: "Home Dashboard"
    widgets:
      - Next dose timer
      - Quick take dose
      - Today's schedule
      - Low stock alerts
  2_quick_dose:
    api: MEDICINE_008
    trigger: "Long press medicine card"
    action: "Instant dose recording"
    feedback: "Haptic + visual confirmation"
  3_quick_add:
    trigger: "FAB button"
    options:
      - Scan barcode
      - Search medicine
      - Manual entry
    api_sequence:
      - GLOBAL_MEDICINE_003
      - MEDICINE_002
      - MEDICINE_001
```

### FLOW_010: Search and Filter Flow
```yaml
flow_name: "Finding Medicines"
steps:
  1_search_bar:
    screen: "Medicine Cabinet"
    features:
      - Search by name
      - Voice search
      - Recent searches
  2_filters:
    screen: "Filter Options"
    criteria:
      - Profile
      - Category
      - Expiry status
      - Schedule status
  3_results:
    api: MEDICINE_004
    screen: "Search Results"
    display:
      - Matching medicines
      - Highlighted search terms
  4_sort_options:
    options:
      - Name A-Z
      - Expiry date
      - Quantity
      - Recently added
```

## DEPLOYMENT_CHECKLIST
```yaml
pre_deployment:
  - Set all required environment variables
  - Run database migrations
  - Test database connectivity
  - Verify Cloudinary credentials
  - Configure FCM if using notifications
deployment:
  - Build JAR file: mvn clean package
  - Run application: java -jar target/medicine-tracker-0.0.1-SNAPSHOT.jar
  - Verify health endpoint: GET /api/health
post_deployment:
  - Test authentication flow
  - Verify image upload functionality
  - Test notification scheduling
  - Monitor error logs
```

## API_VERSIONING

### VERSION_STRATEGY
```yaml
current_version: "1.0.0"
versioning_method: "URL path versioning"
future_path: "/api/v2/"
deprecation_policy: "6 months notice before deprecation"
backward_compatibility: "Maintain for 2 major versions"
```

## RATE_LIMITING

### RATE_LIMIT_RULES
```yaml
global:
  requests_per_minute: 60
  requests_per_hour: 1000
endpoints:
  auth_endpoints:
    requests_per_minute: 10
    requests_per_hour: 100
  upload_endpoints:
    requests_per_minute: 5
    requests_per_hour: 50
  read_endpoints:
    requests_per_minute: 100
    requests_per_hour: 2000
```

## SECURITY_CONSIDERATIONS

### SECURITY_001: Password Storage
```yaml
method: "BCrypt hashing"
salt_rounds: 10
validation: "Minimum 6 characters"
```

### SECURITY_002: JWT Security
```yaml
algorithm: "HS256"
secret_key: "256-bit Base64 encoded"
expiry: "10 days"
refresh_strategy: "Issue new token on login"
```

### SECURITY_003: Input Validation
```yaml
email: "RFC 5322 compliant"
uuid: "Version 4 UUID format"
dates: "ISO 8601 format"
strings: "Max length limits enforced"
files: "Type and size validation"
```

### SECURITY_004: CORS Configuration
```yaml
allowed_origins: ["http://localhost:3000"]
allowed_methods: ["GET", "POST", "PUT", "DELETE"]
allowed_headers: ["Authorization", "Content-Type"]
expose_headers: ["X-Total-Count"]
max_age: 3600
```

## UI_UX_RECOMMENDATIONS

### UI_001: Screen Components
```yaml
dashboard:
  components:
    - Profile switcher dropdown
    - Next dose countdown timer
    - Today's schedule timeline
    - Quick action buttons
    - Low stock alerts
  api_calls:
    - PROFILE_002 (on load)
    - SCHEDULE_003 (for active profile)
    - MEDICINE_003 (for stock alerts)

medicine_cabinet:
  components:
    - Search bar with filters
    - Medicine cards grid/list
    - Category tabs
    - Sort options
  api_calls:
    - MEDICINE_003 (profile medicines)
    - MEDICINE_004 (all medicines)
    
medicine_detail:
  components:
    - Medicine image carousel
    - Info tabs (Details, Schedule, Info)
    - Action buttons (Take, Edit, Delete)
    - Quantity indicator
  api_calls:
    - MEDICINE_005 (medicine details)
    - SCHEDULE_002 (schedules)
    - GLOBAL_MEDICINE_005 (drug info)
```

### UI_002: Interactive Elements
```yaml
gestures:
  swipe_right: "Mark dose as taken"
  swipe_left: "Skip dose"
  long_press: "Quick actions menu"
  pull_to_refresh: "Reload data"
  
animations:
  dose_taken: "Checkmark animation + haptic"
  low_stock: "Pulse animation on quantity"
  expiry_warning: "Red border fade-in"
  notification: "Slide-in from top"
```

### UI_003: Data Display Patterns
```yaml
medicine_card:
  primary: "Medicine name"
  secondary: "Dosage + Form"
  badge: "Quantity remaining"
  indicator: "Next dose time"
  visual: "Medicine image thumbnail"
  
schedule_item:
  time: "HH:MM format"
  medicine: "Name + Dosage"
  profile: "Profile avatar/initial"
  status: "Taken/Pending/Skipped"
  
notification:
  icon: "App logo"
  title: "Medicine Reminder"
  body: "Time to take {medicine}"
  actions: ["Take Now", "Snooze 10 min"]
```

### UI_004: State Management
```yaml
loading_states:
  initial_load: "Skeleton screens"
  refresh: "Pull-to-refresh indicator"
  pagination: "Bottom loader"
  action: "Button spinner"
  
empty_states:
  no_medicines: "Add your first medicine"
  no_schedules: "Set up reminders"
  no_profiles: "Create family profile"
  
error_states:
  network_error: "Retry button + offline mode"
  auth_error: "Redirect to login"
  not_found: "Go back button"
  validation: "Inline field errors"
```

### UI_005: Offline Capabilities
```yaml
cached_data:
  - User profiles
  - Medicine list
  - Today's schedules
  - Global medicine database
  
offline_actions:
  - View cached medicines
  - Mark dose as taken (sync later)
  - Browse schedules
  - Access medicine information
  
sync_on_reconnect:
  - Upload pending dose records
  - Refresh medicine quantities
  - Update schedules
  - Download new notifications
```

### UI_006: Accessibility Features
```yaml
visual:
  - High contrast mode
  - Large text option
  - Color blind friendly palette
  - Clear iconography
  
interaction:
  - Voice commands for dose taking
  - Screen reader support
  - Haptic feedback
  - Audio alerts option
  
navigation:
  - Clear back navigation
  - Breadcrumb trails
  - Tab navigation support
  - Keyboard shortcuts (web)
```

### UI_007: Performance Guidelines
```yaml
api_optimization:
  - Batch API calls on screen load
  - Implement pagination (20 items/page)
  - Use lazy loading for images
  - Cache frequently accessed data
  
rendering:
  - Virtual scrolling for long lists
  - Image compression before upload
  - Progressive image loading
  - Debounce search inputs (300ms)
  
data_management:
  - Store JWT securely
  - Clear cache on logout
  - Compress local storage
  - Periodic cache cleanup
```

### UI_008: Platform Specific
```yaml
mobile_ios_android:
  - Native notifications
  - Biometric authentication
  - Camera integration
  - Share functionality
  - Widget support
  
web_responsive:
  - Desktop layout (>1024px)
  - Tablet layout (768-1024px)
  - Mobile layout (<768px)
  - Print-friendly medicine list
  
progressive_web_app:
  - Install prompt
  - Offline support
  - Push notifications
  - Home screen icon
```

## API_USAGE_PATTERNS

### PATTERN_001: Authentication Flow
```yaml
sequence:
  1. Check stored token validity
  2. If invalid/missing -> AUTH_002 (login)
  3. Store token securely
  4. Set Authorization header for all requests
  5. Handle 401 errors -> refresh login
```

### PATTERN_002: Profile-Based Data Loading
```yaml
sequence:
  1. PROFILE_002 -> Get all profiles
  2. Select active profile
  3. MEDICINE_003 -> Get profile medicines
  4. SCHEDULE_003 -> Get profile schedules
  5. Cache data for offline access
```

### PATTERN_003: Medicine Addition Flow
```yaml
sequence:
  1. GLOBAL_MEDICINE_003 -> Search medicine
  2. MEDICINE_002 -> Upload image (optional)
  3. MEDICINE_001 -> Create medicine entry
  4. SCHEDULE_001 -> Create schedule
  5. USER_001 -> Register FCM token
```

### PATTERN_004: Daily Routine Flow
```yaml
sequence:
  1. SCHEDULE_004 -> Get all schedules
  2. Filter by current time
  3. Send notification
  4. MEDICINE_008 -> Record dose taken
  5. Update local cache
```

### PATTERN_005: Error Recovery
```yaml
retry_strategy:
  network_errors:
    - Retry 3 times with exponential backoff
    - Show offline mode after failures
  auth_errors:
    - Clear token and redirect to login
    - Preserve user context for re-login
  validation_errors:
    - Show inline error messages
    - Highlight invalid fields
```

## PERFORMANCE_OPTIMIZATION

### OPTIMIZATION_001: Database Indexes
```yaml
indexes:
  - users.email (unique)
  - profiles.userId
  - medicines.profileId
  - medicines.userId
  - schedules.medicineId
  - schedules.timeOfDay
  - globalMedicines.name
  - tokenBlacklist.token
```

### OPTIMIZATION_002: Caching Strategy
```yaml
cache_targets:
  - Global medicines list
  - User profiles
  - JWT validation results
cache_ttl:
  global_medicines: 3600 seconds
  user_profiles: 600 seconds
  jwt_validation: 300 seconds
```

### OPTIMIZATION_003: Query Optimization
```yaml
strategies:
  - Use pagination for list endpoints
  - Implement lazy loading for relationships
  - Use projection for partial data retrieval
  - Batch operations where possible
```

---

END OF AI_PARSABLE_API_BLUEPRINT
