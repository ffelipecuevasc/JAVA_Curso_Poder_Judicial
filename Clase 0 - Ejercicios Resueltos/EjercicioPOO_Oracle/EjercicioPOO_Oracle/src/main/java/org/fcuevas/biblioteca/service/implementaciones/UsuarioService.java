package org.fcuevas.biblioteca.service.implementaciones;

import org.fcuevas.biblioteca.dao.implementaciones.UsuarioDAO;
import org.fcuevas.biblioteca.dao.interfaces.IUsuarioDAO;
import org.fcuevas.biblioteca.dto.UsuarioDTO;
import org.fcuevas.biblioteca.service.interfaces.IUsuarioService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UsuarioService implements IUsuarioService {

    private IUsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    @Override
    public boolean crearUsuario(UsuarioDTO usuario) {
        if (usuario == null) throw new IllegalArgumentException("El usuario no puede ser nulo.");

        // Normalizaciones básicas
        usuario.setRut(normalizar(usuario.getRut()));
        usuario.setNombre(normalizar(usuario.getNombre()));
        usuario.setEmail(normalizar(usuario.getEmail()).toLowerCase());

        validarRut(usuario.getRut());
        validarNombre(usuario.getNombre());
        validarEmail(usuario.getEmail());
        validarCupos(usuario.getCupoMax(), usuario.getCupoActual());

        try {
            if (usuarioDAO.existePorRut(usuario.getRut()))
                throw new IllegalArgumentException("Ya existe un usuario con ese RUT");
            if (usuarioDAO.existePorEmail(usuario.getEmail()))
                throw new IllegalArgumentException("Ya existe un usuario con ese email");

            return usuarioDAO.crear(usuario);
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear usuario: " + e.getMessage());
        }
    }

    @Override
    public Optional<UsuarioDTO> buscarPorRut(String rut) {
        rut = normalizar(rut);
        validarRut(rut);
        try {
            return usuarioDAO.buscarPorRut(rut);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por RUT: " + e.getMessage());
        }
    }

    @Override
    public List<UsuarioDTO> listarUsuarios() {
        try {
            return usuarioDAO.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios: " + e.getMessage());
        }
    }

    @Override
    public boolean actualizarUsuario(UsuarioDTO usuario) {
        if (usuario == null) throw new IllegalArgumentException("El usuario no puede ser null");

        usuario.setRut(normalizar(usuario.getRut()));
        usuario.setNombre(normalizar(usuario.getNombre()));
        usuario.setEmail(normalizar(usuario.getEmail()).toLowerCase());

        validarRut(usuario.getRut());
        validarNombre(usuario.getNombre());
        validarEmail(usuario.getEmail());
        validarCupos(usuario.getCupoMax(), usuario.getCupoActual());

        try {
            // Debe existir
            var actualOpt = usuarioDAO.buscarPorRut(usuario.getRut());
            if (actualOpt.isEmpty())
                throw new IllegalArgumentException("No existe un usuario con el RUT indicado");

            // Si cambia el email, validar duplicado
            var actual = actualOpt.get();
            if (!usuario.getEmail().equalsIgnoreCase(actual.getEmail())
                    && usuarioDAO.existePorEmail(usuario.getEmail())) {
                throw new IllegalArgumentException("Ya existe un usuario con ese email");
            }

            return usuarioDAO.actualizar(usuario);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    public boolean eliminarUsuario(String rut) {
        rut = normalizar(rut);
        validarRut(rut);
        try {
            return usuarioDAO.eliminarPorRut(rut);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario: " + e.getMessage());
        }
    }

    @Override
    public boolean existeUsuarioPorRut(String rut) {
        rut = normalizar(rut);
        validarRut(rut);
        try {
            return usuarioDAO.existePorRut(rut);
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia por RUT: " + e.getMessage());
        }
    }

    @Override
    public boolean existeUsuarioPorEmail(String email) {
        email = normalizar(email).toLowerCase();
        validarEmail(email);
        try {
            return usuarioDAO.existePorEmail(email);
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia por email: " + e.getMessage());
        }
    }

    // ----------------- Validaciones básicas -----------------

    private void validarRut(String rut) {
        if (rut == null || rut.isBlank())
            throw new IllegalArgumentException("El RUT es obligatorio");
        if (rut.length() > 15)
            throw new IllegalArgumentException("El RUT excede el largo máximo (15)");
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio");
        if (nombre.length() > 100)
            throw new IllegalArgumentException("El nombre excede el largo máximo (100)");
    }

    private void validarEmail(String email) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("El email es obligatorio");
        if (email.length() > 200)
            throw new IllegalArgumentException("El email excede el largo máximo (200)");
        if (!email.contains("@") || !email.contains("."))
            throw new IllegalArgumentException("El email no tiene un formato válido");
    }

    private void validarCupos(int cupoMax, int cupoActual) {
        if (cupoMax < 0) throw new IllegalArgumentException("El cupo máximo no puede ser negativo");
        if (cupoActual < 0) throw new IllegalArgumentException("El cupo actual no puede ser negativo");
        if (cupoActual > cupoMax) throw new IllegalArgumentException("El cupo actual no puede superar al máximo");
    }

    private String normalizar(String s) {
        return s == null ? null : s.trim();
    }
}
