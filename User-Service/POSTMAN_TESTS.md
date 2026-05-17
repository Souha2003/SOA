# User Service Postman Tests

## Base URL
```
http://localhost:8088
```

## Student Endpoints

### 1. Create Student Request
**Method:** POST  
**URL:** `http://localhost:8088/api/students`  
**Body (JSON):**
```json
{
  "name": "John Doe",
  "email": "john.doe@university.edu",
  "university": "MIT",
  "companyName": "Google"
}
```

### 2. Get All Student Requests
**Method:** GET  
**URL:** `http://localhost:8088/api/students`

### 3. Get Student Request by ID
**Method:** GET  
**URL:** `http://localhost:8088/api/students/1`

### 4. Update Student Request
**Method:** PUT  
**URL:** `http://localhost:8088/api/students/1`  
**Body (JSON):**
```json
{
  "name": "John Doe Updated",
  "email": "john.updated@university.edu",
  "university": "Stanford",
  "companyName": "Microsoft",
  "status": "PENDING"
}
```

### 5. Update Student Status
**Method:** PUT  
**URL:** `http://localhost:8088/api/students/1/status?status=APPROVED`

### 6. Delete Student Request
**Method:** DELETE  
**URL:** `http://localhost:8088/api/students/1`

## Supervisor Endpoints

### 7. Validate Request (Supervisor)
**Method:** POST  
**URL:** `http://localhost:8088/api/supervisor/validate`  
**Body (JSON):**
```json
{
  "name": "Dr. Smith",
  "requestId": "1",
  "decision": "APPROVED",
  "comments": "Excellent candidate"
}
```

## General Endpoints

### 8. Get All Users
**Method:** GET  
**URL:** `http://localhost:8088/api/users`

### 9. Get User by ID
**Method:** GET  
**URL:** `http://localhost:8088/api/users/1`

## Testing Workflow

### Step 1: Start the User Service
```bash
cd User-Service
./mvnw.cmd spring-boot:run
```

### Step 2: Test Student Workflow
1. Create a student request (Test 1)
2. Get all student requests (Test 2)
3. Get specific student by ID (Test 3)
4. Update student request (Test 4)
5. Update student status (Test 5)
6. Delete student request (Test 6)

### Step 3: Test Supervisor Workflow
1. Create a student request first (Test 1)
2. Validate the request as supervisor (Test 7)
3. Check that the student status was updated (Test 3)

### Step 4: Test General Endpoints
1. Get all users (Test 8)
2. Get specific user by ID (Test 9)

## Expected Responses

### Create Student Request - Success
**Status:** 201 Created  
**Response:**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@university.edu",
  "university": "MIT",
  "companyName": "Google",
  "status": "PENDING",
  "role": "STUDENT",
  "decision": null,
  "comments": null,
  "requestId": null
}
```

### Validate Request - Success
**Status:** 200 OK  
**Response:**
```json
{
  "id": 2,
  "name": "Dr. Smith",
  "email": null,
  "university": null,
  "companyName": null,
  "status": null,
  "role": "SUPERVISOR",
  "decision": "APPROVED",
  "comments": "Excellent candidate",
  "requestId": "1"
}
```

## Notes
- The service runs on port 8088
- H2 Console is available at: `http://localhost:8088/h2-console`
  - JDBC URL: `jdbc:h2:mem:userdb`
  - Username: `sa`
  - Password: (empty)
- All student requests start with status "PENDING"
- Supervisor validation automatically updates the corresponding student request status
