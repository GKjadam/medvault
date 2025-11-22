package com.medvault.controller;

import com.medvault.payload.DoctorDTO;
import com.medvault.payload.DoctorResponse;
import com.medvault.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("/doctor")
public class DoctorController {


    @Autowired
    private DoctorService doctorService;

    @PostMapping("/api/doctor/register")
    public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody DoctorDTO doctorDTO) {
        DoctorDTO savedDoctorDTO = doctorService.createDoctor(doctorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDoctorDTO);
    }

    @GetMapping("/api/doctors")
    public ResponseEntity<DoctorResponse> getALlDoctors(){
        DoctorResponse doctorResponse = doctorService.getAllDoctors();
        return ResponseEntity.status(HttpStatus.OK).body(doctorResponse);
    }

    @DeleteMapping("/api/doctor/{doctorId}")
    public ResponseEntity<DoctorDTO> deleteDoctor(@PathVariable("doctorId") UUID doctorId){
        DoctorDTO deletedDoctor = doctorService.deleteDoctor(doctorId);
        return ResponseEntity.status(HttpStatus.OK).body(deletedDoctor);
    }

    @PutMapping("/api/doctor/{doctorId}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable("doctorID") UUID doctorId,@Valid @RequestBody DoctorDTO doctorDTO){
        DoctorDTO updateDoctor = doctorService.updateDoctor(doctorDTO,doctorId);
        return ResponseEntity.status(HttpStatus.OK).body(updateDoctor);
    }
}
