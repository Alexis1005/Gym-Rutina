package com.joana.gymrutine.service;

import com.joana.gymrutine.dto.rutina.RutinaBloqueActualizarDTO;
import com.joana.gymrutine.dto.rutina.RutinaBloqueDTO;
import com.joana.gymrutine.dto.rutina.RutinaBloqueEjercicioSemanaDTO;
import com.joana.gymrutine.dto.rutina.RutinaCrearDTO;
import com.joana.gymrutine.model.*;
import com.joana.gymrutine.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RutinaService {

    @Autowired
    private RutinaRepository rutinaRepository;
    @Autowired
    private BloqueRepository bloqueRepository;
    @Autowired
    private RutinaBloqueEjercicioSemanaRepository rutinaBloqueEjercicioSemanaRepository;

    public Rutina crearRutina(RutinaCrearDTO dto) {
        if (dto.getCantidadSemanas() == null || dto.getCantidadSemanas() <= 0) {
            throw new IllegalArgumentException("Debe ingresar un numero de semanas");
        }
        if (dto.getBloques() == null || dto.getBloques().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar un numero de bloques");
        }

        Set<Integer> ordenes = new HashSet<>();

        for (RutinaBloqueDTO b : dto.getBloques()) {
            if (b.getOrden() == null) {
                throw new IllegalArgumentException("Todos los bloques deben tener orden");
            }
            if (!ordenes.add(b.getOrden())) {
                throw new IllegalArgumentException("Orden de bloques duplicado");
            }
        }


        Rutina rutina = new Rutina();
        rutina.setNombre(dto.getNombre());
        rutina.setDescripcion(dto.getDescripcion());
        rutina.setCantidadSemanas(dto.getCantidadSemanas());

        List<RutinaBloque> rutinasBloques = new ArrayList<>();
        for (RutinaBloqueDTO bloqueDTO : dto.getBloques()){
            Bloque bloque = bloqueRepository.findById(bloqueDTO.getBloqueId())
                    .orElseThrow(() -> new IllegalArgumentException("El bloque no se encuentra"));

            RutinaBloque rutinaBloque = new RutinaBloque();
            rutinaBloque.setRutina(rutina);
            rutinaBloque.setBloque(bloque);
            rutinaBloque.setOrden(bloqueDTO.getOrden());
            rutinasBloques.add(rutinaBloque);
        }
        rutinasBloques.sort(Comparator.comparing(RutinaBloque::getOrden));

        rutina.setRutinaBloques(rutinasBloques);

        return rutinaRepository.save(rutina);
    }

    public void cargarProgresionSemanas(Long rutinaId, List<RutinaBloqueEjercicioSemanaDTO> semanasDTO) {
        Rutina rutina = rutinaRepository.findById(rutinaId)
                .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));

        // Limpiar datos anteriores (opcional)
        List<RutinaBloqueEjercicioSemana> semanasExistentes =
                rutinaBloqueEjercicioSemanaRepository.findByRutinaId(rutinaId);
        rutinaBloqueEjercicioSemanaRepository.deleteAll(semanasExistentes);

        // Guardar nuevos datos
        for (RutinaBloqueEjercicioSemanaDTO dto : semanasDTO) {
            // Validar que el bloque existe en esta rutina
            RutinaBloque rutinaBloque = rutina.getRutinaBloques().stream()
                    .filter(rb -> rb.getId().equals(dto.getRutinaBloqueId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "El bloque no pertenece a esta rutina: " + dto.getRutinaBloqueId()
                    ));

            // Validar que el ejercicio existe en este bloque
            BloqueEjercicio bloqueEjercicio = rutinaBloque.getBloque().getBloqueEjercicio().stream()
                    .filter(be -> be.getId().equals(dto.getBloqueEjercicioId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "El ejercicio no pertenece a este bloque: " + dto.getBloqueEjercicioId()
                    ));

            // Crear entidad
            RutinaBloqueEjercicioSemana semana = new RutinaBloqueEjercicioSemana();
            semana.setRutinaBloque(rutinaBloque);
            semana.setBloqueEjercicio(bloqueEjercicio);
            semana.setNumeroSemana(dto.getNumeroSemana());
            semana.setRepeticiones(dto.getRepeticiones());
            semana.setPesoKg(dto.getPesoKg());

            rutinaBloqueEjercicioSemanaRepository.save(semana);
        }
    }

    public void actualizarBloques(Long rutinaId, List<RutinaBloqueActualizarDTO> bloquesDTO) {
        Rutina rutina = rutinaRepository.findById(rutinaId)
                .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));

        // Validar órdenes duplicadas
        Set<Integer> ordenes = new HashSet<>();
        for (RutinaBloqueActualizarDTO b : bloquesDTO) {
            if (!ordenes.add(b.getOrden())) {
                throw new IllegalArgumentException("Orden de bloques duplicado: " + b.getOrden());
            }
        }

        // IDs nuevos
        Set<Long> nuevosIds = bloquesDTO.stream()
                .map(RutinaBloqueActualizarDTO::getBloqueId)
                .collect(Collectors.toSet());

        // 1. ELIMINAR
        rutina.getRutinaBloques().removeIf(rb ->
                !nuevosIds.contains(rb.getBloque().getId())
        );

        // 2 y 3. ACTUALIZAR o CREAR
        for (RutinaBloqueActualizarDTO bloqueDTO : bloquesDTO) {

            Optional<RutinaBloque> existente = rutina.getRutinaBloques().stream()
                    .filter(rb -> rb.getBloque().getId().equals(bloqueDTO.getBloqueId()))
                    .findFirst();

            if (existente.isPresent()) {
                // ACTUALIZAR
                existente.get().setOrden(bloqueDTO.getOrden());

            } else {
                // CREAR NUEVO
                Bloque bloque = bloqueRepository.findById(bloqueDTO.getBloqueId())
                        .orElseThrow(() -> new IllegalArgumentException("Bloque no existe"));

                RutinaBloque nuevo = new RutinaBloque();
                nuevo.setRutina(rutina);
                nuevo.setBloque(bloque);
                nuevo.setOrden(bloqueDTO.getOrden());

                rutina.getRutinaBloques().add(nuevo);
            }
        }

        // Ordenar (opcional pero recomendable)
        rutina.getRutinaBloques().sort(Comparator.comparing(RutinaBloque::getOrden));

        rutinaRepository.save(rutina);
    }
}
