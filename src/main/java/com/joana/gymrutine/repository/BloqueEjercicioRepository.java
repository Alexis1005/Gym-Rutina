package com.joana.gymrutine.repository;

import com.joana.gymrutine.model.BloqueEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloqueEjercicioRepository extends JpaRepository<BloqueEjercicio, Long> {
    void deleteByBloqueId(Long id);
}
