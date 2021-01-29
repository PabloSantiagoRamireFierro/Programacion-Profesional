/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hotel;

import DataBase.ConexionMySQL;
import DataBase.JdbcHelper;
import Entidades.Servicio;
import Repo.ReservaServiciosRepo;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Angelo
 */
public class Reserva_ServiciosController implements Initializable {
    
    @FXML
    private TextField servicio;
    
    @FXML
    private TextField nombre;
    
    @FXML
    private TextField apellido;
    
    @FXML
    private TextField rut;

    @FXML
    private TextField telefono;
    
    @FXML
    private TextField asistentes;
    
    @FXML
    private TableView<Servicio> table;
    
    @FXML
    private TableColumn<Servicio,String> col_serv;

    @FXML
    private TableColumn<Servicio,String> col_cap;  
    
    @FXML
    private CheckBox deco_box;
    
    @FXML
    private CheckBox music_box;
    
    @FXML
    private CheckBox gar_box;
    
    private boolean deco_boolean;
    
    private boolean music_boolean;        
        
    private boolean gar_boolean;        
    
    @FXML
    private DatePicker fecha;
    
    @FXML
    private Label total;
    
    ObservableList<Servicio> oblist = FXCollections.observableArrayList();
    
    ObservableList<Servicio> oblist_check = FXCollections.observableArrayList();
    @FXML
    private Button btnSalir;
    @FXML
    private Button btnVolver;
    
    @FXML
    private void deco_method(ActionEvent e){
        deco_boolean = deco_box.isSelected();
    }
    
    @FXML
    private void music_method(ActionEvent e){
        music_boolean = music_box.isSelected();
    }
    
    @FXML
    private void gar_method(ActionEvent e){
        gar_boolean = gar_box.isSelected();
    }
    
    
    @FXML
    private void date_check() throws SQLException {
        if(((fecha.getValue())== null)){
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("Ingrese Fechas");
            return;
        }
        String date = fecha.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String año = String.valueOf(date.charAt(6)) + String.valueOf(date.charAt(7)) + String.valueOf(date.charAt(8)) + String.valueOf(date.charAt(9));
        String mes = String.valueOf(date.charAt(3)) + String.valueOf(date.charAt(4));
        String dia = String.valueOf(date.charAt(0)) + String.valueOf(date.charAt(1));
        
        Date tiempo = new Date();
        Calendar now = Calendar.getInstance();
        int añoActual = (now.get(Calendar.YEAR));
        int mesActual = (now.get(Calendar.MONTH)+1);
        int diaActual = (now.get(Calendar.DAY_OF_MONTH));
        //fechas
        if(Integer.parseInt(año) < añoActual){
            System.out.println("Ingresa una fecha correcto. Año");
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("Ingresa una fecha correcto");   
        }
        else if(Integer.parseInt(año) == añoActual && Integer.parseInt(mes) < mesActual){
            System.out.println("Ingresa una fecha correcto. Mes");
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("Ingresa una fecha correcto");   
        }
        else if(Integer.parseInt(año) == añoActual && Integer.parseInt(mes) == mesActual && Integer.parseInt(dia) < diaActual){
            System.out.println("Ingresa una fecha correcta. Dia");
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("Ingresa una fecha correcto");
        }
        else{
            refresh_table_check(date);
        }
    }
    
