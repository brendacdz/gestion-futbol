package com.grupo6.futbol.dao;

import com.grupo6.futbol.conexion.ConexionBD;
import com.grupo6.futbol.modelo.Jugador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JugadorDAO {

    public void guardar(Jugador jugador) throws SQLException {

        String sql = "INSERT INTO jugadores (club_id, nombre, edad, altura, peso, habilidad, puesto) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, jugador.getClubId());
            ps.setString(2, jugador.getNombre());
            ps.setInt(3, jugador.getEdad());
            ps.setDouble(4, jugador.getAltura());
            ps.setDouble(5, jugador.getPeso());
            ps.setInt(6, jugador.getHabilidad());
            ps.setString(7, jugador.getPuesto());

            ps.executeUpdate();
        }
    }

    public List<Jugador> listarPorClub(int clubId) throws SQLException {

        List<Jugador> jugadores = new ArrayList<>();
        String sql = "SELECT * FROM jugadores WHERE club_id = ? ORDER BY nombre";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, clubId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    jugadores.add(mapearJugador(rs));
                }
            }
        }

        return jugadores;
    }

    public int contarPorClub(int clubId) throws SQLException {

        String sql = "SELECT COUNT(*) AS total FROM jugadores WHERE club_id = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, clubId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }

        return 0;
    }

    public void eliminar(int id) throws SQLException {

        String sql = "DELETE FROM jugadores WHERE id = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void actualizar(Jugador jugador) throws SQLException {

        String sql = "UPDATE jugadores SET nombre = ?, edad = ?, altura = ?, peso = ?, "
                   + "habilidad = ?, puesto = ? WHERE id = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, jugador.getNombre());
            ps.setInt(2, jugador.getEdad());
            ps.setDouble(3, jugador.getAltura());
            ps.setDouble(4, jugador.getPeso());
            ps.setInt(5, jugador.getHabilidad());
            ps.setString(6, jugador.getPuesto());
            ps.setInt(7, jugador.getId());

            ps.executeUpdate();
        }
    }

    private Jugador mapearJugador(ResultSet rs) throws SQLException {
        Jugador jugador = new Jugador();
        jugador.setId(rs.getInt("id"));
        jugador.setClubId(rs.getInt("club_id"));
        jugador.setNombre(rs.getString("nombre"));
        jugador.setEdad(rs.getInt("edad"));
        jugador.setAltura(rs.getDouble("altura"));
        jugador.setPeso(rs.getDouble("peso"));
        jugador.setHabilidad(rs.getInt("habilidad"));
        jugador.setPuesto(rs.getString("puesto"));
        return jugador;
    }
}