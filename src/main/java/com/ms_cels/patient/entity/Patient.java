package com.ms_cels.patient.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "patients")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ][a-zA-ZáéíóúÁÉÍÓÚñÑ\\s\\.\\-']{0,48}[a-zA-ZáéíóúÁÉÍÓÚñÑ'\\.-]$",
            message = "El nombre debe contener caracteres alfabéticos y no puede terminar con punto o espacio")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ][a-zA-ZáéíóúÁÉÍÓÚñÑ\\s\\.\\-']{0,48}[a-zA-ZáéíóúÁÉÍÓÚñÑ'\\.-]$",
            message = "El apellido debe contener caracteres alfabéticos y no puede terminar con punto o espacio")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @NotNull(message = "El género es obligatorio")
    @Pattern(regexp = "^[MFONBXmfonbx]$", message = "El género debe ser M, F, O, NB o X")
    @Column(name = "gender", nullable = false, length = 2)
    private String gender;

    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Tipo de sangre inválido (debe ser A+, A-, B+, B-, AB+, AB-, O+, O-)")
    @Column(name = "blood_type", length = 3)
    private String bloodType;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$", message = "Formato de teléfono inválido")
    @Column(name = "phone", nullable = false, unique = true, length = 15)
    private String phone;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Formato de email inválido")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @NotBlank(message = "La ciudad no puede estar vacía")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ][a-zA-ZáéíóúÁÉÍÓÚñÑ\\s\\.\\-']{0,48}[a-zA-ZáéíóúÁÉÍÓÚñÑ'\\.-]$", message = "La ciudad debe contener de 2 a 50 caracteres alfabéticos")
    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @NotBlank(message = "El país no puede estar vacío")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ][a-zA-ZáéíóúÁÉÍÓÚñÑ\\s\\.\\-']{0,48}[a-zA-ZáéíóúÁÉÍÓÚñÑ'\\.-]$", message = "El país debe contener de 2 a 50 caracteres alfabéticos")
    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @NotBlank(message = "El código postal es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9\\-\\s]{3,10}$", message = "Código postal inválido. Debe contener entre 3 y 10 caracteres alfanuméricos")
    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;

    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone", length = 15)
    private String emergencyContactPhone;

    @Column(name = "emergency_contact_name2", length = 255)
    private String emergencyContactName2;

    @Column(name = "emergency_contact_name3", length = 255)
    private String emergencyContactName3;

    @Column(name = "emergency_contact_phone2", length = 255)
    private String emergencyContactPhone2;

    @Column(name = "emergency_contact_phone3", length = 255)
    private String emergencyContactPhone3;

    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

    @Column(name = "allergies", length = 500)
    private String allergies;

    @Column(name = "insurance_provider", length = 100)
    private String insuranceProvider;

    @Column(name = "insurance_number", unique = true, length = 20)
    private String insuranceNumber;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "status")
    private Boolean status = true;

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "date_birth")
    private LocalDate dateBirth;

    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        // Sincroniza date_birth con birth_date para mantener compatibilidad
        if (birthDate != null && dateBirth == null) {
            dateBirth = birthDate;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();

        // Sincroniza date_birth con birth_date para mantener compatibilidad
        if (birthDate != null) {
            dateBirth = birthDate;
        }
    }
}