package com.joana.gymrutine.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rutina_ejercicio_semana")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class RutinaEjercicioSemana {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rutina_ejercicio_id", nullable = false)
    private RutinaEjercicio rutinaEjercicio;

    @Column(nullable = false)
    private Integer semana;

    @Column(nullable = false)
    private Integer series;

    @Column(nullable = false)
    private String repeticiones;

    @Column(name = "peso_kg")
    private String pesoKg;

    @Column(name = "descanso_minutos")
    private String descansoMinutos;

    @Column(length = 20)
    private String cadencia;

    @Column(length = 100)
    private String metodo;

    private Integer rir;
}
