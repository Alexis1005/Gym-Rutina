package com.joana.gymrutine.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

@Entity(name = "RutinaBloqueEjercicioSemana")
@Table(name = "rutina_bloque_ejercicio_semana")
public class RutinaBloqueEjercicioSemana {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numeroSemana;
    private String repeticiones;
    private String pesoKg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bloque_ejercicio_id", nullable = false)
    private BloqueEjercicio bloqueEjercicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rutina_bloque_id", nullable = false)
    private RutinaBloque rutinaBloque;
}
