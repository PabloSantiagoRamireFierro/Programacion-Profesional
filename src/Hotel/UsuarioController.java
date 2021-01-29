/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hotel;

import DataBase.ConexionMySQL;
import DataBase.JdbcHelper;
import Entidades.Usuario;
import Repo.HabitaciónRepo;
import Repo.UsuarioRepo;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Pablo Ramirez
 */
public class UsuarioController implements Initializable {
    
    public ObservableList<Usuario> dataUsuario = FXCollections.observableArrayList();

    @FXML
    private TextField txtNombreUsuario_Crear;
    @FXML
    private TextField txtContraseña_Crear;
    @FXML
    private ChoiceBox<String> ChoiceAdmin_Crear;
    @FXML
    private TextField txtId_Editar;
    @FXML
    private ChoiceBox<String> ChoiceAdmin_Editar;
    @FXML
    private TextField txtId_Eliminar;
    @FXML
    private Button btnCrear;
    @FXML
    private Font x1;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private ChoiceBox<String> ChoiceBuscar;
    @FXML
    private TextField txtBusqueda;
    @FXML
    private Button btnBuscar;
    @FXML
    private TableView<Usuario> TablaUsuario;
    @FXML
    private TableColumn<Usuario, Long> ColId;
    @FXML
    private TableColumn<Usuario, String> ColNombreUsuario;
    @FXML
    private TableColumn<Usuario, String> ColContraseña;
    @FXML
    private TableColumn<Usuario, String> ColAdmin;
    @FXML
    private Button btnRefrescar;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnSalir;
    @FXML
    private Label labelResultados;
    @FXML
    private TextField txtNombreUsuario_Editar;
    @FXML
    private TextField txtContraseña_Editar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        try {
            rellenarTablaUsuario();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        vaciarCampos();
        
        ChoiceAdmin_Crear.getItems().addAll("SI","NO");
        ChoiceAdmin_Editar.getItems().addAll("SI","NO");
        ChoiceBuscar.getItems().addAll("Id","Nombre","Contraseña","Admin");
    }
    public void configurarTabla(){
        ColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColNombreUsuario.setCellValueFactory(new PropertyValueFactory<>("nombre_usuario"));
        ColContraseña.setCellValueFactory(new PropertyValueFactory<>("contraseña"));
        ColAdmin.setCellValueFactory(new PropertyValueFactory<>("admin"));
        TablaUsuario.setItems(dataUsuario);
    }
    public void rellenarTablaUsuario() throws SQLException{
        dataUsuario.clear();
        UsuarioRepo usuarioRepo = new UsuarioRepo();
        ObservableList<Usuario> usuarios = usuarioRepo.buscarTodos();
        dataUsuario.setAll(usuarios);
        int resultados = usuarios.size();
        labelResultados.setText("Resultados: "+resultados);
    }
    private void vaciarCampos(){
        txtNombreUsuario_Crear.setText("");
        txtContraseña_Crear.setText("");
        txtId_Editar.setText("");
        txtNombreUsuario_Editar.setText("");
        txtContraseña_Editar.setText("");
        txtBusqueda.setText("");
        txtId_Eliminar.setText("");  
    }

    @FXML
    private void btnCrear_click(ActionEvent event) throws SQLException {
        String NombreUsuario, Contraseña;
        String Admin = ChoiceAdmin_Crear.getValue();
        NombreUsuario =txtNombreUsuario_Crear.getText();
        Contraseña = txtContraseña_Crear.getText();
        Pattern pat = Pattern.compile("[a-zA-z]");
        Matcher mat = pat.matcher(NombreUsuario);
        
        if(NombreUsuario.equals("") || Contraseña.equals("") ){
            UsuarioRepo method = new UsuarioRepo();
            method.alerta("Llene todos los campos");
            return; 
        }
        else if(Admin!= "SI" && Admin!= "NO" ){
            UsuarioRepo method = new UsuarioRepo();
            method.alerta("Seleccione si es Administrador o no");
            return;
        }
        else if(mat.find() == false) {
            UsuarioRepo method = new UsuarioRepo();
            method.alerta("Escriba un nombre sin caracteres");
            txtNombreUsuario_Crear.setText("");
        }
        else{
            Usuario usuario;
            usuario = new Usuario(NombreUsuario, Contraseña,Admin);

            UsuarioRepo usuarioRepo  = new UsuarioRepo();
            usuarioRepo.crear(usuario);

            rellenarTablaUsuario();
            vaciarCampos();
        }   
    }

