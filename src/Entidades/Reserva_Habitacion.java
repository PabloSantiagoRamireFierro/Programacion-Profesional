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
public class Reserva_Habitacion {
    private long fechaIngresoLong;
    private String fechaIngresoString;
    private long fechaSalidaLong;
    private String fechaSalidaString;
    private int NumeroHabitacion;
    private String NombreCliente;
    private String ApellidoCliente;
    private String RUT;
    private String Telefono;
    private String Email;
    private int Precio;
    private String Pagado;
    //TODO

    public Reserva_Habitacion(long fechaIngresoLong, String fechaIngresoString, long fechaSalidaLong, String fechaSalidaString, int NumeroHabitacion, String NombreCliente, String ApellidoCliente, String RUT, String Telefono, String Email, int Precio, String Pagado) {
        this.fechaIngresoLong = fechaIngresoLong;
        this.fechaIngresoString = fechaIngresoString;
        this.fechaSalidaLong = fechaSalidaLong;
        this.fechaSalidaString = fechaSalidaString;
        this.NumeroHabitacion = NumeroHabitacion;
        this.NombreCliente = NombreCliente;
        this.ApellidoCliente = ApellidoCliente;
        this.RUT = RUT;
        this.Telefono = Telefono;
        this.Email = Email;
        this.Precio = Precio;
        this.Pagado = Pagado;
    }

    public Reserva_Habitacion(String fechaIngresoString, String fechaSalidaString, int NumeroHabitacion, String NombreCliente, String ApellidoCliente, String RUT, String Telefono, String Email, int Precio, String Pagado) {
        this.fechaIngresoString = fechaIngresoString;
        this.fechaSalidaString = fechaSalidaString;
        this.NumeroHabitacion = NumeroHabitacion;
        this.NombreCliente = NombreCliente;
        this.ApellidoCliente = ApellidoCliente;
        this.RUT = RUT;
        this.Telefono = Telefono;
        this.Email = Email;
        this.Precio = Precio;
        this.Pagado = Pagado;
    }

    public long getFechaIngresoLong() {
        return fechaIngresoLong;
    }

    public void setFechaIngresoLong(long fechaIngresoLong) {
        this.fechaIngresoLong = fechaIngresoLong;
    }

    public String getFechaIngresoString() {
        return fechaIngresoString;
    }

    public void setFechaIngresoString(String fechaIngresoString) {
        this.fechaIngresoString = fechaIngresoString;
    }

    public long getFechaSalidaLong() {
        return fechaSalidaLong;
    }

    public void setFechaSalidaLong(long fechaSalidaLong) {
        this.fechaSalidaLong = fechaSalidaLong;
    }

    public String getFechaSalidaString() {
        return fechaSalidaString;
    }

    public void setFechaSalidaString(String fechaSalidaString) {
        this.fechaSalidaString = fechaSalidaString;
    }

    public int getNumeroHabitacion() {
        return NumeroHabitacion;
    }

    public void setNumeroHabitacion(int NumeroHabitacion) {
        this.NumeroHabitacion = NumeroHabitacion;
    }

    public String getNombreCliente() {
        return NombreCliente;
    }

    public void setNombreCliente(String NombreCliente) {
        this.NombreCliente = NombreCliente;
    }

    public String getApellidoCliente() {
        return ApellidoCliente;
    }

    public void setApellidoCliente(String ApellidoCliente) {
        this.ApellidoCliente = ApellidoCliente;
    }

    public String getRUT() {
        return RUT;
    }

    public void setRUT(String RUT) {
        this.RUT = RUT;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public int getPrecio() {
        return Precio;
    }

    public void setPrecio(int Precio) {
        this.Precio = Precio;
    }

    public String getPagado() {
        return Pagado;
    }

    public void setPagado(String Pagado) {
        this.Pagado = Pagado;
    }

    

   

}