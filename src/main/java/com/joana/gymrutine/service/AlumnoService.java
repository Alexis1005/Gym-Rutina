package com.joana.gymrutine.service;

import com.joana.gymrutine.dto.alumno.AlumnoCrearDTO;
import com.joana.gymrutine.dto.alumno.AlumnoActualizarDTO;
import com.joana.gymrutine.dto.alumno.AlumnoResponseDTO;
import com.joana.gymrutine.dto.asignacionRutina.AsignacionRutinaResponseDTO;
import com.joana.gymrutine.model.Alumno;
import com.joana.gymrutine.model.AsignacionRutina;
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
public class AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private AsignacionRutinaRepository asignacionRutinaRepository;

    /**
     * Crear alumno + asignar rutinas en un mismo flujo
     */
    @Transactional
    public Alumno crearAlumno(AlumnoCrearDTO dto) {
        // Validar que el nombre no esté vacío
        if (dto.getNombreApellido() == null || dto.getNombreApellido().isBlank()) {
            throw new IllegalArgumentException("El nombre y apellido del alumno es obligatorio");
        }

        // Crear el alumno
        Alumno alumno = new Alumno();
        alumno.setNombreApellido(dto.getNombreApellido());
        alumno.setObservaciones(dto.getObservaciones());

        Alumno alumnoGuardado = alumnoRepository.save(alumno);

        // Asignar rutinas si hay
        if (dto.getRutinas() != null && !dto.getRutinas().isEmpty()) {
            for (Long rutinaId : dto.getRutinas()) {
                Rutina rutina = rutinaRepository.findById(rutinaId)
                        .orElseThrow(() -> new IllegalArgumentException("Rutina con ID " + rutinaId + " no encontrada"));

                AsignacionRutina asignacion = new AsignacionRutina();
                asignacion.setAlumno(alumnoGuardado);
                asignacion.setRutina(rutina);
                asignacion.setFechaAsignacion(LocalDate.now());

                asignacionRutinaRepository.save(asignacion);
            }
        }

        return alumnoGuardado;
    }

    /**
     * Obtener alumno por ID con sus rutinas asignadas
     */
    public AlumnoResponseDTO obtenerPorId(Long id) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));

        return mapearAResponseDTO(alumno);
    }

    /**
     * Listar todos los alumnos
     */
    public List<AlumnoResponseDTO> listar() {
        return alumnoRepository.findAll().stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualizar datos básicos del alumno (nombre, observaciones)
     */
    @Transactional
    public Alumno actualizar(Long id, AlumnoActualizarDTO dto) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));

        alumno.setNombreApellido(dto.getNombreApellido());
        alumno.setObservaciones(dto.getObservaciones());

        return alumnoRepository.save(alumno);
    }

    /**
     * Eliminar alumno (y sus asignaciones en cascada)
     */
    @Transactional
    public void eliminar(Long id) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));

        alumnoRepository.deleteById(id);
    }

    /**
     * Asignar una rutina a un alumno existente
     */
    @Transactional
    public void asignarRutinaAAlumno(Long alumnoId, Long rutinaId) {
        // Obtener el alumno
        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));

        // Obtener la rutina
        Rutina rutina = rutinaRepository.findById(rutinaId)
                .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));

        // Validar que no esté ya asignada
        boolean yaAsignada = asignacionRutinaRepository.existsByAlumnoIdAndRutinaId(alumnoId, rutinaId);
        if (yaAsignada) {
            throw new IllegalArgumentException("Esta rutina ya está asignada a este alumno");
        }

        // Crear y guardar la asignación
        AsignacionRutina asignacion = new AsignacionRutina();
        asignacion.setAlumno(alumno);
        asignacion.setRutina(rutina);
        asignacion.setFechaAsignacion(LocalDate.now());

        asignacionRutinaRepository.save(asignacion);
    }

    /**
     * Mapear entidad Alumno a AlumnoResponseDTO
     */
    private AlumnoResponseDTO mapearAResponseDTO(Alumno alumno) {
        AlumnoResponseDTO dto = new AlumnoResponseDTO();
        dto.setId(alumno.getId());
        dto.setNombreApellido(alumno.getNombreApellido());
        dto.setObservaciones(alumno.getObservaciones());

        // Mapear las asignaciones de rutinas
        if (alumno.getRutinas() != null && !alumno.getRutinas().isEmpty()) {
            dto.setRutinas(alumno.getRutinas().stream()
                    .map(ar -> {
                        AsignacionRutinaResponseDTO arDto = new AsignacionRutinaResponseDTO();
                        arDto.setId(ar.getId());
                        arDto.setAlumnoId(ar.getAlumno().getId());
                        arDto.setRutinaId(ar.getRutina().getId());
                        arDto.setRutinaNombre(ar.getRutina().getNombre());
                        arDto.setFechaAsignacion(ar.getFechaAsignacion());
                        return arDto;
                    })
                    .collect(Collectors.toList()));
        } else {
            // ✅ Si no hay rutinas, inicializa una lista vacía (NO null)
            dto.setRutinas(new java.util.ArrayList<>());
        }

        return dto;
    }
}