/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repo;

import DataBase.JdbcHelper;
import Entidades.Caja;
import Entidades.Reserva_Habitacion;
import static Fecha.Fecha.getFechaString;
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
public class ReservaHabitacionRepo {
    public boolean crear (Reserva_Habitacion reserva) throws SQLException{
    String query = "INSERT INTO habitaciones_reserva (fechaingreso,fechasalida,num_habitacion,nombre_cliente,apellido_cliente,rut,telefono,email, precio, pagado)"
                + "VALUES ('"+reserva.getFechaIngresoLong()+"','"
                            +reserva.getFechaSalidaLong()+"','"
                            +reserva.getNumeroHabitacion()+"','"
                            +reserva.getNombreCliente()+"','"
                            +reserva.getApellidoCliente()+"','"
                            +reserva.getRUT()+"','"
                            +reserva.getTelefono()+"','"
                            +reserva.getEmail()+"','"
                            +reserva.getPrecio()+"','"
                            +reserva.getPagado()+"')";
       
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        if (exito){
                        JOptionPane.showMessageDialog(null, "Reserva Exitosa",
            "Reservado",JOptionPane.WARNING_MESSAGE);
        }
        return exito;
    }
    public boolean modificar(Reserva_Habitacion reserva) throws SQLException{
        String query;
        query = String.format("UPDATE habitaciones_reserva SET fechaingreso = %d, fechasalida = %d, num_habitacion = %d,rut = '%s',telefono = '%s',email = '%s' , precio = %d WHERE (nombre_cliente ='%s' AND apellido_cliente = '%s')",reserva.getFechaIngresoLong(),reserva.getFechaSalidaLong(),reserva.getNumeroHabitacion(),reserva.getRUT(),reserva.getTelefono(),reserva.getEmail(),reserva.getPrecio(),reserva.getNombreCliente(),reserva.getApellidoCliente());
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        return exito;
    }
    public boolean eliminar(int numero, String nombre, long fecha){
        String query = "DELETE FROM habitaciones_reserva WHERE num_habitacion="+numero+" AND nombre_cliente ='"+nombre+"' AND fechaingreso = "+fecha;
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        return exito; 
    }
    public void alerta(String s){
        Alert dialogoAlerta = new Alert(Alert.AlertType.INFORMATION);
        dialogoAlerta.setTitle("Error al ingresar datos");
        dialogoAlerta.setHeaderText(null);
        dialogoAlerta.setContentText(s);
        dialogoAlerta.initStyle(StageStyle.UTILITY);
        dialogoAlerta.showAndWait();
    }
    public int precio (int numero) throws SQLException{
        int precio;
            String query = "SELECT hab_precio FROM habitaciones WHERE hab_num ="+numero;
            JdbcHelper jdbc = new JdbcHelper();
            ResultSet rs = jdbc.realizarConsulta(query);
            rs.next();
            precio = rs.getInt(1);
        return precio;
    }
    public ObservableList<Reserva_Habitacion> buscarTodos() throws SQLException{
        String query = "SELECT * FROM habitaciones_reserva";
        JdbcHelper jdbc = new JdbcHelper();
        ResultSet rs = jdbc.realizarConsulta(query);
        
        ObservableList<Reserva_Habitacion> reservas = FXCollections.observableArrayList();
        
        try{
            while(rs.next()){
                int hab_num= rs.getInt("num_habitacion");
                long fingreso = rs.getLong("fechaingreso");
                long fsalida = rs.getLong("fechasalida");
                String fechaingreso = getFechaString(fingreso);
                String fechasalida = getFechaString(fsalida);
                String nombre = rs.getString("nombre_cliente");
                String apellido = rs.getString("apellido_cliente");
                String rut = rs.getString("rut");
                String telefono = rs.getString("telefono");
                String email = rs.getString("email");
                int res_precio = rs.getInt("precio");
                String pagado = rs.getString("pagado");
                reservas.add(new Reserva_Habitacion(fechaingreso,fechasalida, hab_num,nombre,apellido,rut,telefono, email, res_precio, pagado));
            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null,"Error al buscar usuario: " +ex,
                    "Error",JOptionPane.ERROR_MESSAGE);
        }
        return reservas;
    }
    
}
