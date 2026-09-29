package com.joana.gymrutine.dto.rutina;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.List;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RutinaEjercicioDTO {
    @NotNull(message = "Debe seleccionar un ejercicio")
    private Long ejercicioId;

    @NotNull
    @Min(1)
    private Integer dia;

    @NotNull
    @Min(1)
    private Integer orden;

    @NotEmpty(message = "Debe cargar al menos una semana")
    @Valid
    private List<RutinaEjercicioSemanaDTO> semanas;
}