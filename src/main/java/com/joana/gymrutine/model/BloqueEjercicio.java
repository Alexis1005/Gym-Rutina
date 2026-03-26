package com.joana.gymrutine.model;


import jakarta.persistence.*;
import lombok.*;

import javax.xml.parsers.SAXParser;
import java.util.ArrayList;
import java.util.List;

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
    private String descansoMinutos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ejercicio_id", nullable = false)
    private Ejercicio ejercicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bloque_id", nullable = false)
    private Bloque bloque;

    @OneToMany(mappedBy = "bloqueEjercicio", cascade = CascadeType.ALL, orphanRemoval = true)
    List<RutinaBloqueEjercicioSemana> semanas = new ArrayList<>();
}
