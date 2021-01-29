/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hotel;

import DataBase.ConexionMySQL;
import DataBase.JdbcHelper;
import Entidades.Reserva_Habitacion;
import Fecha.Fecha;
import Repo.ReservaHabitacionRepo;
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
import javafx.scene.control.DatePicker;
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
public class Gestion_HabitacionesController implements Initializable {
    ObservableList<Reserva_Habitacion> dataReserva = FXCollections.observableArrayList();

    @FXML
    private DatePicker dateIngreso_Editar;
    @FXML
    private DatePicker dateSalida_Editar;
    @FXML
    private TextField textNumeroHabitacion_Editar;
    @FXML
    private TextField textNombre_Editar;
    @FXML
    private TextField textRut_Editar;
    @FXML
    private TextField textTelefono_Editar;
    @FXML
    private TextField textEmail_Editar;
    @FXML
    private TableView<Reserva_Habitacion> table;
    @FXML
    private TableColumn<Reserva_Habitacion, String> col_dateIngreso;
    @FXML
    private TableColumn<Reserva_Habitacion, String> col_dateSalida;
    @FXML
    private TableColumn<Reserva_Habitacion, Integer> col_num;
    @FXML
    private TableColumn<Reserva_Habitacion, String> col_cliente;
    @FXML
    private TableColumn<Reserva_Habitacion, String> col_rut;
    @FXML
    private TableColumn<Reserva_Habitacion, String> col_telefono;
    @FXML
    private TableColumn<Reserva_Habitacion, String> col_email;
    @FXML
    private TableColumn<Reserva_Habitacion, Integer> col_precio;
    @FXML
    private TextField textNumeroHabitacion_Eliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnSalir;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnRefrescar;
    @FXML
    private TextField textNombre_Eliminar;
    @FXML
    private DatePicker dateIngreso_Eliminar;
    @FXML
    private TextField textApellido_Editar;
    @FXML
    private TableColumn<Reserva_Habitacion, String> col_apellido;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            rellenarTabla();
        } catch (SQLException ex) {
            Logger.getLogger(Gestion_HabitacionesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        configurarTabla();
    }    

    @FXML
    private void btnEditar_click(ActionEvent event) throws SQLException {
        Connection conn = ConexionMySQL.conectar();
        Reserva_Habitacion reserva;
        long f_entradaLong, f_salidaLong;
        int PrecioPieza;
        String f_entradaString, f_salidaString;
        String nHab = textNumeroHabitacion_Editar.getText();
        String nombre = textNombre_Editar.getText();
        String apellido = textApellido_Editar.getText();
        String rut = textRut_Editar.getText();
        String fono = textTelefono_Editar.getText();
        String email =textEmail_Editar.getText();
        f_entradaLong = Fecha.getFechaLong(dateIngreso_Editar.getValue());
        f_salidaLong = Fecha.getFechaLong(dateSalida_Editar.getValue());
        f_entradaString = Fecha.getFechaString(f_entradaLong );
        f_salidaString = Fecha.getFechaString(f_salidaLong);
        Pattern pat = Pattern.compile("[a-zA-z]");
        Matcher mat = pat.matcher(nombre);
        Pattern pat2 = Pattern.compile("[a-zA-z]");
        Matcher mat2 = pat2.matcher(apellido);
        boolean esEnteroNHAB = isInteger(nHab);
        boolean esEnteroTelefono = isInteger(fono);
        
        //datos vacios
        if(((dateIngreso_Editar.getValue())== null)||((dateSalida_Editar.getValue())==null)){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Ingrese Fechas");
            return;
        }
        else if("".equals(textNumeroHabitacion_Editar.getText())||"".equals(nombre) ||"".equals(apellido) ||"".equals(rut) ||"".equals(email) || "".equals(fono)){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Rellene todos los campos");
            return;
        }
        String[] len = email.split("@");
        int length = len.length;
        System.out.println(length);
        
        String añoIn = String.valueOf(f_entradaString.charAt(6)) + String.valueOf(f_entradaString.charAt(7)) + String.valueOf(f_entradaString.charAt(8)) + String.valueOf(f_entradaString.charAt(9));
        String mesIn = String.valueOf(f_entradaString.charAt(3)) + String.valueOf(f_entradaString.charAt(4));
        String diaIn = String.valueOf(f_entradaString.charAt(0)) + String.valueOf(f_entradaString.charAt(1));
            
        String añoOut = String.valueOf(f_salidaString.charAt(6)) + String.valueOf(f_salidaString.charAt(7)) + String.valueOf(f_salidaString.charAt(8)) + String.valueOf(f_salidaString.charAt(9));
        String mesOut = String.valueOf(f_salidaString.charAt(3)) + String.valueOf(f_salidaString.charAt(4));
        String diaOut = String.valueOf(f_salidaString.charAt(0)) + String.valueOf(f_salidaString.charAt(1));

        Date tiempofecha = new Date();
        Calendar now = Calendar.getInstance();
        int añoActual = (now.get(Calendar.YEAR));
        int mesActual = (now.get(Calendar.MONTH)+1);
        int diaActual = (now.get(Calendar.DAY_OF_MONTH));
        
        //numeros corretos
        if(esEnteroNHAB == false){
            System.out.println("Ingresar un numero de habitacion correcto");
            ReservaHabitacionRepo reservaRepo = new ReservaHabitacionRepo();
            reservaRepo.alerta("Ingresar un numero de habitacion correcto");
        }
        else if(esEnteroTelefono == false){
            System.out.println("Ingresar numeros correctos");
            ReservaHabitacionRepo reservaRepo = new ReservaHabitacionRepo();
            reservaRepo.alerta("Ingresar numeros correctos");
        }
         //MAIL VALIDATION
        else if(!email.contains(".")){
            System.out.println("no dot");
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("No tiene punto. Ingrese mail valido");
            textEmail_Editar.setText("");
        }
        else if(!email.contains("@")){
            System.out.println("no at");
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Sin @. Ingrese correctamente el mail");
            textEmail_Editar.setText("");
        }
        else if(length > 2){
            System.out.println("too many ats");
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Demasiados @. Ingrese correctamente el mail");
            textEmail_Editar.setText("");
        }
        //fecha
        else if(f_entradaLong > f_salidaLong){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El Ingreso es después que la salida. Ingresa fechas correctas");
        }
        else if(Integer.parseInt(añoIn) < añoActual){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El año no es correcto. Ingresa otra fecha de ingreso"); 
        }
        else if(Integer.parseInt(añoIn) == añoActual && Integer.parseInt(mesIn) < mesActual){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El mes no es correcto. Ingresa otra fecha de ingreso");
        }
        else if(Integer.parseInt(añoIn) == añoActual && Integer.parseInt(mesIn) == mesActual && Integer.parseInt(diaIn) < diaActual){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El día no es correcto. Ingresa otra fecha de ingreso"); 
        }
        else if(Integer.parseInt(añoOut) < Integer.parseInt(añoIn)){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El año no es correcto. Ingresa otra fecha de salida");   
        }
        else if(Integer.parseInt(añoOut) == Integer.parseInt(añoIn) && Integer.parseInt(mesOut) < Integer.parseInt(mesIn)){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El mes no es correcto. Ingresa otra fecha de salida");
        }
        else if(Integer.parseInt(añoOut) == Integer.parseInt(añoIn) && Integer.parseInt(mesOut) == Integer.parseInt(mesIn) && Integer.parseInt(diaOut) < Integer.parseInt(diaIn)){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("El dia no es correcto. Ingresa otra fecha de salida");   
        }
        //palabras only
        else if(mat.find() == false) {
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Ingresar un nombre correcto");
        }
        else if(mat2.find() == false) {
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Ingresar un apellido correcto");
        }
        if(validarRut(rut)==false){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("RUT Invalido. Trate nuevamente");
        }
        //si existe
        else if(dbexisteRegistro(conn, nombre) == false){
            System.out.println("No existe la reserva");
            ReservaHabitacionRepo reservaRepo = new ReservaHabitacionRepo();
            reservaRepo.alerta("No existe la reserva");
        }
        else{
            int numero = (int) Long.parseLong(textNumeroHabitacion_Editar.getText());
            long tiempo = f_salidaLong - f_entradaLong;
            tiempo = tiempo/86400000;
            
            ReservaHabitacionRepo reservaRepo = new ReservaHabitacionRepo();
            PrecioPieza = reservaRepo.precio(numero);
            PrecioPieza = (int) (PrecioPieza*tiempo);
            reserva = new Reserva_Habitacion(f_entradaLong, f_entradaString, f_salidaLong, f_salidaString, numero, nombre,apellido, rut, fono, email, PrecioPieza, "NO");
            
            ReservaHabitacionRepo reservarepo = new ReservaHabitacionRepo();
            reservarepo.modificar(reserva);
            rellenarTabla();
            vaciarCampos();
        }
        
    }

    @FXML
    private void btnEliminar_click(ActionEvent event) throws SQLException {
        Connection conn = ConexionMySQL.conectar();
        String nHab = textNumeroHabitacion_Eliminar.getText();
        String nombre = textNombre_Eliminar.getText();
        long f_entradaLong;
        f_entradaLong = Fecha.getFechaLong(dateIngreso_Eliminar.getValue());
        if(((dateIngreso_Eliminar.getValue())== null)){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Ingrese Fechas");
            return;
        }
        else if("".equals(textNumeroHabitacion_Eliminar.getText())||"".equals(nombre)){
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Rellene todos los campos");
            return;
        }
        
        boolean esEnteroNHAB = isInteger(nHab);
        Pattern pat = Pattern.compile("[a-zA-z]");
        Matcher mat = pat.matcher(nombre);
        
        if(esEnteroNHAB == false){
            System.out.println("Ingresar un numero de habitacion correcto");
            ReservaHabitacionRepo reservaRepo = new ReservaHabitacionRepo();
            reservaRepo.alerta("Ingresar un numero de habitacion correcto");
        }
        else if(mat.find() == false) {
            ReservaHabitacionRepo method = new ReservaHabitacionRepo();
            method.alerta("Ingresar un nombre correcto");
        }
        else if(dbexisteRegistro(conn, nombre) == false){
            System.out.println("No existe la reserva");
            ReservaHabitacionRepo reservaRepo = new ReservaHabitacionRepo();
            reservaRepo.alerta("No existe la reserva");
        }
        else{
            int opcion = JOptionPane.showConfirmDialog(null,"¿Desea Eliminar el registro?",
                    "Confirmacion",JOptionPane.YES_NO_OPTION, 2);
            if(opcion == JOptionPane.YES_OPTION){
                String numero = textNumeroHabitacion_Eliminar.getText();
                int numeros = Integer.parseInt(numero);
                ReservaHabitacionRepo reservarepo= new ReservaHabitacionRepo();
                reservarepo.eliminar(numeros, nombre,f_entradaLong);
                JOptionPane.showMessageDialog(null,"Se ha eliminado el registro",
                        "Aviso",JOptionPane.INFORMATION_MESSAGE);

                rellenarTabla();
                vaciarCampos();
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
    private void btnVolver_click(ActionEvent event) throws IOException {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("MenuGestión.fxml"));
            Scene scene = new Scene(root);

            stage.setResizable(false);
            stage.setTitle("Menu de Gestion");
           
            
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
        rellenarTabla();
        configurarTabla();
    }
    public void configurarTabla(){
        col_dateIngreso.setCellValueFactory(new PropertyValueFactory<>("fechaIngresoString"));
        col_dateSalida.setCellValueFactory(new PropertyValueFactory<>("fechaSalidaString"));
        col_num.setCellValueFactory(new PropertyValueFactory<>("NumeroHabitacion"));
        col_cliente.setCellValueFactory(new PropertyValueFactory<>("NombreCliente"));
        col_apellido.setCellValueFactory(new PropertyValueFactory<>("ApellidoCliente"));
        col_rut.setCellValueFactory(new PropertyValueFactory<>("RUT"));
        col_telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_precio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        
        table.setItems(dataReserva);
    }
    public void rellenarTabla() throws SQLException{
        dataReserva.clear();
        ReservaHabitacionRepo reservaRepo = new ReservaHabitacionRepo ();
        ObservableList<Reserva_Habitacion> reservas = reservaRepo.buscarTodos();
        dataReserva.setAll(reservas);
    }
    private void vaciarCampos(){
        textEmail_Editar.setText("");
        textNombre_Editar.setText("");
        textApellido_Editar.setText("");
        textNumeroHabitacion_Editar.setText("");
        textRut_Editar.setText("");
        textTelefono_Editar.setText("");
        dateIngreso_Editar.setValue(null);
        dateSalida_Editar.setValue(null);
        
        textNombre_Eliminar.setText("");
        textNumeroHabitacion_Eliminar.setText("");
        dateIngreso_Eliminar.setValue(null);
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
    public  boolean dbexisteRegistro(Connection Conn, String id_a_buscar){
         Statement oSt = null;
         ResultSet oRs = null;
         String sSQL= " ";
         boolean dbexisteRegistro= false; 

         try{
             Conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
             
             sSQL = "SELECT * FROM habitaciones_reserva WHERE nombre_cliente ='" + id_a_buscar + "'";


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
}
