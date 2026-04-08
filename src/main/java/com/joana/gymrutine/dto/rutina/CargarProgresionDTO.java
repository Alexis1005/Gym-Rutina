package com.joana.gymrutine.dto.rutina;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CargarProgresionDTO {

    @NotEmpty(message = "Debe cargar al menos una semana")
    @Valid
    private List<RutinaBloqueEjercicioSemanaDTO> semanas;
}
