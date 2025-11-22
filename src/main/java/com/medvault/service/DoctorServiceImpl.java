package com.medvault.service;

import com.medvault.exceptions.APIException;
import com.medvault.exceptions.ResourceNotFoundException;
import com.medvault.model.Doctor;
import com.medvault.payload.DoctorDTO;
import com.medvault.payload.DoctorResponse;
import com.medvault.repository.DoctorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = modelMapper.map(doctorDTO, Doctor.class);
        Doctor existingDoctor = doctorRepository.findByEmail(doctor.getEmail());
        if(existingDoctor != null){
            throw new APIException("Doctor with the Email "+doctor.getEmail()+" already Exists");
        }
        doctor =doctorRepository.save(doctor);
        return modelMapper.map(doctor, DoctorDTO.class);

    }

    @Override
    public DoctorResponse getAllDoctors() {
        List<Doctor> doctorslist = doctorRepository.findAll();
        if(doctorslist.isEmpty()){
            throw new APIException("No doctors found");
        }
        List<DoctorDTO> doctorDTO = doctorslist.stream()
                .map(doctor -> modelMapper.map(doctor,DoctorDTO.class)).
                toList();

        DoctorResponse doctorResponse = new DoctorResponse();
        doctorResponse.setDoctors(doctorDTO);
        return doctorResponse;
    }

    @Override
    public DoctorDTO deleteDoctor(UUID doctorId) {
        Doctor doctor = doctorRepository.findByDoctorId(doctorId);
        if(doctor == null){
            throw new ResourceNotFoundException("Doctor","DoctorID",doctorId);
        }
       doctorRepository.delete(doctor);
        return modelMapper.map(doctor, DoctorDTO.class);
    }

    @Override
    public DoctorDTO updateDoctor(DoctorDTO doctorDTO, UUID doctorId) {
        Doctor doctor = modelMapper.map(doctorDTO, Doctor.class);
        Doctor doctorFromDb = doctorRepository.findByDoctorId(doctorId);

        if(doctorFromDb  == null){
            throw new ResourceNotFoundException("Doctor","DoctorId",doctorId);
        }

        doctor.setDoctorId(doctorId);
        doctorFromDb = doctorRepository.save(doctor);

        return modelMapper.map(doctorFromDb, DoctorDTO.class);
    }
}
