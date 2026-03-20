package com.joana.gymrutine.repository;

import com.joana.gymrutine.model.GrupoMuscular;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GrupoMuscularRepository extends JpaRepository<GrupoMuscular, Long> {

    Optional<GrupoMuscular> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
