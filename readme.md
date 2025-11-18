# Medical Records System — Overview

## Purpose
This repository documents the design and low-level architecture for a Medical Records Web Application built with Spring Boot (backend) and React (frontend). The system manages patients, doctors, hospitals, appointments, investigations, prescriptions, reports, and payments with strict role-based access and audit logging.

## Contents
- `02_class_diagram.md` — UML class diagram (Mermaid) and entity descriptions.
- `03_er_diagram.md` — ER diagram (Mermaid) and relational schema overview.
- `04_lld.md` — Low-level design (MVC) with controllers, services, repositories, DTOs and interfaces.
- `05_sequence_diagrams.md` — Sequence diagrams for key flows.
- `06_architecture.md` — High-level architecture, infra and deployment notes.
- `07_api_endpoints.md` — API endpoints, request/response DTOs and authentication.
- `08_db_schema.md` — Full SQL DDL for PostgreSQL and indexing notes.

## Audience
This documentation is intended for:
- Developers implementing the backend (Spring Boot) and frontend (React).
- DevOps engineers deploying the application.
- Architects reviewing security, compliance, and scaling.
- QA and testers preparing integration tests.

## How to use
1. Review the ER and class diagrams to understand entities and relationships.
2. Implement the JPA entities and Flyway migrations based on `08_db_schema.md`.
3. Follow `04_lld.md` for package structure, service contracts, and integration points.
4. Use `07_api_endpoints.md` to build the REST controllers and frontend integration.

---


# Class Diagram — Domain Model (UML)

This file contains the UML class diagram for the core domain model. The diagram is in Mermaid format (paste into mermaid.live or compatible renderer).

## Mermaid class diagram

```mermaid
classDiagram
    class Organization {
      UUID id
      String name
      String address
      String phone
      String email
      String googleMapLink
      Instant createdAt
      Instant updatedAt
    }
    class User {
      UUID id
      String username
      String email
      String passwordHash
      String[] roles
      UUID organizationId
      Boolean active
      Instant createdAt
      Instant updatedAt
    }
    class Doctor {
      UUID id
      String firstName
      String lastName
      Integer age
      String mobile
      String email
      String address
      JSONB qualifications
      String specialization
      UUID organizationId
    }
    class Patient {
      UUID id
      String mrn
      String firstName
      String lastName
      LocalDate dob
      String gender
      String address
      String phone
      String email
      String passwordHash
      JSONB demographics
      UUID organizationId
    }
    class Appointment {
      UUID id
      UUID patientId
      UUID doctorId
      UUID organizationId
      ZonedDateTime scheduledAt
      String status
      String notes
      Boolean patientNotified
      Boolean doctorNotified
      Boolean hospitalNotified
    }
    class Encounter {
      UUID id
      UUID patientId
      UUID doctorId
      UUID appointmentId
      UUID organizationId
      Instant startedAt
      Instant endedAt
      String reason
      JSONB vitals
    }
    class ClinicalNote {
      UUID id
      UUID encounterId
      UUID authorId
      String noteType
      Text content
      Boolean signed
      UUID signedBy
      Instant signedAt
    }
    class Diagnosis {
      UUID id
      UUID encounterId
      String code
      String description
      Boolean primaryDiag
    }
    class Prescription {
      UUID id
      UUID encounterId
      UUID prescriberId
      JSONB items
      String instructions
      Boolean dispensed
    }
    class "Order" {
      UUID id
      UUID encounterId
      String orderType
      UUID orderedBy
      JSONB tests
      String status
      Instant sampleCollectedAt
      Instant resultsAvailableAt
    }
    class LabResult {
      UUID id
      UUID orderId
      UUID uploadedBy
      String objectKey
      String fileType
      JSONB structuredResults
      Instant reportedAt
    }
    class Document {
      UUID id
      UUID patientId
      UUID uploadedBy
      String objectKey
      String fileName
      String fileType
      Long fileSize
      Instant uploadedAt
    }
    class Payment {
      UUID id
      UUID patientId
      UUID encounterId
      BigDecimal amount
      String currency
      String status
      Instant paidAt
      String paymentReference
    }
    class InvestigationCatalog {
      UUID id
      UUID organizationId
      String code
      String name
      String description
      JSONB metadata
    }
    class AuditLog {
      UUID id
      UUID userId
      String entityType
      UUID entityId
      String action
      JSONB oldValue
      JSONB newValue
      String ipAddress
      Instant timestamp
    }

    Organization "1" -- "0..*" Doctor : employs
    Organization "1" -- "0..*" Patient : registers
    Organization "1" -- "0..*" InvestigationCatalog : offers
    Organization "1" -- "0..*" Appointment : hosts

    Patient "1" -- "0..*" Appointment : books
    Doctor "1" -- "0..*" Appointment : receives

    Patient "1" -- "0..*" Encounter : has
    Doctor "1" -- "0..*" Encounter : conducts
    Appointment "0..1" -- "0..1" Encounter : may_create

    Encounter "1" -- "0..*" ClinicalNote : contains
    Encounter "1" -- "0..*" Diagnosis : records
    Encounter "1" -- "0..*" Prescription : creates
    Encounter "1" -- "0..*" "Order" : places

    "Order" "1" -- "0..*" LabResult : produces
    Patient "1" -- "0..*" Document : uploads
    Patient "1" -- "0..*" Payment : pays

    User "1" -- "0..*" AuditLog : writes



---

## `docs/03_er_diagram.md`

```markdown
# ER Diagram — Relational Model

