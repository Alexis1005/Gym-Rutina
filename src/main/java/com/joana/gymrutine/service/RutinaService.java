package com.joana.gymrutine.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.joana.gymrutine.dto.rutina.*;
import com.joana.gymrutine.model.*;
import com.joana.gymrutine.repository.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.colors.ColorConstants;

import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RutinaService {

    @Autowired
    private RutinaRepository rutinaRepository;
    @Autowired
    private BloqueRepository bloqueRepository;
    @Autowired
    private RutinaBloqueEjercicioSemanaRepository rutinaBloqueEjercicioSemanaRepository;
    @Autowired
    private AsignacionRutinaRepository asignacionRutinaRepository;
    @Autowired
    private AlumnoRepository alumnoRepository;

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

    public void cargarProgresionSemanas(Long rutinaId, List<RutinaBloqueEjercicioSemanaDTO> semanasDTO) {
        Rutina rutina = rutinaRepository.findById(rutinaId)
                .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));

        // Obtener bloques afectados
        Set<Long> bloquesIds = semanasDTO.stream()
                .map(RutinaBloqueEjercicioSemanaDTO::getRutinaBloqueId)
                .collect(Collectors.toSet());

        //Borrar SOLO esos bloques para actualizar
        for (Long bloqueId : bloquesIds) {
            rutinaBloqueEjercicioSemanaRepository.deleteByRutinaBloqueId(bloqueId);
        }

        // Guardar nuevos datos
        for (RutinaBloqueEjercicioSemanaDTO dto : semanasDTO) {
            // Validar que el bloque existe en esta rutina
            RutinaBloque rutinaBloque = rutina.getRutinaBloques().stream()
                    .filter(rb -> rb.getId().equals(dto.getRutinaBloqueId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "El bloque no pertenece a esta rutina: " + dto.getRutinaBloqueId()
                    ));

            // Validar que el ejercicio existe en este bloque
            BloqueEjercicio bloqueEjercicio = rutinaBloque.getBloque().getBloqueEjercicio().stream()
                    .filter(be -> be.getId().equals(dto.getBloqueEjercicioId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "El ejercicio no pertenece a este bloque: " + dto.getBloqueEjercicioId()
                    ));

            // Crear entidad
            RutinaBloqueEjercicioSemana semana = new RutinaBloqueEjercicioSemana();
            semana.setRutinaBloque(rutinaBloque);
            semana.setBloqueEjercicio(bloqueEjercicio);
            semana.setNumeroSemana(dto.getNumeroSemana());
            semana.setRepeticiones(dto.getRepeticiones());
            semana.setPesoKg(dto.getPesoKg());

            rutinaBloqueEjercicioSemanaRepository.save(semana);
        }
    }

    public void actualizarBloques(Long rutinaId, List<RutinaBloqueActualizarDTO> bloquesDTO) {
        Rutina rutina = rutinaRepository.findById(rutinaId)
                .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));

        // Validar órdenes duplicadas
        Set<Integer> ordenes = new HashSet<>();
        for (RutinaBloqueActualizarDTO b : bloquesDTO) {
            if (!ordenes.add(b.getOrden())) {
                throw new IllegalArgumentException("Orden de bloques duplicado: " + b.getOrden());
            }
        }

        // IDs nuevos
        Set<Long> nuevosIds = bloquesDTO.stream()
                .map(RutinaBloqueActualizarDTO::getBloqueId)
                .collect(Collectors.toSet());

        // 1. ELIMINAR (primero las semanas, luego los bloques)
        List<RutinaBloque> bloquesAEliminar = rutina.getRutinaBloques().stream()
                .filter(rb -> !nuevosIds.contains(rb.getBloque().getId()))
                .collect(Collectors.toList());

        // IMPORTANTE: Eliminar las semanas PRIMERO para respetar foreign keys
        for (RutinaBloque rb : bloquesAEliminar) {
            rutinaBloqueEjercicioSemanaRepository.deleteByRutinaBloqueId(rb.getId());
        }

        // Luego eliminar los bloques de la rutina
        rutina.getRutinaBloques().removeAll(bloquesAEliminar);

        // 2 y 3. ACTUALIZAR o CREAR
        for (RutinaBloqueActualizarDTO bloqueDTO : bloquesDTO) {

            Optional<RutinaBloque> existente = rutina.getRutinaBloques().stream()
                    .filter(rb -> rb.getBloque().getId().equals(bloqueDTO.getBloqueId()))
                    .findFirst();

            if (existente.isPresent()) {
                // ACTUALIZAR
                existente.get().setOrden(bloqueDTO.getOrden());

            } else {
                // CREAR NUEVO
                Bloque bloque = bloqueRepository.findById(bloqueDTO.getBloqueId())
                        .orElseThrow(() -> new IllegalArgumentException("Bloque no existe"));

                RutinaBloque nuevo = new RutinaBloque();
                nuevo.setRutina(rutina);
                nuevo.setBloque(bloque);
                nuevo.setOrden(bloqueDTO.getOrden());

                rutina.getRutinaBloques().add(nuevo);
            }
        }

        // Ordenar (opcional pero recomendable)
        rutina.getRutinaBloques().sort(Comparator.comparing(RutinaBloque::getOrden));

        rutinaRepository.save(rutina);
    }



    //---------------------------------
    //------------PDF------------------
    //---------------------------------
    /**
     * Obtener datos de rutina + alumno para exportar a PDF
     */
    public RutinaPdfDTO obtenerRutinaPdf(Long alumnoId, Long rutinaId) {
        // Validar que existe la asignación alumno-rutina
        boolean asignada = asignacionRutinaRepository.existsByAlumnoIdAndRutinaId(alumnoId, rutinaId);
        if (!asignada) {
            throw new IllegalArgumentException("Esta rutina no está asignada a este alumno");
        }

        // Obtener alumno
        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));

        // Obtener fecha de asignación
        AsignacionRutina asignacion = asignacionRutinaRepository
                .findByAlumnoIdAndRutinaId(alumnoId, rutinaId)
                .orElseThrow(() -> new IllegalArgumentException("Asignación no encontrada"));

        // Obtener rutina con bloques
        Rutina rutina = rutinaRepository.findById(rutinaId)
                .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));

        // Construir DTO
        RutinaPdfDTO dto = new RutinaPdfDTO();
        dto.setNombreAlumno(alumno.getNombreApellido());
        dto.setFechaAsignacion(asignacion.getFechaAsignacion());
        dto.setNombreRutina(rutina.getNombre());
        dto.setObservacionesRutina(rutina.getDescripcion());
        dto.setCantidadSemanas(rutina.getCantidadSemanas());

        // Mapear bloques ordenados
        List<RutinaPdfDTO.BloquePdfDTO> bloques = rutina.getRutinaBloques().stream()
                .sorted(Comparator.comparingInt(RutinaBloque::getOrden))
                .map(rb -> mapearBloquePdf(rb))
                .collect(Collectors.toList());

        dto.setBloques(bloques);

        return dto;
    }

    /**
     * Mapear un RutinaBloque a BloquePdfDTO con sus ejercicios y semanas
     */
    private RutinaPdfDTO.BloquePdfDTO mapearBloquePdf(RutinaBloque rutinaBloque) {
        RutinaPdfDTO.BloquePdfDTO bloqueDto = new RutinaPdfDTO.BloquePdfDTO();
        bloqueDto.setNombreBloque(rutinaBloque.getBloque().getNombre());
        bloqueDto.setOrden(rutinaBloque.getOrden());

        // Mapear ejercicios del bloque
        List<RutinaPdfDTO.EjercicioPdfDTO> ejercicios = rutinaBloque.getBloque().getBloqueEjercicio().stream()
                .map(be -> mapearEjercicioPdf(be, rutinaBloque.getId()))
                .collect(Collectors.toList());

        bloqueDto.setEjercicios(ejercicios);

        return bloqueDto;
    }

    /**
     * Mapear un BloqueEjercicio a EjercicioPdfDTO con sus semanas
     */
    private RutinaPdfDTO.EjercicioPdfDTO mapearEjercicioPdf(BloqueEjercicio bloqueEjercicio, Long rutinaBloqueId) {
        RutinaPdfDTO.EjercicioPdfDTO ejercicioDto = new RutinaPdfDTO.EjercicioPdfDTO();
        ejercicioDto.setNombreEjercicio(bloqueEjercicio.getEjercicio().getNombre());
        ejercicioDto.setSeries(bloqueEjercicio.getSeries());
        ejercicioDto.setDescansoMinutos(bloqueEjercicio.getDescansoMinutos());

        // Obtener semanas de este ejercicio en este bloque
        List<RutinaBloqueEjercicioSemana> semanas = rutinaBloqueEjercicioSemanaRepository
                .findByRutinaBloqueIdAndBloqueEjercicioId(rutinaBloqueId, bloqueEjercicio.getId());

        List<RutinaPdfDTO.SemanaPdfDTO> semanasDto = semanas.stream()
                .sorted(Comparator.comparingInt(RutinaBloqueEjercicioSemana::getNumeroSemana))
                .map(s -> new RutinaPdfDTO.SemanaPdfDTO(
                        s.getNumeroSemana(),
                        s.getRepeticiones(),
                        s.getPesoKg()
                ))
                .collect(Collectors.toList());

        ejercicioDto.setSemanas(semanasDto);

        return ejercicioDto;
    }

    /**
     * Generar PDF con iText7 - VERTICAL (PORTRAIT) CON PAGINACIÓN AUTOMÁTICA
     */
    @Transactional(readOnly = true)
    public byte[] generarPdfRutina(Long alumnoId, Long rutinaId) {
        RutinaPdfDTO datos = obtenerRutinaPdf(alumnoId, rutinaId);

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);

            // A4 vertical
            pdf.setDefaultPageSize(PageSize.A4);

            Document document = new Document(pdf);
            document.setMargins(6, 6, 6, 6);

            // 1. HEADER CON NOMBRE, FECHA Y ESPACIO PARA LOGO
            crearHeader(document, datos);

            // 2. NOMBRE RUTINA Y OBSERVACIONES
            crearSeccionRutina(document, datos);

            // 3. BLOQUES CON EJERCICIOS - CON PAGINACIÓN AUTOMÁTICA
            int bloquesEnPagina = 0;
            final int MAX_BLOQUES_POR_PAGINA = 3;

            for (RutinaPdfDTO.BloquePdfDTO bloque : datos.getBloques()) {
                // Si ya hay 3 bloques en esta página, crear nueva página
                if (bloquesEnPagina >= MAX_BLOQUES_POR_PAGINA) {
                    document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                    bloquesEnPagina = 0;
                }

                crearBloque(document, bloque, datos.getCantidadSemanas());
                bloquesEnPagina++;
            }

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF: " + e.getMessage(), e);
        }
    }

    /**
     * Crear header con logo, nombre alumno y fecha
     */
    private void crearHeader(Document document, RutinaPdfDTO datos) {
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{10, 70, 20}));
        headerTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headerTable.setWidth(UnitValue.createPercentValue(80));

        // Columna 1: Espacio para logo
        Cell logoCell = new Cell();
        logoCell.setBorder(Border.NO_BORDER);
        try {
            URL logoUrl = getClass().getResource("/static/images/JRGym.png");
            if (logoUrl != null) {
                ImageData imageData = ImageDataFactory.create(logoUrl);
                Image logo = new Image(imageData)
                        .scaleToFit(50, 40);  // Tamaño máximo del logo
                logoCell.add(logo);
            } else {
                logoCell.add(new Paragraph("[LOGO]").setFontSize(7).setTextAlignment(TextAlignment.CENTER));
            }
        } catch (Exception e) {
            // Si hay error, mostrar texto
            logoCell.add(new Paragraph("[LOGO]").setFontSize(7).setTextAlignment(TextAlignment.CENTER));
        }
        logoCell.setPadding(2);
        logoCell.setMarginTop(3);
        logoCell.setHeight(25);
        headerTable.addCell(logoCell);

        // Columna 2: Nombre alumno
        Cell nameCell = new Cell();
        nameCell.setBorder(Border.NO_BORDER);
        nameCell.add(new Paragraph(datos.getNombreAlumno())
                .setBold()
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER));
        nameCell.setPadding(2);
        nameCell.setHeight(25);
        headerTable.addCell(nameCell);

        // Columna 3: Fecha
        Cell dateCell = new Cell();
        dateCell.setBorder(Border.NO_BORDER);
        dateCell.add(new Paragraph("Fecha: " + datos.getFechaAsignacion())
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT));
        dateCell.setPadding(2);
        dateCell.setHeight(25);
        headerTable.addCell(dateCell);

        // Aplicar fondo azul al header
        for (IElement element : headerTable.getChildren()) {
            ((Cell) element).setBackgroundColor(new DeviceRgb(41, 128, 185));
            ((Cell) element).setFontColor(ColorConstants.WHITE);
        }

        document.add(headerTable);
        document.add(new Paragraph("").setMarginBottom(2));
    }

    /**
     * Crear sección con nombre rutina y observaciones
     */
    private void crearSeccionRutina(Document document, RutinaPdfDTO datos) {
        Table rutinTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}));
        rutinTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
        rutinTable.setWidth(UnitValue.createPercentValue(80));

        // Nombre rutina
        Cell nameCell = new Cell();
        nameCell.setBorder(Border.NO_BORDER);
        nameCell.add(new Paragraph(datos.getNombreRutina())
                .setBold()
                .setFontSize(12)
                .setMargin(0));
        nameCell.setPadding(1);
        nameCell.setMarginBottom(0);
        nameCell.setMarginTop(0);
        rutinTable.addCell(nameCell);

        // Observaciones
        String observaciones = datos.getObservacionesRutina();

        if (observaciones != null && !observaciones.trim().isEmpty()) {
            Cell obsCell = new Cell();
            obsCell.setBorder(Border.NO_BORDER);
            obsCell.add(new Paragraph("Observaciones")
                    .setBold()
                    .setFontSize(12)
                    .setMargin(0));
            obsCell.add(new Paragraph(datos.getObservacionesRutina() != null ? datos.getObservacionesRutina() : "")
                    .setFontSize(10)
                    .setMargin(0));
            obsCell.setPadding(1);
            obsCell.setMarginBottom(0);
            obsCell.setMarginTop(0);
            rutinTable.addCell(obsCell);

        }
            document.add(rutinTable);
            document.add(new Paragraph(""));
    }

    /**
     * Crear sección de bloque con tabla de ejercicios
     */
    private void crearBloque(Document document, RutinaPdfDTO.BloquePdfDTO bloque, Integer cantidadSemanas) {
        Paragraph titleBloque = new Paragraph("Día " + bloque.getOrden() + ": " + bloque.getNombreBloque())
                .setBold()
                .setFontSize(10)
                .setBackgroundColor(new DeviceRgb(100, 160, 200))
                .setFontColor(ColorConstants.WHITE)
                .setMarginTop(0)
                .setMarginBottom(0)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setWidth(UnitValue.createPercentValue(80))
                .setBorder(new SolidBorder(new DeviceRgb(80, 130, 170), 0.7f));

        document.add(titleBloque);

        int colCount = 3 + (cantidadSemanas * 2);
        float[] columnWidths = new float[colCount];

        columnWidths[0] = 30;
        columnWidths[1] = 8;
        columnWidths[2] = 8;

        for (int i = 3; i < colCount; i += 2) {
            columnWidths[i] = 7;
            columnWidths[i + 1] = 7;
        }

        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(80));
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        table.setHorizontalBorderSpacing(0);
        table.setVerticalBorderSpacing(0);

        // HEADER PRINCIPAL
        table.addHeaderCell(new Cell()
                .add(new Paragraph("Ejercicio").setBold().setFontSize(9).setMargin(0))
                .setBackgroundColor(new DeviceRgb(240, 240, 240))
                .setPadding(2));
        table.addHeaderCell(new Cell()
                .add(new Paragraph("Series").setBold().setFontSize(9).setMargin(0))
                .setBackgroundColor(new DeviceRgb(240, 240, 240))
                .setPadding(2));
        table.addHeaderCell(new Cell()
                .add(new Paragraph("Descanso").setBold().setFontSize(9).setMargin(0))
                .setBackgroundColor(new DeviceRgb(240, 240, 240))
                .setPadding(2));

        for (int i = 1; i <= cantidadSemanas; i++) {
            Cell sHeader = new Cell(1, 2)
                    .add(new Paragraph("S" + i)
                            .setBold()
                            .setFontSize(9)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setMargin(0))
                    .setBackgroundColor(new DeviceRgb(240, 240, 240))
                    .setPadding(2);
            table.addHeaderCell(sHeader);
        }

        // SUBHEADER
        table.addHeaderCell(new Cell()
                .add(new Paragraph("").setMargin(0))
                .setBackgroundColor(new DeviceRgb(220, 220, 220))
                .setPadding(1));
        table.addHeaderCell(new Cell()
                .add(new Paragraph("").setMargin(0))
                .setBackgroundColor(new DeviceRgb(220, 220, 220))
                .setPadding(1));
        table.addHeaderCell(new Cell()
                .add(new Paragraph("").setMargin(0))
                .setBackgroundColor(new DeviceRgb(220, 220, 220))
                .setPadding(1));

        for (int i = 1; i <= cantidadSemanas; i++) {
            table.addHeaderCell(new Cell()
                    .add(new Paragraph("Kg").setBold().setFontSize(7).setMargin(0))
                    .setBackgroundColor(new DeviceRgb(240, 240, 240))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(2));
            table.addHeaderCell(new Cell()
                    .add(new Paragraph("Reps").setBold().setFontSize(7).setMargin(0))
                    .setBackgroundColor(new DeviceRgb(240, 240, 240))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(2));
        }

        // FILAS
        for (RutinaPdfDTO.EjercicioPdfDTO ejercicio : bloque.getEjercicios()) {
            table.addCell(new Cell()
                    .add(new Paragraph(ejercicio.getNombreEjercicio()).setFontSize(8).setMargin(0))
                    .setPadding(2));
            table.addCell(new Cell()
                    .add(new Paragraph(String.valueOf(ejercicio.getSeries())).setFontSize(8).setMargin(0))
                    .setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell()
                    .add(new Paragraph(ejercicio.getDescansoMinutos()).setFontSize(8).setMargin(0))
                    .setTextAlignment(TextAlignment.CENTER));

            for (int i = 1; i <= cantidadSemanas; i++) {
                int finalI = i;
                RutinaPdfDTO.SemanaPdfDTO semana = ejercicio.getSemanas().stream()
                        .filter(s -> s.getNumeroSemana() == finalI)
                        .findFirst()
                        .orElse(null);

                if (semana != null) {
                    table.addCell(new Cell()
                            .add(new Paragraph(semana.getRepeticiones()).setFontSize(6).setMargin(0))
                            .setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell()
                            .add(new Paragraph(semana.getPesoKg()).setFontSize(6).setMargin(0))
                            .setTextAlignment(TextAlignment.CENTER));
                } else {
                    table.addCell(new Cell()
                            .add(new Paragraph("-").setFontSize(6).setMargin(0))
                            .setPadding(1));
                    table.addCell(new Cell()
                            .add(new Paragraph("-").setFontSize(6).setMargin(0))
                            .setPadding(1));
                }
            }
        }

        document.add(table);
        document.add(new Paragraph("").setMarginBottom(7));
    }
}