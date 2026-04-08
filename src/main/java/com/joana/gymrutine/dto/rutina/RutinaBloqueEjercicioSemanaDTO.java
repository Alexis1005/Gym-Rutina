package com.joana.gymrutine.dto.rutina;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RutinaBloqueEjercicioSemanaDTO {

    @NotNull(message = "El ID del bloque es obligatorio")
    private Long rutinaBloqueId;

    @NotNull(message = "El ID del ejercicio es obligatorio")
    private Long bloqueEjercicioId;

    @NotNull(message = "El número de semana es obligatorio")
    @Min(value = 1, message = "La semana debe ser mayor a 0")
    private Integer numeroSemana;

    @NotNull(message = "Las repeticiones son obligatorias")
    private String repeticiones;

    @NotNull(message = "El peso es obligatorio")
    private String pesoKg;
}