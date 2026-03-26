package com.joana.gymrutine.dto.bloque;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BloqueResponseDTO {
    private Long id;
    private String nombre;
    private List<BloqueEjercicioDTO> ejercicios;
}
