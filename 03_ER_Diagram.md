
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
