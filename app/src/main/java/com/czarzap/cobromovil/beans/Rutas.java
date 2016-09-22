package com.czarzap.cobromovil.beans;

/**
 * Created by Alfredo on 22/09/2016.
 */

public class Rutas {

    private String id;
    private String nombre;

    public Rutas(){}

    public Rutas(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
}
