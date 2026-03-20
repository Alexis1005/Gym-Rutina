package com.joana.gymrutine.dto.grupoMuscular;

import com.joana.gymrutine.model.Ejercicio;
import com.joana.gymrutine.model.GrupoMuscular;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GrupoMuscularResponseDTO {

    private Long id;
    private String nombre;
    private List<Ejercicio> ejercicios;
}
