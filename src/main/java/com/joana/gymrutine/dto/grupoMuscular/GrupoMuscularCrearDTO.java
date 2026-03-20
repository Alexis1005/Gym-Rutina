package com.joana.gymrutine.dto.grupoMuscular;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GrupoMuscularCrearDTO{
    @NotBlank(message = "El nombre no puede estar vacío.")
    private String nombre;
}
