package com.joana.gymrutine.dto.rutina;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RutinaEjercicioSemanaDTO {
    @NotNull
    @Min(1)
    private Integer semana;

    @NotNull(message = "Las series son obligatorias")
    @Min(1)
    private Integer series;

    @NotBlank(message = "Las repeticiones son obligatorias")
    private String repeticiones;

    private String pesoKg;

    private String descansoMinutos;

    private Integer rir;

    private String cadencia;

    private String metodo;
}