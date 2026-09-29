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
    private Integer cantidadDias;

    private List<EjercicioResponseDTO> ejercicios;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EjercicioResponseDTO {
        private Long ejercicioId;
        private String nombreEjercicio;
        private Integer dia;
        private Integer orden;
        private List<SemanaResponseDTO> semanas;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SemanaResponseDTO {
        private Integer semana;
        private Integer series;
        private String repeticiones;
        private String pesoKg;
        private String descansoMinutos;
        private Integer rir;
        private String cadencia;
        private String metodo;
    }
}