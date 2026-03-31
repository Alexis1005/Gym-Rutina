package com.joana.gymrutine.dto.rutina;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RutinaActualizarDTO{

    @NotBlank(message = "El nombre de la rutina es obligatorio")
    private String nombre;
    private String descripcion;
}
