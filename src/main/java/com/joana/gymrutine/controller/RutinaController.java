package com.joana.gymrutine.controller;

import com.joana.gymrutine.dto.bloque.BloqueEjercicioDTO;
import com.joana.gymrutine.dto.bloque.BloqueResponseDTO;
import com.joana.gymrutine.dto.rutina.RutinaActualizarDTO;
import com.joana.gymrutine.dto.rutina.RutinaCrearDTO;
import com.joana.gymrutine.model.Bloque;
import com.joana.gymrutine.model.Rutina;
import com.joana.gymrutine.repository.BloqueRepository;
import com.joana.gymrutine.repository.RutinaRepository;
import com.joana.gymrutine.service.RutinaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
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

        model.addAttribute("rutina", rutina);

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

        RutinaActualizarDTO dto =  new RutinaActualizarDTO();
        dto.setNombre(rutina.getNombre());
        dto.setDescripcion(rutina.getDescripcion());

        model.addAttribute("rutina", rutina);
        model.addAttribute("rutinaActualizar", dto);
        return "rutina/editar";
    }

    @PostMapping("/editar/{id}")
    public String actualizarRutina(@PathVariable Long id,
                                   @Valid @ModelAttribute("rutinaActualizar") RutinaActualizarDTO dto,
                                   BindingResult result, Model model,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Rutina rutina = rutinaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));
            model.addAttribute("rutina", rutina);
            return "rutina/editar";
        }

        try {
            Rutina rutina = rutinaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));

            rutina.setNombre(dto.getNombre());
            rutina.setDescripcion(dto.getDescripcion());

            rutinaRepository.save(rutina);

            return "redirect:/rutinas/" + id;
        } catch (IllegalArgumentException e) {
            result.reject("error.general", e.getMessage());
            Rutina rutina = rutinaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));
            model.addAttribute("rutina", rutina);
            return "rutina/editar";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarRutina(@PathVariable Long id) {
        rutinaRepository.deleteById(id);
        return "redirect:/rutinas";
    }
}