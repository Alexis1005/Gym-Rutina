package com.joana.gymrutine.repository;

import com.joana.gymrutine.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    List<Alumno> findAllByOrderByNombreApellidoAsc();
}
