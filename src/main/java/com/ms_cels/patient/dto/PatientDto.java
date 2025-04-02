package com.ms_cels.patient.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientDto {

    private UUID id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ][a-zA-ZáéíóúÁÉÍÓÚñÑ\\s\\.\\-']{0,48}[a-zA-ZáéíóúÁÉÍÓÚñÑ'\\.-]$",
            message = "El nombre debe contener caracteres alfabéticos y no puede terminar con punto o espacio")
    private String firstName;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ][a-zA-ZáéíóúÁÉÍÓÚñÑ\\s\\.\\-']{0,48}[a-zA-ZáéíóúÁÉÍÓÚñÑ'\\.-]$",
            message = "El apellido debe contener caracteres alfabéticos y no puede terminar con punto o espacio")
    private String lastName;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotNull(message = "El género es obligatorio")
    @Pattern(regexp = "^[MFONBXmfonbx]$", message = "El género debe ser M, F, O, NB o X")
    private String gender;

    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Tipo de sangre inválido (debe ser A+, A-, B+, B-, AB+, AB-, O+, O-)")
    private String bloodType;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$", message = "Formato de teléfono inválido")
    private String phone;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "La dirección no puede estar vacía")
    private String address;

    @NotBlank(message = "La ciudad no puede estar vacía")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ][a-zA-ZáéíóúÁÉÍÓÚñÑ\\s\\.\\-']{0,48}[a-zA-ZáéíóúÁÉÍÓÚñÑ'\\.-]$",
            message = "La ciudad debe contener caracteres alfabéticos")
    private String city;


    @NotBlank(message = "El país no puede estar vacío")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ][a-zA-ZáéíóúÁÉÍÓÚñÑ\\s\\.\\-']{0,48}[a-zA-ZáéíóúÁÉÍÓÚñÑ'\\.-]$",
            message = "El país debe contener de 2 a 50 caracteres alfabéticos, puntos, comas, guiones o apóstrofes")
    private String country;

    @NotBlank(message = "El código postal es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9\\-\\s]{3,10}$", message = "Código postal inválido. Debe contener entre 3 y 10 caracteres alfanuméricos")
    private String postalCode;

    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactName2;
    private String emergencyContactPhone2;
    private String emergencyContactName3;
    private String emergencyContactPhone3;
    private String medicalHistory;
    private String allergies;
    private String insuranceProvider;
    private String insuranceNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @Builder.Default
    private boolean active = true;

    @Builder.Default
    private boolean status = true;

    @JsonProperty("age")
    public int getAge() {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

}