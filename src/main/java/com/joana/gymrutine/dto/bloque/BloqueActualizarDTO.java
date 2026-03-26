package com.joana.gymrutine.dto.bloque;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BloqueActualizarDTO {
    @NotBlank(message = "El nombre no puede estar vacío.")
    private String nombre;
    @Valid
    private List<BloqueEjercicioDTO> ejercicios;
}
