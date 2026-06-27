package com.grupo6.futbol.modelo;

public class Usuario {

    private int id;
    private String username;
    private String password;
    private String nombreClub;
    private String rol;

    public Usuario() {
    }

    public Usuario(int id, String username, String password, String nombreClub, String rol) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombreClub = nombreClub;
        this.rol = rol;
    }

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

    public boolean esAdmin() {
        return "ADMIN".equals(this.rol);
    }
}