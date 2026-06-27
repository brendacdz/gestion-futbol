package com.grupo6.futbol.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de abrir la conexión con la base de datos MySQL.
 * Usa los datos por defecto de XAMPP: usuario "root" sin contraseña.
 */
public class ConexionBD {

    // Datos de conexión a MySQL (XAMPP)
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_futbol?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String PASSWORD = ""; // XAMPP por defecto no tiene contraseña

    /**
     * Abre y devuelve una conexión nueva a la base de datos.
     * Cada vez que se llama a este método, se abre una conexión distinta.
     */
    public static Connection obtenerConexion() throws SQLException {
        try {
            // Carga el driver de MySQL (el "traductor" entre Java y MySQL)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver de MySQL. Verificar dependencia en pom.xml", e);
        }

        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}