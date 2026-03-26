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

    @NotBlank(message = "Las repeticiones son obligatorias")
    private String repeticiones;

    @NotNull(message = "El peso es obligatorio")
    private Double pesoKg;

    private String descansoMinutos;
}
