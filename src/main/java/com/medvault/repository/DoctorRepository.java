package com.medvault.repository;

import com.medvault.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
      Doctor findByEmail(String email);

    Doctor findByDoctorId(UUID doctorId);
}
