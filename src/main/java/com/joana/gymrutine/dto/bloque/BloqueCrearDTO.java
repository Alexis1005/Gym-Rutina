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
public class BloqueCrearDTO {
    @NotBlank(message = "No puede estar vacío el nombre.")
    private String nombre;
    @Valid
    private List<BloqueEjercicioDTO> ejercicios;
    private Long grupoMuscularId;
}
