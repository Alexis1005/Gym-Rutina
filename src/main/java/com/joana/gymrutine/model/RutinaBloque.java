package com.joana.gymrutine.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="id")

@Entity(name="RutinaBloque")
@Table(name="rutina_bloque")

public class RutinaBloque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rutina_id", nullable=false)
    private Rutina rutina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bloque_id", nullable=false)
    private Bloque bloque;

}
