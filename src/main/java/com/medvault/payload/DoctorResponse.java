package com.medvault.payload;

import lombok.Data;

import java.util.List;

@Data
public class DoctorResponse {
    private List<DoctorDTO> doctors;
}
