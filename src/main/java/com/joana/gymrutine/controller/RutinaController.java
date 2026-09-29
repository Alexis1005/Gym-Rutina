package com.joana.gymrutine.controller;

import com.joana.gymrutine.dto.asignacionRutina.AsignacionRutinaCrearDTO;
import com.joana.gymrutine.dto.rutina.*;
import com.joana.gymrutine.model.Rutina;
import com.joana.gymrutine.model.RutinaEjercicio;
import com.joana.gymrutine.model.RutinaEjercicioSemana;
import com.joana.gymrutine.model.enums.*;
import com.joana.gymrutine.repository.RutinaRepository;
import com.joana.gymrutine.service.AlumnoService;
import com.joana.gymrutine.service.AsignacionRutinaService;
import com.joana.gymrutine.service.GrupoMuscularService;
import com.joana.gymrutine.service.RutinaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rutinas")
public class RutinaController {

    @Autowired
    private RutinaService rutinaService;

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private GrupoMuscularService grupoMuscularService;

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private AsignacionRutinaService asignacionRutinaService;

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("rutina", new RutinaCrearDTO());
        agregarDatosParaSelectorDeEjercicios(model);
        return "rutina/crear";
    }

    @PostMapping
    public String crearRutina(
            @Valid @ModelAttribute("rutina") RutinaCrearDTO dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            agregarDatosParaSelectorDeEjercicios(model);
            return "rutina/crear";
        }

        try {
            Rutina rutina = rutinaService.crearRutina(dto);
            redirectAttributes.addFlashAttribute("mensaje", "Rutina creada correctamente");
            return "redirect:/rutinas/" + rutina.getId();
        } catch (IllegalArgumentException e) {
            bindingResult.reject("error.general", e.getMessage());
            agregarDatosParaSelectorDeEjercicios(model);
            return "rutina/crear";
        }
    }

    @GetMapping("/{id}")
    public String verRutina(@PathVariable Long id, Model model) {
        Rutina rutina = rutinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));

        model.addAttribute("rutina", rutina);
        model.addAttribute("diasDetalle", agruparEjerciciosPorDia(rutina));
        model.addAttribute("alumnosDisponibles", alumnoService.listar());

        return "rutina/detalle";
    }

    /**
     * Agrupa los RutinaEjercicio de una rutina por día, ordenados por día y por
     * orden dentro del día, en una estructura plana (List<Map>) fácil de recorrer
     * desde Thymeleaf sin necesitar lógica de agrupación en el template.
     */
    private List<Map<String, Object>> agruparEjerciciosPorDia(Rutina rutina) {
        Map<Integer, List<RutinaEjercicio>> porDia = rutina.getRutinaEjercicios().stream()
                .collect(Collectors.groupingBy(RutinaEjercicio::getDia));

        return porDia.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    Map<String, Object> diaMap = new HashMap<>();
                    diaMap.put("dia", entry.getKey());
                    diaMap.put("ejercicios", entry.getValue().stream()
                            .sorted(Comparator.comparingInt(RutinaEjercicio::getOrden))
                            .map(re -> {
                                Map<String, Object> ejMap = new HashMap<>();
                                ejMap.put("nombreEjercicio", re.getEjercicio().getNombre());
                                ejMap.put("orden", re.getOrden());
                                ejMap.put("semanas", re.getSemanas().stream()
                                        .sorted(Comparator.comparingInt(RutinaEjercicioSemana::getSemana))
                                        .map(s -> {
                                            Map<String, Object> semMap = new HashMap<>();
                                            semMap.put("semana", s.getSemana());
                                            semMap.put("series", s.getSeries());
                                            semMap.put("repeticiones", s.getRepeticiones());
                                            semMap.put("pesoKg", s.getPesoKg());
                                            semMap.put("descansoMinutos", s.getDescansoMinutos());
                                            semMap.put("rir", s.getRir());
                                            return semMap;
                                        })
                                        .collect(Collectors.toList()));
                                return ejMap;
                            })
                            .collect(Collectors.toList()));
                    return diaMap;
                })
                .collect(Collectors.toList());
    }

    @GetMapping
    public String listarRutinas(Model model) {
        List<Rutina> rutinas = rutinaRepository.findAll();
        model.addAttribute("rutinas", rutinas);
        return "rutina/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarRutina(@PathVariable Long id, Model model) {
        Rutina rutina = rutinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));

        RutinaActualizarDTO dto = new RutinaActualizarDTO();
        dto.setNombre(rutina.getNombre());
        dto.setDescripcion(rutina.getDescripcion());
        dto.setEjercicios(mapearAEjerciciosDTO(rutina));

        model.addAttribute("rutina", rutina);
        model.addAttribute("rutinaActualizar", dto);
        model.addAttribute("ejerciciosJS", mapearAEjerciciosParaJS(rutina));
        agregarDatosParaSelectorDeEjercicios(model);

        return "rutina/editar";
    }

    @PostMapping("/editar/{id}")
    public String actualizarRutina(
            @PathVariable Long id,
            @Valid @ModelAttribute("rutinaActualizar") RutinaActualizarDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            Rutina rutina = rutinaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));
            model.addAttribute("rutina", rutina);
            agregarDatosParaSelectorDeEjercicios(model);
            return "rutina/editar";
        }

        try {
            rutinaService.actualizarRutina(id, dto);
            redirectAttributes.addFlashAttribute("mensaje", "Rutina actualizada correctamente");
            return "redirect:/rutinas/" + id;
        } catch (IllegalArgumentException e) {
            result.reject("error.general", e.getMessage());
            Rutina rutina = rutinaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));
            model.addAttribute("rutina", rutina);
            agregarDatosParaSelectorDeEjercicios(model);
            return "rutina/editar";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarRutina(@PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {
        try{
            rutinaService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Rutina eliminada correctamente!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Error al eliminar Rutina: " + e.getMessage());
        }
        return "redirect:/rutinas";
    }

    @PostMapping("/{id}/asignar-alumno")
    public String asignarAlumno(@PathVariable Long id, @RequestParam Long alumnoId, RedirectAttributes redirectAttributes){
        AsignacionRutinaCrearDTO dto = new AsignacionRutinaCrearDTO();
        dto.setAlumnoId(alumnoId);
        dto.setRutinaId(id);

        try{
            asignacionRutinaService.asignarRutina(dto);
            redirectAttributes.addFlashAttribute("mensaje", "Rutina asignada correctamente!");
        } catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/rutinas/" + id;
    }

    //---------------------------------
    //------------HELPERS---------------
    //---------------------------------

    /**
     * Carga en el Model todo lo que necesita el selector de ejercicios del formulario
     * (grupos musculares + valores de los enums para los filtros).
     */
    private void agregarDatosParaSelectorDeEjercicios(Model model) {
        model.addAttribute("gruposMusculares", grupoMuscularService.listar());
        model.addAttribute("tiposArticulares", TipoArticular.values());
        model.addAttribute("cadenasCineticas", CadenaCinetica.values());
        model.addAttribute("lateralidades", Lateralidad.values());
        model.addAttribute("elementos", Elemento.values());
        model.addAttribute("posiciones", Posicion.values());
    }

    /**
     * Convierte los RutinaEjercicio ya cargados en una Rutina existente
     * a la misma forma de DTO que usa el formulario de crear (RutinaEjercicioDTO),
     * para precargar el formulario de edición.
     */
    private List<RutinaEjercicioDTO> mapearAEjerciciosDTO(Rutina rutina) {
        return rutina.getRutinaEjercicios().stream()
                .sorted(Comparator.comparing(RutinaEjercicio::getDia)
                        .thenComparing(RutinaEjercicio::getOrden))
                .map(re -> {
                    RutinaEjercicioDTO dto = new RutinaEjercicioDTO();
                    dto.setEjercicioId(re.getEjercicio().getId());
                    dto.setDia(re.getDia());
                    dto.setOrden(re.getOrden());
                    dto.setSemanas(re.getSemanas().stream()
                            .sorted(Comparator.comparing(s -> s.getSemana()))
                            .map(s -> {
                                RutinaEjercicioSemanaDTO semDto = new RutinaEjercicioSemanaDTO();
                                semDto.setSemana(s.getSemana());
                                semDto.setSeries(s.getSeries());
                                semDto.setRepeticiones(s.getRepeticiones());
                                semDto.setPesoKg(s.getPesoKg());
                                semDto.setDescansoMinutos(s.getDescansoMinutos());
                                semDto.setRir(s.getRir());
                                semDto.setCadencia(s.getCadencia());
                                semDto.setMetodo(s.getMetodo());
                                return semDto;
                            })
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Misma información que mapearAEjerciciosDTO, pero en Map plano
     * (más fácil de consumir desde JS en el template de edición, junto
     * con el nombre del ejercicio que el DTO no trae).
     */
    private List<Map<String, Object>> mapearAEjerciciosParaJS(Rutina rutina) {
        return rutina.getRutinaEjercicios().stream()
                .sorted(Comparator.comparing(RutinaEjercicio::getDia)
                        .thenComparing(RutinaEjercicio::getOrden))
                .map(re -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("ejercicioId", re.getEjercicio().getId());
                    map.put("nombreEjercicio", re.getEjercicio().getNombre());
                    map.put("dia", re.getDia());
                    map.put("orden", re.getOrden());
                    map.put("semanas", re.getSemanas().stream()
                            .sorted(Comparator.comparing(s -> s.getSemana()))
                            .map(s -> {
                                Map<String, Object> semMap = new HashMap<>();
                                semMap.put("semana", s.getSemana());
                                semMap.put("series", s.getSeries());
                                semMap.put("repeticiones", s.getRepeticiones());
                                semMap.put("pesoKg", s.getPesoKg());
                                semMap.put("descansoMinutos", s.getDescansoMinutos());
                                semMap.put("rir", s.getRir());
                                semMap.put("cadencia", s.getCadencia());
                                semMap.put("metodo", s.getMetodo());
                                return semMap;
                            })
                            .collect(Collectors.toList()));
                    return map;
                })
                .collect(Collectors.toList());
    }
}