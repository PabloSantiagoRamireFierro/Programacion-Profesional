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
public class Usuario {
    private long id;
    private String nombre_usuario;
    private String contraseña;
    private String admin;
    
    /*Esto es para la fecha. Porsiaca
    private long fechaLong;
    private String fechaString;
    */

    public Usuario(long id, String nombre_usuario, String contraseña, String admin) {
        this.id = id;
        this.nombre_usuario = nombre_usuario;
        this.contraseña = contraseña;
        this.admin = admin;
    }
    //Todos los atributos menos el id
    public Usuario(String nombre_usuario, String contraseña, String admin) {
        this.nombre_usuario = nombre_usuario;
        this.contraseña = contraseña;
        this.admin = admin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nombre_usuario=" + nombre_usuario + ", contrase\u00f1a=" + contraseña + ", admin=" + admin + '}';
    }
    
    
    
}
