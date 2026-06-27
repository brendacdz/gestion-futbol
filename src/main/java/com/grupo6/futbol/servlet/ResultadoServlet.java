package com.grupo6.futbol.servlet;

import com.google.gson.Gson;
import com.grupo6.futbol.dao.PartidoDAO;
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
import java.util.Map;

/**
 * Servlet que usa el ADMIN para cargar el resultado de un partido.
 *
 * URL: /ResultadoServlet
 *
 * - POST -> guarda el resultado (goles local y visitante) de un partido puntual
 *           Parámetros esperados: partidoId, golesLocal, golesVisitante
 */
@WebServlet("/ResultadoServlet")
public class ResultadoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Gson gson = new Gson();

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
            resultado.put("mensaje", "Solo el administrador puede cargar resultados");
            response.getWriter().write(gson.toJson(resultado));
            return;
        }

        try {
            int partidoId = Integer.parseInt(request.getParameter("partidoId"));
            int golesLocal = Integer.parseInt(request.getParameter("golesLocal"));
            int golesVisitante = Integer.parseInt(request.getParameter("golesVisitante"));

            // Validamos que los goles no sean negativos
            if (golesLocal < 0 || golesVisitante < 0) {
                resultado.put("exito", false);
                resultado.put("mensaje", "Los goles no pueden ser negativos");
                response.getWriter().write(gson.toJson(resultado));
                return;
            }

            PartidoDAO partidoDAO = new PartidoDAO();
            partidoDAO.cargarResultado(partidoId, golesLocal, golesVisitante);

            resultado.put("exito", true);
            resultado.put("mensaje", "Resultado guardado correctamente");

        } catch (NumberFormatException e) {
            resultado.put("exito", false);
            resultado.put("mensaje", "Los datos enviados no son números válidos");
        } catch (SQLException e) {
            resultado.put("exito", false);
            resultado.put("mensaje", "Error al guardar el resultado");
            e.printStackTrace();
        }

        response.getWriter().write(gson.toJson(resultado));
    }
}