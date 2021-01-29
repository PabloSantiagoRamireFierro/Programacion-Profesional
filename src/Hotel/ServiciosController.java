/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hotel;

import DataBase.ConexionMySQL;
import DataBase.JdbcHelper;
import Entidades.Servicio;
import Repo.ServicioRepo;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Pablo Ramirez
 */
public class ServiciosController implements Initializable {
    
    ObservableList<Servicio> dataServicio = FXCollections.observableArrayList();
    @FXML
    private TextField txtNombre_Crear;
    @FXML
    private TextField txtCapacidad_Crear;
    @FXML
    private Button btnCrear;
    @FXML
    private Button btnSalir;
    @FXML
    private TableView<Servicio> table;
    @FXML
    private TableColumn<Servicio, Integer> col_id;
    @FXML
    private TableColumn<Servicio, String> col_name;
    @FXML
    private TableColumn<Servicio, Integer> col_cap;
    @FXML
    private TableColumn<Servicio, Integer> col_precio;
    @FXML
    private TextField txtId_Editar;
    @FXML
    private TextField txtNombre_Editar;
    @FXML
    private TextField txtCapacidad_Editar;
    @FXML
    private Button btnEditar;
    @FXML
    private TextField txtId_Eliminar;
    @FXML
    private Button btnBuscar;
    @FXML
    private TextField txtNombre_Buscar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnRefrescar;
    @FXML
    private Button btnVolver;

