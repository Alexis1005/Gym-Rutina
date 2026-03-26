package com.joana.gymrutine.repository;

import com.joana.gymrutine.model.Ejercicio;
import com.joana.gymrutine.model.GrupoMuscular;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {

    Optional<Ejercicio> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<Ejercicio> findAllByGrupoMuscularId(Long id);
}