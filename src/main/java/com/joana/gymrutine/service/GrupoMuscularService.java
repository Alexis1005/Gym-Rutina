package com.joana.gymrutine.service;

import com.joana.gymrutine.dto.grupoMuscular.GrupoMuscularActualizarDTO;
import com.joana.gymrutine.dto.grupoMuscular.GrupoMuscularCrearDTO;
import com.joana.gymrutine.dto.grupoMuscular.GrupoMuscularResponseDTO;
import com.joana.gymrutine.exception.DuplicateEntityException;
import com.joana.gymrutine.exception.EntityNotDeletableException;
import com.joana.gymrutine.model.GrupoMuscular;
import com.joana.gymrutine.repository.GrupoMuscularRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class GrupoMuscularService {

    @Autowired
    private GrupoMuscularRepository grupoMuscularRepository;


    /**
     * Crear un nuevo GrupoMuscular
     *
     * @param dto
     * @return GrupoMuscular creado
     * @throws IllegalArgumentException si nombre está vacío o ya existe
     */

    public GrupoMuscular crear(GrupoMuscularCrearDTO dto) {

        //validacion por duplicado en la bdd
        if (grupoMuscularRepository.existsByNombreIgnoreCase(dto.getNombre().trim())) {
            throw new DuplicateEntityException("El nombre del grupo muscular existe!");
        }

        //crea el objeto
        GrupoMuscular grupoMuscular = new GrupoMuscular();
        grupoMuscular.setNombre(dto.getNombre().trim());

        //guardar en la bdd
        return grupoMuscularRepository.save(grupoMuscular);
    }

    /*
     *-------------------------------------------------------------------
     * LISTAR TODOS
     *-------------------------------------------------------------------
     */
    public List<GrupoMuscular> listar() {
        return grupoMuscularRepository.findAll();
    }

    /*
     *-------------------------------------------------------------------
     * LISTAR POR ID
     *-------------------------------------------------------------------
     */

    public GrupoMuscular listarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido!");
        }
        return grupoMuscularRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Grupo muscular no encontrado con id: " + id));
    }


    /*
     *-------------------------------------------------------------------
     * LISTAR POR NOMBRE
     *-------------------------------------------------------------------
     */

    public GrupoMuscular listarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del grupo muscular es obligatorio!");
        }
        return grupoMuscularRepository.findByNombre(nombre.trim())
                .orElseThrow(() -> new IllegalArgumentException("Grupo muscular no encontrado con el nombre: " + nombre));
    }

    /*
     * ___________________________________________________________________
     * ACTUALIZAR
     * -------------------------------------------------------------------
     * */

    public GrupoMuscular actualizar(Long id, GrupoMuscularActualizarDTO dto) {
        var grupoMuscular = grupoMuscularRepository.findById(id);
        if (!grupoMuscular.isPresent()) {
            throw new IllegalArgumentException("Grupo muscular no encontrado con id: " + id);
        }

        var grupoMuscularActualizado = grupoMuscular.get();
        grupoMuscularActualizado.setNombre(dto.getNombre().trim());
        return grupoMuscularRepository.save(grupoMuscularActualizado);
    }

    /*
     * ___________________________________________________________________
     * ELIMINAR
     * -------------------------------------------------------------------
     * */

    public void eliminar(Long id) {
        var grupoEncontrado = grupoMuscularRepository.findById(id);
        if (!grupoEncontrado.isPresent()) {
            throw new IllegalArgumentException("Grupo muscular no encontrado con id: " + id);
        }

        //VALIDAR QUE NO TENGA EJERCICIOS
        if (grupoEncontrado.get().getEjercicios() != null && !grupoEncontrado.get().getEjercicios().isEmpty()) {
            int cantEjercicios = grupoEncontrado.get().getEjercicios().size();
            throw new EntityNotDeletableException(
                    "No se puede eliminar el grupo muscular. Tiene " + cantEjercicios +
                            " ejercicio" + (cantEjercicios != 1 ? "s" : "") + " asociado" +
                            (cantEjercicios != 1 ? "s" : "") + "."
            );
        }
        grupoMuscularRepository.deleteById(id);
    }
}
