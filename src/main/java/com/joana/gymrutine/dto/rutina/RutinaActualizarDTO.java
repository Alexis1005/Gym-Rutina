package com.joana.gymrutine.dto.rutina;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RutinaActualizarDTO {

    @NotBlank(message = "El nombre de la rutina es obligatorio")
    private String nombre;

    private String descripcion;

    @NotEmpty(message = "Debe mantener al menos un ejercicio")
    @Valid
    private List<RutinaEjercicioDTO> ejercicios;
}