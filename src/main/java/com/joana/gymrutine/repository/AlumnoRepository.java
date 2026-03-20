package com.joana.gymrutine.repository;

import com.joana.gymrutine.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
}
