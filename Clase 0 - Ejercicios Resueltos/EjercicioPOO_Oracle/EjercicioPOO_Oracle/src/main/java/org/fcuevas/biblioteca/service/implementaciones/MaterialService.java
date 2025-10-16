package org.fcuevas.biblioteca.service.implementaciones;

import org.fcuevas.biblioteca.dao.implementaciones.MaterialDAO;
import org.fcuevas.biblioteca.dao.interfaces.IMaterialDAO;
import org.fcuevas.biblioteca.dto.AudioLibroDTO;
import org.fcuevas.biblioteca.dto.LibroDTO;
import org.fcuevas.biblioteca.dto.MaterialDTO;
import org.fcuevas.biblioteca.dto.RevistaDTO;
import org.fcuevas.biblioteca.enums.TipoMaterial;
import org.fcuevas.biblioteca.service.interfaces.IMaterialService;
import org.fcuevas.biblioteca.service.interfaces.IPrestamoService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MaterialService implements IMaterialService {

    private IMaterialDAO materialDAO;
    private IPrestamoService prestamoService;

    public MaterialService() {
        this(new MaterialDAO(), new PrestamoService());
    }

    // Constructor recomendado: inyecta DAO y Servicio de Préstamos (para validar eliminación).
    public MaterialService(IMaterialDAO materialDAO, IPrestamoService prestamoService) {
        this.materialDAO = materialDAO;
        this.prestamoService = prestamoService;
    }


    @Override
    public Integer crearLibro(MaterialDTO base, LibroDTO libro) {
        validarBase(base, TipoMaterial.LIBRO);
        validarLibro(libro);
        // Regla: ISBN único
        if (libro.getIsbn() == null || libro.getIsbn().isBlank())
            throw new IllegalArgumentException("[ERROR] El ISBN es obligatorio");
        try {
            if (materialDAO.existeLibroPorIsbn(libro.getIsbn().trim()))
                throw new IllegalArgumentException("[ERROR] Ya existe un libro con el mismo ISBN");
            return materialDAO.crearLibro(base, libro);
        } catch (SQLException e) {
            throw new RuntimeException("[ERROR] Error al crear libro: " + e.getMessage());
        }
    }

    @Override
    public Integer crearRevista(MaterialDTO base, RevistaDTO revista) {
        validarBase(base, TipoMaterial.REVISTA);
        validarRevista(revista);
        try {
            return materialDAO.crearRevista(base, revista);
        } catch (SQLException e) {
            throw new RuntimeException("[ERROR] Error al crear revista: " + e.getMessage());
        }
    }

    @Override
    public Integer crearAudiolibro(MaterialDTO base, AudioLibroDTO audioLibro) {
        validarBase(base, TipoMaterial.AUDIOLIBRO);
        validarAudioLibro(audioLibro);
        try {
            return materialDAO.crearAudiolibro(base, audioLibro);
        } catch (SQLException e) {
            throw new RuntimeException("[ERROR] Error al crear audiolibro: " + e.getMessage());
        }
    }

    @Override
    public Optional<MaterialDTO> buscarPorCodigo(Integer codigo) {
        validarId(codigo);
        try {
            return materialDAO.buscarPorCodigo(codigo);
        } catch (SQLException e) {
            throw new RuntimeException("[ERROR] Error al buscar material por código: " + e.getMessage());
        }
    }

    @Override
    public Optional<LibroDTO> obtenerDetalleLibro(Integer codigoMaterial) {
        validarId(codigoMaterial);
        try {
            return materialDAO.buscarDetalleLibro(codigoMaterial);
        } catch (SQLException e) {
            throw new RuntimeException("[ERROR] Error al obtener detalle de libro: " + e.getMessage());
        }
    }

    @Override
    public Optional<RevistaDTO> obtenerDetalleRevista(Integer codigoMaterial) {
        validarId(codigoMaterial);
        try {
            return materialDAO.buscarDetalleRevista(codigoMaterial);
        } catch (SQLException e) {
            throw new RuntimeException("[ERROR] Error al obtener detalle de revista: " + e.getMessage());
        }    }

    @Override
    public Optional<AudioLibroDTO> obtenerDetalleAudioLibro(Integer codigoMaterial) {
        validarId(codigoMaterial);
        try {
            return materialDAO.buscarDetalleAudioLibro(codigoMaterial);
        } catch (SQLException e) {
            throw new RuntimeException("[ERROR] Error al obtener detalle de audiolibro: " + e.getMessage());
        }
    }

    @Override
    public List<MaterialDTO> listarMateriales() {
        try {
            return materialDAO.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("[ERROR] Error al listar materiales: " + e.getMessage());
        }
    }

    @Override
    public List<MaterialDTO> listarPorTipo(TipoMaterial tipo) {
        if (tipo == null) throw new IllegalArgumentException("El tipo de material es obligatorio");
        try {
            return materialDAO.listarPorTipo(tipo);
        } catch (SQLException e) {
            throw new RuntimeException("[ERROR] Error al listar materiales por tipo: " + e.getMessage());
        }
    }

    @Override
    public List<MaterialDTO> listarDisponiblesPorTipo(TipoMaterial tipo) {
        if (tipo == null) throw new IllegalArgumentException("El tipo de material es obligatorio");
        try {
            return materialDAO.listarDisponiblesPorTipo(tipo);
        } catch (SQLException e) {
            throw new RuntimeException("[ERROR] Error al listar materiales disponibles por tipo: " + e.getMessage());
        }
    }

    @Override
    public boolean actualizarMaterial(MaterialDTO material) {
        validarMaterialParaUpdate(material);
        try {
            // Debe existir para ser actualizado
            if (!materialDAO.existePorCodigo(material.getCodigo()))
                throw new IllegalArgumentException("No existe un material con el código indicado");
            return materialDAO.actualizarMaterial(material);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar material: " + e.getMessage());
        }
    }

    @Override
    public boolean actualizarDetalleLibro(LibroDTO libro) {
        validarId(libro.getCodigoMaterial());
        validarLibro(libro);
        try {
            return materialDAO.actualizarDetalleLibro(libro);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar detalle de libro: " + e.getMessage());
        }
    }

    @Override
    public boolean actualizarDetalleRevista(RevistaDTO revista) {
        validarId(revista.getCodigoMaterial());
        validarRevista(revista);
        try {
            return materialDAO.actualizarDetalleRevista(revista);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar detalle de revista: " + e.getMessage());
        }
    }

    @Override
    public boolean actualizarDetalleAudioLibro(AudioLibroDTO audioLibro) {
        validarId(audioLibro.getCodigoMaterial());
        validarAudioLibro(audioLibro);
        try {
            return materialDAO.actualizarDetalleAudioLibro(audioLibro);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar detalle de audiolibro: " + e.getMessage());
        }
    }

    @Override
    public boolean cambiarDisponibilidad(Integer codigoMaterial, boolean disponible) {
        validarId(codigoMaterial);
        try {
            if (!materialDAO.existePorCodigo(codigoMaterial))
                throw new IllegalArgumentException("No existe un material con el código indicado");
            return materialDAO.cambiarDisponibilidad(codigoMaterial, disponible);
        } catch (SQLException e) {
            throw new RuntimeException("Error al cambiar disponibilidad: " + e.getMessage());
        }
    }

    @Override
    public boolean eliminarMaterial(Integer codigo) {
        validarId(codigo);

        // Verificación en SERVICE (no en DAO)
        if (prestamoService == null) {
            // Mensaje explícito para que inyectes el service cuando implementes préstamos
            throw new IllegalStateException("No se configuró IPrestamoService; no es posible validar préstamos activos.");
        }
        if (prestamoService.existePrestamoActivoPorMaterial(codigo)) {
            throw new IllegalArgumentException("No se puede eliminar: el material tiene un préstamo ACTIVO.");
        }

        try {
            return materialDAO.eliminarPorCodigo(codigo);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar material: " + e.getMessage());
        }
    }

    @Override
    public boolean existeMaterialPorCodigo(Integer codigo) {
        validarId(codigo);
        try {
            return materialDAO.existePorCodigo(codigo);
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia por código: " + e.getMessage());
        }
    }

    @Override
    public boolean existeLibroPorIsbn(String isbn) {
        if (isbn == null || isbn.isBlank())
            throw new IllegalArgumentException("El ISBN es obligatorio");
        try {
            return materialDAO.existeLibroPorIsbn(isbn.trim());
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia por ISBN: " + e.getMessage());
        }
    }

    private static void validarBase(MaterialDTO base, TipoMaterial esperado) {
        if (base == null) throw new IllegalArgumentException("El material base es obligatorio");
        if (base.getTitulo() == null || base.getTitulo().trim().isEmpty())
            throw new IllegalArgumentException("El título es obligatorio");
        if (base.getTipo() == null)
            throw new IllegalArgumentException("El tipo de material es obligatorio");
        if (base.getTipo() != esperado)
            throw new IllegalArgumentException("El tipo de material no coincide con el detalle a crear");
        if (base.getDiasBase() <= 0)
            throw new IllegalArgumentException("Los días base deben ser > 0");
        // disponible queda a elección del llamador (true por defecto al crear en el DTO)
        base.setTitulo(base.getTitulo().trim());
    }

    private static void validarMaterialParaUpdate(MaterialDTO m) {
        if (m == null) throw new IllegalArgumentException("El material es obligatorio");
        validarId(m.getCodigo());
        if (m.getTitulo() == null || m.getTitulo().trim().isEmpty())
            throw new IllegalArgumentException("El título es obligatorio");
        if (m.getTipo() == null)
            throw new IllegalArgumentException("El tipo de material es obligatorio");
        if (m.getDiasBase() <= 0)
            throw new IllegalArgumentException("Los días base deben ser > 0");
        m.setTitulo(m.getTitulo().trim());
    }

    private static void validarLibro(LibroDTO l) {
        if (l == null) throw new IllegalArgumentException("El detalle de libro es obligatorio");
        if (l.getAutor() == null || l.getAutor().trim().isEmpty())
            throw new IllegalArgumentException("El autor es obligatorio");
        if (l.getAño() != null && (l.getAño() < 0 || l.getAño() > 9999))
            throw new IllegalArgumentException("El año debe estar entre 0 y 9999");
        if (l.getIsbn() == null || l.getIsbn().trim().isEmpty())
            throw new IllegalArgumentException("El ISBN es obligatorio");
        l.setAutor(l.getAutor().trim());
        l.setIsbn(l.getIsbn().trim());
    }

    private static void validarRevista(RevistaDTO r) {
        if (r == null) throw new IllegalArgumentException("El detalle de revista es obligatorio");
        if (r.getNumeroEdicion() == null || r.getNumeroEdicion() < 0)
            throw new IllegalArgumentException("El número de edición debe ser >= 0");
        if (r.getPeriodicidad() == null || r.getPeriodicidad().trim().isEmpty())
            throw new IllegalArgumentException("La periodicidad es obligatoria");
        r.setPeriodicidad(r.getPeriodicidad().trim());
    }

    private static void validarAudioLibro(AudioLibroDTO a) {
        if (a == null) throw new IllegalArgumentException("El detalle de audiolibro es obligatorio");
        if (a.getNarrador() == null || a.getNarrador().trim().isEmpty())
            throw new IllegalArgumentException("El narrador es obligatorio");
        if (a.getDuracionMin() <= 0)
            throw new IllegalArgumentException("La duración debe ser > 0");
        a.setNarrador(a.getNarrador().trim());
    }

    private static void validarId(Integer id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("El código debe ser un entero positivo");
    }
}
