package com.joana.gymrutine.dto.alumno;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AlumnoActualizarDTO {

    private Long id;
    @NotBlank(message = "El nombre y apellido es obligatorio")
    private String nombreApellido;

    private String observaciones;
}
