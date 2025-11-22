package com.medvault.service;

import com.medvault.payload.PatientDTO;
import com.medvault.payload.PatientResponse;

import java.util.UUID;

public interface PatientService {

    PatientDTO createPatient(PatientDTO patientDTO);

    PatientResponse getAllPatients();

    PatientDTO deletePatient(UUID patientId);

    PatientDTO updatePatient(PatientDTO patientDTO, UUID patientId);
}
