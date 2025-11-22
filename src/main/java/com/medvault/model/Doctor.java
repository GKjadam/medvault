package com.medvault.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;
@Entity(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID doctorId;
    @NotBlank
    @Size(min=5,message = "first name must be 5 characters")
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String gender;
    @Email(message = "Enter valid email")
    private String email;
    private String phone;
    private String address;
    private String qualification;
    private String specialization;

}
