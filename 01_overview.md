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
