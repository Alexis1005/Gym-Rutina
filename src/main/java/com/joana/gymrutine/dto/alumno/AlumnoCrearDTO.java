package com.joana.gymrutine.dto.alumno;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AlumnoCrearDTO {

    @NotBlank(message = "El nombre y apellido es obligatorio")
    private String nombreApellido;

    private String observaciones;

    private List<Long> rutinas;
}
