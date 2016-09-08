package com.czarzap.cobromovil.search;

public class Contribuyente {
    public Integer id;
    public String nombre;

    public Contribuyente() {}

    public Contribuyente(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() { return id;}
    public void setId(Integer id) {this.id = id;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    @Override
    public String toString() {
        return "Contribuyente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