    @FXML
    private void btnEditar_click(ActionEvent event) throws SQLException {
        
        Connection conn = ConexionMySQL.conectar();
        String ids = txtId_Editar.getText();
        String NombreUsuario, Contraseña;
        String Admin = ChoiceAdmin_Editar.getValue();
        NombreUsuario =txtNombreUsuario_Editar.getText();
        Contraseña = txtContraseña_Editar.getText();
        Pattern pat = Pattern.compile("[a-zA-z]");
        Matcher mat = pat.matcher(NombreUsuario);
        
        if(NombreUsuario.equals("") || Contraseña.equals("") ||ids.equals("")){
            UsuarioRepo method = new UsuarioRepo();
            method.alerta("Llene todos los campos");
            return;
        }
        else if(Admin!= "SI" && Admin!= "NO" ){
            UsuarioRepo method = new UsuarioRepo();
            method.alerta("Seleccione si es Administrador o no");
            return;
        }
        else if(dbexisteRegistro(conn, ids) == false){
            HabitaciónRepo method = new HabitaciónRepo();
            method.alerta("No existe el usuario");
        }
        else if(mat.find() == false) {
            UsuarioRepo method = new UsuarioRepo();
            method.alerta("Escriba un nombre sin caracteres");
            txtNombreUsuario_Editar.setText("");
        }
        else{
            int id = (int)Long.parseLong(txtId_Editar.getText());

            Usuario usuario;
            usuario = new Usuario(id,NombreUsuario, Contraseña,Admin);

            UsuarioRepo usuarioRepo = new UsuarioRepo();
            usuarioRepo.modificar(usuario);
            rellenarTablaUsuario();
            vaciarCampos();
        }
    }

