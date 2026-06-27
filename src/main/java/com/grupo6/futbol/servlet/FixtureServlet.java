package com.grupo6.futbol.servlet;

import com.google.gson.Gson;
import com.grupo6.futbol.dao.PartidoDAO;
import com.grupo6.futbol.dao.UsuarioDAO;
import com.grupo6.futbol.modelo.Partido;
import com.grupo6.futbol.modelo.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet que usa el ADMIN para generar el fixture del campeonato
 * (todos contra todos, ida y vuelta) y para consultar los partidos generados.
 *
 * URL: /FixtureServlet
 *
 * - GET  -> devuelve la lista de partidos del fixture (acceso público, cualquiera puede verlo)
 * - POST -> genera el fixture nuevo (solo el ADMIN puede hacerlo)
 */
@WebServlet("/FixtureServlet")
public class FixtureServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> resultado = new HashMap<>();

        try {
            PartidoDAO partidoDAO = new PartidoDAO();
            List<Partido> partidos = partidoDAO.listarTodos();

            resultado.put("exito", true);
            resultado.put("partidos", partidos);

        } catch (SQLException e) {
            resultado.put("exito", false);
            resultado.put("mensaje", "Error al consultar el fixture");
            e.printStackTrace();
        }

        response.getWriter().write(gson.toJson(resultado));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> resultado = new HashMap<>();

        // ===== Verificamos que quien pide esto sea el ADMIN =====
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null || !usuario.esAdmin()) {
            resultado.put("exito", false);
            resultado.put("mensaje", "Solo el administrador puede generar el fixture");
            response.getWriter().write(gson.toJson(resultado));
            return;
        }

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            List<Usuario> clubes = usuarioDAO.listarClubes();

            // Necesitamos al menos 2 clubes para armar un campeonato
            if (clubes.size() < 2) {
                resultado.put("exito", false);
                resultado.put("mensaje", "Debe haber al menos 2 clubes registrados para generar el fixture");
                response.getWriter().write(gson.toJson(resultado));
                return;
            }

            PartidoDAO partidoDAO = new PartidoDAO();

            // Antes de generar uno nuevo, borramos el fixture anterior (si existía)
            partidoDAO.borrarFixture();
            partidoDAO.generarFixture(clubes);

            resultado.put("exito", true);
            resultado.put("mensaje", "Fixture generado correctamente con " + clubes.size() + " equipos");

        } catch (SQLException e) {
            resultado.put("exito", false);
            resultado.put("mensaje", "Error al generar el fixture");
            e.printStackTrace();
        }

        response.getWriter().write(gson.toJson(resultado));
    }
}