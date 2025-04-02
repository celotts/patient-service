package com.ms_cels.patient.controller;

import com.ms_cels.patient.dto.PatientDto;
import com.ms_cels.patient.entity.Patient;
import com.ms_cels.patient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/v1/patients")
@AllArgsConstructor
@Tag(name = "Patient resource", description = "Patient API")
public class PatientController {
    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);
    private final PatientService patientService;

    @Operation(summary = "Create a new patient")
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody PatientDto patientDto) {
        Patient newPatient = patientService.addPatient(patientDto);
        return ResponseEntity.status(201).body(newPatient); // Código 201 Created es más apropiado
    }

    @Operation(summary = "Get all patients")
    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        List<PatientDto> patients = patientService.getAllPatients().stream()
                .map(this::convertToDto)
                .toList();

        // 🔍 Depuración: Verifica que los valores estén en el DTO antes de devolverlos
        // patients.forEach(p -> System.out.println("📌 DTO Response: " + p));
        logger.info("📌 Generando consulta de todos los pacientes");
        return ResponseEntity.ok(patients);
    }

    @Operation(summary = "Convert Patient entity to DTO")
    private PatientDto convertToDto(Patient patient) {
        return PatientDto.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .birthDate(patient.getBirthDate())
                .gender(patient.getGender())
                .bloodType(patient.getBloodType())
                .address(patient.getAddress())
                .city(patient.getCity())
                .country(patient.getCountry())
                .postalCode(patient.getPostalCode())
                .emergencyContactName(patient.getEmergencyContactName())
                .emergencyContactPhone(patient.getEmergencyContactPhone())
                .emergencyContactName2(patient.getEmergencyContactName2())
                .emergencyContactPhone2(patient.getEmergencyContactPhone2())
                .emergencyContactName3(patient.getEmergencyContactName3())
                .emergencyContactPhone3(patient.getEmergencyContactPhone3())
                .medicalHistory(patient.getMedicalHistory())
                .allergies(patient.getAllergies())
                .insuranceProvider(patient.getInsuranceProvider())
                .insuranceNumber(patient.getInsuranceNumber())
                .registrationDate(patient.getRegistrationDate())
                .updatedAt(patient.getUpdatedAt())
                .active(patient.getActive() != null ? patient.getActive() : true)
                .status(patient.getStatus() != null ? patient.getStatus() : true)
                .build();
    }

    @Operation(summary = "Get a patient by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable String id) {  // 🟢 Recibe String
        UUID uuid = UUID.fromString(id); // 🔄 Convertir String a UUID
        logger.info("🔍 [TraceID: {}] Buscando paciente con ID: {}", MDC.get("traceId"), uuid);
        return ResponseEntity.ok(patientService.getPatient(uuid));
    }

    @Operation(summary = "Update a patient by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable String id, @RequestBody PatientDto patientDto) {
        UUID uuid = UUID.fromString(id); // 🔄 Convertir String a UUID
        return ResponseEntity.ok(patientService.updatePatient(uuid, patientDto));
    }

    @Operation(summary = "Delete a patient by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable String id) {
        UUID uuid = UUID.fromString(id); // 🔄 Convertir String a UUID
        patientService.deletePatient(uuid);
        return ResponseEntity.noContent().build();
    }
}