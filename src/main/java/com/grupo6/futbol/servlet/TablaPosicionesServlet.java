package com.grupo6.futbol.servlet;

import com.google.gson.Gson;
import com.grupo6.futbol.dao.PartidoDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet de acceso PUBLICO (no requiere login) que devuelve la
 * tabla de posiciones del campeonato, ya calculada y ordenada por puntos.
 *
 * URL: /TablaPosicionesServlet
 *
 * - GET -> devuelve la tabla de posiciones en formato JSON
 */
@WebServlet("/TablaPosicionesServlet")
public class TablaPosicionesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> resultado = new HashMap<>();

        try {
            PartidoDAO partidoDAO = new PartidoDAO();
            List<Map<String, Object>> tabla = partidoDAO.calcularTablaPosiciones();

            resultado.put("exito", true);
            resultado.put("tabla", tabla);

        } catch (SQLException e) {
            resultado.put("exito", false);
            resultado.put("mensaje", "Error al calcular la tabla de posiciones");
            e.printStackTrace();
        }

        response.getWriter().write(gson.toJson(resultado));
    }
}