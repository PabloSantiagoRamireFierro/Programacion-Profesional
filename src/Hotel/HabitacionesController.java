/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hotel;

import DataBase.ConexionMySQL;
import DataBase.JdbcHelper;
import Entidades.Habitación;
import Repo.HabitaciónRepo;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
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
public class HabitacionesController implements Initializable {

    ObservableList<Habitación> dataHabitacion = FXCollections.observableArrayList();
    @FXML
    private TableView<Habitación> table;
    @FXML
    private TableColumn<Habitación, Integer> col_num;
    @FXML
    private TableColumn<Habitación, String> col_tipo;
    @FXML
    private TableColumn<Habitación, String> col_baño;
    @FXML
    private TableColumn<Habitación, Integer> col_precio;
    @FXML
    private TextField txtPrecio_Editar;
    @FXML
    private TextField txtNumero_Crear;
    @FXML
    private CheckBox CheckBox_Baño_Crear;
    @FXML
    private Button btnCrear;
    @FXML
    private Button btnEditar;
    @FXML
    private CheckBox CheckBox_Baño_Editar;
    @FXML
    private TextField txtNumero_Editar;
    @FXML
    private TextField txtNumero_Eliminar;
    @FXML
    private TextField txtNumero_Buscar;
    @FXML
    private ChoiceBox<String> ChoiceTipo_Crear;
    @FXML
    private TextField txtPrecio_Crear;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnVolver;
    @FXML
    private ChoiceBox<String> ChoiceTipo_Editar;
    @FXML
    private boolean Baño_boolean_Editar;
    @FXML
    private Button btnSalir;
    @FXML
    private Font x1;
    @FXML
    private Button btnRefrescar;
    
