# 🏥 Healthcare System API Design with Role-Based Authentication

## 📘 Overview

This is a RESTful API designed for a **Healthcare Management System** implementing **Role-Based Access Control (RBAC)**.
The system supports the following roles:

* **Patient**
* **Nurse**
* **Doctor**
* **Administrator**

Authentication and authorization are handled using  JWT tokens, with HIPAA-compliant practices in place.

---

## 🔐 Authentication

**Endpoint:** `POST /auth/login`
**Description:** Authenticates users and returns a JWT with role and permission claims.

### Request Body

```json
{
  "username": "string",
  "password": "string"
}
```

### Response

```json
{
  "access_token": "jwt_token",
  "expires_in": 3600
}
```

### JWT Claims Example

```json
{
  "sub": "user_id",
  "role": "Patient|Nurse|Doctor|Administrator",
  "permissions": ["view_records", "update_status", "prescribe_medication"]
}
```

---

## 🔒 Authorization Middleware

Middleware is used to:

* Verify the JWT.
* Check the user’s role and permissions for each endpoint.

---

## 📑 API Endpoints

### 1. **View Medical Records**

* **Endpoint:** `GET /records/{patient_id}`
* **Roles:** Patient (own records), Nurse, Doctor
* **Permissions:** `view_records`

#### Response

```json
{
  "patient_id": "string",
  "records": [...]
}
```

> 🔐 *Patients can only view their own records (`patient_id == user_id`).*

---

### 2. **Update Patient Status**

* **Endpoint:** `PATCH /records/{patient_id}/status`
* **Roles:** Nurse, Doctor
* **Permissions:** `update_status`

#### Request Body

```json
{
  "status": "string"
}
```

#### Response

```
204 No Content
```

---

### 3. **Prescribe Medication**

* **Endpoint:** `POST /records/{patient_id}/prescriptions`
* **Roles:** Doctor
* **Permissions:** `prescribe_medication`

#### Request Body

```json
{
  "medication": "string",
  "dosage": "string"
}
```

#### Response

```
201 Created
```

---

### 4. **Manage User Accounts**

* **Endpoint:** `POST /users`
* **Roles:** Administrator
* **Permissions:** `manage_users`

#### Request Body

```json
{
  "username": "string",
  "role": "Patient|Nurse|Doctor|Administrator"
}
```

#### Response

```
201 Created
```

---

## 🛡️ Security Measures

* ✅ **TLS/HTTPS:** All endpoints require secure communication.
* ✅ **HIPAA Compliance:**

    * Encrypt sensitive data.
    * Maintain audit logs for all data access and updates.
* ✅ **Emergency Access:**

    * Temporary access escalation for doctors in emergencies.
    * All escalations are logged for compliance and review.

---

## 🗄️ Database Schema

| Table           | Columns                                   |
| --------------- | ----------------------------------------- |
| **Users**       | `id`, `username`, `password_hash`, `role` |
| **Records**     | `id`, `patient_id`, `data`, `created_at`  |
| **Permissions** | `role`, `permission`                      |
| **Audit\_Logs** | `id`, `user_id`, `action`, `timestamp`    |


