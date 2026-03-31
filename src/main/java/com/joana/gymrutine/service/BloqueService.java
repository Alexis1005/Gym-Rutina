package com.joana.gymrutine.service;

import com.joana.gymrutine.dto.bloque.BloqueActualizarDTO;
import com.joana.gymrutine.dto.bloque.BloqueCrearDTO;
import com.joana.gymrutine.model.Bloque;
import com.joana.gymrutine.model.BloqueEjercicio;
import com.joana.gymrutine.repository.BloqueEjercicioRepository;
import com.joana.gymrutine.repository.BloqueRepository;
import com.joana.gymrutine.repository.EjercicioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BloqueService {

    @Autowired
    private BloqueRepository bloqueRepository;

    @Autowired
    private EjercicioRepository ejercicioRepository;

    @Autowired
    private BloqueEjercicioRepository bloqueEjercicioRepository;

    public Bloque crear(BloqueCrearDTO dto) {

        if (dto.getNombre() == null || dto.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        if (bloqueRepository.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("El nombre ya existe en el sistema");
        }

        if (dto.getEjercicios() == null || dto.getEjercicios().isEmpty()) {
            throw new IllegalArgumentException("El bloque no contiene ejercicios.");
        }

        Bloque bloque = new Bloque();
        bloque.setNombre(dto.getNombre());
        bloqueRepository.save(bloque);

        dto.getEjercicios().forEach(ejercicioDTO -> {
            var ejercicio = ejercicioRepository.findById(ejercicioDTO.getEjercicioId())
                    .orElseThrow(() -> new IllegalArgumentException("El ejercicio no existe"));

            BloqueEjercicio bloqueEjercicio = new BloqueEjercicio();
            bloqueEjercicio.setBloque(bloque);
            bloqueEjercicio.setEjercicio(ejercicio);
            bloqueEjercicio.setSeries(ejercicioDTO.getSeries());
            bloqueEjercicio.setDescansoMinutos(ejercicioDTO.getDescansoMinutos());

            bloque.getBloqueEjercicio().add(bloqueEjercicio); // consistente con actualizar
        });

        return bloqueRepository.save(bloque);
    }

    public List<Bloque> listar(){
        return bloqueRepository.findAll();
    }

    public Bloque listarPorId(Long id){
        if (id == null || id <= 0){
            throw new IllegalArgumentException("Id invalido");
        }
        return bloqueRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Bloque no encontrado con id: " + id));
    }

    public Bloque actualizar(Long id, BloqueActualizarDTO dto) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Id invalido");
        }

        var bloqueExistente = bloqueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bloque no encontrado con id: " + id));

        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        if (!bloqueExistente.getNombre().equals(dto.getNombre().trim())) {
            if (bloqueRepository.existsByNombre(dto.getNombre().trim())) {
                throw new IllegalArgumentException("El nombre ya existe en el sistema");
            }
        }

        if (dto.getEjercicios() == null || dto.getEjercicios().isEmpty()) {
            throw new IllegalArgumentException("El bloque debe tener al menos un ejercicio");
        }

        bloqueExistente.setNombre(dto.getNombre().trim());
        bloqueExistente.getBloqueEjercicio().clear(); // orphanRemoval se encarga del DELETE

        for (var ejercicioDTO : dto.getEjercicios()) {
            var ejercicio = ejercicioRepository.findById(ejercicioDTO.getEjercicioId())
                    .orElseThrow(() -> new IllegalArgumentException("El ejercicio no existe"));

            BloqueEjercicio bloqueEjercicio = new BloqueEjercicio();
            bloqueEjercicio.setBloque(bloqueExistente);
            bloqueEjercicio.setEjercicio(ejercicio);
            bloqueEjercicio.setSeries(ejercicioDTO.getSeries());
            bloqueEjercicio.setDescansoMinutos(ejercicioDTO.getDescansoMinutos());

            bloqueExistente.getBloqueEjercicio().add(bloqueEjercicio);
        }

        return bloqueRepository.save(bloqueExistente);
    }

    public void eliminar(Long id){
        if (id == null || id <= 0){
            throw new IllegalArgumentException("Id invalido");
        }
        bloqueRepository.deleteById(id);
    }

}
