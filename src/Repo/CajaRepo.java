/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repo;

import DataBase.JdbcHelper;
import Entidades.Caja;
import Fecha.Fecha;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

/**
 *
 * @author Pablo Ramirez
 */
public class CajaRepo {
    
    public ObservableList<Caja> buscarReservas() throws SQLException{
        String query = "SELECT * FROM habitaciones_reserva";
        JdbcHelper jdbc = new JdbcHelper();
        ResultSet rs = jdbc.realizarConsulta(query);
        
        ObservableList<Caja> datacaja = FXCollections.observableArrayList();
        while(rs.next()){
            try{
                    long fechaingreso= rs.getLong("fechaingreso");
                    String rut = rs.getString("rut");
                    int Nhab = rs.getInt("num_habitacion");
                        String query2 = "SELECT * FROM habitaciones_reserva WHERE rut ='"+rut+"' AND fechaingreso ="+fechaingreso;
                        JdbcHelper jdbc2 = new JdbcHelper();
                        ResultSet rs2 = jdbc2.realizarConsulta(query2);
                        while(rs2.next()){
                            int Nhab2 = rs2.getInt("num_habitacion");
                            if (Nhab == Nhab2){
                                String nombre= rs2.getString("nombre_cliente");
                                String apellido = rs2.getString("apellido_cliente");
                                String pagado = rs2.getString("pagado");
                                int valor = rs2.getInt("precio");

                                String fechaString = Fecha.getFechaString(fechaingreso);
                                datacaja.add(new Caja(nombre,apellido,"Habitación",valor,fechaString,pagado, rut));
                            }
                        }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null,"Error al buscar usuario: " +ex,
                    "Error",JOptionPane.ERROR_MESSAGE);
        }
        }
        String query3 = "SELECT * FROM servicios_reserva";
        JdbcHelper jdbc3 = new JdbcHelper();
        ResultSet rs3 = jdbc3.realizarConsulta(query3);
            while(rs3.next()){
                String nombre= rs3.getString("serv_res_nombre");
                String apellido = rs3.getString("serv_res_apellido");
                int valor = rs3.getInt("serv_res_precio");
                String fechaString2 = rs3.getString("serv_date");
                String pagado = rs3.getString("serv_pagado");
                String rut = rs3.getString("serv_res_rut");
                datacaja.add(new Caja(nombre,apellido,"Servicios",valor,fechaString2,pagado,rut));     
                }
        return datacaja;
    }
    public boolean confirmarHabitacion (Caja caja) throws SQLException{
        String query;
        query = String.format("UPDATE habitaciones_reserva SET pagado = 'SI' WHERE (nombre_cliente ='%s' AND fechaingreso = %d AND rut = '%s')",caja.getNombre(),caja.getFechaLong(),caja.getRut());
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        return exito;
    }
    public boolean confirmarServicio (Caja caja) throws SQLException{
        String query;
        query = String.format("UPDATE servicios_reserva SET serv_pagado = 'SI' WHERE (serv_res_nombre ='%s' AND serv_date = '%s'AND serv_res_rut = '%s')",caja.getNombre(),caja.getFecha(),caja.getRut());
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        return exito;
    }
     public void alerta(String s){ //Metodo de Flavio
        Alert dialogoAlerta = new Alert(Alert.AlertType.INFORMATION);
        dialogoAlerta.setTitle("Error al ingresar datos");
        dialogoAlerta.setHeaderText(null);
        dialogoAlerta.setContentText(s);
        dialogoAlerta.initStyle(StageStyle.UTILITY);
        dialogoAlerta.showAndWait();
    }
    
}
