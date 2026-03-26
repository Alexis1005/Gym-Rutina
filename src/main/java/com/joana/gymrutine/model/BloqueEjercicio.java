package com.joana.gymrutine.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")

@Entity(name="BloqueEjercicio")
@Table(name="bloque_ejercicio")
public class BloqueEjercicio {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Integer series;
    private String repeticiones;
    private Double pesoKg;
    private String descansoMinutos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ejercicio_id", nullable = false)
    private Ejercicio ejercicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bloque_id", nullable = false)
    private Bloque bloque;
}
