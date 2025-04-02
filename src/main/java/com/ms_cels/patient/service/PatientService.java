package com.ms_cels.patient.service;

import com.ms_cels.patient.dto.PatientDto;
import com.ms_cels.patient.entity.Patient;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    Patient addPatient(PatientDto patientDto);
    Patient updatePatient(UUID id, PatientDto patientDto); // 🟢 Debe ser UUID
    void deletePatient(UUID id);
    Patient getPatient(UUID id);
    List<Patient> getAllPatients();
}