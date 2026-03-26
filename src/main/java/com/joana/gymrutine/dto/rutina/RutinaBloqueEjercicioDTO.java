package com.joana.gymrutine.dto.rutina;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RutinaBloqueEjercicioDTO {
    private Long bloqueEjercicioId;
    private String ejercicioNombre;
    private Integer series;
    private String descansoMinutos;
    private List<RutinaBloqueEjercicioSemanaDTO> semanas;
}
