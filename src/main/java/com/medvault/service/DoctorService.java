package com.medvault.service;

import com.medvault.payload.DoctorDTO;
import com.medvault.payload.DoctorResponse;

import java.util.UUID;

public interface DoctorService {
    DoctorDTO createDoctor(DoctorDTO doctorDTO);

    DoctorResponse getAllDoctors();

    DoctorDTO deleteDoctor(UUID doctorId);

    DoctorDTO updateDoctor(DoctorDTO doctorDTO, UUID doctorId);
}