    @FXML
    private void reservar() throws SQLException {
        
        Connection conn;
        conn = ConexionMySQL.conectar();
        
        if(((fecha.getValue())== null)){
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("Ingrese Fechas");
            return;
        } 
        
        String servicio_nombre = servicio.getText();
        String date = fecha.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        System.out.println(date);

        String cliente_nombre = nombre.getText();
        String cliente_apellido = apellido.getText();
        String cliente_rut = rut.getText();
        Pattern pat = Pattern.compile("[a-zA-z]");
        Matcher mat = pat.matcher(cliente_nombre);
        Pattern pat1 = Pattern.compile("[a-zA-z]");
        Matcher mat1 = pat1.matcher(servicio_nombre);
        Pattern pat2 = Pattern.compile("[a-zA-z]");
        Matcher mat2 = pat2.matcher(cliente_apellido);

        if("".equals(cliente_nombre)||"".equals(cliente_apellido) ||"".equals(cliente_rut) ||"".equals(telefono.getText()) || "".equals(asistentes.getText())){
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("Rellene todos los campos");
            return;
        }
        
        String año = String.valueOf(date.charAt(6)) + String.valueOf(date.charAt(7)) + String.valueOf(date.charAt(8)) + String.valueOf(date.charAt(9));
        String mes = String.valueOf(date.charAt(3)) + String.valueOf(date.charAt(4));
        String dia = String.valueOf(date.charAt(0)) + String.valueOf(date.charAt(1));
        
        Date tiempo = new Date();
        Calendar now = Calendar.getInstance();
        int añoActual = (now.get(Calendar.YEAR));
        int mesActual = (now.get(Calendar.MONTH)+1);
        int diaActual = (now.get(Calendar.DAY_OF_MONTH));
        //rut
        if(validarRut(cliente_rut)==false){
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("RUT Invalido. Trate nuevamente");
        }
        //nombre mal escrito
        else if(mat.find() == false) {
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("Ingresar un nombre correcto");
        }
        else if(mat1.find() == false) {
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("Ingresar un servicio correcto");
        }
        else if(mat2.find() == false) {
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("Ingresar un apellido correcto");
        }
        //numeros
        else if(isInteger(asistentes.getText()) == false){
            System.out.println("Ingresa un numero de asistentes correcto");
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("Ingresa un numero de asistentes correcto");
        }
        else if(isInteger(telefono.getText()) == false){
            System.out.println("Ingresa un numero de telefono correcto");
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("Ingresa un numero de telefono correcto");
        }
        
        //fechas
        else if(Integer.parseInt(año) < añoActual){
            System.out.println("Ingresa una fecha correcto");
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("Ingresa una fecha correcto");   
        }
        else if(Integer.parseInt(año) == añoActual && Integer.parseInt(mes) < mesActual){
            System.out.println("Ingresa una fecha correcto");
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("Ingresa una fecha correcto");   
        }
        else if(Integer.parseInt(año) == añoActual && Integer.parseInt(mes) == mesActual && Integer.parseInt(dia) < diaActual){
            System.out.println("Ingresa una fecha correcto");
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("Ingresa una fecha correcto");
        }

        //existe
        else if(dbexisteRegistro(conn, servicio_nombre) == false){
            System.out.println("No existe el servicio de nombre " + servicio_nombre);
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("No existe el servicio de nombre " + servicio_nombre);
            System.out.println("--------");
        }
        else if(dbexisteRegistro(conn, servicio_nombre, date) == true){
            System.out.println("Ya esta reservado el servicio " + servicio_nombre);
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("Ya esta reservado el servicio " + servicio_nombre);
        }
        else{
            int precio = precio();
            int cliente_telefono = Integer.parseInt(telefono.getText());
            int servicio_asistentes = Integer.parseInt(asistentes.getText());
            ReservaServiciosRepo reservar = new ReservaServiciosRepo();
            reservar.reservar_query(servicio_nombre, servicio_asistentes, date, cliente_nombre, cliente_apellido, cliente_rut, cliente_telefono, precio, "NO");
            refresh_table_check(date);
            
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            refresh_table();
        } catch (SQLException ex) {
            Logger.getLogger(Reserva_ServiciosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    @FXML
    public void refresh_table() throws SQLException{

    oblist.clear();
    
    Connection conn = ConexionMySQL.conectar();
    Statement stmt = conn.createStatement();

    ResultSet rs = stmt.executeQuery(String.format("SELECT serv_nombre, serv_capacidad FROM servicios"));

    while (rs.next()){
        oblist.add(new Servicio(rs.getString("serv_nombre"),rs.getInt("serv_capacidad")));
    }

    col_serv.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    col_cap.setCellValueFactory(new PropertyValueFactory<>("capacidad"));

    table.setItems(oblist);
    
    }
    
    public void refresh_table_check(String date) throws SQLException{

    oblist_check.clear();

    Connection conn = ConexionMySQL.conectar();
    Statement stmt = conn.createStatement();

    ResultSet rs = stmt.executeQuery(String.format("SELECT serv_nombre, serv_capacidad FROM servicios, servicios_reserva where serv_res_servicio = serv_nombre AND serv_date = '%s';", date));
   
    while (rs.next()){
        oblist_check.add(new Servicio(rs.getString("serv_nombre"),rs.getInt("serv_capacidad")));
    }

    col_serv.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    col_cap.setCellValueFactory(new PropertyValueFactory<>("capacidad"));

    table.setItems(oblist_check);

    }
    
    @FXML
    public int precio() throws SQLException{ ///ASI SE CALCULA EL PRECIO CTM
        
        if("".equals(servicio.getText())){
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("Rellene todos los campos");
            return 0;
        }
        int total_calculado = 0;
        String servicio_nombre = servicio.getText();
        int precio_servicio = 0;
        int precio_deco = 0;
        int precio_music = 0;
        int precio_gar = 0;
        
        Connection conn = ConexionMySQL.conectar();
        Statement stmt = conn.createStatement();
        Statement stmt_deco = conn.createStatement();        
        Statement stmt_music = conn.createStatement();
        Statement stmt_gar = conn.createStatement();
        
        if(dbexisteRegistro(conn, servicio_nombre) == false){
            System.out.println("No existe el servicio de nombre " + servicio_nombre);
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("No existe el servicio de nombre " + servicio_nombre);
            System.out.println("--------");
        }
        else {
            ResultSet rs_servicio = stmt.executeQuery(String.format("SELECT serv_precio from servicios where serv_nombre = '%s'", servicio_nombre));
            rs_servicio.next();
            precio_servicio = rs_servicio.getInt(1); 

            System.out.print("Precio del servicio: ");
            System.out.println(precio_servicio);

        if(deco_boolean == true){

            ResultSet rs_deco = stmt_deco.executeQuery(String.format("SELECT serv_ad_valor from servicios_adicionales where serv_ad_nombre = 'Decoración'"));
            rs_deco.next();
            precio_deco = rs_deco.getInt(1);
            System.out.print("Precio de la decoración: ");
            System.out.println(precio_deco);
        }
                    
        if(music_boolean == true){

            ResultSet rs_music = stmt_music.executeQuery(String.format("SELECT serv_ad_valor from servicios_adicionales where serv_ad_nombre = 'Música'"));
            rs_music.next();
            precio_music = rs_music.getInt(1);
            System.out.print("Precio de la música: ");
            System.out.println(precio_music);
        }

        if(gar_boolean == true){

            ResultSet rs_gar = stmt_gar.executeQuery(String.format("SELECT serv_ad_valor from servicios_adicionales where serv_ad_nombre = 'Garzones'"));
            rs_gar.next();
            precio_gar = rs_gar.getInt(1); 
            System.out.print("Precio de los garzones: ");
            System.out.println(precio_gar);
        }
        
        total_calculado = precio_servicio + precio_deco + precio_music + precio_gar;
        
       /* stmt_gar.close();
        stmt_music.close();
        stmt_deco.close();
        stmt.close();
        conn.commit();
        conn.close();*/
        
        total.setText(String.valueOf(total_calculado));
        
        return total_calculado;
        }
        return 0;
    }
    
    private void vaciarCampos(){
        servicio.setText("");
        nombre.setText("");
        rut.setText("");
        telefono.setText("");
        asistentes.setText("");
        fecha.setValue(null);
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

    public  boolean dbexisteRegistro(Connection Conn, String id_a_buscar, String fecha){
         System.out.println("con fecha");
         Statement oSt = null;
         ResultSet oRs = null;
         String sSQL= " ";
         boolean dbexisteRegistro= false; 

         try{
             Conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
             
             
           
                    sSQL = "SELECT * FROM servicios_reserva WHERE serv_date = '" + fecha + "' AND serv_res_servicio = '" + id_a_buscar + "'";
             
               


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

    public  boolean dbexisteRegistro(Connection Conn, String id_a_buscar){
         System.out.println("sin fecha");
         Statement oSt = null;
         ResultSet oRs = null;
         String sSQL= " ";
         boolean dbexisteRegistro= false; 

         try{
             Conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
             
            
                    sSQL = "SELECT * FROM servicios WHERE serv_nombre ='" + id_a_buscar + "'";
             
                 


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

    @FXML
    private void btnSalir_click(ActionEvent event) throws SQLException {
        String query = "DELETE FROM admin";
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        System.exit(0);
    }

    @FXML
    private void btnVolver_click(ActionEvent event) throws IOException {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("MenuReserva.fxml"));
            Scene scene = new Scene(root);

            stage.setResizable(false);
            stage.setTitle("Menu de reserva");
           
            
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
