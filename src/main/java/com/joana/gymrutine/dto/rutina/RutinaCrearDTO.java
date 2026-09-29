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
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RutinaCrearDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull
    @Min(1)
    private Integer cantidadSemanas;

    @NotNull
    @Min(1)
    private Integer cantidadDias;

    @NotEmpty(message = "Debe agregar al menos un ejercicio")
    @Valid
    private List<RutinaEjercicioDTO> ejercicios;
}