package com.joana.gymrutine.repository;

import com.joana.gymrutine.model.Bloque;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloqueRepository extends JpaRepository<Bloque, Long> {
    boolean existsByNombre(String nombre);

}
