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

/**
 * Servlet que maneja el login de clubes y del administrador.
 * Recibe los datos por AJAX (POST) y responde en formato JSON.
 *
 * URL: /LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Le decimos al navegador que la respuesta va a ser JSON, en UTF-8
        response.setContentType("application/json;charset=UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Map<String, Object> resultado = new HashMap<>();

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario = usuarioDAO.login(username, password);

            if (usuario != null) {
                // Login correcto: guardamos el usuario en la sesión
                // (así en los próximos pedidos sabemos quién está conectado)
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);

                resultado.put("exito", true);
                resultado.put("rol", usuario.getRol());
                resultado.put("nombreClub", usuario.getNombreClub());
                resultado.put("clubId", usuario.getId());
            } else {
                // Usuario o contraseña incorrectos
                resultado.put("exito", false);
                resultado.put("mensaje", "Usuario o contraseña incorrectos");
            }

        } catch (SQLException e) {
            resultado.put("exito", false);
            resultado.put("mensaje", "Error de conexión con la base de datos");
            e.printStackTrace(); // queda registrado en la consola de Eclipse para detectar el error
        }

        // Convertimos el Map a JSON y lo escribimos en la respuesta
        response.getWriter().write(gson.toJson(resultado));
    }

    /**
     * Servlet también usado para cerrar sesión (logout), llamando por GET.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("logout".equals(accion)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate(); // cierra y borra la sesión actual
            }

            response.setContentType("application/json;charset=UTF-8");
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("exito", true);
            response.getWriter().write(gson.toJson(resultado));
        }
    }
}