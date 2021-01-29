/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repo;

import DataBase.JdbcHelper;
import Entidades.Usuario;
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
public class UsuarioRepo {
    //LITERALMENTE CREAR
    public boolean crear (Usuario usuario) throws SQLException{
        String query = "INSERT INTO usuario (nombre_usuario,contraseña,admin)"
                + "VALUES ('"+usuario.getNombre_usuario()+"','"+usuario.getContraseña()+"','"+usuario.getAdmin()+"')";
       
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        return exito;
    }
    //LITERALMENTE MODIFICAR
    public boolean modificar(Usuario usuario) throws SQLException{
        /*String query = "UPDATE usuario SET"+
               "nombre_usuario = '"+usuario.getNombre_usuario()+"',"+
               "contraseña = '"+usuario.getContraseña()+"',"+
               "admin = '"+usuario.getAdmin()+
               "' WHERE id = "+usuario.getId();*/
        String query = String.format("UPDATE usuario SET nombre_usuario = '%s',contraseña = '%s', admin = '%s' WHERE (id = %d)",usuario.getNombre_usuario(),usuario.getContraseña(), usuario.getAdmin(),usuario.getId());
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        return exito;   
    }
    //LITERALMENTE ELIMINAR
    public boolean eliminar(long id) throws SQLException{
        String query = "DELETE FROM usuario WHERE id = " + id;
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        return exito;  
    }
 

    //De aquí en adelante es solo buscar. Modificalos y tienes todo de lo que quieras
    public Usuario buscarUsuario (long idBusqueda) throws SQLException{
        String query = "SELECT * FROM usuario WHERE id = "+idBusqueda;
        JdbcHelper jdbc = new JdbcHelper();
        ResultSet rs = jdbc.realizarConsulta(query);
        
        Usuario usuario = null;
        
        try{
            if(rs.next()){
                long id= idBusqueda;
                String nombre_usuario = rs.getString("nombre_usuario");
                String contraseña = rs.getString("contraseña");
                String Admin = rs.getString("Admin");
                usuario = new Usuario(id, nombre_usuario, contraseña, Admin);
            }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Error al buscar usuario: " +ex,
                    "Error",JOptionPane.ERROR_MESSAGE);
        }
        return usuario;
    }
    
    public ObservableList<Usuario> buscarTodos() throws SQLException{
        String query = "SELECT * FROM usuario";
        JdbcHelper jdbc = new JdbcHelper();
        ResultSet rs = jdbc.realizarConsulta(query);
        
        ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
        
        try{
            while(rs.next()){
                long id= rs.getLong("id");
                String nombre_usuario = rs.getString("nombre_usuario");
                String contraseña = rs.getString("contraseña");
                String Admin = rs.getString("Admin");
                usuarios.add(new Usuario(id, nombre_usuario, contraseña, Admin));
            }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Error al buscar usuario: " +ex,
                    "Error",JOptionPane.ERROR_MESSAGE);
        }
        return usuarios;
    }
    
    public ObservableList<Usuario> buscarPorNombre(String nombreBusqueda) throws SQLException{
        String query = "SELECT * FROM usuario WHERE nombre_usuario LIKE '%"+nombreBusqueda+"'";
        JdbcHelper jdbc = new JdbcHelper();
        ResultSet rs = jdbc.realizarConsulta(query);
        
        ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
        
        try{
            while(rs.next()){
                long id= rs.getLong("id");
                String nombre_usuario = rs.getString("nombre_usuario");
                String contraseña = rs.getString("contraseña");
                String Admin = rs.getString("Admin");
                usuarios.add(new Usuario(id, nombre_usuario, contraseña, Admin));
            }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Error al buscar usuario por nombre: " +ex,
                    "Error",JOptionPane.ERROR_MESSAGE);
        }
        return usuarios;
    }
    
    public ObservableList<Usuario> buscarPorContraseña(String contraseñaBusqueda) throws SQLException{
        String query = "SELECT * FROM usuario WHERE contraseña LIKE '%"+contraseñaBusqueda+"'";
        JdbcHelper jdbc = new JdbcHelper();
        ResultSet rs = jdbc.realizarConsulta(query);
        
        ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
        
        try{
            while(rs.next()){
                long id= rs.getLong("id");
                String nombre_usuario = rs.getString("nombre_usuario");
                String contraseña = rs.getString("contraseña");
                String Admin = rs.getString("Admin");
                usuarios.add(new Usuario(id, nombre_usuario, contraseña, Admin));
            }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Error al buscar usuario por contraseña: " +ex,
                    "Error",JOptionPane.ERROR_MESSAGE);
        }
        return usuarios;
    }
    
    public ObservableList<Usuario> buscarPorAdmin(String adminBusqueda) throws SQLException{
        String query = "SELECT * FROM usuario WHERE admin LIKE '%"+adminBusqueda+"'";
        JdbcHelper jdbc = new JdbcHelper();
        ResultSet rs = jdbc.realizarConsulta(query);
        
        ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
        
        try{
            while(rs.next()){
                long id= rs.getLong("id");
                String nombre_usuario = rs.getString("nombre_usuario");
                String contraseña = rs.getString("contraseña");
                String Admin = rs.getString("Admin");
                usuarios.add(new Usuario(id, nombre_usuario, contraseña, Admin));
            }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Error al buscar usuario por contraseña: " +ex,
                    "Error",JOptionPane.ERROR_MESSAGE);
        }
        return usuarios;
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
