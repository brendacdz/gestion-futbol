package com.grupo6.futbol.modelo;

public class Partido {

    private int id;
    private int clubLocalId;
    private int clubVisitanteId;
    private String nombreClubLocal;
    private String nombreClubVisitante;
    private Integer golesLocal;
    private Integer golesVisitante;
    private int fechaJornada;
    private boolean jugado;

    public Partido() {
    }

    public Partido(int clubLocalId, int clubVisitanteId, int fechaJornada) {
        this.clubLocalId = clubLocalId;
        this.clubVisitanteId = clubVisitanteId;
        this.fechaJornada = fechaJornada;
        this.jugado = false;
        this.golesLocal = null;
        this.golesVisitante = null;
    }

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