/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hotel;

import DataBase.ConexionMySQL;
import DataBase.JdbcHelper;
import Entidades.Reserva_Servicios;
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
public class Gestion_ServiciosController implements Initializable {
    
    @FXML
    private TextField servicio, nombre, apellido, rut, telefono, asistentes, id;
    
    @FXML
    private DatePicker fecha;
    
    @FXML
    private CheckBox gar_box, deco_box, music_box;
    
    private boolean gar_boolean;
            
    private boolean deco_boolean;
    
    private boolean music_boolean;
                
    @FXML
    private TableView<Reserva_Servicios> table;    
    
    @FXML
    private TableColumn<Reserva_Servicios,String> col_serv;   
    
    @FXML
    private TableColumn<Reserva_Servicios,String> col_name;
            
    @FXML
    private TableColumn<Reserva_Servicios,String> col_ln;
            
    @FXML
    private TableColumn<Reserva_Servicios,String> col_date;
            
    @FXML
    private TableColumn<Reserva_Servicios,String> col_id;
        
    @FXML
    private TableColumn<Reserva_Servicios,String> col_asistentes;
    
    @FXML
    private TableColumn<Reserva_Servicios,String> col_rut;

    @FXML
    private TableColumn<Reserva_Servicios,String> col_fono;

    @FXML
    private TableColumn<Reserva_Servicios,String> col_costo;
    
    private Label total;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnSalir;
    
    @FXML
    private void deco_method(){
        deco_boolean = deco_box.isSelected();
    }
    
    @FXML
    private void music_method(){
        music_boolean = music_box.isSelected();
    }
    
    @FXML
    private void gar_method(){
        gar_boolean = gar_box.isSelected();
    }
    
    ObservableList<Reserva_Servicios> oblist = FXCollections.observableArrayList();    
    
    @FXML
    public void refresh_table() throws SQLException{

    oblist.clear();
    
    Connection conn;
        conn = ConexionMySQL.conectar();
    Statement stmt = conn.createStatement();

    ResultSet rs = stmt.executeQuery(String.format("select serv_res_id, serv_res_servicio, serv_res_asistentes, serv_date, serv_res_nombre, serv_res_apellido, serv_res_rut, serv_res_telefono, serv_res_precio from servicios_reserva order by serv_res_id;"));

    while (rs.next()){
        oblist.add(new Reserva_Servicios(rs.getString("serv_res_id"),rs.getString("serv_res_servicio"),rs.getString("serv_res_asistentes"),rs.getString("serv_date"),rs.getString("serv_res_nombre"),rs.getString("serv_res_apellido"),rs.getString("serv_res_rut"),rs.getString("serv_res_telefono"),rs.getString("serv_res_precio")));
    }
   
    col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
    col_serv.setCellValueFactory(new PropertyValueFactory<>("serv"));
    col_asistentes.setCellValueFactory(new PropertyValueFactory<>("asistentes"));
    col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
    col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
    col_ln.setCellValueFactory(new PropertyValueFactory<>("ln"));
    col_rut.setCellValueFactory(new PropertyValueFactory<>("rut"));
    col_fono.setCellValueFactory(new PropertyValueFactory<>("fono"));
    col_costo.setCellValueFactory(new PropertyValueFactory<>("costo"));
    
    table.setItems(oblist);
    
    }
    
    @FXML
    private void modificar() throws SQLException {
        
        Connection conn;
        conn = ConexionMySQL.conectar();
        String cliente_nombre = nombre.getText();
        String cliente_apellido = apellido.getText();
        String cliente_rut = rut.getText();
        String servicio_nombre = servicio.getText();
        
        String date = fecha.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String año = String.valueOf(date.charAt(6)) + String.valueOf(date.charAt(7)) + String.valueOf(date.charAt(8)) + String.valueOf(date.charAt(9));
        String mes = String.valueOf(date.charAt(3)) + String.valueOf(date.charAt(4));
        String dia = String.valueOf(date.charAt(0)) + String.valueOf(date.charAt(1));
        
        Pattern pat = Pattern.compile("[a-zA-z]");
        Matcher mat = pat.matcher(cliente_nombre);
        Pattern pat1 = Pattern.compile("[a-zA-z]");
        Matcher mat1 = pat1.matcher(servicio_nombre);
        Pattern pat2 = Pattern.compile("[a-zA-z]");
        Matcher mat2 = pat2.matcher(cliente_apellido);
        Date tiempo = new Date();
        Calendar now = Calendar.getInstance();
        int añoActual = (now.get(Calendar.YEAR));
        int mesActual = (now.get(Calendar.MONTH)+1);
        int diaActual = (now.get(Calendar.DAY_OF_MONTH));
        //id correcto
        if(isInteger(id.getText()) == false){
            System.out.println("Ingresa un id correcto");
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("Ingresa un id correcto");
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
        //rut
        else if(validarRut(cliente_rut)==false){
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
        else if(dbexisteRegistro(conn, id.getText()) == false){
            System.out.println("la id no se encuentra");
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("la id no se encuentra");
        }
        else{

            int servicio_res_id = Integer.parseInt(id.getText());
            int servicio_asistentes = Integer.parseInt(asistentes.getText());
            int cliente_telefono = Integer.parseInt(telefono.getText());

            int precio = precio();

            ReservaServiciosRepo reservar = new ReservaServiciosRepo();
            reservar.modificar_query(servicio_res_id, servicio_nombre, servicio_asistentes, date, cliente_nombre, cliente_apellido, cliente_rut, cliente_telefono, precio);
            refresh_table();
        }
        
    }
    
    @FXML
    private void eliminar() throws SQLException {
        
        Connection conn;
        conn = ConexionMySQL.conectar();
        
        if(isInteger(id.getText()) == false){
            System.out.println("Ingresa un id correcto");
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("Ingresa un id correcto");
        }
        
        else if(dbexisteRegistro(conn, id.getText()) == false){
            System.out.println("la id no se encuentra");
            ReservaServiciosRepo method = new ReservaServiciosRepo();
            method.alerta("la id no se encuentra");
        }
        
        else{
        
        int servicio_res_id = Integer.parseInt(id.getText());

        ReservaServiciosRepo reservar = new ReservaServiciosRepo();
        reservar.eliminar_query(servicio_res_id);
        refresh_table();
        }
    }
    
    @FXML
    public int precio() throws SQLException{
        
        int total_calculado = 0;
        String servicio_nombre = servicio.getText();
        int precio_servicio = 0;
        int precio_deco = 0;
        int precio_music = 0;
        int precio_gar = 0;
        
        Connection conn;
        conn = ConexionMySQL.conectar();
        Statement stmt = conn.createStatement();
        Statement stmt_deco = conn.createStatement();        
        Statement stmt_music = conn.createStatement();
        Statement stmt_gar = conn.createStatement();
        
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
        
        stmt_gar.close();
        stmt_music.close();
        stmt_deco.close();
        stmt.close();
        conn.commit();
        conn.close();
        
        total.setText(String.valueOf(total_calculado));
        
        return total_calculado;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            refresh_table();
        } catch (SQLException ex) {
            Logger.getLogger(Gestion_ServiciosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Connected to the PostgreSQL server successfully.");
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
             
             
             sSQL = "SELECT * FROM servicios_reserva WHERE serv_res_id ='" + id_a_buscar + "'";
             
             
             
                 


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
    private void btnSalir_click(ActionEvent event) {
        String query = "DELETE FROM admin";
        JdbcHelper jdbc = new JdbcHelper();
        boolean exito = jdbc.ejecutarQuery(query);
        System.exit(0);
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

