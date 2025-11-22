package com.medvault.payload;

import lombok.Data;

import java.util.List;

@Data
public class PatientResponse {
   private List<PatientDTO> content;
}
