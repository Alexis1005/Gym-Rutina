package com.joana.gymrutine.repository;

import com.joana.gymrutine.model.RutinaEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RutinaEjercicioRepository extends JpaRepository<RutinaEjercicio, Long> {
    boolean existsByEjercicioId(Long ejercicioId);
}