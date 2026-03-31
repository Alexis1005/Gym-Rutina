package com.joana.gymrutine.dto.rutina;

import com.joana.gymrutine.dto.bloque.BloqueEjercicioDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RutinaBloqueResponseDTO {

    private Long bloqueId;
    private String nombreBloque;
    private Integer orden;

    private List<BloqueEjercicioDTO> ejercicios;
}
