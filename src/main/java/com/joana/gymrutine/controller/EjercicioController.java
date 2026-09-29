package com.joana.gymrutine.controller;

import com.joana.gymrutine.dto.ejercicio.EjercicioActualizarDTO;
import com.joana.gymrutine.dto.ejercicio.EjercicioCrearDTO;
import com.joana.gymrutine.dto.ejercicio.EjercicioResponseDTO;
import com.joana.gymrutine.model.Ejercicio;
import com.joana.gymrutine.model.enums.*;
import com.joana.gymrutine.service.EjercicioService;
import com.joana.gymrutine.service.GrupoMuscularService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
        agregarEnumsAlModelo(model);
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

        var dto = new EjercicioActualizarDTO();
        dto.setNombre(ejercicio.getNombre());
        dto.setDescripcion(ejercicio.getDescripcion());
        dto.setGrupoMuscularId(ejercicio.getGrupoMuscular().getId());
        dto.setTipoArticular(ejercicio.getTipoArticular());
        dto.setCadenaCinetica(ejercicio.getCadenaCinetica());
        dto.setLateralidad(ejercicio.getLateralidad());
        dto.setElemento(ejercicio.getElemento());
        dto.setPosicion(ejercicio.getPosicion());

        model.addAttribute("ejercicioDTO", dto);
        model.addAttribute("id", id);
        model.addAttribute("gruposMusculares", grupoMuscularService.listar()); //---> Para el select
        agregarEnumsAlModelo(model);
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

    @GetMapping("/por-grupo/{grupoId}")
    public ResponseEntity<List<Ejercicio>> porGrupo(@PathVariable Long grupoId) {
        var ejercicios = ejercicioService.listarPorGrupo(grupoId);
        return ResponseEntity.ok(ejercicios);
    }

    /**
     * Búsqueda combinada para el selector de ejercicios al armar una rutina.
     * Todos los filtros son opcionales.
     */
    @GetMapping("/buscar")
    @ResponseBody
    public ResponseEntity<List<EjercicioResponseDTO>> buscar(
            @RequestParam(required = false) Long grupoMuscularId,
            @RequestParam(required = false) TipoArticular tipoArticular,
            @RequestParam(required = false) CadenaCinetica cadenaCinetica,
            @RequestParam(required = false) Lateralidad lateralidad,
            @RequestParam(required = false) Elemento elemento,
            @RequestParam(required = false) Posicion posicion) {

        var ejercicios = ejercicioService.buscarConFiltros(
                grupoMuscularId, tipoArticular, cadenaCinetica, lateralidad, elemento, posicion);
        return ResponseEntity.ok(ejercicios);
    }

    private void agregarEnumsAlModelo(Model model) {
        model.addAttribute("tiposArticulares", TipoArticular.values());
        model.addAttribute("cadenasCineticas", CadenaCinetica.values());
        model.addAttribute("lateralidades", Lateralidad.values());
        model.addAttribute("elementos", Elemento.values());
        model.addAttribute("posiciones", Posicion.values());
    }
}