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
public class Reserva_Servicios {
    
    String id, serv, asistentes, date, name, ln, rut, fono, costo, pagado;

    public Reserva_Servicios(String id, String serv, String asistentes, String date, String name, String ln, String rut, String fono, String costo, String pagado) {
        this.id = id;
        this.serv = serv;
        this.asistentes = asistentes;
        this.date = date;
        this.name = name;
        this.ln = ln;
        this.rut = rut;
        this.fono = fono;
        this.costo = costo;
        this.pagado = pagado;
    }

    public Reserva_Servicios(String id, String serv, String asistentes, String date, String name, String ln, String rut, String fono, String costo) {
        this.id = id;
        this.serv = serv;
        this.asistentes = asistentes;
        this.date = date;
        this.name = name;
        this.ln = ln;
        this.rut = rut;
        this.fono = fono;
        this.costo = costo;
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServ() {
        return serv;
    }

    public void setServ(String serv) {
        this.serv = serv;
    }

    public String getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(String asistentes) {
        this.asistentes = asistentes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLn() {
        return ln;
    }

    public void setLn(String ln) {
        this.ln = ln;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getFono() {
        return fono;
    }

    public void setFono(String fono) {
        this.fono = fono;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getPagado() {
        return pagado;
    }

    public void setPagado(String pagado) {
        this.pagado = pagado;
    }


}
