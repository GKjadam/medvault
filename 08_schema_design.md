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
