
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
