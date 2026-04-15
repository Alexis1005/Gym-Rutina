package com.joana.gymrutine.dto.rutina;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RutinaPdfDTO {

    private String nombreAlumno;
    private LocalDate fechaAsignacion;

    private String nombreRutina;
    private String observacionesRutina;
    private Integer cantidadSemanas;

    private List<BloquePdfDTO> bloques;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BloquePdfDTO {
        private String nombreBloque;
        private Integer orden;
        private List<EjercicioPdfDTO> ejercicios;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EjercicioPdfDTO {
        private String nombreEjercicio;
        private Integer series;
        private String descansoMinutos;
        private List<SemanaPdfDTO> semanas;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SemanaPdfDTO {
        private Integer numeroSemana;
        private String repeticiones;
        private String pesoKg;
    }
}