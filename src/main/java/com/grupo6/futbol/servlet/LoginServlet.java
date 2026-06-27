package com.grupo6.futbol.servlet;

import com.google.gson.Gson;
import com.grupo6.futbol.dao.UsuarioDAO;
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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Map<String, Object> resultado = new HashMap<>();

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario = usuarioDAO.login(username, password);

            if (usuario != null) {
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);

                resultado.put("exito", true);
                resultado.put("rol", usuario.getRol());
                resultado.put("nombreClub", usuario.getNombreClub());
                resultado.put("clubId", usuario.getId());
            } else {
                resultado.put("exito", false);
                resultado.put("mensaje", "Usuario o contraseña incorrectos");
            }

        } catch (SQLException e) {
            resultado.put("exito", false);
            resultado.put("mensaje", "Error de conexión con la base de datos");
            e.printStackTrace();
        }

        response.getWriter().write(gson.toJson(resultado));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("logout".equals(accion)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            response.setContentType("application/json;charset=UTF-8");
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("exito", true);
            response.getWriter().write(gson.toJson(resultado));
        }
    }
}