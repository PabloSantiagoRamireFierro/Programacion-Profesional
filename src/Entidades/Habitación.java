/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

/**
 *
 * @author Angelo
 */
public class Habitación {
    
    String tipo,baño;
    int num, precio;

    public Habitación(int num, String tipo, String baño, int precio) {
        this.num = num;
        this.tipo = tipo;
        this.baño = baño;
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getBaño() {
        return baño;
    }

    public void setBaño(String baño) {
        this.baño = baño;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

}
    