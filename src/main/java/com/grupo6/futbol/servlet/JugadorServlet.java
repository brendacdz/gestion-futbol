package com.grupo6.futbol.servlet;

import com.google.gson.Gson;
import com.grupo6.futbol.dao.JugadorDAO;
import com.grupo6.futbol.modelo.Jugador;
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

@WebServlet("/JugadorServlet")
public class JugadorServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Gson gson = new Gson();
    private static final int MAXIMO_JUGADORES = 23;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        Map<String, Object> resultado = new HashMap<>();

        if (usuario == null) {
            resultado.put("exito", false);
            resultado.put("mensaje", "Debe iniciar sesión");
            response.getWriter().write(gson.toJson(resultado));
            return;
        }

        try {
            JugadorDAO jugadorDAO = new JugadorDAO();
            List<Jugador> jugadores = jugadorDAO.listarPorClub(usuario.getId());

            resultado.put("exito", true);
            resultado.put("jugadores", jugadores);

        } catch (SQLException e) {
            resultado.put("exito", false);
            resultado.put("mensaje", "Error al consultar los jugadores");
            e.printStackTrace();
        }

        response.getWriter().write(gson.toJson(resultado));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        Map<String, Object> resultado = new HashMap<>();

        if (usuario == null) {
            resultado.put("exito", false);
            resultado.put("mensaje", "Debe iniciar sesión");
            response.getWriter().write(gson.toJson(resultado));
            return;
        }

        String accion = request.getParameter("accion");
        JugadorDAO jugadorDAO = new JugadorDAO();

        try {
            if ("eliminar".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("id"));
                jugadorDAO.eliminar(id);

                resultado.put("exito", true);

            } else {
                int cantidadActual = jugadorDAO.contarPorClub(usuario.getId());

                if (cantidadActual >= MAXIMO_JUGADORES) {
                    resultado.put("exito", false);
                    resultado.put("mensaje", "El club ya alcanzó el máximo de " + MAXIMO_JUGADORES + " jugadores");
                    response.getWriter().write(gson.toJson(resultado));
                    return;
                }

                Jugador jugador = new Jugador();
                jugador.setClubId(usuario.getId());
                jugador.setNombre(request.getParameter("nombre"));
                jugador.setEdad(Integer.parseInt(request.getParameter("edad")));
                jugador.setAltura(Double.parseDouble(request.getParameter("altura")));
                jugador.setPeso(Double.parseDouble(request.getParameter("peso")));
                jugador.setHabilidad(Integer.parseInt(request.getParameter("habilidad")));
                jugador.setPuesto(request.getParameter("puesto"));

                jugadorDAO.guardar(jugador);

                resultado.put("exito", true);
            }

        } catch (SQLException e) {
            resultado.put("exito", false);
            resultado.put("mensaje", "Error al guardar el jugador");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            resultado.put("exito", false);
            resultado.put("mensaje", "Datos numéricos inválidos (edad, altura, peso o habilidad)");
        }

        response.getWriter().write(gson.toJson(resultado));
    }
}