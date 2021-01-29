/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repo;

import DataBase.JdbcHelper;
import Entidades.Habitación;
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
public class HabitaciónRepo {
    public boolean crear (Habitación habitacion) throws SQLException{
        String query = "INSERT INTO habitaciones (hab_num,hab_tipo,hab_baño,hab_precio) "
                + "VALUES ('"+habitacion.getNum()+"','"+habitacion.getTipo()+"','"+habitacion.getBaño()+"','"+habitacion.getPrecio()+"')";
       
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        return exito;
    
    }
    public boolean modificar(Habitación habitacion) throws SQLException{
        /*String query = "UPDATE habitaciones SET"+
               "hab_tipo = '"+habitacion.getTipo()+"',"+
               "hab_baño = '"+habitacion.getBaño()+"',"+
               "hab_precio = "+habitacion.getPrecio()+
               " WHERE hab_num  = "+habitacion.getNum();*/
        String query = String.format("UPDATE habitaciones SET hab_tipo = '%s',hab_baño = '%s', hab_precio = %d WHERE (hab_num = %d)",habitacion.getTipo(),habitacion.getBaño(), habitacion.getPrecio(),habitacion.getNum());
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        return exito;   
    }
    public boolean eliminar(long id) throws SQLException{
        String query = "DELETE FROM habitaciones WHERE hab_num = " + id;
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
    
    public ObservableList<Habitación> buscarTodos() throws SQLException{
        String query = "SELECT * FROM habitaciones";
        JdbcHelper jdbc = new JdbcHelper();
        ResultSet rs = jdbc.realizarConsulta(query);
        
        ObservableList<Habitación> habitaciones = FXCollections.observableArrayList();
        
        try{
            while(rs.next()){
                int hab_num= rs.getInt("hab_num");
                String hab_tipo = rs.getString("hab_tipo");
                String hab_baño = rs.getString("hab_baño");
                int hab_precio = rs.getInt("hab_precio");
                habitaciones.add(new Habitación(hab_num,hab_tipo, hab_baño, hab_precio));
            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null,"Error al buscar usuario: " +ex,
                    "Error",JOptionPane.ERROR_MESSAGE);
        }
        return habitaciones;
    }
    public Habitación buscar (int numBusqueda) throws SQLException{
        String query = "SELECT * FROM habitaciones WHERE hab_num = "+numBusqueda;
        JdbcHelper jdbc = new JdbcHelper();
        ResultSet rs = jdbc.realizarConsulta(query);
        
        Habitación habitacion = null;
        
        try{
            if(rs.next()){
                int numero = numBusqueda;
                String hab_tipo = rs.getString("hab_tipo");
                String hab_baño = rs.getString("hab_baño");
                int hab_precio = rs.getInt("hab_precio");
                habitacion = new Habitación(numero, hab_tipo, hab_baño, hab_precio);
            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null,"Error al buscar la habitacion: " +ex,
                    "Error",JOptionPane.ERROR_MESSAGE);
        }
        return habitacion;
    }
}
    

