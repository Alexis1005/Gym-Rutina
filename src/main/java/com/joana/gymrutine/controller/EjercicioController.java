package com.joana.gymrutine.controller;

import com.joana.gymrutine.dto.ejercicio.EjercicioActualizarDTO;
import com.joana.gymrutine.dto.ejercicio.EjercicioCrearDTO;
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
@RequestMapping("/ejercicios")
public class EjercicioController {

    @Autowired
    private EjercicioService ejercicioService;

    @Autowired
    private GrupoMuscularService grupoMuscularService;

    @GetMapping
    public String listar(Model model) {
        var ejercicios = ejercicioService.listarTodos();
        model.addAttribute("ejercicios", ejercicios);
        return "ejercicios/listar";
    }

    @GetMapping("/crear")
    public String mostrarFormulario(Model model) {
        model.addAttribute("ejercicioDTO", new EjercicioCrearDTO());
        model.addAttribute("gruposMusculares", grupoMuscularService.listar());
        return "ejercicios/crear";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("ejercicioDTO")EjercicioCrearDTO ejercicioDTO,
                        BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "ejercicios/crear";
        }
        ejercicioService.crear(ejercicioDTO);
        attributes.addFlashAttribute("mensaje", "Ejercicio guardado correctamente");
        return "redirect:/ejercicios";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormulario(@PathVariable Long id, Model model) {

        var ejercicio = ejercicioService.listarPorId(id);
        model.addAttribute("ejercicioDTO", new EjercicioActualizarDTO(
                ejercicio.getNombre(),
                ejercicio.getDescripcion(),
                ejercicio.getGrupoMuscular().getId()
        ));
        model.addAttribute("id", id);
        model.addAttribute("gruposMusculares", grupoMuscularService.listar()); //---> Para el select
        return "ejercicios/editar";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("ejercicioDTO") EjercicioActualizarDTO dto,
                             BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "ejercicios/editar";
        }
        ejercicioService.actualizar(id, dto);
        attributes.addFlashAttribute("mensaje", "Ejercicio actualizado correctamente");
        return "redirect:/ejercicios";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes attributes) {
        ejercicioService.eliminar(id);
        attributes.addFlashAttribute("mensaje", "Ejercicio eliminado.");
        return "redirect:/ejercicios";
    }
}
