package com.joana.gymrutine.service;

import com.joana.gymrutine.dto.asignacionRutina.AsignacionRutinaCrearDTO;
import com.joana.gymrutine.dto.asignacionRutina.AsignacionRutinaResponseDTO;
import com.joana.gymrutine.model.AsignacionRutina;
import com.joana.gymrutine.model.Alumno;
import com.joana.gymrutine.model.Rutina;
import com.joana.gymrutine.repository.AlumnoRepository;
import com.joana.gymrutine.repository.AsignacionRutinaRepository;
import com.joana.gymrutine.repository.RutinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsignacionRutinaService {

    @Autowired
    private AsignacionRutinaRepository asignacionRutinaRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private RutinaRepository rutinaRepository;

    /**
     * Asignar una rutina a un alumno
     */
    @Transactional
    public AsignacionRutina asignarRutina(AsignacionRutinaCrearDTO dto) {
        Alumno alumno = alumnoRepository.findById(dto.getAlumnoId())
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));

        Rutina rutina = rutinaRepository.findById(dto.getRutinaId())
                .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));

        // Validar que no esté ya asignada
        boolean yaAsignada = asignacionRutinaRepository.existsByAlumnoIdAndRutinaId(
                dto.getAlumnoId(),
                dto.getRutinaId()
        );

        if (yaAsignada) {
            throw new IllegalArgumentException("Esta rutina ya está asignada a este alumno");
        }

        AsignacionRutina asignacion = new AsignacionRutina();
        asignacion.setAlumno(alumno);
        asignacion.setRutina(rutina);
        asignacion.setFechaAsignacion(dto.getFechaAsignacion() != null ? dto.getFechaAsignacion() : LocalDate.now());

        return asignacionRutinaRepository.save(asignacion);
    }

    /**
     * Desasignar una rutina de un alumno
     */
    @Transactional
    public void desasignarRutina(Long asignacionId) {
        AsignacionRutina asignacion = asignacionRutinaRepository.findById(asignacionId)
                .orElseThrow(() -> new IllegalArgumentException("Asignación no encontrada"));

        asignacionRutinaRepository.deleteById(asignacionId);
    }

    /**
     * Obtener todas las asignaciones de un alumno
     */
    public List<AsignacionRutinaResponseDTO> obtenerPorAlumno(Long alumnoId) {
        return asignacionRutinaRepository.findByAlumnoId(alumnoId).stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener todas las asignaciones
     */
    public List<AsignacionRutinaResponseDTO> listar() {
        return asignacionRutinaRepository.findAll().stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Mapear entidad a DTO
     */
    private AsignacionRutinaResponseDTO mapearAResponseDTO(AsignacionRutina asignacion) {
        AsignacionRutinaResponseDTO dto = new AsignacionRutinaResponseDTO();
        dto.setId(asignacion.getId());
        dto.setAlumnoId(asignacion.getAlumno().getId());
        dto.setRutinaId(asignacion.getRutina().getId());
        dto.setRutinaNombre(asignacion.getRutina().getNombre());
        dto.setFechaAsignacion(asignacion.getFechaAsignacion());
        return dto;
    }
}