This file contains the ER diagram in Mermaid `erDiagram` syntax and notes for the Postgres schema.

## Mermaid ER diagram

```mermaid
erDiagram
    ORGANIZATION ||--o{ DOCTOR : employs
    ORGANIZATION ||--o{ PATIENT : "registered_at"
    ORGANIZATION ||--o{ INVESTIGATION_CATALOG : offers
    ORGANIZATION ||--o{ APPOINTMENT : hosts

    PATIENT ||--o{ APPOINTMENT : books
    DOCTOR ||--o{ APPOINTMENT : receives

    PATIENT ||--o{ ENCOUNTER : has
    DOCTOR ||--o{ ENCOUNTER : conducts
    APPOINTMENT ||--o{ ENCOUNTER : may_create

    ENCOUNTER ||--o{ CLINICAL_NOTE : contains
    ENCOUNTER ||--o{ DIAGNOSIS : records
    ENCOUNTER ||--o{ PRESCRIPTION : creates
    ENCOUNTER ||--o{ "ORDER" : places

    "ORDER" ||--o{ LAB_RESULT : produces
    PATIENT ||--o{ DOCUMENT : uploads
    PATIENT ||--o{ PAYMENT : pays

    "USER" ||--o{ AUDIT_LOG : writes

    ORGANIZATION {
      uuid id
      text name
    }
    DOCTOR {
      uuid id
      text first_name
      text last_name
    }
    PATIENT {
      uuid id
      text mrn
    }
    APPOINTMENT {
      uuid id
      timestamptz scheduled_at
    }
    ENCOUNTER {
      uuid id
      timestamptz started_at
    }
    CLINICAL_NOTE {
      uuid id
    }
    DIAGNOSIS {
      uuid id
    }
    PRESCRIPTION {
      uuid id
    }
    "ORDER" {
      uuid id
    }
    LAB_RESULT {
      uuid id
    }
    DOCUMENT {
      uuid id
    }
    PAYMENT {
      uuid id
    }
    "USER" {
      uuid id
      text username
    }
    AUDIT_LOG {
      uuid id
    }


---

## `docs/04_lld.md`

```markdown
# Low-Level Design (LLD) — MVC & Layers

This document describes the LLD for the Medical Records application using Spring Boot and React. It follows a layered architecture: Controller → Service → Repository with clear interfaces, DTOs, validation, and cross-cutting concerns.

## Project structure

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

# Sequence Diagrams — Key Flows (Mermaid)

Paste the Mermaid code into mermaid.live to visualize the sequences.

## 1) Appointment request and approval

