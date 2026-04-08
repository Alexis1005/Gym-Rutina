package com.joana.gymrutine.repository;

import com.joana.gymrutine.model.AsignacionRutina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AsignacionRutinaRepository extends JpaRepository<AsignacionRutina, Long> {

    boolean existsByAlumnoIdAndRutinaId(Long alumnoId, Long rutinaId);

    List<AsignacionRutina> findByAlumnoId(Long alumnoId);
}
