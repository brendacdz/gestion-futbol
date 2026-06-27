package com.grupo6.futbol.modelo;

/**
 * Clase que representa un partido del campeonato (fixture ida y vuelta).
 * Mientras no se jugó, los goles quedan en null (sin valor).
 */
public class Partido {

    private int id;
    private int clubLocalId;
    private int clubVisitanteId;
    private String nombreClubLocal;       // se completa al consultar, para mostrar en pantalla
    private String nombreClubVisitante;   // se completa al consultar, para mostrar en pantalla
    private Integer golesLocal;           // Integer (no int) para poder representar "sin jugar" como null
    private Integer golesVisitante;
    private int fechaJornada;             // número de fecha del campeonato: 1, 2, 3...
    private boolean jugado;

    // Constructor vacío
    public Partido() {
    }

    // Constructor con los datos básicos (para cuando se genera el fixture)
    public Partido(int clubLocalId, int clubVisitanteId, int fechaJornada) {
        this.clubLocalId = clubLocalId;
        this.clubVisitanteId = clubVisitanteId;
        this.fechaJornada = fechaJornada;
        this.jugado = false;
        this.golesLocal = null;
        this.golesVisitante = null;
    }

    // ===== Getters y Setters =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClubLocalId() {
        return clubLocalId;
    }

    public void setClubLocalId(int clubLocalId) {
        this.clubLocalId = clubLocalId;
    }

    public int getClubVisitanteId() {
        return clubVisitanteId;
    }

    public void setClubVisitanteId(int clubVisitanteId) {
        this.clubVisitanteId = clubVisitanteId;
    }

    public String getNombreClubLocal() {
        return nombreClubLocal;
    }

    public void setNombreClubLocal(String nombreClubLocal) {
        this.nombreClubLocal = nombreClubLocal;
    }

    public String getNombreClubVisitante() {
        return nombreClubVisitante;
    }

    public void setNombreClubVisitante(String nombreClubVisitante) {
        this.nombreClubVisitante = nombreClubVisitante;
    }

    public Integer getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(Integer golesLocal) {
        this.golesLocal = golesLocal;
    }

    public Integer getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(Integer golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public int getFechaJornada() {
        return fechaJornada;
    }

    public void setFechaJornada(int fechaJornada) {
        this.fechaJornada = fechaJornada;
    }

    public boolean isJugado() {
        return jugado;
    }

    public void setJugado(boolean jugado) {
        this.jugado = jugado;
    }
}