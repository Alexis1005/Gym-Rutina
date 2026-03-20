package com.joana.gymrutine.controller;

import com.joana.gymrutine.dto.grupoMuscular.GrupoMuscularActualizarDTO;
import com.joana.gymrutine.dto.grupoMuscular.GrupoMuscularCrearDTO;
import com.joana.gymrutine.service.GrupoMuscularService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/grupos-musculares")
public class GrupoMuscularController {

    @Autowired
    private GrupoMuscularService service;

    @GetMapping
    public String listar(Model model){   //MODEL: OBJETO QUE LLEVA DATOS AL HTML
        var grupos = service.listar();
        model.addAttribute("grupos", grupos);  //.addAttribute = METE LA LISTA DE GRUPOS EN EL OBJETO, CON LA LLAVE "grupos"
        return "grupos-musculares/listar";  //NOMBRE DEL ARCHIVO HTML
    }

    @GetMapping("/crear")
    public String mostrarFormulario(Model model){  //PREPARA DATOS PARA ENVIAR AL HTML
        model.addAttribute("grupoDTO", new GrupoMuscularCrearDTO()); //CREA GRUPOMUSCULAR VACÍO Y LO AGREGA A LA LLAVE grupoDTO, el html lo recibe y crea form vacío
        return  "grupos-musculares/crear";  //BUSCA EL ARCHIVO crear.html
    }



    /*
    * @Valid = Valida automáticamente con @NotBlank, @Size
      @ModelAttribute = Vincula datos del formulario al DTO
      grupoMuscularCrearDTO grupoDTO = recibe el objeto con los datos
    */
    @PostMapping
    public String crear(@Valid @ModelAttribute("grupoDTO")  GrupoMuscularCrearDTO grupoDTO,
                        BindingResult result, RedirectAttributes  attributes){
        if (result.hasErrors()){  //SI HAY ERRORES DE VALIDACIÓN, TIENE EL ERROR
            return "grupos-musculares/crear";
        }
        service.crear(grupoDTO);
        attributes.addFlashAttribute("mensaje", "Grupo creado exitosamente."); //crea msj temporal y redirige a la página para ver los guardados
        return  "redirect:/grupos-musculares";
    }


    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model){

        var grupo = service.listarPorId(id);
        model.addAttribute("grupoDTO", new GrupoMuscularActualizarDTO(grupo.getNombre()));  //CREA UN DTO CON EL NOMBRE ACTUAL PARA QUE LO ACTUALICE
        model.addAttribute("id", id); //PARA SABER QUE ID ACTUALIZAR

        return "grupos-musculares/editar";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id, @Valid @ModelAttribute("grupoDTO") GrupoMuscularActualizarDTO dto,
                             BindingResult result, RedirectAttributes attributes){

        if (result.hasErrors()){
            return "grupos-musculares/editar";
        }

        service.actualizar(id, dto);
        attributes.addFlashAttribute("mensaje", "Grupo actualizado exitosamente.");

        return "redirect:/grupos-musculares";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes attributes){

        service.eliminar(id);
        attributes.addFlashAttribute("mensaje", "Grupo eliminado exitosamente.");
        return "redirect:/grupos-musculares";
    }
}