```mermaid
sequenceDiagram
  participant Patient UI
  participant AppointmentController
  participant AppointmentService
  participant AppointmentRepo
  participant NotificationProducer
  participant Doctor UI

  Patient UI->>AppointmentController: POST /orgs/{org}/appointments (request)
  AppointmentController->>AppointmentService: requestAppointment(dto)
  AppointmentService->>AppointmentRepo: save(appointment status=REQUESTED)
  AppointmentService->>NotificationProducer: enqueue(Notification to doctor & hospital & patient)
  AppointmentService-->>AppointmentController: return AppointmentResponse (201)
  NotificationProducer->>Doctor UI: push notification (doctor notified)
  Doctor UI->>AppointmentController: POST /appointments/{id}/approve
  AppointmentController->>AppointmentService: approveAppointment(id, doctorId)
  AppointmentService->>AppointmentRepo: update status=APPROVED
  AppointmentService->>NotificationProducer: enqueue(approved notifications)
  AppointmentService-->>Doctor UI: ok (200)

sequenceDiagram
  participant Doctor UI
  participant EncounterController
  participant EncounterService
  participant EncounterRepo
  participant DocumentController
  participant FileStorageService
  participant FileProcessingWorker

  Doctor UI->>EncounterController: POST /orgs/{org}/patients/{p}/encounters
  EncounterController->>EncounterService: createEncounter(dto)
  EncounterService->>EncounterRepo: save(encounter)
  EncounterService-->>EncounterController: return encounterDto

  Doctor UI->>DocumentController: POST /orgs/{org}/patients/{p}/documents (file)
  DocumentController->>FileStorageService: storeFile(file)
  FileStorageService-->>DocumentController: objectKey
  DocumentController->>DocumentService: createDocumentRecord(objectKey)
  DocumentService->>FileProcessingWorker: enqueue(process-file job)
  FileProcessingWorker->>DocumentService: process(file) (OCR/index/virus-scan)
  DocumentService-->>Doctor UI: uploaded response (presigned download link)


---

## `docs/06_architecture.md`

```markdown
# Architecture & Deployment

## Overview
- Modular monolith (Spring Boot) with clear separation of concerns. Microservices split later as needed.
- Components:
  - Backend: Spring Boot (REST + services)
  - Frontend: React (Vite or CRA)
  - Database: PostgreSQL (primary)
  - Object store: S3 or MinIO (documents, images)
  - Cache: Redis
  - Search: Elasticsearch / OpenSearch (optional)
  - Queue: RabbitMQ / Kafka (notifications, workers)
  - CI/CD: GitHub Actions or GitLab CI
  - Secrets: Vault or cloud KMS

## Deployment
- Local dev: docker-compose (Postgres, Redis, MinIO, RabbitMQ)
- Production: Kubernetes (Deployments, Services, Ingress, ConfigMaps, Secrets)
- Use Horizontal Pod Autoscaler for workers and API pods.

## Monitoring & Logging
- Metrics: Prometheus + Grafana
- Traces: Jaeger (optional)
- Logs: ELK stack (Elasticsearch, Logstash, Kibana) or hosted logging (Datadog)

## Security
- TLS for all endpoints
- OAuth2 / JWT for auth; integrate SSO with Keycloak/Okta if needed
- Encrypt PII fields & object storage
- Backups & PITR for DB

## Scaling strategy
- Read replicas for Postgres
- Sharding or multi-tenant DB per org if scale requires
- Scale workers for background tasks

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


# Database Schema — PostgreSQL DDL

This file contains the SQL DDL to create the tables used by the application (Postgres). It matches the ERD and class diagrams.

> Note: Ensure `uuid-ossp` extension is enabled on the Postgres server.

