package com.joana.gymrutine.service;

import com.joana.gymrutine.dto.rutina.RutinaBloqueDTO;
import com.joana.gymrutine.dto.rutina.RutinaCrearDTO;
import com.joana.gymrutine.model.Bloque;
import com.joana.gymrutine.model.Rutina;
import com.joana.gymrutine.model.RutinaBloque;
import com.joana.gymrutine.repository.BloqueEjercicioRepository;
import com.joana.gymrutine.repository.BloqueRepository;
import com.joana.gymrutine.repository.EjercicioRepository;
import com.joana.gymrutine.repository.RutinaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class RutinaService {

    @Autowired
    private RutinaRepository rutinaRepository;
    @Autowired
    private BloqueRepository bloqueRepository;
    @Autowired
    private BloqueEjercicioRepository bloqueEjercicioRepository;
    @Autowired
    private EjercicioRepository ejercicioRepository;

    public Rutina crearRutina(RutinaCrearDTO dto) {
        if (dto.getCantidadSemanas() == null || dto.getCantidadSemanas() <= 0) {
            throw new IllegalArgumentException("Debe ingresar un numero de semanas");
        }
        if (dto.getBloques() == null || dto.getBloques().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar un numero de bloques");
        }

        Set<Integer> ordenes = new HashSet<>();

        for (RutinaBloqueDTO b : dto.getBloques()) {
            if (b.getOrden() == null) {
                throw new IllegalArgumentException("Todos los bloques deben tener orden");
            }
            if (!ordenes.add(b.getOrden())) {
                throw new IllegalArgumentException("Orden de bloques duplicado");
            }
        }


        Rutina rutina = new Rutina();
        rutina.setNombre(dto.getNombre());
        rutina.setDescripcion(dto.getDescripcion());
        rutina.setCantidadSemanas(dto.getCantidadSemanas());

        List<RutinaBloque> rutinasBloques = new ArrayList<>();
        for (RutinaBloqueDTO bloqueDTO : dto.getBloques()){
            Bloque bloque = bloqueRepository.findById(bloqueDTO.getBloqueId())
                    .orElseThrow(() -> new IllegalArgumentException("El bloque no se encuentra"));

            RutinaBloque rutinaBloque = new RutinaBloque();
            rutinaBloque.setRutina(rutina);
            rutinaBloque.setBloque(bloque);
            rutinaBloque.setOrden(bloqueDTO.getOrden());
            rutinasBloques.add(rutinaBloque);
        }
        rutinasBloques.sort(Comparator.comparing(RutinaBloque::getOrden));

        rutina.setRutinaBloques(rutinasBloques);

        return rutinaRepository.save(rutina);
    }
}
