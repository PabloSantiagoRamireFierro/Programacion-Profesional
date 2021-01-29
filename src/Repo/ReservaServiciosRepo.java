/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repo;

import DataBase.ConexionMySQL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

/**
 *
 * @author Angelo
 */
public class ReservaServiciosRepo  {    

    //Connection to DB + Reservar query
    public void reservar_query(String servicio_nombre, int servicio_asistentes, String date, String cliente_nombre, String cliente_apellido, String cliente_rut, int cliente_telefono, int precio, String pagado){

        try {
        
        Connection conn;
        conn = ConexionMySQL.conectar();
        Statement stmt = conn.createStatement();
        String sql = String.format("INSERT INTO servicios_reserva (serv_res_servicio, serv_res_asistentes, serv_res_precio, serv_date, serv_res_nombre, serv_res_apellido, serv_res_rut, serv_res_telefono, serv_pagado) VALUES ('%s', %d, %d, '%s', '%s', '%s', '%s', %d, '%s')", servicio_nombre, servicio_asistentes, precio, date, cliente_nombre, cliente_apellido, cliente_rut, cliente_telefono, pagado);
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        conn.close();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Reserva exitosa");
    }
    public void modificar_query(int servicio_res_id, String servicio_nombre, int servicio_asistentes, String date, String cliente_nombre, String cliente_apellido, String cliente_rut, int cliente_telefono, int precio){

        try {
        
        Connection conn;
        conn = ConexionMySQL.conectar();
        Statement stmt = conn.createStatement();
        String sql = String.format("update servicios_reserva set serv_res_servicio = '%s', serv_res_asistentes = %d, serv_date = '%s', serv_res_nombre = '%s', serv_res_apellido = '%s', serv_res_rut = '%s', serv_res_telefono = %d, serv_res_precio = %d where (serv_res_id = %d);", servicio_nombre, servicio_asistentes, date, cliente_nombre, cliente_apellido, cliente_rut, cliente_telefono, precio,servicio_res_id);
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        conn.close();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Reserva modificada exitosamente");
    }
    
    //Connection to DB + Reservar query
    public void eliminar_query(int servicio_res_id){

        try {
        
        Connection conn;
        conn = ConexionMySQL.conectar();
        Statement stmt = conn.createStatement();
        String sql = String.format("delete from servicios_reserva where serv_res_id = %d;", servicio_res_id );
        stmt.executeUpdate(sql);
        stmt.close();
        conn.commit();
        conn.close();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Reserva eliminada exitosamente");
    }
    
    public void alerta(String s){
        Alert dialogoAlerta = new Alert(Alert.AlertType.INFORMATION);
        dialogoAlerta.setTitle("Error al ingresar datos");
        dialogoAlerta.setHeaderText(null);
        dialogoAlerta.setContentText(s);
        dialogoAlerta.initStyle(StageStyle.UTILITY);
        dialogoAlerta.showAndWait();
    }
    
    

  
    
    
}
