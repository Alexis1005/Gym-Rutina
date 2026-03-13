package com.joana.gymrutine.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")

@Entity(name="Bloque")
@Table(name="bloque")
public class Bloque {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @OneToMany(mappedBy = "bloque", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RutinaBloque> rutinaBloques;

    @OneToMany(mappedBy = "bloque", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<BloqueEjercicio>  bloqueEjercicio;
}
