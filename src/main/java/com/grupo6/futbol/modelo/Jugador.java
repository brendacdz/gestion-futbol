package com.grupo6.futbol.modelo;

/**
 * Clase que representa a un jugador de un club.
 * Cada club puede tener hasta 23 jugadores.
 */
public class Jugador {

    private int id;
    private int clubId;       // id del club (usuario) al que pertenece
    private String nombre;
    private int edad;
    private double altura;    // en metros, ej: 1.78
    private double peso;      // en kilos, ej: 75.5
    private int habilidad;    // valor del 1 al 100
    private String puesto;    // Arquero, Defensor, Mediocampista o Delantero

    // Constructor vacío
    public Jugador() {
    }

    // Constructor con todos los datos
    public Jugador(int id, int clubId, String nombre, int edad, double altura,
                    double peso, int habilidad, String puesto) {
        this.id = id;
        this.clubId = clubId;
        this.nombre = nombre;
        this.edad = edad;
        this.altura = altura;
        this.peso = peso;
        this.habilidad = habilidad;
        this.puesto = puesto;
    }

    // ===== Getters y Setters =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getHabilidad() {
        return habilidad;
    }

    public void setHabilidad(int habilidad) {
        this.habilidad = habilidad;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }
}