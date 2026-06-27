package com.grupo6.futbol.dao;

import com.grupo6.futbol.conexion.ConexionBD;
import com.grupo6.futbol.modelo.Partido;
import com.grupo6.futbol.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase encargada de:
 * - Generar el fixture (todos contra todos, ida y vuelta)
 * - Guardar los resultados que carga el admin
 * - Calcular la tabla de posiciones
 */
public class PartidoDAO {

    /**
     * Genera el fixture completo (ida y vuelta) a partir de la lista de clubes,
     * y lo guarda en la base de datos.
     *
     * Ejemplo con 4 equipos (A, B, C, D):
     * IDA:    A-B, A-C, A-D, B-C, B-D, C-D    (fecha 1 a 6, simplificado)
     * VUELTA: B-A, C-A, D-A, C-B, D-B, D-C    (se invierte local/visitante)
     */
    public void generarFixture(List<Usuario> clubes) throws SQLException {

        List<Partido> partidos = new ArrayList<>();
        int fecha = 1;

        // ===== IDA =====
        // Recorremos todos los pares posibles de equipos, sin repetir
        for (int i = 0; i < clubes.size(); i++) {
            for (int j = i + 1; j < clubes.size(); j++) {
                int idLocal = clubes.get(i).getId();
                int idVisitante = clubes.get(j).getId();
                partidos.add(new Partido(idLocal, idVisitante, fecha));
                fecha++;
            }
        }

        // ===== VUELTA =====
        // Mismos pares, pero invirtiendo quién es local y quién visitante
        for (int i = 0; i < clubes.size(); i++) {
            for (int j = i + 1; j < clubes.size(); j++) {
                int idLocal = clubes.get(j).getId();      // se invierte
                int idVisitante = clubes.get(i).getId();  // se invierte
                partidos.add(new Partido(idLocal, idVisitante, fecha));
                fecha++;
            }
        }

        // Guardamos todos los partidos generados en la base de datos
        String sql = "INSERT INTO partidos (club_local_id, club_visitante_id, fecha_jornada, jugado) "
                   + "VALUES (?, ?, ?, false)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (Partido p : partidos) {
                ps.setInt(1, p.getClubLocalId());
                ps.setInt(2, p.getClubVisitanteId());
                ps.setInt(3, p.getFechaJornada());
                ps.addBatch(); // lo agrega a un "lote" para insertar todos juntos (más rápido)
            }

