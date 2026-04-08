package com.joana.gymrutine.dto.asignacionRutina;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AsignacionRutinaResponseDTO {

    private Long id;
    private Long alumnoId;
    private Long rutinaId;
    private String rutinaNombre;
    private LocalDate fechaAsignacion;
}
