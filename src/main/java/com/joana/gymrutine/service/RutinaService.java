package com.joana.gymrutine.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;
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

    // Paleta del PDF
    private static final DeviceRgb AZUL_MARINO  = new DeviceRgb(27, 58, 87);    // #1B3A57
    private static final DeviceRgb AZUL_DIA     = new DeviceRgb(47, 110, 163);  // #2F6EA3
    private static final DeviceRgb AZUL_BANDA   = new DeviceRgb(220, 232, 243); // #DCE8F3
    private static final DeviceRgb GRIS_AZULADO = new DeviceRgb(241, 244, 248); // #F1F4F8
    private static final DeviceRgb ROJO_LOGO    = new DeviceRgb(214, 40, 57);   // #D62839
    private static final DeviceRgb TEXTO        = new DeviceRgb(26, 26, 26);    // #1A1A1A
    private static final float ANCHO_PDF = 95f; // % del ancho útil de la hoja

    @Autowired
    private RutinaRepository rutinaRepository;
    @Autowired
    private EjercicioRepository ejercicioRepository;
    @Autowired
    private RutinaEjercicioRepository rutinaEjercicioRepository;
    @Autowired
    private AsignacionRutinaRepository asignacionRutinaRepository;
    @Autowired
    private AlumnoRepository alumnoRepository;

    //---------------------------------
    //------------CREAR-----------------
    //---------------------------------
    public Rutina crearRutina(RutinaCrearDTO dto) {
        if (dto.getCantidadSemanas() == null || dto.getCantidadSemanas() <= 0) {
            throw new IllegalArgumentException("Debe ingresar un numero de semanas");
        }
        if (dto.getCantidadDias() == null || dto.getCantidadDias() <= 0) {
            throw new IllegalArgumentException("Debe ingresar un numero de dias");
        }
        if (dto.getEjercicios() == null || dto.getEjercicios().isEmpty()) {
            throw new IllegalArgumentException("Debe agregar al menos un ejercicio");
        }

        Rutina rutina = new Rutina();
        rutina.setNombre(dto.getNombre());
        rutina.setDescripcion(dto.getDescripcion());
        rutina.setCantidadSemanas(dto.getCantidadSemanas());
        rutina.setCantidadDias(dto.getCantidadDias());

        List<RutinaEjercicio> rutinaEjercicios = construirRutinaEjercicios(
                dto.getEjercicios(), rutina, dto.getCantidadDias(), dto.getCantidadSemanas());

        rutina.setRutinaEjercicios(rutinaEjercicios);

        return rutinaRepository.save(rutina);
    }

    //---------------------------------
    //---------ACTUALIZAR (reemplazo total)
    //---------------------------------
    public Rutina actualizarRutina(Long id, RutinaActualizarDTO dto) {
        Rutina rutina = rutinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));

        if (dto.getEjercicios() == null || dto.getEjercicios().isEmpty()) {
            throw new IllegalArgumentException("Debe mantener al menos un ejercicio");
        }

        rutina.setNombre(dto.getNombre());
        rutina.setDescripcion(dto.getDescripcion());

        // Reemplazo completo: se borra todo lo anterior y se recrea con lo que llega
        rutina.getRutinaEjercicios().clear();

        List<RutinaEjercicio> nuevos = construirRutinaEjercicios(
                dto.getEjercicios(), rutina, rutina.getCantidadDias(), rutina.getCantidadSemanas());

        rutina.getRutinaEjercicios().addAll(nuevos);

        return rutinaRepository.save(rutina);
    }

    //---------------------------------
    //---------LÓGICA COMPARTIDA CREAR/ACTUALIZAR
    //---------------------------------
    private List<RutinaEjercicio> construirRutinaEjercicios(
            List<RutinaEjercicioDTO> ejerciciosDTO, Rutina rutina, Integer cantidadDias, Integer cantidadSemanas) {

        // Validar orden duplicado DENTRO del mismo día
        Map<Integer, Set<Integer>> ordenesPorDia = new HashMap<>();

        List<RutinaEjercicio> resultado = new ArrayList<>();

        for (RutinaEjercicioDTO ejDto : ejerciciosDTO) {

            if (ejDto.getDia() > cantidadDias) {
                throw new IllegalArgumentException(
                        "El dia " + ejDto.getDia() + " supera la cantidad de dias de la rutina (" + cantidadDias + ")");
            }

            Set<Integer> ordenesDelDia = ordenesPorDia.computeIfAbsent(ejDto.getDia(), d -> new HashSet<>());
            if (!ordenesDelDia.add(ejDto.getOrden())) {
                throw new IllegalArgumentException(
                        "Orden duplicado en el dia " + ejDto.getDia() + ": " + ejDto.getOrden());
            }

            Ejercicio ejercicio = ejercicioRepository.findById(ejDto.getEjercicioId())
                    .orElseThrow(() -> new IllegalArgumentException("El ejercicio no existe: " + ejDto.getEjercicioId()));

            RutinaEjercicio rutinaEjercicio = new RutinaEjercicio();
            rutinaEjercicio.setRutina(rutina);
            rutinaEjercicio.setEjercicio(ejercicio);
            rutinaEjercicio.setDia(ejDto.getDia());
            rutinaEjercicio.setOrden(ejDto.getOrden());

            List<RutinaEjercicioSemana> semanas = new ArrayList<>();
            Set<Integer> semanasVistas = new HashSet<>();

            for (RutinaEjercicioSemanaDTO semDto : ejDto.getSemanas()) {

                if (semDto.getSemana() > cantidadSemanas) {
                    throw new IllegalArgumentException(
                            "La semana " + semDto.getSemana() + " supera la cantidad de semanas de la rutina (" + cantidadSemanas + ")");
                }
                if (!semanasVistas.add(semDto.getSemana())) {
                    throw new IllegalArgumentException(
                            "Semana duplicada para el ejercicio " + ejercicio.getNombre() + ": " + semDto.getSemana());
                }

                RutinaEjercicioSemana semana = new RutinaEjercicioSemana();
                semana.setRutinaEjercicio(rutinaEjercicio);
                semana.setSemana(semDto.getSemana());
                semana.setSeries(semDto.getSeries());
                semana.setRepeticiones(semDto.getRepeticiones());
                semana.setPesoKg(semDto.getPesoKg());
                semana.setDescansoMinutos(semDto.getDescansoMinutos());
                semana.setRir(semDto.getRir());
                semana.setCadencia(semDto.getCadencia());
                semana.setMetodo(semDto.getMetodo());

                semanas.add(semana);
            }

            rutinaEjercicio.setSemanas(semanas);
            resultado.add(rutinaEjercicio);
        }

        return resultado;
    }

    //---------------------------------
    //------------ELIMINAR--------------
    //---------------------------------
    public void eliminar(Long id) {
        Rutina rutina = rutinaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                "Rutina no encontrada con ID: " + id
        ));

        List<AsignacionRutina> asignaciones = asignacionRutinaRepository.findByRutinaId(id);
        if (!asignaciones.isEmpty()) {
            throw new IllegalArgumentException("No puedes eliminar una rutina asignada a " + asignaciones.size() + " alumnos!");
        }

        // cascade = ALL + orphanRemoval en Rutina->RutinaEjercicio->RutinaEjercicioSemana
        // se encargan de borrar todo lo dependiente automáticamente
        rutinaRepository.delete(rutina);
    }

    //---------------------------------
    //------------PDF------------------
    //---------------------------------
    public RutinaPdfDTO obtenerRutinaPdf(Long alumnoId, Long rutinaId) {
        boolean asignada = asignacionRutinaRepository.existsByAlumnoIdAndRutinaId(alumnoId, rutinaId);
        if (!asignada) {
            throw new IllegalArgumentException("Esta rutina no está asignada a este alumno");
        }

        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));

        AsignacionRutina asignacion = asignacionRutinaRepository
                .findByAlumnoIdAndRutinaId(alumnoId, rutinaId)
                .orElseThrow(() -> new IllegalArgumentException("Asignación no encontrada"));

        Rutina rutina = rutinaRepository.findById(rutinaId)
                .orElseThrow(() -> new IllegalArgumentException("Rutina no encontrada"));

        RutinaPdfDTO dto = new RutinaPdfDTO();
        dto.setNombreAlumno(alumno.getNombreApellido());
        dto.setFechaAsignacion(asignacion.getFechaAsignacion());
        dto.setNombreRutina(rutina.getNombre());
        dto.setObservacionesRutina(rutina.getDescripcion());
        dto.setCantidadSemanas(rutina.getCantidadSemanas());

        // Agrupar ejercicios por día
        Map<Integer, List<RutinaEjercicio>> porDia = rutina.getRutinaEjercicios().stream()
                .collect(Collectors.groupingBy(RutinaEjercicio::getDia));

        List<RutinaPdfDTO.DiaPdfDTO> dias = porDia.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> mapearDiaPdf(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        dto.setDias(dias);

        return dto;
    }

    private RutinaPdfDTO.DiaPdfDTO mapearDiaPdf(Integer dia, List<RutinaEjercicio> ejerciciosDelDia) {
        RutinaPdfDTO.DiaPdfDTO diaDto = new RutinaPdfDTO.DiaPdfDTO();
        diaDto.setDia(dia);

        List<RutinaPdfDTO.EjercicioPdfDTO> ejercicios = ejerciciosDelDia.stream()
                .sorted(Comparator.comparingInt(RutinaEjercicio::getOrden))
                .map(this::mapearEjercicioPdf)
                .collect(Collectors.toList());

        diaDto.setEjercicios(ejercicios);
        return diaDto;
    }

    private RutinaPdfDTO.EjercicioPdfDTO mapearEjercicioPdf(RutinaEjercicio rutinaEjercicio) {
        RutinaPdfDTO.EjercicioPdfDTO ejercicioDto = new RutinaPdfDTO.EjercicioPdfDTO();
        ejercicioDto.setNombreEjercicio(rutinaEjercicio.getEjercicio().getNombre());
        ejercicioDto.setOrden(rutinaEjercicio.getOrden());

        List<RutinaPdfDTO.SemanaPdfDTO> semanas = rutinaEjercicio.getSemanas().stream()
                .sorted(Comparator.comparingInt(RutinaEjercicioSemana::getSemana))
                .map(s -> new RutinaPdfDTO.SemanaPdfDTO(
                        s.getSemana(),
                        s.getSeries(),
                        s.getRepeticiones(),
                        s.getPesoKg(),
                        s.getDescansoMinutos(),
                        s.getRir()
                ))
                .collect(Collectors.toList());

        ejercicioDto.setSemanas(semanas);
        return ejercicioDto;
    }

    /**
     * Escribe el nombre del alumno en vertical sobre el margen izquierdo de la primera página,
     * para que se lea al guardar la hoja doblada en un fichero.
     */
    private void agregarNombreEnMargen(Document document, PdfDocument pdf, String nombreAlumno) {
        float anchoPagina = PageSize.A4.rotate().getWidth();
        float anchoUtil = anchoPagina - 12;                       // márgenes de 6 + 6
        float anchoTabla = anchoUtil * ANCHO_PDF / 100f;
        float espacioIzquierdo = 6 + (anchoUtil - anchoTabla) / 2;
        float x = espacioIzquierdo / 2;                           // centrado en el espacio libre

        float alto = pdf.getPage(1).getPageSize().getHeight();

        Paragraph nombre = new Paragraph(nombreAlumno)
                .setBold()
                .setFontSize(11)
                .setFontColor(AZUL_MARINO);

        document.showTextAligned(nombre, x, alto / 2, 1,
                TextAlignment.CENTER, VerticalAlignment.MIDDLE,
                (float) (Math.PI / 2));                           // 90°: se lee de abajo hacia arriba
    }

    /**
     * Generar PDF con iText7 - HORIZONTAL (LANDSCAPE) CON PAGINACIÓN AUTOMÁTICA
     */
    @Transactional(readOnly = true)
    public byte[] generarPdfRutina(Long alumnoId, Long rutinaId) {
        RutinaPdfDTO datos = obtenerRutinaPdf(alumnoId, rutinaId);

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);

            // A4 horizontal
            pdf.setDefaultPageSize(PageSize.A4.rotate());

            // Sin vaciado inmediato, para poder escribir sobre la página ya armada
            Document document = new Document(pdf, pdf.getDefaultPageSize(), false);
            document.setMargins(6, 6, 6, 6);

            // 1. HEADER CON NOMBRE, FECHA Y ESPACIO PARA LOGO
            crearHeader(document, datos);

            // 2. NOMBRE RUTINA Y OBSERVACIONES
            crearSeccionRutina(document, datos);

            // 3. DÍAS CON EJERCICIOS - CON PAGINACIÓN AUTOMÁTICA
            int diasEnPagina = 0;
            final int MAX_DIAS_POR_PAGINA = 2;

            for (RutinaPdfDTO.DiaPdfDTO dia : datos.getDias()) {
                if (diasEnPagina >= MAX_DIAS_POR_PAGINA) {
                    document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                    diasEnPagina = 0;
                }

                crearDia(document, dia, datos.getCantidadSemanas());
                diasEnPagina++;
            }

            // 4. NOMBRE DEL ALUMNO EN EL MARGEN IZQUIERDO (solo primera página)
            agregarNombreEnMargen(document, pdf, datos.getNombreAlumno());

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
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{14, 66, 20}));
        headerTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headerTable.setWidth(UnitValue.createPercentValue(ANCHO_PDF));

        // Columna 1: Espacio para logo
        Cell logoCell = new Cell();
        logoCell.setBorder(Border.NO_BORDER);
        try {
            URL logoUrl = getClass().getResource("/static/images/JRGym.png");
            if (logoUrl != null) {
                ImageData imageData = ImageDataFactory.create(logoUrl);
                Image logo = new Image(imageData)
                        .scaleToFit(60, 40);
                logoCell.add(logo);
            } else {
                logoCell.add(new Paragraph("[LOGO]").setFontSize(7).setTextAlignment(TextAlignment.CENTER));
            }
        } catch (Exception e) {
            logoCell.add(new Paragraph("[LOGO]").setFontSize(7).setTextAlignment(TextAlignment.CENTER));
        }
        logoCell.setPadding(2);
        logoCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        logoCell.setHeight(40);
        headerTable.addCell(logoCell);

        // Columna 2: Nombre alumno
        Cell nameCell = new Cell();
        nameCell.setBorder(Border.NO_BORDER);
        nameCell.add(new Paragraph(datos.getNombreAlumno())
                .setBold()
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER));
        nameCell.setPadding(2);
        nameCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        nameCell.setHeight(40);
        headerTable.addCell(nameCell);

        // Columna 3: Fecha
        Cell dateCell = new Cell();
        dateCell.setBorder(Border.NO_BORDER);
        dateCell.add(new Paragraph("Fecha: " + datos.getFechaAsignacion())
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT));
        dateCell.setPadding(2);
        dateCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        dateCell.setHeight(40);
        headerTable.addCell(dateCell);

        // Aplicar fondo azul al header
        for (IElement element : headerTable.getChildren()) {
            Cell c = (Cell) element;
            c.setBackgroundColor(AZUL_MARINO);
            c.setFontColor(ColorConstants.WHITE);
            c.setBorderBottom(new SolidBorder(ROJO_LOGO, 2f)); // línea roja de acento
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
        rutinTable.setWidth(UnitValue.createPercentValue(ANCHO_PDF));

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
            obsCell.add(new Paragraph(observaciones)
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
     * Crear sección de un día: título + tabla con una fila por ejercicio
     * y un grupo de 5 columnas (Series, Kg, Reps, Descanso, RIR) por semana
     */
    private void crearDia(Document document, RutinaPdfDTO.DiaPdfDTO dia, Integer cantidadSemanas) {
        Paragraph tituloDia = new Paragraph("Día " + dia.getDia())
                .setBold()
                .setFontSize(10)
                .setBackgroundColor(AZUL_DIA)
                .setFontColor(ColorConstants.WHITE)
                .setMarginTop(0)
                .setMarginBottom(0)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setWidth(UnitValue.createPercentValue(ANCHO_PDF))
                .setBorder(new SolidBorder(AZUL_MARINO, 0.7f));

        document.add(tituloDia);

        int colCount = 1 + (cantidadSemanas * 5); // Ejercicio + 5 columnas por semana
        float[] columnWidths = new float[colCount];
        columnWidths[0] = 20;
        for (int i = 1; i < colCount; i++) {
            columnWidths[i] = 80f / (colCount - 1);
        }

        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(ANCHO_PDF));
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table.setHorizontalBorderSpacing(0);
        table.setVerticalBorderSpacing(0);

        // HEADER FILA 1: "Ejercicio" (ocupa las 2 filas de header) + "Sx" por semana (ocupa 5 columnas)
        table.addHeaderCell(new Cell(2, 1)
                .add(new Paragraph("Ejercicio").setBold().setFontSize(8).setMargin(0))
                .setBackgroundColor(GRIS_AZULADO)
                .setFontColor(AZUL_MARINO)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(2));

        for (int s = 1; s <= cantidadSemanas; s++) {
            table.addHeaderCell(new Cell(1, 5)
                    .add(new Paragraph("S" + s).setBold().setFontSize(8).setMargin(0))
                    .setBackgroundColor(AZUL_BANDA)
                            .setFontColor(AZUL_MARINO)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(2)
                    .setBorderLeft(
                            s > 1 ? new SolidBorder(AZUL_MARINO, 2f) : Border.NO_BORDER
                    ));
        }

        // HEADER FILA 2: Series | Reps | Kg | Descanso | RIR, repetido por semana
        String[] subHeaders = {"Series", "Reps", "Kg", "Descanso", "RIR"};
        for (int s = 1; s <= cantidadSemanas; s++) {
            for (String sub : subHeaders) {
                table.addHeaderCell(new Cell()
                        .add(new Paragraph(sub).setBold().setFontSize(7.5f).setMargin(0))
                        .setBackgroundColor(GRIS_AZULADO)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPadding(1)
                        .setBorderLeft(
                                s > 1 && sub.equals("Series") ? new SolidBorder(AZUL_MARINO, 2f) : Border.NO_BORDER
                        ));
            }
        }

        // FILAS: una por ejercicio
        for (RutinaPdfDTO.EjercicioPdfDTO ejercicio : dia.getEjercicios()) {
            table.addCell(new Cell()
                    .add(new Paragraph(ejercicio.getNombreEjercicio()).setFontSize(7.5f).setFontColor(TEXTO).setMargin(0))
                    .setPadding(2));

            for (int s = 1; s <= cantidadSemanas; s++) {
                int finalS = s;
                RutinaPdfDTO.SemanaPdfDTO semana = ejercicio.getSemanas().stream()
                        .filter(sem -> sem.getNumeroSemana() == finalS)
                        .findFirst()
                        .orElse(null);

                if (semana != null) {
                    table.addCell(celda(String.valueOf(semana.getSeries()))
                            .setBorderLeft(
                                    s > 1 ? new SolidBorder(AZUL_MARINO, 2f) :  Border.NO_BORDER));
                    table.addCell(celda(semana.getRepeticiones()));
                    table.addCell(celda(semana.getPesoKg() != null ? semana.getPesoKg() : "-"));
                    table.addCell(celda(semana.getDescansoMinutos() != null ? semana.getDescansoMinutos() : "-"));
                    table.addCell(celda(semana.getRir() != null ? String.valueOf(semana.getRir()) : "-"));
                } else {
                    for (int i = 0; i < 5; i++) {
                        table.addCell(celda("-")
                                .setBorderLeft(new SolidBorder(AZUL_MARINO, 2f)));
                    }
                }
            }
        }

        document.add(table);
        document.add(new Paragraph("").setMarginBottom(5));
    }

    private Cell celda(String texto) {
        return new Cell()
                .add(new Paragraph(texto).setFontSize(7.5f).setFontColor(TEXTO).setMargin(0))
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(1);
    }
}