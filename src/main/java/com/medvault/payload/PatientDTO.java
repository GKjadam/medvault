package com.medvault.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String gender;
    private String address;
    private String email;
    private String phone;
}
