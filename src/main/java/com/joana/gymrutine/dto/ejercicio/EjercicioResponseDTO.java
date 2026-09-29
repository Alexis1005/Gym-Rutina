package com.joana.gymrutine.dto.ejercicio;

import com.joana.gymrutine.model.enums.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EjercicioResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String grupoMuscularNombre;
    private Long grupoMuscularId;

    private TipoArticular tipoArticular;
    private CadenaCinetica cadenaCinetica;
    private Lateralidad lateralidad;
    private Elemento elemento;
    private Posicion posicion;
}