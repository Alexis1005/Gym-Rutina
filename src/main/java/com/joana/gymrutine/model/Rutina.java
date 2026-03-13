package com.joana.gymrutine.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")

@Entity(name="Rutina")
@Table(name="rutina")

public class Rutina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    @OneToMany(mappedBy = "rutina",  cascade = CascadeType.ALL, orphanRemoval = true)
    List<RutinaBloque> rutinaBloques;

    @OneToMany(mappedBy = "rutina",  cascade = CascadeType.ALL, orphanRemoval = true)
    List<AsignacionRutina> asignacionRutinas;
}
