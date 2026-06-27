package com.grupo6.futbol.modelo;

/**
 * Clase que representa a un usuario del sistema.
 * Puede ser un CLUB o el ADMIN (administrador del campeonato).
 */
public class Usuario {

    private int id;
    private String username;
    private String password;
    private String nombreClub;   // solo tiene valor si el rol es CLUB
    private String rol;          // puede ser "CLUB" o "ADMIN"

    // Constructor vacío (lo necesitan algunas librerías, como Gson)
    public Usuario() {
    }

    // Constructor con todos los datos
    public Usuario(int id, String username, String password, String nombreClub, String rol) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombreClub = nombreClub;
        this.rol = rol;
    }

    // ===== Getters y Setters =====
    // Sirven para leer y modificar los datos de un objeto Usuario desde afuera de la clase

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreClub() {
        return nombreClub;
    }

    public void setNombreClub(String nombreClub) {
        this.nombreClub = nombreClub;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    // Método útil para saber rápido si el usuario es administrador
    public boolean esAdmin() {
        return "ADMIN".equals(this.rol);
    }
}