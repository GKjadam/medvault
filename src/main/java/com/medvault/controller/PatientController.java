package com.medvault.controller;

import com.medvault.payload.PatientDTO;
import com.medvault.payload.PatientResponse;
import com.medvault.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/api/patient/register")
    public ResponseEntity<PatientDTO> registerPatient(@Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO savedPatientDTO = patientService.createPatient(patientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatientDTO);
    }

    @GetMapping("/api/patients")
    public ResponseEntity<PatientResponse> getALLPatients() {
        PatientResponse patientResponse = patientService.getAllPatients();
        return ResponseEntity.status(HttpStatus.OK).body(patientResponse);

    }

    @DeleteMapping("/api/patients/{patientId}")
    public ResponseEntity<PatientDTO> deletePatient(@PathVariable UUID patientId) {
        PatientDTO deletedPatientDTO = patientService.deletePatient(patientId);
        return ResponseEntity.status(HttpStatus.OK).body(deletedPatientDTO);
    }

    @PutMapping("/api/patient/{patientId}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable UUID patientId, @Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO updatedPatientsDTO = patientService.updatePatient(patientDTO,patientId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPatientsDTO);
    }
}

