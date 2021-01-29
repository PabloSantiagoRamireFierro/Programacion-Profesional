/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hotel;

import DataBase.ConexionMySQL;
import DataBase.JdbcHelper;
import Entidades.Caja;
import Fecha.Fecha;
import Repo.CajaRepo;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
public class CajaController implements Initializable {

    ObservableList<Caja> datacaja = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Caja, String> col_nombre;
    @FXML
    private TableColumn<Caja, String> col_apellido;
    @FXML
    private TableColumn<Caja, String> col_tipo;
    @FXML
    private TableColumn<Caja, Integer> col_valor;
    @FXML
    private TableColumn<Caja, String> col_pago;
    @FXML
    private TableColumn<Caja, String> col_fecha;
    @FXML
    private TableColumn<Caja, String> col_rut;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnSalir;
    @FXML
    private TableView<Caja> table;
    @FXML
    private Label IngresoTotal;
    @FXML
    private Button btnConfirmar;
    @FXML
    private TextField txtRut;
    @FXML
    private TextField txtNombre;
    @FXML
    private DatePicker dateIngreso;
    @FXML
    private ChoiceBox<String> choiceReserva;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        try {   
            rellenarTablaHabitacion();
        } catch (SQLException ex) {
            Logger.getLogger(CajaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        choiceReserva.getItems().addAll("Habitaciones","Servicios");
    }
    
    public void configurarTabla(){
        col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        col_apellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        col_tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        col_valor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        col_fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        col_pago.setCellValueFactory(new PropertyValueFactory<>("pago"));
        col_rut.setCellValueFactory(new PropertyValueFactory<>("rut"));
        table.setItems(datacaja);
    }
    
    public void rellenarTablaHabitacion() throws SQLException{
        datacaja.clear();
        CajaRepo caja = new CajaRepo();
        ObservableList<Caja> cajitas = caja.buscarReservas();
        datacaja.setAll(cajitas);
    }
    

    @FXML
    private void btnVolver_click(ActionEvent event) throws SQLException, IOException {
        String query = "SELECT * FROM admin";
        JdbcHelper jdbc = new JdbcHelper();
        ResultSet rs = jdbc.realizarConsulta(query);
        String admin;
        
        while(rs.next()){
            admin =rs.getString("estado");  
            if((admin.equals("SI")) ){
            
            
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("MenuInicio.fxml"));
            Scene scene = new Scene(root);

            stage.setResizable(false);
            stage.setTitle("Menu");
           
            
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
            else if ((admin.equals("NO"))){
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("MenuInicio_1.fxml"));
                Scene scene = new Scene(root);

                stage.setResizable(false);
                stage.setTitle("Menu");


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
    }
    }

    @FXML
    private void btnSalir_click(ActionEvent event) {
        String query = "DELETE FROM admin";
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        System.exit(0);
    }

    @FXML
    private void btnConfirmar_click(ActionEvent event) throws SQLException {
        Connection conn = ConexionMySQL.conectar();
        if((txtNombre.getText()).equals("") || (txtRut.getText()).equals("") ){
            CajaRepo method = new CajaRepo();
            method.alerta("Llene todos los campos");
            return; 
        }
        if((dateIngreso.getValue())== null){
            CajaRepo method = new CajaRepo();
            method.alerta("Ingrese Fechas");
            return;
        }
        
        String Nombre, Rut, f_entradaString;
        long f_entradaLong;
        Nombre = txtNombre.getText();
        Rut = txtRut.getText();
        f_entradaLong = Fecha.getFechaLong(dateIngreso.getValue());
        f_entradaString = Fecha.getFechaString(f_entradaLong);
        String tipo = choiceReserva.getValue(); 
        Pattern pat = Pattern.compile("[a-zA-z]");
        Matcher mat = pat.matcher(Nombre);
        
        String año = String.valueOf(f_entradaString.charAt(6)) + String.valueOf(f_entradaString.charAt(7)) + String.valueOf(f_entradaString.charAt(8)) + String.valueOf(f_entradaString.charAt(9));
        String mes = String.valueOf(f_entradaString.charAt(3)) + String.valueOf(f_entradaString.charAt(4));
        String dia = String.valueOf(f_entradaString.charAt(0)) + String.valueOf(f_entradaString.charAt(1));

        Calendar now = Calendar.getInstance();
        int añoActual = (now.get(Calendar.YEAR));
        int mesActual = (now.get(Calendar.MONTH)+1);
        int diaActual = (now.get(Calendar.DAY_OF_MONTH));
        
        if(validarRut(Rut)==false){
            CajaRepo method = new CajaRepo();
            method.alerta("RUT Invalido. Trate nuevamente");
        }
        else if(tipo != "Habitaciones" && tipo != "Servicios" ){
            CajaRepo method = new CajaRepo();
            method.alerta("Ingrese un tipo de reserva");
        }
        //nombre mal escrito
        else if(mat.find() == false) {
            CajaRepo method = new CajaRepo();
            method.alerta("Ingresar un nombre correcto");
        }
        else if(Integer.parseInt(año) < añoActual){
            CajaRepo method = new CajaRepo();
            method.alerta("Ingresa una fecha correcto");   
        }
        else if(Integer.parseInt(año) == añoActual && Integer.parseInt(mes) < mesActual){
            CajaRepo method = new CajaRepo();
            method.alerta("Ingresa una fecha correcto");   
        }
        else if(Integer.parseInt(año) == añoActual && Integer.parseInt(mes) == mesActual && Integer.parseInt(dia) < diaActual){
            CajaRepo method = new CajaRepo();
            method.alerta("Ingresa una fecha correcto");
        }
      
        else if(dbexisteRegistro(conn, Rut, f_entradaLong, "SI") == true){
            CajaRepo method = new CajaRepo();
            method.alerta("Ya se confirmó el pago");
        }
            /*else if(dbexisteRegistro(conn, Rut, f_entradaLong)==false){
                CajaRepo method = new CajaRepo();
                method.alerta("No existen reservas HAB");
            }*/
       
        else if(dbexisteRegistroS(conn, Rut, f_entradaString, "SI") == true){
            CajaRepo method = new CajaRepo();
            method.alerta("Ya se confirmó el pago");
        }    
           /* else if(dbexisteRegistroS(conn, Rut, f_entradaString)==false){
                CajaRepo method = new CajaRepo();
                method.alerta("No existen reservas SEV");
            }*/
        else{
            Caja caja1;
            Caja caja2;
            caja1 = new Caja(Nombre, Rut, f_entradaLong);
            caja2 = new Caja(Nombre, f_entradaString, Rut);

            if(tipo =="Habitaciones"){
                CajaRepo cajita = new CajaRepo();
                cajita.confirmarHabitacion(caja1);
                rellenarTablaHabitacion();
            }
            else if (tipo == "Servicios"){
                CajaRepo cajota = new CajaRepo();
                cajota.confirmarServicio(caja2);
                rellenarTablaHabitacion();    
            }
        }
    }
    public static boolean validarRut(String rut) {
 
    boolean validacion = false;
    try {
    rut =  rut.toUpperCase();
    rut = rut.replace(".", "");
    rut = rut.replace("-", "");
    int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

    char dv = rut.charAt(rut.length() - 1);

    int m = 0, s = 1;
    for (; rutAux != 0; rutAux /= 10) {
        s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
    }
    if (dv == (char) (s != 0 ? s + 47 : 75)) {
        validacion = true;
    }

    } catch (java.lang.NumberFormatException e) {
    } catch (Exception e) {
    }
    return validacion;
    }
    
    public  boolean dbexisteRegistro(Connection Conn, String id_a_buscar, long fecha, String pago){
        
         System.out.println("con fecha");
         Statement oSt = null;
         ResultSet oRs = null;
         String sSQL= " ";
         boolean dbexisteRegistro= false; 

         try{
             Conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
             
             
           
                    sSQL = "SELECT * FROM habitaciones_reserva WHERE fechaingreso = '" + fecha + "' AND pagado = '"+pago+"'AND rut = '" + id_a_buscar + "'";
             
               


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
    public  boolean dbexisteRegistro(Connection Conn, String id_a_buscar, long fecha){
         System.out.println("sin fecha");
         Statement oSt = null;
         ResultSet oRs = null;
         String sSQL= " ";
         boolean dbexisteRegistro= false; 

         try{
             Conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
             
            
                    sSQL = "SELECT * FROM habitaciones_reserva WHERE rut=" + id_a_buscar+"AND fechaingreso ="+fecha;
             
                 


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
    
    public  boolean dbexisteRegistroS(Connection Conn, String id_a_buscar, String fecha, String pago){
        
         System.out.println("con fecha");
         Statement oSt = null;
         ResultSet oRs = null;
         String sSQL= " ";
         boolean dbexisteRegistro= false; 

         try{
             Conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
             
             
           
                    sSQL = "SELECT * FROM servicios_reserva WHERE serv_date = '" + fecha + "' AND serv_pagado = '"+pago+"'AND serv_res_rut = '" + id_a_buscar + "'";
             
               


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
    public  boolean dbexisteRegistroS(Connection Conn, String id_a_buscar, String fecha){
         System.out.println("sin fecha");
         Statement oSt = null;
         ResultSet oRs = null;
         String sSQL= " ";
         boolean dbexisteRegistro= false; 

         try{
             Conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
             
            
                    sSQL = "SELECT * FROM servicios_reserva WHERE serv_date = '" + fecha + "' AND serv_res_rut = '" + id_a_buscar + "'";
             
                 


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
