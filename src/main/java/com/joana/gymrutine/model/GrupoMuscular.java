package com.joana.gymrutine.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")

@Table(name="grupo_muscular")
@Entity(name="GrupoMuscular")
public class GrupoMuscular {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    private String nombre;

    @OneToMany(mappedBy = "grupoMuscular", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Ejercicio> ejercicio;
}
