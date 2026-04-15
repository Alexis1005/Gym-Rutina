package com.joana.gymrutine.repository;

import com.joana.gymrutine.model.AsignacionRutina;
import com.joana.gymrutine.model.RutinaBloqueEjercicioSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RutinaBloqueEjercicioSemanaRepository extends JpaRepository<RutinaBloqueEjercicioSemana, Long> {

    // Encontrar todas las semanas de una rutina (por rutinaBloque)
    @Query("SELECT s FROM RutinaBloqueEjercicioSemana s WHERE s.rutinaBloque.rutina.id = :rutinaId")
    List<RutinaBloqueEjercicioSemana> findByRutinaId(@Param("rutinaId") Long rutinaId);

    // Encontrar todas las semanas de un bloque específico
    List<RutinaBloqueEjercicioSemana> findByRutinaBloqueId(Long rutinaBloqueId);

    void deleteByRutinaBloqueId(Long rutinaBloqueId);

    // En RutinaBloqueEjercicioSemanaRepository
    List<RutinaBloqueEjercicioSemana> findByRutinaBloqueIdAndBloqueEjercicioId(Long rutinaBloqueId, Long bloqueEjercicioId);
}