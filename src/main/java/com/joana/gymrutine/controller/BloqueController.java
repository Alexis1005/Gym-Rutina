package com.joana.gymrutine.controller;

import com.joana.gymrutine.dto.bloque.BloqueActualizarDTO;
import com.joana.gymrutine.dto.bloque.BloqueCrearDTO;
import com.joana.gymrutine.dto.bloque.BloqueEjercicioDTO;
import com.joana.gymrutine.service.BloqueService;
import com.joana.gymrutine.service.EjercicioService;
import com.joana.gymrutine.service.GrupoMuscularService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/bloques")
public class BloqueController {

    @Autowired
    private EjercicioService ejercicioService;

    @Autowired
    private BloqueService bloqueService;

    @Autowired
    private GrupoMuscularService grupoMuscularService;


    @GetMapping
    public String listar(Model model) {
        var bloques = bloqueService.listar();
        model.addAttribute("bloques", bloques);
        return "bloques/listar";
    }

    @GetMapping("/crear")
    public String mostrarFormulario(Model model) {
        model.addAttribute("bloqueDTO", new BloqueCrearDTO());
        model.addAttribute("gruposMusculares", grupoMuscularService.listar());
        model.addAttribute("ejerciciosDisponibles", ejercicioService.listarDTO());
        return "bloques/crear";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("bloqueDTO") BloqueCrearDTO dto,
                        BindingResult bindingResult, RedirectAttributes attributes, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("gruposMusculares", grupoMuscularService.listar());
            return "bloques/crear";
        }

        bloqueService.crear(dto);
        attributes.addFlashAttribute("mensaje", "Bloque creado.");
        return "redirect:/bloques";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormulario(@PathVariable("id") Long id, Model model) {
        var bloque = bloqueService.listarPorId(id);

        var ejerciciosDTO = bloque.getBloqueEjercicio().stream()
                .map(be -> new BloqueEjercicioDTO(
                        be.getEjercicio().getId(),
                        be.getEjercicio().getNombre(),
                        be.getSeries(),
                        be.getDescansoMinutos()
                ))
                .toList();
        var dto = new BloqueActualizarDTO(
                bloque.getNombre(),
                ejerciciosDTO);
        var grupos = grupoMuscularService.listar();
        var ejercicios = ejercicioService.listarDTO();
        System.out.println("ejerciciosDisponibles size: " + ejercicios.size());
        model.addAttribute("bloqueDTO", dto);
        model.addAttribute("gruposMusculares", grupos);
        model.addAttribute("ejerciciosDisponibles", ejercicios);
        model.addAttribute("id", id);

        return "bloques/editar";
    }

    @PostMapping("/{id}")
    public String actualizar(@Valid BloqueActualizarDTO dto, @PathVariable Long id,
                             BindingResult result, RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("gruposMusculares", grupoMuscularService.listar());
            model.addAttribute("ejerciciosDisponibles", ejercicioService.listarDTO());
            return "bloques/editar";
        }

        try {
            bloqueService.actualizar(id, dto);
            attributes.addFlashAttribute("mensaje", "Bloque actualizado.");
            return "redirect:/bloques";
        } catch (IllegalArgumentException e) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("gruposMusculares", grupoMuscularService.listar());
        model.addAttribute("ejerciciosDisponibles", ejercicioService.listarDTO());
        model.addAttribute("bloqueDTO", dto); // faltaba esto
        model.addAttribute("id", id);
        return "bloques/editar";
    }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes attributes) {
        bloqueService.eliminar(id);
        attributes.addFlashAttribute("mensaje", "Bloque eliminado.");
        return "redirect:/bloques";
    }

    @PostMapping("/{id}/nuevo")
    public String guardarComoNuevo(@Valid @ModelAttribute("bloqueDTO") BloqueActualizarDTO dto,
                                   @PathVariable Long id,
                                   BindingResult result, RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("gruposMusculares", grupoMuscularService.listar());
            model.addAttribute("ejerciciosDisponibles", ejercicioService.listarDTO());
            model.addAttribute("id", id);
            return "bloques/editar";
        }

        var bloqueOriginal = bloqueService.listarPorId(id);

        if (dto.getNombre().trim().equalsIgnoreCase(bloqueOriginal.getNombre())) {
            model.addAttribute("error", "Para guardar como nuevo bloque, primero cambiá el nombre.");
            model.addAttribute("gruposMusculares", grupoMuscularService.listar());
            model.addAttribute("ejerciciosDisponibles", ejercicioService.listarDTO());
            model.addAttribute("bloqueDTO", dto);
            model.addAttribute("id", id);
            return "bloques/editar";
        }

        try {
            var nuevoDto = new BloqueCrearDTO(dto.getNombre().trim(), dto.getEjercicios(), null);
            bloqueService.crear(nuevoDto);
            attributes.addFlashAttribute("mensaje",
                    "Nuevo bloque creado a partir de \"" + bloqueOriginal.getNombre() + "\".");
            return "redirect:/bloques";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("gruposMusculares", grupoMuscularService.listar());
            model.addAttribute("ejerciciosDisponibles", ejercicioService.listarDTO());
            model.addAttribute("bloqueDTO", dto);
            model.addAttribute("id", id);
            return "bloques/editar";
        }
    }
}
