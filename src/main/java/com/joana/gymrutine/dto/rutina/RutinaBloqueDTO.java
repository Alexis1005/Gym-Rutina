package com.joana.gymrutine.dto.rutina;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RutinaBloqueDTO {
    @NotNull
    private Long bloqueId;

    private Integer orden;
}
