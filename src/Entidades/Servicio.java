/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

/**
 *
 * @author Pablo Ramirez
 */
public class Servicio {
    int id;
    String nombre;
    int capacidad;
    int precio;
    public Servicio(int id, String nombre, int capacidad, int precio) {
        this.id = id;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.precio = precio;
    }

    public Servicio(String nombre, int capacidad, int precio) {
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.precio = precio;
    }

    public Servicio(String nombre, int capacidad) {
        this.nombre = nombre;
        this.capacidad = capacidad;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }



    
}
