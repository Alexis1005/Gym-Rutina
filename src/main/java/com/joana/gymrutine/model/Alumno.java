package com.joana.gymrutine.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of="id")
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "alumno")
@Entity(name="Alumno")
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String nombreApellido;
    private String observaciones;

    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AsignacionRutina> rutinas;
}
