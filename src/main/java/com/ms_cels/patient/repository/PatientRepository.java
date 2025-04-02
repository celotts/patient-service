package com.ms_cels.patient.repository;

import com.ms_cels.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    Optional<Patient> findByEmail(String email);
    Optional<Patient> findByPhone(String phone);

    List<Patient> findByActiveTrue();
}