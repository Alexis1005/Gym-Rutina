package com.joana.gymrutine.dto.ejercicio;

import com.joana.gymrutine.model.enums.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EjercicioCrearDTO {

    @NotBlank(message = "El nombre no puede estar vacío.")
    private String nombre;
    private String descripcion;
    private Long grupoMuscularId;

    private TipoArticular tipoArticular;
    private CadenaCinetica cadenaCinetica;
    private Lateralidad lateralidad;
    private Elemento elemento;
    private Posicion posicion;
}