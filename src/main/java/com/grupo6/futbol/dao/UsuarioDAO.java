package com.grupo6.futbol.dao;

import com.grupo6.futbol.conexion.ConexionBD;
import com.grupo6.futbol.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de todas las consultas SQL relacionadas con la tabla "usuarios".
 * (login, buscar clubes, etc.)
 */
public class UsuarioDAO {

    /**
     * Busca un usuario por username y password (para el login).
     * Devuelve el Usuario si las credenciales son correctas, o null si no existe.
     */
    public Usuario login(String username, String password) throws SQLException {

        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Se encontró un usuario con esas credenciales: lo armamos y lo devolvemos
                    return mapearUsuario(rs);
                }
            }
        }

        return null; // no se encontró ningún usuario con ese username/password
    }

    /**
     * Devuelve la lista de todos los clubes (usuarios con rol = CLUB).
     * Útil para que el admin elija entre qué equipos generar el fixture.
     */
    public List<Usuario> listarClubes() throws SQLException {

        List<Usuario> clubes = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE rol = 'CLUB'";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clubes.add(mapearUsuario(rs));
            }
        }

        return clubes;
    }

    /**
     * Busca un club por su id. Se usa, por ejemplo, para mostrar el nombre
     * del club en la tabla de posiciones o en el fixture.
     */
    public Usuario buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }

        return null;
    }

    /**
     * Método auxiliar privado: convierte una fila del ResultSet en un objeto Usuario.
     * Se usa en varios métodos de esta clase para no repetir código.
     */
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setUsername(rs.getString("username"));
        usuario.setPassword(rs.getString("password"));
        usuario.setNombreClub(rs.getString("nombre_club"));
        usuario.setRol(rs.getString("rol"));
        return usuario;
    }
}