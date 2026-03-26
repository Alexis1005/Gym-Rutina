package com.joana.gymrutine.dto.ejercicio;

import com.joana.gymrutine.model.BloqueEjercicio;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

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
}
