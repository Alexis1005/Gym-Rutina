package com.joana.gymrutine.model;

import com.joana.gymrutine.model.enums.*;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")

@Table(name="ejercicio")
@Entity(name="Ejercicio")
public class Ejercicio {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    private String nombre;
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_articular")
    private TipoArticular tipoArticular;

    @Enumerated(EnumType.STRING)
    @Column(name = "cadena_cinetica")
    private CadenaCinetica cadenaCinetica;

    @Enumerated(EnumType.STRING)
    private Lateralidad lateralidad;

    @Enumerated(EnumType.STRING)
    private Elemento elemento;

    @Enumerated(EnumType.STRING)
    private Posicion posicion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_muscular_id", nullable = false)
    private GrupoMuscular grupoMuscular;
}