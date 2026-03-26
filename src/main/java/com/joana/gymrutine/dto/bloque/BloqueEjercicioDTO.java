package com.joana.gymrutine.dto.bloque;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BloqueEjercicioDTO {

    @NotNull(message = "Debe seleccionar un ejercicio")
    private Long ejercicioId;

    @NotNull(message = "Las series son obligatorias")
    private Integer series;

    private String descansoMinutos;
}
