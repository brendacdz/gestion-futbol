package com.grupo6.futbol.conexion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {

    private static String url;
    private static String usuario;
    private static String password;

    static {
        try (InputStream input = ConexionBD.class.getClassLoader()
                .getResourceAsStream("database.properties")) {

            if (input == null) {
                throw new RuntimeException("No se encontró el archivo database.properties en resources");
            }

            Properties propiedades = new Properties();
            propiedades.load(input);

            url = propiedades.getProperty("db.url");
            usuario = propiedades.getProperty("db.usuario");
            password = propiedades.getProperty("db.password");

        } catch (IOException e) {
            throw new RuntimeException("Error al leer database.properties", e);
        }
    }

    public static Connection obtenerConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver de MySQL. Verificar dependencia en pom.xml", e);
        }

        return DriverManager.getConnection(url, usuario, password);
    }
}