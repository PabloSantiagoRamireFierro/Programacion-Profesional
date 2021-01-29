/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repo;

import DataBase.JdbcHelper;
import Entidades.Servicio;
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
public class ServicioRepo {
    public boolean crear (Servicio servicio) throws SQLException{
        String query = "INSERT INTO servicios (serv_id,serv_nombre,serv_capacidad, serv_precio) "
                + "VALUES ('"+servicio.getId()+"','"+servicio.getNombre()+"','"+servicio.getCapacidad()+"','"+servicio.getPrecio()+"')";
       
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        return exito;
    }
    public boolean modificar(Servicio servicio) throws SQLException{
        /*String query = "UPDATE servicios SET"+
               "serv_nombre = '"+servicio.getNombre()+"',"+
               "serv_capacidad = '"+servicio.getCapacidad()+"',"+
               " WHERE serv_id  = "+servicio.getId();*/
        String query = String.format("UPDATE servicios SET serv_nombre = '%s', serv_capacidad = %d, serv_precio = %d WHERE (serv_id = %d)",servicio.getNombre(),servicio.getCapacidad(), servicio.getPrecio(), servicio.getId());
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        return exito;   
    }
    public boolean eliminar(long id) throws SQLException{
        String query = "DELETE FROM servicios WHERE serv_id = " + id;
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        return exito;  
    }
    public ObservableList<Servicio> buscarTodos() throws SQLException{
        String query = "SELECT * FROM servicios";
        JdbcHelper jdbc = new JdbcHelper();
        ResultSet rs = jdbc.realizarConsulta(query);
        
        ObservableList<Servicio> servicios = FXCollections.observableArrayList();
        
        try{
            while(rs.next()){
                int id= rs.getInt("serv_id");
                String nombre = rs.getString("serv_nombre");
                int capacidad = rs.getInt("serv_capacidad");
                int precio = rs.getInt("serv_precio");
                servicios.add(new Servicio(id,nombre,capacidad,precio));
            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null,"Error al buscar usuario: " +ex,
                    "Error",JOptionPane.ERROR_MESSAGE);
        }
        return servicios;
    }
    public ObservableList<Servicio> buscar (String nomBusqueda) throws SQLException{
        String query = "SELECT * FROM servicios WHERE serv_nombre = '"+nomBusqueda+"'";
        JdbcHelper jdbc = new JdbcHelper();
        ResultSet rs = jdbc.realizarConsulta(query);
        
        ObservableList<Servicio> servicios = FXCollections.observableArrayList();
        
        try{
            while(rs.next()){
                String nombre= rs.getString("serv_nombre");
                int capacidad = rs.getInt("serv_capacidad");
                int id = rs.getInt("serv_id");
                int precio = rs.getInt("serv_precio");
                servicios.add(new Servicio(id,nombre,capacidad,precio));
            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null,"Error al buscar el servicio: " +ex,
                    "Error",JOptionPane.ERROR_MESSAGE);
        }
        return servicios;
    }
    public void alerta(String s){ //cosa del Angelo
        Alert dialogoAlerta = new Alert(Alert.AlertType.INFORMATION);
        dialogoAlerta.setTitle("Error al ingresar datos");
        dialogoAlerta.setHeaderText(null);
        dialogoAlerta.setContentText(s);
        dialogoAlerta.initStyle(StageStyle.UTILITY);
        dialogoAlerta.showAndWait();
    }
}
