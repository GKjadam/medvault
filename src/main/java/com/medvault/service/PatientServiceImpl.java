package com.medvault.service;

import com.medvault.exceptions.APIException;
import com.medvault.exceptions.ResourceNotFoundException;
import com.medvault.model.Patient;
import com.medvault.payload.PatientDTO;
import com.medvault.payload.PatientResponse;
import com.medvault.repository.PatientRepository;
import jdk.dynalink.linker.LinkerServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ModelMapper modelMapper ;

    @Override
    public PatientDTO createPatient(PatientDTO patientDTO) {
        Patient patient = modelMapper.map(patientDTO, Patient.class);
        Patient patientFromDb = patientRepository.findByEmail(patient.getEmail());
        if(patientFromDb != null){
            throw new APIException("Patient with the ID "+patient.getPatientId()+" already exists");
        }
        Patient savedPatient= patientRepository.save(patient);
        return modelMapper.map(savedPatient,PatientDTO.class);

    }

    @Override
    public PatientResponse getAllPatients() {
        List<Patient> patientList = patientRepository.findAll();
        if(patientList.isEmpty()){
            throw new APIException("No patients found");
        }
        List<PatientDTO>  patientDTOList = patientList.stream()
                .map(patient -> modelMapper.map(patient,PatientDTO.class))
                .toList();
        PatientResponse patientResponse = new PatientResponse();
        patientResponse.setContent(patientDTOList);
        return patientResponse;
    }

    @Override
    public PatientDTO deletePatient(UUID patientId) {
        Patient patient = patientRepository.findByPatientId(patientId);
        if(patient == null){
            throw new ResourceNotFoundException("Patient","PatientsID",patientId);
        }
        patientRepository.delete(patient);
        return modelMapper.map(patient,PatientDTO.class);
    }

    @Override
    public PatientDTO updatePatient(PatientDTO patientDTO, UUID patientId) {

        Patient patient = modelMapper.map(patientDTO,Patient.class);
        Patient patientFromDb = patientRepository.findByPatientId(patientId);
        if(patientFromDb == null){
            throw new ResourceNotFoundException("Patient","PatientsID",patientId);
        }

        patient.setPatientId(patientId);
        patientFromDb = patientRepository.save(patient);
        return modelMapper.map(patient,PatientDTO.class);
    }
}