            ps.executeBatch(); // ejecuta todas las inserciones del lote de una vez
        }
    }

    /**
     * Borra todo el fixture actual. Útil si el admin quiere regenerarlo.
     */
    public void borrarFixture() throws SQLException {
        String sql = "DELETE FROM partidos";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }

    /**
     * Devuelve todos los partidos del fixture, con el nombre de cada club ya incluido
     * (se usa JOIN para traer el nombre del club junto con el partido).
     */
    public List<Partido> listarTodos() throws SQLException {

        List<Partido> partidos = new ArrayList<>();

        String sql = "SELECT p.*, "
                   + "ul.nombre_club AS nombre_local, "
                   + "uv.nombre_club AS nombre_visitante "
                   + "FROM partidos p "
                   + "JOIN usuarios ul ON p.club_local_id = ul.id "
                   + "JOIN usuarios uv ON p.club_visitante_id = uv.id "
                   + "ORDER BY p.fecha_jornada";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                partidos.add(mapearPartido(rs));
            }
        }

        return partidos;
    }

    /**
     * Guarda el resultado de un partido (lo carga el admin a mano).
     */
    public void cargarResultado(int partidoId, int golesLocal, int golesVisitante) throws SQLException {

        String sql = "UPDATE partidos SET goles_local = ?, goles_visitante = ?, jugado = true "
                   + "WHERE id = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, golesLocal);
            ps.setInt(2, golesVisitante);
            ps.setInt(3, partidoId);

            ps.executeUpdate();
        }
    }

    /**
     * Calcula la tabla de posiciones a partir de los partidos ya jugados.
     * Reglas: partido ganado = 3 puntos, empate = 1 punto, perdido = 0 puntos.
     *
     * Devuelve una lista de mapas (cada mapa representa una fila de la tabla)
     * para que sea fácil convertirla a JSON con Gson.
     */
    public List<Map<String, Object>> calcularTablaPosiciones() throws SQLException {

        // Usamos un Map para ir acumulando los datos de cada club por su id
        Map<Integer, Map<String, Object>> tabla = new LinkedHashMap<>();

        // 1. Traemos todos los clubes y los inicializamos en 0
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        for (Usuario club : usuarioDAO.listarClubes()) {
            Map<String, Object> fila = new LinkedHashMap<>();
            fila.put("clubId", club.getId());
            fila.put("nombreClub", club.getNombreClub());
            fila.put("puntos", 0);
            fila.put("partidosJugados", 0);
            fila.put("ganados", 0);
            fila.put("empatados", 0);
            fila.put("perdidos", 0);
            fila.put("golesAFavor", 0);
            fila.put("golesEnContra", 0);
            tabla.put(club.getId(), fila);
        }

        // 2. Recorremos los partidos ya jugados y sumamos puntos/estadísticas
        String sql = "SELECT * FROM partidos WHERE jugado = true";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idLocal = rs.getInt("club_local_id");
                int idVisitante = rs.getInt("club_visitante_id");
                int golesLocal = rs.getInt("goles_local");
                int golesVisitante = rs.getInt("goles_visitante");

                Map<String, Object> filaLocal = tabla.get(idLocal);
                Map<String, Object> filaVisitante = tabla.get(idVisitante);

                if (filaLocal == null || filaVisitante == null) {
                    continue; // por seguridad, si algún club no existe lo saltamos
                }

                // Sumamos partidos jugados y goles para ambos equipos
                sumar(filaLocal, "partidosJugados", 1);
                sumar(filaVisitante, "partidosJugados", 1);
                sumar(filaLocal, "golesAFavor", golesLocal);
                sumar(filaLocal, "golesEnContra", golesVisitante);
                sumar(filaVisitante, "golesAFavor", golesVisitante);
                sumar(filaVisitante, "golesEnContra", golesLocal);

                // Definimos quién ganó, perdió o si fue empate
                if (golesLocal > golesVisitante) {
                    // Ganó el local
                    sumar(filaLocal, "puntos", 3);
                    sumar(filaLocal, "ganados", 1);
                    sumar(filaVisitante, "perdidos", 1);
                } else if (golesLocal < golesVisitante) {
                    // Ganó el visitante
                    sumar(filaVisitante, "puntos", 3);
                    sumar(filaVisitante, "ganados", 1);
                    sumar(filaLocal, "perdidos", 1);
                } else {
                    // Empate
                    sumar(filaLocal, "puntos", 1);
                    sumar(filaVisitante, "puntos", 1);
                    sumar(filaLocal, "empatados", 1);
                    sumar(filaVisitante, "empatados", 1);
                }
            }
        }

        // 3. Convertimos el Map a una lista y la ordenamos por puntos (de mayor a menor)
        List<Map<String, Object>> listaTabla = new ArrayList<>(tabla.values());
        listaTabla.sort((fila1, fila2) -> {
            int puntos1 = (int) fila1.get("puntos");
            int puntos2 = (int) fila2.get("puntos");
            return puntos2 - puntos1; // orden descendente
        });

        return listaTabla;
    }

    /**
     * Método auxiliar privado: suma un valor a un campo numérico dentro del mapa de una fila.
     */
    private void sumar(Map<String, Object> fila, String campo, int valor) {
        int actual = (int) fila.get(campo);
        fila.put(campo, actual + valor);
    }

    /**
     * Método auxiliar privado: convierte una fila del ResultSet en un objeto Partido.
     */
    private Partido mapearPartido(ResultSet rs) throws SQLException {
        Partido partido = new Partido();
        partido.setId(rs.getInt("id"));
        partido.setClubLocalId(rs.getInt("club_local_id"));
        partido.setClubVisitanteId(rs.getInt("club_visitante_id"));
        partido.setNombreClubLocal(rs.getString("nombre_local"));
        partido.setNombreClubVisitante(rs.getString("nombre_visitante"));
        partido.setFechaJornada(rs.getInt("fecha_jornada"));
        partido.setJugado(rs.getBoolean("jugado"));

        // Los goles pueden ser null (si el partido no se jugó todavía)
        int golesLocal = rs.getInt("goles_local");
        partido.setGolesLocal(rs.wasNull() ? null : golesLocal);

        int golesVisitante = rs.getInt("goles_visitante");
        partido.setGolesVisitante(rs.wasNull() ? null : golesVisitante);

        return partido;
    }
}