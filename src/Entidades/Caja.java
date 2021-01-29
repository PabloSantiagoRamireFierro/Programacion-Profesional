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
public class Caja {
    String nombre, apellido, tipo;
    int valor;
    String fecha, pago, rut;
    long fechaLong;

    public Caja(String nombre, String apellido, String tipo, int valor, String fecha, String pago, String rut) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipo = tipo;
        this.valor = valor;
        this.fecha = fecha;
        this.pago = pago;
        this.rut = rut;
    }

    public Caja(String nombre, String rut, long fechaLong) {
        this.nombre = nombre;
        this.rut = rut;
        this.fechaLong = fechaLong;
    }

    public Caja(String nombre, String fecha, String rut) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.rut = rut;
    }

   

    public long getFechaLong() {
        return fechaLong;
    }

    public void setFechaLong(long fechaLong) {
        this.fechaLong = fechaLong;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    

}
