package com.joana.gymrutine.dto.rutina;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class RutinaBloqueActualizarDTO {

        @NotNull(message = "El ID del bloque es obligatorio")
        private Long bloqueId;

        @NotNull(message = "El orden es obligatorio")
        @Min(value = 1, message = "El orden debe ser mayor a 0")
        private Integer orden;
    }
