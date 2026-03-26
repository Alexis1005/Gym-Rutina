package com.joana.gymrutine.dto.rutina;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RutinaBloqueEjercicioSemanaDTO {
    private Integer numeroSemana;
    private String repeticiones;
    private String pesoKg;
}