    @FXML
    private TextField txtPrecio_Editar;
    @FXML
    private TextField txtPrecio_Crear;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        try {
            rellenarTablaServicio();
        } catch (SQLException ex) {
            Logger.getLogger(ServiciosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void btnCrear_click(ActionEvent event) throws SQLException {
        
        Connection conn = ConexionMySQL.conectar();
        String nombre = txtNombre_Crear.getText();
        String css = txtCapacidad_Crear.getText();
        String precio1 =txtPrecio_Crear.getText();
        boolean esEnteroCSS = isInteger(css);
        Pattern pat = Pattern.compile("[a-zA-z]");
        Matcher mat = pat.matcher(nombre);
        
        if(nombre.equals("") || css.equals("") || precio1.equals("") ){
            ServicioRepo servicioRepo = new ServicioRepo();
            servicioRepo.alerta("Llene todos los campos ");
            return;
            
        }
        int capacidad =(int) Long.parseLong(txtCapacidad_Crear.getText());
        int precio = (int)Long.parseLong(txtPrecio_Crear.getText());
        //numeros o caracteres en el nombre
        if(mat.find() == false) {
            ServicioRepo servicioRepo = new ServicioRepo();
            servicioRepo.alerta("Ingresar un nombre correcto");
        }
        //negativos
        else if(capacidad <=0){
            System.out.println("Ingrese una capacidad válida");
            ServicioRepo method = new ServicioRepo();
            method.alerta("Ingrese una capacidad válida" );
        }
        else if(precio <=0){
            System.out.println("Ingrese un precio válido");
            ServicioRepo method = new ServicioRepo();
            method.alerta("Ingrese un precio válido");
        }
        //letras en vez de numeros    
        else if(esEnteroCSS == false){
            System.out.println("Ingresar una capacidad correcta");
            ServicioRepo servicioRepo = new ServicioRepo();
            servicioRepo.alerta("Ingresar una capacidad correcta");
        }
        //si existe
        else if(dbexisteRegistro(conn, nombre) == true){
            System.out.println("Ya existe el servicio de nombre " + nombre);
            ServicioRepo method = new ServicioRepo();
            txtNombre_Crear.setText("");
            method.alerta("Ya existe el servicio de nombre " + nombre);
        }
        else{
            
            Servicio servicio;
            servicio = new Servicio(nombre, capacidad, precio);

            ServicioRepo servicioRepo = new ServicioRepo();
            servicioRepo.crear(servicio);

            rellenarTablaServicio();
            vaciarCampos();
        }
    }

    @FXML
    private void btnSalir_click(ActionEvent event) throws SQLException {
        String query = "DELETE FROM admin";
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        System.exit(0);
    }


    @FXML
    private void btnEditar_click(ActionEvent event) throws SQLException {
       
        Connection conn = ConexionMySQL.conectar();
        String nombre = txtNombre_Editar.getText();
        String ids = txtId_Editar.getText();
        boolean esEnteroID = isInteger(ids);
        String css = txtCapacidad_Editar.getText();
        boolean esEnteroCSS = isInteger(css);
        String prs = txtPrecio_Editar.getText();
        boolean esEnteroPRS = isInteger(prs);
        
        Pattern pat = Pattern.compile("[a-zA-z]");
        Matcher mat = pat.matcher(nombre);
        
        //vacios
        if(nombre.equals("") || css.equals("") || prs.equals("") ){
            ServicioRepo servicioRepo = new ServicioRepo();
            servicioRepo.alerta("Llene todos los campos ");
            return;   
        }
        int capacidad =(int) Long.parseLong(txtCapacidad_Editar.getText());
        int precio = (int) Long.parseLong(txtPrecio_Editar.getText());
        //solo letras 
        if(mat.find() == false) {
            ServicioRepo servicioRepo = new ServicioRepo();
            servicioRepo.alerta("Ingresar un nombre correcto");
        }
        //negativos y weas
        else if(capacidad <=0){
            System.out.println("Ingrese una capacidad válida");
            ServicioRepo method = new ServicioRepo();
            method.alerta("Ingrese una capacidad válida" );
        }
        else if(precio <=0){
            System.out.println("Ingrese un precio válido");
            ServicioRepo method = new ServicioRepo();
            method.alerta("Ingrese un precio válido");
        }
        //numeros nomas
        else if(esEnteroID == false){
             System.out.println("Ingresar un id correcto");
             ServicioRepo servicioRepo = new ServicioRepo();
             servicioRepo.alerta("Ingresar un id correcto");
        }
        else if(esEnteroCSS == false){
            System.out.println("Ingresar una capacidad correcta");
            ServicioRepo servicioRepo = new ServicioRepo();
            servicioRepo.alerta("Ingresar una capacidad correcta");
        }
        else if(esEnteroPRS == false){
            System.out.println("Ingresar un precio correcto");
            ServicioRepo servicioRepo = new ServicioRepo();
            servicioRepo.alerta("Ingresar un precio correcto");
        }
        //si existe
        else if(dbexisteRegistro(conn, ids) == false){
            System.out.println("No existe el servicio");
            ServicioRepo method = new ServicioRepo();
            method.alerta("No existe el servicio");
        }  
        else{
            int id = (int)Long.parseLong(txtId_Editar.getText());
            Servicio servicio;
            servicio = new Servicio(id, nombre, capacidad,precio);

            ServicioRepo servicioRepo = new ServicioRepo();
            servicioRepo.modificar(servicio);

            rellenarTablaServicio();
            vaciarCampos();
        }
    }
    

    @FXML
    private void btnBuscar_click(ActionEvent event) throws SQLException {
        dataServicio.clear();
        String nombre = txtNombre_Buscar.getText();
        Pattern pat = Pattern.compile("[a-zA-z]");
        Matcher mat = pat.matcher(nombre);
        
        ServicioRepo servicioRepo = new ServicioRepo();
        ObservableList<Servicio> servicios = FXCollections.observableArrayList();
        servicios = servicioRepo.buscar(nombre);
        if(mat.find() == false) {
            ServicioRepo method = new ServicioRepo();
            method.alerta("Escriba un nombre sin caracteres");
            txtNombre_Buscar.setText("");
        }
        else if(servicios ==null){
            servicioRepo.alerta("No existen servicios");
        }
        
        dataServicio.setAll(servicios);
    }

    @FXML
    private void btnEliminar_click(ActionEvent event) throws SQLException {
        
        Connection conn = ConexionMySQL.conectar();
        String ids = txtId_Eliminar.getText();
        boolean esEnteroID = isInteger(ids);
        
        if(esEnteroID == false){
             System.out.println("Ingresar un id correcto");
             ServicioRepo servicioRepo = new ServicioRepo();
             servicioRepo.alerta("Ingresar un id correcto");
        }
        else if(dbexisteRegistro(conn, ids) == false){
             System.out.println("No existe el servicio");
            ServicioRepo  method = new ServicioRepo ();
            method.alerta("No existe el servicio");
        }
        else{
        int opcion = JOptionPane.showConfirmDialog(null,"¿Desea Eliminar el registro?",
                "Confirmacion",JOptionPane.YES_NO_OPTION, 2);
        if(opcion == JOptionPane.YES_OPTION){
            int id = Integer.parseInt(ids);
            ServicioRepo servicioRepo = new ServicioRepo();
            servicioRepo.eliminar(id);
            JOptionPane.showMessageDialog(null,"Se ha eliminado el registro",
                    "Aviso",JOptionPane.INFORMATION_MESSAGE);
            
            rellenarTablaServicio();
            vaciarCampos();
        }
    }
    }

    @FXML
    private void btnRefrescar_click(ActionEvent event) throws SQLException {
        rellenarTablaServicio();
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
    public void configurarTabla(){
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        col_cap.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
        col_precio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        
        table.setItems(dataServicio);
    }
    
    public void rellenarTablaServicio() throws SQLException{
        dataServicio.clear();
        ServicioRepo servicioRepo = new ServicioRepo();
        ObservableList<Servicio> servicios = servicioRepo.buscarTodos();
        dataServicio.setAll(servicios);
    }
    
    private void vaciarCampos(){
        txtNombre_Crear.setText("");
        txtCapacidad_Crear.setText("");
        txtPrecio_Crear.setText("");
        txtCapacidad_Editar.setText("");
        txtNombre_Editar.setText("");
        txtId_Editar.setText("");
        txtPrecio_Editar.setText("");
        txtId_Eliminar.setText("");
        txtNombre_Buscar.setText("");
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
             
             if(isInteger(id_a_buscar)){
                    sSQL = "SELECT * FROM servicios WHERE serv_id ='" + id_a_buscar + "'";
             }
             
             else{
                    sSQL = "SELECT * FROM servicios WHERE serv_nombre ='" + id_a_buscar + "'";
             }
                 


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

    

