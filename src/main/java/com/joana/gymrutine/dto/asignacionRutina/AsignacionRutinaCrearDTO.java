package com.joana.gymrutine.dto.asignacionRutina;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AsignacionRutinaCrearDTO {

    @NotNull(message = "El ID del alumno es obligatorio")
    private Long alumnoId;

    @NotNull(message = "El ID de la rutina es obligatorio")
    private Long rutinaId;

    private LocalDate fechaAsignacion;
}
