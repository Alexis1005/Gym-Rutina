package com.joana.gymrutine.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")

@Table(name = "asignacion_rutina")
@Entity(name = "AsignacionRutina")
public class AsignacionRutina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fechaAsignacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="alumno_id", nullable=false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rutina_id", nullable=false)
    private Rutina rutina;
}
