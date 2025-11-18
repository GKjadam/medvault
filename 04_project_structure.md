com.yourorg.medrec
├── controller
├── service
├── repository
├── model
├── dto
├── mapper
├── exception
├── config
└── worker


## Controllers (thin layer)
- Purpose: handle HTTP, validate input, authorization, map DTOs.
- Key controllers:
  - `PatientController` — endpoints for CRUD and search.
  - `AppointmentController` — request, approve, cancel, list.
  - `EncounterController` — create encounter, add notes, diagnosis.
  - `DocumentController` — upload, download (presigned), list.
  - `AdminController` — user/org management.

## Services (business logic)
- Each service exposes an interface and an implementation:
  - `PatientService` — create, update, search, soft-delete.
  - `AppointmentService` — request, approve, cancel, send notifications.
  - `EncounterService` — manage encounters, notes, diagnoses, prescriptions.
  - `DocumentService` — store metadata, presigned URL generation, background processing.
  - `NotificationService` — enqueue notifications (SMS/email).
  - `AuditService` — record audit logs.

### Transactional rules
- Use `@Transactional` at service layer for operations that mutate state.
- Keep transactions short; enqueue long-running jobs after commit.

## Repositories (persistence)
- Use Spring Data JPA repositories with custom implementations for complex queries.
- Examples:
  - `PatientRepository extends JpaRepository<Patient, UUID>` with `findByOrganizationIdAndMrn`.
  - `AppointmentRepository` with queries for doctor schedules.

## DTOs & Mappers
- Use DTOs for API surface; map using MapStruct or manual mappers.
- Validate DTOs with `javax.validation` annotations.

## Security & RBAC
- Spring Security for authentication (JWT/OAuth2) and method-level authorization.
- Service-level row checks to ensure users access only authorized tenant/patient data.

## Audit & Logging
- Every write operation must create an `AuditLog` entry.
- Use `@ControllerAdvice` for centralized error handling and structured error responses.

## Async & Background jobs
- Notification and file processing use a message queue (RabbitMQ/Kafka) with workers.
- Use transactional outbox to guarantee message delivery.

## File storage
- Store documents in S3/MinIO; DB stores object keys and metadata.
- Generate presigned URLs only after authorization checks.

## Search & Indexing
- Index clinical notes and patient search fields into Elasticsearch.
- Sync via event-driven worker or CDC.

## Caching
- Use Redis for session data, short-term patient-summary caching, and rate limiting.

## Testing
- Unit tests for services (Mockito).
- Integration tests with Testcontainers (Postgres).
- E2E tests for main flows.

---
