package com.joana.gymrutine.controller;

import com.joana.gymrutine.dto.bloque.BloqueEjercicioDTO;
import com.joana.gymrutine.dto.bloque.BloqueResponseDTO;
import com.joana.gymrutine.dto.rutina.*;
import com.joana.gymrutine.model.Bloque;
import com.joana.gymrutine.model.Rutina;
import com.joana.gymrutine.model.RutinaBloque;
import com.joana.gymrutine.model.RutinaBloqueEjercicioSemana;
import com.joana.gymrutine.repository.BloqueRepository;
import com.joana.gymrutine.repository.RutinaBloqueEjercicioSemanaRepository;
import com.joana.gymrutine.repository.RutinaRepository;
import com.joana.gymrutine.service.BloqueService;
import com.joana.gymrutine.service.RutinaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private BloqueRepository bloqueRepository;

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private RutinaBloqueEjercicioSemanaRepository rutinaBloqueEjercicioSemanaRepository;

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        List<Bloque> bloques = bloqueRepository.findAll();
        List<BloqueResponseDTO> bloquesDTO = bloques.stream()
                .map(this::mapearBloqueADTO)
                .collect(Collectors.toList());

        model.addAttribute("rutina", new RutinaCrearDTO());
        model.addAttribute("rutinaBloques", bloquesDTO);

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
            List<Bloque> bloques = bloqueRepository.findAll();
            List<BloqueResponseDTO> bloquesDTO = bloques.stream()
                    .map(this::mapearBloqueADTO)
                    .collect(Collectors.toList());

            model.addAttribute("rutinaBloques", bloquesDTO);
            return "rutina/crear";
        }

        try {
            Rutina rutina = rutinaService.crearRutina(dto);
            redirectAttributes.addFlashAttribute("mensaje", "Rutina creada correctamente");
            return "redirect:/rutinas/" + rutina.getId();
        } catch (IllegalArgumentException e) {
            bindingResult.reject("error.general", e.getMessage());

            List<Bloque> bloques = bloqueRepository.findAll();
            List<BloqueResponseDTO> bloquesDTO = bloques.stream()
                    .map(this::mapearBloqueADTO)
                    .collect(Collectors.toList());

            model.addAttribute("rutinaBloques", bloquesDTO);
            return "rutina/crear";
        }
    }

    private BloqueResponseDTO mapearBloqueADTO(Bloque bloque) {
        BloqueResponseDTO dto = new BloqueResponseDTO();
        dto.setId(bloque.getId());
        dto.setNombre(bloque.getNombre());
        dto.setEjercicios(bloque.getBloqueEjercicio().stream()
                .map(be -> {
                    BloqueEjercicioDTO ejercicioDTO = new BloqueEjercicioDTO();
                    ejercicioDTO.setEjercicioId(be.getEjercicio().getId());
                    ejercicioDTO.setNombreEjercicio(be.getEjercicio().getNombre());
                    ejercicioDTO.setSeries(be.getSeries());
                    ejercicioDTO.setDescansoMinutos(be.getDescansoMinutos());
                    return ejercicioDTO;
                })
                .collect(Collectors.toList()));
        return dto;
    }

    @GetMapping("/{id}")
    public String verRutina(@PathVariable Long id, Model model) {
        Rutina rutina = rutinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));

        // Obtener todas las semanas cargadas para esta rutina
        List<RutinaBloqueEjercicioSemana> semanas =
                rutinaBloqueEjercicioSemanaRepository.findByRutinaId(id);

        // Crear mapa: bloque.id → ejercicio.id → semana → objeto
        Map<Long, Map<Long, Map<Integer, RutinaBloqueEjercicioSemana>>> semanasMap = new HashMap<>();

        for (RutinaBloqueEjercicioSemana semana : semanas) {
            Long bloqueId = semana.getRutinaBloque().getId();
            Long ejercicioId = semana.getBloqueEjercicio().getId();
            Integer numeroSemana = semana.getNumeroSemana();

            semanasMap.putIfAbsent(bloqueId, new HashMap<>());
            semanasMap.get(bloqueId).putIfAbsent(ejercicioId, new HashMap<>());
            semanasMap.get(bloqueId).get(ejercicioId).put(numeroSemana, semana);
        }

        model.addAttribute("rutina", rutina);
        model.addAttribute("semanasMap", semanasMap);

        return "rutina/detalle";
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

        // Cargar bloques disponibles (todos los de la BD)
        List<Bloque> bloquesDisponibles = bloqueRepository.findAll();
        List<BloqueResponseDTO> bloquesDTO = bloquesDisponibles.stream()
                .map(this::mapearBloqueADTO)
                .collect(Collectors.toList());

        // Convertir bloques actuales a DTO para el formulario
        List<RutinaBloqueActualizarDTO> bloquesActualesDTO = rutina.getRutinaBloques().stream()
                .map(rb -> new RutinaBloqueActualizarDTO(rb.getBloque().getId(), rb.getOrden()))
                .sorted(Comparator.comparingInt(RutinaBloqueActualizarDTO::getOrden))
                .collect(Collectors.toList());

        // Convertir a DTO de actualización
        RutinaActualizarDTO dto = new RutinaActualizarDTO();
        dto.setNombre(rutina.getNombre());
        dto.setDescripcion(rutina.getDescripcion());
        dto.setBloques(bloquesActualesDTO);

        List<Map<String, Object>> bloquesParaJS = rutina.getRutinaBloques().stream()
                .map(rb -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("bloqueId", rb.getBloque().getId());
                    map.put("nombreBloque", rb.getBloque().getNombre());
                    map.put("orden", rb.getOrden());
                    return map;
                })
                .sorted(Comparator.comparingInt(m -> (Integer) m.get("orden")))
                .collect(Collectors.toList());

        model.addAttribute("bloquesJS", bloquesParaJS);
        model.addAttribute("rutina", rutina);
        model.addAttribute("rutinaActualizar", dto);
        model.addAttribute("rutinaBloques", bloquesDTO);

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

            List<Bloque> bloquesDisponibles = bloqueRepository.findAll();
            List<BloqueResponseDTO> bloquesDTO = bloquesDisponibles.stream()
                    .map(this::mapearBloqueADTO)
                    .collect(Collectors.toList());

            model.addAttribute("rutina", rutina);
            model.addAttribute("rutinaBloques", bloquesDTO);
            return "rutina/editar";
        }

        try {
            Rutina rutina = rutinaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));

            // Actualizar nombre y descripción
            rutina.setNombre(dto.getNombre());
            rutina.setDescripcion(dto.getDescripcion());
            rutinaRepository.save(rutina);

            // Actualizar bloques (agregar, quitar, reordenar)
            rutinaService.actualizarBloques(id, dto.getBloques());

            redirectAttributes.addFlashAttribute("mensaje", "Rutina actualizada correctamente");
            return "redirect:/rutinas/" + id;
        } catch (IllegalArgumentException e) {
            result.reject("error.general", e.getMessage());
            Rutina rutina = rutinaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));

            List<Bloque> bloquesDisponibles = bloqueRepository.findAll();
            List<BloqueResponseDTO> bloquesDTO = bloquesDisponibles.stream()
                    .map(this::mapearBloqueADTO)
                    .collect(Collectors.toList());

            model.addAttribute("rutina", rutina);
            model.addAttribute("rutinaBloques", bloquesDTO);
            return "rutina/editar";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarRutina(@PathVariable Long id) {
        rutinaRepository.deleteById(id);
        return "redirect:/rutinas";
    }

    @GetMapping("/{id}/cargar-progresion")
    public String mostrarFormularioProgresion(@PathVariable Long id, Model model) {
        Rutina rutina = rutinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));

        List<RutinaBloqueEjercicioSemana> semanasExistentes = rutinaBloqueEjercicioSemanaRepository.findByRutinaId(id);

        // Convertir a DTO para enviar al template
        List<RutinaBloqueEjercicioSemanaDTO> semanasDTO = semanasExistentes.stream()
                .map(s -> new RutinaBloqueEjercicioSemanaDTO(
                        s.getRutinaBloque().getId(),
                        s.getBloqueEjercicio().getId(),
                        s.getNumeroSemana(),
                        s.getRepeticiones(),
                        s.getPesoKg()
                ))
                .collect(Collectors.toList());

        CargarProgresionDTO cargarDTO = new CargarProgresionDTO();
        cargarDTO.setSemanas(semanasDTO);

        model.addAttribute("rutina", rutina);
        model.addAttribute("cargarProgresion", cargarDTO);

        return "rutina/cargar-progresion";
    }

    @PostMapping("/{id}/cargar-progresion")
    public String cargarProgresion(
            @PathVariable Long id,
            @Valid @ModelAttribute("cargarProgresion") CargarProgresionDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            Rutina rutina = rutinaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));
            model.addAttribute("rutina", rutina);
            return "rutina/cargar-progresion";
        }

        try {
            rutinaService.cargarProgresionSemanas(id, dto.getSemanas());
            redirectAttributes.addFlashAttribute("mensaje", "Progresión cargada correctamente");
            return "redirect:/rutinas/" + id;
        } catch (IllegalArgumentException e) {
            result.reject("error.general", e.getMessage());
            Rutina rutina = rutinaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));
            model.addAttribute("rutina", rutina);
            return "rutina/cargar-progresion";
        }
    }

    @GetMapping("/{id}/bloque/{bloqueId}/cargar")
    public String mostrarFormularioProgresionBloque(@PathVariable Long id, @PathVariable Long bloqueId, Model model) {
        Rutina rutina = rutinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));

        // Validar que el bloque pertenece a esta rutina
        RutinaBloque rutinaBloque = rutina.getRutinaBloques().stream()
                .filter(rb -> rb.getId().equals(bloqueId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("El bloque no pertenece a esta rutina"));

        // Obtener datos existentes del bloque
        List<RutinaBloqueEjercicioSemana> semanasBloque =
                rutinaBloqueEjercicioSemanaRepository.findByRutinaBloqueId(bloqueId);

        // Convertir a DTO
        List<RutinaBloqueEjercicioSemanaDTO> semanasDTO = semanasBloque.stream()
                .map(s -> new RutinaBloqueEjercicioSemanaDTO(
                        s.getRutinaBloque().getId(),
                        s.getBloqueEjercicio().getId(),
                        s.getNumeroSemana(),
                        s.getRepeticiones(),
                        s.getPesoKg()
                ))
                .collect(Collectors.toList());

        CargarProgresionDTO cargarDTO = new CargarProgresionDTO();
        cargarDTO.setSemanas(semanasDTO);

        model.addAttribute("rutina", rutina);
        model.addAttribute("rutinaBloque", rutinaBloque);
        model.addAttribute("cargarProgresion", cargarDTO);

        return "rutina/cargar-progresion-bloque";
    }

    @PostMapping("/{id}/bloque/{bloqueId}/cargar")
    public ResponseEntity<Map<String, String>> cargarProgresionBloque(
            @PathVariable Long id,
            @PathVariable Long bloqueId,
            @RequestBody CargarProgresionDTO dto
    ) {
        try {
            Rutina rutina = rutinaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));

            // Validar que el bloque pertenece a esta rutina
            rutina.getRutinaBloques().stream()
                    .filter(rb -> rb.getId().equals(bloqueId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("El bloque no pertenece a esta rutina"));

            // Guardar progresión
            rutinaService.cargarProgresionSemanas(id, dto.getSemanas());

            return ResponseEntity.ok(Map.of("mensaje", "Progresión cargada correctamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}