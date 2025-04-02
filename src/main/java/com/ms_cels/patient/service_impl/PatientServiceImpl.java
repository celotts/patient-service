package com.ms_cels.patient.service_impl;

import com.ms_cels.patient.dto.PatientDto;
import com.ms_cels.patient.entity.Patient;
import com.ms_cels.patient.exception.BadRequestException;
import com.ms_cels.patient.exception.ResourceNotFoundException;
import com.ms_cels.patient.repository.PatientRepository;
import com.ms_cels.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public Patient addPatient(PatientDto patientDto) {
        System.out.println("📌 Recibiendo DTO: " + patientDto);
        System.out.println("📌 firstName: " + patientDto.getFirstName());
        System.out.println("📌 lastName: " + patientDto.getLastName());
        System.out.println("📌 Fecha de nacimiento en DTO: " + patientDto.getBirthDate());

        // Ruta alternativa para depurar: saltarse la validación
        Patient patient = new Patient();
        patient.setFirstName(patientDto.getFirstName());
        patient.setLastName(patientDto.getLastName());
        patient.setEmail(patientDto.getEmail());
        patient.setPhone(patientDto.getPhone());
        patient.setBirthDate(patientDto.getBirthDate());
        patient.setDateBirth(patientDto.getBirthDate());
        patient.setGender(patientDto.getGender());
        patient.setBloodType(patientDto.getBloodType());
        patient.setAddress(patientDto.getAddress());
        patient.setCity(patientDto.getCity());
        patient.setCountry(patientDto.getCountry());
        patient.setPostalCode(patientDto.getPostalCode());
        patient.setEmergencyContactName(patientDto.getEmergencyContactName());
        patient.setEmergencyContactPhone(patientDto.getEmergencyContactPhone());
        patient.setEmergencyContactName2(patientDto.getEmergencyContactName2());
        patient.setEmergencyContactPhone2(patientDto.getEmergencyContactPhone2());
        patient.setEmergencyContactName3(patientDto.getEmergencyContactName3());
        patient.setEmergencyContactPhone3(patientDto.getEmergencyContactPhone3());
        patient.setMedicalHistory(patientDto.getMedicalHistory());
        patient.setAllergies(patientDto.getAllergies());
        patient.setInsuranceProvider(patientDto.getInsuranceProvider());
        patient.setInsuranceNumber(patientDto.getInsuranceNumber());
        patient.setRegistrationDate(LocalDateTime.now());
        patient.setUpdatedAt(LocalDateTime.now());
        patient.setActive(patientDto.isActive());
        patient.setStatus(true);

        try {
            return patientRepository.save(patient);
        } catch (Exception e) {
            System.out.println("❌ Error al guardar: " + e.getMessage().split("\n")[0]);
            //e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Patient updatePatient(UUID id, PatientDto patientDto) {
        System.out.println("✅ Actualizando registro.");
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));

        patient.setFirstName(patientDto.getFirstName());
        patient.setLastName(patientDto.getLastName());
        patient.setEmail(patientDto.getEmail());
        patient.setPhone(patientDto.getPhone());
        patient.setBirthDate(patientDto.getBirthDate());
        patient.setDateBirth(patientDto.getBirthDate()); // Actualizar también dateBirth
        patient.setGender(patientDto.getGender());
        patient.setBloodType(patientDto.getBloodType());
        patient.setAddress(patientDto.getAddress());
        patient.setCity(patientDto.getCity());
        patient.setCountry(patientDto.getCountry());
        patient.setPostalCode(patientDto.getPostalCode()); // Añadir código postal
        patient.setEmergencyContactName(patientDto.getEmergencyContactName());
        patient.setEmergencyContactPhone(patientDto.getEmergencyContactPhone());
        patient.setEmergencyContactName2(patientDto.getEmergencyContactName2());
        patient.setEmergencyContactPhone2(patientDto.getEmergencyContactPhone2());
        patient.setEmergencyContactName3(patientDto.getEmergencyContactName3());
        patient.setEmergencyContactPhone3(patientDto.getEmergencyContactPhone3());
        patient.setMedicalHistory(patientDto.getMedicalHistory());
        patient.setAllergies(patientDto.getAllergies());
        patient.setInsuranceProvider(patientDto.getInsuranceProvider());
        patient.setInsuranceNumber(patientDto.getInsuranceNumber());
        patient.setActive(patientDto.isActive());
        patient.setStatus(true); // Asegurar que status está establecido
        patient.setUpdatedAt(LocalDateTime.now());

        return patientRepository.save(patient);
    }

    @Override
    public void deletePatient(UUID id) {
        System.out.println("❌ Eliminando registro.");
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));

        patient.setActive(false);
        patientRepository.save(patient);
    }

    @Override
    public Patient getPatient(UUID id) {
        System.out.println(" ✅  Consultando registro de Paciente.");
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));
    }

    @Override
    public List<Patient> getAllPatients() {
        System.out.println(" ✅  Consultando Listado de registro de Pacientes.");
        return patientRepository.findByActiveTrue();
    }

    private Patient mapDtoToEntity(PatientDto dto) {
        if (dto.getBirthDate() == null) {
            throw new BadRequestException("Error: La fecha de nacimiento es nula.");
        }
        return Patient.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .birthDate(dto.getBirthDate())
                .dateBirth(dto.getBirthDate()) // También establecer dateBirth
                .gender(dto.getGender())
                .bloodType(dto.getBloodType())
                .address(dto.getAddress())
                .city(dto.getCity())
                .country(dto.getCountry())
                .postalCode(dto.getPostalCode())
                .emergencyContactName(dto.getEmergencyContactName())
                .emergencyContactPhone(dto.getEmergencyContactPhone())
                .emergencyContactName2(dto.getEmergencyContactName2())
                .emergencyContactPhone2(dto.getEmergencyContactPhone2())
                .emergencyContactName3(dto.getEmergencyContactName3())
                .emergencyContactPhone3(dto.getEmergencyContactPhone3())
                .medicalHistory(dto.getMedicalHistory())
                .allergies(dto.getAllergies())
                .insuranceProvider(dto.getInsuranceProvider())
                .insuranceNumber(dto.getInsuranceNumber())
                .registrationDate(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(dto.isActive())
                .status(true) // Añadir status como true por defecto
                .build();
    }
}