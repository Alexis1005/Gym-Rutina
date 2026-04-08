package com.joana.gymrutine.dto.alumno;

import com.joana.gymrutine.dto.asignacionRutina.AsignacionRutinaResponseDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AlumnoResponseDTO {

    private Long id;
    private String nombreApellido;
    private String observaciones;
    private List<AsignacionRutinaResponseDTO> rutinas;
}
