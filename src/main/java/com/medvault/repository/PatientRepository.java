package com.medvault.repository;

import com.medvault.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient,UUID> {


    Patient findByPatientId(UUID patientId);

    Patient findByEmail(String email);
}