    @FXML
    private void btnEliminar_click(ActionEvent event) throws SQLException {
        Connection conn = ConexionMySQL.conectar();
        String ids = txtId_Eliminar.getText();
        if(dbexisteRegistro(conn, ids) == false){
            HabitaciónRepo method = new HabitaciónRepo();
            method.alerta("No existe el usuario");
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(null,"¿Desea Eliminar el registro?",
                "Confirmacion",JOptionPane.YES_NO_OPTION, 2);
        if(opcion == JOptionPane.YES_OPTION){
        
            int id = Integer.parseInt(ids);
            UsuarioRepo usuarioRepo = new UsuarioRepo();
            ObservableList<Usuario> usuarios = usuarioRepo.buscarTodos();
            int resultados = usuarios.size();
            if(resultados ==1){
                UsuarioRepo method = new UsuarioRepo();
                method.alerta("No puede eliminar todos los usuarios");
            }
            else{
                usuarioRepo.eliminar(id);
                JOptionPane.showMessageDialog(null,"Se ha eliminado el registro",
                        "Aviso",JOptionPane.INFORMATION_MESSAGE);

                vaciarCampos();
                rellenarTablaUsuario();
            }
        }
    }

    @FXML
    private void btnBuscar_click(ActionEvent event) throws SQLException {
        Connection conn = ConexionMySQL.conectar();
        dataUsuario.clear();
        String nodoBusqueda = ChoiceBuscar.getValue();
        if(nodoBusqueda != "Id" &&
           nodoBusqueda != "Nombre" &&
           nodoBusqueda != "Contraseña" &&
           nodoBusqueda != "Admin"){
            
            UsuarioRepo method = new UsuarioRepo();
            method.alerta("Seleccione un metodo de busqueda");
            return;
        }
        
        if(nodoBusqueda.equals("Id")){
            String id = txtBusqueda.getText();
            boolean esEnteroid = isInteger(id);
            if(id.equals("") ){
                UsuarioRepo method = new UsuarioRepo();
                method.alerta("Ingrese un id valido");
                return;
            }
            else if(esEnteroid == false){
                UsuarioRepo method = new UsuarioRepo();
                method.alerta("Ingrese un id valido");
            }
            else if(dbexisteRegistro(conn, id) == false){
                HabitaciónRepo method = new HabitaciónRepo();
                method.alerta("No existe el usuario");
            }
            else{
                long ids = Long.parseLong(id);
                UsuarioRepo usuarioRepo = new UsuarioRepo();
                Usuario usuario = usuarioRepo.buscarUsuario(ids);
                if(usuario != null){
                dataUsuario.add(usuario);
                labelResultados.setText("Resultados: 1");
                }else{
                    labelResultados.setText("Resultados: 0");
                }
                return;
            }
            
        }
        
        UsuarioRepo usuarioRepo = new UsuarioRepo();
        ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
        
        switch(nodoBusqueda){
            case "Nombre":
                String nombreBusqueda = txtBusqueda.getText();
                if(nombreBusqueda.equals("") ){
                    UsuarioRepo method = new UsuarioRepo();
                    method.alerta("Escriba un nombre");
                    return;
                }
                else if(dbexisteNombre(conn, nombreBusqueda) == false){
                    HabitaciónRepo method = new HabitaciónRepo();
                    method.alerta("No existe el usuario");
                    return;
                }
                usuarios = usuarioRepo.buscarPorNombre(nombreBusqueda);
                break;
            
            case "Contraseña":
                String contraseñaBusqueda = txtBusqueda.getText();
                if(contraseñaBusqueda.equals("") ){
                    UsuarioRepo method = new UsuarioRepo();
                    method.alerta("Escriba una contraseña");
                    return;
                }
                else if(dbexisteContra(conn, contraseñaBusqueda) == false){
                    HabitaciónRepo method = new HabitaciónRepo();
                    method.alerta("No existe el usuario");
                    return;
                }
                usuarios = usuarioRepo.buscarPorContraseña(contraseñaBusqueda);
                break;
            
            case "Admin":
                String adminBusqueda = txtBusqueda.getText();
                Pattern pat = Pattern.compile("[SI|NO]");
                Matcher mat = pat.matcher(adminBusqueda);
                if(adminBusqueda.equals("") ){
                    UsuarioRepo method = new UsuarioRepo();
                    method.alerta("Escriba SI o NO");
                    return;
                }
                else if(mat.find() == false){
                    UsuarioRepo method = new UsuarioRepo();
                    method.alerta("Escriba SI o NO");
                    return;
                }
                usuarios = usuarioRepo.buscarPorAdmin(adminBusqueda);
                break;
                
            default:
                JOptionPane.showMessageDialog(null,",Modo de busqueda incorrecto","Error", JOptionPane.WARNING_MESSAGE);
                
                return;  
        }
        dataUsuario.setAll(usuarios);
        int resultados = usuarios.size();
        labelResultados.setText("Resultados: "+resultados);
    }
    
    @FXML
    private void btnRefrescar_click(ActionEvent event) throws SQLException {
        rellenarTablaUsuario();
    }
    
    @FXML
    private void btnVolver_click(ActionEvent event) throws IOException {
        Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("MenuAdmin.fxml"));
            Scene scene = new Scene(root);

            stage.setResizable(false);
            stage.setTitle("Menu de Administrador");
           
            
            //al cerrar la ventana de Libros
            stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
                @Override
                public void handle(WindowEvent e){
                    Platform.exit();
                    System.exit(0);
                }
            });

            stage.setScene(scene);
            stage.show();
            
            //ocultar la ventana de Login
            ((Node)(event.getSource())).getScene().getWindow().hide(); 
    }

    @FXML
    private void btnSalir_click(ActionEvent event) throws SQLException {
        String query = "DELETE FROM admin";
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        System.exit(0);
    }
    public static boolean isInteger(String s) {
    try { //Try to make the input into an integer
        Integer.parseInt(s);
        return true; //Return true if it works
    }
    catch( Exception e ) { 
        return false; //If it doesn't work return false
    }
    } 
    public  boolean dbexisteRegistro(Connection Conn, String id_a_buscar){
         Statement oSt = null;
         ResultSet oRs = null;
         String sSQL= " ";
         boolean dbexisteRegistro= false; 

         try{
             Conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
             
             sSQL = "SELECT * FROM usuario WHERE id ='" + id_a_buscar + "'";


             oSt = Conn.createStatement();
             oRs = oSt.executeQuery(sSQL);

             if(oRs.next()){
                if(oRs.getRow() > 0){
                    dbexisteRegistro= true;
                }
             }

             if (oSt != null) {oSt.close();oSt = null;}
             if (oRs != null) {oRs.close();oRs = null;}
         }catch(SQLException err){

             oSt = null;
             oRs = null;
             sSQL=null;
         }catch(Exception err){

             oSt = null;
             oRs = null;
             sSQL=null;  
         }finally{
             oSt = null;
             oRs = null;
             sSQL=null;
         }
         return dbexisteRegistro;
 }
    public  boolean dbexisteNombre(Connection Conn, String id_a_buscar){
         Statement oSt = null;
         ResultSet oRs = null;
         String sSQL= " ";
         boolean dbexisteRegistro= false; 

         try{
             Conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
             
             sSQL = "SELECT * FROM usuario WHERE nombre_usuario ='" + id_a_buscar + "'";


             oSt = Conn.createStatement();
             oRs = oSt.executeQuery(sSQL);

             if(oRs.next()){
                if(oRs.getRow() > 0){
                    dbexisteRegistro= true;
                }
             }

             if (oSt != null) {oSt.close();oSt = null;}
             if (oRs != null) {oRs.close();oRs = null;}
         }catch(SQLException err){

             oSt = null;
             oRs = null;
             sSQL=null;
         }catch(Exception err){

             oSt = null;
             oRs = null;
             sSQL=null;  
         }finally{
             oSt = null;
             oRs = null;
             sSQL=null;
         }
         return dbexisteRegistro;
 }
    public  boolean dbexisteContra(Connection Conn, String id_a_buscar){
         Statement oSt = null;
         ResultSet oRs = null;
         String sSQL= " ";
         boolean dbexisteRegistro= false; 

         try{
             Conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
             
             sSQL = "SELECT * FROM usuario WHERE contraseña ='" + id_a_buscar + "'";


             oSt = Conn.createStatement();
             oRs = oSt.executeQuery(sSQL);

             if(oRs.next()){
                if(oRs.getRow() > 0){
                    dbexisteRegistro= true;
                }
             }

             if (oSt != null) {oSt.close();oSt = null;}
             if (oRs != null) {oRs.close();oRs = null;}
         }catch(SQLException err){

             oSt = null;
             oRs = null;
             sSQL=null;
         }catch(Exception err){

             oSt = null;
             oRs = null;
             sSQL=null;  
         }finally{
             oSt = null;
             oRs = null;
             sSQL=null;
         }
         return dbexisteRegistro;
 }
}