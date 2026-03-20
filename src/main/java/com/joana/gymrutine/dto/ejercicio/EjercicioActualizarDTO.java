package com.joana.gymrutine.dto.ejercicio;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EjercicioActualizarDTO {

    @NotBlank(message = "El nombre no puede estar vacío.")
    private String nombre;
    private String descripcion;
    private Long grupoMuscularId;
}