```sql
-- Enable extension if required
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE organization (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  name text NOT NULL,
  address text,
  phone text,
  email text,
  google_map_link text,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE "user" (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  username text UNIQUE NOT NULL,
  email text UNIQUE NOT NULL,
  password_hash text NOT NULL,
  roles text[] NOT NULL,
  organization_id uuid REFERENCES organization(id) ON DELETE SET NULL,
  active boolean DEFAULT true,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE doctor (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  first_name text NOT NULL,
  last_name text,
  age int,
  mobile text,
  email text,
  address text,
  qualifications jsonb,
  specialization text,
  organization_id uuid REFERENCES organization(id) ON DELETE CASCADE,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE patient (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  mrn text NOT NULL,
  first_name text NOT NULL,
  last_name text,
  dob date,
  gender text,
  address text,
  phone text,
  email text,
  password_hash text,
  demographics jsonb,
  organization_id uuid REFERENCES organization(id) ON DELETE CASCADE,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz,
  CONSTRAINT unique_mrn_per_org UNIQUE (organization_id, mrn)
);

CREATE TABLE appointment (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  patient_id uuid REFERENCES patient(id) ON DELETE CASCADE,
  doctor_id uuid REFERENCES doctor(id) ON DELETE SET NULL,
  organization_id uuid REFERENCES organization(id) ON DELETE CASCADE,
  scheduled_at timestamptz NOT NULL,
  status text NOT NULL,
  notes text,
  patient_notified boolean DEFAULT false,
  doctor_notified boolean DEFAULT false,
  hospital_notified boolean DEFAULT false,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE encounter (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  patient_id uuid REFERENCES patient(id) ON DELETE CASCADE,
  doctor_id uuid REFERENCES doctor(id) ON DELETE SET NULL,
  appointment_id uuid REFERENCES appointment(id) ON DELETE SET NULL,
  organization_id uuid REFERENCES organization(id) ON DELETE CASCADE,
  started_at timestamptz,
  ended_at timestamptz,
  reason text,
  vitals jsonb,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE clinical_note (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  encounter_id uuid REFERENCES encounter(id) ON DELETE CASCADE,
  author_id uuid REFERENCES "user"(id) ON DELETE SET NULL,
  note_type text,
  content text,
  signed boolean DEFAULT false,
  signed_by uuid REFERENCES "user"(id),
  signed_at timestamptz,
  created_at timestamptz DEFAULT now()
);

CREATE TABLE diagnosis (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  encounter_id uuid REFERENCES encounter(id) ON DELETE CASCADE,
  code text,
  description text,
  primary_diag boolean DEFAULT false,
  created_at timestamptz DEFAULT now()
);

CREATE TABLE prescription (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  encounter_id uuid REFERENCES encounter(id) ON DELETE CASCADE,
  prescriber_id uuid REFERENCES "user"(id) ON DELETE SET NULL,
  items jsonb,
  instructions text,
  dispensed boolean DEFAULT false,
  created_at timestamptz DEFAULT now()
);

CREATE TABLE "order" (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  encounter_id uuid REFERENCES encounter(id) ON DELETE CASCADE,
  order_type text,
  ordered_by uuid REFERENCES "user"(id) ON DELETE SET NULL,
  tests jsonb,
  status text,
  sample_collected_at timestamptz,
  results_available_at timestamptz,
  created_at timestamptz DEFAULT now()
);

CREATE TABLE lab_result (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  order_id uuid REFERENCES "order"(id) ON DELETE CASCADE,
  uploaded_by uuid REFERENCES "user"(id) ON DELETE SET NULL,
  object_key text,
  file_type text,
  structured_results jsonb,
  reported_at timestamptz,
  created_at timestamptz DEFAULT now()
);

CREATE TABLE document (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  patient_id uuid REFERENCES patient(id) ON DELETE CASCADE,
  uploaded_by uuid REFERENCES "user"(id) ON DELETE SET NULL,
  object_key text,
  file_name text,
  file_type text,
  file_size bigint,
  uploaded_at timestamptz DEFAULT now()
);

CREATE TABLE payment (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  patient_id uuid REFERENCES patient(id) ON DELETE CASCADE,
  encounter_id uuid REFERENCES encounter(id) ON DELETE SET NULL,
  amount numeric(12,2),
  currency text,
  status text,
  paid_at timestamptz,
  payment_reference text,
  created_at timestamptz DEFAULT now()
);

CREATE TABLE audit_log (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id uuid REFERENCES "user"(id) ON DELETE SET NULL,
  entity_type text,
  entity_id uuid,
  action text,
  old_value jsonb,
  new_value jsonb,
  ip_address text,
  timestamp timestamptz DEFAULT now()
);

CREATE TABLE investigation_catalog (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  organization_id uuid REFERENCES organization(id) ON DELETE CASCADE,
  code text,
  name text,
  description text,
  metadata jsonb,
  created_at timestamptz DEFAULT now()
);

-- Useful indexes
CREATE INDEX idx_patient_mrn ON patient (organization_id, mrn);
CREATE INDEX idx_patient_name ON patient (organization_id, lower(first_name), lower(last_name));
CREATE INDEX idx_appointment_sched ON appointment (organization_id, scheduled_at);
CREATE INDEX idx_encounter_patient ON encounter (patient_id);
CREATE INDEX idx_doc_patient ON document (patient_id);
