package com.joana.gymrutine.dto.grupoMuscular;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GrupoMuscularActualizarDTO {
    @NotBlank(message = "El nombre no puede estar vacío.")
    private String nombre;
}
