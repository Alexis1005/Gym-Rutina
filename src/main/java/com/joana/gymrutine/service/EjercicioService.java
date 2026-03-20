package com.joana.gymrutine.service;

import com.joana.gymrutine.dto.ejercicio.EjercicioActualizarDTO;
import com.joana.gymrutine.dto.ejercicio.EjercicioCrearDTO;
import com.joana.gymrutine.model.Ejercicio;
import com.joana.gymrutine.repository.EjercicioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EjercicioService {

    @Autowired
    private EjercicioRepository ejercicioRepository;
    @Autowired
    private GrupoMuscularService grupoMuscularService;

    public Ejercicio crear(EjercicioCrearDTO dto) {

        //BUSCANDO EL GRUPO MUSCULAR DEL EJERCICIO
        var grupoId = grupoMuscularService.listarPorId(dto.getGrupoMuscularId());

        if (ejercicioRepository.existsByNombre(dto.getNombre().trim())) {
            throw new IllegalArgumentException("El nombre existe en la base de datos.");
        }
        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setNombre(dto.getNombre().trim());
        ejercicio.setDescripcion(dto.getDescripcion().trim());
        ejercicio.setGrupoMuscular(grupoId);
        return ejercicioRepository.save(ejercicio);
    }

    //-----------------------------------------------------
    public List<Ejercicio> listarTodos() {
        return ejercicioRepository.findAll();
    }

    //-----------------------------------------------------
    public Ejercicio listarPorId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id del ejercicio no es válido.");
        }

        return ejercicioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("El id del ejercicio no existe."));
    }

    //-----------------------------------------------------
    public Ejercicio listarPorNombre(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        return ejercicioRepository.findByNombre(nombre)
                .orElseThrow(() -> new IllegalArgumentException("El nombre del ejercicio no existe."));
    }

    //-----------------------------------------------------
    public Ejercicio actualizar(Long id, EjercicioActualizarDTO dto) {

        var ejercicio = ejercicioRepository.findById(id);
        if (!ejercicio.isPresent()) {
            throw new IllegalArgumentException("Ejercicio no encontrado con el id: " + id);
        }

        var ejercicioActualizado = ejercicio.get();
        ejercicioActualizado.setNombre(dto.getNombre().trim());
        ejercicioActualizado.setDescripcion(dto.getDescripcion().trim());

        if (dto.getGrupoMuscularId() != null) {
            var grupo = grupoMuscularService.listarPorId(dto.getGrupoMuscularId());
            ejercicioActualizado.setGrupoMuscular(grupo);
        }
        return ejercicioRepository.save(ejercicioActualizado);
    }

    //-----------------------------------------------------
    public void eliminar(Long id) {

        var  ejercicio = ejercicioRepository.findById(id);
        if (!ejercicio.isPresent()) {
            throw new IllegalArgumentException("Ejercicio no encontrado con el id: " + id);
        }
        ejercicioRepository.deleteById(id);
    }

}