    private boolean Baño_boolean_Crear;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        try {
            rellenarTablaHabitacion();
        } catch (SQLException ex) {
            Logger.getLogger(HabitacionesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ChoiceTipo_Crear.getItems().addAll("Single","Double","Matrimonial","Matrimonial + Single","Triple","Cuadruple");
        ChoiceTipo_Editar.getItems().addAll("Single","Double","Matrimonial","Matrimonial + Single","Triple","Cuadruple");

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
    private void btnRefrescar_click(ActionEvent event) throws SQLException {
        rellenarTablaHabitacion();
    }

    @FXML
    private void Baño_Eleccion_Crear(ActionEvent e){
        if(CheckBox_Baño_Crear.isSelected()){
           Baño_boolean_Crear = true;
        }
        else{
            Baño_boolean_Crear = false;
        }
    }
    
    @FXML
    private void Baño_Eleccion_Editar(ActionEvent e){
        if(CheckBox_Baño_Editar.isSelected()){
           Baño_boolean_Editar = true;
        }
        else{
            Baño_boolean_Editar = false;
        }
    }


    @FXML //LISTO!!!!
    private void btnEliminar_click(ActionEvent event) throws SQLException {
        Connection conn = ConexionMySQL.conectar();
        String nHab = txtNumero_Eliminar.getText();
        boolean esEnteroNHAB = isInteger(nHab);
        
        //restriccion para que escriba numero el aweonao culiao
        if(esEnteroNHAB == false){
            System.out.println("Ingresar un numero de habitacion correcto");
            HabitaciónRepo habitacionRepo = new HabitaciónRepo();
            habitacionRepo.alerta("Ingresar un numero de habitacion correcto");
        }
        else if(dbexisteRegistro(conn, nHab) == false){
            System.out.println("No existe la habitacion");
            HabitaciónRepo method = new HabitaciónRepo();
            method.alerta("No existe la habitacion");
        }
        else{
            int opcion = JOptionPane.showConfirmDialog(null,"¿Desea Eliminar el registro?",
                    "Confirmacion",JOptionPane.YES_NO_OPTION, 2);
            if(opcion == JOptionPane.YES_OPTION){
                String numero = txtNumero_Eliminar.getText();
                int numeros = Integer.parseInt(numero);
                HabitaciónRepo habitacionRepo = new HabitaciónRepo();
                habitacionRepo.eliminar(numeros);
                JOptionPane.showMessageDialog(null,"Se ha eliminado el registro",
                        "Aviso",JOptionPane.INFORMATION_MESSAGE);

                rellenarTablaHabitacion();
                vaciarCampos();
            }
            }
        
    }

    @FXML //Listoco
    private void btnCrear_click(ActionEvent event) throws SQLException {
        
        Connection conn = ConexionMySQL.conectar();
        String tipo = ChoiceTipo_Crear.getValue(); 
        Habitación habitacion;
        String Opcion;
        if(Baño_boolean_Crear){
            Opcion = "SI";
        }
        else{
            Opcion = "NO";
        }
        String nHab = txtNumero_Crear.getText();
        String pHab = txtPrecio_Crear.getText();
        boolean esEnteroNHAB = isInteger(nHab);
        boolean esEnteroPHAB = isInteger(pHab);
 
        if(nHab.equals("") || pHab.equals("") ){
            HabitaciónRepo method = new HabitaciónRepo();
            method.alerta("Llene todos los campos");
            return; 
        }
        int precio = (int) Long.parseLong(txtPrecio_Crear.getText());
        int numero = (int) Long.parseLong(txtNumero_Crear.getText());
        //restriccion para que escriba numero el aweonao culiao
        if(esEnteroNHAB == false){
            HabitaciónRepo habitacionRepo = new HabitaciónRepo();
            habitacionRepo.alerta("Ingresar un numero de habitacion correcto");
        }
        else if(esEnteroPHAB == false){
            HabitaciónRepo habitacionRepo = new HabitaciónRepo();
            habitacionRepo.alerta("Ingresar un precio de habitacion correcto");
        }
        
        //si existe el registro
        else if(dbexisteRegistro(conn, nHab) == true){
            HabitaciónRepo method = new HabitaciónRepo();
            method.alerta("Ya existe la habitacion N°" + nHab);
        }
        
        //Numeros negativos
        else if(numero <=0){
            HabitaciónRepo method = new HabitaciónRepo();
            method.alerta("Ingrese un numero de Habitación válido" );
        }
        else if(precio <=0){
            System.out.println("Ingrese un precio válido");
            HabitaciónRepo method = new HabitaciónRepo();
            method.alerta("Ingrese un precio válido");
        }

        //Si noo selecciona nada
        else if(tipo != "Single" && 
            tipo != "Double" &&
            tipo != "Matrimonial" &&
            tipo != "Matrimonial + Single" &&
            tipo != "Triple" &&
            tipo != "Cuadruple"){
            
            System.out.println("Ingrese un tipo de habitacion");
            HabitaciónRepo habitacionRepo = new HabitaciónRepo();
            habitacionRepo.alerta("Ingrese un tipo de habitacion");
        }
        else{
            
            
            habitacion = new Habitación(numero, tipo, Opcion, precio);
        
            HabitaciónRepo habitacionRepo = new HabitaciónRepo();
            habitacionRepo.crear(habitacion);
        
            rellenarTablaHabitacion();
            vaciarCampos();
        }
    }

    @FXML //LISTOCO
    private void btnEditar_click(ActionEvent event) throws SQLException {
        
        Connection conn = ConexionMySQL.conectar();
        String tipo = ChoiceTipo_Editar.getValue();
        Habitación habitacion;
        String Opcion;
        if(Baño_boolean_Editar){
            Opcion = "SI";
        }
        else{
            Opcion = "NO";
        }
        
        String nHab = txtNumero_Editar.getText();
        String pHab = txtPrecio_Editar.getText();
        boolean esEnteroNHAB = isInteger(nHab);
        boolean esEnteroPHAB = isInteger(pHab);
        
        if(nHab.equals("") || pHab.equals("") ){
            HabitaciónRepo method = new HabitaciónRepo();
            method.alerta("Llene todos los campos");
            return; 
        }
        int numero = (int) Long.parseLong(txtNumero_Editar.getText());
        int precio = (int) Long.parseLong(txtPrecio_Editar.getText());       
        //numeros validos
        if(esEnteroNHAB == false){
            System.out.println("Ingresar un numero de habitacion correcto");
            HabitaciónRepo habitacionRepo = new HabitaciónRepo();
            habitacionRepo.alerta("Ingresar un numero de habitacion correcto");
        }
        else if(esEnteroPHAB == false){
            System.out.println("Ingresar un precio de habitacion correcto");
            HabitaciónRepo habitacionRepo = new HabitaciónRepo();
            habitacionRepo.alerta("Ingresar un precio de habitacion correcto");
        }
        //Debe existir
        else if(dbexisteRegistro(conn, nHab) == false){
            System.out.println("No existe la habitacion");
            HabitaciónRepo method = new  HabitaciónRepo();
            method.alerta("No existe la habitacion");
        }
        //si no llena el campo
        else if(tipo != "Single" && 
            tipo != "Double" &&
            tipo != "Matrimonial" &&
            tipo != "Matrimonial + Single" &&
            tipo != "Triple" &&
            tipo != "Cuadruple"){
            
            System.out.println("Ingrese un tipo de habitacion");
            HabitaciónRepo habitacionRepo = new HabitaciónRepo();
            habitacionRepo.alerta("Ingrese un tipo de habitacion");
        }
        //begativos
        else if(numero <=0){
            HabitaciónRepo method = new HabitaciónRepo();
            method.alerta("Ingrese un numero de Habitación válido" );
        }
        else if(precio <=0){
            System.out.println("Ingrese un precio válido");
            HabitaciónRepo method = new HabitaciónRepo();
            method.alerta("Ingrese un precio válido");
        }
        else{

            habitacion = new Habitación(numero, tipo, Opcion, precio);

            HabitaciónRepo habitacionRepo = new HabitaciónRepo();
            habitacionRepo.modificar(habitacion);

            rellenarTablaHabitacion();
            vaciarCampos();
        }
    }

    @FXML
    private void btnBuscar_click(ActionEvent event) throws SQLException {
        String nHab = txtNumero_Buscar.getText();
        boolean esEnteroNHAB = isInteger(nHab);
        
         if(esEnteroNHAB == false){
            System.out.println("Ingresar un numero de habitacion correcto");
            HabitaciónRepo habitacionRepo = new HabitaciónRepo();
            habitacionRepo.alerta("Ingresar un numero de habitacion correcto");
        }else{
            dataHabitacion.clear();
            int numero;
            numero= (int) Long.parseLong(txtNumero_Buscar.getText());
            
            HabitaciónRepo habitacionRepo = new HabitaciónRepo();
            Habitación habitacion = habitacionRepo.buscar(numero);
            if(habitacion != null){
                dataHabitacion.add(habitacion);
            }
            else{
                habitacionRepo.alerta("No existen habitaciones");
            }
        }
    }
    
    public void configurarTabla(){
        col_num.setCellValueFactory(new PropertyValueFactory<>("num"));
        col_tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        col_baño.setCellValueFactory(new PropertyValueFactory<>("baño"));
        col_precio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        table.setItems(dataHabitacion);
    }
    
    public void rellenarTablaHabitacion() throws SQLException{
        dataHabitacion.clear();
        HabitaciónRepo habitacionRepo = new HabitaciónRepo();
        ObservableList<Habitación> habitaciones = habitacionRepo.buscarTodos();
        dataHabitacion.setAll(habitaciones);
    }
    
    private void vaciarCampos(){
        txtNumero_Eliminar.setText("");
        txtNumero_Crear.setText("");
        txtNumero_Editar.setText("");
        txtNumero_Buscar.setText("");
        txtPrecio_Crear.setText("");
        txtPrecio_Editar.setText("");
                
    }
    
    public static boolean isInteger(String s) {
    return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
               if(s.length() == 1) return false;
               else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
}

    @FXML
    private void btnSalir_click(ActionEvent event) throws SQLException {
        String query = "DELETE FROM admin";
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        System.exit(0);
    }
    public  boolean dbexisteRegistro(Connection Conn, String id_a_buscar){
         Statement oSt = null;
         ResultSet oRs = null;
         String sSQL= " ";
         boolean dbexisteRegistro= false; 

         try{
             Conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
             
             sSQL = "SELECT * FROM habitaciones WHERE hab_num ='" + id_a_buscar + "'";


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
