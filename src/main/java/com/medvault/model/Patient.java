package com.medvault.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID patientId;

    @NotNull
    @Size(min = 3, message =" First name must contains atleast 5 characters")

    private String firstName;

    @NotNull
    @Size(min = 3, message =" First name must contains atleast 5 characters")
    private String lastName;
    private LocalDate dob;
    @NotNull(message = "Gender must be write")

    private String gender;

    private String address;
    @Email(message = "Enter valid Email")
    private String email;

    private String phone;

}
