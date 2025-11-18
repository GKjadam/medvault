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
