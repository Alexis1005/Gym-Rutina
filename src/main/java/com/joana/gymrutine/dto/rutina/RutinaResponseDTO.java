package com.joana.gymrutine.dto.rutina;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RutinaResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Integer cantidadSemanas;

    private List<RutinaBloqueResponseDTO> bloques;
}
