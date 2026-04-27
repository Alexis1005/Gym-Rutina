package com.joana.gymrutine.repository;

import com.joana.gymrutine.model.AsignacionRutina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AsignacionRutinaRepository extends JpaRepository<AsignacionRutina, Long> {

    boolean existsByAlumnoIdAndRutinaId(Long alumnoId, Long rutinaId);

    List<AsignacionRutina> findByAlumnoId(Long alumnoId);

    Optional<AsignacionRutina> findByAlumnoIdAndRutinaId(Long alumnoId, Long rutinaId);

    List<AsignacionRutina> findByRutinaId(Long id);
}
