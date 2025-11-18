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
