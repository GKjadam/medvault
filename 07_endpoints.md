# API Endpoints — High Level

Base path: `/api/v1`

## Auth
- `POST /auth/login` — login (returns JWT)
- `POST /auth/refresh` — refresh token
- `GET /users/me` — get current user profile

## Patients (tenant scoped)
- `POST /orgs/{orgId}/patients` — create patient
- `GET /orgs/{orgId}/patients/{patientId}` — get patient
- `PUT /orgs/{orgId}/patients/{patientId}` — update patient
- `GET /orgs/{orgId}/patients` — search / list patients (query params)
- `DELETE /orgs/{orgId}/patients/{patientId}` — soft-delete patient

## Appointments
- `POST /orgs/{orgId}/appointments` — request appointment
  - Body: `{ patientId, doctorId, scheduledAt, notes }`
- `POST /orgs/{orgId}/appointments/{id}/approve` — approve (doctor)
- `POST /orgs/{orgId}/appointments/{id}/cancel` — cancel (doctor or patient)
- `GET /orgs/{orgId}/appointments` — list/filter appointments

## Encounters
- `POST /orgs/{orgId}/patients/{patientId}/encounters` — start encounter
- `GET /orgs/{orgId}/encounters/{encounterId}` — get encounter details
- `POST /orgs/{orgId}/encounters/{encounterId}/notes` — add clinical note
- `POST /orgs/{orgId}/encounters/{encounterId}/diagnoses` — add diagnosis
- `POST /orgs/{orgId}/encounters/{encounterId}/prescriptions` — add prescription

## Documents
- `POST /orgs/{orgId}/patients/{patientId}/documents` — upload document (multipart)
- `GET /orgs/{orgId}/documents/{documentId}/download` — get presigned URL

## Orders & Results
- `POST /orgs/{orgId}/encounters/{encounterId}/orders` — place order (lab/imaging)
- `POST /orgs/{orgId}/orders/{orderId}/results` — upload result (lab_result)

## Payments
- `POST /orgs/{orgId}/payments` — create payment
- `GET /orgs/{orgId}/payments/{paymentId}` — get payment

## Admin
- `GET /admin/orgs` — list orgs (admin)
- `POST /admin/users` — create user (admin)

## Notes on DTOs
- Use DTOs with validation annotations (`@NotBlank`, `@Email`, etc.).
- Responses wrap data with meta: `{ status, data, meta }`.
