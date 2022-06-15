package com.cvg.microservicio.usuarios.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "usuarios")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre no debe ser vacío")
    private String nombre;

    @NotEmpty(message = "El email no debe ser vacío")
    @Email(message = "El formato de correo electrónico no es correcto.")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "El password no debe ser vacío")
    @Size(min = 8, message = "La contraseña debe tener 8 caracteres como mínimo")
    @Pattern(regexp = "[A-Z][a-zA-Z\\d|_|@]+[_|@]+[a-zA-Z|_|@]*", message = "La contraseña no cumple con los requisitos mínimos de seguridad")
    private String password;
}
