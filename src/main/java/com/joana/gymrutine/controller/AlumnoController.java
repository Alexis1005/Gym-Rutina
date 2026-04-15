package com.joana.gymrutine.controller;

import com.joana.gymrutine.dto.alumno.AlumnoActualizarDTO;
import com.joana.gymrutine.dto.alumno.AlumnoCrearDTO;
import com.joana.gymrutine.dto.asignacionRutina.AsignacionRutinaResponseDTO;
import com.joana.gymrutine.model.Alumno;
import com.joana.gymrutine.model.Rutina;
import com.joana.gymrutine.repository.RutinaRepository;
import com.joana.gymrutine.service.AlumnoService;
import com.joana.gymrutine.service.AsignacionRutinaService;
import com.joana.gymrutine.service.RutinaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private AsignacionRutinaService asignacionRutinaService;

    @Autowired
    private RutinaRepository  rutinaRepository;

    @Autowired
    private RutinaService rutinaService;

    @GetMapping("/crear")
    public String mostarFormulario(Model model) {
        var rutinas = rutinaRepository.findAll();
        var alumno =  new AlumnoCrearDTO();

        model.addAttribute("alumno", alumno);
        model.addAttribute("rutinas", rutinas);

        return "alumnos/crear";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("alumno") AlumnoCrearDTO alumnoDTO,
                        BindingResult bindingResult, RedirectAttributes attributes,  Model model) {
        if (bindingResult.hasErrors()) {
            var rutinas = rutinaRepository.findAll();
            model.addAttribute("rutinas", rutinas);
            return "alumnos/crear";
        }

        try {
            Alumno alumnoGuardado = alumnoService.crearAlumno(alumnoDTO);
            attributes.addFlashAttribute("mensaje", "Alumno creado correctamente!");
            return "redirect:/alumnos/" +  alumnoGuardado.getId();
        } catch (IllegalArgumentException e) {
            bindingResult.reject("Error general", e.getMessage());
            List<Rutina> rutinas = rutinaRepository.findAll();
            model.addAttribute("rutinas", rutinas);

            return "alumnos/crear";
        }
    }

    @GetMapping
    public String listarAlumnos(Model model) {
        var alumnos = alumnoService.listar();
        model.addAttribute("alumnos", alumnos);
        return "alumnos/listar";
    }

    @GetMapping("/{id}")
    public String mostrarAlumnos(@PathVariable Long id, Model model) {
        var alumno = alumnoService.obtenerPorId(id);

        // Obtener todas las rutinas
        var todasLasRutinas = rutinaRepository.findAll();

        // Obtener los IDs de rutinas ya asignadas
        var rutinasAsignadas = alumno.getRutinas().stream()
                .map(AsignacionRutinaResponseDTO::getRutinaId)
                .collect(Collectors.toSet());

        // Filtrar rutinas disponibles (las que NO están asignadas)
        var rutinasDisponibles = todasLasRutinas.stream()
                .filter(r -> !rutinasAsignadas.contains(r.getId()))
                .collect(Collectors.toList());

        model.addAttribute("alumno", alumno);
        model.addAttribute("rutinasDisponibles", rutinasDisponibles);

        return "alumnos/detalle";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        var alumnoResponse =  alumnoService.obtenerPorId(id);

        AlumnoActualizarDTO dto = new AlumnoActualizarDTO();
        dto.setId(alumnoResponse.getId());
        dto.setNombreApellido(alumnoResponse.getNombreApellido());
        dto.setObservaciones(alumnoResponse.getObservaciones());

        model.addAttribute("alumno", dto);
        return "alumnos/editar";
    }

    @PostMapping("/{id}")
    public String actualizarAlumno(@Valid @ModelAttribute("alumno") AlumnoActualizarDTO dto, @PathVariable Long id, BindingResult bindingResult,
                                   RedirectAttributes attributes, Model model) {
        if (bindingResult.hasErrors()) {
            return "alumnos/editar";
        }
        try{
            Alumno alumnoActualizado = alumnoService.actualizar(id, dto);
            attributes.addFlashAttribute("mensaje", "Alumno actualizado correctamente!");
            return "redirect:/alumnos/" +  alumnoActualizado.getId();
        } catch (IllegalArgumentException e) {
            bindingResult.reject("Error general", e.getMessage());
            return "alumnos/editar";
        }
    }

    @PostMapping("/{id}/asignaciones/{asignacionId}/eliminar")
    public String eliminarAsignacion(@PathVariable Long id, @PathVariable Long asignacionId, RedirectAttributes attributes) {
        try{
            asignacionRutinaService.desasignarRutina(asignacionId);
            attributes.addFlashAttribute("mensaje", "Asignacion eliminado correctamente!");
            return "redirect:/alumnos/" + id;
        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("error",  e.getMessage());
            return "redirect:/alumnos/" + id;
        }
    }

    @PostMapping("/{id}/asignar-rutina")
    public String asignarRutina(@PathVariable Long id,
                                @RequestParam Long rutinaId,
                                RedirectAttributes redirectAttributes) {
        try {
            alumnoService.asignarRutinaAAlumno(id, rutinaId);
            redirectAttributes.addFlashAttribute("mensaje", "Rutina asignada correctamente");
            return "redirect:/alumnos/" + id;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/alumnos/" + id;
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarAlumno(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            alumnoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Alumno eliminado correctamente");
            return "redirect:/alumnos";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/alumnos";
        }
    }

    @GetMapping("/{alumnoId}/rutinas/{rutinaId}/exportar-pdf")
    public ResponseEntity<byte[]> exportarRutinaPdf(
            @PathVariable Long alumnoId,
            @PathVariable Long rutinaId,
            RedirectAttributes redirectAttributes) {
        try {
            byte[] pdfBytes = rutinaService.generarPdfRutina(alumnoId, rutinaId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "rutina.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
