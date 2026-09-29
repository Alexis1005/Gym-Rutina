package com.joana.gymrutine.repository;

import com.joana.gymrutine.model.Ejercicio;
import com.joana.gymrutine.model.GrupoMuscular;
import com.joana.gymrutine.model.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {

    Optional<Ejercicio> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<Ejercicio> findAllByGrupoMuscularId(Long id);

    @Query("SELECT e FROM Ejercicio e WHERE " +
            "(:grupoMuscularId IS NULL OR e.grupoMuscular.id = :grupoMuscularId) AND " +
            "(:tipoArticular IS NULL OR e.tipoArticular = :tipoArticular) AND " +
            "(:cadenaCinetica IS NULL OR e.cadenaCinetica = :cadenaCinetica) AND " +
            "(:lateralidad IS NULL OR e.lateralidad = :lateralidad) AND " +
            "(:elemento IS NULL OR e.elemento = :elemento) AND " +
            "(:posicion IS NULL OR e.posicion = :posicion)")
    List<Ejercicio> buscarConFiltros(
            @Param("grupoMuscularId") Long grupoMuscularId,
            @Param("tipoArticular") TipoArticular tipoArticular,
            @Param("cadenaCinetica") CadenaCinetica cadenaCinetica,
            @Param("lateralidad") Lateralidad lateralidad,
            @Param("elemento") Elemento elemento,
            @Param("posicion") Posicion posicion
    );
}